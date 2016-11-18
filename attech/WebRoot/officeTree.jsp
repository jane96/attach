<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>部门-用户树</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript"> 
		
		$(document).ready(function() {
			loadZtree();
		});
        function onCheck(e,treeId,treeNode){
            var treeObj=$.fn.zTree.getZTreeObj("officeTree"),
            nodes=treeObj.getCheckedNodes(true),
            v="";
            for(var i=0;i<nodes.length;i++){
            v+=nodes[i].name + ",";
            alert(nodes[i].id); //获取选中节点的值
            }
            }
		function loadZtree(){
			var setting = {
					check:{enable:true,nocheckInherit:true},
					view:{selectedMulti:false},
					data:{
						simpleData:{enable:true}
					},
					callback:{
						beforeClick:function(id, node){
							tree.checkNode(node, !node.checked, true, true);
							return false;
						}
					//onCheck:onCheck
					}
				};
			var url = "/a/sys/office/treeOffice";
			$.post(url,null,function(data){
				
				//alert(data);
				// 用户-机构
				var zNodes2=[
					<c:forEach items="${officeList}" var="office">
							{id:'${office.id}', pId:'${not empty office.parent?office.parent.id:0}', name:"${office.name}"},
	            	</c:forEach>
					<c:forEach items="${userList}" var="user">
							{id:'${user.id}', pId:'${not empty user.office?user.office.id:0}', name:"${user.name}",iconSkin:"diy03"},
	            	</c:forEach>]
				;
				var zNodes2=data;
				//alert(zNodes2);
				// 初始化树结构
				var tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
				// 不选择父节点
				tree2.setting.check.chkboxType = { "Y" : "s", "N" : "s" };
				// 默认选择节点
				/**
				var ids2 = "1,2".split(",");
				for(var i=0; i<ids2.length; i++) {
					var node = tree2.getNodeByParam("id", ids2[i]);
					try{tree2.checkNode(node, true, false);}catch(e){}
				}
				*/
				// 默认展开全部节点
				tree2.expandAll(true);
				// 刷新（显示/隐藏）机构
				refreshOfficeTree();
				
			});
		}
		function a(){
			var treeObj=$.fn.zTree.getZTreeObj("officeTree"),
            nodes=treeObj.getCheckedNodes(true),
            v="";
            for(var i=0;i<nodes.length;i++){
            v+=nodes[i].name + ",";
            alert(nodes[i].id); //获取选中节点的值
            }
		}
	</script>
</head>
<body >
	<div id="officeTree" class="ztree" style="margin-left:100px;margin-top:3px;float:left;"></div>
	<input type="text" name="officeIds"/>
	<input type="button" value="获取" onclick="a()"/>
</body>
</html>
