(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addMemberCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope) {
	initAddMember();
			loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Added Member.',close:true };
			//$scope.alert = { type: 'error', msg: 'Please fill the required fields.',close:false };
			//loadBranchListInfo();
			$scope.showAlert = false;
			$scope.showAlert1 = false;
			$scope.showCompany = false;
			$scope.showBranch = false;
			$scope.companies = [];
			$scope.branches = [];
			function initAddMember(){
				$scope.selectedCompany = {};
				$scope.selectedBranch = {};
				$scope.selectedCustomer = {};
				$scope.addMember={
						firstName:'',
						lastName:'',
						address:'',
						city:'',
						area:'',
						pinCode:'',
						emailId:'',
						contactNumber:'',
						branchCustoMapId:0
						
				};	
			}
			//load compay dropdown data
			function loadCompayInfo(){
				serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
			    .then(function(response){
			    		$scope.companies = response;
			    });
			};
			
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
	 	        	var emptyArray=[];
			    	$scope.myData = emptyArray;
	 	         })
			}
			
			//Post call add customer
			$scope.submitAddCustomer = function(){
				$scope.addMember.branchCustoMapId = $scope.selectedCustomer.selected.branchCustomerMapId;
				serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewMember",$scope.addMember)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					if(successMessage){
						$scope.alert.msg = "You successfully Added Member.";
						$scope.alert.type = "success";
						initAddCustomer();
						$scope.addBranchForm.$setPristine();
						$scope.addBranchForm.$setUntouched();
					}else{
						$scope.showAlert = true;
						$scope.alert.msg =  response[key[1]];
						$scope.alert.type="danger";
					}
					/*$scope.alert.message = successMessage;
					$scope.alert.type = "success";
					initAddMember();
					$scope.addMemberForm.$setPristine();
					$scope.addMemberForm.$setUntouched();*/
				}/*,function(error){
					$scope.showAlert1 = true;
					$scope.alert.message = error;
					$scope.alert.type = "danger";
				}*/);
			}
			 //showCompnay Flag
			if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompany= true;
				$scope.showBranch=true;
				loadCompayInfo();
			}else{
				$scope.showCompany= false;
				//$scope.loadBranchData();
			}
		  	
		  	//showBranch Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 2){
				$scope.showBranch= true;
				$scope.loadBranchData();
				//$scope.loadCustomerData();
			}else{
				//$scope.showBranch=false;
				$scope.loadCustomerData();
			}
			//reset add branch
			$scope.resetAddMember = function(){
				initAddMember();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
	}]);
})();
