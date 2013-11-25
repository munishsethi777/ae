<%@page import="com.satya.IConstants"%>
<%@ page import ="com.satya.BusinessObjects.*"%>
<%
	User loggedInUser = (User)session.getAttribute(IConstants.loggedInUser);
%>
<table style="width:100%;height:50px;" class="ui-widget-header">
	<tr>
    	<td style="padding:10px;">
        	<h1>Assessment Engine for Trainers/Institutions/Corporates</h1>
        </td>
        <td style="font-size:12px;width:350px;">
        	 <% if (loggedInUser != null){ %>
			 		Welcome User:<label style="font-weight:bold"> <%=loggedInUser.getName()%> (<%=loggedInUser.getUsername()%>)</label>
			 	 	<a class="button" style="padding:3px;" href="userDashboard.jsp">Dashboard</a>
			 	 	<a class="button" style="margin-left:10px;padding:3px;" href="User?action=logout">Logout</a>
			<%}%>
        	
        </td>
    </tr>
</table>