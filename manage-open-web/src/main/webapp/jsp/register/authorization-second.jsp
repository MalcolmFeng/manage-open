<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String resourceId = request.getParameter("resourceId");
    String dataSourceType = request.getParameter("dataSourceType");
%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>书面协议和授权书认证</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <%--    <link rel="stylesheet" type="text/css" href="<l:asset path='css/select2.min.css'/>" />--%>
    <%--    <link rel="stylesheet" type="text/css" href="<l:asset path='platform/css/home.css'/>"/>--%>

    <script type="text/javascript" src="<l:asset path='jquery.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='form.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='arttemplate.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='knockout.js'/>"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
</head>
<body>
<div class="">
    <div class="row" style="padding: 0 20vw;display: flex;align-items: center;margin: 15px 0;">
        <img style="width: 1.5vw" src="/skins/skin/img/information.png" />
        <span style="margin-left: 15px;font-weight: bold;">上传书面协议或授权书文件</span>
    </div>
    <!-- 验证信息wrap -->
    <form class="form-horizontal" role="form">
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">上传书面协议和授权书文件<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <div class="input-group" style="width: 60%;">
                    <input class="form-control ue-form" type="text" placeholder="选择上传文件" id="filelist">
                    <div class="input-group-addon ue-form-btn" id="inputfiles">
                        <span class="fa fa-upload"></span>
                    </div>
                </div>
                <div class="progress" style="display: none;margin-top: 10px;">
                    <div class="progress-bar" id="inputpro">
                        <span></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">上传文件描述</label>
            <div class="col-xs-8 col-md-8">
                <textarea id="sketch" class="form-control ue-form Validform_input" rows="5" placeholder="上传文件描述"></textarea>
            </div>
        </div>
        <div class="form-group" style="padding-left: 20vw;">
            <span style="display: block;">提示：</span>
            <span style="display: block;">（1）仅提供上传与书面协议或授权书相关的文件；</span>
            <span style="display: block;">（2）每个附件限制大小20M以内；总附件大小在100M以内；</span>
            <span style="display: block;">（3）违规上传将无条件删除；</span>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"></label>
            <div class="col-sm-9">
                <button type="button" class="btn ue-btn-primary" id="validate">下一步</button>
                <button type="button" class="btn ue-btn" id="cancel">返回</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="/skins/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="/skins/js/bootstrap.js"></script>
<script type="text/javascript" src="/skins/js/ui.js"></script>
<script type="text/javascript" src="/skins/js/form.js"></script>
</body>
</html>