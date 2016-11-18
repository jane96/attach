'use strict';
/** 
  *通知未读信件的数目
*/
app.controller('NoticeNavBarCtrl', ["$http","$scope", "$state", function ($http,$scope, $state) {
		$scope.newNoticeSize=0;
		$scope.messages=[];
		//获取最新未读前5条记录
		$http.post("a/message/noticeReceiver/findSomeNotice",null).success(function(response) {
	                $scope.messages = response;
	    });
		$scope.checkNewNotice = function() {
		    var url = "a/message/noticeReceiver/findIsNewNotice";
		    $http.post(url,null).success(function(response) {
	                $scope.newNoticeSize = response;
	         });
		    try{
			    var ws=new WebSocket('ws://'+ window.location.host+"/ws?type=notice");
			    ws.onopen = function () {
			    	 console.log('Info: notice connection opened.');
		        };
		        ws.onmessage = function (event) {
		        	if(undefined!=event.data&&null!=event.data){
		        	    var noticeData = event.data;
		        	    var noticeJson = eval('('+ noticeData +')');
		        	    if(noticeJson.data.state=="1"){
		        	    	$scope.$apply(function(){
		        				$scope.newNoticeSize--;
		        				$http.post("a/message/noticeReceiver/findSomeNotice",null).success(function(response){
		    	             		$scope.messages = response;
		    	             		console.log($scope.messages);
		        				});
		        				console.log($scope.messages);
		        		 	});
		        	    }else{
		        	    	$scope.$apply(function(){
		        				$scope.newNoticeSize++;
		        				$http.post("a/message/noticeReceiver/findSomeNotice",null).success(function(response){
		    	             		$scope.messages = response;
		    	             		console.log($scope.messages);
		        				});
		        				console.log($scope.messages);
		        			});
		        	    
		        	    }
		        		
		        	}
		        };
		        ws.onclose = function (event) {
		            console.log('Info: notice connection closed.');
		        };
		    }catch(e){}
		}

		$scope.checkNewNotice();
}]);