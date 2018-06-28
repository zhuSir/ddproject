<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维保项目</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
		<%@ include file="/WEB-INF/views/wxweb/head-meta.jsp"%>
		<link rel="stylesheet" href="${ctx}/resources/wxweb/css/in4.css" />
		<script type="text/javascript">
			var sCode = null;
			$(function(){
				sCode = '${code}';
				var res = ${res};
				pushTxt(res);
				initscrollEvent();
			});
			
			function toInvite(type,serviceId,projectId){
				var project = {serviceTypeId : serviceId,id:projectId};
				window.location.href=ctx+"/wx/project/go/project-invite?type="+type
						+"&projectId="+projectId
						+"&serviceTypeId="+serviceId
						+"&isMenu=1";
			}
			
			function initscrollEvent(){
				$(window).scroll(function(){
			　       var scrollTop = $(this).scrollTop();               //滚动条距离顶部的高度
			　　   var scrollHeight = $(document).height();           //当前页面的总高度
			　　   var windowHeight = $(this).height();               //当前可视的页面高度
		　        	if(scrollTop + windowHeight >= scrollHeight){      //距离顶部+当前高度 >=文档总高度 即代表滑动到底部
		　        		if(sCode != undefined && sCode != null){
		　        			var projects = $('.pub');
			　        		var postData = {code:sCode,startIndex:projects.length,pageSize:10};
			　        		$.ajax({
			　        		    url : ctx+"/wx/project/list",
			　        		    type : "POST",
			　        		    data :postData,
			　        		    success : function(data) {
			　        		    	pushTxt(data);
			　        		    },
			　        		    error : function(data) {}
			　        		  });
		　        		}
		       	}else if(scrollTop<=0){         //滚动条距离顶部的高度小于等于0
		        }
		    });
			}
			
			function pushTxt(res){
				if(res != undefined && res != null){
					res = res.data;
					var size = res.length;
					for(var i=0;i<size;i++){
						var projectName = res[i].name;
						var services = res[i].services;
						var htmlTxt = 
						"<div class='pub'>"+
						"<span class='p1'>"+projectName+"</span>"+
						"</div>"+
						"<div class='list'>"+
							"<ul>"+
								"<a onclick='toInvite("+res[i].type+","+services[0].id+","+res[i].id+")'>"+
									"<li class='ls'>"+
										"<img src='${ctx}/resources/wxweb/img/icon-child.png' class='im1'/>"+
										"<span>"+services[0].name+"</span>"+
										"<input hidden='true' value='"+services[0].id+"'/>"+
										"<img src='${ctx}/resources/wxweb/img/Arrow.png' alt='' class='im2'/>"+
									"</li>"+
								"</a>"+
							"</ul>"+
						"</div>";
						$('.main').append(htmlTxt);
					}
				}
			}
		</script>
	</head>
	<body>
		<div class="main"></div>
	</body>
</html>