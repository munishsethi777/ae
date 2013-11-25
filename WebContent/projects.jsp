<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript">
    	var pageName = "projects";
    	var beanName = "Project";
		var dataUrl = "AdminUser?action=getAllProjects";
		var deleteUrl = "AdminUser?action=deleteProject";
		var addUrl = "AdminUser?action=addProject";
	
		var validatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#phoneInput', message: 'Phone is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#mobileInput', message: 'Mobile is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#cityInput', message: 'City is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#countryInput', message: 'Country is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#emailInput', message: 'E-mail is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#emailInput', message: 'Invalid e-mail!', action: 'keyup', rule: 'email' }];
				
		var columns = [
				
				{ text: 'Name', datafield: 'name',editable:false ,width: 190},
				{ text: 'Contact Person', datafield: 'contactPerson',editable:false,width: 140},
				{ text: 'Email', datafield: 'email' ,editable:false,width: 200},
				{ text: 'Registration URL', datafield: 'registrationUrl',editable:true},
				{ text: 'Created On', datafield: 'createdOn',editable:false,width: 140,cellsformat: 'dd-MM-yy hh.mm tt'},				
				{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width: 140,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:60}]				
				

		var dataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'name', type: 'string' },
				{ name: 'description', type: 'string' },
				{ name: 'email', type: 'string' },
				{ name: 'phone', type: 'string' },
				{ name: 'mobile', type: 'string' },
				{ name: 'contactPerson', type: 'string' },
				{ name: 'address', type: 'string' },
				{ name: 'city', type: 'string' },
				{ name: 'country', type: 'string' },
				{ name: 'createdOn', type: 'date' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'lastmodifieddate', type: 'date'},
				{ name: 'registrationUrl', type: 'string'}
				];
		
		
		$(document).ready(function () {
				var editorWidth= "550px";
				var editorHeight = "500px";
				renderGrid("jqxGrid",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,true,editorHeight,editorWidth);
				$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
		});//end document ready	

	
    </script>
</head>
<body class='default'>
	<%@ include file="header.jsp" %>
	<%@ include file="menu.jsp" %>
	<%@ include file="grid.jsp" %>
    <div id='jqxWidget'>
		<label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">Projects Information</label><br>
		<label style="font-family:verdana;font-size: 12px;color:grey">View, Create, Edit, Bulk Delete or Find through various users available in the database.</label>
        <div id="jqxGrid"></div>
		<div id="jqxCreateBeanWindow">
           <div class="title" style="font-weight:bold">Create New Project</div>
           <div id="jqxCreateBeanEditor">
           	<div class="editorErrorDiv"></div>
			<div class="editorSuccessDiv"></div>
		   	<form id="createBeanForm" name="createBeanForm"/>
					<input type="hidden" name="rowId" id="rowIdInput"/>
					<input type="hidden" name="seq" id="seqInput"/>
					<input type="hidden" name="createdOn" id="createdOnInput"/>
					<input type="hidden" name="registrationUrl" id="registrationUrlInput"/>
					
					
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
						<td>E-Mail</td>
						<td><input name="email" type="text" id="emailInput"/></td>
					</tr>
					<tr>
						<td>Phone</td>
						<td><input name="phone" type="text" id="phoneInput"/></td>
					</tr>
					<tr>
						<td>Mobile</td>
						<td><input name="mobile" type="text" id="mobileInput"/></td>
					</tr>
					<tr>
						<td>Contact Person</td>
						<td><input name="contactPerson" type="text" id="contactPersonInput"/></td>
					</tr>
					<tr>
						<td>Address</td>
						<td><input name="address" type="text" id="addressInput"/></td>
					</tr>
                    <tr>
						<td>City</td>
						<td><input name="city" type="text" id="cityInput"/></td>
					</tr>
                    <tr>
						<td>Country</td>
						<td><input name="country" type="text" id="countryInput"/></td>
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
		</div>
		
	
	
	</div>
</body>
</html>
