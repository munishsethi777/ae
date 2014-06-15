//to know when grid gets row added
function addRowCustomQuestionGrid(rowdata){
	$("#gameDetailsForm #gameSeq").val(rowdata.gameSeq);
	newQuestionAdded(rowdata.gameSeq,rowdata.gameTempSeq);
	$('#createQuestionForm')[0].reset();
}
//to know when grid gets row removed
function deleteRowCustomQuestionGrid(selectedIds){
	var gameSeq = $("#gameDetailsForm #gameSeq").val();
	var questionCount = $("#gameDiv"+gameSeq +" #gameTemplateTotalQuestions").val();
	if(questionCount == undefined){
		questionCount = 0;
	}
	questionCount = parseInt(questionCount,10);
	questionCount = questionCount - selectedIds.length;
	$("#quesCount"+gameSeq).html(questionCount);
	$("#gameDiv"+gameSeq +" #gameTemplateTotalQuestions").val(questionCount);
}
function openAddQuestionsUI(gameSeq,templateSeq){
	$("#addQuestionsWindow").jqxWindow('open');
	loadGameDetails(gameSeq);
	loadSelectedQuestionsGrid(gameSeq);
	resetQuestionsEditor();
	$(".selectQuestionsDiv .gameNameInput").val("");
	$(".selectQuestionsDiv .gameDescriptionInput").val("");
	$("#gameDetailsForm #gameSeq").val(gameSeq);
	$("#gameDetailsForm #gameTemplateSeq").val(templateSeq);
	
}
function loadGameDetails(gameSeq){
	if(gameSeq != null && gameSeq != 0){
		var getGameDetailsURL = "AdminUser?action=getGamesBySeqs&gameSeqs=" + gameSeq; 	
		$.getJSON(getGameDetailsURL,function(json){
			if(json[0].status != "failure"){
				//$('#userJqxGrid').jqxGrid('selectrow', json[0]["seq"]);
				$(".selectQuestionsDiv .gameNameInput").val(json[0]["gameTitle"]);
				$(".selectQuestionsDiv .gameDescriptionInput").val(json[0]["gameDescription"]);
				$("#gameDetailsForm #gameSeq").val(gameSeq);
			}
		});
	}
}
function loadSelectedQuestionsGrid(gameSeq){
	var selectedQuestionsEditorWidth= "90%";
	var selectedQuestionsEditorHeight = "100%";
	var selQuesdataUrl = "AdminUser?action=getQuestionsSelectedOnGame&gameSeq="+gameSeq;
	var deleteQuesUrl = "AdminUser?action=removeQuestionFromGame&gameSeq="+gameSeq;
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

	renderGrid("selectedQuestionsGrid","addQuestionsWindow",beanName,selQuesdataUrl,deleteQuesUrl,addUrl,validatorRules,
			columns,questionsDataFields,true,selectedQuestionsEditorHeight,selectedQuestionsEditorWidth,
			addRowCustomQuestionGrid,deleteRowCustomQuestionGrid);
	bindNewClickOnSelectedQuestionsGrid();
	bindDoubleClickOnSelectedQuestionsGrid();
	//unbinding close button of add question form to prevent it from closing the main screen
	$("#closeButton").unbind();
	$("#closeButton").click(function () {
		$("#addQuestionsWindow").jqxWindow('close');
	});
}

