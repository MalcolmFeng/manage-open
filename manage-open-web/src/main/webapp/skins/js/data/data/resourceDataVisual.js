var grid;
var totalCount = 0;
$(document).ready(function () {
	var columns = [];
	options = {};


	var columnRecords;
	//查询该资源数据项，根据数据项列名拼装columns
	var cmd = new G3.Command("com.inspur.od.dataResource.cmd.DataResourceQueryCmd");
	cmd.setParameter("DATA_RESOURCE_ID", resourceId);
	cmd.execute("queryResourceItems");
	if (!cmd.error) {
		columnRecords = cmd.getData();
		//columns.push({data:"ROWNUM",title:0==columnRecords.length?"&nbsp":"序号","sClass":"center",'sWidth':'50px'});
		columns.push({
			data: "ROWNUM", title: 0 == columnRecords.length ? "&nbsp" : "序号", "sClass": "nowrap", 'sWidth': '60px', "mRender": function (data, type, row) {
				if (data == null || data == "null" || data == "undefined") {
					data = "";
				}
				if (data.substr(data.length - 2) == ".0") {
					var length = data.length - 2;
					data = data.substr(0, length);
				}
				return '<span data-toggle="tooltip" title="' + data + '">' + data + '</span>';
			}
		});
		for (var i = 0; i < columnRecords.length; i++) {
			//拼装columns 若有中文描述则使用中文描述作为列名
			columns.push({
				data: columnRecords[i].columnName.toUpperCase(),
				title: "" == myTrim(columnRecords[i].columnDescription) ? columnRecords[i].columnName : columnRecords[i].columnDescription,
				"sClass": "nowrap", 'sWidth': '200px', "mRender": function (data, type, row) {
					if (data == null || data == "null" || data == "undefined") {
						return "";
					}
					data = htmlToString(data);
					return '<span data-toggle="tooltip" title="' + data + '">' + data + '</span>';
				}
			});
		}
		options = {
			scrollX: true, // 水平滚动
			info: false //去掉页码左侧记录数显示
		};

	} else {
		G3.alert("提示", "查询出错");
		return false;
	}


	//数据可视化列表初始化
	grid = new G3.Grid("grid", columns);
	grid.setCmd("com.inspur.od.dataResource.cmd.DataResourceQueryCmd");
	grid.setParameter("DATA_RESOURCE_ID", resourceId);
	grid.setMethod("queryDataByResourceId");
//	options.info = false;//去掉页码左侧记录数显示
	grid.init(options);
	var cmd = grid.getCmd();
	totalCount = cmd.getTotalCount();



	$("#grid_paginate").hide();
	$("#grid_length").hide();
	$("#totalCount").text(totalCount);
});


function exportExcel() {
	var url = webPath + "/jsp/od/dataResource/browse/detail/resourceDataExport.jsp?totalCount=" + totalCount;
	G3.showModalDialog("导出Excel", url, {
		width: 500,
		height: 300
	}, function (e, ret) {
		if (ret && ret != "0") {
			var result = G3.JSON.decode('(' + ret + ')');
			var start = result.start;
			var end = result.end;
			var url = webPath + "jsp/od/dataResource/browse/detail/exportExcelModel.jsp?resourceId=" + resourceId + "&start=" + start + "&end=" + end;
			window.location.href = url;
		}
	});
}

function myTrim(str) { //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 将返回数据中的html代码符号转义，避免页面渲染出错
 * @param htmlCode
 * @returns
 */
function htmlToString(htmlCode) {
	htmlCode = htmlCode.replace(new RegExp("&", 'g'), '&#38;');
	htmlCode = htmlCode.replace(new RegExp("<", 'g'), '&#60;');
	htmlCode = htmlCode.replace(new RegExp(">", 'g'), '&#62;');
	htmlCode = htmlCode.replace(new RegExp("\"", 'g'), '&#34;');
	htmlCode = htmlCode.replace(new RegExp("'", 'g'), '&#39;');
	return htmlCode;
}