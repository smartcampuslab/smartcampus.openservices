'use strict';
app.controller('homeCtrl', ['$scope', '$http',
    function ($scope, $http) {}
]);

app.controller('navCtrl', ['$scope', '$http', 'Auth', '$location',
    function ($scope, $http, Auth, $location) {
        $scope.logout = function () {
            Auth.logout(function () {
                $location.path('/');
            });
        };
    }
]);

app.controller('signinCtrl', ['$scope', '$location', 'Auth',
    function ($scope, $location, Auth) {

        $scope.signin = function () {
            Auth.login($scope.user, function () {
                    $location.path('profile');
                }, function (error) {
                    $scope.error = error;
                }

            );
        };
    }
]);

app.controller('signUpCtrl', ['$scope', '$location', 'User',
    function ($scope, $location, User) {
        $scope.user = new User();
        $scope.submit = function () {
            $scope.user.$save($scope.user, function () {
                $location.path('signin');
            }, function (data) {
                $scope.error = data.data.error;
            });
        };
    }
]);

app.controller('profileCtrl', ['$scope', '$http', '$location', 'User', 'Service', 'Org',
    function ($scope, $http, $location, User, Service, Org) {
        $scope.template = 'partials/profile/_details.html';

        $scope.deleteOrg = function (i) {
            Org.delete({
                id: $scope.orgs[i].id
            }, function () {
                $scope.orgs.splice(i, 1);
                $location.path('profile');
            });
        };

        $scope.deprecateService = function (i) {
            Service.deprecate({
                id: $scope.services[i].id
            }, {}, function () {
                $scope.services[i].state = 'DEPRECATE';
                $location.path('profile');
            });
        };
        $scope.publishService = function (i) {
            Service.publish({
                id: $scope.services[i].id
            }, {}, function () {
                $scope.services[i].state = 'PUBLISH';
                $location.path('profile');
            });
        };
        $scope.unpublishService = function (i) {
            Service.unpublish({
                id: $scope.services[i].id
            }, {}, function () {
                $scope.services[i].state = 'UNPUBLISH';
                $location.path('profile');
            });
        };
        $scope.deleteService = function (i) {
            Service.delete({
                id: $scope.services[i].id
            }, function () {
                $scope.services.splice(i, 1);
                $location.path('profile');
            });
        };

        $scope.submit = function () {
            User.update($scope.user, function () {
                $location.path('profile');
            });
        };

        User.getInfo({}, function (data) {
            $scope.user = data.data;
        });

        Org.get({}, function (data) {
            $scope.orgs = data.data;
        });
        Service.get({}, function (data) {
            $scope.services = data.data;
        });

    }
]);

app.controller('newServiceCtrl', ['$scope', '$http', '$location', 'Service', 'Org', 'Category',
    function ($scope, $http, $location, Service, Org, Category) {
        $scope.protocols = ['OAuth', 'OpenID', 'Public'];
        $scope.accessInformation = {
            authentication: {
                accessProtocol: null,
                accessAttributes: {
                    client_id: null,
                    response_type: null,
                    authorizationUrl: null,
                    grant_type: null
                }
            }
        };
        Category.list({}, function (data) {
            $scope.categories = data.data;
        });

        Org.get({}, function (data) {
            $scope.orgs = data.data;
        });

        $scope.submit = function () {
            if ($scope.service.expiration) {
                $scope.service.expiration = new Date($scope.service.expiration).getTime();
            }
            Service.create($scope.service, function () {
                $location.path('profile');
            });
        };
        $scope.keep = function () {
            $scope.service.accessInformation = $scope.accessInformation;
        };
    }
]);

