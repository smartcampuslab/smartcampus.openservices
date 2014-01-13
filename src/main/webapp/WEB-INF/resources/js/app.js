'use strict';
var app = angular.module('openservices', ['ngRoute', 'ngCookies', 'openservices.directives', 'openservices.services']);
app.config(['$routeProvider', '$locationProvider', '$httpProvider',
  function ($routeProvider, $locationProvider, $httpProvider) {
    var access = routingConfig.accessLevels;
    
    $locationProvider.html5Mode(true);
    
    $httpProvider.interceptors.push(function($q, $location) {
        return {
            'responseError': function(response) {
                if(response.status === 401 || response.status === 403) {
                    $location.path('signin');
                    return $q.reject(response);
                }
                else {
                    return $q.reject(response);
                }
            }
        }
    });



    
    $routeProvider.
    when('/', {
      controller: 'homeCtrl',
      templateUrl: 'partials/home.html',
      access: access.anon
    }).
    when('/callback', {
      controller: 'cbCtrl',
      templateUrl: 'partials/cb.html',
      access: access.anon
    }).
    when('/services', {
      controller: 'servicesCtrl',
      templateUrl: 'partials/services/list.html',
      access: access.anon
    }).
    when('/services/:id', {
      controller: 'serviceCtrl',
      templateUrl: 'partials/services/show.html',
      access: access.anon
    }).
    when('/categories', {
      controller: 'categoriesCtrl',
      templateUrl: 'partials/categories.html',
      access: access.anon
    }).
    when('/signin', {
      controller: 'signinCtrl',
      templateUrl: 'partials/signin.html',
      access: access.anon
    }).
    when('/profile', {
      controller: 'profileCtrl',
      templateUrl: 'partials/profile/show.html',
      access: access.ROLE_NORMAL
    }).
    when('/profile/edit', {
      controller: 'profileCtrl',
      templateUrl: 'partials/profile/edit.html',
      access: access.ROLE_NORMAL
    }).
    when('/profile/services/new', {
      controller: 'newServiceCtrl',
      templateUrl: 'partials/profile/services/edit.html',
      access: access.ROLE_NORMAL
    }).
    when('/profile/services/:id/edit', {
      controller: 'editServiceCtrl',
      templateUrl: 'partials/profile/services/edit.html',
      access: access.ROLE_NORMAL
    }).
    when('/profile/organizations/new', {
      controller: 'newOrgCtrl',
      templateUrl: 'partials/profile/organizations/edit.html',
      access: access.ROLE_NORMAL
    }).
    when('/profile/organizations/:id/edit', {
      controller: 'editOrgCtrl',
      templateUrl: 'partials/profile/organizations/edit.html',
      access: access.ROLE_NORMAL
    }).
    otherwise({
      redirectTo: '/'
    });
  }
]).run(function ($rootScope, $location, Auth) {
$rootScope.logout = function(){
	console.log('logout')
	Auth.logout(function(){
		$location.path("/")
	});
	
}
  var history = [];

  $rootScope.$on('$routeChangeStart', function (event, next) {
    history.push($location.$$path);
    $rootScope.error = null;

    if (Auth.isLoggedIn() && next.originalPath === '/signin') {
    	console.log('redirecting from signin to profile because already loggedin')
      $location.path('/profile');
    }
    if (!Auth.authorize(next.access)) {
      if (Auth.isLoggedIn()) {
    	  console.log('user already loggedin, redirecting to next path', next.originalPath)
        $location.path(next.originalPath);
      } else {
        console.log('signin');
        $location.path('/signin');
      }
    }

  });
  $rootScope.back = function () {
    var prevUrl = history.length > 1 ? history.splice(-2)[0] : '/';
    $location.path(prevUrl);
  };
});
