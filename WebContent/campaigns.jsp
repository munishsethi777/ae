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
<script type="text/javascript" src="js/campaigns.js"></script>
<script type="text/javascript" src="js/campaign_createGame.js"></script>
<script type="text/javascript" src="js/campaign_createUserGroup.js"></script>
    
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
			//{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:80},
			{ text: 'Launch Message', datafield: 'launchMessage',editable:false,width:80},
			{ text: 'Start Date', datafield: 'startDate',editable:false,width:220,cellsformat: 'dd-MM-yy hh.mm tt'},
			{ text: 'End Date', datafield: 'validTilldDate',editable:false,width:220,cellsformat: 'dd-MM-yy hh.mm tt'}
			];

	var dataFieldsCampaign = [
			{ name: 'campSeq', type: 'integer' },
			{ name: 'name', type: 'string' },
			{ name: 'description', type: 'string' },
			{ name: 'validityDays', type: 'string' },
			//{ name: 'isEnabled', type: 'bool' },
			{ name: 'createdOn', type: 'date' },
			{ name: 'lastmodifieddate', type: 'date'},
			{ name: 'launchMessage', type: 'string' },
			{ name: 'startDate', type: 'date' },
			{ name: 'validTillDate', type: 'date' }
			];
	
	
	$(document).ready(function () {
			var editorWidth= "85%";
			var editorHeight = "95%";
			var createWindowId = "jqxCreateCampaignWindow";
			renderGrid("jqxGrid",createWindowId,beanName,dataUrl,deleteCampaignUrl,addUrl,"",columnsCampaign,dataFieldsCampaign,true,editorHeight,editorWidth);
			//$('#'+createWindowId+" #isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
			$('#'+createWindowId).on('open', function (event) { 
				$('#'+createWindowId).jqxWindow({resizable: false,position: { x: 0, y: 0 } }); 
				$("#startDateInput").jqxDateTimeInput({width: '220px', theme: theme ,formatString: 'dd/MM/yyyy hh.mm tt'});
				$("#validTillDateInput").jqxDateTimeInput({width: '220px', theme: theme ,formatString: 'dd/MM/yyyy hh.mm tt'});
				if(editingBeanRow!=null){
					if(editingBeanRow.startDate != undefined){
						$("#startDateInput").jqxDateTimeInput('setDate', getJSDateFromStringForDateTimeInput(editingBeanRow.startDate));
					}
					if(editingBeanRow.validTillDate != undefined){
						$("#validTillDateInput").jqxDateTimeInput('setDate',getJSDateFromStringForDateTimeInput(editingBeanRow.validTillDate));
					}
				}
				//now rendering the usergroup usrs grid on campaign open window.
				renderUserGrid();
				renderUserGroupsGrid();
				loadGameTemplates(getCampaignSeqFromForm());
			});
			$("#addQuestionsWindow").jqxWindow({ 
   				isModal: true, modalOpacity: 0.8,
   				resizable: true, theme: theme, autoOpen: false, 
   				maxWidth:'90%', maxHeight:'80%',width:'90%', height:'80%', showCloseButton: true 
   			});
			createWizardLayout();
			$("#nameInput").jqxInput({	placeHolder : "enter a campaign title", height : 25, width : 500, minLength : 1, maxLength : 256});
			$("#descriptionInput").jqxInput({	placeHolder : "enter a campaign description", height : 25, width : 500, minLength : 1, maxLength : 256});
			$("#launchMessageInput").jqxInput({	placeHolder : "enter a campaign launch message", height : 25, width : 500, minLength : 1, maxLength : 256});
			//savebutton click
			$("#saveCampaignButton").jqxButton({ width: 70, theme: theme });
			createNewEarlierRadios();
	});//end document ready
		
	
			
	
</script>

</head>
<body class='default'>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<%@ include file="grid.jsp" %>
<div id='jqxWidget'>
    <div id="jqxGrid"></div>
	<div id="jqxCreateCampaignWindow">
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
				<div id="createNewGameDiv" style="height:520px;overflow:scroll">
					<div class="selectPendingGamesBlock"></div>
					<div class="selectGameTemplatesBlock"></div>
				</div>
				<div id="useEarlierGameDiv" style="height:520px;display:none">
					<%@ include file="campaignUseEarlierGames.jsp" %>
				</div>
				
			</section>
			<h2>Campaign Trainees</h2>
			<section><!-- Slide 3 -->
				<div id="createSelectUserGroupRadios" style="margin-bottom:6px;">
					<button style="padding:4px 16px;" id="isCreateNewUserGroup">Create new UserGroup</button>
					<button style="padding:4px 16px;" id="isUseEarlierUserGroup">Use earlier UserGroup</button>
				</div>
				<div id="createNewUserGroupDiv" style="height:520px;width:100%;border:thin silver solid">
					<%@ include file="campaignUsersEditor.jsp" %>
				</div>
				<div id="useEarlierUserGroupDiv" style="height:520px;width:100%;display:none">
					<%@ include file="userGroupGridInclude.jsp"%>
				</div>
			</section>
			<h2>Preview Campaign</h2>
			<section>
				<%@ include file="campaignPreview.jsp" %>
			</section>
		</div>	
	</div><!-- Ends createBeanWindow -->
</div><!-- Ends jqxWidget main div -->

<!--  including campaign questions creation file -->
<%@ include file="campaignQuestionsEditorInclude.jsp" %>

<!-- Editing Game Name/Description code here -->
<%-- <%@ include file="campaignEditGameDetailsInclude.jsp" %> --%>
</body>
</html>
