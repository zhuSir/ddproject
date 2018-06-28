<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>创建团队</title>
<%@ include file="head-meta.jsp"%>
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<link rel="stylesheet" href="${ctx}/resources/wxweb/css/1.5.css" />
<script src="${ctx}/resources/wxweb/js/in1.js"></script>
<script type="text/javascript">
	$(function() {
		var errorMsg = '${error}';
		if(errorMsg != ''){
			alert(errorMsg);
		}
	})

	//submit data
	function submitData(){
		var companyName = $("#companyName").val();
		if (companyName == null || companyName == '') {
			alert("团队名称不能为空，请重新填写！");
			return;
		}
		var username = $("#name").val();
		if (username == null || username == '') {
			alert("名称不能为空，请重新填写！");
			return;
		}
		var number = $("#number").val();
		if (number == null || number == '') {
			alert("手机号码不能为空，请重新填写！");
			return;
		}
		var code = $("#code").val();
		if (code == null || code == '') {
			alert("验证码不能为空，请重新填写！");
			return;
		}
		$('#submitData').submit();
	}

	function sendCode() {
		var phone = $('#number').val();
		if (phone == null || phone == '') {
			alert("手机号码不能为空，请重新填写！");
			return;
		}
		$.ajax({
			url : "/dweibao/api/user/xcx/code",
			data : {
				phone : phone,
				type : 0
			},
			success : function(result) {
				var result_code = result.code;
				if (result_code == 0) {
					$(".send").removeAttr("onclick");
					changeNum(30);
				} else {
					alert("result infi:"+result.info);
				}
			},
			error : function(e) {
				alert("请求失败,请重新再试！");
			}
		});
	}
	function changeNum(num) {
		setTimeout(function() {
			if (num > 1) {
				$('.send').text(num);
				--num;
				changeNum(num);
			} else {
				$('.send').text("发送验证码");
				$(".send").attr("onclick", "sendCode()");
			}
		}, 1000);
	}
	
	function changeBtnColor(){
		var companyName = $("#companyName").val();
		var username = $("#name").val();
		var number = $("#number").val();
		var code = $("#code").val();
		if (companyName != null && companyName != '' &&
				username != null && username != '' &&
				number != null && number != '' &&
				code != null && code != '') {
			$("#submitBtn").css("background-color","orange");
			$("#submitBtn").attr("disabled",false);
		}
	}
</script>
</head>
<body>
	<div class="main">
		<form action="${ctx}/wx/company/create-register" method="post" id="submitData">
			<div class="top">
				<label for="">名&nbsp&nbsp&nbsp&nbsp称 :</label> 
				<input type="text" placeholder="填写团队名称" class="ip1" name="companyName" id="companyName" value="${companyName}" />
			</div>
			<div class="tab">
				<ul>
					<li><label for="">姓&nbsp&nbsp&nbsp&nbsp名 :</label>
						<input type="text" id="name" class="ip1 same" name="username" onfocus="changeBtnColor()" value="${username}" />
					</li>
					<li>
						<label class="p1">手机号：</label>
						<input type="text" id="number" class="same ip2" name="phone" onfocus="changeBtnColor()" value="${phone}" />
					</li>
					<li>
						<label class="p1">验证码：</label> 
						<input type="text" class="prov ip2" id="code" name="code" onfocus="changeBtnColor()" onkeyup="changeBtnColor()" /> 
						<span class="send" onclick="sendCode()" style="color: orange;">发送验证码</span>
					</li>
				</ul>
				<input type="hidden" name="openId" id="openId" value="${openId}" >
				<input type="hidden" name="unionid" id="unionid" value="${unionid}" >
			</div>
			<button onclick="submitData()" id="submitBtn" >下一步</button>
		</form>
	</div>
</body>
</html>