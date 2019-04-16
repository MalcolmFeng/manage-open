<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>API市场</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/datadev.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/datalist/css/datalist.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/apiservice/css/apiList.css'/>" />
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
    <script type="text/javascript" src="<l:asset path='jquery.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='form.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='arttemplate.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ztree.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='service/apiservice/apiList.js'/>"></script>
    <title>API市场</title>
</head>
<body>
<div class="container" style="width: 98%; padding-top:10px;">
    <div class="row">
        <form class="form-inline" onsubmit="return false;">
            <div class="input-group">
                <input type="hidden" value="${groupId }" id="groupId"/>
                <input class="form-control ue-form" type="text" id="serviceName" placeholder="请输入关键词"/>
                <div class="input-group-addon ue-form-btn" onclick="forQuery()">
                    <span class="fa fa-search"></span>
                </div>
            </div>
        </form>
    </div>
    <div class="row" id="groupList" style="margin-top:20px;margin-left:10px;">
        <span style="margin-right: 10px;">API分组:</span>
        <ul class="list-inline" style="display:inline-block">
            <li class="activedlink"><a onclick="queryAll();" >全部</a></li>
            <c:forEach items="${groupList }" var="item">
                <li><a onclick="reloadServiceListByGroupId('${item.id}')">${item.name}</a></li>
            </c:forEach>
        </ul>
    </div>
    <div class="row" id="subGroupList" style="margin-left:10px;"></div>
    <div class="row">
        <div id="api-catalog" class="col-xs-3 col-md-3">
            <div class="row">
                <div id="api-cat-top">
                    <span class="navbar-left">API目录</span>
                    <span id="cat-icon" class="glyphicon glyphicon-th-list nav-right"></span>
                </div>
                <div id="api-cat-bottom">
                    <div class="panel-group" id="accordion">
                        <div id="APIList" class="panel panel-default">
                            <ul class="list-group"></ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="api-contents" class="col-xs-9 col-md-9">
            <div class="row">
                <div id="api-con-top">
                    <span id="title-en"></span>
                    <span id="title-ch"></span>
                </div>
                <div id="api-con-bottom">
                    <div id="quick-link">
                        <a class="link-item" href="#api-uat">API用户授权类型</a>
                        <span class="divide-line"></span>
                        <a class="link-item" href="#api-sip">系统级输入参数</a>
                        <span class="divide-line"></span>
                        <a class="link-item" href="#api-aip">应用级输入参数</a>
                        <span class="divide-line"></span>
                        <a class="link-item" href="#api-rp">响应参数</a>
                        <span class="divide-line"></span>
                        <a class="link-item" href="#api-rr">返回结果示例</a>
                        <span class="divide-line"></span>
                        <a class="link-item">错误码</a>
                        <span class="divide-line"></span>
                        <a class="link-item" href="#api-test">API工具</a>
                        <span class="divide-line"></span>
                        <a class="link-item">SDK下载</a>
                    </div>
                    <div id="link-contents">
                        <div class="api-tips">
                            <span id="description"></span>
                        </div>
                        <div class="api-title" id="api-uat">
                            <%--<span>API用户授权类型</span>--%>
                            <span>API用户授权</span>
                        </div>

                        <div class="api-tips" id="api-needAuth">
                        </div>
                        <div class="api-title" id="api-sip">
                            <span>系统级输入参数</span>
                        </div>
                        <table class="table table-striped table-bordered">
                            <thead class="t-head">
                            <tr>
                                <th style="width: 15%">名称</th>
                                <th style="width: 15%">类型</th>
                                <th style="width: 15%">是否必须</th>
                                <th style="width: 55%">描述</th>
                            </tr>
                            </thead>
                            <tbody class="t-body" id="inputParamBody">
                            <tr>
                                <td>_app_timestamp</td>
                                <td>String</td>
                                <td>否</td>
                                <td>请求时间戳</td>
                            </tr>
                            <tr>
                                <td>_app_signature</td>
                                <td>String</td>
                                <td>是</td>
                                <td>请求签名</td>
                            </tr>
                            <tr>
                                <td>_access_token</td>
                                <td>String</td>
                                <td>是</td>
                                <td>用户授权令牌</td>
                            </tr>
                            </tbody>
                        </table>

                        <div class="api-title" id="api-aip">
                            <span>应用级输入参数</span>
                        </div>
                        <table class="table table-striped table-bordered">
                            <thead class="t-head">
                            <tr>
                                <th style="width: 15%">名称</th>
                                <th style="width: 15%">类型</th>
                                <th style="width: 15%">是否必须</th>
                                <th style="width: 55%">描述</th>
                            </tr>
                            </thead>
                            <tbody class="t-body" id="inputParamTbody">
                            </tbody>
                        </table>

                        <div class="api-title" id="api-rp">
                            <span>响应参数</span>
                        </div>
                        <table class="table table-striped table-bordered">
                            <thead class="t-head">
                            <tr>
                                <th style="width: 15%">名称</th>
                                <th style="width: 55%">描述</th>
                            </tr>
                            </thead>
                            <tbody class="t-body" id="responseParamTbody">
                            </tbody>
                        </table>

                        <div class="api-title" id="api-rr">
                            <span>返回结果示例</span>
                        </div>
                        <div class="api-tips" id="returnExample">
                            <span></span>
                        </div>
                        <div class="api-title" id="api-test">
                            <span>API测试工具</span>
                        </div>
                        <div class="api-tips" id="apiTest">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- api列表 -->
<script type="text/html" id="apiListTemp">
    {{each APIList as api}}
    <li class="list-group-item lg-item">
        <a class="lg-link" onclick="getApiDetail('{{api.id}}')" >
            <span class="api-item" title='/{{api.apiGroup}}{{api.reqPath}}'>/{{api.apiGroup}}{{api.reqPath}}</span>
            <span class="api-desc">{{api.description}}</span>
        </a>
    </li>
    {{/each}}
</script>

<script type="text/html" id="inputParamTemp">
    {{each inputParamList as inputParam}}
    <tr>
        <td>{{inputParam.name}}</td>
        <td>{{inputParam.type}}</td>
        {{if inputParam.required == 0}}
            <td>否</td>
        {{else}}
            <td>是</td>
        {{/if}}
        <td>{{inputParam.description}}</td>
    </tr>
    {{/each}}
</script>

<script id="subGroupListTemp" type="text/html">
    <span style="margin-right: 10px;">&nbsp;子分组:</span>
    <ul class="list-inline" style="display:inline-block">
        {{each data as item i}}
        <li id="{{item.id}}" onclick="renderGroup('{{item.id}}')">
            <a onclick="reloadServiceListBySubGroupId('{{item.id}}')">{{item.name}}</a>
        </li>
        {{/each}}
    </ul>
</script>

<script type="text/javascript">
    var context = "<l:assetcontext/>";
</script>

</body>
</html>