'use strict';
app.controller('homeCtrl', [
		'$http',
		'$scope',
		'$rootScope',
		'$location',
		'Catalog',
		'Auth',
		function($http, $scope, $rootScope, $location, Catalog, Auth) {
			$scope.servicesTotal = 0;
			$scope.tagmax = 0;

			$http.get('api/catalog/news?n=5').success(function(data) {
				$scope.news = data.data;
			}).error(function(data) {
				$scope.news = [];
			});

			Catalog.countServices({}, function(response) {
				$scope.servicesTotal = response.totalNumber;
			}, function(res) {
			});

			$http.get('api/catalog/tagcloud?group=counter&order=DESC').success(
					function(data) {
						$scope.taglist = data.data;
						$scope.tagmax = 0;
						for ( var i = 0; i < $scope.taglist.length; i++) {
							if ($scope.taglist[i].counter > $scope.tagmax) {
								$scope.tagmax = $scope.taglist[i].counter;
							}
						}
					}).error(function(data) {
				$scope.taglist = [];
			});

			$scope.getTagCloudBarWidth = function(count, max) {
				var percentage = (count * 100) / max;
				return {
					'width' : percentage + '%'
				};
			};

			$scope.search = function(q) {
				$rootScope.searchQuery = q;
				$location.path('/search');
			};
		} ]);

app.controller('navCtrl', [ '$scope', 'Auth', '$location', '$rootScope',
		function($scope, Auth, $location, $rootScope) {

			if (!Auth.isLoggedIn()) {
				$rootScope.navtemplate = 'partials/nav/_signin.html';
			} else {
				$rootScope.navtemplate = 'partials/nav/_menu.html';
			}

			$scope.logout = function() {
				Auth.logout(function() {
					$rootScope.navtemplate = 'partials/nav/_signin.html';
					$location.path('/');
				});
			};
		} ]);

app.controller('breadCtrl', [ function() {
} ]);

app.controller('signinCtrl', [ '$scope', '$location', 'Auth', '$rootScope',
		'$http', '$window',
		function($scope, $location, Auth, $rootScope, $http, $window) {

			var url = $location.path();
			console.log(url);

			$scope.signin = function() {
				Auth.login($scope.user, function() {
					$rootScope.navtemplate = 'partials/nav/_menu.html';
					$location.path('profile');
				}, function(error) {
					$scope.error = error;
				}

				);
			};

			$scope.signInApiManager = function() {
				console.log('Api Manager sign in');
				console.log($scope.user);
				Auth.apiLogin($scope.user, function() {
					console.log('success api manager login');
					$rootScope.navtemplate = 'partials/nav/_menu.html';
					$location.path('profile');
				}, function(error) {
					$scope.error = error;
				});
			};

			$scope.signinFb = function() {
				console.log('Fb sign in');
				Auth.fbLogin(function(data) {
					console.log('success fb login');
					$rootScope.navtemplate = 'partials/nav/_menu.html';
					$location.path('profile');
				}, function(error) {
					console.log('error fb login');
					$scope.error = error;
				});
			};

			$scope.signinGoogle = function() {
				console.log('Google sign in');

				Auth.googleLogin(function() {
				}, function(data) {
					console.log('success google login');
					$rootScope.navtemplate = 'partials/nav/_menu.html';
					$location.path('profile');
				}, function(error) {
					console.log('error google login');
					$scope.error = error;
				});
			};

			/*
			 * $scope.signinTwitter = function(){ console.log('Twitter sign
			 * in'); Auth.twitterLogin(function(){ console.log('Twitter
			 * success'); $rootScope.navtemplate = 'partials/nav/_menu.html';
			 * $location.path('profile'); }, function(error){
			 * console.log('Twitter error'); $scope.error = error; }); };
			 */

			$scope.signinLinkedin = function() {
				console.log('Linkedin sign in');
				Auth.linkedinLogin(function() {
					console.log('Linkedin success');
					$rootScope.navtemplate = 'partials/nav/_menu.html';
					$location.path('profile');
				}, function(error) {
					console.log('Linkedin error');
					$scope.error = error;
				});
			};
		} ]);

app.controller('signUpCtrl', [ '$scope', 'User', function($scope, User) {
	$scope.user = new User();
	$scope.submit = function() {
		$scope.user.$save($scope.user, function() {
			$scope.success = true;
		}, function(data) {
			$scope.error = data.data.error;
		});
	};
} ]);

app.controller('resetCtrl', [ '$scope', '$location', 'User',
		function($scope, $location, User) {
			$scope.user = new User();
			$scope.submit = function() {

			};
		} ]);
// ToDo - passwCtrl (Giulia)
app.controller('passwCtrl', [ '$scope', '$location', 'User',
		function($scope, $location, User) {
			$scope.submit = function() {
				var password = {
					newP : $scope.newP,
					oldP : $scope.oldP
				};
				User.updatePassw(password, function() {
					/*
					 * $timeout(function(){ $scope.success = true; },60000);
					 */
					$scope.success = true;
					// $location.path('profile');
				}, function(res) {
					$scope.errorMsg = res.data.error;
				});
			};
		} ]);

app.controller('enableCtrl', [ '$scope', '$routeParams', 'User',
		function($scope, $routeParams, User) {
			User.enable({
				key : $routeParams.key
			}, function() {
				$scope.active = true;

			}, function() {
				$scope.active = false;

			});
		} ]);

