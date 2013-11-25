	<link rel="stylesheet" href="js/jqwidgets/styles/jqx.base.css" type="text/css" />
    <link rel="stylesheet" href="js/jqwidgets/styles/jqx.fresh.css" type="text/css" />
    <script type="text/javascript" src="js/scripts/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxcore.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxdata.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxbuttons.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxscrollbar.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxmenu.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxgrid.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxgrid.selection.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxgrid.filter.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxlistbox.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxdropdownlist.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxcheckbox.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxwindow.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxgrid.pager.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxgrid.sort.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxgrid.edit.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxvalidator.js"></script>
	<script type="text/javascript" src="js/jqwidgets/globalization/globalize.js"></script> 
	<script type="text/javascript" src="js/jqwidgets/jqxcalendar.js"></script> 
	<script type="text/javascript" src="js/jqwidgets/jqxdatetimeinput.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxtabs.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxradiobutton.js"></script>
    <script type="text/javascript" src="js/jqwidgets/jqxbuttongroup.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxinput.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxnavigationbar.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxexpander.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxsplitter.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxpanel.js"></script>
	<script type="text/javascript" src="js/jqwidgets/jqxscrollview.js"></script>
	
	<script type="text/javascript" src="js/satyaJqxGrid.js"></script>
	<script>
		$(document).ready(function () {
			
			$("#deleteBeanConfirmation").jqxWindow({ resizable: true, theme: theme, autoOpen: false, width: 450, height: 200, showCloseButton: true });
			$("#NoSelection").jqxWindow({ resizable: true, theme: theme, autoOpen: false, width: 300, height: 175, showCloseButton: true });
			$("#closeButton").jqxButton({ width: 70, theme: theme });
			$("#saveButton").jqxButton({ width: 70, theme: theme });
			$("#yesDelete").jqxButton({ width: 70, theme: theme });
			$("#noDelete").jqxButton({ width: 70, theme: theme });
			$("#okBtn").jqxButton({ width: 50,theme: theme });

		});

	</script>
	
	
	<div id="deleteBeanConfirmation">
	   <div class="title" style="font-weight:bold">Delete Selected Items</div>
	   <div style="padding:15px;">
			<p>Do you really want to delete the selected <label class="countDeletion"></label> items? Click Yes to delete and No to cancel the deletion.</p>
			<br>
			<p align="center">
				<input type="button" id="yesDelete" onClick="javascript:submitDeleteRecord('jqxGrid');" name="yes" value="Yes"/>
				<input type="button" id="noDelete" onClick="$('#deleteBeanConfirmation').jqxWindow('close');" name="no" value="No"/>
			</p>
	   </div>
	</div>
	<div id="NoSelection">
	   <div class="title" style="font-weight:bold">Select Items</div>
	   <div style="padding:15px;">
			<p>Please Select Item for delete.</p>
			<br>
			<p align="center">
				<input type="button" id="okBtn" onClick="$('#NoSelection').jqxWindow('close');" name="ok" value="Ok"/>
			</p>
	   </div>
	</div>