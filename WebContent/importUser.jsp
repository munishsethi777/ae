<script type="text/javascript">
		$(document).ready(function () {
			$("#submitImportButton").jqxButton({ width: 80, height: 25, theme: theme });
			$("#resetImportButton").jqxButton({ width: 80, height: 25, theme: theme });
		});
</script>

<form id="frm1" name="frm1" method="post" enctype="multipart/form-data">
	<p style="margin-top:10px;">Import Users</p>
	<label id="failedlbl"></label>
	<a href="Users.xlsx">Download Sample File</a>

		<table width="500px" style="border:solid silver thin;margin-top:10px;">
	    		<tr>
	    			<td class="gameValueTD">
						<input type="file" name="file" id="file" size="50">
					</td>
					<td class="gameValueTD">
						<label id="responseLbl"></label>
					</td>
	    		</tr>
				<tr>
					<td>
						<label id="failedlbl"></label>
						<a href="AdminUser?action=downloadFailedRows" id="downloadLink">Download Failed Row(s) File Here</a>
					</td>
	    			
				</tr>
				<tr>
    				<td><div class="loadingGif"><img src="images/loading.gif"></div></td>
    			<td>
					<input type="button" name="submitButton" id="submitImportButton" value="Import" />
					<input type="reset" id="resetImportButton" class="btn" value="Reset">
				</td>
    			</tr>
	    </table>
   </form>	
		<script type="text/javascript">
		
			$(document).ready(function () {
			$("#downloadLink").hide();
			$("#responseLbl").text("");
			$("#failedlbl").text("");
			$(".loadingGif").hide();
				$('#submitImportButton').on('click', function () {
					$("#downloadLink").hide();
					$("#responseLbl").text("");
					$("#failedlbl").text("");
					$(".loadingGif").show();
					var formData = new FormData($('#frm1')[0]);
				    $.ajax({
				        url: 'AdminUser?action=importUsers',  //Server script to process data
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
							$(".loadingGif").hide();
							$("#responseLbl").text(data.jsonArr.length + " Row(s) Sucessfully Imported")
							if(data.jsonArr.length > 0){
							    var i = 0;
								if(typeof $("#jqxUsergrid").jqxGrid("getrows") == 'undefined'){
									loadGrid();
								}
								$.each(data.jsonArr, function() {
									$("#jqxUsergrid").jqxGrid('addrow', null, data.jsonArr[i],null,true);
									if(typeof isCampaignUI != 'undefined'){
										if(isCampaignUI){
											if(i==0){
												addUsersGroupFromImportUsers(data.jsonArr);
											}
											updateSelectedUserGrid(data.jsonArr[i])
										}
									}
									i=i+1;
								});
							}
							if(data.hasErrors == true){
								$("#failedlbl").text(data.failedRowCount + " Row(s) Failed to Upload")
								$("#downloadLink").show();
							}
						},
						data: formData,
				        //Options to tell jQuery not to process data or worry about content-type.
				        cache: false,
				        contentType: false,
				        processData: false
					});//end ajax call
				});//end submit button
			});//end document ready
			
				
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
				{ name: 'name', type: 'string' },
				{ name: 'email', type: 'string' },
				{ name: 'mobile', type: 'string' },
				{ name: 'location', type: 'string' },
				{ name: 'username', type: 'string' },
				{ name: 'password', type: 'string' },
				{ name: 'createdOn', type: 'date' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'lastmodifieddate', type: 'date'}
				],
            };
            var dataAdapter = new $.jqx.dataAdapter(source);
            $("#jqxgrid").jqxGrid(
            {
                width: 670,
				height: 300,
                source: dataAdapter,
                theme: theme,
                columns: [{ text: 'First Name', datafield: 'name' ,editable:false,width:"20%"},
				{ text: 'Email Id', datafield: 'email' ,editable:false,width: "20%"},
				{ text: 'UserName', datafield: 'username',editable:false,width: "20%"},
				{ text: 'Location', datafield: 'location' ,editable:false},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width: 80}]
            });
			}
    </script>

<body class='default'>
    <div id='jqxWidget'>
        <div id="jqxUsergrid"></div>
    </div>
</body>

	