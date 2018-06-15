(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editMemberCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$window,$rootScope) {
		//initialize add Branch
		$scope.alert = { type: 'success', msg: 'You successfully Edited Member.',close:true };
		//loadBranchListInfo();
		$scope.showAlert = false;
		$scope.companies = [];

		function initEditMember(){
			$scope.selectedCompany = {};
			$scope.selectedActiveFlag = {};
			$scope.addMember={
					memberId:'',
					//branchName:'',
					//branchAddress:'',
					firstName:'',
					city:'',
					area:'',
					address:'',
					pinCode:'',
					contactNumber:'',
					emailId:''
			};	
		    $scope.branchList={};
		   
		}
		//load compay dropdown data
		function loadCompayInfo(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		};
		//Post call add branch
		$scope.submitEditMember= function(){
			var memberData = {};
			memberData = {
					id:$rootScope.editMember.MemberId,
					//branchName:$scope.editMember.branchName,
					contactNumber:$scope.editMember.contactnumber,
					firstName:$scope.editMember.firstName,
					address:$scope.editMember.address,
					emailId:$scope.editMember.emailId,
					area:$scope.editMember.area,
					city:$scope.editMember.city,
					pinCode:$scope.editMember.pinCode,
					};
			serviceApi.doPostWithData("/RLMS/admin/validateAndUpdateMember",memberData)
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
		$scope.submitAddMember = function(){
			$scope.addMember.branchCustoMapId = $scope.selectedCustomer.selected.branchCustomerMapId;
			serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewMember",$scope.addMember)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				initAddMember();
				$scope.addMemberForm.$setPristine();
				$scope.addMemberForm.$setUntouched();
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		//rese add branch
		$scope.resetAddBranch = function(){
			//initAddBranch();
		}
		$scope.backPage =function(){
			 $window.history.back();
		}
	}]);
})();
