'use strict';
var directives = angular.module('openservices.directives', []);

directives.directive('holder', [
  function () {
    return {
      link: function (scope, element) {
        Holder.run({
          images: element.get(0)
        });
      }
    };
  }
]);

directives.directive('prism', ['$timeout',
  function ($timeout) {
    return {
      link: function (scope, element, attrs) {
        scope.$watch('response', function (val) {
          $timeout(function () {
            Prism.highlightElement(element.get(0))
          }, 0)
        })

      }
    };
  }
]);

directives.directive('signin', ['$timeout', 'Gravatar','$compile',
  function ($timeout, Gravatar, $compile) {
    return {
    	
      link: function (scope, element, attrs, navCtrl) {
        scope.$watch('currentUser.username', function (val) {
        	console.log('directive changed',val)
          if (val) {
        	  var html ='<li class="dropdown">\
	    	        <a class="dropdown-toggle" data-toggle="dropdown">Menu <b class="caret"></b></a>\
	    	        <ul class="dropdown-menu">\
	    	          <li><a href="profile">Profile</a></li>\
	    	          <li class="divider"></li>\
	    	          <li><a ng-click="logout()" href="#">Logout</a></li>\
	    	        </ul>\
	    	      </li>';
        	  var e = $compile(html)(scope);
              element.replaceWith(e);
          }else{
        	  console.log('should be signin')
        	  var html ='<li><a href="signin">Sign in</a></li>';
        	  var e = $compile(html)(scope);
              element.replaceWith(e);
          }
        })

      }
    };
  }
]);

directives.directive('gravatar', ['Gravatar','$compile',
                                function (Gravatar, $compile) {
                                  return {
                                  	
                                    link: function (scope, element, attrs, navCtrl) {
                                      scope.$watch('currentUser.username', function (val) {
                                        if (val) {
                                      	  var html ='<img class="media-object dp img-circle" style="width: 100px;height:100px;" src="' + Gravatar.picture(120) + '" />';
                                      	  var e = $compile(html)(scope);
                                            element.replaceWith(e);
                                        }
                                      })

                                    }
                                  };
                                }
                              ]);

