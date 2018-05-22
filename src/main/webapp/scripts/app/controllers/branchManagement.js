(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('branchManagement', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		$scope.goToAddBranch =function(){
			window.location.hash = "#/add-branch";
		};
		$scope.showTable = false;
		loadCompanyData();
		$scope.selectedCompany={};
		$scope.showCompany = false;
		function loadCompanyData(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		}
		$rootScope.editBranch={};
		$scope.editBranchDetails=function(row){
			$rootScope.editBranch.branchId=row.Branch_Id;
			$rootScope.editBranch.branchName=row.Branch_Name;
			$rootScope.editBranch.branchAddress=row.Address;
			$rootScope.editBranch.area=row.Area;
			$rootScope.editBranch.city=row.City;
			$rootScope.editBranch.pinCode=row.PinCode;
			window.location.hash = "#/edit-branch";
		};
		
		//-------Branch Details Table---------
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
	  	        var data;
	  	        if (searchText) {
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
	  	          var ft = searchText.toLowerCase();
	  	          $http.post('/RLMS/admin/getListOfBranchDtls',companyData).success(function(largeLoad) {
	  	        	$scope.showTable=true;
	  	        	  var branchDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var brachDetailsObj={};
	  	        		brachDetailsObj["Branch_Id"] =largeLoad[i].id;
	  	        		brachDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		brachDetailsObj["Address"] =largeLoad[i].branchAddress;
	  	        		brachDetailsObj["City"] =largeLoad[i].city;
	  	        		brachDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		brachDetailsObj["Number_Of_Technicians"] =largeLoad[i].numberOfTechnicians;
	  	        		brachDetailsObj["Number_Of_Lifts"] =largeLoad[i].numberOfLifts;
	  	        		brachDetailsObj["PinCode"] =largeLoad[i].pinCode;
	  	        		brachDetailsObj["Area"] =largeLoad[i].area;
	  	        		branchDetails.push(brachDetailsObj);
	  	        	  }
	  	            data = branchDetails.filter(function(item) {
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
	  	          $http.post('/RLMS/admin/getListOfBranchDtls',companyData).success(function(largeLoad) {
	  	        	$scope.showTable =true;
	  	        	var branchDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var brachDetailsObj={};
	  	        		brachDetailsObj["Branch_Id"] =largeLoad[i].id;
	  	        		brachDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		brachDetailsObj["Address"] =largeLoad[i].branchAddress;
	  	        		brachDetailsObj["City"] =largeLoad[i].city;
	  	        		brachDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		brachDetailsObj["Number_Of_Technicians"] =largeLoad[i].numberOfTechnicians;
	  	        		brachDetailsObj["Number_Of_Lifts"] =largeLoad[i].numberOfLifts;
	  	        		brachDetailsObj["PinCode"] =largeLoad[i].pinCode;
	  	        		brachDetailsObj["Area"] =largeLoad[i].area;
	  	        		branchDetails.push(brachDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(branchDetails, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
	  	  $scope.loadBranchInfo=function(){
		  	    	 $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
		  	 }
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompany= true;
			}else{
				$scope.loadBranchInfo();
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
					field : "Branch_Name",
					displayName:"Branch Name"
				}, {
					field : "Address",
					displayName:"Address"
				}, {
					field : "Number_Of_Technicians",
					displayName:"Number Of Technicians"
				}, {
					field : "Number_Of_Lifts",
					displayName:"Number Of Lifts"
				}, {
					field : "City",
					displayName:"City"
				}
				/*,{
					cellTemplate :  
			             '<button ng-click="$event.stopPropagation(); editBranchDetails(row.entity);" title="Edit" style="margin-top: 6px;height: 24px;" class="btn-sky"><span class="glyphicon glyphicon-pencil"></span></button>',
					width : 30
				},{
					cellTemplate :  
			             '<button ng-click="$event.stopPropagation(); deleteBranchDetails(row.entity);" title="Delete" style="margin-top: 6px;height: 24px;" class="btn-sky"><span class="glyphicon glyphicon-remove"></span></button>',
					width : 30
				}*/
				]
	  	    };
	}]);
})();