app.controller('enableOrgCtrl', [ '$scope', '$routeParams', 'Org', '$location',
		function($scope, $routeParams, Org, $location) {
			Org.enable({
				key : $routeParams.key
			}, function() {
				$location.path('profile');

			}, function() {
				$scope.active = false;

			});
		} ]);

app
		.controller(
				'profileCtrl',
				[
						'$scope',
						'$rootScope',
						'$routeParams',
						'$http',
						'$location',
						'User',
						'Service',
						'Org',
						'Category',
						function($scope, $rootScope, $routeParams, $http,
								$location, User, Service, Org, Category) {
							$scope.errorMsg = null;
							$rootScope.locTitles = [ 'profile' ];

							$scope.switchTo = function(view) {
								if (view == 'services') {
									$scope.template = 'partials/profile/services/show.html';
									$location.path('/profile/services', false);
								} else if (view == 'organizations') {
									$scope.template = 'partials/profile/organizations/show.html';
									$location.path('/profile/organizations',
											false);
								} else {
									$scope.template = 'partials/profile/_details.html';
									$location.path('/profile', false);
								}
							}
							$scope.switchTo($routeParams.view);

							$scope.deleteOrg = function(i) {
								Org.remove({
									id : $scope.orgs[i].id
								}, function() {
									$scope.orgs.splice(i, 1);
									$location.path('profile');
								}, function(res) {
									$scope.errorMsg = res.data.error;
								});
							};

							$scope.deprecateService = function(i) {
								Service.deprecate({
									id : $scope.services[i].id
								}, {}, function() {
									$scope.services[i].state = 'DEPRECATE';
									$location.path('profile');
								});
							};
							$scope.publishService = function(i) {
								Service.publish({
									id : $scope.services[i].id
								}, {}, function() {
									$scope.services[i].state = 'PUBLISH';
									$location.path('profile');
								});
							};
							$scope.unpublishService = function(i) {
								Service.unpublish({
									id : $scope.services[i].id
								}, {}, function() {
									$scope.services[i].state = 'UNPUBLISH';
									$location.path('profile');
								});
							};
							$scope.deleteService = function(i) {
								Service.remove({
									id : $scope.services[i].id
								}, function() {
									$scope.services.splice(i, 1);
									$location.path('profile');
								});
							};

							$scope.submit = function() {
								User.update($scope.user, function() {
									$location.path('profile');
								}, function(res) {
									$scope.errorMsg = res.data.error;
								});
							};

							User.getInfo({}, function(data) {
								$scope.user = data.data;
							});

							Org.get({}, function(data) {
								$scope.orgs = data.data;
							});
							Service.get({}, function(data) {
								$scope.services = data.data;
							});

							// Categories
							$scope.deleteCategory = function(i) {
								Category.remove({
									id : $scope.categories[i].id
								}, function() {
									$scope.categories.splice(i, 1);
									$location.path('profile');
								}, function(res) {
									$scope.errorMsg = res.data.error;
								});
							};
							Category.list({}, function(data) {
								$scope.categories = data.data;
							});
						} ]);

app
		.controller(
				'newServiceCtrl',
				[
						'$scope',
						'$rootScope',
						'$http',
						'$location',
						'Service',
						'Org',
						'Category',
						function($scope, $rootScope, $http, $location, Service,
								Org, Category) {
							$scope.protocols = [ 'OAuth2', 'OpenID', 'Public' ];

							$scope.serviceFormats = {
								'JSON' : false,
								'XML' : false,
								'text' : false
							};
							$scope.serviceProtocols = {
								'REST' : false,
								'SOAP' : false
							};

							$rootScope.locTitles = [ 'profile', 'services' ];
							$scope.accessInformation = {
								authentication : {
									accessProtocol : null,
									accessAttributes : {
										client_id : null,
										response_type : null,
										authorizationUrl : null,
										grant_type : null
									}
								}
							};
							// set default value
							$scope.accessInformation.authentication.accessProtocol = 'Public';

							Category.list({}, function(data) {
								$scope.categories = data.data;
							});

							Org.get({}, function(data) {
								$scope.orgs = data.data;
							});

							$scope.submit = function() {
								var arr = [];
								for ( var key in $scope.serviceFormats)
									if ($scope.serviceFormats[key])
										arr.push(key);
								$scope.service.accessInformation.formats = arr
										.join();
								arr = [];
								for ( var key in $scope.serviceProtocols)
									if ($scope.serviceProtocols[key])
										arr.push(key);
								$scope.service.accessInformation.protocols = arr
										.join();
								if (!$scope.service.accessInformation.authentication) {
									$scope.service.accessInformation.authentication = {};
									$scope.service.accessInformation.authentication.accessProtocol = 'Public';
								}
								if ($scope.service.expiration) {
									$scope.service.expiration = new Date(
											$scope.service.expiration)
											.getTime();
								} else {
									$scope.service.expiration = '';
								}
								if (typeof $scope.service.tags == 'string') {
									$scope.service.tags = $scope.service.tags
											.split(',');
								}
								Service.create($scope.service, function() {
									$location.path('profile');
								}, function(res) {
									$scope.errorMsg = res.data.error;
								});
							};
							$scope.keep = function() {
								$scope.service.accessInformation = $scope.accessInformation;
							};
						} ]);

