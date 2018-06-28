<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE >
<html>
<head>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>注册团队</title>		
		<%@ include file="head-meta.jsp"%>
		<link rel="stylesheet" href="${ctx}/resources/wxweb/css/create-company.css" />
		<link rel="stylesheet" href="${ctx}/resources/wxweb/css/LArea.css">
		<script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp"></script>
		<script src="${ctx}/resources/wxweb/js/jquery.min.js"></script>
		<script src="${ctx}/resources/wxweb/js/LAreaData1.js"></script>
    	<script src="${ctx}/resources/wxweb/js/LAreaData2.js"></script>
    	<script src="${ctx}/resources/wxweb/js/LArea.js"></script>
    	<script src="${ctx}/resources/wxweb/js/new_Larea.js"></script>
		<script>
			var geocoder = null;
			$(function(){
				var errorMsg = '${error}';
				if(errorMsg != ''){
					alert(errorMsg);
				}
				
				$('.main button').click(function(){
					var vl=$('input').val();
					if(vl==""){
					   $('.main button').attr('disabled', true);
			      	   $('.main button').css('background','#D0D0D0');
					}else{
						$('.main button').attr('disabled', false); 
			   				$('.main button').css('background','#D0D0D0');
			   				$(this).css('background','#ff8100');
					}
				})
				// 调用地址解析类
				geocoder = new qq.maps.Geocoder({
				  complete : function(result) {
				    console.log(result);
				    var str = '';
				    str += result.detail.addressComponents.province + "-";
				    str += result.detail.addressComponents.city + "-";
				    str += result.detail.addressComponents.district;
				    $("#region").val(str);
				    $("#addr").val(result.detail.addressComponents.street);
				    $("#lat").val(result.detail.location.lat);
				    $("#lng").val(result.detail.location.lng);
				  }
				});
				getLocation();
			});

			function getLocation() {
			  // 判断是否支持 获取本地位置
			  if (navigator.geolocation) {
			    navigator.geolocation.getCurrentPosition(showPosition);
			  } else {
			    alert("浏览器不支持定位.");
			  }
			}
			function showPosition(position) {
			  var lat = position.coords.latitude;
			  var lng = position.coords.longitude;
			  var latLng = new qq.maps.LatLng(lat, lng);
			  geocoder.getAddress(latLng);
			}
			
			// submit data
			function submitData(){
				var companyName = $("#companyName").val();
				if (companyName == null || companyName == '') {
					alert("团队名称不能为空，请重新填写！");
					return;
				}
				var region = $("#region").val();
				if (region == null || region == '') {
					alert("地区不能为空，请重新填写！");
					return;
				}
				var addr = $("#addr").val();
				if (addr == null || addr == '') {
					alert("地址不能为空，请重新填写！");
					return;
				}
				$('#submitData').submit();
			}
			
		</script>
	</head>
	<body>
		<div class="main">
		<form action="${ctx}/wx/company/create" method="post" id="submitData">
		<div class="top">
			<label for="">名&nbsp&nbsp&nbsp&nbsp称 :</label>
			<input type="text" placeholder="填写团队名称" class="ip1" name="companyName" value="${companyName}" id="companyName" />
		</div>
		<div class="tab">
			<ul>
				<li>
					<label for="">地&nbsp&nbsp&nbsp&nbsp区 :</label>
					<input type="text" id="region" class="ip1 sames" name="region" value="${region}" readonly="readonly"/>
					<input id="value1" type="hidden" value="20,234,504" readonly="readonly"/>
					<input id="lat" type="hidden" value="24.46887" /> 
					<input id="lng"	type="hidden" value="118.09243" />
					<img src="${ctx}/resources/wxweb/img/Arrow.png" />
				</li>
				<li>
					<label for="">地&nbsp&nbsp&nbsp&nbsp址 :</label>
					<input type="text"  class="ip1 same" id="addr" name="addr" value="${addr}" />
				</li>
			</ul>
			<input type="hidden" name="openId" id="openId" value="${openId}" >
			<input type="hidden" name="unionid" id="unionid" value="${unionid}" >
			<input type="hidden" name="userId" id="userId" value="${userId}" >
		</div>
		<button onclick="submitData()">下一步</button>
		</form>
	</div>
	</body>
</html>