app.controller('editServiceCtrl', ['$scope', '$routeParams', '$location', 'Service', 'Org', 'Category',
    function ($scope, $routeParams, $location, Service, Org, Category) {
        $scope.protocols = ['OAuth', 'OpenID', 'Public'];
        $scope.accessInformation = {
            authentication: {
                accessProtocol: null,
                accessAttributes: {
                    client_id: null,
                    response_type: null,
                    authorizationUrl: null,
                    grant_type: null
                }
            }
        };
        Category.list({}, function (data) {
            $scope.categories = data.data;
        });

        Service.getDescription({
            id: $routeParams.id
        }, function (data) {
            $scope.service = data.data;
            if ($scope.service.accessInformation !== null) {
                $scope.accessInformation = $scope.service.accessInformation;
            }
            if ($scope.service.expiration && $scope.service.expiration > 0) {
                $scope.service.expiration = new Date($scope.service.expiration).toISOString().slice(0, 10);
            }
        });
        Org.get({}, function (data) {
            $scope.orgs = data.data;
        });

        $scope.keep = function () {
            $scope.service.accessInformation = $scope.accessInformation;
        };

        $scope.submit = function () {
            if ($scope.service.expiration) {
                $scope.service.expiration = new Date($scope.service.expiration).getTime();
            }
            Service.update($scope.service, function () {
                $location.path('profile/services/' + $routeParams.id + '/view');
            });
        };
    }
]);

app.controller('viewServiceCtrl', ['$scope', '$routeParams', '$location', 'Service', 'Org', 'Category',
    function ($scope, $routeParams, $location, Service, Org, Category) {

        Service.getDescription({
            id: $routeParams.id
        }, function (data) {
            $scope.service = data.data;
            if ($scope.service.expiration && $scope.service.expiration > 0) {
                $scope.service.expiration = new Date($scope.service.expiration).toISOString().slice(0, 10);
            }
            Org.getById({
                id: data.data.organizationId
            }, function (data) {
                $scope.org = data.data;
            });

            if ($scope.service.category) {
                Category.getById({
                    id: $scope.service.category
                }, function (data) {
                    $scope.category = data.data;
                });
            }

        });
        Service.getMethods({
            id: $routeParams.id
        }, function (data) {
            $scope.methods = data.data;
        });

        $scope.deleteMethod = function (i) {
            Service.deleteMethod({
                id: $scope.methods[i].id
            }, function () {
                $scope.methods.splice(i, 1);
                $location.path('profile/services/' + $scope.service.id + '/view');
            });
        };
    }
]);

app.controller('newMethodCtrl', ['$scope', '$http', '$location', '$routeParams', 'Service',
    function ($scope, $http, $location, $routeParams, Service) {
        $scope.title = 'New';
        $scope.method = {
            serviceId: $routeParams.id,
            testboxProperties: {
                tests: []
            }
        };

        $scope.submit = function () {
            Service.createMethod($scope.method, function () {
                $location.path('profile/services/' + $scope.method.serviceId + '/view');
            });
        };
    }
]);

app.controller('viewMethodCtrl', ['$scope', '$http', '$location', '$routeParams', 'Service',
    function ($scope, $http, $location, $routeParams, Service) {

        Service.getMethod({
            id: $routeParams.id
        }, function (data) {
            $scope.method = data.data;
        });

        $scope.deleteTest = function (i) {
            Service.deleteTest({
                id: $scope.method.id,
                pos: i
            }, $scope.test, function () {
                $scope.method.testboxProperties.tests.splice(i, 1);
            });
        };
    }
]);

app.controller('editMethodCtrl', ['$scope', '$http', '$location', '$routeParams', 'Service',
    function ($scope, $http, $location, $routeParams, Service) {

        Service.getMethod({
            id: $routeParams.id
        }, function (data) {
            $scope.method = data.data;
        });

        $scope.submit = function () {
            Service.updateMethod($scope.method, function () {
                $location.path('profile/services/' + $scope.method.serviceId + '/view');
            });
        };

    }
]);

app.controller('newTestCtrl', ['$scope', '$http', '$location', '$routeParams', 'Service',
    function ($scope, $http, $location, $routeParams, Service) {
        $scope.title = 'New';
        $scope.method = {
            serviceId: $routeParams.id,
            id: $routeParams.method
        };

        $scope.addHeader = function () {
            if (!test.headers) {
                test.headers = {};
            }
            test.headers.push($scope.nheader);
        };
        $scope.submit = function () {
            Service.createTest({
                id: $scope.method.id
            }, $scope.test, function () {
                $location.path('profile/services/' + $scope.method.serviceId + '/methods/' + $scope.method.id + '/view');
            });
        };
    }
]);

