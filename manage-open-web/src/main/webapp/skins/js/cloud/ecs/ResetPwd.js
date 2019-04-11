$(document).ready(function() {
	
	$("#pwdForm").uValidform({
	    btnSubmit:"#sure",
	    datatype:{ 
	    	  "current_pwd": checkCurrentPwd,
	          "pwd": checkPwd	
	    },
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

//验证当前密码是否正确
function checkCurrentPwd(gets,obj,curform,datatype){
	var html = "";
	if(password == gets){
		html = "";
		document.getElementById('exist_tips').innerHTML = html;
		$("#current_pwd").removeClass("input_error");
		return true;
	}
	//显示错误提示文字
	html = template('exist_tips_content',{});
	document.getElementById('exist_tips').innerHTML = html;
	$("#current_pwd").addClass("input_error");
	return false;
}

//验证登录密码
function checkPwd(gets,obj,curform,datatype){
	//密码正则表达式 8-30个字符，必须同时包含(大写字母、小写字母、数字和特殊字符)中至少三项
	var regx = /^(?!([a-zA-Z]+|[a-z\d]+|[a-z~`@#\$%\^&\*\(\)_\-\+=\{\[\}\]\|\\:;\"\'<,>\.\?\/\!]+|[A-Z\d]+|[A-Z~`@#\$%\^&\*\(\)_\-\+=\{\[\}\]\|\\:;\"\'<,>\.\?\/\!]+|[\d~`@#\$%\^&\*\(\)_\-\+=\{\[\}\]\|\\:;\"\'<,>\.\?\/\!]+)$)[a-zA-Z\d~`@#\$%\^&\*\(\)_\-\+=\{\[\}\]\|\\:;\"\'<,>\.\?\/\!]{8,30}$/;
	if(regx.test(gets) == false) {
	      return false;
  }
	return true;
}


//重置密码
function save(){
	$.ajax({
		url: contextPath+ "/service/ecs/resetpwd",
		type: 'post', 
        contentType : 'application/json',
        data: JSON.stringify(
        	{
				"vm_hostname": instanceName,
				"newpassword": $("#pwd").val()
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
}

//弹窗函数
function UIAlert(content){
	$.dialog({
           type: 'alert',
           content: content,
           ok: function () {

           }
    });
}