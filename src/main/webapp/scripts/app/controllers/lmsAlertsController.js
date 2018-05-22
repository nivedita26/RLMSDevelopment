(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('lmsAlertsCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();
		$scope.cutomers=[];
		$scope.filterOptions = {
		  	      filterText: '',
		  	      useExternalFilter: true
		  	    };
		$scope.goToAddAMC = function(){
			window.location.hash = "#/add-amc";
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
					.doPostWithData('/RLMS/admin/getAllBranchesForCompany',companyData)
					.then(function(response) {
						$scope.branches = response;
						$scope.selectedBranch.selected=undefined;
						$scope.selectedCustomer.selected=undefined;
//						$scope.selectedStatus.selected=undefined;
						var emptyReports=[];
						$scope.siteViseReport=emptyReports;
					});
		}
		function initReport(){
			$scope.selectedCompany = {};
			$scope.selectedBranch = {};
			 $scope.lifts=[];
			 $scope.branches = [];
			 $scope.selectedCustomer = {};
			 //$scope.selectedStatus = {};
			 $scope.selectedEventType = {};			 
			 $scope.selectedLift = {};
			 $scope.selectedAmc = {};
			 $scope.showMembers = false;
			 $scope.eventType = [ {
					id : 21,
					name : 'Event'
				}, {
					id : 31,
					name : 'Error'
				}, {
					id : 41,
					name : 'Response'
				} ];
			 
		} 
		// showCompnay Flag
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1) {
			$scope.showCompany = true;
			loadCompanyData();
		} else {
			$scope.showCompany = false;
			$scope.loadBranchData();
		}
		
		// showBranch Flag
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
			$scope.showBranch = true;
		} else {
			$scope.showBranch = false;
		}
		function loadCompanyData() {
			serviceApi
					.doPostWithoutData(
							'/RLMS/admin/getAllApplicableCompanies')
					.then(function(response) {
						$scope.companies = response;
					});
		}
		
		$scope.loadCustomerData = function(){
			var branchData ={};
  	    	if($scope.showBranch == true){
  	    		branchData = {
  	    			branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId
					}
  	    	}else{
  	    		branchData = {
  	    			branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
					}
  	    	}
  	    	serviceApi.doPostWithData('/RLMS/admin/getAllCustomersForBranch',branchData)
 	         .then(function(customerData) {
 	        	 $scope.cutomers = customerData;
 	        	$scope.selectedCustomer.selected=undefined;
				$scope.selectedStatus.selected=undefined;
				$scope.selectedEventType.selected=undefined;
 	         })
		}
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
			$scope.loadCustomerData();
		}
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
	 	         serviceApi.doPostWithData('/RLMS/report/getSiteVisitReport',dataToSend)
	 	         .then(function(data) {
	 	        	$scope.siteViseReport = data.filter(function(item) {
		  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
		  	            });
	 	         })
 	         }else{
 	        	var dataToSend = constructDataToSend();
 	 	         serviceApi.doPostWithData('/RLMS/report/getSiteVisitReport',dataToSend)
 	 	         .then(function(data) {
 	 	        	 $scope.siteViseReport = data;
 	 	         })
 	         }
			$scope.showMembers = true;
		}
	   
	  	 /* $scope.searchCustomer = function(query){
				//console.log(query);
				if(query && query.length > 1){
				 var dataToSend = {
				 	'customerName':query
				 }
					serviceApi.doPostWithData("/RLMS/complaint/getCustomerByName",dataToSend)
					.then(function(customerData){
						//console.log(customerData);
						 $scope.cutomers = customerData;
					},function(error){
						
					});
				} 
				
			}*/
	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	  		var tempStatus = [];
	  		if($scope.selectedEventType.selected){
	  			if($scope.selectedEventType.selected.length===0){
	  				alert("Please select Event Type");
	  			}else{
	  				if($scope.selectedEventType.selected.length){
	  					for (var j = 0; j < $scope.selectedEventType.selected.length; j++) {
	  						tempStatus.push($scope.selectedEventType.selected[j].id);
	  					}
	  				}
	  			}
	  		}else{
	  			alert("Please select Event Type");
	  		}
	  		
			
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
	  				listOfStatusIds:tempStatus,
//	  				fromDate:"",
//	  				toDate:"",
	  				listOfBranchCustoMapIds:tempbranchCustomerMapIds,
	  				serviceCallType:1
	  		};
	  		return data;
	  	  }
	}]);
})();
