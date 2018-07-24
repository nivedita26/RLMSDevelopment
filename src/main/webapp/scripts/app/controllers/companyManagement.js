(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('companyManagement', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope','$window','$timeout', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope,$window,$timeout) {
		$scope.goToAddCompany =function(){
			window.location.hash = "#/add-company";
		};
		$scope.alert = { type: 'success', msg: 'You successfully Deleted Company.',close:true };
		$scope.showAlert = false;
		if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
			$scope.showCompany= true;
		}else{
			$scope.showCompany= false;
		}
		//-------Company Details Table---------
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
	  	  $rootScope.editCompany={};
	  	  $scope.editCompanyDetails=function(row){
	  		$rootScope.editCompany.companyId=row.CompanyId;
	  		$rootScope.editCompany.companyName=row.Company_Name.replace(/-/g, '');
	  		$rootScope.editCompany.ownerName=row.OwnerName;
	  		$rootScope.editCompany.ownerNumber=row.OwnerNumber;
	  		$rootScope.editCompany.ownerEmail=row.OwnerEmail;
	  		$rootScope.editCompany.address=row.Address.replace(/-/g, '');
	  		$rootScope.editCompany.area=row.Area;
	  		$rootScope.editCompany.city=row.City.replace(/-/g, '');
	  		$rootScope.editCompany.pinCode=row.Pincode;
	  		$rootScope.editCompany.contactNumber=row.Contact_Number.replace(/-/g, '');
	  		$rootScope.editCompany.emailId=row.Email_Id.replace(/-/g, '');
	  		$rootScope.editCompany.panNumber=row.PanNumber;
	  		$rootScope.editCompany.tinNumber=row.TinNumber;
	  		$rootScope.editCompany.vatNumber=row.VatNumber;
			window.location.hash = "#/edit-company";
			};
			$scope.deleteCompanyDetails=function(row){
				var deleteCompany = $window.confirm('Are you sure you want to delete the company');
				if(deleteCompany){
					var companyData = {};
					companyData = {
							companyId:row.CompanyId
					};
					serviceApi.doPostWithData("/RLMS/admin/deleteCompany",companyData)
					.then(function(response){
						$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
						$scope.showAlert = true;
						var key = Object.keys(response);
						var successMessage = response[key[0]];
						$scope.alert.msg = successMessage;
						$scope.alert.type = "success";
						$timeout(function() {
							$scope.showAlert=false
						}, 5000);
					},function(error){
						$scope.showAlert = true;
						$scope.alert.msg = error.exceptionMessage;
						$scope.alert.type = "danger";
					});
				}
				
			};
	  	    $scope.getPagedDataAsync = function(pageSize, page, searchText) {
	  	      setTimeout(function() {
	  	        var data;
	  	        if (searchText) {
	  	          var ft = searchText.toLowerCase();
	  	          $http.post('/RLMS/admin/getAllCompanyDetails').success(function(largeLoad) {
	  	        	  var companyDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var companyDetailsObj={};
	  	        		if(!!largeLoad[i].companyId){
	  	        			companyDetailsObj["CompanyId"] =largeLoad[i].companyId;
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			companyDetailsObj["Company_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			companyDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			companyDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			companyDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			companyDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			companyDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			companyDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			companyDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			companyDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfBranches){
	  	        			companyDetailsObj["Total_Branches"] =largeLoad[i].numberOfBranches;
	  	        		}else{
	  	        			companyDetailsObj["Total_Branches"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfTech){
	  	        			companyDetailsObj["Total_Technicians"] =largeLoad[i].numberOfTech;
	  	        		}else{
	  	        			companyDetailsObj["Total_Technicians"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfLifts){
	  	        			companyDetailsObj["Total_Lifts"] =largeLoad[i].numberOfLifts;
	  	        		}else{
	  	        			companyDetailsObj["Total_Lifts"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].ownerName){
	  	        			companyDetailsObj["OwnerName"] =largeLoad[i].ownerName;
	  	        		}
	  	        		if(!!largeLoad[i].ownerNumber){
	  	        			companyDetailsObj["OwnerNumber"] =largeLoad[i].ownerNumber;
	  	        		}
	  	        		if(!!largeLoad[i].ownerEmail){
	  	        			companyDetailsObj["OwnerEmail"] =largeLoad[i].ownerEmail;
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			companyDetailsObj["Area"] =largeLoad[i].area;
	  	        		}
	  	        		if(!!largeLoad[i].pinCode){
	  	        			companyDetailsObj["Pincode"] =largeLoad[i].pinCode;
	  	        		}
	  	        		if(!!largeLoad[i].panNumber){
	  	        			companyDetailsObj["PanNumber"] =largeLoad[i].panNumber;
	  	        		}
	  	        		if(!!largeLoad[i].tinNumber){
	  	        			companyDetailsObj["TinNumber"] =largeLoad[i].tinNumber;
	  	        		}
	  	        		if(!!largeLoad[i].vatNumber){
	  	        			companyDetailsObj["VatNumber"] =largeLoad[i].vatNumber;
	  	        		}
	  	        		companyDetails.push(companyDetailsObj);
	  	        	  }
	  	            data = companyDetails.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
	  	          $http.post('/RLMS/admin/getAllCompanyDetails').success(function(largeLoad) {
	  	        	  var companyDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var companyDetailsObj={};
		  	        	if(!!largeLoad[i].companyId){
	  	        			companyDetailsObj["CompanyId"] =largeLoad[i].companyId;
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			companyDetailsObj["Company_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			companyDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			companyDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			companyDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			companyDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			companyDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			companyDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			companyDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			companyDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfBranches){
	  	        			companyDetailsObj["Total_Branches"] =largeLoad[i].numberOfBranches;
	  	        		}else{
	  	        			companyDetailsObj["Total_Branches"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfTech){
	  	        			companyDetailsObj["Total_Technicians"] =largeLoad[i].numberOfTech;
	  	        		}else{
	  	        			companyDetailsObj["Total_Technicians"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfLifts){
	  	        			companyDetailsObj["Total_Lifts"] =largeLoad[i].numberOfLifts;
	  	        		}else{
	  	        			companyDetailsObj["Total_Lifts"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].ownerName){
	  	        			companyDetailsObj["OwnerName"] =largeLoad[i].ownerName;
	  	        		}
	  	        		if(!!largeLoad[i].ownerNumber){
	  	        			companyDetailsObj["OwnerNumber"] =largeLoad[i].ownerNumber;
	  	        		}
	  	        		if(!!largeLoad[i].ownerEmail){
	  	        			companyDetailsObj["OwnerEmail"] =largeLoad[i].ownerEmail;
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			companyDetailsObj["Area"] =largeLoad[i].area;
	  	        		}
	  	        		if(!!largeLoad[i].pinCode){
	  	        			companyDetailsObj["Pincode"] =largeLoad[i].pinCode;
	  	        		}
	  	        		if(!!largeLoad[i].panNumber){
	  	        			companyDetailsObj["PanNumber"] =largeLoad[i].panNumber;
	  	        		}
	  	        		if(!!largeLoad[i].tinNumber){
	  	        			companyDetailsObj["TinNumber"] =largeLoad[i].tinNumber;
	  	        		}
	  	        		if(!!largeLoad[i].vatNumber){
	  	        			companyDetailsObj["VatNumber"] =largeLoad[i].vatNumber;
	  	        		}
	  	        		companyDetails.push(companyDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(companyDetails, page, pageSize);
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
	  	      columnDefs : [ {
					field : "Company_Name",
					displayName:"Company_Name"
				}, {
					field : "Address",
					displayName:"Address"
				}, {
					field : "City",
					displayName:"City"
				}, {
					field : "Contact_Number",
					displayName:"Contact_Number"
				}
				, {
					field : "Email_Id",
					displayName:"Email_Id"
				}, {
					field : "Total_Branches",
					displayName:"Total_Branches"
				}
				, {
					field : "Total_Technicians",
					displayName:"Total_Technicians"
				}
				, {
					field : "Total_Lifts",
					displayName:"Total_Lifts"
				},{
					cellTemplate :  
			             '<button ng-click="$event.stopPropagation(); editCompanyDetails(row.entity);" title="Edit" style="margin-top: 6px;height: 24px;" class="btn-sky"><span class="glyphicon glyphicon-pencil"></span></button>',
					width : 30
				},{
					cellTemplate :  
			             '<button ng-click="$event.stopPropagation(); deleteCompanyDetails(row.entity);" title="Delete" style="margin-top: 6px;height: 24px;" class="btn-sky"><span class="glyphicon glyphicon-remove"></span></button>',
					width : 30
				}
				]
	  	    };
		
	}]);
})();
