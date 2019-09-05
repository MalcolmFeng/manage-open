<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>IP黑白名单</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ztree.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/datadev.css'/>"/>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
    <script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='form.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ztree.js'/>"></script>
    <script type="text/javascript">
        var context = "<l:assetcontext/>";
        var url = context + "/service/open/iplist/getIpBlacklist";
        var grid;
        $(document).ready(function () {
            var options = {
                ordering: false
            };


            grid = new L.FlexGrid("IpBlacklist", url);
            grid.init(options); //初始化datatable
        });

        function forEdit(openServiceId) {
            window.location.href = context + "/service/open/iplist/edit/" + openServiceId;
        }

        function forCreate() {
            window.location.href = context + "/service/open/iplist/create";
        }

        function forQuery() {
            var ipV4 = $("#ipV4").val();
            var ipV6 = $("#ipV6").val();
            var active = $("#active").val();
            var provider = $("#provider").val();
            var type = $("#type option:selected").val();
            var param = {
                ipV4: ipV4,
                ipV6: ipV6,
                active: active,
                provider: provider,
                type: type
            };
            grid.reload(url, param);
        }

        function forDel(id) {
            $.dialog({
                type: 'confirm',
                content: '确认删除该申请吗?',
                autofocus: true,
                ok: function () {
                    $.ajax({
                        url: context + "/service/open/iplist/delete/" + id,
                        success: function (msg) {
                            if (msg == true) {
                                forQuery();
                                sticky("删除成功！", "success");
                            } else {
                                sticky("删除失败", 'error');
                            }
                        }
                    });
                },
                cancel: function () {
                }
            });
        }

        function reloadIpBlacklist() {
            // 重新请求数据
            $("#IpBlacklist").DataTable().ajax.reload();
        }

        //弹窗提示样式
        function sticky(msg, style, position) {
            var type = style ? style : 'success';
            var place = position ? position : 'center';
            $.sticky(msg, {
                autoclose: 1000,
                position: place,
                style: type
            });
        }

        function forResetType(id, type) {
            var content = type == "white" ? "确认将IP地址加入白名单？" : "确认将IP地址加入黑名单？";
            $.dialog({
                type: 'confirm',
                content: content,
                autofocus: true,
                ok: function () {
                    $.ajax({
                        type: 'POST',
                        contentType: 'application/json;charset=UTF-8',
                        url: context + "/service/open/iplist/updateIpList/",
                        data: JSON.stringify({
                            id: id,
                            type: type
                        }),
                        success: function (resp) {
                            $.dialog({
                                autofocus: true,
                                type: "alert",
                                content: "成功!"
                            });
                            reloadIpBlacklist();
                        }, error: function (resp) {
                            $.dialog({
                                autofocus: true,
                                type: "alert",
                                content: "失败!"
                            });

                        }
                    });
                },
                cancel: function () {
                }
            });
        }

        function forResetActive(id, active) {
            var content = active == "false" ? "确认暂停生效？" : "确认恢复生效？";
            $.dialog({
                type: 'confirm',
                content: content,
                autofocus: true,
                ok: function () {
                    $.ajax({
                        type: 'POST',
                        contentType: 'application/json;charset=UTF-8',
                        url: context + "/service/open/iplist/updateIpList/",
                        data: JSON.stringify(
                            {
                                "id": id,
                                "active": active
                            }
                        ),
                        success: function (resp) {
                            $.dialog({
                                autofocus: true,
                                type: "alert",
                                content: "成功!"
                            });
                            reloadIpBlacklist();
                        }, error: function (resp) {
                            $.dialog({
                                autofocus: true,
                                type: "alert",
                                content: "失败!"
                            });

                        }
                    });
                },
                cancel: function () {
                }
            });
        }

        function renderId(data, type, full, meta) {
            var rowId = meta.settings._iDisplayStart + meta.row + 1;
            return rowId;
        }

        function renderType(data, type, full) {
            var html = '';
            if (data == 'black') {
                html += '<span class="fa fa-ban text-field">&nbsp;黑名单&emsp;</span>';
            } else if (data == 'white') {
                html += '<span class="fa fa-check-circle text-success">&nbsp;白名单&emsp;</span>';
            } else if (data == 'false') {
                html += '<span class="fa fa-times-circle text-warning">&nbsp;暂停生效</span>';
            } else if (data == 'true') {
                html += '<span class="fa fa-check-circle text-success">&nbsp;已生效</span>';
            } else {
                html += '<span class="fa fa-times-circle text-danger">&nbsp;</span>';
            }
            return html;
        }

        function renderOptions(data, type, full) {
            var html = "<div>";
            if (full.type == 'black') {
                html += '<a onclick="forResetType(\'' + full.id + '\',\'' + "white" + '\')">加入白名单</a>&nbsp;&nbsp;';
            } else if (full.type == 'white') {
                html += '<a onclick="forResetType(\'' + full.id + '\',\'' + "black" + '\')">加入黑名单</a>&nbsp;&nbsp;';
            }
            if (full.active == 'true') {
                html += '<a onclick="forResetActive(\'' + full.id + '\',\'' + "false" + '\')">暂停生效</a>&nbsp;&nbsp;';
            } else if (full.active == 'false') {
                html += '<a onclick="forResetActive(\'' + full.id + '\',\'' + "true" + '\')">恢复生效</a>&nbsp;&nbsp;';
            }
            html += '<a onclick="forDel(\'' + full.id + '\')">删除</a>';
            html += '</div>';
            return html;
        }
    </script>
