<script type="text/javascript">
function updateSelectedQuestionGrid(dataRowJson){
	$(gridId).jqxGrid('addrow', null, dataRowJson,null,true);
}

$(document).ready(function () {
	$(gridId).jqxGrid({
	        width: "100%",
	        theme: theme,
	        autoheight: true,
	        sortable: true,
	        altrows: true,
	        enabletooltips: true,
	        editable: true,
	        selectionmode: 'checkbox',
	        columns: [
	          { text: 'Question', datafield: 'quesTitle', width: "50%" },
	          { text: 'Description', datafield: 'description',width: "40%" },
	          { text: 'Points',  datafield: 'points',align: 'right', cellsalign: 'right', width: "5%" },
	          
	        ]
	    });
	    $(gridId).on('rowdoubleclick', function (event){ 
			var args = event.args;
			var rowIndex = args.rowindex;
			var dataRow = $(gridId).jqxGrid('getrowdata', rowIndex);
			alert(dataRow);
	    });
});
</script>
<div id='selectedQuestionGrid'></div>