<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>浪潮 Health Mall 开放平台-开发指南</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='portal/css/dev/devguide.css'/>"/>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
    <script type="text/javascript">
        //项目上下文
        var context = "<%=request.getContextPath()%>";
        //获取静态资源上下文路径
        var assetPath = "<l:assetcontext/>";
    </script>
</head>
<body>
<!-- content -->
<div id="api-container" class="container">
    <div class="row">
        <div id="api-catalog" class="col-xs-3 col-md-3">
            <div class="row">
                <div id="api-cat-top">
                    <span class="navbar-left">开发指南</span>
                    <span id="cat-icon" class="glyphicon glyphicon-th-list nav-right"></span>
                </div>
                <div id="api-cat-bottom">
                    <ul class="nav nav-pills nav-stacked">
                        <li><a href="overview.jsp" target="iframe_link">概述</a></li>
                        <li class="active"><a href="appaccess.jsp" target="iframe_link">应用接入</a></li>
                        <li><a href="document.jsp" target="iframe_link">开发文档</a></li>
                        <li><a href="sdkdownload.jsp" target="iframe_link">SDK下载</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div id="api-contents" class="col-xs-9 col-md-9">
            <div class="row">
                <iframe id="mainframe" src="appaccess.jsp" name="iframe_link" onload="setIframeHeight(this)" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='portal/dev/devguide.js'/>"></script>
</body>
</html>