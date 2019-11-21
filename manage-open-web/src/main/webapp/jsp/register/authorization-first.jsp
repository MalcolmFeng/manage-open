<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String resourceId = request.getParameter("resourceId");
    String dataSourceType = request.getParameter("dataSourceType");
%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>书面协议和授权书认证</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <%--    <link rel="stylesheet" type="text/css" href="<l:asset path='css/select2.min.css'/>" />--%>
    <%--    <link rel="stylesheet" type="text/css" href="<l:asset path='platform/css/home.css'/>"/>--%>

    <script type="text/javascript" src="<l:asset path='jquery.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='form.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='arttemplate.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='knockout.js'/>"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
</head>
<body>
<div class="">
    <div class="row" style="padding: 0 20vw;display: flex;align-items: center;margin: 15px 0;">
        <img style="width: 1.5vw" src="/skins/skin/img/information.png" />
        <span style="margin-left: 15px;font-weight: bold;">填写机构信息</span>
    </div>
    <!-- 验证信息wrap -->
    <form class="form-horizontal" role="form">
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">机构名称<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="institute-name" value="" placeholder="机构名称" disabled="disabled" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">机构性质<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="institute-property" value="" placeholder="机构性质" disabled="disabled" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">法人<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="legal-person" value="" placeholder="法人" disabled="disabled" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">营业执照<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <div class="input-group" style="width: 60%;">
                    <input class="form-control ue-form" type="text" placeholder="选择上传文件" id="filelist">
                    <div class="input-group-addon ue-form-btn" id="inputfiles">
                        <span class="fa fa-upload"></span>
                    </div>
                </div>
                <div class="progress" style="display: none;margin-top: 10px;">
                    <div class="progress-bar" id="inputpro">
                        <span></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">统一社会信用代码/注册号/组织机构代码<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="register-num" value="" placeholder="统一社会信用代码/注册号/组织机构代码" disabled="disabled" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group form-inline">
            <label class="col-xs-3 col-md-3 control-label">机构注册地区<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <select class="form-control ue-form province" data-value="浙江省" data-first-title="选择省" disabled="disabled"></select>
                <select class="form-control ue-form city" data-value="杭州市" data-first-title="选择市" disabled="disabled"></select>
                <select class="form-control ue-form area" data-value="西湖区" data-first-title="选择地区" disabled="disabled"></select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">详细地址<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <textarea id="sketch" class="form-control ue-form Validform_input" rows="5" placeholder="详细地址" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称"></textarea>
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">机构联系人<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="linkman" value="" placeholder="机构联系人" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">机构联系人电话<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="linkman-tel" value="" placeholder="机构联系人电话" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">主管部门负责人<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="principal" value="" placeholder="主管部门负责人" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">主管部门负责人电话<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="principal-tel" value="" placeholder="主管部门负责人电话" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">技术部门负责人<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="technicist" value="" placeholder="技术部门负责人" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-xs-3 col-md-3 control-label">技术部门负责人电话<span class="required">*</span></label>
            <div class="col-xs-8 col-md-8">
                <input type="text" class="form-control ue-form Validform_input" id="technicist-tel" value="" placeholder="技术部门负责人电话" datatype="s5-16" errormsg="昵称至少5个字符,最多16个字符！" nullmsg="请设置正确名称">
                <span class="Validform_checktip Validform_span"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"></label>
            <div class="col-sm-9">
                <button type="button" class="btn ue-btn-primary" id="validate">下一步</button>
                <button type="button" class="btn ue-btn" id="cancel">返回</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="/skins/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="/skins/js/bootstrap.js"></script>
<script type="text/javascript" src="/skins/js/ui.js"></script>
<script type="text/javascript" src="/skins/js/form.js"></script>
</body>
</html>