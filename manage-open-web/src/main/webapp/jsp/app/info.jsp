<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>应用详情</title>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
    <style type="text/css">
        .Validform_input {
            width: 85%;
        }

        .Validform_right {
            width: 10%;
        }

        .appDescription {
            width: 90%;
            overflow-x: hidden
        }

        .control-label {
            text-align: right;
        }

        .content {
            margin: 20px 14px 14px 14px;
            padding: 10px;
        }

        .console-panel-header {
            position: relative;
            background: #F4F5F9;
            border: 1px solid #E1E6EB;
            border-bottom: none;
            padding: 0 12px 0 16px;
            font-size: 14px;
            height: 40px;
            line-height: 38px;
        }

        table.console-panel-body {
            width: 100%;
            border: none;
        }

        .console-panel-body {
            border: 1px solid #E1E6EB;
            background: #FFF;
        }

        table {
            border-collapse: collapse;
            border-spacing: 0;
        }

        .console-panel-header-line {
            display: block;
            position: absolute;
            left: -1px;
            top: -1px;
            height: 41px;
            width: 3px;
            overflow: hidden;
            background: #778;
            font-size: 0;
            line-height: 0;
        }

        .console-float-left {
            float: left !important;
        }

        .console-float-right {
            float: right !important;
        }

        .console-button-wrap {
            display: inline-block;
            cursor: pointer;
        }

        table.console-panel-body tr td {
            background: #FFF;
            border: 1px solid #E1E6EB;
            padding: 16px;
            vertical-align: middle;
            line-height: 140%;
        }

        .console-grey {
            color: #999 !important;
        }

        .console-button-tiny {
            font-size: 12px;
            padding: 0px 8px;
            line-height: 18px;
            height: 20px;
        }

        .console-button:link, .console-button:visited {
            color: #333;
        }

        .console-button {
            width: 100%;
            text-align: center;
            display: inline-block;
            border: 1px solid #DDD;
            background: #F7F7F7;
            padding: 0px 16px;
            cursor: pointer;
            height: 32px;
            line-height: 30px;
            text-decoration: none;
            color: #333;
            font-size: 12px;
        }

        .console-button-tiny span, .console-button-tiny i {
            line-height: 18px;
        }

        table.dataTable thead > tr {
            background-color: #f6f6f6;
        }

        .table-bordered > tbody > tr > td, .table-bordered > thead > tr > th {
            text-align: center;
        }

        .console-button-wrap {
            display: inline-block;
            cursor: pointer;
        }

        #btn1 {
            height: 25px;
            line-height: 25px;
        }

        #pwd {
            border: none;
            outline: medium;
        }

        .div1 {
            margin-right: 5px;
            margin-bottom: 14px;
            display: inline-block;
        }

        .div1 > span {
            margin-right: 5px;
            font-weight: bolder;
            color: blue;
        }

        .div2 {
            box-sizing: border-box;
            display: inline-block;
            border: 1px solid lightgrey;
            background-color: #F7F7F7;
        }
        .modify{
            border: 1px solid lightgrey;
            background-color: #F7F7F7;
            padding: 5px;
        }
        .ue-tabs{
            margin-bottom: 8px;
            border-bottom: 0;
        }
        .ue-tabs>li>a {
            font-size: 12px;
            padding: 0 25px;
            line-height: 30px;
            border-radius: 0;
            border: 1px solid lightgray;
        }
        .ue-tabs>li.active>a, .ue-tabs>li.active>a:focus, .ue-tabs>li.active>a:hover {
            height: 32px;
            color: white;
            border: 1px solid #ddd;
            border-radius: 0;
            background-color: #3e99ff;
        }
        .appKey tbody tr:hover{
            background-color: white;
        }
    </style>

