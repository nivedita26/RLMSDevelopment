angular.module('theme.demos.dashboard', [
  'angular-skycons',
  'theme.demos.forms',
  'theme.demos.tasks',
  'ngStorage'
])
  .controller('DashboardController', ['$scope', '$timeout', '$window', '$modal', 'serviceApi', '$filter', '$rootScope','$localStorage','locker','$http', function ($scope, $timeout, $window, $modal, serviceApi, $filter, $rootScope,$localStorage,locker,$http) {
    'use strict';
    var moment = $window.moment;
    var _ = $window._;
    $scope.loadingChartData = false;
    $scope.refreshAction = function () {
      $scope.loadingChartData = true;
      $timeout(function () {
        $scope.loadingChartData = false;
      }, 2000);
    };
    $rootScope.showDasboardForInditech=false;
    $rootScope.showDasboardForOthers=false;
    
    $http({
		  method: 'POST',
		  url: '/RLMS/getLoggedInUser'
		}).then(function successCallback(response) {
			$rootScope.loggedInUserInfoForDashboard=response;
			if($rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 1 || $rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 2){
				$rootScope.showDasboardForInditech= true;
				$rootScope.showDasboardForOthers=false;
			}else{
				$rootScope.showDasboardForOthers=true;
				$rootScope.showDasboardForInditech=false;
			}
		  }, function errorCallback(response) {
		  });
    $scope.totalServerItemsForComplaints = 0;

    $scope.messages = [{
      name: 'Sanket',
      message: 'Mobile number changed',
      time: '3m',
      class: 'notification-danger',
      thumb: 'assets/demo/avatar/paton.png',
      read: false
    }, {
      name: 'Test User',
      message: 'Regstered today',
      time: '17m',
      class: 'notification-danger',
      thumb: 'assets/demo/avatar/corbett.png',
      read: false
    }];

    $scope.setRead = function (item, $event) {
      $event.preventDefault();
      $event.stopPropagation();
      item.read = true;
    };

    $scope.setUnread = function (item, $event) {
      $event.preventDefault();
      $event.stopPropagation();
      item.read = false;
    };

    $scope.setReadAll = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();
      angular.forEach($scope.messages, function (item) {
        item.read = true;
      });
    };

    $scope.unseenCount = $filter('filter')($scope.messages, {
      read: false
    }).length;

    $scope.$watch('messages', function (messages) {
      $scope.unseenCount = $filter('filter')(messages, {
        read: false
      }).length;
    }, true);
    $scope.notifications = [{
      text: 'Site visited by technician',
      time: '4m',
      class: 'notification-success',
      iconClasses: 'glyphicon glyphicon-ok',
      seen: true
    }, {
      text: 'Complaint registered today',
      time: '10m',
      class: 'notification-user',
      iconClasses: 'glyphicon glyphicon-user',
      seen: false
    }];

    $scope.setSeen = function (item, $event) {
      $event.preventDefault();
      $event.stopPropagation();
      item.seen = true;
    };

    $scope.setUnseen = function (item, $event) {
      $event.preventDefault();
      $event.stopPropagation();
      item.seen = false;
    };

    $scope.setSeenAll = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();
      angular.forEach($scope.notifications, function (item) {
        item.seen = true;
      });
    };

    $scope.unseenCountForNotifications = $filter('filter')($scope.notifications, {
      seen: false
    }).length;

    $scope.$watch('notifications', function (notifications) {
      $scope.unseenCountForNotifications = $filter('filter')(notifications, {
        seen: false
      }).length;
    }, true);

    $scope.dataTiles = {
      color: 'red',
      href: '',
      title: 'Sample',
      text: '123'
    };

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

    $scope.pagingOptionsForComplaints = {
      pageSizes: [10, 20, 50],
      pageSize: 10,
      currentPage: 1
    };

    $scope.pendingComplaints = {
      title: 'Pending Complaints',
      text: '0',
      color: 'red'
    };

    $scope.assignedComplaints = {
      title: 'Assigned Complaints',
      text: '0',
      color: 'amber'
    };

    $scope.attemptedTodayComplaints = {
      title: 'Complaints Attempted Today',
      text: '0',
      color: 'blue'
    };
    $scope.resolvedComplaints = {
      title: 'Resolved Complaints',
      text: '0',
      color: 'green'
    };
    $scope.totalComplaints = {
      title: 'Total Complaints',
      text: '0',
      color: 'indigo'
    };
    $scope.newCustomerRegistered = {
      title: 'New Customers Registered',
      text: '0',
      color: 'grey'
    };

    $scope.getComplaintsCount = function (complaintStatus) {
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');

      for (var i = 0; i < str_array.length; i++) {
        // Trim the excess whitespace.
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        // Add additional code here, such as:
        complaintStatusArray.push(str_array[i]);
      }
      setTimeout(
        function () {
          var dataToSend = $scope
            .construnctObjeToSend(complaintStatusArray);
          serviceApi
            .doPostWithData(
            '/RLMS/dashboard/getListOfComplaintsForDashboard',
            dataToSend)
            .then(
            function (
              largeLoad) {
              if (complaintStatusArray.includes('2') && complaintStatusArray.length == 1 && largeLoad.length > 0) {
                $scope.pendingComplaints.text = largeLoad.length;
              }
              if (complaintStatusArray.includes('3') && complaintStatusArray.length == 1 && largeLoad.length > 0) {
                $scope.assignedComplaints.text = largeLoad.length;
              }
              if (complaintStatusArray.includes('5') && complaintStatusArray.length == 1 && largeLoad.length > 0) {
                $scope.resolvedComplaints.text = largeLoad.length;
              }
              if (complaintStatusArray.includes('2') && complaintStatusArray.length == 3 && largeLoad.length > 0) {
                $scope.totalComplaints.text = largeLoad.length;
              }
              /*if(complaintStatusArray.includes('2') && complaintStatusArray.length==1 && largeLoad.length>0){
                $scope.newCustomerRegistered.text=largeLoad.length;   
              }*/
            });
        }, 100);
    };

    $scope.getComplaintsCountForSiteVisited = function (complaintStatus) {
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');

      for (var i = 0; i < str_array.length; i++) {
        // Trim the excess whitespace.
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        // Add additional code here, such as:
        complaintStatusArray.push(str_array[i]);
      }
      setTimeout(
        function () {
          var dataToSend = $scope
            .construnctObjeToSend(complaintStatusArray);
          serviceApi
            .doPostWithData(
            '/RLMS/dashboard/getListOfComplaintsForSiteVisited',
            dataToSend)
            .then(
            function (
              largeLoad) {
              if (largeLoad.length > 0) {
                $scope.attemptedTodayComplaints.text = largeLoad.length;
              }
            });
        }, 100);
    };

    $scope.getComplaintsCount('2');
    $scope.getComplaintsCount('3');
    $scope.getComplaintsCount('5');
    $scope.getComplaintsCount('2,3,5');
    $scope.getComplaintsCountForSiteVisited('2,3,5');

    $scope.gridOptionsForComplaints = {
      data: 'myComplaintsData',
      rowHeight: 40,
      enablePaging: true,
      showFooter: true,
      totalServerItems: 'totalServerItemsForComplaints',
      pagingOptions: $scope.pagingOptionsForComplaints,
      filterOptions: $scope.filterOptionsForModal,
      multiSelect: false,
      gridFooterHeight: 35,
      enableRowSelection: true,
      selectedItems: [],
      afterSelectionChange: function (rowItem, event) {
      },
      columnDefs: [{
        field: "Number",
        displayName: "Number",
        width: 120
      }, {
        field: "Title",
        displayName: "Title",
        width: 120
      }, {
        field: "Remark",
        displayName: "Details",
        width: 120
      }, {
        field: "Registration_Date",
        displayName: "Registration Date",
        width: 120
      }
        , {
        field: "Service_StartDate",
        displayName: "Service Start Date",
        width: 160
      }, {
        field: "Service_End_Date",
        displayName: "Service End Date",
        width: 120
      }
        , {
        field: "Address",
        displayName: "Address",
        width: 120
      }
        , {
        field: "City",
        displayName: "City",
        width: 120
      }, {
        field: "Status",
        displayName: "Status",
        width: 120
      }
        , {
        field: "Technician",
        displayName: "Technician",
        width: 120
      }, {
        field: "complaintId",
        displayName: "complaintId",
        visible: false,
      }
      ]
    };

    $scope.gridOptions = {
      data: 'myData',
      rowHeight: 40,
      enablePaging: true,
      showFooter: true,
      totalServerItems: 'totalServerItems',
      pagingOptions: $scope.pagingOptions,
      filterOptions: $scope.filterOptions,
      multiSelect: false,
      gridFooterHeight: 35,
      groupBy: 'customerName',
      columnDefs: [{
        field: "amcAmount",
        displayName: "AMC Amount",
        width: 120
      }, {
        field: "amcStartDate",
        displayName: "AMC Start Date",
        width: 120
      }, {
        field: "amcTypeStr",
        displayName: "AMC Type",
        width: 120
      }, {
        field: "area",
        displayName: "Area",
        width: 120
      }, {
        field: "city",
        displayName: "City",
        width: 120
      }, {
        field: "customerName",
        displayName: "Customer Name",
        width: 120
      }, {
        field: "dueDate",
        displayName: "Due Date",
        width: 120
      }, {
        field: "liftNumber",
        displayName: "Lift Number",
        width: 120
      }, {
        field: "status",
        displayName: "Status",
        width: 120
      }
      ]
    };

    $scope.filterOptionsForModal = {
      filterText: '',
      useExternalFilter: true
    };
    $scope.setPagingData = function (data, page, pageSize) {
      var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
      $scope.myData = pagedData;
      $scope.totalServerItems = data.length;
      if (!$scope.$$phase) {
        $scope.$apply();
      }
    };
    $scope.setPagingDataForComplaints = function (data, page, pageSize) {
      var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
      $scope.myComplaintsData = pagedData;
      $scope.totalServerItemsForComplaints = data.length;
      if (!$scope.$$phase) {
        $scope.$apply();
      }
    };
    function constructDataToSend() {
    	var tempStatus =[];
    	tempStatus.push(38);
    	tempStatus.push(39);
    	tempStatus.push(40);
    	tempStatus.push(41);
      var data = {
        companyId: $rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId,
        listOFStatusIds:tempStatus
      }
      return data;
    }
    $scope.getPagedDataAsync = function (pageSize, page, searchText) {
      setTimeout(function () {
        var data;
        if (searchText) {
          var ft = searchText.toLowerCase();
          data = $scope.myData.filter(function (item) {
            return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
          });
          $scope.setPagingData(data, page, pageSize);
        } else {
          var dataToSend = constructDataToSend();
          serviceApi.doPostWithData('/RLMS/dashboard/getAMCDetails', dataToSend)
            .then(function (largeLoad) {
              var details = [];
              for (var i = 0; i < largeLoad.length; i++) {
                var detailsObj = {};
                if (!!largeLoad[i].customerName) {
                  detailsObj["customerName"] = largeLoad[i].customerName;
                } else {
                  detailsObj["customerName"] = " - ";
                }
                if (!!largeLoad[i].liftNumber) {
                  detailsObj["liftNumber"] = largeLoad[i].liftNumber;
                } else {
                  detailsObj["liftNumber"] = " - ";
                }
                if (!!largeLoad[i].status) {
                  detailsObj["status"] = largeLoad[i].status;
                } else {
                  detailsObj["status"] = " - ";
                }
                if (!!largeLoad[i].amcAmount) {
                  detailsObj["amcAmount"] = largeLoad[i].amcAmount;
                } else {
                  detailsObj["amcAmount"] = " - ";
                }
                if (!!largeLoad[i].amcTypeStr) {
                  detailsObj["amcTypeStr"] = largeLoad[i].amcTypeStr;
                } else {
                  detailsObj["amcTypeStr"] = " - ";
                }
                if (!!largeLoad[i].amcStartDate) {
                  detailsObj["amcStartDate"] = largeLoad[i].amcStartDate;
                } else {
                  detailsObj["amcStartDate"] = " - ";
                }
                if (!!largeLoad[i].dueDate) {
                  detailsObj["dueDate"] = largeLoad[i].dueDate;
                } else {
                  detailsObj["dueDate"] = " - ";
                }
                if (!!largeLoad[i].area) {
                  detailsObj["area"] = largeLoad[i].area;
                } else {
                  detailsObj["area"] = " - ";
                }
                if (!!largeLoad[i].city) {
                  detailsObj["city"] = largeLoad[i].city;
                } else {
                  detailsObj["city"] = " - ";
                }
                details.push(detailsObj);
              }
              data = details.filter(function (item) {
                return true;
              });
              $scope.setPagingData(data, page, pageSize);
            });
        }
      }, 100);
    };


    $scope.loadAMCList = function () {
      $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
    };
    $scope.loadAMCList();

    $scope.$watch('pagingOptions', function (newVal, oldVal) {
      if (newVal !== oldVal) {
        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
      }
    }, true);
    $scope.$watch('pagingOptionsForComplaints', function (newVal, oldVal) {
      if (newVal !== oldVal) {
        $scope.getPagedDataAsyncForComplaints($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText, $scope.currentComplaintStatus, $scope.currentModel);
      }
    }, true);
    $scope.$watch('filterOptions', function (newVal, oldVal) {
      if (newVal !== oldVal) {
        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
      }
    }, true);
    $scope
      .$watch(
      'filterOptionsForModal',
      function (newVal, oldVal) {
        if (newVal !== oldVal) {
          $scope
            .getPagedDataAsyncForComplaints(
            $scope.pagingOptionsForComplaints.pageSize,
            $scope.pagingOptionsForComplaints.currentPage,
            $scope.filterOptionsForModal.filterText,
            $scope.currentComplaintStatus,
            $scope.currentModel);
        }
      }, true);


    $scope.getPagedDataAsyncForComplaints = function (pageSize,
      page, searchText, complaintStatus, callingModel) {
      var url;
      if (callingModel == 'attemptedToday') {
        url = '/RLMS/dashboard/getListOfComplaintsForSiteVisited';
      } else {
        url = '/RLMS/dashboard/getListOfComplaintsForDashboard';
      }
      console.log("Url called is -->" + url);
      setTimeout(
        function () {
          var data;
          if (searchText) {
            var ft = searchText
              .toLowerCase();
            var dataToSend = $scope
              .construnctObjeToSend(complaintStatus);
            serviceApi
              .doPostWithData(url, dataToSend)
              .then(
              function (largeLoad) {
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
                  if (!!largeLoad[i].serviceEndDateStr) {
                    userDetailsObj["Service_End_Date"] = largeLoad[i].serviceEndDateStr;
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
                  .filter(function (
                    item) {
                    return JSON
                      .stringify(
                      item)
                      .toLowerCase()
                      .indexOf(
                      ft) !== -1;
                  });
                $scope
                  .setPagingDataForComplaints(
                  data,
                  page,
                  pageSize);
              });
          } else {
            var dataToSend = $scope
              .construnctObjeToSend(complaintStatus);
            serviceApi
              .doPostWithData(url,
              dataToSend)
              .then(
              function (
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
                  if (!!largeLoad[i].serviceEndDateStr) {
                    userDetailsObj["Service_End_Date"] = largeLoad[i].serviceEndDateStr;
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
                  .setPagingDataForComplaints(
                  userDetails,
                  page,
                  pageSize);
              });

          }
        }, 100);
    };

    $scope.cancel = function () {
      $scope.modalInstance.dismiss('cancel');
    };
    $scope.openDemoModal = function (size, currentModelOpen, complaintStatus,headerValue) {
      var emptyComplaintsArray=[];
      $scope.myComplaintsData=emptyComplaintsArray;
      $scope.pagingOptionsForComplaints.currentPage=1;
      $scope.totalServerItemsForComplaints = 0;
      $scope.currentModel = currentModelOpen;
      $scope.modalHeaderVal=headerValue;
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');

      for (var i = 0; i < str_array.length; i++) {
        // Trim the excess whitespace.
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        // Add additional code here, such as:
        complaintStatusArray.push(str_array[i]);
      }
      $scope.currentComplaintStatus = complaintStatusArray;
      $scope.getPagedDataAsyncForComplaints($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "", complaintStatusArray, currentModelOpen);
      $scope.complaintStatusValue = complaintStatusArray;
      $scope.modalInstance = $modal.open({
        templateUrl: 'demoModalContent.html',
        scope: $scope
      })
    }

    $scope.construnctObjeToSend = function (complaintStatus) {
      var dataToSend = {
        statusList: [],
        companyId: $rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsCompanyMaster.companyId
      };
      dataToSend["statusList"] = complaintStatus;
      return dataToSend;
    }

    $scope.percentages = [53, 65, 23, 99];
    $scope.randomizePie = function () {
      $scope.percentages = _.shuffle($scope.percentages);
    };

    $scope.plotStatsData = [{
      data: [
        [1, 1500],
        [2, 2200],
        [3, 1100],
        [4, 1900],
        [5, 1300],
        [6, 1900],
        [7, 900],
        [8, 1500],
        [9, 900],
        [10, 1200],
      ],
      label: 'Page Views'
    }, {
      data: [
        [1, 3100],
        [2, 4400],
        [3, 2300],
        [4, 3800],
        [5, 2600],
        [6, 3800],
        [7, 1700],
        [8, 2900],
        [9, 1900],
        [10, 2200],
      ],
      label: 'Unique Views'
    }];

    $scope.plotStatsOptions = {
      series: {
        stack: true,
        lines: {
          // show: true,
          lineWidth: 2,
          fill: 0.1
        },
        splines: {
          show: true,
          tension: 0.3,
          fill: 0.1,
          lineWidth: 3
        },
        points: {
          show: true
        },
        shadowSize: 0
      },
      grid: {
        labelMargin: 10,
        hoverable: true,
        clickable: true,
        borderWidth: 0
      },
      tooltip: true,
      tooltipOpts: {
        defaultTheme: false,
        content: 'View Count: %y'
      },
      colors: ['#b3bcc7'],
      xaxis: {
        tickColor: 'rgba(0,0,0,0.04)',
        ticks: 10,
        tickDecimals: 0,
        autoscaleMargin: 0,
        font: {
          color: 'rgba(0,0,0,0.4)',
          size: 11
        }
      },
      yaxis: {
        tickColor: 'transparent',
        ticks: 4,
        tickDecimals: 0,
        //tickColor: 'rgba(0,0,0,0.04)',
        font: {
          color: 'rgba(0,0,0,0.4)',
          size: 11
        },
        tickFormatter: function (val) {
          if (val > 999) {
            return (val / 1000) + 'K';
          } else {
            return val;
          }
        }
      },
      legend: {
        labelBoxBorderColor: 'transparent',
      }
    };

    $scope.plotRevenueData = [{
      data: [
        [1, 1100],
        [2, 1400],
        [3, 1200],
        [4, 800],
        [5, 600],
        [6, 800],
        [7, 700],
        [8, 900],
        [9, 700],
        [10, 300]
      ],
      label: 'Revenues'
    }];

    $scope.plotRevenueOptions = {
      series: {

        // lines: {
        //     show: true,
        //     lineWidth: 1.5,
        //     fill: 0.1
        // },
        bars: {
          show: true,
          fill: 1,
          lineWidth: 0,
          barWidth: 0.6,
          align: 'center'
        },
        points: {
          show: false
        },
        shadowSize: 0
      },
      grid: {
        labelMargin: 10,
        hoverable: true,
        clickable: true,
        borderWidth: 0
      },
      tooltip: true,
      tooltipOpts: {
        defaultTheme: false,
        content: 'Revenue: %y'
      },
      colors: ['#b3bcc7'],
      xaxis: {
        tickColor: 'transparent',
        //min: -0.5,
        //max: 2.7,
        tickDecimals: 0,
        autoscaleMargin: 0,
        font: {
          color: 'rgba(0,0,0,0.4)',
          size: 11
        }
      },
      yaxis: {
        ticks: 4,
        tickDecimals: 0,
        tickColor: 'rgba(0,0,0,0.04)',
        font: {
          color: 'rgba(0,0,0,0.4)',
          size: 11
        },
        tickFormatter: function (val) {
          if (val > 999) {
            return '$' + (val / 1000) + 'K';
          } else {
            return '$' + val;
          }
        }
      },
      legend: {
        labelBoxBorderColor: 'transparent'
      }
    };

    $scope.currentPage = 1;
    $scope.itemsPerPage = 7;

    $scope.accountsInRange = function () {
      return this.userAccounts.slice(this.currentPage * 7, this.currentPage * 7 + 7);
    };

    $scope.uaHandle = function ($index) {
      // console.log(ua);
      this.userAccounts.splice($index, 1);
    };

    $scope.uaHandleSelected = function () {
      this.userAccounts = _.filter(this.userAccounts, function (item) {
        return (item.rem === false || item.rem === undefined);
      });
    };

    var avatars = ['potter.png', 'tennant.png', 'johansson.png', 'jackson.png', 'jobs.png'];
    $scope.userAccounts = [{
      'picture': _.shuffle(avatars).shift(),
      'name': 'Foreman Bullock',
      'email': 'foremanbullock@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Alberta Ochoa',
      'email': 'albertaochoa@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Terry Hahn',
      'email': 'terryhahn@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Donovan Doyle',
      'email': 'donovandoyle@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Stacie Blankenship',
      'email': 'stacieblankenship@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Julie Nunez',
      'email': 'julienunez@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Lacey Farrell',
      'email': 'laceyfarrell@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Stacy Cooke',
      'email': 'stacycooke@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Teri Frost',
      'email': 'terifrost@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Dionne Payne',
      'email': 'dionnepayne@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Kaufman Garrison',
      'email': 'kaufmangarrison@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Curry Avery',
      'email': 'curryavery@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Carr Sharp',
      'email': 'carrsharp@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Cooper Scott',
      'email': 'cooperscott@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Juana Spencer',
      'email': 'juanaspencer@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Madelyn Marks',
      'email': 'madelynmarks@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Bridget Ray',
      'email': 'bridgetray@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Santos Christensen',
      'email': 'santoschristensen@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Geneva Rivers',
      'email': 'genevarivers@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Carmella Bond',
      'email': 'carmellabond@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Duke Munoz',
      'email': 'dukemunoz@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Ramos Rasmussen',
      'email': 'ramosrasmussen@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Maricela Sweeney',
      'email': 'maricelasweeney@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Carmen Riley',
      'email': 'carmenriley@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Whitfield Hartman',
      'email': 'whitfieldhartman@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Jasmine Keith',
      'email': 'jasminekeith@geemail.com'
    }, {
      'picture': _.shuffle(avatars).shift(),
      'name': 'Baker Juarez',
      'email': 'bakerjuarez@geemail.com'
    }];

    $scope.drp_start = moment().subtract(1, 'days').format('MMMM D, YYYY');
    $scope.drp_end = moment().add(31, 'days').format('MMMM D, YYYY');
    $scope.drp_options = {
      ranges: {
        'Today': [moment(), moment()],
        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
        'This Month': [moment().startOf('month'), moment().endOf('month')],
        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
      },
      opens: 'left',
      startDate: moment().subtract(29, 'days'),
      endDate: moment()
    };

    $scope.epDiskSpace = {
      animate: {
        duration: 0,
        enabled: false
      },
      barColor: '#e6da5c',
      trackColor: '#ebedf0',
      scaleColor: false,
      lineWidth: 5,
      size: 100,
      lineCap: 'circle'
    };

    $scope.epBandwidth = {
      animate: {
        duration: 0,
        enabled: false
      },
      barColor: '#d95762',
      trackColor: '#ebedf0',
      scaleColor: false,
      lineWidth: 5,
      size: 100,
      lineCap: 'circle'
    };

    $scope.mapspace = {
      animate: {
        duration: 0,
        enabled: false
      },
      barColor: '#ef553a',
      trackColor: '#ebedf0',
      scaleColor: false,
      lineWidth: 3,
      size: 75,
      lineCap: 'circle'
    };
  }]);