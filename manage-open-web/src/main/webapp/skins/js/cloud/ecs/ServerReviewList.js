$(document).ready(function () {

    $.ajax({
        url: contextPath + "/service/ecs/getPlatformType",
        type: 'get',
        async: false,
        success: function (res) {
            if (JSON.parse(res).length == 0) {
                $.dialog({
                    type: 'confirm',
                    content: "当前服务不可用，是否配置",
                    ok: function () {
                        $.dialog({
                            type: 'iframe',
                            url: contextPath + '/jsp/cloud/ecs/PlatformInfo.jsp',
                            title: '云平台配置保存/更新',
                            width: 500,
                            height: 250,
                            onclose: function () {
                                if (this.returnValue == "ok") {
                                    //重载页面
                                    var resetPaging = false;
                                    var url = contextPath + "/service/ecs/review/getInstances";
                                    grid.reload(url, null, resetPaging);
                                    $("input[type='search']").parent().hide();//隐藏本地搜索框
                                }
                            }
                        })
                    },
                    cancel: function () {

                    }
                });
            }
        }
    });

    // 初始化datatable
    var options = {
        "serverSide": false,
        "orderCellsTop": true,
        "searching": true
    };
    var url = contextPath + "/service/ecs/review/getInstances";
    grid = new L.FlexGrid("instanceList", url);
    grid.init(options); // 初始化datatable
    $("input[type='search']").parent().hide();//隐藏本地搜索框

    //实例名称搜索框
    var column1 = $('#instanceList').DataTable().column(1);
    $("input#instanceName_search").on('keyup change', function () {
        column1.search($(this).val()).draw();
    });

    // 审核
    $(document).on("click", ".review", function () {
        var data = grid.oTable.row($(this).parents("tr")).data();
        var instanceName = data.instance_name;
        window.location.href = contextPath + "/jsp/cloud/ecs/ServerReview.jsp?instancename=" + instanceName;
    });

    // 删除
    $(document).on("click", ".del", function () {
        var data = grid.oTable.row($(this).parents("tr")).data();
        var instanceName = data.instance_name;
        $.dialog({
            type: 'confirm',
            content: '您确定要删除这个主机吗？',
            ok: function () {
                $.ajax({
                    url: contextPath + "/service/ecs/deleteinstance",
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "instance_name": instanceName
                    }),
                    dataType: "json",
                    success: function (res) {
                        var flag = res.result;
                        if ("true" == flag) {
                            var resetPaging = false;
                            var url = contextPath + "/service/ecs/review/getInstances";
                            grid.reload(url, null, resetPaging);
                            $("input[type='search']").parent().hide();//隐藏本地搜索框
                        } else {
                            UIAlert("删除失败");
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    });

    //批量删除
    $("#delAll").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要删除选中的密钥对吗？',
            ok: function () {
                //获取选中行的数据
                var str = "删除";
                var arr = document.querySelectorAll('input[name="checkboxlist"]');
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].checked) {
                        var data = grid.oTable.row($(arr[i]).parents("tr")).data();
                        var instanceName = data.instance_name;
                        str += instanceName;
                    }
                }
                UIAlert(str);

                $.ajax({
                    url: contextPath + "/service/review/deleteinstances",
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "instance_name": ""
                    }),
                    success: function (res) {
                        var flag = res.result;
                        if ("success" == flag) {
                            UIAlert("批量删除成功");
                            //重载页面
//            		        var resetPaging = false;
//            		        var url = contextPath + "/service/notify/getnotice";
//            		        grid.reload(url, null, resetPaging);
                        } else {
                            UIAlert("批量删除失败");
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    });

    //批量通过
    $("#passAll").click(function () {
        //获取选中行的数据
        var str = "通过";
        var arr = document.querySelectorAll('input[name="checkboxlist"]');
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].checked) {
                var data = grid.oTable.row($(arr[i]).parents("tr")).data();
                var instanceName = data.instance_name;
                str += instanceName;
            }
        }
        UIAlert(str);

        $.dialog({
            type: 'confirm',
            content: '您确定要通过选中的实例吗？',
            ok: function () {
                $.ajax({
                    url: contextPath + "/service/review/passinstances",
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "instance_name": ""
                    }),
                    success: function (res) {
                        var flag = res.result;
                        if ("success" == flag) {
                            UIAlert("批量通过成功");
                            //重载页面
//            		        var resetPaging = false;
//            		        var url = contextPath + "/service/notify/getnotice";
//            		        grid.reload(url, null, resetPaging);
                        } else {
                            UIAlert("批量通过失败");
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    });

    //批量驳回
    $("#rejectAll").click(function () {
        //获取选中行的数据
        var str = "驳回";
        var arr = document.querySelectorAll('input[name="checkboxlist"]');
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].checked) {
                var data = grid.oTable.row($(arr[i]).parents("tr")).data();
                var instanceName = data.instance_name;
                str += instanceName;
            }
        }
        UIAlert(str);

        $.dialog({
            type: 'confirm',
            content: '您确定要驳回选中的密钥对吗？',
            ok: function () {
                $.ajax({
                    url: contextPath + "/service/review/rejectinstances",
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "instance_name": ""
                    }),
                    success: function (res) {
                        var flag = res.result;
                        if ("success" == flag) {
                            UIAlert("批量驳回成功");
                            //重载页面
//            		        var resetPaging = false;
//            		        var url = contextPath + "/service/notify/getnotice";
//            		        grid.reload(url, null, resetPaging);
                        } else {
                            UIAlert("批量驳回失败");
                        }
                    }
                });
            },
            cancel: function () {
            }
        });
    });

});

