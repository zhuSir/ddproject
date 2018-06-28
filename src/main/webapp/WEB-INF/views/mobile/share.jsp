<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name ="viewport" content ="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="apple-touch-fullscreen" content="YES" />

    <title>分享</title>
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
		body,textarea,input,button,select,keygen,legend {font: .24rem/1.14 yahei,\5b8b\4f53;color:#333 ;outline:0 ;}
		a{color: #333; }
		button{border: none ;}
		input{-webkit-appearance: none; }
		body{min-width: 320px;max-width:640px;margin: 0 auto;}
		img{margin-bottom:.75rem;width:7.5rem;}
		.btn{
			display: block;
			margin: .25rem auto;
			width:5.1rem;
			line-height: .95rem;
			border-radius: 4px;
			text-align: center;
			color:#fff;
		}
		.btn:before{
			content: '';
			display: inline-block;
			vertical-align: top;
			height: .95rem;
			width: .8rem;
			background-position: center;
			background-repeat: no-repeat;
			background-size: .4rem;
		}
		@-webkit-keyframes flipInX {
		    0% {
		        -webkit-transform: perspective(400px) rotateX(90deg);
		        transform: perspective(400px) rotateX(90deg);
		        opacity: 0;
		    }
		
		    40% {
		        -webkit-transform: perspective(400px) rotateX(-10deg);
		        transform: perspective(400px) rotateX(-10deg);
		    }
		
		    70% {
		        -webkit-transform: perspective(400px) rotateX(10deg);
		        transform: perspective(400px) rotateX(10deg);
		    }
		
		    100% {
		        -webkit-transform: perspective(400px) rotateX(0deg);
		        transform: perspective(400px) rotateX(0deg);
		        opacity: 1;
		    }
		}
		
		@keyframes flipInX {
		    0% {
		        -webkit-transform: perspective(400px) rotateX(90deg);
		        -ms-transform: perspective(400px) rotateX(90deg);
		        transform: perspective(400px) rotateX(90deg);
		        opacity: 0;
		    }
		
		    40% {
		        -webkit-transform: perspective(400px) rotateX(-10deg);
		        -ms-transform: perspective(400px) rotateX(-10deg);
		        transform: perspective(400px) rotateX(-10deg);
		    }
		
		    70% {
		        -webkit-transform: perspective(400px) rotateX(10deg);
		        -ms-transform: perspective(400px) rotateX(10deg);
		        transform: perspective(400px) rotateX(10deg);
		    }
		
		    100% {
		        -webkit-transform: perspective(400px) rotateX(0deg);
		        -ms-transform: perspective(400px) rotateX(0deg);
		        transform: perspective(400px) rotateX(0deg);
		        opacity: 1;
		    }
		}
		.flipInX { -webkit-backface-visibility: visible !important;-ms-backface-visibility: visible !important;backface-visibility: visible !important;-webkit-animation-name: flipInX;animation-name: flipInX;}
		.animated {-webkit-animation-duration: 1s; animation-duration: 1s;-webkit-animation-fill-mode: both;animation-fill-mode: both;z-index: 100;}
		.Android.btn{background-color: #fe3c2e}
		.IOS.btn{background-color: #333;animation-delay:.5s;-webkit-animation-delay:.5s; /* Safari 和 Chrome */}
		.Android.btn:before{background-image: url(${ctx}/images/front/download_az_icon_05.png)}
		.IOS.btn:before{background-image: url(${ctx}/images/front/download_ios_icon_09.png)}
	
	</style>
   
    <!--[if lt IE 9]>
      <script src="${ctx}/js/html5shiv.js"></script>
      <script src="${ctx}/js/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  	<input type="hidden" name="type_v" value="${type}">
  	<img alt="" src="${ctx }/images/front/download_pic_02.png">
  	<a id="android" class="btn Android animated flipInX">
  		<span>下载Android版</span>
  	</a>
  	<a id="ios" class="btn IOS animated flipInX" href="">
  		<span>下载IOS版</span>
  	</a>
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
	    var type = $("input[name='type_v']").val();
	    if(type==0){
	    	$("#android").attr("href","http://fir.im/l74r");
	    	$("#ios").attr("href","itms-apps://itunes.apple.com/us/app/dong-dong-jia-fang/id1176821427?l=zh&ls=1&mt=8");
	    }else{
	    	$("#android").attr("href","http://fir.im/rndc");
	    	$("#ios").attr("href","itms-apps://itunes.apple.com/us/app/dong-dong-yi-fang/id1176838525?l=zh&ls=1&mt=8");
	    }
		  
	  })
  	</script>
  </body>
</html>