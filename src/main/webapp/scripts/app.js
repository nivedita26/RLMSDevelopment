angular
  .module('rlmsApp', [
    'theme',
    'theme.demos',
    'base64',
    'ngStorage',
    'angular-locker'
  ])
  .config(['$provide', '$routeProvider', function($provide, $routeProvider) {
    'use strict';
    $routeProvider
      .when('/', {
        templateUrl: 'views/index.html',
        resolve: {
          loadCalendar: ['$ocLazyLoad', function($ocLazyLoad) {
            return $ocLazyLoad.load([
              'bower_components/fullcalendar/fullcalendar.js',
            ]);
          }]
        }
      })
      .when('/:templateFile', {
        templateUrl: function(param) {
          return 'views/' + param.templateFile + '.html';
        }
      })
      .when('#', {
        templateUrl: 'views/add-company.html',
      })
      .otherwise({
        redirectTo: '#/'
      });
  }])
  .run(['$http','$rootScope','$localStorage','locker',function($http,$rootScope,$localStorage,locker) {
	  $http({
		  method: 'POST',
		  url: '/RLMS/getLoggedInUser'
		}).then(function successCallback(response) {
		    console.log(response);
		    $rootScope.loggedInUserInfo = response;
		    $rootScope.showDasboardForInditech=false;
		    $rootScope.showDasboardForOthers=false;
		    if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1 || $rootScope.loggedInUserInfoForDashboard.data.userRole.rlmsSpocRoleMaster.roleLevel == 2){
	    		$rootScope.showDasboardForInditech= true;
	    		$rootScope.showDasboardForOthers=false;
	    	}else{
	    		$rootScope.showDasboardForOthers=true;
	    		$rootScope.showDasboardForInditech=false;
	    	}	
		  }, function errorCallback(response) {
		  });
  }]);;