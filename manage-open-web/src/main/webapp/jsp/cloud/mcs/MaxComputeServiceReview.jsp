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
    <link rel="stylesheet" type="text/css" href="<l:asset path='cloud/mcs/maxComputeServiceReview.css'/>"/>
    <title>大数据计算服务实例审核</title>
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
                <!-- 申请人 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">申请人：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="apply_user" name="apply_user"></label>
                    </div>
                </div>
                <!-- 申请时间 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">申请时间：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="apply_time" name="apply_time"></label>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- 配置信息 -->
    <div class="box configInfo">
        <div class="box-header">
            <div class="box-left"></div>
            <div style="float:left;">配置信息</div>
        </div>
        <br>
        <div class="box-content">
            <form class="form-horizontal" id="configInfoForm" name="configInfoForm" onsubmit="return false">
                <!-- 产品版本 -->
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
                <!-- 存储资源 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">存储资源：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="storage_resource" name="storage_resource"></label>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!--审核信息-->
    <div class="box reviewInfo">
        <div class="box-header">
            <div class="box-left"></div>
            <div style="float:left;">基本信息</div>
        </div>
        <br>
        <div class="box-content">
            <form class="form-horizontal" id="reviewInfoForm" name="reviewInfoForm" onsubmit="return false">
                <!-- 审核状态 -->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">审核状态：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="status" name="staus"></label>
                    </div>
                </div>
                <!--审核意见-->
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">审核意见：</label>
                    <div class="col-xs-8 col-md-8" id="audit_reply_content">
                        <textarea class="form-control ue-form pull-left audit_reply" id="audit_reply" rows="5" cols="30" style="margin-top: 10px"></textarea>
                    </div>
                </div>
                <!-- 审核时间 -->
                <div class="form-group" id="reply_time_div">
                    <label class="col-xs-2 col-md-2 control-label">审核时间：</label>
                    <div class="col-xs-8 col-md-8">
                        <label id="reply_time" name="reply_time"></label>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="button-row">
        <div class="form-group">
            <label class="col-sm-1 control-label"></label>
            <div class="col-sm-9">
                <button type="button" class="btn ue-btn-primary" id="pass">通过</button>
                <button type="button" class="btn ue-btn-primary" id="reject">驳回</button>
            </div>
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
<script type="text/javascript" src="<l:asset path='cloud/mcs/maxComputeServiceReview.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
    var contextPath = '<%=request.getContextPath()%>';
    var instanceId = '<%=request.getParameter("instanceId")%>';
    $(".back").click(function () {
        window.location.href = contextPath + "/service/mcs/review";
    });
</script>
</html>