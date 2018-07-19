-	(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('assignRoleCtrl', ['$scope', '$filter','serviceApi','$route','utility','$rootScope', function($scope, $filter,serviceApi,$route,utility,$rootScope) {
		initAssignRole();
		loadRolesData();
		$scope.alert = { type: 'success', msg: 'You successfully Assigned Role.',close:true };
		$scope.showAlert = false;
		$scope.showCompany = false;
		$scope.showBranch = false;
		$scope.roles = [];
		$scope.companies = [];
		$scope.branches = [];
		$scope.users = [];
		
		function initAssignRole(){
			$scope.isRoleSelected = false;
			$scope.selectedRole = {};
			$scope.selectedCompany = {};
			$scope.selectedBranch = {};
			$scope.selectedUser = {};
			$scope.assignRole={
					userId:'',
					companyId:'',
					spocRoleId:''
			}
		};
		
		//Load Roles Data
		function loadRolesData(){
		    serviceApi.doPostWithoutData('/RLMS/admin/getApplicableRoles')
		    .then(function(response){
		    	$scope.roles = response;
		    });
		}
		//Proceed Role
		$scope.proceedRole = function(){
			loadCompanyData();
			//loadBranchData();
			$scope.isRoleSelected = true;
		}
	  	
		function loadCompanyData(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		}
		$scope.loadBranchData = function(){
			/*var data = {
				companyId : $scope.selectedCompany.selected.companyId
			}*/
			var companyData={};
			if($scope.showCompany == true){
  	    		companyData = {
						companyId : $scope.selectedCompany.selected.companyId
					}
  	    	}else{
  	    		companyData = {
						companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
					}
  	    	}
			loadUsersData();
		    serviceApi.doPostWithData('/RLMS/admin/getAllBranchesForCompany',companyData)
		    .then(function(response){
		    	$scope.branches = response;
		    	
		    });
		}
		function loadUsersData(){
			var companyId = $scope.selectedCompany.selected.companyId;
			var data = {
					companyId : companyId
				}
		    serviceApi.doPostWithData('/RLMS/admin/getAllUsersForCompany',data)
		    .then(function(response){
		    	$scope.users = response;
		    	console.log(response);
		    });
		}	
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1) {
		$scope.showCompany = true;
		loadCompanyData();
		
	} else {
		$scope.showCompany = false;
		$scope.loadBranchData();
	}
		/*if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 2) {
			$scope.showBranch = true;
			$scope.loadBranchData();
			
		} else {
			$scope.showBranch = false;
		}*/
		//Post call
		$scope.submitAssignRole = function(){
			$scope.assignRole.companyBranchMapId = $scope.selectedBranch.selected.companyBranchMapId;
			$scope.assignRole.companyId = $scope.selectedCompany.selected.companyId;
			$scope.assignRole.spocRoleId = $scope.selectedRole.selected.spocRoleId;
			$scope.assignRole.userId = $scope.selectedUser.selected.userId;
			serviceApi.doPostWithData("/RLMS/admin/assignRole",$scope.assignRole)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				initAssignRole();
				$scope.assignRoleForm.$setPristine();
				$scope.assignRoleForm.$setUntouched();
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			})
		};
		//Reset Add company form
		$scope.resetAssignRole = function(){
			$scope.showAlert = false;
			initAssignRole();
			$scope.assignRoleForm.$setPristine();
			$scope.assignRoleForm.$setUntouched();
		};
	}]);
})();
