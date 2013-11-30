<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript">
		var pageName = "campaigns";
		var tempSeq = 0;
		var pageNo = 1;
		var beanName = "Campaign";
		var dataUrl = "AdminUser?action=getAllCampaigns";
		var deleteUrl = "AdminUser?action=deleteCampaign";
		var addUrl = "AdminUser?action=addCampaign";
		var isSelectedUserGroupTab = true;
	    isCampaignUI = true;
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
				{ name: 'launchMessage', type: 'string' }
				//{ name: 'startDate', type: 'date' },
				//{ name: 'validTillDate', type: 'date' }
				];
		var isShowButtons = true;

		$(document).ready(function () {
				var editorWidth= "70%";
				var editorHeight = "100%";
				renderGrid("jqxGrid",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,true,editorHeight,editorWidth);
				$("#isEnabledInput").jqxCheckBox({ width: 120, height: 25, theme: theme });
				$('#jqxCreateBeanWindow').on('open', function (event) { 
					pageNo = 1;
					$('#jqxCreateBeanWindow').jqxWindow({ position: 'center',resizable: false }); 
					$("#jqxCampaignWizard").jqxScrollView('changePage', 0);//first slide always
					dataUserUrl = "AdminUser?action=getSelectedUserGroupsByCampaign&campaignSeq=" + $("#seqInput").val() 	
					addUserUrl = "AdminUser?action=addUserGroupFromCampaign&campaignSeq=" + $("#seqInput").val();	
					renderGrid("userJqxGrid",beanUserName,dataUserUrl,deleteUserUrl,addUserUrl,userValidatorRules,userColumns,userDataFields,true,"100%","80%");
					loadGameTemplates($("#seqInput").val())
				});
				$("#addQuestionsWindow").jqxWindow({ 
    				isModal: true, modalOpacity: 0.8,
    				resizable: true, theme: theme, autoOpen: false, 
    				maxWidth:'80%', maxHeight:'90%',width:'80%', height:'90%', showCloseButton: true 
    			});
				
				
				$("#nameInput").jqxInput({	placeHolder : "enter a campaign title", height : 25, width : 500, minLength : 1, maxLength : 256});
				$("#descriptionInput").jqxInput({	placeHolder : "enter a campaign description", height : 25, width : 500, minLength : 1, maxLength : 256});
				$("#startDateInput").jqxCalendar({width: 220, height: 220, theme: theme });
				$("#validTillDateInput").jqxCalendar({width: 220, height: 220, theme: theme });
				$("#launchMessageInput").jqxInput({	placeHolder : "enter a campaign launch message", height : 25, width : 500, minLength : 1, maxLength : 256});
				//savebutton click
				$("#saveCampaignButton").jqxButton({ width: 70, theme: theme });
				$("#saveCampaignButton").click(function () {
					var validationResult = function (isValid) {
						if (isValid) {
							submitAddRecord("jqxGrid");
						}
					}
					$('#createCampaignForm').jqxValidator('validate', validationResult);
				});	
				$('#createCampaignForm').jqxValidator({
					animationDuration:5,
					rules: validatorRules
				});		
				$("#createCampaignForm").on('validationSuccess', function () {
					$("#createCampaignForm-iframe").fadeIn('fast');
				});
				$("#closeButton").click(function () {
					$('#jqxCreateBeanWindow').jqxWindow('close'); 
				});
				createWizardLayout();
				createNewEarlierRadios();	
		});//end document ready
		
		function createWizardLayout(){
			//Wizard Styling and Scripting
			$('#jqxCampaignWizard').jqxScrollView({
					width : '100%',
					height : '100%',
					theme : theme,
					showButtons: false
			});
			//wizard buttons
			
			$("#movePreviousCampaignWizard").jqxButton({ width: 70, theme: theme });
			$("#movePreviousCampaignWizard").click(function () {
				$("#jqxCampaignWizard").jqxScrollView('back');
				backPage();
			});
			 
			$("#moveNextCampaignWizard").jqxButton({ width: 70, theme: theme });
			$("#moveNextCampaignWizard").click(function () {
				if(pageNo == 1){
					saveCampaign();
				}else if(pageNo == 2){
					pageNo = pageNo + 1;
					$("#jqxCampaignWizard").jqxScrollView('forward');
				}else if(pageNo == 3){
					saveUserGroup();
				}
			
			});
			
			$("#closeCampaignWizard").jqxButton({ width: 70, theme: theme });
			$("#closeCampaignWizard").click(function () {
				$("#jqxCreateBeanWindow").jqxWindow('close'); 
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
					isSelectedUserGroupTab = true;
				}else{
					$("#createNewUserGroupDiv").hide();
	            	$("#useEarlierUserGroupDiv").show();
					isSelectedUserGroupTab = false;
				}
			});
		}
		function forwardPage(){
			if(pageNo < 4){
				pageNo = pageNo + 1;
				$("#jqxCampaignWizard").jqxScrollView('forward');
			}
			
		}
		function backPage(){
			if(pageNo > 1){
				pageNo = pageNo - 1;
				$("#jqxCampaignWizard").jqxScrollView('back');
			}
		}
		
		function saveCampaign(){
			var validationResult = function (isValid) {
				if (isValid) {
					submitAddRecord("jqxGrid");	
					forwardPage();
				}
			}
			$('#createCampaignForm').jqxValidator('validate', validationResult);
		}
		
		function saveUserGroup(){
			if(isSelectedUserGroupTab){
				var validationResult = function (isValid) {
					if (isValid) {
						addUsersGroupFromEditor();
						forwardPage();
					}
				}
				$('#createUserForm').jqxValidator('validate', validationResult);
			}else{
				addUserGroupFromEarlier();
				forwardPage();
			}
			
		}
		
		
