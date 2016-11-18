<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<html>
  <head>
    <title>page.html</title>
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
	
  </head>
  <style type="text/css"> 

</style> 
  
  <script type="text/javascript">
  function nextpage(cpage)
  {
  	document.getElementById("currentPages").value=cpage+1;
  	testform.submit();
  }
  function prepage(cpage)
  {
  	document.getElementById("currentPages").value=cpage-1;
  	testform.submit();
  }
  function directpage(cpage)
  {
  	testform.submit();
  }
  </script>
  <body>
    <table>
    	<tr>
    		<td align="right">
    			${form.page.currentPages} /${form.page.totlePages}&nbsp;页 &nbsp;
    			<s:if test="form.page.currentPages != 1">
    				<a href="#" onclick="prepage(${form.page.currentPages})">上一页</a>&nbsp;
    			</s:if>
    			<s:if test="form.page.currentPages != form.page.totlePages">
    			<a href="#" onclick="nextpage(${form.page.currentPages})">下一页</a>&nbsp;
    			</s:if>
    			<a href="#" onclick="directpage()">跳转至</a><input style="width:25px" name="form.page.currentPages" id="currentPages" value="${form.page.currentPages}"/>页
    		</td>
    		<input type="hidden" id="totleNumber" name="form.page.totleNumber" value="${form.page.totleNumber}"/>
    		<input type="hidden" id="totlePages" name="form.page.totlePages" value="${form.page.totlePages}"/>
    		<input type="hidden" id="currentNumber" name="form.page.currentNumber" value="${form.page.currentNumber}"/>
    		<input type="hidden" id="actionName" name="form.page.actionName" value="${form.page.actionName}"/> 
    	</tr>
    </table>
  </body>
</html>