app
		.controller(
				'editServiceCtrl',
				[
						'$scope',
						'$rootScope',
						'$routeParams',
						'$location',
						'Service',
						'Org',
						'Category',
						function($scope, $rootScope, $routeParams, $location,
								Service, Org, Category) {
							$scope.protocols = [ 'OAuth2', 'OpenID', 'Public' ];
							$scope.serviceFormats = {
								'JSON' : false,
								'XML' : false,
								'text' : false
							};
							$scope.serviceProtocols = {
								'REST' : false,
								'SOAP' : false
							};

							$scope.accessInformation = {
								authentication : {
									accessProtocol : null,
									accessAttributes : {
										client_id : null,
										response_type : null,
										authorizationUrl : null,
										grant_type : null
									}
								}
							};
							Category.list({}, function(data) {
								$scope.categories = data.data;
							});

							Service
									.getDescription(
											{
												id : $routeParams.id
											},
											function(data) {
												$scope.service = data.data;
												$rootScope.locTitles = [
														'profile', 'services',
														$scope.service.name ];
												if ($scope.service.accessInformation !== null) {
													$scope.accessInformation = $scope.service.accessInformation;
												}
												if ($scope.service.expiration
														&& $scope.service.expiration > 0) {
													$scope.service.expiration = new Date(
															$scope.service.expiration)
															.toISOString()
															.slice(0, 10);
												} else {
													$scope.service.expiration = '';
												}
												var arr = $scope.accessInformation.formats
														.split(',');
												angular
														.forEach(
																arr,
																function(value,
																		key) {
																	if (value in $scope.serviceFormats)
																		$scope.serviceFormats[value] = true
																});

												arr = $scope.accessInformation.protocols
														.split(',');
												angular
														.forEach(
																arr,
																function(value,
																		key) {
																	if (value in $scope.serviceProtocols)
																		$scope.serviceProtocols[value] = true
																});
											});
							Org.get({}, function(data) {
								$scope.orgs = data.data;
							});

							$scope.keep = function() {
								$scope.service.accessInformation = $scope.accessInformation;
							};

							$scope.submit = function() {
								var arr = [];
								for ( var key in $scope.serviceFormats)
									if ($scope.serviceFormats[key])
										arr.push(key);
								$scope.service.accessInformation.formats = arr
										.join();
								arr = [];
								for ( var key in $scope.serviceProtocols)
									if ($scope.serviceProtocols[key])
										arr.push(key);
								$scope.service.accessInformation.protocols = arr
										.join();

								if ($scope.service.expiration) {
									$scope.service.expiration = new Date(
											$scope.service.expiration)
											.getTime();
								}
								if (typeof $scope.service.tags == 'string'
										&& $scope.service.tags.length >= 1)
									$scope.service.tags = $scope.service.tags
											.split(',');

								Service.update($scope.service, function() {
									$location.path('profile/services/'
											+ $routeParams.id);
								}, function(res) {
									$scope.errorMsg = res.data.error;
								});
							};
						} ]);

app.controller('viewServiceCtrl', [
		'$scope',
		'$rootScope',
		'$routeParams',
		'$location',
		'Service',
		'Org',
		'Category',
		function($scope, $rootScope, $routeParams, $location, Service, Org,
				Category) {

			Service.getDescription({
				id : $routeParams.id
			},
					function(data) {
						$scope.service = data.data;
						$rootScope.locTitles = [ 'profile', 'services',
								$scope.service.name ];
						if ($scope.service.expiration
								&& $scope.service.expiration > 0) {
							$scope.service.expiration = new Date(
									$scope.service.expiration).toISOString()
									.slice(0, 10);
						}
						Org.getById({
							id : data.data.organizationId
						}, function(data) {
							$scope.org = data.data;
						});

						if ($scope.service.category) {
							Category.getById({
								id : $scope.service.category
							}, function(data) {
								$scope.category = data.data;
							});
						}

					});
			Service.getMethods({
				id : $routeParams.id
			}, function(data) {
				$scope.methods = data.data;
			});

			$scope.deleteMethod = function(i) {
				Service.deleteMethod({
					id : $scope.methods[i].id
				}, function() {
					$scope.methods.splice(i, 1);
					$location.path('profile/services/' + $scope.service.id
							+ '/view');
				});
			};
		} ]);

