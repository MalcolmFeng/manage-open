var pageSize=9;
$(function() {
    initServiceList(0);
    $("#groupList .list-inline").on("click","li",function(){
        $(this).addClass("activedlink").siblings().removeClass("activedlink");
    });
    //清空子分组
    $("#subGroupList").empty();

});
function todetail(data) {
    window.location.href = context + "/jsp/data/data/getDetail.jsp?remoteId="+data;
}
function pageSelectCallback(curPage, pagination) {
    initServiceList(curPage,"page");
}

function renderGroup(groupId) {
    $("#" + groupId).addClass("activedlink").siblings().removeClass("activedlink");
}

//查询框回车键
$("#serviceName").bind('keypress',function(event) {
    if (event.keyCode == "13") {
        forQuery();
    }
});

function forQuery() {
    initServiceList(0);
}

function queryAll() {
    $("#groupId").val("");
    initServiceList(0);
    //清空子分组
    $("#subGroupList").empty();
}


function setPaginationInfo(curPage,pageCount) {
    var html = '<span>共&nbsp;'+pageCount+'&nbsp;页,当前第&nbsp;' + curPage + '&nbsp;页</span>';
    $(".pagination_info").empty().append(html);
}

//初始化服务列表
function initServiceList(curPage,isPage) {
    var start=pageSize*curPage;
    var param = {
        start: start,
        limit:pageSize,
        dataTotal:true,
        groupId: $("#groupId").val(),
        name: $("#serviceName").val()
    };
    $.ajax({
        type: "post",
        url: context + "/service/open/data/getApplyList",
        data: param,
        async:false,
        dataType:"json",
        success: function(result) {
            console.log(result);
            var html = '';
            if(result['data'] != null && result['data'] != '') {
                html = template('servicelist', result);
            } else {
                html = template('emptylist');
            }
            $("#openServiceList").empty().append(html);
            // 设置页面高度
            setPageHeight();
            //重新显示分页
            var totalPage=0;
            if(result.total>0){
                totalPage=  result.total%pageSize==0?result.total/pageSize:parseInt(result.total/pageSize)+1
                setPaginationInfo(curPage+1,totalPage);
            }else{
                setPaginationInfo(0,totalPage);
            }
            if(!isPage){
                initPagination = function() {
                    $(".pagination").pagination(totalPage, {
                        num_edge_entries: 1,
                        num_display_entries: 4,
                        items_per_page:1,
                        callback: pageSelectCallback
                    });
                }();
            }
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
        url: context + "/service/data/group/list/"+groupId,
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
    window.location.href = context + "/service/open/data/get/" + openServiceId+"/apply";
}
function loadServiceInfo(openServiceId) {
    window.location.href = context + "/service/open/data/get/" + openServiceId+"/apply";
}