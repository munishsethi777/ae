<script>

function selectedQuestionGridInit(){
	/* $(".deleteSelectedQuestionButton").jqxButton({ width: 70, theme: theme });
	$(".deleteSelectedQuestionButton").click(function () {
		var selectedRowIndexes = $("#selectedQuestionsGrid").jqxGrid('selectedrowindexes');
		if(selectedRowIndexes.length > 0 ){
			$("#deleteBeanConfirmation .countDeletion").html(selectedRowIndexes.length);
			$("#deleteBeanConfirmation").jqxWindow('open');
		}else{
			$("#NoSelection").jqxWindow('open');
		}
	});
	$(".selectQuestionsDiv").jqxPanel({ width:"100%",height:"100%", theme: theme }); */
    /* $("#selectedQuestionsGrid").jqxGrid({
        width: "90%",
        height: "100px",
        theme: theme,
        autoheight: true,
        sortable: true,
        altrows: true,
        enabletooltips: true,
        editable: true,
        selectionmode: 'checkbox',
        columns: [
          { text: 'Question', datafield: 'quesTitle', width: "50%",editable:false },
          { text: 'Description', datafield: 'description',width: "40%",editable:false },
          { text: 'Points',  datafield: 'points',align: 'right', cellsalign: 'right', width: "10%",editable:false },
          
        ]
    });
    $("#selectedQuestionsGrid").on('rowdoubleclick', function (event){ 
		var args = event.args;
		var rowIndex = args.rowindex;
		var dataRow = $("#selectedQuestionsGrid").jqxGrid('getrowdata', rowIndex);
		alert(dataRow.id);
    });
     var dataRow = [
       	{	"id": "1",
			"quesTitle": "Hot Chocolate",
			"description": "Chocolate Beverage",
			"points": "370",
			"bane": "munish"
   		},
       	{	"id": "2",
			"quesTitle": "Cold Chocolate",
			"description": "Chocolate Beverage",
			"points": "30",
			"bane": "munish sethi"
   		},
       	{	"id": "3",
			"quesTitle": "Mild Chocolate",
			"description": "Chocolate Beverage",
			"points": "320",
			"bane": "munish sethi"
   		},
       	{	"id": "4",
			"quesTitle": "Mild Coke",
			"description": "Chocolate Beverage",
			"points": "320",
			"bane": "munish sethi"
   		},
       	{	"id": "5",
			"quesTitle": "Mild Soup",
			"description": "Chocolate Beverage",
			"points": "320",
			"bane": "munish sethi"
   		}
      ]; */
    /* updateSelectedQuestionGrid(dataRow); */
}
</script>

<script>
function loadSelectedQuestionsGrid(gameSeq){

	var selectedQuestionsEditorWidth= "70%";
	var selectedQuestionsEditorHeight = "100%";
	var selQuesdataUrl = "AdminUser?action=getQuestionsSelectedOnGame&gameSeq="+gameSeq;
	var deleteUrl = "AdminUser?action=deleteQuestions";
	var validatorRules = [];
			
	var columns = [				
			{ text: 'Title', datafield: 'quesTitle' ,editable:false,width:"25%"},
			{ text: 'Description', datafield: 'description' ,editable:false},
			{ text: 'Points(+)', datafield: 'points',editable:false,width:80,cellsalign: 'right',align: 'right' },
			{ text: 'Points(-)', datafield: 'negativePoints',editable:false,width:80,cellsalign: 'right',align: 'right' },
			{ text: 'Time Avlb.', datafield: 'maxSecondsAllowed',editable:false,width:80,cellsalign: 'right',align: 'right' },
			{ text: 'Chances', datafield: 'extraAttemptsAllowed',editable:false,width:80,cellsalign: 'right',align: 'right' },
			{ text: 'Created On', datafield: 'createdOn',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
			{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
			{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:60}
			];

	var dataFields = [
			{ name: 'seq', type: 'integer' },
			{ name: 'quesTitle', type: 'string' },
			{ name: 'description', type: 'string' },
			{ name: 'points', type: 'string' },
			{ name: 'negativePoints', type: 'string' },
			{ name: 'maxSecondsAllowed', type: 'string' },
			{ name: 'extraAttemptsAllowed', type: 'string' },
			{ name: 'isEnabled', type: 'bool' },
			{ name: 'createdOn', type: 'date' },
			{ name: 'lastmodifieddate', type: 'date'},
			{ name: 'answer1', type: 'string'},
			{ name: 'isAnswerCorrect', type: 'string' },
			{ name: 'answer2', type: 'string'},
			{ name: 'answer3', type: 'string'},
			{ name: 'answer4', type: 'string'}
			
			];
	renderGrid("selectedQuestionsGrid",beanName,selQuesdataUrl,deleteUrl,addUrl,validatorRules,
			columns,dataFields,true,selectedQuestionsEditorHeight,selectedQuestionsEditorWidth);
	
}

</script>
<div id="selectedQuestionsGrid"></div>
<input type="button" class="deleteSelectedQuestionButton" value="Delete"/>