'use strict';

services.factory('RemoteApi', ['oAuth','noauth','$q',
  function (oAuth, noauth, $q) {
    function RemoteApi (type){
        switch (type) { 
        case 'OAuth': 
          this.api = oAuth;
        break; 
        case 'OpenID': 
          this.api = null; 
        break; 
        case 'Public': 
          this.api = noauth; 
        break; 
      }
    }
    RemoteApi.prototype.authorize = function(config){
    	var deferred = $q.defer();
    	var self = this;
    	for (var key in config){
    		this.api.config[key] = config[key];
    	}
    	this.api.authorize(function (result) {
    		deferred.resolve(result);
          });
    	return deferred.promise;
    };
    return RemoteApi;
  }
])

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

    var getRootPath = function() {
    	var abs = $location.absUrl();
    	var path = $location.path();
    	if (path != '' && path != '/') {
    		return abs.substring(0,abs.indexOf(path));
    	} else {
    		return abs;
    	}
    }
    
    var getParams = function (self) {
      return {
        response_type: self.config.response_type,
        client_id: self.config.client_id,
        redirect_uri: self.config.redirectUri,
        scope: self.config.scopes.join(",")
      }
    };

    return {
      config: {
        client_id: null,
        redirectUri: getRootPath()+'/callback',
        authorizationUrl: null,
        verifyFunc: null,
        response_type: null,
        grant_type: null,
        scopes: []
      },
      authorize: function (cb) {
        var params = angular.extend(getParams(this));
        var url = this.config.authorizationUrl + '?' + objectToQueryString(params)
        var popup = window.open(url, popupOptions.name, formatPopupOptions(popupOptions.openParams));
        angular.element($window).bind('message', function (event) {
          if (event.originalEvent.origin == $window.location.origin) {
            $rootScope.$apply(function () {
              if (event.originalEvent.data) {
                cb({Authorization: 'Bearer ' + event.originalEvent.data.access_token})
              }
            })
          }
        });
      }
    }
  }
]);

services.factory('noauth', [
   function () {
     return {
       config: {},
       authorize: function (cb) {
           cb({});
       }
     }
   }
 ]);
