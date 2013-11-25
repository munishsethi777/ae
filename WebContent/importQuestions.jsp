<form id="questionForm" name="questionForm" method="post" enctype="multipart/form-data">
			<p style="margin-top:10px;">Import Question</p>
			<td >
				<label id="Questionfailedlbl"></label>
						<p>Please download the <a href="Questions.xlsx">sample file</a> and fill in questions to upload on server.</p>
						
					</td>
			<table width="500px" style="border:solid silver thin;margin-top:10px;">
	    		<tr>
	    			<td class="gameValueTD">
						<input type="file" name="file" id="file" size="50">
					</td>
					<td class="gameValueTD">
						<label id="quslbl"></label>
					</td>
	    		</tr>
				<tr>
				<td >
				<label id="failedlbl"></label>
						<a href="AdminUser?action=downloadFailedRows" id="downloadQuestionLink">Download Failed Row(s) File Here</a>
						
					</td>
	    			
				</tr>
				<tr>
    			<td class=""></td>
    			<td>
					<input type="button" name="submitQuestionButton" id="submitQuestionButton" value="Submit" />
					<input type="reset" class="btn">
				</td>
    		</tr>
	    	</table>
    	</form>	
		<script type="text/javascript">		
			$(document).ready(function () {
			$("#downloadQuestionLink").hide();
			$("#quslbl").text("")
			$("#Questionfailedlbl").text("")
				$('#submitQuestionButton').on('click', function () {
					$("#downloadQuestionLink").hide();
					$("#quslbl").text("");
					$("#Questionfailedlbl").text("");
					var formData = new FormData($('#questionForm')[0]);
				    $.ajax({
				        url: 'AdminUser?action=importQuestions',  //Server script to process data
				        type: 'POST',
						dataType: "json",
				        xhr: function() {  // Custom XMLHttpRequest
				            var myXhr = $.ajaxSettings.xhr();
				            if(myXhr.upload){ // Check if upload property exists
				                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // For handling the progress of the upload
				            }
				            return myXhr;
				        },
				        //Ajax events
				        success:  function(data){
						$("#quslbl").text(data.jsonArr.length + " Row(s) Sucessfully Imported")
							if(data.jsonArr.length > 0){
							    var i = 0;
								if(typeof $("#jqxQuestiongrid").jqxGrid("getrows") == 'undefined'){
									loadGrid();
								}
								$.each(data.jsonArr, function() {
									$("#jqxQuestiongrid").jqxGrid('addrow', null, data.jsonArr[i],null,true);
									if(typeof isCampaignUI != 'undefined'){
										if(isCampaignUI){
											if(i==0){
												addGameFromImportQues(data.jsonArr);
											}
											updateSelectedQuestionGrid(data.jsonArr[i])
										}
									}
									i=i+1;
								});
							}
								if(data.hasErrors == true){
									$("#Questionfailedlbl").text(data.failedRowCount + " Row(s) Failed to Upload")
									$("#downloadQuestionLink").show();
									
								}
							
						},
						
				        // Form data
				        data: formData,
				        //Options to tell jQuery not to process data or worry about content-type.
				        cache: false,
				        contentType: false,
				        processData: false
				    });
				});
				});
			
				
			function progressHandlingFunction(e){
				    if(e.lengthComputable){
				        $('progress').attr({value:e.loaded,max:e.total});
				    }
			}
			
	        function loadGrid(){
			var theme = "fresh";
            // prepare the data
            var source =
            {
                datatype: "json",
                datafields: [
				{ name: 'seq', type: 'integer' },
				{ name: 'quesTitle', type: 'string' },
				{ name: 'description', type: 'string' },
				{ name: 'points', type: 'string' },
				{ name: 'negativePoints', type: 'string' },
				{ name: 'maxSecondsAllowed', type: 'string' },
				{ name: 'extraAttemptsAllowed', type: 'string'}
				],
            };
            var dataAdapter = new $.jqx.dataAdapter(source);
            $("#jqxQuestiongrid").jqxGrid(
            {
                width: 670,
				height: 300,
                source: dataAdapter,
                theme: theme,
                columns: [				
				{ text: 'Title', datafield: 'quesTitle' ,editable:false,width:"25%"},
				{ text: 'Description', datafield: 'description' ,editable:false},
				{ text: 'Points(+)', datafield: 'points',editable:false,width:80,cellsalign: 'right',align: 'right' },
				{ text: 'Points(-)', datafield: 'negativePoints',editable:false,width:80,cellsalign: 'right',align: 'right' },
				{ text: 'Time Avlb.', datafield: 'maxSecondsAllowed',editable:false,width:80,cellsalign: 'right',align: 'right' },
				{ text: 'Chances', datafield: 'extraAttemptsAllowed',editable:false,width:80,cellsalign: 'right',align: 'right' },
				]
            });
			}
    </script>

<body class='default'>
    <div id='jqxWidget'>
        <div id="jqxQuestiongrid"></div>
    </div>
</body>

	