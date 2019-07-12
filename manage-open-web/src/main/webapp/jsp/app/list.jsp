<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>应用管理列表</title>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>


</head>
<body>
<div class="topdist"></div>
<div class="container" style="width: 96%; padding-top:10px;">
    <div class="row">
        <form class="form-inline" onsubmit="return false;">

            <div class="input-group">
                <input class="form-control ue-form" autocomplete="off" type="text" id="tableName" placeholder="请输入应用名称"/>
                <div class="input-group-addon ue-form-btn" onclick="forQuery()">
                    <span class="fa fa-search"></span>
                </div>
            </div>

            <div class="btn-group pull-right">
                <button id="add" type="button" class="btn ue-btn">
                    <span class="fa fa-plus"></span>创建应用
                </button>

            </div>
        </form>
    </div>
    <div class="row">
        <table id="appList" class="table table-bordered table-hover">
            <thead>
            <tr>
                <th width="5%" data-field="appId" data-render="renderid">序号</th>
                <th width="25%" data-field="appName" data-render="renderName">应用名称</th>
                <th width="25%" data-field="appDescription">描述</th>

                <th width="25%" data-field="appCreateTime">创建时间</th>

                <th width="20%" data-field="appId" data-render="renderoptions">操作</th>
            </tr>
            </thead>
        </table>
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
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>

<script type="text/javascript">
    var context = "<l:assetcontext/>";
    var url = context + "/service/service/app/list/getInstances" ;
    $(document).ready(function () {
        var options = {
            ordering: false
        };
        grid = new L.FlexGrid("appList", url);
        grid.init(options); //初始化datatable

        // 增加
        $("#add").bind("click", add);

    });



    function del(appId) {
        $.dialog({
            type: 'confirm',
            content: '确认删除该记录?',
            autofocus: true,
            ok: function () {
                $.ajax({
                    url: context + "/service/service/app/delete/" + appId,
                    success: function (msg) {
                        if (msg == true) {
                            reloadAppList();
                            sticky("删除成功！");
                        } else {
                            sticky("删除失败", 'error', 'center');
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    }
    function forQuery() {
        var param = {
            tableName: $("#tableName").val()

        };
        grid.reload(url, param);

    }
    function forView(appId) {
        window.location.href = context + "/service/service/app/getInfo/" + appId;
    }

    function reloadAppList() {
        // 重新请求数据
        $("#appList").DataTable().ajax.reload();
    }

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

    function add() {
        $.dialog({
            type: "iframe",
            title: "创建应用",
            url: context + '/jsp/app/add.jsp',
            width: 500,
            height: 300,
            onclose: function () {
                reloadAppList();
                if (this.returnValue) {
                    sticky("保存数据成功！");
                }
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
                    reloadAppList();
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

    function renderName(data, type, full) {
        var html = '';
        html += '<a onclick="forView(\''+ full.appId +'\')">'+ data + '</a>&emsp;';
        return html;
    }

    function renderoptions(data, type, full) {
        var html = "<div>";

        html += '<a onclick="modify(\'' + data + '\')">编辑</a>&emsp; ' +
            '<a class="del" onclick="del(\'' + data + '\')">删除</a></div>';
        return html;
    }

</script>
</html>