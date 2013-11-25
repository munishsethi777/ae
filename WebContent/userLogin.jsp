<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
  	<html>
  	<head>
  		<%@ include file="header.jsp" %> 
  	</head> 
  	<body>    
    <table align="center" width="40%" border="0" style="margin-top:20px">
       		
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
        <td class="ui-widget-header" style="padding:10px 10px 10px 10px;">User Login</td>
        </tr>
      <tr>
        <td class="ui-widget-content">
            <form name="frm1" method="post" action="User?action=login">        
                <table width="100%" border="0" style="padding:10px 10px 10px 10px;">
                   <tr>
                    <td width="22%">Username :</td>
                    <td width="78%"><input name="username" type="username" size="30">
                      &nbsp;</td>
                  </tr>
                  <tr>
                    <td width="22%">Password :</td>
                    <td width="78%"><input name="password" type="password" size="30">
                      &nbsp;</td>
                  </tr>
                  
                  <tr>
                    <td>&nbsp;</td>
                    <td><input type="submit" name="submit" value=" Login " />
                         
                        <input type="reset" name="Reset" value="Reset">
                        
                    
                    </td></tr>
                     <tr>
                    <td>&nbsp;</td>
                    <td><a href="forgotPassword.php5">Forgot Password</a>
                    </td>
                  </tr>
                </table>
              </form> 
         </td>
        </tr>
        
    </table>

    
    
    
    
    </Div>
     
       <script language="javascript">
    function submitform()
    {
        if(document.frm1.adminPassword.value=="")
        {
            alert("enter the password");
            return false;
        }
        else
        {
            return true;
        }

    }
</script>

    </body>
</html>


