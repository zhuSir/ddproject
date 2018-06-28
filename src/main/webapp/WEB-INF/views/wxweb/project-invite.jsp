<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邀请同事</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<%@ include file="head-meta.jsp"%>
		<link href="${ctx}/resources/wxweb/css/in3.css" rel="stylesheet" />
		<script src="${ctx}/js/jquery.qrcode.min.js"></script>
		<script>
		$(function(){
			$('.foot .pb').click(function(){
				$('.wrap').show();
				$('.invited').show();
				$('html,body').addClass('ovfHiden');
			});
			$('.xia p').click(function(){
				$('.wrap').hide();
				$('.invited').hide();
				$('html,body').removeClass('ovfHiden');
			});
			var path = '${webPath}';
			var qrcode= $('#qrcode').qrcode(path).hide();
		    var canvas=qrcode.find('canvas').get(0);  
		    $('#qrcode_img').attr('src',canvas.toDataURL('image/jpg'));
		    $("#strong").html('${projectName}');
		});
		</script>
	</head>
	<body>
		<div class="main">
			<p class="pt">恭喜你</p>
			<div class="dx">
				<p>你已经成功创建 <strong class="st1" id="strong">立信广场项目之一</strong> 维保项目。</p>
				<p class="p2">邀请同事，开始体验便捷的移动维保管理!</p>
				<p class="pse"><spn class="s1">长按二维码，转发给微信好友。</spn></p>
				<div id="qrcode" ></div>
				<img class="im1" src="" id="qrcode_img" >
			</div>
		</div>
		<div class="wrap"></div>
		<div class="invited">
			<div class="sh">
				<p>将你要邀请的人建立一个群，发送给朋友时选择这个群即可。
				</p>
			</div>
			<div class="xia">
				<p>我知道了</p>
			</div>
		</div>
	</body>
</html>