var global = {
    password: "",
    keyName: "",
    isShow: false,
    vmName: ""
};

$(document).ready(function () {
    //初始化页面信息
    initPageInfo();

    //远程登录
    $("#telnet").click(function () {
        UIAlert("远程登录");
    });

    //更改实例规格
    $("#modify_specification").click(function () {
        var instanceId = $("#instance_id").text();
        $.dialog({
            type: 'iframe',
            url: contextPath + '/jsp/cloud/ecs/ModifyInstanceSpecification.jsp?instancename=' + global.vmName,
            title: '变更实例规格',
            width: 600,
            height: 350,
            onclose: function () {
                if (this.returnValue == "ok") {
                    //刷新当前页面
                    window.location.href = contextPath + "/service/ecs/manage";
                    //$("#instance_name").text("变更规格成功");
                }
            }
        });
    });

    $("#poweredOn").click(function () {
        var instance_name = [];
        $.dialog({
            type: 'confirm',
            content: '您确定要对此实例执行开机操作吗？',
            ok: function () {
                instance_name.push(global.vmName);
                $.ajax({
                    url: contextPath + "/service/ecs/powerOperation",
                    type: "post",
                    contentType: 'application/json',
                    async: false,
                    data: JSON.stringify(
                        {
                            "instance_name": instance_name,
                            "operation": "poweredOn"
                        }
                    ),
                    dataType: "json",
                    success: function (res) {
                        var flag = res.result;
                        if ("true" == flag) {
//				            UIAlert("操作成功!");
                            window.location.href = contextPath + "/service/ecs/manage";
                        } else {
                            UIAlert(L.getLocaleMessage("savefailed", "操作失败！"));
                        }
                    }
                })
            },
            cancel: function () {
            }
        })
    });

    $("#poweredOff").click(function () {
        var instance_name = [];
        $.dialog({
            type: 'confirm',
            content: '您确定要对此实例执行关机操作吗？',
            ok: function () {
                instance_name.push(global.vmName);
                $.ajax({
                    url: contextPath + "/service/ecs/powerOperation",
                    type: "post",
                    contentType: 'application/json',
                    async: false,
                    data: JSON.stringify(
                        {
                            "instance_name": instance_name,
                            "operation": "poweredOff"
                        }
                    ),
                    dataType: "json",
                    success: function (res) {
                        var flag = res.result;
                        if ("true" == flag) {
//				            UIAlert("操作成功!");
                            window.location.href = contextPath + "/service/ecs/manage";
                        } else {
                            UIAlert(L.getLocaleMessage("savefailed", "操作失败！"));
                        }
                    }
                })
            },
            cancel: function () {
            }
        })
    });

    $("#reset").click(function () {
        var instance_name = [];
        $.dialog({
            type: 'confirm',
            content: '您确定要对此实例执行重启操作吗？',
            ok: function () {
                instance_name.push(global.vmName);
                $.ajax({
                    url: contextPath + "/service/ecs/powerOperation",
                    type: "post",
                    contentType: 'application/json',
                    async: false,
                    data: JSON.stringify(
                        {
                            "instance_name": instance_name,
                            "operation": "reset"
                        }
                    ),
                    dataType: "json",
                    success: function (res) {
                        var flag = res.result;
                        if ("true" == flag) {
//				            UIAlert("操作成功!");
                            window.location.href = contextPath + "/service/ecs/manage";
                        } else {
                            UIAlert(L.getLocaleMessage("savefailed", "操作失败！"));
                        }
                    }
                })
            },
            cancel: function () {
            }
        })
    });
});

