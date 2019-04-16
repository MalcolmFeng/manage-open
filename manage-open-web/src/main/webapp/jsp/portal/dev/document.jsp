<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html lang="zh-CN" style="height:100%">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>浪潮 Health Mall 开放平台-开发文档</title>

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
                <span class="title-ch">开发文档</span>
            </div>
            <div class="api-con-bottom">
                <div class="link-contents">
                    <div class="sub-title">
                        一、SDK使用方法
                    </div>
                    <div class="sub-con">
                        <p><b>1）引入平台开发SDK</b></p>
                        <p>针对java开发团队，开放服务平台提供了一套java版SDK，可用于服务的调用。引入步骤如下：</p>
                        <p>创建web应用工程</p>
                        <p>下载平台服务相关的SDK包</p>
                        <p>将SDK包拷贝到web应用工程的lib文件夹中</p>
                        <p>将上述jar包添加到web应用工程的 Java Build Path</p>
                        <p><b>2）修改配置文件</b></p>
                        <p>在WEB-INF/classes路径下添加config.properties文件</p>
                        <div style="background-color: #D9D9D9"><pre>
    ##应用id和sercert
    CLIENT_ID = d095c30000
    CLIENT_SECRET = 98e21fdf4ace4f57be52962a09ab3a2b</pre></div>
                        <p><b>3）调用API服务</b></p>
                        <p>调用API需要创建OpenServiceClient对象和参数实体RequestParams，必须传递的参数包括服务上下文（context）和服务版本（version），调用sendRequest ()方法即可调用平台提供的API服务。</p>
                        <div style="background-color: #D9D9D9"><pre>
    String context = "/weatherByName";
    String version = "1.0";
    OpenServiceClient client = new OpenServiceClient ();
    RequestParams openServiceParam = new RequestParams ();

    //如果需要用户授权的服务，需要添加ticket参数,ticket为爱健康app授权登录后返回的ticket
    openServiceParam.addParam ("ticket ", “1001010010001001010”);
    openServiceParam.setContext (context);
    openServiceParam.setVersion (version);
    String jsonObj = client.sendRequest (openServiceParam);</pre></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div style="clear: both"></div>
</body>
</html>