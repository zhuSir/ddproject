<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name ="viewport" content ="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="apple-touch-fullscreen" content="YES" />

    <title>绑定成功</title>
	<%@ include file="/WEB-INF/views/commons/head-meta.jsp"%>
	<%@ include file="/WEB-INF/views/commons/js-meta.jsp"%>
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
		body{
			min-width: 320px;max-width:640px;
			font: .28rem microsoft yahei, arial, verdana, helvetica, sans-serif;
			color: #333;
		}
		.remind{
			text-align: center;
			line-height: 1rem;
			padding: .2rem 0;
		}
		.icon{
			display: inline-block;
			vertical-align: top;
			width: .5rem;
			height: 1rem;
			background: url(${ctx }/images/icon52_03.png) center no-repeat;
			background-size: .35rem;
		}
		ul > li{
			border-bottom: 1px solid #e0e0e0;
			padding: .1rem .1rem;
			margin: 0 .3rem;
			line-height: .8rem;
		}
		ul > li > span{
			color: #808080;
		}
	</style>
   
    <!--[if lt IE 9]>
      <script src="${ctx}/js/html5shiv.js"></script>
      <script src="${ctx}/js/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  	<div class="remind">
  		<i class="icon"></i>绑定成功
  	</div>
  	<ul>
  		<li>
  			<font class="rightArea">${user.name} </font>
  			<span>姓名</span>
  		</li>
  		<li>
  			<font class="rightArea">${user.phone} </font>
  			<span>手机</span>
  		</li>
  		<li>
  			<font class="rightArea">${user.companyName} </font>
  			<span>团队</span>
  		</li>
  		<li>
  			<font class="rightArea">${user.title} </font>
  			<span>职位</span>
  		</li>
  		<li>
  			<font class="rightArea">${user.mail} </font>
  			<span>邮箱</span>
  		</li>
  		<li>
  			<font class="rightArea">${user.remark} </font>
  			<span>简介</span>
  		</li>
  	</ul>
  	
  	<script type="text/javascript">
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
	  
	  })
  	</script>
  </body>
</html>