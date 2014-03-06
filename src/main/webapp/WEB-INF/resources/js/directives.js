'use strict';
var directives = angular.module('openservices.directives', []);

directives.directive('gravatar', ['Gravatar', '$compile',
    function (Gravatar, $compile) {
        return {

            link: function (scope, element, attrs) {
                attrs.$observe('gravatar', function (email) {
                    if (email) {
                        var html = '<img class="img-responsive img-circle" style="width: ' + attrs.size + 'px;height:' + attrs.size + 'px;" src="' + Gravatar.picture(attrs.size, email) + '" />';
                        var e = $compile(html)(scope);
                        element.replaceWith(e);
                    }

                });
            }
        };
    }
]);

directives.directive('fileselect', ['$parse',
    function ($parse) {
        return function (scope, elem, attr) {
            var fn = $parse(attr['fileselect']);
            elem.bind('change', function (evt) {
                scope.$apply(function () {
                    fn(scope, {
                        files: evt.target.files[0],
                        $event: evt
                    });
                });
            });
        };
    }
]);
