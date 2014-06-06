var theme = "fresh";
var columnCheckBox = null;
var updatingCheckState = false;
var isSelectionGrid = false;
var editingBeanRow = null;

var gId = "";
function renderGrid(gridId,createWindowId,beansName,dataUrl,deleteUrl,addUrl,validatorRules,gridColumns,gridDataFields,isShowButtons,editorHeight,editorWidth){
	gId = gridId;
	
		$("#"+createWindowId).jqxWindow({
			resizable: true, theme: theme, autoOpen: false, maxWidth:2000, 
			maxHeight: 1000, width:editorWidth,height:editorHeight, showCloseButton: true,
			resizable: true,animationType: 'fade',isModal: true,modalOpacity: 0.8});
		var getAdapter = function () {
			var source =
				{
				url:dataUrl,
				datatype: "json",
				datafields:gridDataFields,
				updaterow: function (rowid, rowdata, commit) {
					commit(true);
					//$("#jqxCreateBeanWindow").jqxWindow('close');
				},
				addrow: function (rowid, rowdata, position, commit) {
					commit(true);
					//$("#jqxCreateBeanWindow").jqxWindow('close');
				},
				deleterow: function (selectedIndexes, commit) {
					commit(true);
					$('#deleteBeanConfirmation').jqxWindow('close');	
				}
				
			};
			var dataAdapter = new $.jqx.dataAdapter(source);
			return dataAdapter;
		}
		$("#"+gridId).jqxGrid({
			width: "100%",
			height: "100%",
			editable: true,
			filterable: true,
			source: getAdapter(),
			theme: theme,
			showstatusbar: true,
			pageable: true,
			sortable: true,
			altrows: true,
			enabletooltips: true,
			
			selectionmode: 'checkbox',
			renderstatusbar: function (statusbar) {
				if(isShowButtons == true){
					var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
					var addButton = $("<div id='addButton' style='float: left; margin-left: 5px;'><img style='position: relative; margin-top: 2px;' src='images/icons/add.png' width='16px' height='16px'/><span style='margin-left: 4px; position: relative; top: -3px;'>New</span></div>");
					var deleteButton = $("<div style='float: left; margin-left: 5px;'><img style='position: relative; margin-top: 2px;' src='images/icons/delete.png' width='16px' height='16px'/><span style='margin-left: 4px; position: relative; top: -3px;'>Delete</span></div>");
					var reloadButton = $("<div style='float: left; margin-left: 5px;'><img style='position: relative; margin-top: 2px;' src='images/icons/refresh.png' width='16px' height='16px'/><span style='margin-left: 4px; position: relative; top: -3px;'>Reload</span></div>");
					container.append(addButton);
					container.append(deleteButton);
					container.append(reloadButton);
					statusbar.append(container);
					addButton.jqxButton({ theme: theme, width: 65, height: 17 });
					deleteButton.jqxButton({ theme: theme, width: 65, height: 17 });
					reloadButton.jqxButton({ theme: theme, width: 65, height: 17 });
					
					addButton.click(function (event) {
						showHideErrorMessageEditorDiv(false,".editorErrorDiv");
						if(typeof $('#'+createWindowId+' #createBeanForm')[0] != 'undefined'){
							$('#'+createWindowId+' #createBeanForm')[0].reset();
						}
						$('#'+createWindowId+' #isEnabledInput').val(false);
						$('#'+createWindowId+' #isEnabledInput').removeAttr('checked');
						$("#"+ createWindowId + " #seqInput").val(0);
						var x = ($(window).width() - $("#"+createWindowId).jqxWindow('width')) / 2 + $(window).scrollLeft();
		                var y = ($(window).height() - $("#"+createWindowId).jqxWindow('height')) / 2 + $(window).scrollTop();
		                $("#"+createWindowId).jqxWindow({ position: { x: x, y: y} });
		                
						$("#"+createWindowId).jqxWindow('open');
						$('#'+createWindowId).jqxWindow({ title: 'Create New '+ beansName }); 
					});
					deleteButton.click(function (event) {
						var selectedRowIndexes = $("#"+gridId).jqxGrid('selectedrowindexes');
						if(selectedRowIndexes.length > 0 ){
							$("#deleteBeanConfirmation #gridId").val(gridId);
							$("#deleteBeanConfirmation #deleteUrl").val(deleteUrl);
							$("#deleteBeanConfirmation .countDeletion").html(selectedRowIndexes.length);
							$("#deleteBeanConfirmation").jqxWindow('open');
							
						}else{
							$("#NoSelection").jqxWindow('open');
						}
					});
					reloadButton.click(function (event) {
						$("#"+gridId).jqxGrid({ source: getAdapter() });
					});
				}
			},
								   
			columns: gridColumns
		});//jqxgrid rendering
		
		$("#"+gridId).on('rowdoubleclick', function (event){ 
			var args = event.args;
			var rowIndex = args.rowindex;
			var dataRow = $("#"+gridId).jqxGrid('getrowdata', rowIndex);
			doubleClickEditRow(createWindowId,dataRow,rowIndex,gridDataFields);
		});//end double click method

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
					columnCheckBox.jqxCheckBox({ checked: true });
				}
				else if (checkedItemsCount == 0) {
					columnCheckBox.jqxCheckBox({ checked: false });
				}
				else {
					columnCheckBox.jqxCheckBox({ checked: null });
				}
			}
		$("#"+gridId).on('sort', function (event) {
			updatePageState(gridId);
		});

		$("#"+gridId).on('pagechanged', function (event) {
			updatePageState(gridId);
		});
		//validator
		$('#createBeanForm').jqxValidator({
			animationDuration:5,
			rules: validatorRules
		});
		
		//savebutton click
		$("#saveButton").click(function () {
			var validationResult = function (isValid) {
				if (isValid) {
					submitAddRecord(addUrl,gridId,createWindowId,gridDataFields);
				}
			}
			$('#createBeanForm').jqxValidator('validate', validationResult);
		});
		
		$("#createBeanForm").on('validationSuccess', function () {
			$("#createBeanForm-iframe").fadeIn('fast');
		});
		$("#closeButton").click(function () {
			$('#jqxCreateBeanWindow').jqxWindow('close'); 
		});
		$("#yesDelete").unbind();
		$("#yesDelete").click(function () {
			
			deleteGridId = $("#deleteBeanConfirmation #gridId").val();
			deletingUrl = $("#deleteBeanConfirmation #deleteUrl").val();
			var selectedRowIndexes = $("#"+deleteGridId).jqxGrid('selectedrowindexes');
			var ids = "";
			$.each(selectedRowIndexes, function(index , value){
				var dataRow = $("#"+deleteGridId).jqxGrid('getrowdata', value);
				if(typeof dataRow.seq === 'undefined'){
					ids = ids + dataRow.campSeq + ",";
					//becuase campaign grid has campSeq and not seq
				}else{
					ids = ids + dataRow.seq + ",";
					
				}
			});
			$.getJSON(deletingUrl+"&ids=" + ids,function(json){
				if(json[0]['status'] == 'success'){
					$("#"+deleteGridId).jqxGrid('deleterow', selectedRowIndexes);
				}
			});
			 $("#"+gridId).jqxGrid('clearselection');
		});
		
	}//redner grid function