</script>
<body class='default'>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<%@ include file="grid.jsp" %>
<div id='jqxWidget'>
	<label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">Campaigns Information</label><br>
	<label style="font-family:verdana;font-size: 12px;color:grey">View, Create, Edit, Bulk Delete or Find through various users available in the database.</label>

    <div id="jqxGrid"></div>
	<div id="jqxCreateBeanWindow">
		<div class="title" style="font-weight:bold">Create New Project</div>
		<span style="float:right;margin:10px;" id="campaignWizardButtons">
			<label>Step 1 of 3</label>
			<input type="button" value="previous" id="movePreviousCampaignWizard" />
			<input type="button" value="next" id="moveNextCampaignWizard" />
			<input type="button" value="close" id="closeCampaignWizard" />
		</span>
			<div id="jqxCampaignWizard" class="jqxCreateBeanEditor">
			   	<div><!-- Slide 1 -->
			   		<%@ include file="campaignEditorInclude.jsp"%>
				</div>
				<div><!-- Slide 2  -->
					<div id="createSelectGameRadios" style="clear:both">
						<button style="padding:4px 16px;" id="isCreateNewGame">Create new Game</button>
						<button style="padding:4px 16px;" id="isUseEarlierGame">Use earlier Game</button>
					</div>
					<div id="createNewGameDiv">
						<%@ include file="campaignSelectGameTemplateBlock.jsp"%>
					</div>
					<div id="useEarlierGameDiv" style="display:none">
						<%@ include file="gameGridInclude.jsp"%>
					</div>
			</div>
			<div style="width:100%;height:100%"><!-- Slide 3 -->
				<div id="createSelectUserGroupRadios" style="clear:both">
					<button style="padding:4px 16px;" id="isCreateNewUserGroup">Create new UserGroup</button>
					<button style="padding:4px 16px;" id="isUseEarlierUserGroup">User earlier UserGroup</button>
				</div>
				<div id="createNewUserGroupDiv" style="width:100%;height:100%">
					<%@ include file="campaignUsersEditor.jsp"%>
				</div>
				<div id="useEarlierUserGroupDiv" style="display:none">
					<%@ include file="userGroupGridInclude.jsp"%>
				</div>
				
			</div>
			<div><!-- Slide 4 -->
				Preview Window
			</div>
		</div>	
	</div><!-- Ends createBeanWindow -->
</div><!-- Ends jqxWidget main div -->

<!--  including js scripting for game creation -->
<%@ include file="js_creategame.jsp" %>

<%@ include file="campaignQuestionsEditorInclude.jsp" %>

</body>
</html>
