package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.Image;
import com.satya.BusinessObjects.Project;
import com.satya.Managers.ImageMgrI;
import com.satya.Persistence.DataStoreMgr;
import com.satya.Persistence.ImageDataStoreI;
import com.satya.Utils.ImageUtils;

public class ImageMgr implements ImageMgrI {

	private static final String IMAGE_UPLOADED_SUCESSFULLY = "Image Uploaded Sucessfully.";
	private static final String IMAGE_UPLOAD_JSP = "uploadImage.jsp";

	@Override
	public void uploadImage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<String> errorMsgs = new ArrayList<String>();
		if (errorMsgs != null && errorMsgs.size() > 0) {
			request.setAttribute(IConstants.errMessages, errorMsgs);
		} else {
			Boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				try {
					List fileItems = upload.parseRequest(request);
					Iterator i = fileItems.iterator();
					while (i.hasNext()) {
						FileItem fi = (FileItem) i.next();
						if (!fi.isFormField()) {
							saveImage(fi, request);
						}
					}
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
			List<String> sccMsgs = new ArrayList<String>();
			sccMsgs.add(IMAGE_UPLOADED_SUCESSFULLY);
			request.setAttribute(IConstants.sccMessages, sccMsgs);
		}
		request.getRequestDispatcher(IMAGE_UPLOAD_JSP).forward(request,
				response);
	}

	private void saveImage(FileItem fi, HttpServletRequest request)
			throws Exception {
		DataStoreMgr dataStoreMgr = ApplicationContext.getApplicationContext()
				.getDataStoreMgr();
		ImageDataStoreI imageDataStore = dataStoreMgr.getImageDataStore();
		Image image = new Image();
		String fileName = fi.getName();
		byte[] imageBytes = fi.get();
		String imageTitle = ImageUtils.getImageFileNameWithoutExtn(fileName);
		image.setImageTitle(imageTitle);
		image.setImageBytes(imageBytes);
		image.setImagePath(fileName);
		image.setImageSize(fi.getSize());
		image.setImageSaveDate(new Date());
		// Image type will be EnumType.
		image.setImageType("QuestionImage");
		Project currentProject = ApplicationContext.getApplicationContext()
				.getAdminWorkspaceProject(request);
		image.setProject(currentProject);
		image.setAdmin(currentProject.getAdmin());
		imageDataStore.save(image);
	}

	@Override
	public JSONArray getAllImagesJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Image> images = getAllImages(request, response);
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for (Image image : images) {
			jsonArr.put(Image.toJson(image));
		}
		try {
			mainJsonObject.put("jsonArr", jsonArr);
		} catch (Exception e) {

		}
		return jsonArr;

	}

	@Override
	public List<Image> getAllImages(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Project currentProject = ApplicationContext.getApplicationContext()
				.getAdminWorkspaceProject(request);
		ImageDataStoreI imageDataStore = ApplicationContext
				.getApplicationContext().getDataStoreMgr().getImageDataStore();
		List<Image> images = imageDataStore.findByProject(currentProject
				.getSeq());
		return images;
	}

	@Override
	public JSONObject delete(long imageSeq) throws Exception {
		JSONObject json = new JSONObject();
		String status = IConstants.SUCCESS;
		String message = IConstants.msg_DeletedSuccessfully;
		try {
			ImageDataStoreI imageDataStore = ApplicationContext
					.getApplicationContext().getDataStoreMgr()
					.getImageDataStore();
			imageDataStore.delete(imageSeq);

		} catch (Exception e) {
			status = IConstants.FAILURE;
			message = IConstants.ERROR + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, imageSeq);
		return json;
	}

	@Override
	public JSONArray deleteBulk(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if (idsStr != null && !idsStr.equals("")) {
			String[] ids = idsStr.split(",");
			for (String id : ids) {
				long imageSeq = Long.parseLong(id);
				json = this.delete(imageSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}

}
