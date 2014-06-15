<!--  all main methods are in "js_creategame.jsp". see this file, how template block generate and question related methods. -->
<script type="text/javascript">
//isCampaignUI variable used in importQuestion.jsp. for check if importQuestions is using from campaign ui 
var isCampaignUI = true;
//updateSelectedQuestionGrid is used in importQuestion.jsp for add the imported row in selectedQuestion grid.
$(document).ready(function () {
	//set new id as this form is being reused
    $(".saveQuestionButton").attr("id", "saveQuestionButtonClick");
	$(".saveQuestionButton").jqxButton({ width: 70, theme: theme });
	//generate add Question jqxWindow
	$("#addQuestionsWindow").jqxWindow({ 
				resizable: true, theme: theme, autoOpen: false,  showCloseButton: true,
				resizable: true,animationType: 'fade',isModal: true,modalOpacity: 0.8});
	
	//Game Name Desc controls.
	$("#gameNameInput").jqxInput({height : 25, width : 400,	minLength : 1, maxLength : 256});
	$("#gameDescriptionInput").jqxInput({height : 25, width : 400,	minLength : 1, maxLength : 256});
		
	var addQuestionValidatorRules = 
			[
				{ input: '#quesTitleInput', message: 'Question is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '.questionForm #descriptionInput', message: 'Description is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#answer1Input', message: 'Answer 1 is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#answer2Input', message: 'Answer 2 is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#answer3Input', message: 'Answer 3 is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#answer4Input', message: 'Answer 4 is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#gameNameInput', message: 'Game Name is required!', action: 'keyup, blur', rule: 'required' },
			];
	
	$("#saveQuestionButtonClick").click(function (event) {
		//var validationResult = function (isValid) {
			//if (isValid) {
				addQuestionAndGame();
			//}
		//};
		//$('#createQuestionForm').jqxValidator('validate', validationResult);
	});
	$('#createQuestionForm').jqxValidator({
		animationDuration:5,
		rules: addQuestionValidatorRules
	});
			
	//savebutton click			
	$("#createQuestionForm").on('validationSuccess', function () {
		$("#createQuestionForm-iframe").fadeIn('fast');
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
    $('#mainSplitter').jqxSplitter({ width: "100%", height: "100%", orientation: 'horizontal', theme: theme, panels: [{ size: 300 }, { size: 200}] });
    $(".editorArea").jqxPanel({ width:"100%", theme: theme });
    
    //Saving just game name and description with top save button
    var saveGameDetailsValidationRules = 
			[
				{ input: '#gameNameInput', message: 'Game Name is required!', action: 'keyup, blur', rule: 'required' }
			];
	
    $("#saveGameDetails1").jqxButton({ width: 70, theme: theme });
	$("#saveGameDetails1").unbind();
	$("#saveGameDetails1").click(function (event) {
		var validationResult = function (isValid) {
			if (isValid) {
				saveGameDetails(gameTemplateSeq);
			}
		};
		$('#gameDetailsForm').jqxValidator('validate', validationResult);
	});
	$('#gameDetailsForm').jqxValidator({
		animationDuration:5,
		rules: saveGameDetailsValidationRules
	});
			
	//savebutton click			
	$("#gameDetailsForm").on('validationSuccess', function () {
		$("#gameDetailsForm-iframe").fadeIn('fast');
	});
	

	
});//end document ready

</script>
<div id="addQuestionsWindow">
           <div class="title" style="font-weight:bold">Create Game</div>
  		   <div id="jqxCreateBeanEditor" style="overflow: hidden;">
  		   		<div id="mainSplitter">
	  		   		<div class="splitter-panel selectQuestionsDiv"><!-- Selected Questions Area -->
	  		   			<div style="padding:2px;">
	  		   				<form id="gameDetailsForm">
	  		   					<input type="hidden" name="gameSeq" id="gameSeq"/>
	  		   					<input type="hidden" name="gameTemplateSeq" id="gameTemplateSeq"/>
			  		   			Name:<input type="text" name="gameName" id="gameNameInput" class="gameNameInput"/>&nbsp;
			  		   			Description:<input type="text" name="gameDescription" id="gameDescriptionInput" class="gameDescriptionInput"/>
			  		   			<input value='Save Game' type='button' id='saveGameDetails1'/>
			  		   			<label style="color:red;margin-left:10px" class="questionEditorErrorDiv"></label>
		  		   			</form>
	  		   			</div>
		  		   		<div id="selectedQuestionsGrid"></div>
  		   			</div>
  		   			
  		   			<div class="splitter-panel editorArea" style="height:100%"><!-- Editor Area -->
				   		<div class="saveOptions" style="clear:both;margin-bottom:40px;margin-top:10px;">
							<div id='isCreateQuestion' style="float:left">Create new question</div>
							<!-- <div id='isSelectQuestion' style="float:left">Select questions</div> -->
							<div id='isImportQuestions' style="float:left">Import from xls</div>
						</div>
				   		<div class="tabContent1">
							<form id="createQuestionForm" name="createQuestionForm" method="POST"/>
								<%@include file="questionFormInclude.jsp"%>
							</form>
						</div>
						<div class="tabContent2" style="display:none">
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

