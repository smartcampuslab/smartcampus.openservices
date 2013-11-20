'use strict';
app.controller('homeCtrl', ['$scope', '$http',
  function ($scope, $http) {}
]);

app.controller('signinCtrl', ['$scope', '$http', '$location', '$cookies',
  function ($scope, $http, $location, $cookies) {
    $scope.signin = function () {
      $http.get('/data/user.json').success(function (user) {
        $cookies.user = JSON.stringify(user);
        $location.path('/user');
      });
    };
  }
]);

app.controller('userCtrl', ['$scope', '$http', '$cookies',
  function ($scope, $http, $cookies) {
    $scope.user = JSON.parse($cookies.user);
  }
]);
