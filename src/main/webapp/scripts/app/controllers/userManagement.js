(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('userManagement', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope','$timeout','$window', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope,$timeout,$window) {
		loadCompanyData();
		 $scope.selectedCompany={};
		 $scope.showTable = false;
		$scope.goToAddUser =function(){
			window.location.hash = "#/add-user";
		};
		$scope.editUser =function(){
			window.location.hash = "#/edit-user";
		};
		$scope.alert = { type: 'success', msg: 'You successfully Deleted User.',close:true };
		$scope.showAlert = false;
		$rootScope.editUser={};
		$scope.editThisUser=function(row){
			var fullName=row.Name.replace(/-/g, '').split(" ");
			$rootScope.editUser.userId=row.Id;
			$rootScope.editUser.firstName=fullName[0];
			$rootScope.editUser.lastName=fullName[1];
			$rootScope.editUser.company=row.Company.replace(/-/g, '');
			$rootScope.editUser.contactnumber=row.Contact_Number.replace(/-/g, '');
			$rootScope.editUser.address=row.Address.replace(/-/g, '');
			$rootScope.editUser.area=row.Area;
			$rootScope.editUser.city=row.City.replace(/-/g, '');
			$rootScope.editUser.pincode=row.PinCode;
			$rootScope.editUser.branch=row.Branch.replace(/-/g, '');
			$rootScope.editUser.role=row.Role.replace(/-/g, '');
			$rootScope.editUser.emailid=row.Email_Id.replace(/-/g, '');
			window.location.hash = "#/edit-user";
		};
		$scope.deleteUserDetails=function(row){
			var deleteUser = $window.confirm('Are you sure you want to delete the user');
			if(deleteUser){
				var userData = {};
				userData = {
						userId:row.Id
				};
				serviceApi.doPostWithData("/RLMS/admin/deleteUser",userData)
				.then(function(response){
					$scope.loadUsersInfo();
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					$timeout(function() {
						$scope.showAlert=false;
					}, 5000);
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			
		};
		$scope.showCompany = false;
		
		function loadCompanyData(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
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
	  	          var ft = searchText.toLowerCase();
	  	        var companyData ={};
	  	    	if($scope.showCompany == true){
	  	    		companyData = {
  						companyId : $scope.selectedCompany.selected.companyId
  					}
	  	    	}else{
	  	    		companyData = {
  						companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
  					}
	  	    	}
	  	        serviceApi.doPostWithData('/RLMS/admin/getAllRegisteredUsers',companyData)
	  	         .then(function(largeLoad) {
	  	        	$scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].userId){
	  	        			userDetailsObj["Id"] =largeLoad[i].userId;
	  	        		}else{
	  	        			userDetailsObj["Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].userName){
	  	        			userDetailsObj["Name"] =largeLoad[i].userName;
	  	        		}else{
	  	        			userDetailsObj["Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			userDetailsObj["Company"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			userDetailsObj["Company"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			userDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			userDetailsObj["Area"] =largeLoad[i].area;
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].pinCode){
	  	        			userDetailsObj["PinCode"] =largeLoad[i].pinCode;
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].userRoleName){
	  	        			userDetailsObj["Role"] =largeLoad[i].userRoleName;
	  	        		}else{
	  	        			userDetailsObj["Role"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			userDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			userDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            data = userDetails.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
	  	        	var companyData ={};
		  	    	if($scope.showCompany == true){
		  	    		companyData = {
	  						companyId : $scope.selectedCompany.selected.companyId
	  					}
		  	    	}else{
		  	    		companyData = {
	  						companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
	  					}
		  	    	}
	  	        	serviceApi.doPostWithData('/RLMS/admin/getAllRegisteredUsers',companyData).then(function(largeLoad) {
	  	        		 $scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var userDetailsObj={};
		  	        	if(!!largeLoad[i].userId){
	  	        			userDetailsObj["Id"] =largeLoad[i].userId;
	  	        		}else{
	  	        			userDetailsObj["Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].userName){
	  	        			userDetailsObj["Name"] =largeLoad[i].userName;
	  	        		}else{
	  	        			userDetailsObj["Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			userDetailsObj["Company"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			userDetailsObj["Company"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			userDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			userDetailsObj["Area"] =largeLoad[i].area;
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].pinCode){
	  	        			userDetailsObj["PinCode"] =largeLoad[i].pinCode;
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].userRoleName){
	  	        			userDetailsObj["Role"] =largeLoad[i].userRoleName;
	  	        		}else{
	  	        			userDetailsObj["Role"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			userDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			userDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(userDetails, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
	  	  $scope.loadUsersInfo=function(){
	  	    	 $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
	  	    }
	  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
			$scope.showCompany= true;
		}else{
			$scope.loadUsersInfo();
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
	  	      gridFooterHeight:35,
	  	    columnDefs : [ {
				field : "Name",
				displayName:"Name",
				width: "160"
			},{
				field : "Address",
				displayName:"Address",
				width: "160"
			}, {
				field : "Branch",
				displayName:"Branch",
				width: "160"
			},{
				field : "City",
				displayName:"City",
				width: "140"
			},{
				field : "Role",
				displayName:"Role",
				width: "170"
			},{
				field : "Contact_Number",
				displayName:"Contact Number",
				width: "170"
			},  {
				field : "Email_Id",
				displayName:"Email_Id",
				width: "190"
			},{
				cellTemplate :  
		             '<button ng-click="$event.stopPropagation(); editThisUser(row.entity);" title="Edit" style="margin-top: 6px;height: 38px;width: 38px;" class="btn-sky"><span class="glyphicon glyphicon-pencil"></span></button>',
				width : 40
			},{
				cellTemplate :  
		             '<button ng-click="$event.stopPropagation(); deleteUserDetails(row.entity);" title="Delete" style="margin-top: 6px;height: 38px;width: 38px;" class="btn-sky"><span class="glyphicon glyphicon-remove"></span></button>',
				width : 40
			}
			]
	  	    };
		
	}]);
})();
