var app = angular.module('clattApp', ['clatt-ui']);
app.run(['$rootScope', '$state', '$stateParams',"$http",
function ($rootScope, $state, $stateParams,$http) {

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
    var timeRandom = Date.parse(new Date()); 
    var logoUser= $.ajax({url: "a/sys/user/findLoginUser?timeRandom="+timeRandom,async: false}).responseText;
    
    var userObject = eval("("+logoUser+")");
    
    if(userObject.photo==undefined){
    	userObject.photo = "";
    }
   // console.debug(userObject.photo);
    $rootScope.user = {
        name: userObject.name,
        job: 'ng-Dev',
        picture: '../../../templates/'+userObject.photo
    };
    var roleArray= $.ajax({url: "a/sys/role/getPermissions",async: false}).responseText;
    $rootScope.hasRole=function(role){
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
    cfpLoadingBarProvider.includeSpinner = true;

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
		try{
			angular.element('.ng-invalid[name=' + firstError + ']').focus();
		}catch (e) {}
    	$toaster.pop("error", "温馨提示", "存在不规范的输入内容，请修改");
   }
   return invalidResult;
}

//排序封装
function order(params){
	var orderBy = "";
	if((params.orderBy()+'') != ''){
		orderBy = params.orderBy()+'';
	}
	if(orderBy.substring(0,1) == '+'){
		orderBy = 'asc-'+orderBy.substring(1,orderBy.length);
	}else if(orderBy.substring(0,1) == '-'){
		orderBy = 'desc-'+orderBy.substring(1,orderBy.length);
	}
	return orderBy;
}

//ngTable封装,通过@resource获取
function initData1($defer, $filter,params, response){
	var totalRecords = 8;
	var filterData = params.filter() ? $filter('filter')(response, params.filter()) : response;
	gridData = params.sorting() ? $filter('orderBy')(filterData, params.orderBy()) : filterData;
	params.total(totalRecords);
	$defer.resolve(gridData.slice( 0 ,  params.count()));
	return response;
}


//ngTable封装,通过$http获取
function initData($defer, $filter,params, response){
	var totalRecords = response.count;//总记录条数
	var data = [];
	if(response.list!=undefined){
    	data = response.list;
    }
	var filterData = params.filter() ? $filter('filter')(data, params.filter()) : data;
	gridData = params.sorting() ? $filter('orderBy')(filterData, params.orderBy()) : filterData;
	params.total(totalRecords);
	$defer.resolve(gridData.slice(0,params.count()));
	return data;
}

var base = {page:1,pageSize:10};
//字符串替换函数
function replace(expression,find,replacewith)   
{   
    alert("222");
    var blnFlag;   
    if(expression=="")   
        blnFlag=false;   
    else   
    {   
        var loc=expression.indexOf(find);   
        var findLen=find.length;   
        var replaceWithLen=replacewith.length;   

        if(loc!=-1)   
            blnFlag = true;   
        else   
            blnFlag =false;   

        while(blnFlag)   
        {   
            expression = expression.substr(0,loc)+replacewith+expression.substr(loc+findLen);   
            loc = expression.indexOf(find,loc+replaceWithLen);
            if(loc == -1)  blnFlag = false;   
        }   
    }   
    return expression;   
} 

//get方法需要对中文转码
function URI(input){
	return encodeURIComponent(encodeURIComponent(input));
}