</head>
<body>
<div class="content">

    <div class="div1">
        <span>|</span>应用详情
    </div>

    <div class="div2 " id="back"><span class="fa fa-reply"></span><a>返回应用列表</a></div>


    <div class="console-panel" style="margin-bottom:20px;">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">基本信息</div>
            <div class="console-float-right">
                <span id="modify" class="modify"><a onclick="modify('${app.appId}')">修改</a></span>
            </div>
        </div>
        <table id="table_id" class="console-panel-body">
            <tbody>
            <tr>
                <td class="ng-binding"><span class="console-grey ng-binding">应用名称：</span><span id="appName">${app.appName}</span></td>
                <td colspan="2" class="ng-binding"><span class="console-grey ng-binding">应用ID：</span>${app.appId}</td>
            </tr>
            <tr>
                <td colspan="4"><span class="console-grey ng-binding">描述：</span><span id="appDescription"
                        class="ng-binding">${app.appDescription}</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div>
    <ul id="myTab" class="nav nav-tabs ue-tabs">
        <li class="active"><a href="#a1" data-toggle="tab">已授权的API</a></li>
        <li ><a  id="appKey" class="appKey" href="#a2" data-toggle="tab">AppKey</a></li>
    </ul>
    </div>

    <div id="myTabContent" class="tab-content">

        <div class="tab-pane active" id="a1">
            <div class="">
                <table id="AuthorizedApi" class="table table-bordered table-hover">
                    <thead>
                    <tr>
                        <th width="5%" data-field="apiId" data-render="renderid">序号</th>
                        <th width="25%" data-field="apiName" >Api名称</th>
                        <th width="10%" data-field="groupName">分组</th>
                        <th width="10%" data-field="eironment">环境</th>
                        <th width="10%" data-field="authUser">授权者</th>
                        <th width="10%" data-field="authTime">授权时间</th>
                        <th width="30%" data-field="apiId" data-render="renderoptions">操作</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>

        <div class="tab-pane" id="a2">
            <div class="">
                <table id="app_key" class="table table-bordered  dataTable appKey">
                    <thead>
                    <tr>
                        <th width="30%">AppKey</th>
                        <th width="30%" >AppSecret</th>
                        <th width="40%" >操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${app.appKey}</td>
                        <td><input class="input" type="password" value="${app.appSecret}" id="pwd">&nbsp;&nbsp;<input
                                type="button" value="显示"
                                id="btn1"/></td>
                        <td><a onclick="reset('${app.appId}')" >重置AppSecret</a></td>
                    </tr>

                    </tbody>

                </table>
            </div>

        </div>
    </div>



</div>
</body>

<!--[if lt IE 9]>
<script src="<l:asset path='html5shiv.js'/>"></script>
<script src="<l:asset path='respond.js'/>"></script>
<![endif]-->

<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script type="text/javascript" src="<l:asset path='jquery.form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>

<script type="text/javascript">
    var context = "<l:assetcontext/>";
    var appid="${app.appId}";
    var url = context + "/service/service/app/list/getAuthorizedApi/" +appid;
    $(function () {

        var options = {
            ordering: false
        };
        grid = new L.FlexGrid("AuthorizedApi", url);
        grid.init(options); //初始化datatable

        // 返回
        $("#back").bind("click", function () {
            window.location.href = context + "/jsp/app/list.jsp";
        });

        $('#myTab a').click(function(e) {
            e.preventDefault();
            $(this).tab('show');
        });

    });

    //弹窗提示样式
    function sticky(msg, style, position) {
        var type = style ? style : 'success';
        var place = position ? position : 'top';
        $.sticky(
            msg,
            {
                autoclose: 1000,
                position: place,
                style: type
            }
        );
    }

    function reset(appId) {
        $.dialog({
            type: 'confirm',
            content: '重置AppSecret，将会影响您的API调用，确认重置AppSecret?',
            autofocus: true,
            ok: function () {
                $.ajax({
                    url: context + "/service/service/app/reset/" + appId,
                    success: function (res) {
                        $("#pwd").val(res.appSecret);
                        sticky("重置成功！");
                    }
                });
            },
            cancel: function () {
            }
        });
    }

    function modify(appId) {
        if (typeof(appId) != 'undefined') {
            $.dialog({
                type: "iframe",
                title: "修改应用",
                url: context + '/service/service/app/edit/' + appId,
                width: 500,
                height:300,
                onclose: function () {
                    $.ajax({
                        url: context + "/service/service/app/get/" + appId,
                        success: function (res) {
                            $("#appName").html(res.appName);
                            $("#appDescription").html(res.appDescription);
                        }
                    });
                    if (this.returnValue) {
                        sticky("修改数据成功！");
                    }
                }
            });
        }
    }

  function renderid(data, type, full, meta) {
        var rowId = meta.settings._iDisplayStart + meta.row + 1;
        return rowId;
    }

  /*  function renderName(data, type, full) {
        var html = '';
        html += '<a onclick="forView(\''+ full.appId +'\')">'+ data + '</a>&emsp;';
        return html;
    }*/

    function renderoptions(data, type, full) {
        var html = "<div>";

        html += '<a onclick="viewApiInfo(\'' + data + '\')">查看Api详情</a>&emsp; ' +
            '<a class="del" onclick="testApi(\'' + data + '\')">调试Api</a></div>';
        return html;
    }
    function viewApiInfo(apiId) {
        window.location.href = context + "/service/open/api/getInfo/"+apiId;
    }
    function testApi(apiId) {
        window.location.href = context +"/service/api/execute/test/" + apiId;
    }

    //显示密码
    var pwd = document.getElementById("pwd");
    var btn1 = document.getElementById("btn1");
    btn1.onclick = function () {

        if (pwd.type == "password") {
            pwd.type = "text";
            btn1.value = "隐藏";
        } else {
            pwd.type = "password";
            btn1.value = "显示"
        }
    };

</script>
</html>