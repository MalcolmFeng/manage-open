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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/ServerApply.css'/>"/>
	<title>云主机申请</title>
</head>
<body>
<div class="col-xs-12 col-md-12 header">
	<a class="back"><span>实例列表</span></a>
	<a class="back" style="float: right"><span>返回列表</span></a>
	<hr>
</div>

<div class="content">
	<form class="form-horizontal" id="applyServer" name="applyServer" onsubmit="return false">
		<!-- 规格 -->
		<div class="form-group" id="vcpu_memory_val">
			<label class="col-xs-3 col-md-1 control-label">规格：</label>
			<div class="col-xs-8 col-md-8">
	            <label for="vcpu" class="pull-left">vCPU</label>
	            <select class="form-control ue-form pull-left vcpu" id="vcpu"></select>
	            <label for="memory" class="pull-left">内存</label>
	            <select class="form-control ue-form pull-left memory" id="memory"></select>
            </div>
        </div>
	    
	    <!-- 镜像 -->
	    <div class="form-group" id="os_version_val">
				<label class="col-xs-3 col-md-1 control-label">镜像：</label>
		        <div class="col-xs-8 col-md-8">
		            <label for="os" class="pull-left">操作系统</label>
		            <select class="form-control ue-form pull-left os" data-value="CentOS" id="os"></select>
		            <label for="version" class="pull-left">版本</label>
		            <select class="form-control ue-form pull-left version" data-value="7.4 64位" id="version"></select>
	            </div>
        </div>
	    	    
	    <!-- 磁盘 -->
	    <!-- 系统盘 -->
	    <div class="form-group">
	        <label class="col-xs-3 col-md-1 control-label"><span style="color:red">*</span>磁盘：</label>
	        <div class="col-xs-8 col-md-8">
	        	<label class="pull-left">系统盘</label>
	        	<input type="number" class="form-control ue-form pull-left" value="100" title="容量范围：100~500" id="system_disk" name="system_disk" min="100" max="500" onblur="toInteger(this)" datatype="systemDisk"/>
	        	<span class="unit-span" style="margin-left:5px">GB</span>
	        </div>
	    </div>
	    <!-- 数据盘 -->
	    <div class="form-group">
	        <label class="col-xs-3 col-md-1 control-label"></label>
	        <div class="col-xs-8 col-md-8">
				<label class="pull-left">数据盘</label>
				<input type="number" class="form-control ue-form pull-left data_disk_capacity" id="data_disk_size" title="容量范围：100~500" value="100" min="100" max="500" onblur="toInteger(this)" datatype="dataDisk"/>
				<span class="unit-span pull-left" style="margin-left:5px;margin-right:10px">GB</span>
				<label class="pull-left">数量</label>
				<input type="number" class="form-control ue-form pull-left data_disk_quantity" id="data_disk_num" value="1" min="1" datatype="positiveInteger"/>
	        	<%--<div class="btn-group pull-center pull-left">--%>
	        		<%--<button type="button" class="btn ue-btn-primary add">--%>
	        		<%--<span class="fa fa-plus"></span></button>--%>
        		<%--</div>--%>
        		<%--<div>--%>
		        	<%--<label class="pull-left">增加一块数据盘，您还可以选择<span id="data_disk_num"></span>块</label>--%>
	        	<%--</div>--%>
	        </div>
	    </div>
	    
	    <!-- 登录凭证 -->
	    <div class="form-group">
	        <label class="col-xs-3 col-md-1 control-label"><span style="color:red">*</span>登录凭证：</label>
	        <div class="col-xs-8 col-md-8">
	        	<input type="radio" checked="checked" name="login" value="key"><span style="margin-left:10px">密钥对</span>
	        	<input type="radio" name="login" value="pwd" style="margin-left:10px"><span style="margin-left:10px">自定义密码</span>
				<span id="login_tips" style="margin-left: 10px"></span>
	        </div>
	    </div>	    
	    <div id="login_content"></div>
	    
	    <!-- 实例名称 -->
	    <div class="form-group">
	        <label class="col-xs-3 col-md-1 control-label"><span style="color:red">*</span>实例名称：</label>
	        <div class="col-xs-8 col-md-8">
	        	<input type="text" class="form-control ue-form Validform_input" id="instance_name" name="instance_name" value="${instanceName}" datatype="instance"/>
				<div id="exist_tips"></div>
	        </div>
	    </div>

		<div class="form-group">
			<label class="col-xs-3 col-md-1 control-label"></label>
			<div class="col-xs-8 col-md-8">
				<span class="rules">2-128个字符，以大小写字母或中文开头，可包含数字、"."、"_"、":"或"-"</span>
			</div>
		</div>
	    
	    <!-- 申请数量 -->
	    <div class="form-group">
	        <label class="col-xs-3 col-md-1 control-label"><span style="color:red">*</span>申请数量：</label>
	        <div class="col-xs-8 col-md-8">
	        	<input type="number" class="form-control ue-form Validform_input pull-left" id="apply_number" name="apply_number" value="1" min="1" datatype="positiveInteger"/>
	        	<span style="margin-left:5px">台</span>
	        </div>
	    </div>

		<!--申请理由-->
		<div class="form-group" >
			<label class="col-xs-3 col-md-1 control-label">申请理由：</label>
			<div class="col-xs-8 col-md-8">
				<textarea class="form-control ue-form pull-left apply_reason" id="apply_reason" rows="5" cols="30"></textarea>
			</div>
		</div>
		<br>
	   
	    <div class="form-group">
	        <label class="col-sm-1 control-label"></label>
	        <div class="col-sm-9">
	            <button type="button" class="btn ue-btn-primary" id="apply">立即申请</button>
	            <button type="button" class="btn ue-btn" id="cancel">取消</button>
	            <span id="msgdemo"></span>
	        </div>
	    </div>
	</form>
