'use strict';

services.factory('RemoteApi', ['oAuth2','noauth','$q',
  function (oAuth2, noauth, $q) {
    function RemoteApi (type){
        switch (type) { 
        case 'OAuth2': 
          this.api = oAuth2;
        break; 
        case 'OpenID': 
          this.api = null; 
        break; 
        case 'Public': 
          this.api = noauth; 
        break; 
      }
    }
    RemoteApi.prototype.authorize = function(methodId){
    	var deferred = $q.defer();
    	this.api.methodId = methodId;
    	this.api.authorize(function (result) {
    		deferred.resolve(result);
          });
    	return deferred.promise;
    };
    return RemoteApi;
  }
])

services.factory('oAuth2', ['$http', '$window', '$location', '$rootScope',
  function ($http, $window, $location, $rootScope) {

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
    };
    
    return {
      authorize: function (cb) {
    	var url = getRootPath()+ "/api/testbox/authorize/"+this.methodId;
        window.open(url, popupOptions.name, formatPopupOptions(popupOptions.openParams));
        angular.element($window).bind('message', function (event) {
          if (event.originalEvent.origin == $window.location.origin) {
            $rootScope.$apply(function () {
              if (event.originalEvent.data) {
                cb(event.originalEvent.data);
              }
            });
          }
        });
      }
    };
  }
]);

services.factory('noauth', [
   function () {
     return {
       authorize: function (cb) {
           cb({});
       }
     };
   }
 ]);
