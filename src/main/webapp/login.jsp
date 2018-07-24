<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>RLMS</title>
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
		<link rel="stylesheet" href="css/font-awesome.css" />
		<link rel="stylesheet" href="css/styles.css">

	</head>
	<body class="login">
		<div id="wrapper" class="focusedform">
			<div class="static-content-wrapper">
				<div class="static-content">
					<!-- ngView:  --><div id="wrap" ng-view="" class="mainview-animation animated ng-scope"><div class="verticalcenter ng-scope" ng-controller="SignupPageController">
					<a href="#/"><img src="img/INDITECHLOGO.png" alt="Logo" class="brand"></a>
					<div class="panel panel-primary">
						<div class="panel-body">
							<h4 class="text-center" style="margin-bottom: 25px;">Just click log in!</h4>
							<form class="form-horizontal ng-pristine ng-valid" action="/RLMS/resources/j_spring_security_check" method="post">
								<div class="form-group">
									<label for="email" class="control-label col-sm-4" style="text-align: left;">Username</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="username" id="email" placeholder="Email">
									</div>
								</div>
								<div class="form-group">
									<label for="password" class="control-label col-sm-4" style="text-align: left;">Password</label>
									<div class="col-sm-8">
										<input type="password" class="form-control" name="password" id="password" placeholder="Password">
									</div>
								</div>
								<div class="clearfix">
									<div class="pull-right"><label><input type="checkbox" checked=""> Remember Me</label></div>
								</div>
								<button type="submit" href="#/" class="btn btn-primary btn-block ng-isolate-scope" data-pulsate="{reach:10, pause: 2000}" style="outline: rgba(38, 133, 238, 0) solid 2px; box-shadow: rgba(38, 133, 238, 0) 0px 0px 6px; outline-offset: 10px;">Log In</button>
							</form>
						</div>
						<div class="panel-footer">
							<a href="#/" class="pull-left btn btn-link" style="padding-left:0">Forgot password?</a>
							<div class="pull-right" onClick="signup();">
								<a class="btn btn-default">Signup</a>
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
			function signup(){
				window.location.pathname="RLMS/signup.jsp"
				
			}
		</script>
</body>
</html>