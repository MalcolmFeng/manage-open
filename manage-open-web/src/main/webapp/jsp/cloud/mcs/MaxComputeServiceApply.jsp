<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- 需要引用的CSS -->
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='cloud/mcs/maxComputeServiceApply.css'/>"/>
    <title>大数据计算服务申请</title>
</head>

<body>
<div class="col-xs-12 col-md-12 header">
    <a class="back"><span>实例列表</span></a>
    <a class="back" style="float: right"><span>返回列表</span></a>
    <hr>
</div>

<div class="content">
    <form class="form-horizontal" id="mcsApply" name="mcsApply" onsubmit="return false">
        <!-- 实例名称 -->
        <div class="form-group">
            <label class="col-xs-3 col-md-1 control-label"><span style="color:red">*</span>实例名称：</label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="instance_name" name="instance_name"
                       value="${mcs.instanceName}" datatype="instance_name"/>
                <span class="rules">可使用自动生成名称，也可自定义。自定义实例名称可由大写字母、小写字母、数字、“-”、“_”组成，最大支持64个字符。</span>
            </div>
        </div>

        <!-- 产品版本 -->
        <div class="form-group">
            <label class="col-xs-3 col-md-1 control-label">产品版本：</label>
            <div class="col-xs-8 col-md-8">
                <select class="form-control ue-form pull-left" name="service_version" id="service_version">
                    <c:forEach items="${bigDataServices}" var="bigDataService">
                        <option value="${bigDataService.serviceId}-${bigDataService.serviceName}-${bigDataService.serviceVersion}">${bigDataService.serviceName} ${bigDataService.serviceVersion}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <!-- 服务说明 -->
        <div class="form-group">
            <label class="col-xs-3 col-md-1 control-label">服务说明：</label>
            <div class="col-xs-8 col-md-8">
                <table id="service-component-table" class="table table-bordered table-hover dataTable">
                    <thead>
                    <tr>
                        <th width="20%" data-field="componentName" data-sortable="false">服务名</th>
                        <th width="20%" data-field="componentVersion" data-sortable="false">版本</th>
                        <th width="160%" data-field="componentDesc" data-sortable="false">描述</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>

        <!-- 计算资源 -->
        <div class="form-group" id="core_num_memory">
            <label class="col-xs-3 col-md-1 control-label">计算资源：</label>
            <div class="col-xs-8 col-md-8">
                <label for="core_num" class="pull-left">vCPU</label>
                <select class="form-control ue-form pull-left core_num" name="core_num" id="core_num"></select>
                <label for="memory" class="pull-left">内存</label>
                <select class="form-control ue-form pull-left memory" name="memory" id="memory"></select>
            </div>
        </div>

        <!-- 存储资源 -->
        <div class="form-group">
            <label class="col-xs-3 col-md-1 control-label">存储资源：</label>
            <div class="col-xs-8 col-md-8">
                <select class="form-control ue-form pull-left" style="width: 50px" id="storage_volume" name="storage_volume">
                    <option value="50" selected>50</option>
                    <option value="100">100</option>
                    <option value="150">150</option>
                    <option value="200">200</option>
                    <option value="250">250</option
                    <option value="300">300</option>
                </select>
                <span class="unit-span" style="margin-left:5px">GB</span>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label"></label>
            <div class="col-sm-9">
                <button type="button" class="btn ue-btn-primary" id="apply">立即申请</button>
                <button type="button" class="btn ue-btn" id="cancel">取消</button>
            </div>
        </div>
    </form>
</div>
</body>

<script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/mcs/maxComputeServiceApply.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
    var contextPath = '<%=request.getContextPath()%>';
    $(".back").click(function () {
        window.location.href = contextPath + "/service/mcs/manage";
    });
</script>
</html>