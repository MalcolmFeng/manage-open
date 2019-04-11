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
    <title>远程服务列表</title>
    
    <!-- 需要引用的CSS -->
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ztree.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/datadev.css'/>" />
    
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
  //全局变量
  var serviceGrid;
  var dialog = parent.dialog.get(window);
  $(document).ready(function() {
	// 初始化表格
	initTable();
    //查询框回车键
    $("#serviceName").bind('keypress',function(event) {
  		if (event.keyCode == "13") {
  			forQuery();
  		 }
  	});
  });
  
  function initTable(){
	  var options = {
	      ordering: false,
          scrollY: "280px"
	  };
	  var url = context + "/service/open/api/remoteList"
	  serviceGrid = new L.FlexGrid("myServiceList", url);
	  serviceGrid.init(options); //初始化datatable
  }
  
  function forView(def) {
//    window.location.href = context + "/service/open/data/get/" + id;
  }
  
  function forRegister() {
//    window.location.href = context + "/service/open/data/release?goback=true";
  }
  
  function forQuery() {
      var serviceName=$("#serviceName").val();
      serviceGrid.setParameter("serviceName",serviceName);;
      reloadMyServiceList();
  }
  function reloadMyServiceList() {
    // 重新请求数据
    $("#myServiceList").DataTable().ajax.reload();
  }

  //弹窗提示样式
  function sticky(msg, style, position) {
    var type = style ? style : 'success';
    var place = position ? position : 'top';
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

  function renderOptions(data, type, full) {
	    var html = "<div>";
//	     html += '<a api="'+full+'" onclick="forSelect('+JSON.stringify(full).replace(/\"/g,"'")+')">选择</a>&nbsp;';
         html += '<a api="'+full+'" onclick="forSelect(\''+ data +'\')")">选择</a>&nbsp;';
	     html += '</div>';
	    return html;
	  }
  function renderName(data, type, full) {
      var html = '';
      html += '<a onclick="forView(\''+ full +'\')">'+ data + '</a>&emsp;';
      return html;
  }
  function forSelect(id){
      dialog.close(id);
      dialog.remove();
  }
</script>
</head>
<body>
	<div class="topdist"></div>
	<div class="container" style="width: 98%; padding-top:10px;">
		<div class="row">
			<form class="form-inline" onsubmit="return false;">
				<div class="input-group" style="width: 50%">
					<input class="form-control ue-form" type="text" id="serviceName" placeholder="请输入api名称"/>
					<div class="input-group-addon ue-form-btn" onclick="forQuery()">
						<span class="fa fa-search"></span>
					</div>
				</div>
			</form>
		</div>
		<div class="row">
			<table id="myServiceList" class="table table-bordered table-hover">
				<thead>
					<tr>
						<th width="5%" data-field="serviceId" data-render="renderId">序号</th>
						<th width="30%" data-field="serviceName" >服务名称</th>
						<th width="10%" data-field="version">版本</th>
						<th width="40%" data-field="desc">描述</th>
						<th width="12%" data-field="serviceId" data-render="renderOptions">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>