(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addAMCCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope','$modal', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope,$modal) {
		initAddAMC();
			//loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Added AMC details.',close:true };
			$scope.showAlert = false;
			$scope.showCompany = false;
			$scope.showBranch = false;
			$scope.companies = [];
			$scope.newCallTypes={};
			$scope.callTypesArray = [];

			                $scope.Add = function () {
			                    //Add the new item to the Array.
			                    var customer = {};
			                    customer.title = $scope.newCallTypes.title;
			                    customer.serviceDate = $scope.newCallTypes.serviceDate;
			                    $scope.callTypesArray.push(customer);

			                    //Clear the TextBoxes.
			                    $scope.newCallTypes.title = "";
			                    $scope.newCallTypes.serviceDate = "";
			                };

			                $scope.Remove = function (index) {
			                    //Find the record using Index from Array.
			                    var name = $scope.callTypesArray[index].title;
			                    if ($window.confirm("Do you want to delete: " + name)) {
			                        //Remove the item from Array using Index.
			                        $scope.callTypesArray.splice(index, 1);
			                    }
			                }
			
			function initAddAMC() {
				$scope.customerSelected = false;
				$scope.selectedCompany={};
				$scope.selectedBranch = {};
				$scope.selectedCustomer = {};
				$scope.selectedLift = {};
				$scope.selectedAmc = {};
				$scope.addAMC={
						liftCustoMapId:'',
						amcEndDate:'',
						amcStartDate:'',
						amcType:'',
						amcAmount:''
				};
				$scope.amcStatuses=[
					 {
						 name:"Under Warranty",
						 id:38
					 },
					 {
						 name:"Renewal Due",
						 id:39
					 },
					 {
						 name:"AMC Pending",
						 id:40
					 },
					 {
						 name:"Under AMC",
						 id:41
					 }
				 ]
				$scope.amcTypes=[
									 {
										 name:"Comprehensive",
										 id:42
									 },
									 {
										 name:"Non Comprehensive",
										 id:43
									 },
									 {
										 name:"On Demand",
										 id:44
									 },
									 {
										 name:"Other",
										 id:45
									 }
								 ]
			}
			$scope.openFlag={
					fromDate:false,
					toDate:false,
					serviceDate:false
			}
			$scope.open = function($event,which) {
			      $event.preventDefault();
			      $event.stopPropagation();
			      if($scope.openFlag[which] != true)
			    	  $scope.openFlag[which] = true;
			      else
			    	  $scope.openFlag[which] = false;
			}
			
			$scope.loadBranchData = function(){
				var companyData={};
				if($scope.showCompany == true){
	  	    		companyData = {
							companyId : $scope.selectedCompany.selected!=undefined?$scope.selectedCompany.selected.companyId:0
						}
	  	    	}else{
	  	    		companyData = {
							companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
						}
	  	    	}
			    serviceApi.doPostWithData('/RLMS/admin/getAllBranchesForCompany',companyData)
			    .then(function(response){
			    	$scope.branches = response;
			    	$scope.selectedBranch.selected = undefined;
			    	$scope.selectedCustomer.selected = undefined;
			    	var emptyArray=[];
			    	$scope.myData = emptyArray;
			    });
			}
			$scope.loadCustomerData = function(){
				var branchData ={};
	  	    	if($scope.showBranch == true){
	  	    		branchData = {
	  	    			branchCompanyMapId : $scope.selectedBranch.selected!=null?$scope.selectedBranch.selected.companyBranchMapId:0
						}
	  	    	}else{
	  	    		branchData = {
	  	    			branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
						}
	  	    	}
	  	    	serviceApi.doPostWithData('/RLMS/admin/getAllCustomersForBranch',branchData)
	 	         .then(function(customerData) {
	 	        	 $scope.cutomers = customerData;
	 	        	 $scope.selectedCustomer.selected = undefined;
	 	        	var emptyArray=[];
			    	$scope.myData = emptyArray;
	 	         })
			}
			
			$scope.loadLifts = function() {
				
				var dataToSend = {
					branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
				}
				serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
						.then(function(liftData) {
							$scope.lifts = liftData;
						})
				
				serviceApi.doPostWithData('/RLMS/complaint/getCustomerDtlsById',dataToSend)
						.then(function(data) {
							$scope.customerSelected = true;
							$scope.companyName = data.companyName;
							$scope.branchName = data.branchName
						})
			}
			$scope.addServiceCall=function(){
				$scope.callTypesArray = [];
				$scope.modalInstance = $modal.open({
					templateUrl: 'addServiceCallsTemplate',
					scope:$scope
				})
			}
			 $scope.cancelAssign = function(){
	        	  $scope.modalInstance.dismiss('cancel');
	          }
			//Post call add customer
			$scope.submitaddAMC = function(){
			//	$scope.addAMC.liftCustomerMapId =  $scope.selectedCustomer.selected.branchCustomerMapId;
				$scope.addAMC.liftCustoMapId=$scope.selectedLift.selected.liftId;
				$scope.addAMC.amcType=$scope.selectedAmc.selected.id;
				$scope.addAMC.amcServiceCalls=$scope.callTypesArray;
				//$scope.addAMC.amcType=42;
				//$scope.addAMC.amcEdDate=$filter('date')($scope.addAMC.amcEdDate, "dd-MMM-yyyy");
				//$scope.addAMC.amcStDate=$filter('date')($scope.addAMC.amcStDate, "dd-MMM-yyyy");
				serviceApi.doPostWithData("/RLMS/report/addAMCDetailsForLift",$scope.addAMC)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddAMC();
					$scope.addAMCForm.$setPristine();
					$scope.addAMCForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			
		  
			//rese add branch
			$scope.resetaddAmc = function(){
				initAddComplaint();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
			
		  	
		  	//showBranch Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
				$scope.showBranch= true;
				$scope.loadBranchData();
				$scope.loadCustomerData();

			}else{
				$scope.showBranch=false;
				$scope.loadCustomerData();

			}
		  	
			/*$scope.searchCustomer = function(query){
				//console.log(query);
				if(query && query.length > 1){
				 var dataToSend = {
				 	'customerName':query
				 }
					serviceApi.doPostWithData("/RLMS/complaint/getCustomerByName",dataToSend)
					.then(function(customerData){
						//console.log(customerData);
						 $scope.cutomers = customerData;
					},function(error){
						
					});
				} 
				
			}*/
	}]);
})();
