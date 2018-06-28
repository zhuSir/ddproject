<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>错误页面</title>
		<%@ include file="head-meta.jsp"%>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="${ctx}/resources/wxweb/css/1.6.css" />
		<script>
			$(function(){
				$('footer ul li').click(function(){
					var index = $(this).index();
					$(this).attr('class',"content").siblings('ul li').attr('class','ss');
					$('.content').eq(index).show(200).siblings('.content').hide();
				});
				  $('footer ul li').click(function(){
				  	 $('footer ul li').css('color','#666666');
				  	 $(this).css('color','#FF8100');
				  })
			})
		</script>
	</head>
	<body>
		<div class="main">
			<div class="content" >
				<img src="${ctx}/resources/wxweb/img/icon-error.png" alt="" class="img2"/>
				<p class="p">请用微信平台打开</p>
			</div>
		</div>
	</body>
</html>