<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
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
    <link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/ModifyKey.css'/>"/>
    <title>管理控制台-修改密钥</title>
</head>
<body>
<div class="content">
    <div id="login_content"></div>

    <div class="form-group">
        <label class="col-xs-3 col-md-3 control-label"></label>
        <div class="col-xs-8 col-md-5" style="margin-top: 10px">
            <button type="button" class="btn ue-btn-primary" id="sure">确定</button>
            <button type="button" class="btn ue-btn" id="cancel">取消</button>
        </div>
    </div>
</div>

<script id="login_key" type="text/html">
    <div class="form-group">
        <label class="col-xs-3 col-md-3 control-label">密钥对：</label>
        <div class="col-xs-8 col-md-8">
            <select class="form-control ue-form pull-left key" id="key_pair" style="max-width:150px" datatype="*">
                <option value="">请选择密钥对</option>
                {{each keyList as item}}
                <option value="{{item}}">{{item}}</option>
                {{/each}}
            </select>
            <span class="fa fa-refresh pull-left" title="点击刷新数据" onclick="refresh_key()"></span>
            <button type="button" class="btn ue-btn-primary pull-left" id="addKey" style="margin-left:10px"
                    onclick="toNewKey()">新建密钥对
            </button>
        </div>
    </div>
</script>
</body>

<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/ModifyKey.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
    var contextPath = '<%=request.getContextPath()%>';
    var instanceName = '<%=request.getParameter("instancename")%>';
</script>
</html>