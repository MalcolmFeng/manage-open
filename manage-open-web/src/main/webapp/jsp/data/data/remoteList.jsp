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
    <title>远程数据列表</title>
    
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
  var clusterId='${clusterList[0].clusterId}';
  var clusterName='${clusterList[0].clusterName}';
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
	      ordering: false
	  };
	  var url = context + "/service/open/data/remoteList"
	  serviceGrid = new L.FlexGrid("myServiceList", url);
      serviceGrid.setParameter("clusterId",clusterId);
      serviceGrid.setParameter("clusterName",clusterName);
	  serviceGrid.init(options); //初始化datatable
  }
  
  function forView(def) {
      return;
      $.ajax({
          type: "post",
          url: context + "/service/data/group/list/" + groupId,
          data:JSON.stringify({parentId:groupId}),
          contentType:'application/json;charset=UTF-8',
          success: function(data) {
              if(data != null && data.data != null && data.data.length != 0) {
                  var html = template("subgrouplist2", data);
                  $("#subgrouplistDiv").empty().append(html);
                  $("#subgrouplistDiv").css("display","inline-block");
                  if(parentId){//更新数据的情况
                      $("#subgroupSelect").val(defGroupId);
                      $("input[name=groupId]").val(defGroupId);
                  }else{
                      $("input[name=groupId]").val(data.data[0].id);//默认赋值第一个子组
                  }
              }else{
                  $("#subgrouplistDiv").css("display","none");
              }
          }
      });

    window.location.href = context + "/service/open/data/get/" + id;
  }
  
  function forRegister() {
    window.location.href = context + "/service/open/data/release?goback=true";
  }
  
  function forQuery() {
      var clusterId=$("#cluster").val();
	  var clusterName=$('#cluster option:selected').text();;
      serviceGrid.setParameter("clusterId",clusterId);
      serviceGrid.setParameter("clusterName",clusterName);
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
	     html += '<a onclick="forView(\'' + full + '\')">发布</a>&nbsp;';
	     html += '</div>';
	    return html;
	  }
  function renderName(data, type, full) {
      var html = '';
      html += '<a onclick="forView(\''+ full +'\')">'+ data + '</a>&emsp;';
      return html;
  }
</script>
</head>
<body>
	<div class="topdist"></div>
	<div class="container" style="width: 98%; padding-top:10px;">
		<div class="row">
			<form class="form-inline" onsubmit="return false;">
			    <select id="cluster" class="form-control ue-form" onchange="forQuery()">
					<c:forEach items="${clusterList}" var="cluster">
						<option value="${cluster.clusterId }">${cluster.clusterName}</option>
					</c:forEach>
			    </select>
				<%--<div class="btn-group pull-right">--%>
					<%--<button id="add" type="button" class="btn ue-btn" onclick="forRegister()">--%>
						<%--<span class="fa fa-plus"></span>发布服务--%>
					<%--</button>--%>
				<%--</div>--%>
			</form>
		</div>
		<div class="row">
			<table id="myServiceList" class="table table-bordered table-hover">
				<thead>
					<tr>
						<th width="5%" data-field="id" data-render="renderId">序号</th>
						<th width="20%" data-field="name"  data-render="renderName">数据名称</th>
						<th width="20%" data-field="tableName">表名称</th>
						<th width="30%" data-field="description">数据描述</th>
						<th width="12%" data-field="id" data-render="renderOptions">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>