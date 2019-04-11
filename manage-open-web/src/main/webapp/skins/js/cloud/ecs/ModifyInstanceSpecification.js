$(document).ready(function(){	
	initPageDom();
	
	$("#specificationForm").uValidform({
	    btnSubmit:"#sure",
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

function initPageDom() {
	//初始化下拉框
	initSelect();	
}

//初始化下拉框
function initSelect() {
	//规格联动下拉框
	$.cxSelect.defaults.url = contextPath + "/jsp/cloud/ecs/data/cpu";
	$('#vcpu_memory_val').cxSelect({
		selects: ['vcpu', 'memory'],
		nodata: 'none',
		required: true
	});
}

//确认调整
function save(){
	var vCPU = $("#vcpu").val().split(" ")[0];
	var memory = $("#memory").val().split(" ")[0];
	var applyReason = $("#apply_reason").val();

	$.ajax({
		url: contextPath+ "/service/ecs/applyChangeFormat",
		type: 'post', 
        contentType : 'application/json',
        data: JSON.stringify(
        		{
        			"vm_name": instanceName,
        			"vm_cpu": parseInt(vCPU),
        			"vm_memory": parseInt(memory),
					"apply_reason": applyReason
	        	}
        ),
		dataType: "json",
		success: function(res){
			var flag=res.result;
			if("true" == flag){
				var dialog = parent.dialog.get(window);
				dialog.close("ok");
			} else {
				UIAlert("变更失败!");
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