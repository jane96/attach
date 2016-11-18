app.controller('${className}FormCtrl', ["$scope","SweetAlert","paramService","$state","toaster","$http",
    function($scope, SweetAlert,paramService,$state,toaster,$http) {
	$scope.p={};
	if(null!=$state.params.${className}Id){
		  $http.post("./a/${urlPrefix}/form?id="+$state.params.${className}Id).success(function(response) {
			  $scope.p=response;
		  })
	}
	$scope.submit = function(){//表单提交
		var invalidResult = formInvalid($scope.form1,toaster);//表单验证	
		if(!invalidResult){
			return;
		}
		$http.post("./a/${urlPrefix}/save?random="+Math.ceil(Math.random()*100)+"&"+paramService.param($scope.p)).success(function(response){
			  if(response.success)
				  $state.go('app.menu${moduleName}_${className}_view');//跳转到列表
			  else
				  SweetAlert.swal("保存失败", response.message, "error");
		  })
    }
	
}]);