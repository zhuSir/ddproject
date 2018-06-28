<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>意见反馈</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<%@ include file="head-meta.jsp"%>
		<link href="${ctx}/resources/wxweb/css/1.9.css" rel="stylesheet" />
		<script type="text/javascript">
			function submitData(){
				var content_str = $("#content").val();
				if(content_str ==null || content_str == ""){
					return ;
				}
				var data = {content : content_str};
				var userId = $("#userId").val();
				if(userId != null && userId != ""){
					data.userId = userId;
				}
				$.ajax({
					url : "${ctx}/advice-feedback",
					data : data,
					type: "post",
					dataType : 'json',
					contentType:"application/x-www-form-urlencoded;charset=utf-8",
					success : function(result) {
						alert(result.info);
						$("#content").val("");
					},
					error : function(e) {}
				});
			}
		</script>
	</head>
	<body>
		<div class="main">
			<div class="text">
				<textarea name="" id="content" placeholder="点赞的、吐槽的，建议的统统可以发给我们，我们还会第一时间处理。"></textarea>
			</div>
			<input type="hidden" name="userId" id="userId" value="${userId}" >
			<button type="submit" onclick="submitData()" style="background-color: orange" >提交</button>
		</div>
	</body>
</html>