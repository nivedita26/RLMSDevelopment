(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addComplaintCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope','$modal', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope,$modal) {
		initAddComplaint();
			//loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Added Complaint.',close:true };
			$scope.showAlert = false;
			$scope.showCompany = false;
			$scope.showBranch = false;
			$scope.companies = [];
			$scope.branches = [];
			$scope.cutomers=[];
			
			$scope.loadBranchData = function(){
				var companyData={};
				if($scope.showCompany == true){
	  	    		companyData = {
							companyId : $scope.selectedCompany.selected!=undefined?$scope.selectedCompany.selected.companyId:0
						}
	  	    	}else{
	  	    		companyData = {
							companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
						}
	  	    	}
			    serviceApi.doPostWithData('/RLMS/admin/getAllBranchesForCompany',companyData)
			    .then(function(response){
			    	$scope.branches = response;
			    	$scope.selectedBranch.selected = undefined;
			    	$scope.selectedCustomer.selected = undefined;
			    	$scope.selectedLift.selected =undefined;
			    	var emptyArray=[];
			    	$scope.myData = emptyArray;
			    });
			}
			$scope.loadCustomerData = function(){
				var branchData ={};
	  	    	if($scope.showBranch == true){
	  	    		branchData = {
	  	    			branchCompanyMapId : $scope.selectedBranch.selected!=null?$scope.selectedBranch.selected.companyBranchMapId:0
						}
	  	    	}else{
	  	    		branchData = {
	  	    			branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
						}
	  	    	}
	  	    	serviceApi.doPostWithData('/RLMS/admin/getAllCustomersForBranch',branchData)
	 	         .then(function(customerData) {
	 	        	 $scope.cutomers = customerData;
	 	        	 $scope.selectedCustomer.selected = undefined;
	 	        	 $scope.selectedLift.selected =undefined;
	 	        	var emptyArray=[];
			    	$scope.myData = emptyArray;
	 	         })
			}
			function initAddComplaint() {
				$scope.customerSelected = false;
				$scope.selectedCompany = {};
				$scope.selectedBranch = {};
				$scope.selectedCustomer = {};
				$scope.selectedCallType = {};
				$scope.selectedComplaintTitle = {};
				$scope.selectedLift = {};			
				$scope.companyName='';
				$scope.branchName='';				
				$scope.addComplaint={
						branchCompanyMapId:0,
						liftCustomerMapId:0,
						branchCustomerMapId:0,
						companyId:0,
						//callType:0,
						callType:'',
						complaintsTitle:'',
						complaintsRemark:'',
						registrationType:2,
						//fromDate:'',
						//toDate:''
				};
				
				$scope.complaintTitle=[
					{
						id : 0,
						name : 'Stucked between floor'
					},{
						id : 1,
						name : 'Door open close issue'
					},{
						id : 2,
						name : 'Door sensor not working'
					},{
						id : 3,
						name : 'Level mismatch'
					},{
						id : 4,
						name : 'Lift lights not working'
					},{
						id : 5,
						name : 'Lift fan not working'
					},{
						id : 6,
						name : 'Lift intercom'
					},{
						id : 7,
						name : 'Buttons not working'
					},{
						id : 8,
						name : 'call not taken from lop / cop'
					},{
						id : 9,
						name : 'Auto call book'
					},{
						id : 10,
						name : 'Display not working'
					},{
						id : 11,
						name : 'Display error E'
					},{
						id : 12,
						name : 'Display some error cod'
					},{
						id : 13,
						name : 'Rescue not working'
					},{
						id : 14,
						name : 'Jerks and rollbacks'
					},{
						id : 15,
						name : 'Vibrates during running'
					},{
						id : 16,
						name : 'Alarm not working'
					},{	
						id : 17,
						name : 'Gate lock not operating'
					},{
						id : 18,
						name : 'Wrong annoucement'
					},{
						id : 19,
						name : 'Music is off'
					},{
						id : 20,
						name : 'Lift Installation'
					},{
						id : 21,
						name : 'AMC Service Call'
					},{
						id : 22,
						name : 'LMS alert Call'
					},{
						id : 23,
						name : 'Lift configuration Call'
					},{
						id : 24,
						name : 'Under Warranty Support Call'
					},{
						id:25,
						name :'Operator Initiated Call'
					}
					];
				
				$scope.callType=[
					{
						id: 1,
						name:'Lift Installation Call'
					},{
						id: 2,
						name:'Configuration/Settings Call'
					},{
						id: 3,
						name:'AMC Call'
					},{
						id: 4,
						name:'Under Warranty Support Call'
					},{
						id: 5,
						name:'LMS Alert Call'
					},{
						id: 6,
						name:'Operator Assigned/Generic Call'
					},{
						id: 7,
						name:'User Raised Call Through App'
					},{
						id: 8,
						name:'User Raised Call Through Telephone'
					},{
						id: 9,
						name:'Reassign Call'
					}
				];
			}
			$scope.openFlag={
					fromDate:false,
					toDate:false
			}
			$scope.open = function($event,which) {
			      $event.preventDefault();
			      $event.stopPropagation();
			      if($scope.openFlag[which] != true)
			    	  $scope.openFlag[which] = true;
			      else
			    	  $scope.openFlag[which] = false;
			  }
			//load compay dropdown data
			function loadCompayInfo(){
				serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
			    .then(function(response){
			    		$scope.companies = response;
			    });
			};
		
			$scope.loadLifts = function() {
				
				var dataToSend = {
					//branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
					branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
				}
				serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
						.then(function(liftData) {
							$scope.lifts = liftData;
						})
				
				serviceApi.doPostWithData('/RLMS/complaint/getCustomerDtlsById',dataToSend)
						.then(function(data) {
							$scope.customerSelected = true;
							$scope.companyName = data.companyName;
							$scope.branchName = data.branchName
						})
			}
			//Post call add customer
			$scope.submitAddComplaint = function(){
				$scope.addComplaint.callType = $scope.selectedCallType.selected.id;
				$scope.addComplaint.complaintsTitle = $scope.selectedComplaintTitle.selected.name;
				$scope.addComplaint.liftCustomerMapId = $scope.selectedLift.selected.liftId;
				$scope.addComplaint.registrationType = 31;
				serviceApi.doPostWithData("/RLMS/complaint/validateAndRegisterNewComplaint",$scope.addComplaint)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddComplaint();
					$scope.addComplaintForm.$setPristine();
					$scope.addComplaintForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			 //showCompnay Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompany= true;
				//loadCompayInfo();
			}else{
				$scope.showCompany= false;
				$scope.loadBranchData();
			}
		  	
		  	//showBranch Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
		  		$scope.showBranch= true;
				$scope.loadBranchData();
			}else{
				$scope.showBranch=false;
				$scope.loadCustomerData();
			}
			//reset add branch
			$scope.resetaddComplaint = function(){
				initAddComplaint();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
			
			/*$scope.searchCustomer = function(query){
				//console.log(query);
				if(query && query.length > 1){
				 var dataToSend = {
				 	'customerName':query
				 }
					serviceApi.doPostWithData("/RLMS/complaint/getCustomerByName",dataToSend)
					.then(function(customerData){
						//console.log(customerData);
						 $scope.cutomers = customerData;
					},function(error){
						
					});
				} 
				
			}*/

			
		  /*	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showBranch= true;
				$scope.loadBranchData();

			}else{
				$scope.showBranch=false;
				$scope.loadCustomerData();

			}*/
	}]);
})();
