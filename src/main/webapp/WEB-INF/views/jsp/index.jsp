<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en" ng-app="openservices">

<head>
  <title>Open Services</title>
  <base href="/openservice/" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <!-- 
  <link rel="stylesheet" href="resources/css/bootstrap.min.css" />
  <link rel="stylesheet" href="resources/css/openservices.css" />
  <link rel="stylesheet" href="resources/css/font-awesome.min.css" />
  <link rel="stylesheet" href="resources/css/animate.min.css" />
  <link rel="stylesheet" href="resources/css/prism.css" /> 
   -->
  <link rel="stylesheet" href="css/bootstrap.min.css" />
  <link rel="stylesheet" href="css/openservices.css" />
  <link rel="stylesheet" href="css/font-awesome.min.css" />
  <link rel="stylesheet" href="css/animate.min.css" />
  <link rel="stylesheet" href="css/prism.css" /> 
  
  
</head>

<body>
  <div id="wrap">
    <nav class="navbar navbar-default" role="navigation">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">Open Services</a>
      </div>

      <div ng-controller="navCtrl" class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li class="active"><a href="#">Whishlist</a>
          </li>
          <li><a href="#">Apps</a>
          </li>
          <li><a href="services">Services</a>
          </li>
          <li><a href="categories">Categories</a>
          </li>
          <li><a ng-click="logout()" href="">Categories</a>
          </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li gravatar><a href="signin">Sign in</a> 
          </li>
        </ul>
      </div>
      <!-- /.navbar-collapse -->
    </nav>
    <div class="view" ng-view></div>
  </div>

  <div id="footer">
    <div class="container">
      <p class="text-muted credit">&copy; 2013 Smart Campus Lab &middot; <a href="#">Privacy</a> &middot; <a href="#">Terms</a>
      </p>
    </div>
  </div>
  
  <script src="js/vendor/underscore.min.js"></script>
  <script src="js/vendor/jquery-2.0.3.min.js"></script>
  <script src="js/vendor/bootstrap.min.js"></script>
  <script src="js/vendor/holder.js"></script>
  <script src="js/vendor/prism.js"></script>
  <script src="http://connect.facebook.net/en_US/all.js"></script>

  <script src="js/vendor/angular.min.js"></script>
  <script src="js/vendor/angular-route.min.js"></script>
  <script src="js/vendor/angular-resource.min.js"></script>
  <script src="js/vendor/angular-cookies.min.js"></script>
  <script src="js/routingConfig.js"></script>
  <script src="js/app.js"></script>
  <script src="js/controllers.js"></script>
  <script src="js/directives.js"></script>
  <script src="js/services.js"></script>

</body>

</html>
