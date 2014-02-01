<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet" href="css/steps/normalize.css">
<link rel="stylesheet" href="css/steps/main.css">
<link rel="stylesheet" href="css/steps/jquery.steps.css">
<script src="js/modernizr-2.6.2.min.js"></script>
<script src="js/jquery.cookie-1.3.1.js"></script>
<script src="js/jquery.steps.js"></script>
    
<script type="text/javascript">
	var pageName = "campaigns";
	var tempSeq = 0;
	var beanName = "Campaign";
	var dataUrl = "AdminUser?action=getAllCampaigns";
	var deleteCampaignUrl = "AdminUser?action=deleteCampaign";
	var addUrl = "AdminUser?action=addCampaign";
	var isShowButtons = true;
	
			
	var columnsCampaign = [
			{ text: 'Name', datafield: 'name',editable:false,width:190  },
			{ text: 'Description', datafield: 'description',editable:false },
			{ text: 'Validity Days', datafield: 'validityDays',editable:false,width:100,cellsalign: 'right',align: 'right'},
			{ text: 'Created On', datafield: 'createdOn',editable:false,width:220,cellsformat: 'dd-MM-yy hh.mm tt'},
			{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:220,cellsformat: 'dd-MM-yy hh.mm tt'},
			{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:80},
			{ text: 'Launch Message', datafield: 'launchMessage',editable:false,width:80},
			{ text: 'Start Date', datafield: 'startDate',editable:false,width:220,cellsformat: 'dd-MM-yy hh.mm tt'},
			{ text: 'End Date', datafield: 'validTilldDate',editable:false,width:220,cellsformat: 'dd-MM-yy hh.mm tt'}
			];

	var dataFieldsCampaign = [
			{ name: 'seq', type: 'integer' },
			{ name: 'name', type: 'string' },
			{ name: 'description', type: 'string' },
			{ name: 'validityDays', type: 'string' },
			{ name: 'isEnabled', type: 'bool' },
			{ name: 'createdOn', type: 'date' },
			{ name: 'lastmodifieddate', type: 'date'},
			{ name: 'launchMessage', type: 'string' },
			{ name: 'campaignSeq', type: 'integer' },
			{ name: 'startDate', type: 'date' },
			{ name: 'validTillDate', type: 'date' }
			];
	
	function getJSDateFromStringForDateTimeInput(dateStr){
		//dateStr = "17-01-2014 12.00 AM";
		var date = parseInt(dateStr.substring(0,2));
		var month = parseInt(dateStr.substring(3,5));
		var year = parseInt(dateStr.substring(6,10));
		var hours = parseInt(dateStr.substring(11,13));
		var minutes = parseInt(dateStr.substring(14,16));
		var ampm = dateStr.substring(17,19);
		if(ampm == "PM"){
			hours = hours + 12;
		}
		var dated = new Date(year,month,date,hours,minutes);
		return dated;
	}
	$(document).ready(function () {
			var editorWidth= "85%";
			var editorHeight = "80%";

			renderGrid("jqxGrid",beanName,dataUrl,deleteCampaignUrl,addUrl,"",columnsCampaign,dataFieldsCampaign,true,editorHeight,editorWidth);
			$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });

			$('#jqxCreateBeanWindow').on('open', function (event) { 
				$('#jqxCreateBeanWindow').jqxWindow({resizable: false,position: { x: 0, y: 0 } }); 
				$("#startDateInput").jqxDateTimeInput({width: '220px', theme: theme ,formatString: 'dd/MM/yyyy hh.mm tt'});
				$("#validTillDateInput").jqxDateTimeInput({width: '220px', theme: theme ,formatString: 'dd/MM/yyyy hh.mm tt'});
				$("#startDateInput").jqxDateTimeInput('setDate', getJSDateFromStringForDateTimeInput(editingBeanRow.startDate));
				$("#validTillDateInput").jqxDateTimeInput('setDate',getJSDateFromStringForDateTimeInput(editingBeanRow.validTillDate));
				//now rendering the usergroup usrs grid on campaign open window.
				renderUserGrid();
				loadGameTemplates($("#seqInput").val());
			});
			$("#addQuestionsWindow").jqxWindow({ 
   				isModal: true, modalOpacity: 0.8,
   				resizable: true, theme: theme, autoOpen: false, 
   				maxWidth:'85%', maxHeight:'80%',width:'85%', height:'80%', showCloseButton: true 
   			});

			$("#nameInput").jqxInput({	placeHolder : "enter a campaign title", height : 25, width : 500, minLength : 1, maxLength : 256});
			$("#descriptionInput").jqxInput({	placeHolder : "enter a campaign description", height : 25, width : 500, minLength : 1, maxLength : 256});
			$("#launchMessageInput").jqxInput({	placeHolder : "enter a campaign launch message", height : 25, width : 500, minLength : 1, maxLength : 256});
			//savebutton click
			$("#saveCampaignButton").jqxButton({ width: 70, theme: theme });
			createWizardLayout();
			createNewEarlierRadios();	
	});//end document ready
		
	function createWizardLayout(){
		$("#jqxCampaignWizard").steps({
			headerTag: "h2",
			bodyTag: "section",
			transitionEffect: "slideLeft",
			enableFinishButton: true,
			enablePagination: true,
			enableAllSteps: true,
			height:"300px",
			/* Events */
		    onStepChanging: function (event, currentIndex, newIndex) { 
		    	clearErrorMessageDivs();
		    	//when a slide loses focus
		    	var bool = true;
		    	if(currentIndex == 0){
		    		bool =  saveCampaignDetails();

		    	}else if(currentIndex == 1){
		    		saveCampaignGames();
		    	}else if(currentIndex == 2){
		    		saveCampaignUserGroup();
		    	}
		    	// when preview slide gets focus
		    	if(newIndex == 3 && bool==true){
		    		previewCampaign();
		    	}
		    	return bool; 
		    },
		    onStepChanged: function (event, currentIndex, priorIndex) { },
		    onFinishing: function (event, currentIndex) { return true; }, 
		    onFinished: function (event, currentIndex) { },
		});
	}
			
	function saveCampaignUserGroup(){
		var userGroupSelectedIndex = $('#createSelectUserGroupRadios').jqxButtonGroup('getSelection');
		if(userGroupSelectedIndex == 1){
			addUserGroupFromEarlier();		
		}else{
			updateUserGroup();
		}
	}
					
	function saveCampaignDetails(){
		var isStepChange = false;
		var validationCampResult = function (isValid) {
			if (isValid) {
				saveCampaignDetailsAction("jqxGrid");
				isStepChange = isValid;
			}
		}
		$('#createCampaignForm').jqxValidator('validate', validationCampResult);
		return isStepChange;
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
				gameSeq = $("#selectedTemplateDivId"+gameTemplateSeq +" #gameSeq"+ gameTemplateSeq).val();
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
	function getCampaignSeqFromForm(){
		var campaignSeq = $("#createCampaignForm #seqInput").val();
		return campaignSeq;
	}

	function saveCampaignDetailsAction(gridId){
		var isValid = false;
		dataRow = {};
		dataRow['rowId'] = $("#createCampaignForm #rowIdInput").val();
		$.each(dataFieldsCampaign,function(index,value){
			dataRow[value.name] = $("#createCampaignForm #"+ value.name +"Input").val();
			if(value.type == "radio"){
				dataRow[value.name] = $('input[name='+ value.name +']:radio:checked').val();
			}
		});
		$.getJSON(addUrl,dataRow,function(json){
			if(json['status'] == 'success'){
				isValid = true;
				dataRow['lastmodifieddate'] = json['lastModified'];	
				if(dataRow['seq'] == null || dataRow['seq'] == "" || dataRow['seq'] == "0"){		
					dataRow['seq'] = json['seq'];				
					if(typeof isCampaignUI != 'undefined'){
						if(isCampaignUI){
							$("#createCampaignForm #seqInput").val(json['seq']);
						}
					}
					dataRow['createdOn'] = json['createdOn'];
					//var commit = $("#"+gridId).jqxGrid('addrow', null, dataRow,null,true);
				}else{
					//var commit = $("#"+gridId).jqxGrid('updaterow', dataRow['rowId'], dataRow,true);
				}
				//$("#"+gridId).jqxGrid('ensurerowvisible', dataRow['rowId']);
			}else{
				displaySaveErrors(json['message']);
			}
			return isValid;
		});
	}
	function createNewEarlierRadios(){
		$("#createSelectGameRadios").jqxButtonGroup({ mode: 'radio' , theme: theme});
		$('#createSelectGameRadios').jqxButtonGroup('setSelection', 0);
		$('#createSelectGameRadios').on('selected', function () { 
			var clickedOptionIndex = $('#createSelectGameRadios').jqxButtonGroup('getSelection');
			if(clickedOptionIndex == 0){
				$("#createNewGameDiv").show();
            	$("#useEarlierGameDiv").hide();
			}else{
				$("#createNewGameDiv").hide();
            	$("#useEarlierGameDiv").show();
			}
		});
		
		$("#createSelectUserGroupRadios").jqxButtonGroup({ mode: 'radio' , theme: theme});
		$('#createSelectUserGroupRadios').jqxButtonGroup('setSelection', 0);
		$('#createSelectUserGroupRadios').on('selected', function () { 
			var clickedOptionIndex = $('#createSelectUserGroupRadios').jqxButtonGroup('getSelection');
			if(clickedOptionIndex == 0){
				$("#createNewUserGroupDiv").hide();
            	$("#useEarlierUserGroupDiv").show();
			}else{
				$("#createNewUserGroupDiv").show();
            	$("#useEarlierUserGroupDiv").hide();
			}
		});
	}
</script>

</head>
<body class='default'>
<%@ include file="menu.jsp" %>
<%@ include file="grid.jsp" %>
<div id='jqxWidget'>
    <div id="jqxGrid"></div>
	<div id="jqxCreateBeanWindow">
		<div class="title" style="font-weight:bold">Create New Campaign</div>
		<div id="jqxCampaignWizard" class="jqxCreateBeanEditor">
		   	<h2>Campaign Information</h2>
		   	<section><!-- Slide 1 -->
		   		<%@ include file="campaignEditorInclude.jsp"%>
			</section>
			<h2>Campaign Games</h2>
			<section><!-- Slide 2  -->
				<div id="createSelectGameRadios" style="clear:both">
					<button style="padding:4px 16px;" id="isCreateNewGame">Create new Games</button>
					<button style="padding:4px 16px;" id="isUseEarlierGame">Use earlier Games</button>
				</div>
				<div id="createNewGameDiv" style="height:400px;overflow:scroll">
					<%@ include file="campaignSelectGameTemplateBlock.jsp" %>
				</div>
				<div id="useEarlierGameDiv" style="display:none">
					<%@ include file="campaignUseEarlierGames.jsp" %>
				</div>
				
			</section>
			<h2>Campaign Trainees</h2>
			<section><!-- Slide 3 -->
				<div id="createSelectUserGroupRadios" style="clear:both;margin-bottom:6px;">
					<button style="padding:4px 16px;" id="isUseEarlierUserGroup">Use earlier UserGroup</button>
					<button style="padding:4px 16px;" id="isCreateNewUserGroup">Create new UserGroup</button>
				</div>
				<div id="useEarlierUserGroupDiv">
					<%@ include file="userGroupGridInclude.jsp"%>
				</div>
				<div id="createNewUserGroupDiv" style="display:none">
					<%@ include file="campaignUsersEditor.jsp"%>
				</div>
				
			</section>
			<h2>Preview Campaign</h2>
			<section><%@ include file="campaignPreview.jsp" %>
			</section>
		</div>	
	</div><!-- Ends createBeanWindow -->
</div><!-- Ends jqxWidget main div -->

<!--  including js scripting for game creation -->
<%@ include file="js_creategame.jsp" %>

<!--  including campaign questions creation file -->
<%@ include file="campaignQuestionsEditorInclude.jsp" %>

<!-- Editing Game Name/Description code here -->
<%@ include file="campaignEditGameDetailsInclude.jsp" %>
</body>
</html>
