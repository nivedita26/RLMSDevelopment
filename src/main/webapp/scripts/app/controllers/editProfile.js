(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editProfileCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','pinesNotifications','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$window,pinesNotifications,$rootScope) {
		//initialize add Branch
		initEditUser();
		loadCompayInfo();
		$scope.backPage = function(){
			$window.history.back();
		}
		//$scope.alert = { type: 'success', msg: 'You successfully edited user.',close:true };
		//$scope.showAlert = false;
		function initEditUser(){
			$scope.selectedCompany = {};
			$scope.addUser={
				firstName:'',
				lastName:'',
				address:'',
				contactNumber:'',
				emailId:'',
				companyId:'',
				city:'',
				area:'',
				pinCode:''
			};	
		    //$scope.companies = [];
		    $scope.userList={};
		}
		
		$scope.alert = { type: 'success', msg: 'You successfully edited Profile.',close:true };
		$scope.showAlert = false;
		
		//load company dropdown data
		function loadCompayInfo(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		};
		
		
	  	        	serviceApi.doPostWithData('/RLMS/getLoggedInUser')
	  	        	.then(function(largeLoad) {
	  	        		 $scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  //for(var i=0;i==largeLoad.length;i++){
	  	        	  if(largeLoad){
		  	        	var userDetailsObj={};
		  	        	if(!!largeLoad.userId){
	  	        			userDetailsObj["Id"] =largeLoad.userId;
	  	        		}else{
	  	        			userDetailsObj["Id"] =" - ";
	  	        		}
		  	        	if(!!largeLoad.userRole.rlmsUserMaster.username){
	  	        			userDetailsObj["username"] =largeLoad.userRole.rlmsUserMaster.username;
	  	        		}else{
	  	        			userDetailsObj["username"] =" - ";
	  	        		}
		  	        	if(!!largeLoad.userRole.rlmsUserMaster.firstName){
	  	        			userDetailsObj["firstName"] =largeLoad.userRole.rlmsUserMaster.firstName;
	  	        		}else{
	  	        			userDetailsObj["firstName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad.userRole.rlmsUserMaster.lastName){
	  	        			userDetailsObj["lastName"] =largeLoad.userRole.rlmsUserMaster.lastName;
	  	        		}else{
	  	        			userDetailsObj["lastName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad.userRole.rlmsUserMaster.address){
	  	        			userDetailsObj["address"] =largeLoad.userRole.rlmsUserMaster.address;
	  	        		}else{
	  	        			userDetailsObj["address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad.userRole.rlmsUserMaster.contactNumber){
	  	        			userDetailsObj["Contact_Number"] =largeLoad.userRole.rlmsUserMaster.contactNumber;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad.userRole.rlmsUserMaster.area){
	  	        			userDetailsObj["Area"] =largeLoad.userRole.rlmsUserMaster.area;
	  	        		}else{
	  	        			userDetailsObj["Area"] =" - ";
	  	        		}
	  	        		if(!!largeLoad.userRole.rlmsUserMaster.city){
	  	        			userDetailsObj["City"] =largeLoad.userRole.rlmsUserMaster.city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad.userRole.rlmsUserMaster.pincode){
	  	        			userDetailsObj["PinCode"] =largeLoad.userRole.rlmsUserMaster.pincode;
	  	        		}else{
	  	        			userDetailsObj["PinCode"] =" - ";
	  	        		}
	  	        		
	  	        		if(!!largeLoad.userRole.role){
	  	        			userDetailsObj["Role"] =largeLoad.userRole.role;
	  	        		}else{
	  	        			userDetailsObj["Role"] =" - ";
	  	        		}
	  	        		if(!!largeLoad.userRole.rlmsUserMaster.emailId){
	  	        			userDetailsObj["Email_Id"] =largeLoad.userRole.rlmsUserMaster.emailId;
	  	        		}else{
	  	        			userDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad.userRole.rlmsUserMaster.password){
	  	        			userDetailsObj["password"] =largeLoad.userRole.rlmsUserMaster.password;
	  	        		}else{
	  	        			userDetailsObj["password"] =" - ";
	  	        		}
	  	        		
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	           // $scope.setPagingData(userDetails, page, pageSize);
	  	          });
	  	        	
	  	        	/*$scope.editUser.userId=Id;
	  	        	$scope.editUser.firstName=firstName;*/
		
		$scope.submitEditProfile = function(){
			var userData = {};
			userData = {
					userId:userDetails.Id,
					firstName:userDetails.firstName,
					lastName:lastName,
					address:$scope.editUser.address,
					area:$scope.editUser.area,
					contactNumber:$scope.editUser.contactnumber,
					emailId:$scope.editUser.emailid,
					city:$scope.editUser.city,
					pinCode:$scope.editUser.pincode,
			};
			serviceApi.doPostWithData("/RLMS/admin/validateAndUpdateUser",userData)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		//reset add branch
		$scope.initEditUser = function(){
			$scope.showAlert = false;
			//initAddUser();
			//initEditUser();
			//$scope.addUserForm.$setPristine();
			//$scope.addUserForm.$setUntouched();
		}
		
	}]);
})();
