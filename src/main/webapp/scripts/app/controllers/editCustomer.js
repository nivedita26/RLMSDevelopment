(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editCustomerCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope) {
	initAddCustomer();
			loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Edited Customer.',close:true };			
			$scope.showAlert = false;
			$scope.showCompany=false;
			$scope.showBranch=false;
			$scope.companies = [];
			$scope.branches = [];
			function initAddCustomer(){
				$scope.selectedCompany = {};
				$scope.selectedBranch = {};
				$scope.selectedCustomerTypes = {};
				$scope.selectedStatus = {};
				$scope.addCustomer={
						firstName:'',
						//lastName:'',
						address:'',
						city:'',
						area:'',
						pinCode:0,
						customerType:'',
						emailID:'',
						cntNumber:'',
						vatNumber:'',
						tinNumber:'',
						panNumber:'',
						//branchName:'',
						//companyName:'',
						totalNumberOfLifts:'',
						branchCompanyMapId:'',
						chairmanName :'',
						chairmanNumber :0,
						chairmanEmail :'',
						secretaryName :'',
						secretaryNumber :0,
						secretaryEmail :'',
						treasurerName :'',
						treasurerNumber :'',
						treasurerEmail :'',
						watchmenName :'',
						watchmenNumber :'',
						watchmenEmail :'',
						activeFlag:'',
						customerId:''
						
				};	
				/*$scope.customerTypes = [
					{
						name:"RESIDENTIAL",
						id:15
					},
					{
						name:"COMMERCIAL",
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
				];
				$scope.activeFlags=[
					{
						name:"Inactive",
						id:0
					},
					{
						name:"Active",
						id:1
					}
				];*/

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
			    serviceApi.doPostWithData('/RLMS/admin/getListOfCustomerDtls',companyData)
			    .then(function(response){
			    	$scope.branches = response;
			    	$scope.selectedBranch.selected = undefined;
			    	$scope.selectedCustomer.selected = undefined;
			    	var emptyArray=[];
			    	$scope.myData = emptyArray;
			    });
			}
			
			if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompany= true;
				loadCompanyData();
			}else{
				$scope.showCompany= false;
				$scope.loadBranchData();
			}		  			
			
			//Post call add customer
			$scope.submitEditCustomer = function(){		
				var customerData={};
				customerData={
						customerId: $rootScope.editCustomer.customerId,
						firstName:$scope.editCustomer.firstName,
						address:$scope.editCustomer.address,
						area:$scope.editCustomer.area,
						cntNumber:$scope.editCustomer.cntNumber,
						emailID:$scope.editCustomer.emailID,
						city:$scope.editCustomer.city,
						pinCode:$scope.editCustomer.pinCode,
						activeFlag:$scope.editCustomer.activeFlag,
						chairmanName :$scope.editCustomer.chairmanName,
						chairmanNumber :$scope.editCustomer.chairmanNumber,
						chairmanEmail :$scope.editCustomer.chairmanEmail,
						secretaryName :$scope.editCustomer.secretaryName,
						secretaryNumber :$scope.editCustomer.secretaryNumber,
						secretaryEmail :$scope.editCustomer.secretaryEmail,
						treasurerName :$scope.editCustomer.treasurerName,
						treasurerNumber :$scope.editCustomer.treasurerNumber,
						treasurerEmail :$scope.editCustomer.treasurerEmail,
						watchmenName :$scope.editCustomer.watchmenName,
						watchmenNumber :$scope.editCustomer.watchmenNumber,
						watchmenEmail :$scope.editCustomer.watchmenEmail,
						vatNumber:$scope.editCustomer.vatNumber,
						tinNumber:$scope.editCustomer.tinNumber,
						panNumber:$scope.editCustomer.panNumber,
						customerType:$scope.editCustomer.customerType
				}				
				serviceApi.doPostWithData("/RLMS/admin/validateAndUpdateCustomer",customerData)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddCustomer();
					$scope.editCustomerForm.$setPristine();
					$scope.editCustomerForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			
			$scope.submitAddCustomer = function(){
				//$scope.addCustomer.companyName = $scope.selectedCompany.selected.companyName;
				$scope.addCustomer.branchName = $scope.selectedBranch.selected.rlmsBranchMaster.branchName;
				$scope.addCustomer.branchCompanyMapId = $scope.selectedBranch.selected.companyBranchMapId;
				//$scope.addCustomer.customerType =$scope.selectedCustomerTypes.selected.id;
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
				$scope.editCustomerForm.$setPristine();
				$scope.editCustomerForm.$setUntouched();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
	}]);
})();
