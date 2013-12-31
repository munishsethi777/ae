<script>

function openAddQuestionsUI(gameSeq){
	$("#addQuestionsWindow").jqxWindow('open');
	loadSelectedQuestionsGrid(gameSeq);
}
function updateSelectedQuestionGrid(dataRowJson){
	$("#selectedQuestionsGrid").jqxGrid('addrow', null, dataRowJson,null,true);
}

function getFormData(isImport){
	var gameSeq = $("#gameSeq" + tempSeq).val();
	dataRow = [];
	
	var obj = new Object();
    if(gameSeq ==0){
		var gameTemplateSeq = tempSeq;
		obj.name = "gameTempSeq";
		obj.value = gameTemplateSeq;
	}else{
		obj.name = "gameSeq";
		obj.value = gameSeq;
	}
	var index = 0;
	if(!isImport){
		dataRow = $("#createQuestionForm").serializeArray();
		index = dataRow.length;
	}
	
	dataRow[index] = obj;
	var obj2 = new Object();
	var campaignSeq = $("#createCampaignForm #seqInput").val();
	obj2.name = "campaignSeq";
	obj2.value = campaignSeq;
	dataRow[index + 1] = obj2;
	return dataRow ;
}

function addGameFromImportQues(quesJson){
	data = getFormData(true);
	var quesSeqArr = [];
	var i = 0
	$.each(quesJson, function() {
		quesSeqArr.push(quesJson[i]['seq']);
		i =i+1;
	})
	var obj1 = new Object();
	obj1.name = "quesSeqs";
	obj1.value = quesSeqArr;
	data[2] = obj1;
	
	dataRow = data;
	var action = "AdminUser?action=addGameFromImportQuestion"
	submitAction(action,dataRow);
}

function addQuestionAndGame(){		
	dataRow = getFormData(false);
	//submitAddRecord("selectedQuestionsGrid");
	submitAction("AdminUser?action=addQuestions",dataRow);
}

function submitAction(action,dataRow){
	$.getJSON(action,dataRow,function(json){
		if(json['status'] == 'success'){
			if(dataRow['seq'] == null || dataRow['seq'] == "" || dataRow['seq'] == "0"){		
				dataRow['seq'] = json['seq'];
				dataRowJson = {};
				 $.each(dataRow, function() {
						dataRowJson[this.name] = this.value || '';
				 });
				 if(json['gameSeq'] != 0){
					$("#gameSeq" + tempSeq).val(json['gameSeq']);
					$(".editGameButton" + tempSeq).show();
				 }
				 
				$("#selectedQuestionsGrid").jqxGrid('addrow', null, dataRowJson,null,true);
				newQuestionAdded(tempSeq);
			}$('#createQuestionForm')[0].reset();
		}
	});
}
function newQuestionAdded(templateSeq){
	var questionCount = $("#selectedTemplateDivId"+templateSeq +" #gameTemplateTotalQuestions").val();
	if(questionCount == ""){
		questionCount = 0;
	}
	questionCount = parseInt(questionCount,10);
	questionCount = questionCount + 1;
	$("#quesCount" + templateSeq).html("Count : " + questionCount);
	$("#selectedTemplateDivId"+templateSeq +" #gameTemplateTotalQuestions").val(questionCount);
}

function loadGameTemplates(campaignSeq){
	var getTemplatesUrl = "AdminUser?action=getAllGameTemplates&campaignSeq="+campaignSeq;
	$(".selectGameTemplatesBlock").html("");
	$.getJSON(getTemplatesUrl,function(JSON){
		$.each(JSON, function(index,val){
			content = getGameTemplateDiv(index,val);
			$(content).appendTo(".selectGameTemplatesBlock");
			
			$("#templateSeqRadio"+val.seq).jqxCheckBox({ width: 20, height: 25, theme: theme });
			$("#templateSeqRadio"+val.seq).on('change', function(event) {
				var checked = event.args.checked;
	            if (checked){
	            	$("#addQuestionLink"+val.seq).show();
	            	$("#selectedTemplateDivId"+val.seq).addClass('backColoredWhite');
	            }else{
	            	$("#addQuestionLink"+val.seq).hide();
	            	$("#selectedTemplateDivId"+val.seq).removeClass('backColoredWhite');
	            }
			});
			if(val.gameSeq != 0){
				$("#templateSeqRadio"+val.seq).jqxCheckBox('check');
			}
			$(".editGameButton"+val.seq).jqxButton({ width: 70, theme: theme });
			$(".editGameButton"+val.seq).on('click', function(event) {
				editGameDetails(event.currentTarget.id);
			});
			
			$("#demo"+val.seq).jqxButton({ width: 70, theme: theme });
			$("#demo"+val.seq).on('click', function(event) {
				alert("demo");
			});
			$("#addQuestionLink"+val.seq).jqxButton({ width: 120, theme: theme });
			$("#addQuestionLink"+val.seq).on('click', function(event){
				tempSeq = event.currentTarget.id;
				tempSeq = tempSeq.replace("addQuestionLink" , "");
				gameSeq = $("#gameSeq"+tempSeq).val();
				openAddQuestionsUI(gameSeq);							
			});
			$("#deleteBeanConfirmation").jqxWindow({ resizable: true, theme: theme, autoOpen: false, width: 450, height: 200, showCloseButton: true });
		});
	});
	loadEarlierGames(campaignSeq);
}

