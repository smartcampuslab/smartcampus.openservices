'use strict';
var services = angular.module('openservices.services', ['ngResource', 'ngCookies']);

services.factory('Auth', ['$rootScope', '$cookieStore',
  function ($http, $cookieStore) {
    var accessLevels = routingConfig.accessLevels,
      userRoles = routingConfig.userRoles,
      currentUser = $cookieStore.get('user') || {
        username: '',
        role: userRoles.public
      };

    $cookieStore.remove('user');

    function changeUser(user) {
      _.extend(currentUser, user);
    };

    return {
      authorize: function (accessLevel, role) {
        if (role === undefined)
          role = currentUser.role;

        return accessLevel.bitMask & role.bitMask;
      },
      isLoggedIn: function (user) {
        if (user === undefined)
          user = currentUser;
        return user.role.title == userRoles.user.title || user.role.title == userRoles.admin.title;
      },
      register: function (user, success, error) {
        $http.post('/register', user).success(function (res) {
          changeUser(res);
          success();
        }).error(error);
      },
      login: function (user, success, error) {
        $http.post('/login', user).success(function (user) {
          changeUser(user);
          success(user);
        }).error(error);
      },
      logout: function (success, error) {
        $http.post('/logout').success(function () {
          changeUser({
            username: '',
            role: userRoles.public
          });
          success();
        }).error(error);
      },
      accessLevels: accessLevels,
      userRoles: userRoles,
      user: currentUser
    };
  }
]);

services.factory('Facebook', ['$resource', '$cookies', '$rootScope',
  function ($resource, $cookies, $rootScope) {
    var obj = {
      login: function (service, cb) {
        switch (service) {
        case 'fb':
          FB.login(function (response) {
            return cb(response);
          }, {
            scope: 'email'
          });
          break;
        }
      },
      fbMe: function () {
        FB.api('/me', function (response) {
          return response;
        });
      }
    };

    FB.init({
      appId: '408153889317850',
      channelUrl: 'channel.html',
      status: true,
      cookie: true,
      xfbml: true
    });

    FB.Event.subscribe('auth.authResponseChange', function (response) {
      if (response.status === 'connected') {
        $rootScope.authed = true;
        //create session here
        FB.api('/me', function (response) {
          $rootScope.$apply(function () {
            $rootScope.user = response;
          });
        });
      } else {
        //destroy backend session if user is not loggedin
      }
    });

    return obj;
  }
]);

services.factory('User', ['$resource', '$cookies', '$rootScope',
  function ($resource, $cookies, $rootScope) {
    return $resource('/api/user/:id', {}, {
      update: {
        method: 'PUT'
      },
      getUserinfo: {
        method: 'GET',
        url: '/api/user/username/:email'
      }
    });
  }
]);
