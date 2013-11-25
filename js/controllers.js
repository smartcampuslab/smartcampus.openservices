'use strict';
app.controller('homeCtrl', ['$scope', '$http',
  function ($scope, $http) {}
]);

app.controller('signinCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.signin = function () {
      $location.path('/profile');
    };
  }
]);

app.controller('profileCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.template = 'partials/profile/_details.html';

    $http.get('/data/user.json').success(function (user) {
      $scope.user = user;
    });
  }
]);

app.controller('categoriesCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.getCategoriesRows = function (categories, size) {
      var rows = [];

      if ( !! categories) {
        var counter = 0;
        var buffer = [];
        for (var i = 0; i < categories.length; i++) {
          var category = categories[i];
          buffer.push(category);
          counter++;
          if (counter === size || (i + 1) === categories.length) {
            rows.push(buffer);
            buffer = [];
            counter = 0;
          }
        }
      }
      return rows;
    };

    $http.get('/data/categories.json').success(function (categories) {
      $scope.categories = categories;
      $scope.categoriesRows = $scope.getCategoriesRows(categories, 4);
    });

    $scope.setCategoryActive = function (category) {
      $scope.categoryActive = category;
      $location.path('/services');
    };
  }
]);

app.controller('servicesCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    if ( !! $scope.categoryActive) {
      $scope.categoryActive = undefined;
    }

    $http.get('/data/services.json').success(function (services) {
      $scope.services = services;
    });

    $scope.servicesActive = [];

    $scope.isServiceActive = function (service) {
      return $scope.servicesActive.indexOf(service) > -1;
    };

    $scope.toggleServiceActive = function (service) {
      var index = $scope.servicesActive.indexOf(service);
      if (index === -1) {
        // add to servicesActive
        $scope.servicesActive.push(service);
      } else {
        // remove
        $scope.servicesActive.splice(index, 1);
      }
    };
  }
]);
