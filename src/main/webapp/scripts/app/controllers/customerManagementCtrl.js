(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('customerManagementCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initCustomerList();
		$scope.showCompany = false;
		$scope.showBranch = false;
		$scope.goToAddCustomer = function(){
			window.location.hash = "#/add-customer";
		}
		function initCustomerList(){
			 $scope.selectedCompany={};
			 $scope.selectedBranch = {};
			 $scope.branches=[];
			 $scope.showTable = false;
		} 
		function loadCompanyData(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		}
		$scope.loadBranchData = function(){
			var companyData={};
			if($scope.showCompany == true){
  	    		companyData = {
						companyId : $scope.selectedCompany.selected.companyId
					}
  	    	}else{
  	    		companyData = {
						companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
					}
  	    	}
		    serviceApi.doPostWithData('/RLMS/admin/getAllBranchesForCompany',companyData)
		    .then(function(response){
		    	$scope.branches = response;
		    	$scope.selectedBranch.selected=undefined;
		    	var emptyArray=[];
		    	$scope.myData = emptyArray;
		    });
		}
	    $scope.filterOptions = {
	  	      filterText: '',
	  	      useExternalFilter: true
	  	    };
	  	    $scope.totalServerItems = 0;
	  	    $scope.pagingOptions = {
	  	      pageSizes: [10, 20, 50],
	  	      pageSize: 10,
	  	      currentPage: 1
	  	    };
	  	    $scope.setPagingData = function(data, page, pageSize) {
	  	      var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
	  	      $scope.myData = pagedData;
	  	      $scope.totalServerItems = data.length;
	  	      if (!$scope.$$phase) {
	  	        $scope.$apply();
	  	      }
	  	    };
	  	    $scope.getPagedDataAsync = function(pageSize, page, searchText) {
	  	      setTimeout(function() {
	  	        var data;
	  	        if (searchText) {
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
	  	          var ft = searchText.toLowerCase();
	  	        serviceApi.doPostWithData('/RLMS/admin/getListOfCustomerDtls',branchData)
	  	         .then(function(largeLoad) {
	  	        	$scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].firstName){
	  	        			userDetailsObj["Name"] =largeLoad[i].firstName;
	  	        		}else{
	  	        			userDetailsObj["Name"] =" - ";
	  	        		}	  	        		
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].cntNumber){
	  	        			userDetailsObj["Contact_Number"] =largeLoad[i].cntNumber;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailID){
	  	        			userDetailsObj["Email_Id"] =largeLoad[i].emailID;
	  	        		}else{
	  	        			userDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
	  	        			if(!!largeLoad[i].companyName){
	  	        				userDetailsObj["Company"] =largeLoad[i].companyName;
	  	        			}else{
	  	        				userDetailsObj["Company"] =" - ";
	  	        			}
	  	        		}
	  	        		if(!!largeLoad[i].totalNumberOfLifts){
	  	        			userDetailsObj["Lifts_Count"] =largeLoad[i].totalNumberOfLifts;
	  	        		}else{
	  	        			userDetailsObj["Lifts_Count"] =" - ";
	  	        		}
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            data = userDetails.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
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
	  	        	serviceApi.doPostWithData('/RLMS/admin/getListOfCustomerDtls',branchData).then(function(largeLoad) {
	  	        		 $scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var userDetailsObj={};
	  	        		if(!!largeLoad[i].firstName){
	  	        			userDetailsObj["Name"] =largeLoad[i].firstName;
	  	        		}else{
	  	        			userDetailsObj["Name"] =" - ";
	  	        		}	  	        			  	        	
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}  	        		
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}	  	        		
	  	        		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
	  	        			if(!!largeLoad[i].companyName){
	  	        				userDetailsObj["Company"] =largeLoad[i].companyName;
	  	        			}else{
	  	        				userDetailsObj["Company"] =" - ";
	  	        			}
	  	        		}
	  	        		if(!!largeLoad[i].cntNumber){
	  	        			userDetailsObj["Contact_Number"] =largeLoad[i].cntNumber;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailID){
	  	        			userDetailsObj["Email_Id"] =largeLoad[i].emailID;
	  	        		}else{
	  	        			userDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		
	  	        		if(!!largeLoad[i].totalNumberOfLifts){
	  	        			userDetailsObj["Lift_Count"] =largeLoad[i].totalNumberOfLifts;
	  	        		}else{
	  	        			userDetailsObj["Lift_Count"] =" - ";
	  	        		}
	  	        		
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(userDetails, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
	  	    $scope.loadCustomerInfo=function(){
	  	    	
	  	    	 $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
	  	    }
	  	    $scope.resetCustomerList=function(){
	  	    	initCustomerList();
	  	    }
	  	    //showCompnay Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompany= true;
				loadCompanyData();
			}else{
				$scope.showCompany= false;
				$scope.loadBranchData();
			}
		  	
		  	//showBranch Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
				$scope.showBranch= true;
			}else{
				$scope.showBranch=false;
			}
		  	
	  	    $scope.$watch('pagingOptions', function(newVal, oldVal) {
	  	      if (newVal !== oldVal) {
	  	        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
	  	      }
	  	    }, true);
	  	    $scope.$watch('filterOptions', function(newVal, oldVal) {
	  	      if (newVal !== oldVal) {
	  	        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
	  	      }
	  	    }, true);

	  	    $scope.gridOptions = {
	  	      data: 'myData',
	  	      rowHeight: 40,
	  	      enablePaging: true,
	  	      showFooter: true,
	  	      totalServerItems: 'totalServerItems',
	  	      pagingOptions: $scope.pagingOptions,
	  	      filterOptions: $scope.filterOptions,
	  	      multiSelect: false,
	  	      gridFooterHeight:35
	  	    };
		
	}]);
})();
