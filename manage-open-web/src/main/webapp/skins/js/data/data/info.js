$(function() {
//	  	   loadServiceApplyCount(openServiceId);
    // 切换tab页
    $(document).on("click", "#myTab>li", switchTabClick);

    $(document).on("click", "#tableNameList>li", tableLiClick);

});

// 切换tab页
function switchTabClick() {
    var tabpage = $(this).find("a").attr("data-tab");
    if(tabpage == "overview") {
        $("#overview").show();
        $("#sjlb").hide();
    } else if(tabpage == "sjlb") {
        $("#overview").hide();
        $("#sjlb").show();
        // 初始表格
        initTableName();
    }
}

// 初始数据队列中的表
function initTableName() {
    $.ajax({
        url: context + "/service/open/data/getTableInfo?remoteId=" + remoteId,
        success: function(data) {
            var dataSet = eval(data.json);
            if(dataSet.length > 0) {
                var temp= template("tableTemp", {"tableList": dataSet});
                $("#tableNameList").empty().append(temp);

                $("#tableNameList>li:first").addClass("active");
                var resourceId =  $("#tableNameList>li:first").attr("data-resourceid");
                initTableCol(resourceId);
            }
        }
    });
}

// 切换表格
function tableLiClick() {
    $(this).addClass("active").siblings().removeClass("active");
    var resourceId = $(this).attr("data-resourceid");
    // 加载数据项信息
    initTableCol(resourceId);
}

// 加载数据项信息
function initTableCol(resourceId) {
    $.ajax({
        url: context + "/service/open/data/getItemsByResourceId?resourceId=" + resourceId,
        success: function(data) {
            var dataSet = eval("("+data.json+")");
            if(dataSet.recordSet.length > 0) {
                var colSet = dataSet.recordSet;
                $("#colDataList>thead>tr").empty();
                for(var i = 0; i < colSet.length; i++) {
                    var html = '<th>'+colSet[i].data.columnName+'</th>';
                    $("#colDataList>thead>tr").append(html);
                }

                var temp= template("colDataTableTemp", {"colList": dataSet.recordSet});
                $("#colDataTable>tbody").empty().append(temp);

                $("#colDataList>tbody").empty();
                initColData(resourceId, colSet);
            }
        }
    });
}

// 加载数据列
function initColData(resourceId, colSet) {
    $.ajax({
        url: context + "/service/open/data/getDataByResourceId?DATA_RESOURCE_ID="+resourceId+"&limit=10",
        success: function(data) {
            var dataSet = eval("("+data.json+")");
            $("#totalCount").text(dataSet.count);
            if(dataSet.recordSet.length > 0) {
                var colDataSet = dataSet.recordSet;
                for(var i = 0; i < colDataSet.length; i++) {
                    var dataList = [];
                    for(var j = 0; j < colSet.length; j++) {
                        for(var key in colDataSet[i].data){
                            if(colSet[j].data.columnName == key.toLowerCase()) {
                                dataList.push(colDataSet[i].data[key]);
                            }
                        }
                    }
                    var temp= template("colDataListTemp", {"colDataList": dataList});
                    $("#colDataList>tbody").append(temp);
                }
            }
        }
    });
}

function loadServiceApplyCount(openServiceId) {
    $.ajax({
        type: "get",
        url: context + "/service/scdev/myapply/count/" + openServiceId,
        success: function(count) {
            if(IsNumeric(count)) {
                $("#apply_num").html(count + "&nbsp;次");
            } else {
                $("#apply_num").html("0&nbsp;次");
            }
        }
    })
}

// 判断返回值是否为整数
function IsNumeric(input){
    return (input - 0) == input && (''+input).trim().length > 0;
}

function applyDataDef(id){
    $.ajax({
        type: "get",
        url: context + "/service/open/data/apply/" + id,
        success: function(resp) {
            if(resp.result==true) {
                $.dialog({
                    autofocus: true,
                    type: "alert",
                    content: "成功!"
                });
                // TODO 跳转到我的申请页面
                window.location.href = context + "/service/data/apply/getPage";
            } else {
                $.dialog({
                    autofocus: true,
                    type: "alert",
                    content:"失败!"+resp.message
                });
            }
        }
    })
}