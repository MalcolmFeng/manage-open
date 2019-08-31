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
        .outparam .col-md-2{
            width: 12.5%;
        }

    </style>

</head>
<body>
<div class="content">

    <div class="div1">
        <span>|</span>测试Api-定义
    </div>

    <div class="div2 " id="testOnline"><a href="javascript:void(0)" onclick="test();">在线测试</a></div>

    <div class="div2 " id="back"><span class="fa fa-reply"></span><a onclick="history.back(-1);">返回</a></div>


    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">名称及描述</div>
        </div>
        <table id="table1_id" class="console-panel-body">
            <tbody>
            <tr>
                <td width="50%" class="ng-binding"><span class="console-grey ng-binding">分组：</span><span
                        id="group">${groupName}</span></td>
                <td class="ng-binding"><span class="console-grey ng-binding">名称：</span>${serviceDef.name}</td>
            </tr>

            <tr>
                <td colspan="2"><span class="console-grey ng-binding">描述：</span><span id="appDescription"
                                                                                      class="ng-binding">${serviceDef.description}</span>
                </td>
            </tr>


            </tbody>
        </table>
    </div>

    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">请求基础定义</div>
        </div>
        <table id="table2_id" class="console-panel-body">
            <tbody>
            <tr>
                <td width="50%" class="ng-binding"><span class="console-grey ng-binding">Path：</span><span
                        id="path"> ${serviceDef.reqPath}</span></td>
                <td class="ng-binding"><span class="console-grey ng-binding">协议：</span><span
                        id="protocol">${serviceDef.protocol}</span></td>
            </tr>
            <tr>
                <td width="50%" class="ng-binding"><span class="console-grey ng-binding">HTTP Method：</span><span
                        id="httpmethod">${serviceDef.httpMethod}</span></td>
                <td class="ng-binding"><span class="console-grey ng-binding">加密类型：</span><span
                        id="encryptionType">
                    <c:if test="${serviceDef.encryptionType eq '0' }">未加密</c:if>
                    <c:if test="${!serviceDef.encryptionType eq '0' }">${serviceDef.encryptionType}</c:if>
                </span></td>
            </tr>
            <tr>
                <td width="50%" class="ng-binding"><span class="console-grey ng-binding">API限流：</span><span
                        id="encryptionType2"> ${serviceDef.limitCount} 次/秒</span></td>
                <%--                        id="encryptionType2"> ${serviceDef.maxQps} 次/秒</span></td>--%>
            </tr>

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
                        <div class="col-md-2">是否必填</div>
                        <div class="col-md-2">类型</div>
                        <div class="col-md-2">描述</div>
                        <div class="clearfix"></div>
                    </li>
                    <c:if test="${not empty inputParam}">
                        <c:forEach items="${inputParam}" var="item">
                            <li class="list-group-item">
                                <div class="col-md-2">${item.name }</div>
                                <div class="col-md-2">
                                    <c:if test="${item.required eq '0' }">否</c:if>
                                    <c:if test="${item.required eq '1' }">是</c:if>
                                </div>
                                <div class="col-md-2">
                                        ${item.type}
                                        <%--<c:choose>
                                            <c:when test="${item.type eq 'number'}">数值型</c:when>
                                            <c:otherwise>字符型</c:otherwise>
                                        </c:choose>--%>
                                </div>
                                <div class="col-md-2">${item.description }</div>
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
     <c:if test="${canViewBackEnd eq true}">
    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">后端服务信息</div>
        </div>
        <table id="table4_id" class="console-panel-body">
            <tbody>
            <tr>
                <td class="ng-binding" colspan="2"><span class="console-grey ng-binding">后端服务地址：</span>
                    <span>${serviceDef.scAddr}</span></td>
            </tr>
            <tr>
                <td class="ng-binding"><span class="console-grey ng-binding">后端服务协议：</span>
                    <span>${serviceDef.scProtocol}</span></td>
                <td class="ng-binding"><span class="console-grey ng-binding">HTTP Method：</span>
                    <span>${serviceDef.scHttpMethod}</span>
                </td>
            </tr>

            </tbody>
        </table>
    </div>

    <div class="console-panel console-bottom">
        <div class="console-panel-header" style="margin-bottom: -6px; ">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">后端服务参数</div>
        </div>
        <div>
       <div class="table_row outparam">
            <ul class="list-group">
                <li class="list-group-item">
                    <div class="col-md-2">后端参数名称</div>
                    <div class="col-md-2">后端参数类型</div>
                    <div class="col-md-2">后端参数位置</div>
                    <div class="col-md-2">是否必填</div>
                    <div class="col-md-2">排序</div>
                    <div class="col-md-2">后端参数描述</div>
                    <div class="col-md-2">对应入参名称</div>
                    <div class="col-md-2">对应入参类型</div>
                    <div class="clearfix"></div>
                </li>
                <c:if test="${not empty inputParam}">
                    <c:forEach items="${inputParam}" var="item">
                        <li class="list-group-item">
                            <div class="col-md-2">${item.scName }</div>
                            <div class="col-md-2">${item.scType }</div>
                            <div class="col-md-2">${item.scParamType }</div>
                            <div class="col-md-2">
                                <c:if test="${item.scRequired eq '0' }">否</c:if>
                                <c:if test="${item.scRequired eq '1' }">是</c:if>
                            </div>

                            <div class="col-md-2">${item.scSeq }</div>
                            <div class="col-md-2">${item.scDescription }</div>
                            <div class="col-md-2">${item.name }</div>
                            <div class="col-md-2">${item.type }</div>
                            <div class="clearfix"></div>
                        </li>
                    </c:forEach>
                </c:if>
                <c:if test="${empty inputParam }">
                    <li class="list-group-item">
                        <div>无后端参数信息</div>
                    </li>
                </c:if>
            </ul>
        </div>
        </div>

    </div>
     </c:if>

    <div class="console-panel console-bottom">
        <div class="console-panel-header">
            <span class="console-panel-header-line"></span>
            <div class="console-float-left ng-binding">返回结果</div>
        </div>
        <table id="table7_id" class="console-panel-body">
            <tbody>
            <tr>
                <td><span class="console-grey ng-binding">返回类型：</span>
                    <span id="31" class="ng-binding">${serviceDef.contentType}</span>
                </td>
            </tr>
            <tr>
                <td><span class="console-grey ng-binding">返回结果示例：</span>
                    <span id="32" class="ng-binding">${serviceDef.returnSample}</span>
                </td>
            </tr>
            <%--<tr>--%>
                <%--<td><span class="console-grey ng-binding">失败返回结果示例：</span>--%>
                    <%--<span id="33"class="ng-binding">${app.appDescription}</span>--%>
                <%--</td>--%>
            <%--</tr>--%>
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
    var id = '${serviceDef.id}';
    $(function () {

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


    function renderid(data, type, full, meta) {
        var rowId = meta.settings._iDisplayStart + meta.row + 1;
        return rowId;
    }

    function test() {
        var url=context+"/service/api/execute/test/"+id;
        /*window.location.href =url;*/
        $.dialog({
            type: "iframe",
            url: url,
            title: "API测试工具",
            width: 1000,
            height: 650
        });
    }


</script>
</html>
