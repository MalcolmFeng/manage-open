<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>开放平台-API调用统计</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/monitor/api_monitor_view.css'/>"/>
    <script type="text/javascript">
        //项目上下文
        var context = "<%=request.getContextPath()%>";
        //获取静态资源上下文路径
        var assetPath = "<l:assetcontext/>";
    </script>
    <script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='echarts.min.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='service/monitor/api_monitor_view.js'/>"></script>
</head>
<body>

<div class="container" style="width: 100%">
    <%--    <div class="row clearfix">--%>
    <%--        <div class="col-md-12 column">--%>
    <%--            <div class="page-header">--%>
    <%--                <h1>API调用统计</h1>--%>
    <%--            </div>--%>
    <%--        </div>--%>
    <%--    </div>--%>
    <div class="row clearfix" style="padding-left: 2%;padding-right: 2%">
        <div class="col-md-6 column">
            <div class="row clearfix">
                <div class="col-md-6 column ">
                    <div class="panel panel-default all-total-count panel-body col-xs-10" style="padding-right: 3%">
                        <div class="col-md-11 col-xs-11">
                            <div class="row">
                                <div class="count">总调用量</div>
                                <div class="count-text"><span id="allTotalCount">0</span></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 column">
                    <div class="panel panel-default all-total-count panel-body col-xs-10" style="padding-right: 3%">
                        <div class="col-md-11 col-xs-11">
                            <div class="row">
                                <div class="count">总调用成功量<span class="count-outher" id="allTotalSuccessCount">0</span>
                                </div>
                                <div class="count">总异常次数<span class="count-outher" id="allTotalFalseCount">0</span>
                                </div>
                                <div class="count">成功率<span class="count-outher" id="allTotalSuccessPercent">0</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6 column">
            <div class="row clearfix">
                <div class="col-md-6 column">
                    <div class="panel panel-default all-total-count panel-body col-xs-10" style="padding-right: 3%">
                        <div class="col-md-11 col-xs-11">
                            <div class="row">
                                <div class="count">今日总调用量</div>
                                <div class="count-text"><span id="todayTotalCount">0</span></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 column">
                    <div class="panel panel-default all-total-count panel-body col-xs-10" style="padding-right: 3%">
                        <div class="col-md-11 col-xs-11">
                            <div class="row">
                                <div class="count">今日调用成功量<span class="count-outher"
                                                                id="todayTotalSuccessCount">0</span></div>
                                <div class="count">今日异常次数<span class="count-outher" id="todayTotalFalseCount">0</span>
                                </div>
                                <div class="count">成功率<span class="count-outher" id="todayTotalSuccessPercent">0</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-6 column">
            <div id="dayTotalCountEcharts" style="width: 100%;height:700px;"></div>
        </div>
        <div class="col-md-6 column">
            <div id="topApiCountEcharts" style="width: 100%;height:700px;"></div>
        </div>
    </div>
</div>

</body>
</html>