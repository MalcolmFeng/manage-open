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
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/register.css'/>"/>

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
        #groupSelect{
            width:100%;
            height: 98%;
        }
        .btn{
            float: right;
            margin: 10px;
        }
    </style>
</head>
<body>
<div>
    <div style="width: 60%; text-align: center;margin-left: 20%;">
        <div >
            <table id="myApiList" class="table table-bordered table-hover">
                <input class="btn ue-btn"type="button" value="发布API" onclick="saveAll()">
                <thead>
                <tr>
                    <th width="5%" data-field="id" data-render="renderCheckBox" >
                        <input type="checkbox" id="selectAll"/>
                    </th>
                    <th width="30%" data-field="name">API名称</th>
                    <th width="30%"  data-field="id" data-render="serviceRender">服务名称</th>
                    <th width="15%" data-field="id" data-render="renderOptions">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    var context = "<l:assetcontext/>";
    var url = context + "/service/open/api/getApiListByUserIdNew";
    var grid;
    var html="";
    $(document).ready(function() {
        ServiceGroupList();
        var options = {
            ordering: false
        };

        grid = new L.FlexGrid("myApiList", url);
        grid.init(options); //初始化datatable
    });
    function serviceRender(data, type, full){
        var htmlArr =html;
        htmlArr=htmlArr+('<input style="display: none" class="'+data+'">');
        return htmlArr;
    }

    function renderOptions(data, type, full) {
        var html = '';
        html += '<div>';
        html += '    <a onclick="save(\''+data+'\')">发布</a>';
        html += '</div>';
        return html;
    }
    function renderCheckBox(data, type, full){
        return '<input type="checkbox" value="' + data + '" name="id">';
    }
    function save(apiId){
        var groupId=$("."+apiId+"").prev().val();
        var subgroupId=$("#subgroupSelect").val();
        if(groupId == null || groupId == '') {
            sticky("请选择服务分组","error");
            return false;
        }
        $.ajax({
            type: "post",
            url: context + "/service/open/api/releaseApi",
            data:{id:apiId,groupId:groupId,subgroupId:subgroupId},
            success: function(msg) {
                if (msg ==true) {
                    sticky("发布成功","success");
                    grid.reload();
                    // setTimeout(function() {
                    //     window.location.href = context + "/service/open/api/getOpenApiPage";
                    // }, 1000);
                } else {
                    sticky(msg.msg, 'error', 'center');
                }
            }
        });
    };
    function saveAll() {
        var cs = $("input[type='checkBox']:checked").serialize();
        var ids = cs.split("&");
        var id = [];
        for (var i = 0; i < ids.length; i++) {
            var tempid = ids[i].split("=");
            id[i] = tempid[1];
        }
        if (id[0]!=undefined) {
            for (var i = 0; i < id.length; i++) {
                save(id[i]);
            }
            grid.reload();
        } else {
            sticky("请选择要发布的API","warning")
        }
    }
    function ServiceGroupList() {
        $.ajax({
            type: "post",
            url: context + "/service/service/group/list/-1",
            data:JSON.stringify({parentId:-1}),
            async:false,
            contentType:'application/json;charset=UTF-8',
            success: function(data) {
                html = template("grouplist2", data);
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
    /**更新弹窗提示*/
    function sticky(msg, style, position) {
        var type = style ? style : 'success';
        var place = position ? position : 'top';
        $.sticky(
            msg,
            {
                autoclose : 2000,
                position : place,
                style : type
            }
        );
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