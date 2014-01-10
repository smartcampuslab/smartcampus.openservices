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

directives.directive('gravatar', ['$timeout', 'Gravatar',
  function ($timeout, Gravatar) {
    return {
      link: function (scope, element, attrs) {
        scope.$watch('currentUser.username', function (val) {
          if (val) {
            element[0].outerHTML = '<a href="profile">Profile <img class="media-object" src="' + Gravatar.picture(40) + '" /></a>';
          }
        })

      }
    };
  }
]);
