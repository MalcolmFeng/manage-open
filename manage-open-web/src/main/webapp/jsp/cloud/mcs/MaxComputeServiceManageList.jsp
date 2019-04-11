<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
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
	<link rel="stylesheet" type="text/css" href="<l:asset path='cloud/mcs/maxComputeServiceManageList.css'/>"/>
	<title>大数据计算服务实例</title>
  </head>
	
  <body>
  <div class="content">
      <div class="col-xs-12 col-md-12 header">
          <div class="row">
              <div class="form-inline">
                  <div id="header" class="pull-left">
                      <p>
                          <span style="font-weight: bold; font-size: 130%">实例列表</span>
                      </p>
                  </div>
                  <div class="btn-group pull-right">
                      <!-- 创建实例 -->
                      <button id="addInstance" type="button" class="btn ue-btn-primary ">
                          <span class="fa fa-plus">创建实例</span>
                      </button>
                  </div>
              </div>
          </div>
      </div>

      <div class="hr">
          <hr>
      </div>

      <div class="pull-left">
          <!-- 根据实例名称查询 -->
          <div class="pull-left search-group">
              <div class="pull-left">
                  <input style="width: 350px; height: 28px" class="form-control ue-form" id="instanceName_search"
                         placeholder="选择实例属性项搜索，或者输入关键字识别搜索">
              </div>
          </div>
      </div>

      <div class="list_header">
          <div>
              <!-- 批量删除 -->
              <%--<div class="btn-group pull-right">
                  <button id="delAll" type="button" class="btn ue-btn-primary">
                      <span class="fa fa-trash"></span>删除
                  </button>
              </div>--%>
          </div>
      </div>

      <!-- 实例列表 -->
      <table id="instanceList" class="table table-bordered table-hover">
          <thead>
          <tr>
              <th width="5%" data-field="instance_id" data-sortable="false" data-render="renderCheckbox"><input type="checkbox" id="selectAll" onclick="forSelectAll()"></th>
              <th width="30%" data-field="instance_name" data-sortable="false">实例名称/ID</th>
              <th width="20%" data-field="create_time" data-sortable="false">创建时间</th>
              <th width="10%" data-field="service_version" data-sortable="false">产品版本</th>
              <th width="15%" data-field="status" data-sortable="false" data-render="renderStatus">状态</th>
              <th width="20%" data-field="status" data-sortable="false" data-render="renderOptions">操作</th>
          </tr>
          </thead>
      </table>
  </div>
  </body>

  <script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='form.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='cloud/mcs/maxComputeServiceManageList.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='i18n.js'/>"></script>
  <script type="text/javascript">
    var contextPath = '<%=request.getContextPath()%>';
  </script>
</html>