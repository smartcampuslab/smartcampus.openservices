'use strict';
var services = angular.module('openservices.services', [ 'ngResource',
		'ngCookies' ]);

services
		.factory(
				'Auth',
				[
						'$http',
						'$cookieStore',
						'$rootScope',
						'$window',
						'$location',
						function($http, $cookieStore, $rootScope, $window, $location) {
							var accessLevels = routingConfig.accessLevels, userRoles = routingConfig.userRoles;

							// check if user is a string then json
							if (typeof $cookieStore.get('user') == 'string'
									&& $cookieStore.get('user') !== "") {
								var jsonObj = JSON.parse($cookieStore.get('user'));
								$rootScope.currentUser = {
									username : jsonObj.username,
									role : userRoles[jsonObj.role]
								};
								changeUser($rootScope.currentUser);
								$cookieStore.remove('user');
								$cookieStore.put('user', $rootScope.currentUser);

							} else {
								$rootScope.currentUser = $cookieStore.get('user') || {
									username : '',
									role : userRoles.public
								};

							}

							// $cookieStore.remove('user');

							function changeUser(user) {
								_.extend($rootScope.currentUser, user);
							}

							return {
								authorize : function(accessLevel, role) {
									if (role === undefined) {
										role = $rootScope.currentUser.role;
									}
									return accessLevel.bitMask & role.bitMask;
								},
								isLoggedIn : function(user) {
									if (user === undefined) {
										user = $rootScope.currentUser;
									}
									return user
											&& user.role
											&& (user.role.title === userRoles.ROLE_USER.title || user.role.title === userRoles.ROLE_NORMAL.title || user.role.title === userRoles.ROLE_ADMIN.title);
								},
								register : function(user, success, error) {
									$http.post('/register', user).success(function(res) {
										changeUser(res);
										success();
									}).error(error);
								},
								login : function(user, success, error) {
									$http.post('perform_login', $.param(user), {
										headers : {
											'Content-Type' : 'application/x-www-form-urlencoded'
										}
									}).success(function(data) {
										var role = data.data.role;
										var templ = {
											username : data.data.username,
											role : userRoles[role]
										};

										changeUser(templ);
										$cookieStore.remove('user');
										$cookieStore.put('user', $rootScope.currentUser);
										success(user);
									}).error(function(data) {
										error(data.error);
									});
								},
								getLoginURL : function() {
									$http.get('signin').success(function(data) {
										console.log('data : ' + data);
										if(data.length > 0) {
											$window.location.href = data;
										} else {
											$location.path('signin');
										}
									});
								},
								apiLogin : function(user, success, error) {
									$http.post('apimanager/perform_login', $.param(user), {
										headers : {
											'Content-Type' : 'application/x-www-form-urlencoded'
										}
									}).success(function(data) {
										console.log('Service apiLogin');
										console.log(data);
										$window.location.href = data.data;

									}).error(function(data) {
										error(data.error);
									});
								},
								fbLogin : function(error) {
									$http.get('api/social/fb').success(function(data) {
										console.log('Fb success, data ' + data.data);
										$window.location.href = data.data;

									}).error(function(data) {
										console.log('Fb error, ' + data.error);
										error(data.error);
									});
								},
								googleLogin : function(error) {
									$http.get('api/oauth/google/auth').success(function(data) {
										console.log('Google success, data ' + data.data);
										$window.location.href = data.data;

									}).error(function(data) {
										console.log('Google error');
										error(data.error);
									});
								},
								linkedinLogin : function(error) {
									$http.get('api/oauth/linkedin/auth').success(function(data) {
										console.log('Linkedin success, data ' + data.data);
										$window.location.href = data.data;

									}).error(function(data) {
										error(data.error);
									});
								},
								logout : function(success, error) {
									$http.get('logout').success(function() {

										changeUser({
											username : '',
											role : userRoles.public
										});
										$cookieStore.remove('user');
										success();
									}).error(error);
								},
								accessLevels : accessLevels,
								userRoles : userRoles,
								user : $rootScope.currentUser
							};
						} ]);

services.factory('User', [ '$resource', function($resource) {
	return $resource('api/user/:id', {}, {
		update : {
			method : 'POST',
			url : 'api/user/modify'
		},
		getInfo : {
			method : 'GET',
			url : 'api/user/my'
		},
		enable : {
			method : 'GET',
			url : 'api/user/add/enable/:key'
		},
		save : {
			method : 'POST',
			url : 'api/user/add'
		},
		updatePassw : {
			method : 'POST',
			url : 'api/user/passw/modify'
		},
		userData : {
			method : 'GET',
			url : 'api/user/:id'
		}
	});
} ]);

services.factory('Category', [ '$resource', function($resource) {
	return $resource('api/category', {}, {
		getById : {
			method : 'GET',
			url : 'api/category/:id',
		},
		list : {
			method : 'GET',
			url : 'api/category/',
		},
		create : {
			method : 'POST',
			url : 'api/category/add',
		},
		update : {
			method : 'PUT',
			url : 'api/category/modify',
		},
		remove : {
			method : 'DELETE',
			url : 'api/category/delete/:id',
		}

	});
} ]);

