<script type="text/javascript">
	function loadEarlierGames(campaignSeq){
		var dataUrl = "AdminUser?action=getAllGames&campaignSeq="+campaignSeq;
		$(".gamesBlock").html("");
		$.getJSON(dataUrl,function(JSON){
			$.each(JSON, function(index,val){
				content = getGamesDiv(index,val);
	    		$(content).appendTo(".gamesBlock");
	    		$(".gamesBlock #earlierGameSeqRadio"+val.seq).jqxCheckBox({ width: 20, height: 25, theme: theme });
				$(".gamesBlock  #earlierGameSeqRadio"+val.seq).on('change', function(event) {
					var checked = event.args.checked;
		            if (checked){
		            	$(".gamesBlock #addQuestionLink"+val.seq).show();
		            	$(".gamesBlock #selectedTemplateDivId"+val.seq).addClass('backColoredWhite');
		            }else{
		            	$(".gamesBlock #addQuestionLink"+val.seq).hide();
		            	$(".gamesBlock #selectedTemplateDivId"+val.seq).removeClass('backColoredWhite');
		            }
				});
				if(val.isSelectedOnCampaign == "true"){
					$(".gamesBlock #earlierGameSeqRadio"+val.seq).jqxCheckBox('check');
				}
				
				
				$(".gamesBlock #demo"+val.seq).jqxButton({ width: 70, theme: theme });
				$(".gamesBlock #demo"+val.seq).on('click', function(event) {
					alert("demo");
				});
				$(".gamesBlock #addQuestionLink"+val.seq).jqxButton({ width: 120, theme: theme });
				$(".gamesBlock #addQuestionLink"+val.seq).on('click', function(event){
					tempSeq = event.currentTarget.id;
					tempSeq = tempSeq.replace("addQuestionLink" , "");
					gameSeq = $(".gamesBlock #gameSeq"+tempSeq).val();
					openAddQuestionsUI(gameSeq)
					$(".gamesBlock #addQuestionsWindow").jqxWindow('open');							
				});
	    	});
	       });
	}
function getGamesDiv(index,json){
	var content= "";
	content += "<div class='selectGameTemplatesDiv' id='selectedTemplateDivId"+json.seq+"'>";
	/* content += "<input type='hidden' id='gameTemplateSeq' value='" + json.seq + "'/>"; */
	content += "<input type='hidden' id='earlierGameSeq"+json.seq +"' value='"+json.gameSeq +"'/>";
		content += "<div class='gameIcon'>";
			content += "<img src='" + json.imagePath +"'/>";
		content += "</div>";
		content += "<div class='gameTemplateDetailsDiv'>";
		content += "<div style='float:right' id='earlierGameSeqRadio"+ json.seq +"'></div>";
		content += '<h1>' + json.gameTitle + '</h1>';
	   	content += '<div class="smallFonts" style="height:70px;">' + json.gameDescription + '</div>';
	content += '<div style="margin-top:10px;">';
	content += "<input style='display:inline-table' value='Demo' type='button' id='demo"+json.seq+"'/>";
	content += "<input value='Questions' type='button' id='addQuestionLink"+json.seq+"' class='marL10' style='display:none;margin-left:10px;float:right'/>";
	content += '</div>';
	content += "</div>";
   	content += "<br class='clr'>";
		content += '</div>';
		return content;
}
	
</script>
    <div class="gamesBlock"></div>