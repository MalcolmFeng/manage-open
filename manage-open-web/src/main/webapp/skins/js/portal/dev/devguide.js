$(function () {
    initPageEvent();
    setIframeHeight(document.getElementById('mainframe'));
});


function initPageEvent() {
    //切换侧导航active状态
    $(document).on("click","#api-cat-bottom>ul li",activeToggle);
}

function setIframeHeight(iframe) {
    if (iframe) {
        var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
        if (iframeWin.document.body) {
            iframe.height = $("#mainframe").contents().find("#api-container").height();
        }
    }
}

//切换侧导航active状态
function activeToggle() {
    $(this).addClass("active").siblings().removeClass("active");
}


