<script type="text/javascript">
		var pageUserName = "users";
		var beanUserName = "User";
		var dataUserUrl = "AdminUser?action=getAllUsers" ;
		var deleteUserUrl = "AdminUser?action=deleteUser";
		var addUserUrl  = "AdminUser?action=addUser";
		
		
		var userValidatorRules = [
				{ input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#usernameInput', message: 'Username is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#passwordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
				{ input: '#emailInput', message: 'Enter valid email!', action: 'keyup, blur', rule: 'email' },
				{ input: '#mobileInput', message: 'Mobile is required!', action: 'keyup, blur', rule: 'required' }];
	
		var userColumns = [
				{ text: 'First Name', datafield: 'name' ,editable:false,width:"20%"},
				{ text: 'Email Id', datafield: 'email' ,editable:false,width: "20%"},
				{ text: 'UserName', datafield: 'username',editable:false,width: "20%"},
				{ text: 'Location', datafield: 'location' ,editable:false},
				{ text: 'Signup Date', datafield: 'createdOn',editable:false,width: 150,cellsformat: 'dd-MM-yy hh.mm tt'},
				{ text: 'Enabled', datafield: 'isEnabled',columntype: 'checkbox',editable:false,width: 80}];
		var userDataFields = [
				{ name: 'seq', type: 'integer' },
				{ name: 'userGroupSeq', type: 'integer'},
				{ name: 'userGroupName', type: 'string' },
				{ name: 'userGroupDescription', type: 'string' },
				{ name: 'name', type: 'string' },
				{ name: 'email', type: 'string' },
				{ name: 'mobile', type: 'string' },
				{ name: 'location', type: 'string' },
				{ name: 'username', type: 'string' },
				{ name: 'password', type: 'string' },
				{ name: 'createdOn', type: 'date' },
				{ name: 'isEnabled', type: 'bool' },
				{ name: 'lastmodifieddate', type: 'date'}
				];

		//rendering caleld from campaigns.jsp when window opens
		function renderUserGrid(){
			var campaignSeq = $("#createCampaignForm #seqInput").val();
			if(campaignSeq != null){
				dataUserUrl = "AdminUser?action=getSelectedUserGroupsByCampaign&campaignSeq=" + campaignSeq; 	
				addUserUrl = "";	
				
			}
			renderGrid("userJqxGrid",beanUserName,dataUserUrl,deleteUserUrl,addUserUrl,
					userValidatorRules,userColumns,userDataFields,true,"100%","80%");
			$('#userJqxGrid').on('initialized', function () {
				loadUserGroupDetails();
			}); 
			
		}
		function loadUserGroupDetails(){
			alert("loaded grid");
			var campaignSeq = $("#createCampaignForm #seqInput").val();
			if(campaignSeq != null){
				getUserGroupDetailsUrl = "AdminUser?action=getUserGroupsSelectedOnCampaign&campaignSeq=" + campaignSeq; 	
				$.getJSON(getUserGroupDetailsUrl,function(json){
					if(json[0].status != "failure"){
						$('#userJqxGrid').jqxGrid('selectrow', json[0]["seq"]);
						$("#userGroupSeqInput").val(json[0]["seq"]);
						$("#userGroupNameInput").val(json[0]["name"]);
						$("#userGroupDescriptionInput").val(json[0]["description"]);
					}
				});
			}
		}
 </script>
 <div id="userJqxGrid"></div>