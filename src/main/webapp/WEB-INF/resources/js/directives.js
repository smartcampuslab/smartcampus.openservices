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

directives.directive('prism', ['$timeout','$compile',
  function ($timeout, $compile) {
    return {
      link: function (scope, element, attrs) {
        element.ready(function(){
            Prism.highlightElement(element[0]);
            $compile(element.contents())(scope);
        });
      }
    };
  }
]);

directives.directive('gravatar', ['Gravatar','$compile',
    function (Gravatar, $compile) {
      return {
      	
        link: function (scope, element, attrs) {
        	attrs.$observe('gravatar', function (email) {
        		if(email){
            		var html ='<img class="media-object dp img-circle" style="width: 100px;height:100px;" src="' + Gravatar.picture(120, email) + '" />';
            		var e = $compile(html)(scope);
                      element.replaceWith(e);
        		}

            });
        }
      };
    }
  ]);

