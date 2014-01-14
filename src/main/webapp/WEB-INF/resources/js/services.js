'use strict';
var services = angular.module('openservices.services', ['ngResource', 'ngCookies']);

services.factory('Auth', ['$http', '$cookieStore', '$rootScope',
  function ($http, $cookieStore, $rootScope) {
    var accessLevels = routingConfig.accessLevels,
      userRoles = routingConfig.userRoles
      $rootScope.currentUser = $cookieStore.get('user') || {
        username: '',
        role: userRoles.public
      };

    //$cookieStore.remove('user');

    function changeUser(user) {
      _.extend($rootScope.currentUser, user);
    }

    return {
      authorize: function (accessLevel, role) {
        if (role === undefined) {
          role = $rootScope.currentUser.role;
        }
        return accessLevel.bitMask & role.bitMask;
      },
      isLoggedIn: function (user) {
        if (user === undefined) {
          user = $rootScope.currentUser;
        }
        return user && user.role && (user.role.title === userRoles.ROLE_NORMAL.title || user.role.title === userRoles.ROLE_ADMIN.title);
      },
      register: function (user, success, error) {
        $http.post('/register', user).success(function (res) {
          changeUser(res);
          success();
        }).error(error);
      },
      login: function (user, success, error) {
        $http.post('perform_login', $.param(user), {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }).success(function (data) {
        	console.log('user data: ', data)
          var role = data.role
          var templ = {
        	  username : data.username,
            role: userRoles[role]
          };
          
          changeUser(templ);
          $cookieStore.remove('user');
          $cookieStore.put('user',$rootScope.currentUser);
          success(user)
        });
      },
      logout: function (success, error) {
        $http.get('logout').success(function () {
          
          changeUser({
            username: '',
            role: userRoles.public
          });
          $cookieStore.remove('user');
          $location.path('/')
          success();
        }).error(error);
      },
      accessLevels: accessLevels,
      userRoles: userRoles,
      user: $rootScope.currentUser
    };
  }
]);

services.factory('Facebook', ['$resource', '$cookies', '$rootScope',
  function ($resource, $cookies, $rootScope) {
    $rootScope.authed = false;
    var obj = {
      login: function (cb) {
        FB.login(function (response) {
          return cb(response);
        }, {
          scope: 'email'
        });
      },
      me: function () {
        FB.api('/me', function (response) {
          return response;
        });
      },
      picture: function (cb) {
        FB.api('/me/picture', function (response) {
          cb(response)
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
            console.log('setting user role')
            $rootScope.currentUser.role = routingConfig.userRoles.user;
            $rootScope.currentUser.username = response.email;
          });
        });
      } else {
        //destroy backend session if user is not loggedin
      }
    });

    return obj;
  }
]);

services.factory('User', ['$resource',
  function ($resource) {
    return $resource('api/user/:id', {}, {
      update: {
        method: 'POST',
        url: 'api/user/modify'
      },
      getInfo: {
        method: 'GET',
        url: 'api/user/my'
      },
      create: {
        method: 'POST',
        url: '/api/user/add'
      },
      verify: {
        method: 'POST',
        url: '/api/user/add/verify',
        params: {
          user: {}
        }
      },
      enable: {
        method: 'POST',
        url: '/api/user/add/enable'
      },
      disable: {
        method: 'GET',
        url: '/api/user/disable/:email'
      },
      save: {
          method: 'POST',
          url: 'api/user/add'
        }
    });
  }
]);

services.factory('Catalog', ['$resource',
  function ($resource) {
    return $resource('/api/catalog', {}, {
      update: {
        method: 'PUT'
      },
      searchService: {
        method: 'GET',
        url: '/api/catalog/service/search/:token',
        isArray: true
      },
      browseServiceCat: {
        method: 'GET',
        url: '/api/catalog/service/browse/category/:category',
        isArray: true
      },
      browseServiceTags: {
        method: 'GET',
        url: '/api/catalog/service/browse/tags/:tag',
        isArray: true
      },
      searchOrg: {
        method: 'GET',
        url: '/api/catalog/org/search/:org',
        isArray: true
      },
      browseOrgCat: {
        method: 'GET',
        url: '/api/catalog/org/browse/category/:category',
        isArray: true
      }
    });
  }
]);

