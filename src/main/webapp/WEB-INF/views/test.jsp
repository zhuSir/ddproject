<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js" ></script>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="css/layui.css" />
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
	<c:set var="ctx" value='<%=request.getAttribute("appType")%>'/>
<!--   	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"> -->
<!-- 	<link rel="stylesheet" href="//res.layui.com/layui/build/css/layui.css" media="all"> -->
</head>
<body style="margin: 20px;font-size: 20px;">
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
	  <legend>测试页面</legend>
	</fieldset>
<!-- 	<form class="layui-form" action=""  > -->
	<div class="layui-form-item">
	  <label class="layui-form-label">URL:</label>
	  <div class="layui-input-block" style="margin-right: 100px;" >
	    <input type="text" id="test_url" lay-verify="title" autocomplete="off" placeholder="please input url" class="layui-input">
	  </div>
	</div>
	<div class="layui-form-item" >
	    <div class="layui-inline">
	      <label class="layui-form-label">KEY:</label>
	      <div class="layui-input-inline">
	        <input type="tel" id="key1" lay-verify="phone" autocomplete="off" class="layui-input">
	      </div>
	      <label class="layui-form-label">VALUE:</label>
	      <div class="layui-input-inline">
	        <input type="tel" id="val1" lay-verify="phone" autocomplete="off" class="layui-input">
	      </div>
	    </div>
		<i class="layui-icon" onclick="add_param()" style=" cursor:pointer; font-size: 30px; margin-left:20px; color: #1E9FFF;">&#xe654;</i>
		
		<button class="layui-btn layui-btn-normal" onclick="toto()" style="margin-left:30px;" >请求</button>
	</div>
	
	<div class="layui-form-item" id="params-div" ></div>
	
    <div class="layui-form-item layui-form-text" style="margin-right: 100px;"  >
	  <div class="layui-input-block">
	    <textarea placeholder="RESULTS.." id="result_text" rows="20" class="layui-textarea" readonly="readonly"></textarea>
	  </div>
	</div>

	<script type="text/javascript">

	(function(m, ei, q, i, a, j, s) {
        m[i] = m[i] || function() {
            (m[i].a = m[i].a || []).push(arguments)
        };
        j = ei.createElement(q),
            s = ei.getElementsByTagName(q)[0];
        j.async = true;
        j.charset = 'UTF-8';
        j.src = 'http://static.meiqia.com/dist/meiqia.js';
        s.parentNode.insertBefore(j, s);
    })(window, document, 'script', '_MEIQIA');
    _MEIQIA('entId', 58150);
	
		var id_index = 1;
		
		function add_param(){
			id_index++;
			$("#params-div").append(
					"<div class=\"layui-form-item\" >"+
				    "<div class=\"layui-inline\">"+
				      "<label class=\"layui-form-label\">KEY:</label>"+
				      "<div class=\"layui-input-inline\">"+
				        "<input type=\"text\" id=\"key"+id_index+"\" lay-verify=\"phone\" autocomplete=\"off\" class=\"layui-input\">"+
				     " </div>"+
				     " <label class=\"layui-form-label\">VALUE:</label>"+
				    "  <div class=\"layui-input-inline\">"+
				     "   <input type=\"text\" id=\"val"+id_index+"\" lay-verify=\"phone\" autocomplete=\"off\" class=\"layui-input\">"+
				   "   </div>"+
				  "  </div>"+
					"</div>" );
			
		}
	
		function toto(){
			var params_json = "{";
			for(var i=0;i<id_index;i++){
				var paramKey = $("#key"+(i+1)).val();
				var paramVal = $("#val"+(i+1)).val();
				if($.trim(paramKey) != "" && $.trim(paramVal) != "" ){
					if(isNaN(paramVal)){
						params_json = params_json+"\""+paramKey+"\":\""+paramVal+"\",";
					}else{
						params_json = params_json+"\""+paramKey+"\":"+paramVal+",";
					}
				}
			}
			params_json = params_json.substr(0,params_json.length-1)+"}";
			
			var test_url = $("#test_url").val();

			var paramObj = eval('(' + params_json + ')');
			
			$.ajax({
				url : test_url,
				type : 'post',
				dataType : 'json',
// 				contentType : "application/json;charset=utf-8",  
				data : paramObj,
				success : function(data) {
					$("#result_text").val(data);
					var data_str = format_json(data,false);
					$("#result_text").val(data_str);
				},
				error : function(data) {
					alert(data);
				}
			});
		}
		
		function format_json(txt,compress/*是否为压缩模式*/){/* 格式化JSON源码(对象转换为JSON文本) */  
	        var indentChar = '    ';   
	        if(/^\s*$/.test(txt)){   
	            alert('数据为空,无法格式化! ');   
	            return;   
	        }
	        try{
		        var data=txt;
// 				var data=eval('('+txt+')');
			}   
	        catch(e){
	            alert('数据源语法错误,格式化失败! 错误信息: '+e.description,'err');   
	            return;   
	        };   
	        var draw=[],last=false,This=this,line=compress?'':'\n',nodeCount=0,maxDepth=0;   
	           
	        var notify=function(name,value,isLast,indent/*缩进*/,formObj){   
	            nodeCount++;/*节点计数*/  
	            for (var i=0,tab='';i<indent;i++ )tab+=indentChar;/* 缩进HTML */  
	            tab=compress?'':tab;/*压缩模式忽略缩进*/  
	            maxDepth=++indent;/*缩进递增并记录*/  
	            if(value&&value.constructor==Array){/*处理数组*/  
	                draw.push(tab+(formObj?('"'+name+'":'):'')+'['+line);/*缩进'[' 然后换行*/  
	                for (var i=0;i<value.length;i++)   
	                    notify(i,value[i],i==value.length-1,indent,false);   
	                draw.push(tab+']'+(isLast?line:(','+line)));/*缩进']'换行,若非尾元素则添加逗号*/  
	            }else   if(value&&typeof value=='object'){/*处理对象*/  
	                    draw.push(tab+(formObj?('"'+name+'":'):'')+'{'+line);/*缩进'{' 然后换行*/  
	                    var len=0,i=0;   
	                    for(var key in value)len++;   
	                    for(var key in value)notify(key,value[key],++i==len,indent,true);   
	                    draw.push(tab+'}'+(isLast?line:(','+line)));/*缩进'}'换行,若非尾元素则添加逗号*/  
	                }else{   
	                        if(typeof value=='string')value='"'+value+'"';   
	                        draw.push(tab+(formObj?('"'+name+'":'):'')+value+(isLast?'':',')+line);   
	                };   
	        };   
	        var isLast=true,indent=0;   
	        notify('',data,isLast,indent,false);   
	        return draw.join('');   
	    }  
	</script>
</body>
</html>