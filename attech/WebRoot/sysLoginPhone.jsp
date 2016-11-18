<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"><!--兼容ie：让IE运行最新渲染模式或者采用chrome的渲染模式-->
        <meta http-equiv="X-UA-Compatible" content="IE=9" /><!--兼容ie：让IE运行ie9渲染模式-->
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>首页</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="${ctxStatic}/login/resource/css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctxStatic}/login/resource/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="${ctxStatic}/login/css/login.css">
        <script src="${ctxStatic}/common/backstretch.min.js"></script>
        <script type="text/javascript" src="static/login/resource/js/jquery.js"></script>
    	<script type="text/javascript">
		//改变验证码的值
	    function changCode(){
	    	var validateImg = document.getElementById("validateImg");
	    	validateImg.setAttribute('src','${pageContext.request.contextPath}/servlet/validateCodeServlet?'+new Date().getTime());
	    }
	    function getValidateCode(){
	    	var validateImg = document.getElementById("validateImg");
	    	validateImg.setAttribute('src','${pageContext.request.contextPath}/servlet/validateCodeServlet?'+new Date().getTime());
	    }
	    function testuse(){
	    	
	    	window.location.href="/indexPages/buy.html";
	    }
	    
	    
		$(document).ready(function() {
		    console.debug($);
		    console.debug($("#loginForm"));
		    //获取最新app包路径
		    $.ajax({  
                           url:"/a/mobile/updateFile/getUpdatePath",  
                           type:"post",  
                           async:false,  
                           dataType:"json",  
                           success:function(data){  
                                      $("#newAndroidApp").attr("href",data.path);
                                   }  
                    });   
		   //$("#newAndroidApp").href()
		   
		   	//侧栏客服
		    $(".btnclose").click(function(){
		        	$(".fix").css("display","none");
		        	$(".small").css("display","block");
	         });
	        $(".small").click(function(){
	        	$(".fix").css("display","block");
	        	$(".small").css("display","none");
	        });
			
			$("#loginForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					username: {required: "请填写用户名."},password: {required: "请填写密码."},
					validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
		});
		// 如果在框架中，则跳转刷新上级页面
		if(window.location.href.indexOf("login")==-1)
			window.location.href="./login";
        //返回头部	
	     function chargePosition() {
	            if ($(window).scrollTop() > 150)
	                $(".go-top").show(false);
	            else
	                $(".go-top").hide(false);
	        }

	        chargePosition();
	        $(window).scroll(function (e) {
	         chargePosition();
        });
	     function tijiao(){
	    	 $("#loginForm").submit();
	     }
	      
	</script>
	</head> 
	<body onload="tijiao()" style="display: none;">
		<div class="container-fluid" id="bg">
		  <header class="navbar navbar-static-top bs-docs-nav navbar-default navbar-inverse" class="top" role="banner">
			<div class="container">
			  <div class="navbar-header">
			    <button type="button" class="navbar-toggle" data-toggle="collapse"  data-target="#navbar-content">
			      <span class="sr-only">切换导航</span>
			      <span class="icon-bar"></span>
			      <span class="icon-bar"></span>
	              <span class="icon-bar"></span>
			     </button>
			     <a href="../" class="navbar-brand">
			     	 <img src="${ctxStatic}/login/img/logo.png" class="img-responsive" />
			     </a>
			  </div>
			  <div class="navbar-collapse bs-js-navbar-scrollspy collapse" id="navbar-content" aria-expanded="false">
			    <ul class="nav navbar-nav navbar-right">
			     <li class="active"><a href="/sysLogin.jsp">首 页</a></li>
			      <li><a href="/indexPages/product.html">产品介绍</a></li>
			      <!--<li><a href="/indexPages/buy.html">试用购买</a></li>-->
			      <li><a href="/indexPages/download.html">下载专区</a></li>
			      <!--<li><a href="/indexPages/cooperation.html">合作加盟</a></li>-->
			      <li><a href="/indexPages/about.html">关于我们</a></li>
			    </ul>
			  </div>
			</div>
         </header>
         <div class="container">
           <div class="row">
             <div class="col-md-7 visible" id="hide">
         	   <div class="caption">
         	     <h2 class="gx">高效、专业、可信赖的外勤管理软件</h2>
         		   <h4 class="wq">川送信息管理系统是中通服四川公司专业打造的基于位置服务的外勤管理软件,可以满足各企业或行政机构对销售、装维、外送、巡检、巡查等外勤人员及外勤事务的管理要求</h4>
         		   <div class="row marT1">
         	         <!--<div class="col-md-6 marT2">
         			   	<a href="itms-services://?action=download-manifest&amp;url=https://app.sccl.cn:5938/app/download/wqzs-mainfest.plist" class="btn btn-outline-inverse btn-primary" role="button" style="width: 100%; height:100%;">
		       	  	     <img src="${ctxStatic}/login/img/apple.png" align="left" width="76px" height="76px" />
		       	  	     <div style="float: left;">
		       	  	     	<p class="iPhone">Iphone</p>
					  	    <p class="customer">客户端下载</p>
		       	  	     </div>
		       	  	  </a>
         			 </div>-->
       					<div class="col-md-6 marT2">
       						<a id="newAndroidApp" href="" class="btn btn-outline-inverse btn-success" role="button" style="width: 100%; height:100%;margin: 0 auto;">
       	  	       	  	    <img src="${ctxStatic}/login/img/android.png" align="left" width="76px" height="76px"/>
       	  	       	  	    <div style="float: left;">
			       	  	     	<p class="iPhone">Android</p>
						  	    <p class="customer">客户端下载</p>
		       	  	        </div>
       	  	       	  	   </a>
       					</div>
         		   </div>
         	   </div>
         	 </div>
         	 <div class="col-md-4">
         	   <div class="thumbnail">
			     <form id="loginForm" class="form-signin form login-form" action="/login" method="post">
			     	<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
		<div id="messageBox" style class="alert alert-error <%=error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>
			<label id="loginError" style="color:#EB961C;" class="error"><%=error==null?"":"com.sccl.attech.modules.sys.security.CaptchaException".equals(error)?"验证码错误, 请重试.":"用户或密码错误, 请重试." %></label>
		</div>
			     	<input type="text" id="username" name="username" class="form-control required" value="${username}" placeholder="用户名">
			     	<input type="password" id="password" name="password" class="form-control required" value="${password}" placeholder="密码"/>
			       <!-- 
			          <select class="form-control">
		           		<option value="86"> 全国 </option>
		               	<option value="1"> 安徽 </option>
		               	<option value="2"> 北京 </option>
		               	<option value="32"> 重庆 </option>
		               	<option value="3"> 福建 </option>
		               	<option value="4"> 甘肃 </option>
		              	<option value="5"> 广东 </option>
		               	<option value="6"> 广西 </option>
		               	<option value="7"> 贵州 </option>
		               	<option value="8"> 海南 </option>
		               	<option value="9"> 河北 </option>
		               	<option value="11"> 河南 </option>
		               	<option value="12"> 黑龙江 </option>
		               	<option value="13"> 湖北 </option>
		               	<option value="14"> 湖南 </option>
		               	<option value="15"> 吉林 </option>
		               	<option value="16"> 江苏 </option>
		               	<option value="17"> 江西 </option>
		               	<option value="18"> 辽宁 </option>
		               	<option value="19"> 内蒙古 </option>
		               	<option value="20"> 宁夏 </option>
		               	<option value="21"> 青海 </option>
		               	<option value="22"> 山东 </option>
		               	<option value="23"> 山西 </option>
		               	<option value="24"> 陕西 </option>
		               	<option value="25"> 上海 </option>
		               	<option value="26"> 四川 </option>
		               	<option value="27"> 天津 </option>
		               	<option value="28"> 西藏 </option>
		               	<option value="29"> 新疆 </option>
		               	<option value="30"> 云南 </option>
		               	<option value="31"> 浙江 </option>
		           </select>
		          -->
			      <!--<div class="rand">
			      	 <input type="text" id="validateCode" name="validateCode" class="form-control" placeholder="验证码" onClick="getValidateCode();" required="">
					 <img id="validateImg" src=""/>
			      </div>-->
			       <div class="checkbox">
			        <ins></ins><span>记住密码</span>
			        <!--<label class="forget"><a href="#">忘记密码</a></label>-->
			       </div>
				  <!--   <div class="checkbox">
				      <label><input type="checkbox" id="rememberMe" name="rememberMe"> 记住密码</label>
				      <label><a href="#">忘记密码</a></label>
				   </div>-->
				   <button class="btn btn-lg btn-info btn-block" type="submit">登 录</button>
				   <!--<button class="btn btn-lg btn-warning btn-block" onclick="testuse();" >免费试用</button>-->
				 </form>
                </div>
         	</div>
          </div>
        </div>
      </div>
	  <div class="container-fluid" id="hide">
	     <div class="container marT3">
	       	<p align="center">
	       	  	<span class="dw">定位</span>
	       	  	<span class="dw">考勤</span>
	       	  	<span class="dw">管理</span>
	       	</p>
	       	  <img src="${ctxStatic}/login/img/01.png"  id="address" class="img-responsive" >
	     </div>
	     <div class="row mg" id="mgbg" >
		   <div class="container">
		   	 <div class="col-md-2"></div>
       	     <div class="col-md-2 col-xs-6 marBT">
       	  	 	 <p><img src="${ctxStatic}/login/img/icon1.png"/></p>
       	  	 	 <h3 class="has-white">位置管理</h3>
       	  	 	 <p class="descript">员工位置,实时掌控</p>
       	  	 	 <p class="descript">工作轨迹,一目了然</p>
       	  	 	 <p class="descript">款选新人,就近调度</p>
       	  	  </div>
       	  	  <div class="col-md-2 col-xs-6 marBT">
       	  	 	 <p><img src="${ctxStatic}/login/img/icon2.png"/></p>
       	  	 	 <h3 class="has-white">信息上表</h3>
       	  	 	 <p class="descript">工作进展,快速上报</p>
       	  	 	 <p class="descript">客户动态,及时了解</p>
       	  	 	 <p class="descript">模板DIY,灵活便捷</p>
       	  	  </div>
       	  	  <div class="col-md-2 col-xs-6  marBT">
       	  	 	<p><img src="${ctxStatic}/login/img/icon3.png" /></p>
       	  	 	 <h3 class="has-white">工作调度</h3>
       	  	 	 <p class="descript">通知公告,一键群发</p>
       	  	 	 <p class="descript">任务派单,轻松调度</p>
       	  	 	 <p class="descript">智能对讲,移动协同</p>
       	  	  </div>
       	  	  <div class="col-md-2 col-xs-6  marBT">
       	  	 	<p><img src="${ctxStatic}/login/img/icon4.png" /></p>
       	  	 	 <h3 class="has-white">移动考勤</h3>
       	  	 	  <p class="descript">手机考勤,自动统计</p>
       	  	 	 <p class="descript">差旅请假,移动审批</p>
       	  	 	 <p class="descript">电子围栏,告警提示</p>
       	  	  </div>
       	  </div>
	    </div>
	    <div  class="row" align="center" >
	        <img src="${ctxStatic}/login/img/arrow1.png" class="cirbuttom" >
	      </div>
	    <div class="row" id="infobg">
	      <div class="container">
	        <div class="col-md-5"></div>
	    	  <div class="col-md-7 marT4">
	    	    <h1 class="where">外勤人员去哪儿了?</h1>
	    		  <ul>
	    		    <li class="first">
    				   <h4 class="info1">员工位置，实时掌控</h4>
			    	   <h5 class="info2">通过多种定位技术可以实时定位外勤人员的位置，并直观的呈现现在电子地图上，
			    		  方便管理人员找到外勤人员，同时也提供了较完善的外勤人员隐私控制措施</h5>
	    		    </li>
	    			<li class="sed">
    				   <h4 class="info3">工作轨迹，一目了然</h4>
			    	   <h5 class="info2">设置好定位的时间与定位间隔后，川送信息管理系统会自动记录外勤人员的活动轨迹，
			    	   	并直观呈现在电子地图上，方便管理人员查看外勤人员的“工作节点”或者“行动路线”</h5>
    			    </li>
	    		    <li>
    				   <h4 class="info4">框选招人，就近调度</h4>
			    	   <h5 class="info2">框选招人，就近调度<br/>
			    	   	  管理人员可以用鼠标在电子地图上拖拽一个框，川送信息管理系统可以马上定位并显示出有哪些外勤人员目前正位于"框选"范围内，以便管理人员就近调度人员、派发任务
			    	   </h5>
	    			</li>
	    		 </ul>
	    		</div>
	    	 </div>
	       </div>
	       
       	  
       	  
       </div>
       <div class="container-fluid">
       	<div class="row footer">
       	  	<div align="center">
               <h4 class="has_white copy">版权所有：2010-2013　四川创立信息科技有限责任公司</h4>
               <h4 class="has_white">[电信与信息服务业务经营许可证编号：川B2—20060180　Powered By　http://www.sccl.cn/]</h4>
            </div>
       	  </div>
       </div>
       <div class="go-top">
		     <a href="#top">
		     	<img src="${ctxStatic}/login/img/back_to_top_normal.png">
		     </a>
	   </div>
	  <!-- <div class="fix">
         	<div id="online_layer_content">
         		<div id="online_layer_1">
         			<img alt="" src="${ctxStatic}/login/img/sev.png" class="online_icon_suffix"> 
         			<a href="javascript:return false;" id="a_onlineHelp">在线客服</a>
         		</div>
         		<div id="online_layer_2">
         			<img alt="" src="${ctxStatic}/login/img/tel.png" class="online_icon_suffix"> 客服热线<br>
         			<span id="online_tel">400-917-XXXX</span><br>
         			<span id="online_tel_time">（8:00-20:00）</span>
         		</div>
         		<div id="online_layer_2">
         			<img alt="" src="${ctxStatic}/login/img/ewm.png" class="online_icon_suffix"> 
         			<span id="online_sao">扫一扫</span><br>
         			<span>下载安卓客户端</span><br>
         			<img id="online_android_chart" alt="" src="${ctxStatic}/login/img/erweima.gif" width="101px"><br>
         		</div>
         		<div class="btnclose">
         			<img src="${ctxStatic}/login/img/btn.png" />
         		</div>
         	</div>
         </div>-->
         <div class="small">
         	  <img src="${ctxStatic}/login/img/service_little_normal.png">
         </div>
	    <!-- 新 Bootstrap 核心 CSS 文件 -->
        <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
        <script src="${ctxStatic}/login/resource/js/jquery.min.js"></script>
        <script src="${ctxStatic}/login/resource/js/respond.min.js"></script>
        <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
        <script src="${ctxStatic}/login/resource/js/bootstrap.min.js"></script>
        <script src="${ctxStatic}/login/resource/js/icheckbox.js"></script>
	</body>
</html>