</head>
<body>
<div class="container" style="width: 98%; padding-top:10px;">
    <div class="row">
        <div class="btn-group pull-right">
            <button id="add" type="button" class="btn ue-btn" onclick="forCreate()">
                <span class="fa fa-plus"></span>添加黑白名单
            </button>
        </div>
        <hr style=" width:100%; display:inline-flex"/>
    </div>
    <div class="row">
        <form class="form-inline" onsubmit="return false;">
            <div class="input-group">
                <input class="form-control ue-form" type="text" id="ipV4" placeholder="请输入IP V4地址"/>
            </div>
            <div class="input-group">
                <input class="form-control ue-form" type="text" id="ipV6" placeholder="请输入IP V6地址"/>
                <div class="input-group-addon ue-form-btn" onclick="forQuery()">
                    <span class="fa fa-search"></span>
                </div>
            </div>

            <div class="pull-right">
                <span>IP名单类型:</span>
                <select id="type" class="form-control ue-form" onchange="forQuery()">
                    <option value="">全部</option>
                    <option value="black">黑名单</option>
                    <option value="white">白名单</option>
                </select>
            </div>
            <div class="pull-right">
                <span>IP是否生效:</span>
                <select id="active" class="form-control ue-form" onchange="forQuery()">
                    <option value="">全部</option>
                    <option value="false">暂停生效</option>
                    <option value="true">生效</option>
                </select>
            </div>
        </form>
    </div>
    <div class="row">
        <table id="IpBlacklist" class="table table-bordered table-hover">
            <thead>
            <tr>
                <th width="5%" data-field="id" data-render="renderId">序号</th>
                <th width="10%" data-field="type" data-render="renderType">黑白名单类型</th>
                <th width="20%" data-field="ipV4">IP V4地址</th>
                <th width="20%" data-field="ipV6">IP V6地址</th>
                <th width="10%" data-field="createTime">创建时间</th>
                <th width="10%" data-field="provider">创建人</th>
                <th width="10%" data-field="active" data-render="renderType">当前状态</th>
                <th width="15%" data-field="dtDataId" data-render="renderOptions">操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<script id="emptylist" type="text/html">
    <div class="data-empty">
        <span class="norecord"></span>
    </div>
</script>
</body>
</html>