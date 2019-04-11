var global = {
		domain: {
			existKey: false,
			IMPORT_EXAMPLE: {
				content: "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQChYaZjH0O5O9dYO/uvHqo1zf8v39zYPBWxdNBLKCMWA08lyeVA/ZzYrAOCcQ6DjsReM5R4x7+sRgs8t8PFwbEPhWTKw0JFqpngZU2ipxg65rAc7zqssqysVSrz9ex1Io0pWP6O20k7j4mrsUtpS3UAAqKPt0V6kdpBY0d+0yy4t1vRfswZJc5uoaVmORqczQCriQKoIBIVH1fh1HAzFtsvTttXNAsWUjOW1Ptq9il0nefOFOU95wLbf8tmxhLkdXeyDOe8bmPqzjLlrMKoDcQEy4usqS+FWD8zsO1UAo9ntGGBfQm+iLCx56Z4HEqIwH0tdc2ZF4rUV0uLUplKDs35 imported-openssh-key"
			}
		}
};

$(document).ready(function(){	
	
	//根据单选按钮切换密钥创建类型
	$("input[type='radio']").bind('click',function(){
		var value = $("input[name='pattern']:checked").val();
		var html = '';
		if("import" == value){
			html = template('import_key_content',{});
		}
	    document.getElementById('import_key').innerHTML = html;
	});
	
	$("#newKey").uValidform({
	    btnSubmit:"#sure",
	    datatype:{ 
	    	"keyName": checkKeyName
	    },
	    callback:function(form){
	        $.dialog({
	            type: 'confirm',
	            content: "您确认添加密钥对吗？（注意：自动创建密钥对时会下载私钥，该私钥只能下载一次，请注意保存！）",
	            ok: function () {save();},
	            cancel: function () {}
	        });
	    }
	});	
	
	//点击导入样例
	$(document).on("click","#importExample",function() {
		$("#publicKey").val(global.domain.IMPORT_EXAMPLE.content);
	});

	//取消
	$("#cancel").click(function(){
		var dialog = parent.dialog.get(window);
		dialog.close();
	})
});

//检查密钥对名称是否已存在
function existKey(name){
	$.ajax({
		url: contextPath + "/service/ecs/getKey",
		type: 'post',
		contentType: 'application/json',
		async: false,
		data: JSON.stringify(
			{
				"key_name": name
			}
		),
		dataType: "json",
		success: function (res) {
			var flag = res.result;
			if ("success" == flag) {
				global.domain.existKey = true;
			} else {
				global.domain.existKey = false;
			}
		}
	});
}

//验证密钥对名称
function checkKeyName(gets,obj,curform,datatype){
	//密钥对名称正则表达式 2-128个字符，以大小写字母或中文开头，可包含数字、"."、"_"、":"或"-"
	var regx = /^([a-zA-Z]|[\u4E00-\u9FA5]){1}[a-zA-Z\u4E00-\u9FA5\d\.\:\-\_]{1,127}$/;
	if(regx.test(gets) == false) {
		return false;
	}

	//检查密钥对名称是否已存在
	existKey(gets);
	if(global.domain.existKey){
		html = template('exist_tips_content',{});
		document.getElementById('exist_tips').innerHTML = html;
		$("#keyName").addClass("input_error");
		return false;
	}else{
		html = "";
		document.getElementById('exist_tips').innerHTML = html;
		$("#keyName").removeClass("input_error");
	}
	return true;
}

function save(){
	var key_name = $("#keyName").val();
	var public_key = "";
	if ($("#publicKey").val() != undefined) {
		public_key = $("#publicKey").val();
	}

	$.ajax({
		url: contextPath + "/service/ecs/createKey",
		type: 'post',
		contentType: 'application/json',
		async: false,
		data: JSON.stringify(
			{
				"key_name": key_name,
				"public_key": public_key
			}
		),
		dataType: "json",
		success: function (res) {
			var flag = res.result;
			if ("true" == flag) {
//				UIAlert("保存成功!");
				window.location.href = contextPath + "/service/ecs/key?downloadprivatekey=true&keyname=" + key_name;
			} else {
				UIAlert(L.getLocaleMessage("savefailed", "创建失败！"));
			}
		}
	});
}

// 弹窗函数
function UIAlert(content) {
	$.dialog({
		type: 'alert',
		content: content,
		ok: function () {
		}
	});
}