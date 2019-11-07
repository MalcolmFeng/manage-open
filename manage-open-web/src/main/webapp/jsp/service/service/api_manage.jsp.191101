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
	<title>API管理</title>

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
        $(document).ready(function() {
            var options = {
                info: true,
                ordering: false
            };

            var url = context + "/service/open/api/listOnlineAPI"
            grid = new L.FlexGrid("myauthList",url);
            grid.init(options); //初始化datatable
        });

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


        function offline(openServiceId) {
            $.dialog({
                type: 'confirm',
                content: '确认api下线请求?',
                autofocus: true,
                ok: function() {
                    $.ajax({
                        url : context + "/service/open/api/offline/" + openServiceId,
                        success: function(msg){
                            if(msg == true) {
                                reloadAuditList();
                                sticky("下线成功！");
                            } else {
                                sticky("下线失败", 'error', 'center');
                            }
                        }
                    });
                },
                cancel: function(){}
            });
        }
        function forQuery() {
            var serviceName = $("#serviceName").val();
            grid.setParameter("name", serviceName);
            reloadAuditList();
        }
        function reloadAuditList() {
            // 重新请求数据
            $("#myauthList").DataTable().ajax.reload();
        }
        function renderId(data, type, full, meta) {
            var rowId = meta.settings._iDisplayStart + meta.row + 1;
            return rowId;
        }

        function renderOptions(data, type, full) {
            var html = '';
            html += '<div>';
           // html += '    <a onclick="pass(\''+data+'\')">查看</a>&emsp;';
            html += '    <a class="del" onclick="offline(\''+ data +'\')">下线</a>';
            html += '</div>';
            return html;
        }
        function renderName(data, type, full) {
            var html = '';
            html += '<a onclick="forView(\''+ full.id +'\')">'+ data + '</a>&emsp;';
            return html;
        }

        function forView(id) {
            window.location.href = context + "/service/open/api/getInfo/" + id;
        }

	</script>
</head>
<body>
<div class="topdist"></div>
<div class="container" style="width: 98%; padding-top:10px;">
	<div class="row">
		<form class="form-inline" onsubmit="return false;">
			<div class="input-group">
				<input class="form-control ue-form" type="text" id="serviceName" placeholder="请输入api名称"/>
				<div class="input-group-addon ue-form-btn" onclick="forQuery()">
					<span class="fa fa-search"></span>
				</div>
			</div>
		</form>
	</div>
	<div class="row">
		<table id="myauthList" class="table table-bordered table-hover">
			<thead>
			<tr>
				<th width="5%" data-field="id" data-render="renderId">序号</th>
				<th width="25%" data-field="name" data-render="renderName">服务名称</th>
				<th width="20%" data-field="provider">服务发布人</th>
				<th width="20%" data-field="description">服务描述</th>
				<th width="15%" data-field="createTime">创建时间</th>
				<th width="15%" data-field="id" data-render="renderOptions">操作</th>
			</tr>
			</thead>
		</table>
	</div>
</div>
</body>
</html>