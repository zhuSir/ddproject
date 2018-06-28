<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<%@ include file="/WEB-INF/views/wxweb/head-meta.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/wxweb/css/in1.2.css" />
<link rel="stylesheet" href="${ctx}/resources/wxweb/css/LArea.css" />
<script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp"></script>
<script src="${ctx}/resources/wxweb/js/LAreaData1.js"></script>
<script src="${ctx}/resources/wxweb/js/LAreaData2.js"></script>
<script src="${ctx}/resources/wxweb/js/LArea.js"></script>
<script src="${ctx}/resources/wxweb/js/project.js"></script>
<title>创建项目</title>
</head>
<body>
	<div class="main">
	<input id="userId" type="hidden" value="${userInfo.id}" />
	<input id="userName" type="hidden" value="${userInfo.name}" />
	<input id="token" type="hidden" value="${userInfo.token}" />
	<input id="userPhone" type="hidden" value="${userInfo.phone}" />
		<div class="sh">
			<ul>
				<li>
					<span>名称：</span> 
					<input type="text" placeholder="填写项目名称" name="name" id="name" data-info="请填写项目名称" />
				</li>
				<li>
					<span>团队：</span>
					<input type="text" class="black" id="companyName" name="companyName" data-info="请填写团队名称" value="${userInfo.companyName}"/>
				</li>
			</ul>
		</div>
		<div class="zh">
			<ul>
				<li>
					<span for="">地区 :</span> 
					<input type="text" id="addressStr" class="ip1 same" data-info="请填写地址" readonly="readonly"/>
					<input id="value1" type="hidden" value="20,234,504" /> 
					<input id="lat" type="hidden" value="24.46887" /> 
					<input id="lng" type="hidden" value="118.09243" /> 
					<img src="${ctx}/resources/wxweb/img/Arrow.png" class="im1 black" />
				</li>
				<li>
					<span>地址：</span> 
					<input type="text" class="black" name="address" id="address" data-info="请填写地址" />
				</li>
			</ul>
		</div>
		<div class="xia">
			<ul>
				<li class="lis">
					<p class="p1">
						<span class="s1">报修负责方(甲方)</span> 
						<!-- <span class="s2">微信邀请 
							<img src="${ctx}/resources/wxweb/img/btn-weh.png" alt="" />
						</span>
						-->
					</p>
					<p class="p2">
						姓名： <input type="text" placeholder="报修负责人姓名" id="ownerName"
							name="ownerName" data-info="请填写姓名" />
					</p>
				</li>
				<li class="lst"><span>电话：</span> <input type="text" placeholder="报修负责人电话"
					class="black" id="ownerPhone" name="ownerPhone" data-info="请填写联系电话" />
				</li>
			</ul>
		</div>
		<button type="submit" id="submit" data-type="1">提交</button>
	</div>
	<div class="wrap"></div>
	<div class="share">
		<div class="sh">
			<img src="${ctx}/resources/wxweb/img/image-hand-t.png" />
		</div>
		<p>我知道了</p>
	</div>
</body>
</html>