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
				renderGrid("userGroupJqxGrid","addusersWindow","userGroups",
						userGroupDataUrl,deleteUserGroupUrl,"","",userGroupcolumns,userGroupDataFields,false,editorHeight,editorWidth);
		});//end document ready	
	
		function addUserGroupFromEarlier(){
			var selectedRowIndexes = $("#userGroupJqxGrid").jqxGrid('selectedrowindexes');
			var ids = "";
			var i = 0;
			$.each(selectedRowIndexes, function(index , value){
				var dataRow = $("#userGroupJqxGrid").jqxGrid('getrowdata', value);
				ids = ids + dataRow.seq;
			});
			var campaignSeq = $("#createCampaignForm #seqInput").val();
			$.getJSON("AdminUser?action=addCampaignUserGroup&userGroupSeq=" + ids + "&campaignSeq=" + campaignSeq ,function(json){
				if(json['status'] == 'success'){
					$("#userGroupJqxGrid").jqxGrid('clearselection');
				}
			});
		}
    </script>
	<div id="userGroupJqxGrid"></div>