"use strict";
var global = {
    flexgrid: null // 存放grid
};

$(function () {
    // 初始化页面元素
    initPageDom();

    // 隐藏本地搜索框
    $("input[type='search']").parent().hide();

    // 实例名称搜索框
    var column1 = $('#instanceList').DataTable().column(1);
    $("input#instanceName_search").on('keyup change', function () {
        column1.search($(this).val()).draw();
    });

    // 审核
    $(document).on("click", ".review", function () {
        var data = global.flexgrid.oTable.row($(this).parents("tr")).data();
        var instanceId = data.instance_id;
        window.location.href = contextPath + "/jsp/cloud/mcs/MaxComputeServiceReview.jsp?instanceId=" + instanceId;
    });

    // 删除
    $(document).on("click", ".del", function () {
        var data = global.flexgrid.oTable.row($(this).parents("tr")).data();
        var instanceId = data.instance_id;

        $.dialog({
            type: 'confirm',
            content: '您确定要删除这个实例吗？',
            ok: function () {
                $.ajax({
                    url: contextPath + "/service/mcs/deleteInstance",
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "instance_id": instanceId
                    }),
                    dataType: "json",
                    success: function (res) {
                        if ("true" === res.result) {
                            $.dialog({
                                type: 'alert',
                                content: "删除成功",
                                ok: function () {
                                    var resetPaging = false;
                                    var url = contextPath + "/service/mcs/review/getInstances";
                                    global.flexgrid.reload(url, null, resetPaging);
                                    // 隐藏本地搜索框
                                    $("input[type='search']").parent().hide();
                                }
                            });
                        } else {
                            $.dialog({
                                type: 'alert',
                                content: "删除失败",
                                ok: function () {
                                    var resetPaging = false;
                                    var url = contextPath + "/service/mcs/review/getInstances";
                                    global.flexgrid.reload(url, null, resetPaging);
                                    // 隐藏本地搜索框
                                    $("input[type='search']").parent().hide();
                                }
                            });
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    });

    // 批量删除
    $("#delAll").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要删除选中的实例吗？',
            ok: function () {
                // 获取选中行的数据
                var str = "";
                var arr = document.querySelectorAll('input[name="checkboxlist"]');
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].checked) {
                        var data = global.flexgrid.oTable.row($(arr[i]).parents("tr")).data();
                        var instanceId = data.instance_id;
                        str += instanceId + ",";
                    }
                }

                $.ajax({
                    url: contextPath + "/service/mcs/deleteInstances",
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "instance_id": str
                    }),
                    dataType: "json",
                    success: function (res) {
                        if ("true" === res.result) {
                            $.dialog({
                                type: 'alert',
                                content: "批量删除成功",
                                ok: function () {
                                    var resetPaging = false;
                                    var url = contextPath + "/service/mcs/review/getInstances";
                                    global.flexgrid.reload(url, null, resetPaging);
                                    // 隐藏本地搜索框
                                    $("input[type='search']").parent().hide();
                                }
                            });
                        } else {
                            $.dialog({
                                type: 'alert',
                                content: "批量删除失败",
                                ok: function () {
                                    var resetPaging = false;
                                    var url = contextPath + "/service/mcs/review/getInstances";
                                    global.flexgrid.reload(url, null, resetPaging);
                                    // 隐藏本地搜索框
                                    $("input[type='search']").parent().hide();
                                }
                            });
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    });

    // 批量通过
    $("#passAll").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要通过选中的实例吗？',
            ok: function () {
                // 获取选中行的数据
                var str = "";
                var arr = document.querySelectorAll('input[name="checkboxlist"]');
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].checked) {
                        var data = global.flexgrid.oTable.row($(arr[i]).parents("tr")).data();
                        var instanceId = data.instance_id;
                        str += instanceId + ",";
                    }
                }

                $.ajax({
                    url: contextPath + "/service/mcs/passInstances",
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "instance_id": str
                    }),
                    dataType: "json",
                    success: function (res) {
                        if ("true" === res.result) {
                            $.dialog({
                                type: 'alert',
                                content: "批量通过成功",
                                ok: function () {
                                    var resetPaging = false;
                                    var url = contextPath + "/service/mcs/review/getInstances";
                                    global.flexgrid.reload(url, null, resetPaging);
                                    // 隐藏本地搜索框
                                    $("input[type='search']").parent().hide();
                                }
                            });
                        } else {
                            $.dialog({
                                type: 'alert',
                                content: "批量通过失败",
                                ok: function () {
                                    var resetPaging = false;
                                    var url = contextPath + "/service/mcs/review/getInstances";
                                    global.flexgrid.reload(url, null, resetPaging);
                                    // 隐藏本地搜索框
                                    $("input[type='search']").parent().hide();
                                }
                            });
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    });

    // 批量驳回
    $("#rejectAll").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要驳回选中的实例吗？',
            ok: function () {
                // 获取选中行的数据
                var str = "";
                var arr = document.querySelectorAll('input[name="checkboxlist"]');
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].checked) {
                        var data = global.flexgrid.oTable.row($(arr[i]).parents("tr")).data();
                        var instanceId = data.instance_id;
                        str += instanceId + ",";
                    }
                }

                $.ajax({
                    url: contextPath + "/service/mcs/rejectInstances",
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "instance_id": str
                    }),
                    dataType: "json",
                    success: function (res) {
                        if ("true" === res.result) {
                            $.dialog({
                                type: 'alert',
                                content: "批量驳回成功",
                                ok: function () {
                                    var resetPaging = false;
                                    var url = contextPath + "/service/mcs/review/getInstances";
                                    global.flexgrid.reload(url, null, resetPaging);
                                    // 隐藏本地搜索框
                                    $("input[type='search']").parent().hide();
                                }
                            });
                        } else {
                            $.dialog({
                                type: 'alert',
                                content: "批量驳回失败",
                                ok: function () {
                                    var resetPaging = false;
                                    var url = contextPath + "/service/mcs/review/getInstances";
                                    global.flexgrid.reload(url, null, resetPaging);
                                    // 隐藏本地搜索框
                                    $("input[type='search']").parent().hide();
                                }
                            });
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    });
});