app
		.controller(
				'methodCtrl',
				[
						'$scope',
						'$http',
						'$location',
						'$routeParams',
						'Service',
						function($scope, $http, $location, $routeParams,
								Service) {
							if (!!$routeParams.method) {
								Service.getMethod({
									id : $routeParams.method
								}, function(data) {
									$scope.method = data.data;
								});
								$scope.submit = function() {
									Service
											.updateMethod(
													$scope.method,
													function() {
														$location
																.path('profile/services/'
																		+ $routeParams.id);
													},
													function(res) {
														$scope.errorMsg = res.data.error;
													});
								};
							} else {
								$scope.title = 'New';
								$scope.method = {
									serviceId : $routeParams.id,
									testboxProperties : {
										tests : []
									},
									executionProperties : {}
								};
								$scope.submit = function() {
									Service
											.createMethod(
													$scope.method,
													function() {
														$location
																.path('profile/services/'
																		+ $routeParams.id);
													},
													function(res) {
														$scope.errorMsg = res.data.error;
													});
								};
							}
							$scope.deleteTest = function(i) {
								$scope.method.testboxProperties.tests.splice(i,
										1);
							};

							$scope.editTest = function(idx) {
								$scope.testErrorMsg = null;
								if (idx >= 0) {
									$scope.test = angular
											.copy($scope.method.testboxProperties.tests[idx]);
								} else {
									$scope.test = {};
								}
								$scope.ntestheader = null;
								$scope.testIdx = idx;
							}

							$scope.addTest = function() {
								for ( var i = 0; i < $scope.method.testboxProperties.tests.length; i++) {
									if (i == $scope.testIdx)
										continue;
									if ($scope.method.testboxProperties.tests[i].name == $scope.test.name) {
										$scope.testErrorMsg = 'Duplicate test name';
										return false;
									}
								}
								if ($scope.testIdx >= 0) {
									$scope.method.testboxProperties.tests[$scope.testIdx] = $scope.test;
								} else {
									$scope.method.testboxProperties.tests
											.push($scope.test);
								}
								$('#testModal').modal('hide');
							}

							$scope.addHeader = function() {
								if (!$scope.method.executionProperties.headers) {
									$scope.method.executionProperties.headers = {};
								}
								var vals = $scope.nheader.value.split(',');
								for ( var i = 0; i < vals.length; i++) {
									vals[i] = vals[i].trim();
								}
								$scope.method.executionProperties.headers[$scope.nheader.name] = vals;
							};
							$scope.deleteHeader = function(n) {
								delete $scope.method.executionProperties.headers[n];
							};
							$scope.methodValid = function() {
								return !!$scope.method
										&& !!$scope.method.name
										&& !!$scope.method.executionProperties.httpMethod;
							}

							$scope.addTestHeader = function() {
								if (!$scope.test.headers) {
									$scope.test.headers = {};
								}
								$scope.test.headers[$scope.ntestheader.name] = $scope.ntestheader.value;
							};
							$scope.deleteTestHeader = function(n) {
								delete $scope.test.headers[n];
							};

							$scope.testHasBody = function() {
								return !!$scope.method
										&& ($scope.method.executionProperties.httpMethod == 'POST' || $scope.method.executionProperties.httpMethod == 'PUT');
							}
							$scope.testValid = function() {
								return !!$scope.test && !!$scope.test.name
										&& !!$scope.test.description
										&& !!$scope.test.requestPath;
							}

						} ]);

app.controller('newOrgCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$location',
		'Org',
		'Category',
		function($scope, $rootScope, $http, $location, Org, Category) {
			$scope.title = 'New';

			$rootScope.locTitles = [ 'profile', 'organizations' ];
			Category.list({}, function(data) {
				$scope.categories = data.data;
			});

			$scope.onFile = function(file) {
				$scope.file = file;
			};

			$scope.submit = function() {
				Org.create($scope.org, function(res) {
					if ($scope.file) {
						$http({
							method : 'POST',
							url : 'api/file/upload/' + res.data.id,
							headers : {
								'Content-Type' : false
							},
							transformRequest : function() {
								var formData = new FormData();
								formData.append('file', $scope.file);
								return formData;
							}
						}).success(
								function() {
									res.data.logo = 'upload/' + res.data.id + '/'
											+ $scope.file.name;
									Org.update(res.data, function() {
										$location.path('profile');
									});
								}).error(function(res) {
							$scope.errorMsg = res.data.error;
						});
					}
					$location.path('profile');
				}, function(res) {
					$scope.errorMsg = res.data.error;
				});
			};

		} ]);

app.controller('editOrgCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$location',
		'$routeParams',
		'Org',
		'Category',
		function($scope, $rootScope, $http, $location, $routeParams, Org,
				Category) {
			$scope.title = 'Edit';
			$scope.onFile = function(file) {
				$scope.file = file;
			};
			Org.getById({
				id : $routeParams.id
			}, function(org) {
				$scope.org = org.data;
				$rootScope.locTitles = [ 'profile', 'organizations',
						$scope.org.name ];
			});
			Category.list({}, function(data) {
				$scope.categories = data.data;
			});
			$scope.submit = function() {
				if ($scope.file) {
					$http({
						method : 'POST',
						url : 'api/file/upload/' + $scope.org.id,
						headers : {
							'Content-Type' : false
						},
						transformRequest : function() {
							var formData = new FormData();
							formData.append('file', $scope.file);
							return formData;
						}
					}).success(
							function() {
								$scope.org.logo = 'upload/' + $scope.org.id
										+ '/' + $scope.file.name;
								Org.update($scope.org, function() {
									$location.path('profile');
								});

							}).error(function(res) {
						$scope.errorMsg = res.data.error;
					});

				} else {
					Org.update($scope.org, function() {
						$location.path('profile');
					}, function(res) {
						$scope.errorMsg = res.data.error;
					});
				}

			};
		} ]);

app.controller('editOrgMembersCtrl', [ '$scope', '$http', '$location', 'Org',
		function($scope, $http, $location, Org) {

			$scope.serviceCount = function(i) {
				return $scope.categoryData.services[i];
			};
		} ]);

app.controller('categoriesCtrl', [ '$scope', '$rootScope', '$http',
		'$location', 'Catalog',
		function($scope, $rootScope, $http, $location, Catalog) {
			$rootScope.locTitles = [ 'categories' ];

			Catalog.browseAllServiceCat({}, function(data) {
				$scope.categoryData = data.data;
			});

			$scope.serviceCount = function(i) {
				return $scope.categoryData.services[i];
			};
		} ]);

app.controller('categoryCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$location',
		'Catalog',
		'Category',
		'$routeParams',
		function($scope, $rootScope, $http, $location, Catalog, Category,
				$routeParams) {
			$scope.start = 0;
			$scope.end = 9;
			$scope.update = function() {
				Catalog.browseServiceCat({
					category : $routeParams.category,
					first : $scope.start,
					last : $scope.end,
					order : 'name'
				}, function(services) {
					$scope.total = services.totalNumber;
					$scope.services = services.data;
				});
			};
			$scope.update();

			Category.getById({
				id : $routeParams.category
			}, function(data) {
				$scope.category = data.data;
				$rootScope.locTitles = [ 'categories', $scope.category.name ];
			});

			$scope.next = function() {
				if ($scope.end < $scope.total) {
					$scope.start += 10;
					$scope.end += 10;
					$scope.update();
				}
			};

			$scope.prev = function() {
				if ($scope.start > 0) {
					$scope.start -= 10;
					$scope.end -= 10;
					$scope.update();
				}
			};

			$scope.serviceCount = function(i) {
				return $scope.categoryData.services[i];
			};
		} ]);

