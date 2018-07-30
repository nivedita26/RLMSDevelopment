(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editBranchCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$window,$rootScope) {
		//initialize add Branch
		 initEditBranch();
		 loadCompayInfo();
		$scope.alert = { type: 'success', msg: 'You successfully Edited Branch.',close:true };
		//loadBranchListInfo();
		$scope.showAlert = false;
		$scope.companies = [];
		
		function initEditBranch(){
			$scope.selectedCompany = {};
			$scope.selectedActiveFlag = {};
			$scope.selectedStatus = {};
			$scope.addBranch={
					companyId:'',
					branchName:'',
					branchAddress:'',
					city:'',
					area:'',
					pinCode:'',
					activeFlag:''
			};	
			/*$scope.activeFlag=[
				{
					
					name:'InActive',
					id:0
					
				},
				{				
					name:'Active',
					id:1
				}
			];*/
			 $scope.branchList={};
		   
		}
		
		//load company dropdown data
		function loadCompayInfo(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		};
		
		//Post call add branch
		$scope.submitEditBranch = function(){
			var branchData = {};
			//$scope.status=['Active','Inactive'];
			branchData = {
					id:$rootScope.editBranch.branchId,
					branchName:$scope.editBranch.branchName,
					branchAddress:$scope.editBranch.branchAddress,
					area:$scope.editBranch.area,
					activeFlag:$scope.editBranch.activeFlag,
					city:$scope.editBranch.city,
					pinCode:$scope.editBranch.pinCode
					
					};
			serviceApi.doPostWithData("/RLMS/admin/editBranchInCompany",branchData)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				$scope.addBranchForm.$setPristine();
				$scope.addBranchForm.$setUntouched();
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		//rese add branch
	/*	$scope.resetEditBranch = function(){
			initEditBranch();
			//$window.location.reload();
		}*/
		$scope.backPage =function(){
			 $window.history.back();
		}
	}]);
})();
