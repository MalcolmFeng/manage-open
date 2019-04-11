<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>我的应用列表</title>
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
           ordering: false
       };      
        var url = context+"/service/open/api/getAppByUserId";
        grid = new L.FlexGrid("appList",url);
        grid.setParameter("openServiceId", '${param.openServiceId }');
        grid.setParameter("applyFlag",'${param.applyFlag}');
        grid.init(options); //初始化datatable
    });
    
    function reloadAppList() {
      // 重新请求数据
      $("#appList").DataTable().ajax.reload();
    }
    
    function renderId(data, type, full, meta) {
      var rowId = meta.settings._iDisplayStart + meta.row + 1;
      return rowId;
    }

    function renderOperations(data, type, full) {
        var html = '<div>';
      if(full.applyFor == false) {
        html += '<a onclick="applyService(\''+ full.appId +'\',\''+ full.userId +'\', \''+ full.appName +'\')">申请</a>';
      } else {
        html += '已申请';
      }
      html += '</div>';
      return html;
    }
    
    function applyService(appId,userId, appName) {
      var param = {
        openServiceId: $("#openServiceId").val(),
        applyFlag:$("#applyFlag").val(),
        appId:appId,
        userId: userId,
        appName: appName
      };
      
      $.ajax({
        type: "post",
        url: context + "/service/open/apply/apply",
        data: param,
        success: function(result) {
          if(result == true) {
            sticky("申请成功");
            reloadAppList();
          } else {
            sticky("申请失败", "error");
          }
        }
      });
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

    function forQuery() {
        var appName = $("#appName").val();
        grid.setParameter("appName",appName);
        reloadAppList();
    }
</script>
<style>

		table.dataTable thead>tr {
			height: 30px;
		}
		table.dataTable thead>tr>th {
			line-height: 45px;
			height: 45px;
		}
		.table-bordered>tbody>tr>td {
			height: 30px;
			line-height: 30px;
			white-space: nowrap;
			overflow: hidden;
		}

	


</style>


</head>
<body>
    <div class="container">
        <div class="row">
            <form class="form-inline" onsubmit="return false;">
                <div class="input-group" style="width:40%">
                    <input class="form-control ue-form" type="text" id="appName" placeholder="请输入应用名称"/>
                    <div class="input-group-addon ue-form-btn" onclick="forQuery()">
                        <span class="fa fa-search"></span>
                    </div>
                </div>
            </form>
        </div>
      <div class="row">
       <input type="hidden" id="openServiceId" value="${param.openServiceId }" />
       <input type="hidden" id="applyFlag" value="${param.applyFlag}" />
        <table id="appList" class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th width="10%" data-field="appId" data-render="renderId">序号</th>
                    <th width="30%" data-field="appName">应用名称</th>
                    <th width="25%" data-field="userId">创建者</th>
                    <th width="20%" data-field="appCreateTime">创建时间</th>
                    <th width="15%" data-field="userId" data-render="renderOperations">操作</th>
                </tr>
            </thead>
        </table>
    </div>
  </div>
</body>
</html>