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
    <link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/PlatformInfo.css'/>"/>
    <title>云平台配置保存/更新</title>
</head>
<body>
<div class="content">
    <form class="form-horizontal" id="platformInfoForm" name="platformInfoForm" onsubmit="return false">
        <!-- 云平台类型-->
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">云平台类型：</label>
            <div class="col-xs-5 col-md-5">
                <select class="form-control ue-form platform_type" id="platform_type">
                    <option value="vsphere">vsphere</option>
                </select>
            </div>
        </div>
        <!-- 主机IP -->
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">主机IP：</label>
            <div class="col-xs-5 col-md-5">
                <input type="text" autocomplete="off" class="form-control ue-form" id="host" name="host" datatype="*">
            </div>
        </div>
        <!-- 用户名 -->
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">用户名：</label>
            <div class="col-xs-5 col-md-5">
                <input type="text" autocomplete="off" class="form-control ue-form" id="username" name="username" datatype="*">
            </div>
        </div>
        <!-- 密码 -->
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">密码：</label>
            <div class="col-xs-5 col-md-5">
                <input type="password" autocomplete="off" class="form-control ue-form" id="password" name="password" datatype="*">
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label"></label>
            <div class="col-xs-5 col-md-5">
                <button type="button" class="btn ue-btn-primary" id="sure">确定</button>
                <button type="button" class="btn ue-btn" id="cancel">取消</button>
            </div>
        </div>
    </form>
</div>
</body>

<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/PlatformInfo.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
    var contextPath = '<%=request.getContextPath()%>';
</script>
</html>