app
		.controller(
				'servicesCtrl',
				[
						'$scope',
						'$rootScope',
						'$http',
						'$routeParams',
						'$filter',
						'Catalog',
						'Category',
						function($scope, $rootScope, $http, $routeParams,
								$filter, Catalog, Category) {

							$scope.selectCategory = function(catId) {

								if (catId == -1) { // All
									var index = $scope.categoriesFilter
											.indexOf(catId);
									if (index === -1) {
										$scope.categoriesFilter = [ -1 ];
									} else {
										$scope.categoriesFilter
												.splice(index, 1);
									}
								} else {
									var index = $scope.categoriesFilter
											.indexOf(catId);
									if (index === -1) {
										$scope.categoriesFilter.push(catId);
									} else {
										$scope.categoriesFilter
												.splice(index, 1);
									}
								}
								$scope.update(true);
							};

							$scope.setOrderBy = function(order) {
								$scope.orderBySelected = order;
								$scope.update(true);
							};

							$scope.getServiceByCategories = function() {
								var ids = $scope.categoriesFilter;

								console.log("Search categories with values: "
										+ ids);

								Catalog
										.browseServiceCats(
												{
													categories : ids
															.indexOf(-1) !== -1 ? null
															: ids,
													first : $scope.firstOfPage,
													last : $scope.resultsPerPage,
													order : $scope.orderBySelected.value,
													q : $scope.query
												},
												function(services) {
													$scope.services = services.data;
													$scope
															.updateCounters(services.totalNumber);
												}, function(res) {
													$scope.services = [];
												});
							};

							$scope.getServicesAll = function() {
								Catalog
										.listServices(
												{
													first : $scope.firstOfPage,
													last : $scope.resultsPerPage,
													order : $scope.orderBySelected.value,
													q : $scope.query,
													tag : $scope.queryTag
												},
												function(services) {
													$scope.services = services.data;
													$scope
															.updateCounters(services.totalNumber);
												}, function(res) {
													$scope.services = [];
												});
							};

							$scope.update = function(resetPages) {
								if (resetPages == true) {
									$scope.firstOfPage = 0;
									$scope.currentPage = 1;
								}

								if ($scope.categoriesFilter.length == 0) {
									$scope.services = [];
								} else if ($scope.categoriesFilter.indexOf(-1) !== -1) {
									$scope.getServicesAll();
								} else {
									$scope.getServiceByCategories();
								}
							};

							$scope.goToPage = function(p) {
								$scope.page = p;
								$scope.firstOfPage = ($scope.page - 1)
										* $scope.resultsPerPage;
								$scope.update();
							};

							$scope.updateCounters = function(totalServicesCount) {
								$scope.totalServices = totalServicesCount;
								var lop = $scope.firstOfPage
										+ $scope.resultsPerPage;
								$scope.lastOfPage = lop > $scope.totalServices ? $scope.totalServices
										: lop;
								$scope.totalPages = Math
										.ceil($scope.totalServices
												/ $scope.resultsPerPage);
							};

							$scope.isServiceActive = function(service) {
								return $scope.servicesActive.indexOf(service) > -1;
							};

							$scope.toggleServiceActive = function(service) {
								var index = $scope.servicesActive
										.indexOf(service);
								if (index === -1) {
									// add to servicesActive
									$scope.servicesActive.push(service);
								} else {
									// remove
									$scope.servicesActive.splice(index, 1);
								}
							};

							$scope.orderByOptions = [ {
								key : 'A - Z',
								value : 'name'
							}, {
								key : 'Z - A',
								value : 'namedesc'
							}, {
								key : 'date',
								value : 'date'
							} ];

							// default: A - Z
							$scope.orderBySelected = $scope.orderByOptions[0];

							$scope.categories = [];
							$scope.categoriesFilter = [ -1 ];

							$scope.totalServices = 0;
							$scope.firstOfPage = 0;
							$scope.resultsPerPage = 5;
							$scope.pagePerPagination = 5;
							$scope.currentPage = 1;
							$scope.lastOfPage = 0;
							$scope.services = [];
							$scope.query = null;

							$rootScope.locTitles = $rootScope.searchQuery ? [ 'search' ]
									: [ 'services' ];

							if (!!$routeParams.tag) {
								$scope.queryTag = $routeParams.tag;
							}

							if (!!$scope.categoryActive) {
								$scope.categoryActive = undefined;
							}

							// Start!
							Category.list({}, function(data) {
								$scope.categories = data.data;
								$scope.update();
							});

							if ($rootScope.searchQuery != null) {
								$scope.query = $rootScope.searchQuery;
								$scope.categoriesFilter = $scope.categories;
								$scope.getServiceByCategories();
								$rootScope.searchQuery = null;
							}
						} ]);

