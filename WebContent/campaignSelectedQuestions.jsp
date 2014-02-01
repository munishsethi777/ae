<!--  all main methods are in "js_creategame.jsp". see this file, how template block generate and question related methods. -->
<div id="selectedQuestionsGrid"></div>
<input type="button" class="deleteSelectedQuestionButton" value="Delete"/>

<script type="text/javascript">
function bindDoubleClickOnSelectedQuestionsGrid() {
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
					$('#isEnabledInput').jqxCheckBox('check');
				}else{
					$('#isEnabledInput').jqxCheckBox('uncheck');
				}
			}
		});
	});
}
</script>