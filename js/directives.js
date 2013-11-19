'use strict';
var directives = angular.module('openservices.directives', []);

directives.directive('bsHolder', [
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
