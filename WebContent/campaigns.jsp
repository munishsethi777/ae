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
		var deleteUrl = "AdminUser?action=deleteCampaign";
		var addUrl = "AdminUser?action=addCampaign";
	
		var validatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' }];
				
		var columns = [
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

		var dataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'name', type: 'string' },
				{ name: 'description', type: 'string' },
				{ name: 'validityDays', type: 'string' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'createdOn', type: 'date' },
				{ name: 'lastmodifieddate', type: 'date'},
				{ name: 'launchMessage', type: 'string' },
				{ name: 'campaignSeq', type: 'integer' }
				//{ name: 'validTillDate', type: 'date' }
				];
		var isShowButtons = true;

		$(document).ready(function () {
				var editorWidth= "85%";
				var editorHeight = "80%";
				renderGrid("jqxGrid",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,true,editorHeight,editorWidth);
				$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
				$('#jqxCreateBeanWindow').on('open', function (event) { 
					$('#jqxCreateBeanWindow').jqxWindow({resizable: false,position: { x: 0, y: 0 } }); 
					$("#jqxCampaignWizard").jqxScrollView('changePage', 0);//first slide always
					//now rendering the usergroup usrs grid on campaign open window.
					renderUserGrid();
					//in edit mode
					loadUserGroupDetails();
					loadGameTemplates($("#seqInput").val());
				});
				$("#addQuestionsWindow").jqxWindow({ 
    				isModal: true, modalOpacity: 0.8,
    				resizable: true, theme: theme, autoOpen: false, 
    				maxWidth:'90%', maxHeight:'90%',width:'90%', height:'90%', showCloseButton: true 
    			});
				
				
				$("#nameInput").jqxInput({	placeHolder : "enter a campaign title", height : 25, width : 500, minLength : 1, maxLength : 256});
				$("#descriptionInput").jqxInput({	placeHolder : "enter a campaign description", height : 25, width : 500, minLength : 1, maxLength : 256});
				$("#startDateInput").jqxCalendar({width: 220, height: 220, theme: theme });
				$("#validTillDateInput").jqxCalendar({width: 220, height: 220, theme: theme });
				$("#launchMessageInput").jqxInput({	placeHolder : "enter a campaign launch message", height : 25, width : 500, minLength : 1, maxLength : 256});
				//savebutton click
				$("#saveCampaignButton").jqxButton({ width: 70, theme: theme });
	
				$('#createCampaignForm').jqxValidator({
					animationDuration:5,
					rules: validatorRules
				});		
				$("#createCampaignForm").on('validationSuccess', function () {
					$("#createCampaignForm-iframe").fadeIn('fast');
				});
				
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
			    	if(newIndex == 1){
			    		saveCampaignDetails();
			    	}else if(newIndex == 2){
			    		saveCampaignGames();
			    	}else if(newIndex == 3){
			    		previewCampaign();
			    	}
			    	return true; 
			    },
			    onStepChanged: function (event, currentIndex, priorIndex) { },
			    onFinishing: function (event, currentIndex) { return true; }, 
			    onFinished: function (event, currentIndex) { },
			});
			
			function saveCampaignDetails(){
				saveCampaignDetailsAction("jqxGrid");
				/* var validationResultCampaign = function (isValidCampaign) {
					if (isValidCampaign) {
						alert("sae camp");
						saveCampaignDetailsAction("jqxGrid");
					}
				};
				$('#createCampaignForm').jqxValidator('validate', validationResultCampaign); */
			}
			function saveCampaignGames(){
				//var gameRadioSelectedIndex = $('#createSelectGameRadios').jqxButtonGroup('getSelection');
				//if(gameRadioSelectedIndex == 1){
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
							gameSeq = this.name.substr(16);
							gameSeqs.push(gameSeq);
						}
					});
					var campaignSeq = $("#createCampaignForm #seqInput").val();
					var dataRow = {};
					dataRow["campaignSeq"] = campaignSeq;
					dataRow["gamesSeqs"] = gameSeqs.toString();
					$.getJSON("AdminUser?action=setGamesOnCampaign",dataRow,function(json){
						alert(json);
					});
				//}
			}
			
			
		}
		function saveCampaignDetailsAction(gridId){
			dataRow = {};
			dataRow['rowId'] = $("#createCampaignForm #rowIdInput").val();
			$.each(dataFields,function(index,value){
				dataRow[value.name] = $("#createCampaignForm #"+ value.name +"Input").val();
				if(value.type == "radio"){
					dataRow[value.name] = $('input[name='+ value.name +']:radio:checked').val()
				}
			});
			$.getJSON(addUrl,dataRow,function(json){
				if(json['status'] == 'success'){
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
					$("#createNewUserGroupDiv").show();
	            	$("#useEarlierUserGroupDiv").hide();
				}else{
					$("#createNewUserGroupDiv").hide();
	            	$("#useEarlierUserGroupDiv").show();
				}
			});
		}
</script>
</head>
<body class='default'>
<%@ include file="menu.jsp" %>
<%@ include file="grid.jsp" %>
<div id='jqxWidget'>
	<!-- <label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">Campaigns Information</label><br>
	<label style="font-family:verdana;font-size: 12px;color:grey">View, Create, Edit, Bulk Delete or Find through various users available in the database.</label>
 -->
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
					<button style="padding:4px 16px;" id="isCreateNewUserGroup">Create new UserGroup</button>
					<button style="padding:4px 16px;" id="isUseEarlierUserGroup">Use earlier UserGroup</button>
				</div>
				<div id="createNewUserGroupDiv">
					<%@ include file="campaignUsersEditor.jsp"%>
				</div>
				<div id="useEarlierUserGroupDiv" style="display:none">
					<%@ include file="userGroupGridInclude.jsp"%>
				</div>
				
			</section>
			<h2>Preview Campaign</h2>
			<section><!-- Slide 4 -->
				<%@ include file="campaignPreview.jsp"%>
			</section>
		</div>	
	</div><!-- Ends createBeanWindow -->
</div><!-- Ends jqxWidget main div -->

<!--  including js scripting for game creation -->
<%@ include file="js_creategame.jsp" %>

<%@ include file="campaignQuestionsEditorInclude.jsp" %>

</body>
</html>
