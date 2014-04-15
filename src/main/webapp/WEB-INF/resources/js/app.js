'use strict';
var app = angular.module('openservices', ['ngRoute', 'ngCookies', 'openservices.directives', 'openservices.services', 'hljs']);

app.filter('fromNow', function () {
    return function (dateString) {
        return moment(dateString).fromNow()
    };
});

app.config(['$routeProvider', '$locationProvider', '$httpProvider', 'hljsServiceProvider',
    function ($routeProvider, $locationProvider, $httpProvider, hljsServiceProvider) {
        var access = routingConfig.accessLevels;
        $locationProvider.html5Mode(true);

        $httpProvider.interceptors.push(function ($q, $location) {
            return {
                'responseError': function (response) {
                    if (response.status === 401) {
                        $location.path('signin');
                        return $q.reject(response);
                    } else {
                        return $q.reject(response);
                    }
                }
            };
        });

        $routeProvider.
        when('/', {
            controller: 'homeCtrl',
            templateUrl: 'partials/home.html',
            access: access.public
        }).
        when('/search', {
            controller: 'servicesCtrl',
            templateUrl: 'partials/services/list.html',
            access: access.public
        }).
        when('/search/:tag', {
            controller: 'servicesCtrl',
            templateUrl: 'partials/services/list.html',
            access: access.public
        }).
        when('/services', {
            controller: 'servicesCtrl',
            templateUrl: 'partials/services/list.html',
            access: access.public
        }).
        when('/services/:id', {
            controller: 'serviceCtrl',
            templateUrl: 'partials/services/show.html',
            access: access.public
        }).
        when('/categories', {
            controller: 'categoriesCtrl',
            templateUrl: 'partials/categories.html',
            access: access.public
        }).
        when('/categories/:category', {
            controller: 'categoryCtrl',
            templateUrl: 'partials/services/list.html',
            access: access.public
        }).
        when('/organizations', {
            controller: 'organizationsCtrl',
            templateUrl: 'partials/organizations.html',
            access: access.public
        }).
        when('/organizations/:id', {
            controller: 'organizationCtrl',
            templateUrl: 'partials/organization.html',
            access: access.public
        }).
        when('/organizations/:id/services', {
            controller: 'organizationServicesCtrl',
            templateUrl: 'partials/services/list.html',
            access: access.public
        }).
        when('/signin', {
            controller: 'signinCtrl',
            templateUrl: 'partials/signin.html',
            access: access.public
        }).
        when('/signup', {
            controller: 'signUpCtrl',
            templateUrl: 'partials/signup.html',
            access: access.public
        }).
        when('/reset', {
            controller: 'resetCtrl',
            templateUrl: 'partials/reset.html',
            access: access.public
        }).
        when('/profile/change', {
            controller: 'passwCtrl',
            templateUrl: 'partials/profile/change.html',
            access: access.ROLE_NORMAL
        }).
        when('/enable/:key', {
            controller: 'enableCtrl',
            templateUrl: 'partials/enable.html',
            access: access.public
        }).
        when('/org/enable/:key', {
            controller: 'enableOrgCtrl',
            templateUrl: 'partials/enableOrg.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile', {
            controller: 'profileCtrl',
            templateUrl: 'partials/profile/show.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/edit', {
            controller: 'profileCtrl',
            templateUrl: 'partials/profile/edit.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/services/new', {
            controller: 'newServiceCtrl',
            templateUrl: 'partials/profile/services/edit.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/services/:id', {
            controller: 'viewServiceCtrl',
            templateUrl: 'partials/profile/services/view.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/services/:id/edit', {
            controller: 'editServiceCtrl',
            templateUrl: 'partials/profile/services/edit.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/services/:id/methods/new', {
            controller: 'newMethodCtrl',
            templateUrl: 'partials/profile/methods/edit.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/services/:id/methods/:method/edit', {
            controller: 'editMethodCtrl',
            templateUrl: 'partials/profile/methods/edit.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/services/:id/methods/:method/view', {
            controller: 'viewMethodCtrl',
            templateUrl: 'partials/profile/methods/view.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/services/:id/methods/:method/tests/new', {
            controller: 'newTestCtrl',
            templateUrl: 'partials/profile/methods/test.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/services/:id/methods/:method/tests/:pos/edit', {
            controller: 'editTestCtrl',
            templateUrl: 'partials/profile/methods/test.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/organizations/new', {
            controller: 'newOrgCtrl',
            templateUrl: 'partials/profile/organizations/edit.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/organizations/:id/edit', {
            controller: 'editOrgCtrl',
            templateUrl: 'partials/profile/organizations/edit.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/organizations/:id/members', {
            controller: 'showOrgMembersCtrl',
            templateUrl: 'partials/profile/organizations/members/show.html',
            access: access.ROLE_NORMAL
        }).
        when('/profile/category/:id', {
            controller: 'editCategoryCtrl',
            templateUrl: 'partials/profile/admin/categories/edit.html',
            access: access.ROLE_ADMIN
        }).
        when('/profile/categories/new', {
            controller: 'newCategoryCtrl',
            templateUrl: 'partials/profile/admin/categories/edit.html',
            access: access.ROLE_ADMIN
        }).
        otherwise({
            redirectTo: '/'
        });

    }
]).run(function ($rootScope, $location, Auth, $routeParams, $cookieStore) {
    var history = [];
    var value = $cookieStore.get('value');
    if (value === false) {
        $cookieStore.remove('user');
    }

    $rootScope.$on('$routeChangeStart', function (event, next) {
        history.push($location.$$path);

        $rootScope.loc = $location.path().split('/');
        $rootScope.loc.splice(0, 1);
        if (!Auth.authorize(next.access)) {
            if (!Auth.isLoggedIn() || value === false) {
                $location.path('/signin');
            }
        }

    });
    $rootScope.back = function () {
        var prevUrl = history.length > 1 ? history.splice(-2)[0] : '/';
        $location.path(prevUrl);
    };
});