app.controller('editTestCtrl', ['$scope', '$http', '$location', '$routeParams', 'Service',
    function ($scope, $http, $location, $routeParams, Service) {
        $scope.title = 'Edit';
        Service.getMethod({
            id: $routeParams.id
        }, function (data) {
            $scope.method = data.data;
            $scope.test = $scope.method.testboxProperties.tests[$routeParams.pos];
        });

        $scope.addHeader = function () {
            if (!$scope.test.headers) {
                $scope.test.headers = {};
            }
            $scope.test.headers[$scope.nheader.name] = $scope.nheader.value;
        };
        $scope.deleteHeader = function (n) {
            delete $scope.test.headers[n];
        };

        $scope.submit = function () {
            Service.updateTest({
                id: $scope.method.id,
                pos: $routeParams.pos
            }, $scope.test, function () {
                $location.path('profile/services/' + $scope.method.serviceId + '/methods/' + $scope.method.id + '/view');
            });
        };
    }
]);

app.controller('newOrgCtrl', ['$scope', '$http', '$location', 'Org', 'Category',
    function ($scope, $http, $location, Org, Category) {
        $scope.title = 'New';

        Category.list({}, function (data) {
            $scope.categories = data.data;
        });

        $scope.submit = function () {
            Org.create($scope.org, function () {
                $location.path('profile');
            });
        };

    }
]);

app.controller('editOrgCtrl', ['$scope', '$http', '$location', '$routeParams', 'Org', 'Category',
    function ($scope, $http, $location, $routeParams, Org, Category) {
        $scope.title = 'Edit';
        $scope.onFile = function (file) {
            $scope.file = file;
        };
        Org.getById({
            id: $routeParams.id
        }, function (org) {
            $scope.org = org.data;
        });
        Category.list({}, function (data) {
            $scope.categories = data.data;
        });
        $scope.submit = function () {
            $http({
                method: 'POST',
                url: 'api/file/' + $scope.org.id,
                headers: {
                    'Content-Type': false
                },
                transformRequest: function () {
                    var formData = new FormData();
                    formData.append('file', $scope.file);
                    return formData;
                }
            }).success(function (data) {

            });

            Org.update($scope.org, function () {
                $location.path('profile');
            });
        };
    }
]);

app.controller('editOrgMembersCtrl', ['$scope', '$http', '$location', 'Org',
    function ($scope, $http, $location, Org) {

        $scope.serviceCount = function (i) {
            return $scope.categoryData.services[i];
        };
    }
]);

app.controller('categoriesCtrl', ['$scope', '$http', '$location', 'Catalog',
    function ($scope, $http, $location, Catalog) {
        Catalog.browseAllServiceCat({}, function (data) {
            $scope.categoryData = data.data;
        });

        $scope.serviceCount = function (i) {
            return $scope.categoryData.services[i];
        };
    }
]);

app.controller('servicesCtrl', ['$scope', '$http', '$routeParams', 'Catalog',
    function ($scope, $http, $routeParams, Catalog) {
        $scope.start = 0;
        $scope.end = 9;

        if ( !! $scope.categoryActive) {
            $scope.categoryActive = undefined;
        }

        if ($routeParams.category) {
            Catalog.browseServiceCat({
                category: $routeParams.category
            }, function (services) {
                $scope.services = services.data;
            });
        } else if ($routeParams.org) {
            Catalog.browseServiceOrg({
                org: $routeParams.org
            }, function (services) {
                $scope.services = services.data;
            });
        } else {
            Catalog.listServices({
                start: $scope.start,
                end: $scope.end,
                sort: 'name'
            }, function (services) {
                $scope.total = services.totalNumber;
                services.data.forEach(function (e) {
                    e.tags = e.tags.split(',');
                });
                $scope.services = services.data;
            });
        }

        $scope.servicesActive = [];

        $scope.page = function (direction) {

            if (direction === 'next') {
                if ($scope.end < $scope.total) {
                    $scope.start += 10;
                    $scope.end += 10;
                }

            } else {
                if ($scope.start > 0) {
                    $scope.start -= 10;
                    $scope.end -= 10;
                }

            }

            Catalog.listServices({
                start: $scope.start,
                end: $scope.end,
                sort: 'name'
            }, function (services) {
                services.data.forEach(function (e) {
                    e.tags = e.tags.split(',');
                });
                $scope.services = services.data;
            });
        };

        $scope.isServiceActive = function (service) {
            return $scope.servicesActive.indexOf(service) > -1;
        };

        $scope.toggleServiceActive = function (service) {
            var index = $scope.servicesActive.indexOf(service);
            if (index === -1) {
                // add to servicesActive
                $scope.servicesActive.push(service);
            } else {
                // remove
                $scope.servicesActive.splice(index, 1);
            }
        };
    }
]);

