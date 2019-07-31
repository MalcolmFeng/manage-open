'use strict';

$(function () {
    initPageDom();
    initPageEvent();
});

function initPageDom() {
    initAccount();
}

function initAccount() {
    $.ajax({
        url: context + "/service/pay/accountOverView",
        type: "GET",
        success: function (data) {
            var dataRtn = data.accountBalance;
            $("#accountNum").text(dataRtn);
        }
    });
}

function initPageEvent() {
    $(document).on("click", "#payBtn", function () {
        window.location.href = context + "/jsp/service/pay/payinfo.jsp";
    });
}