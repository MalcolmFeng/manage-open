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
<title>数据分组列表</title>
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
var pid = '<%=request.getParameter("pid")%>';
$(document).ready(function() {
    var options = {
        ordering: false
    };
    
	var url = context+"/service/data/group/list/" + (pid == 'null' ? '-1' : pid);
	grid = new L.FlexGrid("groupList",url); 
	grid.init(options); //初始化datatable
	
	// 增加
	$("#add").bind("click", add);
	// 返回
	$("#back").bind("click", function() {
	  window.location.href = context + "/jsp/data/group/list.jsp";
	});
});

function manageChildren(groupId) {
    window.location.href=context+"/jsp/data/group/list.jsp?pid=" + groupId;
} 

function del(groupId) {
    $.dialog({
		type: 'confirm',
		content: '确认删除该记录?',
	    autofocus: true,
		ok: function() {
		  $.ajax({
		    url : context + "/service/data/group/delete/" + groupId,
		    success: function(msg){
		      if(msg == true) {
		        reloadGroupList();
		        sticky("删除成功！");
		      } else {
		        sticky("删除失败", 'error', 'center');
		      }
		    }
		  });
		},
		cancel: function(){}
    });
}

function reloadGroupList() {
  // 重新请求数据
  $("#groupList").DataTable().ajax.reload();
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

function add() {
    $.dialog({
		type: "iframe",
		title: "添加服务分组",
		url: context + '/jsp/data/group/add.jsp?pid=' + (pid == 'null' ? '-1' : pid),
		width: 450,
		height: 250,
        onclose: function () {
            reloadGroupList();
            if(this.returnValue){
           	 	sticky("保存数据成功！");
            }
		}
	});
}

function modify(groupId){
	if(typeof(groupId) != 'undefined'){
	    $.dialog({
			type: "iframe",
			title: "编辑数据分组",
			url: context + '/service/data/group/edit/' + groupId,
			width: 420,
			height: 200,
	        onclose: function () {
	            reloadGroupList();
	            if(this.returnValue){
	           	 	sticky("编辑数据成功！");
	            }
			}
		});
	}
}
   
function renderid(data, type, full, meta) {
  var rowId = meta.settings._iDisplayStart + meta.row + 1;
  return rowId;
}

function renderoptions(data, type, full) {
    var html = "<div>";
    if(pid == 'null' || pid == 0) {
        html += '<a onclick="manageChildren(\'' + data + '\')">管理子分组 </a>&emsp;'
    }
    html += '<a onclick="modify(\''+data+'\')">编辑</a>&emsp; ' + 
        '<a class="del" onclick="del(\''+ data +'\')">删除</a></div>';
    return html;
}

</script>
</head>
<body>
	<div class="topdist"></div>
	<div class="container" style="width: 96%; padding-top:10px;">
	   <div class="row">
		<form class="form-inline" onsubmit="return false;">										
		    <div class="btn-group pull-right">
				<button id="add" type="button" class="btn ue-btn">
					<span class="fa fa-plus"></span>增加
				</button>
				<c:if test="${not empty param.pid }">
					<button id="back" type="button" class="btn ue-btn">
						<span class="fa fa-reply"></span>返回
					</button>
				</c:if>
			</div>
		</form>
	</div>
	<div class="row">
		<table id="groupList" class="table table-bordered table-hover">
			<thead>
				<tr>
					<th width="5%" data-field="id" data-render="renderid">序号</th>
					<th width="35%" data-field="name">分组名称</th>
					<th width="35%" data-field="seq">显示顺序</th>
					<th width="25%" data-field="id" data-render="renderoptions">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	</div>
</body>
</html>