services.factory('Service', ['$resource',
  function ($resource) {
    return $resource('/api/service/my/:userId', {}, {
      list: {
        method: 'GET',
        url: '/api/service/view',
        isArray: true
      },
      getDescription: {
        method: 'GET',
        url: 'api/service/view/description/:id'
      },
      getMethods: {
        method: 'GET',
        url: '/api/service/view/method/:id',
        isArray: true
      },
      getHistory: {
        method: 'GET',
        url: '/api/service/view/history/:id',
        isArray: true
      },
      create: {
        method: 'POST',
        url: 'api/service/add'
      },
      update: {
        method: 'POST',
        url: '/api/service/modify',
        params: {
          service: {}
        }
      },
      publish: {
        method: 'POST',
        url: '/api/service/publish',
        params: {
          service: {}
        }
      },
      unpublish: {
        method: 'POST',
        url: '/api/service/unpublish',
        params: {
          service: {}
        }
      },
      deprecate: {
        method: 'POST',
        url: '/api/service/deprecate',
        params: {
          service: {}
        }
      },
      createMethod: {
        method: 'POST',
        url: '/api/service/method/add',
        params: {
          service: {}
        }
      },
      updateMethod: {
        method: 'POST',
        url: '/api/service/method/modify',
        params: {
          service: {}
        }
      },
      deleteMethod: {
        method: 'POST',
        url: '/api/service/method/delete',
        params: {
          service: {}
        }
      },
      copyMethod: {
        method: 'POST',
        url: '/api/service/method/copy',
        params: {
          service: {}
        }
      },
      get: {
          method: 'GET',
          url: 'api/service/my'
        }

    });
  }
]);

services.factory('Org', ['$resource',
  function ($resource) {
    return $resource('api/org/:name', {}, {
      get: {
        method: 'GET',
        url: 'api/org/my',
      },
      getById: {
          method: 'GET',
          url: 'api/org/:id',
        },
      list: {
        method: 'GET',
        url: 'api/org/list',
        isArray: true
      },
      getOrgHistory: {
        method: 'GET',
        url: 'api/org/activity/history/:id',
        isArray: true
      },
      create: {
        method: 'POST',
        url: 'api/org/add'
      },
      delete: {
        method: 'DELETE',
        url: 'api/org/delete/:id'
      },
      update: {
        method: 'PUT',
        url: 'api/org/modify'
      },
      addOwner: {
        method: 'POST',
        url: 'api/org/manage/owner/add/:key'
      },
      deleteOwner: {
        method: 'POST',
        url: 'api/org/manage/owner/delete'
      }
    });
  }
]);

