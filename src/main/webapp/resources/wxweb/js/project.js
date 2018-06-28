var geocoder = null;
$(function() {
   // 邀请弹窗
   $('.lis .s2').click(function() {
     $('.wrap').show();
     $('.share').show();
     $('html,body').addClass('ovfHiden');
   });
  $('.share p').click(function() {
    $('.wrap').hide();
    $('.share').hide();
    $('html,body').removeClass('ovfHiden');
  });
  
  $('#submit').css('background', '#ff8100');
  var area1 = new LArea();
  area1.init({
    'trigger' : '#addressStr', // 触发选择控件的文本框，同时选择完毕后name属性输出到该位置
    'valueTo' : '#value1', // 选择完毕后id属性输出到该位置
    'keys' : {
      id : 'id',
      name : 'name'
    }, // 绑定数据源相关字段 id对应valueTo的value属性输出 name对应trigger的value属性输出
    'type' : 1, // 数据源类型
    'data' : LAreaData
  // 数据源
  });
  area1.value=[14,153,3471];
  // 调用地址解析类
  geocoder = new qq.maps.Geocoder({
    complete : function(result) {
      console.log(result);
      var str = '';
      str += result.detail.addressComponents.province + "-";
      str += result.detail.addressComponents.city + "-";
      str += result.detail.addressComponents.district;
      $("#addressStr").val(str);
      $("#address").val(result.detail.addressComponents.street);
      $("#lat").val(result.detail.location.lat);
      $("#lng").val(result.detail.location.lng);
    }
  });
  getLocation();
  
  // 提交按钮绑定事件
  $("#submit").click(function() {
    createProject();
  });
})

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

function createProject() {
  var type=$("#submit").data("type");
  var ownerName="",ownerPhone="",maintainUserName="",maintainUserMobile="";
  if (type==1) {
    ownerName=$("#ownerName").val();
    ownerPhone=$("#ownerPhone").val();
    maintainUserName=$("#userName").val();
    maintainUserMobile=$("#userPhone").val();
  }else {
    maintainUserName=$("#ownerName").val();
    maintainUserMobile=$("#ownerPhone").val();
    ownerName=$("#userName").val();
    ownerPhone=$("#userPhone").val();
  }
  console.log(ownerName);
  $("#submit").attr('disabled', true);
  // 遍历是否都已填
  var check=true;
  $("input[type='text']").each(function(){  // 遍历input标签，判断是否有内容未填写
　　　　var vl=$(this).val();
　　　　if(vl==""){
　　　　　　alert($(this).data("info"));
        check=false;
        return false;
　　　　}
　　});
  if(!check){
    $("#submit").removeAttr('disabled');
    return;
  }
  // 获取定位坐标
  geocoder.getLocation($("#addressStr").val().replace("-","")+$("#address").val());
  var addressArr=$("#addressStr").val().split("-");
  var timestamp=new Date().getTime();
  var newArr=new Array(3);
  if (addressArr.length==2) {
    newArr[0]=addressArr[0];
    newArr[1]=addressArr[0];
    newArr[2]=addressArr[1];
  }else
    newArr=addressArr;
  var postData = {
      userId:$("#userId").val(),
      name:$("#name").val(),
      maintain_contract:new Date().getTime(),
      province:newArr[0],
      city:newArr[1],
      area:newArr[2],
      address:$("#address").val(),
      lat:$("#lat").val(),
      lng:$("#lng").val(),
      ownerName:ownerName,
      ownerPhone:ownerPhone,
      companyName:$("#companyName").val(),
      companyId:$("#companyId").val(),
      "services[0].name":$("#name").val()+"服务",
      "services[0].maintainUserName":maintainUserName,
      "services[0].maintainUserMobile":maintainUserMobile
  }
  console.log(postData);
  $.ajax({
    url : ctx+"/wx/project/do/project-create",
    type : "POST",
    data :postData,
    success : function(data) {
      $("#submit").removeAttr('disabled');
      if (data.code!=0) {
        serverErrorMsg(data);
        return;
      }
      data = data.data;
      var projectId = data.id;
      var serviceTypes = data.services;
      window.location.href=ctx+"/wx/project/go/project-invite?type="+type+"&projectId="+projectId+"&serviceTypeId="+serviceTypes[0].id;
    },
    error : function(data) {
      $("#submit").removeAttr('disabled');
    }
  });
}