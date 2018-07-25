(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('callSpecificReportCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();
		$scope.cutomers=[];
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
 	        	 $scope.selectedLift.selected = undefined;
 	        	 $scope.selectedCallID.selected = undefined;
 	        	 var emptyArray=[];
 	        	 $scope.myData = emptyArray;
 	         })
		}
		
		function initReport(){
			$scope.selectedCompany = {};
			$scope.selectedBranch = {};
			 $scope.lifts=[];
			 $scope.callID=[];
			 $scope.branches = [];
			 $scope.selectedCustomer = {};
			 $scope.selectedLift = {};
			 $scope.selectedCallID= {};
			 $scope.selectedAmc = {};
			 $scope.showMembers = false;
			 			 
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
			//$scope.loadCallID();
		}
		
		
		$scope.loadLifts = function() {
			
  		var dataToSend = {
  				branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
				branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
			}
			serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
					.then(function(liftData) {
						$scope.lifts = liftData;
						$scope.selectedLift.selected = undefined;
						$scope.selectedCallID.selected = undefined;
						
					})

		}
$scope.loadCallID=function(){
	var dataToSend = {
				branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
			branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
		}
		serviceApi.doPostWithData('/RLMS/report/callSpecificReport',dataToSend)
				.then(function(callData) {
					$scope.callID = callData;			
					
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
			$scope.showBranch = true;
		} else {
			$scope.showBranch = false;
		}
		
		
		/*if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
			$scope.showBranch == true
			$scope.loadCustomerData();
		}*/
		//Show Member List
		//$scope.filterOptions.filterText='';

		$scope.loadCallSpecificList = function(){
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
		  	        serviceApi.doPostWithData('/RLMS/report/callSpecificReport',dataToSend)
		  	         .then(function(largeLoad) {
		  	        	  var details=[];
		  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        		var detailsObj={};
		  	        		detailsObj["SrNo"] =i+1 +".";
		  	        		
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
		  	        		if(!!largeLoad[i].status){
		  	        			detailsObj["Status"] =largeLoad[i].status;
		  	        		}else{
		  	        			detailsObj["Status"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].callAssignedDateStr){
		  	        			detailsObj["CallAssignedDate"] =largeLoad[i].callAssignedDateStr;
		  	        		}else{
		  	        			detailsObj["CallAssignedDate"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].resolvedDateStr){
		  	        			detailsObj["CallResolvedDate"] =largeLoad[i].resolvedDateStr;
		  	        		}else{
		  	        			detailsObj["CallResolvedDate"] =" - ";
		  	        		}
		  	        		
		  	        		if(!!largeLoad[i].complaintNumber){
		  	        			detailsObj["CallId"] =largeLoad[i].complaintNumber;
		  	        		}else{
		  	        			detailsObj["CallId"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].customerAddress){
		  	        			detailsObj["address"] =largeLoad[i].customerAddress;
		  	        		}else{
		  	        			detailsObj["address"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].customerCity){
		  	        			detailsObj["city"] =largeLoad[i].customerCity;
		  	        		}else{
		  	        			detailsObj["city"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].registeredBy){
		  	        			detailsObj["CallRegisteredBy"] =largeLoad[i].registeredBy;
		  	        		}else{
		  	        			detailsObj["CallRegisteredBy"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].serviceCallTypeStr){
		  	        			detailsObj["CallType"] =largeLoad[i].serviceCallTypeStr;
		  	        		}else{
		  	        			detailsObj["CallType"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].title){
		  	        			detailsObj["Title"] =largeLoad[i].title;
		  	        		}else{
		  	        			detailsObj["Title"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].registrationDateStr){
		  	        			detailsObj["RegistrationDate"] =largeLoad[i].registrationDateStr;
		  	        		}else{
		  	        			detailsObj["RegistrationDate"] =" - ";
		  	        		}if(largeLoad[i].siteVisitDetailsList){
		  	        				if(!!largeLoad[i].siteVisitDetailsList.techName){
		  	        					detailsObj["Technician"] =largeLoad[i].siteVisitDetailsList.techName;
		  	        				}else{
		  	        					detailsObj["Technician"] =" - ";
		  	        				}
		  	        				if(largeLoad[i].siteVisitDetailsList.listOFAllVisits){
		  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[i].fromDateDtr){
		  	        						detailsObj["FromDate"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[i].fromDateDtr;
		  	        					}else{
		  	        						detailsObj["FromDate"] =" - ";
		  	        					}
		  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[i].toDateStr){
		  	        						detailsObj["ToDate"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[i].toDateStr;
		  	        					}else{
		  	        						detailsObj["ToDate"] =" - ";
		  	        					}
		  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[i].remark){
					  	        			detailsObj["Remark"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[i].remark;
					  	        		}else{
					  	        			detailsObj["Remark"] =" - ";
					  	        		}
		  	        				}
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
			  	    	
		  	        	serviceApi.doPostWithData('/RLMS/report/callSpecificReport',dataToSend)
		  	        	.then(function(largeLoad) {
		  	        	  var details=[];
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
				  	        		detailsObj["SrNo"] =i+1 +".";
				  	        		
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
				  	        		if(!!largeLoad[i].status){
				  	        			detailsObj["Status"] =largeLoad[i].status;
				  	        		}else{
				  	        			detailsObj["Status"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].callAssignedDateStr){
				  	        			detailsObj["CallAssignedDate"] =largeLoad[i].callAssignedDateStr;
				  	        		}else{
				  	        			detailsObj["CallAssignedDate"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].resolvedDateStr){
				  	        			detailsObj["CallResolvedDate"] =largeLoad[i].resolvedDateStr;
				  	        		}else{
				  	        			detailsObj["CallResolvedDate"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].remark){
				  	        			detailsObj["Remark"] =largeLoad[i].remark;
				  	        		}else{
				  	        			detailsObj["Remark"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].complaintNumber){
				  	        			detailsObj["CallId"] =largeLoad[i].complaintNumber;
				  	        		}else{
				  	        			detailsObj["CallId"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].customerAddress){
				  	        			detailsObj["address"] =largeLoad[i].customerAddress;
				  	        		}else{
				  	        			detailsObj["address"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].customerCity){
				  	        			detailsObj["city"] =largeLoad[i].customerCity;
				  	        		}else{
				  	        			detailsObj["city"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].registeredBy){
				  	        			detailsObj["CallRegisteredBy"] =largeLoad[i].registeredBy;
				  	        		}else{
				  	        			detailsObj["CallRegisteredBy"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].serviceCallTypeStr){
				  	        			detailsObj["CallType"] =largeLoad[i].serviceCallTypeStr;
				  	        		}else{
				  	        			detailsObj["CallType"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].title){
				  	        			detailsObj["Title"] =largeLoad[i].title;
				  	        		}else{
				  	        			detailsObj["Title"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].registrationDateStr){
				  	        			detailsObj["RegistrationDate"] =largeLoad[i].registrationDateStr;
				  	        		}else{
				  	        			detailsObj["RegistrationDate"] =" - ";
				  	        		}
				  	        		if(largeLoad[i].siteVisitDetailsList){
			  	        				if(!!largeLoad[i].siteVisitDetailsList.techName){
			  	        					detailsObj["Technician"] =largeLoad[i].siteVisitDetailsList.techName;
			  	        				}else{
			  	        					detailsObj["Technician"] =" - ";
			  	        				}
				  	        				if(largeLoad[i].siteVisitDetailsList.listOFAllVisits){
				  	        					for(j=0;j<=listOFAllVisits.length;j++){
					  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].fromDateDtr){
				  	        						detailsObj["FromDate"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].fromDateDtr;
					  	        					}else{
					  	        						detailsObj["FromDate"] =" - ";
					  	        					}
					  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].toDateStr){
					  	        						detailsObj["ToDate"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].toDateStr;
					  	        					}else{
					  	        						detailsObj["ToDate"] =" - ";
					  	        					}
					  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[i].remark){
								  	        			detailsObj["Remark"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].remark;
								  	        		}else{
								  	        			detailsObj["Remark"] =" - ";
								  	        		}
				  	        					}
			  	        					}
				  	        		}
				  	        		details.push(detailsObj);
				  	        	  
		  	        				  }
		  	        				 }
		  	        			  }
		  	        		}
		  	        		  if((($scope.selectedCallID.selected) &&($scope.selectedCallID.selected.complaintNumber === largeLoad[i].complaintNumber)) ){
		  	        			  var detailsObj={};
		  	        		detailsObj["SrNo"] =i+1 +".";
		  	        		
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
		  	        		if(!!largeLoad[i].status){
		  	        			detailsObj["Status"] =largeLoad[i].status;
		  	        		}else{
		  	        			detailsObj["Status"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].callAssignedDateStr){
		  	        			detailsObj["CallAssignedDate"] =largeLoad[i].callAssignedDateStr;
		  	        		}else{
		  	        			detailsObj["CallAssignedDate"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].resolvedDateStr){
		  	        			detailsObj["CallResolvedDate"] =largeLoad[i].resolvedDateStr;
		  	        		}else{
		  	        			detailsObj["CallResolvedDate"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].remark){
		  	        			detailsObj["Remark"] =largeLoad[i].remark;
		  	        		}else{
		  	        			detailsObj["Remark"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].complaintNumber){
		  	        			detailsObj["CallId"] =largeLoad[i].complaintNumber;
		  	        		}else{
		  	        			detailsObj["CallId"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].customerAddress){
		  	        			detailsObj["address"] =largeLoad[i].customerAddress;
		  	        		}else{
		  	        			detailsObj["address"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].customerCity){
		  	        			detailsObj["city"] =largeLoad[i].customerCity;
		  	        		}else{
		  	        			detailsObj["city"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].registeredBy){
		  	        			detailsObj["CallRegisteredBy"] =largeLoad[i].registeredBy;
		  	        		}else{
		  	        			detailsObj["CallRegisteredBy"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].serviceCallTypeStr){
		  	        			detailsObj["CallType"] =largeLoad[i].serviceCallTypeStr;
		  	        		}else{
		  	        			detailsObj["CallType"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].title){
		  	        			detailsObj["Title"] =largeLoad[i].title;
		  	        		}else{
		  	        			detailsObj["Title"] =" - ";
		  	        		}
		  	        		if(!!largeLoad[i].registrationDateStr){
		  	        			detailsObj["RegistrationDate"] =largeLoad[i].registrationDateStr;
		  	        		}else{
		  	        			detailsObj["RegistrationDate"] =" - ";
		  	        		}
		  	        		if(largeLoad[i].siteVisitDetailsList){
	  	        				if(!!largeLoad[i].siteVisitDetailsList.techName){
	  	        					detailsObj["Technician"] =largeLoad[i].siteVisitDetailsList.techName;
	  	        				}else{
	  	        					detailsObj["Technician"] =" - ";
	  	        				}
	  	        				if(largeLoad[i].siteVisitDetailsList.listOFAllVisits){
	  	        					for(j=0;j<=listOFAllVisits.length;j++){
		  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].fromDateDtr){
	  	        						detailsObj["FromDate"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].fromDateDtr;
		  	        					}else{
		  	        						detailsObj["FromDate"] =" - ";
		  	        					}
		  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].toDateStr){
		  	        						detailsObj["ToDate"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].toDateStr;
		  	        					}else{
		  	        						detailsObj["ToDate"] =" - ";
		  	        					}
		  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[i].remark){
					  	        			detailsObj["Remark"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].remark;
					  	        		}else{
					  	        			detailsObj["Remark"] =" - ";
					  	        		}
	  	        					}
  	        					}
		  	        		}
		  	        		details.push(detailsObj);
		  	        	  }
		  	        		  if((!($scope.selectedCallID.selected))&& (!($scope.selectedLift.selected))){

					  	        	var detailsObj={};
				  	        		detailsObj["SrNo"] =i+1 +".";
				  	        		
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
				  	        		if(!!largeLoad[i].status){
				  	        			detailsObj["Status"] =largeLoad[i].status;
				  	        		}else{
				  	        			detailsObj["Status"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].callAssignedDateStr){
				  	        			detailsObj["CallAssignedDate"] =largeLoad[i].callAssignedDateStr;
				  	        		}else{
				  	        			detailsObj["CallAssignedDate"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].resolvedDateStr){
				  	        			detailsObj["CallResolvedDate"] =largeLoad[i].resolvedDateStr;
				  	        		}else{
				  	        			detailsObj["CallResolvedDate"] =" - ";
				  	        		}
				  	        		
				  	        		if(!!largeLoad[i].complaintNumber){
				  	        			detailsObj["CallId"] =largeLoad[i].complaintNumber;
				  	        		}else{
				  	        			detailsObj["CallId"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].customerAddress){
				  	        			detailsObj["address"] =largeLoad[i].customerAddress;
				  	        		}else{
				  	        			detailsObj["address"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].customerCity){
				  	        			detailsObj["city"] =largeLoad[i].customerCity;
				  	        		}else{
				  	        			detailsObj["city"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].registeredBy){
				  	        			detailsObj["CallRegisteredBy"] =largeLoad[i].registeredBy;
				  	        		}else{
				  	        			detailsObj["CallRegisteredBy"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].serviceCallTypeStr){
				  	        			detailsObj["CallType"] =largeLoad[i].serviceCallTypeStr;
				  	        		}else{
				  	        			detailsObj["CallType"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].title){
				  	        			detailsObj["Title"] =largeLoad[i].title;
				  	        		}else{
				  	        			detailsObj["Title"] =" - ";
				  	        		}
				  	        		if(!!largeLoad[i].registrationDateStr){
				  	        			detailsObj["RegistrationDate"] =largeLoad[i].registrationDateStr;
				  	        		}else{
				  	        			detailsObj["RegistrationDate"] =" - ";
				  	        		}if(largeLoad[i].siteVisitDetailsList){
				  	        				if(!!largeLoad[i].siteVisitDetailsList.techName){
				  	        					detailsObj["Technician"] =largeLoad[i].siteVisitDetailsList.techName;
				  	        				}else{
				  	        					detailsObj["Technician"] =" - ";
				  	        				}
				  	        				if(largeLoad[i].siteVisitDetailsList.listOFAllVisits){
				  	        					for(j=0;j<=listOFAllVisits.length;j++){
				  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].fromDateDtr){
				  	        						detailsObj["FromDate"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].fromDateDtr;
				  	        					}else{
				  	        						detailsObj["FromDate"] =" - ";
				  	        					}
				  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].toDateStr){
				  	        						detailsObj["ToDate"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].toDateStr;
				  	        					}else{
				  	        						detailsObj["ToDate"] =" - ";
				  	        					}
				  	        					if(!!largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].remark){
							  	        			detailsObj["Remark"] =largeLoad[i].siteVisitDetailsList.listOFAllVisits[j].remark;
							  	        		}else{
							  	        			detailsObj["Remark"] =" - ";
							  	        		}
				  	        					}
				  	        				}
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
		  	      //groupBy:'customerName',
		  	      columnDefs : [{
						field : "SrNo",
						displayName:"Sr No.",
						width : 100
			  	  },{
						field : "CallId",
						displayName:"Call Id",
						width : 140
			  	  },{
						field : "CallType",
						displayName:"Call Type",
						width : 140
			  	  },{
						field : "Title",
						displayName:"Title",
						width : 140
			  	  },{
						field : "CallRegisteredBy",
						displayName:"Call Registered By",
						width : 140
			  	  },{
						field : "liftNumber",
						displayName:"Lift Number",
						width : 120
			  	  },{
						field : "CustomerName",
						displayName:"Customer",
						width : 140
			  	  },{
						field : "address",
						displayName:"Customer Address",
						width : 140
			  	  },{
						field : "BranchName",
						displayName:"Branch",
						width : 140
			  	  },{
						field : "city",
						displayName:"City",
						width : 140
			  	  },{
						field : "RegistrationDate",
						displayName:"Call Registration Date",
						width : 140
			  	  },{
						field : "CallAssignedDate",
						displayName:"Call Assigned Date",
						width : 160
			  	  },{
						field : "CallResolvedDate",
						displayName:"Call Resolved Date",
						width : 160
			  	  },{
						field : "Technician",
						displayName:"Technician",
						width : 120
			  	  },{
						field : "FromDate",
						displayName:"From Date",
						width : 120
			  	  },{
						field : "ToDate",
						displayName:"To Date",
						width : 120
			  	  },{
						field : "Remark",
						displayName:"Remarks",
						width : 120
		  	      },{
						field : "Status",
						displayName:"Status",
						width : 120
			  	  }
		  	      ]
		  	    };
	   

	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	  		/*var tempStatus = [];
	  		if($scope.selectedEventType.selected){
	  			if($scope.selectedEventType.selected.length===0){
	  				alert("Please select Event Type");
	  				for (var j = 0; j < $scope.selectedEventType.selected.length; j++) {
	  					tempStatus.push($scope.selectedEventType.selected[j].id);
	  					//}
	  				}
	  			}
	  		}else{
	  			alert("Please select Event Type");
	  		}*/		
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
