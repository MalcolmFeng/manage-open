<%@ page import="net.sf.json.JSONObject" %>
<%@ page import="com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor" %>
<%@ page import="java.util.Iterator" %>
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
    <title>测试Api-定义</title>
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
            margin-left: 5px;
        }

        .modify {
            border: 1px solid lightgrey;
            background-color: #F7F7F7;
            padding: 5px;
        }

        .ue-tabs {
            margin-bottom: 8px;
            border-bottom: 0;
        }

        .ue-tabs > li > a {
            font-size: 12px;
            padding: 0 25px;
            line-height: 30px;
            border-radius: 0;
            border: 1px solid lightgray;
        }

        .ue-tabs > li.active > a, .ue-tabs > li.active > a:focus, .ue-tabs > li.active > a:hover {
            height: 32px;
            color: white;
            border: 1px solid #ddd;
            border-radius: 0;
            background-color: #3e99ff;
        }

        .console-bottom {
            margin-bottom: 16px;
        }

        .param {
            background-color: #F4F5F9;
        }

        .table tbody tr:hover {
            background-color: #F4F5F9;
        }

        table.dataTable thead > tr {
            background-color: #F4F5F9;
        }

        table.dataTable thead > tr > th {
            font-weight: normal;
        }

        table.param tbody td {
            border-left-width: 0;
            border-bottom-width: 1px;
        }

        .outparam .col-md-2 {
            width: 12.5%;
        }

    </style>

