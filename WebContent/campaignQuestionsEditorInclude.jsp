<!--  all main methods are in "js_creategame.jsp". see this file, how template block generate and question related methods. -->
<script type="text/javascript">
//isCampaignUI variable used in importQuestion.jsp. for check if importQuestions is using from campaign ui 
var isCampaignUI = true;
//updateSelectedQuestionGrid is used in importQuestion.jsp for add the imported row in selectedQuestion grid.
$(document).ready(function () {
    $(".saveQuestionButton").attr("id", "saveQuestionButtonClick");
	$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
	$(".saveQuestionButton").jqxButton({ width: 70, theme: theme });
	$(".closeQuestionButton").jqxButton({ width: 70, theme: theme });
		
	
	var validatorRules = [
				{ input: '#quesTitleInput', message: 'Question is required!', action: 'keyup, blur', rule: 'required' }];
	$("#saveQuestionButtonClick").click(function (event) {
		var validationResult = function (isValid) {
		if (isValid) {
			addQuestionAndGame();
		}
	}
		$('#createQuestionForm').jqxValidator('validate', validationResult);
	});
	$('#createQuestionForm').jqxValidator({
		animationDuration:5,
		rules: validatorRules
	});
			
	//savebutton click			
	$("#createQuestionForm").on('validationSuccess', function () {
		$("#createQuestionForm-iframe").fadeIn('fast');
	});
	$("#closeButton").click(function () {
		$('#jqxCreateBeanWindow').jqxWindow('close'); 
	});
	
	$("#isCreateQuestion").jqxRadioButton({groupName:'saveOptions', width: 180, height: 25, checked: true, theme: theme });
	$("#isSelectQuestion").jqxRadioButton({groupName:'saveOptions', width: 180, height: 25, theme: theme });
	$("#isImportQuestions").jqxRadioButton({ groupName:'saveOptions',width: 180, height: 25, theme: theme });
    $("#isCreateQuestion").on('change', function (event) {
    	$(".tabContent1").show();
    	$(".tabContent2").hide();
    	$(".tabContent3").hide();
    });
    $("#isSelectQuestion").on('change', function (event) {
    	$(".tabContent1").hide();
    	$(".tabContent2").show();
    	$(".tabContent3").hide();
    });
    $("#isImportQuestions").on('change', function (event) {
    	$(".tabContent1").hide();
    	$(".tabContent2").hide();
    	$(".tabContent3").show();
   });
    //$("#selectedQuestionsExpander").jqxExpander({ width: '100%', theme: theme });
    $('#mainSplitter').jqxSplitter({ width: "100%", height: "100%", orientation: 'horizontal', theme: theme, panels: [{ size: 200 }, { size: 300}] });
    $(".editorArea").jqxPanel({ width:"100%", theme: theme });
     
});//end document ready

</script>
<div id="addQuestionsWindow">
           <div class="title" style="font-weight:bold">Create New Question</div>
  		   <div id="jqxCreateBeanEditor" style="overflow: hidden;">
  		   		<div id="mainSplitter">
	  		   		<div class="splitter-panel selectQuestionsDiv"><!-- Selected Questions Area -->
		  		   		<%@include file="campaignSelectedQuestions.jsp"%> 
  		   			</div>
  		   			
  		   			<div class="splitter-panel editorArea" style="height:100%"><!-- Editor Area -->
				   		<div class="saveOptions" style="clear:both;margin-bottom:40px;margin-top:10px;">
							<div id='isCreateQuestion' style="float:left">Create new question</div>
							<div id='isSelectQuestion' style="float:left">Select questions</div>
							<div id='isImportQuestions' style="float:left">Import from xls</div>
						</div>
				   		<div class="tabContent1">
							<form id="createQuestionForm" name="createQuestionForm" method="POST"/>
								<%@include file="questionFormInclude.jsp"%>
							</form>
						</div>
						<div class="tabContent2">
							<p>Please download the <a href="#">sample file</a> and fill in questions to upload on server.</p>
							<p><input type = "file"></p>
						</div>
						<div class="tabContent3" style="display:none">
							<%@ include file="importQuestions.jsp" %>
						</div>
					</div>
					
				</div><!-- Ends Splitter here -->	
			</div><!-- Crate Bean Editor Ends -->
</div><!-- Add Question Window ends -->

