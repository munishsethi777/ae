var theme = "fresh";
var columnCheckBox = null;
var updatingCheckState = false;
var isSelectionGrid = false;
var editingBeanRow = null;
function renderGrid(gridId,beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,isShowButtons,editorHeight,editorWidth){	
		$("#jqxCreateBeanWindow").jqxWindow({ 
				resizable: true, theme: theme, autoOpen: false, maxWidth:2000, 
				maxHeight: 1000, width:editorWidth,height:editorHeight, showCloseButton: true,
				resizable: true,animationType: 'fade',isModal: true,modalOpacity: 0.8});
				var getAdapter = function () {
					var source =
						{
						url:dataUrl,
						datatype: "json",
						datafields:dataFields,
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
						var addButton = $("<div style='float: left; margin-left: 5px;'><img style='position: relative; margin-top: 2px;' src='images/icons/add.png' width='16px' height='16px'/><span style='margin-left: 4px; position: relative; top: -3px;'>New</span></div>");
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
							showHideErrorMessageEditorDiv(false);
							if(typeof $('#createBeanForm')[0] != 'undefined'){
								$('#createBeanForm')[0].reset();
							}
							$('#isEnabledInput').val(false);
							$("#isEnabledInput").removeAttr('checked');
							$("#jqxCreateBeanWindow #seqInput").val(0);
							var x = ($(window).width() - $("#jqxCreateBeanWindow").jqxWindow('width')) / 2 + $(window).scrollLeft();
			                var y = ($(window).height() - $("#jqxCreateBeanWindow").jqxWindow('height')) / 2 + $(window).scrollTop();
			                $("#jqxCreateBeanWindow").jqxWindow({ position: { x: x, y: y} });
			                
							$("#jqxCreateBeanWindow").jqxWindow('open');
							$('#jqxCreateBeanWindow').jqxWindow({ title: 'Create New '+ beanName }); 
						});
						deleteButton.click(function (event) {
							var selectedRowIndexes = $("#"+gridId).jqxGrid('selectedrowindexes');
							if(selectedRowIndexes.length > 0 ){
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
									   
				columns: columns
			});//jqxgrid rendering
			
			$("#"+gridId).on('rowdoubleclick', function (event){ 
				var args = event.args;
				var rowIndex = args.rowindex;
				var dataRow = $("#"+gridId).jqxGrid('getrowdata', rowIndex);
				editingBeanRow = dataRow;//page level variable for use in jsp page
				$("#jqxCreateBeanWindow #rowIdInput").val(rowIndex);
				$.each(dataFields,function(index,value){
					if(value.name != "isEnabled"){
						 $("#jqxCreateBeanWindow #"+ value.name +"Input").val(dataRow[value.name]);
						 if(value.type=="bool"){
							 if(dataRow[value.name] == true || dataRow[value.name] == "true"){						 
								$("#" + value.name +"Input").prop('checked', true);
							 }else{
								$("#" + value.name +"Input").prop('checked', false);
							 }
						 }
						 if(value.type=="radio"){
							$("input[name='" + value.name + "'][value='" + dataRow[value.name] + "']").attr("checked", "checked");
						 }
					}else{
						if(dataRow[value.name] == true){
							$('#isEnabledInput').jqxCheckBox('check');
						}else{
							$('#isEnabledInput').jqxCheckBox('uncheck');
						}
					}
				});
				var x = ($(window).width() - $("#jqxCreateBeanWindow").jqxWindow('width')) / 2 + $(window).scrollLeft();
				var y = ($(window).height() - $("#jqxCreateBeanWindow").jqxWindow('height')) / 2 + $(window).scrollTop();
				$("#jqxCreateBeanWindow").jqxWindow({ position: { x: x, y: y} });
				$('#jqxCreateBeanWindow').jqxWindow({ title: 'Edit '+ beanName }); 
				$('#jqxCreateBeanWindow').jqxWindow('open'); 
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
						submitAddRecord(gridId);
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
			
			
			
		}//redner grid function

function submitAddRecord(gridId){
	
	dataRow = {};
	dataRow['rowId'] = $("#jqxCreateBeanWindow #rowIdInput").val();
	$.each(dataFields,function(index,value){
		dataRow[value.name] = $("#jqxCreateBeanWindow #"+ value.name +"Input").val();
		if(value.type == "radio"){
			dataRow[value.name] = $('input[name='+ value.name +']:radio:checked').val()
		}
	});
	if(isSelectionGrid == true){
		var dataRow =  getSelectedRowsData(dataRow);
	}
	$.getJSON(addUrl,dataRow,function(json){
		if(json['status'] == 'success'){
			dataRow['lastmodifieddate'] = json['lastModified'];	
			if(dataRow['seq'] == null || dataRow['seq'] == "" || dataRow['seq'] == "0"){		
				dataRow['seq'] = json['seq'];				
				
				dataRow['createdOn'] = json['createdOn'];
				var commit = $("#"+gridId).jqxGrid('addrow', null, dataRow,null,true);
			}else{
				var commit = $("#"+gridId).jqxGrid('updaterow', dataRow['rowId'], dataRow,true);
			}
				if(typeof isCampaignUI != 'undefined'){
					if(isCampaignUI){
						$("#campaignSeq").val(json['seq']);
					}
				}
			$("#"+gridId).jqxGrid('ensurerowvisible', dataRow['rowId']);
		}else{
			displaySaveErrors(json['message']);
		}
	});
	
}
function displaySaveErrors(message){
	displayErrorMessageOnEditor(message);
}
function displayErrorMessageOnEditor(message){
	showHideErrorMessageEditorDiv(true);
	$(".editorErrorDiv").text(message);
}
function showHideErrorMessageEditorDiv(bool){
	if(bool == true){
		$(".editorErrorDiv").show();
	}else{
		$(".editorErrorDiv").hide();
	}
}
function submitDeleteRecord(gridId){
	var selectedRowIndexes = $("#"+gridId).jqxGrid('selectedrowindexes');
	var ids = "";
	var i = 0;
	$.each(selectedRowIndexes, function(index , value){
		var dataRow = $("#"+gridId).jqxGrid('getrowdata', value);
		ids = ids + dataRow.seq + ",";
	});
	$.getJSON(deleteUrl+"&ids=" + ids,function(json){
		if(json[0]['status'] == 'success'){
			$("#"+gridId).jqxGrid('deleterow', selectedRowIndexes);
		}
	});
	 $("#"+gridId).jqxGrid('clearselection');
}
				


	