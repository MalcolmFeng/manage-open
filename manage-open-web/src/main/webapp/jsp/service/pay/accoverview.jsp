<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>浪潮 Health Mall 开放平台-账户总览</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/pay/accoverview.css'/>"/>
    <script type="text/javascript">
        //项目上下文
        var context = "<%=request.getContextPath()%>";
        //获取静态资源上下文路径
        var assetPath = "<l:assetcontext/>";
    </script>
</head>
<body style="height:100%;">
<div id="overview">
    <%--    <div class="row">--%>
    <%--        <div id="page-title">账户总览</div>--%>
    <%--    </div>--%>
    <div id="recharge-panel" class="panel panel-default">
        <div class="panel-body">
            <div id="balance-text" class="col-md-10 col-xs-10">
                <div class="row">
                    <div id="amount">可用额度</div>
                    <div id="amount-text"><i class="fa fa-cny" style=""></i><span id="accountNum"></span></div>
                </div>
            </div>
            <div id="recharge-btn">
                <button type="button" id="payBtn" class="btn btn-primary">充值</button>
            </div>
        </div>
    </div>
    <div id="recharge-desc" class="panel panel-default col-md-12 col-xs-12" style="display: none">
        <i class="fa fa-check-circle" style=""></i>可用额度大于零，不会影响业务的正常使用！
    </div>
</div>

<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='service/pay/accoverview.js'/>"></script>
</body>
</html>