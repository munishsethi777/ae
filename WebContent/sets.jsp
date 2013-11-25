<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>

    <script type="text/javascript">
		var pageName = "sets";
		var beanName = "Assignment Set";
		var dataUrl = "AdminUser?action=getAllSets";
		var deleteUrl = "AdminUser?action=deleteSet";
		var addUrl = "AdminUser?action=addSet";
		var validatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' }];
				
		var columns = [
				{ text: 'Name', datafield: 'name',editable:false,width:250 },
				{ text: 'Description', datafield: 'description' ,editable:false},
				{ text: 'Created On', datafield: 'createdOn',editable:false,width:220,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:220,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:60}
				];

		var dataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'name', type: 'string' },
				{ name: 'description', type: 'string' },
				{ name: 'createdOn', type: 'string' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'lastmodifieddate', type: 'date'}
				];
						
		//for the purpose of selection grid for children items
		var sourceGridColumnCheckBox_ = null;
		var sourceGridUpdatingCheckState_ = false;
		
		var destinationGridColumnCheckBox_ = null;
		var destinationGridUpdatingCheckState_ = false;
		
		var sourceGridId_ = "sourceGrid";
		var destinationGridId_ = "destinationGrid";
		var sourceGridWidth_ = "45%";
		var destinationGridWidth_ = "45%";
		var selectionGridSourceUrl_ = "AdminUser?action=getGamesAvailableOnSet";
		var selectionGridDestinationUrl_ = "AdminUser?action=getGamesSelectedOnSet";
		var theme_ = "energyblue";
		
		var selectionGridColumns_ = [				
				{ text: 'Title', datafield: 'gameTitle' ,editable:false},
				{ text: 'Description', datafield: 'description' ,editable:false},
				{ text: 'Created On', datafield: 'createdOn',editable:false}
		];
				
		var selectGriddataFields_ = [
				{ name: 'seq', type: 'integer' },
				{ name: 'gameTitle', type: 'string' },
				{ name: 'description', type: 'string' },
				{ name: 'createdOn', type: 'date' },
		];
		
		$(document).ready(function () {
				var editorWidth= "75%";
				var editorHeight = "600px";
				renderGrid("jqxGrid",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,true,editorHeight,editorWidth);
				$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
				renderSelectionGrid(0,sourceGridColumnCheckBox_,sourceGridUpdatingCheckState_,destinationGridColumnCheckBox_,destinationGridUpdatingCheckState_,sourceGridId_,destinationGridId_,sourceGridWidth_,destinationGridWidth_,selectionGridSourceUrl_,selectionGridDestinationUrl_,theme_,selectionGridColumns_,selectGriddataFields_);
				
				$('#jqxCreateBean').on('open', function (event) { 
					$('#jqxCreateBean').jqxWindow({ position: 'center'}); 
					var editingParentSeq = $("#jqxCreateBean #seqInput").val();
					
					$("#"+sourceGridId_).jqxGrid({source:getSelectionGridAdapter(selectionGridSourceUrl_,editingParentSeq)});
					$("#"+destinationGridId_).jqxGrid({source:getSelectionGridAdapter(selectionGridDestinationUrl_,editingParentSeq)});
					
				});
				function getSelectionGridAdapter(url,editingParentSeq){
					var source1 = {
						url:url + "&setSeq="+editingParentSeq,
						id: 'seq',
						datatype: "json",
						datafields:selectGriddataFields_,
					};
					var dataAdapter = new $.jqx.dataAdapter(source1);
					return dataAdapter;
				}
		});//end document ready	
		function getSelectedRowsData(dataRow){
				var allrows = $("#"+destinationGridId_).jqxGrid("getrows");
				var allRowIds = new Array();
				for(var i = 0;i<allrows.length;i++){
					allRowIds.push(allrows[i].seq);
				}
				dataRow['selectedChildrenRows'] = allRowIds.toString();
				return dataRow;
			}
    </script>
</head>
<body class='default'>
	<%@ include file="header.jsp" %>
	<%@ include file="menu.jsp" %>
	<%@ include file="grid.jsp" %>
    <div id='jqxWidget'>
		<label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">Assessment Sets Information</label><br>
		<label style="font-family:verdana;font-size: 12px;color:grey">View, Create, Edit, Bulk Delete or Find through various sets available in the database.</label>
        <br> <br><div id="jqxGrid">
        </div>
		<div id="jqxCreateBean">
           <div class="title" style="font-weight:bold">Create New Set</div>
		   <div style="overflow: hidden;padding:10px;">
				<form id="createBeanForm" name="createBeanForm"/>
					<input type="hidden" name="rowId" id="rowIdInput"/>
					<input type="hidden" name="seq" id="seqInput"/>
					<input type="hidden" name="id" id="idInput"/>
					<input type="hidden" name="createdOn" id="createdOnInput"/>
					<table style="overflow: hidden;">
					<tr>
						<td>Name</td>
						<td><input name="name" type="text" id="nameInput"/></td>
					</tr>
					<tr>
						<td>Description</td>
						<td><input name="description" type="text" id="descriptionInput"/></td>
					</tr>
					<tr>
						<td>Enabled</td>
						<td><div id="isEnabledInput"></div></td>
					</tr>
					<tr>
						<td><input type="button" style='margin-top: 15px; margin-left: 50px; float: left;' value="Save" id="saveButton" /></td>
						<td><input type="button" style='margin-left: 5px; margin-top: 15px; float: left;' value="Close" id="closeButton" /></td>
					</tr>
				</table>
				</form>
				<p>Select from the available list of Games and move them to the Games of the Set.</p>
				<%@ include file="selectionGridComponent.jsp" %>
				<div id='jqxWidget' style="margin-top:20px;">
					<div id="sourceGrid" style="float:left"></div>
					<div style="float:left;margin:50px 10px 0px 10px;padding:20px;">
						<img class="btn addRowBtn" src="images/icons/arrowRight.png"><br>
						<img class="btn deleteRowBtn" src="images/icons/arrowLeft.png"><br>
						<img class="btn addAllRowBtn" src="images/icons/arrowAllRight.png"><br>
						<img class="btn deleteAllRowBtn" src="images/icons/arrowAllLeft.png">
					</div>
					<div id="destinationGrid" style="float:left;"></div>
				</div>
				
           </div>
		</div>
		
	
	
	</div>
</body>
</html>
