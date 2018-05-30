(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editComplaintCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope','$modal', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope,$modal) {
			//loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Edited Complaint.',close:true };
			$scope.showAlert = false;
			
			$scope.complaintTitle=[
				{
					id : 0,
					name : 'Stucked between floor'
				},{
					id : 1,
					name : 'Door open close issue'
				},{
					id : 2,
					name : 'Door sensor not working'
				},{
					id : 3,
					name : 'Level mismatch'
				},{
					id : 4,
					name : 'Lift lights not working'
				},{
					id : 5,
					name : 'Lift fan not working'
				},{
					id : 6,
					name : 'Lift intercom'
				},{
					id : 7,
					name : 'Buttons not working'
				},{
					id : 8,
					name : 'call not taken from lop / cop'
				},{
					id : 9,
					name : 'Auto call book'
				},{
					id : 10,
					name : 'Display not working'
				},{
					id : 11,
					name : 'Display error E'
				},{
					id : 12,
					name : 'Display some error cod'
				},{
					id : 13,
					name : 'Rescue not working'
				},{
					id : 14,
					name : 'Jerks and rollbacks'
				},{
					id : 15,
					name : 'Vibrates during running'
				},{
					id : 16,
					name : 'Alarm not working'
				},{	
					id : 17,
					name : 'Gate lock not operating'
				},{
					id : 18,
					name : 'Wrong annoucement'
				},{
					id : 19,
					name : 'Music is off'
				},{
					id : 20,
					name : 'Lift Installation'
				},{
					id : 21,
					name : 'AMC Service Call'
				},{
					id : 22,
					name : 'LMS alert Call'
				},{
					id : 23,
					name : 'Lift update Parameter'
				}
				];
			
			$scope.callType=[
				{
					id: 1,
					name:'Lift Installation call'
				},{
					id: 2,
					name:'Configuration/Settings call'
				},{
					id: 3,
					name:'AMC call'
				},{
					id: 4,
					name:'Under Warranty Support call'
				},{
					id: 5,
					name:'LMS alert call'
				},{
					id: 6,
					name:'Operator assigned/Generic call'
				},{
					id: 7,
					name:'User raised call through App'
				},{
					id: 8,
					name:'User raised call through Telephone'
				},{
					id: 9,
					name:'Reassign call'
				}
			];
	
			$scope.editFlag={
					regDate:false,
					serviceStartDate:false,
					serviceEndDate:false
			}
			$scope.open = function($event,which) {
			      $event.preventDefault();
			      $event.stopPropagation();
			      if($scope.editFlag[which] != true)
			    	  $scope.editFlag[which] = true;
			      else
			    	  $scope.editFlag[which] = false;
			  }
			
			$scope.submitEditComplaint = function(){
				$scope.selectedTechnician;
				var complaintsData = {};
				var userRole;
				var userName;
				if($scope.selectedTechnician){
					userRole=$scope.selectedTechnician.userRoleId;
					userName=$scope.selectedTechnician.name;
				}
				complaintsData = {
						 complaintNumber:$scope.editComplaint.complaintsNumber,
						 complaintId:$scope.editComplaint.complaintsNumber,
						 registrationDateStr:$filter('date')($scope.editComplaint.regDate, "dd-MMM-yyyy"),
						 serviceStartDateStr:$filter('date')($scope.editComplaint.serviceStartDate, "dd-MMM-yyyy"),
						 actualServiceEndDateStr:$filter('date')($scope.editComplaint.serviceEndDate, "dd-MMM-yyyy"),
						 liftAddress:$scope.editComplaint.complaintsAddress + $scope.editComplaint.complaintsCity,
						 status:$scope.selectedComplaintStatus,
						 title:$scope.editComplaint.complaintTitle,
						 remark:$scope.editComplaint.complaintsRemark,
						 technicianDtls:userName,
						 userRoleId:userRole,
						 serviceCallType:$rootScope.serviceCallTypeSelect
				};
				serviceApi.doPostWithData("/RLMS/complaint/validateAndUpdateComplaint",complaintsData)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddComplaint();
					$scope.addComplaintForm.$setPristine();
					$scope.addComplaintForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
		  	
			$scope.resetEditComplaint = function(){
				$window.history.back();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
	}]);
})();
