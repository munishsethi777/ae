<script type="text/javascript">


$(document).ready(function () {
		var validatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' }];
		$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
		$(".saveQuestionButton").jqxButton({ width: 70, theme: theme });
		
		$("#isCreateQuestion").jqxRadioButton({groupName:'saveOptions', width: 180, height: 25, checked: true, theme: theme });
           $("#isImportQuestions").jqxRadioButton({ groupName:'saveOptions',width: 180, height: 25, theme: theme });
            $("#isCreateQuestion").on('change', function (event) {
            	$(".tabContent1").show();
            	$(".tabContent2").hide();
            });
            $("#isImportQuestions").on('change', function (event) {
            	$(".tabContent1").hide();
            	$(".tabContent2").show(); 
           });

          
});//end document ready	
  </script>

           <div class="title" style="font-weight:bold">Create New Question</div>
  		   <div id="jqxCreateBeanEditor">
		   		<div class="saveOptions" style="clear:both;margin-bottom:40px;">
					<div id='isCreateQuestion' style="float:left">Create new Question</div>
					<div id='isImportQuestions' style="float:left">Import Questions from xls</div>
				</div>
		   		<div class="tabContent1">
					<form id="createBeanForm" name="createBeanForm" method="POST"/>
						<%@ include file="questionFormInclude.jsp" %>
					</form>
				</div>
				<div class="tabContent2" style="display:none">
					<%@ include file="importQuestions.jsp" %>
				</div>
					
			</div>

