<script>
	var isPublishable = true;
	$(document).ready(function () {
		$("#publishCampaign").jqxButton({ width: 160, theme: theme , height:30, disabled:false});
		$("#publishCampaign").on('click', function(event) {
			publishCampaign();
		});
	});
	function previewCampaign(){
		isPublishable = true;
		generateSelectedGamesAndUserGroupDiv();
	}
	function publishCampaign(){
		$.getJSON("AdminUser?action=publishCampaign&campaignSeq="+ getCampaignSeqFromForm(),function(json){
			if(json.status == "success"){
				alert('published sucessfully');
			}
		});
	}
	
	function generateSelectedGamesAndUserGroupDiv(){
		$(".selectedGamesPreviewDiv").html("<img src='images/loading.gif'/>");
		$.getJSON("AdminUser?action=getCampaignForPreview&campaignSeq="+ getCampaignSeqFromForm(),function(json){
			//preview campaign details
			setCampaignDetails(json.campaign);
			
			//preview games divs
			$(".selectedGamesPreviewDiv").html("");
			$(json.games).each(function() {
				content = getGamesPreviewDiv(this);
				$(content).appendTo(".selectedGamesPreviewDiv");
			});
			
			//preview usergroup div
			$(".selectedUserGroupPreviewDiv").html("");
			content = getUserGroupPreviewDiv(json.userGroup);
			$(".selectedUserGroupPreviewDiv").html(content);
			
			//publish button enable/disable
			if(isPublishable == false){
				$("#publishCampaign").jqxButton({disabled:true});
			}else{
				$("#publishCampaign").jqxButton({disabled:false});
			}
		});
	}
	function setCampaignDetails(json){
		if(json.name != ""){
			$("#previewCampaignName").html(json.name);
		}
		if(json.description != ""){
			$("#previewCampaignDescription").html(json.description);
		}
	}
	function getUserGroupPreviewDiv(json){
		var content = "";
		if(json.status == "failure"){
			content += "<label>"+ json.message +"</label>";
			content += "<br><label style='color:red'>Please select or create a usergroup to publish the campaign.</label>";
			isPublishable = false;
		}else{
			content += "<label>"+ json.name +"</label><br>";
			content += "<label>"+ json.description +"</label>";
		}
		return content;		
	}
	function getGamesPreviewDiv(json){
		var content= "";
		content += "<div class='selectGameTemplatesDiv'>";
			content += "<div class='gameIcon'>";
				content += "<img src='" + json.imagePath +"'/>";
			content += "</div>";
			content += "<div class='gameTemplateDetailsDiv'>";
				content += "<div style='float:right' id='earlierGameSeqRadio"+ json.seq +"'></div>";
				content += '<label style="font-size:18px;display:block">' + json.gameTitle + '</label>';
			   	content += '<div class="smallFonts" style="height:70px;">' + json.gameDescription + '</div>';
			   	content += '<div class="smallFonts" style="margin-top:5px;color:blue">Questions added: ' + json.totalQuestions + ' of ' + json.maxQuestions +'</div>';
				if(json.status == "incompleted"){
					content += '<div class="smallFonts" style="margin-top:5px;color:red">Insufficient questions added to publish campaign.</div>';
					isPublishable = false;
				}
			   	content += '<div style="margin-top:10px;">';
					content += "<input value='Questions' type='button' id='addQuestionLink"+json.seq+"' class='marL10' style='display:none;margin-left:10px;float:right'/>";
				content += '</div>';
			content += "</div>";
			content += "<br class='clr'>";
		content += "</div>";
	   	
		return content;
	}
</script>
<div style="height:400px;overflow:scroll">
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td>
    	<label style="font-size:20px" id="previewCampaignName">Campaign for New Joining Employees</label><br>
	    <label style="font-size:14px" id="previewCampaignDescription">Campaign description goes here in the smaller fonts</label>
	</td>
	<td align="right">
		<input value='Publish Campaign' type='button' id='publishCampaign'/>
		
	</td>
  </tr>
</table>

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td class="selectedGamesPreviewDiv">

    </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td class="selectedUserGroupPreviewDiv">

    </td>
  </tr>
</table>
</div>