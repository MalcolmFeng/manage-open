<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>充值中心</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/pay/payinfo.css'/>"/>

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
<!-- 页面结构 -->
<!-- 登录内容 -->
<div class="pay-container">
    <div class="row">
        <div class="con-title" style="padding-left: 15px">充值</div>
    </div>
    <div class="con-body">
        <div class="pay-balance">余额：¥0.00</div>
        <ul class="list-inline pay-way">
            <li>支付宝充值</li>
        </ul>
        <div class="recharge-account">
            <label>充值金额：</label>
            <input class="col-xs-9 col-md-9 ue-form" type="text" name="rechargeAccount" id="rechargeAccount"/>
            <label style="margin-left: 10px">元</label>
        </div>

        <div class="alipay-img">
            <img src="<l:asset path='img/alipaylogo.jpg'/>">
        </div>
        <div class="alipay-warning">
            <p>温馨提示：</p>
            <p>1. 不支持信用卡方式充值。</p>
            <p>2. 充值后请及时对支付订单进行结算，以免影响正常服务。</p>
        </div>
        <button type="button" id="payBtnNoAccount" class="btn ue-btn-disable">充值</button>
        <button type="button" id="payBtn" class="btn ue-btn-primary" style="display:none;">充值</button>
    </div>
</div>

<!-- 需要引用的JS -->
<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='service/pay/payinfo.js'/>"></script>
<script type="text/javascript">
    var context = "<%=request.getContextPath()%>";
</script>
</body>
</html>