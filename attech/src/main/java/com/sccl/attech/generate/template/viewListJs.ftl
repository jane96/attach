app.controller('${className}ListController', ["$http", "$scope", "$filter","$state", "ngTableParams", "$aside", "SweetAlert", "toaster","cfpLoadingBar","paramService",
    function($http, $scope, $filter,$state, ngTableParams, $aside, SweetAlert, toaster,cfpLoadingBar,paramService) {
	$scope.params = {};
	$scope.data=[];
    //加载数据
    $scope.tableParams = new ngTableParams({page: 1, count: 10, sorting: {updateDate: 'desc'}},
    {getData: function ($defer, params){
         params.url = "./a/${urlPrefix}/list";
         paramService.ladTableData($scope, $defer, params);
     }});
        //选择数据
        $scope.selected = function(p) {
            angular.forEach($scope.data, function(item) {
                if (p == item) {
                    p.$selected = !p.$selected;
					$scope.selv = p;
                } else {
                    item.$selected = false;
                } 
            });
        };
        
        //暂时未增加
         $scope.export=function(){
        	 
         }

        $scope.edit = function(p, selCount) {
            if (selCount == 0 || selCount > 1) {
                SweetAlert.swal({
                    title: "提示",
                    text: "请选择一条数据编辑!",
                    type: "warning",
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "确定"
                });
            } else
            	$state.go('app.menu${moduleName}_${className}_edit',{${className}Id:p.id});
        };

        $scope.delete = function(p, selCount) {
        	if (selCount == 0 || selCount > 1) {
                SweetAlert.swal({
                    title: "提示",
                    text: "请选择一条数据编辑!",
                    type: "warning",
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "确定"
                });
            } else {
				delit(p);                    
            }
        };
        function delit(p, $modalInstance) {
            SweetAlert.swal({
                title: "您确认删除吗？",
                text: "删除的数据将不能恢复!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function(isConfirm) {
                if (isConfirm) {
                    var url = "./a/${urlPrefix}/delete?id=" + p.id;
                    $http.post(url).success(function(response) {
                         if (response.success) {
                            var index = $scope.data.indexOf(p);
                            $scope.data.splice(index, 1);
                            if ($modalInstance != null) $modalInstance.close();
                            $scope.tableParams.reload();
                        }
                        toaster.pop(1, "温馨提示", response.message);
                    });
                } else {
                    //SweetAlert.swal("Cancelled", "data is safe :)", "error");
                }
            });
        }
}]);