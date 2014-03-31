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
//code from http://codepen.io/brunoscopelliti/pen/nFlvm
//Error: p is not defined
directives.directive('checkStrength', [
    function(){
		return{
			replace: false,
			restrict: 'EACM',
			link: function(scope, elem, attrs){
				var strength = {
					colors: ['#F00','#F90','#FF0','#9F0','#0F0'],
					measureStrength: function (p) {
	                    var _force = 0;                    
	                    var _regex = /[$-:-?{-~!"^_`\[\]]/g;
	                                          
	                    var _lowerLetters = /[a-z]+/.test(p);                    
	                    var _upperLetters = /[A-Z]+/.test(p);
	                    var _numbers = /[0-9]+/.test(p);
	                    var _symbols = _regex.test(p);
	                                          
	                    var _flags = [_lowerLetters, _upperLetters, _numbers, _symbols];                    
	                    var _passedMatches = $.grep(_flags, function (el) { return el === true; }).length;                                          
	                    
	                    _force += 2 * p.length + ((p.length >= 10) ? 1 : 0);
	                    _force += _passedMatches * 10;
	                        
	                    // penality (short password)
	                    _force = (p.length <= 6) ? Math.min(_force, 10) : _force;                                      
	                    
	                    // penality (poor variety of characters)
	                    _force = (_passedMatches == 1) ? Math.min(_force, 10) : _force;
	                    _force = (_passedMatches == 2) ? Math.min(_force, 20) : _force;
	                    _force = (_passedMatches == 3) ? Math.min(_force, 40) : _force;
	                    
	                    return _force;

	                },
	                getColor: function (s) {

	                    var idx = 0;
	                    if (s <= 10) { idx = 0; }
	                    else if (s <= 20) { idx = 1; }
	                    else if (s <= 30) { idx = 2; }
	                    else if (s <= 40) { idx = 3; }
	                    else { idx = 4; }

	                    return { idx: idx + 1, col: this.colors[idx] };

	                }
	            };
			
				scope.$watch(attrs.checkStrength, function(){
					if(scope.newP ==='' || !scope.newP){
						elem.css({"display": "none"});
					}else{
						var c = strength.getColor(strength.measureStrength(scope.newP));
						elem.css({"display": "inline"});
						elem.children('li')
							.css({"background": "#DDD"})
							.slice(0,c.idx)
							.css({"background": c.col});
					}
				});
			}, template: '<li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li>'
		};	
}]);