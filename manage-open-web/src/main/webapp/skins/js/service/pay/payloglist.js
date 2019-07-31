$(document).ready(function () {

    var url = context + "/service/pay/paylogData";     //充值记录接口
    grid = new L.FlexGrid("payList", url);
    grid.init({}); //初始化datatable
});

//渲染操作列
function renderoptions(data, type, full) {
    //渲染继续支付按钮需要进行判断，当状态是未支付状态时才可以
    var html = "";
    var gopayBtn = '<div><a onclick="goPay(\'' + full.orderId + '\')">继续支付</a>';
    if (full.payStatus == '0') {/* 状态为未支付 */
        html += gopayBtn;
    } else if (full.payStatus == '2') {
        //无链接
        html += '';
    } else {
        html += '';
    }
    return html;
}

//渲染支付状态
function renderstatus(data, type, full) {
    //渲染继续支付按钮需要进行判断，当状态是未支付状态时才可以
    var status;
    if (full.payStatus == '0') {/* 状态为未支付 */
        status = '<span><i class="fa fa-clock-o" style="margin-right: 5px;color: #ccc"></i>未支付</span>';
    } else if (full.payStatus == '2') {
        status = '<span><i class="fa fa-exclamation-circle" style="margin-right: 5px;color: red"></i>支付异常</span>';
    } else {
        status = '<span><i class="fa fa-check-circle" style="margin-right: 5px;color: green"></i>支付成功</span>';
    }
    return status;
}

function reloadpayList() {
    // 重新请求数据
    $("#payList").DataTable().ajax.reload();
}

function goPay(orderId) {
    var url = context + "/service/pay/requestForPay?orderId=" + orderId;
    window.open(url, "_blank");
}

//弹窗提示样式
function sticky(msg, style, position) {
    var type = style ? style : 'success';
    var place = position ? position : 'top';
    $.sticky(
        msg,
        {
            autoclose: 1000,
            position: place,
            style: type
        }
    );
}

