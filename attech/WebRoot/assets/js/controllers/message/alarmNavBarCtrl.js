'use strict';
/** 
  *告警未读记录的数目
*/
app.controller('AlarmNavBarCtrl', ["$http","$scope", "$state", function ($http,$scope, $state) {
	$scope.newAlarmSize1=0;
	$scope.messages=[];
	//获取最新未读前5条记录
	$http.post("a/message/alarmRecords/findSomeAlarm",null).success(function(response) {
                $scope.messages = response;
    });
 	$scope.checkNewAlarm = function() { 
	    var url = "a/message/alarmRecords/findIsNewAlarm";
	    $http.post(url,null).success(function(response) {
                $scope.newAlarmSize1 = response;
         });
	    try{
		    var ws=new WebSocket('ws://'+ window.location.host+"/ws?type=alarm");
		    ws.onopen = function () {
		    	console.log('Info: alarm connection opened.');
	       };
	        ws.onmessage = function (event) {
	        	if(undefined!=event.data&&null!=event.data)
	        		var alarmData = event.data;
        	    	var alarmJson = eval('('+ alarmData +')');
	        		if(alarmJson.data.state=="1"){
	        			$scope.$apply(function(){
	            			$scope.newAlarmSize1--;
	            			$http.post("a/message/alarmRecords/findSomeAlarm",null).success(function(response) {
	                            $scope.messages = response;
	                            console.log($scope.messages);
	            			});
	            			console.log($scope.messages);
	            		});
	        	    }else{
	        	    	$scope.$apply(function(){
	            			$scope.newAlarmSize1++;
	            			$http.post("a/message/alarmRecords/findSomeAlarm",null).success(function(response) {
	                            $scope.messages = response;
	                            console.log($scope.messages);
	            			});
	            			console.log($scope.messages);
	            		});
	        	    }
	        };
       }catch(e){}
	}
	
	$scope.checkNewAlarm();
}]);