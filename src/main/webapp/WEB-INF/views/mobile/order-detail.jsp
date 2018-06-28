<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name ="viewport" content ="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="apple-touch-fullscreen" content="YES" />

    <title>报障单详情</title>
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
		.colorMain{
			color: #fd9100;
		}
		body{
			min-width: 320px;max-width:640px;
			font: .28rem microsoft yahei, arial, verdana, helvetica, sans-serif;
			color: #333;
		}
		.order-info{
			padding: .35rem .3rem;
			border-bottom: 1px solid #e0e0e0;
			font-size: 0;
			position: relative;
		}
		.order-info .name{
			display: inline-block;
			vertical-align: top;
			margin-left: .15rem;
		}
		.order-info .name span{
			font-size: .32rem;
		}
		.order-info .name p{
			font-size: .28rem;
			color: #808080;
			width: 5rem;
			margin-top: .2rem;
		}
		.new-tab{
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			background: url(${ctx}/images/front/index_icon13_47.png) right .3rem center no-repeat;
			background-size: .2rem;
		}
		.wrap{
			border-bottom: 1px solid #e0e0e0;
			padding-bottom: .35rem;
			width: 7.5rem;
			position: relative;
		}
		.title{
			line-height: 1rem;
			padding: 0 .2rem;
			font-size: .32rem;
		}
		.title .icon{
			height: 1rem;
			width: .5rem;
			display: inline-block;
			vertical-align: top;
		}
		.title.people .icon{
			background: url(${ctx}/images/mobile/people.png) center no-repeat;
			background-size: .35rem;
		}
		.title.look .icon{
			background: url(${ctx}/images/mobile/look.png) center no-repeat;
			background-size: .35rem;
		}
		.title.fault .icon{
			background: url(${ctx}/images/mobile/no_click.png) center no-repeat;
			background-size: .35rem;
		}
		.details{
			margin-left: .7rem;
		} 
		.details li{
			margin-bottom: .25rem;
		} 
		.details li span{
			color: #808080;
		} 
		.details li font{
			display: inline-block;
			vertical-align: top;
			width: 5rem;
		}
		.img-list{
			font-size: 0;
			margin-left: .7rem;
		}
		.img-list li{
			display: inline-block;
			vertical-align: top;
		}
		.commitments{
			display: inline-block;
			vertical-align: top;
			margin-left: .25rem;
		}
		.commitments p{
			line-height: .55rem;
			font-size: .32rem;
		}
		.commitments span{
			font-size: .28rem;
			color: #808080;
		}
		.phone-btn{
			width: .95rem;
			height: .96rem;
			background: url(${ctx }/images/mobile/phone.png) center no-repeat;
			background-size: .5rem;
			margin-right: .66rem;
		}
		.vip{
			position: relative;
		}
		.vip:after{
			content: '';
			position: absolute;
			bottom: -.08rem;
			right:0;
			width: .36rem;
			height:.36rem;
			display: block;
			background: url(${ctx}/images/icon7_15.png) center no-repeat;
			background-size: .35rem;
		}
		.btn{
			display: block;
			width: 1.47rem;
			line-height: .57rem;
			border: 1px solid #fd9100;
			color:#fd9100;
			text-align: center;
			margin: .45rem .3rem;
		}
	</style>
   
    <!--[if lt IE 9]>
      <script src="${ctx}/js/html5shiv.js"></script>
      <script src="${ctx}/js/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  	<div class="order-info">
  		<div style="display:inline-block;vertical-align:top;width:1.4rem;height:1.4rem;background:url(${ctx}/images/icon17_03.png) center no-repeat;background-size:cover"></div>
  		<div class="name">
  			<span>金域蓝湾项目</span>
  			<p>福建省厦门市思明区软件园二期望海路39号楼215</p>
  		</div>
  		<a class="new-tab"></a>
  	</div>
  	<div class="wrap">
  		<p class="title fault">
  			<i class="icon"></i>报障信息
  		</p>
  		<ul class="details">
  			<li>
  				<span>报障状态：</span>
  				<font class="colorMain">待评价</font>
  			</li>
  			<li>
  				<span>服务内容：</span>
  				<font>视频监控</font>
  			</li>
  			<li>
  				<span>故障级别：</span>
  				<font>紧急</font>
  			</li>
  			<li>
  				<span>报障编号：</span>
  				<font>xp1234567</font>
  			</li>
  			<li>
  				<span>报障时间：</span>
  				<font>2016-10-08</font>
  			</li>
  			<li>
  				<span>受理时间：</span>
  				<font>2016-10-08</font>
  			</li>
  			<li>
  				<span>保障说明：</span>
  				<font>福建省厦门市思明区软件园二期望海路39号楼215</font>
  			</li>
  		</ul>
  		<ul class="img-list">
  			<c:forEach begin="0" end="3"> 
  			<li>
  				<div style="display:inline-block;vertical-align:top;width:1.45rem;height:1.45rem;background:url(${ctx}/images/icon17_03.png) center no-repeat;background-size:cover"></div>
  			</li>
  			</c:forEach>
  		</ul>
  	</div>
  	<div class="wrap">
  		<p class="title people">
  			<i class="icon"></i>申请人
  		</p>
  		<a class="rightArea phone-btn"></a>
  		<div class="vip" style="display:inline-block;vertical-align:top;width:.96rem;height:.96rem;border-radius:100%;margin-left:.7rem;background:url(${ctx}/images/icon17_03.png) center no-repeat;background-size:cover">  		</div>
  		<div class="commitments">
  			<p>小龙君</p>
  			<span>厦门致上科技</span>
  		</div>
  	</div>
  	<div class="wrap">
  		<p class="title people">
  			<i class="icon"></i>乙方负责人
  		</p>
  		<a class="rightArea phone-btn"></a>
  		<div class="" style="display:inline-block;vertical-align:top;width:.96rem;height:.96rem;border-radius:100%;margin-left:.7rem;background:url(${ctx}/images/icon17_03.png) center no-repeat;background-size:cover">  		</div>
  		<div class="commitments">
  			<p>小龙君</p>
  			<span>厦门致上科技</span>
  		</div>
  	</div>
  	<div class="wrap" style="padding-bottom: 0;">
  		<p class="title look">
  			<i class="icon"></i>查看报障单
  		</p>
  		<a class="new-tab"></a>
  	</div>
  	<div class="wrap" style="padding-bottom: 0;">
  		<p class="title look">
  			<i class="icon"></i>查看现场
  		</p>
  		<a class="new-tab"></a>
  	</div>
  	<a class="btn rightArea" href="#">去评价</a>
  	<div class="clear"></div>
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