app
		.controller(
				'organizationsCtrl',
				[
						'$scope',
						'$rootScope',
						'$http',
						'$routeParams',
						'$location',
						'Catalog',
						'Category',
						function($scope, $rootScope, $http, $routeParams,
								$location, Catalog, Category) {
							$scope.orderByOptions = [ {
								key : 'A - Z',
								value : 'name'
							}, {
								key : 'Z - A',
								value : 'namedesc'
							}, {
								key : 'date',
								value : 'date'
							} ];

							// default: A - Z
							$scope.orderBySelected = $scope.orderByOptions[0];

							$scope.categories = [];
							$scope.categoriesFilter = [ -1 ];

							$scope.selectCategory = function(catId) {

								if (catId == -1) { // All
									var index = $scope.categoriesFilter
											.indexOf(catId);
									if (index === -1) {
										$scope.categoriesFilter = [ -1 ];
									} else {
										$scope.categoriesFilter
												.splice(index, 1);
									}
								} else {
									var index = $scope.categoriesFilter
											.indexOf(catId);
									if (index === -1) {
										$scope.categoriesFilter.push(catId);
									} else {
										$scope.categoriesFilter
												.splice(index, 1);
									}
								}
								$scope.update(true);
							};

							$scope.setOrderBy = function(order) {
								$scope.orderBySelected = order;
								$scope.update(true);
							};

							$scope.totalServices = 0;
							$scope.firstOfPage = 0;
							$scope.resultsPerPage = 5;
							$scope.pagePerPagination = 5;
							$scope.currentPage = 1;
							$scope.lastOfPage = 0;

							$rootScope.locTitles = $rootScope.searchQuery ? [ 'search' ]
									: [ 'organizations' ];

							if (!!$routeParams.tag) {
								$scope.query = $routeParams.tag;
							}

							if (!!$scope.categoryActive) {
								$scope.categoryActive = undefined;
							}

							$scope.getOrgsAll = function() {
								Catalog
										.listOrgs(
												{
													first : $scope.firstOfPage,
													last : $scope.resultsPerPage,
													order : $scope.orderBySelected.value,
													q : $scope.query,
													cats : $scope.categoriesFilter
															.indexOf(-1) !== -1 ? null
															: $scope.categoriesFilter
												},
												function(data) {
													$scope.orgs = data.data;
													$scope
															.fillOrgsCategoryName($scope.orgs);
													$scope
															.updateCounters(data.totalNumber);
												}, function(res) {
													$scope.orgs = [];
												});
							};

							/*
							 * $scope.getOrgsByCategories = function () { var
							 * ids = $scope.categoriesFilter; var cats = ''; for
							 * (var i = 0; i < ids.length; i++) { if (i > 0) {
							 * cats += ','; } cats += ids[i]; }
							 * 
							 * $scope.getOrgsAll(); Catalog.browseOrgCats({
							 * categories: cats, first: $scope.firstOfPage,
							 * last: $scope.resultsPerPage, order:
							 * $scope.orderBySelected.value, q:
							 * $rootScope.searchQuery }, function (data) {
							 * $scope.orgs.splice(0,$scope.orgs.length);
							 * data.data.forEach(function(s){ if(!$scope.query ||
							 * s.name.indexOf($scope.query) >= 0) {
							 * $scope.orgs.push(s); } });
							 * $scope.fillOrgsCategoryName($scope.orgs);
							 * $scope.updateCounters(data.totalNumber); },
							 * function (res) { $scope.orgs = []; }); };
							 */

							$scope.update = function(resetPages) {
								if (resetPages == true) {
									$scope.firstOfPage = 0;
									$scope.currentPage = 1;
								}

								if ($scope.categoriesFilter.length == 0) {
									$scope.orgs = [];
								} else {
									$scope.getOrgsAll();
								}
							};

							$scope.goToPage = function(p) {
								$scope.page = p;
								$scope.firstOfPage = ($scope.page - 1)
										* $scope.resultsPerPage;
								$scope.update();
							};

							$scope.updateCounters = function(totalServicesCount) {
								$scope.totalServices = totalServicesCount;
								var lop = $scope.firstOfPage
										+ $scope.resultsPerPage;
								$scope.lastOfPage = lop > $scope.totalServices ? $scope.totalServices
										: lop;
								$scope.totalPages = Math
										.ceil($scope.totalServices
												/ $scope.resultsPerPage);
							};

							$rootScope.searchQuery = null;
							$scope.query = null;
							$scope.servicesActive = [];

							$scope.isServiceActive = function(service) {
								return $scope.servicesActive.indexOf(service) > -1;
							};

							$scope.toggleServiceActive = function(service) {
								var index = $scope.servicesActive
										.indexOf(service);
								if (index === -1) {
									// add to servicesActive
									$scope.servicesActive.push(service);
								} else {
									// remove
									$scope.servicesActive.splice(index, 1);
								}
							};

							$scope.getCategoryName = function(id) {
								for ( var i = 0; i < $scope.categories.length; i++) {
									var cat = $scope.categories[i];
									if (cat.id == id) {
										return cat.name;
									}
								}
								return null;
							};

							$scope.fillOrgsCategoryName = function(orgs) {
								for ( var o = 0; o < orgs.length; o++) {
									var org = orgs[o];
									org.categoryName = $scope
											.getCategoryName(org.category);
								}
							};

							// Start!
							Category.list({}, function(data) {
								$scope.categories = data.data;
								$scope.update();
							});

							$scope.search = function(q) {
								$rootScope.searchQuery = q;
								$location.path('/organizations');
								$scope.update();
							};
						} ]);

