<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
  	<html>
  	<head>
  		<%@ include file="header.jsp" %>
		<%@ include file="grid.jsp" %>
		<script type="text/javascript">
			$(document).ready(function () {
				$('#submitButton').on('click', function () {
					var validationResult = function (isValid) {
						if (isValid) {
							$('#frm1').submit();
						}
					}
					$('#frm1').jqxValidator('validate', validationResult);
				});
			
				$('#frm1').jqxValidator({
						animationDuration:5,
						rules: [{ input: '#emailId', message: 'Email is required!', action: 'keyup, blur', rule: 'required' },
								{ input: '#userName', message: 'User Name is required!', action: 'keyup, blur', rule: 'required' },
								{ input: '#password', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
								{ input: '#cpassword', message: 'Confirm Password is required!', action: 'keyup, blur', rule: 'required' },
			                    { input: '#cpassword', message: 'Passwords doesn\'t match!', action: 'keyup, focus', rule: function (input, commit) {
			                        // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
			                        if (input.val() === $('#password').val()) {
			                            return true;
			                        }
			                        return false;
			                    }
			                    },		
						] ,            
				});
				
			});
		</script> 	
  	</head> 
  	<body>
  	<table align="center" width="40%" border="0" style="margin-top:10px">
     <%	Admin admin =(Admin) request.getAttribute("registeringAdmin"); %>  		
       		<%	List<String> errMessages =(List<String>) request.getAttribute("errMessages"); %>
			<c:if test="${errMessages != null}">
				<tr>       
        		<td style="padding:10px 10px 10px 10px;" class="ui-state-error">
					<c:forEach items="${errMessages}" var="msg">
						<c:out value="${msg}" /> </br>
					</c:forEach>
				</td>
       			</tr>
			</c:if>

		  
      <tr>
        <td class="ui-widget-header" style="padding:10px 10px 10px 10px;">New Admin Signup Form</td>
        </tr>
      <tr>
        <td class="ui-widget-content">
            <form name="frm1" id="frm1" method="post" action="AdminUser?action=signup">        
                <table width="100%" border="0" style="padding:10px 10px 10px 10px;">
                  <tr>
                    <td width="22%">Email Id</td>
                    <td width="78%"><input name="emailId" id="emailId" type="text" size="30" value='<c:out value="${admin.emailId}" />'></td>
                  </tr>
                  <tr>
                    <td width="22%">Username</td>
                    <td width="78%"><input name="userName" id="userName" type="text" size="30" value='<c:out value="${admin.username}" />'></td>
                  </tr>
                  <tr>
                    <td width="22%">Password</td>
                    <td width="78%"><input name="password" id="password" type="password" size="30"></td>
                  </tr>
                  <tr>
                    <td width="22%">Confirm Password</td>
                    <td width="78%"><input name="cpassword" id="cpassword" type="password" size="30"></td>
                  </tr>
                  
                  <tr>
                    <td width="22%">Full Name</td>
                    <td width="78%"><input name="fullName" id="fullName" type="text" size="30" value='<c:out value="${admin.name}" />'></td>
                  </tr>
                  <tr>
                    <td width="22%">Mobile</td>
                    <td width="78%"><input name="mobile" id="mobile" type="text" size="30" value='<c:out value="${admin.mobile}" />'></td>
                  </tr>
                  <tr>
                    <td width="22%">City</td>
                    <td width="78%"><input name="city" id="city" type="text" size="30" value='<c:out value="${admin.city}" />'></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td>
                    	<input type="button" name="submitButton" id="submitButton" value="Register" />
                        <input type="reset" name="Reset" value="Reset">
					</td>
                  </tr>
                </table>
              </form> 
         </td>
        </tr>
        
    </table>
    </body>
</html>