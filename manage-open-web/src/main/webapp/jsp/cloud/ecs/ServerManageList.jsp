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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/ServerManageList.css'/>"/>
	<title>管理控制-实例列表</title>
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
	          	           
	           <div class="btn-group pull-right">
	                <!-- 创建实例 -->
					<button id="addInstance" type="button" class="btn ue-btn ">
	                    <span class="fa fa-plus"></span>创建实例
	                </button>
	           </div>                                     
	      </div>
	   </div>
	</div>
	
	<div class="hr"><hr></div>
	
	<%--<div class="list_header">--%>
		<%--<div>--%>
           <%--<!-- 批量启动 -->--%>
           <%--<div class="btn-group">--%>
                <%--<button id="startAll" type="button" class="btn ue-btn-primary">--%>
                   <%--<span class="fa fa-play"></span>启动--%>
               <%--</button>--%>
           <%--</div>--%>
		   <%--<!-- 批量停止 -->--%>
           <%--<div class="btn-group">--%>
                <%--<button id="stopAll" type="button" class="btn ue-btn-primary">--%>
                   <%--<span class="fa fa-stop"></span>停止--%>
               <%--</button>--%>
           <%--</div>	--%>
           <%--<!-- 批量重启 -->--%>
			<%--<div class="btn-group">--%>
                <%--<button id="restartAll" type="button" class="btn ue-btn-primary">--%>
                   <%--<span class="fa fa-refresh"></span>重启--%>
               <%--</button>--%>
           <%--</div>	--%>
           <%--<!-- 批量删除 -->--%>
           <%--<div class="btn-group">--%>
                <%--<button id="delAll" type="button" class="btn ue-btn-primary">--%>
                   <%--<span class="fa fa-trash"></span>删除--%>
               <%--</button>--%>
           <%--</div>--%>
		<%--</div>	--%>
	<%--</div>--%>
	
	<!-- 实例列表 -->
	<table id="instanceList" class="table table-bordered table-hover">
	    <thead>
	        <tr>
	        	<th width="5%" data-field="vm_name" data-sortable="false" data-render="rendercheckbox"><input type="checkbox" id="selectAll" onclick="forSelectAll()"></th>
	            <th width="30%" data-field="instance_name" data-sortable="false">实例名称</th>
	            <th width="10%" data-field="ip" data-sortable="false">IP地址</th>
	            <th width="8%" data-field="status" data-sortable="false" data-render="renderStatus">状态</th>
	            <th width="27%" data-field="specification_iso" data-sortable="false">规格/镜像</th>
	            <th width="20%" data-field="status" data-sortable="false" data-render="renderoptions">操作</th>
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
<script type="text/javascript" src="<l:asset path='cloud/ecs/ServerManageList.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
</script>
</html>