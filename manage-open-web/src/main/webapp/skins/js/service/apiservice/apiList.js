$(function() {
    initServiceList();
    $("#groupList .list-inline").on("click","li",function(){
        $(this).addClass("activedlink").siblings().removeClass("activedlink");
    });
    //清空子分组
    $("#subGroupList").empty();

    //查询框回车键
    $("#serviceName").bind('keypress',function(event) {
        if (event.keyCode == "13") {
            forQuery();
        }
    });
});

function renderGroup(groupId) {
    $("#" + groupId).addClass("activedlink").siblings().removeClass("activedlink");
}

function forQuery() {
    initServiceList();
}

function queryAll() {
    $("#groupId").val("");
    initServiceList();
    //清空子分组
    $("#subGroupList").empty();
}

//初始化服务列表
function initServiceList() {
    var param = {
        groupId: $("#groupId").val(),
        name: $("#serviceName").val()
    };
    $.ajax({
        type: "post",
        url: "/manage-open/service/open/api/getApplyList",
        data: param,
        async:false,
        dataType:"json",
        success: function(result) {
            var temp = template("apiListTemp", {"APIList": result.data});
            $("#APIList>ul").empty().append(temp);

        }
    });
}
function setPageHeight() {
    var height = $(".ue-menu-right .container").innerHeight();
    $(".ue-menu-right").height(height + 50);
}

function reloadServiceListByGroupId(groupId) {
    $("#groupId").val(groupId);
    initServiceList(0);
    //清空子分组
    $("#subGroupList").empty();
    //加载子分组
    loadSubGroup(groupId);
}

function loadSubGroup(groupId) {
    $.ajax({
        type: "post",
        url: "/manage-open/service/service/group/list/"+groupId,
        data: JSON.stringify({parentId:groupId}),
        contentType:"application/json;charset=utf-8",
        success: function(result) {
            var html = '';
            if(result['data'] != null && result['data'] != '') {
                html = template('subGroupListTemp', result);
                $("#subGroupList").empty().append(html);
            }
        }
    });
}

function reloadServiceListBySubGroupId(groupId) {
    $("#groupId").val(groupId);
    initServiceList(0);
}

function loadAppList(openServiceId) {
    var apply_Flag="0";
    $.dialog({
        type: "iframe",
        title: "应用列表",
        url: context + '/jsp/service/applist/apply_list.jsp?openServiceId=' + openServiceId+"&applyFlag="+apply_Flag,
        width: 700,
        height: 400
    });
}

// 查看api详情
function getApiDetail(id, obj) {
    $(".lg-link").removeClass("clicked");
    $(obj).addClass("clicked");
    $.ajax({
        url: "/manage-open/service/open/api/get/"+id+"/info",
        success: function(result) {
            if(result.apply) {
                var inputParam = result.inputParam;
                var serviceInfo = result.serviceInfo;
                var outputParam = result.outputParam;

                // 加载接口信息
                $("#title-en").text(serviceInfo.openAddr);
                $("#title-ch").text(serviceInfo.name);
                $("#description").text(serviceInfo.description);
                if(serviceInfo.authType == "1") {
                    $("#api-needAuth").text("需要授权");
                } else {
                    $("#api-needAuth").text("无需授权");
                }

                var temp = template("inputParamTemp", {"inputParamList": inputParam});
                $("#inputParamTbody").empty().append(temp);

                try{
                var returnExample=JSON.parse(serviceInfo.returnSample);
                $("#returnExample").JSONView(returnExample, {collapsed: false, nl2br: true});
                }catch(e){
                    document.getElementById("returnExample").innerHTML = serviceInfo.returnSample ;
                }

                var tempOut = template("outputParamTemp", {"outputParamList": outputParam});
                $("#responseParamTbody").empty().append(tempOut);

            }

        }
    });
}