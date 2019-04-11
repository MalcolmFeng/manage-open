<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>api发布</title>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <style type="text/css">
        .Validform_input {
            width: 85%;
        }
        .Validform_right {
            width: 10%;
        }
        .control-label {
            text-align: right;
        }
    </style>
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->

    <script  type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
    <script  type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
    <script  type="text/javascript" src="<l:asset path='form.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='arttemplate.js'/>" ></script>
    <script  type="text/javascript" src="<l:asset path='jquery.form.js'/>"></script>
    <script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript">
        var context = "<l:assetcontext/>";
       //console.log(<%=request.getParameter("openServiceId")%>);
        $(function() {
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
            $("#saveForm").uValidform({
                btnSubmit:"#saveBtn",
                callback:function(form){
                    var id=$("#openServiceId").val();
                    save(id);
                }
            });

            $("#closeBtn").click(function(){
                parent.dialog.get(window).close(false);
            });

        });

        function save(id){
            var groupId=$("#groupSelect").val();
            $.ajax({
                type: "post",
                url: context + "/service/open/api/releaseApi",
                //data: $("form").serialize(),
                data:{id:id,groupId:groupId},
                success: function(msg) {
                    if (msg ==true) {
                        parent.dialog.get(window).close(true);
                    } else {
                        sticky(msg.msg, 'error', 'center');
                    }
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
        };
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

</head>
<body>

<div class="container" id="sandbox-container">
    <div class="col-xs-12 col-md-12">
        <form class="form-horizontal" id="saveForm" name="saveForm" onsubmit="return false">
           <%-- <input type="hidden" value="${param.pid }" name="parentId" />--%>
            <input type="hidden" id="openServiceId" value="${param.openServiceId }" />
            <div class="form-group">
                <div class="col-xs-3 col-md-3 control-label">
                    <label class="control-label">服务分组<span class="required">*</span></label>
                </div>
                <div class="col-xs-9 col-md-9">
                    <div id="groupDiv" style="width: 45%; display: inline-block"></div>
                    <div id="subgrouplistDiv" style="width: 35%; display:none"></div>
                    <span class="Validform_checktip Validform_span" style="float: none"></span>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-3 col-md-3 control-label"></label>
                <div class="col-xs-9 col-md-9">
                    <button type="button" class="btn ue-btn-primary" id="saveBtn">发布</button>&ensp;
                    <button type="button" class="btn ue-btn" id="closeBtn">取消</button>
                    <span id="msgdemo"></span>
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>
