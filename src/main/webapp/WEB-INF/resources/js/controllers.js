'use strict';
app.controller('homeCtrl', ['$scope', '$http',
  function ($scope, $http) {}
]);

app.controller('navCtrl', ['$scope', '$http','Auth', '$location',
                            function ($scope, $http, Auth, $location) {
	$scope.logout = function(){
		console.log('loggingout')
		Auth.logout(function(){
			console.log('logged out')
			$location.path('/')
		});
	}
}
                          ]);

app.controller('signinCtrl', ['$scope', '$location', 'Auth',
  function ($scope, $location, Auth) {

    $scope.signin = function (service) {
    	console.log('trying to login')
      Auth.login($scope.user, function () {
        $location.path('profile');
      });
    };
  }
]);

app.controller('signUpCtrl', ['$scope', '$location', 'User',
   function ($scope, $location, User) {
	$scope.user = new User();
    $scope.submit = function () {
    	$scope.user.$save($scope.user,function(){
    		$location.path('signin')
    	})
    };
}
]);

app.controller('profileCtrl', ['$scope', '$http', '$location', 'User', 'Service', 'Org', 
  function ($scope, $http, $location, User, Service, Org) {
    $scope.template = 'partials/profile/_details.html';

    $scope.deleteOrg = function (i) {
      Org.delete({id:$scope.orgs[i].id}, function() {
    	  console.log('org deleted');
    	  $scope.orgs.splice(i, 1);
    	  $location.path('profile')
      });
    };
    $scope.modifyOrg = function (i) {
        Org.update($scope.orgs[i], function() {
      	  console.log('org updated');
      	  $location.path('profile')
        });
      };

    $scope.deleteService = function (i) {
      $scope.services.splice(i, 1);
    };

    $scope.submit = function () {
            $scope.user.$update($scope.user, function() {
            	console.log('user updated')
                $location.path('profile')
            });
    };
    


    User.getInfo({}, function(data) {
    	$scope.user = data;
    });
    
    Org.get({}, function(data) {
    	console.log('getting orgs',data)
        $scope.orgs = data.orgs;
    });
    Service.get({}, function(data) {
    	console.log('getting services',data)
        $scope.services = data.services;
    });
    
    
  }
]);

app.controller('newServiceCtrl', ['$scope', '$http', '$location', 'Service', 'Org',
  function ($scope, $http, $location, Service, Org) {

    Org.get({}, function(data) {
    	console.log('getting orgs',data)
        $scope.orgs = data.orgs;
    });

    $scope.submit = function(){
    	console.log('saving service');
    	if ($scope.service.expiration) {
    		$scope.service.expiration = new Date($scope.service.expiration).getTime();
    	}
    	Service.create($scope.service,function(){
    		$location.path('profile');
    	});
    }
  }
]);

app.controller('editServiceCtrl', ['$scope', '$routeParams', '$location', 'Service',
  function ($scope, $routeParams, $location, Service) {
	$scope.service = Service.getDescription({id: $routeParams.id})
  }
]);

app.controller('newOrgCtrl', ['$scope', '$http', '$location', 'Org',
  function ($scope, $http, $location, Org) {
    $scope.title = 'New';
    $scope.submit = function () {
        Org.create($scope.org, function() {
        	console.log('org created')
            $location.path('profile')
        });
    };

  }
]);

app.controller('editOrgCtrl', ['$scope', '$http', '$location', '$routeParams', 'Org',
  function ($scope, $http, $location, $routeParams, Org) {
    $scope.title = 'Edit';
    Org.getById({id:$routeParams.id}, function (org) {
      $scope.org = org;
    });
    $scope.submit = function () {
    	Org.update($scope.org, function() {
    		console.log('org updated');
    	    $location.path('profile');
    	});
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
