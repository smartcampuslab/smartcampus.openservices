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
    when('/user', {
      controller: 'userCtrl',
      templateUrl: '/partials/user.html'
    }).
    otherwise({
      redirectTo: '/'
    });
  }
]);
