<%@page import="com.satya.IConstants"%>
<%@ page import ="com.satya.BusinessObjects.*"%>
<%
	Admin loggedInAdmin = (Admin)session.getAttribute(IConstants.loggedInAdmin);
	Project adminWorkspaceProject = (Project)session.getAttribute(IConstants.adminWorkspaceProject);
%>
<table style="width:100%;height:50px;" class="ui-widget-header">
	<tr>
    	<td style="padding:10px;">
        	<h1>Assessment Engine for Trainers/Institutions/Corporates</h1>
        </td>
        <td style="font-size:12px;width:350px;">
        	 <% if (loggedInAdmin != null){ %>
			 		Welcome Admin:<label style="font-weight:bold"> <%=loggedInAdmin.getName()%> (<%=loggedInAdmin.getUsername()%>)</label>
			 	 	
			 	 	<a class="button" style="margin-left:10px;padding:3px;" href="AdminUser?action=logout">Logout</a>
					
			 	 	<br/>
            		<%if(loggedInAdmin.getProjects()!=null && 
            				loggedInAdmin.getProjects().size() > 0 ){	
            			
            		%>
   
					  	Current Project:
            			<select id="workspaceProjectSelect" style="font-size:10px;margin-top:5px;" onChange="changeProjectSession()">
					 		<c:forEach items="${loggedInAdmin.projects}" var="project">
								<option value="${project.seq}" ${adminWorkspaceProject.seq == project.seq ? 'selected' : ''}><c:out value="${project.name}" /></option>
			            	</c:forEach>
					    </select>
					<%}else{
					
					}%>        		
            		
            			
			<%}%>
        	
        </td>
    </tr>
</table>