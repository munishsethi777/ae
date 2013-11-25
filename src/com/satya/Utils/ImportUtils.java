package com.satya.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import validator.AEValidationMessage;

import com.satya.ImportedSet;
import com.satya.RowImporterI;


public class ImportUtils {
	private static final String ERRORS = "Errors";
	private RowImporterI rowImporterI;
	public ImportUtils(){
	}
	public ImportUtils(RowImporterI rowImporter){
		rowImporterI = rowImporter;
	}
	public ImportedSet importFromXls(HttpServletRequest request,
			HttpServletResponse response){
		Map<String,String> rowDataMap = null;
		ImportedSet importSet = new ImportedSet();
		try{
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List items = upload.parseRequest(request);
				FileItem fileItem = (FileItem) items.get(0);
				if (!fileItem.getName().equals("")) {
					int mid = fileItem.getName().lastIndexOf(".");
					String ext = fileItem.getName().substring(mid + 1,
							fileItem.getName().length());
					if (!ext.contains("xls"))
						throw new Exception("Format not supported");
				} else {
					throw new Exception("Select File");
				}
				
			InputStream is = fileItem.getInputStream();
			
			Workbook workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row>rowIterator = sheet.rowIterator();
						
			//first row will be column headers in xls file.
			String[] colHeaders = getColmnHeaders(sheet);
			List<Object>savedObjects = new ArrayList<Object>();
			Map<Row, List<AEValidationMessage>>errorsMap = new HashMap<Row, List<AEValidationMessage>>();
			int index = 0;
			int failedRowCount = 1;
			while(rowIterator.hasNext()){
				if(index == 0){
					rowIterator.next();
					index ++;
					continue;				
				}
				Row row = rowIterator.next();
				rowDataMap = getColmnData(row, colHeaders);
				Object object = rowImporterI.saveData(rowDataMap);
				if(object == null){
					errorsMap.put(row, rowImporterI.getValidationMessages());
				}else{
					savedObjects.add(object);
				}
				
			}
			importSet.setObjList(savedObjects);
			if(errorsMap.size() > 0){
					exportFailedRows(request,workbook,errorsMap);
					importSet.setHasErrors(true);
					importSet.setFailedRowCount(failedRowCount);
					failedRowCount ++;
			}
			}
		}catch(Exception e){			
			String msg = e.getMessage();
		}
		return importSet;
	}
	public void exportFailedRows(HttpServletRequest request,
			Workbook workbook,Map<Row, List<AEValidationMessage>>errorMap) 
									throws InvalidFormatException, IOException{
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		Sheet sheet = workbook.getSheetAt(0);
		//Check if error columns is exists
		int errColIndex = sheet.getRow(0).getLastCellNum()-1;
		Cell errorCell = sheet.getRow(0).getCell(errColIndex);
		if(!errorCell.toString().equals(ERRORS)){
			errColIndex = errColIndex + 1;
			Cell cell = sheet.getRow(0).createCell(errColIndex);
			cell.setCellValue(ERRORS);
		}
		int lastRowCount = sheet.getLastRowNum();
		for(int rowCount=lastRowCount; rowCount>=1; rowCount--){
			Row row = sheet.getRow(rowCount);
			if(row != null){
				if(!errorMap.containsKey(row)){
					sheet.removeRow(row);
				}else{
					List<AEValidationMessage>errorMessages = (List<AEValidationMessage>)errorMap.get(row);
					StringBuffer errorColValue = new StringBuffer();;
					for(AEValidationMessage validationMessage : errorMessages){
						errorColValue.append(validationMessage.getErrorMessages());
					}
					Cell rowCell = sheet.getRow(rowCount).createCell(errColIndex);
					rowCell.setCellValue(errorColValue.toString());
				}
			}
		}
		workbook.write(byteArrayStream);
		HttpSession session = request.getSession(true);
		session.setAttribute("failedListingsXLS",byteArrayStream.toByteArray() );
	}
	
	public Map<String, String>getColmnData(Row rowData,String[] colHeaders){
		Map<String,String> colDataMap = new HashMap<String, String>();
		Iterator<Cell>cellIterator = rowData.cellIterator();
		colDataMap = new HashMap<String,String>();
		//int i = 0;
//		while(cellIterator.hasNext()){					
//			Cell cell = cellIterator.next();
//			String value = cell.toString();
//			colDataMap.put(colHeaders[i], value);
//			i++;
//		}
		
		for(int i=0;i<rowData.getLastCellNum();i++){
			Cell cell = rowData.getCell(i);	
			String value = null;
			if(cell != null){
				value = cell.toString();
			}			
			colDataMap.put(colHeaders[i], value);
		}
		return colDataMap;
	}
	
	public String[] getColmnHeaders(Sheet sheet){
		Row rowData = sheet.getRow(0);		
		Iterator<Cell>cellIterator = rowData.cellIterator();
		String cells[] = new String[rowData.getLastCellNum()];
		int i = 0;
		while(cellIterator.hasNext()){					
			Cell cell = cellIterator.next();
			String value = cell.toString();
			cells[i] = value;
			i++;
		}
		return cells;
	}
	public Object downloadFailedListingsCSV(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		//String fileType = request.getParameter("fileType");
		byte[] bytes = (byte[]) session.getAttribute("failedListingsXLS");
		response.setContentType("text/xlsx");
		response.setHeader("Content-disposition", "attachment; filename="
				+ "Failed Rows.xlsx");
//		if (fileType.equals("CSV")) {
//			response.setContentType("text/csv");
//			response.setHeader("Content-disposition", "attachment; filename="
//					+ "Failed Rows.csv");
//		}

		OutputStream os = response.getOutputStream();
		os.write(bytes);
		os.close();
		return null;
	}
	 
}
