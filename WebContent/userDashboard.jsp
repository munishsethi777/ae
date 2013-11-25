<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<html>
<body class='default'>
		<%@ include file="userHeader.jsp" %>
		<style>
			.campaignsDiv{
				border:1px silver solid;
				background:siver;
				padding:10px;
				width:1000px;
			}
			.campaignDiv{
				border:1px silver solid;
				padding:5px;
				margin-bottom:10px;
			}
			.campaignLink{
				display:block;
				font-size:22px;
				color:navy;
				text-decoration:none;
			}
		</style>
		<script>
		function getCampaignDiv(campaign){
			var str = "<div class='campaignDiv'>";
			str += "<a href='campaign.jsp?campaignSeq="+ campaign.seq +"' class='campaignLink'>"+ campaign.name+"</a>";
			str += campaign.description;
			str += "</br>Started On: "+ campaign.createdOn;
			str += "</br>Valid till: "+ campaign.validityDays;
			str += "</div>";
			return str;
		}
		$(document).ready(function () {
			$.getJSON("User?action=getCampaigns",function(data){
				$.each(data,function(key,value){
					$('.campaignsDiv').append(getCampaignDiv(value));
	            });
			});
		});
		</script>
		<div style="padding:20px;">
			<Div class="campaignsDiv"></div>
		</div>
		
		
		
		
		
		
</body>
</html>
