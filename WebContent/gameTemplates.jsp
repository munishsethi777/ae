<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
<script type="text/javascript">
	$(document).ready(function(){
		var dataUrl = "AdminUser?action=getAllGameTemplates";
		
		$.getJSON(dataUrl,function(JSON){
			$.each(JSON, function(index,val){
				content = getGameTemplateDiv(index,val);
	    		$(content).appendTo(".gameTemplatesBlock");
	    	});
	       });
		function getGameTemplateDiv(index,json){
			var content="";
			content += "<div class='gameTemplateDiv rounded'>";
				content += "<div class='gameIcon'>";
					content += "<img src='" + json.imagePath +"'/>";
				content += "</div>";
				content += "<div class='gameTemplateDetailsDiv'>";
					content += '<h1>' + json.name + '</h1>';
			   		content += '<h2>' + json.description + '</h2>';
			   		content += '<p>';
			   			content += "<a href='#' class='link'>More Details</a>";
			   			content += "<a href='#' class='link marL10'>Demo</a>";
			   		content += '</p>';
			   	content += "</div>";
		   		content += "<br class='clr'>";
	   		content += '</div>';
	   		return content;
		}
	});
</script>
</head>
<body class='default'>
	<%@ include file="header.jsp" %>
	<%@ include file="menu.jsp" %>
    <div id='jqxWidget'>
		<h1>Games Templates</h1>
		<h2>Look at the available games templates that you may use to create your custom games with your choice of questions. Each 
		game has a different look and feel. So pick the one you feel is best for your need.</h2>
		
		<div class="gameTemplatesBlock">
		
		</div>
		
	</div>
</body>
</html>
