<script type="text/javascript">
$(document).ready(function () {
	//generate earlier Questions jqxWindow
	$("#gameQuestionsWindow").jqxWindow({ 
			resizable: true, theme: theme, autoOpen: false,  showCloseButton: true,
			animationType: 'fade',isModal: true,modalOpacity: 0.8,width:700,height:400,});
});//end document ready
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
				gameSeq = $(".gamesBlock #earlierGameSeq"+val.seq).val();
				showGameQuestions(gameSeq);							
			});
    	});
     });
}
//generate the div for each game
function getGamesDiv(index,json){
	var content= "";
	content += "<div class='selectGameTemplatesDiv' id='selectedTemplateDivId"+json.seq+"'>";
	/* content += "<input type='hidden' id='gameTemplateSeq' value='" + json.seq + "'/>"; */
	content += "<input type='hidden' id='earlierGameSeq"+json.seq +"' value='"+json.seq +"'/>";
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
//display the questions of the game
function showGameQuestions(gameSeq){
	$("#gameQuestionsWindow").jqxWindow('open');
	var gameQuestionsColumns = [
			{ text: 'Title', datafield: 'title',editable:false,width:"320"  },
			{ text: 'Answers', datafield: 'answers',editable:false,width:"320"},
			{ text: 'Points', datafield: 'points',editable:false,width:"60"}
			];

	var gameQuestionDataFields = [
			{ name: 'title', type: 'integer' },
			{ name: 'answers', type: 'string' },
			{ name: 'points', type: 'integer' }
			];
	var dataUrl = "AdminUser?action=getGameQuestionsAnswers&gameSeq="+gameSeq;
	var getAdapter = function () {
		var source =
			{
			url:dataUrl,
			datatype: "json",
			datafields: gameQuestionDataFields,			
		};
		var dataAdapter = new $.jqx.dataAdapter(source);
		return dataAdapter;
	}
	$("#gameQuestionsGridDiv").jqxGrid({
		width: "100%",
		height: "100%",
		source: getAdapter(),
		theme: theme,
		altrows: true,
		enabletooltips: true,
		columns: gameQuestionsColumns,
		autorowheight: true,
        autoheight: true
	});//jqxgrid rendering
	
}


	
</script>
    <div class="gamesBlock"></div>
    <div id="gameQuestionsWindow">
    <div class="title" style="font-weight:bold">Game Questions</div>
  		   <div id="gameQuestionsGridDiv"></div>
	</div>
	
	