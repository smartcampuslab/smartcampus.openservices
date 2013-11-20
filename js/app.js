'use strict';
var app = angular.module('openservices', ['ngRoute', 'openservices.directives']);
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
      controller: 'homeCtrl',
      templateUrl: '/partials/services.html'
    }).
    when('/categories', {
      controller: 'homeCtrl',
      templateUrl: '/partials/categories.html'
    }).
    when('/signin', {
      controller: 'signinCtrl',
      templateUrl: '/partials/signin.html'
    }).
    otherwise({
      redirectTo: '/'
    });
  }
]);