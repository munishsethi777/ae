<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<html lang="en">
<head>

    <script type="text/javascript">
   		var pageName = "userGroups";
    	var beanName = "UserGroup";
		var dataUrl = "AdminUser?action=getAllUserGroups";
		var deleteUrl = "AdminUser?action=deleteUserGroup";
		var addUrl = "AdminUser?action=addUserGroup";
	
		var validatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' }];
				
		var columns = [
				{ text: 'Name', datafield: 'name',editable:false,width:300 },
				{ text: 'Description', datafield: 'description',editable:false },
				{ text: 'Created On', datafield: 'createdOn',editable:false,width:170,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:170,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:60}
				];

		var dataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'id', type: 'integer' },
				{ name: 'name', type: 'string' },
				{ name: 'description', type: 'string' },
				{ name: 'createdOn', type: 'date' },
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
		var selectionGridSourceUrl_ = "AdminUser?action=getUsersAvailableOnUserGroup";
		var selectionGridDestinationUrl_ = "AdminUser?action=getUsersSelectedOnUserGroup";
		var theme_ = "energyblue";
		
		var selectionGridColumns_ = [
				{ text: 'Name', datafield: 'name',editable:false,width:"70" },
				{ text: 'Location', datafield: 'location',editable:false,width:"80" },
				{ text: 'UserName', datafield: 'username',editable:false,width:"80" },
				{ text: 'Email', datafield: 'email',editable:false}];
		var selectGriddataFields_ = [
				{ name: 'seq', type: 'integer' },
				{ name: 'name', type: 'string' },
				{ name: 'email', type: 'string' },
				{ name: 'mobile', type: 'string' },
				{ name: 'location', type: 'string' },
				{ name: 'username', type: 'string' },
				{ name: 'password', type: 'string' },
				{ name: 'createdon', type: 'date' },
				{ name: 'isenabled', type: 'bool' }];
		
		
		$(document).ready(function () {
				var editorWidth= "80%";
				var editorHeight = "80%";
				renderGrid("jqxGrid",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,true,editorHeight,editorWidth);
				$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
				renderSelectionGrid(0,sourceGridColumnCheckBox_,sourceGridUpdatingCheckState_,destinationGridColumnCheckBox_,destinationGridUpdatingCheckState_,sourceGridId_,destinationGridId_,sourceGridWidth_,destinationGridWidth_,selectionGridSourceUrl_,selectionGridDestinationUrl_,theme_,selectionGridColumns_,selectGriddataFields_);
				
				$('#jqxCreateBeanWindow').on('open', function (event) { 
					$('#jqxCreateBeanWindow').jqxWindow({ position: 'center'}); 
					var editingParentSeq = $("#jqxCreateBeanWindow #seqInput").val();
					
					$("#"+sourceGridId_).jqxGrid({source:getSelectionGridAdapter(selectionGridSourceUrl_,editingParentSeq)});
					$("#"+destinationGridId_).jqxGrid({source:getSelectionGridAdapter(selectionGridDestinationUrl_,editingParentSeq)});
					
				});
				function getSelectionGridAdapter(url,editingParentSeq){
					var source1 = {
						url:url + "&userGroupSeq="+editingParentSeq,
						id: 'seq',
						datatype: "json",
						datafields:selectGriddataFields_,
					};
					var dataAdapter = new $.jqx.dataAdapter(source1);
					return dataAdapter;
				}
				
				//handling the tabs and radios here
				//$('#jqxTabs').jqxTabs({ width: '100%', height:'85%', theme: theme, toggleMode:'none' });
				$("#isSelectionGrid").jqxRadioButton({ width: 230, height: 25, checked: true, theme: theme });
	            $("#isImportUsers").jqxRadioButton({ width: 180, height: 25, theme: theme });
	            $("#isRegisterUsersURL").jqxRadioButton({ width: 150, height: 25, theme: theme });
	            $("#isSelectionGrid").on('change', function (event) {
	            	$(".tabContent1").show();
	            	$(".tabContent2").hide();
	            	$(".tabContent3").hide();
	            });
	            $("#isImportUsers").on('change', function (event) {
	            	$(".tabContent1").hide();
	            	$(".tabContent2").show();
	            	$(".tabContent3").hide();    
	            });
	            $("#isRegisterUsersURL").on('change', function (event) {
	            	$(".tabContent1").hide();
	            	$(".tabContent2").hide();
	            	$(".tabContent3").show();    
	            });
	            
		});//end document ready	
	
		function getSelectedRowsData(dataRow){
			var gridId = destinationGridId_;
			if($("#isImportUsers").val()){
				var gridId = "jqxgrid";
			}
			var allrows = $("#"+gridId).jqxGrid("getrows");
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
<div id='mainBodyDiv'>
	<label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">UserGroups Information</label><br>
	<label style="font-family:verdana;font-size: 12px;color:grey">View, Create, Edit, Bulk Delete or Find through various usergroups available in the database.</label>
	<br> <br>
	<div id="jqxGrid"></div>
	
	<!-- Editor Area Starts here -->
	<div id="jqxCreateBeanWindow">
		<div class="title" style="height:30px;font-size:16px;line-height:25px;font-weight:bold">Create New Project</div>
		<div id="jqxCreateBeanEditor">
			<div class="editorErrorDiv"></div>
			<div class="editorSuccessDiv"></div>
			 <form id="createBeanForm" name="createBeanForm"/>
				<input type="hidden" name="rowId" id="rowIdInput"/>
				<input type="hidden" name="seq" id="seqInput"/>
				
				<input type="hidden" name="createdOn" id="createdOnInput"/>
				<table style="overflow: hidden;">
					<tr>
						<td>Name</td>
						<td><input name="name" type="text" id="nameInput"/></td>
						<td><input type="button"  value="Save" id="saveButton" /></td>
					</tr>
					<tr>
						<td>Description</td>
						<td><input name="description" type="text" id="descriptionInput"/></td>
						<td><input type="button" value="Close" id="closeButton" /></td>
					</tr>
						<tr>
						<td>Enabled</td>
						<td><div id="isEnabledInput"></div></td>
						<td></td>
					</tr>
					<tr>

						<td colspan="3">
							<div id='isSelectionGrid' style="float:left">Select from Available users</div>
							<div id='isImportUsers' style="float:left">Import users xls</div>
							<div id='isRegisterUsersURL' style="float:left">Register users</div>
						</td>
						
					</tr>
				</table>
			</form>
				
				
		   	<div class="tabContent1">
				<p>Select from the available list of users and move them to the users of the usergroup.</p>
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
			<div class="tabContent2" style="display:none">
				<%@include file="importUser.jsp" %>	
			</div>
			<div class="tabContent3" style="display:none">
				Save usergroup
			</div>
		
		</div><!-- Ending jqxCreateBeanEditor -->
	</div> <!-- Ending jqxCreateBeanWindow -->
</div><!-- Ending mainBodyDiv -->
</body>
</html>
