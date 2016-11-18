app.service('uploadService', ['$timeout','Upload',"cfpLoadingBar",function uploadService($timeout,Upload,cfpLoadingBar) {
	return {
			/**
			 * file 当前上传的文件对象 File类型
			 * fileEle 文件上传框
			 * callback 执行完上传的回调函数
			 * */
			uploadFile:function($scope,fileEle,callBack,referenceId,tableName,businessType,pathName,num) {//上传文件
				if(undefined==fileEle||null==fileEle)return;
				var file=fileEle.files[0];
				cfpLoadingBar.start();
		 		file.upload = Upload.upload({ url: 'upload.htm/'+referenceId+"/"+tableName+"/"+encodeURI(businessType)+"/"+encodeURI(pathName)+"/"+encodeURI(num), method: 'POST',  file: file,fileFormDataName: 'file'});
			file.upload.then(function(response) {
		 			$timeout(function() {
		 				$(fileEle).val(""); 
		 				file.result = response.data;
		 				 $scope.$apply(function() {
		 					 $scope.uploadFile++;
		 					 callBack.call(this,file,fileEle,$scope);
		 					 cfpLoadingBar.complete();
		 				  });
		 			});
		 		}, function(response) {
		 			cfpLoadingBar.complete();
		 			if (response.status > 0)
		 				$scope.errorMsg = response.status + ': ' + response.data;
		 	});
	 	},
	 	/**
	 	 * 上传多个文件
		 * selector 文件框选择器
		 * callback 单个文件上传的回调函数
		 * */
	 	saveAllFile:function($scope,selector,callBack){
	 		var deferred=$.Deferred();
	 		uploadServer=this;
	 		var files=[];
			angular.forEach(angular.element(selector),function(fileElemt, key) {
				if(angular.element(fileElemt).val()!=''){
					this.push(fileElemt);
				}
			},files);
			if(files.length<1)
				deferred.resolve();
			$scope.uploadFile=0;
			angular.forEach(files,function(fileInput, key) {
				uploadServer.uploadFile($scope,fileInput,callBack);
			});
			$scope.$watch('uploadFile', function(newValue, oldValue) {
				if(newValue==files.length&&newValue>0)
					deferred.resolve();
			});
			return deferred.promise();
	 	}
	}
}]);
