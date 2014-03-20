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

directives.directive('showValidation', [
    function() {
	    return {
	        restrict: "A",
	        link: function(scope, element, attrs, ctrl) {
	
	            if (element.get(0).nodeName.toLowerCase() === 'form') {
	                element.find('.form-group').each(function(i, formGroup) {
	                    showValidation(angular.element(formGroup), attrs.showValidation);
	                });
	            } else {
	                showValidation(element, attrs.showValidation);
	            }
	
	            function showValidation(formGroupEl, msg) {
	                var input = formGroupEl.find('input[ng-model],textarea[ng-model],select[ng-model]');
	                if (input.length > 0) {
	                    scope.$watch(function() {
	                        return input.hasClass('ng-invalid');// && input.hasClass('ng-dirty');
	                    }, function(isInvalid) {
	                        formGroupEl.toggleClass('has-error', isInvalid);
	                        var errmsg = formGroupEl.find('#err');
	                        errmsg.remove();
	                        if (isInvalid) {
		                    	var label = formGroupEl.find('label');
	                        	msg = label.text();
	                        	if (msg) {
	                        		formGroupEl.append('<span id="err" class="control-label">'+constructMsg(msg, input)+'</span>');
	                        	}
	                        }
	                    });
	                }
	            }
	            function constructMsg(fieldLabel, input) {
	            	if (input.hasClass('ng-invalid-required')) return fieldLabel + ' is required';
	            	return null;
	            }
	            
	        }
	    };
    }
]);    