app.controller('organizationCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$routeParams',
		'Catalog',
		'Category',
		'Org',
		function($scope, $rootScope, $http, $routeParams, Catalog, Category,
				Org) {
			Catalog.getOrgById({
				id : $routeParams.id
			}, function(data) {
				$scope.org = data.data;
				$rootScope.locTitles = [ 'organizations', $scope.org.name ];
				if ($scope.org.category) {
					Category.getById({
						id : $scope.org.category
					}, function(data) {
						$scope.category = data.data;
					});
				}

			});
			Org.getOrgHistory({
				id : $routeParams.id
			}, function(data) {
				$scope.histories = data.data;
			}, function(res) {
				$scope.histories = null;
			});
		} ]);

app
		.controller(
				'organizationServicesCtrl',
				[
						'$scope',
						'$rootScope',
						'$http',
						'$routeParams',
						'Org',
						'Catalog',
						'Category',
						function($scope, $rootScope, $http, $routeParams, Org,
								Catalog, Category) {

							$scope.getServiceByCategories = function() {
								var ids = $scope.categoriesFilter;

								console.log("Search categories with values: "
										+ ids);

								Catalog
										.browseServiceOrg(
												{
													org : $routeParams.id,
													first : $scope.firstOfPage,
													last : $scope.resultsPerPage,
													order : $scope.orderBySelected.value,
													cats : ids[0] !== -1 ? ids
															: null,
													q : $scope.query
												},
												function(services) {
													$scope.services = services.data;
													$scope
															.updateCounters(services.totalNumber);
												}, function(res) {
													$scope.services = [];
												});
							};
							$scope.setOrderBy = function(order) {
								$scope.orderBySelected = order;
								$scope.update(true);
							};

							$scope.update = function(resetPages) {
								if (resetPages == true) {
									$scope.firstOfPage = 0;
									$scope.currentPage = 1;
								}
								$scope.getServiceByCategories();
							};

							$scope.goToPage = function(p) {
								$scope.page = p;
								$scope.firstOfPage = ($scope.page - 1)
										* $scope.resultsPerPage;
								$scope.update();
							};

							$scope.updateCounters = function(totalServicesCount) {
								$scope.totalServices = totalServicesCount;
								var lop = $scope.firstOfPage
										+ $scope.resultsPerPage;
								$scope.lastOfPage = lop > $scope.totalServices ? $scope.totalServices
										: lop;
								$scope.totalPages = Math
										.ceil($scope.totalServices
												/ $scope.resultsPerPage);
							};

							$scope.isServiceActive = function(service) {
								return $scope.servicesActive.indexOf(service) > -1;
							};

							$scope.toggleServiceActive = function(service) {
								var index = $scope.servicesActive
										.indexOf(service);
								if (index === -1) {
									// add to servicesActive
									$scope.servicesActive.push(service);
								} else {
									// remove
									$scope.servicesActive.splice(index, 1);
								}
							};

							$scope.orderByOptions = [ {
								key : 'A - Z',
								value : 'name'
							}, {
								key : 'Z - A',
								value : 'namedesc'
							}, {
								key : 'date',
								value : 'date'
							} ];

							// default: A - Z
							$scope.orderBySelected = $scope.orderByOptions[0];

							$scope.categories = [];
							$scope.categoriesFilter = [ -1 ];

							$scope.selectCategory = function(catId) {

								if (catId == -1) { // All
									var index = $scope.categoriesFilter
											.indexOf(catId);
									if (index === -1) {
										$scope.categoriesFilter = [ -1 ];
									} else {
										$scope.categoriesFilter
												.splice(index, 1);
									}
								} else {
									var index = $scope.categoriesFilter
											.indexOf(catId);
									if (index === -1) {
										$scope.categoriesFilter.push(catId);
									} else {
										$scope.categoriesFilter
												.splice(index, 1);
									}
								}
								$scope.update(true);
							};

							$scope.totalServices = 0;
							$scope.firstOfPage = 0;
							$scope.resultsPerPage = 5;
							$scope.pagePerPagination = 5;
							$scope.currentPage = 1;
							$scope.lastOfPage = 0;

							$rootScope.locTitles = $rootScope.searchQuery ? [ 'search' ]
									: [ 'organizations' ];

							if (!!$routeParams.tag) {
								$scope.query = $routeParams.tag;
							}

							if (!!$scope.categoryActive) {
								$scope.categoryActive = undefined;
							}

							// Start!
							Category.list({}, function(data) {
								$scope.categories = data.data;
								$scope.update(true);
							});
						} ]);

