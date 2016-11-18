var app = angular.module('clipApp', ['clip-two']);
app.run(['$rootScope', '$state', '$stateParams',
function ($rootScope, $state, $stateParams) {

    // Attach Fastclick for eliminating the 300ms delay between a physical tap and the firing of a click event on mobile browsers
    FastClick.attach(document.body);

    // Set some reference to access them from any scope
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;

    // GLOBAL APP SCOPE
    // set below basic information
    $rootScope.app = {
        name: '川送信息管理系统', // name of your project
        author: '四川省送变电建设有限责任公司', // author's name or company name
        description: '川送信息管理系统管理平台', // brief description
        version: '1.0', // current version
        year: ((new Date()).getFullYear()), // automatic current year (for copyright information)
        isMobile: (function () {// true if the browser is a mobile device
            var check = false;
            if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
                check = true;
            };
            return check;
        })(),
        layout: {
            isNavbarFixed: true, //true if you want to initialize the template with fixed header
            isSidebarFixed: true, // true if you want to initialize the template with fixed sidebar
            isSidebarClosed: false, // true if you want to initialize the template with closed sidebar
            isFooterFixed: false, // true if you want to initialize the template with fixed footer
            theme: 'theme-1', // indicate the theme chosen for your project
            logo: 'assets/images/logo.png', // relative path of the project logo
        }
    };
    //获取登录用户
    var logoUser= $.ajax({url: "a/sys/user/findLoginUser",async: false}).responseText;
    var userObject = eval("("+logoUser+")")
    $rootScope.user = {
    		
        name: userObject.name,
        job: 'ng-Dev',
        picture: 'app/img/user/02.jpg'
    };
    $rootScope.hasRole=function(role){
    	var roleArray= $.ajax({url: "a/sys/role/getPermissions",async: false}).responseText;
    	var isRole=$.inArray(role, eval(roleArray));
    	return isRole>-1?true:false;
    };
}]);
//app.config(['uiMapLoadParamsProvider', function (uiMapLoadParamsProvider) {
//    uiMapLoadParamsProvider.setParams({
//        v: '2.0',
//        ak:'RTPRmbT7hbkFk9Rear6gsgWH'// your map's develop key
//    });
//}])
// translate config
app.config(['$translateProvider',
function ($translateProvider) {

    // prefix and suffix information  is required to specify a pattern
    // You can simply use the static-files loader with this pattern:
    $translateProvider.useStaticFilesLoader({
        prefix: 'assets/i18n/',
        suffix: '.json'
    });

    // Since you've now registered more then one translation table, angular-translate has to know which one to use.
    // This is where preferredLanguage(langKey) comes in.
    $translateProvider.preferredLanguage('chs');

    // Store the language in the local storage
    $translateProvider.useLocalStorage();

}]);
// Angular-Loading-Bar
// configuration
app.config(['cfpLoadingBarProvider',
function (cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeBar = true;
    cfpLoadingBarProvider.includeSpinner = false;

}]);
//通用表单验证
function formInvalid(form,$toaster){
	var firstError = null;
	var invalidResult = true;
    if (form.$invalid) {
           var field = null, firstError = null;
           for (field in form) {
                  if (field[0] != '$') {
                        if (firstError === null && !form[field].$valid) {
                            firstError = form[field].$name;
                        }

                        if (form[field].$pristine) {
                            form[field].$dirty = true;
                        }
                    }
      }
		invalidResult = false;
        angular.element('.ng-invalid[name=' + firstError + ']').focus();
    	$toaster.pop("error", "温馨提示", "存在不规范的输入内容，请修改");
   }
   return invalidResult;
}
