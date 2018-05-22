(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addCustomerCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window', function($scope, $filter,serviceApi,$route,utility,$window) {
	initAddCustomer();
			loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Added Customer.',close:true };
			//loadBranchListInfo();
			$scope.showAlert = false;
			$scope.companies = [];
			$scope.branches = [];
			function initAddCustomer(){
				$scope.selectedCompany = {};
				$scope.selectedBranch = {};
				$scope.selectedCustomerTypes = {};
				$scope.addCustomer={
						firstName:'',
						lastName:'',
						address:'',
						city:'',
						area:'',
						pinCode:'',
						vatNumber:'',
						tinNumber:'',
						panNumber:'',
						customerType:'',
						emailID:'',
						cntNumber:'',
						branchName:'',
						companyName:'',
						totalNumberOfLifts:'',
						branchCompanyMapId:'',
						chairmanName :'',
						chairmanNumber :'',
						chairmanEmail :'',
						secretaryName :'',
						secretaryNumber :'',
						secretaryEmail :'',
						treasurerName :'',
						treasurerNumber :'',
						treasurerEmail :'',
						watchmenName :'',
						watchmenNumber :'',
						watchmenEmail :'',
						
						
				};	
				$scope.customerTypes = [
					{
						name:"RESIDENTIAL",
						id:15
					},
					{
						name:"COMMERTIAL",
						id:16
					},
					{
						name:"BUNGLOW",
						id:17
					},
					{
						name:"HOSPITAL",
						id:18
					},
					{
						name:"GOODS",
						id:19
					},
					{
						name:"DUMB_WAITER",
						id:20
					}
				]
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
			//Post call add customer
			$scope.submitAddCustomer = function(){
				$scope.addCustomer.companyName = $scope.selectedCompany.selected.companyName;
				$scope.addCustomer.branchName = $scope.selectedBranch.selected.rlmsBranchMaster.branchName;
				$scope.addCustomer.branchCompanyMapId = $scope.selectedBranch.selected.companyBranchMapId;
				$scope.addCustomer.customerType =$scope.selectedCustomerTypes.selected.id;
				serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewCustomer",$scope.addCustomer)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddCustomer();
					$scope.addCustomerForm.$setPristine();
					$scope.addCustomerForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			//reset add branch
			$scope.resetAddCustomer = function(){
				initAddCustomer();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
	}]);
})();
