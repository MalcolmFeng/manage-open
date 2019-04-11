var global = {
    flexgrid: null // 存放grid
};

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
                                    var url = contextPath + "/service/ecs/manage/getInstances";
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
    var url = contextPath + "/service/ecs/manage/getInstances";
    grid = new L.FlexGrid("instanceList", url);
    grid.init(options); // 初始化datatable
    $("input[type='search']").parent().hide();//隐藏本地搜索框

    //实例名称搜索框
    var column1 = $('#instanceList').DataTable().column(1);
    $("input#instanceName_search").on('keyup change', function () {
        column1.search($(this).val()).draw();
    });

    // 远程登录
    $(document).on("click", ".telnet", function () {
        var data = grid.oTable.row($(this).parents("tr")).data();
        var instanceName = data.instance_name;
        UIAlert("远程登录成功");
    });

    // 管理
    $(document).on("click", ".detail", function () {
        var data = grid.oTable.row($(this).parents("tr")).data();
        var instanceName = data.instance_name;
        window.location.href = contextPath + "/jsp/cloud/ecs/ServerManage.jsp?instancename=" + instanceName;
    });

    //批量启动
    $("#startAll").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要停止选中的实例吗？',
            ok: function () {
                //获取选中行的数据
                var str = "停止";
                var arr = document.querySelectorAll('input[name="checkboxlist"]');
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].checked) {
                        var data = grid.oTable.row($(arr[i]).parents("tr")).data();
                        var instanceName = data.instance_name;
                        str += instanceName;
                    }
                }
                UIAlert(str);
            },
            cancel: function () {
            }
        });
    });

    //批量停止
    $("#stopAll").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要停止选中的实例吗？',
            ok: function () {
                //获取选中行的数据
                var str = "停止";
                var arr = document.querySelectorAll('input[name="checkboxlist"]');
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].checked) {
                        var data = grid.oTable.row($(arr[i]).parents("tr")).data();
                        var instanceName = data.instance_name;
                        str += instanceName;
                    }
                }
                UIAlert(str);
            },
            cancel: function () {
            }
        });
    });

    //批量重启
    $("#restartAll").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要重启选中的实例吗？',
            ok: function () {
                //获取选中行的数据
                var str = "重启";
                var arr = document.querySelectorAll('input[name="checkboxlist"]');
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].checked) {
                        var data = grid.oTable.row($(arr[i]).parents("tr")).data();
                        var instanceName = data.instance_name;
                        str += instanceName;
                    }
                }
                UIAlert(str);
            },
            cancel: function () {
            }
        });
    });

    //批量删除
    $("#delAll").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要删除选中的实例吗？',
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
            },
            cancel: function () {
            }
        });
    });

    //创建实例
    $("#addInstance").click(function () {
        window.location.href = contextPath + "/service/ecs/apply";
    });

});

//复选框
function rendercheckbox(data, type, full) {
    return '<input type="checkbox" value="' + data + '" id="checkbox" name="checkboxlist" onclick="forSelectItem()">';
}