</head>
<body>
<div class="content">

    <div class="div1">
        <span>|</span>API调用详情
    </div>

    <div class="div2 " id="back"><span class="fa fa-reply"></span><a onclick="history.back(-1);">返回</a></div>


    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">名称及描述</div>
        </div>
        <table id="table1_id" class="console-panel-body">
            <tbody>
            <tr>
                <td width="50%" class="ng-binding"><span class="console-grey ng-binding">API服务ID：</span><span
                        id="apiServiceId">
                    <c:if test="${not empty apiServiceMonitor.apiServiceId }">${apiServiceMonitor.apiServiceId}</c:if>
                    <c:if test="${empty apiServiceMonitor.apiServiceId }">-</c:if>
                </span></td>
                <td class="ng-binding"><span class="console-grey ng-binding">调用者ID：</span>
                    <c:if test="${not empty apiServiceMonitor.callerUserId }">${apiServiceMonitor.callerUserId}</c:if>
                    <c:if test="${empty apiServiceMonitor.callerUserId }">-</c:if>
                </td>
            </tr>

            <tr>
                <td width="50%" class="ng-binding"><span class="console-grey ng-binding">API服务名称：</span><span
                        id="appDescription" class="ng-binding">
                    <c:if test="${not empty apiServiceMonitor.apiServiceName }">${apiServiceMonitor.apiServiceName}</c:if>
                    <c:if test="${empty apiServiceMonitor.apiServiceName }">-</c:if>
                </span>
                <td class="ng-binding"><span class="console-grey ng-binding">调用状态：</span>
                    <c:if test="${apiServiceMonitor.result eq '200' }">成功</c:if>
                    <c:if test="${!(apiServiceMonitor.result eq '200') }">失败，${apiServiceMonitor.notes}</c:if>
                </td>
                </td>
            </tr>


            </tbody>
        </table>
    </div>

    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">请求消息</div>
        </div>
        <table id="table2_id" class="console-panel-body">
            <tbody>
            <tr>
                <td class="ng-binding"><span class="console-grey ng-binding">请求时间：</span><span>
                    <c:if test="${not empty apiServiceMonitor.requestTime }">${apiServiceMonitor.requestTime}</c:if>
                    <c:if test="${empty apiServiceMonitor.requestTime }">-</c:if>
                </span></td>
                <td class="ng-binding"><span class="console-grey ng-binding">请求者IP：</span><span>
                    <c:if test="${not empty apiServiceMonitor.callerIp }">${apiServiceMonitor.callerIp}</c:if>
                    <c:if test="${empty apiServiceMonitor.callerIp }">-</c:if>
                </span></td>
                <td class="ng-binding"><span class="console-grey ng-binding">HTTP Method：</span><span>
                    <c:if test="${not empty apiServiceMonitor.openServiceMethod }">${apiServiceMonitor.openServiceMethod}</c:if>
                    <c:if test="${empty apiServiceMonitor.openServiceMethod }">-</c:if>
                </span></td>
            </tr>
            <tr>
                <td colspan="3" class="ng-binding"><span class="console-grey ng-binding">HTTP Header：</span><span
                        id="httpHeader"></span></td>
            </tr>
            <%
                ApiServiceMonitor apiServiceMonitor = (ApiServiceMonitor) request.getAttribute("apiServiceMonitor");
                String header = apiServiceMonitor.getOpenServiceInputHeader();
                if (header != null) {
                    JSONObject jsonObject = JSONObject.fromObject(header);
                    Iterator<String> it = jsonObject.keys();
                    while (it.hasNext()) {
                        String key = it.next();
            %>
            <tr>
                <td colspan="3" class="ng-binding"><span
                        class="console-grey ng-binding"><%=key%>：</span><span><%=jsonObject.getString(key)%></span></td>
            </tr>
            <%
                }
            } else {
            %>
            <td colspan="3" class="ng-binding"><span class="console-grey ng-binding">暂无数据</span></td>
            <%
                }
            %>

            </tbody>
        </table>
    </div>

    <div class="console-panel console-bottom">
        <div class="console-panel-header" style="margin-bottom: -6px; ">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">请求入参信息</div>
        </div>

        <div>
            <div class="table_row">
                <ul class="list-group">
                    <li class="list-group-item">
                        <div class="col-md-2">参数名</div>
                        <div class="col-md-2">参数值</div>
                        <div class="clearfix"></div>
                    </li>
                    <c:if test="${not empty inputParam}">
                        <c:forEach items="${inputParam}" var="item">
                            <li class="list-group-item">
                                <div class="col-md-2">${item.name }</div>
                                <div class="col-md-2">${item.value }</div>
                                <div class="clearfix"></div>
                            </li>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty inputParam }">
                        <li class="list-group-item">
                            <div>无入参信息</div>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>

    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">请求出参信息</div>
        </div>
        <table class="console-panel-body">
            <tbody>
            <tr>
                <td class="ng-binding"><span class="console-grey ng-binding">响应时间：</span><span>
                    <c:if test="${not empty apiServiceMonitor.responseTime }">${apiServiceMonitor.responseTime}</c:if>
                    <c:if test="${empty apiServiceMonitor.responseTime }">-</c:if>
                </span></td>
            </tr>
            <tr>
                <td class="ng-binding"><span class="console-grey ng-binding">出参信息：</span><span>
                    <c:if test="${not empty apiServiceMonitor.openServiceOutput }">${apiServiceMonitor.openServiceOutput}</c:if>
                    <c:if test="${empty apiServiceMonitor.openServiceOutput }">-</c:if>
                </span></td>
            </tr>
            </tbody>
        </table>
    </div>


    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">开放平台调用服务的请求消息</div>
        </div>
        <table class="console-panel-body">
            <tbody>
            <tr>
                <td class="ng-binding"><span
                        class="console-grey ng-binding">HTTP Method：</span><span>${apiServiceMonitor.serviceMethod}</span>
                </td>
            </tr>
            <tr>
                <td colspan="3" class="ng-binding"><span
                        class="console-grey ng-binding">HTTP Header：</span><span></span></td>
            </tr>
            <%
                String serviceHeader = apiServiceMonitor.getOpenServiceInputHeader();
                if (serviceHeader != null) {
                    JSONObject jsonObject = JSONObject.fromObject(serviceHeader);
                    Iterator<String> it = jsonObject.keys();
                    while (it.hasNext()) {
                        String key = it.next();
            %>
            <tr>
                <td colspan="3" class="ng-binding"><span
                        class="console-grey ng-binding"><%=key%>：</span><span><%=jsonObject.getString(key)%></span></td>
            </tr>
            <%
                }
            } else {
            %>
            <td colspan="3" class="ng-binding"><span class="console-grey ng-binding">暂无数据</span></td>
            <%
                }
            %>

            </tbody>
        </table>
    </div>

    <div class="console-panel console-bottom">
        <div class="console-panel-header" style="margin-bottom: -6px; ">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">开放平台调用服务的入参信息</div>
        </div>

        <div>
            <div class="table_row">
                <ul class="list-group">
                    <li class="list-group-item">
                        <div class="col-md-2">参数名</div>
                        <div class="col-md-2">参数值</div>
                        <div class="clearfix"></div>
                    </li>
                    <c:if test="${not empty serviceInputsParam}">
                        <c:forEach items="${serviceInputsParam}" var="item">
                            <li class="list-group-item">
                                <div class="col-md-2">${item.name }</div>
                                <div class="col-md-2">${item.value }</div>
                                <div class="clearfix"></div>
                            </li>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty serviceInputsParam }">
                        <li class="list-group-item">
                            <div>无入参信息</div>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>

    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">开放平台调用服务的出参信息</div>
        </div>
        <table class="console-panel-body">
            <tbody>
            <tr>
                <td class="ng-binding"><span class="console-grey ng-binding">接口调用时长：</span><span>
                    <c:if test="${not empty apiServiceMonitor.serviceTotalTime }">${apiServiceMonitor.serviceTotalTime} 毫秒</c:if>
                    <c:if test="${empty apiServiceMonitor.serviceTotalTime }">-</c:if>
                </span></td>
            </tr>
            <tr>
                <td class="ng-binding"><span class="console-grey ng-binding">出参信息：</span><span>
                    <c:if test="${not empty apiServiceMonitor.serviceOutput }">${apiServiceMonitor.serviceOutput}</c:if>
                    <c:if test="${empty apiServiceMonitor.serviceOutput }">-</c:if>
                </span></td>
            </tr>
            </tbody>
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
<script type="text/javascript" src="<l:asset path='jquery.form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>

<script type="text/javascript">
    var context = "<l:assetcontext/>";
    var id = '${apiServiceMonitor.id}';
    $(function () {
    });


    function renderid(data, type, full, meta) {
        var rowId = meta.settings._iDisplayStart + meta.row + 1;
        return rowId;
    }


</script>
</html>
