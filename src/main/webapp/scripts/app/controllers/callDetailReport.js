(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('callDetailedReportCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();
		$scope.cutomers=[];
		$scope.eventId=[];
		$scope.filterOptions = {
		  	      filterText: '',
		  	      useExternalFilter: true
		  	    };
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
 	        	 $scope.selectedCallType.selected = undefined;
 	        	 var emptyArray=[];
 	        	 $scope.myData = emptyArray;
 	         })
		}
		
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
		// showCompnay Flag
				
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
		
		
		/*$scope.loadLifts = function() {
			
  		var dataToSend = {
  				branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
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
		}*/
		
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1) {
			$scope.showCompany = true;
			loadCompanyData();
			
		} else {
			$scope.showCompany = false;
			$scope.loadBranchData();
		}
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
			$scope.showBranch = true;
			//loadDefaultComplaintData();
		} else {
			$scope.showBranch = false;
		}
		
		/*if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
			$scope.loadCustomerData();
		}*/
		
		/*if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
			$scope.showBranch == true
			$scope.loadCustomerData();
		}*/
		//Show Member List
		//$scope.filterOptions.filterText='';

		$scope.loadCallDetailList = function(){
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
		  	        serviceApi.doPostWithData('/RLMS/report/callDetailsReport',dataToSend)
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
		  	        }
		  	        else {		  	        	
		  	        	var dataToSend = constructDataToSend();
			  	    	
		  	        	serviceApi.doPostWithData('/RLMS/report/callDetailsReport',dataToSend)
		  	        	.then(function(largeLoad) {
		  	        	  var details=[];
		  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        		  
		  	        		if(($scope.selectedCallType.selected) && ($scope.selectedCallType.selected.id ===largeLoad[i].serviceCallType)){
			  	        		var detailsObj={};		  	        	
			  	        		detailsObj["SrNo"] = i+1 + ".";
		  	        		if(!!largeLoad[i].customerName){
		  	        			detailsObj["Customer"] =largeLoad[i].customerName;
		  	        		}else{
		  	        			detailsObj["Customer"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].serviceCallTypeStr){
		  	        			detailsObj["CallType"] =largeLoad[i].serviceCallTypeStr;
		  	        		}else{
		  	        			detailsObj["CallType"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].liftNumber){
		  	        			detailsObj["liftNumber"] =largeLoad[i].liftNumber;
		  	        		}else{
		  	        			detailsObj["liftNumber"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].status){
		  	        			detailsObj["Status"] =largeLoad[i].status;
		  	        		}else{
		  	        			detailsObj["Status"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].title){
		  	        			detailsObj["Title"] =largeLoad[i].title;
		  	        		}else{
		  	        			detailsObj["Title"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].registrationDateStr){
		  	        			detailsObj["RegDate"] =largeLoad[i].registrationDateStr;
		  	        		}else{
		  	        			detailsObj["RegDate"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].totalDaysRequiredToResolveComplaint){
		  	        			detailsObj["TotalDaysTaken"] =largeLoad[i].totalDaysRequiredToResolveComplaint;
		  	        		}else{
		  	        			detailsObj["TotalDaysTaken"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].lastVisitedDate){
		  	        			detailsObj["LastVisitedDate"] =largeLoad[i].lastVisitedDate;
		  	        		}else{
		  	        			detailsObj["LastVisitedDate"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].callAssignedDateStr){
		  	        			detailsObj["callAssignedDate"] =largeLoad[i].callAssignedDateStr;
		  	        		}else{
		  	        			detailsObj["callAssignedDate"] =" - ";
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
		  	        		
		  	          if(!($scope.selectedCallType.selected)){
		  	        		  
		  	        		  
		  	        		var detailsObj={};		  	        	
		  	        		detailsObj["SrNo"] = i+1 + ".";
	  	        		if(!!largeLoad[i].customerName){
	  	        			detailsObj["Customer"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			detailsObj["Customer"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceCallTypeStr){
	  	        			detailsObj["CallType"] =largeLoad[i].serviceCallTypeStr;
	  	        		}else{
	  	        			detailsObj["CallType"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			detailsObj["liftNumber"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			detailsObj["liftNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].status){
	  	        			detailsObj["Status"] =largeLoad[i].status;
	  	        		}else{
	  	        			detailsObj["Status"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].title){
	  	        			detailsObj["Title"] =largeLoad[i].title;
	  	        		}else{
	  	        			detailsObj["Title"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].registrationDateStr){
	  	        			detailsObj["RegDate"] =largeLoad[i].registrationDateStr;
	  	        		}else{
	  	        			detailsObj["RegDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].totalDaysRequiredToResolveComplaint){
	  	        			detailsObj["TotalDaysTaken"] =largeLoad[i].totalDaysRequiredToResolveComplaint;
	  	        		}else{
	  	        			detailsObj["TotalDaysTaken"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].lastVisitedDate){
	  	        			detailsObj["LastVisitedDate"] =largeLoad[i].lastVisitedDate;
	  	        		}else{
	  	        			detailsObj["LastVisitedDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].callAssignedDateStr){
	  	        			detailsObj["callAssignedDate"] =largeLoad[i].callAssignedDateStr;
	  	        		}else{
	  	        			detailsObj["callAssignedDate"] =" - ";
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
						field : "SrNo",
						displayName:"Sr No.",
						width : 100
			  	  },{
						field : "CallType",
						displayName:"Call Type",
						width : 140
			  	  },{
						field : "Title",
						displayName:"Title",
						width : 140
			  	  },{
						field : "liftNumber",
						displayName:"Lift Number",
						width : 120
			  	  },{
						field : "Customer",
						displayName:"Customer",
						width : 140
			  	  },{
						field : "RegDate",
						displayName:"Registration Date ",
						width : 140
			  	  },{
						field : "callAssignedDate",
						displayName:"Technician Assigned Date",
						width : 170
			  	  },{
						field : "Status",
						displayName:"Status",
						width : 140
			  	  },{
						field : "LastVisitedDate",
						displayName:"Last Visited Date",
						width : 160
			  	  },{
						field : "TotalDaysTaken",
						displayName:"Total Days Taken",
						width : 160
		  	      }
		  	      ]
		  	    };
	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	
	  		var tempbranchCustomerMapIds = [];
			/*if($scope.selectedCustomer.selected.length > 0){
				for (var j = 0; j < $scope.selectedCustomer.selected.length; j++) {
					tempbranchCustomerMapIds.push($scope.selectedCustomer.selected[j].branchCustomerMapId);
				}
			}*/
	  		//tempbranchCustomerMapIds.push($scope.selectedCustomer.selected.branchCustomerMapId);
			
			/*if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
				$scope.companyBranchMapIdForCustomer=$rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId;
			}else{
				$scope.companyBranchMapIdForCustomer=$scope.selectedBranch.selected.companyBranchMapId;
			}*/
	  		var data = {
	  				//listOfUserRoleIds:tempListOfUserRoleIds,
	  				//listOfEventTypeIds:tempStatus,
	  				branchCustomerMapId:$scope.selectedCustomer.selected.branchCustomerMapId
	  		};
	  		return data;
	  	  }
	}]);
})();
