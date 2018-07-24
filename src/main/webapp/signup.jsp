<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>RLMS</title>
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
		<link rel="stylesheet" href="css/font-awesome.css" />
		<link rel="stylesheet" href="css/styles.css">
	</head>
	<body ng-app="signup">
		<div id="wrapper" class="focusedform">
			<div class="static-content-wrapper">
				<div class="static-content">
					<!-- ngView:  --><div id="wrap" ng-view="" class="mainview-animation animated ng-scope">
					<div class="verticalcenter" ng-controller="SignupPageController">
	<a href="#/"><img src="img/INDITECHLOGO.png" alt="Logo" class="brand" /></a>
	<div class="panel panel-primary">
		<div class="panel-body">
			<h2 class="text-center" style="margin-bottom: 25px;">Sign Up</h2>
			<form action="#" class="form-horizontal" style="margin-bottom: 0px !important;">
			<div class="form-group">
					<div class="col-sm-12">
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-user"></i></span>
							<input type="text" class="form-control" ng-model="signup.registrationId" id="registrationCode" placeholder="Registration Code">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-user"></i></span>
							<input type="text" class="form-control" ng-model="signup.userName" id="username" placeholder="Username">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-lock"></i></span>
							<input type="password" ng-model="signup.password" class="form-control" id="password" placeholder="Password">
						</div>
					</div>
				</div>
			</form>	
		</div>
		<div class="panel-footer">
			<div class="pull-left">
				<a href="#/index" class="btn btn-default">Cancel</a>
			</div>
			<div class="pull-right">
				<a href="#" ng-click="postSignup();" class="btn btn-success">Sign up</a>
			</div>
		</div>
	</div>
</div>
				</div> <!--wrap -->
			</div>
			<footer role="contentinfo" ng-show="!layoutLoading" class="">
				<div class="clearfix">
					<ul class="list-unstyled list-inline pull-left">
						<li>Telesist &copy; 2015</li>
					</ul>
					<button class="pull-right btn btn-default btn-sm hidden-print" back-to-top="" style="padding: 1px 10px;"><i class="fa fa-angle-up"></i></button>
				</div>
			</footer>
		</div>
	</div>
</div>
<script type="text/javascript">
angular.module("signup",[]).controller("SignupPageController",["$scope","$http",function($scope,$http){
	var signup={
			registrationId:'',
			userName:'',
			password:''	
	};
	$scope.postSignup = function(){
		$http.post("/RLMS/admin/registerUser",$scope.signup)
        .success(function (response, status, headers, responseConfig) {
        	alert("Registration Success");
        })
        .error(function (error, status, headers, config) {
        	alert("Registration Failed");
        });
	}
}]);
</script>
</body>
</html>