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
    <title>API发布</title>
    
    <!-- 需要引用的CSS -->
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ztree.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/datadev.css'/>" />
   <link rel="stylesheet" type="text/css" href="<l:asset path='data/register_old.css'/>"/>

    <script type="text/javascript" src="<l:asset path='jquery.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>" ></script>
	<script type="text/javascript" src="<l:asset path='form.js'/>" ></script>
	<script type="text/javascript" src="<l:asset path='arttemplate.js'/>" ></script>	
	<script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ztree.js'/>"></script>

<title>数据编辑</title>
<style type="text/css">
	.Validform_input {
	    width: 50%;
	    margin-right: 10px;
	}
</style>
</head>
<body>
	<div>
	  <div class="col-xs-12 col-md-12" style="margin-top: 50px;margin-left: 200px;">

		<form class="form-horizontal" id="saveForm" name="saveForm" onsubmit="return false;">
			<div class="form-group">
				<input type="hidden" id="openServiceId" />
				<div class="col-xs-2 col-md-2 control-label">
					<label class="control-label">API列表<span class="required">*</span></label>
				</div>
				<div class="col-xs-10 col-md-10">
					<div id="apiDiv" style="width: 25%; display: inline-block"></div>
					<span class="Validform_checktip Validform_span" style="float: none"></span>
				</div>
			</div>
		    <div class="form-group">
		        <input type="hidden" name="groupId" value="${serviceDef.groupId }"/>
		    	<div class="col-xs-2 col-md-2 control-label">
    			    <label class="control-label">服务分组<span class="required">*</span></label>
    			</div>
    			<div class="col-xs-10 col-md-10">
    				<div id="groupDiv" style="width: 25%; display: inline-block"></div>
    				<div id="subgrouplistDiv" style="width: 25%; display:none"></div>
    				<span class="Validform_checktip Validform_span" style="float: none"></span>
    			</div>
		    </div>
			<div class="form-group" >
			  <label class="col-xs-2 col-md-2 control-label"></label>
			  <div class="col-xs-10 col-md-10">
				<input type="submit" class="btn ue-btn-primary" value="发布">&emsp;
				 <input id="goback" type="button" class="btn ue-btn" value="返回">
			  </div>
		  </div>
		  <input type="hidden" id="id" value="${serviceDef.id }" />
		</form>
      </div>
	</div>
	<script type="text/javascript">
	    var context = "<l:assetcontext/>";
	    $(function() {
	      ApiListByUserId();
	      ServiceGroupList();

	    $("#saveForm").uValidform({
            datatype: {
                "verifyGroup": verifyGroup
            },
	        callback:function(form){
	            $.dialog({
	                type: 'confirm',
	                content: '您确定要提交表单吗？',
	                ok: function () {
						save();
	                },
	                cancel: function () {}
	            });
	        }
	      });


	      $("#goback").click(function(){
	      	location.href=context + "/service/open/api/getOpenApiPage";
	      });
	    });


		function save(){
			var groupId=$("#groupSelect").val();
			var subgroupId=$("#subgroupSelect").val();
			var apiId=$("#openServiceId").val();

			$.ajax({
				type: "post",
				url: context + "/service/open/api/releaseApi",
				data:{id:apiId,groupId:groupId,subgroupId:subgroupId},
				success: function(msg) {
					if (msg ==true) {
                        $.dialog({
                            autofocus: true,
                            type: "alert",
                            content:"发布成功!"
                        });
                        setTimeout(function() {
                            window.location.href = context + "/service/open/api/getOpenApiPage";
                        }, 1000);
					} else {
						sticky(msg.msg, 'error', 'center');
					}
				}
			});
		};

	    function ApiListByUserId(){
            $.ajax({
                type: "post",
                url: context + "/service/open/api/getApiListByUserId",
                contentType:'application/json;charset=UTF-8',
                success: function(data) {
                    var html = template("apilist", data);
                    $("#apiDiv").empty().append(html);

                }
            });
		}
        function ServiceGroupList() {
            $.ajax({
                type: "post",
                url: context + "/service/service/group/list/-1",
                data:JSON.stringify({parentId:-1}),
                contentType:'application/json;charset=UTF-8',
                success: function(data) {
                    var html = template("grouplist2", data);
                    $("#groupDiv").empty().append(html);
                    var groupId=null;
                    if(groupId != null && groupId != '') {
                        setGroupId(groupId);
                    }
                    var parentId=null;
                    loadSubServiceGroupList(parentId);
                }
            });
        };
        function  loadSubServiceGroupList(parentId) {
            // 清空子分组列表
            $("#subgrouplistDiv").empty();
            var groupId = $("#groupSelect option:selected").val();//获取父组
            $("#groupId").val(groupId);
            if(groupId != null && groupId != '') {
                $.ajax({
                    type: "post",
                    url: context + "/service/service/group/list/" + groupId,
                    data:JSON.stringify({parentId:groupId}),
                    contentType:'application/json;charset=UTF-8',
                    success: function(data) {
                        if(data != null && data.data != null && data.data.length != 0) {
                            var html = template("subgrouplist2", data);
                            $("#subgrouplistDiv").empty().append(html);
                            $("#subgrouplistDiv").css("display","inline-block");
                            if(parentId){//更新数据的情况
                                $("#subgroupSelect").val(defGroupId);
                                $("#groupId").val(defGroupId);
                            }else{
                                $("#groupId").val(data.data[0].id);//默认赋值第一个子组
                            }
                        }else{
                            $("#subgrouplistDiv").css("display","none");
                        }
                    }
                });
            }
        };
        function selectServiceGroup(obj) {
            $("#groupId").val($("#subgroupSelect").val());
        };

        function setGroupId(groupId) {
            if(groupId != null && groupId != '') {
                $.ajax({
                    type: "get",
                    url: context + "/service/service/group/get/" + groupId,
                    success: function(data) {
                        if(data != null && data.parentId != null) {
                            if("-1"==data.parentId){
                                $("#groupSelect").val(groupId);
                            }else{
                                $("#groupSelect").val(data.parentId);
                                loadSubServiceGroupList(data.parentId);
                            }
                        }
                    }
                });
            }
        };

        function loadApiList() {
		$("#openServiceId").val($("#apiSelect").val());
        };
	    function verifyGroup(gets, obj, curform, datatype) {
	      if(gets == null || gets == '') {
	        obj.attr("errormsg", "请选择服务分组");
	        return false;
	      }
	    }
	</script>

	<script id="apilist" type="text/html">
		<select id="apiSelect" class="form-control ue-form" onchange="loadApiList();" datatype="s" nullmsg="必填">
			<option value="">请选择API</option>
			{{each data as api}}
			<option value="{{api.id}}">{{api.name}}</option>
			{{/each}}
		</select>
	</script>

	<script id="grouplist2" type="text/html">
		<select id="groupSelect" class="form-control ue-form" onchange="loadSubServiceGroupList();" datatype="s" nullmsg="必填">
			<option value="">请选择分组</option>
			{{each data as group}}
			<option value="{{group.id}}">{{group.name}}</option>
			{{/each}}
		</select>
	</script>

	<script id="subgrouplist2" type="text/html">
		<select id="subgroupSelect" class="form-control ue-form" onchange="selectServiceGroup();">
			{{each data as group}}
			<option value="{{group.id}}">{{group.name}}</option>
			{{/each}}
		</select>
	</script>
</body>
</html>