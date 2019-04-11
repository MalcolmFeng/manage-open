<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/ServerReviewList.css'/>"/>
	<title>审核-实例列表</title>
</head>
<body>
<div class="content">
	<div class="col-xs-12 col-md-12 header">          
	    <div class="row">
	        <div class="form-inline"> 
	        	<div class="pull-left">
	                <!-- 根据实例名称查询 -->
					<div class="pull-left search-group">
	            		<div class="pull-left">
	            			<input class="form-control ue-form" id="instanceName_search" placeholder="输入实例名称查询">
	            		</div>	            			           			
					</div>
	           </div>                                      
	      </div>
	   </div>
	</div>
	
	<div class="hr"><hr></div>
	
	<%--<div class="list_header">--%>
		<%--<div>--%>
           <%--<!-- 批量通过 -->--%>
           <%--<div class="btn-group">--%>
                <%--<button id="passAll" type="button" class="btn ue-btn-primary">--%>
                   <%--<span class="fa fa-check"></span>通过--%>
               <%--</button>--%>
           <%--</div>--%>
		   <%--<!-- 批量驳回 -->--%>
           <%--<div class="btn-group">--%>
                <%--<button id="rejectAll" type="button" class="btn ue-btn-primary">--%>
                   <%--<span class="fa fa-close"></span>驳回--%>
               <%--</button>--%>
           <%--</div>	--%>
           <%--<!-- 批量删除 -->--%>
			<%--<div class="btn-group">--%>
                <%--<button id="delAll" type="button" class="btn ue-btn-primary">--%>
                   <%--<span class="fa fa-trash"></span>删除--%>
               <%--</button>--%>
           <%--</div>				--%>
		<%--</div>	--%>
	<%--</div>--%>
	
	<!-- 实例列表 -->
	<table id="instanceList" class="table table-bordered table-hover">
	    <thead>
	        <tr>
	        	<th width="5%" data-field="instance_name" data-sortable="false" data-render="rendercheckbox"><input type="checkbox" id="selectAll" onclick="forSelectAll()"></th>
	            <th width="23%" data-field="instance_name" data-sortable="false">实例名称</th>
	            <th width="31%" data-field="specification_iso" data-sortable="false">规格|镜像</th>
	            <th width="8%" data-field="status" data-sortable="false" data-render="renderStatus">状态</th>
	            <th width="8%" data-field="apply_user" data-sortable="false">申请人</th>
	            <th width="15%" data-field="apply_time" data-sortable="false">申请时间</th>
	            <th width="10%" data-field="status" data-sortable="false" data-render="renderoptions">操作</th>
	        </tr>
	    </thead>
	</table>
	
</div>
</body>
<script  type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>
<script type="text/javascript" src="<l:asset path='cloud/ecs/ServerReviewList.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
</script>
</html>