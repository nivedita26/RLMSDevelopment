(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('resetPassword', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope) {
		initResetPass();
		$scope.alert = { type: 'success', msg: 'You successfully changed the password.',close:true };
		
	 	
		$scope.showAlert = false;
		function initResetPass(){
			$scope.changePassword={
					userId:'',
					oldPassword:'',
					newPassword:'',
			};	
		}
		//if (){}
		//submit  changed password
		$scope.submitResetPassword = function(){
			var dataToSend={
					userId:$rootScope.loggedInUserInfo.data.userRole.rlmsUserMaster.userId,
					oldPassword:$scope.changePassword.oldPassword,
					newPassword:$scope.changePassword.newPassword,					
			}
			serviceApi.doPostWithData("/RLMS/API/resetPassword",dataToSend)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				if(successMessage){
					$scope.alert.msg = "You successfully changed the password.";
					$scope.alert.type = "success";
					initResetPass();
					$scope.addBranchForm.$setPristine();
					$scope.addBranchForm.$setUntouched();
				}else{
					$scope.showAlert = true;
					$scope.alert.msg =  "Invalid Old Password";
					$scope.alert.type="danger";
				}
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error;
				$scope.alert.type = "danger";
			});
		}
		//rese add branch
		$scope.resetAddBranch = function(){
			initResetPass();
		}
	}]);
})();
