'use strict';

$(function () {
    initPageDom();
    initPageEvent();
});

function initPageDom() {
    initAccountOverView();
}

function initAccountOverView() {
    $.ajax({
        url: context + "/service/pay/accountOverView",
        type: "GET",
        success: function (data) {
            var dataRtn = data.accountBalance;
            $(".pay-balance").text("¥ " + dataRtn);
        }
    });
}

function initPageEvent() {
    $("#payBtn").click(pay);
    $(document).on("change", "#rechargeAccount", rechargeAccountChange);
}

function pay() {
    var rechargeAccount = $("#rechargeAccount").val();
//	if(!rechargeAccount || !(/^[0-9]{1,18}[\.]?[0-9]{0,2}$/.test(rechargeAccount))){
//		 alert("输入金额不合法");
//		 return;
//	}
    $.dialog({
        type: 'iframe',
        url: context + '/jsp/service/pay/recharge.jsp',
        padding: 0,
        title: '充值',
        width: 600,
        height: 120,
        okValue: '充值成功',
        ok: function () {
            // 点击充值成功，查询余额，并更新数据
            initAccountOverView();
        },
        cancelValue: '充值失败', // 失败的话暂时不处理，可以去充值记录里面继续支付
        cancel: function () {
        }
    });
    var url = context + "/service/pay/requestForPay?amount=" + $("#rechargeAccount").val() + "&orderTitle=openService&orderDesc=This is open service order";
    window.open(url, "_blank");
}

function rechargeAccountChange() {
    var account = $("#rechargeAccount").val();
    if (account != "" && account != null) {
        if ((/^[0-9]{1,18}[\.]?[0-9]{0,2}$/.test(account))) {
            $("#payBtn").show();
            $("#payBtnNoAccount").hide();
        } else {
//			alert("请输入正确的金额，小数后最多两位");
            $.dialog({
                type: 'alert',
                content: '请输入正确的金额，小数点后最多两位！',
                ok: function () {
                }
            });
            $("#payBtn").hide();
            $("#payBtnNoAccount").show();
        }
    } else {
        $("#payBtn").hide();
        $("#payBtnNoAccount").show();
    }
}