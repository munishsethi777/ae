
	<script type="text/javascript">
		$(document).ready(function () {
			$("#nameInput").jqxInput({	placeHolder : "enter your full name", height : 25, width : 500,	minLength : 1, maxLength : 256});
			$("#usernameInput").jqxInput({	placeHolder : "enter username", height : 25, width : 500,	minLength : 1, maxLength : 256});
			$("#passwordInput").jqxInput({	placeHolder : "", height : 25, width : 500,	minLength : 1, maxLength : 256});
			$("#emailInput").jqxInput({	placeHolder : "", height : 25, width : 500,	minLength : 1, maxLength : 256});
			$("#mobileInput").jqxInput({	placeHolder : "", height : 25, width : 500,	minLength : 1, maxLength : 256});
			$("#locationInput").jqxInput({	placeHolder : "enter your location", height : 25, width : 500,	minLength : 1, maxLength : 256});
			
			
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
		});//end document ready	
    </script>

	
	
		<div id="jqxCreateBeanEditor">
			<div class="editorErrorDiv"></div>
			<div class="editorSuccessDiv"></div>
			
			<div class="optionRadios" style="clear:both;margin-bottom:40px;">
				<div id='isCreateNewUser' style="float:left">Create new User</div>
				<div id='isImportUsers' style="float:left">Import Users from xls</div>
				<div id='isRegisterUsersURL' style="float:left">Registration URL</div>
			</div>
			<p style="clear:both"></p>
			<div class="tabContent1">
				<form id="createBeanForm" name="createBeanForm" method="POST"/>
					<input type="hidden" name="rowId" id="rowIdInput"/>
					<input type="hidden" name="seq" id="seqInput"/>
					<input type="hidden" name="createdOn" id="createdOnInput"/>
					<table style="overflow: hidden;">
						<tr>
							<td>Name</td>
							<td><input name="name" type="text" id="nameInput"/></td>
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
							<td><input type="button" style='margin-top: 15px; margin-left: 50px; float: left;' value="Save" id="saveButton" /></td>
							<td><input type="button" style='margin-left: 5px; margin-top: 15px; float: left;' value="Close" id="closeButton" /></td>
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

