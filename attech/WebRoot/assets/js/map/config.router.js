'use strict';

/**
 * Config for the router
 */
app.config(['$stateProvider', '$urlRouterProvider', '$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$ocLazyLoadProvider', 'JS_REQUIRES',
function ($stateProvider, $urlRouterProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $ocLazyLoadProvider, jsRequires) {

    app.controller = $controllerProvider.register;
    app.directive = $compileProvider.directive;
    app.filter = $filterProvider.register;
    app.factory = $provide.factory;
    app.service = $provide.service;
    app.constant = $provide.constant;
    app.value = $provide.value;

    // LAZY MODULES

    $ocLazyLoadProvider.config({
        debug: false,
        events: true,
        modules: jsRequires.modules
    });

    // APPLICATION ROUTES
    // -----------------------------------
    // For any unmatched url, redirect to /app/dashboard
    $urlRouterProvider.otherwise("/app/layer");
    //

    // Set up the states
    $stateProvider.state('app', {
        url: "/app",
        templateUrl: "pages/mapApp.html",
        resolve: loadSequence('modernizr', 'moment', 'angularMoment', 'uiSwitch', 'perfect-scrollbar-plugin', 'toaster', 'ngAside', 'vAccordion', 'sweet-alert', 'chartjs', 'tc.chartjs', 'oitozero.ngSweetAlert', 'chatCtrl','noticeNavBarCtrl'),
        abstract: true
    }).state('error', {
        url: '/error',
        template: '<div ui-view class="fade-in-up"></div>'
    }).state('error.404', {
        url: '/404',
        templateUrl: "pages/utility_404.html",
    }).state('error.500', {
        url: '/500',
        templateUrl: "pages/utility_500.html",
    }).state('app.message', {
    	url: '/message',
        templateUrl: "pages/message/sms_send.html",
        title: '发送短心',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('ngTable','validationCtrl','angularBootstrapNavTree', 'treeCtrl')
    }).state('app.personInformation', {
    	url: '/personInformation',
        templateUrl: "pages/monitoringcenter/person.html",
        title: '个人基本信息',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('flow', 'ui.select', 'monospaced.elastic', 'ui.mask', 'touchspin-plugin', 'selectCtrl','userCtrl','flow','selectCtrl')
    }).state('app.showPictrue', {
    	url: '/showPictrue',
        templateUrl: "pages/monitoringcenter/showPicture.html",
        title: '图片显示',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('flow', 'ui.select', 'monospaced.elastic', 'ui.mask', 'touchspin-plugin', 'selectCtrl','userCtrl','flow','selectCtrl')
    }).state('app.location', {
    	url: '/location',
        templateUrl: "pages/monitoringcenter/locationRecorder.html",
        title: '历史定位信息',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('ngTable','validationCtrl','angularBootstrapNavTree', 'treeCtrl')
    }).state('app.history', {
    	url: '/history',
        templateUrl: "pages/monitoringcenter/mapHistory.html",
        title: '历史定位信息',
        icon: 'ti-layout-media-left-alt',
		resolve: loadSequence('mapHistoryCtrl','ngTable','validationCtrl','angularBootstrapNavTree', 'treeCtrl')
    });


    // Generates a resolve object previously configured in constant.JS_REQUIRES (config.constant.js)
    function loadSequence() {
        var _args = arguments;
        return {
            deps: ['$ocLazyLoad', '$q',
			function ($ocLL, $q) {
			    var promise = $q.when(1);
			    for (var i = 0, len = _args.length; i < len; i++) {
			        promise = promiseThen(_args[i]);
			    }
			    return promise;

			    function promiseThen(_arg) {
			        if (typeof _arg == 'function')
			            return promise.then(_arg);
			        else
			            return promise.then(function () {
			                var nowLoad = requiredData(_arg);
			                if (!nowLoad)
			                    return $.error('Route resolve: Bad resource name [' + _arg + ']');
			                return $ocLL.load(nowLoad);
			            });
			    }

			    function requiredData(name) {
			        if (jsRequires.modules)
			            for (var m in jsRequires.modules)
			                if (jsRequires.modules[m].name && jsRequires.modules[m].name === name)
			                    return jsRequires.modules[m];
			        return jsRequires.scripts && jsRequires.scripts[name];
			    }
			}]
        };
    }
}]);