function updateSelectedQuestionGrid(dataRowJson){
	$("#selectedQuestionsGrid").jqxGrid('addrow', null, dataRowJson,null,true);
}
function getFormData(isImport){
	var gameName = $(".selectQuestionsDiv .gameNameInput").val();
	var gameDescription = $(".selectQuestionsDiv .gameDescriptionInput").val();

	
	var gameSeq = $("#gameDetailsForm #gameSeq").val();
	dataRow = [];
	
	var obj = new Object();
    if(gameSeq ==0){
		var gameTemplateSeq = $("#gameDetailsForm #gameTemplateSeq").val();;
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
	var campaignSeq = getCampaignSeqFromForm();
	obj2.name = "campaignSeq";
	obj2.value = campaignSeq;
	dataRow[index + 1] = obj2;
	
	var gameNameObj = new Object();
	gameNameObj.name = "gameName";
	gameNameObj.value = gameName;
	dataRow[index + 2] = gameNameObj;
	
	var gameDescriptionObj = new Object();
	gameDescriptionObj.name = "gameDescription";
	gameDescriptionObj.value = gameDescription;
	dataRow[index + 3] = gameDescriptionObj;
	
	dataRow['seq'] = $("#createQuestionForm #seqInput").val(); 
	dataRow['rowId'] = $("#createQuestionForm #rowIdInput").val(); 
	return dataRow ;
}
function addGameFromImportQues(quesJson){
	data = getFormData(true);
	var quesSeqArr = [];
	var i = 0;
	$.each(quesJson, function() {
		quesSeqArr.push(quesJson[i]['seq']);
		i =i+1;
	});
	var obj1 = new Object();
	obj1.name = "quesSeqs";
	obj1.value = quesSeqArr;
	data[2] = obj1;
	
	dataRow = data;
	var action = "AdminUser?action=addGameFromImportQuestion";
	//submitAction(action,dataRow);
	submitAddRecordAction(action,"selectedQuestionsGrid",dataRow);
}
function addQuestionAndGame(){
	dataRow = getFormData(false);
	dataRowJson = {};
	$.each(dataRow, function() {
		dataRowJson[this.name] = this.value || '';
	});
	submitAddRecordAction("AdminUser?action=addQuestions","selectedQuestionsGrid",dataRowJson,"questionEditorErrorDiv");
	
}
//Method called with top save button
function saveGameDetails(gameTemplateSeq){
	dataRow = $("#gameDetailsForm").serializeArray();
	var campaignSeq = getCampaignSeqFromForm();
	var templateSeq = $("#gameDetailsForm #gameTemplateSeq").val();
	
	var url = "AdminUser?action=saveGameDetails&campaignSeq="+campaignSeq;
	url += "&templateSeq="+templateSeq;
	$.getJSON(url,dataRow,function(json){
		if(json['status'] == 'success'){
			var gameSeq = json['gameSeq'];
			var templateSeq = $("#gameDetailsForm #gameTemplateSeq").val();
			$("#gameDetailsForm #gameSeq").val(gameSeq);
			if($('#gameDiv'+gameSeq).length==0){
				createNewGameBlockDiv(gameSeq,templateSeq);
			}
		}
	});
}
function newQuestionAdded(gameSeq,templateSeq){
	var questionCount = $("#gameDiv"+gameSeq +" #gameTemplateTotalQuestions").val();
	if(questionCount == undefined){
		questionCount = 0;
	}
	questionCount = parseInt(questionCount,10);
	questionCount = questionCount + 1;
	if(questionCount == 1){
		if($('#gameDiv'+gameSeq).length==0){
			createNewGameBlockDiv(gameSeq,templateSeq);
		}
	}
	$("#quesCount"+gameSeq).html(questionCount);
	$("#gameDiv"+gameSeq +" #gameTemplateTotalQuestions").val(questionCount);
	
}
function createNewGameBlockDiv(gameSeq, tempSeq){
	var json = {};
	json['seq'] = tempSeq;
	json['gameSeq'] = gameSeq;
	json['name'] = $("#gameDetailsForm #gameNameInput").val();
	json['description'] = $("#gameDetailsForm #gameDescriptionInput").val();
	json['maxQuestions'] = $("#templateDiv"+ tempSeq +" #maxQuestions").val();
	json['totQuest'] = 1;
	json['imagePath'] = $("#templateDiv"+ tempSeq +" #imgPath").prop("src");
	addGameBlock(json);
}
//load templates and games of this campaign seq only
function loadGameTemplates(campaignSeq){
	var getTemplatesUrl = "AdminUser?action=getAllGameTemplates&campaignSeq="+campaignSeq;
	$(".selectPendingGamesBlock").html("");
	$(".selectGameTemplatesBlock").html("");
	$.getJSON(getTemplatesUrl,function(JSON){
		$.each(JSON, function(index,val){
			addGameBlock(val);
		});
	});
	loadEarlierGames(campaignSeq);
}

function addGameBlock(json){
	content = getGameTemplateDiv(json);
	var gameCheckboxId = "gameCheckbox"+json.seq;
	if(json.gameSeq != ""){
		$(content).appendTo(".selectPendingGamesBlock");
		gameCheckboxId = "gameCheckbox"+json.gameSeq;
		$("#demo"+json.gameSeq).jqxButton({ width: 70, theme: theme });
		$("#demo"+json.gameSeq).on('click', function(event) {
			alert("demo");
		});
	}else{
		$(content).appendTo(".selectGameTemplatesBlock");
		$("#demo"+json.seq).jqxButton({ width: 70, theme: theme });
		$("#demo"+json.seq).on('click', function(event) {
			alert("demo");
		});
	}
	
	$("#"+gameCheckboxId).jqxCheckBox({ width: 20, height: 25, theme: theme });
	$("#"+gameCheckboxId).on('change', function(event) {
		var checked = event.args.checked;
        if (checked){
        	if(json.gameSeq != ""){
            	$("#editGameLink"+json.gameSeq).show();
            	$("#gameDiv"+json.gameSeq).addClass('backColoredWhite');
        	}
        }else{
        	if(json.gameSeq != ""){
            	$("#editGameLink"+json.gameSeq).hide();
            	$("#gameDiv"+json.gameSeq).removeClass('backColoredWhite');
        	}
        }
	});
	if(json.gameSeq != 0){
		$("#gameCheckbox"+json.gameSeq).jqxCheckBox('check');
	}

	
	$("#editGameLink"+json.gameSeq).jqxButton({ width: 120, theme: theme });
	$("#editGameLink"+json.gameSeq).on('click', function(event){
		blockId = event.currentTarget.id;
		blockId = blockId.replace('editGameLink','');
		gameSeq = blockId;
		openAddQuestionsUI(gameSeq,0);							
	});
	
	//$("#createGameLink"+json.seq).jqxButton({ width: 120, theme: theme });
	$("#createGameLink"+json.seq).on('click', function(event){
		blockId = event.currentTarget.id;
		blockId = blockId.replace('createGameLink','');
		openAddQuestionsUI(0,blockId);							
	});
}
function getGameTemplateDiv(json){
	if(json.gameSeq != ""){
		return getGameDiv(json);
	}
	var totQuest = 0;
	if(json.totalQuestions != undefined){
		totQuest = json.totalQuestions;
	}
	var content= "";
	content += "<div class='selectGameTemplatesDiv' id='templateDiv"+json.seq+"'>";
	content += "<input type='hidden' id='gameTemplateTotalQuestions' value='"+ totQuest +"'/>";
	content += "<input type='hidden' id='gameTemplateSeq' value='" + json.seq + "'/>";
	content += "<input type='hidden' id='maxQuestions' value='"+json.maxQuestions +"'/>";
	content += "<div class='gameIcon'>";
	content += "<img id='imgPath' src='" + json.imagePath +"'/>";
	content += "</div>";
	content += "<div class='gameTemplateDetailsDiv'>";
	content += '<label style="font-size:13px;" id="gameTitle'+ json.seq +'">' + json.name + '</label>';
	content += " <label>("+ json.maxQuestions +' Ques)</label><br>';
   	//content += "<br><a href='#' id='demo"+json.seq+"'>demo</a> ";
   	content += "<a href='#' id='createGameLink"+json.seq+"'>create new game</a>";
	content += "</div>";
	content += "<br class='clr'>";
	content += '</div>';
	return content;
}

function getGameDiv(json){
	var totQuest = 0;
	if(json.totalQuestions != undefined){
		totQuest = json.totalQuestions;
	}
	var gameCheckboxId =  "gameCheckbox"+ json.gameSeq;
	var questionCountId = "quesCount" + json.gameSeq;
	var content= "";
	content += "<div class='pendingGameDiv' id='gameDiv"+json.gameSeq+"'>";
	content += "<input type='hidden' id='gameTemplateTotalQuestions' value='"+ totQuest +"'/>";
	content += "<input type='hidden' id='gameTemplateSeq' value='" + json.seq + "'/>";
	content += "<input type='hidden' id='gameSeq"+json.seq +"' value='"+json.gameSeq +"'/>";
	content += "<input type='hidden' id='maxQuestions' value='"+json.maxQuestions +"'/>";
	content += "<div class='gameIcon'>";
	content += "<img id='imgPath' src='" + json.imagePath +"'/>";
	content += "</div>";
	content += "<div class='gameTemplateDetailsDiv'>";
	content += "<div style='float:right' id='"+ gameCheckboxId +"'></div>";
	content += '<label style="font-size:12px;" id="gameTitle'+ json.gameSeq +'">' + json.name + '</label>';
	content += "<br><label id='" + questionCountId + "'>"+ totQuest +"</label>";
	content += "<label> /"+ json.maxQuestions +' Questions</label>';
   	content += '<div class="smallFonts" id="gameDescription'+ json.gameSeq +'">' + json.description + '</div>';
	content += '<div style="margin-top:10px;">';
	content += "<input style='display:inline-table' value='Demo' type='button' id='demo"+json.gameSeq+"'/>";
	content += "<input value='Edit Game' type='button' id='editGameLink"+json.gameSeq+"' class='marL10' style='margin-left:10px;float:right'/>";
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


function bindNewClickOnSelectedQuestionsGrid(){
	$("#statusbarselectedQuestionsGrid #addButton").unbind('click');
	$("#statusbarselectedQuestionsGrid #addButton").click(function (event) {
		resetQuestionsEditor();
	});
}
function resetQuestionsEditor(){
	showHideErrorMessageEditorDiv(false,".editorErrorDiv");
	if(typeof $('#createQuestionForm')[0] != 'undefined'){
		$('#createQuestionForm')[0].reset();
	}
	$("#isEnabledQuestionInput").jqxCheckBox('check');
	$("#createQuestionForm #seqInput").val(0);
}
function bindDoubleClickOnSelectedQuestionsGrid() {
	//$("#selectedQuestionsGrid").unbind('rowdoubleclick');
	$("#selectedQuestionsGrid").unbind('rowdoubleclick');
	$("#selectedQuestionsGrid").on('rowdoubleclick', function (event){ 
		var args = event.args;
		var rowIndex = args.rowindex;
		var dataRow = $("#selectedQuestionsGrid").jqxGrid('getrowdata', rowIndex);
		editingBeanRow = dataRow;//page level variable for use in jsp page
		$("#createQuestionForm #rowIdInput").val(rowIndex);
		$.each(questionsDataFields,function(index,value){
			if(value.name != "isEnabled"){
				 var rowColVal=dataRow[value.name];
				 if (rowColVal==undefined){
					 if (value.type="date"){
						// rowColVal=new Date();
					 }else{
						 rowColVal="";
					 }
				 }
				// if (value.type="date"){
				//	 $("#jqxCreateBeanWindow #"+ value.name +"Input").jqxCalendar('setDate', rowColVal); 
					 //$("#jqxCreateBeanWindow #"+ value.name +"Input").val(rowColVal);
				// }else{
					 $("#createQuestionForm #"+ value.name +"Input").val(rowColVal);
				// }
				
				 if(value.type=="bool"){
					if(dataRow[value.name] == true || dataRow[value.name] == "true"){						 
						$("#" + value.name +"Input").prop('checked', true);
					}else{
						$("#" + value.name +"Input").prop('checked', false);
					}
				 }
				 if(value.type=="radio"){
					$("input[name='" + value.name + "'][value='" + dataRow[value.name] + "']").attr("checked", "checked");
					 // to hanle null field case //$("input[name='" + value.name + "'][value='" + rowColVal + "']").attr("checked", "checked");
				 }
			}else{
				if(dataRow[value.name] == true){
					//$('#isEnabledInput').jqxCheckBox('check');
				}else{
					//$('#isEnabledInput').jqxCheckBox('uncheck');
				}
			}
		});
	});
}
function getAllSelectedGamesSeqs(){
	var $allPublishedGamesRadios = $( "input[name^='earlierGameSeqRadio']" );
	var gameSeqs = new Array();
	$($allPublishedGamesRadios).each(function() {
		if(this.value == "true"){
			inputName = this.name;
			gameSeq = this.name.substr(19);
			gameSeqs.push(gameSeq);
		}
	});
	var $allUnpublishedGamesRadios = $( "input[name^='gameCheckbox']" );
	$($allUnpublishedGamesRadios).each(function() {
		if(this.value == "true"){
			inputName = this.name;
			gameSeq = this.name.substring(12);
			gameSeqs.push(gameSeq);
		}
	});
	return gameSeqs;
}
	
function saveCampaignGames(){
	gameSeqs = getAllSelectedGamesSeqs();
	var campaignSeq = getCampaignSeqFromForm();
	var dataRow = {};
	dataRow["campaignSeq"] = campaignSeq;
	dataRow["gamesSeqs"] = gameSeqs.toString();
	$.getJSON("AdminUser?action=setGamesOnCampaign",dataRow,function(json){
		//games saved put validations if any
	});
}


//load earlier games not having this campaign seq
function loadEarlierGames(campaignSeq){
	var dataUrl = "AdminUser?action=getAllGames&campaignSeq="+campaignSeq;
	$(".gamesBlock").html("");
	$.getJSON(dataUrl,function(JSON){
		$.each(JSON, function(index,val){
			//display games which are not already on this campaign
			if(val['isSelectedOnCampaign'] == undefined){
				content = getEarlierGameDiv(index,val);
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
				//if(val.isSelectedOnCampaign == "true"){
					//$(".gamesBlock #earlierGameSeqRadio"+val.seq).jqxCheckBox('check');
				//}
				
				
				$(".gamesBlock #demo"+val.seq).jqxButton({ width: 70, theme: theme });
				$(".gamesBlock #demo"+val.seq).on('click', function(event) {
					alert("demo");
				});
				$(".gamesBlock #addQuestionLink"+val.seq).jqxButton({ width: 120, theme: theme });
				$(".gamesBlock #addQuestionLink"+val.seq).on('click', function(event){
					gameSeq = $(".gamesBlock #earlierGameSeq"+val.seq).val();
					showGameQuestions(gameSeq);							
				});
			}
    	});
     });
}
//generate the div for each game
function getEarlierGameDiv(index,json){
	var content= "";
	content += "<div style='height:120px;width:18%' class='pendingGameDiv' id='earlierGameDiv"+json.seq+"'>";
	/* content += "<input type='hidden' id='gameTemplateSeq' value='" + json.seq + "'/>"; */
	content += "<input type='hidden' id='earlierGameSeq"+json.seq +"' value='"+json.seq +"'/>";
		content += "<div class='gameIcon'>";
			content += "<img src='" + json.imagePath +"'/>";
		content += "</div>";
		content += "<div class='gameTemplateDetailsDiv'>";
		content += "<div style='float:right' id='earlierGameSeqRadio"+ json.seq +"'></div>";
		content += '<h3>' + json.gameTitle + '</h3>';
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
//function submitAction(action,dataRow){
//$.getJSON(action,dataRow,function(json){
//	if(json['status'] == 'success'){
//		if(dataRow['seq'] == null || dataRow['seq'] == "" || dataRow['seq'] == "0"){		
//			dataRow['seq'] = json['seq'];
//			dataRowJson = {};
//			 $.each(dataRow, function() {
//					dataRowJson[this.name] = this.value || '';
//			 });
//			 if(json['gameSeq'] != 0){
//				$("#gameSeq" + tempSeq).val(json['gameSeq']);
//				$(".editGameButton" + tempSeq).show();
//			 }
//			 
//			$("#selectedQuestionsGrid").jqxGrid('addrow', null, dataRowJson,null,true);
//			newQuestionAdded(tempSeq);
//		}$('#createQuestionForm')[0].reset();
//	}
//});
//}