services.factory('Gravatar', ['$http', '$rootScope',
  function ($http, $rootScope) {

    return {
      picture: function (size) {
        var MD5 = function (s) {
          function L(k, d) {
            return (k << d) | (k >>> (32 - d))
          }

          function K(G, k) {
            var I, d, F, H, x;
            F = (G & 2147483648);
            H = (k & 2147483648);
            I = (G & 1073741824);
            d = (k & 1073741824);
            x = (G & 1073741823) + (k & 1073741823);
            if (I & d) {
              return (x ^ 2147483648 ^ F ^ H)
            }
            if (I | d) {
              if (x & 1073741824) {
                return (x ^ 3221225472 ^ F ^ H)
              } else {
                return (x ^ 1073741824 ^ F ^ H)
              }
            } else {
              return (x ^ F ^ H)
            }
          }

          function r(d, F, k) {
            return (d & F) | ((~d) & k)
          }

          function q(d, F, k) {
            return (d & k) | (F & (~k))
          }

          function p(d, F, k) {
            return (d ^ F ^ k)
          }

          function n(d, F, k) {
            return (F ^ (d | (~k)))
          }

          function u(G, F, aa, Z, k, H, I) {
            G = K(G, K(K(r(F, aa, Z), k), I));
            return K(L(G, H), F)
          }

          function f(G, F, aa, Z, k, H, I) {
            G = K(G, K(K(q(F, aa, Z), k), I));
            return K(L(G, H), F)
          }

          function D(G, F, aa, Z, k, H, I) {
            G = K(G, K(K(p(F, aa, Z), k), I));
            return K(L(G, H), F)
          }

          function t(G, F, aa, Z, k, H, I) {
            G = K(G, K(K(n(F, aa, Z), k), I));
            return K(L(G, H), F)
          }

          function e(G) {
            var Z;
            var F = G.length;
            var x = F + 8;
            var k = (x - (x % 64)) / 64;
            var I = (k + 1) * 16;
            var aa = Array(I - 1);
            var d = 0;
            var H = 0;
            while (H < F) {
              Z = (H - (H % 4)) / 4;
              d = (H % 4) * 8;
              aa[Z] = (aa[Z] | (G.charCodeAt(H) << d));
              H++
            }
            Z = (H - (H % 4)) / 4;
            d = (H % 4) * 8;
            aa[Z] = aa[Z] | (128 << d);
            aa[I - 2] = F << 3;
            aa[I - 1] = F >>> 29;
            return aa
          }

          function B(x) {
            var k = "",
              F = "",
              G, d;
            for (d = 0; d <= 3; d++) {
              G = (x >>> (d * 8)) & 255;
              F = "0" + G.toString(16);
              k = k + F.substr(F.length - 2, 2)
            }
            return k
          }

          function J(k) {
            k = k.replace(/rn/g, "n");
            var d = "";
            for (var F = 0; F < k.length; F++) {
              var x = k.charCodeAt(F);
              if (x < 128) {
                d += String.fromCharCode(x)
              } else {
                if ((x > 127) && (x < 2048)) {
                  d += String.fromCharCode((x >> 6) | 192);
                  d += String.fromCharCode((x & 63) | 128)
                } else {
                  d += String.fromCharCode((x >> 12) | 224);
                  d += String.fromCharCode(((x >> 6) & 63) | 128);
                  d += String.fromCharCode((x & 63) | 128)
                }
              }
            }
            return d
          }
          var C = Array();
          var P, h, E, v, g, Y, X, W, V;
          var S = 7,
            Q = 12,
            N = 17,
            M = 22;
          var A = 5,
            z = 9,
            y = 14,
            w = 20;
          var o = 4,
            m = 11,
            l = 16,
            j = 23;
          var U = 6,
            T = 10,
            R = 15,
            O = 21;
          s = J(s);
          C = e(s);
          Y = 1732584193;
          X = 4023233417;
          W = 2562383102;
          V = 271733878;
          for (P = 0; P < C.length; P += 16) {
            h = Y;
            E = X;
            v = W;
            g = V;
            Y = u(Y, X, W, V, C[P + 0], S, 3614090360);
            V = u(V, Y, X, W, C[P + 1], Q, 3905402710);
            W = u(W, V, Y, X, C[P + 2], N, 606105819);
            X = u(X, W, V, Y, C[P + 3], M, 3250441966);
            Y = u(Y, X, W, V, C[P + 4], S, 4118548399);
            V = u(V, Y, X, W, C[P + 5], Q, 1200080426);
            W = u(W, V, Y, X, C[P + 6], N, 2821735955);
            X = u(X, W, V, Y, C[P + 7], M, 4249261313);
            Y = u(Y, X, W, V, C[P + 8], S, 1770035416);
            V = u(V, Y, X, W, C[P + 9], Q, 2336552879);
            W = u(W, V, Y, X, C[P + 10], N, 4294925233);
            X = u(X, W, V, Y, C[P + 11], M, 2304563134);
            Y = u(Y, X, W, V, C[P + 12], S, 1804603682);
            V = u(V, Y, X, W, C[P + 13], Q, 4254626195);
            W = u(W, V, Y, X, C[P + 14], N, 2792965006);
            X = u(X, W, V, Y, C[P + 15], M, 1236535329);
            Y = f(Y, X, W, V, C[P + 1], A, 4129170786);
            V = f(V, Y, X, W, C[P + 6], z, 3225465664);
            W = f(W, V, Y, X, C[P + 11], y, 643717713);
            X = f(X, W, V, Y, C[P + 0], w, 3921069994);
            Y = f(Y, X, W, V, C[P + 5], A, 3593408605);
            V = f(V, Y, X, W, C[P + 10], z, 38016083);
            W = f(W, V, Y, X, C[P + 15], y, 3634488961);
            X = f(X, W, V, Y, C[P + 4], w, 3889429448);
            Y = f(Y, X, W, V, C[P + 9], A, 568446438);
            V = f(V, Y, X, W, C[P + 14], z, 3275163606);
            W = f(W, V, Y, X, C[P + 3], y, 4107603335);
            X = f(X, W, V, Y, C[P + 8], w, 1163531501);
            Y = f(Y, X, W, V, C[P + 13], A, 2850285829);
            V = f(V, Y, X, W, C[P + 2], z, 4243563512);
            W = f(W, V, Y, X, C[P + 7], y, 1735328473);
            X = f(X, W, V, Y, C[P + 12], w, 2368359562);
            Y = D(Y, X, W, V, C[P + 5], o, 4294588738);
            V = D(V, Y, X, W, C[P + 8], m, 2272392833);
            W = D(W, V, Y, X, C[P + 11], l, 1839030562);
            X = D(X, W, V, Y, C[P + 14], j, 4259657740);
            Y = D(Y, X, W, V, C[P + 1], o, 2763975236);
            V = D(V, Y, X, W, C[P + 4], m, 1272893353);
            W = D(W, V, Y, X, C[P + 7], l, 4139469664);
            X = D(X, W, V, Y, C[P + 10], j, 3200236656);
            Y = D(Y, X, W, V, C[P + 13], o, 681279174);
            V = D(V, Y, X, W, C[P + 0], m, 3936430074);
            W = D(W, V, Y, X, C[P + 3], l, 3572445317);
            X = D(X, W, V, Y, C[P + 6], j, 76029189);
            Y = D(Y, X, W, V, C[P + 9], o, 3654602809);
            V = D(V, Y, X, W, C[P + 12], m, 3873151461);
            W = D(W, V, Y, X, C[P + 15], l, 530742520);
            X = D(X, W, V, Y, C[P + 2], j, 3299628645);
            Y = t(Y, X, W, V, C[P + 0], U, 4096336452);
            V = t(V, Y, X, W, C[P + 7], T, 1126891415);
            W = t(W, V, Y, X, C[P + 14], R, 2878612391);
            X = t(X, W, V, Y, C[P + 5], O, 4237533241);
            Y = t(Y, X, W, V, C[P + 12], U, 1700485571);
            V = t(V, Y, X, W, C[P + 3], T, 2399980690);
            W = t(W, V, Y, X, C[P + 10], R, 4293915773);
            X = t(X, W, V, Y, C[P + 1], O, 2240044497);
            Y = t(Y, X, W, V, C[P + 8], U, 1873313359);
            V = t(V, Y, X, W, C[P + 15], T, 4264355552);
            W = t(W, V, Y, X, C[P + 6], R, 2734768916);
            X = t(X, W, V, Y, C[P + 13], O, 1309151649);
            Y = t(Y, X, W, V, C[P + 4], U, 4149444226);
            V = t(V, Y, X, W, C[P + 11], T, 3174756917);
            W = t(W, V, Y, X, C[P + 2], R, 718787259);
            X = t(X, W, V, Y, C[P + 9], O, 3951481745);
            Y = K(Y, h);
            X = K(X, E);
            W = K(W, v);
            V = K(V, g)
          }
          var i = B(Y) + B(X) + B(W) + B(V);
          return i.toLowerCase()
        };

        var size = size || 80;
        return 'http://www.gravatar.com/avatar/' + MD5($rootScope.currentUser.username) + '.jpg?s=' + size;

      }
    }
  }
]);

