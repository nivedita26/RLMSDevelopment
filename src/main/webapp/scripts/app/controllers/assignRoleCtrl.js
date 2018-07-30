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
			if($scope.selectedRole.selected.roleName==="COMPANY_OPERATOR"){
				$scope.showCompany=false;
				$scope.showBranch=false;
			}
			if($scope.selectedRole.selected.roleName===("BRANCH_ADMIN")){
				//$scope.showCompany=false;
				$scope.showBranch=true;
			}
			if($scope.selectedRole.selected.roleName===("BRANCH_OPERATOR")){
				//$scope.showCompany=false;
				$scope.showBranch=true;
			}
			if($scope.selectedRole.selected.roleName===("TECHNICIAN")){
				//$scope.showCompany=false;
				$scope.showBranch=true;
			}
		}
	  	
		function loadCompanyData(){
			$scope.loadBranchData();
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
			var companyId =  $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId;
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
		} else {
			$scope.showCompany = false;
		}
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 2) {
			$scope.showBranch = true;
		} else {
			$scope.showBranch = false;
		}
		//Post call
		$scope.submitAssignRole = function(){
			if ($scope.selectedRole.selected.roleName===("COMPANY_OPERATOR")) {
				$scope.assignRole.companyBranchMapId = $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId;
			}else{
				$scope.assignRole.companyBranchMapId = $scope.selectedBranch.selected.companyBranchMapId;
			}
			$scope.assignRole.companyId = $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId;
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
