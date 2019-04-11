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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/ModifyBasicInfo.css'/>"/>
	<title>管理控制台-修改基本信息</title>
</head>
<body>
<div class="content">
	<form class="form-horizontal" id="modifyInfoForm" name="modifyInfoForm" onsubmit="return false">
		<!-- 实例名称 -->
		<div class="form-group" style="margin-bottom:0">
	        <label class="col-xs-2 col-md-2 control-label"><span style="color:red">*</span>实例名称：</label>
	        <div class="col-xs-8 col-md-8">
	            <input type="text" class="form-control ue-form" id="instance_name" name="instance_name" value="" datatype="*2-128">          				
	        </div>	        
	    </div>
	    <div class="form-group">
	        <label class="col-xs-2 col-md-2 control-label"></label>
	        <div class="col-xs-8 col-md-8 rules">	      
	            <span class="col-xs-8 col-md-8">长度限制为2-128个字符</span>      				
	        </div>	        
	    </div>
	    
	    <!-- 实例描述 -->
	    <div class="form-group" style="margin-bottom:0">
		    <label class="col-xs-2 col-md-2 control-label">实例描述：</label>
		    <div class="col-xs-8 col-md-5">
		    	<textarea class="form-control ue-form" id="description" name="description" datatype="description"></textarea>
		    </div>
		</div>
		<div class="form-group">
	        <label class="col-xs-2 col-md-2 control-label"></label>
	        <div class="col-xs-8 col-md-8 rules">	      
	            <span class="col-xs-8 col-md-8">长度限制为2-256个字符</span>      				
	        </div>	        
	    </div>
	    
	    <div class="form-group">
		    <label class="col-xs-2 col-md-2 control-label"></label>
		    <div class="col-xs-8 col-md-5">
		    	<button type="button" class="btn ue-btn-primary" id="sure">确定</button>
	            <button type="button" class="btn ue-btn" id="cancel">取消</button>
		    </div>
		</div>
	</form>
</div>
</body>
<script  type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/ModifyBasicInfo.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
	var instanceid = '<%=request.getParameter("instanceid")%>';
</script>
</html>