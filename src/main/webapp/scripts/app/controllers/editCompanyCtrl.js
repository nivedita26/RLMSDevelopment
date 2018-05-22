(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editCompanyCtrl', ['$scope', '$filter','serviceApi','$route','utility','pinesNotifications','$timeout','$window','$rootScope', function($scope, $filter,serviceApi,$route,utility,pinesNotifications,$timeout,$window,$rootScope) {
		$scope.alert = { type: 'success', msg: 'You successfully Edited Company.',close:true };
		//function to initialize addCompany Model
		$scope.showAlert = false;
		$scope.editCompany.ownerNumber=parseInt($rootScope.editCompany.ownerNumber,10);
		//Post call
		$scope.submitEditCompany = function(){
			var companyData = {};
			companyData = {
					companyId:$rootScope.editCompany.companyId,
					companyName:$scope.editCompany.companyName,  
					ownerName:$scope.editCompany.ownerName,    
					ownerNumber:$scope.editCompany.ownerNumber,  
					ownerEmail:$scope.editCompany.ownerEmail,   
					address:$scope.editCompany.address,      
					area:$scope.editCompany.area,         
					city:$scope.editCompany.city,         
					pinCode:$scope.editCompany.pinCode,      
					contactNumber:$scope.editCompany.contactNumber,
					emailId:$scope.editCompany.emailId,      
					panNumber:$scope.editCompany.panNumber,    
					tinNumber:$scope.editCompany.tinNumber,    
					vatNumber:$scope.editCompany.vatNumber 
			};
			serviceApi.doPostWithData("/RLMS/admin/updateCompany",companyData)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		};
		//Reset Add company form
		$scope.resetAddCompany = function(){
			$scope.showAlert = false;
			$scope.addCompanyForm.$setPristine();
			$scope.addCompanyForm.$setUntouched();
			//$route.reload();
		};
		$scope.backPage =function(){
			 $window.history.back();
		}
		
	}]);
})();