//操作
function renderoptions(data, type, full) {
    var html = "";
    var status = data.split("/");

    //根据状态更多展示不同下拉项
    switch (status[0]) {
        case "0":
        case "2":
            html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn detail" style="margin-left:5px"><span></span>管理</button></div>';
            break;
        case "3":
            html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn detail" style="margin-left:5px"><span></span>管理</button><div class="btn-group" style="margin-left:5px"><div class="dropdown"><button class="btn btn-xs ue-btn dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown">更多<span class="caret"></span></button><ul class="dropdown-menu dropdown-menu-right ue-dropdown-menu"><li class="ue-dropdown-angle"></li><li onclick="del_click(this)"><a><span class="fa fa-trash"></span>删除</a></li></ul></div></div></div>';
            break;
        case "1":
        case "4":
        case "5":
            if (status[1] == 1) {
                html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn detail" style="margin-left:5px"><span></span>管理</button><div class="btn-group" style="margin-left:5px"><div class="dropdown"><button class="btn btn-xs ue-btn dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown">更多<span class="caret"></span></button><ul class="dropdown-menu dropdown-menu-right ue-dropdown-menu"><li class="ue-dropdown-angle"></li><li onclick="del_click(this)"><a><span class="fa fa-trash"></span>删除</a></li></ul></div></div></div>';
            } else {
                html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn detail" style="margin-left:5px"><span></span>管理</button><div class="btn-group" style="margin-left:5px"><div class="dropdown"><button class="btn btn-xs ue-btn dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown">更多<span class="caret"></span></button><ul class="dropdown-menu dropdown-menu-right ue-dropdown-menu"><li class="ue-dropdown-angle"></li><li onclick="del_click(this)"><a><span class="fa fa-trash"></span>删除</a></li></ul></div></div></div>';
            }
            break;
            // switch (status[1]) {
            //     case "0":
            //     case "2":
            //         html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn telnet"><span></span>远程登录</button><button type="button" class="btn btn-xs ue-btn detail" style="margin-left:5px"><span></span>管理</button><div class="btn-group" style="margin-left:5px"><div class="dropdown"><button class="btn btn-xs ue-btn dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown">更多<span class="caret"></span></button><ul class="dropdown-menu dropdown-menu-right ue-dropdown-menu"><li class="ue-dropdown-angle"></li><li onclick="resetPwd_click(this)"><a><span class="fa fa-undo"></span>重置密码</a></li><li onclick="del_click(this)"><a><span class="fa fa-trash"></span>删除</a></li></ul></div></div></div>';
            //         break;
            //     case "1":
            //         html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn telnet"><span></span>远程登录</button><button type="button" class="btn btn-xs ue-btn detail" style="margin-left:5px"><span></span>管理</button><div class="btn-group" style="margin-left:5px"><div class="dropdown"><button class="btn btn-xs ue-btn dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown">更多<span class="caret"></span></button><ul class="dropdown-menu dropdown-menu-right ue-dropdown-menu"><li class="ue-dropdown-angle"></li><li onclick="resetPwd_click(this)"><a><span class="fa fa-undo"></span>重置密码</a></li><li onclick="start_click(this)"><a><span class="fa fa-play"></span>启动</a></li><li onclick="del_click(this)"><a><span class="fa fa-trash"></span>删除</a></li></ul></div></div></div>';
            //         break;
            //     case "3":
            //         html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn telnet"><span></span>远程登录</button><button type="button" class="btn btn-xs ue-btn detail" style="margin-left:5px"><span></span>管理</button><div class="btn-group" style="margin-left:5px"><div class="dropdown"><button class="btn btn-xs ue-btn dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown">更多<span class="caret"></span></button><ul class="dropdown-menu dropdown-menu-right ue-dropdown-menu"><li class="ue-dropdown-angle"></li><li onclick="resetPwd_click(this)"><a><span class="fa fa-undo"></span>重置密码</a></li><li onclick="stop_click(this)"><a><span class="fa fa-stop"></span>停止</a></li><li onclick="restart_click(this)"><a><span class="fa fa-refresh"></span>重启</a></li><li onclick="del_click(this)"><a><span class="fa fa-trash"></span>删除</a></li></ul></div></div></div>';
            //         break;
            //     case "10":
            //         html = '<div class="btn-group pull-center"><button type="button" class="btn btn-xs ue-btn telnet"><span></span>远程登录</button><button type="button" class="btn btn-xs ue-btn detail" style="margin-left:5px"><span></span>管理</button><div class="btn-group" style="margin-left:5px"><div class="dropdown"><button class="btn btn-xs ue-btn dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown">更多<span class="caret"></span></button><ul class="dropdown-menu dropdown-menu-right ue-dropdown-menu"><li class="ue-dropdown-angle"></li><li onclick="changeSpecification_click(this)"><a><span class="fa fa-pencil"></span>变更规格</a></li><li onclick="resetPwd_click(this)"><a><span class="fa fa-undo"></span>重置密码</a></li><li onclick="start_click(this)"><a><span class="fa fa-play"></span>启动</a></li><li onclick="del_click(this)"><a><span class="fa fa-trash"></span>删除</a></li></ul></div></div></div>';
            //         break;
            // }
    }
    return html;
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

//变更规格
function changeSpecification_click(obj) {
    var data = grid.oTable.row($(obj).parents("tr")).data();
    var instanceId = data.instance_id;
    $.dialog({
        type: 'iframe',
        url: contextPath + '/jsp/cloud/ecs/ModifyInstanceSpecification.jsp?instanceid=' + instanceId,
        title: '变更实例规格',
        width: 450,
        height: 150,
        onclose: function () {
            if (this.returnValue == "ok") {
                //重载页面
                var resetPaging = false;
                var url = contextPath + "/service/manage/getinstances";
                grid.reload(url, null, resetPaging);
            }
        }
    });
}

//重置密码
function resetPwd_click(obj) {
    var data = grid.oTable.row($(obj).parents("tr")).data();
    var instanceId = data.instance_id;
    $.dialog({
        type: 'iframe',
        url: contextPath + '/jsp/cloud/ecs/ResetPwd.jsp?instanceid=' + instanceId,
        title: '重置密码',
        width: 600,
        height: 240,
        onclose: function () {
            if (this.returnValue == "ok") {
                //重载页面
                var resetPaging = false;
                var url = contextPath + "/service/manage/getinstances";
                grid.reload(url, null, resetPaging);
            }
        }
    });
}

//启动
function start_click(obj) {
    UIAlert("启动");
}

//停止
function stop_click(obj) {
    UIAlert("停止");
}

//重启
function restart_click(obj) {
    UIAlert("重启");
}

//删除
function del_click(obj) {
    var data = grid.oTable.row($(obj).parents("tr")).data();
    var instanceName = data.vm_name;
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
       		            var url = contextPath + "/service/ecs/manage/getInstances";
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