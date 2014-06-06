function openAddQuestionsUI(gameSeq){
	$("#addQuestionsWindow").jqxWindow('open');
	loadGameDetails(gameSeq);
	loadSelectedQuestionsGrid(gameSeq);
	resetQuestionsEditor();
	$(".selectQuestionsDiv .gameNameInput").val("");
	$(".selectQuestionsDiv .gameDescriptionInput").val("");
}
function loadGameDetails(gameSeq){
	if(gameSeq != null){
		var getGameDetailsURL = "AdminUser?action=getGamesBySeqs&gameSeqs=" + gameSeq; 	
		$.getJSON(getGameDetailsURL,function(json){
			if(json[0].status != "failure"){
				//$('#userJqxGrid').jqxGrid('selectrow', json[0]["seq"]);
				$(".selectQuestionsDiv .gameNameInput").val(json[0]["gameTitle"]);
				$(".selectQuestionsDiv .gameDescriptionInput").val(json[0]["gameDescription"]);
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
			columns,questionsDataFields,true,selectedQuestionsEditorHeight,selectedQuestionsEditorWidth);
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
	var gameSeq = $("#gameSeq" + tempSeq).val();
	newQuestionAdded(gameSeq,tempSeq);
	$('#createQuestionForm')[0].reset();
}
function newQuestionAdded(gameSeq, templateSeq){
	if(gameSeq != 0){
		
	}
	var questionCount = $("#gamesDiv"+templateSeq +" #gameTemplateTotalQuestions").val();
	if(questionCount == ""){
		questionCount = 0;
	}
	questionCount = parseInt(questionCount,10);
	questionCount = questionCount + 1;
	$("#quesCount" + templateSeq).html(questionCount);
	$("#templateDiv"+templateSeq +" #gameTemplateTotalQuestions").val(questionCount);
	if(questionCount == 1){
		var gameSeq =  $("#gameDiv"+blockId+" #gameSeq"+blockId).val();
		createNewGameBlockDiv(gameSeq,templateSeq);
	}
}
function createNewGameBlockDiv(gameSeq, tempSeq){
	var json = {};
	json['seq'] = tempSeq;
	json['gameSeq'] = gameSeq;
	json['name'] = 'Name';
	json['description'] = 'desc';
	json['maxQuestions'] = 10;
	json['totQuest'] = 1;
	addGameBlock(json);
}
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
	var templateSeqRadioId = "templateSeqRadio"+json.seq;
	if(json.gameSeq != ""){
		$(content).appendTo(".selectPendingGamesBlock");
		templateSeqRadioId = "templateSeqRadio"+json.gameSeq;
	}else{
		$(content).appendTo(".selectGameTemplatesBlock");
	}
	
	$("#"+templateSeqRadioId).jqxCheckBox({ width: 20, height: 25, theme: theme });
	$("#"+templateSeqRadioId).on('change', function(event) {
		var checked = event.args.checked;
        if (checked){
        	if(json.gameSeq != ""){
            	$(".selectPendingGamesBlock #editGameLink"+json.gameSeq).show();
            	$(".selectPendingGamesBlock #gameDiv"+json.gameSeq).addClass('backColoredWhite');
        	}else{
        		$(".selectGameTemplatesBlock #createGameLink"+json.seq).show();
            	$(".selectGameTemplatesBlock #templateDiv"+json.seq).addClass('backColoredWhite');
        	}
        }else{
        	if(json.gameSeq != ""){
            	$(".selectPendingGamesBlock #editGameLink"+json.gameSeq).hide();
            	$(".selectPendingGamesBlock #gameDiv"+json.gameSeq).removeClass('backColoredWhite');
        	}else{
        		$(".selectGameTemplatesBlock #createGameLink"+json.seq).hide();
            	$(".selectGameTemplatesBlock #templateDiv"+json.seq).removeClass('backColoredWhite');
        	}
        }
	});
	if(json.gameSeq != 0){
		$("#templateSeqRadio"+json.gameSeq).jqxCheckBox('check');
	}

	$("#demo"+json.seq).jqxButton({ width: 70, theme: theme });
	$("#demo"+json.seq).on('click', function(event) {
		alert("demo");
	});
	$("#editGameLink"+json.gameSeq).jqxButton({ width: 120, theme: theme });
	$("#editGameLink"+json.gameSeq).on('click', function(event){
		blockId = event.currentTarget.id;
		blockId = blockId.replace('editGameLink','');
		gameSeq = blockId;
		openAddQuestionsUI(gameSeq);							
	});
	
	$("#createGameLink"+json.seq).jqxButton({ width: 120, theme: theme });
	$("#createGameLink"+json.seq).on('click', function(event){
		blockId = event.currentTarget.id;
		blockId = blockId.replace('createGameLink','');
		openAddQuestionsUI(0);							
	});
}
function getGameTemplateDiv(json){
	var totQuest = 0;
	if(json.totalQuestions != undefined){
		totQuest = json.totalQuestions;
	}
	var divId = "templateDiv"+json.seq;
	var templateSeqRadioId = "templateSeqRadio"+ json.seq;
	if(json.gameSeq != ""){
		divId = "gameDiv"+json.gameSeq;
		templateSeqRadioId =  "templateSeqRadio"+ json.gameSeq;
	}
	var content= "";
	content += "<div class='selectGameTemplatesDiv' id='"+divId+"'>";
	content += "<input type='hidden' id='gameTemplateTotalQuestions' value='"+ totQuest +"'/>";
	content += "<input type='hidden' id='gameTemplateSeq' value='" + json.seq + "'/>";
	content += "<input type='hidden' id='gameSeq"+json.seq +"' value='"+json.gameSeq +"'/>";
		content += "<div class='gameIcon'>";
		content += "<img id='imgPath' src='" + json.imagePath +"'/>";
		content += "</div>";
		content += "<div class='gameTemplateDetailsDiv'>";
		content += "<div style='float:right' id='"+ templateSeqRadioId +"'></div>";
		content += '<label style="font-size:20px;" id="gameTitle'+ json.seq +'">' + json.name + '</label>';
		content += " <label id='quesCount" + json.seq + "'>"+ totQuest +"</label>";
		content += "<label> /"+ json.maxQuestions +' Questions</label>';
	   	content += '<div class="smallFonts" style="height:70px;" id="gameDescription'+ json.seq +'">' + json.description + '</div>';
	content += '<div style="margin-top:10px;">';

	content += "<input style='display:inline-table' value='Demo' type='button' id='demo"+json.seq+"'/>";
	//if its template block
	var createGameBtnTitle = "Create Game";
	var linkId = "createGameLink"+json.seq;
	if(json.gameSeq != ""){
		//if its game block
		createGameBtnTitle = "Edit Game";
		linkId = "editGameLink"+json.gameSeq;
	}
	content += "<input value='"+ createGameBtnTitle +"' type='button' id='"+linkId+"' class='marL10' style='display:none;margin-left:10px;float:right'/>";
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
	var $allUnpublishedGamesRadios = $( "input[name^='templateSeqRadio']" );
	$($allUnpublishedGamesRadios).each(function() {
		if(this.value == "true"){
			inputName = this.name;
			gameTemplateSeq = this.name.substr(16);
			gameSeq = $("#templateDiv"+gameTemplateSeq +" #gameSeq"+ gameTemplateSeq).val();
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