var global = {
    domain: {
        existServerName: false,
        KEY_SELECT_DATA: {
            keyList: []
        },
        DATA_DISK_NUM: 4,
        USER: {
            user: "root"
        }
    }
};

$(document).ready(function () {
    initPageDom();

    //根据单选按钮切换登录凭证
    $("input[type='radio']").bind('click', function () {
        var value = $("input[name='login']:checked").val();
        var html = '';
        switch (value) {
            case 'key':
                html = template('login_key', global.domain.KEY_SELECT_DATA);
                break;
            case 'pwd':
                html = template('login_pwd', global.domain.USER);
                break;
        }
        document.getElementById('login_content').innerHTML = html;
    });

    //点击增加数据盘
    // $(".add").bind('click', function () {
    //     var html = template('data_disk', {});
    //     //查找增加按钮的带有"form-group"类的父元素,并在其前面添加div
    //     var obj = $(this).parent().parent().parent();
    //     obj.before(html);
    //
    //     //减少数据盘数量数值
    //     global.domain.DATA_DISK_NUM -= 1;
    //     $("#data_disk_num").text(global.domain.DATA_DISK_NUM);
    //     //可增加数据盘为0时，增加按钮不可点击
    //     if (global.domain.DATA_DISK_NUM == 0) {
    //         $(".add").attr("disabled", true);
    //     }
    // });

    $("#applyServer").uValidform({
        btnSubmit: "#apply",
        datatype: {
            "instance": checkInstance,
            "keypair": checkKeypair,
            "pwd": checkPwd,
            "systemDisk": checkSystemDisk,
            "dataDisk": checkDataDisk,
            "positiveInteger": checkPositiveInteger
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

    $("#cancel").click(function () {
        window.location.href = contextPath + "/service/ecs/manage";
    });

});

//点击新建密钥对打开新建密钥对窗口
function toNewKey() {
    $.dialog({
        type: 'iframe',
        url: contextPath + '/jsp/cloud/ecs/NewKey.jsp',
        title: '新建密钥对',
        width: 800,
        height: 500,
        onclose: function () {
            if (this.returnValue == "ok") {
                //刷新当前页面
                //window.location.href = contextPath + "/service/ecs/manage";
                //$("#instance_name").text("变更规格成功");
            }
        }
    });
}

//点击刷新密钥对选项
function refresh_key() {
    initLogin();
}

//点击删除当前行数据盘
// function remove_data_disk(obj) {
//     var parent = $(obj).parent().parent().parent();
//     parent.remove();
//     global.domain.DATA_DISK_NUM += 1;
//     $("#data_disk_num").text(global.domain.DATA_DISK_NUM);
//     $(".add").removeAttr("disabled");
// }

function initPageDom() {
    //初始化下拉框
    initSelect();

    //初始化登录凭证
    initLogin();

    //初始化可增加数据盘数量
    $("#data_disk_num").text(global.domain.DATA_DISK_NUM);

}

//初始化登录凭证
function initLogin() {
    $.ajax({
        url: contextPath + "/service/ecs/getKeyList",
        type: 'get',
        async: false,
        success: function (res) {
            global.domain.KEY_SELECT_DATA = JSON.parse(res);
        }
    });

    var html = template('login_key', global.domain.KEY_SELECT_DATA);
    document.getElementById('login_content').innerHTML = html;
}

//初始化下拉框
function initSelect() {
    //规格联动下拉框
    //$.cxSelect.defaults.data = global.domain.cpu;
    $.cxSelect.defaults.url = contextPath + "/jsp/cloud/ecs/data/cpu";
    $('#vcpu_memory_val').cxSelect({
        selects: ['vcpu', 'memory'],
        nodata: 'none',
        required: true
    });

    //镜像联动下拉框
    //$.cxSelect.defaults.data = global.domain.memory;
    $.cxSelect.defaults.url = contextPath + "/jsp/cloud/ecs/data/os";
    $('#os_version_val').cxSelect({
        selects: ['os', 'version'],
        nodata: 'none',
        required: true
    });
}

//系统盘失去焦点时若输入为小数则自动转换为整数
function toInteger(obj) {
    if ($(obj).val() < 100) {
        $(obj).val(100);
    } else if ($(obj).val() > 500) {
        $(obj).val(500);
    } else {
        $(obj).val($(obj).val().split(".")[0]);
    }
}

//检查云主机名称是否已存在
function existServerName(name) {
    var applyNumber = $("#apply_number").val();

    $.ajax({
        url: contextPath + "/service/ecs/checkEcsName",
        type: 'post',
        contentType: 'application/json',
        async: false,
        data: JSON.stringify(
            {
                "vm_number": applyNumber,
                "new_vm_name_apply": name
            }
        ),
        dataType: "json",
        success: function (res) {
            if ("exist" == res.response) {
                global.domain.existKey = true;
            } else {
                global.domain.existKey = false;
            }
        }
    });
}

//验证实例名称
function checkInstance(gets, obj, curform, datatype) {
    //实例名称正则表达式 2-128个字符，以大小写字母或中文开头，可包含数字、"."、"_"、":"或"-"
    var regx = /^([a-zA-Z]|[\u4E00-\u9FA5]){1}[a-zA-Z\u4E00-\u9FA5\d\.\:\-\_]{1,127}$/;
    if (regx.test(gets) == false) {
        return false;
    }

    //检查云主机名称是否已存在
    existServerName(gets);
    if (global.domain.existKey) {
        html = template('exist_tips_content', {});
        document.getElementById('exist_tips').innerHTML = html;
        $("#instance_name").addClass("input_error");
        return false;
    } else {
        html = "";
        document.getElementById('exist_tips').innerHTML = html;
        $("#instance_name").removeClass("input_error");
    }
    return true;
}

//验证系统盘
function checkSystemDisk(gets, obj, curform, datatype) {
    var res = isPositiveInteger(gets, 20, 500);
    return res;
}

//验证数据盘容量
function checkDataDisk(gets, obj, curform, datatype) {
    var res = isPositiveInteger(gets, 20, 500);
    return res;
}

//验证数量是否为正整数
function checkPositiveInteger(gets, obj, curform, datatype) {
    var res = isPositiveInteger(gets, null, null);
    return res;
}

function checkKeypair(gets, obj, curform, datatype) {
    if (gets === '') {
        html = '<span style="margin-left:5px;color:#ffb437">请先输入密钥对或自定义密码</span>';
        document.getElementById('login_tips').innerHTML = html;
        return false;
    }
    return true;
}

//验证密码
function checkPwd(gets, obj, curform, datatype) {
    if (gets === '') {
        html = '<span style="margin-left:5px;color:#ffb437">请先输入密钥对或自定义密码</span>';
        document.getElementById('login_tips').innerHTML = html;
        return false;
    }

    //密码正则表达式 8-30个字符，必须同时包含(大写字母、小写字母、数字和特殊字符)中至少三项
    var regx = /^(?!([a-zA-Z]+|[a-z\d]+|[a-z~`@#\$%\^&\*\(\)_\-\+=\{\[\}\]\|\\:;\"\'<,>\.\?\/\!]+|[A-Z\d]+|[A-Z~`@#\$%\^&\*\(\)_\-\+=\{\[\}\]\|\\:;\"\'<,>\.\?\/\!]+|[\d~`@#\$%\^&\*\(\)_\-\+=\{\[\}\]\|\\:;\"\'<,>\.\?\/\!]+)$)[a-zA-Z\d~`@#\$%\^&\*\(\)_\-\+=\{\[\}\]\|\\:;\"\'<,>\.\?\/\!]{8,30}$/;
    if (regx.test(gets) == false) {
        html = '<span style="margin-left:5px;color:#ffb437">密码格式不符</span>';
        document.getElementById('login_tips').innerHTML = html;
        return false;
    }
    return true;
}

//验证正整数及范围
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
    var vCPU = parseInt($("#vcpu").val().split(" ")[0]);
    var memory = parseInt($("#memory").val().split(" ")[0]);
    var os_version = $("#os").val() + "-" + $("#version").val().split(" ")[0];
    var system_disk = parseInt($("#system_disk").val());
    var data_disk_size = parseInt($("#data_disk_size").val());
    var data_disk_num = parseInt($("#data_disk_num").val());
    var instance_name = $("#instance_name").val();
    var apply_number = parseInt($("#apply_number").val());
    var apply_reason = $.trim($("#apply_reason").val());

    var value = $("input[name='login']:checked").val();
    var key_pair = "";
    var pwd = "";
    //密钥对
    if ("key" == value) {
        var key_pair = $("#key_pair").val();
    } else if ("pwd" == value) {
        //登录密码
        var pwd = $("#pwd").val();
    }

    $.ajax({
        url: contextPath + "/service/ecs/saveInstance",
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(
            {
                "vm_number": apply_number,
                "vm_system": os_version,
                "vm_cpu": vCPU,
                "vm_memory": memory,
                "vm_os_disk_size": system_disk,
                "vm_data_disk_number": data_disk_num,
                "vm_data_disk_size": data_disk_size,
                "apply_reason": apply_reason,
                "vm_name_apply": instance_name,
                "key_name": key_pair,
                "rootpassword": pwd

            }
        ),
        dataType: "json",
        success: function (res) {
            var flag = res.result;
            if ("true" == flag) {
//				UIAlert("保存成功!");
                window.location.href = contextPath + "/service/ecs/manage";
            } else {
                UIAlert(L.getLocaleMessage("savefailed", "保存失败！"));
            }
        }
    });
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