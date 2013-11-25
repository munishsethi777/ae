
<script>
	function loadGameTemplates(campaignSeq){
		var getTemplatesUrl = "AdminUser?action=getAllGameTemplates&campaignSeq="+campaignSeq;
		$.getJSON(getTemplatesUrl,function(JSON){
			$.each(JSON, function(index,val){
				content = getGameTemplateDiv(index,val);
				$(content).appendTo(".selectGameTemplatesBlock");
				$("#templateSeqRadio"+val.seq).jqxCheckBox({ width: 20, height: 25, theme: theme });
				$("#templateSeqRadio"+val.seq).on('change', function(event) {
					var checked = event.args.checked;
		            if (checked){
		            	$("#addQuestionLink"+val.seq).show();
		            	$("#selectedTemplateDivId"+val.seq).addClass('backColoredWhite');
		            }else{
		            	$("#addQuestionLink"+val.seq).hide();
		            	$("#selectedTemplateDivId"+val.seq).removeClass('backColoredWhite');
		            }
				});
				if(val.gameSeq != 0){
					$("#templateSeqRadio"+val.seq).jqxCheckBox('check');
				}
				
				
				$("#demo"+val.seq).jqxButton({ width: 70, theme: theme });
				$("#demo"+val.seq).on('click', function(event) {
					alert("demo");
				});
				$("#addQuestionLink"+val.seq).jqxButton({ width: 120, theme: theme });
				$("#addQuestionLink"+val.seq).on('click', function(event){
					tempSeq = event.currentTarget.id;
					tempSeq = tempSeq.replace("addQuestionLink" , "");
					gameSeq = $("#gameSeq"+tempSeq).val();
					openAddQuestionsUI(gameSeq)
					$("#addQuestionsWindow").jqxWindow('open');							
				});
				$("#deleteBeanConfirmation").jqxWindow({ resizable: true, theme: theme, autoOpen: false, width: 450, height: 200, showCloseButton: true });
			});
		});
	}
	
	function getGameTemplateDiv(index,json){
		var content= "";
		content += "<div class='selectGameTemplatesDiv' id='selectedTemplateDivId"+json.seq+"'>";
		content += "<input type='hidden' id='gameTemplateSeq' value='" + json.seq + "'/>";
		content += "<input type='hidden' id='gameSeq"+json.seq +"' value='"+json.gameSeq +"'/>";
			content += "<div class='gameIcon'>";
				content += "<img src='" + json.imagePath +"'/>";
			content += "</div>";
			content += "<div class='gameTemplateDetailsDiv'>";
			content += "<div style='float:right' id='templateSeqRadio"+ json.seq +"'></div>";
			content += '<h1>' + json.name + '</h1>';
		   	content += '<div class="smallFonts" style="height:70px;">' + json.description + '</div>';
		content += '<div style="margin-top:10px;">';
		content += "<input value='Demo' type='button' id='demo"+json.seq+"'/>";
		content += "<input value='Add Questions' type='button' id='addQuestionLink"+json.seq+"' class='marL10' style='display:none;margin-left:10px'/>";
		content += '</div>';
		content += "</div>";
	   	content += "<br class='clr'>";
			content += '</div>';
			return content;
	}
</script>

<div class="selectGameTemplatesBlock"></div>