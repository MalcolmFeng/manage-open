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
    <title>机构认证</title>

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
    .registerDiv{
       border: 1px solid #eee;
        position: relative;
        margin-top: 30px;
    }
    .registerDiv img{
        margin-left: 30px;
        margin-top: 30px;
    }
    .registerTitle{
        font-size: 18px;
        font-weight: bold;
        margin-top: 25px;
        color: #333333;
    }
    .registerDiv>div>div{
        margin-left: 15px;
    }
    .registerContent{
        color: #666;
        margin-top: 25px;
    }
    .btn{
        position: absolute;
        top: 50%;
        margin-left: 88%;
        width: 80px;
        height: 30px;
        background-color: #3B8BE7;
        color: #fff;
        margin-top: -15px;

    }
    .btn:hover{
        color:#3B8BE7 ;
        background-color: #fff;
        border: 1px solid #3B8BE7;
    }
        .contentDiv{
            width: 70%;
        }
        .thirdContent{
            margin-top: 20px;
        }

</style>
</head>
<body style="width: 100%;height: 100%">
<div style="height: 25px"></div>
 <div style="margin-left: 10%">机构认证:</div>
    <div style="width: 100%;height: 100%;display: flex;flex-direction: column;">
        <div class ="registerDiv firstStep" style="width:80%;margin-left:10%;height: 120px;display: flex;flex-direction: row;">
            <div>
                <img src="<l:asset path='img/register/1.png'/>">
            </div>
            <div class="contentDiv">
                <div class="registerTitle">省平台机构认证</div>
                <div class="registerContent">①完善机构信息 >  ②后台调用接口 >  ③人工确认，完成认证</div>
            </div>
            <button class="btn" id="firstBtn">立即认证</button>
        </div>
        <div class ="registerDiv firstStep" style="width:80%;margin-left:10%;height: 120px;display: flex;flex-direction: row;">
            <div>
                <img src="<l:asset path='img/register/1-2.png'/>">
            </div>
            <div class="contentDiv">
                <div class="registerTitle">书面协议和授权书认证</div>
                <div class="registerContent">①完善机构信息 >  ②上传书面协议或授权书文件 >  ③系统核对，完成认证</div>
            </div>
            <button class="btn" id="secondBtn">立即认证</button>
        </div>
        <div class ="registerDiv firstStep" style="width:80%;margin-left:10%;height: 120px;display: flex;flex-direction: row;">
            <div>
                <img src="<l:asset path='img/register/1--3.png'/>">
            </div>
            <div class="contentDiv">
                <div class="registerTitle">对公账号认证</div>
                <div class="registerContent thirdContent">①完善机构信息，填写机构对公账号>  ②我方向对公账号打款（2-3个工作日）>  ④收到打款后，输入打款金额 >  ⑤验证输入金额，完成认证</div>
            </div>
            <button class="btn" id="thirdBtn">立即认证</button>
        </div>
    </div>
</body>
<script>
    $(document).on("change", "#firstBtn", function(){
        window.location.href = context + "/jsp/data/data/"

    });
    $(document).on("change", "#secondBtn", function(){
        window.location.href = context + "/jsp/data/data/"

    });
    $(document).on("change", "#thirdBtn", function(){
        window.location.href = context + "/jsp/data/data/"

    });
</script>

</html>