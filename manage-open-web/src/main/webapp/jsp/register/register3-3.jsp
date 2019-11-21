<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String resourceId = request.getParameter("resourceId");
    String dataSourceType = request.getParameter("dataSourceType");
%>
<html lang="en" style="height:100%">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>人工认证</title>

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

    <script type="text/javascript">
        //项目上下文
        var context = "<%=request.getContextPath()%>";
        //获取静态资源上下文路径
        var assetPath = "<l:assetcontext/>";

    </script>
    <style>
        .activeStep{
            height: 40px;
            width: 40px;
            border-radius: 20px;
            background-color: rgba(64,148,251,1);
            color: #FFFFFF;
            text-align: center;
            line-height: 40px;
        }
        .otherStep{
            height: 40px;
            width: 40px;
            border-radius: 20px;
            background-color: rgba(238,238,238,1);
            color: rgba(102,102,102,1);
            text-align: center;
            line-height: 40px;
        }
        .topTitle{
            font-size: 16px;
            font-weight: bold;
        }
        .btn{
            width: 80px;
            height: 30px;
            background-color: #3B8BE7;
            color: #fff;
            margin-top: 15px;
            margin-left: 10px;
        }
        .btn:hover{
            color:#3B8BE7 ;
            background-color: #fff;
            border: 1px solid #3B8BE7;
        }

    </style>
</head>
<body style="width: 100%;height: 100%">
<div class="topTitle" style="background-color: rgba(248,248,248,1); width: 97%;border: 1px solid rgba(238,238,238,1);height: 100px;margin: 0 auto;margin-top: 30px;display: flex;justify-content: center;align-items: center;">
    <div style="display: flex;flex-direction: row;align-items: center;">
        <div class=" otherStep ">01</div>
        <div>&nbsp;填写机构信息</div>
    </div>
    <div style="width:25%;height: 2px;border: 1px solid rgba(238,238,238,1);background-color: rgba(238,238,238,1);"></div>

    <div style="display: flex;flex-direction: row;align-items: center;">
        <div class=" otherStep">02</div>
        <div>&nbsp;后台调用机构信息核查</div>
    </div>
    <div style="width:25%;height: 2px;border: 1px solid rgba(238,238,238,1);background-color: rgba(238,238,238,1);"></div>
    <div class="" style="display: flex;flex-direction: row;align-items: center;">
        <div class=" activeStep" >03</div>
        <div>&nbsp;人工回访，完成认证</div>
    </div>
</div>
<div class="content" style=" width: 60% ;margin-left:20%;height: 120px;margin-top: 40px;display: flex;flex-direction:column;justify-content: center;align-items: center;">
    <div class="topContent">
        <img src="<l:asset path='img/register/1-4.png'/>">
        <span>您提交的认证文件，后台正在核对，预计2-3个工作日反馈结果，请耐心等候。</span>
    </div>
    <div style="margin-top: 10px">
        <button class="btn closeBtn">关闭</button>
        <button class="btn backBtn">返回</button>
    </div>
</div>
</body>
</html>