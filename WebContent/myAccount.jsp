<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<%@page import="com.satya.IConstants"%>
<%@ page import ="com.satya.BusinessObjects.*"%>
  	<html>
  	<head>
  		<%@ include file="header.jsp" %>
  		<%@ include file="menu.jsp" %>
		<%@ include file="grid.jsp" %>
		<script type="text/javascript">
			pageName = "profile";
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
						rules: [{ input: '#emailId', message: 'Email is required!', action: 'keyup, blur', rule: 'required' }		
						] ,            
				});
				
			});
		</script> 	
  	</head> 
  	<body>
  	<table align="center" width="40%" border="0" style="margin-top:10px">
     <%	Admin admin = (Admin)session.getAttribute(IConstants.loggedInAdmin); %>  		
     <%List<String> errMessages =(List<String>) request.getAttribute("errMessages"); %>
	  <%List<String> sccMessages =(List<String>) request.getAttribute("sccMessages"); %>
			<c:if test="${errMessages != null}">
				<tr>       
        		<td style="padding:10px 10px 10px 10px;" class="ui-state-error">
					<c:forEach items="${errMessages}" var="msg">
						<c:out value="${msg}" /> </br>
					</c:forEach>
				</td>
       			</tr>
			</c:if>
			<c:if test="${sccMessages != null}">
				<tr>       
        		<td style="padding:10px 10px 10px 10px;" class="ui-state-message">
					<c:forEach items="${sccMessages}" var="msg">
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
            <form name="frm1" id="frm1" method="post" action="AdminUser?action=updateAccount">        
                <table width="100%" border="0" style="padding:10px 10px 10px 10px;">
                  <tr>
                    <td width="22%">Email Id</td>
                    <td width="78%"><input name="emailId" id="emailId" type="text" size="30" value='<c:out value="<%=admin.getEmail()%>" />'></td>
                  </tr>
                  <tr>
                    <td width="22%">Full Name</td>
                    <td width="78%"><input name="fullName" id="fullName" type="text" size="30" value='<c:out value="<%=admin.getName()%>" />'></td>
                  </tr>
                  <tr>
                    <td width="22%">Mobile</td>
                    <td width="78%"><input name="mobile" id="mobile" type="text" size="30" value='<c:out value="<%=admin.getMobile()%>" />'></td>
                  </tr>
                  <tr>
                    <td width="22%">City</td>
                    <td width="78%"><input name="city" id="city" type="text" size="30" value='<c:out value="<%=admin.getCity()%>" />'></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td>
                    	<input type="button" name="submitButton" id="submitButton" value="Update" />
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