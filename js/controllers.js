'use strict';
app.controller('homeCtrl', ['$scope', '$http',
  function ($scope, $http) {}
]);

app.controller('categoriesCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $http.get('/data/categories.json').success(function (categories) {
      $scope.categories = categories;
    });
  }
]);
