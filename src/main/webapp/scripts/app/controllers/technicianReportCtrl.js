(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('technicainReportCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();
		$scope.filterOptions = {
		  	      filterText: '',
		  	      useExternalFilter: true
		  	    };
		function initReport(){
			$scope.selectedCompany = {};
			$scope.selectedBranch = {};
			 $scope.branches = [];
			 $scope.companies = [];
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
						var emptySite=[];
						$scope.siteViseReport=emptySite;
					});
		}
		// showCompnay Flag
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1 ) {
			$scope.showCompany = true;
			loadCompanyData();
		} else {
			$scope.showCompany = false;
			
		}
		// showBranch Flag
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
			$scope.showBranch = true;
			$scope.loadBranchData();
			//loadCompanyData();
		} else {
			$scope.showBranch = false;
			//$scope.loadBranchData();
			//$scope.loadReportList();
			var emptySite=[];
			$scope.siteViseReport=emptySite;
		}
		
		function loadCompanyData() {
			serviceApi
					.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
					.then(function(response) {
						$scope.companies = response;
					});
		}
		$scope.filterOptions.filterText='';
		$scope.$watch('filterOptions', function(newVal, oldVal) {
	  	      if (newVal !== oldVal) {
	  	        $scope.loadReportList($scope.filterOptions.filterText);
	  	      }
	  	    }, true);
		
		$scope.loadReportList = function(searchText){
			if (searchText) {
	  	          var ft = searchText.toLowerCase();
	  	        var branchData ={};
	  	    	if($scope.showBranch == true){
	  	    		branchData = {
	  	    			branchCompanyMapId : $scope.selectedBranch.selected!=null?$scope.selectedBranch.selected.companyBranchMapId:0
						}
	  	    	}else{
	  	    		branchData = {
	  	    			branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
						}
	  	    	}
 	         //var dataToSend = constructDataToSend();
 	         serviceApi.doPostWithData('/RLMS/report/getTechnicianWiseReport',branchData)
 	         .then(function(data) {
 	        	 $scope.siteViseReport = data.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
 	         })
			}else{
				//var dataToSend = constructDataToSend();
				var branchData ={};
	  	    	if($scope.showBranch == true){
	  	    		branchData = {
	  	    			branchCompanyMapId : $scope.selectedBranch.selected!=null?$scope.selectedBranch.selected.companyBranchMapId:0
						}
	  	    	}else{
	  	    		branchData = {
	  	    			branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
						}
	  	    	}
				
	 	         serviceApi.doPostWithData('/RLMS/report/getTechnicianWiseReport',branchData)
	 	         .then(function(data) {
	 	        	 $scope.siteViseReport = data;
	 	        	 
	 	         })
			}
			$scope.showMembers = true;
		}
	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	  		  if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
	  			branchCompanyMapId=$rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId;
	  		  }else{
	  			branchCompanyMapId=$scope.selectedBranch.selected.companyBranchMapId;
	  		  }
	  		var data = {
	  				'branchCompanyMapId':branchCompanyMapId,
	  				//'companyId':$scope.selectedCompany.selected.companyId,
	  		};
	  		return data;
	  	  }
	}]);
})();
