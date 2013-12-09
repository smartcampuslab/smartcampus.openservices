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

    $scope.deleteOrg = function (i) {
      $scope.user.orgs.splice(i, 1)
    };

    $scope.deleteService = function (i) {
      $scope.services.splice(i, 1)
    };

    $scope.submit = function () {
      $location.path('/profile')
    }

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
    $scope.title = "New"
    $scope.policies = ["public", "private"]
    $scope.service = {
      license: 'The MIT License (MIT)\
\
Copyright (c) <year> <copyright holders>\
\
Permission is hereby granted, free of charge, to any person obtaining a copy\
of this software and associated documentation files (the "\
      Software "), to deal\
in the Software without restriction, including without limitation the rights\
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\
copies of the Software, and to permit persons to whom the Software is\
furnished to do so, subject to the following conditions:\
\
The above copyright notice and this permission notice shall be included in\
all copies or substantial portions of the Software.\
\
THE SOFTWARE IS PROVIDED "\
      AS IS ", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\
THE SOFTWARE.'
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
    $scope.title = "Edit"
    $scope.policies = ["public", "private"]
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
    $scope.title = "New"
    $scope.submit = function () {
      $location.path('/profile')
    }

  }
]);

app.controller('editOrgCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.title = "Edit"
    $http.get('/data/user.json').success(function (user) {
      $scope.org = user.orgs[0];
    });
    $scope.submit = function () {
      $location.path('/profile')
    }
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

app.controller('serviceCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {

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
    }
    // $scope.$watch('request', function () {
    //   $scope.parsedrequest = JSON.stringify(angular.copy($scope.request), null, 2)
    // }, true)
    $scope.send = function () {
      $http({
        method: 'GET',
        url: '/data/vas.json'
      }).success(function (data, status, headers) {
        var tmp = {};
        tmp.headers = headers()
        tmp.body = data
        $scope.response = JSON.stringify(tmp, null, 2);
      })
    }

    $scope.addheader = function () {
      $scope.request.headers.push({
        type: '',
        value: ''
      })
    }

    $scope.removeheader = function (index) {
      $scope.request.headers.splice(index, 1);
    }
  }
]);
