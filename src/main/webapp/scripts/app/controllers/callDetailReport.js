(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('callDetailedReportCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();
		$scope.cutomers=[];
		$scope.filterOptions = {
		  	      filterText: '',
		  	      useExternalFilter: true
		  	    };

		function initReport(){
			$scope.selectedCompany = {};
			$scope.selectedBranch = {};
			$scope.lifts=[];
			$scope.branches = [];
			$scope.selectedCustomer = {};
			// $scope.selectedStatus = {};
			$scope.selectedCallType = {};
			$scope.selectedLift = {};
			// $scope.selectedAmc = {};
			$scope.showMembers = false;
			$scope.callType=[
				{
					id: 1,
					name:'Lift Installation Call'
				},{
					id: 2,
					name:'Configuration/Settings Call'
				},{
					id: 3,
					name:'AMC Call'
				},{
					id: 4,
					name:'Under Warranty Support Call'
				},{
					id: 5,
					name:'LMS Alert Call'
				},{
					id: 6,
					name:'Operator Assigned/Generic Call'
				},{
					id: 7,
					name:'User Raised Call Through App'
				},{
					id: 8,
					name:'User Raised Call Through Telephone'
				},{
					id: 9,
					name:'Reassign Call'
				}
			];
		} 
		
		function loadCompanyData() {
			serviceApi
					.doPostWithoutData(
							'/RLMS/admin/getAllApplicableCompanies')
					.then(function(response) {
						$scope.companies = response;
					});
		}
		
		$scope.loadBranchData = function() {
			var companyData = {};
			if ($scope.showCompany == true) {
				companyData = {
					companyId : $scope.selectedCompany.selected.companyId
				}
			} else {
				companyData = {
					companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
				}
			}
			serviceApi
					.doPostWithData(
							'/RLMS/admin/getAllBranchesForCompany',
							companyData)
					.then(function(response) {
						$scope.branches = response;
						$scope.selectedBranch.selected=undefined;
						$scope.selectedCustomer.selected=undefined;
						var emptyComplaint=[];
						$scope.myData=emptyComplaint;
					});
		}
		
		$scope.loadLifts = function() {
			var dataToSend = {
				branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
				branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
			}
			serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
					.then(function(liftData) {
						$scope.lifts = liftData;
					})
			
		}
		
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1) {
			$scope.showCompany = true;
			loadCompanyData();
			
		} else {
			$scope.showCompany = false;
			$scope.loadBranchData();
		}
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
			$scope.showBranch = false;
			//loadDefaultComplaintData();
		} else {
			$scope.showBranch = false;
			$scope.loadCustomerData();
		}
		
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
			$scope.loadCustomerData();
		}		
		
		$scope.loadCustomerData = function() {
			var branchData = {};
			if ($scope.showBranch == true) {
				branchData = {
					branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId
				}
			} else {
				branchData = {
					branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
				}
			}
			serviceApi
					.doPostWithData(
							'/RLMS/admin/getAllCustomersForBranch',
							branchData)
					.then(
							function(customerData) {
								var tempAll = {
									branchCustomerMapId : -1,
									firstName : "All"
								}
								$scope.cutomers = customerData;
								$scope.cutomers
								.unshift(tempAll);
								$scope.selectedCustomer.selected=undefined;
								//$scope.selectedLifts.selected=undefined;
							})
		}
/*		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
			$scope.searchCustomer();
		}*/
		//Show Member List
		$scope.filterOptions.filterText='';
		$scope.$watch('filterOptions', function(newVal, oldVal) {
	  	      if (newVal !== oldVal) {
	  	        $scope.loadReportList($scope.filterOptions.filterText);
	  	      }
	  	    }, true);
		
		$scope.loadReportList = function(searchText){
			if (searchText) {
	  	          var ft = searchText.toLowerCase();
	  	        var dataToSend = constructDataToSend();
	 	         serviceApi.doPostWithData('/RLMS/report/callDetailedReport',dataToSend)
	 	         .then(function(data) {
	 	        	$scope.siteViseReport = data.filter(function(item) {
		  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
		  	            });
	 	         })
 	         }else{
 	        	var dataToSend = constructDataToSend();
 	 	         serviceApi.doPostWithData('/RLMS/report/callDetailedReport',dataToSend)
 	 	         .then(function(data) {
 	 	        	 $scope.siteViseReport = data;
 	 	         })
 	         }
			$scope.showMembers = true;
		}
	   
	  	 /* $scope.searchCustomer = function(query){
				console.log(query);
				if(query && query.length > 1){
				 var dataToSend = {
				 	'customerName':query
				 }
					serviceApi.doPostWithData("/RLMS/complaint/getCustomerByName",dataToSend)
					.then(function(customerData){
						console.log(customerData);
						 $scope.cutomers = customerData;
						 $scope.selectedCallType.selected=undefined;
					},function(error){
						
					});
				} 
				
			}*/
	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	  		/*var tempStatus = [];
	  		if($scope.selectedCallType.selected){
	  			if($scope.selectedCallType.selected.length===0){
	  				alert("Please select Call Type");
	  			}else{
	  				if($scope.selectedCallType.selected.length){
	  					for (var j = 0; j < $scope.selectedCallType.selected.length; j++) {
	  						tempStatus.push($scope.selectedCallType.selected[j].id);
	  					}
	  				}
	  			}
	  		}else{
	  			alert("Please select Call Type");
	  		}		*/
	  		var tempbranchCustomerMapIds = [];
			if($scope.selectedCustomer.selected.length > 0){
				for (var j = 0; j < $scope.selectedCustomer.selected.length; j++) {
					tempbranchCustomerMapIds.push($scope.selectedCustomer.selected[j].branchCustomerMapId);
				}
			}
			
			if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
				$scope.companyBranchMapIdForCustomer=$rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId;
			}else{
				$scope.companyBranchMapIdForCustomer=$scope.selectedBranch.selected.companyBranchMapId;
			}
	  		var data = {
	  				companyBranchMapId:$scope.companyBranchMapIdForCustomer,
	  				//companyId:9,
	  				//listOfUserRoleIds:tempListOfUserRoleIds,
	  				//listOfStatusIds:tempStatus,
//	  				fromDate:"",
//	  				toDate:"",
	  				listOfBranchCustoMapIds:tempbranchCustomerMapIds,
	  				serviceCallType:$scope.selectedCallType.selected.id
	  		};
	  		return data;
	  	  }
	}]);
})();