//查询基本信息-配置信息
function initPageInfo() {
    $.ajax({
        url: contextPath + "/service/ecs/manage/getDetails",
        type: 'post',
        contentType: 'application/json',
        async: false,
        data: JSON.stringify(
            {
                "instance_name": instanceName
            }
        ),
        dataType: "json",
        success: function (res) {
            $("#instance_name").text(res.instanceName);
            $("#audit_reply").text(res.auditReply);
            $("#apply_time").text(res.applyTime);
            $("#cpu").text(res.coreNum + "vCPU");
            $("#memory").text(res.memory + "GB");
            $("#os").text(res.ecsOs);
            $("#system_disk").text(res.osDiskSize + "GB");
            $("#data_disk_size").text(res.dataDiskSize + "GB");
            $("#data_disk_num").text(res.dataDiskNum);

            global.password = res.password;
            global.keyName = res.keyName;
            global.vmName = res.vmName;

            switch (res.applyStatus) {
                case "0":
                    $("#review_status").text("创建主机待审核");
                    hideStatus();
                    $("#running_status_content").hide();
                    $("#audit_reply_content").hide();
                    $("#create_time_content").hide();
                    break;
                case "1":
                    $("#review_status").text("创建主机审核通过");
                    runStatus(res);
                    break;
                case "4":
                    $("#review_status").text("配置修改审核通过");
                    runStatus(res);
                    break;
                case "5":
                    $("#review_status").text("配置修改审核驳回");
                    runStatus(res);
                    break;
                case "2":
                    $("#review_status").text("创建主机驳回");
                    hideStatus();
                    $("#running_status_content").hide();
                    $("#create_time_content").hide();
                    break;
                case "3":
                    $("#review_status").text("配置修改待审核");
                    hideStatus();
                    $("#running_status_content").hide();
                    $("#create_time").text(res.createTime);
                    $("#audit_reply_content").hide();
                    break;
            }

            if (res.lockStatus == "0") {
                hideStatus();
                $("#running_status").text("已锁定");
                $("#create_time").text(res.createTime);
            }
        }
    });
}

function hideStatus() {
    $("#telnet").hide();
    $("#poweredOn").hide();
    $("#poweredOff").hide();
    $("#reset").hide();
    $("#more").hide();
    $("#modify_specification").hide();
    $("#ip_content").hide();
    $("#password_content").hide();
    $("#username_content").hide();
    $("#key_name_content").hide();
}

function runStatus(res) {
    switch (res.runStatus) {
        case "-1":
            hideStatus();
            $("#running_status").text("未创建");
            $("#create_time_content").hide();
            break;
        case "0":
            hideStatus();
            $("#running_status").text("创建中");
            $("#create_time").text(res.createTime);
            break;
        case "1":
            $("#running_status").text("已启动");
            $("#telnet").show();
            $("#poweredOn").hide();
            $("#poweredOff").show();
            $("#reset").show();
            $("#more").show();
            $("#create_time").text(res.createTime);
            $("#modify_specification").show();
            if (res.ipAddress == "") {
                $("#ip").text("正在获取中...")
            } else {
                $("#ip").text(res.ipAddress);
            }
            $("#username").text("root");
            $("#password").text("******");
            $("#key_name").text(res.keyName);
            break;
        case "2":
            hideStatus();
            $("#running_status").text("已关机");
            $("#poweredOn").show();
            $("#more").show();
            $("#create_time").text(res.createTime);
            $("#modify_specification").show();
            break;
        case "3":
            hideStatus();
            $("#running_status").text("挂起");
            $("#create_time").text(res.createTime);
            break;
        case "4":
            hideStatus();
            $("#running_status").text("未知");
            $("#create_time").text(res.createTime);
            break;
        case "5":
            hideStatus();
            $("#running_status").text("创建失败");
            $("#create_time").text(res.createTime);
            break;
        case "6":
            hideStatus();
            $("#running_status").text("删除中");
            $("#create_time").text(res.createTime);
            break;
        case "7":
            hideStatus();
            $("#running_status").text("已删除");
            $("#create_time").text(res.createTime);
            break;
        case "8":
            hideStatus();
            $("#running_status").text("删除失败");
            $("#create_time").text(res.createTime);
            break;
    }
}

//重置密码
function resetPwd_click() {
    $.dialog({
        type: 'iframe',
        url: contextPath + '/jsp/cloud/ecs/ResetPwd.jsp?instancename=' + global.vmName + '&password=' + global.password,
        title: '重置密码',
        width: 600,
        height: 240,
        onclose: function () {
            if (this.returnValue == "ok") {
                location.reload();
            }
        }
    });
}

//修改密钥
function modifyKey_click() {
    $.dialog({
        type: 'iframe',
        url: contextPath + '/jsp/cloud/ecs/ModifyKey.jsp?instancename=' + global.vmName,
        title: '修改密钥',
        width: 600,
        height: 240,
        onclose: function () {
            if (this.returnValue == "ok") {
                location.reload();
            }
        }
    });
}

function viewPassword() {
    if (global.isShow) {
        $("#password").text("******");
        $("#password_button").text("显示密码");
        global.isShow = false;
    } else {
        $("#password").text(global.password);
        $("#password_button").text("隐藏密码");
        global.isShow = true;
    }
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