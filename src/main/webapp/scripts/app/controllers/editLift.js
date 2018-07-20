(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editLiftCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','pinesNotifications','$rootScope','$modal', function($scope, $filter,serviceApi,$route,$http,utility,$window,pinesNotifications,$rootScope,$modal) {
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
					$scope.editLift.machinePhoto=$scope.currentFile;
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
		    
		    
		$scope.alert = { type: 'success', msg: 'You successfully Added Lift.',close:true };
		$scope.alert = { type: 'error', msg: '',close:false };
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
						name:'Commercial'
					},
					{
						id:2,
						name:'Bunglow'
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
					name:'NonPluggable'
				}
			];
			$scope.addLift={
					fyaTranId:null,
					liftType:0,
					branchCustomerMapId :'',
					liftNumber : '',
					customerName:'',
					branchName:'',
					companyName:'',
					liftId:'',
					address : '',
					city:'',
					rea:'',
					pinCode:'',
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

					doorType : 0,
					noOfStops :'',
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
					alarm : 0,
					alarmBattery : '',
					accessControl : '',
					activeFlag:'',
					imei :'',
					lmsEventFromContactNo:'',
					liftCustomerMapId:null,
					//status:'',
					isBlank:false,
					photoType:'',
					totalLiftCountForCustomer:0,
					
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
/*		function parseBase64(){
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
		}*/
		$scope.submitEditLift = function(){
			var liftData={};
			liftData={
									
					liftNumber: "739",
					address: $rootScope.editLift.address,
					customerName: "nivi",
					branchName: "Karnataka",
					companyName: null,
					city: "pune",
					area: "pune",
					pinCode: 41107,
					companyId: null,
					branchCompanyMapId: null,
					branchCustomerMapId: null,
					liftId: 180,
					latitude: "18.5580",
					longitude: "73.8075",
					serviceStartDate: 1531852200000,
					serviceStartDateStr: "18-Jul-2018",
					serviceEndDate: 1537122600000,
					serviceEndDateStr: "17-Sep-2018",
					dateOfInstallation: 1530383400000,
					dateOfInstallationStr: "01-Jul-2018",
					amcStartDate: 1530383400000,
					amcEndDate: null,
					amcStartDateStr: "01-Jul-2018",
					amcEndDateStr: "30-Jul-2019",
					amcType: 43,
					amcTypeStr: "NonComprehensive",
					amcAmount: "250000",
					doorType: 0,
					noOfStops: "12",
					engineType: 1,
					machineMake: "",
					machineCapacity: "",
					machineCurrent: "",
					machinePhoto: "",
					breakVoltage: "",
					panelMake: "",
					panelPhoto: "",
					ard: "",
					ardPhoto: "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",
					noOfBatteries: 0,
					batteryCapacity: "",
					batteryMake: "",
					copMake: "",
					copPhoto: "",
					lopMake: "",
					lopPhoto: "/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAABGAAD/4QNvaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjAtYzA2MSA2NC4xNDA5NDksIDIwMTAvMTIvMDctMTA6NTc6MDEgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6QjAzQUI3OEJCMzY2RTIxMTk4RTg5MDU2QUYzQTAwQUEiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RThENzFBRTc2OTE0MTFFMjgzQTdBODI4QUJBMTNDMzIiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RThENzFBRTY2OTE0MTFFMjgzQTdBODI4QUJBMTNDMzIiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNS4xIFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoxMzUzMzg1MUJFNjZFMjExOThFODkwNTZBRjNBMDBBQSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpCMDNBQjc4QkIzNjZFMjExOThFODkwNTZBRjNBMDBBQSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6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",
					collectiveType: 0,
					simplexDuplex: 0,
					cartopPhoto: "",
					autoDoorMake: "",
					autoDoorHeaderPhoto: "",
					wiringShceme: 0,
					wiringPhoto: "",
					fireMode: 0,
					intercomm: "",
					alarm: 0,
					alarmBattery: "",
					accessControl: "",
					lobbyPhoto: "",
					fyaTranId: null,
					liftCustomerMapId: null,
					photoType: null,
					liftType: 2,
					activeFlag: null,
					imei: "6253689",
					lmsEventFromContactNo: "9788989",
					totalLiftCountForCustomer: null,
					blank: false
				/*	liftId:$scope.editLift.liftId,
					address:$scope.editLift.address,
					liftType:$scope.editLift.liftType,
					liftNumber :$scope.editLift.liftNumber ,
					city:$scope.editLift.city,
					area:$scope.editLift.area,
					pinCode:$scope.editLift.pinCode,
					latitude : $scope.editLift.latitude,
					longitude : $scope.editLift.longitude,
					serviceStartDateStr : $scope.editLift.serviceStartDate,
					serviceEndDateStr : $scope.editLift.serviceEndDate,
					dateOfInstallationStr: $scope.editLift.dateOfInstallation,
					amcStartDateStr : $scope.editLift.amcStartDate,
					amcEndDateStr :$scope.editLift.amcEndDate,
					amcType :$scope.editLift.amcType,
					amcAmount : $scope.editLift.amcAmount,

					doorType :$scope.editLift.doorType,
					noOfStops : $scope.editLift.noOfStops,
					engineType : $scope.editLift.engineType,
					machineMake : $scope.editLift.machineMake,
					machineCapacity : $scope.editLift.machineCapacity,
					machineCurrent : $scope.editLift.machineCurrent,
					breakVoltage : $scope.editLift.breakVoltage,
					panelMake : $scope.editLift.panelMake,
					ard : $scope.editLift.ard,
					noOfBatteries :$scope.editLift.noOfBatteries,
					batteryCapacity : $scope.editLift.batteryCapacity,
					batteryMake : $scope.editLift.batteryMake,
					copMake : $scope.editLift.copMake,
					lopMake :$scope.editLift.lopMake,
					collectiveType :$scope.editLift.collectiveType,
					simplexDuplex : $scope.editLift.simplexDuplex,
					autoDoorMake :$scope.editLift.autoDoorMake,
					wiringShceme : $scope.editLift.wiringShceme,
					fireMode :$scope.editLift.fireMode,
					intercomm : $scope.editLift.intercomm,
					alarm : $scope.editLift.alarm,
					alarmBattery : $scope.editLift.alarmBattery,
					accessControl :$scope.editLift.accessControl,
					imei :$scope.editLift.imei,
					liftType :$scope.editLift.liftType,
					lmsEventFromContactNo:$scope.editLift.lmsEventFromContactNo,
					amcType:$scope.selectedAMCType.selected.id,
					engineType:$scope.selectedEngineMachineType.selected.id,
					wiringShceme:$scope.selectedWiringScheme.selected.id,
					collectiveType:$scope.selectedCollectiveType.selected.id,
					simplexDuplex:$scope.selectedSimplexDuplex.selected.id,
					doorType:$scope.selectedDoorType.selected.id,
					
					machinePhoto : $scope.editLift.machinePhoto.base64,
					panelPhoto : $scope.editLift.panelPhoto.base64,
					ardPhoto : $scope.editLift.ardPhoto.base64,
					lopPhoto :$scope.editLift.lopPhoto.base64,
					copPhoto : $scope.editLift.copPhoto.base64,
					cartopPhoto :$scope.editLift.cartopPhoto.base64,
					autoDoorHeaderPhoto :$scope.editLift.autoDoorHeaderPhoto.base64,
					wiringPhoto :$scope.editLift.wiringPhoto.base64,
					lobbyPhoto : $scope.editLift.    t            	! .base64,
					//status:'',
					//activeFlag:
					amcTypeStr:'',
					dateOfInstallation:'',
					serviceStartDate:'',
					serviceEndDate:'',
					amcStartDate:'',
					amcEndDate:'',
						liftCustomerMapId:null,
						//status:'',
						isBlank:false,
						photoType:'',
						totalLiftCountForCustomer:0,
					*/
			}
			/*parseBase64();
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
			}*/
			//$scope.addLift.liftTypeName=$scope.addLift.liftTypeName;
			//$scope.addLift.branchCustomerMapId = $scope.selectedCustomer.selected.branchCustomerMapId
			serviceApi.doPostWithData("/RLMS/admin/lift/updateLiftParams",liftData)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				initAddCustomer();
				$scope.editLiftForm.$setPristine();
				$scope.editLiftForm.$setUntouched();
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		
		/*$scope.submitAddLift = function(){
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
			//$scope.addLift.liftTypeName=$scope.addLift.liftTypeName;
			$scope.addLift.branchCustomerMapId = $scope.selectedCustomer.selected.branchCustomerMapId
			serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewLift",$scope.addLift)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				if(successMessage!=false){
				$scope.alert.msg = response[key[1]];
				$scope.alert.type = "success";
				resetAddLift();
				//initAddLift();
				$scope.addLiftForm.$setPristine();
				$scope.addLiftForm.$setUntouched();
		     	}
				else{
					$scope.showAlert = true;
					$scope.alert.msg =  response[key[1]];
					$scope.alert.type="danger";
				}
				$scope.alert.type=' ';
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}*/
		//reset edit branch
		$scope.resetEditLift = function(){
			$scope.showAlert = false;
			initAddLift();
			$scope.addLiftForm.$setPristine();
			$scope.addLiftForm.$setUntouched();
		}
		
	}]);
})();
