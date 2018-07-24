(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('configurLiftController', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initConfigLift();
		$scope.changeTab = function(targetIndex){
			angular.element('#tab'+targetIndex).trigger('click');
		}
		//Time Picker Start
			$scope.mytime = new Date();
		
		    $scope.hstep = 1;
		    $scope.mstep = 15;
		    $scope.sstep = 1;
		
		    $scope.options = {
		      hstep: [1, 2, 3],
		      mstep: [1, 5, 10, 15, 25, 30],
		      sstep: [1, 2, 3]
		    };
		
		    $scope.ismeridian = false;
		    $scope.toggleMode = function() {
		      $scope.ismeridian = !$scope.ismeridian;
		    };
		
		    $scope.update = function() {
		      var d = new Date();
		      d.setHours(14);
		      d.setMinutes(0);
		      $scope.mytime = d;
		    };
		
		    $scope.changed = function() {
		    	$scope.r3RTCHours=$scope.mytime.getHours();
		    	$scope.r4RTCMinutes=$scope.mytime.getMinutes();
		    	$scope.r5RTCSeconds=$scope.mytime.getSeconds();
		    };
		    
		
		    $scope.clear = function() {
		      $scope.mytime = null;
		    };
		    $scope.changeSelection=function(selectedValue){
		    	if(selectedValue=='0'){
		    		$scope.manualModeSelected=true;
		    		$scope.autoDoorSensorModeSelected=false;
		    	}else if(selectedValue=='1' || selectedValue=='3'){
		    		$scope.autoDoorSensorModeSelected=true;
		    		$scope.manualModeSelected=false;
		    	}
		    };
		    var ConvertBase = function (num) {
		        return {
		            from : function (baseFrom) {
		                return {
		                    to : function (baseTo) {
		                        return parseInt(num, baseFrom).toString(baseTo);
		                    }
		                };
		            }
		        };
		    };
		        
		    // binary to decimal
		    var bin2dec = function (num) {
		        return ConvertBase(num).from(2).to(10);
		    };
		    $scope.stateChangedFlag2 = function (qId) {
		    	var binaryValues = ["0","0","0","0", "0","0", "0", "0"];
		    	if($scope.a.flag2.bit5){
		    		binaryValues[2]="1";
		    	}if($scope.a.flag2.bit4){
		    		binaryValues[3]="1";
		    	}if($scope.a.flag2.bit3){
		    		binaryValues[4]="1";
		    	}if($scope.a.flag2.bit2){
		    		binaryValues[5]="1";
		    	}if($scope.a.flag2.bit1){
		    		binaryValues[6]="1";
		    	}if($scope.a.flag2.bit0){
		    		binaryValues[7]="1";
		    	}
		    	var finalvalues="";
		    	for(var i=0;i<binaryValues.length;i++){
		    	finalvalues=finalvalues.concat(binaryValues[i]);
		    	}
		    	$scope.aflag2decimalvalue=bin2dec(finalvalues);
		    }
		    $scope.stateChangedFlag3 = function () {
		    	var binaryValues = ["0","0","0","0", "0","0", "0", "0"];
		    	if($scope.a.flag3.bit5){
		    		binaryValues[2]="1";
		    	}if($scope.a.flag3.bit4){
		    		binaryValues[3]="1";
		    	}if($scope.a.flag3.bit3){
		    		binaryValues[4]="1";
		    	}if($scope.a.flag3.bit2){
		    		binaryValues[5]="1";
		    	}if($scope.a.flag3.bit1){
		    		binaryValues[6]="1";
		    	}if($scope.a.flag3.bit0){
		    		binaryValues[7]="1";
		    	}
		    	var finalvalues="";
		    	for(var i=0;i<binaryValues.length;i++){
		    	finalvalues=finalvalues.concat(binaryValues[i]);
		    	}
		    	$scope.aflag3decimalvalue=bin2dec(finalvalues);
		    }
		    
		    $scope.stateChangedFlag4 = function () {
		    	var binaryValues = ["0","0","0","0", "0","0", "0", "0"];
		    	if($scope.a.flag4.bit5){
		    		binaryValues[2]="1";
		    	}if($scope.a.flag4.bit4){
		    		binaryValues[3]="1";
		    	}if($scope.a.flag4.bit3){
		    		binaryValues[4]="1";
		    	}if($scope.a.flag4.bit2){
		    		binaryValues[5]="1";
		    	}if($scope.a.flag4.bit1){
		    		binaryValues[6]="1";
		    	}if($scope.a.flag4.bit0){
		    		binaryValues[7]="1";
		    	}
		    	var finalvalues="";
		    	for(var i=0;i<binaryValues.length;i++){
		    	finalvalues=finalvalues.concat(binaryValues[i]);
		    	}
		    	$scope.aflag4decimalvalue=bin2dec(finalvalues);
		    }
		    $scope.floorArray=[];
		    $scope.changeStops=function(stops){
		    	$scope.floorArray=[];
		    	if(stops <= 23){
		    		for(var i=0;i<stops;i++){
		    			$scope.floorArray.push(i);
		    		}
		    	}
		    	console.log("Number of Stops changed",$scope.floorArray);
		    };
		    
		    
		    $scope.stateChangedmodeflag = function () {
		    	var binaryValues = ["0","0","0","0", "0","0", "0", "0"];
		    	if($scope.a.modeflag.bit5){
		    		binaryValues[2]="1";
		    	}if($scope.a.modeflag.bit4){
		    		binaryValues[3]="1";
		    	}if($scope.a.modeflag.bit3){
		    		binaryValues[4]="1";
		    	}if($scope.a.modeflag.bit2){
		    		binaryValues[5]="1";
		    	}if($scope.a.modeflag.bit1){
		    		binaryValues[6]="1";
		    	}if($scope.a.modeflag.bit0){
		    		binaryValues[7]="1";
		    	}
		    	var finalvalues="";
		    	for(var i=0;i<binaryValues.length;i++){
		    	finalvalues=finalvalues.concat(binaryValues[i]);
		    	}
		    	$scope.modeflagdecimalvalue=bin2dec(finalvalues);
		    }
		//Time Picker End
		function initConfigLift(){
			$scope.a={};
			$scope.bd={};
			$scope.p={};
			$scope.rtc={};
			$scope.a.homeLandingFloorNumber = false;
			$scope.a.disableHomelanding = false;
			$scope.a.homeLandingTime = '';
			$scope.a.fireModeFloorSelection = "";
			$scope.a.Date='';
			$scope.a.flag2 = {
						bit0 : 0,
						bit1 : 0,
						bit2 : 0,
						bit3 : 0,
						bit4 : 0,
						bit5 : 0
			}
			
			 
			$scope.a.flag3 = {
						bit0 : 0,
						bit1 : 0,
						bit2 : 0,
						bit3 : 0,
						bit4 : 0,
						bit5 : 0
			}
						$scope.a.flag4 = {
						bit0 : 0,
						bit1 : 0,
						bit2 : 0,
						bit3 : 0,
						bit4 : 0,
						bit5 : 0
			}
			
			
			$scope.$watch("a.disableHomelanding",
                    function handleChange( newValue, oldValue ) {
                    	if(newValue == true){
                    		$scope.a.homeLandingFloorNumber = 99;
                    	}else{
                    		$scope.a.homeLandingFloorNumber = 0;
                    	}
                    }
                );
            $scope.openFlag={
					Date:false,
			}
			$scope.open = function($event,which) {
			      $event.preventDefault();
			      $event.stopPropagation();
			      if($scope.openFlag[which] != true)
			    	  $scope.openFlag[which] = true;
			      else
			    	  $scope.openFlag[which] = false;
			  }
            $scope.readDate=function(date){
            	$scope.r0RTCDate=date.getDate()
            	$scope.r1RTCMonth=date.getMonth()+1;
            	$scope.r2RTCYear=date.getFullYear();
            	console.log("Selected date is-->"+date);
            }
            if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.rtcOptionsForInditechAdmin= true;
            }
            $scope.isUPSConnected=true;
		}
		
	}]);
})();
