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
    <title>授权app列表</title>
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
    var isInit=false;
    var options={};
    $(document).ready(function() {
        options = {
           ordering: false
       };      
        var url = context+"/service/open/api/getAppByAuth";
        grid = new L.FlexGrid("appList",url);
        grid.setParameter("openServiceId", '${param.openServiceId }');
        grid.setParameter("applyFlag",'${param.applyFlag}');
//        grid.init(options); //初始化datatable
    });
    
    function reloadAppList() {
        if(isInit){
            // 重新请求数据
            $("#appList").DataTable().ajax.reload();
        }else{
            isInit=true;
            grid.init(options);
        }

    }
    
    function renderId(data, type, full, meta) {
      var rowId = meta.settings._iDisplayStart + meta.row + 1;
      return rowId;
    }

    function renderOperations(data, type, full) {
        var html = '<div>';
      if(full.applyFor == false) {
        html += '<a onclick="applyService(\''+ full.appId +'\',\''+ full.userId +'\', \''+ full.appName +'\')">授权</a>';
      } else {
        html += '已授权';
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
        url: context + "/service/open/apply/auth",
        data: param,
        success: function(result) {
          if(result == true) {
            sticky("授权成功");
            reloadAppList();
          } else {
            sticky("授权失败", "error");
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
        var selectType=$("#selectType").val();
        var value = $("#appName").val();
        if(selectType==0){
            grid.setParameter("appName",value);
        }else if(selectType==1){
            if(!value){
                sticky("请输入应用KEY","error")
                return;
            }
            grid.setParameter("appKey",value);
        }else if(selectType==2){
            if(!value){
                sticky("请输入用户名","error")
                return;
            }
            grid.setParameter("user",value);
        }
        grid.setParameter("selectType",selectType);
        reloadAppList();
    }
    function change(){
        var selectType=$("#selectType").val();
        if(isInit){//清除已有数据
            $("#appList tbody").html("");
        }
        if(selectType==0){
            $("#appName").attr("placeholder","请输入应用名称");
        }else if(selectType==1){
            $("#appName").attr("placeholder","请输入应用KEY");
        }else if(selectType==2){
            $("#appName").attr("placeholder","请输入用户名");
        }
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
        <div class="row" style="margin-bottom: 2px">
            <form class="form-inline" onsubmit="return false;">
                <div class="input-group">
                    <select id="selectType" class="form-control ue-form" onchange="change()">
                        <option value="0" selected="selected">我的应用</option>
                        <option value="1" >通过应用KEY</option>
                        <option value="2">通过平台用户</option>
                    </select>
                </div>
                <div class="input-group" style="margin-left: 150px;margin-top: -30px;width: 40%">
                    <input class="form-control ue-form" type="text" id="appName" placeholder="请输入应用名称" />
                    <div class="input-group-addon ue-form-btn" id="" onclick="forQuery()">
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