services.factory('oAuth', ['$http', '$window', '$location', '$rootScope',
  function ($http, $window, $location, $rootScope) {

    var objectToQueryString = function (obj) {
      var str = [];
      angular.forEach(obj, function (value, key) {
        str.push(encodeURIComponent(key) + "=" + encodeURIComponent(value));
      });

      return str.join("&");
    };
    var popupOptions = {
      name: 'AuthPopup',
      openParams: {
        width: 650,
        height: 300,
        resizable: true,
        scrollbars: true,
        status: true
      }
    };
    var formatPopupOptions = function (options) {
      var pairs = [];
      angular.forEach(options, function (value, key) {
        if (value || value === 0) {
          value = value === true ? 'yes' : value;
          pairs.push(key + '=' + value);
        }
      });
      return pairs.join(',');
    };

    var getParams = function (self) {
      return {
        response_type: self.config.response_type,
        client_id: self.config.clientId,
        redirect_uri: self.config.redirectUri,
        scope: self.config.scopes.join(",")
      }
    };

    return {
      config: {
        clientId: null,
        redirectUri: $location.protocol() + '://' + $location.host() + '/callback',
        authorizationEndpoint: null,
        localStorageName: 'accessToken',
        verifyFunc: null,
        response_type: 'token',
        grant_type: 'authorization_code',
        scopes: []
      },
      getToken: function (cb) {
        var params = angular.extend(getParams(this));
        var url = this.config.authorizationEndpoint + '?' + objectToQueryString(params)
        console.log(url)
        var popup = window.open(url, popupOptions.name, formatPopupOptions(popupOptions.openParams));

        angular.element($window).bind('message', function (event) {
          if (event.originalEvent.origin == $window.location.origin) {
            $rootScope.$apply(function () {
              if (event.originalEvent.data) {
                cb(event.originalEvent.data)
              }
            })
          }
        });
      }
    }
  }
]);