function rendercheckbox(data, type, full) {
    return '<input type="checkbox" value="' + data + '" id="checkbox" name="checkboxlist" onclick="forSelectItem()">';
}

//操作
function renderoptions(data, type, full) {
    var html = "";
    var status = data.split("/");

    if (status[0] == "0" || status[0] == "2") {
        return '<div class="btn-group pull-center"><li><a class="review">' + "审核" + "</a></li></div>"
    }
    if (status[0] == "3") {
        return '<div class="btn-group pull-center"><li><a class="review">' + "审核" + '</a>&nbsp;&nbsp;<a class="del">' + "删除" + '</a></li></div>';
    }
    return '<div class="btn-group pull-center"><li><a class="review">' + "查看" + '</a>&nbsp;&nbsp;<a class="del">' + "删除" + '</a></li></div>';
}

// 状态列显示效果
function renderStatus(data, type, full) {
    var html = "";
    var status = data.split("/");

    switch (status[0]) {
        case "0":
            html = '<div><span>创建主机待审核</span></div>';
            break;
        case "1":
        case "4":
        case "5":
            switch (status[1]) {
                case "-1":
                    html = '<div><span>未创建</span></div>';
                    break;
                case "0":
                    html = '<div><span>创建中</span></div>';
                    break;
                case "1":
                    html = '<div><span>已启动</span></div>';
                    break;
                case "2":
                    html = '<div><span>已关机</span></div>';
                    break;
                case "3":
                    html = '<div><span>挂起</span></div>';
                    break;
                case "4":
                    html = '<div><span>未知</span></div>';
                    break;
                case "5":
                    html = '<div><span>创建失败</span></div>';
                    break;
                case "6":
                    html = '<div><span>删除中</span></div>';
                    break;
                case "7":
                    html = '<div><span>已删除</span></div>';
                    break;
                case "8":
                    html = '<div><span>删除失败</span></div>';
                    break;
            }
            break;
        case "2":
            html = '<div><span>创建主机驳回</span></div>';
            break;
        case "3":
            html = '<div><span>配置修改待审核</span></div>';
            break;
    }
    return html;
}

//全选 反选
function forSelectAll() {
    var chk = document.getElementById("selectAll");
    var arr = document.querySelectorAll('input[name="checkboxlist"]');
    for (var i = 0, len = arr.length; i < len; i++) {
        arr[i].checked = chk.checked;
    }
}

//列表中的checkbox全选 反选
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

//弹窗函数
function UIAlert(content) {
    $.dialog({
        type: 'alert',
        content: content,
        ok: function () {

        }
    });
}