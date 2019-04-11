var global = {
    user_id: ""
};

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
            $("#instance_id").text(res.instanceId);
            $("#service_version").text(res.serviceVersion);
            $("#compute_resource").text(res.coreNum + "vCPU | " + res.memory + "GB内存");
            $("#storage_resource").text(res.storageVolume + "GB");
            $("#apply_time").text(res.applyTime);

            switch (res.applyStatus) {
                case "0":
                    $("#apply_status").text("待审核");
                    $("#audit_opinion_div").hide();
                    break;
                case "1":
                    $("#apply_status").text("审批通过");
                    if (res.auditOpinion != null) {
                        $("#audit_opinion").text(res.auditOpinion);
                    } else {
                        $("#audit_opinion").text("");
                    }
                    break;
                case "2":
                    $("#apply_status").text("审批驳回");
                    if (res.auditOpinion != null) {
                        $("#audit_opinion").text(res.auditOpinion);
                    } else {
                        $("#audit_opinion").text("");
                    }
                    break;
            }

            // 审核状态为待审核或审批驳回时，运行状态和访问信息不显示
            if ("0" == res.applyStatus || "2" == res.applyStatus) {
                $("#run_status_content").hide();
                $("#create_time_content").hide();
                $("#accountInfo").hide();
                $("#spaceInfo").hide();
            } else {
                switch (res.runStatus) {
                    case "0":
                        $("#run_status").text("创建中");
                        $("#accountInfo").hide();
                        $("#spaceInfo").hide();
                        $("#create_time_content").hide();
                        break;
                    case "1":
                        $("#run_status").text("创建成功");
                        $("#principal").text(res.userId + "@INDATA.COM");
                        $("#create_time").text(res.createTime);
                        showSpaceInfo();
                        break;
                    case "2":
                        $("#run_status").text("创建失败");
                        $("#accountInfo").hide();
                        $("#spaceInfo").hide();
                        $("#create_time_content").hide();
                        break;
                    case "3":
                        $("#run_status").text("运行中");
                        global.user_id = res.userId;
                        $("#principal").text(res.userId + "@INDATA.COM");
                        showSpaceInfo();
                        $("#create_time").text(res.createTime);
                        break;
                    case "4":
                        $("#run_status").text("删除中");
                        $("#create_time").text(res.createTime);
                        $("#accountInfo").hide();
                        $("#spaceInfo").hide();
                        break;
                    case "5":
                        $("#run_status").text("删除失败");
                        $("#create_time").text(res.createTime);
                        $("#accountInfo").hide();
                        $("#spaceInfo").hide();
                        break;
                }
            }
        }
    });

    // $.ajax({
    //     url: contextPath + "/service/mcs/getStorageVolume?instanceId=" + $("#instance_id").text() + "&userId=" + global.user_id,
    //     type: 'get',
    //     dataType: "json",
    //     success: function (res) {
    //         var spaceQuota = res.spaceQuota;
    //         var spaceUsed = res.spaceUsed;
    //         var useRate = res.useRate;
    //
    //         $(".progress-bar").css("width", useRate + "%");
    //         $("#storageUsage").text(useRate + "%" + "  " + spaceUsed + "G/" + spaceQuota + "G");
    //     }
    // })
}

function downloadCA() {
    window.location.href = "http://" + window.location.host + "/dev/keytab/" + global.user_id + "_keytab.zip";
}

function downloadhosts() {
    window.location.href = "http://" + window.location.host + "/dev/service/home/downloadFiles?subPath=hostConfig&fileName=hosts&clusterName=cluster3948";
}

function showSpaceInfo() {
    // $("#hdfs_path").text("/dev/" + global.user_id.split("-")[0] + "/" + $("#instance_id").text().substring(0, 4));
    // $("#hive_dbname").text(global.user_id.split("-")[0] + "_" + $("#instance_id").text().substring(0, 4));
    // $.ajax({
    //     url: contextPath + "/service/mcs/getHiveUrl?userId=" + global.user_id,
    //     type: 'get',
    //     dataType: "json",
    //     success: function (res) {
    //         $("#hive_url").text(res.hiveJdbcConnection);
    //         $("#hive2_url").text(res.hive2JdbcConnection)
    //     }
    // });
    // $("#yarn_name").text(global.user_id.split("-")[0] + "_" + $("#instance_id").text().substring(0, 4));
    $.ajax({
        url: contextPath + "/service/mcs/queryTenant?instanceId=" + $("#instance_id").text() + "&userId=" + global.user_id,
        type: 'get',
        dataType: "json",
        success: function (res) {
            var data = res.data;
            $.each(data, function (index, item) {
                var tr = "<tr><td>" + global.user_id.split("-")[0] + "_" + $("#instance_id").text().substring(0, 4) + "</td>"
                    + "<td>" + item.service + "</td>"
                    + "<td>" + item.resPath + "</td>";
                if (item.service == "HDFS") {
                    tr = tr + "<td>" + "【空间配额：" + item.spaceQuota + "GB】"
                        + "【已使用：" + item.spaceUsed + "GB】"
                        + "【已使用占比：" + item.useRate + "%】</td></tr>";
                }
                if (item.service == "Hive") {
                    tr = tr + "<td>" + "【HDFS URL：" + item.resQuota1 + "】</td></tr>";
                }
                if (item.service == "Yarn") {
                    tr = tr + "<td>" + "【资源占比：" + item.resQuota1 + "%】【资源使用上限：" + item.resQuota2 + "%】</td></tr>";
                }
                $("#tenant_space").append(tr);
            })
        }
    });
}