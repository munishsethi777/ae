
<!DOCTYPE html>
<html lang="en">
<head>

	
    <script type="text/javascript">
    	isSelectionGrid = true;
		function renderSelectionGrid(editSeq,sourceGridColumnCheckBox,sourceGridUpdatingCheckState,destinationGridColumnCheckBox,destinationGridUpdatingCheckState,	
		                      sourceGridId,destinationGridId,sourceGridWidth,destinationGridWidth,selectionGridSourceUrl,selectionGridDestinationUrl,theme,selectionGridColumns,selectGriddataFields){
				if(editSeq != 0){
					selectionGridSourceUrl = selectionGridSourceUrl + "&parentSeq="+editSeq;		
					selectionGridDestinationUrl = selectionGridDestinationUrl + "&parentSeq="+editSeq;	
				}
				var getAdapter = function () {
					var source =
						{
						url:"",
						id: 'seq',
						datatype: "json",
						datafields:selectGriddataFields
					};
					var dataAdapter = new $.jqx.dataAdapter(source);
					return dataAdapter;
				}
				var getAdapter1 = function () {
					var source = {
						url:"",
						id: 'seq',
						datatype: "json",
						datafields:selectGriddataFields,
					};
					var dataAdapter = new $.jqx.dataAdapter(source);
					return dataAdapter;
				}
				
				
				$("#"+sourceGridId).jqxGrid({
					width: sourceGridWidth,
					height:320,
					source: getAdapter(),
					theme: theme,
					pageable: true,
					sortable: true,
					selectionmode:'checkbox',
					editable:true,
					columns: selectionGridColumns
				});
				var updatePageState = function (gridId) {
					var datainfo = $("#"+gridId).jqxGrid('getdatainformation');
					var pagenum = datainfo.paginginformation.pagenum;
					var pagesize = datainfo.paginginformation.pagesize;
					var startrow = pagenum * pagesize;
					// select the rows on the page.
					$("#"+gridId).jqxGrid('beginupdate');
					var checkedItemsCount = 0;
					for (var i = startrow; i < startrow + pagesize; i++) {
						var boundindex = $("#"+gridId).jqxGrid('getrowboundindex', i);
						var value = $("#"+gridId).jqxGrid('getcellvalue', boundindex, 'available');
						if (value) checkedItemsCount++;
						if (value) {
							$("#"+gridId).jqxGrid('selectrow', boundindex);
						}
						else {
							$("#"+gridId).jqxGrid('unselectrow', boundindex);
						}
					}

					$("#"+gridId).jqxGrid('endupdate');
					if (checkedItemsCount == pagesize) {
						if(gridId == sourceGridId){
							sourceGridColumnCheckBox.jqxCheckBox({ checked: true });
						}else{
							destinationGridColumnCheckBox.jqxCheckBox({ checked: true });
						}
					}
					else if (checkedItemsCount == 0) {
						if(gridId == sourceGridId){
							sourceGridColumnCheckBox.jqxCheckBox({ checked: false });
						}else{
							destinationGridColumnCheckBox.jqxCheckBox({ checked: false });
						}
					}
					else {
						if(gridId == sourceGridId){
							sourceGridColumnCheckBox.jqxCheckBox({ checked: null });
						}else{
							destinationGridColumnCheckBox.jqxCheckBox({ checked: null });
						}
					}
				}
				$("#"+sourceGridId).on('sort', function (event) {
					updatePageState(sourceGridId);
				});

				$("#"+sourceGridId).on('pagechanged', function (event) {
					updatePageState(sourceGridId);
				});

				$("#"+sourceGridId).on('rowdoubleclick', function (event){ 
					moveSelectedRow(sourceGridId,destinationGridId,event);
				});
				$("#"+sourceGridId).on('cellvaluechanged', function (event) {
					 if (event.args.value) {
						 $("#"+sourceGridId).jqxGrid('selectrow', event.args.rowindex);
					 }
					 else {
						 $("#"+sourceGridId).jqxGrid('unselectrow', event.args.rowindex);
					 }
					if (sourceGridColumnCheckBox) {
						 var datainfo = $("#"+sourceGridId).jqxGrid('getdatainformation');
						 var pagesize = datainfo.paginginformation.pagesize;
						 var pagenum = datainfo.paginginformation.pagenum;
						 var selectedRows = $("#"+sourceGridId).jqxGrid('getselectedrowindexes');
						 var state = false;
						 var count = 0;
						 $.each(selectedRows, function () {
							 if (pagenum * pagesize <= this && this < pagenum * pagesize + pagesize) {
								 count++;
							 }
						 });

						 if (count != 0) state = null;
						 if (count == pagesize) state = true;
						 if (count == 0) state = false;
						 
						 sourceGridUpdatingCheckState = true;
						 $(sourceGridColumnCheckBox).jqxCheckBox({ checked: state });

						 sourceGridUpdatingCheckState = false;
					 }
				});

				$("#"+destinationGridId).jqxGrid({
					editable:true,
					width: destinationGridWidth,
					height:320,
					source: getAdapter1(),
					theme: theme,
					pageable: true,
					sortable: true,
					altrows: true,
					enabletooltips: true,
					selectionmode:'checkbox',
					editable:true,
					columns: selectionGridColumns
				});
				$("#"+destinationGridId).on('sort', function (event) {
					updatePageState(destinationGridId);
				});

				$("#"+destinationGridId).on('pagechanged', function (event) {
					updatePageState(destinationGridId);
				});
				$("#"+destinationGridId).on('rowdoubleclick', function (event){ 
					moveSelectedRow(destinationGridId,sourceGridId,event);
				});
				$("#"+destinationGridId).on('cellvaluechanged', function (event) {
					 if (event.args.value) {
						 $("#"+destinationGridId).jqxGrid('selectrow', event.args.rowindex);
					 }
					 else {
						 $("#"+destinationGridId).jqxGrid('unselectrow', event.args.rowindex);
					 }
					if (destinationGridColumnCheckBox) {
						 var datainfo = $("#"+destinationGridId).jqxGrid('getdatainformation');
						 var pagesize = datainfo.paginginformation.pagesize;
						 var pagenum = datainfo.paginginformation.pagenum;
						 var selectedRows = $("#"+destinationGridId).jqxGrid('getselectedrowindexes');
						 var state = false;
						 var count = 0;
						 $.each(selectedRows, function () {
							 if (pagenum * pagesize <= this && this < pagenum * pagesize + pagesize) {
								 count++;
							 }
						 });

						 if (count != 0) state = null;
						 if (count == pagesize) state = true;
						 if (count == 0) state = false;
						 
						 destinationGridUpdatingCheckState = true;
						 $(destinationGridColumnCheckBox).jqxCheckBox({ checked: state });

						 destinationGridUpdatingCheckState = false;
					 }
				});
		
				function moveSelectedRow(sourceGridId,destinationGridId,event){
					var args = event.args;
					var rowIndex = args.rowindex;
					var dataRow = getRowData(sourceGridId,rowIndex);
					moveItem(sourceGridId,destinationGridId,dataRow,true);
				}
				function moveItem(sourceGridId, destGridId, dataRow,isDeleteRow){
					dataRow.available = false;
					addRow(destGridId,dataRow);
					if(isDeleteRow == true){
						deleteRow(sourceGridId,dataRow);
					}
				}
				function moveSelectedItems(sourceGridId, destGridId){
					var selectedRowIndexes = $("#"+sourceGridId).jqxGrid('selectedrowindexes');
					var rowIDs = new Array();
					for (var i = 0; i < selectedRowIndexes.length; i += 1) {
						var dataRow = getRowData(sourceGridId,selectedRowIndexes[i]);
						rowIDs.push(dataRow.uid);
						moveItem(sourceGridId,destGridId,dataRow,false);
					};
					removeItems(sourceGridId,rowIDs);
					$("#"+sourceGridId).jqxGrid('clearselection');
					$(sourceGridColumnCheckBox).jqxCheckBox({ checked: false });
					$(destinationGridColumnCheckBox).jqxCheckBox({ checked: false });
				}
				function moveAllItems(sourceGridId, destGridId){
					var rows = $('#'+sourceGridId).jqxGrid('getdisplayrows');
					var rowIDs = new Array();
					for (var i = 0; i < rows.length; i += 1) {
						rowIDs.push(rows[i].uid);
						moveItem(sourceGridId,destGridId,rows[i],false);
					}
					removeItems(sourceGridId,rowIDs);
					$("#"+sourceGridId).jqxGrid('clearselection');
				}
				function addRow(gridId, dataRow){
					$("#"+gridId).jqxGrid('addrow', null, dataRow);
				}
				function deleteRow(gridId, dataRow){
					var id = $("#"+gridId).jqxGrid('getrowid', dataRow.rowIndex);
					$("#"+gridId).jqxGrid('deleterow', id);
				}
				function getRowData(gridId, rowIndex){
					var dataRow = $("#"+gridId).jqxGrid('getrowdata', rowIndex);
					dataRow.rowIndex = rowIndex;
					return dataRow;
				}
				function isItemAlreadyAdded(destGridId,seq) {
					var rows = $("#"+ destGridId).jqxGrid('getrows');
					for (var i = 0; i < rows.length; i += 1) {
						if(rows[i].seq == seq){
							return true;
						}
					}
					return false;
				};
				function removeItems(gridId, selectedRowIndexes){
					$("#"+gridId).jqxGrid('deleterow', selectedRowIndexes);
				}
		
				
				
				$(".btn").jqxButton({ width: 24});
				$('.addRowBtn').on('click', function (event){ 
					moveSelectedItems(sourceGridId, destinationGridId);
				});
				$('.deleteRowBtn').on('click', function (event){ 
					moveSelectedItems(destinationGridId, sourceGridId);
				});
				$('.addAllRowBtn').on('click', function (event){ 
					moveAllItems(sourceGridId, destinationGridId);
				});
				$('.deleteAllRowBtn').on('click', function (event){ 
					moveAllItems(destinationGridId, sourceGridId);
				});
				


		}
		

		
	</script>
</head>
	

