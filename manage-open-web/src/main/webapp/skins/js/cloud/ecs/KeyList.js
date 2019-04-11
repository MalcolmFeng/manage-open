$(document).ready(function() {
	
	// 初始化datatable
	var options = {
		"serverSide" : false,
		"orderCellsTop" : true,
		"searching": true
	};
	var url = contextPath+"/service/ecs/getKeys";
	grid = new L.FlexGrid("keyList", url);
	grid.init(options); // 初始化datatable
	$("input[type='search']").parent().hide();//隐藏本地搜索框
	
	//密钥对名称搜索框
	var column1 = $('#keyList').DataTable().column(1);
	$("input#keyName_search").on('keyup change', function() {
		column1.search($(this).val()).draw();
	});

	if (isDownload == "true") {
		window.location.href = contextPath + "/service/ecs/downloadPrivateKey?keyname=" + keyName;
		// var link = contextPath + "/service/ecs/downloadPrivateKey?keyname=" + keyName;
		// //弹出窗口实现密钥对下载
		// var $eleForm = $("<form method='get'></form>");
		// $eleForm.attr("action", link);
		// $(document.body).append($eleForm);
		// //提交表单，实现下载
		// $eleForm.submit();
	}
	
	// 下载
	// $(document).on("click",".download",function() {
    //     var data = grid.oTable.row($(this).parents("tr")).data();
    //     var keyName = data.key_name;
    //     var link = "";
    //     //根据keyName获取下载链接
    //     $.ajax({
    // 		url: contextPath+ "/service/ecs/getprivatekeyurl",
    // 		type: 'post',
    // 		async: false,
    //         contentType : 'application/json',
    //         data: JSON.stringify(
    //         		{
    //                 	"key_name": keyName
    //                 }),
    // 		success: function(res){
    // 			//必须async为false时才能给link赋完值后执行下载，当ajax为异步时必须将下载代码放在回调函数里
    // 			link = res.url;
    // 		}
    // 	});
    //     //弹出窗口实现密钥对下载
    //     var $eleForm = $("<form method='get'></form>");
    // 	$eleForm.attr("action", link);
    // 	$(document.body).append($eleForm);
    // 	//提交表单，实现下载
    // 	$eleForm.submit();
    // });
	
	// 删除
	$(document).on("click",".del",function() {
        var data = grid.oTable.row($(this).parents("tr")).data();
        var keyName = data.key_name;
        $.dialog({
            type: 'confirm',
            content: '您确定要删除这个密钥对吗？',
            ok: function () {
            	$.ajax({
            		url: contextPath+ "/service/ecs/deleteKey",
            		type: 'post', 
                    contentType : 'application/json',
                    data: JSON.stringify({
                    	"key_name": keyName
                    	}),
					dataType: "json",
            		success: function(res){
            			var flag = res.result;
            			if("true"==flag){
//            				UIAlert("删除成功");
            				//重载页面
           		        	var resetPaging = false;
           		        	var url = contextPath + "/service/ecs/getKeys";
           		        	grid.reload(url, null, resetPaging);
            			} else {
            				UIAlert("删除失败");
            			}
            		}
            	});      
        	},
            cancel: function () {}
        });         
    });
	
	//批量删除
	$("#delAll").click(function(){
		//获取选中行的数据
		var str = "";
		var arr = document.querySelectorAll('input[name="checkboxlist"]');
		for(var i=0;i<arr.length;i++){
			if(arr[i].checked){
				var data = grid.oTable.row($(arr[i]).parents("tr")).data();
				var keyName = data.key_name;
				str += keyName + ",";
			}
		}

		$.dialog({
            type: 'confirm',
            content: '您确定要删除选中的密钥对吗？',
            ok: function () {
            	$.ajax({
            		url: contextPath+ "/service/ecs/deleteKeys",
            		type: 'post', 
                    contentType : 'application/json',
                    data: JSON.stringify({
                    	"key_name": str
                    	}),
					dataType: "json",
            		success: function(res){
            			var flag = res.result;
            			if("true"==flag){
//            				UIAlert("批量删除成功");
            				//重载页面
           		        	var resetPaging = false;
           		        	var url = contextPath + "/service/ecs/getKeys";
           		        	grid.reload(url, null, resetPaging);
            			} else {
            				UIAlert("批量删除失败");
            			}
            		}
            	});      
        	},
            cancel: function () {}
        });    
    }); 
	
	//创建密钥对
	$("#addKey").click(function(){
		window.location.href = contextPath + "/jsp/cloud/ecs/NewKey.jsp";
	})

});

function rendercheckbox(data, type, full) {
    return '<input type="checkbox" value="' + data + '" id="checkbox" name="checkboxlist" onclick="forSelectItem()">';
}

//操作
function renderoptions(data, type, full) {
	return '<div class="btn-group pull-center"><li><a class="del">'+"删除"+'</a></li></div>';
}

//全选 反选
function forSelectAll() {
	var chk = document.getElementById("selectAll");
	var arr = document.querySelectorAll('input[name="checkboxlist"]');
	for(var i = 0, len = arr.length; i < len; i++) {
		arr[i].checked = chk.checked;
	}
}

//列表中的checkbox全选 反选
function forSelectItem() {
	var arr = document.querySelectorAll('input[name="checkboxlist"]');
	var chkNum = 0;
	for(var i = 0, len = arr.length; i < len; i++) {
		if(arr[i].checked) {
			chkNum++;
		}
	}
	document.getElementById("selectAll").checked = (arr.length == chkNum);
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