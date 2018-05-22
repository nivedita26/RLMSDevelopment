(function() {
	'use strict';
	angular
			.module('rlmsApp')
			.controller(
					'complaiantManagementCtrl',
					[
							'$scope',
							'$filter',
							'serviceApi',
							'$route',
							'$http',
							'utility',
							'$rootScope',
							'$modal',
							'$log',
							'$window',
							function($scope, $filter, serviceApi, $route,
									$http, utility, $rootScope,$modal,$log,$window) {
								initCustomerList();
								$scope.showCompany = false;
								$scope.showBranch = false;
								
								$scope.goToAddComplaint = function() {
									window.location.hash = "#/add-complaint";
								};
								
								function initCustomerList() {
									$scope.date = {
								        startDate: moment().subtract(1, "days"),
								        endDate: moment()
								    };
									$scope.alert = { type: 'success', msg: 'You successfully Added Complaint.',close:true };
									$scope.showAlert = false;
									$scope.selectedCompany = {};
									$scope.selectedBranch = {};
									$scope.selectedCustomer = {};
									$scope.selectedCalltype = {};
									$scope.selectedLifts = {};
									$scope.branches = [];
									$scope.callType = [{type:"Complaints"},{type:"Service Calls"}];
									$scope.selectedlifts = {};
									$scope.selectedStatus = {};
									$scope.selectedTechnician = {};
									$scope.dateRange={};
									$scope.isAssigned=true;
									var today = new Date().toISOString().slice(0, 10);
									$scope.dateRange.date = {"startDate": today, "endDate": today};
									$scope.status = [ {
										id : 2,
										name : 'Pending'
									}, {
										id : 3,
										name : 'Assigned'
									}, {
										id : 4,
										name : 'Completed'
									} ];
									$scope.lifts = [];
									$scope.showAdvanceFilter = false;
									$scope.showTable = false;
								}
								function loadCompanyData() {
									serviceApi
											.doPostWithoutData(
													'/RLMS/admin/getAllApplicableCompanies')
											.then(function(response) {
												$scope.companies = response;
											});
								}

								function loadDefaultComplaintData() {
									var dataToSend = {
										branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId,
										branchCustomerMapId : -1,
										listOfLiftCustoMapId : [],
										statusList : [],
										serviceCallType : 0
									};
									serviceApi
											.doPostWithData('/RLMS/complaint/getListOfComplaints', dataToSend)
											.then(
													function(largeLoad) {
														$scope.complaints = largeLoad;
														$scope.showTable = true;
														var userDetails = [];
														for (var i = 0; i < largeLoad.length; i++) {
															var userDetailsObj = {};
															if (!!largeLoad[i].complaintNumber) {
																userDetailsObj["Number"] = largeLoad[i].complaintNumber;
															} else {
																userDetailsObj["Number"] = " - ";
															}
															if (!!largeLoad[i].title) {
																userDetailsObj["Title"] = largeLoad[i].title;
															} else {
																userDetailsObj["Title"] = " - ";
															}
															if (!!largeLoad[i].remark) {
																userDetailsObj["Remark"] = largeLoad[i].remark;
															} else {
																userDetailsObj["Remark"] = " - ";
															}
															if (!!largeLoad[i].registrationDateStr) {
																userDetailsObj["Registration_Date"] = largeLoad[i].registrationDateStr;
															} else {
																userDetailsObj["Registration_Date"] = " - ";
															}
															if (!!largeLoad[i].serviceStartDateStr) {
																userDetailsObj["Service_StartDate"] = largeLoad[i].serviceStartDateStr;
															} else {
																userDetailsObj["Service_StartDate"] = " - ";
															}
															if (!!largeLoad[i].serviceStartDateStr) {
																userDetailsObj["Service_Start_Date"] = largeLoad[i].serviceStartDateStr;
															} else {
																userDetailsObj["Service_Start_Date"] = " - ";
															}
															if (!!largeLoad[i].actualServiceEndDateStr) {
																userDetailsObj["Service_End_Date"] = largeLoad[i].actualServiceEndDateStr;
															} else {
																userDetailsObj["Service_End_Date"] = " - ";
															}
															if (!!largeLoad[i].liftAddress) {
																userDetailsObj["Address"] = largeLoad[i].liftAddress;
															} else {
																userDetailsObj["Address"] = " - ";
															}
															if (!!largeLoad[i].city) {
																userDetailsObj["City"] = largeLoad[i].city;
															} else {
																userDetailsObj["City"] = " - ";
															}
															if (!!largeLoad[i].status) {
																userDetailsObj["Status"] = largeLoad[i].status;
															} else {
																userDetailsObj["Status"] = " - ";
															}
															if (!!largeLoad[i].technicianDtls) {
																userDetailsObj["Technician"] = largeLoad[i].technicianDtls;
															} else {
																userDetailsObj["Technician"] = " - ";
															}
															if (!!largeLoad[i].complaintId) {
																userDetailsObj["complaintId"] = largeLoad[i].complaintId;
															} else {
																userDetailsObj["complaintId"] = " - ";
															}
															userDetails.push(userDetailsObj);
															}
														$scope.totalServerItems = 0;
														$scope.pagingOptions = {
															pageSizes : [10, 20, 50],
															pageSize : 10,
															currentPage : 1
														};

														$scope.setPagingData(userDetails, 1, 10);
													});

								}

								$scope.loadBranchData = function() {
									var companyData = {};
									if ($scope.showCompany == true) {
										companyData = {
											companyId : $scope.selectedCompany.selected.companyId
										}
									} else {
										companyData = {
											companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
										}
									}
									serviceApi
											.doPostWithData(
													'/RLMS/admin/getAllBranchesForCompany',
													companyData)
											.then(function(response) {
												$scope.branches = response;
												$scope.selectedBranch.selected=undefined;
												$scope.selectedCustomer.selected=undefined;
												var emptyComplaint=[];
												$scope.myData=emptyComplaint;
											});
								}
								$scope.loadCustomerData = function() {
									var branchData = {};
									if ($scope.showBranch == true) {
										branchData = {
											branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId
										}
									} else {
										branchData = {
											branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
										}
									}
									serviceApi
											.doPostWithData(
													'/RLMS/admin/getAllCustomersForBranch',
													branchData)
											.then(
													function(customerData) {
														var tempAll = {
															branchCustomerMapId : -1,
															firstName : "All"
														}
														$scope.cutomers = customerData;
														$scope.cutomers
																.unshift(tempAll);
														$scope.selectedCustomer.selected=undefined;
													})
								}
								$scope.loadLifts = function() {
									var dataToSend = {
										branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
										branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
									}
									serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
											.then(function(liftData) {
												$scope.lifts = liftData;
											})
								}
								// Toggle Advance Filter
								$scope.toggleAdvanceFilter = function() {
									if ($scope.showAdvanceFilter == true) {
										$scope.showAdvanceFilter = false;
									} else {
										$scope.showAdvanceFilter = true;
										$scope.loadLifts();
									}
								};
								$scope.filterOptions = {
									filterText : '',
									useExternalFilter : true
								};
								$scope.totalServerItems = 0;
								$scope.pagingOptions = {
									pageSizes : [ 10, 20, 50 ],
									pageSize : 10,
									currentPage : 1
								};
								$scope.setPagingData = function(data, page,
										pageSize) {
									var pagedData = data.slice((page - 1)
											* pageSize, page * pageSize);
									$scope.myData = pagedData;
									$scope.totalServerItems = data.length;
									if (!$scope.$$phase) {
										$scope.$apply();
									}
								};
								$scope.getPagedDataAsync = function(pageSize,
										page, searchText) {

								setTimeout(
											function() {
												var data;
												if (searchText) {
													var ft = searchText
															.toLowerCase();
													var dataToSend = $scope
															.construnctObjeToSend();
													serviceApi
															.doPostWithData('/RLMS/complaint/getListOfComplaints',dataToSend)
															.then(
																	function(largeLoad) {
																		$scope.complaints = largeLoad;
																		$scope.showTable = true;
																		var userDetails = [];
																		for (var i = 0; i < largeLoad.length; i++) {
																			var userDetailsObj = {};
																			if (!!largeLoad[i].complaintNumber) {
																				userDetailsObj["Number"] = largeLoad[i].complaintNumber;
																			} else {
																				userDetailsObj["Number"] = " - ";
																			}
																			if (!!largeLoad[i].title) {
																				userDetailsObj["Title"] = largeLoad[i].title;
																			} else {
																				userDetailsObj["Title"] = " - ";
																			}
																			if (!!largeLoad[i].remark) {
																				userDetailsObj["Remark"] = largeLoad[i].remark;
																			} else {
																				userDetailsObj["Remark"] = " - ";
																			}
																			
																			if (!!largeLoad[i].registrationDateStr) {
																				userDetailsObj["Registration_Date"] = largeLoad[i].registrationDateStr;
																			} else {
																				userDetailsObj["Registration_Date"] = " - ";
																			}
																			if (!!largeLoad[i].serviceStartDateStr) {
																				userDetailsObj["Service_StartDate"] = largeLoad[i].serviceStartDateStr;
																			} else {
																				userDetailsObj["Service_StartDate"] = " - ";
																			}
																			if (!!largeLoad[i].serviceStartDateStr) {
																				userDetailsObj["Service_Start_Date"] = largeLoad[i].serviceStartDateStr;
																			} else {
																				userDetailsObj["Service_Start_Date"] = " - ";
																			}
																			if (!!largeLoad[i].actualServiceEndDateStr) {
																				userDetailsObj["Service_End_Date"] = largeLoad[i].actualServiceEndDateStr;
																			} else {
																				userDetailsObj["Service_End_Date"] = " - ";
																			}
																			if (!!largeLoad[i].liftAddress) {
																				userDetailsObj["Address"] = largeLoad[i].liftAddress;
																			} else {
																				userDetailsObj["Address"] = " - ";
																			}
																			if (!!largeLoad[i].city) {
																				userDetailsObj["City"] = largeLoad[i].city;
																			} else {
																				userDetailsObj["City"] = " - ";
																			}
																			if (!!largeLoad[i].status) {
																				userDetailsObj["Status"] = largeLoad[i].status;
																			} else {
																				userDetailsObj["Status"] = " - ";
																			}
																			if (!!largeLoad[i].technicianDtls) {
																				userDetailsObj["Technician"] = largeLoad[i].technicianDtls;
																			} else {
																				userDetailsObj["Technician"] = " - ";
																			}
																			if (!!largeLoad[i].complaintId) {
																				userDetailsObj["complaintId"] = largeLoad[i].complaintId;
																			} else {
																				userDetailsObj["complaintId"] = " - ";
																			}
																			
																			userDetails
																					.push(userDetailsObj);
																		}
																		data = userDetails
																				.filter(function(
																						item) {
																					return JSON
																							.stringify(
																									item)
																							.toLowerCase()
																							.indexOf(
																									ft) !== -1;
																				});
																		$scope
																				.setPagingData(
																						data,
																						page,
																						pageSize);
																	});
												} else {
													var dataToSend = $scope
															.construnctObjeToSend();
													serviceApi
															.doPostWithData(
																	'/RLMS/complaint/getListOfComplaints',
																	dataToSend)
															.then(
																	function(
																			largeLoad) {
																		$scope.complaints = largeLoad;
																		$scope.showTable = true;
																		var userDetails = [];
																		for (var i = 0; i < largeLoad.length; i++) {
																			var userDetailsObj = {};
																			if (!!largeLoad[i].complaintNumber) {
																				userDetailsObj["Number"] = largeLoad[i].complaintNumber;
																			} else {
																				userDetailsObj["Number"] = " - ";
																			}
																			if (!!largeLoad[i].title) {
																				userDetailsObj["Title"] = largeLoad[i].title;
																			} else {
																				userDetailsObj["Title"] = " - ";
																			}
																			if (!!largeLoad[i].remark) {
																				userDetailsObj["Remark"] = largeLoad[i].remark;
																			} else {
																				userDetailsObj["Remark"] = " - ";
																			}
																			
																			if (!!largeLoad[i].registrationDateStr) {
																				userDetailsObj["Registration_Date"] = largeLoad[i].registrationDateStr;
																			} else {
																				userDetailsObj["Registration_Date"] = " - ";
																			}
																			if (!!largeLoad[i].serviceStartDateStr) {
																				userDetailsObj["Service_StartDate"] = largeLoad[i].serviceStartDateStr;
																			} else {
																				userDetailsObj["Service_StartDate"] = " - ";
																			}
																			if (!!largeLoad[i].serviceStartDateStr) {
																				userDetailsObj["Service_Start_Date"] = largeLoad[i].serviceStartDateStr;
																			} else {
																				userDetailsObj["Service_Start_Date"] = " - ";
																			}
																			if (!!largeLoad[i].actualServiceEndDateStr) {
																				userDetailsObj["Service_End_Date"] = largeLoad[i].actualServiceEndDateStr;
																			} else {
																				userDetailsObj["Service_End_Date"] = " - ";
																			}
																			if (!!largeLoad[i].liftAddress) {
																				userDetailsObj["Address"] = largeLoad[i].liftAddress;
																			} else {
																				userDetailsObj["Address"] = " - ";
																			}
																			if (!!largeLoad[i].city) {
																				userDetailsObj["City"] = largeLoad[i].city;
																			} else {
																				userDetailsObj["City"] = " - ";
																			}
																			if (!!largeLoad[i].status) {
																				userDetailsObj["Status"] = largeLoad[i].status;
																			} else {
																				userDetailsObj["Status"] = " - ";
																			}
																			if (!!largeLoad[i].technicianDtls) {
																				userDetailsObj["Technician"] = largeLoad[i].technicianDtls;
																			} else {
																				userDetailsObj["Technician"] = " - ";
																			}
																			if (!!largeLoad[i].complaintId) {
																				userDetailsObj["complaintId"] = largeLoad[i].complaintId;
																			} else {
																				userDetailsObj["complaintId"] = " - ";
																			}
																			userDetails
																					.push(userDetailsObj);
																		}
																		$scope
																				.setPagingData(
																						userDetails,
																						page,
																						pageSize);
																	});

												}
											}, 100);
								};
								$scope.construnctObjeToSend = function() {
									var dataToSend = {
											branchCompanyMapId:0,
											branchCustomerMapId:0,
											listOfLiftCustoMapId:[],
											statusList:[],
											serviceCallType:0
											
									};
									if($scope.selectedCalltype.selected.type=="Complaints"){
										$rootScope.serviceCallTypeSelect=0;
										dataToSend["serviceCallType"]=0;
									}else{
										$rootScope.serviceCallTypeSelect=1;
										dataToSend["serviceCallType"]=1;
									}
									if ($scope.showBranch == true) {
										dataToSend["branchCompanyMapId"] = $scope.selectedBranch.selected.companyBranchMapId
									} else {
										dataToSend["branchCompanyMapId"] = $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
									}
									dataToSend["branchCustomerMapId"] = $scope.selectedCustomer.selected.branchCustomerMapId;
									
									if($scope.showAdvanceFilter){
										var tempLiftIds = [];
										for (var i = 0; i < $scope.selectedlifts.selected.length; i++) {
											tempLiftIds
													.push($scope.selectedlifts.selected[i].liftId);
										}
										var tempStatus = [];
										for (var j = 0; j < $scope.selectedStatus.selected.length; j++) {
											tempStatus
													.push($scope.selectedStatus.selected[j].id);
										}
										dataToSend["listOfLiftCustoMapId"] = tempLiftIds;
										dataToSend["statusList"] = tempStatus;
										//dataToSend["fromDate"]=$scope.dateRange.date.startDate;
										//dataToSend["toDate"]=$scope.dateRange.date.endDate;
									}
									return dataToSend;
								}
								$scope.loadComplaintsList = function() {
									$scope.getPagedDataAsync(
											$scope.pagingOptions.pageSize,
											$scope.pagingOptions.currentPage);
								}
								$scope.resetComplaintList = function() {
									initCustomerList();
								};
								// showCompnay Flag
								if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1) {
									$scope.showCompany = true;
									loadCompanyData();
									loadDefaultComplaintData();
								} else {
									$scope.showCompany = false;
									$scope.loadBranchData();
								}

								// showBranch Flag
								if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
									$scope.showBranch = true;
								} else {
									$scope.showBranch = false;
								}
								
								if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 3) {
									$scope.loadCustomerData();
								}

								$scope
										.$watch(
												'pagingOptions',
												function(newVal, oldVal) {
													if (newVal !== oldVal) {
														$scope
																.getPagedDataAsync(
																		$scope.pagingOptions.pageSize,
																		$scope.pagingOptions.currentPage,
																		$scope.filterOptions.filterText);
													}
												}, true);
								$scope
										.$watch(
												'filterOptions',
												function(newVal, oldVal) {
													if (newVal !== oldVal) {
														$scope
																.getPagedDataAsync(
																		$scope.pagingOptions.pageSize,
																		$scope.pagingOptions.currentPage,
																		$scope.filterOptions.filterText);
													}
												}, true);

								 var templateWithTooltip = '<div><span tooltip="{{row.getProperty(col.field)}}" tooltip-append-to-body="true" tooltip-placement="right" >{{row.getProperty(col.field)}}</span></div>';
								$scope.gridOptions = {
									data : 'myData',
									rowHeight : 40,
									enablePaging : true,
									showFooter : true,
									totalServerItems : 'totalServerItems',
									pagingOptions : $scope.pagingOptions,
									filterOptions : $scope.filterOptions,
									multiSelect : false,
									gridFooterHeight : 35,
									enableRowSelection: true,
									selectedItems: [],
									afterSelectionChange:function(rowItem, event){
										//$scope.showAlert = false;
										//console.log(rowItem);
										//console.log(event);
										//var selected = $filter('filter')($scope.complaints,{complaintId:$scope.gridOptions.selectedItems[0].complaintId});
//										if(selected[0].Status == "Assigned"){
//											$scope.isAssigned = true;
//										}else{
//											$scope.isAssigned = false;
//										}
									},
									columnDefs : [ {
										field : "Number",
										displayName:"Number",
										width : 120
									}, {
										field : "Title",
										displayName:"Title",
										width : 120
									}, {
										field : "Remark",
										displayName:"Details",
										width : 120,
										cellTemplate: templateWithTooltip,
										cellClass: 'cellToolTip'
									}, {
										field : "Registration_Date",
										displayName:"Registration Date",
										width : 120
									}
									, {
										field : "Service_StartDate",
										displayName:"Service Start Date",
										width : 160
									}, {
										field : "Service_End_Date",
										displayName:"Service End Date",
										width : 120
									}
									, {
										field : "Address",
										displayName:"Address",
										width : 120
									}
									, {
										field : "City",
										displayName:"City",
										width : 120
									}, {
										field : "Status",
										displayName:"Status",
										width : 120
									}
									, {
										field : "Technician",
										displayName:"Technician",
										width : 120
									},{
										field : "complaintId",
										displayName:"complaintId",
										visible: false,
									},{
										cellTemplate :  
								             '<button ng-click="$event.stopPropagation(); editThisRow(row.entity);" title="Edit" style="margin-top: 6px;height: 24px;" class="btn-sky"><span class="glyphicon glyphicon-pencil"></span></button>',
										width : 30
									}
									]
								};
								/*if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel != 1){
									$scope.gridOptions.columnDefs[0].visible = false;
								}*/								
								$rootScope.editComplaint={};
								$rootScope.technicianDetails=[];
								$rootScope.complaintStatusArray=['Pending','Assigned','Completed','In Progress'];
								$scope.editThisRow=function(row){
									if(row.Status==='Resolved' || row.Status==='Completed'){
										$window.confirm('Complaint already completed or resolved');
									}else{
										$rootScope.editComplaint.complaintsNumber=row.Number.replace(/-/g, '');
										$rootScope.editComplaint.complaintsTitle=row.Title.replace(/-/g, '');
										$rootScope.editComplaint.complaintsRemark=row.Remark.replace(/-/g, '');
										$rootScope.editComplaint.complaintsAddress=row.Address.replace(/-/g, '');
										$rootScope.editComplaint.complaintsCity=row.City.replace(/-/g, '');
										$rootScope.editComplaint.regDate=row.Registration_Date;
										$rootScope.editComplaint.serviceEndDate=row.Service_End_Date;
										$rootScope.editComplaint.serviceStartDate=row.Service_StartDate;
										$rootScope.selectedComplaintStatus=row.Status;
										//$rootScope.editComplaint.complaintsStatus=row.Status.replace(/-/g, '');
										var dataToSend ={
												complaintId:row.Number
										}
									//	if($scope.selectedCalltype.selected.type=="Complaints"){
											$rootScope.serviceCallTypeSelect=0;
											dataToSend["serviceCallType"]=0;
//										}else{
//											$rootScope.serviceCallTypeSelect=1;
//											dataToSend["serviceCallType"]=1;
//										}
										serviceApi.doPostWithData('/RLMS/complaint/getAllTechniciansToAssignComplaint',dataToSend)
										.then(function(data) {
											$rootScope.techniciansForEditComplaints = data;
											var technicianArray=$rootScope.techniciansForEditComplaints;
											technicianArray.forEach(function(technician) {
												if(row.Technician.includes(technician.name)){
													$rootScope.selectedTechnician=technician;
												}
											});
											window.location.hash = "#/edit-complaint";
										});
									}
									
								};
//								 $scope.$watch('gridOptions.selectedItems', function(oldVal , newVal) {
//								     console.log("________")
//								    		 console.log(newVal);
//								    });
								$scope.assignComplaint =function(){
									//var selected = $filter('filter')($scope.complaints,{complaintId:$scope.gridOptions.selectedItems[0].complaintId});
									if($scope.gridOptions.selectedItems[0].Status == "Pending"){
										if($scope.gridOptions.selectedItems[0].Title.trim()==="" || $scope.gridOptions.selectedItems[0].Title.trim()==="-" || $scope.gridOptions.selectedItems[0].Remark.trim()==="" || $scope.gridOptions.selectedItems[0].Remark.trim()==="-"){
											alert("Edit complaint first to add mendatory fields like title, details");
										}else{
											$scope.selectedComplaintId = $scope.gridOptions.selectedItems[0].complaintId;
											var dataToSend ={
													complaintId:$scope.selectedComplaintId
											}
//											if($scope.selectedCalltype.selected.type=="Complaints"){
												$rootScope.serviceCallTypeSelect=0;
												dataToSend["serviceCallType"]=0;
//											}else{
//												$rootScope.serviceCallTypeSelect=1;
//												dataToSend["serviceCallType"]=1;
//											}
											serviceApi.doPostWithData('/RLMS/complaint/getAllTechniciansToAssignComplaint',dataToSend)
											.then(function(data) {
											console.log("DATA /RLMS/complaint/getAllTechniciansToAssignComplaint :",JSON.stringify(data));

												$scope.technicians = data;
											})
											$scope.modalInstance = $modal.open({
										        templateUrl: 'assignComplaintTemplate',
										        scope:$scope
										     })
										}
									}else{
										alert("Already Assigned Complaint");
									}
										
							}
								
								$scope.loadMap =function(){
									var dataINPOPUP = "<p><b>Technician Location</b><br>Name: "+$scope.selectedTechnician.selected.name+"<br>Contact Number: "+$scope.selectedTechnician.selected.contactNumber+"<br>Assigned Complaint: "+$scope.selectedTechnician.selected.countOfComplaintsAssigned+"<br>Latitude: "+$scope.selectedTechnician.selected.latitude+"<br>Longitude: "+$scope.selectedTechnician.selected.longitude+"</p>";
									$scope.map = new GMaps({
						    div: '#map',
						    lat: $scope.selectedTechnician.selected.latitude,
						    lng: $scope.selectedTechnician.selected.longitude
						    });
									$scope.map.addMarker({
									 lat: $scope.selectedTechnician.selected.latitude,
						    lng: $scope.selectedTechnician.selected.longitude,
						    title: 'Technician Location',
					     click: function(e) {
					     	content: dataINPOPUP
					     },
					    	infoWindow: {
					    	 content: dataINPOPUP
									}
					   });		
								}
							
								
								$scope.submitAssign = function() {
									var dataToSend ={
											complaintId:$scope.selectedComplaintId,
											userRoleId:$scope.selectedTechnician.selected.userRoleId,
											serviceCallType:$rootScope.serviceCallTypeSelect
									}
									serviceApi.doPostWithData('/RLMS/complaint/assignComplaint',dataToSend)
									.then(function(response) {
										$scope.showAlert = true;
										var key = Object.keys(response);
										var successMessage = response.response;
										$scope.alert.msg = successMessage;
										$scope.alert.type = "success";
										$scope.loadComplaintsList();
									})
									setTimeout(function(){ $scope.modalInstance.dismiss(); }, 1000)					            
						          };
						          $scope.cancelAssign = function(){
						        	  $scope.modalInstance.dismiss('cancel');
						          }
							}]);
	
})();
