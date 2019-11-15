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
    <title>API调用列表</title>

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
        $(document).ready(function () {
            var options = {
                info: true,
                ordering: false
            };

            var url = context + "/service/dev/monitor/getApiMonitorList";
            grid = new L.FlexGrid("myauthList", url);
            grid.init(options); //初始化datatable
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

        function forQuery() {
            var serviceName = $("#serviceName").val();
            var serviceApiResult = $("#serviceApiResult").val();
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var serviceCallerUserId = $("#serviceCallerUserId").val();
            var startDate;
            var endDate;
            if (startTime != "") {
                startDate = new Date(Date.parse(startTime.replace(/-/g, "/")));
                startTime = startTime.substr(0, 4) + "-" + startTime.substr(5, 2) + "-" + startTime.substr(8, 2);
            }
            if (endTime != "") {
                endDate = new Date(Date.parse(endTime.replace(/-/g, "/")));
                endTime = addDate(endTime, 1);
                endTime = endTime.substr(0, 4) + "-" + endTime.substr(5, 2) + "-" + endTime.substr(8, 2);
            }
            if (startTime != "" && endTime != "" && startDate > endDate) {
                G3.alert("提示", "起始时间不可大于终止时间!");
                return;
            }

            if (serviceName != null || serviceName != "") {
                grid.setParameter("apiServiceName", serviceName);
            }
            if (serviceApiResult != null || serviceApiResult != "") {
                grid.setParameter("result", serviceApiResult);
            }
            if (startTime != null || startTime != "") {
                grid.setParameter("startTime", startTime);
            }
            if (endTime != null || endTime != "") {
                grid.setParameter("endTime", endTime);
            }
            if (serviceCallerUserId != null || serviceCallerUserId != "") {
                grid.setParameter("callerUserId", serviceCallerUserId);
            }
            reloadApiList();
        }

        function forView(id) {
            window.location.href = context + "/service/dev/monitor/getInfo/" + id;
        }

        function toDetails(id) {
            window.location.href = context + "/service/dev/monitor/getDetail/" + id;
        }

        function reloadApiList() {
            // 重新请求数据
            $("#myauthList").DataTable().ajax.reload();
        }

        function renderId(data, type, full, meta) {
            var rowId = meta.settings._iDisplayStart + meta.row + 1;
            return rowId;
        }

        function renderName(data, type, full) {
            var html = '';
            if (data != null) {
                if (full.result == "200") {
                    html += '<a onclick="forView(\'' + full.id + '\')">' + data + '</a>&emsp;';
                } else {
                    html += '<a style="color: red" onclick="forView(\'' + full.id + '\')">' + data + '</a>&emsp;';
                }
            } else {
                html += '<a style="color: red" onclick="forView(\'' + full.id + '\')">' + "调用接口失败" + '</a>&emsp;';
            }
            return html;
        }

        function renderStatus(data, type, full) {
            if ("200" == full.result) {
                if("b983cc8ee7814d2d9d4702fa08f230e7" == full.apiServiceId){
                    return '<a onclick="toDetails(\'' + full.id + '\')">' + '成功' + '</a>';
                } else if ("b983c3452346456v224562fa08f230e4" == full.apiServiceId) {
                    return '<a onclick="toDetails(\'' + full.id + '\')">' + '成功' + '</a>';
                }else {
                    return "成功";
                }
            } else if ("10001" == full.result) {
                return "API分组错误";
            } else if ("10002" == full.result) {
                return "API服务不存在";
            } else if ("10003" == full.result) {
                return "API服务当前状态不可用";
            } else if ("10004" == full.result) {
                return "查询授权应用异常";
            } else if ("10005" == full.result) {
                return "API未授权应用";
            } else if ("10006" == full.result) {
                return "验证签名不正确";
            } else if ("10007" == full.result) {
                return "账户余额不足";
            } else if ("10008" == full.result) {
                return "输入参数异常";
            } else if ("99999" == full.result) {
                return "未知错误";
            }else if ("20202" == full.result) {
                return "IP地址没有访问权限";
            } else if ("20302" == full.result) {
                return "请求过于频繁";
            } else {
                return "未知系统错误";
            }
        }

        function renderPath(data, type, full) {
            var html = '';
            if (data != null) {
                var url = data;
                if (url.indexOf('?') > 0) {
                    url = url.split('?')[0];
                }
                var f = url.substr(url.indexOf('/api/execute/do') + 15);

                if (full.result == "200") {
                    html += '<a onclick="forView(\'' + full.id + '\')">' + f + '</a>&emsp;';
                } else {
                    html += '<a style="color: red" onclick="forView(\'' + full.id + '\')">' + f + '</a>&emsp;';
                }
            } else {
                html += '<a style="color: red" onclick="forView(\'' + full.id + '\')">' + "——" + '</a>&emsp;';
            }
            return html;
        }

        /**
         * 时间选择框
         */
        function selectCalendar(id) {
            $('#startTime').datetimepicker({
                minView: "month",
                format: "yyyy-mm-dd",
                language: 'zh-CN',
                autoclose: true,
                endDate: new Date()
            });
            $('#endTime').datetimepicker({
                minView: "month",
                format: "yyyy-mm-dd",
                language: 'zh-CN',
                autoclose: true,
                endDate: new Date()
            });
            $("#" + id).trigger("focus");
            if (id == "startTime") {
                $('#startTime').datetimepicker().on('hide', function () {
                    if ($("#startTime").val() != "") {
                        $('#endTime').datetimepicker('setStartDate', $("#startTime").val());
                    }

                });
            } else {
                $('#endTime').datetimepicker().on('hide', function () {
                    if ($("#endTime").val() != "") {
                        $('#startTime').datetimepicker('setEndDate', $("#endTime").val());
                    }

                });
            }
        }

        function addDate(dateStr, days) {
            var date = new Date(Date.parse(dateStr.replace(/-/g, "/")));
            var now = date.valueOf();
            now = now + days * 24 * 60 * 60 * 1000;

            var resultDate = new Date(now);
            var y = resultDate.getFullYear();
            var m = resultDate.getMonth() + 1;
            if (m.toString().length == 1) {
                m = '0' + m;
            }

            var d = resultDate.getDate();
            if (d.toString().length == 1) {
                d = '0' + d;
            }

            return y + "-" + m + "-" + d;
        }

    </script>
</head>
<body>
<div class="topdist"></div>
<div class="container" style="width: 98%; padding-top:10px;">
    <div class="row">
        <form class="form-inline" onsubmit="return false;">
            <div class="input-group" style="width: 100%;">
                <table id="queryTable" style="width: 100%;">
                    <tr>
                        <th width="20%"><input class="form-control" type="text" id="serviceName"
                                               placeholder="请输入api名称"/></th>
                        <th width="20%">
                            <select id="serviceApiResult" class="form-control">
                                <option value="">请选择调用状态</option>
                                <option value="">全部</option>
                                <option value="200">成功</option>
                                <%--                                <option value="10001">API分组错误</option>--%>
                                <option value="10002">API服务不存在</option>
                                <option value="10003">API服务当前状态不可用</option>
                                <option value="10004">查询授权应用异常</option>
                                <option value="10005">API未授权应用</option>
                                <option value="10006">验证签名不正确</option>
                                <option value="10007">账户余额不足</option>
                                <option value="10008">输入参数异常</option>
                                <option value="99999">未知错误</option>
                            </select></th>
                        <th width="20%">
                            <div class="input-group" id="startTimeDiv" onclick="selectCalendar('startTime');"
                                 style="display: inline-table;">
                                <%--							<input type="text" class="form-control" id="startTime" readonly="readonly" style="display: inline-block; width: 100%;" placeholder="请输入开始时间"/>--%>
                                <input type="text" class="form-control" id="startTime"
                                       style="display: inline-block; width: 100%;" placeholder="请输入开始时间"/>
                            </div>
                        </th>
                        <th width="20%">
                            <div class="input-group" id="endTimeDiv" onclick="selectCalendar('endTime');"
                                 style="display: inline-table;">
                                <%--							<input type="text" class="form-control" id="endTime" readonly="readonly" style="display: inline-block; width: 100%;" placeholder="请输入结束时间"/>--%>
                                <input type="text" class="form-control" id="endTime"
                                       style="display: inline-block; width: 100%;" placeholder="请输入结束时间"/>
                            </div>
                        </th>
                        <th width="20%"><input class="form-control" type="text" id="serviceCallerUserId"
                                               placeholder="请输入调用者ID"/></th>
                    </tr>
                </table>
                <div class="input-group-addon ue-form-btn" onclick="forQuery()">
                    <span class="fa fa-search"></span>
                </div>
            </div>
            <div class="btn-group pull-right">
            </div>
        </form>
    </div>
    <div class="row">
        <table id="myauthList" class="table table-bordered table-hover">
            <thead>
            <tr>
                <th width="2%" data-field="rowId" data-render="renderId">序号</th>
                <th width="25%" data-field="openServiceRequestURL" data-render="renderPath">调用地址</th>
                <th width="15%" data-field="apiServiceName" data-render="renderName">API名称</th>
                <th width="8%" data-field="auditStatus" data-render="renderStatus">状态</th>
                <th width="10%" data-field="callerUserId">调用者</th>
                <th width="10%" data-field="callerIp">调用者IP</th>
                <th width="15%" data-field="requestTime">调用时间</th>
                <th width="15%" data-field="responseTime">响应时间</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>
</html>
