<form id="createCampaignForm" name="createCampaignForm" method="POST" style="padding:10px;height:520px"/>
			<div class="editorErrorDiv"></div>
			<div class="editorSuccessDiv"></div>
					<input type="hidden" name="rowId" id="rowIdInput"/>
					<input type="hidden" name="campSeq" id="campSeqInput"/>
					<input type="hidden" name="createdOn" id="createdOnInput"/>
					<input type="hidden" name="validityDays" id="validityDaysInput"/>
					<table class="formTable">
						<tr>
							<td>Name</td>
							<td><input name="name" type="text" id="nameInput"/></td>
						</tr>
						<tr>
							<td>Description</td>
							<td><input name="description" type="text" id="descriptionInput"/></td>
						</tr>
						<tr>
							<td>Campaign Dates</td>
							<td>
								<div style="display:inline-table" id="startDateInput"/></div>
								<div style="display:inline-table;margin:-6px 3px 0px 3px"/>To</div>
								<div style="display:inline-table" id="validTillDateInput"/></div>
							</td>
						</tr>
						<tr>
							<td>Launch Message</td>
							<td><input name="launchMessage" type="text" id="launchMessageInput"/></td>
						</tr>
						<!-- <tr>
							<td>Enabled</td>
							<td><div id="isEnabledInput"></div></td>
						</tr> -->
						<!-- <tr>
							<td><input type="button" style='margin-top: 15px; margin-left: 50px; float: left;' value="Save" id="saveCampaignButton" /></td>
							<td><input type="button" style='margin-left: 5px; margin-top: 15px; float: left;' value="Close" id="closeButton" /></td>
						</tr> -->
					</table>
				</form>
<script type="text/javascript">
$(document).ready(function () {
	var campValidatorRules = [
		{ input: '#launchMessageInput', message: 'Launch Message is required!', action: 'keyup, blur', rule: 'required' },
		{ input: '#nameInput', message: 'Campaign Name is required!', action: 'keyup, blur', rule: 'required' }
	];
	$("#saveCampaignButton").jqxButton({ width: 70, theme: theme });
	
	$('#createCampaignForm').jqxValidator({
		animationDuration:5,
		rules: campValidatorRules
	});		
	$("#createCampaignForm").on('validationSuccess', function () {
		//$("#createCampaignForm-iframe").fadeIn('fast');
	});
});
</script>