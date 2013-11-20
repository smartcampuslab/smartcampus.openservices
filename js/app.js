'use strict';
var app = angular.module('openservices', ['ngRoute', 'ngCookies', 'openservices.directives']);
app.config(['$routeProvider', '$locationProvider',
  function ($routeProvider, $locationProvider) {
    $locationProvider
      .html5Mode(true);
    $routeProvider.
    when('/', {
      controller: 'homeCtrl',
      templateUrl: '/partials/home.html'
    }).
    when('/categories', {
      controller: 'homeCtrl',
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
