<%@ include file="includeJars.jsp" %>
<%@ include file="includeJS.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript">
		var pageName = "questions";
		var beanName = "Question";
		var dataUrl = "AdminUser?action=getAllQuestions";
		var deleteUrl = "AdminUser?action=deleteQuestions";
		var addUrl = "AdminUser?action=addQuestions";
		var validatorRules = [
				{ input: '#quesTitleInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' }
				];
				
		var columns = [				
				{ text: 'Title', datafield: 'quesTitle' ,editable:false,width:"25%"},
				{ text: 'Description', datafield: 'description' ,editable:false},
				{ text: 'Points(+)', datafield: 'points',editable:false,width:80,cellsalign: 'right',align: 'right' },
				{ text: 'Points(-)', datafield: 'negativePoints',editable:false,width:80,cellsalign: 'right',align: 'right' },
				{ text: 'Time Avlb.', datafield: 'maxSecondsAllowed',editable:false,width:80,cellsalign: 'right',align: 'right' },
				{ text: 'Chances', datafield: 'extraAttemptsAllowed',editable:false,width:80,cellsalign: 'right',align: 'right' },
				{ text: 'Created On', datafield: 'createdOn',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Last Modified', datafield: 'lastmodifieddate',editable:false,width:150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width:60},
				{ text: 'Hint', datafield: 'hint' ,editable:false}
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
				{ name: 'answer4', type: 'string'},
				{ name: 'hint', type: 'string'}
				];

		$(document).ready(function () {
				var editorWidth= "60%";
				var editorHeight = "70%";
				$(".saveQuestionButton").attr("id", "saveButton");
				renderGrid("jqxGrid",beanName,dataUrl,deleteUrl,addUrl,validatorRules,columns,dataFields,true,editorHeight,editorWidth);
				});//end document ready	
    </script>
</head>
<body class='default'>
	<%@ include file="header.jsp" %>
	<%@ include file="menu.jsp" %>
	<%@ include file="grid.jsp" %>
	
	
    <div id='jqxWidget'>
		<label style="font-family:verdana;font-size: 16px;color:black;font-weight:bold">Questions Information</label><br>
		<label style="font-family:verdana;font-size: 12px;color:grey">View, Create, Edit, Bulk Delete or Find through various sets available in the database.</label>
        <div id="jqxGrid"></div>
		<div id="jqxCreateBeanWindow">
				<%@ include file="questionEditorInclude.jsp" %>
		</div>
	</div>
    
	
</body>
</html>
