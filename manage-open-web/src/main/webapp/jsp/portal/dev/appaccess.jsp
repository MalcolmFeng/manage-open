<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>浪潮 Health Mall 开放平台-应用接入</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='portal/css/dev/document.css'/>"/>
    <script type="text/javascript">
        //项目上下文
        var context = "<%=request.getContextPath()%>";
        //获取静态资源上下文路径
        var assetPath = "<l:assetcontext/>";
    </script>
</head>
<body style="height:100%;">
<div id="api-container" class="container col-xs-12 col-md-12" style="margin: 0px">
    <div class="row">
        <div id="api-contents">
            <div class="api-con-top">
                <span class="title-ch">应用接入</span>
            </div>
            <div class="api-con-bottom">
                <div class="link-contents">
                    <div class="sub-title">
                        1、注册成为开发者
                    </div>
                    <div class="sub-con">
                        <img src="<l:asset path='portal/img/appaccess/1.jpg'/>">
                        <p>点击“控制台”按钮进行登录或注册，并根据登录信息判断是否为开发者，若该账号不是开发者则打开如上图所示的开发者注册页面，按要求填写相关信息，完成注册。</p>
                    </div>
                    <div class="sub-title">
                        2、注册应用，等待审批
                    </div>
                    <div class="sub-con">
                        <img src="<l:asset path='portal/img/appaccess/2.jpg'/>">
                        <p>成为开发者后可在“应用管理”的子菜单中注册应用，点击列表页面右上角的“增加”按钮，弹出如上图所示的注册应用的对话框，按要求填写相应的注册信息，并点击“保存”按钮。</p>
                        <p>注册应用后，会在“应用注册”页面看见相关信息，此时的“审核状态”为“待审核”，请耐心等待管理员的审批。</p>
                    </div>
                    <div class="sub-title">
                        3、应用注册成功并且通过审核后可以为应用申请服务
                    </div>
                    <div class="sub-con">
                        <img src="<l:asset path='portal/img/appaccess/3.jpg'/>">
                        <p>应用注册完成并通过管理员的审核后，可在“API服务”中的“服务申请”页面为刚注册的应用申请服务，目前提供的服务有“登录”、“验证码”、“实名认证”等多种服务，点击服务右侧的“申请”按钮，弹出如上图所示的对话框，找到相应的应用点击“申请”，并等待管理员进行授权。</p>
                    </div>
                    <div class="sub-title">
                        4、服务申请审批通过之后查看服务详情
                    </div>
                    <div class="sub-con">
                        <img src="<l:asset path='portal/img/appaccess/4.jpg'/>">
                        <p>对已经提出服务申请的应用，可在“API服务”中的“我的申请”中查看相关服务详情，如上图所示，包括应用名称、服务名称、申请时间和授权状态等信息展示，也可点击操作列中的“查看”按钮，查询详细信息。</p>
                    </div>
                    <div class="sub-title">
                        5、申请并通过审核后，请线下完成应用开发
                    </div>
                    <div class="sub-con">
                        <p>服务申请完成并且管理员同意授权后，应在线下完成该应用的开发。</p>
                    </div>
                    <div class="sub-title">
                        6、应用开发完成发布应用上线
                    </div>
                    <div class="sub-con">
                        <img src="<l:asset path='portal/img/appaccess/6.jpg'/>">
                        <p>应用开发完成后，可在“应用管理”中的“应用发布”页面发布应用，找到该应用并点击“发布”按钮，弹出如上图所示的对话框，按要求填写相关版本发布信息并点击“保存”按钮，完成应用发布操作。</p>
                    </div>
                    <div class="sub-title">
                        7、可查看应用使用服务详情
                    </div>
                    <div class="sub-con">
                        <img src="<l:asset path='portal/img/appaccess/7.jpg'/>">
                        <p>最后，可在“应用管理”的“应用服务”页面中查看已经发布上线的应用所申请的服务名称、服务状态和调用次数等。</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>