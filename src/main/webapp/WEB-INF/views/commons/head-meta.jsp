<%@page import="com.xmsmartcity.extra.aliyun.OssUtil"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<!--  <meta name="viewport" content="width=device-width,target-densitydpi=high-dpi,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/> -->

<!-- <meta name="viewport" content="width=320,user-scalable=no" />  -->
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" /> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="version" value="1.12"/>
<c:set var="ctx" value="<%=request.getContextPath()%>"/>
<c:set var="static_ctx" value="<%=request.getContextPath()%>"/>
<c:set var="file_ctx" value="<%=OssUtil.FILE_ACCESS_URL%>"/>
<c:set var="img_ctx" value="<%=OssUtil.IMG_ACCESS_URL%>"/>

<script> var ctx = '${ctx}';</script>
<script> var static_ctx = '${static_ctx}';</script>
<script> var file_ctx = '${file_ctx}';</script>
<script> var img_ctx = '${img_ctx}';</script>

<link rel="stylesheet" type="text/css" href="${static_ctx}/css/plugins/artDialog.css?v=${version}">
<link rel="stylesheet" type="text/css" href="${static_ctx}/css/style.default.css?v=${version}">
<link rel="stylesheet" type="text/css" href="${static_ctx}/css/style.css?v=${version}">
<link rel="stylesheet" type="text/css" href="${static_ctx}/css/pagebar.css?v=${version}" />
<link rel="stylesheet" type="text/css" href="${static_ctx}/css/frontDialog.css?v=${version}" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/plugins/jquery.datetimepicker.css?v=${version}" />
<!--pageBag样式  -->
<link rel="stylesheet" type="text/css" href="${static_ctx}/css/pagebar.css?v=${version}" />
<%-- <link rel="stylesheet" type="text/css" href="${static_ctx}/css/jc.css?v=${version}" />
<link rel="stylesheet" type="text/css" href="${static_ctx}/css/re-ayc.css?v=${version}" /> --%>

