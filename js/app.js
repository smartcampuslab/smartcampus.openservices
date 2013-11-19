'use strict';
var app = angular.module('openservices', ['ngRoute', 'openservices.directives']);
app.config(['$routeProvider',
  function ($routeProvider) {
    $routeProvider.
    when('/', {
      controller: 'homeCtrl',
      templateUrl: '/partials/home.html'
    }).
    when('/categories', {
      controller: 'homeCtrl',
      templateUrl: '/partials/categories.html'
    }).
    otherwise({
      redirectTo: '/'
    });
  }
]);
