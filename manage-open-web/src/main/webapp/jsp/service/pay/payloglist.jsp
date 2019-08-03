<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html lang="zh-CN" style="height:100%">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>浪潮 Health Mall 开放平台-充值记录</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/pay/payloglist.css'/>"/>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
    <script type="text/javascript">
        //项目上下文
        var context = "<%=request.getContextPath()%>";
    </script>
</head>
<body style="height:100%;overflow: hidden;">
<div class="container col-xs-12 col-md-12">
    <%--    <div class="row">--%>
    <%--        <div id="page-title" class="col-xs-12 col-md-12">充值记录</div>--%>
    <%--    </div>--%>
    <div class="col-xs-12 col-md-12" style="margin-top: 20px;">
        <table id="payList" class="table table-bordered table-hover">
            <thead>
            <tr>
                <th width="25%" data-field="orderId">订单号</th>
                <th width="20%" data-field="paySeq" data-sortable="false">支付流水号</th>
                <th width="20%" data-field="updateTime" data-sortable="false">日期</th>
                <th width="10%" data-field="amount" data-sortable="false">充值金额</th>
                <th width="15%" data-field="authzStatus" data-sortable="false" data-render="renderstatus">状态</th>
                <th width="10%" data-field="log_id" data-sortable="false" data-render="renderoptions">操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='service/pay/payloglist.js'/>"></script>
</body>
</html>