<style type="text/css">
	.menu a{
		background-color:#EEE;
		font-family:verdana;
		font-size:12px;
		color:grey;
		padding:4px 10px 4px 10px;
		border:white solid thin;
		text-decoration:none;
		margin-right:10px;
		line-height:10px;
	}
	.menu a:hover,.menu .selected{
		background-color:white;
		color:grey;	
	}
</style>
<script>
	$(document).ready(function () {
		$("#"+pageName).addClass("ui-widget-header");	
	});
</script>
<table style="width:100%;background:#cfdde9;margin-bottom:15px">
	<tr>
    	<td style="background:#808080;padding:10px;height:20px;" colspan="2">
        	<div class="menu">
                <a id="dashboard" href="dashboard.jsp">Dashboard</a>
                <a id="projects" href="projects.jsp">Projects</a>
                <a id="users" href="users.jsp">Users</a>
                <a id="userGroups" href="usergroups.jsp">UserGroups</a>
                <a id="questions" href="questions.jsp">Questions</a>
                <a id="gameTemplates"href="gameTemplates.jsp">Game Templates</a>
                <a id="games" href="games.jsp">Games</a>
                <a id="campaigns" href="campaigns.jsp">Campaigns</a>
                <a id="results" href="results.jsp">Results</a>
                <a id="analytics" href="#">Analytics</a>
                <a id="profile" href="myAccount.jsp">Profile</a>
           	</div>
       	</td>
    </tr>
</table>