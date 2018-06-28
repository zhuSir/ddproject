<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邀请同事</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<%@ include file="head-meta.jsp"%>
		<link href="${ctx}/resources/wxweb/css/1.8.css" rel="stylesheet" />
<%-- 		<link href="${ctx}/resources/wxweb/css/project-invite-menu.css" rel="stylesheet" /> --%>
		<script src="${ctx}/js/jquery.qrcode.min.js"></script>
		<script type="text/javascript">
		$(function (){
			var path = '${path}';
			var qrcode= $('#qrcode').qrcode(path).hide();
		    var canvas=qrcode.find('canvas').get(0);  
		    $('#qrcode_img').attr('src',canvas.toDataURL('image/jpg'))  
		})
		</script>
		<script>
			$(function(){
				$('.pb').click(function(){
					$('.wrap').show();
					$('.invited').show();
					$('html,body').addClass('ovfHiden');
				});
				$('.xia p').click(function(){
					$('.wrap').hide();
					$('.invited').hide();
					$('html,body').removeClass('ovfHiden');
				});
			});
		</script>
	</head>
	<body>
		<div class="main">
			<p class="pt">邀请同事，开始体验</p>
			<p class="p0">便捷的移动维保管理</p>
			<div class="pict">
				<img class="im1" src="" id="qrcode_img" >
			</div>
			<p class="orange">长按二维码</p>
			<p class="p0 orange">转发给微信好友</p>
			<p class="pb">
					<span>如何一次邀请多个人？</span>
					<img src="${ctx}/resources/wxweb/img/btn-gray.png" alt="" />
					<div id="qrcode" ></div>
			</p>
		</div>
		<div class="wrap"></div>
		<div class="invited">
			<div class="sh">
				<p>将你要邀请的人建立一个群，发送给朋友时选择这个群即可。</p>
			</div>
			<div class="xia">
				<p>我知道了</p>
			</div>
		</div>
	</body>
</html>