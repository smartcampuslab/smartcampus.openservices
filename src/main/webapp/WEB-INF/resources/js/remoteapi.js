'use strict';

services.factory('RemoteApi', ['oAuth',
  function (oAuth) {
    function RemoteApi (type){
        switch (type) { 
        case 'oauth': 
          this.api = oAuth;
        break; 

        case 'openid': 
          this.api = null; 
        break; 
      }
    }
    RemoteApi.prototype.authorize = function(config){
    	for (var key in config){
    		this.api.config[key] = config[key];
    	}
    	this.api.authorize(function (result) {
             return result;
          });
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
        redirectUri: $location.protocol() + '://' + $location.host() + ':8080/openservice/callback',
        authorizationUrl: null,
        verifyFunc: null,
        response_type: null,
        grant_type: null,
        scopes: []
      },
      authorize: function (cb) {
        var params = angular.extend(getParams(this));
        var url = this.config.authorizationUrl + '?' + objectToQueryString(params)
        console.log(url)
        var popup = window.open(url, popupOptions.name, formatPopupOptions(popupOptions.openParams));
        console.log(popup)
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
