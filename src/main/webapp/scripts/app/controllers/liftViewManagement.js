(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('liftViewManagementCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initCustomerList();
		$scope.showCompany = false;
		$scope.showBranch = false;
		$scope.goToAddLift =function(){
			window.location.hash = "#/add-lift";
		};
		function initCustomerList(){
			 $scope.selectedCompany={};
			 $scope.selectedBranch = {};
			 $scope.branches=[];
			 $scope.showTable = false;
		} 
		
		$rootScope.AMCType=[
			{
				id:42,
				name:'Comprehensive'
			},
			{
				id:43,
				name:'Non Comprehensive'
			},
			{
				id:44,
				name:'On Demand'
			},
			{
				id:45,
				name:'Other'
			},
		];
		//Door Type
		$rootScope.DoorType=[
			{
				id:0,
				name:'Auto Door'
			},
			{
				id:1,
				name:'Manual Door'
			}
		];
		//Engine-Machine Type
		$rootScope.EngineMachineTypes=[
			{
				id:0,
				name:'Geared'
			},
			{
				id:1,
				name:'Gearless'
			}
		];
		//Collective Type
		$rootScope.CollectiveType=[
			{
				id:0,
				name:'Down Collective'
			},
			{
				id:1,
				name:'Full Collective'
			}
		];
		//SimplexDuplex - Group
		$rootScope.SimplexDuplex=[
			{
				id:0,
				name:'Simplex'
			},
			{
				id:1,
				name:'Duplex'
			},
			{
				id:2,
				name:'Group'
			}
		];
		//WiringScheme
		$rootScope.WiringSchemes=[
			{
				id:0,
				name:'Pluggable'
			},
			{
				id:1,
				name:'NonPluggable'
			}
		];
		//intercom
		$rootScope.Intercomms=[
			{
				id:1,
				name:'Yes'
			},
			{
				id:0,
				name:'No'
			}
		];
		//Alarm
		$rootScope.Alarms=[
			{
				id:1,
				name:'Yes'
			},
			{
				id:0,
				name:'No'
			}
		];
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
		    	var emptyLiftArray=[];
		    	$scope.myData=emptyLiftArray;
		    	
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
	  	        serviceApi.doPostWithData('/RLMS/admin/getLiftDetailsForBranch',branchData)
	  	         .then(function(largeLoad) {
	  	        	$scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			userDetailsObj["Lift_Number"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			userDetailsObj["Lift_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].customerName){
	  	        			userDetailsObj["Customer_Name"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			userDetailsObj["Customer_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceStartDateStr){
	  	        			userDetailsObj["Service_Start_Date"] =largeLoad[i].serviceStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceEndDateStr){
	  	        			userDetailsObj["Service_End_Date"] =largeLoad[i].serviceEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["Installation_Date"] =largeLoad[i].dateOfInstallationStr;
	  	        		}else{
	  	        			userDetailsObj["Installation_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDateStr){
	  	        			userDetailsObj["Amc_Start_Date"] =largeLoad[i].amcStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcEndtDateStr){
	  	        			userDetailsObj["Amc_End_Date"] =largeLoad[i].amcEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcTypeStr){
	  	        			userDetailsObj["amcType"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			userDetailsObj["amcType"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftId){
	  	        			userDetailsObj["liftId"] =largeLoad[i].liftId;
	  	        		}else{
	  	        			userDetailsObj["liftId"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftType){
	  	        			userDetailsObj["liftType"] =largeLoad[i].liftType;
	  	        		}else{
	  	        			userDetailsObj["liftType"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftType){
	  	        			if(largeLoad[i].liftType === 1){
	  	        				userDetailsObj["LiftTypeStr"]="Geared";
	  	        			}else if(largeLoad[i].liftType === 2){
	  	        				userDetailsObj["LiftTypeStr"]="Gearless";
	  	        			}else{
	  	        			userDetailsObj["LiftTypeStr"] ="Hydraulic";
	  	        			}
	  	        		}else{
	  	        			userDetailsObj["LiftTypeStr"] =" - ";
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
	  	        	serviceApi.doPostWithData('/RLMS/admin/getLiftDetailsForBranch',branchData).then(function(largeLoad) {
	  	        		 $scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			userDetailsObj["Lift_Number"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			userDetailsObj["Lift_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftId){
	  	        			userDetailsObj["liftId"] =largeLoad[i].liftId;
	  	        		}else{
	  	        			userDetailsObj["liftId"] ="";
	  	        		}
	  	        		if(!!largeLoad[i].customerName){
	  	        			userDetailsObj["Customer_Name"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			userDetailsObj["Customer_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}	  	        		
	  	        		if(!!largeLoad[i].serviceStartDateStr){
	  	        			userDetailsObj["Service_Start_Date"] =largeLoad[i].serviceStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceEndDateStr){
	  	        			userDetailsObj["Service_End_Date"] =largeLoad[i].serviceEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["Installation_Date"] =largeLoad[i].dateOfInstallationStr;
	  	        		}else{
	  	        			userDetailsObj["Installation_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDateStr){
	  	        			userDetailsObj["Amc_Start_Date"] =largeLoad[i].amcStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcEndDateStr){
	  	        			userDetailsObj["Amc_End_Date"] =largeLoad[i].amcEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcTypeStr){
	  	        			userDetailsObj["amcTypeStr"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			userDetailsObj["amcTypeStr"] ="-";
	  	        		}
	  	        		if(!!largeLoad[i].amcType){
	  	        			userDetailsObj["amcType"] =largeLoad[i].amcType;
	  	        		}else{
	  	        			userDetailsObj["amcType"] ="0";
	  	        		}
	  	        		if(!!largeLoad[i].amcAmount){
	  	        			userDetailsObj["Amc_Amount"] =largeLoad[i].amcAmount;
	  	        		}else{
	  	        			userDetailsObj["Amc_Amount"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftType){
	  	        			userDetailsObj["liftType"] =largeLoad[i].liftType;
	  	        		}else{
	  	        			userDetailsObj["liftType"] ="";
	  	        		}
	  	        		if(!!largeLoad[i].liftType){
	  	        			if(largeLoad[i].liftType === 1){
	  	        				userDetailsObj["LiftTypeStr"]="Geared";
	  	        			}else if(largeLoad[i].liftType === 2){
	  	        				userDetailsObj["LiftTypeStr"]="Gearless";
	  	        			}else{
	  	        			userDetailsObj["LiftTypeStr"] ="Hydraulic";
	  	        			}
	  	        		}else{
	  	        			userDetailsObj["LiftTypeStr"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].pinCode){
	  	        			userDetailsObj["PinCode"] =largeLoad[i].pinCode;
	  	        		}else{
	  	        			userDetailsObj["PinCode"] ="";
	  	        		}
	  	        		if(!!largeLoad[i].latitude){
	  	        			userDetailsObj["Latitude"] =largeLoad[i].latitude;
	  	        		}else{
	  	        			userDetailsObj["Latitude"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].longitude){
	  	        			userDetailsObj["Longitude"] =largeLoad[i].longitude;
	  	        		}else{
	  	        			userDetailsObj["Longitude"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			userDetailsObj["Area"] =largeLoad[i].area;
	  	        		}else{
	  	        			userDetailsObj["Area"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].noOfStops){
	  	        			userDetailsObj["NoOfStops"] =largeLoad[i].noOfStops;
	  	        		}else{
	  	        			userDetailsObj["NoOfStops"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].machineMake){
	  	        			userDetailsObj["MachineMake"] =largeLoad[i].machineMake;
	  	        		}else{
	  	        			userDetailsObj["MachineMake"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].machineCapacity){
	  	        			userDetailsObj["machineCapacity"] =largeLoad[i].machineCapacity;
	  	        		}else{
	  	        			userDetailsObj["machineCapacity"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].machineCurrent){
	  	        			userDetailsObj["MachineCurrent"] =largeLoad[i].machineCurrent;
	  	        		}else{
	  	        			userDetailsObj["MachineCurrent"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].breakVoltage){
	  	        			userDetailsObj["BreakVoltage"] =largeLoad[i].breakVoltage;
	  	        		}else{
	  	        			userDetailsObj["BreakVoltage"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].panelMake){
	  	        			userDetailsObj["PanelMake"] =largeLoad[i].panelMake;
	  	        		}else{
	  	        			userDetailsObj["PanelMake"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].ard){
	  	        			userDetailsObj["ard"] =largeLoad[i].ard;
	  	        		}else{
	  	        			userDetailsObj["ard"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].noOfBatteries){
	  	        			userDetailsObj["noOfBatteries"] =largeLoad[i].noOfBatteries;
	  	        		}else{
	  	        			userDetailsObj["noOfBatteries"] ="";
	  	        		}
	  	        		if(!!largeLoad[i].batteryCapacity){
	  	        			userDetailsObj["batteryCapacity"] =largeLoad[i].batteryCapacity;
	  	        		}else{
	  	        			userDetailsObj["batteryCapacity"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].batteryMake){
	  	        			userDetailsObj["batteryMake"] =largeLoad[i].batteryMake;
	  	        		}else{
	  	        			userDetailsObj["batteryMake"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].copMake){
	  	        			userDetailsObj["copMake"] =largeLoad[i].copMake;
	  	        		}else{
	  	        			userDetailsObj["copMake"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].lopMake){
	  	        			userDetailsObj["lopMake"] =largeLoad[i].lopMake;
	  	        		}else{
	  	        			userDetailsObj["lopMake"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].alarmBattery){
	  	        			userDetailsObj["alarmBattery"] =largeLoad[i].alarmBattery;
	  	        		}else{
	  	        			userDetailsObj["alarmBattery"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].accessControl){
	  	        			userDetailsObj["accessControl"] =largeLoad[i].accessControl;
	  	        		}else{
	  	        			userDetailsObj["accessControl"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].imei){
	  	        			userDetailsObj["imei"] =largeLoad[i].imei;
	  	        		}else{
	  	        			userDetailsObj["imei"] ="0";
	  	        		}
	  	        		if(!!largeLoad[i].lmsEventFromContactNo){
	  	        			userDetailsObj["lmsEventFromContactNo"] =largeLoad[i].lmsEventFromContactNo;
	  	        		}else{
	  	        			userDetailsObj["lmsEventFromContactNo"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].autoDoorMake){
	  	        			userDetailsObj["autoDoorMake"] =largeLoad[i].autoDoorMake;
	  	        		}else{
	  	        			userDetailsObj["autoDoorMake"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].fireMode){
	  	        			userDetailsObj["fireMode"] =largeLoad[i].fireMode;
	  	        		}else{
	  	        			userDetailsObj["fireMode"] ="";
	  	        		}
	  	        		if(!!largeLoad[i].intercomm){
	  	        			userDetailsObj["intercomm"] =largeLoad[i].intercomm;
	  	        		}else{
	  	        			userDetailsObj["intercomm"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].alarm){
	  	        			userDetailsObj["alarm"] =largeLoad[i].alarm;
	  	        		}else{
	  	        			userDetailsObj["alarm"] ="";
	  	        		}
	  	        		if(largeLoad[i].doorType=="0"||largeLoad[i].doorType=="1"){
	  	        			userDetailsObj["doorType"] =largeLoad[i].doorType;
	  	        		}else{
	  	        			userDetailsObj["doorType"] =" - ";
	  	        		}
	  	        		if(largeLoad[i].engineType=="0"||largeLoad[i].engineType=="1"){
	  	        			userDetailsObj["engineType"] =largeLoad[i].engineType;
	  	        		}else{
	  	        			userDetailsObj["engineType"] =" - ";
	  	        		}
	  	        		if(largeLoad[i].collectiveType=="0"||largeLoad[i].collectiveType=="1"){
	  	        			userDetailsObj["collectiveType"] =largeLoad[i].collectiveType;
	  	        		}else{
	  	        			userDetailsObj["collectiveType"] =" - ";
	  	        		}
	  	        		if(largeLoad[i].simplexDuplex=="0"||largeLoad[i].simplexDuplex=="1"){
	  	        			userDetailsObj["simplexDuplex"] =largeLoad[i].simplexDuplex;
	  	        		}else{
	  	        			userDetailsObj["simplexDuplex"] =" - ";
	  	        		}
	  	        		if(largeLoad[i].wiringShceme=="0"||largeLoad[i].wiringShceme=="1"){
	  	        			userDetailsObj["wiringShceme"] =largeLoad[i].wiringShceme;
	  	        		}else{
	  	        			userDetailsObj["wiringShceme"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].machinePhoto){
	  	        			userDetailsObj["machinePhoto"] =largeLoad[i].machinePhoto;
	  	        		}else{
	  	        			userDetailsObj["machinePhoto"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].panelPhoto){
	  	        			userDetailsObj["panelPhoto"] =largeLoad[i].panelPhoto;
	  	        		}else{
	  	        			userDetailsObj["panelPhoto"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].ardPhoto){
	  	        			userDetailsObj["ardPhoto"] =largeLoad[i].ardPhoto;
	  	        		}else{
	  	        			userDetailsObj["ardPhoto"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].lopPhoto){
	  	        			userDetailsObj["lopPhoto"] =largeLoad[i].lopPhoto;
	  	        		}else{
	  	        			userDetailsObj["lopPhoto"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].copPhoto){
	  	        			userDetailsObj["copPhoto"] =largeLoad[i].copPhoto;
	  	        		}else{
	  	        			userDetailsObj["copPhoto"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].autoDoorHeaderPhoto){
	  	        			userDetailsObj["autoDoorHeaderPhoto"] =largeLoad[i].autoDoorHeaderPhoto;
	  	        		}else{
	  	        			userDetailsObj["autoDoorHeaderPhoto"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].cartopPhoto){
	  	        			userDetailsObj["cartopPhoto"] =largeLoad[i].cartopPhoto;
	  	        		}else{
	  	        			userDetailsObj["cartopPhoto"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].lobbyPhoto){
	  	        			userDetailsObj["lobbyPhoto"] =largeLoad[i].lobbyPhoto;
	  	        		}else{
	  	        			userDetailsObj["lobbyPhoto"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].wiringPhoto){
	  	        			userDetailsObj["wiringPhoto"] =largeLoad[i].wiringPhoto;
	  	        		}else{
	  	        			userDetailsObj["wiringPhoto"] =" - ";
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
	  	      width:80,
	  	      enablePaging: true,
	  	      showFooter: true,
	  	      totalServerItems: 'totalServerItems',
	  	      pagingOptions: $scope.pagingOptions,
	  	      filterOptions: $scope.filterOptions,
	  	      multiSelect: false,
	  	      gridFooterHeight:35,
	  	      columnDefs : [ {
					field : "liftId",
					displayName:"Lift Id",
					width: "120"
				}, {
					field : "Lift_Number",
					displayName:"Lift Number",
					width: "120"
				}, {
					field : "LiftTypeStr",
					displayName:"Lift Type",
					width: "140"
				}, {
					field : "Customer_Name",
					displayName:"Customer",
					width: "140"
				}, {
					field : "Address",
					displayName:"Address",
					width: "140"
				}, {
					field : "Branch_Name",
					displayName:"Branch",
					width: "140"
				},{
					field : "City",
					displayName:"City",
					width: "120"
				}, {
					field : "Installation_Date",
					displayName:"Installation Date",
					width: "140"
				}, {
					field : "Service_Start_Date",
					displayName:"Warranty Start Date",
					width: "140"
				}, {
					field : "Service_End_Date",
					displayName:"Warranty End Date",
					width: "140"
				},
				{
					field : "Amc_Start_Date",
					displayName:"Amc Start Date",
					width: "140"
				},
				{
					field : "Amc_End_Date",
					displayName:"Amc End Date",
					width: "140"
				},
				{
					field : "amcTypeStr",
					displayName:"AMC Type",
					width: "140"
				},{
					cellTemplate :  
			             '<button ng-click="$event.stopPropagation(); viewLiftDetails(row.entity);" title="View" style="margin-top: 2px;height: 32px;width :32px;" class="btn-sky"><span class="glyphicon glyphicon-eye-open"></span></button>',
					width : 35
				},{
					cellTemplate :  
			             '<button ng-click="$event.stopPropagation(); editLiftDetails(row.entity);" title="Edit" style="margin-top: 2px;height: 32px;width :32px;" class="btn-sky"><span class="glyphicon glyphicon-pencil"></span></button>',
					width : 35
				}
				]
	  	    };
	  
	  	    $rootScope.viewLift={};
			
	  	    $scope.viewLiftDetails=function(row){
	  	    	
						$rootScope.viewLift.liftId=row.liftId;
						$rootScope.viewLift.address=row.Address.replace(/-/g, '');
						$rootScope.viewLift.city=row.City.replace(/-/g, '');
						$rootScope.viewLift.customerName=row.Customer_Name.replace(/-/g, '');
						$rootScope.viewLift.branchName=row.Branch_Name.replace(/-/g, '');
						$rootScope.viewLift.serviceStartDate=row.Service_Start_Date;
						$rootScope.viewLift.serviceEndDate=row.Service_End_Date;
						$rootScope.viewLift.dateOfInstallation=row.Installation_Date;
						$rootScope.viewLift.amcStartDate=row.Amc_Start_Date;
						$rootScope.viewLift.area=row.Area;
						$rootScope.viewLift.liftNumber=row.Lift_Number;
						$rootScope.viewLift.amcEndDate=row.Amc_End_Date;
						$rootScope.viewLift.amcType=row.amcTypeStr;
						$rootScope.viewLift.amcAmount=row.Amc_Amount;
						$rootScope.viewLift.pinCode=row.PinCode;						
						$rootScope.viewLift.latitude=row.Latitude;
						$rootScope.viewLift.longitude=row.Longitude;
						$rootScope.viewLift.noOfStops=row.NoOfStops;
						$rootScope.viewLift.machineMake=row.MachineMake;
						$rootScope.viewLift.machineCurrent=row.MachineCurrent;
						$rootScope.viewLift.machineCapacity=row.machineCapacity;
						$rootScope.viewLift.breakVoltage=row.BreakVoltage;
						$rootScope.viewLift.panelMake=row.PanelMake;
						$rootScope.viewLift.ard=row.ard;
						$rootScope.viewLift.noOfBatteries=row.noOfBatteries;
						$rootScope.viewLift.batteryCapacity=row.batteryCapacity;
						$rootScope.viewLift.batteryMake=row.batteryMake;
						$rootScope.viewLift.copMake=row.copMake;
						$rootScope.viewLift.lopMake=row.lopMake;
						$rootScope.viewLift.autoDoorMake=row.autoDoorMake;
						$rootScope.viewLift.fireMode=row.fireMode;
						$rootScope.viewLift.intercomm=row.intercomm;
						$rootScope.viewLift.alarm=row.alarm;
						$rootScope.viewLift.alarmBattery=row.alarmBattery;
						$rootScope.viewLift.accessControl=row.accessControl;
						$rootScope.viewLift.imei=row.imei;
						$rootScope.viewLift.liftType=row.LiftType;
						$rootScope.viewLift.lmsEventFromContactNo=row.lmsEventFromContactNo;
						
						$rootScope.viewLift.machinePhoto=row.machinePhoto;
						$rootScope.viewLift.panelPhoto=row.panelPhoto;
						$rootScope.viewLift.ardPhoto=row.ardPhoto;
						$rootScope.viewLift.lopPhoto=row.lopPhoto;
						$rootScope.viewLift.copPhoto=row.copPhoto;
						$rootScope.viewLift.cartopPhoto=row.cartopPhoto;
						$rootScope.viewLift.autoDoorHeaderPhoto=row.autoDoorHeaderPhoto;
						$rootScope.viewLift.wiringPhoto=row.wiringPhoto;
						$rootScope.viewLift.lobbyPhoto=row.lobbyPhoto;
						
						if(row.simplexDuplex=="1"){
			  	    		$rootScope.viewLift.simplexDuplex="Duplex";
			  	    	}else if(row.simplexDuplex=="2"){
			  	    		$rootScope.viewLift.simplexDuplex="Group";
			  	    	}else{
			  	    		$rootScope.viewLift.simplexDuplex="Simplex";
			  	    	}
						if(row.wiringShceme=="1"){
			  	    		$rootScope.viewLift.wiringShceme="NonPluggable";
			  	    	}else{
			  	    		$rootScope.viewLift.wiringShceme="Pluggable";
			  	    	}
						if(row.doorType=="1"){
			  	    		$rootScope.viewLift.doorType="Manual Door";
			  	    	}else{
			  	    		$rootScope.viewLift.doorType="Auto Door";
			  	    	}
						if(row.collectiveType=="1"){
			  	    		$rootScope.viewLift.collectiveType="Full Collective";
			  	    	}else{
			  	    		$rootScope.viewLift.collectiveType="Down Collective";
			  	    	}
						if(row.engineType=="1"){
			  	    		$rootScope.viewLift.engineType="Gearless";
			  	    	}else{
			  	    		$rootScope.viewLift.engineType="Geared";
			  	    	}
						
						window.location.hash = "#/view-lift";
					
				};
				
				  $rootScope.editLift={};
				  	$scope.selectedDoorType={};

						$scope.editLiftDetails=function(row){
							//serviceApi.doPostWithData('/RLMS/admin/getLiftById',row.liftId)
							//.then(function(data) {
								
								//$rootScope.editLift = data.response;
							
								$rootScope.editLift.liftId=row.liftId;
								$rootScope.editLift.address=row.Address.replace(/-/g, '');
								$rootScope.editLift.city=row.City.replace(/-/g, '');
								$rootScope.editLift.customerName=row.Customer_Name.replace(/-/g, '');
								$rootScope.editLift.branchName=row.Branch_Name.replace(/-/g, '');
								$rootScope.editLift.serviceStartDate=row.Service_Start_Date;
								$rootScope.editLift.serviceEndDate=row.Service_End_Date;
								$rootScope.editLift.dateOfInstallation=row.Installation_Date;
								$rootScope.editLift.amcStartDate=row.Amc_Start_Date;
								$rootScope.editLift.area=row.Area;
								$rootScope.editLift.liftNumber=row.Lift_Number;
								$rootScope.editLift.amcEndDate=row.Amc_End_Date;
								$rootScope.editLift.amcType=row.amcType;
								$rootScope.editLift.amcAmount=row.Amc_Amount;
								$rootScope.editLift.pinCode=row.PinCode;						
								$rootScope.editLift.latitude=row.Latitude;
								$rootScope.editLift.longitude=row.Longitude;
								$rootScope.editLift.noOfStops=row.NoOfStops;
								$rootScope.editLift.machineMake=row.MachineMake;
								$rootScope.editLift.machineCurrent=row.MachineCurrent;
								$rootScope.editLift.machineCapacity=row.machineCapacity;
								$rootScope.editLift.breakVoltage=row.BreakVoltage;
								$rootScope.editLift.panelMake=row.PanelMake;
								$rootScope.editLift.ard=row.ard;
								$rootScope.editLift.noOfBatteries=row.noOfBatteries;
								$rootScope.editLift.batteryCapacity=row.batteryCapacity;
								$rootScope.editLift.batteryMake=row.batteryMake;
								$rootScope.editLift.copMake=row.copMake;
								$rootScope.editLift.lopMake=row.lopMake;
								$rootScope.editLift.autoDoorMake=row.autoDoorMake;
								$rootScope.editLift.fireMode=row.fireMode;
								$rootScope.editLift.intercomm=row.intercomm;
								$rootScope.editLift.alarm=row.alarm;
								$rootScope.editLift.alarmBattery=row.alarmBattery;
								$rootScope.editLift.accessControl=row.accessControl;
								$rootScope.editLift.imei=row.imei;
								$rootScope.editLift.liftType=row.LiftType;
								$rootScope.editLift.lmsEventFromContactNo=row.lmsEventFromContactNo;
								$rootScope.editLift.doorType=row.doorType;
								$rootScope.editLift.engineType=row.engineType;
								$rootScope.editLift.wiringShceme=row.wiringShceme;
								$rootScope.editLift.collectiveType=row.collectiveType;
								$rootScope.editLift.simplexDuplex=row.simplexDuplex;
								
								$rootScope.editLift.machinePhoto=row.machinePhoto;
								$rootScope.editLift.panelPhoto=row.panelPhoto;
								$rootScope.editLift.ardPhoto=row.ardPhoto;
								$rootScope.editLift.lopPhoto=row.lopPhoto;
								$rootScope.editLift.copPhoto=row.copPhoto;
								$rootScope.editLift.cartopPhoto=row.cartopPhoto;
								$rootScope.editLift.autoDoorHeaderPhoto=row.autoDoorHeaderPhoto;
								$rootScope.editLift.wiringPhoto=row.wiringPhoto;
								$rootScope.editLift.lobbyPhoto=row.lobbyPhoto;
								
								//var technicianArray=$rootScope.techniciansForEditComplaints;
								
								window.location.hash = "#/edit-lift";
							//});	
						
										
						};
	}]);
})();
