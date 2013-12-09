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

directives.directive('pp', [
  function () {
    return {
      link: function (scope, element) {
        prettyPrint();
      }
    };
  }
]);
