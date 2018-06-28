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
    <link rel="stylesheet" type="text/css" href="${ctx}/css/in1.css"/>
    <script src="${ctx}/js/jquery-1.7.1.min.js"></script>
    <script src="${ctx}/js/in1.js"></script>
    <script type="text/javascript" charset="utf-8">
    	function initData(){
	      	$('.dodo .qu').click(function(){
	      		$('.dodo').hide();
	      	});
	      	var errorMsg = "${error}";
	      	if(errorMsg != ""){
	      		alert(errorMsg);
	      	}
	      	var userName = '${userName}';
	      	if(userName != ''){
	      		userName = "亲爱的"+userName;
	      		$('.chehu').text(userName);
	      	}
      	}
      	
      	function checkData(){
      		var username = $('#username').val();
      		if(username == null || username == ''){
      			alert("用户名不能为空，请重新填写！");
      			return false;
      		}
      		var phone = $('#userphone').val();
      		if(phone == null || phone == ''){
      			alert("手机号码不能为空，请重新填写！");
      			return false;
      		}
      		var code = $('#code').val();
      		if(code == null || code == ''){
      			alert("验证码不能为空，请重新 填写！");
      			return false;
      		}
      		var passwd = $('#password').val();
      		if(passwd == null || passwd == ''){
      			alert("密码不能为空，请重新填写！");
      			return false;
      		}
      		return true;
      	}
      	
      	function submitData(){
      		var isTrue = checkData();
      		if(isTrue){
	      		$('.mui-input-group').submit();
      		}
      	}
      	
      	function sendCode(){
      		var phone = $('#userphone').val();
      		if(phone == null || phone == ''){
      			alert("手机号码不能为空，请重新填写！");
      			return ;
      		}
			$.post("/dweibao/api/user/xcx/code",
					{phone:phone,type:0},
					function(result){
						var resultObj = $.parseJSON(result);
				   		var result_code = resultObj.code;
				   		if(result_code == 0){
				   			$(".fas").removeAttr("onclick");
					      	changeNum(30);
				   		}
			   			alert(resultObj.info);
					});
      	}
      	function changeNum(num){
      		setTimeout(function (){
	      		if(num > 1){
	      			$('.fas').text(num);
	      			--num;
	      			changeNum(num);
	    		}else{
	    			$('.fas').text("发送验证码");
	    			$(".fas").attr("onclick","sendCode()");
	    		}
      		},1000);
      	}
    </script>
</head>
<body onload="initData()" >
	<div class="mui-content">
		<div class="party">
			<img src="${ctx}/images/pict.png" alt="" />
			<p class="chehu">亲爱的用户:</p>
			<p class="pcx"><strong>${reUserName}</strong><span class="te">(<span class="tn">${phone}</span>)</span>在"咚咚维保云"上创建了<strong class="s1"
				>${companyName}</strong> 团队自有维修管理平台。</p>
			<p class="px">快速加入团队！</p>
		</div>
		<form class="mui-input-group" action="/dweibao/join-company" method="post" >
		<ul>
			<li>
				<span class="sp1">手机号</span>
				<input type="text" class="pass" id="userphone" name="phone" />
			</li>
			<li>
				<span class="sp1">姓&nbsp&nbsp&nbsp名</span>
				<input type="text" id="username" class="ip2 pass" name="userName" class="ip2"/>
			</li>
			<li>
				<span class="sp1">验证码</span>
				<input type="text" id="code" class="inp1" name="code" />
				<span class="sp2" onclick="sendCode()" >发送验证码</span>
			</li>
			<li>
				<span class="sp1">密&nbsp&nbsp&nbsp码</span>
				<input type="password" class="ip2 pass"  id="password"  name="password" />
			</li>
		</ul>
		<input  type="text" hidden="true" name="reUserId" value="${reUserId}">
		<input  type="text" hidden="true" name="companyId" value="${companyId}" >
		<input  type="text" hidden="true" name="openId" value="${openId}" >
		<input  type="text" hidden="true" name="unionid" value="${unionid}" >
	</form>
	<button class="mui-btn btn1" onclick="submitData()" >确定</button>
	</div>
	<div class="box"></div>
	<div class="dodo">
		<div class="sh">
			<p>您已经在咚咚维保云平台加入了"xxxx团队"，打开"咚咚维保云"app，马上开始报修，维保等工作。</p>
		</div>
		<div class="xia">
			<p class="qu">确定</p>
		</div>
	</div>
</body>
</html>