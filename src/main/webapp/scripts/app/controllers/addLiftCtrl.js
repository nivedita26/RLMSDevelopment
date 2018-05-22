(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addUserCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','pinesNotifications','$rootScope','$modal', function($scope, $filter,serviceApi,$route,$http,utility,$window,pinesNotifications,$rootScope,$modal) {
		//initialize add Branch
		initAddLift();
		$scope.displayMachinePhoto=false;
		$scope.displayPanelPhoto=false;
		$scope.displayArdPhoto=false;
		$scope.displayLopPhoto=false;
		$scope.displayCopPhoto=false;
		$scope.displayCarTopPhoto=false;
		$scope.displayHeaderPhoto=false;
		$scope.displayWiringPhoto=false;
		$scope.displayLobbyPhoto=false;
		
		
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
		
		$scope.loadSelectedLiftTypeInfo = function(liftTypeId){
			var dataToSend ={
					liftType:liftTypeId,
					branchCustomerMapId:$scope.selectedCustomer.selected.branchCustomerMapId
			}
			$scope.addLift.liftType=liftTypeId;
			$scope.modalInstance.dismiss('cancel');
			serviceApi.doPostWithData('/RLMS/admin/getLiftMasterForType',dataToSend)
	         .then(function(liftdata) {
	        	 if(liftdata.blank != true){
	        		 for(var key in liftdata) {
	        		        if(typeof liftdata[key] !== 'undefined' && typeof liftdata[key] !== 'null') {
	        		        	if(key == "amcType"){
	        		        		$scope.selectedAMCType.id = liftdata[key];
	        		        	}else if(key == "doorType"){
	        		        		$scope.selectedAMCType.id =liftdata[key];
	        		        	}else if(key == "engineType"){
	        		        		$scope.selectedEngineMachineType.id =utility.parseInteger(liftdata[key]);
	        		        	}else if(key == "collectiveType"){
	        		        		$scope.selectedCollectiveType.id = utility.parseInteger(liftdata[key]);
	        		        	}else if(key == "simplexDuplex"){
	        		        		$scope.selectedSimplexDuplex.id = utility.parseInteger(liftdata[key]);
	        		        	}else if(key == "wiringShceme"){
	        		        		$scope.selectedWiringScheme.id = utility.parseInteger(liftdata[key]);
	        		        	}else{
	        		        		$scope.addLift[key] = utility.parseInteger(liftdata[key]);
	        		        	}
	        		            
	        		        }
	        		    }
	        	 }
	        	$scope.addLift["address"] = liftdata['address'];
	        	$scope.addLift["city"] = liftdata['city'];
	        	$scope.addLift["area"] = liftdata['area'];
	        	$scope.addLift["pinCode"] = liftdata['pinCode'];
	        		 //$scope.addLift = liftdata;
	         })
		}
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
		$scope.alert = { type: 'success', msg: 'You successfully Added Lift.',close:true };
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
			$scope.selectedEngineMachineType={};
			$scope.selectedCollectiveType={};
			$scope.selectedSimplexDuplex={};
			$scope.selectedWiringScheme={};
			$scope.customerType=[
					{
						id:0,
						name:'Residential'
					},
					{
						id:1,
						name:'Commertial'
					},
					{
						id:2,
						name:'Bunglo'
					},
					{
						id:3,
						name:'Hospital'
					},
					{
						id:4,
						name:'Goods'
					},
					{
						id:5,
						name:"Dumb Waiter"
					}
			];
			//AMC Type
			$scope.AMCType=[
				{
					id:42,
					name:'Comprehensive'
				},
				{
					id:43,
					name:'Non-Comprehensive'
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
			$scope.DoorType=[
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
			$scope.EngineMachineType=[
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
			$scope.CollectiveType=[
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
			$scope.SimplexDuplex=[
				{
					id:0,
					name:'Simplex'
				},
				{
					id:1,
					name:'Duplex'
				},
				{
					id:1,
					name:'Group'
				}
			];
			//WiringScheme
			$scope.WiringScheme=[
				{
					id:0,
					name:'Pluggable'
				},
				{
					id:1,
					name:'NonPluggabel'
				}
			];
			$scope.addLift={
					liftType:'',
					branchCustomerMapId :'',
					liftNumber : '',
					address : '',
					city:'',
					area:'',
					pinCode:'',
					latitude : '',
					longitude : '',
					serviceStartDate : '',
					serviceEndDate : '',
					dateOfInstallation : '',
					amcStartDate : '',
					amcEndDate:'',
					amcType : 0,
					amcAmount : 0,

					doorType : 0,
					noOfStops : 0,
					engineType : 0,
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
					collectiveType : 0,
					simplexDuplex : 0,
					autoDoorMake : '',
					wiringShceme : 0,
					fireMode : 0,
					intercomm : '',
					alarm : '',
					alarmBattery : '',
					accessControl : '',
					imei :'',
					lmsEventFromContactNo:'',
					
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
			//$scope.addLift.serviceStartDate = new Date();
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
		function parseBase64(){
			if($scope.addLift.machinePhoto != ''){
				$scope.addLift.machinePhoto = $scope.addLift.machinePhoto.base64;
			}
			if($scope.addLift.panelPhoto != ''){
				$scope.addLift.panelPhoto = $scope.addLift.panelPhoto.base64;
			}
			if($scope.addLift.ardPhoto != ''){
				$scope.addLift.ardPhoto = $scope.addLift.ardPhoto.base64;
			}
			if($scope.addLift.lopPhoto != ''){
				$scope.addLift.lopPhoto = $scope.addLift.lopPhoto.base64;
			}
			if($scope.addLift.copPhoto != ''){
				$scope.addLift.copPhoto = $scope.addLift.copPhoto.base64;
			}
			if($scope.addLift.cartopPhoto != ''){
				$scope.addLift.cartopPhoto = $scope.addLift.cartopPhoto.base64;
			}
			if($scope.addLift.autoDoorHeaderPhoto != ''){
				$scope.addLift.autoDoorHeaderPhoto = $scope.addLift.autoDoorHeaderPhoto.base64;
			}
			if($scope.addLift.wiringPhoto != ''){
				$scope.addLift.wiringPhoto = $scope.addLift.wiringPhoto.base64;
			}
			if($scope.addLift.lobbyPhoto != ''){
				$scope.addLift.lobbyPhoto = $scope.addLift.lobbyPhoto.base64;
			}
		}
		$scope.submitAddLift = function(){
			parseBase64();
			//addLift.customerType = $scope.selectedCustomerType;
			
        if($scope.selectedAMCType.selected){
	      $scope.addLift.amcType = $scope.selectedAMCType.selected.id;
       }
	    	if($scope.selectedAMCType.selected){
	    			$scope.addLift.amcType = $scope.selectedAMCType.selected.id;
		}
			if($scope.selectedDoorType.selected){
				$scope.addLift.doorType = $scope.selectedDoorType.selected.id;
			}
			if($scope.selectedEngineMachineType.selected){
				$scope.addLift.engineType = $scope.selectedEngineMachineType.selected.id;
			}
			if($scope.selectedCollectiveType.selected){
				$scope.addLift.collectiveType = $scope.selectedCollectiveType.selected.id;
		    }
		   if($scope.selectedSimplexDuplex.selected){
			   $scope.addLift.simplexDuplex = $scope.selectedSimplexDuplex.selected.id;
		   }
		   if($scope.selectedWiringScheme.selected){
			$scope.addLift.wiringShceme = $scope.selectedWiringScheme.selected.id;
		   }
			
			if($scope.addLift.fireMode){
				$scope.addLift.fireMode = 1;
			}else{
				$scope.addLift.fireMode = 0;
			}
			$scope.addLift.branchCustomerMapId = $scope.selectedCustomer.selected.branchCustomerMapId
			serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewLift",$scope.addLift)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				initAddLift();
				$scope.addLiftForm.$setPristine();
				$scope.addLiftForm.$setUntouched();
				
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		$scope.showWizardFun = function(){
			$scope.modalInstance = $modal.open({
		        templateUrl: 'selectLiftType',
		        scope:$scope,
		        size:"sm"
		     });
//			var dataToSend = {
//					branchCustomerMapId:$scope.selectedCustomer.selected.branchCustomerMapId
//			}
//			serviceApi.doPostWithData('/RLMS/admin/getAddressDetailsOfLift',dataToSend)
//	         .then(function(liftdata) {
//	        	 $scope.addLift.address = liftdata.address;
//	        	 $scope.addLift.area = liftdata.area;
//	        	 $scope.addLift.city = liftdata.city;
//	        	 $scope.addLift.pinCode = liftdata.pinCode;
//	         })
			$scope.showWizard = true;
		}
		//rese add branch
		$scope.resetAddLift = function(){
			$scope.showAlert = false;
			initAddLift();
			$scope.addLiftForm.$setPristine();
			$scope.addLiftForm.$setUntouched();
		}
		
	}]);
})();
