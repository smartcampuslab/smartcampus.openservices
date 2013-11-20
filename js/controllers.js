'use strict';
app.controller('homeCtrl', ['$scope', '$http',
  function ($scope, $http) {}
]);

app.controller('signinCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.signin = function () {
      $location.path('/user');
    };
  }
]);

app.controller('userCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.template = 'profile';

    $http.get('/data/user.json').success(function (user) {
      $scope.user = user;
    });
  }
]);
