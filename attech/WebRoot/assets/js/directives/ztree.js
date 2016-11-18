app.directive('ztree', function() {
	return {
	    restrict: 'AE',
	    require: '?ngModel',
		link : function($scope, element, attrs, warpCtl) {
			var setting = {
      				check:{enable:true,nocheckInherit:true},
      				view:{selectedMulti:false},
      				data:{simpleData:{enable:true}},
      				callback:{
      					beforeClick:function(id, node){
      						tree.checkNode(node, !node.checked, true, true);
      						return false;
      					},
      					onCheck: function(event, treeId, treeNode) {
      						var ids = [], nodes =tree.getCheckedNodes(true);
      						for(var i=0; i<nodes.length; i++) {
      							ids.push(nodes[i].id);
      						}
      						$scope.$apply(function () {
      							warpCtl.$setViewValue(ids);
      						});
      					}
      				}
      		};
			setting=angular.extend(setting, $scope[attrs.setting]);
			var tree=$.fn.zTree.init(element, setting, $scope[attrs.zNodes]);
			tree.expandNode(tree.getNodes()[0], true, false, true);
			$scope.$watch(warpCtl,function(){
					var selectVals=warpCtl.$viewValue;
					if(null==selectVals||undefined==selectVals)return;
						angular.forEach(selectVals.split(","), function(value, key) {
							var node =tree.getNodeByParam("id", value);
							try{tree.checkNode(node, true, false);}catch(e){}
						});
				}
	        )
			
		}
	};
});