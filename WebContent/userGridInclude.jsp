<script type="text/javascript">
		var pageUserName = "users";
		var beanUserName = "User";
		var dataUserUrl = "AdminUser?action=getAllUsers" ;
		var deleteUserUrl = "AdminUser?action=deleteUser";
		var addUserUrl  = "AdminUser?action=addUser";
		
		
		var userValidatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#usernameInput', message: 'Username is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#passwordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#emailInput', message: 'Enter valid email!', action: 'keyup, blur', rule: 'email' },
				{ input: '#mobileInput', message: 'Mobile is required!', action: 'keyup, blur', rule: 'required' }];
	
		var userColumns = [
				{ text: 'First Name', datafield: 'name' ,editable:false,width:"20%"},
				{ text: 'Email Id', datafield: 'email' ,editable:false,width: "20%"},
				{ text: 'UserName', datafield: 'username',editable:false,width: "20%"},
				{ text: 'Location', datafield: 'location' ,editable:false},
				{ text: 'Signup Date', datafield: 'createdOn',editable:false,width: 150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width: 80}];
		var userDataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'userGroupSeq', type: 'integer'},
				{ name: 'userGroupName', type: 'string' },
				{ name: 'userGroupDescription', type: 'string' },
				{ name: 'name', type: 'string' },
				{ name: 'email', type: 'string' },
				{ name: 'mobile', type: 'string' },
				{ name: 'location', type: 'string' },
				{ name: 'username', type: 'string' },
				{ name: 'password', type: 'string' },
				{ name: 'createdOn', type: 'date' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'lastmodifieddate', type: 'date'}
				];
		var userEditorWidth= "75%";
		var userEditorHeight = "70%";
		$(document).ready(function () {
			
			
		});//end document ready	
 </script>
 <div id="userJqxGrid"></div>