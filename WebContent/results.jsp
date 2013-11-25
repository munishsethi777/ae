<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript">
    	var pageName = "results";
    	var beanName = "Games";
		var dataUrl = "AdminUser?action=getResultsForGrid&campaignSeq=1";
		var deleteUrl = "";
		var addUrl = "";
		var validatorRules = [
				{ }];
				
		var columns = [				
				{ text: 'User', datafield: 'userName' ,editable:false,width:180},
				{ text: 'Game', datafield: 'gameName' ,editable:false,width:180},
				{ text: 'Set', datafield: 'setName',editable:false},
				{ text: 'Score', datafield: 'totalScore',editable:false,width:80},
				{ text: 'Time', datafield: 'timeTaken',editable:false,width:80},
				{ text: 'Dated', datafield: 'createdOn',editable:false,cellsformat: 'dd-MM-yy hh.mm tt',width:150}
			];

		var dataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'userName', type: 'string' },
				{ name: 'gameName', type: 'string' },
				{ name: 'totalScore', type: 'string' },
				{ name: 'timeTaken', type: 'string' },
				{ name: 'campaignName', type: 'string' },
				{ name: 'setName', type: 'string' },
				{ name: 'userGroupName', type: 'string' },
				{ name: 'createdOn', type: 'date'}
			];
		var isShowButtons = false;
		
		
		$(document).ready(function () {
				var editorWidth= "1000px";
				var editorHeight = "600px";
				renderGrid("jqxGrid",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,false,editorHeight,editorWidth);
				renderCampaignsDD();
				renderDD(null, "userGroupsDD");
				renderDD(null, "assessmentSetsDD");
				renderDD(null, "gamesDD");
				$(".searchBtn").jqxButton({ width: '70', theme: theme });
				$(".searchBtn").on('click', function (event) {
					var item = $("#campaignsDD").jqxDropDownList('getSelectedItem');
					campaignSeq = item.value;
					
					var source1 = {
						url:"AdminUser?action=getResultsForGrid&campaignSeq=" + campaignSeq,
						id: 'seq',
						datatype: "json",
						datafields:dataFields,
					};
					var dataAdapter = new $.jqx.dataAdapter(source1);
					$("#jqxGrid").jqxGrid({source:dataAdapter});
					
					
				});
				
				
				
		});//end document ready	
		
		//common methods for DropDowns
		function renderDD(url, id){
			var source = {
				datatype: "json",
				datafields: [
					{ name: 'seq' },
					{ name: 'name' }
				],
				url: url,
				async: false
			};
			var dataAdapter = new $.jqx.dataAdapter(source);
			$("#"+id).jqxDropDownList({
				selectedIndex: 0, source: dataAdapter, displayMember: "name", valueMember: "seq", width: 200, height: 25, theme: theme
			});
		}
		function renderCampaignsDD(){
			var url = "AdminUser?action=getAllCampaigns";
			var source = {
				datatype: "json",
				datafields: [
					{ name: 'seq' },
					{ name: 'name' }
				],
				url: url,
				async: false
			};
			var dataAdapter = new $.jqx.dataAdapter(source);
			$("#campaignsDD").jqxDropDownList({
				selectedIndex: 0, source: dataAdapter, displayMember: "name", valueMember: "seq", width: 200, height: 25, theme: theme
			});
			$("#campaignsDD").on('select', function (event) {
				if (event.args) {
					var item = event.args.item;
					if (item) {
						campaignSeq = item.originalItem.seq;
						renderUserGroupsDD(campaignSeq);
						renderAssessmentSetsDD(campaignSeq);
					}
				}
			});
		}
		function renderUserGroupsDD(campaignSeq){
			var url = "AdminUser?action=getUserGroupsByCampaign&campaignSeq="+campaignSeq;
			renderDD(url,"userGroupsDD");
		}
		function renderAssessmentSetsDD(campaignSeq){
			var url = "AdminUser?action=getAssessmentSetsByCampaign&campaignSeq="+campaignSeq;
			renderDD(url,"assessmentSetsDD");
			$("#assessmentSetsDD").on('select', function (event) {
				if (event.args) {
					var item = event.args.item;
					if (item) {
						selectedSeq = item.originalItem.seq;
						renderGamesDD(selectedSeq);
					}
				}
			});
		}
		function renderGamesDD(assessmentSetSeq){
			var url = "AdminUser?action=getGamesByAssessmentSet&setSeq="+assessmentSetSeq;
			renderDD(url,"gamesDD");
			$("#gamesDD").on('select', function (event) {
				if (event.args) {
					var item = event.args.item;
					if (item) {
						selectedGameSeq = item.originalItem.seq;
					}
				}
			});
		}
		
		
    </script>
</head>
<body class='default'>
	<%@ include file="header.jsp" %>
	<%@ include file="menu.jsp" %>
	<%@ include file="grid.jsp" %>
    <div id='jqxWidget'>
		<label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">Results Information</label><br>
		<label style="font-family:verdana;font-size: 12px;color:grey">Search by Campaign, Assessment Set and Users for their Results</label>
        <div style="margin:5px 0px 5px 0px;" class="searchFilters">
        		<table>
					<tr>
						<td>Campaign:<div id="campaignsDD"></div></td>
						<td>User Groups:<div id="userGroupsDD"></div></td>
						<td>Assessment Set:<div id="assessmentSetsDD"></div></td>
						<td>Games:	<div id="gamesDD"></div></td>
						<td><br><button class="searchBtn">Search</button></td>
					</tr>
				</table>
        
        </div>
        
        
        <div id="jqxGrid"></div>

		
	
	
	</div>
</body>
</html>
