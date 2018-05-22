(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('workListCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
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
	  	    $scope.alert = { type: 'success', msg: ' You successfully Added Lift.',close:true };
			$scope.showAlert = false;
	  	    $scope.getPagedDataAsync = function(pageSize, page, searchText) {
	  	    	
	  	      setTimeout(function() {
	  	        var data;
	  	        if (searchText) {
	  	          var ft = searchText.toLowerCase();
	  	        serviceApi.doPostWithoutData('/RLMS/admin/getLiftsToBeApproved')
	  	         .then(function(largeLoad) {
	  	         $scope.response = largeLoad;
	  	        	$scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			userDetailsObj["Lift_Number"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			userDetailsObj["Lift_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].customerName){
	  	        			userDetailsObj["customerName"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftId){
	  	        			userDetailsObj["liftId"] =largeLoad[i].liftId;
	  	        		}else{
	  	        			userDetailsObj["liftId"] =" - ";
	  	        		}
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            data = userDetails.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
	  	        	
	  	        	serviceApi.doPostWithoutData('/RLMS/admin/getLiftsToBeApproved').then(function(largeLoad) {
	  	        	  var userDetails=[];
	  	        	  $scope.response = largeLoad;
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var userDetailsObj={};
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			userDetailsObj["Lift_Number"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			userDetailsObj["Lift_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].customerName){
	  	        			userDetailsObj["Customer_Name"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftId){
	  	        			userDetailsObj["liftId"] =largeLoad[i].liftId;
	  	        		}else{
	  	        			userDetailsObj["liftId"] =" - ";
	  	        		}
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(userDetails, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
	  	    	 $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
	  	    

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
    		  enableHighlighting: true,
    		  enableRowSelection: true,
    		  selectedItems: [],
    		  columnDefs: [{
		          field: 'Lift_Number',
		          displayName: 'Lift Number',
		        }, {
		          field: 'Address',
		          displayName: 'Address'
		        },
		        {
		          field: 'Customer_Name',
		          displayName: 'Customer Name'
		        }, {
		          field: 'Branch_Name',
		          displayName: 'Branch Name'
		        },{
		          field: 'liftId',
		          displayName: 'liftId',
		          visible: false,
		        }]
	  	    };
	  	    //liftId
	  	    $scope.approveLift =function(){
	  	    //var slectedRow = $filter('filter')($scope.response,{liftNumber:$scope.gridOptions.selectedItems[0]}.Lift_Number,companyName:$scope.gridOptions.selectedItems[0]}.Customer_Name)
	  	    var selected = $filter('filter')($scope.response,{liftId:$scope.gridOptions.selectedItems[0].liftId}); 
	  	    	var dataToSend = {
	  	    			fyaTranId:selected[0].fyaTranId,
	  	    			liftId:selected[0].liftId
	  	    	};
	  	    	serviceApi.doPostWithData('/RLMS/admin/validateAndApproveLift',dataToSend)
	  	    	.then(function(response) {
	  	    		$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
	  	    		$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
	  	    	})
	  	    }
		
	}]);
})();
