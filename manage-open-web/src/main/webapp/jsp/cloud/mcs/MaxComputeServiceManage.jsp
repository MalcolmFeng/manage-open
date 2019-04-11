<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <!-- 需要引用的CSS -->
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='cloud/mcs/maxComputeServiceManage.css'/>"/>
    <title>大数据计算服务实例管理控制台</title>
</head>

<body>
<div class="col-xs-12 col-md-12 header">
    <a class="back"><span>实例列表</span></a>
    <a class="back" style="float:right"><span>返回列表</span></a>
    <hr>
</div>

<div class="content">
    <!-- 基本信息 -->
    <div class="box basicInfo">
        <div class="box-header">
            <div class="box-left"></div>
            <div style="float:left;">基本信息</div>
        </div>
        <br>
        <div class="box-content">
            <form class="form-horizontal" id="basicInfoForm" name="basicInfoForm" onsubmit="return false">
                <!-- 实例名称 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">实例名称：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="instance_name" name="instance_name"></label>
                    </div>
                </div>
                <!-- 实例ID -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">实例ID：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="instance_id" name="instance_id"></label>
                    </div>
                </div>
                <!-- 审核状态 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">审核状态：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="apply_status" name="apply_status"></label>
                    </div>
                </div>
                <!-- 审核意见 -->
                <div class="form-group" id="audit_opinion_div">
                    <label class="col-xs-2 col-md-2 control-label">审核意见：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="audit_opinion" name="apply_status"></label>
                    </div>
                </div>
                <!-- 申请时间 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">申请时间：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="apply_time" name="apply_time"></label>
                    </div>
                </div>
                <!-- 运行状态 -->
                <div class="form-group" id="run_status_content">
                    <label class="col-xs-2 col-md-2 control-label">运行状态：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="run_status" name="run_status"></label>
                    </div>
                </div>
                <!-- 创建时间 -->
                <div class="form-group" id="create_time_content">
                    <label class="col-xs-2 col-md-2 control-label">创建时间：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="create_time" name="create_time"></label>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- 访问信息 -->
    <div class="box accountInfo" id="accountInfo">
        <div class="box-header">
            <div class="box-left"></div>
            <div style="float:left;">访问信息</div>
        </div>
        <br>
        <div class="box-content">
            <form class="form-horizontal" id="accountInfoForm" name="accountInfoForm" onsubmit="return false">
                <!-- 访问地址 -->
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">访问地址：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<label id="service_address" name="service_address"></label>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!-- 用户名 -->
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">用户名：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<label id="service_username" name="service_username"></label>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!-- 密码 -->
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">密码：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<label id="service_passwd" name="service_passwd"></label>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!-- principal -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">Principal：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="principal" name="principal"></label>
                    </div>
                </div>
                <!-- 证书下载 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">下载证书：</label>
                    <div class="col-xs-8 col-md-8">
                        <button type="button" class="btn ue-btn-primary" onclick="downloadCA()">
                            <span class="fa fa-arrow-circle-down">下载</span>
                        </button>
                    </div>
                </div>
                <!-- hosts下载 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">下载hosts：</label>
                    <div class="col-xs-8 col-md-8">
                        <button type="button" class="btn ue-btn-primary" onclick="downloadhosts()">
                            <span class="fa fa-arrow-circle-down">下载</span>
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- 配置信息 -->
    <div class="box configInfo" id="configInfo">
        <div class="box-header">
            <div class="box-left"></div>
            <div style="float:left;">配置信息</div>
        </div>
        <br>
        <div class="box-content">
            <form class="form-horizontal" id="configInfoForm" name="configInfoForm" onsubmit="return false">
                <!-- 版本 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">产品版本：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="service_version" name="service_version"></label>
                    </div>
                </div>
                <!-- 计算资源 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">计算资源：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="compute_resource" name="compute_resource"></label>
                    </div>
                </div>
                <!-- 存储资源-->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">存储资源：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="storage_resource" name="storage_resource"></label>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- 空间信息 -->
    <div class="box spaceInfo" id="spaceInfo">
        <div class="box-header">
            <div class="box-left"></div>
            <div style="float:left;">租户空间</div>
        </div>
        <br>
        <div class="box-content">
            <form class="form-horizontal" id="spaceInfoForm" name="spaceInfoForm" onsubmit="return false">
                <!-- 计算资源使用情况 -->
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">计算资源使用情况：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<div class="progress">--%>
                            <%--<div class="progress-bar" role="progressbar" aria-valuenow="50"--%>
                                 <%--aria-valuemin="0" aria-valuemax="100" style="width: 50%">--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<span id="computeUsage" class="computeUsage">50%&nbsp;&nbsp;2G/4G</span>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!-- HDFS路径 -->
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">HDFS路径：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<label id="hdfs_path" name="hdfs_path"></label>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<!-- 存储资源使用情况 -->--%>
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">存储资源使用情况：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<div class="progress">--%>
                            <%--<div class="progress-bar" role="progressbar" aria-valuenow="40"--%>
                                 <%--aria-valuemin="0" aria-valuemax="100" style="width: 40%">--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<span id="storageUsage" class="storageUsage">40%&nbsp;&nbsp;16G/40G</span>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<!--Hive数据库名称-->--%>
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">Hive数据库名称：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<label id="hive_dbname" name="hive_dbname"></label>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<!-- Hive数据库连接 -->--%>
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">Hive数据库连接：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<label id="hive_url" name="hive_url"></label>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<!--Hive2数据库连接-->--%>
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">Hive2数据库连接：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<label id="hive2_url" name="hive2_url"></label>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<!-- Yarn队列名称 -->--%>
                <%--<div class="form-group">--%>
                    <%--<label class="col-xs-2 col-md-2 control-label">Yarn队列名称：</label>--%>
                    <%--<div class="col-xs-8 col-md-8">--%>
                        <%--<label id="yarn_name" name="yarn_name"></label>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <div class="form-group">
                    <table class="table table-striped" id="tenant_space" style="width: 80%; margin-left: 30px">
                        <thead>
                        <tr>
                            <th width="15%">租户名称</th>
                            <th width="10%">服务</th>
                            <th width="25%">资源</th>
                            <th width="55%">说明</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </form>
        </div>
    </div>
    <br>
    <div class="form-group">
        <div class="col-sm-12">
            <button type="button" class="btn ue-btn-primary back" style="min-width: 120px; min-height: 40px; margin-left: 50px;">确定
            </button>
        </div>
    </div>
</div>
</body>

<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/mcs/maxComputeServiceManage.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
    var contextPath = '<%=request.getContextPath()%>';
    var instanceId = '<%=request.getParameter("instanceId")%>';
    $(".back").click(function () {
        window.location.href = contextPath + "/service/mcs/manage";
    });
</script>
</html>