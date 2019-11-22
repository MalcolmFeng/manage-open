var pageSize=9;
var detailData=[];
$(function() {
    initServiceList(0);
    $("#groupList .list-inline").on("click","li",function(){
        $(this).addClass("activedlink").siblings().removeClass("activedlink");
    });
    //清空子分组
    $("#subGroupList").empty();
    /* 初始化左侧菜单 */
    changeCatalogInfo();
    /* 初始化详细资源信息 */
    getDetailResourceInfo(0);
    /* 侧导航滚动条 */
    initBoxScroll();
    /* 切换左侧菜单 */
    $(document).on("click",".sub-catalog", changeCatalogInfo);
    /* 点击左侧菜单显示具体资源列表 */
    $(document).on("click",".catalog-body>li", getDetailResourceInfo(0));
    /* 点击资源列表名称 */
    $(document).on("click",".content-list a", showDetailResourceInfo);


});
$(document).on("click",".catalog-body>li",function(){
    $(this).addClass("activeTitle").siblings().removeClass("activeTitle");
    // $(".catalog-body>li>a").eq(0).css({"color":"#428bca"})
    getDetailResourceInfo(0);
});
function todetail(data) {
    window.location.href = context + "/jsp/data/data/getDetail.jsp?remoteId="+data;
}
function pageSelectCallback(curPage, pagination) {
    initServiceList(curPage,"page");
    getDetailResourceInfo(curPage,"page");
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
            var html = '';
            if(result['data'] != null && result['data'] != '') {
                html = template('detailTemp', result.data);
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

/* 侧导航滚动条 */
function initBoxScroll () {
    $(".catalog-body").slimscroll({
        height: 355,
        size: "5px",
        color: "#949faa",
        distance: "2px",
        wheelStep: "12px",
        railVisible: true,
        railColor: "#ecf0f6",
        railOpacity: 1,
        allowPageScroll: true
    });
}

/* 切换左侧菜单 */
function changeCatalogInfo() {
    var elementAttr = $(this).attr("data-attr") || "data-catalog";
    $(this).addClass("active");
    $(this).siblings().removeClass("active");
    if(elementAttr == "data-catalog") {
        var catalogParam = {
            "total": 4,
            "data": [
                {
                    "remoteId": "ff8080816e725c2b016e72688fd70001",
                    "HasResource": true,
                    "catalogId": "ff8080816e725c2b016e725c2b1c0000",
                    "isConnected": "-1",
                    "isDataSource": true,
                    "name": "电子病历资源库",
                    "resourceNum": "5"
                },
                {
                    "remoteId": "ff8080816e73cd2f016e73cd2fd30000",
                    "HasResource": true,
                    "catalogId": "ff8080816e725c2b016e725c2b1c0000",
                    "isConnected": "-1",
                    "isDataSource": true,
                    "name": "健康档案资源库",
                    "resourceNum": "7"
                },
                {
                    "remoteId": "ff8080816e73cd2f016e73ceab910001",
                    "HasResource": false,
                    "catalogId": "ff8080816e725c2b016e725c2b1c0000",
                    "isConnected": "-1",
                    "isDataSource": true,
                    "name": "全员人口资源库",
                    "resourceNum": "1"
                },
                {
                    "remoteId": "ff8080816e81c020016e81c020850000",
                    "HasResource": false,
                    "catalogId": "ff8080816e725c2b016e725c2b1c0000",
                    "isConnected": "-1",
                    "isDataSource": true,
                    "name": "基础资源库",
                    "resourceNum": "0"
                }
            ]
        };
        var temp = template("catalogTemp",catalogParam);
        $(".catalog-body").empty().append(temp);
    } else if(elementAttr == "data-market") {
        var param = {
            start:0,
            limit:9,
            dataTotal:true,
            groupId:'',
            name:''
        };
        $.ajax({
            type: "post",
            url: context + "/service/open/data/getApplyList",
            data: param,
            async: false,
            dataType:"json",
            success: function (data) {
                var resourceNum = data.data;
                var resourceNumSet = [];
                for(var i = 0; i < resourceNum.length; i++) {
                    resourceNum[i]["resourceNum"] = resourceNum[i].tableName.split(",").length;
                }
                resourceNum[1]["resourceNum"]=0;
                var temp = template("catalogTemp",data);
                $(".catalog-body").empty().append(temp);
            },
            error: function () {}
        });
    }
    getDetailResourceInfo(0);
}

/* 点击显示具体数据资源 */
function getDetailResourceInfo(curPage,isPage) {
    var start=pageSize*curPage;
    var catalogName = $(".sub-catalog.active").attr("data-attr");
    var dataId = "";
    if(catalogName == "data-catalog") {
        dataId = $(".activeTitle").children().eq(0).attr("data-id")|| "ff8080816e725c2b016e72688fd70001";
        var params = {
            "params": {
                "javaClass": "ParameterSet",
                "map": {
                    "needTotal": true,
                    "DATA_SOURCE_ID": ignoreSpaces(dataId),
                    "start": start,
                    "limit": pageSize
                },
                "length": 7
            },
            "context": {
                "javaClass": "HashMap",
                "map": {},
                "length": 0
            }
        };
        $.ajax({
            url: getRootPath() + "/odmgr/command/ajax/com.inspur.od.dataSource.cmd.DataSourceQueryCmd/getReourceByDataSource",
            data : JSON.stringify(params),
            type : "POST",
            contentType : "application/json",
            success: function (data) {
                detailData = JSON.parse(data);
                var temp = template("detailTemp",detailData);
                $(".content-body").empty().append(temp);
                $("#total-catalog").empty().append(detailData.total);
                setPageHeight();
                //重新显示分页
                var totalPage=0;
                if(detailData.total>0){
                    totalPage=  detailData.total%pageSize==0?detailData.total/pageSize:parseInt(detailData.total/pageSize)+1
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
            },
            error: function () {}
        });
    } else if (catalogName == "data-market") {
        dataId =  $(".activeTitle").children().eq(0).attr("data-id") || "ff8080816e7d1bb0016e7d1d139c0001";
        $.ajax({
            type: "get",
            url: context + "/service/open/data/getTableInfo?remoteId=" + ignoreSpaces(dataId),
            dataType : "json",
            contentType: "application/json",
            success: function (data) {
                var detailData = JSON.parse(data.json);
                var dataSet = {
                    "rows": []
                };
                dataSet.rows = detailData;
                var temp = template("detailTemp",dataSet);
                $(".content-body").empty().append(temp);
                $("#total-catalog").empty().append(detailData.length);

                setPageHeight();
                //重新显示分页
                var totalPage=0;
                if(detailData.length>0){
                    totalPage=  detailData.length%pageSize==0?detailData.length/pageSize:parseInt(detailData.length/pageSize)+1
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
            },
            error: function () {}
        });
    }
}

function showDetailResourceInfo() {
    var dataSourceId = $(this).attr("data-id");
    $.dialog({
        type: 'iframe',
        url: getRootPath() + '/odmgr/jsp/od/dataResource/browse/detail/resourceDetail.jsp?resourceId=' + dataSourceId + '&source=manage',
        title: '资源详情',
        width: 700,
        height: 450,
        onshow: function () {
        },
        onclose: function () {

        },
        oniframeload: function () {
        }
    });
}

function getRootPath(){
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPath=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return localhostPath;
}

function ignoreSpaces(string) {
    var temp = "";
    string = '' + string;
    var splitstring = string.split(" ");
    for(var i = 0; i < splitstring.length; i++)
        temp += splitstring[i];
    return temp;
}