function submitAddRecord(saveUrl,gridId,createWindowId,gridDataFields){
	dataRow = {};
	dataRow['rowId'] = $("#"+createWindowId+" #rowIdInput").val();
	$.each(gridDataFields,function(index,value){
		dataRow[value.name] = $("#"+createWindowId+" #"+ value.name +"Input").val();
		if(value.type == "radio"){
			dataRow[value.name] = $('input[name='+ value.name +']:radio:checked').val()
		}
	});
	if(isSelectionGrid == true){
		dataRow =  getSelectedRowsData(dataRow);
	}
	submitAddRecordAction(saveUrl,gridId,dataRow,"editorErrorDiv");
}
function submitAddRecordAction(saveUrl,gridId,dataRow,errDivClass){
	$.getJSON(saveUrl,dataRow,function(json){
		if(json['status'] == 'success'){
			dataRow['lastmodifieddate'] = json['lastModified'];	
			if(dataRow['seq'] == null || dataRow['seq'] == "" || dataRow['seq'] == "0"){		
				dataRow['seq'] = json['seq'];				
				if(typeof isCampaignUI != 'undefined'){
					if(isCampaignUI){
						$("#campaignSeq").val(json['seq']);
					}
				}
				dataRow['createdOn'] = json['createdOn'];
				var commit = $("#"+gridId).jqxGrid('addrow', null, dataRow,null,true);
			}else{
				var commit = $("#"+gridId).jqxGrid('updaterow', dataRow['rowId'], dataRow,true);
			}
			$("#"+gridId).jqxGrid('ensurerowvisible', dataRow['rowId']);
		}else{
			displaySaveErrors(json['message'],"."+errDivClass);
		}
	});
	
}
function displaySaveErrors(message,errorDivName){
	displayErrorMessageOnEditor(message,errorDivName);
}
function displayErrorMessageOnEditor(message,errorDivName){
	showHideErrorMessageEditorDiv(true,errorDivName);
	$(errorDivName).text(message);
}
function showHideErrorMessageEditorDiv(bool,errorDivName){
	if(bool == true){
		$(errorDivName).show();
	}else{
		$(errorDivName).hide();
	}
}
function clearErrorMessageDivs(){
	showHideErrorMessageEditorDiv(false,".editorErrorDiv");
	$(".editorErrorDiv").text("");
	
}
function doubleClickEditRow(windowId,dataRow,rowIndex,gridDataFields){
	editingBeanRow = dataRow;//page level variable for use in jsp page
	$("#"+windowId+" #rowIdInput").val(rowIndex);
	$.each(gridDataFields,function(index,value){
		if(value.name != "isEnabled"){
			 var rowColVal=dataRow[value.name];
			 if (rowColVal==undefined){
				 if (value.type="date"){
					// rowColVal=new Date();
					 rowColVal="";
				 }else{
					 rowColVal="";
				 }
			 }
			 if (value.type == "date"){
				 //$("#jqxCreateBeanWindow #"+ value.name +"Input").jqxDateTimeInput('setDate', rowColVal);
			 }else{
				 $("#"+windowId+" #"+ value.name +"Input").val(rowColVal);
			 }
			
			 if(value.type=="bool"){
				 if(dataRow[value.name] == true || dataRow[value.name] == "true"){						 
					$("#" + value.name +"Input").prop('checked', true);
					
				 }else{
					$("#" + value.name +"Input").prop('checked', false);
					
				 }
			 }
			 if(value.type=="radio"){
				$("input[name='" + value.name + "'][value='" + dataRow[value.name] + "']").attr("checked", "checked");
				 // to hanle null field case //$("input[name='" + value.name + "'][value='" + rowColVal + "']").attr("checked", "checked");
			 }
		}else{
			if(dataRow[value.name] == true){
				$("#"+windowId+" #isEnabledInput").jqxCheckBox('check');
			}else{
				$("#"+windowId+" #isEnabledInput").jqxCheckBox('uncheck');
			}
		}
	});
	var x = ($(window).width() - $("#"+windowId).jqxWindow('width')) / 2 + $(window).scrollLeft();
	var y = ($(window).height() - $("#"+windowId).jqxWindow('height')) / 2 + $(window).scrollTop();
	$("#"+windowId).jqxWindow({ position: { x: x, y: y} });
	$('#'+windowId).jqxWindow({ title: 'Edit '+ beanName }); 
	$('#'+windowId).jqxWindow('open');
	clearErrorMessageDivs();
}
				


	