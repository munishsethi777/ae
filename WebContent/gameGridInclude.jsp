<script type="text/javascript">
		var gamedataUrl = "AdminUser?action=getAllGames";
		var gameColumns = [				
				{ text: 'Title', datafield: 'gameTitle' ,editable:false},
				{ text: 'Description', datafield: 'gameDescription' ,editable:false},
				{ text: 'Created On', datafield: 'createdOn',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:60}
				];

		var gameDataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'gameTitle', type: 'string' },
				{ name: 'gameTemplate', type: 'integer' },
				{ name: 'gameDescription', type: 'string' },
				{ name: 'createdOn', type: 'date' },
				{ name: 'gameMaxSecondsAllowed', type: 'string' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'lastmodifieddate', type: 'date'}
				];
		
			$(document).ready(function () {
				var editorWidth= "85%";
				var editorHeight = "90%";
				
				renderGrid("gameJqxGrid","Game",gamedataUrl,"","","",gameColumns,gameDataFields,false,"90%","85%");
				
		});
</script>
<div id="gameJqxGrid"></div>