<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<title>创建项目</title>
	<%@ include file="/WEB-INF/views/wxweb/head-meta.jsp"%>
	<link rel="stylesheet" href="${ctx}/resources/wxweb/css/in2.css" />
</head>
	<body>
		<div class="main">
			<p class="ps">选择你的角色</p>
			<div class="btn">
				<button class="bt1">
					<a href="${ctx}/wx/project/project-create?type=0" >我是报修方(甲方)</a>
				</button>
				<button class="bt2">
					<a href="${ctx}/wx/project/project-create?type=1">我是维保方(乙方)</a>
				</button>
			</div>
		</div>
	</body>
</html>
