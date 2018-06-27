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
					lastName:'',
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
					memberId:$rootScope.editMember.memberId,
					//branchName:$scope.editMember.branchName,
					contactNumber:$scope.editMember.contactnumber,
					firstName:$scope.editMember.firstName,
					lastName:$scope.editMember.lastName,
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
				$scope.editMemberForm.$setPristine();
				$scope.editMemberForm.$setUntouched();
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
		//reset add branch
		$scope.resetEditMember = function(){
			//initAddBranch();
		}
		$scope.backPage =function(){
			 $window.history.back();
		}
	}]);
})();
