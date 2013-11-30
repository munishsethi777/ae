<script type="text/javascript">
   		var pageName = "userGroups";
    	var beanName = "UserGroup";
		var userGroupDataUrl = "AdminUser?action=getAllUserGroups";
		var deleteUserGroupUrl = "AdminUser?action=deleteUserGroup";
		
	
		var userGroupvalidatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' }];
				
		var userGroupcolumns = [
				{ text: 'Name', datafield: 'name',editable:false,width:300 },
				{ text: 'Description', datafield: 'description',editable:false },
				{ text: 'Created On', datafield: 'createdOn',editable:false,width:170,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:170,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:60}
				];

		var  userGroupDataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'id', type: 'integer' },
				{ name: 'name', type: 'string' },
				{ name: 'description', type: 'string' },
				{ name: 'createdOn', type: 'date' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'lastmodifieddate', type: 'date'}];
		
			$(document).ready(function () {
				var editorWidth= "80%";
				var editorHeight = "80%";
				renderGrid("userGroupJqxGrid","userGroups",userGroupDataUrl,deleteUserGroupUrl,"","",userGroupcolumns,userGroupDataFields,false,editorHeight,editorWidth);
				$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
				
				
	            
		});//end document ready	
	
		function addFromEarlierUserGroup(dataRow){
			var gridId = destinationGridId_;
			if($("#isImportUsers").val()){
				var gridId = "userGroupJqxGrid";
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
	<div id="userGroupJqxGrid"></div>