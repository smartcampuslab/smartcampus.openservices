'use strict';
app.controller('homeCtrl', ['$scope', '$http',
  function ($scope, $http) {}
]);

app.controller('signinCtrl', ['$scope', '$http', '$location', 'Auth',
  function ($scope, $http, $location, Auth) {

    $scope.signin = function (service) {
      Auth.login(service, function () {
        $location.path('/profile');
      });

    };
  }
]);

app.controller('profileCtrl', ['$scope', '$http', '$location', 'User', '$rootScope',
  function ($scope, $http, $location, User) {
    $scope.template = 'partials/profile/_details.html';
    ////User.getUserinfo({
    //  email: $rootScope.user.email
    //});
    $scope.deleteOrg = function (i) {
      $scope.user.orgs.splice(i, 1);
    };

    $scope.deleteService = function (i) {
      $scope.services.splice(i, 1);
    };

    $scope.submit = function () {
      $location.path('/profile');
    };

    $http.get('/data/user.json').success(function (user) {
      $scope.user = user;
    });

    $http.get('/data/services.json').success(function (services) {
      $scope.services = services;
    });
  }
]);

app.controller('newServiceCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.title = 'New';
    $scope.policies = ['public', 'private'];
    $scope.service = {
      license: 'somelicense'
    };
    $http.get('/data/user.json').success(function (user) {
      $scope.orgs = user.orgs;
    });

    $http.get('/data/categories.json').success(function (cats) {
      $scope.cats = cats;
    });

    $scope.submit = function () {
      $location.path('/profile');
    };
  }
]);

app.controller('editServiceCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.title = 'Edit';
    $scope.policies = ['public', 'private'];
    $http.get('/data/user.json').success(function (user) {
      $scope.orgs = user.orgs;
    });
    $http.get('/data/service.json').success(function (service) {
      $scope.service = service;
    });
    $http.get('/data/categories.json').success(function (cats) {
      $scope.cats = cats;
    });
    $scope.submit = function () {
      $location.path('/profile');
    };
  }
]);

app.controller('newOrgCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.title = 'New';
    $scope.submit = function () {
      $location.path('/profile');
    };

  }
]);

app.controller('editOrgCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.title = 'Edit';
    $http.get('/data/user.json').success(function (user) {
      $scope.org = user.orgs[0];
    });
    $scope.submit = function () {
      $location.path('/profile');
    };
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

app.controller('servicesCtrl', ['$scope', '$http',
  function ($scope, $http) {
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

app.controller('serviceCtrl', ['$scope', '$http',
  function ($scope, $http) {
    function toTitleCase(str) {
      return str.replace(/(?:^|-)\w/g, function (match) {
        return match.toUpperCase();
      });
    }

    $scope.request = {
      method: 'GET /aac/basicprofile/me HTTPS/1.1',
      endpoint: 'vas-dev.smartcampuslab.it',
      headers: [{
        type: 'Accept',
        value: 'application/json'
      }, {
        type: 'Authorization',
        value: 'Bearer {user access token}'
      }]
    };
    // $scope.$watch('request', function () {
    //   $scope.parsedrequest = JSON.stringify(angular.copy($scope.request), null, 2)
    // }, true)
    $scope.send = function () {
      $http({
        method: 'GET',
        url: '/data/vas.json'
      }).success(function (data, status, headers) {
        $scope.response = 'HTTP/1.1 ' + status + '\n';
        for (var key in headers()) {
          $scope.response += toTitleCase(key) + ': ' + headers()[key] + '\n';
        }
        $scope.response += '\n' + JSON.stringify(data, null, 2);
      });
    };

    $scope.addheader = function () {
      $scope.request.headers.push({
        type: '',
        value: ''
      });
    };

    $scope.removeheader = function (index) {
      $scope.request.headers.splice(index, 1);
    };
  }
]);
