<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务分组管理</title>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
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

<script  type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='form.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='jquery.form.js'/>"></script>
<script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>

<script type="text/javascript">
   var context = "<l:assetcontext/>";
	$(function() {
	    $("#saveForm").uValidform({
			btnSubmit:"#saveBtn",
			datatype:{//传入自定义datatype类型;
			    "gname": gname
			},
			callback:function(form){
			  save();
			}
		});
		//返回服务分组页面
		$("#closeBtn").click(function() {
		    parent.dialog.get(window).close(false);
		})
	});
	
	//保存实例
	function save(){
	    $.ajax({
	      type: "post",
	      url: context + "/service/service/group/save",
	      data: $("form").serialize(),
	      success: function(msg) {
	          console.log(msg);
	          if (msg.flag == "true") {
	              parent.dialog.get(window).close(true);
	    	  } else {
	    		  sticky(msg.msg, 'error', 'center');
	    	  }
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
		        autoclose : 1000, 
		        position : place,
		        style : type
		    }
		);
	}
    
	//分组名称的校验
	function gname(gets,obj,curform,datatype){
		if(gets.length > 32) {
		    obj.attr("errormsg","不能超过32个字符");
			return false;
		}
		var regx = /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
		return regx.test(gets);
	}
</script>
</head>
<body>
	<div class="container" id="sandbox-container">
	  <div class="col-xs-12 col-md-12">
			<form class="form-horizontal" id="saveForm" name="saveForm" onsubmit="return false">
				<input type="hidden" value="${group.id}" name="id" />
				<input type="hidden" value="${group.parentId }" name="parentId" />
				<div class="form-group">
					<label class="col-xs-3 col-md-3 control-label">
					          分组名称<span class="required">*</span>
					</label>
					<div class="col-xs-9 col-md-9">
						<input type="text" class="form-control ue-form Validform_input"
							id="groupName" name="name" value="${group.name}" placeholder="服务分组名称" 
							datatype="gname" errormsg="请输入字母、数字、汉字或下划线" nullmsg="请填写服务分组名称" />
						<span class="Validform_checktip Validform_span"></span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-xs-3 col-md-3 control-label">显示顺序</label>
					<div class="col-xs-9 col-md-9">
					    <input type="text" class="form-control ue-form Validform_input" name="seq"
							 errormsg="显示顺序必须为数字(0-99)" datatype="n1-2" nullmsg="显示顺序" value="${group.seq }">
						<span class="Validform_checktip Validform_span"></span>
					</div>
				</div>

				<div class="form-group">
					<label class="col-xs-4 col-md-4 control-label"></label>
					<div class="col-xs-8 col-md-8">
						<button type="button" class="btn ue-btn-primary" id="saveBtn">保存</button>&ensp;
						<button type="button" class="btn ue-btn" id="closeBtn">关闭</button>
						<span id="msgdemo"></span>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>