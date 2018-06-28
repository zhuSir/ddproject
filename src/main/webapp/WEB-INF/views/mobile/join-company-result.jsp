<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
	    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	    <title></title>
	    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
		<c:set var="ctx" value="<%=request.getContextPath()%>"/>
	    <link rel="stylesheet" type="text/css" href="${ctx}/css/in2.css"/>
	    <script src="${ctx}/js/jquery-1.7.1.min.js"></script>
	    <script src="${ctx}/js/in2.js"></script>
	</head>
	<body>
		<div class="mui-content">
			<div class="party">
				<p class="px">恭喜你！</p>
				<p class="pcx">已经成功加入<span class="te">${companyName}</span>的团队的维保项目管理平台。</p>
				<p class="pp">立即下载"咚咚维保云"APP，马上开始报修、维保等工作!</p>
				<img src="${ctx}/images/dd.png" class="dd"/>
			</div>
			<button><a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.xmsmartcity.dweibao">立即下载</a></button>
			<img src="${ctx}/images/invite.png" alt="" class="wema" />
		</div>
	</body>
</html>