</div>

<script id="login_key" type="text/html">
	<div class="form-group">
		<label class="col-xs-3 col-md-1 control-label"></label>
		<div class="col-xs-8 col-md-8">
			<label class="pull-left">密钥对</label>
			<select class="form-control ue-form pull-left key" id="key_pair" style="max-width:150px" datatype="keypair">
				<option value="">请选择密钥对</option>
				{{each keyList as item}}
					<option value="{{item}}">{{item}}</option>
				{{/each}}
			</select>
			<span class="fa fa-refresh pull-left" title="点击刷新数据" onclick="refresh_key()"></span>
			<button type="button" class="btn ue-btn-primary pull-left" id="addKey" style="margin-left:10px" onclick="toNewKey()">新建密钥对</button>
		</div>
	</div>
</script>

<script id="login_pwd" type="text/html">
	<div class="form-group">
	    <label class="col-xs-3 col-md-1 control-label"></label>
	    <div class="col-xs-8 col-md-8">
	       <label class="pull-left">&nbsp;&nbsp;&nbsp;&nbsp;登录名：</label>
	       <span class="pull-left" id="login_user">{{user}}</span>
	    </div>
	</div>
	<div class="form-group">
	    <label class="col-xs-3 col-md-1 control-label"></label>
	    <div class="col-xs-8 col-md-8">
	       <label class="pull-left"><span style="color:red">*</span>登录密码：</label>
	       <input type="password" autocomplete="off" class="form-control ue-form pull-left" id="pwd" name="pwd" style="max-width:200px" datatype="pwd"/>
	       <span class="rules">8-30个字符，必须同时包含大写字母、小写字母、数字和特殊字符中至少三项</span>
	    </div>
	</div>
	<div class="form-group">
	    <label class="col-xs-3 col-md-1 control-label"></label>
	    <div class="col-xs-8 col-md-8">
	       <label class="pull-left"><span style="color:red">*</span>确认密码：</label>
	       <input type="password" autocomplete="off" class="form-control ue-form pull-left" id="re_pwd" style="max-width:200px" datatype="*" recheck="pwd"/>
	    </div>
	</div>
</script>

<%--<script id="data_disk" type="text/html">--%>
	<%--<div class="form-group">--%>
	    <%--<label class="col-xs-3 col-md-1 control-label"></label>--%>
	    <%--<div class="col-xs-8 col-md-8">--%>
	        <%--<div class="btn-group pull-center pull-left">--%>
	        	<%--<button type="button" class="btn ue-btn-primary del" onclick="remove_data_disk(this)">--%>
	        	<%--<span class="fa fa-minus"></span></button>--%>
        	<%--</div>--%>
        	<%--<div>--%>
		    	<%--<label class="pull-left">数据盘</label>--%>
		    	<%--<input type="number" class="form-control ue-form pull-left data_disk_capacity" title="容量范围：20~32768" value="20" min="20" max="32768" onblur="toInteger(this)" datatype="dataDisk"/>--%>
		    	<%--<span class="unit-span pull-left" style="margin-left:5px;margin-right:10px">GB</span>--%>
		    	<%--<label class="pull-left">数量</label>--%>
		    	<%--<input type="number" class="form-control ue-form pull-left data_disk_quantity" value="1" min="1" onblur="toInteger(this)" datatype="positiveInteger"/>--%>
	     	<%--</div>--%>
	   <%--</div>--%>
	<%--</div>	--%>
<%--</script>--%>

<script id="exist_tips_content" type="text/html">
	<span style="margin-left:5px;color:#ffb437">云主机名称已存在</span>
</script>
</body>	
<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/ServerApply.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
	$(".back").click(function () {
		window.location.href = contextPath + "/service/ecs/manage";
	});
</script>
</html>