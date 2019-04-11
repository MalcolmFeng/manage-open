"use strict";
var global = {
    flexgrid: null // 存放grid
};

$(function () {
    // 初始化页面元素
    initPageDom();

    // 初始化页面事件
    initPageEvent();

    // 初始化下拉框
    initSelect();

    // 表单校验
    $("#mcsApply").uValidform({
        btnSubmit: "#apply",
        datatype: {
            "instance_name": checkInstance,
            "storage_volume": checkStorageVolume
        },
        callback: function (form) {
            $.dialog({
                type: 'confirm',
                content: "您确认申请吗？",
                ok: function () {
                    save();
                },
                cancel: function () {
                }
            });
        }
    });
});

function initPageDom() {
    // 初始化表格
    global.flexgrid = initTable();
}

function initTable() {
    var serviceId = $("#service_version").val().split("-")[0];
    var url = contextPath + "/service/mcs/getComponents";
    var options = {
        ordering: false,
        paging: false
    };
    var grid = new L.FlexGrid("service-component-table", url);
    grid.setParameter("service_id", serviceId);
    grid.init(options);
    return grid;
}

function initPageEvent() {
    // 大数据计算服务产品版本change事件
    $("#service_version").on("change", serviceVersionChange);
}

function serviceVersionChange() {
    var serviceId = $("#service_version").val().split("-")[0];
    global.flexgrid.reload(null, {"service_id": serviceId});
}

// 初始化下拉框
function initSelect() {
    // 规格联动下拉框
    // $.cxSelect.defaults.data = global.domain.cpu;
    $.cxSelect.defaults.url = contextPath + "/jsp/cloud/mcs/data/cpu";
    $('#core_num_memory').cxSelect({
        selects: ['core_num', 'memory'],
        nodata: 'none',
        required: true
    });
}

// 存储容量失去焦点时若输入为小数则自动转换为整数
function toInteger(obj) {
    if ($(obj).val() < 20) {
        $(obj).val(20);
    } else if ($(obj).val() > 500) {
        $(obj).val(500);
    } else {
        $(obj).val($(obj).val().split(".")[0]);
    }
}

// 验证实例名称
function checkInstance(gets, obj, curform, datatype) {
    // 可使用自动生成名称，也可自定义。自定义实例名称可由大写字母、小写字母、数字、“-”、“_”组成，最大支持64个字符
    var regx = /^[a-zA-Z\u4E00-\u9FA5\d\.\:\-\_]{1,64}$/;
    if (regx.test(gets) == false) {
        return false;
    }
    return true;
}

// 验证存储容量
function checkStorageVolume(gets, obj, curform, datatype) {
    var res = isPositiveInteger(gets, 20, 500);
    return res;
}

// 验证正整数及范围
function isPositiveInteger(num, min, max) {
    var regx = /^[1-9]\d*$/;
    if (regx.test(num) == false) {
        return false;
    }
    if (min != null && max != null) {
        if (num < min || num > max) {
            return false;
        }
    }
    return true;
}

function save() {
    var instanceName = $("#instance_name").val();
    var serviceVersion = $("#service_version").val();
    var coreNum = $("#core_num").val();
    var memory = $("#memory").val();
    var storageVolume = $("#storage_volume").val();
    $.ajax({
        url: contextPath + "/service/mcs/saveInstance",
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(
            {
                "instanceName": instanceName,
                "serviceVersion": serviceVersion,
                "coreNum": coreNum,
                "memory": memory,
                "storageVolume": storageVolume
            }
        ),
        dataType: "json",
        success: function (res) {
            if ("true" === res.result) {
                window.location.href = contextPath + "/service/mcs/manage";
            } else {
                UIAlert(L.getLocaleMessage("savefailed", "保存失败！"));
            }
        }
    });
}

$("#cancel").click(function () {
    window.location.href = contextPath + "/service/mcs/manage";
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