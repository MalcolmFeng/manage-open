$(document).ready(function() {
	//初始化页面信息
	initPageInfo();
	
	$("#modifyInfoForm").uValidform({
	    btnSubmit:"#sure",
	    datatype:{ 
	          "description": checkDescription	
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

})

function initPageInfo(){
	$.ajax({
	      url: contextPath + "/service/manage/getdetailsbyinstanceid",
	      type: 'post', 
	        contentType : 'application/json',
	        async: false,
	        data: JSON.stringify(
	        		{
	        			"instance_id": instanceid
		        	}
	        ),	   
	      success: function(res) {
	         $("#instance_name").val(res.instance_name);
	         $("#description").text(res.description);	         
	      }
	});
}

//验证实例描述
function checkDescription(gets,obj,curform,datatype){
	//可以不输入，若有输入则字符个数在2~256
    if(gets.length==0||(gets.length>=2&&gets.length<=256)) {
       return true;
    }  
    return false;
}


//保存信息
function save(){
	var formObj = {};
	$("#modifyInfoForm").serializeArray().map(function(x){formObj[x.name] = x.value;});
	$.ajax({
		url: contextPath+ "/service/manage/savebasicinfo",
		type: 'post', 
        contentType : 'application/json',
        data: JSON.stringify(formObj),
		success: function(res){
			var flag=res.result;
			if("success"==flag){
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