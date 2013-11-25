    function changeProjectSession(){
            var projectSeq = $("#workspaceProjectSelect").val();
            //$(".parameterTD").html("<img src='images/loading.gif'> loading station channels...");
            $.getJSON("AdminUser?action=changeWorkspaceProject&projectSeq="+projectSeq, function(json){
            	if(json.status == "success"){
            		location.reload();
            	}else{
            		alert("error occured");
            	}
            });
    }

    