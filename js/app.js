'use strict';
var app = angular.module('openservices', ['ngRoute', 'ngCookies', 'ngAnimate', 'openservices.directives']);
app.config(['$routeProvider', '$locationProvider',
  function ($routeProvider, $locationProvider) {
    $locationProvider
      .html5Mode(true);
    $routeProvider.
    when('/', {
      controller: 'homeCtrl',
      templateUrl: '/partials/home.html'
    }).
    when('/services', {
      controller: 'servicesCtrl',
      templateUrl: '/partials/services.html'
    }).
    when('/categories', {
      controller: 'categoriesCtrl',
      templateUrl: '/partials/categories.html'
    }).
    when('/signin', {
      controller: 'signinCtrl',
      templateUrl: '/partials/signin.html'
    }).
    when('/profile', {
      controller: 'profileCtrl',
      templateUrl: '/partials/profile/show.html'
    }).
     when('/profile/edit', {
      controller: 'profileCtrl',
      templateUrl: '/partials/profile/edit.html'
    }).
    when('/profile/services/new', {
      controller: 'newServiceCtrl',
      templateUrl: '/partials/profile/services/edit.html'
    }).
    when('/profile/services/:id/edit', {
      controller: 'editServiceCtrl',
      templateUrl: '/partials/profile/services/edit.html'
    }).
    when('/profile/organizations/new', {
      controller: 'newOrgCtrl',
      templateUrl: '/partials/profile/organizations/edit.html'
    }).
    when('/profile/organizations/:id/edit', {
      controller: 'editOrgCtrl',
      templateUrl: '/partials/profile/organizations/edit.html'
    }).
    otherwise({
      redirectTo: '/'
    });
  }
]).run(function($rootScope, $location){
    var history = [];

    $rootScope.$on('$routeChangeSuccess', function() {
        history.push($location.$$path);
    });
    $rootScope.back = function () {
        var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";
        $location.path(prevUrl);
    };
})