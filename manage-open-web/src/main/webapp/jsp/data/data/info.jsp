<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>服务详情</title>

    <!-- 需要引用的CSS -->
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='data/datalist/css/info.css'/>" />
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
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
	<script type="text/javascript" src="<l:asset path='data/data/info.js'/>"></script>
	<%--<script type="text/javascript" src="<l:asset path='data/data/resourceDataVisual.js'/>"></script>--%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据详情</title>
</head>
<body>
	<div class="container" style="width: 98%; padding-top:10px;">
		<div class="row tab-con">
			<ul id="myTab" class="tab">
				<li class="active"><a href="#overview" data-tab="overview" data-toggle="tab">概览</a></li>
				<li><a href="#sjlb" data-tab="sjlb" data-toggle="tab">数据列表</a></li>
			</ul>
		</div>
		<div id="overview">
			<div class="row" style="margin-bottom: 10px;">
				<img style="float: left; margin: 0px 10px 10px 0px;" src="<%=request.getContextPath() %>/skins/skin/data/datalist/img/default-64.png"/>
				<div class="title-text">
					<div class="service-name">${serviceInfo.name }</div>
					<div class="service-time">发布时间: ${serviceInfo.updateTime }</div>
					<div class="service-status">状态:<c:if test="${serviceInfo.auditStatus eq 'online' }">发布上线</c:if>
						<c:if test="${serviceInfo.auditStatus eq 'audit' }">待审核</c:if>
						<c:if test="${serviceInfo.auditStatus eq 'reject' }">拒绝上线</c:if>
						<div class="button-group">
							<button type="button" onclick="history.back();" id="backBtn">返回</button>
							<c:if test="${apply}">
								<button type="button" onclick="applyDataDef('${serviceInfo.id}')" id="applyBtn">申请</button>
							</c:if>
						</div>

					</div>
				</div>
			</div>
			<div class="row" style="margin-bottom: 10px;">
				<div class="service_info_title">基本信息</div>
				<div>
					<label>数据描述：</label>
					<label>${serviceInfo.description }</label>
				</div>
				<div>
					<label >是否需要用户授权：</label>
					<label>
						<c:choose>
							<c:when test="${serviceInfo.authType==1}">
								是
							</c:when>
							<c:otherwise>
								否
							</c:otherwise>
						</c:choose>
					</label>
				</div>
				<div>
					<label >更新时间：</label>
					<label>${serviceInfo.updateTime}</label>
				</div>

				<c:choose>
					<c:when test="${apply==false}">
						<div>
							<label >JDBC URL：</label>
							<label>${serviceInfo.jdbcUrl}</label>
						</div>
						<div>
							<label >库名：</label>
							<label>${serviceInfo.instanceName}</label>
						</div>
						<div>
							<label >表名：</label>
							<label>${serviceInfo.tableName}</label>
						</div>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="row">
				<div class="service_info_title">数据统计</div>
				<iframe id="detailPage" frameborder="0" src='${serviceInfo.dataDetailUrl}' width="100%" height="100%"></iframe>
			</div>

		</div>
		<div id="sjlb" style="display: none">
			<div class="table-name-con col-md-3">
				<ul id="tableNameList">
					<li>无表格</li>
				</ul>
			</div>
			<div class="table-col-con col-md-9">

				<div class="service_info_title" id="tablename">数据项信息</div>
				<div class="service_info_title">数据项信息</div>
				<table id="colDataTable" class="table table-hover table-bordered">
					<thead>
					<tr>
						<th>中文名称</th>
						<th>列名</th>
						<th>类型</th>
						<th>长度</th>
						<th>可为空</th>
					</tr>
					</thead>
					<tbody></tbody>
				</table>

				<div>
					<div class="pagination_info">
					</div>
					<div class="pagination_number">
						<div class="pagination"></div>
					</div>
					<div class="clearfix"></div>
				</div>

				<div class="service_info_title" style="margin-top: 40px;">数据可视化</div>
				<div class="pull-left">
					共<span id="totalCount" style="color: #3F8BE8"></span>条数据
				</div>
				<table class="table table-hover table-bordered" id="colDataList">
					<thead>
						<tr></tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				<%--<table class="table table-hover table-bordered" id="grid">
					<thead>
					</thead>
				</table>--%>
			</div>
		</div>
  </div>

	<!--表-->
	<script type="text/html" id="tableTemp">
		{{each tableList as table}}
		<li data-resourceId='{{table.resourceId}}' data-text="{{table.tableName}}"><img src="<%=request.getContextPath() %>/skins/skin/data/datalist/img/table-logo.png"/>{{table.resourceName}}</li>
		{{/each}}
	</script>
	<!--数据项 -->
	<script type="text/html" id="colDataTableTemp">
		{{each colList as col}}
		<tr>
			<td>{{col.data.columnDescription}}</td>
			<td>{{col.data.columnName}}</td>
			<td>{{col.data.columnType}}</td>
			<td>{{col.data.columnLength}}</td>
			{{if col.data.isNull == 1}}
				<td>是</td>
			{{else}}
				<td>否</td>
			{{/if}}
		</tr>
		{{/each}}
	</script>

	<!--数据表可视化-->
	<script type="text/html" id="colDataListTemp">
		<tr>
			{{each colDataList as colData}}
			<td>{{colData}}</td>
			{{/each}}
		</tr>
	</script>

	<script type="text/javascript">
        var context = "<l:assetcontext/>";
        var openDataDefId = '${serviceInfo.id}';
        var remoteId = '${serviceInfo.remoteId}';
	</script>
</body>
</html>