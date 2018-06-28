<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>加入创建团队</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<%@ include file="/WEB-INF/views/wxweb/head-meta.jsp"%>
		<link rel="stylesheet" href="${ctx}/resources/wxweb/css/1.7.css"/>
		<script>
			$(function(){
				$('.join .bt1 a ').click(function(){
					$('.wrap').show();
					$('.invited').show();
					$('html,body').addClass('ovfHiden');
				});
				$('.xia p').click(function(){
					$('.wrap').hide();
					$('.invited').hide();
					$('html,body').removeClass('ovfHiden');
				});
			})
			
			function toCreateCompany(){
				
			}
		</script>
	</head>
	<body>
		<div class="main">
			<img src="${ctx}/resources/wxweb/img/no-into.png" alt="" />
			<div class="join">
				<button class="bt1">
					<a href="#javascript;">加入团队</a>
				</button>
				<button class="bt2">
					<a href="${ctx}/wx/company/to/create-company?userId=${userId}&openId=${openId}&unionid=${unionid}">创建团队</a>
				</button>
			</div>
			<div class="xx">
				<p class="pt">拨打  <a href="tel:400-6568-189">400-6568-189</a>一对一服务</p>
			</div>
		</div>
		<div class="wrap"></div>
		<div class="invited">
			<div class="sh">
				<p>请向团队成员或维修项目成员索要邀请链接，然后点击链接即可加入团队。</p>
			</div>
			<div class="xia">
				<p>我知道了</p>
			</div>
		</div>
	</body>
</html>