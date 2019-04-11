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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/ServerReview.css'/>"/>
	<title>云主机审核</title>
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
		</div>
		<br>
		<div class="box-content">
			<form class="form-horizontal" id="basicInfoForm" name="basicInfoForm" onsubmit="return false">
				<!-- 实例名称 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">实例名称：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="instance_name" name="instance_name" ></label>
			        </div>
			    </div>
			    <!-- 申请人 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">申请人：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="apply_user" name="apply_user" ></label>
			        </div>
			    </div>
			    <!-- 申请数量 -->
			    <div class="form-group" id="apply_num_div">
			        <label class="col-xs-2 col-md-2 control-label">申请数量：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="apply_num" name="apply_num" ></label>
			        </div>
			    </div>
			    <!-- 申请时间 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">申请时间：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="apply_time" name="apply_time" ></label>
			        </div>
			    </div>
				<!-- 申请理由 -->
				<div class="form-group">
					<label class="col-xs-2 col-md-2 control-label">申请理由：</label>
					<div class="col-xs-8 col-md-8">
						<label id="apply_reason" name="apply_reason" ></label>
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
		</div>
		<br>
		<div class="box-content">
			<form class="form-horizontal" id="configInfoForm" name="configInfoForm" onsubmit="return false">
				<!-- CPU -->
				<div class="form-group">
					<label class="col-xs-2 col-md-2 control-label" id="cpu_label">CPU：</label>
					<div class="col-xs-1 col-md-1">
						<label id="cpu" name="cpu" ></label>
					</div>
					<div id="cpu_new_div">
						<label class="control-label" style="margin-left: 20px">申请CPU：
							<label id="cpu_new" name="cpu_new" ></label>
						</label>
					</div>
				</div>
				<!-- 内存 -->
				<div class="form-group">
					<label class="col-xs-2 col-md-2 control-label" id="memory_label">内存：</label>
					<div class="col-xs-1 col-md-1">
						<label id="memory" name="memory" ></label>
					</div>
					<div id="memory_new_div">
						<label class="control-label" style="margin-left: 20px">申请内存：
							<label id="memory_new" name="memory_new" ></label>
						</label>
					</div>
				</div>
			    <!-- 镜像 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">镜像：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="iso" name="iso" ></label>
			        </div>
			    </div>
				<div id="disk_div">
					<!-- 系统盘 -->
					<div class="form-group">
						<label class="col-xs-2 col-md-2 control-label">系统盘：</label>
						<div class="col-xs-8 col-md-8">
							<label id="system_disk" name="system_disk" ></label>
						</div>
					</div>
					<!-- 数据盘大小 -->
					<div class="form-group">
						<label class="col-xs-2 col-md-2 control-label">数据盘大小：</label>
						<div class="col-xs-8 col-md-8">
							<label id="data_disk_size" name="data_disk_size" ></label>
						</div>
					</div>
					<!-- 数据盘数量 -->
					<div class="form-group">
						<label class="col-xs-2 col-md-2 control-label">数据盘数量：</label>
						<div class="col-xs-8 col-md-8">
							<label id="data_disk_num" name="data_disk_num" ></label>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!--审核信息-->
	<div class="box reviewInfo">
		<div class="box-header">
			<div class="box-left"></div>
			<div style="float:left;">审核信息</div>
		</div>
		<br>
		<div class="box-content">
			<form class="form-horizontal" id="reviewInfoForm" name="reviewInfoForm" onsubmit="return false">
				<!-- 审核装态 -->
			    <div class="form-group">
			        <label class="col-xs-2 col-md-2 control-label">审核状态：</label>
			        <div class="col-xs-8 col-md-8">
			            <label id="status" name="status" ></label>
			        </div>
			    </div>
			    <!-- 虚拟化环境 -->
			    <div class="form-group" id="virtual_environment_div">
			        <label class="col-xs-2 col-md-2 control-label">虚拟环境：</label>
			        <div class="col-xs-8 col-md-8" id="virtual_environment_content"></div>
			    </div>
				<!--审核意见-->
				<div class="form-group">
					<label class="col-xs-2 col-md-2 control-label">审核意见：</label>
					<div class="col-xs-8 col-md-8" id="audit_reply_content">
						<textarea class="form-control ue-form pull-left audit_reply" id="audit_reply" rows="5" cols="30" style="margin-top: 10px"></textarea>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="button-row">
		<div class="form-group">
	        <label class="col-sm-1 control-label"></label>
	        <div class="col-sm-9">
	            <button type="button" class="btn ue-btn-primary" id="pass">通过</button>
	            <button type="button" class="btn ue-btn-primary" id="reject">驳回</button>
	        </div>
	    </div>
    </div>
</div>
</body>

<script id="virtual_environment_script" type="text/html">
	<select class="form-control ue-form pull-left" id="virtual_environment" style="max-width:160px" datatype="*">
		<option value="">请选择云平台类型</option>
		{{each platformType as item}}
			<option value="{{item}}">{{item}}</option>
		{{/each}}
	</select>
	<span class="fa fa-refresh pull-left" style="margin-left: 10px; margin-top: 10px" title="点击刷新数据" onclick="refresh_platform_type()"></span>
	<button type="button" class="btn ue-btn-primary pull-left" id="addPlatformType" style="margin-left:10px">配置云平台类型</button>
</script>

<script  type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/ServerReview.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
	var instanceName = '<%=request.getParameter("instancename")%>';
	$(".back").click(function(){
    	window.location.href = contextPath + "/service/ecs/review";
    });
</script>
</html>