app.controller('organizationsCtrl', ['$scope', '$http', '$routeParams', 'Catalog',
    function ($scope, $http, $routeParams, Catalog) {
        Catalog.listOrgs({}, function (data) {
            $scope.orgs = data.data;
        });
    }
]);
app.controller('organizationCtrl', ['$scope', '$http', '$routeParams', 'Catalog', 'Category',
    function ($scope, $http, $routeParams, Catalog, Category) {
        Catalog.getOrgById({
            id: $routeParams.id
        }, function (data) {
            $scope.org = data.data;
            if ($scope.org.category) {
                Category.getById({
                    id: $scope.org.category
                }, function (data) {
                    $scope.category = data.data;
                });
            }

        });
    }
]);

app.controller('cbCtrl', ['$location',
    function ($location) {
        function parseKeyValue(keyValue) {
            var obj = {}, key_value, key;
            angular.forEach((keyValue || '').split('&'), function (keyValue) {
                if (keyValue) {
                    key_value = keyValue.split('=');
                    key = decodeURIComponent(key_value[0]);
                    obj[key] = angular.isDefined(key_value[1]) ? decodeURIComponent(key_value[1]) : true;
                }
            });
            return obj;
        }

        var queryString = $location.url().substring(10); // preceding slash omitted
        var params = parseKeyValue(queryString);

        window.opener.postMessage(params, '*');
        window.close();
    }
]);

app.controller('serviceCtrl', ['$scope', '$routeParams', 'Catalog', 'Category', '$http', '$location', 'oAuth', 'RemoteApi',
    function ($scope, $routeParams, Catalog, Category, $http, $location, oAuth, RemoteApi) {
        $scope.remoteapi;
        $scope.template = 'partials/services/_about.html';
        $scope.request = {};
        Catalog.getServiceById({
            id: $routeParams.id
        }, function (data) {
            $scope.service = data.data;

            Catalog.getOrgById({
                id: $scope.service.organizationId
            }, function (data) {
                $scope.org = data.data;
            });
            if ($scope.service.category) {
                Category.getById({
                    id: $scope.service.category
                }, function (data) {
                    $scope.category = data.data;
                });
            }
            Catalog.getServiceMethods({
                id: $routeParams.id
            }, function (data) {
                $scope.methods = data.data;
                for (var i = 0; i < $scope.methods.length; i++) {
                    if ($scope.methods[i].testboxProperties) {
                        $scope.methods[i].testboxProperties.authentication = $scope.service.accessInformation.authentication;
                    }
                }
            });

        });

        function toTitleCase(str) {
            return str.replace(/(?:^|-)\w/g, function (match) {
                return match.toUpperCase();
            });
        }

        $scope.checkBeforeSend = function () {
            //remoteapi.ready ? true : false
            if ($scope.request.method && $scope.request.sample && $scope.request.sample.requestPath && $scope.request.sample.requestMethod) {
                return true;
            } else {
                return false;
            }
        };

        $scope.send = function () {
            var config = $scope.service.accessInformation.authentication.accessAttributes;
            $scope.remoteapi = new RemoteApi($scope.service.accessInformation.authentication.accessProtocol);
            $scope.remoteapi.authorize(config).then(function (result) {
                if (!$scope.request.sample.headers) {
                    $scope.request.sample.headers = {};
                }
                _.extend($scope.request.sample.headers, result);
                $http({
                    method: 'POST',
                    url: 'api/testbox',
                    data: $scope.request.sample,
                    withCredentials: true
                }).success(function (data, status, headers) {
                    $scope.response = 'HTTP/1.1 ' + status + '\n';
                    for (var key in headers()) {
                        $scope.response += toTitleCase(key) + ': ' + headers()[key] + '\n';
                    }
                    //$scope.response ='HTTP/1.1 200 OK\nContent-Type: application/xml\nX-Response-Time: 10ms\n\n<root>\n    <status code="0">\n        Successful\n    </status>\n</root>'
                    $scope.response += '\n' + data.data;
                });

                $scope.request.headers = result;
            });
        };

    }
]);
