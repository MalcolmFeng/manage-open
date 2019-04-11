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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/ResetPwd.css'/>"/>
	<title>管理控制台-重置密码</title>
</head>
<body>
<div class="content">
	<form class="form-horizontal" id="pwdForm" name="pwdForm" onsubmit="return false">
		<!-- 当前密码 -->
		<div class="form-group">
	        <label class="col-xs-3 col-md-3 control-label"><span style="color:red">*</span>当前密码：</label>
	        <div class="col-xs-8 col-md-8">
	            <input type="password" autocomplete="off" class="form-control ue-form" id="current_pwd" name="current_pwd" value="" datatype="current_pwd"> 
	            <div id="exist_tips"></div>         				
	        </div>	        
	    </div>
		
		<!-- 登录密码 -->
		<div class="form-group" style="margin-bottom:0">
	        <label class="col-xs-3 col-md-3 control-label"><span style="color:red">*</span>登录密码：</label>
	        <div class="col-xs-8 col-md-8">
	            <input type="password" autocomplete="off" class="form-control ue-form" id="pwd" name="pwd" value="" datatype="pwd">          				
	        </div>	        
	    </div>
	    <div class="form-group">
	        <label class="col-xs-3 col-md-3 control-label"></label>
	        <div class="col-xs-8 col-md-8 rules">	      
	            <span class="col-xs-12 col-md-12">8-30个字符，必须同时包含大写字母、小写字母、数字和特殊字符中至少三项</span>      				
	        </div>	        
	    </div>
	    
	    <!-- 确认密码 -->
	    <div class="form-group">
		    <label class="col-xs-3 col-md-3 control-label"><span style="color:red">*</span>确认密码：</label>
		    <div class="col-xs-8 col-md-5">
		    	<input type="password" autocomplete="off" class="form-control ue-form" id="re_pwd" name="re_pwd" datatype="*" recheck="pwd"></textarea>
		    </div>
		</div>
	    
	    <div class="form-group">
		    <label class="col-xs-3 col-md-3 control-label"></label>
		    <div class="col-xs-8 col-md-5">
		    	<button type="button" class="btn ue-btn-primary" id="sure">确定</button>
	            <button type="button" class="btn ue-btn" id="cancel">取消</button>
		    </div>
		</div>
	</form>
</div>

<script id="exist_tips_content" type="text/html">
	<span style="margin-left:5px;color:#ffb437">当前密码输入错误</span>
</script>
</body>
<script  type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/ResetPwd.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
	var instanceName = '<%=request.getParameter("instancename")%>';
	var password = '<%=request.getParameter("password")%>';
</script>
</html>