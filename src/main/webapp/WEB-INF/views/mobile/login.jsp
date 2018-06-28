<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name ="viewport" content ="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="apple-touch-fullscreen" content="YES" />
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
	<c:set var="ctx" value="<%=request.getContextPath()%>"/>
	
	<script type="text/javascript" src="${ctx}/js/jquery-1.7.1.min.js" ></script>
	<script> var ctx = '${ctx}';</script>
    <title>绑定账号</title>
<%-- 	<%@ include file="/WEB-INF/jsp/commons/head-meta.jsp"%> --%>
<%-- 	<%@ include file="/WEB-INF/jsp/commons/js-meta.jsp"%> --%>
	<style type="text/css">
		/* reset */
		*,html,body,h1,h2,h3,h4,h5,h6,div,dl,dt,dd,ul,ol,li,p,blockquote,pre,hr,figure,table,caption,th,td,form,fieldset,legend,input,button,textarea,menu {margin: 0;padding: 0; }
		header,footer,section,article,aside,nav,hgroup,address,figure,figcaption,menu,details {display: block; }
		table{border-collapse: collapse;border-spacing: 0; }
		caption,th{text-align: left;font-weight: normal; }
		html,body,fieldset,img,iframe,abbr {border: 0; }
		i,cite,em,var,address,dfn{font-style: normal; }
		[hidefocus],summary{outline: 0; }
		li{list-style: none; }
		h1,h2,h3,h4,h5,h6,small{font-size: 100%; }
		sup,sub{font-size: 83%; }
		pre,code,kbd,samp{font-family: inherit; }
		q:before,q:after{content: none; }
		textarea{overflow: auto;resize: none; }
		label,summary{cursor: default; }
		a,button{cursor: pointer; }
		h1,h2,h3,h4,h5,h6,em,strong,b{font-weight: bold; }
		del,ins,u,s,a,a:hover{text-decoration: none; }
		body,textarea,input,button,select,keygen,legend {font: 12px/1.14 arial,\5b8b\4f53;color:#333 ;outline:0 ;}
		a{color: #333; }
		button{border: none ;}
		input{-webkit-appearance: none; }
		.leftArea{
			float: left;
		}
		.rightArea{
			float: right;
		}
		.clear{
			clear: both;
		}
		html{height:100%;background: url(${ctx}/images/mobile/back-img.png) center top no-repeat;
			background-size: cover;background-color: #263238;}
		body{
			min-width: 320px;max-width:640px;
			font: .28rem microsoft yahei, arial, verdana, helvetica, sans-serif;
			color: #333;
		}
		.title{
			padding: 2.5rem 0 .5rem 0rem;
		    font-size: .5rem;
		    text-align: center;
			background: url(${ctx}/images/mobile/logo.png) 3.2rem 0.5rem no-repeat;
		    background-size: 5rem;
			color:#fff;
		}
		form{

		}
		label{
			display: block;
			margin: .3rem .3rem;
			text-align: center;
		}
		label p{
			color: #fff;
			font-size: .32rem;
			margin: .1rem 0;
		}
		label input,.btn{
			width: 5.9rem;
			height: .8rem;
			padding: 0 .5rem;
			border-radius: 50px;
			background-color: transparent;
			border: 1px solid #e0e0e0;
			margin-left: -1px;
			font-size: .28rem;
			color: #fff;
			text-align: center;
		}
		.btn{
			display: block;
			margin: .8rem auto;
			line-height: .8rem;
			background-color: #e69d17;
			border: 1px solid #e69d17;
		}
		.btn:active{
			background-color: #ea9800;
			border: 1px solid #ea9800;
		}
		::-webkit-input-placeholder { /* WebKit browsers */ 
			color: #e0e0e0; 
		}
		/* 弹窗 */
		.layermbox {
			display: none;
		    position: absolute;
		    left: 0;
		    top: 0;
		    width: 100%;
		    z-index: 19891014;
		}
		.layermmain, .laymshade {
		    position: fixed;
		    left: 0;
		    top: 0;
		    width: 100%;
		    height: 100%;
		}
		.laymshade {
		    background-color: rgba(0, 0, 0, .2);
		    pointer-events: auto;
		}
		.layermmain {
		    display: table;
		    pointer-events: none;
		}
		.layermmain .section {
		    display: table-cell;
		    vertical-align: middle;
		    text-align: center;
		}
		.layermanim {
		    animation-name: bounceIn;
		    -webkit-animation-name: bounceIn;
		}
		.layermchild {
		    text-align: left;
		    background-color: #fff;
		    font-size: 14px;
		    border-radius: 6px;
		    box-shadow: 0 0 8px rgba(0, 0, 0, .1);
		    pointer-events: auto;
		    -webkit-animation-fill-mode: both;
		    animation-fill-mode: both;
		    -webkit-animation-duration: .18s;
		    animation-duration: .18s;
		}
		.layermchild {
		    display: inline-block;
		    position: relative;
		}
		.layermcont {
		    padding: .3rem .35rem;
		    line-height: .3rem;
		    border-radius: .1rem;
		    text-align: center;
		    font-size: .3rem;
		}
		.layer-meg {
		    margin: 0 .15rem;
		    text-align: center;
		    color: #fff;
		    font-size: .3rem;
		}

		@-webkit-keyframes bounceIn {
			0% {
				opacity: 0;
				-webkit-transform: scale(.5);
				transform: scale(.5)
			}
			100% {
				opacity: 1;
				-webkit-transform: scale(1);
				transform: scale(1)
			}
		}
	</style>
   
    <!--[if lt IE 9]>
      <script src="${ctx}/js/html5shiv.js"></script>
      <script src="${ctx}/js/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  	<div class="title">Login</div>
	<form id="login-bind">
	
	<input type="hidden" name="openId" value="${wxUser.openId}">
	<input type="hidden" name="unionid" value="${wxUser.unionid}">
		<label class="username">
			<p>账号</p>
			<i class="icon"></i><input type="text" name="userName" placeholder="请输入账户名">
		</label>
		<label class="password">
			<p>密码</p>
			<i class="icon"></i><input type="password" name="password" placeholder="请输入密码">
		</label>
		<a class="login-btn btn" href="#">立即绑定</a>
	</form>
	<!-- 弹窗 -->
	<div class="layermbox"><div class="laymshade" onclick="hide()"></div><div class="layermmain"><div class="section"><div class="layermchild  layermanim" style="background: rgba(0,0,0,.7);"><div class="layermcont"><div class="layer-meg">请输入账户名</div></div></div></div></div></div>
  	
  	
  	<script type="text/javascript">
  	
		$('#login-bind').submit(function(){
				
				var formData = $('#login-bind').serialize();
				//$('#login-form').find('input:submit').attr('disabled',true).attr('value','正 在 登 录 ...');
				$.ajax({
					url : ctx + "/mp/mobile/register/go/binding",
					type : 'post',
					dataType : 'json',
					data : formData,
					success : function(data){
						if(!data.success){
							promptMessage ("绑定失败"+data.info,'warning',false);
							$('#login-form').append('<span class="error">'+data.info+'</span>');
							return;
						}
						location.href = ctx + '/mp/mobile/register/go/binding-ok?id='+data.info;
					},
					error : function(data){
						$('#login-bind').find('.error').remove();
						$('#login-bind').append('<span class="error">服务器异常</span>');
					}
				});
				//已通过异步提交，所以要组织表单再次提交
				return false;
			});
		
  	
	  setFontSize();
	
	  //前端页面 单位转换 px 转 rem
	  function setFontSize(){
	      var _self = this;
	      _self.width = 750; //设置默认最大宽度
	      _self.fontSize = 100; //默认字体大小
	      _self.widthProportion = function(){
	        var p = (document.body && document.body.clientWidth || document.getElementsByTagName("html" )[0].offsetWidth)/_self.width;
	        return p>1?1:p<0.426?0.426:p;
	      };
	      _self.changePage = function(){
	          document.getElementsByTagName("html" )[0].setAttribute("style", "font-size:"+_self.widthProportion()*_self.fontSize+"px !important");
	          //  alert(_self.widthProportion());
	      };
	      //窗口大小改变
	      _self.changePage();
	      window.addEventListener('resize' ,function(){_self.changePage();}, false);
	  }
	  $(function(){
	  	$('.login-btn').click(function(){
	  		var n = $('input[name="userName"]').val();
	  		var p = $('input[name="password"]').val();
			if(n == '' || n == null){
				usernameError();
			}else if(p == '' || p == null){
				passwordError();
			}else{
				
				var formData = $('#login-bind').serialize();
				//$('#login-form').find('input:submit').attr('disabled',true).attr('value','正 在 登 录 ...');
				$.ajax({
					url : ctx + "/mp/mobile/register/go/binding",
					type : 'post',
					dataType : 'json',
					data : formData,
					success : function(data){
						if(!data.success){
							$('.layer-meg').text("绑定失败"+data.info);
							$('.layermbox').show().delay(3000).hide(0);
							$('#login-form').append('<span class="error">'+data.info+'</span>');
							return;
						}
						location.href = ctx + '/mp/mobile/register/go/binding-ok?id='+data.info;
					},
					error : function(data){
						$('#login-bind').find('.error').remove();
						$('#login-bind').append('<span class="error">服务器异常</span>');
					}
				});
				
				
			}
	  	});
	  })
	  function usernameError(){
		  	$('.layermbox').show().delay(3000).hide(0);
		  	$('.layer-meg').text('请输入账号');
		  }
	  function passwordError(){
	  	$('.layermbox').show().delay(3000).hide(0);
	  	$('.layer-meg').text('请输入密码');
	  }
	  function error(){
	  	$('.layermbox').show().delay(3000).hide(0);
	  	$('.layer-meg').text('账号或者密码错误');
	  }
	  function hide(){
		  $('.layermbox').hide();
	  }
  	</script>
  </body>
</html>