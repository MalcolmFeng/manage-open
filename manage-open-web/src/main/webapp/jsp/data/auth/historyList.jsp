<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务授权列表</title>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>

<!--[if lt IE 9]>
<script src="<l:asset path='html5shiv.js'/>"></script>
<script src="<l:asset path='respond.js'/>"></script>
<![endif]-->

<script  type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>

<script type="text/javascript">
var context = "<l:assetcontext/>";
$(document).ready(function() {
    var options = {
        info: true,
        ordering: false
    };
    
	var url = context+"/service/data/apply/listhistory";
	grid = new L.FlexGrid("authList",url);
	grid.init(options); //初始化datatable
});


function reloadAuthList() {
  // 重新请求数据
  $("#authList").DataTable().ajax.reload();
}

//弹窗提示样式
function sticky(msg, style, position) {
	var type = style ? style : 'success';
	var place = position ? position : 'top';
	$.sticky(
	    msg,
	    {
	        autoclose : 1000, 
	        position : place,
	        style : type
	    }
	);
}

function renderId(data, type, full, meta) {
    var rowId = meta.settings._iDisplayStart + meta.row + 1;
    return rowId;
}

function renderName(data, type, full) {
    var html = '';
    html += '<a onclick="forView(\''+ full.dtDataId +'\')">'+ data + '</a>&emsp;';
    return html;
}
function renderStatus(data, type, full) {
  var html = '';
  if(data == 1) {
    html += '<span class="fa fa-check-circle text-success">&nbsp;已同意</span>';
  } else if(data == 2) {
    html += '<span class="fa fa-times-circle text-danger">&nbsp;已驳回</span>';
  }
  return html;
}

function forQuery() {
    var tableName = $("#serviceName").val();
    grid.setParameter("tableName", tableName);
    reloadAuthList();
}

function reloadAuthList() {
    // 重新请求数据
    $("#authList").DataTable().ajax.reload();
}

function forView(openDataId) {
    window.location.href = context + "/service/open/data/get/" + openDataId;
}

</script>
</head>
<body>
	<div class="topdist"></div>
	<div class="container" style="width: 98%; padding-top:10px;">
	    <div class="row">
		<form class="form-inline" onsubmit="return false;">										
			<div class="input-group">
		        <input class="form-control ue-form" type="text" id="serviceName" placeholder="请输入数据名称"/>
		        <div class="input-group-addon ue-form-btn" onclick="forQuery()">
		        	<span class="fa fa-search"></span>
		        </div>
	        </div>
		</form>
	</div>
	<div class="row">
		<table id="authList" class="table table-bordered table-hover">
			<thead>
			<tr>
				<th width="5%" data-field="id" data-render="renderId">序号</th>
				<th width="15%" data-field="name" data-render="renderName">数据名称</th>
				<th width="20%" data-field="tableName">表名称</th>
				<th width="10%" data-field="description">数据描述</th>
				<th width="10%" data-field="applicant">申请人</th>
				<th width="10%" data-field="applyTime">申请时间</th>
				<th width="10%" data-field="authTime">授权时间</th>
				<th width="10%" data-field="authUser">授权人员</th>
				<th width="10%" data-field="authStatus" data-render="renderStatus">授权结果</th>
			</tr>
			</thead>
		</table>
	</div>
	</div>
</body>
</html>