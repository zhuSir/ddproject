<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<%@ include file="/WEB-INF/views/wxweb/head-meta.jsp"%>
<title>扫码错误页面</title>
<style>
body {
	background: #F1F1F1;
}

img {
	margin-top: 27%;
	margin-left: 36%;
	width: 27%;
	height: 27%;
}

p {
	font-size: 16px;
	color: #000000;
	text-align: center;
}

.p1 {
	margin-top: 15px;
}

.p2 {
	margin-top: -17px;
}
</style>
</head>
<body>
	<img src="${ctx}/resources/wxweb/img/icon-low.png" />
	<p class="p1">微信版本太低，无法报修,</p>
	<p class="p2">请更新后使用</p>
</body>
</html>