app
		.controller(
				'serviceCtrl',
				[
						'$scope',
						'$rootScope',
						'$routeParams',
						'Catalog',
						'Category',
						'$http',
						'$location',
						'RemoteApi',
						function($scope, $rootScope, $routeParams, Catalog,
								Category, $http, $location, RemoteApi) {
							$scope.remoteapi;
							$scope.methodMap = {};

							$scope.toggle = function(id) {
								var m = $scope.methodMap[id];
								$('#body' + id).collapse('toggle');
								if (!!m && !m._request) {
									m._request = {
										requestPath : m.executionProperties.requestPathTemplate,
										requestNody : m.executionProperties.requestBodyTemplate
									};
								}
							};
							$scope.checkBeforeSend = function(id) {
								var m = $scope.methodMap[id];
								return !m.testboxProperties.testable
										&& (m._request == null || m._request.name == null);
							};

							$scope.reset = function(id) {
								var m = $scope.methodMap[id];
								m._request = {
									requestPath : m.executionProperties.requestPathTemplate,
									requestNody : m.executionProperties.requestBodyTemplate
								};
								m._response = null;
								m._call = null;
							};

							$scope.useTest = function(id, test) {
								var m = $scope.methodMap[id];
								m._request.name = test.name;
								m._request.requestPath = test.requestPath;
								m._request.requestBody = test.requestBody;
								m._request.description = test.description;
							};

							$scope.hasBody = function(method) {
								return !!method
										&& (method.executionProperties.httpMethod == 'POST' || method.executionProperties.httpMethod == 'PUT');
							};

							Catalog
									.getServiceById(
											{
												id : $routeParams.id
											},
											function(data) {
												$scope.service = data.data;
												$rootScope.locTitles = [
														'services',
														$scope.service.name ];

												Catalog
														.getOrgById(
																{
																	id : $scope.service.organizationId
																},
																function(data) {
																	$scope.org = data.data;
																});
												if ($scope.service.category) {
													Category
															.getById(
																	{
																		id : $scope.service.category
																	},
																	function(
																			data) {
																		$scope.category = data.data;
																	});
												}
												Catalog
														.getServiceMethods(
																{
																	id : $routeParams.id
																},
																function(data) {
																	$scope.methods = data.data;
																	for ( var i = 0; i < $scope.methods.length; i++) {
																		if ($scope.methods[i].testboxProperties) {
																			$scope.methods[i].testboxProperties.authentication = $scope.service.accessInformation.authentication;
																		}
																		$scope.methodMap[$scope.methods[i].id] = $scope.methods[i];
																	}
																});
												Catalog
														.getListServiceHistory(
																{
																	id : $routeParams.id
																},
																function(data) {
																	$scope.histories = data.data;
																});

											});

							function toTitleCase(str) {
								return str.replace(/(?:^|-)\w/g,
										function(match) {
											return match.toUpperCase();
										});
							}

							$scope.formatJson = function(j) {
								if (!j)
									return '';
								try {
									var obj = JSON.parse(j);
									return JSON.stringify(obj, undefined, 2);
								} catch (err) {
									return j;
								}
							};

							$scope.send = function(id) {
								$scope.currentMethod = $scope.methodMap[id];
								$scope.remoteapi = new RemoteApi(
										$scope.currentMethod.testboxProperties.authentication.accessProtocol);
								$scope.remoteapi
										.authorize($scope.currentMethod.id)
										.then(
												function(result) {
													var ctx = $scope.service.accessInformation.endpoint;
													var r = $scope.currentMethod._request.requestPath;
													if (ctx[ctx.length - 1] === '/') {
														ctx = ctx.slice(0, -1);
													}

													if (r[0] === '/') {
														r = r.slice(1);
													}

													var req = {
														name : $scope.currentMethod._request.name,
														requestUrl : encodeURI(ctx
																+ '/' + r),
														requestBody : $scope.currentMethod._request.requestBody,
														credentials : result
													};
													$http(
															{
																method : 'POST',
																url : 'api/testbox/test/'
																		+ $scope.currentMethod.id,
																data : req,
																withCredentials : true
															})
															.success(
																	function(
																			data,
																			status,
																			headers) {
																		$scope.currentMethod._response = 'HTTP/1.1 '
																				+ status
																				+ '\n';
																		for ( var key in headers()) {
																			$scope.currentMethod._response += toTitleCase(key)
																					+ ': '
																					+ headers()[key]
																					+ '\n';
																		}
																		$scope.currentMethod._response += '\n'
																				+ $scope
																						.formatJson(data.data);
																	})
															.error(
																	function(
																			data,
																			status,
																			headers) {
																		$scope.currentMethod._response = 'HTTP/1.1 '
																				+ status
																				+ '\n';
																		if (data) {
																			$scope.currentMethod._response += data.data;
																		}
																	});

													$scope.currentMethod._call = req.requestUrl;
													if (!!req.requestBody) {
														$scope.currentMethod._call += '\n'
																+ $scope
																		.formatJson(req.requestBody);
													}
													$scope.currentMethod._request.headers = result;
												});
							};

						} ]);

app.controller('showOrgMembersCtrl', [
		'$scope',
		'$rootScope',
		'Org',
		'Catalog',
		'$routeParams',
		function($scope, $rootScope, Org, Catalog, $routeParams) {
			Catalog.getOrgById({
				id : $routeParams.id
			}, function(data) {
				$scope.org = data.data;
				$rootScope.locTitles = [ 'profile', 'organizations',
						$scope.org.name, 'members' ];
			});

			Org.getMembers({
				id : $routeParams.id
			}, function(data) {
				$scope.members = data.data;
			});

			$scope.invite = function() {
				$scope.friend.org_id = $routeParams.id;
				Org.addOwner($scope.friend);
			};

			$scope.remove = function(i) {
				Org.deleteOwner({
					user_id : $scope.members[i].id,
					org_id : $routeParams.id
				}, function() {
					$scope.members.splice(i, 1);
				});
			};
		} ]);

app.controller('editCategoryCtrl', [ '$scope', '$routeParams', '$location',
		'Category', function($scope, $routeParams, $location, Category) {

			Category.getById({
				id : $routeParams.id
			}, function(data) {
				$scope.category = data.data;
			});

			$scope.submit = function() {

				Category.update($scope.category, function() {
					$location.path('profile');
				}, function(res) {
					$scope.errorMsg = res.data.error;
				});
			};
		} ]);

app.controller('newCategoryCtrl', [ '$scope', '$location', 'Category',
		function($scope, $location, Category) {
			$scope.submit = function() {
				Category.create($scope.category, function() {
					$location.path('profile');
				}, function(res) {
					$scope.errorMsg = res.data.error;
				});
			};
		} ]);
