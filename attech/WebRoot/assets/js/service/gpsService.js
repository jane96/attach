app.service('gpsService',["$http","SweetAlert",function uploadService($http,SweetAlert) {
	return {
		getPostion:function(callback,isAddress){
			if (window.navigator.geolocation){
		        var options = {
		        		enableHighAccuracy:false,//精准定位
		        		timeout:30000, //超时时间,毫秒 
		        		maximumAge:0
		        		}
		      navigator.geolocation.getCurrentPosition(function(position){
		    	var lng = position.coords.longitude; 
		        var lat = position.coords.latitude;
		        if(undefined!=isAddress&&isAddress)
					$http.post("./a/monitoringcenter/locationEnclosure/getAddress?xalis="+lat+"&yalis="+lng).success(function(response) {//根据
						position.address=response;
						callback.call(this,position);
				    });
		        else
		        	callback.call(this,position);
		      }, function(error){
		    	  var message="";
		    	 switch(error.code){
		    	    case error.PERMISSION_DENIED:
		    	    	message="用户不允许地理定位."
		    	      break;
		    	    case error.POSITION_UNAVAILABLE:
		    	    	message="无法获取当前位置."
		    	      break;
		    	    case error.TIMEOUT:
		    	    	message="操作超时."
		    	      break;
		    	    case error.UNKNOWN_ERROR:
		    	    	message="未知错误."
		    	      break;
		    	   }
			 	SweetAlert.swal({title: "定位提示", text: "定位失败,"+message,type: "error",confirmButtonColor: "#DD6B55",confirmButtonText: "确定"});
		      }, options); 
		    }else
			 	SweetAlert.swal({title: "定位提示", text: "浏览器不支持定位!",type: "error",confirmButtonColor: "#DD6B55",confirmButtonText: "确定"});
		}
	
	}
}]);