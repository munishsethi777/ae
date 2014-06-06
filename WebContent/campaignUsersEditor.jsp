<div id="jqxCreateUserBeanEditor1" style="height:100%;width:100%">
	<table style="overflow: hidden; margin: 10px;">
		<tr>
			<td>User Group Name</td>
			<td><input name="userGroupName" type="text"
				id="userGroupNameInput" /></td>

			<td>Description</td>
			<td><input name="userGroupDescription" type="text"
				id="userGroupDescriptionInput" /></td>
		</tr>
	</table>
	
	<div id="mainUserSplitter">
		<div class="splitter-panel">
			<div id="userJqxGrid"></div>
		</div>
		<div class="splitter-panel">
			
			<div id='isCreateNewUser' style="float: left">Create new User</div>
			<div id='isImportUsers' style="float: left">Import Users from xls</div>
			<div id='isRegisterUsersURL' style="float: left">Registration URL</div>
			
			<div class="userEditorErrorDiv"></div>
			<div class="userEditorSuccessDiv"></div>
			
			<div class="tabContent1" style="clear:both;padding:3px;">
				<form id="createUserForm" name="createUserForm" method="POST" />
					<input type="hidden" name="seq" id="seqInput"/>
					<input type="hidden" name="rowId" id="rowIdInput" /> 
					<input type="hidden" name="userGroupSeq" id="userGroupSeqInput" /> 
					<input type="hidden" name="createdOn" id="createdOnInput" /> </br>
					<table>
					<tr>
						<td style="width:6%">Name</td>
						<td><input name="nameUser" type="text" id="nameUserInput" /></td>
					
						<td style="width:6%">Mobile</td>
						<td><input name="mobile" type="text" id="mobileInput" /></td>
						
						<td style="width:6%">E-Mail</td>
						<td><input name="email" type="text" id="emailInput" /></td>
					</tr>
					<tr>
						<td>Location</td>
						<td><input name="location" type="text" id="locationInput" /></td>
					
						<td>Username</td>
						<td><input name="username" type="text" id="usernameInput" /></td>
					
						<td>Password</td>
						<td><input name="password" type="text" id="passwordInput" /></td>
					</tr>
					<tr>
						<td>Enabled</td>
						<td><div id="isEnabledUserInput"></div></td>
					
						<td><input type="button" value="Save User" id="addUserButton" />
						</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					</table>
				</form>
			</div>
			<div class="tabContent2" style="clear:both;display: none">
				<%@include file="importUser.jsp"%>

			</div>
			<div class="tabContent3" style="clear:both;display: none">
				You may use this URL to get users registered under the current project.
				<div class="registrationURLDiv" style="font-size: 18px; color: red"></div>
			</div>
	</div>
</div></div>