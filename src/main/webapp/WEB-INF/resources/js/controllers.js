'use strict';
app.controller('homeCtrl', ['$scope', '$http',
  function ($scope, $http) {}
]);

app.controller('signinCtrl', ['$scope', '$location', 'Auth',
  function ($scope, $location, Auth) {

    $scope.signin = function (service) {
      Auth.login($scope.user, function () {
        $location.path('profile');
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
      $location.path('profile');
    };

    $http.get('data/user.json').success(function (user) {
      $scope.user = user;
    });

    $http.get('data/services.json').success(function (services) {
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
    $http.get('data/user.json').success(function (user) {
      $scope.orgs = user.orgs;
    });

    $http.get('data/categories.json').success(function (cats) {
      $scope.cats = cats;
    });

    $scope.submit = function () {
      $location.path('profile');
    };
  }
]);

app.controller('editServiceCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.title = 'Edit';
    $scope.policies = ['public', 'private'];
    $http.get('data/user.json').success(function (user) {
      $scope.orgs = user.orgs;
    });
    $http.get('data/service.json').success(function (service) {
      $scope.service = service;
    });
    $http.get('data/categories.json').success(function (cats) {
      $scope.cats = cats;
    });
    $scope.submit = function () {
      $location.path('profile');
    };
  }
]);

app.controller('newOrgCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.title = 'New';
    $scope.submit = function () {
      $location.path('profile');
    };

  }
]);

app.controller('editOrgCtrl', ['$scope', '$http', '$location',
  function ($scope, $http, $location) {
    $scope.title = 'Edit';
    $http.get('data/user.json').success(function (user) {
      $scope.org = user.orgs[0];
    });
    $scope.submit = function () {
      $location.path('profile');
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

    $http.get('data/categories.json').success(function (categories) {
      $scope.categories = categories;
      $scope.categoriesRows = $scope.getCategoriesRows(categories, 4);
    });

    $scope.setCategoryActive = function (category) {
      $scope.categoryActive = category;
      $location.path('services');
    };
  }
]);

app.controller('servicesCtrl', ['$scope', '$http',
  function ($scope, $http) {
    if ( !! $scope.categoryActive) {
      $scope.categoryActive = undefined;
    }

    $http.get('api/service/view').success(function (services) {
      console.log(services);
      $scope.services = services.services;
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

app.controller('cbCtrl', ['$location',
  function ($location) {
    function parseKeyValue(keyValue) {
      var obj = {}, key_value, key;
      angular.forEach((keyValue || "").split('&'), function (keyValue) {
        if (keyValue) {
          key_value = keyValue.split('=');
          key = decodeURIComponent(key_value[0]);
          obj[key] = angular.isDefined(key_value[1]) ? decodeURIComponent(key_value[1]) : true;
        }
      });
      return obj;
    }

    var queryString = $location.url().substring(10); // preceding slash omitted
    var params = parseKeyValue(queryString);
    // TODO: The target origin should be set to an explicit origin.  Otherwise, a malicious site that can receive
    //       the token if it manages to change the location of the parent. (See:
    //       https://developer.mozilla.org/en/docs/DOM/window.postMessage#Security_concerns)

    window.opener.postMessage(params, "*");
    window.close();
  }
]);

app.controller('serviceCtrl', ['$scope', '$http', '$cookieStore', '$location', 'oAuth',
  function ($scope, $http, $cookieStore, $location, oAuth) {
    oAuth.config.clientId = 'fcb1cb81-50a7-4948-8f46-05a1f14e7089';
    oAuth.config.scopes = ["smartcampus.profile.basicprofile.me"]

    $scope.getToken = function () {
      oAuth.config.authorizationEndpoint = $scope.request.endpoint + $scope.request.method.authdescriptor.authUrl;
      oAuth.getToken(function (data) {
        console.log(data)
        $scope.request.sample.headers.Authorization = 'Bearer ' + data.access_token
        // $http({
        //   method: 'POST',
        //   url: $scope.request.endpoint + $scope.request.method.authdescriptor.validationUrl,
        //   params: {
        //     grant_type: oAuth.config.grant_type,
        //     code: data.code,
        //     client_id: oAuth.config.clientId,
        //     redirect_uri: 'http://localhost/callback'
        //   }
        // }).success(function (data) {
        //   console.log('token', data)
        // }).error(function (err) {
        //   console.log(err)
        // })
      });

    }

    $scope.request = {};
    $http.get("data/service.json").success(function (data) {
      $scope.service = data;
    })

    function toTitleCase(str) {
      return str.replace(/(?:^|-)\w/g, function (match) {
        return match.toUpperCase();
      });
    }

    if ($cookieStore.get('token') == undefined) {

      if ($location.hash() != "") {
        $http.post($scope.request.method.authdescriptor.validationUrl + $location.hash().match(/#(.*)/)[1]).
        success(function (data) {
          console.log(data)
          $cookieStore.put('token', data);
          $scope.token = JSON.stringify(token, undefined, 2);
        });
      }
    }

    // $scope.$watch('request', function () {
    //   $scope.parsedrequest = JSON.stringify(angular.copy($scope.request), null, 2)
    // }, true)
    $scope.checkBeforeSend = function () {
      //&& request.method.authdescriptor && !request.sample.headers['Authorization']
      if ($scope.request.method && $scope.request.endpoint && $scope.request.sample && !$scope.request.method.authdescriptor.type) {
        return true
      } else if ($scope.request.method && $scope.request.endpoint && $scope.request.sample && $scope.request.method.authdescriptor.type && $scope.request.sample.headers['Authorization']) {
        return true
      } else {
        return false
      }
    }
    $scope.checkBeforeToken = function () {
      //&& request.method.authdescriptor && !request.sample.headers['Authorization']
      if ($scope.request.method && $scope.request.endpoint && $scope.request.sample && $scope.request.method.authdescriptor) {
        return true
      } else {
        return false
      }
    }

    $scope.send = function () {
      console.info($scope.request.sample.headers);
      $http({
        method: $scope.request.method.type,
        url: $scope.request.endpoint + $scope.request.method.url,
        data: $scope.request.sample.body,
        headers: $scope.request.sample.headers,
        withCredentials: true
      }).success(function (data, status, headers) {
        $scope.response = 'HTTP/1.1 ' + status + '\n';
        for (var key in headers()) {
          $scope.response += toTitleCase(key) + ': ' + headers()[key] + '\n';
        }
        $scope.response += '\n' + JSON.stringify(data, null, 2);
      }).error(function (err) {
        console.log(err)
      });
    };

  }
]);
