(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('lmsAlertsCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();
		$scope.cutomers=[];
		$scope.eventId=[];
		$scope.filterOptions = {
		  	      filterText: '',
		  	      useExternalFilter: true
		  	    };
		/*$scope.goToAddAMC = function(){
			window.location.hash = "#/add-amc";
		}*/
		
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
						//$scope.selectedStatus.selected=undefined;
						//$scope.selectedEventType.selected=undefined;
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
			 $scope.eventType = [ 
				{
					id : 1,
					name : 'EVENT'
				}, {
					id : 2,
					name : 'ERROR'
				}, {
					id : 3,
					name : 'ALL'
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
 	        	 $scope.selectedLift.selected = undefined;
 	        	// $scope.selectedCallID.selected = undefined;
 	        	 var emptyArray=[];
 	        	 $scope.myData = emptyArray;
 	         })
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
		
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
			$scope.loadCustomerData();
		}
		
		$scope.loadEventList = function(){
			$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
			$scope.showMembers = true;
		}
		
		//Show Member List
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
		  	        serviceApi.doPostWithData('/RLMS/report/getListOfEvents',dataToSend)
		  	         .then(function(largeLoad) {
		  	        	  var details=[];
		  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        		var detailsObj={};
		  	        		detailsObj["No"] = i+1 +".";
	  	        			
	  	        			if(!!largeLoad[i].customerName){
		  	        			detailsObj["CustomerName"] =largeLoad[i].customerName;
		  	        		}else{
		  	        			detailsObj["CustomerName"] =" - ";
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
		  	        		if(!!largeLoad[i].liftAddress){
		  	        			detailsObj["liftAddress"] =largeLoad[i].liftAddress;
		  	        		}else{
		  	        			detailsObj["liftAddress"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].city){
		  	        			detailsObj["city"] =largeLoad[i].city;
		  	        		}else{
		  	        			detailsObj["city"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].eventFromContactNo){
		  	        			detailsObj["LMSContactNo"] =largeLoad[i].eventFromContactNo;
		  	        		}else{
		  	        			detailsObj["LMSContactNo"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].eventType){
		  	        			detailsObj["EventType"] =largeLoad[i].eventType;
		  	        		}else{
		  	        			detailsObj["EventType"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].date){
		  	        			detailsObj["EventDateTime"] =largeLoad[i].date;
		  	        		}else{
		  	        			detailsObj["EventDateTime"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].eventFromContactNo){
		  	        			detailsObj["eventFromContactNo"] =largeLoad[i].eventFromContactNo;
		  	        		}else{
		  	        			detailsObj["eventFromContactNo"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].eventDescription){
		  	        			detailsObj["Description"] =largeLoad[i].eventDescription;
		  	        		}else{
		  	        			detailsObj["Description"] =" - ";
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
			  	    	
		  	        	serviceApi.doPostWithData('/RLMS/report/getListOfEvents',dataToSend).then(function(largeLoad) {
		  	        	  var details=[];
		  	        	  var k=0;
		  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        		
		  	        		if($scope.selectedLift.selected  && $scope.selectedLift.selected.length>0){	  
		  	        			var tempLiftIds = [];
		  	        			  for (var i = 0; i < $scope.selectedLift.selected.length; i++) {
		  	        				  tempLiftIds.push($scope.selectedLift.selected[i].liftNumber);
		  	        			  }
		  	        			  for(var j=0; j<tempLiftIds.length;j++){
		  	        				 for(var i=0;i<largeLoad.length;i++){
		  	        				  if(tempLiftIds[j]==largeLoad[i].liftNumber){ 
			  	        	var detailsObj={};
	  	        			k=k+1;
			  	        	detailsObj["No"] = k +".";
	  	        			
	  	        			if(!!largeLoad[i].customerName){
		  	        			detailsObj["CustomerName"] =largeLoad[i].customerName;
		  	        		}else{
		  	        			detailsObj["CustomerName"] =" - ";
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
		  	        		if(!!largeLoad[i].liftAddress){
		  	        			detailsObj["liftAddress"] =largeLoad[i].liftAddress;
		  	        		}else{
		  	        			detailsObj["liftAddress"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].city){
		  	        			detailsObj["city"] =largeLoad[i].city;
		  	        		}else{
		  	        			detailsObj["city"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].eventFromContactNo){
		  	        			detailsObj["LMSContactNo"] =largeLoad[i].eventFromContactNo;
		  	        		}else{
		  	        			detailsObj["LMSContactNo"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].eventType){
		  	        			detailsObj["EventType"] =largeLoad[i].eventType;
		  	        		}else{
		  	        			detailsObj["EventType"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].date){
		  	        			detailsObj["EventDateTime"] =largeLoad[i].date;
		  	        		}else{
		  	        			detailsObj["EventDateTime"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].eventDescription){
		  	        			detailsObj["Description"] =largeLoad[i].eventDescription;
		  	        		}else{
		  	        			detailsObj["Description"] =" - ";
		  	        		}
		  	        		details.push(detailsObj);
		  	        	  }
		  	        				 }
		  	        			  }
		  	        		}if(!($scope.selectedLift.selected)){
				  	        	var detailsObj={};
		  	        			
				  	        	detailsObj["No"] = i+1 +".";
		  	        			
		  	        			if(!!largeLoad[i].customerName){
			  	        			detailsObj["CustomerName"] =largeLoad[i].customerName;
			  	        		}else{
			  	        			detailsObj["CustomerName"] =" - ";
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
			  	        		if(!!largeLoad[i].liftAddress){
			  	        			detailsObj["liftAddress"] =largeLoad[i].liftAddress;
			  	        		}else{
			  	        			detailsObj["liftAddress"] =" - ";
			  	        		}
			  	        		if(!!largeLoad[i].city){
			  	        			detailsObj["city"] =largeLoad[i].city;
			  	        		}else{
			  	        			detailsObj["city"] =" - ";
			  	        		}
			  	        		if(!!largeLoad[i].eventFromContactNo){
			  	        			detailsObj["LMSContactNo"] =largeLoad[i].eventFromContactNo;
			  	        		}else{
			  	        			detailsObj["LMSContactNo"] =" - ";
			  	        		}
			  	        		if(!!largeLoad[i].eventType){
			  	        			detailsObj["EventType"] =largeLoad[i].eventType;
			  	        		}else{
			  	        			detailsObj["EventType"] =" - ";
			  	        		}
			  	        		if(!!largeLoad[i].date){
			  	        			detailsObj["EventDateTime"] =largeLoad[i].date;
			  	        		}else{
			  	        			detailsObj["EventDateTime"] =" - ";
			  	        		}
			  	        		if(!!largeLoad[i].eventFromContactNo){
			  	        			detailsObj["eventFromContactNo"] =largeLoad[i].eventFromContactNo;
			  	        		}else{
			  	        			detailsObj["eventFromContactNo"] =" - ";
			  	        		}
			  	        		if(!!largeLoad[i].eventDescription){
			  	        			detailsObj["Description"] =largeLoad[i].eventDescription;
			  	        		}else{
			  	        			detailsObj["Description"] =" - ";
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
						field : "No",
						displayName:"Sr No.",
						width : 100
			  	  },{
						field : "EventType",
						displayName:"Event Type",
						width : 160
			  	  },{
						field : "LMSContactNo",
						displayName:"LMS Contact no.",
						width : 160
			  	  },{
						field : "EventDateTime",
						displayName:"Event DateTime",
						width : 160
			  	  },{
						field : "Description",
						displayName:"Description",
						width : 160
			  	  },{
						field : "CustomerName",
						displayName:"Customer",
						width : 140
			  	  },{
						field : "BranchName",
						displayName:"Branch",
						width : 140
			  	  },{
						field : "liftNumber",
						displayName:"Lift Number",
						width : 140
			  	  },{
						field : "liftAddress",
						displayName:"Lift Address",
						width : 140
			  	  },{
						field : "city",
						displayName:"City",
						width : 140
			  	  }
		  	      ]
		  	    };
	   
	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	  		var tempbranchCustomerMapIds = [];
			//if($scope.selectedCustomer.selected.length ){
			//	for (var j = 0; j < $scope.selectedCustomer.selected.length; j++) {
					tempbranchCustomerMapIds.push($scope.selectedCustomer.selected.branchCustomerMapId);
				//}
			//}
			
			if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
				$scope.companyBranchMapIdForCustomer=$rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId;
			}else{
				$scope.companyBranchMapIdForCustomer=$scope.selectedBranch.selected.companyBranchMapId;
			}
			var tempEventType;
			if($scope.selectedEventType.selected.name =="ALL"){
				tempEventType=="-1";
			}else{
				tempEventType=$scope.selectedEventType.selected.name;
			}
				
	  		var data = {
	  				//companyBranchMapId:$scope.companyBranchMapIdForCustomer,
	  				//companyId:9,
	  				//listOfUserRoleIds:tempListOfUserRoleIds,
	  				//listOfEventTypeIds:$scope.selectedEventType.selected.id,
	  				eventType:tempEventType,
	  				branchCustomerMapId:tempbranchCustomerMapIds,
	  				//serviceCallType:1
	  		};
	  		return data;
	  	  }
	}]);
})();
