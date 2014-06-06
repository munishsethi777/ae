<script>
	$(document).ready(function() {
		$("#isAnswer1CorrectInput").jqxRadioButton({
			width : 50,
			height : 25,
			checked : true,
			theme : theme
		});
		$("#isAnswer2CorrectInput").jqxRadioButton({
			width : 50,
			height : 25,
			theme : theme
		});
		$("#isAnswer3CorrectInput").jqxRadioButton({
			width : 50,
			height : 25,
			theme : theme
		});
		$("#isAnswer4CorrectInput").jqxRadioButton({
			width : 50,
			height : 25,
			theme : theme
		});

		$("#isAnswer1CorrectInput").on('change', function(event) {
			$("#isAnswerCorrectInput").val("1");
		});
		$("#isAnswer2CorrectInput").on('change', function(event) {
			$("#isAnswerCorrectInput").val("2");
		});
		$("#isAnswer3CorrectInput").on('change', function(event) {
			$("#isAnswerCorrectInput").val("3");
		});
		$("#isAnswer4CorrectInput").on('change', function(event) {
			$("#isAnswerCorrectInput").val("4");
		});
		
		$("#quesTitleInput").jqxInput({	placeHolder : "enter a question", height : 25, width : 500,	minLength : 1, maxLength : 256});
		$(".questionForm #descriptionInput").jqxInput({placeHolder : "enter description for the question", height : 25, width : 600,	minLength : 1, maxLength : 256});
		$("#hintInput").jqxInput({placeHolder : "provide hints for the question", height : 25, width : 600,	minLength : 1, maxLength : 256});
		$("#pointsInput").jqxInput({ height : 25, width : 38,	minLength : 1, maxLength : 10});
		$("#answer1Input").jqxInput({placeHolder : "possible answer for the question", height : 25, width : 400,	minLength : 1, maxLength : 256});
		$("#answer2Input").jqxInput({placeHolder : "possible answer for the question", height : 25, width : 400,	minLength : 1, maxLength : 256});
		$("#answer3Input").jqxInput({placeHolder : "possible answer for the question", height : 25, width : 400,	minLength : 1, maxLength : 256});
		$("#answer4Input").jqxInput({placeHolder : "possible answer for the question", height : 25, width : 400,	minLength : 1, maxLength : 256});
		
		//$("#answer1FeedbackInput").jqxInput({placeHolder : "feedback for answer 1", height : 20, width : 600,	minLength : 1, maxLength : 256});
		//$("#answer2FeedbackInput").jqxInput({placeHolder : "feedback for answer 2", height : 20, width : 600,	minLength : 1, maxLength : 256});
		//$("#answer3FeedbackInput").jqxInput({placeHolder : "feedback for answer 3", height : 20, width : 600,	minLength : 1, maxLength : 256});
		//$("#answer4FeedbackInput").jqxInput({placeHolder : "feedback for answer 4", height : 20, width : 600,	minLength : 1, maxLength : 256});
		
		
		$("#negativePointsInput").jqxInput({height : 25, width : 50,	minLength : 1, maxLength : 256});
		$("#maxSecondsAllowedInput").jqxInput({height : 25, width : 50,	minLength : 1, maxLength : 256});
		$("#extraAttemptsAllowedInput").jqxInput({height : 25, width : 50,	minLength : 1, maxLength : 256});
		$("#isEnabledQuestionInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
		
	});
</script>

<input type="hidden" name="rowId" id="rowIdInput" />
<input type="hidden" name="seq" id="seqInput" />
<input type="hidden" name="createdOn" id="createdOnInput" />
<input name="isAnswerCorrect" type="hidden" id="isAnswerCorrectInput" />
<table width="100%" class="formTable questionForm">
	<tr>
		<td style="width: 12%" class="marginTop">Question</td>
		<td><input name="quesTitle" type="text" id="quesTitleInput" />
			Points <input name="points" type="text" id="pointsInput" />
		</td>
	</tr>
	<tr>
		<td class="marginTop">Description</td>
		<td><input name="description" type="text"
			id="descriptionInput" /></td>
	</tr>
	<tr>
		<td class="marginTop">Question Hint</td>
		<td><input name="hint" type="text"
			id="hintInput" /></td>
	</tr>
	<tr>
		<td class="marginTop">Answer 1</td>
		<td>
			<input name="answer1" size="33" type="text" id="answer1Input" style="display:inline-table"/>
			<div id='isAnswer1CorrectInput' style="display: inline-table"></div>
			<!-- <input class="marginTop" name="answer1Feedback" type="text" id="answer1FeedbackInput"/>	 -->
			
			<span>Answer 2</span>
			<input name="answer2" size="33" type="text" id="answer2Input" style="display:inline-table"/>
			<div id='isAnswer2CorrectInput' style="display:inline-table"></div>
		</td>
	</tr>

	<tr>
		<td class="marginTop">Answer 3</td>
		<td>
			<input name="answer3" size="33" type="text" id="answer3Input" style="display:inline-table"/>
			<div id='isAnswer3CorrectInput' style="display: inline-table"></div>
			<!-- <input class="marginTop" name="answer1Feedback" type="text" id="answer1FeedbackInput"/>	 -->
			
			<span>Answer 4</span>
			<input name="answer4" size="33" type="text" id="answer4Input" style="display:inline-table"/>
			<div id='isAnswer4CorrectInput' style="display:inline-table"></div>
		</td>
	</tr>

	<tr>
		<td class="marginTop">
			Negative Points
		</td>
		<td>
			<input name="negativePoints" type="text" id="negativePointsInput" />

			<div style="margin-left:17px;display:inline-table;">Maximum Time Allowed (seconds)
			<input name="maxSecondsAllowed" type="text" id="maxSecondsAllowedInput" /></div>

			<div style="margin-left:17px;display:inline-table;">Extra Attempts Allowed
			<input name="extraAttemptsAllowed" type="text" id="extraAttemptsAllowedInput" /></div>
		</td>
	</tr>
	<tr>
		<td class="marginTop">Enabled</td>
		<td><div name="isEnabledQuestion" id="isEnabledQuestionInput"></div></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<input type="button" class="saveQuestionButton"
			style='margin-top: 15px; margin-left: 50px; float: left;'
			value="Save"/> 
			
			<input type="button"
			class="closeQuestionButton"
			style='margin-left: 5px; margin-top: 15px; float: left;'
			value="Close" id="closeButton" /></td>
	</tr>
</table>