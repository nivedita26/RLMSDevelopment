(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('customerManagementCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initCustomerList();
		loadCompanyData();
		$scope.showCompany = false;
		$scope.showBranch = false;
		$scope.goToAddCustomer = function(){
			window.location.hash = "#/add-customer";
		}
		$scope.editCustomer =function(){
			window.location.hash = "#/edit-customer";
		};
		function initCustomerList(){
			 $scope.selectedCompany={};
			 $scope.selectedBranch = {};
			 //$scope.selectedActiveFlag = {};
			 $scope.branches=[];
			 $scope.showTable = false;
		} 
		
		$rootScope.editCustomer={};
		$rootScope.customerTypes=[{id:15,name:'RESIDENTIAL'},{id:16,name:'COMMERCIAL'},{id:17,name:'BUNGLOW'},{id:18,name:'HOSPITAL'},{id:19,name:'GOODS'},{id:20,name:'DUMB_WAITER'}];
		$rootScope.activeFlags=[{id:1,name:'Active'},{id:0,name:'Inactive'}];
		$scope.editCustomerDetails=function(row){
			$rootScope.editCustomer.customerId=row.CustomerId;
			$rootScope.editCustomer.firstName=row.Name;
			//$rootScope.editCustomer.lastName=row.lastName;
			$rootScope.editCustomer.cntNumber=row.Contact_Number.replace(/-/g, '');
			$rootScope.editCustomer.address=row.Address.replace(/-/g, '');
			$rootScope.editCustomer.area=row.Area;
			$rootScope.editCustomer.customerType=row.CustomerType;
			//$rootScope.selectedCustomerTypes=row.CustomerType;
			$rootScope.editCustomer.city=row.City.replace(/-/g, '');
			$rootScope.editCustomer.pinCode=row.PinCode;
			$rootScope.editCustomer.emailID=row.Email_Id.replace(/-/g, '');
			$rootScope.editCustomer.totalNumberOfLifts=row.Lift_Count;
			$rootScope.editCustomer.chairmanName=row.ChairmanName.replace(/-/g, '');
			$rootScope.editCustomer.chairmanNumber=row.ChairmanNumber.replace(/-/g, '');
			$rootScope.editCustomer.chairmanEmail=row.ChairmanEmail.replace(/-/g, '');
			$rootScope.editCustomer.secretaryName=row.SecretaryName.replace(/-/g, '');
			$rootScope.editCustomer.secretaryNumber=row.SecretaryNumber.replace(/-/g, '');
			$rootScope.editCustomer.secretaryEmail=row.SecretaryEmail.replace(/-/g, '');
			$rootScope.editCustomer.treasurerName=row.TreasurerName.replace(/-/g, '');
			$rootScope.editCustomer.treasurerNumber=row.TreasurerNumber.replace(/-/g, '');
			$rootScope.editCustomer.treasurerEmail=row.TreasurerEmail.replace(/-/g, '');
			$rootScope.editCustomer.watchmenName=row.WatchmenName.replace(/-/g, '');
			$rootScope.editCustomer.watchmenNumber=row.WatchmenNumber.replace(/-/g, '');
			$rootScope.editCustomer.watchmenEmail=row.WatchmenEmail.replace(/-/g, '');
			$rootScope.editCustomer.vatNumber=row.VatNumber.replace(/-/g, '');
			$rootScope.editCustomer.tinNumber=row.TinNumber.replace(/-/g, '');
			$rootScope.editCustomer.panNumber=row.PanNumber.replace(/-/g, '');
			$rootScope.editCustomer.activeFlag=row.Active_Flag;
			window.location.hash = "#/edit-customer";
		};
		
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
		  	        	if(!!largeLoad[i].customerId){
	  	        			userDetailsObj["CustomerId"] =largeLoad[i].customerId;
	  	        		}else{
	  	        			userDetailsObj["CustomerId"] =" - ";
	  	        		}
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
	  	        		if(!!largeLoad[i].area){
	  	        			userDetailsObj["Area"] =largeLoad[i].area;
	  	        		}else{
	  	        			userDetailsObj["Area"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].pinCode){
	  	        			userDetailsObj["PinCode"] =largeLoad[i].pinCode;
	  	        		}else{
	  	        			userDetailsObj["PinCode"] =" - ";
	  	        		}
	  	        		if(largeLoad[i].activeFlag=="0"||largeLoad[i].activeFlag=="1"){
	  	        			userDetailsObj["Active_Flag"] =largeLoad[i].activeFlag;
	  	        		}else{
	  	        			userDetailsObj["Active_Flag"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].chairmanName){
	  	        			userDetailsObj["ChairmanName"] =largeLoad[i].chairmanName;
	  	        		}else{
	  	        			userDetailsObj["ChairmanName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].chairmanNumber){
	  	        			userDetailsObj["ChairmanNumber"] =largeLoad[i].chairmanNumber;
	  	        		}else{
	  	        			userDetailsObj["ChairmanNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].chairmanEmail){
	  	        			userDetailsObj["ChairmanEmail"] =largeLoad[i].chairmanEmail;
	  	        		}else{
	  	        			userDetailsObj["ChairmanEmail"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].treasurerName){
	  	        			userDetailsObj["TreasurerName"] =largeLoad[i].treasurerName;
	  	        		}else{
	  	        			userDetailsObj["TreasurerName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].treasurerEmail){
	  	        			userDetailsObj["TreasurerEmail"] =largeLoad[i].treasurerEmail;
	  	        		}else{
	  	        			userDetailsObj["TreasurerEmail"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].treasurerNumber){
	  	        			userDetailsObj["TreasurerNumber"] =largeLoad[i].treasurerNumber;
	  	        		}else{
	  	        			userDetailsObj["TreasurerNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].secretaryEmail){
	  	        			userDetailsObj["SecretaryEmail"] =largeLoad[i].secretaryEmail;
	  	        		}else{
	  	        			userDetailsObj["SecretaryEmail"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].secretaryName){
	  	        			userDetailsObj["SecretaryName"] =largeLoad[i].secretaryName;
	  	        		}else{
	  	        			userDetailsObj["SecretaryName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].secretaryNumber){
	  	        			userDetailsObj["SecretaryNumber"] =largeLoad[i].secretaryNumber;
	  	        		}else{
	  	        			userDetailsObj["SecretaryNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].watchmenEmail){
	  	        			userDetailsObj["WatchmenEmail"] =largeLoad[i].watchmenEmail;
	  	        		}else{
	  	        			userDetailsObj["WatchmenEmail"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].watchmenNumber){
	  	        			userDetailsObj["WatchmenNumber"] =largeLoad[i].watchmenNumber;
	  	        		}else{
	  	        			userDetailsObj["WatchmenNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].vatNumber){
	  	        			userDetailsObj["VatNumber"] =largeLoad[i].vatNumber;
	  	        		}else{
	  	        			userDetailsObj["VatNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].tinNumber){
	  	        			userDetailsObj["TinNumber"] =largeLoad[i].tinNumber;
	  	        		}else{
	  	        			userDetailsObj["TinNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].panNumber){
	  	        			userDetailsObj["PanNumber"] =largeLoad[i].panNumber;
	  	        		}else{
	  	        			userDetailsObj["PanNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].watchmenName){
	  	        			userDetailsObj["WatchmenName"] =largeLoad[i].watchmenName;
	  	        		}else{
	  	        			userDetailsObj["WatchmenName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].customerType){
	  	        			userDetailsObj["CustomerType"] =largeLoad[i].customerType;
	  	        		}else{
	  	        			userDetailsObj["CustomerType"] =" - ";
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
				$scope.loadCustomerInfo();
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
	  	    	  width: "170"
	  	      },{
	  	    	  field : "Address",
	  	    	  displayName:"Address",
	  	    	  width: "170"
	  	      }, {
	  	    	  field : "Branch",
	  	    	  displayName:"Branch",
	  	    	  width: "160"
	  	      },{
	  	    	  field : "City",
	  	    	  displayName:"City",
	  	    	  width: "160"
	  	      },{
	  	    	  field : "Contact_Number",
	  	    	  displayName:"Contact Number",
	  	    	  width: "170"
	  	      },  {
	  	    	  field : "Email_Id",
	  	    	  displayName:"Email Id",
	  	    	  width: "190"
	  	      },{
	  	    	  field : "Lift_Count",
	  	    	  displayName:"Lift Count",
	  	    	  width: "110"
	  	      },{
	  	    	  cellTemplate :  
	  	    		  '<button ng-click="$event.stopPropagation(); editCustomerDetails(row.entity);" title="Edit" style="margin-top: 6px;height: 38px;width: 38px;" class="btn-sky"><span class="glyphicon glyphicon-pencil"></span></button>',
  	    		  width : 40
	  	      }
			]
	  	    };
		
	}]);
})();
