function createWizardLayout(){
	$("#jqxCampaignWizard").steps({
		headerTag: "h2",
		bodyTag: "section",
		transitionEffect: "slideLeft",
		enableFinishButton: true,
		enablePagination: true,
		enableAllSteps: false,
		height:"550px",
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
				
function saveCampaignDetails(){
	var isStepChange = false;
	var validationCampResult = function (isValid) {
		if (isValid) {
			dataRow = {};
			dataRow['rowId'] = $("#createCampaignForm #rowIdInput").val();
			$.each(dataFieldsCampaign,function(index,value){
				dataRow[value.name] = $("#createCampaignForm #"+ value.name +"Input").val();
				if(value.type == "radio"){
					dataRow[value.name] = $('input[name='+ value.name +']:radio:checked').val();
				}
			});
			saveCampaignDetailsAction(dataRow,"jqxGrid");
			isStepChange = isValid;
		}
	}
	$('#createCampaignForm').jqxValidator('validate', validationCampResult);
	return isStepChange;
}

function saveCampaignDetailsAction(dataRow,gridId){
	$.getJSON(addUrl,dataRow,function(json){
		if(json['status'] == 'success'){
			dataRow['lastmodifieddate'] = json['lastModified'];	
			if(dataRow['campSeq'] == null || dataRow['campSeq'] == "" || dataRow['campSeq'] == "0"){		
				dataRow['seq'] = json['seq'];				
				if(typeof isCampaignUI != 'undefined'){
					if(isCampaignUI){
						$("#createCampaignForm #campSeqInput").val(json['seq']);
					}
				}
				dataRow['createdOn'] = json['createdOn'];
				//var commit = $("#"+gridId).jqxGrid('addrow', null, dataRow,null,true);
			}else{
				//var commit = $("#"+gridId).jqxGrid('updaterow', dataRow['rowId'], dataRow,true);
			}
			return true;
		}else{
			displaySaveErrors(json['message']);
			return false;
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
function getCampaignSeqFromForm(){
	var campaignSeq = $("#createCampaignForm #campSeqInput").val();
	return campaignSeq;
}