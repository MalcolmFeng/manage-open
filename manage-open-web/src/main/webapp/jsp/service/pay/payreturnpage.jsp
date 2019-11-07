<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>浪潮 Health Mall 开放平台-支付结果</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/pay/payreturnpage.css'/>"/>
    <script type="text/javascript">
        //项目上下文
        var context = "<%=request.getContextPath()%>";
        //获取静态资源上下文路径
        var assetPath = "<l:assetcontext/>";
    </script>
</head>
<body style="height:100%;">
<div class="container col-xs-12 col-md-12">
    <div class="row">
        <div id="page-title" class="col-xs-12 col-md-12">支付结果</div>
    </div>
    <div id="payment-result" class="col-xs-12 col-md-12">
		<c:if test="${ code == '200' }">
			<div id="payment-status">
				<i class="fa fa-check-circle-o"></i>
				<span id="status-text">${msg }</span><!-- <span id="countTime"></span> 秒后关闭当前窗口 -->
			</div>
			<div id="payment-list">
				<table class="grid" cellspacing="0">
					<tbody>
					<tr>
						<td class="head">订单编号</td>
						<td class="con">${out_trade_no }</td>
					</tr>
					<tr>
						<td class="head">支付编号</td>
						<td class="con">${trade_no }</td>
					</tr>
					<tr>
						<td class="head">金额</td>
						<td id="money" class="con"><i class="fa fa-yen"></i>${ total_amount }</td>
					</tr>
					</tbody>
				</table>
			</div>
		</c:if>
		<c:if test="${ code != '200' }">
			<div id="payment-status" style="background-color: #fffae3">
				<i class="fa fa-times-circle-o" style="color: red"></i>
				<span id="status-text">支付失败！${ msg }</span>
			</div>
		</c:if>

		<div id="opt-btn" class="col-xs-12 col-md-12">
			<a id="my-account" href="" class="btn btn-primary" role="button" onclick="closeTab()">关闭</a>
			<!-- <a id="my-account" href="" class="btn btn-primary" role="button">我的账户</a>
            <a id="go-on" href="" class="btn btn-primary" role="button">继续支付</a> -->
		</div>
    </div>
</div>
<!-- 需要引用的JS -->
<%-- <script type="text/javascript" src="<l:asset path='jquery.js'/>"></script> --%>
<script type="text/javascript">
	//var time=15;
	/* function closeWindow(){
        window.setTimeout('closeWindow()',1000);
        debugger;
        if(time>0){
            //document.getElementById("show").innerHTML=" 倒计时<font color=red>"+time+"</font>秒后关闭当前窗口";
            document.getElementById("countTime").innerText=time;
            time--;
        }  else{

            window.opener=null; //关闭窗口时不出现提示窗口
            window.open('','_self');
            window.close();
        }
    } */

	//window.onload=closeWindow;
    function closeTab() {

		// 关闭窗口前，要不要发送一个ajax请求查询订单，以确认是否支付成功？？？

        window.opener = null; //关闭窗口时不出现提示窗口
        window.open('', '_self');
		window.close();

    }
</script>
</body>
</html>