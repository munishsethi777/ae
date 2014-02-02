<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<html lang="en">
<head>
	<script type="text/javascript">
		var pageName = "users";
		var beanName = "User";
		var dataUrl = "AdminUser?action=getAllUsers";
		var deleteUrl = "AdminUser?action=deleteUser";
		var addUrl = "AdminUser?action=addUser";
		
		var validatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#usernameInput', message: 'Username is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#passwordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#emailInput', message: 'Enter valid email!', action: 'keyup, blur', rule: 'email' },
				{ input: '#mobileInput', message: 'Mobile is required!', action: 'keyup, blur', rule: 'required' }];
	
		var columns = [
				{ text: 'First Name', datafield: 'name' ,editable:false,width:"20%"},
				{ text: 'Email Id', datafield: 'email' ,editable:false,width: "20%"},
				{ text: 'UserName', datafield: 'username',editable:false,width: "20%"},
				{ text: 'Location', datafield: 'location' ,editable:false,},
				{ text: 'Signup Date', datafield: 'createdOn',editable:false,width: 150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width: 150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width: 80}];
		var dataFields = [
				{ name: 'seq', type: 'integer' },
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
		$(document).ready(function () {
			var editorWidth= "75%";
			var editorHeight = "70%";
			renderGrid("jqxGrid","jqxCreateBeanWindow",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,true,editorHeight,editorWidth);
		});//end document ready	
    </script>
</head>
<body class='default'>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<%@ include file="grid.jsp" %>
<div id='jqxWidget'>
	<label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">Users Information</label><br>
	<label style="font-family:verdana;font-size: 12px;color:grey">View, Create, Edit, Bulk Delete or Find through various users available in the database.</label>
	<div id="jqxGrid"></div>
	<div id="jqxCreateBeanWindow">
		<%@ include file="usersEditorInclude.jsp" %>
	</div>
</div>
</body>
</html>
