<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<!-- 需要引用的CSS -->
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/NewKey.css'/>"/>
	<title>创建密钥对</title>
</head>
<body>
<div class="container">
	<div class="head">
		<a class="toKeyList"><span>返回密钥对列表</span></a>
	</div>
</div>

<div class="hr"><hr></div>

<div class="content">
	<form class="form-horizontal" id="newKey" name="newKey" onsubmit="return false">
		<!-- 密钥对名称 -->
		<div class="form-group" style="margin-bottom:0">
	        <label class="col-xs-3 col-md-2 control-label"><span style="color:red">*</span>密钥对名称：</label>
	        <div class="col-xs-8 col-md-8">
	            <input type="text" type="text" class="form-control ue-form Validform_input" id="keyName" name="keyName" value="" datatype="keyName">
	            <div id="exist_tips"></div>	          				
	        </div>	        
	    </div>
	    <div class="form-group">
	        <label class="col-xs-3 col-md-2 control-label"></label>
	        <div class="col-xs-8 col-md-8">	      
	            <span class="col-xs-8 col-md-8 rules">2-128个字符，以大小写字母或中文开头，可包含数字、"."、"_"、":"或"-"</span>      				
	        </div>	        
	    </div>
	    
	    <!-- 创建类型 -->
	    <div class="form-group">
	        <label class="col-xs-3 col-md-2 control-label"><span style="color:red">*</span>创建类型：</label>
	        <div class="col-xs-8 col-md-5">
	        	<input type="radio" checked="checked" name="pattern" value="new"><span style="margin-left:10px">自动新建密钥对</span>
	        	<input type="radio" name="pattern" value="import" style="margin-left:10px"><span style="margin-left:10px">导入已有密钥对</span>
	        </div>
	    </div>
	    
	    <div id="import_key"></div>
	   
	    <div class="form-group">
	        <label class="col-sm-1 control-label"></label>
	        <div class="col-sm-9">
	            <button type="button" class="btn ue-btn-primary" id="sure">确定</button>
	            <button type="button" class="btn ue-btn" id="cancel">取消</button>
	            <span id="msgdemo"></span>
	        </div>
	    </div>
	</form>
</div>

<script id="import_key_content" type="text/html">
	<div class="form-group">
	    <label class="col-xs-3 col-md-2 control-label"><span style="color:red">*</span>公钥内容：</label>
	    <div class="col-xs-8 col-md-5">
	    	<textarea class="form-control ue-form" id="publicKey" name="publicKey" rows="3" cols="30" datatype="*"></textarea>
	    </div>
	</div>
	<div class="form-group">
	    <label class="col-xs-3 col-md-2 control-label"></label>
	    <div class="col-xs-8 col-md-5">
	       <label>(Base64编码)</label>
	       <button type="button" class="btn ue-btn-primary" id="importExample">导入样例</button>
	    </div>
	</div>
</script>

<script id="exist_tips_content" type="text/html">
	<span style="margin-left:5px;color:#ffb437">密钥对名称已存在</span>
</script>

</body>	
<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/NewKey.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
	
	$(".toKeyList").click(function(){
		window.location.href = contextPath + "/service/ecs/key";
	})
</script>
</html>