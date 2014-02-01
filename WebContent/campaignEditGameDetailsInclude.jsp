<script>
//passing gameTemplateSeq because the hidden inputs names are using templateIds
function editGameDetails(gameTemplateSeq){
	var gameSeq = $("#selectedTemplateDivId"+gameTemplateSeq+" #gameSeq"+gameTemplateSeq).val();
	$("#editGameEditor #gameEditorSeq").val(gameSeq);
	var gameName = $("#selectedTemplateDivId"+gameTemplateSeq+" #gameTitle"+gameTemplateSeq).html();
	var gameDesc = $("#selectedTemplateDivId"+gameTemplateSeq+" #gameDescription"+gameTemplateSeq).html();
	$("#gameEditorTitle").val(gameName);
	$("#gameEditorDescription").val(gameDesc);
	
	$("#editGameEditor").jqxWindow({ 
			isModal: true, modalOpacity: 0.8,
			resizable: true, theme: theme, autoOpen: true, 
			maxWidth:'400px', maxHeight:'200px',width:'400px', height:'200px', showCloseButton: true 
	});
	
	$("#saveGameDetails").jqxButton({ width: 70, theme: theme });
	$("#saveGameDetails").unbind();
	$("#saveGameDetails").on('click', function(event) {
		saveGameDetails(gameTemplateSeq);
	});
	$("#closeGameDetails").jqxButton({ width: 70, theme: theme });
	$("#closeGameDetails").unbind();
	$("#closeGameDetails").on('click', function(event) {
		$("#editGameEditor").jqxWindow("close");
	});
	$("#gameEditorTitle").jqxInput({height : 25, width : 300, minLength : 1, maxLength : 256});
	$("#gameEditorDescription").jqxInput({height : 25, width : 300, minLength : 1, maxLength : 256});
	
	$("#editGameEditor").jqxWindow("open");	
}
function saveGameDetails(gameTemplateSeq){
	dataRow = $("#editGameDetailsForms").serializeArray();
	var url = "AdminUser?action=saveGameDetails";
	$.getJSON(url,dataRow,function(json){
		$("#selectedTemplateDivId"+gameTemplateSeq+" #gameTitle"+gameTemplateSeq).html(dataRow[1].value);
		$("#selectedTemplateDivId"+gameTemplateSeq+" #gameDescription"+gameTemplateSeq).html(dataRow[2].value);
		$("#editGameEditor").jqxWindow("close");
	});
}
</script>
<div id="editGameEditor" style="display:none">
	<div class="title" style="font-weight:bold">Edit Game Details</div>
	<div>
		<form name="editGameDetailsForms" id="editGameDetailsForms">
			<input type="hidden" name="gameSeq" id="gameEditorSeq"/>
			<table class="formTable">
				<tr>
					<td width="100px">Title:</td>
					<td><input type="text" name="gameTitle" id="gameEditorTitle"/></td>
				</tr>
				<tr>
					<td>Description:</td>
					<td><input type="text" name="gameDescription" id="gameEditorDescription"/></td>
				</tr>
				<tr>
					<td align="right"><input value='Save' type='button' id='saveGameDetails'/></td>
					<td align="left"><input value='Close' type='button' id='closeGameDetails'/></td>
				</tr>
			</table>
		</form>
	</div>
</div>