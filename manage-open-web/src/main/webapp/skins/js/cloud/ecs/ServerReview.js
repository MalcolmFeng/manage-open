var global = {
    PLATFORM_TYPE_SELECT_DATA: {
        platformType: []
    }
};

$(document).ready(function () {

    // 初始化datatable
    // var options = {
    //     "serverSide": false,
    //     "orderCellsTop": true,
    //     "paging": false,
    //     "info": false
    // };
    // var url = contextPath + "/service/ecs/getDataDisksByInstanceId?instanceId=" + instanceId;
    // grid = new L.FlexGrid("data_disk_list", url);
    // grid.init(options); // 初始化datatable

    //初始化信息
    initPageInfo();

    initPlatformType();

    //通过
    $("#pass").click(function () {
        if ($("#status").text() == "创建主机待审核" && $("#virtual_environment").val() == "") {
            $("#virtual_environment").focus();
            return;
        }
        $.dialog({
            type: 'confirm',
            content: '您确定要通过这个实例吗？',
            ok: function () {
                if ($("#status").text() == "配置修改待审核") {
                    $.ajax({
                        url: contextPath + "/service/ecs/auditChangeFormat",
                        type: 'post',
                        contentType: 'application/json',
                        async: false,
                        data: JSON.stringify(
                            {
                                "vm_name": $("#instance_name").text(),
                                "audit_status": 4,
                                "audit_reply": $("#audit_reply").val()
                            }
                        ),
                        dataType: "json",
                        success: function (res) {
                            if ("true" === res.result) {
                                window.location.href = contextPath + "/service/ecs/review";
                            } else {
                                UIAlert(L.getLocaleMessage("rejectfailed", "通过失败！"));
                            }
                        }
                    });
                } else {
                    $.ajax({
                        url: contextPath + "/service/ecs/passInstance",
                        type: 'post',
                        contentType: 'application/json',
                        async: false,
                        data: JSON.stringify(
                            {
                                "vm_name_audit": $("#instance_name").text(),
                                "audit_status": 1,
                                "audit_reply": $("#audit_reply").val(),
                                "platform_type": $("#virtual_environment").val(),
                                "apply_status": $("#status").text(),
                                "vm_cpu": $("#cpu").text().split(" ")[0],
                                "vm_memory": $("#memory").text().split(" ")[0]
                            }
                        ),
                        dataType: "json",
                        success: function (res) {
                            if ("true" === res.result) {
                                window.location.href = contextPath + "/service/ecs/review";
                            } else {
                                UIAlert(L.getLocaleMessage("rejectfailed", "通过失败！"));
                            }
                        }
                    });
                }
            },
            cancel: function () {
            }
        });
    });

    //驳回
    $("#reject").click(function () {
        $.dialog({
            type: 'confirm',
            content: '您确定要驳回这个实例吗？',
            ok: function () {
                if ($("#status").text() == "配置修改待审核") {
                    $.ajax({
                        url: contextPath + "/service/ecs/auditChangeFormat",
                        type: 'post',
                        contentType: 'application/json',
                        async: false,
                        data: JSON.stringify(
                            {
                                "vm_name": instanceName,
                                "audit_status": 5,
                                "audit_reply": $.trim($("#audit_reply").val())
                            }
                        ),
                        dataType: "json",
                        success: function (res) {
                            if ("true" === res.result) {
                                window.location.href = contextPath + "/service/ecs/review";
                            } else {
                                UIAlert(L.getLocaleMessage("rejectfailed", "驳回失败！"));
                            }
                        }
                    })
                } else {
                    $.ajax({
                        url: contextPath + "/service/ecs/rejectInstance",
                        type: 'post',
                        contentType: 'application/json',
                        async: false,
                        data: JSON.stringify(
                            {
                                "vm_name_audit": instanceName,
                                "audit_status": 2,
                                "audit_reply": $.trim($("#audit_reply").val()),
                                "platform_type": $("#virtual_environment").val()
                            }
                        ),
                        dataType: "json",
                        success: function (res) {
                            if ("true" === res.result) {
                                window.location.href = contextPath + "/service/ecs/review";
                            } else {
                                UIAlert(L.getLocaleMessage("rejectfailed", "驳回失败！"));
                            }
                        }
                    })
                }
            },
            cancel: function () {
            }
        });
    });

    $("#addPlatformType").click(function () {
        $.dialog({
            type: 'iframe',
            url: contextPath + '/jsp/cloud/ecs/PlatformInfo.jsp',
            title: '云平台配置保存/更新',
            width: 500,
            height: 250,
            onclose: function () {
                if (this.returnValue == "ok") {
                    //刷新当前页面
                    //window.location.href = contextPath + "/service/ecs/manage";
                    //$("#instance_name").text("变更规格成功");
                }
            }
        });
    })
});

//查询基本信息-配置信息-审核信息
function initPageInfo() {
    $.ajax({
        type: "post",
        url: contextPath + "/service/ecs/review/getDetails",
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
            $("#apply_user").text(res.userId);
            $("#apply_num").text(res.vmNumber);
            $("#apply_time").text(res.applyTime);
            $("#apply_reason").text(res.applyReason);
            $("#cpu").text(res.coreNum + " vCPU ");
            $("#memory").text(res.memory + " GB内存");
            $("#iso").text(res.ecsOs);
            $("#system_disk").text(res.osDiskSize + "GB");
            $("#data_disk_size").text(res.dataDiskSize + "GB");
            $("#data_disk_num").text(res.dataDiskNum);

            $("#cpu_new_div").hide();
            $("#memory_new_div").hide();

            switch (res.applyStatus) {
                case "0":
                    $("#status").text("创建主机待审核");
                    break;
                case "1":
                    $("#status").text("创建主机通过");
                    showReply(res);
                    break;
                case "2":
                    $("#status").text("创建主机驳回");
                    showReply(res);
                    break;
                case "3":
                    $("#status").text("配置修改待审核");
                    $("#apply_num_div").hide();
                    $("#disk_div").hide();
                    $("#virtual_environment_div").hide();
                    $("#cpu_new_div").show();
                    $("#memory_new_div").show();
                    $("#cpu_label").text("原CPU：");
                    $("#memory_label").text("原内存：");
                    $("#cpu_new").text(res.vmCpuNew + " vCPU");
                    $("#memory_new").text(res.vmMemoryNew + " GB内存");
                    break;
                case "4":
                    $("#status").text("配置修改通过");
                    showReply(res);
                    break;
                case "5":
                    $("#status").text("配置修改驳回");
                    showReply(res);
                    break;
            }
        }
    });
}

function initPlatformType() {
    $.ajax({
        url: contextPath + "/service/ecs/getPlatformType",
        type: 'get',
        async: false,
        success: function (res) {
            global.PLATFORM_TYPE_SELECT_DATA.platformType = JSON.parse(res);
        }
    });

    var html = template('virtual_environment_script', global.PLATFORM_TYPE_SELECT_DATA.platformType);
    document.getElementById('virtual_environment_content').innerHTML = html;
}

function refresh_platform_type() {
    initPlatformType();
}

function showReply(res) {
    $("#virtual_environment_div").hide();
    $("#audit_reply_content").html("<span>" + res.auditReply + "</span>");
    $("#pass").hide();
    $("#reject").hide();
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