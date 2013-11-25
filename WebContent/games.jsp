<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript">
		var pageName = "games";
		var beanName = "Game";
		var dataUrl = "AdminUser?action=getAllGames";
		var deleteUrl = "AdminUser?action=deleteGame";
		var addUrl = "AdminUser?action=addGame";
		var validatorRules = [
				{ input: '#gameTitleInput', message: 'Title is required!', action: 'keyup, blur', rule: 'required' }];
				
		var columns = [				
				{ text: 'Title', datafield: 'gameTitle' ,editable:false},
				{ text: 'Description', datafield: 'gameDescription' ,editable:false},
				{ text: 'Created On', datafield: 'createdOn',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:60}
				];

		var dataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'gameTitle', type: 'string' },
				{ name: 'gameTemplate', type: 'integer' },
				{ name: 'gameDescription', type: 'string' },
				{ name: 'createdOn', type: 'date' },
				{ name: 'gameMaxSecondsAllowed', type: 'string' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'lastmodifieddate', type: 'date'}
				];
		var isShowButtons = false;
		
		//for the purpose of selection grid for children items
		var sourceGridColumnCheckBox_ = null;
		var sourceGridUpdatingCheckState_ = false;
		
		var destinationGridColumnCheckBox_ = null;
		var destinationGridUpdatingCheckState_ = false;
		
		var sourceGridId_ = "sourceGrid";
		var destinationGridId_ = "destinationGrid";
		var sourceGridWidth_ = "440px";
		var destinationGridWidth_ = "440px";
		var selectionGridSourceUrl_ = "AdminUser?action=getQuestionsAvailableOnGame";
		var selectionGridDestinationUrl_ = "AdminUser?action=getQuestionsSelectedOnGame";
		var theme_ = "energyblue";
		
		var selectionGridColumns_ = [				
				{ text: 'Title', datafield: 'quesTitle' ,editable:false},
				{ text: 'Description', datafield: 'description' ,editable:false},
				{ text: 'Created On', datafield: 'createdOn',editable:false}
		];
				
		var selectGriddataFields_ = [
				{ name: 'seq', type: 'integer' },
				{ name: 'quesTitle', type: 'string' },
				{ name: 'description', type: 'string' },
				{ name: 'createdOn', type: 'date' },
		];
		//Question Addition vars starts ----------------------
		var validatorRulesForQuestions = 
			[
				{ input: '#quesTitleInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#answer1Input', message: 'Answer 1 is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#answer2Input', message: 'Answer 2 is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#answer3Input', message: 'Answer 3 is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#answer4Input', message: 'Answer 4 is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#pointsInput', message: 'Points is required!', action: 'keyup, blur', rule: 'required' }
			];
		var questionDataFields = [
		  				{ name: 'seq', type: 'integer' },
		  				{ name: 'quesTitle', type: 'string' },
		  				{ name: 'description', type: 'string' },
		  				{ name: 'points', type: 'string' },
		  				{ name: 'negativePoints', type: 'string' },
		  				{ name: 'maxSecondsAllowed', type: 'string' },
		  				{ name: 'extraAttemptsAllowed', type: 'string' },
		  				{ name: 'isEnabled', type: 'bool' },
		  				{ name: 'createdOn', type: 'date' },
		  				{ name: 'lastmodifieddate', type: 'date'},
		  				{ name: 'answer1', type: 'string'},
		  				{ name: 'isAnswerCorrect', type: 'radio' },
		  				{ name: 'answer2', type: 'string'},
		  				{ name: 'answer3', type: 'string'},
		  				{ name: 'answer4', type: 'string'}
		  				
		  				];      		
		$(document).ready(function () {
				var editorWidth= "85%";
				var editorHeight = "90%";
				
				renderGrid("jqxGrid",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,true,editorHeight,editorWidth);
				$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
				$(".isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
				$(".closeQuestionButton").hide();
				$("#createQuestionBeanForm #saveButton").jqxButton({ width: 50,theme: theme });
				renderSelectionGrid(0,sourceGridColumnCheckBox_,sourceGridUpdatingCheckState_,destinationGridColumnCheckBox_,destinationGridUpdatingCheckState_,sourceGridId_,destinationGridId_,sourceGridWidth_,destinationGridWidth_,selectionGridSourceUrl_,selectionGridDestinationUrl_,theme_,selectionGridColumns_,selectGriddataFields_);
				
				$('#jqxCreateBeanWindow').on('open', function (event) { 
					if(event.args != null){
						$('#jqxCreateBeanWindow').jqxWindow({ position: 'center'}); 
						var editingParentSeq = $("#jqxCreateBeanWindow #seqInput").val();
						$("#"+sourceGridId_).jqxGrid({source:getSelectionGridAdapter(selectionGridSourceUrl_,editingParentSeq)});
						$("#"+destinationGridId_).jqxGrid({source:getSelectionGridAdapter(selectionGridDestinationUrl_,editingParentSeq)});
						//Reset question form
						$('#createQuestionBeanForm')[0].reset();
						$('#isEnabledInput').val(false);
						$("#isEnabledInput").removeAttr('checked');
						$("#createQuestionBeanForm #seqInput").val(0);
					
					}
					
				});
				function getSelectionGridAdapter(url,editingParentSeq){
					var source1 = {
						url:url + "&gameSeq="+editingParentSeq,
						id: 'seq',
						datatype: "json",
						datafields:selectGriddataFields_,
					};
					var dataAdapter = new $.jqx.dataAdapter(source1);
					return dataAdapter;
				}
	            
				//----------------gameTemplateDropDown starts----------------
				
					var url = "AdminUser?action=getAllGameTemplates";
					var source =
					{
						datatype: "json",
						datafields: [
							{ name: 'seq' },
							{ name: 'name' }
						],
						url: url,
						async: false
					};
					var dataAdapter = new $.jqx.dataAdapter(source);
					$("#gameTemplateDD").jqxDropDownList({
						selectedIndex: 0, source: dataAdapter, displayMember: "name", valueMember: "seq", width: 200, height: 25, theme: theme
					});
					// subscribe to the select event.
					$("#gameTemplateDD").on('select', function (event) {
						if (event.args) {
							var item = event.args.item;
							if (item) {
								
							}
						}
					});
				//----------------gameTemplate dropdown creation ends---------------
				
				//-------------Add new Question scripts starts here-----------------
				$('#createQuestionBeanForm').jqxValidator({
					animationDuration:5,
					rules: validatorRulesForQuestions
				});
				$("#createQuestionBeanForm .saveQuestionButton").click(function () {
					var validationResult = function (isValid) {
						if (isValid) {
							submitQuestion();
						}
					}
					$('#createQuestionBeanForm').jqxValidator('validate', validationResult);
				});
				$("#createQuestionBeanForm").on('validationSuccess', function () {
					$("#createQuestionBeanForm-iframe").fadeIn('fast');
				});
				function submitQuestion(gridId){//method adding a new question run time
					dataRow = {};
					dataRow['rowId'] = $("#createQuestionBeanForm #rowIdInput").val();
					$.each(questionDataFields,function(index,value){
						dataRow[value.name] = $("#createQuestionBeanForm #"+ value.name +"Input").val();
						if(value.type == "radio"){
							dataRow[value.name] = $('input[name='+ value.name +']:radio:checked').val()
						}
					});

					$.getJSON("AdminUser?action=addQuestions",dataRow,function(json){
						if(json['status'] == 'success'){
						dataRow['lastmodifieddate'] = json['lastModified'];	
						
							if(dataRow['seq'] == null || dataRow['seq'] == "" || dataRow['seq'] == "0"){	
								dataRow['createdOn'] = json['createdOn'];			
								dataRow['seq'] = json['seq'];				
								dataRow['createdOn'] = json['createdOn'];
								var commit = $("#"+sourceGridId_).jqxGrid('addrow', null, dataRow,null,true);
								var commit = $("#"+destinationGridId_).jqxGrid('addrow', null, dataRow,null,true);
							}
							$("#"+sourceGridId_).jqxGrid('ensurerowvisible', dataRow['rowId']);
							$("#"+destinationGridId_).jqxGrid('ensurerowvisible', dataRow['rowId']);
						}
					});
					
				}//submit add queston ends.
				//-------------Add new Question scripts ends here-----------------
				var getTemplatesUrl = "AdminUser?action=getAllGameTemplates";
				$.getJSON(getTemplatesUrl,function(JSON){
					$.each(JSON, function(index,val){
						content = getGameTemplateDiv(index,val);
			    		$(content).appendTo(".selectGameTemplatesBlock");
			    		$("#templateSeqRadio"+val.seq).jqxRadioButton({theme:theme});
			    		$("#templateSeqRadio"+val.seq).on('change', function(event) {
			    			var checked = event.args.checked;
			                if (checked) {
			                	$(".selectGameTemplatesDiv").removeClass('backColoredGrey');
			                    $("#selectedTemplateDivId"+val.seq).addClass('backColoredGrey');
			                }
			    		});
			    		if(index == 0){
			    			$("#templateSeqRadio"+val.seq).jqxRadioButton('check');
			    		}
			    	});
			    });
				function getGameTemplateDiv(index,json){
					var content="";
					content += "<div class='selectGameTemplatesDiv' id='selectedTemplateDivId"+json.seq+"'>";
						content += "<div class='gameIcon'>";
							content += "<img src='" + json.imagePath +"'/>";
						content += "</div>";
						content += "<div class='gameTemplateDetailsDiv'>";
							content += "<div style='float:right' id='templateSeqRadio"+ json.seq +"'></div>";
							content += '<h1>' + json.name + '</h1>';
					   		content += '<h2>' + json.description + '</h2>';
					   		content += '<p>';
					   			content += "<a href='#' class='link marL10'>Demo</a>";
					   		content += '</p>';
					   	content += "</div>";
				   		content += "<br class='clr'>";
			   		content += '</div>';
			   		return content;
				}
				
				$('#jqxCreateBeanEditor').jqxTabs({ width: '100%', height:'80%', theme: theme });
				
				$("#gameTitleInput").jqxInput({	placeHolder : "enter a game title", height : 25, width : 500, minLength : 1, maxLength : 256});
				$("#gameDescriptionInput").jqxInput({	placeHolder : "enter a game description", height : 25, width : 500, minLength : 1, maxLength : 256});
				$("#gameMaxSecondsAllowedInput").jqxInput({height : 25, width : 100, minLength : 1, maxLength : 256});
				$("#questionsAccordion").jqxNavigationBar({ width: '100%', height: '80%', expandMode: 'singleFitHeight', theme: theme });

				
		});//end document ready	
		function getSelectedRowsData(dataRow){
				var allrows = $("#"+destinationGridId_).jqxGrid("getrows");
				var allRowIds = new Array();
				for(var i = 0;i<allrows.length;i++){
					allRowIds.push(allrows[i].seq);
				}
				dataRow['selectedChildrenRows'] = allRowIds.toString();
				var gameTemplate = $("#gameTemplateDD").jqxDropDownList('getSelectedItem').value;
				dataRow['gameTemplate'] = gameTemplate;
				return dataRow;
			}
    </script>
</head>
<body class='default'>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<%@ include file="grid.jsp" %>
<div id='jqxWidget'>
	<label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">Games Information</label><br>
	<label style="font-family:verdana;font-size: 12px;color:grey">View, Create, Edit, Bulk Delete or Find through various sets available in the database.</label>
    <div id="jqxGrid"></div>
	<div id="jqxCreateBeanWindow">
		<div class="title" style="font-weight:bold">Create New Set</div>
		<span class="wew" style="width:100%">
			<ol style="float:left">Select options from the following three tabs.
				<li>Enter Game Details.</li>
				<li>Pick a Template from te available set of templates.</li>
				<li>Add Questions to the template to create a game.</li>
			</ol>
			<span style="float:right;margin:10px 10px 0px 0px;">
				Step 1 of 3
			</span>
		</span>
		<div id="jqxCreateBeanEditor">
			<ul>
				<li>Enter Game Description</li>
				<li>Select Template</li>
				<li>Add Questions</li>
				
			</ul>
		   	<div class="tabContent1">
				<form id="createBeanForm" name="createBeanForm"  method="POST"/>
					<input type="hidden" name="rowId" id="rowIdInput"/>
					<input type="hidden" name="seq" id="seqInput"/>
					<input type="hidden" name="createdOn" id="createdOnInput"/>
					<table class="formTable">
						<tr>
							<td>Name</td>
							<td><input name="gameTitle" type="text" id="gameTitleInput"/></td>
						</tr>
						<tr>
							<td>Description</td>
							<td><input name="gameDescription" type="text" id="gameDescriptionInput" size="100"/></td>
						</tr>
						<tr>
							<td>Time Allowed</td>
							<td> 
								<input name="gameMaxSecondsAllowed" type="text" id="gameMaxSecondsAllowedInput" size="2"/>
								in seconds
							</td>
						</tr>
						<tr>
							<td>Enabled</td>
							<td><div id="isEnabledInput"></div></td>
						</tr>
						<tr>
							<td></td>
							<td>
								<input type="button" style='margin-top: 15px; float: left;' value="Save" id="saveButton" />
								<input type="button" style='margin-left: 5px; margin-top: 15px; float: left;' value="Close" id="closeButton" />
							</td>
						</tr>
					</table>
				</form>
			</div>
			
			<div class="tabContent2">
				<div class="selectGameTemplatesBlock"></div>
			</div>
			
			<div class="tabContent3">
			
				<div id='questionsAccordion'>
					<div>Add New Question</div>
					<div>
						<form id="createQuestionBeanForm" name="createQuestionBeanForm" method="POST"/>
							<%@ include file="questionFormInclude.jsp" %>
						</form>
					</div>
					
					<div>Select from Earlier Saved</div>
					<div>
						<p>Select from the available list of questions and move them to the questions of the game.</p>
						<%@ include file="selectionGridComponent.jsp" %>
						<div id='jqxWidget' style="margin-top:20px;">
							<div id="sourceGrid" style="float:left"></div>
							<div style="float:left;margin:50px 10px 0px 10px;padding:20px;">
								<img class="btn addRowBtn" src="images/icons/arrowRight.png"><br>
								<img class="btn deleteRowBtn" src="images/icons/arrowLeft.png"><br>
								<img class="btn addAllRowBtn" src="images/icons/arrowAllRight.png"><br>
								<img class="btn deleteAllRowBtn" src="images/icons/arrowAllLeft.png">
							</div>
							<div id="destinationGrid" style="float:left;"></div>
						</div>
					</div>
					
					
				</div><!-- QuestionAccordion Ends -->
			
			</div><!-- Tabcontent 3 ends -->
				
		</div><!-- Ends jqxCreateBeanEditor -->
	</div><!-- Ends jqxCreateBeanWindow -->
</div><!-- Ends jqxWidget -->
</body>
</html>
