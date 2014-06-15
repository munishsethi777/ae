
var dataUserUrl = "AdminUser?action=getAllUsers" ;
var deleteUserUrl = "AdminUser?action=deleteUser";
var addUserUrl  = "AdminUser?action=addUser";
var userGroupDataUrl = "AdminUser?action=getAllUserGroups";
var deleteUserGroupUrl = "AdminUser?action=deleteUserGroup";
userGridId = "#userJqxGrid";

var userValidatorRules = [
        { input: '#userGroupNameInput', message: 'Usergroup Name is required!', action: 'keyup, blur', rule: 'required' },
		{ input: '#nameUserInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' },
		{ input: '#usernameInput', message: 'Username is required!', action: 'keyup, blur', rule: 'required' },
		{ input: '#passwordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
		{ input: '#emailInput', message: 'Enter valid email!', action: 'keyup, blur', rule: 'email' },
		{ input: '#mobileInput', message: 'Mobile is required!', action: 'keyup, blur', rule: 'required' }];
var userColumns = [
		{ text: 'Name', datafield: 'nameUser' ,editable:false,width:"20%"},
		{ text: 'Email Id', datafield: 'email' ,editable:false,width: "20%"},
		{ text: 'UserName', datafield: 'username',editable:false,width: "20%"},
		{ text: 'Signup Date', datafield: 'createdOn',editable:false,width: 150,cellsformat: 'dd-MM-yy hh.mm tt'}];
var userDataFields = [
		{ name: 'seq', type: 'integer' },
		{ name: 'userGroupSeq', type: 'integer'},
		{ name: 'userGroupName', type: 'string' },
		{ name: 'userGroupDescription', type: 'string' },
		{ name: 'nameUser', type: 'string' },
		{ name: 'email', type: 'string' },
		{ name: 'mobile', type: 'string' },
		{ name: 'location', type: 'string' },
		{ name: 'username', type: 'string' },
		{ name: 'password', type: 'string' },
		{ name: 'createdOn', type: 'date' },
		{ name: 'isEnabled', type: 'bool' },
		{ name: 'lastmodifieddate', type: 'date'}
		];
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



function loadControls(){
	$("#addUserButton").jqxButton({	width : 120,	theme : theme});
	
	$("#isEnabledUserInput").jqxCheckBox({width : 120,height : 25,theme : theme});
	$("#isCreateNewUser").jqxRadioButton({width : 180,height : 25,checked : true,theme : theme});
	$("#isImportUsers").jqxRadioButton({width : 180,height : 25,theme : theme});
	$("#isRegisterUsersURL").jqxRadioButton({width : 180,height : 25,theme : theme});
	
	
	/*$(".userEditorArea .optionRadios").jqxButtonGroup({ mode: 'radio' , theme: theme});
	$('.userEditorArea .optionRadios').jqxButtonGroup('setSelection', 0);*/
	
	$("#isCreateNewUser").on('change', function(event) {
		$(".tabContent1").show();
		$(".tabContent2").hide();
		$(".tabContent3").hide();
	});
	$("#isImportUsers").on('change', function(event) {
		$(".tabContent1").hide();
		$(".tabContent2").show();
		$(".tabContent3").hide();
	});
	$("#isRegisterUsersURL").on('change', function(event) {
		$(".tabContent1").hide();
		$(".tabContent2").hide();
		$(".tabContent3").show();
	});
	var getRegistrationUrlURL = "AdminUser?action=getRegistrationUrl";
	$.get(getRegistrationUrlURL, function(response) {
		$(".registrationURLDiv").html("<p><a href='"+ response +"'>" + response	+ "</a></p>");
	});
	$("#addUserButton").unbind();
	$("#addUserButton").click(function(event) {
		var validationResult = function (isValid) {
			if (isValid) {
				addUsersGroupFromEditor();
			}
		};
		$('#createUserForm').jqxValidator('validate', validationResult);
	});
	
	$('#createUserForm').jqxValidator(
			{rules : userValidatorRules});
	//savebutton click			
//	$("#createUserForm").on('validationSuccess',
//			function() {
//				$("#createUserForm-iframe").fadeIn('fast');
//			});

	$('#mainUserSplitter').jqxSplitter({ width: "100%", height: "100%", 
		orientation: 'vertical', theme: theme, panels: [{ size: "60%" }, { size: "40%"}] });
   	$(".userEditorArea").jqxPanel({width : "100%",height : "100%",theme : theme	});

}//load controls ends
function updateSelectedUserGrid(dataRowJson) {
	$(userGridId).jqxGrid('addrow', null, dataRowJson, null, true);
}
function getUserFormData(isImport) {
	dataRow = [];
	var userGroupSeqObj = new Object();
	var userGroupSeq = $("#userGroupSeqInput").val();
	userGroupSeqObj.name = "userGroupSeq";
	userGroupSeqObj.value = userGroupSeq;

	var campaignSeqObj = new Object();
	var campaignSeq = getCampaignSeqFromForm();
	campaignSeqObj.name = "campaignSeq";
	campaignSeqObj.value = campaignSeq;
	
	var userGroupNameObject = new Object();
	var userGroupName = $("#userGroupNameInput").val();
	userGroupNameObject.name = "userGroupName";
	userGroupNameObject.value = userGroupName;
	
	var userGroupDescriptionObj = new Object();
	var userGroupDescription = $("#userGroupDescriptionInput").val();
	userGroupDescriptionObj.name = "userGroupDescription";
	userGroupDescriptionObj.value = userGroupDescription;
	var index = 0;
	if (!isImport) {
		dataRow = $("#createUserForm").serializeArray();
		index = dataRow.length;
	}
	dataRow[index++] = userGroupSeqObj;
	dataRow[index++] = campaignSeqObj;
	dataRow[index++] = userGroupNameObject;
	dataRow[index++] = userGroupDescriptionObj;
	return dataRow;
}

function addUsersGroupFromImportUsers(userJson) {
	data = getUserFormData(true);
	var userSeqArr = [];
	var i = 0
	$.each(userJson, function() {
		userSeqArr.push(userJson[i]['seq']);
		i = i + 1;
	})
	var obj1 = new Object();
	obj1.name = "userSeqs";
	obj1.value = userSeqArr;
	data[data.length] = obj1;
	dataRow = data;
	submitAddUsers(dataRow);
}
function saveCampaignUserGroup(){
	var userGroupSelectedIndex = $('#createSelectUserGroupRadios').jqxButtonGroup('getSelection');
	if(userGroupSelectedIndex == 1){
		addUserGroupFromEarlier();		
	}else{
		updateUserGroup();
	}
}
function addUsersGroupFromEditor() {
	dataRow = getUserFormData(false)
	submitAddUsers(dataRow);
}
function updateUserGroup(){
	dataRow = getUserFormData(true);
	$.getJSON("AdminUser?action=updateUserGroup", dataRow,	function(json) {
		if (json['status'] == 'success'){
			
		}
	});
}
function submitAddUsers(dataRow) {
	$.getJSON("AdminUser?action=addUserGroupFromCampaign", dataRow,
		function(json) {
			if (json['status'] == 'success') {
				if (dataRow['seq'] == null || dataRow['seq'] == ""
						|| dataRow['seq'] == "0") {
					dataRow['seq'] = json['userGroupSeq'];
					$("#userGroupSeq").val(json['userGroupSeq']);
					dataRowJson = {};
					$.each(dataRow, function() {
						dataRowJson[this.name] = this.value || '';
					});
				}
				$(userGridId).jqxGrid('addrow', null, dataRowJson,null, true);
				$('#createUserForm')[0].reset();
			}else{
				displaySaveErrors(json['message'],".userEditorErrorDiv");
			}
		});
}


function renderUserGrid(){
	var campaignSeq = getCampaignSeqFromForm();
	if(campaignSeq != null){
		dataUserUrl = "AdminUser?action=getUsersInSelectedUserGroupByCampaign&campaignSeq=" + campaignSeq; 	
		addUserUrl = "";	
	}
	renderGrid("userJqxGrid","jqxCreateUserBeanEditor","User",dataUserUrl,deleteUserUrl,addUserUrl,
			userValidatorRules,userColumns,userDataFields,true,"280","95%");
	$('#userJqxGrid').on('initialized', function () {
		loadUserGroupDetails();
	});
	bindNewClickOnSelectedUsersGrid();
	bindDoubleClickOnSelectedUsersGrid();
	loadControls();
}
function loadUserGroupDetails(){
	var campaignSeq = getCampaignSeqFromForm();
	if(campaignSeq != null){
		getUserGroupDetailsUrl = "AdminUser?action=getUserGroupsSelectedOnCampaign&campaignSeq=" + campaignSeq; 	
		$.getJSON(getUserGroupDetailsUrl,function(json){
			if(json[0].status != "failure"){
				$('#userJqxGrid').jqxGrid('selectrow', json[0]["seq"]);
				$("#userGroupSeqInput").val(json[0]["seq"]);
				$("#userGroupNameInput").val(json[0]["name"]);
				$("#userGroupDescriptionInput").val(json[0]["description"]);
			}
		});
	}
}
function bindNewClickOnSelectedUsersGrid(){
	$("#statusbaruserJqxGrid #addButton").unbind('click');
	$("#statusbaruserJqxGrid #addButton").click(function (event) {
		resetUsersEditor();
	});
}
function resetUsersEditor(){
	showHideErrorMessageEditorDiv(false,".userEditorErrorDiv");
	if(typeof $('#createUserForm')[0] != 'undefined'){
	$('#createUserForm')[0].reset();
}
$("#isEnabledUserInput").jqxCheckBox('check');
$("#createUserForm #seqInput").val(0);
}
function bindDoubleClickOnSelectedUsersGrid() {
	//$("#selectedQuestionsGrid").unbind('rowdoubleclick');
$("#userJqxGrid").unbind('rowdoubleclick');
$("#userJqxGrid").on('rowdoubleclick', function (event){ 
	var args = event.args;
	var rowIndex = args.rowindex;
	var dataRow = $("#userJqxGrid").jqxGrid('getrowdata', rowIndex);
	editingBeanRow = dataRow;//page level variable for use in jsp page
	$("#createUserForm #rowIdInput").val(rowIndex);
	$.each(userDataFields,function(index,value){
		if(value.name != "isEnabled"){
			 var rowColVal=dataRow[value.name];
			 if (rowColVal==undefined){
				 if (value.type="date"){
					// rowColVal=new Date();
				 }else{
					 rowColVal="";
				 }
			 }
			// if (value.type="date"){
			//	 $("#jqxCreateBeanWindow #"+ value.name +"Input").jqxCalendar('setDate', rowColVal); 
				 //$("#jqxCreateBeanWindow #"+ value.name +"Input").val(rowColVal);
			// }else{
				 $("#createUserForm #"+ value.name +"Input").val(rowColVal);
			// }
			
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
				$('#isEnabledUserInput').jqxCheckBox('check');
			}else{
				$('#isEnabledUserInput').jqxCheckBox('uncheck');
				}
			}
		});
	});
}
	
function renderUserGroupsGrid(){
	var editorWidth= "85%";
	var editorHeight = "80%";
	//generate usergroups grid with all usergroups in db
	renderGrid("userGroupJqxGrid","addusersWindow","userGroups",
			userGroupDataUrl,deleteUserGroupUrl,"","",userGroupcolumns,userGroupDataFields,false,editorHeight,editorWidth);
}

function addUserGroupFromEarlier(){
	var selectedRowIndexes = $("#userGroupJqxGrid").jqxGrid('selectedrowindexes');
	var ids = "";
	$.each(selectedRowIndexes, function(index , value){
		var dataRow = $("#userGroupJqxGrid").jqxGrid('getrowdata', value);
		ids = ids + dataRow.seq;
	});
	var campaignSeq = getCampaignSeqFromForm();
	$.getJSON("AdminUser?action=addCampaignUserGroup&userGroupSeq=" + ids + "&campaignSeq=" + campaignSeq ,function(json){
		if(json['status'] == 'success'){
			//$("#userGroupJqxGrid").jqxGrid('clearselection');
		}
	});
}