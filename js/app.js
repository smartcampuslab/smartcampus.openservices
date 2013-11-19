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
    when('/categories', {
      controller: 'homeCtrl',
      templateUrl: '/partials/categories.html'
    }).
    when('/login', {
      controller: 'loginCtrl',
      templateUrl: '/partials/login.html'
    }).
    otherwise({
      redirectTo: '/'
    });
  }
]);
