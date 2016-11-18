app.service('paramService',["$filter","$http","cfpLoadingBar",function uploadService($filter,$http,cfpLoadingBar) {
	orderAndPage=function(params,tableParams){
    	if((tableParams.orderBy()+'') != '')
			orderBy = tableParams.orderBy()+'';
		if(orderBy.substring(0,1) == '+')
			orderBy = 'asc-'+orderBy.substring(1,orderBy.length);
		else if(orderBy.substring(0,1) == '-')
			orderBy = 'desc-'+orderBy.substring(1,orderBy.length);
		params.pageNo=tableParams.page()+1;
		params.pageSize=tableParams.count();
		params.orderBy=orderBy;
    }
	return {
		param :function(obj) {
            var query = '';
            var paramServer=this;
            var name, value, fullSubName, subName, subValue, innerObj, i;
            for (name in obj) {
                value = obj[name];
                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += paramServer.param(innerObj) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '.' + subName;
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += paramServer.param(innerObj) + '&';
                    }
                }else if(angular.isDate(value)){
                	query +=name+"="+$filter('date')(value,'yyyy-MM-dd HH:mm:ss')+ '&'
                } else if (value !== undefined && value !== null) {
                    query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
                }
            }
 
            return query.length ? query.substr(0, query.length - 1) : query;
        },
        ladTableData:function($scope,$defer,tableParams){
        	orderAndPage($scope.params,tableParams);
        	var url=tableParams.url;
        	cfpLoadingBar.start();
        	url+="?random="+Math.ceil(Math.random()*100)+
			"&"+this.param($scope.params)+(undefined!=tableParams.appendUrl?tableParams.appendUrl:"");
   			$http.get(url).success(function(response) {
   				cfpLoadingBar.complete();
   				if(response.totalElements<1) return;
    			$scope.data = response.list;
				var gridData = tableParams.sorting() ? $filter('orderBy')($scope.data, tableParams.orderBy()) : $scope.data;
				tableParams.total(response.totalElements);
           		$defer.resolve(gridData.slice(0,tableParams.count()));
    		});	
        }
	}
}]);