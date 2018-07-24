(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addCompanyCtrl', ['$scope', '$filter','serviceApi','$route','utility','pinesNotifications','$timeout','$window', function($scope, $filter,serviceApi,$route,utility,pinesNotifications,$timeout,$window) {
		initAddCompany();
		$scope.alert = { type: 'success', msg: 'You successfully Added Company.',close:true };
		$scope.addUserAlert = { type: 'success', msg: 'You successfully Added User.',close:true };
		//function to initialize addCompany Model
		$scope.showAlert = false;
		function initAddCompany(){
			$scope.addCompany={
					companyName:'',
					address:'',
					city:'',
					area:'',
					pinCode:'',
					contactNumber:'',
					emailId:"",
					panNumber:'',
					tinNumber:'',
					vatNumber:'',
					ownerName:'',
					ownerNumber:0,
					ownerEmail:''
			}
		};
		//Post call
		$scope.submitAddCompany = function(){
			serviceApi.doPostWithData("/RLMS/admin/addNewCompany",$scope.addCompany)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				//utility.showMessage('Company Added',successMessage,'success');
				initAddCompany();
				$scope.addCompanyForm.$setPristine();
				$scope.addCompanyForm.$setUntouched();
				//$route.reload();
					
				
				//utility.showMessage('Added Company',successMessage,'success');
				
			},function(response){
				$scope.showAlert = true;
				$scope.alert.msg = response.exceptionMessage;
				$scope.alert.type = "danger";
			})
		};
		//Reset Add company form
		$scope.resetAddCompany = function(){
			$scope.showAlert = false;
			initAddCompany();
			$scope.addCompanyForm.$setPristine();
			$scope.addCompanyForm.$setUntouched();
			//$route.reload();
		};
		$scope.backPage =function(){
			 $window.history.back();
		}
		//add user ctrl
		initAddUser();
		loadCompayInfo();
		function initAddUser(){
			$scope.selectedCompany = {};
			$scope.addUser={
				firstName:'',
				lastName:'',
				address:'',
				contactNumber:'',
				emailId:'',
				companyId:''	
			};	
		    //$scope.companies = [];
		    $scope.userList={};
		}
		function loadCompayInfo(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		};
		$scope.submitAddUser = function(){
			$scope.addUser.companyId = $scope.selectedCompany.selected.companyId;
			serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewUser",$scope.addUser)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.addUserAlert.msg = successMessage;
				$scope.addUserAlert.type = "success";
				//utility.showMessage("",successMessage,"success");
				initAddUser();
				$scope.addUserForm.$setPristine();
				$scope.addUserForm.$setUntouched();
				
			},function(error){
				$scope.showAlert = true;
				$scope.addUserAlert.msg = error.exceptionMessage;
				$scope.addUserAlert.type = "danger";
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
