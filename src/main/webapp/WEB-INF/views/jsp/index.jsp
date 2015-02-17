<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en" ng-app="openservices">

<head>
  <title>Open Services</title>
  <base href="<%=request.getContextPath() %>/" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <!-- 
  <link rel="stylesheet" href="resources/css/bootstrap.min.css" />
  <link rel="stylesheet" href="resources/css/openservices.css" />
  <link rel="stylesheet" href="resources/css/font-awesome.min.css" />
  <link rel="stylesheet" href="resources/css/animate.min.css" />
  <link rel="stylesheet" href="resources/css/prism.css" /> 
   -->
  <link rel="stylesheet" href="css/bootstrap.min.css" />
  <link rel="stylesheet" href="css/font-awesome.min.css" />
  <link rel="stylesheet" href="css/animate.min.css" />
  <link rel="stylesheet" href="css/datepicker3.css" />  
  <link rel="stylesheet" href="css/railscasts.css">
  <link rel="stylesheet" href="css/strength.css">
  <link rel="stylesheet" href="css/openservices.css" />
</head>

<body>
  <div class="container">
    <div class="row">
      <div class="col-md-8 col-md-offset-2">
        <h1>Open Services</h1>
      </div>
    </div>    
  </div>
  <nav class="navbar navbar-default" role="navigation">
    <div class="container">
	    <div class="row">
	      <div class="col-md-8 col-md-offset-2">
		      <div class="navbar-header">
		        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
		          <span class="sr-only">Toggle navigation</span>
		          <span class="icon-bar"></span>
		          <span class="icon-bar"></span>
		          <span class="icon-bar"></span>
		        </button>
            </ul>
		      </div>
		
		      <div ng-controller="navCtrl" id="bs-example-navbar-collapse-1">
		        <ul class="nav navbar-nav">
              <li ng-class="{active: loc[0] === ''}">
                <a class="nav-left" href="#">Home</a>
              </li>
		          <li ng-class="{active: loc[0] === 'services'}">
		            <a href="services">Services</a>
		          </li>
		          <!-- <li ng-class="{active: loc[0] === 'categories'}">
		            <a href="categories">Categories</a>
		          </li> -->
		          <li ng-class="{active: loc[0] === 'organizations'}"><a href="organizations">Organizations</a>
		          </li>
		        </ul>
		        <ul ng-include="navtemplate" class="nav navbar-nav navbar-right">
		
		        </ul>
		      </div>
		    </div>  
		  </div>    
    </div>
    <!-- /.navbar-collapse -->
  </nav>
  <!-- ng-hide="loc[0] === ''" -->
  <div class="container" id="bread" ng-hide="loc[0] === ''" >
    <div class="col-md-12">
    <div class="row">
      <div class="col-md-8 col-md-offset-2">
	    <ol ng-controller="breadCtrl" class="animated fadeIn breadcrumb">
		    <li ng-repeat="location in loc">
	        <a ng-href="{{loc.slice(0,$index+1).join('/')}}">{{locTitles[$index] ? locTitles[$index] : location}}</a>
	      </li>
      </ol>
      </div>
    </div>
    </div>
  </div>
  <div class="container main-container">
    <div class="col-md-12 view" ng-view></div>
  </div>

  <div id="footer">
    <div class="container">
	    <div class="row">
	      <div class="col-md-8 col-md-offset-2">
		      <p class="credit">&copy; 2015 Smart Community Lab &middot; <a href="#">Privacy</a> &middot; <a href="#">Terms</a>
		      </p>
	      </div>
	    </div>    
    </div>
  </div>
  
  <script src="js/vendor/underscore.min.js"></script>
  <script src="js/vendor/jquery-2.1.0.min.js"></script>
  <script src="js/vendor/bootstrap.min.js"></script>
  <script src="js/vendor/datepicker.js"></script>
  <script src="js/vendor/highlight.pack.js"></script>
  <script src="js/vendor/moment-with-langs.min.js"></script>
  
  <script src="js/vendor/angular.min.js"></script>
  <script src="js/vendor/angular-route.min.js"></script>
  <script src="js/vendor/angular-resource.min.js"></script>
  <script src="js/vendor/angular-cookies.min.js"></script>
  <script src="js/vendor/angular-highlight.min.js"></script>
  <script src="js/vendor/ui-bootstrap-tpls-0.10.0.min.js"></script>
  
  <script src="js/vendor/marked.min.js"></script>
  <script src="js/vendor/angular-marked.js"></script>

  <script src="js/routingConfig.js"></script>
  <script src="js/app.js"></script>
  <script src="js/controllers.js"></script>
  <script src="js/directives.js"></script>
  <script src="js/services.js"></script>
  <script src="js/remoteapi.js"></script>

</body>

</html>
