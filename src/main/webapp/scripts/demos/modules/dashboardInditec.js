angular.module('theme.demos.dashboard.indi', [
  'angular-skycons',
  'theme.demos.forms',
  'theme.demos.tasks',
  'ngStorage'
])
  .controller('DashboardControllerInditech', ['$scope', '$timeout', '$window', '$modal', 'serviceApi', '$filter', '$rootScope','$localStorage','locker','$http', function ($scope, $timeout, $window, $modal, serviceApi, $filter, $rootScope,$localStorage,locker,$http) {
    'use strict';

    $scope.totalServerItemsForComplaints = 0;
    $scope.pagingOptionsForComplaints = {
      pageSizes: [10, 20, 50],
      pageSize: 10,
      currentPage: 1
    };
    
    //spinner
    var app = angular.module("MyApp", ["ngResource"]);

    app.config(function ($httpProvider) {
      $httpProvider.responseInterceptors.push('myHttpInterceptor');

      var spinnerFunction = function spinnerFunction(data, headersGetter) {
        $("#mySpinner").show();
        return data;
      };

      $httpProvider.defaults.transformRequest.push(spinnerFunction);
    });

    app.factory('myHttpInterceptor', function ($q, $window) {
      return function (promise) {
        return promise.then(function (response) {
          $("#mySpinner").hide();
          return response;
        }, function (response) {
          $("#mySpinner").hide();
          return $q.reject(response);
        });
      };
    });
    //spinner end

    $scope.showCompanies=false;
    $scope.showAmc=true;
    $scope.showBranches=false;
    $rootScope.showDasboardForInditech=false;
    $rootScope.showDasboardForOthers=false;
    //$rootScope.showDashboardForOperator=false;
    
   /* $scope.loggedInuserDetails={
    		userName:{
    			firstName:'0',
    			color:'white'
    		}
    		
    };*/
    $http({
		  method: 'POST',
		  url: '/RLMS/getLoggedInUser'
		}).then(function successCallback(response) {
			
		/*	if(response.data==""){
				alert("Your session has expired. Please login again");
				window.location="login.jsp"
		}
			else{
*/			$rootScope.loggedInUserInfoForDashboard=response;
			
			if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.spocRoleId == 4 || $rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.spocRoleId == 6){
				window.location.hash = "#/complaint-management";
			}

			if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 4 &&$rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.spocRoleId < 4  ){
				$rootScope.showDasboardForInditech= true;
				$rootScope.showDasboardForOthers=false;
			}else{
				//$rootScope.showDasboardForOthers=true;
				$rootScope.showDasboardForInditech=true;
				//$rootScope.showDashboardForOperator=true;
			}
			if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompanies= true;
				$scope.showBranches=true;
			}else{
				$scope.showCompanies= false;
			}
			
			if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 2){
				//$scope.showCompanies= false;
				$scope.showBranches=true;
			}else{
				//$scope.showCompanies= false;
				$scope.showBranches=false;
			}
			
	//	}
/*			 $scope.loggedInuserDetails.userName.firstName=$rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsUserMaster.firstName
*/		  }, function errorCallback(response) {
	  });
    
    $scope.technicianData = {
    		
    		totalTechnicians: {
    			title: 'Total',
    			text: '0',
    			color: 'red'
    		},
    		activeTechnicians: {
    			title: 'Active',
    			text: '0',
    			color: 'amber'
    		},
    		inactiveTechnicians: {
    			title: 'Inactive',
    			text: '0',
    			color: 'blue'
    		}
    	};
    
    $scope.event = {
    		inout: {
    	        title: 'In-Out Event',
    	        text: '0',
    	        color: 'red'
    	      },
    		error: {
    			title: 'ERROR',
    			text: '0',
    			color: 'amber'
               },
            responses: {
   				title: 'RESPONSE',
   				text: '0',
   				color: 'blue'
              },
              todaysEvents: {
 				title: 'Todays Events',
 				text: '0',
 				color: 'green'
              },
              todaysErrors: {
   				title: 'Todays Errors',
   				text: '0',
   				color: 'indigo'
              },
              todaysResponses: {
 				title: 'Todays Responses',
 				text: '0',
 				color: 'toyo'
               }
    	    };
    $scope.amcSeriveCalls = {
    	        title: 'AMC Service Calls',
    	        text: '0',
    	        color: 'red'
    	    };
    
    $scope.liftStatus = {
    	      totalInstalled: {
    	        title: 'Total Installed',
    	        text: '0',
    	        color: 'red'
    	      },
    	      activeLiftStatus: {
    	        title: 'Under Warranty',
    	        text: '0',
    	        color: 'amber'
    	      },
    	      inactiveLiftStatus: {
    	        title: 'Warranty Expired',
    	        text: '0',
    	        color: 'blue'
    	      }
    	    };
    
    $scope.amcDetailsData = {
    	      totalRenewalForThisMonth: {
    	        title: 'Total Renewal For This Month',
    	        text: '0',
    	        color: 'red'
    	      },
    	      activeAmc: {
    	        title: 'Under AMC',
    	        text: '0',
    	        color: 'amber'
    	      },
    	      inactiveAmc: {
    	        title: 'Inactive',
    	        text: '0',
    	        color: 'blue'
    	      },
    	      expiredAmc: {
      	        title: 'AMC Expired',
      	        text: '0',
      	        color: 'blue'
      	      }
    	    };
   
    $scope.companiesData = {
  	      totalCompanies: {
  	        title: 'Total',
  	        text: '0',
  	        color: 'red'
  	      },
  	      activeCompanies: {
  	        title: 'Active',
  	        text: '0',
  	        color: 'amber'
  	      },
  	      inactiveCompanies: {
  	        title: 'Inactive',
  	        text: '0',
  	        color: 'blue'
  	      },
  	      serviceRenewalDueCompanies: {
    	        title: 'Service Renewal Due',
    	        text: '0',
    	        color: 'blue'
    	      }
  	    };
    
    $scope.customersDetails = {
  	      totalCustomers: {
  	        title: 'Total',
  	        text: '0',
  	        color: 'red'
  	      },
  	      activeCustomers: {
  	        title: 'Active',
  	        text: '0',
  	        color: 'amber'
  	      },
  	      inactiveCustomers: {
  	        title: 'Inactive',
  	        text: '0',
  	        color: 'blue'
  	      }
  	    };

    $scope.branchDetails = {
    		totalBranches: {
    	        title: 'Total',
    	        text: '0',
    	        color: 'red'
    	      },
    	      activeBranches: {
    	        title: 'Active',
    	        text: '0',
    	        color: 'amber'
    	      },
    	      inactiveBranches: {
    	        title: 'Inactive',
    	        text: '0',
    	        color: 'blue'
    	      }
    	    };
    
    $scope.complaintsData = {
      totalComplaints: {
        title: 'Total',
        text: '0',
        color: 'red'
      },
      totalUnassignedComplaints: {
        title: 'Total Unassigned',
        text: '0',
        color: 'amber'
      },
      totalAssignedComplaints: {
        title: 'Total Assigned',
        text: '0',
        color: 'blue'
      },
      totalResolvedComplaints: {
        title: 'Total Resolved',
        text: '0',
        color: 'green'
      },
      totalPendingComplaints: {
        title: 'Total Pending',
        text: '0',
        color: 'indigo'
      },
      avgLogPerDay: {
        title: 'Avg Log Per Day',
        text: '0',
        color: 'toyo'
      },
      todaysTotalComplaints: {
        title: 'Todays Total',
        text: '0',
        color: 'red'
      },
      todaysUnassignedComplaints: {
        title: 'Todays Unassigned',
        text: '0',
        color: 'amber'
      },
      todaysAssignedComplaints: {
        title: 'Todays Assigned',
        text: '0',
        color: 'blue'
      },
      todaysTotalAssignedComplaints: {
          title: 'Todays Total Assigned',
          text: '0',
          color: 'indigo'
        },
      todaysResolvedComplaints: {
        title: 'Todays Resolved',
        text: '0',
        color: 'green'
      },
      todaysTotalResolvedComplaints: {
          title: 'Todays Total Resolved',
          text: '0',
          color: 'toyo'
        },
      todaysPandingComplaints: {
        title: 'Todays Pending',
        text: '0',
        color: 'indigo'
      },
      avgResolvedPerDayRegistered: {
        title: 'Avg Resolved Per Day',
        text: '0',
        color: 'grey'
      }
    };

    $scope.pendingComplaints = {
      title: 'Pending Complaints',
      text: '0',
      color: 'red'
    };

    $scope.assignedComplaints = {
      title: 'Assigned Complaints',
      text: '0',
      color: 'amber'
    };

    $scope.attemptedTodayComplaints = {
      title: 'Complaints Attempted Today',
      text: '0',
      color: 'blue'
    };
    $scope.resolvedComplaints = {
      title: 'Resolved Complaints',
      text: '0',
      color: 'green'
    };
    $scope.totalComplaints = {
      title: 'Total Complaints',
      text: '0',
      color: 'indigo'
    };
    $scope.newCustomerRegistered = {
      title: 'New Customers Registered',
      text: '0',
      color: 'toyo'
    };
    $scope.gridOptionsForComplaints = {
      data: 'myComplaintsData',
      rowHeight: 40,
      enablePaging: true,
      showFooter: true,
      totalServerItems: 'totalServerItemsForComplaints',
      pagingOptions: $scope.pagingOptionsForComplaints,
      filterOptions: $scope.filterOptionsForModal,
      multiSelect: false,
      gridFooterHeight:50,
      enableRowSelection: true,
      selectedItems: [],
      afterSelectionChange: function (rowItem, event) {
      }
    };

    $scope.filterOptionsForModal = {
      filterText: '',
      useExternalFilter: true
    };

    $scope.todaysDate = new Date();
    $scope.todaysDate.setHours(0, 0, 0, 0);
    $scope.testComplaintValue="Before";
    $scope.getComplaintsCount = function (complaintStatus) {
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');
      for (var i = 0; i < str_array.length; i++) {
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        complaintStatusArray.push(str_array[i]);
      }
      $scope.testComplaintValue="After";
      $scope.loading = true;
    //Total Calls
      setTimeout(
        function () {
          var dataToSend = $scope
            .construnctObjeToSend(complaintStatusArray);
          serviceApi
            .doPostWithData(
       		'/RLMS/dashboard/getListOfTotalComplaintsCountByCallType',dataToSend)
            .then(
            function (
              largeLoad) {             
            	  var totalCount=0;
            	  for (var i = 0; i < largeLoad.length; i++)
            	  {
            		  if(largeLoad[i].totalCallTypeCount!=null){
            			  totalCount=totalCount+largeLoad[i].totalCallTypeCount;
            			  $scope.complaintsData.totalComplaints.text = totalCount;  
            		  }else{
            			  $scope.complaintsData.totalComplaints.text ="0";
            		  }
            		 /* if(largeLoad[i].avgLogsPerDay!=null){
            			  $scope.complaintsData.avgLogPerDay.text = largeLoad[i].avgLogsPerDay;
            		  }*/
            	  }
            	  if(largeLoad[0].avgLogsPerDay!=null){
        			  $scope.complaintsData.avgLogPerDay.text = largeLoad[0].avgLogsPerDay;
        		  }
            	  if(largeLoad[0].avgResolvedPerDay!=null){
        			  $scope.complaintsData.avgResolvedPerDayRegistered.text = largeLoad[0].avgResolvedPerDay;
        		  }
            	  $scope.loading = false;            
            });
        }, 100);
      
    //Todays Total Calls 
     setTimeout(
    	        function () {
    	          var dataToSend = $scope
    	            .construnctObjeToSend(complaintStatusArray);
    	          serviceApi
    	            .doPostWithData(
    	       		'/RLMS/dashboard/getListOfTodaysComplaintsCountByCallType',dataToSend)
    	            .then(
    	            function (
    	              largeLoad) {
    	            	  var totalCount=0;
    	            	  for (var i = 0; i < largeLoad.length; i++)
    	            	  {
    	            		  if(largeLoad[i].todaysCallTypeCount!=null){
    	            			  totalCount=totalCount+largeLoad[i].todaysCallTypeCount;
    	            			  $scope.complaintsData.todaysTotalComplaints.text = totalCount;
    	            		  }else{
    	            			  $scope.complaintsData.todaysTotalComplaints.text = "0";
    	            		  }
    	            	  }
    	            	                  

    	            });
    	        }, 100);
     
     // Total Calls by Status
      setTimeout(
        function () {
          var dataToSend = $scope
            .construnctObjeToSend(complaintStatusArray);
          serviceApi
            .doPostWithData(
       		'/RLMS/dashboard/getListOfTotalComplaintsCountByStatus',dataToSend)
            .then(
            function (
              largeLoad) {
            	  var totalCount=0;
            	  for (var i = 0; i < largeLoad.length; i++)
            	  {
            		  if(largeLoad[i].callStatus== "Pending"){
            		  if(largeLoad[i].totalCallStatusCount!=null){
            			  totalCount=totalCount+largeLoad[i].totalCallStatusCount;
            			  $scope.complaintsData.totalUnassignedComplaints.text = totalCount;
            		  }else{
               			$scope.complaintsData.totalUnassignedComplaints.text == "0";
               		  }
            		  }
            	  }

            
              //Total Assigned
              	 var totalCount=0;
               	  for (var i = 0; i < largeLoad.length; i++)
               	  {
               		 if(largeLoad[i].callStatus== "Assigned"||largeLoad[i].callStatus== "In Progress"){
               			 if(largeLoad[i].totalCallStatusCount!=null){
               				 totalCount=totalCount+largeLoad[i].totalCallStatusCount;
               				$scope.complaintsData.totalAssignedComplaints.text = totalCount;
               		  }else{
               			$scope.complaintsData.totalAssignedComplaints.text == "0";
               		  }
               		 }
               	  }
               	  
              //total Resolved
              	 var totalCount=0;
              	  for (var i = 0; i < largeLoad.length; i++)
              	  {
              		  if(largeLoad[i].callStatus== "Resolved"){
              		  if(largeLoad[i].totalCallStatusCount!=null){
              			  totalCount=totalCount+largeLoad[i].totalCallStatusCount;
              			 $scope.complaintsData.totalResolvedComplaints.text = totalCount;
              		  }else{
               			$scope.complaintsData.totalResolvedComplaints.text == "0";
               		  }
              		  }
              		
              	  }
            });
        }, 100);
      
      //Todays Total Calls by Status
      setTimeout(
    	        function () {
    	          var dataToSend = $scope
    	            .construnctObjeToSend(complaintStatusArray);
    	          serviceApi
    	            .doPostWithData(
    	       		'/RLMS/dashboard/getListOfTodaysComplaintsCountByStatus',dataToSend)
    	            .then(
    	            function (
    	              largeLoad) {
    	            	  var totalCount=0;
    	            	  for (var i = 0; i < largeLoad.length; i++)
    	            	  {
    	            		  if(largeLoad[i].callStatus== "Pending"){
    	            		  if(largeLoad[i].todaysCallStatusCount!=null){
    	            			  totalCount=totalCount+largeLoad[i].todaysCallStatusCount;
    	            			  $scope.complaintsData.todaysUnassignedComplaints.text = totalCount;
    	            		  }else{
    	               			$scope.complaintsData.todaysUnassignedComplaints.text == "0";
    	               		  }
    	            		  }
    	            	  }
    	                
    	                              
    	              //Total Assigned
    	              	 var totalCount=0;
    	               	  for (var i = 0; i < largeLoad.length; i++)
    	               	  {
    	               		 if(largeLoad[i].callStatus== "Assigned"||largeLoad[i].callStatus== "In Progress"){
    	               			 if(largeLoad[i].todaysCallStatusCount!=null){
    	               				 totalCount=totalCount+largeLoad[i].todaysCallStatusCount;
    	               				$scope.complaintsData.todaysAssignedComplaints.text = totalCount;
    	               		  }else{
    	               			$scope.complaintsData.todaysAssignedComplaints.text == "0";
    	               		  }
    	               		 }
    	               	  }
    	               	  
    	              //total Resolved
    	              	 var totalCount=0;
    	              	  for (var i = 0; i < largeLoad.length; i++)
    	              	  {
    	              		  if(largeLoad[i].callStatus== "Resolved"){
    	              		  if(largeLoad[i].todaysCallStatusCount!=null){
    	              			  totalCount=totalCount+largeLoad[i].todaysCallStatusCount;
    	              			 $scope.complaintsData.todaysResolvedComplaints.text = totalCount;
    	              		  }else{
    	               			$scope.complaintsData.todaysResolvedComplaints.text == "0";
    	               		  }
    	              		  }
    	              		
    	              	  }
    	            });
    	        }, 100);
      
      //Todays Total Assigned
      //Todays Total Calls by Status
      setTimeout(
    	        function () {
    	          var dataToSend = $scope
    	            .construnctObjeToSend(complaintStatusArray);
    	          serviceApi
    	            .doPostWithData(
    	       		'/RLMS/dashboard/getListOfTodaysTotalComplaintsCountByStatus',dataToSend)
    	            .then(
    	            function (
    	              largeLoad) {
    	                              
    	              //Todays Total Assigned
    	              	 var totalCount=0;
    	               	  for (var i = 0; i < largeLoad.length; i++)
    	               	  {
    	               		 if(largeLoad[i].callStatus== "Assigned"||largeLoad[i].callStatus== "In Progress"){
    	               			 if(largeLoad[i].todaysCallStatusCount!=null){
    	               				 totalCount=totalCount+largeLoad[i].todaysCallStatusCount;
    	               				$scope.complaintsData.todaysTotalAssignedComplaints.text = totalCount;
    	               		  }else{
    	               			$scope.complaintsData.todaysTotalAssignedComplaints.text == "0";
    	               		  }
    	               		 }
    	               	  }
    	               	  
    	              //Todays Total Resolved
    	              	 var totalCount=0;
    	              	  for (var i = 0; i < largeLoad.length; i++)
    	              	  {
    	              		  if(largeLoad[i].callStatus== "Resolved"){
    	              		  if(largeLoad[i].todaysCallStatusCount!=null){
    	              			  totalCount=totalCount+largeLoad[i].todaysCallStatusCount;
    	              			 $scope.complaintsData.todaysTotalResolvedComplaints.text = totalCount;
    	              		  }else{
    	               			$scope.complaintsData.todaysTotalResolvedComplaints.text == "0";
    	               		  }
    	              		  }
    	              		
    	              	  }
    	            });
    	        }, 100);
    };
       
    

    $scope.getComplaintsCountForSiteVisited = function (complaintStatus) {
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');

      for (var i = 0; i < str_array.length; i++) {
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        complaintStatusArray.push(str_array[i]);
      }
      setTimeout(
        function () {
          var dataToSend = $scope
            .construnctObjeToSend(complaintStatusArray);
          serviceApi
            .doPostWithData(
            '/RLMS/dashboard/getListOfComplaintsForSiteVisited',
            dataToSend)
            .then(
            function (
              largeLoad) {
              if (largeLoad.length > 0) {
                $scope.attemptedTodayComplaints.text = largeLoad.length;
              }
            });
        }, 100);
    };

    $scope.getComplaintsCount('2');
    $scope.getComplaintsCount('3');
    $scope.getComplaintsCount('5');
    $scope.getComplaintsCount('2,3,5');
    $scope.getComplaintsCountForSiteVisited('2,3,5');

    $scope.todaysComplaintsList = function (title) {
      var emptyComplaintsArray = [];
      $scope.myComplaintsData = emptyComplaintsArray;
      $scope.pagingOptionsForComplaints.currentPage = 1;
      $scope.totalServerItemsForComplaints = 0;
      if (title == "todaysTotalPending") {
        $scope.tempComplaintsData = $scope.todaysPendingComplaints;
      } else if (title == "todaysUnassigned") {
        $scope.tempComplaintsData = $scope.todaysUnassignedComplaints;
      } else if (title == "todaysResolved") {
        $scope.tempComplaintsData = $scope.todaysResolvedComplaints;
      } else if (title == "todaysAssigned") {
        $scope.tempComplaintsData = $scope.todaysAssignedComplaints;
      } else if (title == "todaysTotal") {
        $scope.tempComplaintsData = $scope.todaysTotalComplaints;
      }
      var userDetails = [];
      var userDetailsObj = {};
      var i = 0;
      for (i = 0; i < $scope.tempComplaintsData.length; i++) {
        userDetailsObj["No"] = $scope.tempComplaintsData[i].complaintNumber;
        userDetailsObj["CompanyName"] = $scope.tempComplaintsData[i].companyName;
        userDetailsObj["Title"] = $scope.tempComplaintsData[i].title;
        userDetailsObj["City"] = $scope.tempComplaintsData[i].city;
        userDetails.push(userDetailsObj);
      }
      $scope.setPagingDataForComplaints(userDetails, $scope.pagingOptionsForComplaints.currentPage, $scope.pagingOptionsForComplaints.pageSize);
      $scope.modalInstance = $modal.open({
        templateUrl: 'demoModalContent.html',
        scope: $scope
      });
    };

    $scope.openDemoModal = function (currentModelOpen, complaintStatus, headerValue,isTodaysData,headingValue) {
      var emptyComplaintsArray = [];
      $scope.myComplaintsData = emptyComplaintsArray;
      $scope.pagingOptionsForComplaints.currentPage = 1;
      $scope.totalServerItemsForComplaints = 0;
      $scope.filterOptionsForModal.filterText='';
      $scope.currentModel = currentModelOpen;
      $scope.modalHeaderVal = headerValue;
      $scope.modalHeading = headingValue;
      $scope.isTodaysData=isTodaysData;
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');
      for (var i = 0; i < str_array.length; i++) {
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        complaintStatusArray.push(str_array[i]);
      }
      $scope.currentComplaintStatus = complaintStatusArray;
      $scope.getPagedDataAsyncForComplaints($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "", complaintStatusArray, currentModelOpen,isTodaysData,headerValue);
      $scope.complaintStatusValue = complaintStatusArray;
      $scope.modalInstance = $modal.open({
        templateUrl: 'demoModalContent.html',
        scope: $scope
      });
    };

    $scope.$watch('pagingOptionsForComplaints', function (newVal, oldVal) {
      if (newVal !== oldVal) {
    	  if($scope.currentModel==="complaints"){
      		$scope
              .getPagedDataAsyncForComplaints(
              $scope.pagingOptionsForComplaints.pageSize,
              $scope.pagingOptionsForComplaints.currentPage,
              $scope.filterOptionsForModal.filterText,
              $scope.currentComplaintStatus,
              $scope.currentModel,
              $scope.isTodaysData);
      	}else if($scope.currentModel==="amcDetails"){
      		$scope.getPagedDataAsyncForAllAMCDetails($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForAMC);
      	}else if($scope.currentModel==="technician"){
      		$scope.getPagedDataAsyncForTechnician($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForTechnician);
      	}else if($scope.currentModel==="liftStatus"){
      		$scope.getPagedDataAsyncForAllLiftStatus($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForLiftStatus);
      	}else if($scope.currentModel==="customerDetails"){
      		$scope.getPagedDataAsyncForAllCustomers($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForCustomers);
      	}else if($scope.currentModel==="companyDetails"){
      		$scope.getPagedDataAsyncForAllCompanies($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForCompanies);
      	}else if($scope.currentModel==="events"){
      		$scope.getPagedDataAsyncForEvents($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForEvents);
      	}
      	else if($scope.currentModel==="branches"){
      		$scope.getPagedDataAsyncForAllBranches($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForBranches);
      	};
      }
    }, true);
    $scope
      .$watch(
      'filterOptionsForModal',
      function (newVal, oldVal) {
        if (newVal !== oldVal) {
        	if($scope.currentModel==="complaints"){
        		$scope
                .getPagedDataAsyncForComplaints(
                $scope.pagingOptionsForComplaints.pageSize,
                $scope.pagingOptionsForComplaints.currentPage,
                $scope.filterOptionsForModal.filterText,
                $scope.currentComplaintStatus,
                $scope.currentModel,
                $scope.isTodaysData);
        	}else if($scope.currentModel==="amcDetails"){
        		$scope.getPagedDataAsyncForAllAMCDetails($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForAMC);
        	}else if($scope.currentModel==="technician"){
          		$scope.getPagedDataAsyncForTechnician($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForTechnician);
          	}else if($scope.currentModel==="liftStatus"){
          		$scope.getPagedDataAsyncForAllLiftStatus($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForLiftStatus);
          	}else if($scope.currentModel==="customerDetails"){
          		$scope.getPagedDataAsyncForAllCustomers($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForCustomers);
          	}else if($scope.currentModel==="companyDetails"){
          		$scope.getPagedDataAsyncForAllCompanies($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForCompanies);
          	}else if($scope.currentModel==="events"){
          		$scope.getPagedDataAsyncForEvents($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForEvents);
          	}
          	else if($scope.currentModel==="branches"){
          		$scope.getPagedDataAsyncForAllBranches($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText,$scope.activeFlagForBranches);
          	};
          
        }
      }, true);

    $scope.setPagingDataForComplaints = function (data, page, pageSize) {
        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
        $scope.myComplaintsData = pagedData;
        $scope.totalServerItemsForComplaints = data.length;
        if (!$scope.$$phase) {
          $scope.$apply();
        }
      };
      
    $scope.getPagedDataAsyncForComplaints = function (pageSize,
      page, searchText, complaintStatus, callingModel,isTodaysData,headerValue) {
      var url;
      
      url = '/RLMS/dashboard/getListOfTodaysComplaintsCountByCallType';
      
      setTimeout(
        function () {
          var data;
          if (searchText) {
            var ft = searchText
              .toLowerCase();
            var dataToSend = $scope
              .construnctObjeToSend(complaintStatus);
            serviceApi
              .doPostWithData(url, dataToSend)
              .then(
              function (largeLoad) {
                $scope.complaints = largeLoad;
                $scope.showTable = true;
                var userDetails = [];
                if(isTodaysData){
                	largeLoad=largeLoad.filter(function (item) {
                        return (new Date(item.updatedDate)).getTime() === $scope.todaysDate.getTime();
                      });
                }
                for (var i = 0; i < largeLoad.length; i++) {
                  var userDetailsObj = {};
                  /*if (!!largeLoad[i].complaintNumber) {
                    userDetailsObj["No"] = i+1 +".";
                  } else {
                    userDetailsObj["No"] = " - ";
                  }
                  if (!!largeLoad[i].branchName) {
                    userDetailsObj["Branch"] = largeLoad[i].branchName;
                  } else {
                    userDetailsObj["Branch"] = " - ";
                  }
                  if (!!largeLoad[i].customerName) {
                      userDetailsObj["Customer"] = largeLoad[i].customerName;
                    } else {
                      userDetailsObj["Customer"] = " - ";
                    }
                  if (!!largeLoad[i].city) {
                      userDetailsObj["City"] = largeLoad[i].city;
                    } else {
                      userDetailsObj["City"] = " - ";
                    }
                  if (complaintStatus.includes("3")) {
                      if (!!largeLoad[i].title) {
                        userDetailsObj["Title"] = largeLoad[i].title;
                      } else {
                        userDetailsObj["Title"] = " - ";
                      }
                    } 
                  if (!!largeLoad[i].totalComplaints) {
                    userDetailsObj["Total_Complaints"] = largeLoad[i].totalComplaints;
                  } else {
                    userDetailsObj["Total_Complaints"] = " - ";
                  }*/
                  
                  if (!!largeLoad[i].callType) {
                      userDetailsObj["CallType"] = largeLoad[i].callType;
                    } else {
                      userDetailsObj["CallType"] = " - ";
                    }
                  if (!!largeLoad[i].totalCallTypeCount) {
                      userDetailsObj["TotalCount"] = largeLoad[i].totalCallTypeCount;
                    } else {
                      userDetailsObj["TotalCount"] = " - ";
                    }
                  userDetails
                    .push(userDetailsObj);
                }
                
                data = userDetails
                  .filter(function (
                    item) {
                    return JSON
                      .stringify(
                      item)
                      .toLowerCase()
                      .indexOf(
                      ft) !== -1;
                  });
                $scope
                  .setPagingDataForComplaints(
                  data,
                  page,
                  pageSize);
              });
          } else   if (headerValue=="Total Calls" ) {
            var dataToSend = $scope
              .construnctObjeToSend(complaintStatus);
            serviceApi
              .doPostWithData('/RLMS/dashboard/getListOfTotalComplaintsCountByCallType',
              dataToSend)
              .then(
              function (
                largeLoad) {
                $scope.complaints = largeLoad;
                $scope.showTable = true;
                var userDetails = [];
                for (var i = 0; i < largeLoad.length; i++) {
                  var userDetailsObj = {};
                  
                    userDetailsObj["No"] = i+1 +".";
                  
                  if (!!largeLoad[i].callType) {
                    userDetailsObj["CallType"] = largeLoad[i].callType;
                  } else {
                    userDetailsObj["CallType"] = " - ";
                  }
                  if (!!largeLoad[i].totalCallTypeCount) {
                    userDetailsObj["TotalCount"] = largeLoad[i].totalCallTypeCount;
                  } else {
                    userDetailsObj["TotalCount"] = " - ";
                  }
                  userDetails
                    .push(userDetailsObj);
                }
                $scope
                  .setPagingDataForComplaints(
                  userDetails,
                  page,
                  pageSize);
              });

          }else if(isTodaysData){
        	  if(headerValue =="Todays Total Calls"){
        		var dataToSend = $scope
                .construnctObjeToSend(complaintStatus);
            	serviceApi
                .doPostWithData('/RLMS/dashboard/getListOfTodaysComplaintsCountByCallType',
                dataToSend)
                .then(
                function (
                  largeLoad) {
                  $scope.complaints = largeLoad;
                  $scope.showTable = true;
                  var userDetails = [];
                  
                  for (var i = 0; i < largeLoad.length; i++) {
                      var userDetailsObj = {};
                      
                        userDetailsObj["No"] = i+1 +".";
                      
                      if (!!largeLoad[i].callType) {
                        userDetailsObj["CallType"] = largeLoad[i].callType;
                      } else {
                        userDetailsObj["CallType"] = " - ";
                      }
                      if (!!largeLoad[i].todaysCallTypeCount) {
                        userDetailsObj["TotalCount"] = largeLoad[i].todaysCallTypeCount;
                      } else {
                        userDetailsObj["TotalCount"] = " - ";
                      }
                      userDetails
                        .push(userDetailsObj);
                    }
                    $scope
                      .setPagingDataForComplaints(
                      userDetails,
                      page,
                      pageSize);
                  });
        	  }else if(headerValue==="Todays Unassigned" ||headerValue==="Todays Assigned"|| headerValue==="Todays Resolved"){
        		  var dataToSend = $scope
                  .construnctObjeToSend(complaintStatus);
              	serviceApi
                  .doPostWithData('/RLMS/dashboard/getListOfTodaysComplaintsCountByStatus',
                  dataToSend)
                  .then(
                  function (
                    largeLoad) {
                    $scope.complaints = largeLoad;
                    $scope.showTable = true;
                    var userDetails = [];
                    var data=[];
                    if(headerValue==="Todays Unassigned"){     	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].callStatus== "Pending"){
	                			var dataCount={};
	                			dataCount.callType=largeLoad[i].callType
	                			dataCount.todaysCallStatusCount=largeLoad[i].todaysCallStatusCount
	                			
	                			data.push(dataCount);
	                		}
	                	}
	                }
                  if(headerValue==="Todays Assigned"){
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].callStatus=="Assigned" || largeLoad[i].callStatus== "In Progress"){
	                			var dataCount={};
	                			dataCount.callType=largeLoad[i].callType
	                			dataCount.callStatus=largeLoad[i].callStatus
	                			dataCount.todaysCallStatusCount=largeLoad[i].todaysCallStatusCount            			
	                			data.push(dataCount);
	                		}
	                	}
	                }
                  if(headerValue==="Todays Resolved"){     	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].callStatus=="Resolved"){
	                			var dataCount={};
	                			dataCount.callType=largeLoad[i].callType
	                			dataCount.todaysCallStatusCount=largeLoad[i].todaysCallStatusCount
	                			
	                			data.push(dataCount);
	                		}
	                	}
	                }         
                    for (var i = 0; i < data.length; i++) {
                        var userDetailsObj = {};
                        
                          userDetailsObj["No"] = i+1 +".";
                        
                        if (!!data[i].callType) {
                          userDetailsObj["CallType"] = data[i].callType;
                        } else {
                          userDetailsObj["CallType"] = " - ";
                        }
                        if(headerValue==="Todays Assigned"){
                        if (!!data[i].callStatus) {
                            userDetailsObj["CallStatus"] = data[i].callStatus;
                          } else {
                            userDetailsObj["CallStatus"] = " - ";
                          }
                        }
                        if (!!data[i].todaysCallStatusCount) {
                          userDetailsObj["TotalCount"] = data[i].todaysCallStatusCount;
                        } else {
                          userDetailsObj["TotalCount"] = " - ";
                        }
                        userDetails
                          .push(userDetailsObj);
                      }
                      $scope
                        .setPagingDataForComplaints(
                        userDetails,
                        page,
                        pageSize);
                    });
        	  }else{
        		  var dataToSend = $scope
                  .construnctObjeToSend(complaintStatus);
              	serviceApi
                  .doPostWithData('/RLMS/dashboard/getListOfTodaysTotalComplaintsCountByStatus',
                  dataToSend)
                  .then(
                  function (
                    largeLoad) {
                    $scope.complaints = largeLoad;
                    $scope.showTable = true;
                    var userDetails = [];
                    var data=[];

                  if(headerValue==="Todays Total Assigned"){
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].callStatus=="Assigned" ||largeLoad[i].callStatus== "In Progress"){
	                			var dataCount={};
	                			dataCount.callType=largeLoad[i].callType
	                			dataCount.callStatus=largeLoad[i].callStatus
	                			dataCount.todaysCallStatusCount=largeLoad[i].todaysCallStatusCount	                			
	                			data.push(dataCount);
	                		}
	                	}
	                }
                  if(headerValue==="Todays Total Resolved"){     	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].callStatus=="Resolved"){
	                			var dataCount={};
	                			dataCount.callType=largeLoad[i].callType
	                			dataCount.todaysCallStatusCount=largeLoad[i].todaysCallStatusCount
	                			
	                			data.push(dataCount);
	                		}
	                	}
	                }         
                    for (var i = 0; i < data.length; i++) {
                        var userDetailsObj = {};
                        
                          userDetailsObj["No"] = i+1 +".";
                        
                        if (!!data[i].callType) {
                          userDetailsObj["CallType"] = data[i].callType;
                        } else {
                          userDetailsObj["CallType"] = " - ";
                        }
                        if(headerValue==="Todays Total Assigned"){
                            if (!!data[i].callStatus) {
                                userDetailsObj["CallStatus"] = data[i].callStatus;
                              } else {
                                userDetailsObj["CallStatus"] = " - ";
                              }
                            }
                        if (!!data[i].todaysCallStatusCount) {
                          userDetailsObj["TotalCount"] = data[i].todaysCallStatusCount;
                        } else {
                          userDetailsObj["TotalCount"] = " - ";
                        }
                        userDetails
                          .push(userDetailsObj);
                      }
                      $scope
                        .setPagingDataForComplaints(
                        userDetails,
                        page,
                        pageSize);
                    });
        	  }
          }else{
        	  var dataToSend = $scope
              .construnctObjeToSend(complaintStatus);
            	serviceApi
                .doPostWithData('/RLMS/dashboard/getListOfTotalComplaintsCountByStatus',
                dataToSend)
                .then(
                function (
                  largeLoad) {
                  $scope.complaints = largeLoad;
                  $scope.showTable = true;
                  var userDetails = [];
                  var data=[];
                  if(headerValue==="Total Unassigned"){     	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].callStatus== "Pending"){
	                			var dataCount={};
	                			dataCount.callType=largeLoad[i].callType
	                			dataCount.totalCallStatusCount=largeLoad[i].totalCallStatusCount
	                			
	                			data.push(dataCount);
	                		}
	                	}
	                }
                  if(headerValue==="Total Assigned"){
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].callStatus=="Assigned" ||largeLoad[i].callStatus== "In Progress"){
	                			var dataCount={};
	                			dataCount.callType=largeLoad[i].callType
	                			dataCount.callStatus=largeLoad[i].callStatus
	                			dataCount.totalCallStatusCount=largeLoad[i].totalCallStatusCount	                			
	                			data.push(dataCount);
	                		}
	                	}
	                }
                  if(headerValue==="Total Resolved"){     	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].callStatus=="Resolved"){
	                			var dataCount={};
	                			dataCount.callType=largeLoad[i].callType
	                			dataCount.totalCallStatusCount=largeLoad[i].totalCallStatusCount
	                			
	                			data.push(dataCount);
	                		}
	                	}
	                }                  
                  for (var i = 0; i < data.length; i++) {
                      var userDetailsObj = {};
                      
                        userDetailsObj["No"] = i+1 +".";
                      
                          if (!!data[i].callType) {
		                    userDetailsObj["CallType"] = data[i].callType;
		                  } else {
		                    userDetailsObj["CallType"] = " - ";
		                  }
                          if(headerValue==="Total Assigned"){
                              if (!!data[i].callStatus) {
                                  userDetailsObj["CallStatus"] = data[i].callStatus;
                                } else {
                                  userDetailsObj["CallStatus"] = " - ";
                                }
                              }
                    	  if (!!data[i].totalCallStatusCount) {
                    		  userDetailsObj["TotalCount"] = data[i].totalCallStatusCount;
                    	  } else {
                    		  userDetailsObj["TotalCount"] = " - ";
                    	  }          
                      userDetails
                        .push(userDetailsObj);
                    }
                    $scope
                      .setPagingDataForComplaints(
                      userDetails,
                      page,
                      pageSize);
                  });
          }
        }, 100);
    };

    $scope.cancel = function () {
      $scope.modalInstance.dismiss('cancel');
    };

    $scope.construnctObjeToSend = function (complaintStatus) {
    	if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 3){
    		var tempbranchCompanyMapId=0;
        	tempbranchCompanyMapId = $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
        }else{
        	var tempcompanyId=0;
        	tempcompanyId=$rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId
        }
      var dataToSend = {
        statusList: [],
        companyId: tempcompanyId,// $rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId
        branchCompanyMapId:tempbranchCompanyMapId
      };
      dataToSend["statusList"] = complaintStatus;
      return dataToSend;
    };
    
    $scope.openDemoModalForTechnician = function (currentModelOpen, headerValue, activeFlag, headingValue) {
        var emptyComplaintsArray = [];
        $scope.myComplaintsData = emptyComplaintsArray;
        $scope.pagingOptionsForComplaints.currentPage = 1;
        $scope.totalServerItemsForComplaints = 0;
        $scope.filterOptionsForModal.filterText='';
        $scope.currentModel = currentModelOpen;
        $scope.modalHeaderVal = headerValue;
        $scope.modalHeading = headingValue;
        $scope.activeFlagForTechnician = activeFlag;
        $scope.getPagedDataAsyncForTechnician($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",activeFlag); 
        $scope.modalInstance = $modal.open({
          templateUrl: 'demoModalContent.html',
          scope: $scope
        });
      };
    
    $scope.getPagedDataAsyncForTechnician = function (pageSize,
    	      page, searchText, activeFlag) {
    	      var url;
    	      url = '/RLMS/dashboard/getTotalCountOfTechniciansForBranch';
    	      setTimeout(
    	        function () {
    	          var data;
    	          if (searchText) {
    	            var ft = searchText
    	              .toLowerCase();
    	            var dataToSend = $scope
    	              .construnctObjeToSendForTechnician();
    	            serviceApi
    	              .doPostWithData(url, dataToSend)
    	              .then(
    	              function (largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                if (activeFlag=="Active") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });
      	                }
    	                if (activeFlag=="InActive") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });
      	                }
    	                for (var i = 0; i < largeLoad.length; i++) {
    	                  var userDetailsObj = {};
    	                  if (!!largeLoad[i].userId) {
    	                    userDetailsObj["No"] = i+1 +".";
    	                  } else {
    	                    userDetailsObj["No"] = " - ";
    	                  }
    	                  if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
	    	                  if (!!largeLoad[i].branchName) {
	      	                    userDetailsObj["Branch"] = largeLoad[i].branchName;
	      	                  } else {
	      	                    userDetailsObj["Branch"] = " - ";
	      	                  }
    	                  }
    	                  if (!!largeLoad[i].city) {
      	                    userDetailsObj["City"] = largeLoad[i].city;
      	                  } else {
      	                    userDetailsObj["City"] = " - ";
      	                  }
    	                  if (!!largeLoad[i].totalTechnicianCount) {
      	                    userDetailsObj["Total_Technician"] = largeLoad[i].totalTechnicianCount;
      	                  } else {
      	                    userDetailsObj["Total_Technician"] = " - ";
      	                  }
    	                  userDetails
    	                    .push(userDetailsObj);
    	                }
    	                
    	                data = userDetails
    	                  .filter(function (
    	                    item) {
    	                    return JSON
    	                      .stringify(
    	                      item)
    	                      .toLowerCase()
    	                      .indexOf(
    	                      ft) !== -1;
    	                  });
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  data,
    	                  page,
    	                  pageSize);
    	              });
    	          } else {
    	            var dataToSend = $scope
    	              .construnctObjeToSendForTechnician();
    	            serviceApi
    	              .doPostWithData(url,
    	              dataToSend)
    	              .then(
    	              function (
    	                largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                var dataCount={};
    	                var data=[];
    	                if (activeFlag=="Active") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
    	                		return item.activeFlag === 1;   	                	
    	                	});*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].totolActiveTechnician!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.count=largeLoad[i].totolActiveTechnician
    	                			
    	                			data.push(dataCount);
    	                		}    	                		
    	                	}    	                	   	                	
      	                }
    	                if (activeFlag=="InActive") {
    	                	
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].totalInactiveTechnician!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.count=largeLoad[i].totalInactiveTechnician
    	                			
    	                			data.push(dataCount);
    	                		}
    	                	}
      	                }
    	                if (activeFlag=="Total") {
    	                	
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].count!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.count=largeLoad[i].count
    	                			
    	                			data.push(dataCount);
    	                		}
    	                	}
      	                }
    	                for (var i = 0; i < data.length; i++) {
      	                  var userDetailsObj = {};
      	                var dataCount={};
      	                  
      	                    userDetailsObj["No"] = i+1 +".";
      	                  if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
	      	                  if (!!data[i].branchName) {
	        	                userDetailsObj["Branch"] = data[i].branchName;
	        	              } else {
	        	                userDetailsObj["Branch"] = " - ";
	        	              }
      	                  }
      	                  if (!!data[i].city) {
      	                    userDetailsObj["City"] = data[i].city;
      	                  } else {
      	                    userDetailsObj["City"] = " - ";
      	                  }
      	                  if (!!data[i].count) {
        	                userDetailsObj["Total_Technician"] = data[i].count;
        	              } else {
        	                userDetailsObj["Total_Technician"] = " - ";
        	              }
      	                  userDetails
      	                    .push(userDetailsObj);
      	                }
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  userDetails,
    	                  page,
    	                  pageSize);
    	              });

    	          }
    	        }, 100);
    	    };
    	    $scope.getTechnicianCount = function (technicianStatus) {
    	        var complaintStatusArray = [];
    	        
    	        setTimeout(
    	          function () {
    	            var dataToSend = $scope
    	              .construnctObjeToSendForTechnician();
    	            serviceApi
    	              .doPostWithData(
    	              '/RLMS/dashboard/getTotalCountOfTechniciansForBranch',
    	              dataToSend)
    	              .then(
    	              function (
    	                largeLoad) {
    	                 if (technicianStatus=="Active") {
    	                	var totalCount= 0;
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].totolActiveTechnician!=null){
    	                			totalCount=totalCount+largeLoad[i].totolActiveTechnician;
    	                			$scope.technicianData.activeTechnicians.text=totalCount;
    	                		}/*else{
    	                			$scope.technicianData.activeTechnicians.text="0";
    	                		}*/
    	                	}
    	                	  	                 
    	                  
    	                }
    	                if (technicianStatus=="InActive") {
    	                	var totalCount= 0;
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].totalInactiveTechnician!=null){
    	                			totalCount=totalCount+largeLoad[i].totalInactiveTechnician;
    	                			$scope.technicianData.inactiveTechnicians.text=totalCount;
    	                		}/*else{
    	                			$scope.technicianData.inactiveTechnicians.text="0";
    	                		}*/
    	                	}
    	                	  	                 
    	                  
    	                }
    	                
    	                if(technicianStatus=="TotalTechnician"){
    	                	var totalCount= 0;
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].count!=null){
    	                			totalCount=totalCount+largeLoad[i].count;
    	                		}
    	                		
    	                	}
        	                  $scope.technicianData.totalTechnicians.text=totalCount;
    	                }
    	              });
    	          }, 100);
    	      };
    	      
    $scope.getTechnicianCount("Active");
    $scope.getTechnicianCount("InActive");
    $scope.getTechnicianCount("TotalTechnician");
    
    $scope.construnctObjeToSendForTechnician = function () {
    	if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 3){
    		var tempbranchCompanyMapId=0;
        	tempbranchCompanyMapId = $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
        }else{
        	var tempcompanyId=0;
        	tempcompanyId=$rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId
        }
        var dataToSend = {
          companyId:tempcompanyId ,// $rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId,
          branchCompanyMapId:tempbranchCompanyMapId
          }
        
        return dataToSend;
      };
      
      //AMC Start
      $scope.constructDataToSendForAllAMCDetails=function() {
        	var tempStatus =[];
        	tempStatus.push(38);
        	tempStatus.push(39);
        	tempStatus.push(40);
        	tempStatus.push(41);
        	if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 3){
        		var tempbranchCompanyMapId=0;
            	tempbranchCompanyMapId = $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
            }else{
            	var tempcompanyId=0;
            	tempcompanyId=$rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId
            }
          var data = {
            companyId:tempcompanyId, //$rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId,
            branchCompanyMapId:tempbranchCompanyMapId,
            listOFStatusIds:tempStatus
          };
          return data;
        };
        
        $scope.openDemoModalForAllAMCDetails = function (currentModelOpen, headerValue, activeFlag, headingValue) {
            var emptyComplaintsArray = [];
            $scope.myComplaintsData = emptyComplaintsArray;
            $scope.pagingOptionsForComplaints.currentPage = 1;
            $scope.totalServerItemsForComplaints = 0;
            $scope.currentModel = currentModelOpen;
            $scope.filterOptionsForModal.filterText='';
            $scope.modalHeaderVal = headerValue;
            $scope.modalHeading = headingValue;
            $scope.activeFlagForAMC=activeFlag;
            $scope.getPagedDataAsyncForAllAMCDetails($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",activeFlag); 
            $scope.modalInstance = $modal.open({
              templateUrl: 'demoModalContent.html',
              scope: $scope
            });
          };
        
        $scope.getPagedDataAsyncForAllAMCDetails = function (pageSize,
      	      page, searchText, activeFlag) {
      	      var url;
      	      url = '/RLMS/dashboard/getAllAMCDetailsCount';
      	      setTimeout(
      	        function () {
      	          var data;
      	          if (searchText) {
      	            var ft = searchText
      	              .toLowerCase();
      	            var dataToSend = $scope
      	              .constructDataToSendForAllAMCDetails();
      	            serviceApi
      	              .doPostWithData(url, dataToSend)
      	              .then(
      	              function (largeLoad) {
      	                $scope.complaints = largeLoad;
      	                $scope.showTable = true;
      	                var userDetails = [];
      	                
      	                if (activeFlag=="Active") {
      	                	largeLoad = largeLoad.filter(function (item) {
        	                    return item.activeFlag === 1 && ((new Date(item.amcEdDate)).getTime() >= $scope.todaysDate.getTime());
        	                  });
        	                }
      	                if (activeFlag=="InActive") {
      	                	largeLoad = largeLoad.filter(function (item) {
        	                    return item.activeFlag === 0;
        	                  });
        	                }
      	                if(activeFlag==="Expire"){
      	                	largeLoad = largeLoad.filter(function (item) {
      	  	                    return (new Date(item.amcEdDate)).getTime() < $scope.todaysDate.getTime();
      	  	                  });
      	                }
      	                if(activeFlag==="RenewalForThisMonth"){
      	                	largeLoad = largeLoad.filter(function (item) {
      	                		return item.activeFlag === 1 && ((new Date(item.amcEdDate)).getTime() < $scope.todaysDate.getTime());
      	  	                  });
      	                }
      	                for (var i = 0; i < largeLoad.length; i++) {
      	                  var userDetailsObj = {};
      	                  if (!!largeLoad[i].liftNumber) {
        	                    userDetailsObj["No"] = i+1 +".";
        	                  } else {
        	                    userDetailsObj["No"] = " - ";
        	                  }
      	                      if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
	        	                  if (!!largeLoad[i].branchName) {
	        	                    userDetailsObj["Branch"] = largeLoad[i].branchName;
	        	                  } else {
	        	                    userDetailsObj["Branch"] = " - ";
	        	                  }
      	                      }    
        	                  if (!!largeLoad[i].customerName) {
        	                    userDetailsObj["Customer"] = largeLoad[i].customerName;
        	                  } else {
        	                    userDetailsObj["Customer"] = " - ";
        	                  }
        	                  if (!!largeLoad[i].city) {
            	                userDetailsObj["City"] = largeLoad[i].city;
            	              } else {
            	                userDetailsObj["City"] = " - ";
            	              }
        	                  if (!!largeLoad[i].totalAMCCount) {
        	                	userDetailsObj["TotalAMC"] = largeLoad[i].totalAMCCount;
        	                  } else {
        	                	userDetailsObj["TotalAMC"] = " - ";
        	                  }

      	                  userDetails
      	                    .push(userDetailsObj);
      	                }
      	                
      	                data = userDetails
      	                  .filter(function (
      	                    item) {
      	                    return JSON
      	                      .stringify(
      	                      item)
      	                      .toLowerCase()
      	                      .indexOf(
      	                      ft) !== -1;
      	                  });
      	                $scope
      	                  .setPagingDataForComplaints(
      	                  data,
      	                  page,
      	                  pageSize);
      	              });
      	          } else {
      	            var dataToSend = $scope
      	              .constructDataToSendForAllAMCDetails();
      	            serviceApi
      	              .doPostWithData(url,
      	              dataToSend)
      	              .then(
      	              function (
      	                largeLoad) {
      	                $scope.complaints = largeLoad;
      	                $scope.showTable = true;
      	                var userDetails = [];
      	                var data = [];
      	                var dataCount={};
      	                if (activeFlag=="Active") {
      	                	/*largeLoad = largeLoad.filter(function (item) {
        	                    return item.activeFlag === 1 && ((new Date(item.amcEdDate)).getTime() >= $scope.todaysDate.getTime());
        	                  });*/
      	                	
      	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].underAMCCount!=null || largeLoad[i].renewalDueCount!=null ){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalAMCCount=largeLoad[i].underAMCCount +largeLoad[i].renewalDueCount

    	                			data.push(dataCount);
    	                		}    	                		
    	                	}      
      	                	
      	                }
      	                if (activeFlag=="InActive") {
      	                	largeLoad = largeLoad.filter(function (item) {
        	                    return item.activeFlag === 0;
        	                  });
        	                }
      	                if(activeFlag==="Expire"){
      	                	
      	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].amcPendingCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalAMCCount=largeLoad[i].amcPendingCount
    	                			
    	                			data.push(dataCount);
    	                		}    	                		
    	                	}      	                	
      	                }      	                    	              
      	                if(activeFlag==="RenewalForThisMonth"){
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                		return item.activeFlag === 1 && ((new Date(item.amcEdDate)).getTime() <= $scope.todaysDate.getTime());
    	  	                  });*/      	                	
      	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].renewalDueCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalAMCCount=largeLoad[i].renewalDueCount
    	                			
    	                			data.push(dataCount);
    	                		}
    	                		
    	                	}
      	                
    	                }
      	                for (var i = 0; i < data.length; i++) {
        	                  var userDetailsObj = {};
        	                    userDetailsObj["No"] = i+1 +".";
        	                  if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){ 
	        	                  if (!!data[i].branchName) {
	        	                    userDetailsObj["Branch"] = data[i].branchName;
	        	                  } else {
	        	                    userDetailsObj["Branch"] = " - ";
	        	                  }
        	                  }
        	                  if (!!data[i].customerName) {
        	                    userDetailsObj["Customer"] = data[i].customerName;
        	                  } else {
        	                    userDetailsObj["Customer"] = " - ";
        	                  }
        	                  if (!!data[i].city) {
        	                    userDetailsObj["City"] = data[i].city;
        	                  } else {
        	                    userDetailsObj["City"] = " - ";
        	                  }
        	                  if (!!data[i].totalAMCCount) {
        	                	userDetailsObj["TotalAMC"] = data[i].totalAMCCount;
        	                  } else {
        	                	userDetailsObj["TotalAMC"] = " - ";
        	                  }       	                 
        	                  userDetails
        	                    .push(userDetailsObj);
        	                }
      	                $scope
      	                  .setPagingDataForComplaints(
      	                  userDetails,
      	                  page,
      	                  pageSize);
      	              });

      	          }
      	        }, 100);
      	    }; 

      	    
        $scope.getActiveAMCCount = function (amcStatus) {
  	        
  	        setTimeout(
  	          function () {
  	            var dataToSend = $scope
  	              .constructDataToSendForAllAMCDetails();
  	            serviceApi
  	              .doPostWithData(
  	              '/RLMS/dashboard/getAllAMCDetailsCount',
  	              dataToSend)
  	              .then(
  	              function (
  	                largeLoad) {
  	                if (amcStatus=="Active") {

  	                	var totalCount= 0;
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].underAMCCount!=null||largeLoad[i].renewalDueCount!=null){
	                			totalCount=totalCount+(largeLoad[i].underAMCCount +largeLoad[i].renewalDueCount);
	                			  $scope.amcDetailsData.activeAmc.text=totalCount;
	                		}else{
	                   			$scope.amcDetailsData.activeAmc.text == "0";
	                 		  }
	                	}
   	                }
  	                if(amcStatus=="InActive"){
  	                	$scope.inactiveAMCDetails = largeLoad.filter(function (item) {
    	                    return item.activeFlag === 0;
    	                  });
    	                  $scope.amcDetailsData.inactiveAmc.text=$scope.inactiveAMCDetails.length;
  	                }
  	                if(amcStatus=="Expire"){

  	                	var totalCount= 0;
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].amcPendingCount!=null){
	                			totalCount=totalCount+largeLoad[i].amcPendingCount;
	                			$scope.amcDetailsData.expiredAmc.text=totalCount;
	                		}else{
	                   			$scope.amcDetailsData.expiredAmc.text == "0";
	                 		  }
	                	}
    	                  
  	                }
  	                if(amcStatus=="RenewalForThisMonth"){
  	                	/*$scope.renewalForThisMonthAMC = largeLoad.filter(function (item) {
  	                		return item.activeFlag === 1 && ((new Date(item.amcEdDate)).getTime() <= $scope.todaysDate.getTime());
    	                  });*/
  	                	var totalCount= 0;
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		
	                		if(largeLoad[i].renewalDueCount!=null){
	                			totalCount=totalCount+largeLoad[i].renewalDueCount;
	                			 $scope.amcDetailsData.totalRenewalForThisMonth.text=totalCount;
	                		}else{
	                   			$scope.amcDetailsData.totalRenewalForThisMonth.text == "0";
	                 		  }
	                	}
    	                 
  	                }
  	              });
  	          }, 100);
  	      };
        
        $scope.getActiveAMCCount("Active");
        $scope.getActiveAMCCount("InActive");
        $scope.getActiveAMCCount("Expire");
        $scope.getActiveAMCCount("RenewalForThisMonth");
        //AMC End
      
        //Lift Count 
      $scope.openDemoModalForAllLiftStatusDetails = function (currentModelOpen, headerValue, activeFlag, headingValue) {
          var emptyComplaintsArray = [];
          $scope.myComplaintsData = emptyComplaintsArray;
          $scope.pagingOptionsForComplaints.currentPage = 1;
          $scope.totalServerItemsForComplaints = 0;
          $scope.currentModel = currentModelOpen;
          $scope.filterOptionsForModal.filterText='';
          $scope.modalHeaderVal = headerValue;
          $scope.modalHeading = headingValue;
          $scope.activeFlagForLiftStatus=activeFlag;
          $scope.getPagedDataAsyncForAllLiftStatus($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",activeFlag); 
          $scope.modalInstance = $modal.open({
            templateUrl: 'demoModalContent.html',
            scope: $scope
          });
        };
      
      $scope.getPagedDataAsyncForAllLiftStatus = function (pageSize,
    	      page, searchText, activeFlag) {
    	      var url;
    	      url = '/RLMS/dashboard/getAllAMCDetailsCount';
    	      setTimeout(
    	        function () {
    	          var data;
    	          if (searchText) {
    	            var ft = searchText
    	              .toLowerCase();
    	            var dataToSend = $scope
    	              .constructDataToSendForAllLiftStatus();
    	            serviceApi
    	              .doPostWithData(url, dataToSend)
    	              .then(
    	              function (largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                if (activeFlag=="Active") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });
      	                }
    	                if (activeFlag=="InActive") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });
      	                }
    	                for (var i = 0; i < largeLoad.length; i++) {
    	                	var userDetailsObj = {};
        	                if (!!largeLoad[i].liftId) {
          	                    userDetailsObj["No"] = i+1 + ".";
          	                  } else {
          	                    userDetailsObj["No"] = " - ";
          	                  }
        	                if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
          	                  if (!!largeLoad[i].branchName) {
          	                    userDetailsObj["Branch"] = largeLoad[i].branchName;
        	                  }else {
          	                    userDetailsObj["Branch"] = " - ";
          	                  }
        	                }
          	                  if (!!largeLoad[i].customerName) {
            	                userDetailsObj["Customer"] = largeLoad[i].customerName;
            	              } else {
            	                userDetailsObj["Customer"] = " - ";
            	              }
      	  	        		  if(!!largeLoad[i].city){
      	  	        			userDetailsObj["City"] =largeLoad[i].city;
      	  	        		  }else{
      	  	        			userDetailsObj["City"] =" - ";
      	  	        		  }
          	                  if (!!largeLoad[i].totalFigure) {
            	                 userDetailsObj["TotalLifts"] = largeLoad[i].totalFigure;
            	              } else {
            	                 userDetailsObj["TotalLifts"] = " - ";
            	              }
          	                  userDetails
          	                    .push(userDetailsObj);
          	                }
    	                
    	                data = userDetails
    	                  .filter(function (
    	                    item) {
    	                    return JSON
    	                      .stringify(
    	                      item)
    	                      .toLowerCase()
    	                      .indexOf(
    	                      ft) !== -1;
    	                  });
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  data,
    	                  page,
    	                  pageSize);
    	              });
    	          } else {
    	            var dataToSend = $scope
    	              .constructDataToSendForAllLiftStatus();
    	            serviceApi
    	              .doPostWithData(url,
    	              dataToSend)
    	              .then(
    	              function (
    	                largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                var data=[];
    	                if (activeFlag=="Active") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].underWarrantyCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalLiftCount=largeLoad[i].underWarrantyCount
    	                			
    	                			data.push(dataCount);
    	                		}
    	                		
    	                	}
      	                }
    	                if (activeFlag=="InActive") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].notUnderWarranty!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalLiftCount=largeLoad[i].notUnderWarranty 
    	                			
    	                			data.push(dataCount);
    	                		}
    	                		
    	                	}
      	                }if(activeFlag=="Total"){
      	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].totalLiftCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalLiftCount=largeLoad[i].totalLiftCount
    	                			
    	                			data.push(dataCount);
    	                		}
    	                		
    	                	}
      	                	
      	                }
    	                for (var i = 0; i < data.length; i++) {
    	                	var userDetailsObj = {};
    	                
      	                    userDetailsObj["No"] =i+1 + ".";
      	                  if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
	      	                  if (!!data[i].branchName) {
	      	                    userDetailsObj["Branch"] = data[i].branchName;
	      	                  } else {
	      	                    userDetailsObj["Branch"] = " - ";
	      	                  }
      	                  }
      	                  if (!!data[i].customerName) {
        	                userDetailsObj["Customer"] = data[i].customerName;
        	              } else {
        	                userDetailsObj["Customer"] = " - ";
        	              }
  	  	        		  if(!!data[i].city){
  	  	        			userDetailsObj["City"] =data[i].city;
  	  	        		  }else{
  	  	        			userDetailsObj["City"] =" - ";
  	  	        		  }
      	                  if (!!data[i].totalLiftCount) {
        	                 userDetailsObj["TotalLifts"] = data[i].totalLiftCount;
        	              } else {
        	                 userDetailsObj["TotalLifts"] = " - ";
        	              }
      	                  userDetails
      	                    .push(userDetailsObj);
      	                };
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  userDetails,
    	                  page,
    	                  pageSize);
    	              });

    	          }
    	        }, 100);
    	    }; 
      
      $scope.constructDataToSendForAllLiftStatus=function() {
    	  if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 3){
      		var tempbranchCompanyMapId=0;
          	tempbranchCompanyMapId = $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
          }else{
          	var tempcompanyId=0;
          	tempcompanyId=$rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId
          }
          var data = {
            companyId:tempcompanyId, //$rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId
            branchCompanyMapId:tempbranchCompanyMapId
          };
          return data;
        };
      
      $scope.getLiftStatusDetailsCount = function (liftStatus) {
	        
	        setTimeout(
	          function () {
	            var dataToSend = $scope
	              .constructDataToSendForAllLiftStatus();
	            serviceApi
	              .doPostWithData(
	              '/RLMS/dashboard/getAllAMCDetailsCount',
	              dataToSend)
	              .then(
	              function (
	                largeLoad) {
	                if (liftStatus=="Active") {
	                  /*$scope.activeLiftStatus = largeLoad.filter(function (item) {
	                    return item.activeFlag === 1;
	                  });*/
	                	var totalCount=0;
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].underWarrantyCount!=null){
	                			totalCount=totalCount+largeLoad[i].underWarrantyCount;
	                		}	                		
	                	}
	                	
	                	$scope.liftStatus.activeLiftStatus.text=totalCount;
	                }
	                if(liftStatus=="InActive"){
	                	/*$scope.inactiveLiftStatus = largeLoad.filter(function (item) {
	                    return item.activeFlag === 0;
	                  });*/
	                	var totalCount=0;
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].notUnderWarranty!=null ){
	                			totalCount=totalCount+largeLoad[i].notUnderWarranty;	                		
	                		}
	                	}
	                	$scope.liftStatus.inactiveLiftStatus.text=totalCount;
	                }
	                if(liftStatus=="Total"){
	                	var totalCount=0;
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].totalLiftCount!=null){
	                			totalCount=totalCount+largeLoad[i].totalLiftCount;
	                		}
	                	}
	                	$scope.liftStatus.totalInstalled.text=totalCount;
	                }
	              });
	          }, 100);
	      };
    
      $scope.getLiftStatusDetailsCount("Active");
      $scope.getLiftStatusDetailsCount("InActive");
      $scope.getLiftStatusDetailsCount("Total");
      
      $scope.openDemoModalForAllCustomers = function (currentModelOpen, headerValue, activeFlag, headingValue) {
          var emptyComplaintsArray = [];
          $scope.myComplaintsData = emptyComplaintsArray;
          $scope.pagingOptionsForComplaints.currentPage = 1;
          $scope.totalServerItemsForComplaints = 0;
          $scope.currentModel = currentModelOpen;
          $scope.filterOptionsForModal.filterText='';
          $scope.modalHeaderVal = headerValue;
          $scope.modalHeading = headingValue;
          $scope.activeFlagForCustomers=activeFlag;
          $scope.getPagedDataAsyncForAllCustomers($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",activeFlag); 
          $scope.modalInstance = $modal.open({
            templateUrl: 'demoModalContent.html',
            scope: $scope
          });
        };
      
      $scope.getPagedDataAsyncForAllCustomers = function (pageSize,
    	      page, searchText, activeFlag) {
    	      var url;
    	      url = '/RLMS/dashboard/getCustomerCountForDashboard';
    	      setTimeout(
    	        function () {
    	          var data;
    	          if (searchText) {
    	            var ft = searchText
    	              .toLowerCase();
    	            var dataToSend = $scope
    	              .constructDataToSendForAllLiftStatus();
    	            serviceApi
    	              .doPostWithData(url, dataToSend)
    	              .then(
    	              function (largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                if (activeFlag=="Active") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });
      	                }
    	                if (activeFlag=="InActive") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });
      	                }
    	                for (var i = 0; i < largeLoad.length; i++) {
    	                  var userDetailsObj = {};
    	                  if (!!largeLoad[i].customerId) {
      	                    userDetailsObj["No"] = i+1;
      	                  } else {
      	                    userDetailsObj["No"] = " - ";
      	                  }
    	                  if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
	      	                  if (!!largeLoad[i].branchName) {
	      	                    userDetailsObj["Branch"] = largeLoad[i].branchName;
	      	                  } else {
	      	                    userDetailsObj["Branch"] = " - ";
	      	                  }
    	                  }
      	                  if (!!largeLoad[i].city) {
          	                userDetailsObj["City"] = largeLoad[i].city;
          	              } else {
          	                userDetailsObj["City"] = " - ";
          	              }
      	                  if (!!largeLoad[i].totalFigure) {
            	            userDetailsObj["TotalCustomer"] = largeLoad[i].totalFigure;
            	            } else {
            	            userDetailsObj["TotalCustomer"] = " - ";
            	          }
    	                  userDetails
    	                    .push(userDetailsObj);
    	                }
    	                   data = userDetails
    	                  .filter(function (
    	                    item) {
    	                    return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
    	                  });
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  data,
    	                  page,
    	                  pageSize);
    	              });
    	          } else {
    	            var dataToSend = $scope
    	              .constructDataToSendForAllLiftStatus();
    	            serviceApi
    	              .doPostWithData(url,
    	              dataToSend)
    	              .then(
    	              function (
    	                largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                var data=[];
    	                if (activeFlag=="Active") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].activeFlagCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalFigure=largeLoad[i].activeFlagCount

    	                			data.push(dataCount);
    	                		}    	                		
    	                	}
      	                }
    	                if (activeFlag=="InActive") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].inactiveFlagCount!="0"){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalFigure=largeLoad[i].inactiveFlagCount

    	                			data.push(dataCount);
    	                		}    	                		
    	                	}      
      	                }
    	                if (activeFlag=="Total") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].customerCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchName=largeLoad[i].branchName
    	                			dataCount.customerName=largeLoad[i].customerName
    	                			dataCount.city=largeLoad[i].city
    	                			dataCount.totalFigure=largeLoad[i].customerCount

    	                			data.push(dataCount);
    	                		}    	                		
    	                	}      
      	                }
    	                for (var i = 0; i < data.length; i++) {
    	                	var userDetailsObj = {};
    	                	
          	                    userDetailsObj["No"] =i+1 +".";
          	                  
          	                  if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
    	      	                  if (!!largeLoad[i].branchName) {
    	      	                    userDetailsObj["Branch"] = largeLoad[i].branchName;
    	      	                  } else {
    	      	                    userDetailsObj["Branch"] = " - ";
    	      	                  }
        	                  }
          	                  if (!!data[i].city) {
              	                userDetailsObj["City"] = data[i].city;
              	              } else {
              	                userDetailsObj["City"] = " - ";
              	              }
          	                  if (!!data[i].totalFigure) {
                	            userDetailsObj["Total_Customers"] = data[i].totalFigure;
                	            } else {
                	            userDetailsObj["Total_Customers"] = " - ";
                	          }
          	                  
      	                  userDetails
      	                    .push(userDetailsObj);
      	                };
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  userDetails,
    	                  page,
    	                  pageSize);
    	              });

    	          }
    	        }, 100);
    	    }; 
      
      $scope.getCustomerDetailsCount = function (liftStatus) {
	        
	        setTimeout(
	          function () {
	            var dataToSend = $scope
	              .constructDataToSendForAllLiftStatus();
	            serviceApi
	              .doPostWithData(
	              '/RLMS/dashboard/getCustomerCountForDashboard',
	              dataToSend)
	              .then(
	              function (
	                largeLoad) {
	                if (liftStatus=="Active") {
	                 /* $scope.activeCustomers = largeLoad.filter(function (item) {
	                    return item.activeFlag === 1;
	                  });*/
	                	var totalCount=0;
	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].activeFlagCount!=null){
	                			totalCount=totalCount+largeLoad[i].activeFlagCount;
	                		}
	                	}
	                  $scope.customersDetails.activeCustomers.text=totalCount;
	                }
	                if(liftStatus=="InActive"){
	                	/*$scope.inactiveCustomers = largeLoad.filter(function (item) {
	                    return item.activeFlag === 0;
	                  });*/
	                	var totalCount=0;
	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].inactiveFlagCount!=null){
	                			totalCount=totalCount+largeLoad[i].inactiveFlagCount;
	                		}
	                	}
	                	
	                  $scope.customersDetails.inactiveCustomers.text=totalCount;
	                }
	                if(liftStatus=="Total"){
	                	var totalCount=0;
	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].customerCount!=null){
	                			totalCount=totalCount+largeLoad[i].customerCount;
	                		}
	                	}
	                  $scope.customersDetails.totalCustomers.text=totalCount;
	                }
	              });
	          }, 100);
	      };
      
      $scope.getCustomerDetailsCount("Active");
      $scope.getCustomerDetailsCount("InActive");
      $scope.getCustomerDetailsCount("Total");
      
      $scope.openDemoModalForAllBranches = function (currentModelOpen, headerValue, activeFlag, headingValue) {
          var emptyComplaintsArray = [];
          $scope.myComplaintsData = emptyComplaintsArray;
          $scope.pagingOptionsForComplaints.currentPage = 1;
          $scope.totalServerItemsForComplaints = 0;
          $scope.currentModel = currentModelOpen;
          $scope.filterOptionsForModal.filterText='';
          $scope.modalHeaderVal = headerValue;
          $scope.modalHeading = headingValue;
          $scope.activeFlagForBranches=activeFlag;
          $scope.getPagedDataAsyncForAllBranches($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",activeFlag); 
          $scope.modalInstance = $modal.open({
            templateUrl: 'demoModalContent.html',
            scope: $scope
          });
        };
      
      $scope.getPagedDataAsyncForAllBranches = function (pageSize,
    	      page, searchText, activeFlag) {
    	      var url;
    	      url = '/RLMS/dashboard/getListOfBranchCountDtlsForDashboard';
    	      setTimeout(
    	        function () {
    	          var data;
    	          if (searchText) {
    	            var ft = searchText
    	              .toLowerCase();
    	            var dataToSend = $scope
    	              .constructDataToSendForAllLiftStatus();
    	            serviceApi
    	              .doPostWithData(url, dataToSend)
    	              .then(
    	              function (largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                if (activeFlag=="Active") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });
      	                }
    	                if (activeFlag=="InActive") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });
      	                }
    	                for (var i = 0; i < largeLoad.length; i++) {
    	                  var userDetailsObj = {};
    	                  if (!!largeLoad[i].id) {
      	                    userDetailsObj["No"] =i+1;
      	                  } else {
      	                    userDetailsObj["No"] = " - ";
      	                  }
      	                  if (!!largeLoad[i].city) {
          	                userDetailsObj["City"] = largeLoad[i].city;
          	              } else {
          	                userDetailsObj["City"] = " - ";
          	              }
      	                  if (!!largeLoad[i].totalBranches) {
          	                userDetailsObj["TotalFigure"] = largeLoad[i].totalBranches;
          	              } else {
          	                userDetailsObj["TotalFigure"] = " - ";
          	              }
    	                  userDetails
    	                    .push(userDetailsObj);
    	                }
    	                
    	                data = userDetails
    	                  .filter(function (
    	                    item) {
    	                    return JSON
    	                      .stringify(
    	                      item)
    	                      .toLowerCase()
    	                      .indexOf(
    	                      ft) !== -1;
    	                  });
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  data,
    	                  page,
    	                  pageSize);
    	              });
    	          } else {
    	            var dataToSend = $scope
    	              .constructDataToSendForAllLiftStatus();
    	            serviceApi
    	              .doPostWithData(url,
    	              dataToSend)
    	              .then(
    	              function (
    	                largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                var data = [];
    	                if (activeFlag=="Active") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].branchActiveFlagCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchCity=largeLoad[i].branchCity
    	                			dataCount.branchCount=largeLoad[i].branchActiveFlagCount

    	                			data.push(dataCount);
    	                		}    	                		
    	                	}
      	                }
    	                if (activeFlag=="InActive") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].branchInactiveFlagCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchCity=largeLoad[i].branchCity
    	                			dataCount.branchCount=largeLoad[i].branchInactiveFlagCount

    	                			data.push(dataCount);
    	                		}    	                		
    	                	}
      	                }
    	                if (activeFlag=="Total") {
    	                	/*largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });*/
    	                	for (var i = 0; i < largeLoad.length; i++) {
    	                		
    	                		if(largeLoad[i].branchActiveFlagCount!=null ||largeLoad[i].branchInactiveFlagCount!=null){
    	                			var dataCount={};
    	                			dataCount.branchCity=largeLoad[i].branchCity
    	                			dataCount.branchCount=largeLoad[i].branchInactiveFlagCount +largeLoad[i].branchActiveFlagCount 

    	                			data.push(dataCount);
    	                		}
    	                		else if(largeLoad[i].branchActiveFlagCount!=null ){
    	                			var dataCount={};
    	                			dataCount.branchCity=largeLoad[i].branchCity
    	                			dataCount.branchCount=largeLoad[i].branchActiveFlagCount

    	                			data.push(dataCount);
    	                		}
    	                		else if(largeLoad[i].branchInactiveFlagCount!=null ){
    	                			var dataCount={};
    	                			dataCount.branchCity=largeLoad[i].branchCity
    	                			dataCount.branchCount=largeLoad[i].branchInactiveFlagCount

    	                			data.push(dataCount);
    	                		}
    	                	}
      	                }
    	                for (var i = 0; i < data.length; i++) {
    	                	var userDetailsObj = {};
          	                     userDetailsObj["No"] = i+1 +".";

          	                  if (!!data[i].branchCity) {
              	                userDetailsObj["City"] = data[i].branchCity;
              	              } else {
              	                userDetailsObj["City"] = " - ";
              	              }
          	                  if (!!data[i].branchCount) {
            	                userDetailsObj["TotalBranches"] = data[i].branchCount;
            	              } else {
            	                userDetailsObj["TotalBranches"] = " - ";
            	              }
      	                  userDetails
      	                    .push(userDetailsObj);
      	                };
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  userDetails,
    	                  page,
    	                  pageSize);
    	              });

    	          }
    	        }, 100);
    	    }; 
      
      $scope.getBranchesCount = function (liftStatus) {
	        setTimeout(
	          function () {
	            var dataToSend = $scope
	              .constructDataToSendForAllLiftStatus();
	            serviceApi
	              .doPostWithData(
	              '/RLMS/dashboard/getListOfBranchCountDtlsForDashboard',
	              dataToSend)
	              .then(
	              function (
	                largeLoad) {
	                if (liftStatus=="Active") {
	                  /*$scope.activeBranches = largeLoad.filter(function (item) {
	                    return item.activeFlag === 1;
	                  });*/
	                	var totalCount=0;
	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].branchActiveFlagCount!=null){
	                			totalCount=totalCount+largeLoad[i].branchActiveFlagCount;
	                		}
	                	}
	                  $scope.branchDetails.activeBranches.text=totalCount;
	                }
	                if(liftStatus=="InActive"){
	                	/*$scope.inactiveBranches = largeLoad.filter(function (item) {
	                    return item.activeFlag === 0;
	                  });*/
	                	var totalCount=0;
	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].branchInactiveFlagCount!=null){
	                			totalCount=totalCount+largeLoad[i].branchInactiveFlagCount;
	                		}
	                	}
	                  $scope.branchDetails.inactiveBranches.text=totalCount;
	                }
	                if(liftStatus=="Total"){
	                	var totalCount=0;
	                	
	                	for (var i = 0; i < largeLoad.length; i++) {
	                		if(largeLoad[i].branchInactiveFlagCount!=null || largeLoad[i].branchActiveFlagCount!=null){
	                			totalCount=totalCount+(largeLoad[i].branchInactiveFlagCount +largeLoad[i].branchActiveFlagCount) ;
	                		}
	                		else if(largeLoad[i].branchInactiveFlagCount!=null){
	                			totalCount=totalCount+(largeLoad[i].branchInactiveFlagCount) ;
	                		}
	                		else if(largeLoad[i].branchActiveFlagCount!=null){
	                			totalCount=totalCount+(largeLoad[i].branchActiveFlagCount) ;
	                		}

	                	}
	                	$scope.branchDetails.totalBranches.text=totalCount;
	                }
	              });
	          }, 100);
	      };
      
      $scope.getBranchesCount("Active");
      $scope.getBranchesCount("InActive");
      $scope.getBranchesCount("Total");
      
      $scope.openDemoModalForAllCompanies = function (currentModelOpen, headerValue, activeFlag, headingValue) {
          var emptyComplaintsArray = [];
          $scope.myComplaintsData = emptyComplaintsArray;
          $scope.pagingOptionsForComplaints.currentPage = 1;
          $scope.totalServerItemsForComplaints = 0;
          $scope.currentModel = currentModelOpen;
          $scope.filterOptionsForModal.filterText='';
          $scope.modalHeaderVal = headerValue;
          $scope.modalHeading = headingValue;
          $scope.activeFlagForCompanies=activeFlag;
          $scope.getPagedDataAsyncForAllCompanies($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",activeFlag); 
          $scope.modalInstance = $modal.open({
            templateUrl: 'demoModalContent.html',
            scope: $scope
          });
        };
      
      $scope.getPagedDataAsyncForAllCompanies = function (pageSize,
    	      page, searchText, activeFlag) {
    	      var url;
    	      url = '/RLMS/dashboard/getListOfBranchCountDtlsForDashboard';
    	      setTimeout(
    	        function () {
    	          var data;
    	          if (searchText) {
    	            var ft = searchText
    	              .toLowerCase();
    	            serviceApi
    	              .doPostWithData(url)
    	              .then(
    	              function (largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                if (activeFlag=="Active") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });
      	                }
    	                if (activeFlag=="InActive") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });
      	                }
    	                for (var i = 0; i < largeLoad.length; i++) {
    	                  var userDetailsObj = {};
    	                  if (!!largeLoad[i].companyId) {
      	                    userDetailsObj["No"] = largeLoad[i].companyId;
      	                  } else {
      	                    userDetailsObj["No"] = " - ";
      	                  }
      	                  if (!!largeLoad[i].companyName) {
      	                    userDetailsObj["CompanyName"] = largeLoad[i].companyName;
      	                  } else {
      	                    userDetailsObj["CompanyName"] = " - ";
      	                  }
      	                  if (!!largeLoad[i].city) {
          	                userDetailsObj["City"] = largeLoad[i].city;
          	              } else {
          	                userDetailsObj["City"] = " - ";
          	              }
      	                  if (!!largeLoad[i].numberOfBranches) {
            	                userDetailsObj["TotalBranches"] = largeLoad[i].numberOfBranches;
            	              } else {
            	                userDetailsObj["TotalBranches"] = " - ";
            	              }

    	                  userDetails
    	                    .push(userDetailsObj);
    	                }
    	                
    	                data = userDetails
    	                  .filter(function (
    	                    item) {
    	                    return JSON
    	                      .stringify(
    	                      item)
    	                      .toLowerCase()
    	                      .indexOf(
    	                      ft) !== -1;
    	                  });
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  data,
    	                  page,
    	                  pageSize);
    	              });
    	          } else {
    	            serviceApi
    	              .doPostWithData(url)
    	              .then(
    	              function (
    	                largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                if (activeFlag=="Active") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });
      	                }
    	                if (activeFlag=="InActive") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });
      	                }
    	                for (var i = 0; i < largeLoad.length; i++) {
    	                	var userDetailsObj = {};
    	                	if (!!largeLoad[i].companyId) {
          	                    userDetailsObj["No"] = largeLoad[i].companyId;
          	                  } else {
          	                    userDetailsObj["No"] = " - ";
          	                  }
          	                  if (!!largeLoad[i].companyName) {
          	                    userDetailsObj["CompanyName"] = largeLoad[i].companyName;
          	                  } else {
          	                    userDetailsObj["CompanyName"] = " - ";
          	                  }
          	                  if (!!largeLoad[i].city) {
              	                userDetailsObj["City"] = largeLoad[i].city;
              	              } else {
              	                userDetailsObj["City"] = " - ";
              	              }
          	                  if (!!largeLoad[i].numberOfBranches) {
                	                userDetailsObj["TotalBranches"] = largeLoad[i].numberOfBranches;
                	              } else {
                	                userDetailsObj["TotalBranches"] = " - ";
                	              }
      	                  userDetails
      	                    .push(userDetailsObj);
      	                };
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  userDetails,
    	                  page,
    	                  pageSize);
    	              });

    	          }
    	        }, 100);
    	    }; 
      
      $scope.getCompaniesCount = function (companyStatus) {
	        
	        setTimeout(
	          function () {
	            serviceApi
	              .doPostWithData(
	              '/RLMS/dashboard/getAllCompanyDetailsForDashboard')
	              .then(
	              function (
	                largeLoad) {
	                if (companyStatus=="Active") {
	                  $scope.activeCompanies = largeLoad.filter(function (item) {
	                    return item.activeFlag === 1;
	                  });
	                  $scope.companiesData.activeCompanies.text=$scope.activeCompanies.length;
	                }
	                if(companyStatus=="InActive"){
	                	$scope.inactiveCompanies = largeLoad.filter(function (item) {
	                    return item.activeFlag === 0;
	                  });
	                  $scope.companiesData.inactiveCompanies.text=$scope.inactiveCompanies.length;
	                }
	                if(companyStatus=="Total"){
	                  $scope.companiesData.totalCompanies.text=largeLoad.length;
	                }
	              });
	          }, 100);
	      };
      $scope.getCompaniesCount("Active");
      $scope.getCompaniesCount("InActive");
      $scope.getCompaniesCount("Total");
      
      $scope.getCountForEvent = function (eventType) {
	        setTimeout(
	          function () {
	        	  var dataToSend = $scope
	              .constructDataToSendForAllLiftStatus();
	            serviceApi
	              .doPostWithData('/RLMS/dashboard/getEventCountForLift',
	              dataToSend)
	              .then(
	              function (
	                largeLoad) {
	            	  if (eventType=="Event") {
		                  /*$scope.inout = largeLoad.filter(function (item) {
		                    //return item.eventType === "EVENT";
		                    return item.eventType==="EVENT";
		                  });*/
	            		  var totalCount= 0;
	  	                	for (var i = 0; i < largeLoad.length; i++) {
	  	                		
	  	                		if(largeLoad[i].totolEventCount!=null){
	  	                			totalCount=totalCount+largeLoad[i].totolEventCount;
	  	                			
	  	                		}else{
	  	                			$scope.event.inout.text="0";
	  	                		}
	  	                	}
	  	                	 $scope.event.inout.text=totalCount;
		                }
	            	  if (eventType=="Error") {
		                  /*$scope.error = largeLoad.filter(function (item) {
		                    return item.eventType === "ERROR";
		                  });*/
	            		  var totalCount= 0;
	  	                	for (var i = 0; i < largeLoad.length; i++) {
	  	                		if(largeLoad[i].totalErrorCount!=null){
	  	                			totalCount=totalCount+largeLoad[i].totalErrorCount;
	  	                			
	  	                		}else{
	  	                			$scope.event.error.text="0";
	  	                		}
	  	                	}
	  	                	$scope.event.error.text=totalCount;

	            	  }
	            	  if (eventType=="Response") {
	            		  var totalCount= 0;
  	                	for (var i = 0; i < largeLoad.length; i++) {
  	                		if(largeLoad[i].totalResCount!=null){
  	                			totalCount=totalCount+largeLoad[i].totalResCount;
  	                			
  	                		}else{
  	                			$scope.event.responses.text="0";
  	                		}
  	                	}
  	                	$scope.event.responses.text=totalCount;
	            	  }
	                //  $scope.event.inout.text=largeLoad.length;
	              });
	          }, 100);
	        
	        setTimeout(
	  	          function () {
	  	        	  var dataToSend = $scope
	  	              .constructDataToSendForAllLiftStatus();
	  	            serviceApi
	  	              .doPostWithData('/RLMS/dashboard/getTodaysEventCountForLift',
	  	              dataToSend)
	  	              .then(
	  	              function (
	  	                largeLoad) {
	  	            	  if (eventType=="Event") {
	  		                  /*$scope.inout = largeLoad.filter(function (item) {
	  		                    //return item.eventType === "EVENT";
	  		                    return item.eventType==="EVENT";
	  		                  });*/
	  	            		  var totalCount= 0;
	  	  	                	for (var i = 0; i < largeLoad.length; i++) {
	  	  	                		
	  	  	                		if(largeLoad[i].totolEventCount!=null){
	  	  	                			totalCount=totalCount+largeLoad[i].totolEventCount;
	  	  	                		 $scope.event.todaysEvents.text=totalCount;
	  	  	                		}else{
	  	  	                			$scope.event.todaysEvents.text==='0';
	  	  	                		}
	  	  	                	}
	  		                }
	  	            	  if (eventType=="Error") {
	  		                  /*$scope.error = largeLoad.filter(function (item) {
	  		                    return item.eventType === "ERROR";
	  		                  });*/
	  	            		  var totalCount= 0;
	  	  	                	for (var i = 0; i < largeLoad.length; i++) {
	  	  	                		if(largeLoad[i].totalErrorCount!=null){
	  	  	                			totalCount=totalCount+largeLoad[i].totalErrorCount;
	  	  	                		$scope.event.todaysErrors.text=totalCount;
	  	  	                		}else{
	  	  	                			$scope.event.todaysErrors.text==='0';
	  	  	                		}
	  	  	                	}
	  	            	  }
	  	            	  if (eventType=="Response") {
	  	            		  var totalCount= 0;
	    	                	for (var i = 0; i < largeLoad.length; i++) {
	    	                		if(largeLoad[i].totalResCount!=null){
	    	                			totalCount=totalCount+largeLoad[i].totalResCount;
	    	                			$scope.event.todaysResponses.text=totalCount;
	    	                		}else{
	    	                			$scope.event.todaysResponses.text==='0';
	    	                		}
	    	                	}
	  	            	  }
	  	                //  $scope.event.inout.text=largeLoad.length;
	  	              });
	  	          }, 100);
	      };
      //add event api call
     /*$scope.getCountForEvent = function (eventName) {
	       	   $http({method: 'GET',
    	        url: '/RLMS/API/addEvents',
    	        params: {from: "9423720625",message:"RLMS,*12,LMS EVENT,E2,OUT PRESSED,FLOOR No.00,10:52,28/05/18;"}
    	    })   .success(function(data) {
	           // $scope.names = eval(data);
	            console.log(data)
	        })
	        .error(function(data) {
	            alert(data);
	            console.log('Error: ' + data);
	        });
    	   };*/
	      $scope.getCountForEvent("Event");
	      $scope.getCountForEvent("Error");
	      $scope.getCountForEvent("Response");
          $scope.getPagedDataAsyncForEvents = function (pageSize,page, searchText, eventType,headingValue) {
    	  var url;
    	  var dataToSend = $scope.constructDataToSendForAllLiftStatus();
    	  if(headingValue==="Events" || headingValue==="Errors"|| headingValue==="Responses" ){
    	     url = '/RLMS/dashboard/getEventCountForLift'
    	  }else{
    		  url = '/RLMS/dashboard/getTodaysEventCountForLift'
    	  }
    	      setTimeout(
    	        function () {
    	          var data;
    	          if (searchText) {
    	            var ft = searchText
    	              .toLowerCase();
    	              serviceApi
    	              .doPostWithData(url,dataToSend)
    	              .then(
    	              function (largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                
    	                if (eventType=="Event") {
    	                	largeLoad = largeLoad.filter(function (item) {
  		                    return item.eventType === "EVENT";
  		                  });
  		                }
  	            	  	if (eventType=="Error") {
  	            		largeLoad= largeLoad.filter(function (item) {
  		                    return item.eventType === "ERROR";
  		                  });
  	            	  	}
  	            	  	if (eventType=="Response") {
    	            		largeLoad= largeLoad.filter(function (item) {
    		                    return item.eventType === "RESPONSE";
    		                  });
    	            	  	}
    	                for (var i = 0; i < largeLoad.length; i++) {
        	                  var userDetailsObj = {};
        	                  userDetailsObj["No"] = i+1;
      	                    
      	                    if (!!data[i].liftNumber) {
     	                    	   userDetailsObj["LiftNo"] = data[i].liftNumber;
     	                       } else {
     	                    	   userDetailsObj["LiftNo"] = " - ";
     	                       }
      	                    if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
    	      	                  if (!!largeLoad[i].branchName) {
    	      	                    userDetailsObj["Branch"] = largeLoad[i].branchName;
    	      	                  } else {
    	      	                    userDetailsObj["Branch"] = " - ";
    	      	                  }
        	                  }
    	                       if (!!data[i].customerName) {
    	                    	   userDetailsObj["Customer"] = data[i].customerName;
							   } else {
								   userDetailsObj["Customer"] = " - ";
							   }
      	                  if (!!data[i].city) {
      	                	  userDetailsObj["City"] = data[i].city;
          	              } else {
          	            	  userDetailsObj["City"] = " - ";
          	              }
      	                 if (!!data[i].totalFigure) {
      	                	 userDetailsObj["TotalCount"] = data[i].totalFigure;
          	              } else {
          	            	 userDetailsObj["TotalCount"] = " - ";
          	              }
        	                  userDetails
        	                    .push(userDetailsObj);
        	                }
    	                
    	                data = userDetails
    	                  .filter(function (
    	                    item) {
    	                    return JSON
    	                      .stringify(
    	                      item)
    	                      .toLowerCase()
    	                      .indexOf(
    	                      ft) !== -1;
    	                  });
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  data,
    	                  page,
    	                  pageSize);
    	              });
    	          } else {
    	            serviceApi
    	              .doPostWithData(url,dataToSend)
    	              .then(
    	              function (
    	                largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                var data = [];
    	                if (eventType=="Event") {    	                	
	  	                	for (var i = 0; i < largeLoad.length; i++) {
	  	                		
	  	                		if(largeLoad[i].totolEventCount!=null){
	  	                			var dataCount={};
	  	                			dataCount.liftNumber=largeLoad[i].liftNumber
	  	                			dataCount.branchName=largeLoad[i].branchName
	  	                			dataCount.customerName=largeLoad[i].customerName
	  	                			dataCount.city=largeLoad[i].city
	  	                			dataCount.totalFigure=largeLoad[i].totolEventCount
		                			data.push(dataCount);
	  	                		}
	  	                	}
  		                }
  	            	  	if (eventType=="Error") {
  	            	  		for (var i = 0; i < largeLoad.length; i++) {
  	            	  			if(largeLoad[i].totalErrorCount!=null){
  	            	  				var dataCount={};
  	            	  				dataCount.liftNumber=largeLoad[i].liftNumber
  	            	  				dataCount.branchName=largeLoad[i].branchName
  	            	  				dataCount.customerName=largeLoad[i].customerName
  	            	  				dataCount.city=largeLoad[i].city
  	            	  				dataCount.totalFigure=largeLoad[i].totalErrorCount  
		                			data.push(dataCount);
  	            	  			}
  	            	  		}
  	            	  	}
  	            	  if (eventType=="Response") {  	            		  
  	            		  for (var i = 0; i < largeLoad.length; i++) {
  	            			  if(largeLoad[i].totalResCount!=null){
  	            				var dataCount={};
  	            				dataCount.liftNumber=largeLoad[i].liftNumber
  	            				dataCount.branchName=largeLoad[i].branchName
  	            				dataCount.customerName=largeLoad[i].customerName
  	            				dataCount.city=largeLoad[i].city
  	            				dataCount.totalFigure=largeLoad[i].totalResCount
	                			data.push(dataCount);
  	            			  }
  	            		  }
	            	  }
    	                for (var i = 0; i < data.length; i++) {
      	                  var userDetailsObj = {};
      	                  
        	                    userDetailsObj["No"] = i+1;
        	                    
        	                    if (!!data[i].liftNumber) {
       	                    	   userDetailsObj["LiftNo"] = data[i].liftNumber;
       	                       } else {
       	                    	   userDetailsObj["LiftNo"] = " - ";
       	                       }
        	                    if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
      	      	                  if (!!largeLoad[i].branchName) {
      	      	                    userDetailsObj["Branch"] = largeLoad[i].branchName;
      	      	                  } else {
      	      	                    userDetailsObj["Branch"] = " - ";
      	      	                  }
          	                  }
      	                       if (!!data[i].customerName) {
      	                    	   userDetailsObj["Customer"] = data[i].customerName;
							   } else {
								   userDetailsObj["Customer"] = " - ";
							   }
        	                  if (!!data[i].city) {
        	                	  userDetailsObj["City"] = data[i].city;
            	              } else {
            	            	  userDetailsObj["City"] = " - ";
            	              }
        	                 if (!!data[i].totalFigure) {
        	                	 userDetailsObj["TotalCount"] = data[i].totalFigure;
            	              } else {
            	            	 userDetailsObj["TotalCount"] = " - ";
            	              }
      	                  userDetails
      	                    .push(userDetailsObj);
      	                }
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  userDetails,
    	                  page,
    	                  pageSize);
    	              });

    	          }
    	        }, 100);
    	    }; 
      
      $scope.openDemoModalForEvents = function (currentModelOpen, headerValue, activeFlag, headingValue) {
          var emptyComplaintsArray = [];
          $scope.myComplaintsData = emptyComplaintsArray;
          $scope.pagingOptionsForComplaints.currentPage = 1;
          $scope.totalServerItemsForComplaints = 0;
          $scope.filterOptionsForModal.filterText='';
          $scope.currentModel = currentModelOpen;
          $scope.modalHeaderVal = headerValue;
          $scope.modalHeading = headingValue;
          $scope.activeFlagForEvents = activeFlag;
          $scope.getPagedDataAsyncForEvents($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",activeFlag,headerValue); 
          $scope.modalInstance = $modal.open({
            templateUrl: 'demoModalContent.html',
            scope: $scope
          });
        };
        
        /*$scope.getCountAmcSrviceCalls = function (eventName) {
	        setTimeout(
	          function () {
	            serviceApi
	              .doPostWithData(
	              '/RLMS/dashboard/getListOfAmcServiceCalls',$scope.construnctObjeToSendForAmcCalls())
	              .then(
	              function (
	                largeLoad) {
	                  $scope.amcSeriveCalls.text=largeLoad.length;
	              });
	          }, 100);
	      };
	      $scope.construnctObjeToSendForAmcCalls = function () {
	          var dataToSend = {
	            statusList: [],
	            companyId: $rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId
	          };
	          //dataToSend["statusList"] = complaintStatus;
	          return dataToSend;
	        };
        $scope.openDemoModalForAmcServiceCalls = function (currentModelOpen, headerValue, activeFlag, headingValue) {
            var emptyComplaintsArray = [];
            $scope.myComplaintsData = emptyComplaintsArray;
            $scope.pagingOptionsForComplaints.currentPage = 1;
            $scope.totalServerItemsForComplaints = 0;
            $scope.filterOptionsForModal.filterText='';
            $scope.currentModel = currentModelOpen;
            $scope.modalHeaderVal = headerValue;
            $scope.modalHeading = headingValue;
            $scope.activeFlagForTechnician = activeFlag;
            $scope.getPagedDataAsyncForAmcServiceCalls($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",activeFlag); 
            $scope.modalInstance = $modal.open({
              templateUrl: 'demoModalContent.html',
              scope: $scope
            });
          };
            $scope.getCountAmcSrviceCalls("AmcServiceCall");
            $scope.getPagedDataAsyncForAmcServiceCalls = function (pageSize,
          	      page, searchText, activeFlag) {
          	      var url;
          	      var dataToSend = $scope
                      .construnctObjeToSendForAmcCalls();
          	      url = '/RLMS/dashboard/getListOfAmcServiceCalls',
          	      setTimeout(
          	        function () {
          	          var data;
          	          if (searchText) {
          	            var ft = searchText
          	              .toLowerCase();
          	              serviceApi
          	              .doPostWithData(url,dataToSend)
          	              .then(
          	              function (largeLoad) {
          	                $scope.complaints = largeLoad;
          	                $scope.showTable = true;
          	                var userDetails = [];
          	                           
          	                for (var i = 0; i < largeLoad.length; i++) {
              	                  var userDetailsObj = {};
              	                  if (!!largeLoad[i].eventId) {
                	                    userDetailsObj["No"] = i+1;
                	                  } else {
                	                    userDetailsObj["No"] = " - ";
                	                  }
              	                  if (!!largeLoad[i].branchName) {
         	                    	   userDetailsObj["Branch"] = largeLoad[i].branchName;
         	                       } else {
         	                    	   userDetailsObj["Branch"] = " - ";
         	                       }
              	                  if (!!largeLoad[i].customerName) {
           	                    	   userDetailsObj["Customer"] = largeLoad[i].customerName;
           	                       } else {
           	                    	   userDetailsObj["Customer"] = " - ";
           	                       }
                	               if (!!largeLoad[i].city) {
                	                   userDetailsObj["City"] = largeLoad[i].city;
                	               } else {
                	                   userDetailsObj["City"] = " - ";
                	               }
                	               if (!!largeLoad[i].totalCalls) {
                    	               userDetailsObj["Total_ServiceCalls"] = largeLoad[i].totalCalls;
                    	           } else {
                    	               userDetailsObj["Total_ServiceCalls"] = " - ";
                    	           }
              	                  userDetails
              	                    .push(userDetailsObj);
              	                }
          	                
          	                data = userDetails
          	                  .filter(function (
          	                    item) {
          	                    return JSON
          	                      .stringify(
          	                      item)
          	                      .toLowerCase()
          	                      .indexOf(
          	                      ft) !== -1;
          	                  });
          	                $scope
          	                  .setPagingDataForComplaints(
          	                  data,
          	                  page,
          	                  pageSize);
          	              });
          	          } else {
          	            serviceApi
          	              .doPostWithData(url,dataToSend)
          	              .then(
          	              function (
          	                largeLoad) {
          	                $scope.complaints = largeLoad;
          	                $scope.showTable = true;
          	                var userDetails = [];
          	                for (var i = 0; i < largeLoad.length; i++) {
                	                  var userDetailsObj = {};
                	                
                  	                    userDetailsObj["No"] = i+1;
                  	               
                    	                  if (!!largeLoad[i].branchName) {
                	                    	   userDetailsObj["Branch"] = largeLoad[i].branchName;
                	                       } else {
                	                    	   userDetailsObj["Branch"] = " - ";
                	                       }
                     	                  if (!!largeLoad[i].customerName) {
                  	                    	   userDetailsObj["Customer"] = largeLoad[i].customerName;
                  	                       } else {
                  	                    	   userDetailsObj["Customer"] = " - ";
                  	                       }
                       	               if (!!largeLoad[i].city) {
                       	                   userDetailsObj["City"] = largeLoad[i].city;
                       	               } else {
                       	                   userDetailsObj["City"] = " - ";
                       	               }
                       	               if (!!largeLoad[i].totalCalls) {
                           	               userDetailsObj["Total_ServiceCalls"] = largeLoad[i].totalCalls;
                           	           } else {
                           	               userDetailsObj["Total_ServiceCalls"] = " - ";
                           	           }
                       	               userDetails
                	                    .push(userDetailsObj);
                	                }
          	                $scope
          	                  .setPagingDataForComplaints(
          	                  userDetails,
          	                  page,
          	                  pageSize);
          	              });
          	          }
          	        }, 100);
          	    };*/
          	    //For edit Profile
          	
  }]);