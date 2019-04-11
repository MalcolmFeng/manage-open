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

    // 创建实例
    $("#addInstance").click(function () {
        window.location.href = contextPath + "/service/mcs/apply";
    });

    // 管理
    $(document).on("click", ".detail", function () {
        var data = global.flexgrid.oTable.row($(this).parents("tr")).data();
        var instanceId = data.instance_id;
        window.location.href = contextPath + "/jsp/cloud/mcs/MaxComputeServiceManage.jsp?instanceId=" + instanceId;
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
                                    var url = contextPath + "/service/mcs/manage/getInstances";
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
                                    var url = contextPath + "/service/mcs/manage/getInstances";
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
    })
});

function initPageDom() {
    // 初始化表格
    global.flexgrid = initTable();
}

function initTable() {
    var url = contextPath + "/service/mcs/manage/getInstances";
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
    return '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn detail"><span></span>管理</button></div>';

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

// 删除
function del_click(obj) {
    $.dialog({
        type: 'confirm',
        content: '您确定要删除这个实例吗？',
        ok: function () {
            var data = global.flexgrid.oTable.row($(obj).parents("tr")).data();
            var instanceId = data.instance_id;

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
                                var url = contextPath + "/service/mcs/manage/getInstances";
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
                                var url = contextPath + "/service/mcs/manage/getInstances";
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
}