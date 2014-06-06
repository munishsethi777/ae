<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<html>
<style>
	#gameDiv{
		width:98%;
		border:1px silver solid;
		float:left;
		padding:8px;
		margin:4px;
		clear:both
	}
	.gameName{
		font-size:26px;
		padding:8px 0px 16px 0px;
	}
	.gameDescription{
		font-size:18px;
		display:block;
		margin-bottom:20px;
	}
	.otherDetails label{
		margin-right:30px;
	}
	.gameLink{
		font-size:14px;
		display:block;
	}
	.gameImage{
		float:left;
		margin-right:8px;
		border:1px silver solid;
	}
</style>
<body class='default'>
		<%@ include file="userHeader.jsp" %>
		<%	String campaignSeq = request.getParameter("campaignSeq");%>
		<script>
			function getGameDiv(game){
				var str = "";
				str += "<Div id='gameDiv'>";
				str += "<img src='"+ game.imagePath +"' class='gameImage'/>";
				str += "<Div class='gameName'>"+ game.gameTitle +"</Div>";
				str += "<Div class='gameDescription'>"+ game.gameDescription +"</Div>";
				str += "<Div class='otherDetails'>";
				str += 	"<label>Total Questions : "+ game.totalQuestions +" Questions</label>";
				var timeAllowed = "unlimited";
				if(game.gameMaxSecondsAllowed > 0){
					timeAllowed=game.gameMaxSecondsAllowed + " Seconds";
				}
				str += 	"<label>Time Allowed : "+ timeAllowed +"</label>";
				str += "</Div>";
				if(game.result != null){
					str += "<br>Score:"+ game.result.scoreEarned;
					str += "<br>Time Took:"+ game.result.timeTook;
					str += "<br>Dated:"+ game.result.playedOn;
				}else{
					str += "<a href='User?action=loadPlayer&gid="+ game.seq +"&cid="+ <%=campaignSeq%> +"' class='gameLink'>play now</a>";
					
				}
				str += "</Div>";
				return str;
			}
			$(document).ready(function () {
				$.getJSON("User?action=getGamesForCampaign&campaignSeq="+<%=campaignSeq%>,function(data){
					$.each(data,function(key,value){
						$('.setsDiv').append(getGameDiv(value));
		            });
				});
			});
		</script>
		<div style="padding:20px;">
			<Div class="setsDiv"></div>
		</div>
		
		
		
		
		
		
</body>
</html>
