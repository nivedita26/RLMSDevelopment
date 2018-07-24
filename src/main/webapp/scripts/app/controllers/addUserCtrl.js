(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addUserCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','pinesNotifications', function($scope, $filter,serviceApi,$route,$http,utility,$window,pinesNotifications) {
		//initialize add Branch
		initAddUser();
		loadCompayInfo();
		//loadBranchListInfo();
		$scope.backPage = function(){
			$window.history.back();
		}
		$scope.alert = { type: 'success', msg: 'You successfully Added new user.',close:true };
		$scope.showAlert = false;
		function initAddUser(){
			$scope.selectedCompany = {};
			$scope.addUser={
				firstName:'',
				lastName:'',
				address:'',
				contactNumber:'',
				emailId:'',
				companyId:'',
				city:'',
				area:'',
				pinCode:''
			};	
		    //$scope.companies = [];
		    $scope.userList={};
		}
		//load compay dropdown data
		function loadCompayInfo(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		};
		//Post call add branch
		$scope.submitAddUser = function(){
			$scope.addUser.companyId = $scope.selectedCompany.selected.companyId;
			serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewUser",$scope.addUser)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				//utility.showMessage("",successMessage,"success");
				initAddUser();
				$scope.addUserForm.$setPristine();
				$scope.addUserForm.$setUntouched();
				
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		//rese add branch
		$scope.resetAddUser = function(){
			$scope.showAlert = false;
			initAddUser();
			$scope.addUserForm.$setPristine();
			$scope.addUserForm.$setUntouched();
		}
		
	}]);
})();
