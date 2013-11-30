<form id="createCampaignForm" name="createCampaignForm" style="padding:10px"/>
					<input type="hidden" name="rowId" id="rowIdInput"/>
					<input type="hidden" name="seq" id="seqInput"/>
					<input type="hidden" name="campaignSeq" id="campaignSeq"/>
					<input type="hidden" name="createdOn" id="createdOnInput"/>
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
								<div style="display:inline-table">From
									<div id="startDateInput"/></div>
								</div>
								
								<div style="display:inline-table;margin-left:20px;">To
									<div id="validTillDateInput"/></div>
								</div>
								
							</td>
						</tr>
						<tr>
							<td>Launch Message</td>
							<td><input name="launchMessage" type="text" id="launchMessageInput"/></td>
						</tr>
						<tr>
							<td>Enabled</td>
							<td><div id="isEnabledInput"></div></td>
						</tr>
						<tr>
							<td><input type="button" style='margin-top: 15px; margin-left: 50px; float: left;' value="Save" id="saveCampaignButton" /></td>
							<td><input type="button" style='margin-left: 5px; margin-top: 15px; float: left;' value="Close" id="closeButton" /></td>
						</tr>
					</table>
				</form>