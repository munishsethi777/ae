<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<%@ include file="grid.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>


	
</head>	
<body class='default'>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<%@ include file="grid.jsp" %>
	<form action="AdminUser?action=uploadImage" id="form1" method="POST" enctype="multipart/form-data">
		<%	List<String> errMessages =(List<String>) request.getAttribute("errMessages"); %>
		<c:if test="${errMessages != null}">
				<p style="padding:10px 10px 10px 10px;" class="ui-state-error">
				<c:forEach items="${errMessages}" var="msg">
					<c:out value="${msg}" /> </br>
				</c:forEach>
				</p>
		</c:if>
		<%	List<String> sccMessages =(List<String>) request.getAttribute("sccMessages"); %>
		<c:if test="${sccMessages != null}">
				<p style="padding:10px 10px 10px 10px;" class="ui-state-highlight">
				<c:forEach items="${sccMessages}" var="sccmsg">
					<c:out value="${sccmsg}" /> </br>
				</c:forEach>
				</p>
		</c:if>
			<p style="margin-top:10px;">Upload Image</p>
			<table width="500px" style="border:solid silver thin;margin-top:10px;">
	    		<tr>
					
	    			<td class="gameValueTD">
							<input type="file" id="attachement" name="attachement" size="50">
					</td>
					
	    		</tr>
				<tr>
    			<td class=""></td>
    			<td>
					<input type="button" id="btnSub" name="btnSub" value="Submit">
					<input type="reset" class="btn">
				</td>
    		</tr>
	    	</table>
    	</form>
		<%@ include file="imageGrid.jsp"%>
		<script type="text/javascript">
			var validatorRules = [
							{ input: '#attachement', message: 'select Image file to upload', action: 'keyup, focus', rule: function (input, commit) {
                        // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                        if ($('#attachement').val() != "") {
                            return true;
                        }
                        return false;
                    }
					}]
			$("#btnSub").click(function () {
					alert( $("#attachement").val());
					var validationResult = function (isValid) {
						if (isValid) {
							$('#form1').submit();
						}
					}
					$('#form1').jqxValidator('validate', validationResult);
				});	
				$('#form1').jqxValidator({
					animationDuration:5,
					rules: validatorRules
				});	
		</script>
	</body>
</html>