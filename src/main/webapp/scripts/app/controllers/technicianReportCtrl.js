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
		// showCompnay Flag
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3 ) {
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
					.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
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
					.doPostWithData('/RLMS/admin/getAllBranchesForCompany',companyData)
					.then(function(response) {
						$scope.branches = response;
						$scope.selectedBranch.selected=undefined;
						var emptySite=[];
						$scope.siteViseReport=emptySite;
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
 	         var dataToSend = constructDataToSend();
 	         serviceApi.doPostWithData('/RLMS/report/getTechnicianWiseReport',dataToSend)
 	         .then(function(data) {
 	        	 $scope.siteViseReport = data.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
 	         })
			}else{
				var dataToSend = constructDataToSend();
	 	         serviceApi.doPostWithData('/RLMS/report/getTechnicianWiseReport',dataToSend)
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
	  		var data = {
	  				'branchCompanyMapId':$scope.selectedBranch.selected.companyBranchMapId,
	  				'companyId':$scope.selectedCompany.selected.companyId,
	  		};
	  		return data;
	  	  }
	}]);
})();
