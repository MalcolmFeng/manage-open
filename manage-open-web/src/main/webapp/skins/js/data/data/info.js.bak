var pageSize=10;
var tablename="";
var colResourceId;
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
				$("#tablename").empty().append("数据表名称："+dataSet[0].tableName);
                $("#tableNameList>li:first").addClass("active");
                var resourceId =  $("#tableNameList>li:first").attr("data-resourceid");
				colResourceId = resourceId;
                initTableCol(0);
				initColData();
            }
        }
    });
}

// 切换表格
function tableLiClick() {
    $(this).addClass("active").siblings().removeClass("active");
	tableName=$(this).attr("data-text");
    $("#tablename").empty().append("数据表名称："+tableName);
    var resourceId = $(this).attr("data-resourceid");
	colResourceId = resourceId;
    // 加载数据项信息
    initTableCol(0);
	initColData();
}

// 加载数据项信息
function initTableCol(curPage,isPage) {
    var start = pageSize*curPage;
    $.ajax({
        url: context + "/service/open/data/getItemsByResourceId?resourceId=" + colResourceId + "&limit=" + pageSize+ "&start=" + start,
        success: function(data) {
            var dataSet = eval("("+data.json+")");
            if(dataSet.recordSet.length > 0) {
                var colSet = dataSet.recordSet;
                       
                var temp= template("colDataTableTemp", {"colList": dataSet.recordSet});
                $("#colDataTable>tbody").empty().append(temp);

                /* setPageHeight();*/
                //重新显示分页
                var totalPage=0;
                if(dataSet.totalCount>0){
                    totalPage=  dataSet.totalCount%pageSize==0?dataSet.totalCount/pageSize:parseInt(dataSet.totalCount/pageSize)+1;
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
        }
    });
}

function setPaginationInfo(curPage,pageCount) {
    var html = '<span>共&nbsp;'+pageCount+'&nbsp;页,当前第&nbsp;' + curPage + '&nbsp;页</span>';
    $(".pagination_info").empty().append(html);
}

function pageSelectCallback(curPage, pagination) {
    initTableCol(curPage,"page");
}

// 加载数据列
function initColData() {
    $("#colDataList>thead>tr").empty();
    $("#colDataList>tbody").empty();

	 $.ajax({
        url: context + "/service/open/data/getItemsByResourceId?resourceId=" + colResourceId + "&limit=9999999&start=0",
        success: function(data) {
            var dataSet = eval("("+data.json+")");
            if(dataSet.recordSet.length > 0) {
				var colSet = dataSet.recordSet;

				for(var i = 0; i < colSet.length; i++) {
                    var html = '<th>'+colSet[i].data.columnName+'</th>';
                    $("#colDataList>thead>tr").append(html);
                }

				$.ajax({
					url: context + "/service/open/data/getDataByResourceId?DATA_RESOURCE_ID="+colResourceId+"&limit=10",
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