<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>我的申请</title>

    <!-- 需要引用的CSS -->
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
       <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
       <link rel="stylesheet" type="text/css" href="<l:asset path='css/ztree.css'/>" />
       <link rel="stylesheet" type="text/css" href="<l:asset path='data/datadev.css'/>" />
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
<script type="text/javascript">
  var context = "<l:assetcontext/>";
  var url = context + "/service/open/apply/applylist";
  var grid;
  $(document).ready(function() {
    var options = {
      ordering: false
    };


    grid = new L.FlexGrid("myApplyList", url);
    grid.init(options); //初始化datatable
  });

  function forEdit(openServiceId) {
    window.location.href = context + "/service/data/apply/edit/" + openServiceId;
  }

  function forView(openServiceId,api_service_id) {

      window.location.href = context + "/service/open/api/get/" + api_service_id+"/apply";
  }

  function forQuery() {
    var serviceName = $("#tableName").val();
    var authStatus = $("#authStatus option:selected").val();
    var param = {
        serviceName: $("#tableName").val(),
          authStatus: $("#authStatus option:selected").val()
      };
      grid.reload(url, param);
  }
  function forDel(applyId) {
    $.dialog({
      type: 'confirm',
      content: '确认删除该申请吗?',
      autofocus: true,
      ok: function() {
        $.ajax({
          url: context + "/service/open/apply/delete/" + applyId,
          success: function(msg) {
            if (msg == true) {
              forQuery();
              sticky("删除成功！", "success");
            } else {
              sticky("删除失败", 'error');
            }
          }
        });
      },
      cancel: function() {
      }
    });
  }

  function reloadMyApplyList() {
    // 重新请求数据
    $("#myApplyList").DataTable().ajax.reload();
  }

  //弹窗提示样式
  function sticky(msg, style, position) {
    var type = style ? style : 'success';
    var place = position ? position : 'center';
    $.sticky(msg, {
      autoclose: 1000,
      position: place,
      style: type
    });
  }

  function renderId(data, type, full, meta) {
    var rowId = meta.settings._iDisplayStart + meta.row + 1;
    return rowId;
  }

  function renderAuthStatus(data, type, full) {
    var html = '';
    if(data == '0') {
      html += '<span class="fa fa-clock-o text-info">&nbsp;待授权&emsp;</span>';
    } else if(data == '1') {
      html += '<span class="fa fa-check-circle text-success">&nbsp;同意授权</span>';
    } else if(data == '2') {
      html += '<span class="fa fa-times-circle text-danger">&nbsp;拒绝授权</span>';
    }
    return html;
  }

  function renderOptions(data, type, full) {
	  var html = "<div>";
	  if (full.apiStatus == '2') {
	  	//api服务状态是上线状态才可以查看
		  if (full.auth_status != '1') {
			  //申请状态不是审核通过都可以删除
			  html += '<a onclick="forView(\'' + full.id + '\',\'' + full.api_service_id + '\')">查看</a>&nbsp;&nbsp;';
			  html += '<a onclick="forDel(\'' + full.id + '\')">删除</a>';
		  } else {
			  html += '<a onclick="forView(\'' + full.id + '\',\'' + full.api_service_id + '\')">查看</a>';
		  }
	  }else{
		  html += '<a onclick="forDel(\'' + full.id + '\')">删除</a>';
	  }
	  html += '</div>';
	  return html;
  }
</script>
</head>
<body>
	<div class="container" style="width: 98%; padding-top:10px;">
		<div class="row">
			<form class="form-inline" onsubmit="return false;">
				<div class="input-group">
			        <input class="form-control ue-form" type="text" id="tableName" placeholder="请输入服务名称"/>
			        <div class="input-group-addon ue-form-btn" onclick="forQuery()">
			        	<span class="fa fa-search"></span>
			        </div>
		        </div>
		        <div class="pull-right">
		            <select id="authStatus" class="form-control ue-form" onchange="forQuery()">
		                <option value="">全部</option>
		                <option value="0">待授权</option>
		                <option value="1">同意授权</option>
		                <option value="2">拒绝授权</option>
		            </select>
		        </div>
			</form>
		</div>
		<div class="row">
			<table id="myApplyList" class="table table-bordered table-hover">
				<thead>
					<tr>
						<th width="5%" data-field="id" data-render="renderId">序号</th>
						<th width="25%" data-field="app_name">应用名称</th>
						<th width="25%" data-field="name">服务名称</th>
						<th width="15%" data-field="apply_time">申请时间</th>
						<th width="15%" data-field="auth_status" data-render="renderAuthStatus">授权状态</th>
						<th width="15%" data-field="dtDataId" data-render="renderOptions">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<script id="emptylist" type="text/html">
		<div class="data-empty">
			<span class="norecord"></span>
		</div>
	</script>
</body>
</html>