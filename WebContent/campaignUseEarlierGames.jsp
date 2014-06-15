<script type="text/javascript">
$(document).ready(function () {
	//generate earlier Questions jqxWindow
	$("#gameQuestionsWindow").jqxWindow({ 
			resizable: true, theme: theme, autoOpen: false,  showCloseButton: true,
			animationType: 'fade',isModal: true,modalOpacity: 0.8,width:700,height:400,});
});//end document ready

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
	
	