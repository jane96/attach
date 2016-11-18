'use strict';
/** 
  * 告警记录相关控制器
*/
app.controller('AlarmInboxCtrl', ["$http","$scope", "$state", function ($http,$scope, $state) {
    $scope.noAvatarImg = "assets/images/default-user.png";
	$scope.messages=[];
	//默认获取所有记录
	$http.post("a/message/alarmRecords/findAllAlarm",null).success(function(response) {
                $scope.messages = response;
           });
              
}]);
//告警记录详情controller
app.controller('ViewAlarmCrtl', ["$http",'$scope', '$stateParams','SweetAlert','toaster',
function ($http,$scope, $stateParams, SweetAlert,toaster) {
    function getById(arr, id) {
        for (var d = 0, len = arr.length; d < len; d += 1) {
            if (arr[d].id == id) {
                return arr[d];
            }
        }
    }
	$http.post("a/message/alarmRecords/findAllAlarm", null).success(function(response) {
        $scope.message = getById(response, $stateParams.inboxID);
        if($scope.message.state=="0"){
    		$http.get("a/message/alarmRecords/changeAlarmState?state=1&alarmId="+$stateParams.alarmId,null).success(function(response) {
           });
           for(var i=0;i<$scope.messages.length;i++){ //设置记录状态
           	 if($scope.messages[i].id==$stateParams.alarmId){
           	 	$scope.messages[i].state="1";
           	 }
           }
    	}
    });
    //删除
    $scope.deleteNotice = function(alarmId){    	
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
                    var url = "a/message/alarmReceiver/delete?id=" + alarmId;
                    $http.post(url).success(function(response) {
                        if (response.status == "1") {
                            toaster.pop(response.status, "温馨提示", response.message);
                            $scope.message = [];
							for(var i=0;i<$scope.messages.length;i++){ //设置通知状态
           	 					if($scope.messages[i].id==alarmId){
           	 						$scope.messages[i]={};
           	 					}
        					}
                        }
                        
                    });
                } else {
                    //SweetAlert.swal("Cancelled", "data is safe :)", "error");
                }
            });
    };
}]);