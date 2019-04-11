$(document).ready(function(){
    $("#platformInfoForm").uValidform({
        btnSubmit:"#sure",
        callback:function(form){
            save();
        }
    });

    //取消
    $("#cancel").click(function(){
        var dialog = parent.dialog.get(window);
        dialog.close();
    })

});

function save() {
    $.ajax({
        url: contextPath+ "/service/ecs/platformSave",
        type: 'post',
        contentType : 'application/json',
        data: JSON.stringify(
            {
                "platform_type": $("#platform_type").val(),
                "vsphere_info_host": $("#host").val(),
                "vsphere_info_user": $("#username").val(),
                "vsphere_info_passwd": $("#password").val()
            }
        ),
        dataType: "json",
        success: function(res){
            var flag=res.result;
            if("true" == flag){
                var dialog = parent.dialog.get(window);
                dialog.close("ok");
            } else {
                UIAlert("保存失败!");
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