(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editLiftCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','pinesNotifications','$rootScope','$modal', function($scope, $filter,serviceApi,$route,$http,utility,$window,pinesNotifications,$rootScope,$modal) {
		//initialize add Branch
		initAddLift();
		$scope.alert = { type: 'success', msg: 'You successfully Edited Lift.',close:true };
		$scope.alert = { type: 'error', msg: '',close:false };
		/*$scope.displayMachinePhoto=false;
		$scope.displayPanelPhoto=false;
		$scope.displayArdPhoto=false;
		$scope.displayLopPhoto=false;
		$scope.displayCopPhoto=false;
		$scope.displayCarTopPhoto=false;
		$scope.displayHeaderPhoto=false;
		$scope.displayWiringPhoto=false;
		$scope.displayLobbyPhoto=false;*/
		
		//show popup for selecting lift
		$scope.setMachinePhoto=function(element,photoType){
			$scope.currentFile=element.files[0];
			var reader=new FileReader();
			reader.onload=function(event){
				if(photoType==="Machine"){
					$scope.image_source=event.target.result;
					$scope.displayMachinePhoto=true;
					$scope.addLift.machinePhoto=$scope.currentFile;
				}
				if(photoType==="Panel"){
					$scope.image_source1=event.target.result;
					$scope.displayPanelPhoto=true;
					$scope.addLift.panelPhoto=$scope.currentFile;
				}
				if(photoType==="ARD"){
					$scope.image_source2=event.target.result;
					$scope.displayArdPhoto=true;
					$scope.addLift.ardPhoto=$scope.currentFile;
				}
				if(photoType==="LOP"){
					$scope.image_source3=event.target.result;
					$scope.displayLopPhoto=true;
					$scope.addLift.lopPhoto=$scope.currentFile;
				}
				if(photoType==="COP"){
					$scope.image_source4=event.target.result;
					$scope.displayCopPhoto=true;
					$scope.addLift.copPhoto=$scope.currentFile;
				}
				if(photoType==="CarTop"){
					$scope.image_source5=event.target.result;
					$scope.displayCarTopPhoto=true;
					$scope.addLift.cartopPhoto=$scope.currentFile;
				}
				if(photoType==="Header"){
					$scope.image_source6=event.target.result;
					$scope.displayHeaderPhoto=true;
					$scope.addLift.autoDoorHeaderPhoto=$scope.currentFile;
				}
				if(photoType==="Wiring"){
					$scope.image_source7=event.target.result;
					$scope.displayWiringPhoto=true;
					$scope.addLift.wiringPhoto=$scope.currentFile;
				}
				if(photoType==="Lobby"){
					$scope.image_source8=event.target.result;
					$scope.displayLobbyPhoto=true;
					$scope.addLift.lobbyPhoto=$scope.currentFile;
				}
				$scope.apply();
			};
			reader.readAsDataURL(element.files[0]);
		};
		
		// Date Picker
		$scope.today = function() {
		      $scope.dt = new Date();
		    };
		    $scope.today();

		    $scope.clear = function() {
		      $scope.dt = null;
		    };

		    // Disable weekend selection
		    $scope.disabled = function(date, mode) {
		      return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
		    };
		    
		    var warrantyPeriod= $scope.warrantyPeriod;
		    //var serviceEndDate =$scope.serviceStartDate
		    $scope.toggleMin = function() {
		      $scope.minDate = $scope.minDate ? null : new Date();
		    };
		    $scope.toggleMin();
		    $scope.dateOptions = {
		      formatYear: 'yy',
		      startingDay: 1,
		    };

		    $scope.initDate = new Date('2016-15-20');
		    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
		    $scope.format = $scope.formats[0];
		    
		    
	    //Date Picker End
		    
		$scope.showAlert = false;
		$scope.showCompany = false;
		$scope.showBranch = false;
		
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
		    	
		    });
		}
		$scope.loadCustomerData = function(){
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
  	    	serviceApi.doPostWithData('/RLMS/admin/getAllCustomersForBranch',branchData)
 	         .then(function(customerData) {
 	        	 $scope.cutomers = customerData;
 	         })
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
		}
	  	
		function initAddLift(){
			$scope.selectedCompany={};
			$scope.selectedBranch = {};
			$scope.selectedCustomer = {};
			$scope.selectedCustomerType={};
			$scope.selectedAMCType={};
			$scope.selectedDoorType={};
			$scope.selectedFireMode={};
			$scope.selectedEngineMachineType={};
			$scope.selectedCollectiveType={};
			$scope.selectedSimplexDuplex={};
			$scope.selectedWiringScheme={};

			$scope.addLift={
					customerName:'',
					fyaTranId:null,
					liftType:0,
					branchCustomerMapId :'',
					liftNumber : '',
					customerName:'',
					branchName:'',
					companyName:'',
					liftId:0,
					address : '',
					city:'',
					area:'',
					pinCode:0,
					latitude : '',
					longitude : '',
					serviceStartDate : '',
					serviceEndDate : '',
					serviceEndDateStr:'',
					serviceStartDateStr:'',
					dateOfInstallation : '',
					dateOfInstallationStr:'',
					amcStartDateStr:'',
					amcEndDateStr:'',
					amcStartDate : '',
					amcEndDate:'',
					amcType : 0,
					amcAmount : '',
					amcTypeStr : '',

					doorType : 0,
					noOfStops :'',
					engineType : '',
					machineMake : '',
					machineCapacity : '',
					machineCurrent : '',
					breakVoltage : '',
					panelMake : '',
					ard : '',
					noOfBatteries :0,
					batteryCapacity : '',
					batteryMake : '',
					copMake : '',
					lopMake : '',
					collectiveType : '',
					simplexDuplex : '',
					autoDoorMake : '',
					wiringShceme : '',
					fireMode : 0,
					intercomm : '',
					alarm : 0,
					alarmBattery : '',
					accessControl : '',
					activeFlag:0,
					imei :'',
					lmsEventFromContactNo:'',
					liftCustomerMapId:null,
					//status:'',
					isBlank:false,
					photoType:'',
					totalLiftCountForCustomer:null,
					
					machinePhoto : '',
					panelPhoto : '',
					ardPhoto : '',
					lopPhoto : '',
					copPhoto : '',
					cartopPhoto : '',
					autoDoorHeaderPhoto : '',
					wiringPhoto : '',
					lobbyPhoto	 : '',
			};
			$scope.showWizard = false;
			
		}
		$scope.openFlag={
				serviceStartDate:false,
				serviceEndDate:false,
				dateOfInstallation :false,
				amcStartDate :false,
				amcEndDate:false,
		}
		$scope.open = function($event,which) {
		      $event.preventDefault();
		      $event.stopPropagation();
		      if($scope.openFlag[which] != true)
		    	  $scope.openFlag[which] = true;	    
		      else
		    	  $scope.openFlag[which] = false;
		    };
		  

		    //load compay dropdown data
		//Post call add branch
		$scope.submitEditLift = function(){
			if($scope.editLift.fireMode=== "false"){
				$scope.editLift.fireMode=0
			}else{
				$scope.editLift.fireMode=1
			}
			
			var liftData={};
			liftData={									
					customerName: '',
					branchName: '',					
					liftNumber: $scope.editLift.liftNumber,
					address:$scope.editLift.address,
					companyName: null,
					city: $scope.editLift.city,
					area: $scope.editLift.area,
					pinCode: $scope.editLift.pinCode,
					companyId: null,
					//branchCompanyMapId: null,
					//branchCustomerMapId: null,
					liftId:$scope.editLift.liftId,
					latitude:$scope.editLift.latitude,
					longitude: $scope.editLift.longitude,
					serviceStartDateStr: $scope.editLift.serviceStartDate,
					serviceStartDate:null,
					serviceEndDateStr:$scope.editLift.serviceEndDate,
					serviceEndDate: null,
					dateOfInstallationStr:$scope.editLift.dateOfInstallation,
					dateOfInstallation: null,
					amcStartDateStr: $scope.editLift.amcStartDate,
					amcEndDateStr: $scope.editLift.amcEndDate,
					amcStartDate: null,
					amcEndDate: null,
					amcType: $scope.editLift.amcType,
					//amcType: $scope.selectedAMCType.selected.id,
					//amcTypeStr:  $scope.selectedAMCType.selected.name,
					amcTypeStr:  $scope.editLift.amcTypeStr,
					amcAmount: $scope.editLift.amcAmount,
					//doorType: $scope.selectedDoorType.selected.id,
					doorType: $scope.editLift.doorType,
					noOfStops: $scope.editLift.noOfStops,
					engineType: $scope.editLift.engineType,
					machineMake: $scope.editLift.machineMake,
					machineCapacity:$scope.editLift.machineCapacity,
					machineCurrent:$scope.editLift.machineCurrent,
					machinePhoto:$scope.editLift.machinePhoto.base64,
					breakVoltage: $scope.editLift.breakVoltage,
					panelMake:$scope.editLift.panelMake,
					panelPhoto: $scope.editLift.panelPhoto.base64,
					ard: $scope.editLift.ard,
					ardPhoto:$scope.editLift.ardPhoto.base64,
					noOfBatteries:$scope.editLift.noOfBatteries,
					batteryCapacity:$scope.editLift.batteryCapacity,
					batteryMake: $scope.editLift.batteryMake,
					copMake: $scope.editLift.copMake,
					copPhoto: $scope.editLift.copPhoto.base64,
					lopMake:$scope.editLift.lopMake,
					lopPhoto:$scope.editLift.lopPhoto.base64,
					//collectiveType: $scope.selectedCollectiveType.selected.id,
					collectiveType: $scope.editLift.collectiveType,
					//simplexDuplex: $scope.selectedSimplexDuplex.selected.id,
					simplexDuplex: $scope.editLift.simplexDuplex,
					cartopPhoto: $scope.editLift.cartopPhoto.base64,
					autoDoorMake: $scope.editLift.autoDoorMake,
					autoDoorHeaderPhoto: $scope.editLift.autoDoorHeaderPhoto.base64,
					wiringShceme: $scope.editLift.wiringShceme,
					wiringPhoto: $scope.editLift.wiringPhoto.base64,
					fireMode: $scope.editLift.fireMode,
					intercomm: $scope.editLift.intercomm,
					alarm: $scope.editLift.alarm,
					alarmBattery:  $scope.editLift.alarmBattery,
					accessControl:$scope.editLift.accessControl,
					lobbyPhoto:$scope.editLift.lobbyPhoto.base64,
					fyaTranId: null,
					liftCustomerMapId: null,
					photoType: null,
					liftType: $scope.editLift.liftType,
					activeFlag: null,
					imei:$scope.editLift.imei,
					lmsEventFromContactNo: $scope.editLift.lmsEventFromContactNo,
					totalLiftCountForCustomer: null,
					blank: false
			
			}
			
		/*	parseBase64();
			//addLift.customerType = $scope.selectedCustomerType;
			
	    	if($scope.selectedAMCType.selected){
	    			$scope.editLift.amcType = $scope.selectedAMCType.selected.id;
		}
			if($scope.selectedDoorType.selected){
				$scope.editLift.doorType = $scope.selectedDoorType.selected.id;
			}
			if($scope.selectedEngineMachineType.selected){
				$scope.editLift.engineType = $scope.selectedEngineMachineType.selected.id;
			}
			if($scope.selectedCollectiveType.selected){
				$scope.editLift.collectiveType = $scope.selectedCollectiveType.selected.id;
		    }
		   if($scope.selectedSimplexDuplex.selected){
			   $scope.editLift.simplexDuplex = $scope.selectedSimplexDuplex.selected.id;
		   }
		   if($scope.selectedWiringScheme.selected){
			$scope.editLift.wiringShceme = $scope.selectedWiringScheme.selected.id;
		   }
			
			if($scope.addLift.fireMode){
				$scope.addLift.fireMode = 1;
			}else{
				$scope.addLift.fireMode = 0;
			}*/
			//$scope.addLift.liftTypeName=$scope.addLift.liftTypeName;
			//$scope.addLift.branchCustomerMapId = $scope.selectedCustomer.selected.branchCustomerMapId
			
			serviceApi.doPostWithData("/RLMS/admin/lift/updateLiftParams",liftData)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				if(successMessage){
				$scope.alert.msg = response[key[1]];
				$scope.alert.type = "success";
				//resetAddLift();
				initAddLift();
				$scope.editLiftForm.$setPristine();
				$scope.editLiftForm.$setUntouched();
		     	}
				else{
					$scope.showAlert = true;
					$scope.alert.msg =  response[key[1]];
					$scope.alert.type="danger";
				}
				//$scope.alert.type=' ';
			},function(error){
				$scope.showAlert = false;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		
		//reset edit branch
		$scope.resetEditLift = function(){
			$scope.showAlert = false;
			initAddLift();
			$scope.editLiftForm.$setPristine();
			$scope.editLiftForm.$setUntouched();
		}
		
	}]);
})();
