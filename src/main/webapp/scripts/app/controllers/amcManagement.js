(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('amcManagementCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initAMCList();
		$scope.cutomers=[];
		$scope.showCompany = false;
		$scope.showBranch = false;
		$scope.goToAddAMC = function(){
			window.location.hash = "#/add-amc";
		}
		function initAMCList(){
			 $scope.lifts=[];
			 $scope.selectedCompany={};
			 $scope.selectedBranch = {};
			 $scope.selectedCustomer = {};
			 $scope.selectedLift = {};
			 $scope.selectedAmc = {};
			 $scope.showMembers = false;
			 $scope.amcStatuses=[
				 {
					 name:"Under Warranty",
					 id:38
				 },
				 {
					 name:"Renewal Due",
					 id:39
				 },
				 {
					 name:"AMC Pending",
					 id:40
				 },
				 {
					 name:"Under AMC",
					 id:41
				 }
			 ]
			 
		} 
		/*$scope.loadCustomerData = function(){
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
 	         })
		}*/
		//Show Member List
		$scope.loadAMCList = function(){
			$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
			$scope.showMembers = true;
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
	  	          var ft = searchText.toLowerCase();
	  	        var dataToSend = constructDataToSend();
	  	        serviceApi.doPostWithData('/RLMS/report/getAMCDetailsForLifts',dataToSend)
	  	         .then(function(largeLoad) {
	  	        	  var details=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var detailsObj={};
	  	        		if(!!largeLoad[i].firstName){
	  	        			detailsObj["firstName"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			detailsObj["firstName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			detailsObj["BranchName"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			detailsObj["BranchName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			detailsObj["liftNumber"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			detailsObj["liftNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].status){
	  	        			detailsObj["status"] =largeLoad[i].status;
	  	        		}else{
	  	        			detailsObj["status"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcAmount){
	  	        			detailsObj["amcAmount"] =largeLoad[i].amcAmount;
	  	        		}else{
	  	        			detailsObj["amcAmount"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcTypeStr){
	  	        			detailsObj["amcTypeStr"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			detailsObj["amcTypeStr"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDate){
	  	        			detailsObj["amcStartDate"] =largeLoad[i].amcStartDate;
	  	        		}else{
	  	        			detailsObj["amcStartDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dueDate){
	  	        			detailsObj["dueDate"] =largeLoad[i].dueDate;
	  	        		}else{
	  	        			detailsObj["dueDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			detailsObj["area"] =largeLoad[i].area;
	  	        		}else{
	  	        			detailsObj["area"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			detailsObj["city"] =largeLoad[i].city;
	  	        		}else{
	  	        			detailsObj["city"] =" - ";
	  	        		}
	  	        		details.push(detailsObj);
	  	        	  }
	  	            data = details.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
	  	        	
	  	        	var dataToSend = constructDataToSend();
		  	    	
	  	        	serviceApi.doPostWithData('/RLMS/report/getAMCDetailsForLifts',dataToSend).then(function(largeLoad) {
	  	        	  var details=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var detailsObj={};
	  	        		if(!!largeLoad[i].customerName){
	  	        			detailsObj["firstName"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			detailsObj["firstName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			detailsObj["BranchName"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			detailsObj["BranchName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			detailsObj["liftNumber"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			detailsObj["liftNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].status){
	  	        			detailsObj["status"] =largeLoad[i].status;
	  	        		}else{
	  	        			detailsObj["status"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcAmount){
	  	        			detailsObj["amcAmount"] =largeLoad[i].amcAmount;
	  	        		}else{
	  	        			detailsObj["amcAmount"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcTypeStr){
	  	        			detailsObj["amcTypeStr"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			detailsObj["amcTypeStr"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDate){
	  	        			detailsObj["amcStartDate"] =largeLoad[i].amcStartDate;
	  	        		}else{
	  	        			detailsObj["amcStartDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcEndDate){
	  	        			detailsObj["amcEndDate"] =largeLoad[i].amcEndDate;
	  	        		}else{
	  	        			detailsObj["amcEndDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dueDate){
	  	        			detailsObj["dueDate"] =largeLoad[i].dueDate;
	  	        		}else{
	  	        			detailsObj["dueDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			detailsObj["area"] =largeLoad[i].area;
	  	        		}else{
	  	        			detailsObj["area"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			detailsObj["city"] =largeLoad[i].city;
	  	        		}else{
	  	        			detailsObj["city"] =" - ";
	  	        		}
	  	        		details.push(detailsObj);
	  	        	  }
	  	            $scope.setPagingData(details, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
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
	  	      gridFooterHeight:35,
	  	      groupBy:'customerName',
	  	      columnDefs : [{
					field : "liftNumber",
					displayName:"Lift Number",
					width : 140
		  	  },{
					field : "firstName",
					displayName:"Customer",
					width : 140
		  	  },{
					field : "area",
					displayName:"Address",
					width : 140
		  	  },{
					field : "branchName",
					displayName:"Branch",
					width : 140
		  	  },{
					field : "city",
					displayName:"City",
					width : 140
		  	  },{
					field : "amcTypeStr",
					displayName:"AMC Type",
					width : 140
		  	  },{
					field : "amcStartDate",
					displayName:"AMC Start Date",
					width : 160
		  	  },{
					field : "amcEndDate",
					displayName:"AMC End Date",
					width : 160
		  	  },{
					field : "dueDate",
					displayName:"Due Date",
					width : 120
		  	  },{
					field : "amcAmount",
					displayName:"AMC Amount",
					width : 120
	  	      },{
					field : "status",
					displayName:"Status",
					width : 120
		  	  }
	  	      ]
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
			$scope.loadCustomerData = function(){
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
	  	    	serviceApi.doPostWithData('/RLMS/admin/getAllCustomersForBranch',branchData)
	 	         .then(function(customerData) {
	 	        	 $scope.cutomers = customerData;
	 	        	 $scope.selectedCustomer.selected = undefined;
	 	        	var emptyArray=[];
			    	$scope.myData = emptyArray;
	 	         })
			}
	  	  $scope.loadLifts = function() {
				/*
				var dataToSend = {
					//branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
					branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
				}
				serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
						.then(function(liftData) {
							$scope.lifts = liftData;
						})*/
	  		var dataToSend = {
					branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
				}
				serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
						.then(function(liftData) {
							$scope.lifts = liftData;
						})
				
				serviceApi.doPostWithData('/RLMS/admin/getAllCustomersForBranch',dataToSend)
						.then(function(data) {
							$scope.customerSelected = true;
							$scope.companyName = data.companyName;
							$scope.branchName = data.branchName
						})
			}
	  	  /*$scope.searchCustomer = function(query){
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
	  	  $scope.resetAMCList = function(){
	  		initAMCList();
	  	  }
	  	  function constructDataToSend(){
	  		var tempLiftIds = [];
			for (var i = 0; i < $scope.selectedLift.selected.length; i++) {
				tempLiftIds.push($scope.selectedLift.selected[i].liftId);
			}
			var tempCusto = [];
			for (var j = 0; j < $scope.selectedCustomer.selected.length; j++) {
				tempCusto.push($scope.selectedCustomer.selected[j].branchCustomerMapId);
			}
			var tempStatus =[];
			for (var j = 0; j < $scope.selectedAmc.selected.length; j++) {
				tempStatus.push($scope.selectedAmc.selected[j].id);
			}
			var data = {
	        			liftCustomerMapId:tempLiftIds,
	        			listOfBranchCustomerMapId:tempCusto,
	        			listOFStatusIds:tempStatus
  	    	}
			var tempStatus =[]
			return data;
	  	  }
	  	  
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
				$scope.showBranch= true;
				$scope.loadBranchData();
				$scope.loadCustomerData();

			}else{
				$scope.showBranch=false;
				

			}
		  	
	}]);
})();
