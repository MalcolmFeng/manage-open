<%@page contentType="text/html; charset=utf-8" %>
<%@taglib uri="/tags/loushang-web" prefix="l" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>数据检索平台 Data Search</title>

    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <style>
        body, .body {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            width: 100%;
            height: 100%;
        }
    </style>
    <!-- HTML5 shim 和 Respond.js 是为了让 IE8 支持 HTML5 元素和媒体查询（media queries）功能 -->
    <!-- 警告：通过 file:// 协议（就是直接将 html 页面拖拽到浏览器中）访问页面时 Respond.js 不起作用 -->
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
</head>
<body>
<div class="body">
    <iframe id="wf" name="wf" style="width: 100%;height: 100%;border: none"></iframe>
    <div class="row">
        <div class="col-md-12">
            <button class="btn ue-btn" onclick="submit()">提交</button>
        </div>
    </div>
</div>
<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript">
    var context = "<l:assetcontext/>";
    $(function () {
        $("#wf").attr("src","http://172.19.221.67:7070/open-bsp/command/dispatcher/org.loushang.workflow.tasklist.forward.TaskListDispatcherCmd/newTaskForward?procDefUniqueId=2c935dc36e67aeaf016e680d3aa80979")
    })
    function submit() {
        wf.window.createAndSend();
    }
</script>
</body>
</html>