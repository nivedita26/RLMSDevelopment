(function () {
    'use strict';
     angular.module('rlmsApp').service('utility',['$http','$q','pinesNotifications',function($http,$q,pinesNotifications){
     	var utility = this;
     	utility.showMessage = function(title,msg,type){
     		pinesNotifications.notify({
     	        title: title,
     	        text: msg,
     	        type: type,
     	        hide: false
     	      });
     	};
     	utility.parseInteger = function(num){
     		if(parseInt(num) != NaN){
     			return parseInt(num); 
     		}else{
     			return num;
     		}
     	}
     }]);
})();
