(function () {
    'use strict';
     angular.module('rlmsApp').service('serviceApi',['$http','$q',function($http,$q){
     	var serviceArr = this;
     	serviceArr.doPostWithData = function(url,data){
     		var deferred = $q.defer();
     		$http.post(url, data)
	            .success(function (response, status, headers, responseConfig) {
	            	deferred.resolve(response);
	            })
	            .error(function (error, status, headers, config) {
	            	deferred.reject(error);
	            });
     		return deferred.promise;
     	};
     	serviceArr.doPostWithoutData = function(url){
     		var deferred = $q.defer();
     		$http.post(url)
	            .success(function (response, status, headers, responseConfig) {
	            	deferred.resolve(response);
	            })
	            .error(function (error, status, headers, config) {
	            	deferred.reject("Error occured while making ajax call");
	            });
     		return deferred.promise;
     	};
     	serviceArr.doGetWithoutData = function(url){
     		var deferred = $q.defer();
     		$http.get(url)
	            .success(function (response, status, headers, responseConfig) {
	            	deferred.resolve(response);
	            })
	            .error(function (error, status, headers, config) {
	            	deferred.reject("Error occured while making ajax call");
	            });
     		return deferred.promise;
     	};
     }]);
})();
