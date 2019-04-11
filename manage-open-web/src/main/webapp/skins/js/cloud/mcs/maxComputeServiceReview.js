$(document).ready(function () {
    // 初始化页面信息
    initPageInfo();
});

// 查询基本信息-配置信息
function initPageInfo() {
    $.ajax({
        url: contextPath + "/service/mcs/getDetailsByInstanceId",
        type: 'post',
        contentType: 'application/json',
        async: false,
        data: JSON.stringify(
            {
                "instance_id": instanceId
            }
        ),
        dataType: "json",
        success: function (res) {
            $("#instance_name").text(res.instanceName);
            $("#service_version").text(res.serviceVersion);
            $("#apply_user").text(res.userId);
            $("#apply_time").text(res.applyTime);
            $("#compute_resource").text(res.coreNum + "vCPU | " + res.memory + "GB内存");
            $("#storage_resource").text(res.storageVolume + "GB");

            switch (res.applyStatus) {
                case "0":
                    $("#status").text("待审核");
                    $("#reply_time_div").hide();
                    break;
                case "1":
                    $("#status").text("审批通过");
                    if (res.auditOpinion != null) {
                        $("#audit_reply_content").html("<span>" + res.auditOpinion + "</span>");
                    } else {
                        $("#audit_reply_content").html("<span></span>");
                    }
                    $("#reply_time").text(res.replyTime);
                    break;
                case "2":
                    $("#status").text("审批驳回");
                    if (res.auditOpinion != null) {
                        $("#audit_reply_content").html("<span>" + res.auditOpinion + "</span>");
                    } else {
                        $("#audit_reply_content").html("<span></span>");
                    }
                    $("#reply_time").text(res.replyTime);
                    break;
            }

            // 审核状态为审核通过或审批驳回时，通过和驳回按钮不显示
            if ("1" == res.applyStatus || "2" == res.applyStatus) {
                $("#pass").hide();
                $("#reject").hide();
            }
        }
    })
}

// 通过
$("#pass").click(function () {
    $.dialog({
        type: 'confirm',
        content: '您确定要通过这个实例吗？',
        ok: function () {
            $.ajax({
                url: contextPath + "/service/mcs/passInstance",
                type: 'post',
                contentType: 'application/json',
                async: false,
                data: JSON.stringify(
                    {
                        "instance_id": instanceId,
                        "audit_opinion": $("#audit_reply").val()
                    }
                ),
                dataType: "json",
                success: function (res) {
                    if ("true" === res.result) {
                        window.location.href = contextPath + "/service/mcs/review";
                    } else {
                        UIAlert(L.getLocaleMessage("passfailed",res.message));
                    }
                }
            })
        },
        cancel: function () {
        }
    });
});

// 驳回
$("#reject").click(function () {
    $.dialog({
        type: 'confirm',
        content: '您确定要驳回这个实例吗？',
        ok: function () {
            $.ajax({
                url: contextPath + "/service/mcs/rejectInstance",
                type: 'post',
                contentType: 'application/json',
                async: false,
                data: JSON.stringify(
                    {
                        "instance_id": instanceId,
                        "audit_opinion": $("#audit_reply").val()
                    }
                ),
                dataType: "json",
                success: function (res) {
                    if ("true" === res.result) {
                        window.location.href = contextPath + "/service/mcs/review";
                    } else {
                        UIAlert(L.getLocaleMessage("rejectfailed", "驳回失败！"));
                    }
                }
            })
        },
        cancel: function () {
        }
    });
});

// 弹窗函数
function UIAlert(content) {
    $.dialog({
        type: 'alert',
        content: content,
        ok: function () {
        }
    });
}