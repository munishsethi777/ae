<script type="text/javascript">
	userGridId= "#userJqxGrid";
	function updateSelectedUserGrid(dataRowJson){
		$(userGridId).jqxGrid('addrow', null, dataRowJson,null,true);
	}
	function getUserFormData(isImport){
		dataRow = [];		
		var obj = new Object();
		var userGroupSeq = $("#userGroupSeq").val();
		obj.name = "userGroupSeq";
		obj.value = userGroupSeq;
		
		
		var obj1 = new Object();
		var campaignSeq = $("#campaignSeq").val();
		obj1.name = "campaignSeq";
		obj1.value = campaignSeq;
		var index = 0;
		if(!isImport){
			dataRow = $("#createUserForm").serializeArray();
			index = dataRow.length;
		}
		dataRow[index] = obj;
		dataRow[index + 1] = obj1
		return dataRow;
	}
	
	function addUsersGroupFromImportUsers(userJson){
		data = getUserFormData(true);
		var userSeqArr = [];
		var i = 0
		$.each(userJson, function() {
			userSeqArr.push(userJson[i]['seq']);
			i =i+1;
		})
		var obj1 = new Object();
		obj1.name = "userSeqs";
		obj1.value = userSeqArr;
		data[1] = obj1;
		dataRow = data;
		submitAddUsers(dataRow);
	}
	
	
	
	function addUsersGroupFromEditor(){
		dataRow = getUserFormData(false)
		submitAddUsers(dataRow);
	}
		function submitAddUsers(dataRow){			
			$.getJSON("AdminUser?action=addUserGroupFromCampaign",dataRow,function(json){
				if(json['status'] == 'success'){
					if(dataRow['seq'] == null || dataRow['seq'] == "" || dataRow['seq'] == "0"){		
						dataRow['seq'] = json['seq'];
						$("#userGroupSeq").val(json['userGroupSeq']);
						dataRowJson = {};
						 $.each(dataRow, function() {
							dataRowJson[this.name] = this.value || '';
						 });
					}
					$(userGridId).jqxGrid('addrow', null, dataRowJson,null,true);
					$('#createUserForm')[0].reset();
				}
			});
	    }
		
		$(document).ready(function () {
		var userVlidatorRules = [
				{ input: '#NameUserInput', message: 'Name is required!', action: 'keyup, blur', rule:'required' }];
				
			$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
			$("#isCreateNewUser").jqxRadioButton({ width: 180, height: 25, checked: true, theme: theme });
            $("#isImportUsers").jqxRadioButton({ width: 180, height: 25, theme: theme });
            $("#isRegisterUsersURL").jqxRadioButton({ width: 180, height: 25, theme: theme });
            $("#isCreateNewUser").on('change', function (event) {
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
            var getRegistrationUrlURL = "AdminUser?action=getRegistrationUrl";
            $.get(getRegistrationUrlURL,function(response){
            	$(".registrationURLDiv").html("<p><a href='"+ response +"'>"+ response +"</a></p>");
            	
            });
		$("#saveUserButton").click(function (event) {
			var validationResult = function (isValid) {
				if (isValid) {
					addUsersGroupFromEditor();
				}
			}
			$('#createUserForm').jqxValidator('validate', validationResult);
		});
		$('#createUserForm').jqxValidator({
					animationDuration:5,
					rules: userVlidatorRules
		});
			
			//savebutton click			
		$("#createUserForm").on('validationSuccess', function () {
			$("#createUserForm-iframe").fadeIn('fast');
		});
		$("#closeButton").click(function () {
			$('#jqxCreateBeanWindow').jqxWindow('close'); 
		});
		
	
		
		
	    $('#mainUserSplitter').jqxSplitter({ width: "100%", height: "100%", orientation: 'horizontal', theme: theme });
	    $(".userEditorArea").jqxPanel({ width:"100%", height: "100%", theme: theme });
	    
		
	});//end document ready	
</script>

	
<div id="addusersWindow" style="width:100%;height:100%">	
		  <div id="jqxCreateBeanEditor" style="height:100%;">
  		   	<div id="mainUserSplitter" style="width:100%;height:100%">
				<div class="splitter-panel">
		  		   	<div style="padding:5px;">
						<%@include file="userGridInclude.jsp"%>	
  		   			</div>
  		   		</div>
				<div class="splitter-panel userEditorArea" style="height:100%;width:100%">
					<div class="optionRadios" style="clear:both;margin-bottom:40px;">
						<div id='isCreateNewUser' style="float:left">Create new User</div>
						<div id='isImportUsers' style="float:left">Import Users from xls</div>
						<div id='isRegisterUsersURL' style="float:left">Registration URL</div>
					</div>
					<div class="tabContent1">
						<form id="createUserForm" name="createUserForm" method="POST"/>
							<input type="hidden" name="rowId" id="rowIdInput"/>
							<input type="hidden" name="userGroupSeq" id="userGroupSeqInput"/>
							<input type="hidden" name="createdOn" id="createdOnInput"/>
							<table style="overflow: hidden;">	
								<tr>
									<td>User Group Name</td>
									<td><input name="userGroupName" type="text" id="userGroupNameInput"/></td>
								
									<td>Description</td>
									<td><input name="userGroupDescription" type="text" id="userGroupDescriptionInput"/></td>
								</tr>
							</table>
							</br>
							<table style="overflow: hidden;">
								<tr>
									<td>Name</td>
									<td><input name="name" type="text" id="NameUserInput"/></td>
								</tr>
								<tr>
									<td>Mobile</td>
									<td><input name="mobile" type="text" id="mobileInput"/></td>
								</tr>
								<tr>
									<td>E-Mail</td>
									<td><input name="email" type="text" id="emailInput"/></td>
								</tr>
								<tr>
									<td>Location</td>
									<td><input name="location" type="text" id="locationInput"/></td>
								</tr>
								<tr>
									<td>Username</td>
									<td><input name="username" type="text" id="usernameInput"/></td>
								</tr>
								<tr>
									<td>Password</td>
									<td><input name="password" type="text" id="passwordInput"/></td>
								</tr>
								<tr>
									<td>Enabled</td>
									<td><div id="isEnabledInput"></div></td>
								</tr>					
								<tr>
									<td><input type="button" style='margin-top: 15px; margin-left: 50px; float: left;' value="Save" id="saveUserButton" /></td>
								</tr>
							</table>
						</form>
		           	</div>
					<div class="tabContent2" style="display:none">
							<%@include file="importUser.jsp" %>		
							
					</div>
					<div class="tabContent3" style="display:none">
							You may use this URL to get users registered under the current project.
							<div class="registrationURLDiv" style="font-size:18px;color:red"></div>
					</div>
				</div>
			</div>			
		</div>
</div>
