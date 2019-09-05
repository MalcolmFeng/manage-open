<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>IP黑白名单</title>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <style type="text/css">
        .Validform_input {
            width: 85%;
        }

        .Validform_right {
            width: 10%;
        }

        .control-label {
            text-align: right;
        }
    </style>
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->

    <script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='form.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='jquery.form.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript">
        var context = "<l:assetcontext/>";
        var isIPV4 = true;
        $(function () {
            $("#saveForm").uValidform({
                btnSubmit: "#saveBtn",
                datatype: {//传入自定义datatype类型;
                    "checkipv6": checkipv6,
                    "checkipv4": checkipv4
                },
                callback: function (form) {
                    $.dialog({
                        type: 'confirm',
                        content: '您确定要将[' + (isIPV4 ? $("#ipV4").val() : $("#ipV6").val()) + ']添加到' + ($("#listType").val() == 'black' ? '黑名单' : '白名单') + '吗？',
                        ok: function () {
                            save();
                        },
                        cancel: function () {
                        }
                    });
                }
            });

            $("#ipType").change(function () {
                var result = $("#ipType option:selected").val();
                if (result == "v4") {
                    $("#6").hide();
                    $("#4").show();
                    isIPV4 = true;
                } else {
                    $("#4").hide();
                    $("#6").show();
                    isIPV4 = false;

                }
            })
        });

        //保存实例
        function save() {
            $.ajax({
                type: "post",
                url: context + "/service/open/iplist/addIpList",
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify({
                    id: "",
                    ipV4: isIPV4 ? $("#ipV4").val() : "",
                    ipV6: isIPV4 ? "" : $("#ipV6").val(),
                    type: $("#listType").val(),
                    active: $("#active").val()
                }),
                success: function (msg) {
                    console.log(msg);
                    if (msg.code == "200") {
                        $.dialog({
                            autofocus: true,
                            type: "alert",
                            content: "成功!"
                        });
                        setTimeout(function () {
                            window.location.href = context + "/service/open/iplist/getPage";
                        }, 1000);
                    } else {
                        sticky(msg.error, 'error', 'center');
                    }
                },
                error: function (msg) {
                    sticky(msg, 'error', 'center');
                }
            });
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

        function checkipv4(gets, obj, curform, datatye) {
            if (!isIPV4) {
                return true;
            }
            var patt = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;
            if (!patt.test(gets)) {
                obj.attr("errormsg", "请填写正确的IPv4地址");
                return false;
            }
            return true;
        }

        function checkipv6(gets, obj, curform, datatye) {
            if (isIPV4) {
                return true;
            }
            var patt = /^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$/;
            if (!patt.test(gets)) {
                obj.attr("errormsg", "请填写正确的IPv6地址");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>

<div class="container" id="sandbox-container">
    <div class="col-xs-12 col-md-12">
        <form class="form-horizontal" id="saveForm" name="saveForm" onsubmit="return false">
            <div id="step_0" class="_step">
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">IP地址类型<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <select class="form-control  ue-form Validform_input" datatype="s" placeholder="是否生效"
                                id="ipType" name="ipType" data-bind="value: ipType">
                            <option value="v4">IPv4</option>
                            <option value="v6">IPv6</option>
                        </select>
                        <span class="Validform_checktip Validform_span">请选择IP地址类型</span>
                    </div>
                </div>
                <div id="4" class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">IPv4 地址<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <input type="text" class="form-control ue-form Validform_input" id="ipV4"
                               name="name" value="${ipList.ipV4}" placeholder="IPv4 地址"
                               datatype="checkipv4" errormsg="请输入正确的IPv4地址" nullmsg="请输入IPv4 地址"/>
                        <span class="Validform_checktip Validform_span">请输入IPv4 地址</span>
                    </div>
                </div>

                <div id="6" class="form-group" style="display: none">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">IPv6 地址<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <input type="text" class="form-control ue-form Validform_input" id="ipV6"
                               name="name" value="${ipList.ipV6}" placeholder="IPv6 地址"
                               datatype="checkipv6" errormsg="请输入正确的IPv6地址" nullmsg="请输入IPv6地址"/>
                        <span class="Validform_checktip Validform_span">请输入IPv6地址</span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">黑白名单类型<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <select class="form-control  ue-form Validform_input" datatype="s" placeholder="黑白名单类型"
                                id="listType" name="listType" data-bind="value: listType">
                            <option value="black">黑名单</option>
                            <option value="white">白名单</option>
                        </select>
                        <span class="Validform_checktip Validform_span">请选择黑白名单类型</span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">是否生效<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <select class="form-control  ue-form Validform_input" datatype="s" placeholder="是否生效"
                                id="active" name="active" data-bind="value: active">
                            <option value="true">生效</option>
                            <option value="false">暂停生效</option>
                        </select>
                        <span class="Validform_checktip Validform_span">请选择是否生效</span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">备注</label>
                    </div>
                    <div class="col-xs-10 col-md-10 text-left radio" style="margin-top: 5px;">
                        <input type="text" style="width: 25%" class="form-control ue-form Validform_input" id="price"
                               name="price" value="${serviceDef.price}" placeholder="价格"/>
                    </div>
                </div>

                <%--                <div class="form-group">--%>
                <%--                    <label class="col-xs-3 col-md-3 control-label"></label>--%>
                <%--                    <div class="col-xs-9 col-md-9">--%>
                <%--                        <button type="button" class="btn ue-btn-primary" id="saveBtn">添加</button>&ensp;--%>
                <%--                        <button type="button" class="btn ue-btn" id="closeBtn">取消</button>--%>
                <%--                        <span id="msgdemo"></span>--%>
                <%--                    </div>--%>
                <%--                </div>--%>

                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label"></label>
                    <div class="col-xs-10 col-md-10">
                        <input type="submit" class="btn ue-btn-primary" value="添加">&emsp;
                        <input id="goback" type="button" class="btn ue-btn" value="返回">
                    </div>
                </div>

            </div>
        </form>
    </div>
</div>

</body>
</html>
