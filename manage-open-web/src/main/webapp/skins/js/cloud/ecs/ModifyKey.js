var global = {
    domain: {
        KEY_SELECT_DATA: {
            keyList: []
        }
    }
};

$(document).ready(function () {
    initLogin();

    $("#sure").click(function () {
        $.ajax({
            url: contextPath+ "/service/ecs/modifyKey",
            type: 'post',
            contentType : 'application/json',
            data: JSON.stringify(
                {
                    "vm_hostname": instanceName,
                    "key_name": $("#key_pair").val()
                }),
            dataType: "json",
            success: function(res){
                var flag=res.result;
                if("true"==flag){
                    var dialog = parent.dialog.get(window);
                    dialog.close("ok");
                } else {
                    UIAlert("保存失败!");
                }
            }
        });
    });

    //取消
    $("#cancel").click(function(){
        var dialog = parent.dialog.get(window);
        dialog.close();
    })
});

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

function refresh_key() {
    initLogin();
}

function toNewKey() {
    window.open(contextPath + "/jsp/cloud/ecs/NewKey.jsp");
}