(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('liftManagementCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initCustomerList();
		$scope.showCompany = false;
		$scope.showBranch = false;
		$scope.goToAddLift =function(){
			window.location.hash = "#/add-lift";
		};
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
		    	var emptyLiftArray=[];
		    	$scope.myData=emptyLiftArray;
		    	
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
	  	        serviceApi.doPostWithData('/RLMS/admin/getLiftDetailsForBranch',branchData)
	  	         .then(function(largeLoad) {
	  	        	$scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			userDetailsObj["Lift_Number"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			userDetailsObj["Lift_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].customerName){
	  	        			userDetailsObj["Customer_Name"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			userDetailsObj["Customer_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceStartDateStr){
	  	        			userDetailsObj["Service_Start_Date"] =largeLoad[i].serviceStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceEndDateStr){
	  	        			userDetailsObj["Service_End_Date"] =largeLoad[i].serviceEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["Installation_Date"] =largeLoad[i].dateOfInstallationStr;
	  	        		}else{
	  	        			userDetailsObj["Installation_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDateStr){
	  	        			userDetailsObj["Amc_Start_Date"] =largeLoad[i].amcStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcEndtDateStr){
	  	        			userDetailsObj["Amc_End_Date"] =largeLoad[i].amcEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["amcType"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			userDetailsObj["amcType"] =" - ";
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
	  	        	serviceApi.doPostWithData('/RLMS/admin/getLiftDetailsForBranch',branchData).then(function(largeLoad) {
	  	        		 $scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			userDetailsObj["Lift_Number"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			userDetailsObj["Lift_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].customerName){
	  	        			userDetailsObj["Customer_Name"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			userDetailsObj["Customer_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}	  	        		
	  	        		if(!!largeLoad[i].serviceStartDateStr){
	  	        			userDetailsObj["Service_Start_Date"] =largeLoad[i].serviceStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceEndDateStr){
	  	        			userDetailsObj["Service_End_Date"] =largeLoad[i].serviceEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["Installation_Date"] =largeLoad[i].dateOfInstallationStr;
	  	        		}else{
	  	        			userDetailsObj["Installation_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDateStr){
	  	        			userDetailsObj["Amc_Start_Date"] =largeLoad[i].amcStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcEndtDateStr){
	  	        			userDetailsObj["Amc_End_Date"] =largeLoad[i].amcEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["amcType"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			userDetailsObj["amcType"] =" - ";
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
	  	      width:80,
	  	      enablePaging: true,
	  	      showFooter: true,
	  	      totalServerItems: 'totalServerItems',
	  	      pagingOptions: $scope.pagingOptions,
	  	      filterOptions: $scope.filterOptions,
	  	      multiSelect: false,
	  	      gridFooterHeight:35,
	  	      columnDefs : [ {
					field : "Lift_Number",
					displayName:"Lift Number",
					width: "120"
				}, {
					field : "Customer_Name",
					displayName:"Customer",
					width: "140"
				}, {
					field : "Address",
					displayName:"Address",
					width: "140"
				}, {
					field : "Branch_Name",
					displayName:"Branch",
					width: "140"
				}
				, {
					field : "City",
					displayName:"City",
					width: "120"
				}, {
					field : "Installation_Date",
					displayName:"Installation Date",
					width: "140"
				}, {
					field : "Service_Start_Date",
					displayName:"Service Start Date",
					width: "140"
				}, {
					field : "Service_End_Date",
					displayName:"Service End Date",
					width: "140"
				},
				{
					field : "Amc_Start_Date",
					displayName:"Amc Start Date",
					width: "140"
				},
				{
					field : "Amc_End_Date",
					displayName:"Amc End Date",
					width: "140"
				},
				{
					field : "amcType",
					displayName:"AMC Type",
					width: "140"
				}
				]
	  	    };
		
	}]);
})();
