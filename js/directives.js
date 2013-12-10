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
      link: function (scope, element) {
        $timeout(function () {
          Prism.highlightElement(element.get(0))
        }, 0)

      }
    };
  }
]);