function getGameTemplateDiv(index,json){
	var content= "";
	content += "<div class='selectGameTemplatesDiv' id='selectedTemplateDivId"+json.seq+"'>";
	content += "<input type='hidden' id='gameTemplateTotalQuestions'/>";
	content += "<input type='hidden' id='gameTemplateSeq' value='" + json.seq + "'/>";
	content += "<input type='hidden' id='gameSeq"+json.seq +"' value='"+json.gameSeq +"'/>";
		content += "<div class='gameIcon'>";
			content += "<img src='" + json.imagePath +"'/>";
		content += "</div>";
		content += "<div class='gameTemplateDetailsDiv'>";
			
		content += "<div style='float:right' id='templateSeqRadio"+ json.seq +"'></div>";
		content += '<label style="font-size:20px;" id="gameTitle'+ json.seq +'">' + json.name + '</label>';
		var totQuest = 0;
		if(json.totalQuestions != undefined){
			totQuest = json.totalQuestions;
		}
		content += " <label id='quesCount" + json.seq + "'> "+ totQuest +" /</label>";
		content += "<label>"+ json.maxQuestions +' Questions</label>';
	   	content += '<div class="smallFonts" style="height:70px;" id="gameDescription'+ json.seq +'">' + json.description + '</div>';
	content += '<div style="margin-top:10px;">';
	if(json.gameSeq != ""){
		content += "<input style='display:inline-table;margin-right:4px' value='Edit' class='editGameButton"+json.seq+"' type='button' id='"+json.seq+"'/>";	
	}
	content += "<input style='display:inline-table' value='Demo' type='button' id='demo"+json.seq+"'/>";
	content += "<input value='Add Questions' type='button' id='addQuestionLink"+json.seq+"' class='marL10' style='display:none;margin-left:10px;float:right'/>";
	content += '</div>';
	content += "</div>";
   	content += "<br class='clr'>";
		content += '</div>';
		return content;
}
	var questionsDataFields = [
               			{ name: 'seq', type: 'integer' },
               			{ name: 'quesTitle', type: 'string' },
               			{ name: 'description', type: 'string' },
               			{ name: 'points', type: 'string' },
               			{ name: 'negativePoints', type: 'string' },
               			{ name: 'maxSecondsAllowed', type: 'string' },
               			{ name: 'extraAttemptsAllowed', type: 'string' },
               			{ name: 'isEnabledQuestion', type: 'bool' },
               			{ name: 'createdOn', type: 'date' },
               			{ name: 'lastmodifieddate', type: 'date'},
               			{ name: 'answer1', type: 'string'},
               			{ name: 'isAnswerCorrect', type: 'string' },
               			{ name: 'answer2', type: 'string'},
               			{ name: 'answer3', type: 'string'},
               			{ name: 'answer4', type: 'string'}
               			
          ];
function loadSelectedQuestionsGrid(gameSeq){

	var selectedQuestionsEditorWidth= "70%";
	var selectedQuestionsEditorHeight = "100%";
	var selQuesdataUrl = "AdminUser?action=getQuestionsSelectedOnGame&gameSeq="+gameSeq;
	var deleteUrl = "AdminUser?action=deleteQuestions";
	var validatorRules = [];
			
	var columns = [				
			{ text: 'Title', datafield: 'quesTitle' ,editable:false,width:"25%"},
			{ text: 'Description', datafield: 'description' ,editable:false},
			{ text: 'Points(+)', datafield: 'points',editable:false,width:80,cellsalign: 'right',align: 'right' },
			{ text: 'Points(-)', datafield: 'negativePoints',editable:false,width:80,cellsalign: 'right',align: 'right' },
			{ text: 'Time Avlb.', datafield: 'maxSecondsAllowed',editable:false,width:80,cellsalign: 'right',align: 'right' },
			{ text: 'Chances', datafield: 'extraAttemptsAllowed',editable:false,width:80,cellsalign: 'right',align: 'right' },
			{ text: 'Created On', datafield: 'createdOn',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
			{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
			{ text: 'Enabled', datafield: 'isEnabledQuestion',columntype: 'checkbox',editable:false,width:60}
			];

	
	renderGrid("selectedQuestionsGrid",beanName,selQuesdataUrl,deleteUrl,addUrl,validatorRules,
			columns,questionsDataFields,true,selectedQuestionsEditorHeight,selectedQuestionsEditorWidth);
	//unbinding close button of add question form to prevent it from closing the main screen
	$("#closeButton").unbind();
	$("#closeButton").click(function () {
		$("#addQuestionsWindow").jqxWindow('close');
	});
	
}

</script>