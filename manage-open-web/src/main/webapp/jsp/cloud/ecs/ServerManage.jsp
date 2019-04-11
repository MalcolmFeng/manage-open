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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/ServerManage.css'/>"/>
	<title>云主机管理控制台</title>
</head>
<body>
<div class="col-xs-12 col-md-12 header">
	<a class="back"><span>实例列表</span></a>
	<a class="back" style="float:right"><span>返回列表</span></a>
	<hr>
</div>

<div class="content">
	<!-- 基本信息 -->
	<div class="box basicInfo">
		<div class="box-header">
			<div class="box-left"></div>
			<div style="float:left;">基本信息</div>
			<div class="btn-group" role="group">
				<!-- 远程登录 -->
				<%--<button class="btn ue-btn" id="telnet">远程登录</button>--%>
				<!-- 开机 -->
				<button class="btn ue-btn" id="poweredOn">开机</button>
				<!-- 关机 -->
				<button class="btn ue-btn" id="poweredOff">关机</button>
				<!-- 重启 -->
				<button class="btn ue-btn" id="reset">重启</button>
                <!-- 更多 -->
				<div class="btn-group">
					<button class="btn ue-btn dropdown-toggle" type="button" id="more" data-toggle="dropdown">更多<span class="caret"></span></button>
					<ul class="dropdown-menu dropdown-menu-right ue-dropdown-menu">
						<li class="ue-dropdown-angle"></li>
						<li onclick="resetPwd_click()" id="resetPwd"><a>重置密码</a></li>
						<li onclick="modifyKey_click()" id="modifyKey"><a>修改密钥</a></li>
					</ul>
				</div>			
			</div>
		</div>
		<br/>
		<div class="box-content">
			<form class="form-horizontal" id="basicInfoForm" name="basicInfoForm" onsubmit="return false">
				<!-- 实例名称 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">实例名称：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="instance_name" name="instance_name" ></label>					
			        </div>
			    </div>
			    <!-- 审核状态 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">审核状态：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="review_status" name="review_status" ></label>					
			        </div>
			    </div>
				<!-- 审核意见 -->
				<div class="form-group" id="audit_reply_content">
					<label class="col-xs-2 col-md-2 control-label">审核意见：</label>
					<div class="col-xs-8 col-md-8">
						<label id="audit_reply" name="audit_reply" ></label>
					</div>
				</div>
			    <!-- 申请时间 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">申请时间：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="apply_time" name="apply_time" ></label>					
			        </div>
			    </div>
			    <!-- 运行状态 -->
			    <div class="form-group" id="running_status_content">
			        <label class="col-xs-2 col-md-2 control-label">运行状态：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="running_status" name="running_status" ></label>					
			        </div>
			    </div>
			    <!-- 创建时间 -->
			    <div class="form-group" id="create_time_content">
			        <label class="col-xs-2 col-md-2 control-label">创建时间：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="create_time" name="create_time" ></label>					
			        </div>
			    </div>
			    
			</form>
		</div>
	</div>
	<!-- 配置信息 -->
	<div class="box configInfo">
		<div class="box-header">
			<div class="box-left"></div>
			<div style="float:left;">配置信息</div>
			<div>
				<div class="btn-group">
					<!-- 更改实例规格 -->
					<span class="specification_tips"><button class="btn ue-btn" id="modify_specification">更改实例规格</button></span>
	           </div>			
			</div>
		</div>
		<br/>
		<div class="box-content">
			<form class="form-horizontal" id="configInfoForm" name="configInfoForm" onsubmit="return false">
				<!-- CPU -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">CPU：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="cpu" name="cpu" ></label>					
			        </div>
			    </div>
			    <!-- 内存 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">内存：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="memory" name="memory" ></label>					
			        </div>
			    </div>
				<!-- 系统盘 -->
				<div class="form-group">
					<label class="col-xs-2 col-md-2 control-label">系统盘：</label>
					<div class="col-xs-8 col-md-8">
						<label id="system_disk" name="system_disk" ></label>
					</div>
				</div>
				<div class="form-group">
					<!-- 数据盘 -->
					<label class="col-xs-2 col-md-2 control-label">数据盘：</label>
					<div class="col-xs-1 col-md-1">
						<label id="data_disk_size" name="data_disk_size" ></label>
					</div>
					<!-- 数据盘数量 -->
					<label class="col-xs-1 col-md-1 control-label">数量：</label>
					<div class="col-xs-1 col-md-1">
						<label id="data_disk_num" name="data_disk_num" ></label>
					</div>
				</div>
			    <!-- 操作系统 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">操作系统：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="os" name="os" ></label>					
			        </div>
			    </div>
			    <!-- 私有IP -->
			    <div class="form-group" id="ip_content">
			        <label class="col-xs-2 col-md-2 control-label">私有IP：</label>
			        <div class="col-xs-8 col-md-8">	
			            <label id="ip" name="ip" ></label>
			        </div>
			    </div>
				<!-- 登录用户名 -->
				<div class="form-group" id="username_content">
					<label class="col-xs-2 col-md-2 control-label">登录用户名：</label>
					<div class="col-xs-8 col-md-8">
						<label id="username" name="username" ></label>
					</div>
				</div>
				<!-- 登录密码 -->
				<div class="form-group" id="password_content">
					<label class="col-xs-2 col-md-2 control-label">登录密码：</label>
					<div class="col-xs-8 col-md-8">
						<label id="password" name="password" ></label>
						<button id="password_button" class="password_button" onclick="viewPassword()">显示密码</button>
					</div>
				</div>
				<!-- 登录密钥 -->
				<div class="form-group" id="key_name_content">
					<label class="col-xs-2 col-md-2 control-label">登录密钥：</label>
					<div class="col-xs-8 col-md-8">
						<label id="key_name" name="key_name" ></label>
					</div>
				</div>
			</form>
		</div>
	</div>
	<br/>
	<div class="form-group">
		<div class="col-sm-12">
			<button type="button" class="btn ue-btn-primary back" style="min-width: 120px; min-height: 40px; margin-left: 50px;">确定
			</button>
		</div>
	</div>
</div>
</body>
<script  type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/ServerManage.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
	var instanceName = '<%=request.getParameter("instancename")%>';
	$(".back").click(function(){
    	window.location.href = contextPath + "/service/ecs/manage";
    });
</script>
</html>