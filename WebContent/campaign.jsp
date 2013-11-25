<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<html>
<style>
	#game{
		width:350px;
		border:1px silver solid;
		float:left;
		padding:8px;
		margin:4px;
	}
	.gameName{
		font-size:26px;
	}
	.gameLink{
		font-size:14px;
		display:block;
	}
	.gameTemplateImage{
		height:100px;
		width:100px;
		float:left;
		margin-right:8px;
		border:1px silver solid;
	}
	.setDiv{
		display:block;
		clear:both;
	}
</style>
<body class='default'>
		<%@ include file="userHeader.jsp" %>
		<%	String campaignSeq = request.getParameter("campaignSeq");%>
		<script>
			function getSetDiv(set){
				var str = "<div class='setDiv'>";
				str += "<h2>"+ set.name +"</h2>";
				var gamesCount = set.games.length;
				
				for(i = 0 ; i<gamesCount ; i++){
					str += "<Div id='game'>";
					var game = set.games[i];
					str += "<img src='"+ game.gameTemplateImagePath +"' class='gameTemplateImage'/>";
					str += "<label class='gameName'>"+ game.name +"</label>";
					if(game.resultDated != null){
						str += "<br>Score:"+ game.totalScore;
						str += "<br>Time Took:"+ game.totalTime;
						str += "<br>Dated:"+ game.resultDated;
					}else{
						str += "<a href='User?action=loadPlayer&gid="+ game.seq +"&cid="+ <%=campaignSeq%> +"&sid="+ set.seq +"' class='gameLink'>play now</a>";
						
					}
					str += "</Div>";
				}
				
				str += set.description;
				str += "</div>";
				return str;
			}
			$(document).ready(function () {
				$.getJSON("User?action=getSetsByCampaign&campaignSeq="+<%=campaignSeq%>,function(data){
					$.each(data,function(key,value){
						$('.setsDiv').append(getSetDiv(value));
		            });
				});
			});
		</script>
		<div style="padding:20px;">
			<Div class="setsDiv"></div>
		</div>
		
		
		
		
		
		
</body>
</html>