function initPageDom() {
    // 初始化表格
    global.flexgrid = initTable();
}

function initTable() {
    var url = contextPath + "/service/mcs/review/getInstances";
    var options = {
        "serverSide": false,
        "orderCellsTop": true,
        "searching": true
    };
    var grid = new L.FlexGrid("instanceList", url);
    grid.init(options);
    return grid;
}

// 复选框
function renderCheckbox(data, type, full) {
    return '<input type="checkbox" value="' + data + '" id="checkbox" name="checkboxlist" onclick="forSelectItem()">';
}

// 实例名称列显示效果
function renderNameId(data, type, full) {
    var arr = data.split("/");
    return '<div><span>' + arr[0] + '</span>&nbsp&nbsp&nbsp<span>' + arr[1] + '</span></div>';
}

// 操作
function renderOptions(data, type, full) {
    var html = '';
    var status = data.split("/");

    switch (status[0]) {
        case "0":
            html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn review"><span></span>审核</button><button type="button" class="btn btn-xs ue-btn del" style="margin-left:5px"><span></span>删除</button></div>';
            break;
        case "1":
            html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn review"><span></span>查看</button><button type="button" class="btn btn-xs ue-btn del" style="margin-left:5px"><span></span>删除</button></div>';
            break;
        case "2":
            html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn review"><span></span>查看</button><button type="button" class="btn btn-xs ue-btn del" style="margin-left:5px"><span></span>删除</button></div>';
            break;
    }
    return html;
}

// 全选 反选
function forSelectAll() {
    var chk = document.getElementById("selectAll");
    var arr = document.querySelectorAll('input[name="checkboxlist"]');
    for (var i = 0, len = arr.length; i < len; i++) {
        arr[i].checked = chk.checked;
    }
}

// 列表中的checkbox全选 反选
function forSelectItem() {
    var arr = document.querySelectorAll('input[name="checkboxlist"]');
    var chkNum = 0;
    for (var i = 0, len = arr.length; i < len; i++) {
        if (arr[i].checked) {
            chkNum++;
        }
    }
    document.getElementById("selectAll").checked = (arr.length == chkNum);
}

// 服务规格列显示效果
function renderSpecification(data, type, full) {
    var arr = data.split("/");
    return '<div><span>计算资源：' + arr[0] + 'vCPU | ' + arr[1] + 'GB内存<br/>存储资源：' + arr[2] + 'GB</span></div>'
}

// 状态列显示效果
function renderStatus(data, type, full) {
    var html = "";
    var status = data.split("/");

    switch (status[0]) {
        case "0":
            html = '<div><span>待审核</span></div>';
            break;
        case "1":
            switch (status[1]) {
                case "0":
                    html = '<div><span>创建中</span></div>';
                    break;
                case "1":
                    html = '<div><span>创建成功</span></div>';
                    break;
                case "2":
                    html = '<div><span>创建失败</span></div>';
                    break;
                case "3":
                    html = '<div><span>运行中</span></div>';
                    break;
                case "4":
                    html = '<div><span>删除中</span></div>';
                    break;
                case "5":
                    html = '<div><span>删除失败</span></div>';
                    break;
            }
            break;
        case "2":
            html = '<div><span>审批驳回</span></div>';
            break;
    }
    return html;
}

// 弹窗函数
function UIAlert(content) {
    $.dialog({
        type: 'alert',
        content: content,
        ok: function () {
        }
    });
}