services.factory('Catalog', [ '$resource', function($resource) {
	return $resource('/api/catalog', {}, {
		countServices : {
			method : 'GET',
			url : 'api/catalog/servicecount',
		},
		listServices : {
			method : 'GET',
			url : 'api/catalog/service',
		},
		getServiceById : {
			method : 'GET',
			url : 'api/catalog/service/:id',
		},
		getServiceMethods : {
			method : 'GET',
			url : 'api/catalog/service/:id/methods',
		},
		getListServiceHistory : {
			method : 'GET',
			url : 'api/catalog/service/:id/history',
		},
		browseServiceCat : {
			method : 'GET',
			url : 'api/catalog/service/category/:category',
		},
		browseServiceCats : {
			method : 'GET',
			url : 'api/catalog/service/category',
		},
		browseServiceOrg : {
			method : 'GET',
			url : 'api/catalog/service/org/:org',
		},
		browseAllServiceCat : {
			method : 'GET',
			url : 'api/catalog/category/services',
		},
		listOrgs : {
			method : 'GET',
			url : 'api/catalog/org'
		},
		getOrgById : {
			method : 'GET',
			url : 'api/catalog/org/:id'
		},
		browseOrgCat : {
			method : 'GET',
			url : 'api/catalog/org/category/:category'
		},
		browseOrgCats : {
			method : 'GET',
			url : 'api/catalog/org/category'
		}
	});
} ]);

services.factory('Service', [ '$resource', function($resource) {
	return $resource('api/service/my/:id', {}, {
		get : {
			method : 'GET',
			url : 'api/service/my'
		},
		list : {
			method : 'GET',
			url : 'api/service/view',
		},
		getDescription : {
			method : 'GET',
			url : 'api/service/view/description/:id'
		},
		getMethods : {
			method : 'GET',
			url : 'api/service/view/method/:id',
		},
		getMethod : {
			method : 'GET',
			url : 'api/service/method/:id',
		},
		getHistory : {
			method : 'GET',
			url : 'api/service/view/history/:id',
		},
		create : {
			method : 'POST',
			url : 'api/service/add'
		},
		update : {
			method : 'PUT',
			url : 'api/service/modify',
			params : {}
		},
		publish : {
			method : 'PUT',
			url : 'api/service/publish/:id',
		},
		unpublish : {
			method : 'PUT',
			url : 'api/service/unpublish/:id',
		},
		deprecate : {
			method : 'PUT',
			url : 'api/service/deprecate/:id',
		},
		welive : {
			method : 'PUT',
			url : 'api/service/welive/publish/:id',
		},
		remove : {
			method : 'DELETE',
			url : 'api/service/delete/:id',
		},
		createMethod : {
			method : 'POST',
			url : 'api/service/method/add',
		},
		updateMethod : {
			method : 'PUT',
			url : 'api/service/method/modify',
		},
		deleteMethod : {
			method : 'DELETE',
			url : 'api/service/method/delete/:id',
		},
		createTest : {
			method : 'POST',
			url : 'api/service/method/:id/test/add',
		},
		updateTest : {
			method : 'PUT',
			url : 'api/service/method/:id/test/:pos',
		},
		deleteTest : {
			method : 'DELETE',
			url : 'api/service/method/:id/test/:pos',
		}

	});
} ]);

services.factory('Org', [ '$resource', function($resource) {
	return $resource('api/org/:name', {}, {
		get : {
			method : 'GET',
			url : 'api/org/my',
		},
		getById : {
			method : 'GET',
			url : 'api/org/:id',
		},
		list : {
			method : 'GET',
			url : 'api/org/list',
			isArray : true
		},
		getOrgHistory : {
			method : 'GET',
			url : 'api/org/activity/history/:id'
		},
		create : {
			method : 'POST',
			url : 'api/org/add'
		},
		remove : {
			method : 'DELETE',
			url : 'api/org/delete/:id'
		},
		update : {
			method : 'PUT',
			url : 'api/org/modify'
		},
		addOwner : {
			method : 'POST',
			url : 'api/org/manage/owner'
		},
		deleteOwner : {
			method : 'POST',
			url : 'api/org/manage/owner/delete'
		},
		getFile : {
			method : 'GET',
			url : 'api/file/download/:id'
		},
		getMembers : {
			method : 'GET',
			url : 'api/org/members/:id'
		},
		enable : {
			method : 'GET',
			url : 'api/org/manage/owner/add/:key'
		}
	});
} ]);

services.factory('Bread', [ '$location', function($location) {
	var loc = $location.path().split('/');
	loc.splice(0, 1);
	return loc;
} ]);

services.service('Constants', function() {
	this.licenseTypes = function() {
		return [ {
			'label' : 'None',
			'value' : 'none'
		}, {
			'label' : 'Standard license',
			'value' : 'std'
		}, {
			'label' : 'External URL',
			'value' : 'ext'
		}, {
			'label' : 'Custom license',
			'value' : 'custom'
		} ];
	};

	this.licenses = function() {
		return [ {
			'label' : 'CC-BY',
			'icon' : 'http://i.creativecommons.org/l/by/3.0/88x31.png',
			'link' : 'https://creativecommons.org/licenses/by/4.0/legalcode'
		}, {
			'label' : 'CC-BY-ND',
			'icon' : 'http://i.creativecommons.org/l/by-nd/3.0/88x31.png',
			'link' : 'https://creativecommons.org/licenses/by-nd/4.0/legalcode'
		}, {
			'label' : 'CC-BY-ND-SA',
			'icon' : 'http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png',
			'link' : 'https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode'
		}, {
			'label' : 'CC-BY-SA',
			'icon' : 'http://i.creativecommons.org/l/by-sa/3.0/88x31.png',
			'link' : 'https://creativecommons.org/licenses/by-sa/4.0/legalcode'
		}, {
			'label' : 'CC-BY-NC',
			'icon' : 'http://i.creativecommons.org/l/by-nc/3.0/88x31.png',
			'link' : 'https://creativecommons.org/licenses/by-nc/4.0/legalcode'
		}, {
			'label' : 'CC-BY-NC-ND',
			'icon' : 'http://i.creativecommons.org/l/by-nc-nd/3.0/88x31.png',
			'link' : 'https://creativecommons.org/licenses/by-nc-nd/4.0/legalcode'
		} ];
	};
});
