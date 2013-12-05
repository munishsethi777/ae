<script type="text/javascript">
		var pageName = "Images";
		var imagerenderer = function (row, datafield, value) {
		                return '<img style="margin-left: 5px;" height="80" width="100" src="images/userImages/' + value + '"/>';
						}
		var imgdataUrl = "AdminUser?action=getAllImages";
		var deleteUrl = "AdminUser?action=deleteImage";
		var imgColumns = [				
				{ text: 'Title', datafield: 'imageTitle' ,editable:false},
				{ text: 'Image', datafield: 'imagePath' ,editable:false,cellsrenderer: imagerenderer},
				{ text: 'Size', datafield: 'imageSize',editable:false,width:150},
				{ text: 'Created On', datafield: 'imageSaveDate',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
				];

		var imgDataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'imageTitle', type: 'string' },
				{ name: 'imagePath', type: 'image' },
				{ name: 'imageSize', type: 'integer' },
				{ name: 'imageSaveDate', type: 'date' }
				];
		
            
			$(document).ready(function () {
				var editorWidth= "85%";
				var editorHeight = "90%";
				
				renderGrid("jqxGrid","Image",imgdataUrl,deleteUrl,"","",imgColumns,imgDataFields,true,"90%","85%");
				$('#jqxGrid').jqxGrid({ rowsheight: 70}); 
				
		});
</script>
<div id="jqxGrid"></div>