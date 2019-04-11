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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/ecs/KeyList.css'/>"/>
	<title>密钥对列表</title>
</head>
<body>
<div class="content">
	<div class="col-xs-12 col-md-12 header">          
	    <div class="row">
	        <div class="form-inline"> 
	        	<div class="pull-left">
	                <!-- 根据密钥对名称查询 -->
					<div class="pull-left search-group">
	            		<div class="pull-left">
	            			<input class="form-control ue-form" id="keyName_search" placeholder="输入密钥对名称查询">
	            		</div>	            			           			
					</div>
	           </div>                                      
	            <div class="btn-group pull-right">
	                <!-- 创建密钥对 -->
					<button id="addKey" type="button" class="btn ue-btn ">
	                    <span class="fa fa-plus"></span>创建密钥对
	                </button>
	           </div>
	      </div>
	   </div>
	</div>
	
	<div class="hr"><hr></div>
	
	<div class="list_header">
		<!-- 批量删除 -->
		<div>
			<button id="delAll" type="button" class="btn ue-btn-primary ">
                   <span class="fa fa-trash"></span>删除
               </button>					
		</div>	
	</div>
	
	<!-- 密钥对列表 -->
	<table id="keyList" class="table table-bordered table-hover">
	    <thead>
	        <tr>
	        	<th width="10%" data-field="key_name" data-sortable="false" data-render="rendercheckbox"><input type="checkbox" id="selectAll" onclick="forSelectAll()"></th>
	            <th width="70%" data-field="key_name" data-sortable="false">密钥对名称</th>
	            <th width="20%" data-field="key_name" data-sortable="false" data-render="renderoptions">操作</th>
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
<script type="text/javascript" src="<l:asset path='cloud/ecs/KeyList.js'/>"></script>
<script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
<script type="text/javascript">
	var contextPath = '<%=request.getContextPath()%>';
	var isDownload = '<%=request.getParameter("downloadprivatekey")%>';
	var keyName = '<%=request.getParameter("keyname")%>'
</script>
</html>