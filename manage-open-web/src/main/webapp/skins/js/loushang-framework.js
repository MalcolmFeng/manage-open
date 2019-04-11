L= {
		version : '1.0'
	};

/*******************************************************************************
 * L 表格 第二种封装
 * @param 表格id
 * @param 表格数据请求的路径
 ******************************************************************************/
L.FlexGrid = function(listId, appUrl) {
	this.listId = listId;  //表格的ID
	this.appUrl = appUrl;
	this.columnList = [];  //列属性列表
	this.columnDefList = [];  //列定义列表
	this.params = new Object();
	this.returns = new Object();
	this.initData = [];
	this.total;
	this.allData = [];
	// 默认参数列表
	this.defaults = {
		bProcessing : true,// DataTables载入数据时，是否显示‘进度’提示
		autoWidth : false,// 展开、合起菜单栏时，表格自适应宽度
		lengthMenu : [ 10, 25, 50, 100 ],// 自定义长度菜单的选项
		iDisplayLength : 10,// 分页，每页的显示数量
		scrollX : "",// 水平滚动
		scrollY : "",
		ordering : true,// 是否启动各个字段的排序功能 ,默认不排序
		order : [],// 默认不设置任何列初始化排序
		info : true,// 是否显示页脚信息，DataTables插件左下角显示记录数
		paging : true,// 是否分页
		bPaginate : true,// 是否显示（应用）分页器
		serverSide: true,
		searching: false,
		oScroll :{
			bCollapse: null,
			sX: null,
			sY: null
		},
		callBack : function() {
		},// 表格加载完成回调函数
		stateSave : false,// Datatables设置
							// stateSave选项后，可以保存最后一次分页信息、排序信息，当页面刷新，或者重新进入这个页面，恢复上次的状态。
		stateDuration : 0,// -1 保存到session中， 0~更大保存到localstorage中， 以秒计算
							// 0表示没有时间限制
		trCheckedCallBack : trCheckedCallBack,// 行选中回调
		trUnCheckedCallBack : trUnCheckedCallBack,// 行取消选中回调
		drawCallback : tableDrawCallback,
		format: tableRowFormat
	// 表单加载完成回调
	};
	this.oTable = null;
};

//扩展表格对象的实例方法属性
L.FlexGrid.prototype = {
	/***************************************************************************
	 * 表格加载初始化方法
	 **************************************************************************/
	init : function(options) {// 列信息数组
		var _that = this;
		// 将用户自定义的信息赋值给default
		if (options) {
			$.each(options, function(i, val) {
				if (typeof val == 'object' && _that.defaults[i]) {
					$.extend(_that.defaults[i], val);
				} else {
					_that.defaults[i] = val;
				}
			});
		}
		//判断是前台分页还后端分页
		var fnserver = this.retrieveData;
		var url="";
		if(this.defaults.serverSide){
			 fnserver = this.retrieveData
			 url = "" ;
		}else{
			fnserver = "";
			url = this.appUrl;
		}
		this.columnsData(); //初始化列数据
		_that.oTable = $('#' + this.listId).DataTable({
			"grid": _that,  //传L.FlexGrid域
			"aLengthMenu" : this.defaults.lengthMenu,// 自定义长度菜单的选项
			"iDisplayLength" : this.defaults.iDisplayLength,// 分页，每页显示的数量
			"bProcessing" : this.defaults.bProcessing,// DataTables载入数据时，是否显示‘进度’提示
			"order" : this.defaults.order,// 默认无初始列的排序
			"ordering" : this.defaults.ordering,// 是否启动各个字段的排序功能 ,默认不排序
			"bAutoWidth" : this.defaults.autoWidth,// 展开、合起菜单栏时，表格自适应宽度
			"bServerSide" : this.defaults.serverSide,
			"fnServerData" : fnserver,
			"sAjaxSource" : url,
			"bRetrieve" : true,
			"paging": this.defaults.paging,
			"scrollX" : this.defaults.scrollX,// 水平滚动
			"scrollY": this.defaults.scrollY,//垂直滚动
	        "scrollCollapse": this.defaults.scrollCollapse,//
			"bPaginate" : this.defaults.bPaginate, // 是否显示（应用）分页器
			"bInfo" : this.defaults.info, // 是否显示页脚信息，DataTables插件左下角显示记录数
			"columns" : _that.columnList,
			"columnDefs": _that.columnDefList,
			"stateSave" : this.defaults.stateSave,// 保存状态
			"stateDuration" : this.defaults.stateDuration,// 保存状态的时间
			"oScroll" :{
				"bCollapse": this.defaults.oScroll.bCollapse,
				"sX": this.defaults.oScroll.sX,
				"sY": this.defaults.oScroll.sY
			},
			"searching": this.defaults.searching,
			"pagingType": "full_numbers",
			"dom": 'f<"H"r>t<"F"ipl><"clearfix">',   //支持搜索框的出现
			"language": {
				"processing": "",
			    "lengthMenu": " _MENU_ ",
			    "loadingRecords": "加载中...",            
			    "zeroRecords": "未查询到记录",
			    "info": "显示 _START_ 到 _END_ 条 共 _TOTAL_ 条记录",
			    "infoEmpty": "",
			    "sSearch": "搜索:",
			    "emptyTable":"<span class='norecord'></span>",
			    "paginate": {//分页的样式文本内容。
			        "previous": "《",
			        "next": "》",
			        "first": "",
			        "last": ""
			    }
			},
			"initComplete" : function() {// 初始化加载回调函数
				_that.defaults.callBack();
			}, // 结束
			"rowCallback": function(row,data,index){ //为每行的最后一个单元格绘制操作按钮
				var $lasttd = $('td:last-child',row);
				if (options.btnDefs && options.btnDefs.length) {
				   var operationBody = _that.defaults.format(data,options).html();
				   var operationBtn = "<div class='operation-btn'><span class='operation-btn-text'>操作</span><span class='fa fa-angle-double-left'></span></div>";
				   if (!$lasttd.find(".operation").length) {
					   var opr_height = (index + 1) * 40;
					   //防止前端分页时的重绘问题
					   $lasttd.append("<div class='operation' style='top:" + opr_height + "px;'>" + operationBtn + operationBody + "</div>");
				   }
				}
		   },
			"drawCallback" : function(settings) {
				var api = this.api();
				_that.defaults.drawCallback(settings, api);
				if(_that.defaults.sScrollY != ""){
					 $(window).bind('resize', function () {
						 _that.oTable.draw( true );
					 }); 
				}
			}
		});
		return _that.oTable;
	},
	columnsData: function(){
		if(columnList){
			return columnList;
		}
		var _that = this;
		//根据表头信息获取列的定义及列的个性化定义
		var columnList = [];
		var columnDefList = [];
		$('#'+_that.listId+' thead tr').each(function(trindex,trelement){ //复杂表头时产生多行
			$(this).find("th").each(function(thindex,thelement){
				var columnMap = new Object();
				var column = false;  //避免加入空对象        
				var columnDefMap = new Object();
				if($(this).attr("data-field")){
					column = true;
					columnMap["data"]=$(this).attr("data-field");
				}
				if($(this).attr("data-sortable")){
					column = true;
					if($(this).attr("data-sortable")=="false"){
						columnMap["bSortable"]=false;
					}else{
						columnMap["bSortable"]=true;
					}
				}
				if($(this).attr("data-render")){
					var render = $(this).attr("data-render");//根据方法名生成对应的方法
					columnDefMap["targets"] = thindex;
					columnDefMap["data"] = $(this).attr("data-field");
					if(render == "checkbox" && typeof render == "string"){
						columnDefMap["render"] = function(data,type,full){
						 return '<input type="checkbox" value="' + data + '" title="' + data + '" id="checkbox" name="checkboxlist"/>';
					    }
					}else if(render == "radio" && typeof render == "string"){
						columnDefMap["render"] = function(data,type,full){
							return '<input type="radio" value="' + data + '" title="' + data + '" id="radio" name="radiolist"/>';
						}
					}else{
					    columnDefMap["render"] = eval(render);
					}

					columnDefList.push(columnDefMap);
				}
				if(column){  //避免加入空对象
					columnList.push(columnMap);
					column = false;
				}
			});
		});
		
		    _that.columnList = columnList;
		    _that.columnDefList = columnDefList;
	},
	/***************************************************************************
	 * 表格加载 数据请求
	 **************************************************************************/
	retrieveData: function(sSource, aoData, fnCallback, oSettings){
		//分页信息
		var start = aoData[3].value;
		var limit = aoData[4].value;
		var _that = oSettings.oInit.grid;
		
		// 判断是否分页，如果不分页，则传递limit为-1
		var paging = _that.defaults.paging;
		if (paging) {
			// 分页信息
			_that.setParameter("start", start);
			_that.setParameter("limit", limit);
		} else {// 不分页
			_that.setParameter("limit", -1);
		}
		
		// 启用排序功能
		if (_that.defaults.ordering) {			
			//排序信息
			//如果有排序信息
			if((aoData[2].value).length > 0){
				//单列排序
				if((aoData[2].value).length == 1){
					var columnIndex = (aoData[2].value)[0].column;
					var order = (aoData[2].value)[0].dir;
					var columnName = (aoData[1].value)[columnIndex].data;
					
					_that.setParameter("orderfield", columnName);
					_that.setParameter("orderdir", order);
				} else {
					//排序信息集合
					var sortList = [];
					//多列联合排序
					for(var i = ((aoData[2].value).length - 1); i >= 0; i--){
						//排序信息map对象 包含两个key field 列信息  dir   排序方向
						var sortMap = new Object();
						var columnIndex = (aoData[2].value)[i].column;
						var order = (aoData[2].value)[i].dir;
						var columnName = (aoData[1].value)[columnIndex].data;
						if(i > 0){
							sortMap["orderfield"] = columnName;
							sortMap["orderdir"] = order;
							sortList.push(sortMap);
						} else if(i == 0){
							_that.setParameter("orderfield", columnName);
							_that.setParameter("orderdir", order);
						}
					}
					_that.setParameter("multisort", sortList);
				}
			}
		}
		_that.execute(_that);
		var initData = _that.initData;
		var draw = aoData[0].value;
		var length = initData.length;
		var total = _that.total;
		var json = {
			"draw": draw,
			"iTotalRecords": total,
			"iTotalDisplayRecords": total,
			"aaData": initData
		};
		//渲染数据
		fnCallback(json);
		// checkbox绑定全（不）选定事件
		$(".table>thead>tr>th input[type='checkbox']").on("click",function(e){
			//选中
			if(e.target.checked){
				var selector = $(this).parentsUntil("table").parent("table#" + _that.listId).find("tbody>tr>td input[type='checkbox']");
				$(selector).each(function() {
					var inputCtrl = $(this);
					inputCtrl.prop("checked", true);
					inputCtrl.parents("tr").addClass("selected");
				});
				// 解决个别表格不能全选的问题
				var selectorTemp = $(this).parentsUntil("table").parent("table").parent().parent(".dataTables_scrollHead").next().find("tbody>tr>td input[type='checkbox']");
				$(selectorTemp).each(function() {
					var inputCtrl = $(this);
					inputCtrl.prop("checked", true);
					inputCtrl.parents("tr").addClass("selected");
				});
			}else{
				var selector = $(this).parentsUntil("table").parent("table#" + _that.listId).find("tbody>tr>td input[type='checkbox']");
				$(selector).each(function() {
					var inputCtrl = $(this);
					inputCtrl.prop("checked", false);
					inputCtrl.parents("tr").removeClass("selected");
				});
				// 解决个别表格不能全取消的问题
				var selectorTemp = $(this).parentsUntil("table").parent("table").parent().parent(".dataTables_scrollHead").next().find("tbody>tr>td input[type='checkbox']");
				$(selectorTemp).each(function() {
					var inputCtrl = $(this);
					inputCtrl.prop("checked", false);
					inputCtrl.parents("tr").removeClass("selected");
				});
			}
		});
		// 表格行内，单选多选点击时，触发给当前行增加\删除类
		$(".table>tbody>tr>td input[type='checkbox']").on("click", function(e) {
			if(e.target.checked){
				$(this).parents("tr").addClass("selected");
				_that.defaults.trCheckedCallBack(this);
			}else{
				$(this).parents("tr").removeClass("selected");
				_that.defaults.trUnCheckedCallBack(this);
			}
		});
		$(".table>tbody>tr>td input[type='radio']").on("click", function(e) {
			if(e.target.checked){
				$(this).parents("tr").siblings().removeClass("selected");
				$(this).parents("tr").addClass("selected");
				_that.defaults.trCheckedCallBack(this);
			}else{
				$(this).parents("tr").removeClass("selected");
				_that.defaults.trUnCheckedCallBack(this);
			}
		});
		
		//为每行操作按钮添加折叠事件
		$(".table>tbody>tr>td").on("click", ".operation-btn", function() {
			var $disCell = $(this).next(".operation-body");
			var $txt = $(this).find('.operation-btn-text');
			if ($disCell.css("display") == "none") {
				$txt.hide();
				$disCell.show(100).css('display', 'table-cell');
				$(this).find('.fa-angle-double-left').removeClass('fa-angle-double-left').addClass('btn ue-btn fa-angle-double-right');
			} else {
				$(this).find('.fa-angle-double-right').removeClass('btn ue-btn fa-angle-double-right').addClass('fa-angle-double-left');
				$disCell.hide(100, function() {
					$txt.show();
				});
			}
		});
	},
	
	/**
	 * 设置初始化表格的数据
	 * @method setInitData
	 * @param {Object} data 参数名称
	 */
	setInitData: function(data){
		this.initData = data;
	},
	
	/**
	 * 获得初始化表格的数据
	 * @method getReturn
	 * @return {Oject} 值
	 */
	getInitData: function(){
		return this.initData;
	},
	
	/***************************************************************************
	 * 设置grid的列
	 * 
	 * @param columnData
	 *            列信息数组
	 **************************************************************************/
	setColumns : function(columnData) {
		this.columns = columnData;
	},
	
	/**
	 * 设置提交到服务器的参数
	 * @method setParameter
	 * @param {String} key 参数名称
	 * @param {String} value 参数值
	 */
	setParameter: function(key, value){
	   this.params[key]=value;
	},
	
	/**
	 * 根据键值从map对象中取值
	 * 
	 * @method get
	 * @param {String}key键值
	 * @return {Oject} 值
	 */
	getParameter: function(key) {
		var val = this.params[key];
		return val;
	},
	
	/**
	 * 获取初始化的所有值
	 * 
	 * @method get
	 * @return {Oject} 值
	 */
	getAllData: function(){
		return this.allData;
	},
	/**
	 * 获得服务器返回的参数
	 * @method getReturn
	 * @param {String} key 参数名称
	 * @return {String} 参数对应的值
	 */
	getReturns: function(key){
		var val = this.returns[key];
		return val;
	},
	
	/**
	 * 执行ajax
	 * @method execute
	 * @param {Boolean} sync 是否同步执行，默认为true
	 */
	execute: function(_that,sync){
		if(sync == false || sync == "false"){
			sync = false;
		}else{
			sync = true;
		}
		var url = this.appUrl;	
    	var json = JSON.stringify(this.params);
		try{
			$.ajax({
				url : url,
				type : "POST",
            	async : !sync,
            	contentType: "application/json",
            	
            	dataType: "json",
				data: json,
				success : function(data){
					_that.initData = data.data;
					_that.total = data.total;
					_that.allData = data;
				},
				error : function(data, textstatus){
					$.dialog({
						 type:"alert",
						 content:data.responseText,
						 autofocus: true,
					 });
				}
			});
		}catch(e){
			throw "请求数据出错";
		}
	},
	
	/**
	 * 重新加载数据 可使用新的数据源
	 * ***/
	reload : function(url, param){//重新加载数据
		$('#' + this.listId).dataTable().fnDestroy();
		if(url){
			this.appUrl = url;
		}
		if(param){
			this.params = param;
		}
		this.init(this.defaults);
	}
};

//空方法
//行选中回调
function trCheckedCallBack(obj) {

}
//行取消回调
function trUnCheckedCallBack(obj) {

}
//表格加载回调
function tableDrawCallback(settings, api) {

}

//表格行操作按钮
function tableRowFormat(data,options){
	var containerBtn = $('<div class="btn-group operation-body"></div>');
	//btnDefs可传两种类型的值：function和object
	if(typeof options.btnDefs == "function"){
		item = options.btnDefs(data);
		containerBtn.append(item);
	}else if(typeof options.btnDefs == "object"){
		for (var i = 0; i < options.btnDefs.length; i++) {
			var item = $('<button class="btn ue-btn" type="button">' + 
							'<span class="' + options.btnDefs[i].icon + '"></span>'+ options.btnDefs[i].text + 
						'</button>');
			item[0].setAttribute("onclick", options.btnDefs[i].handler + "("+ JSON.stringify(data) +")");
			containerBtn.append(item);
		}
	}
	return $('<div></div>').append(containerBtn);
}


//slickGrid
L.EditGrid = function(gridId, url) {
	this.gridId = gridId;  //表格的ID
	this.url = url;	//请求数据的地址
	this.widthType = "%";
	this.params = {
		start: 0,
		limit: 10
	};
	this.changedRows = [];
	// 默认参数列表
	this.defaults = {
		paging: true,
		lengthMenu: [ 10, 25, 50, 100 ],
		info: true,
		editable:true,
		autoEdit: true,
		selectActiveRow: false,	// 点击行时将其选中，并将其余行取消选中
		multiSelect: true, // true：多选(Ctrl+左键单击行)；false：单选；默认：true。
		defaultColumnWidth: 200, // 默认列宽
		enableTextSelectionOnCells: true,
		forceFitColumns:true
	};
	this.oTable = null;
	this.pageBar = null;
	this.dataView = null;
}

// 对象原型方法
L.EditGrid.prototype = {
		init: function(options) {
			var _that = this;
			options = $.extend(_that.defaults, options);
			
			var gridId = _that.gridId + "_ls";
			var columns = _that.getColumns();
			
			// 创建表格容器元素
			$("#" + _that.gridId).after("<div id="+gridId+" class='grid'></div>");
			$("#" + _that.gridId).hide();
			_that.dataView = new Slick.Data.DataView({inlineFilters: true});
			_that.oTable = new Slick.Grid("#" + gridId, _that.dataView, columns, options);
			_that.dataView.onRowCountChanged.subscribe(function (e, args) {
				_that.oTable.updateRowCount();
				_that.oTable.render();
			    });
			_that.dataView.onRowsChanged.subscribe(function (e, args) {
				_that.oTable.invalidateRows(args.rows);
				_that.oTable.render();
			    });
			
			_that.dataView.setFilter(filter); //过滤删除的数据state="2"
			var url = _that.url;
			// 是否分页
			if(_that.defaults.paging) {
				var pageId = _that.gridId + "_ls_page";
				$("#" + gridId).after("<div id="+pageId+" class='page'></div>");
				_that.params["limit"] = options.lengthMenu[0];
				this.pageBar = new Slick.Controls.Pager({
					url: _that.url,
					params: _that.params,
					options: options,
					container: $("#" + pageId), 
					datagrid: _that.oTable,
					dataView: _that.dataView
				});
			}else{
				_that.params["limit"] = -1;
				$.ajax({
					url : url,
					type : "POST",
					async : false,
					contentType: "application/json",
					dataType: "json",
					data: JSON.stringify(_that.params),
					success : function(data){
						_that.dataView.setItems(data.data);
						_that.oTable.render();
					},
					error : function(data, textstatus){
						$.dialog({
							 type:"alert",
							 content:data.responseText,
							 autofocus: true,
						 });
					}
				});
			}
			_that.oTable.setSelectionModel(new Slick.RowSelectionModel({
				selectActiveRow:options.selectActiveRow
			}));
			
			//AutoTooltips
			_that.oTable.registerPlugin( new Slick.AutoTooltips({ enableForHeaderCells: true }) );
			
			if(_that.checkboxSelectColumn) {
				_that.oTable.registerPlugin(_that.checkboxSelectColumn);
			}
			if(_that.widthType == "%") {
				_that.oTable.autosizeColumns();	//自动调整列宽
			}
			
			// 初始化事件
			this.initEvent(_that);
			
			return _that.oTable;
		},
		// 绑定事件
		initEvent: function(grid) {
			// 绑定单元格值的改变事件，用于获取变动的数据
			grid.oTable.onCellChange.subscribe(function(e,args){
				args.item["state"] = 3;
			});
			grid.oTable.onSort.subscribe(function(e,args){
				var comparer = function(a,b){
					return (a[args.sortCol.field]>b[args.sortCol.field])?1:-1;
				}
				grid.dataView.sort(comparer,args.sortAsc);
			})
			
		},
		// 获取变动的数据，即value_state的值为3的行
		getChangedData: function() {
			var changedData = [];
			var data = this.dataView.getItems();
			for(var i = 0,datalen=data.length ; i < datalen; i++) {
				if(data[i]["state"] == 3 || data[i]["state"]==2 || data[i]["state"]==1) {
/*					delete data[i]["value_state"];
*/					changedData.push(data[i]);
				}
			}			
			return changedData;
		},
		
		//获取被选中的所有行的数据
		getSelectedDataItems: function(){
			var grid = this.oTable;
			 var items = grid.getSelectedRows().map(function (x) {
			     return grid.getDataItem(x);
			 });
			 return items;
		},
		
		// 获取列定义
		getColumns: function() {
			var instance = this;
			var columns = [];
			var $ths = $("#" + this.gridId + " th");
			$ths.each(function(i, th){
				var field = th.getAttribute("data-field");
				var name = $(th).text();
				var render = th.getAttribute("data-render");
				var editor = th.getAttribute("data-editor");
				var formatter = th.getAttribute("data-render");
				var sort = th.getAttribute("data-sortable");
				if(render == "checkbox" && typeof render == "string") {
					instance.checkboxSelectColumn = new Slick.CheckboxSelectColumn({multiSelect:true});
					columns.push(instance.checkboxSelectColumn.getColumnDefinition());
					return true;
				}
				if(render == "radio" && typeof render == "string"){
					instance.checkboxSelectColumn = new Slick.CheckboxSelectColumn({multiSelect:false});
					columns.push(instance.checkboxSelectColumn.getColumnDefinition());
					return true;
				}
				
				var col = {};
				col["id"] = col["field"] = field;
				col["name"] = name;
				switch(editor) {
					case "text":
						col["editor"] = Slick.Editors.Text;
						break;
					case "select":
						col["editor"] = Slick.Editors.SelectEditor;
						var source = th.getAttribute("data-source");
						if(void 0 == source) {
							alert("下拉框需要设置data-source属性");
							columns = false;
							return false;
						}
						if (typeof(eval(source)) == "function") {
							source = window[source]();
							if(typeof(source) == "string") {
								source = eval("("+source+")");
							}
						}else if(typeof(source) == "string"){
							source = eval("("+source+")");
						}
						
						col["source"] = source;
						break;
					case "date":
						col["editor"] = Slick.Editors.MonthDateEditor;
						break;
						
					default:
						break;
				}
				
				if(formatter) {
					col["formatter"] = eval(formatter);
				}
				if(sort) {
					col["sortable"] = sort;
				}
				
				columns.push(col);
			});
			
			return columns;
		},
		/**
		 * 增加一行
		 **/
		 addRow: function(rowData) {
			 if(!rowData["id"]){
				 rowData["id"] = this.dataView.getLength();
			 }
			 rowData["state"] = 1;
             this.dataView.addItem(rowData);
		 },
		 
		 /**
		 * 删除一行
		 **/
		 deleteRow: function(){
			 var grid = this.oTable;
			 var dview = this.dataView;
			 var row = grid.getSelectedRows();//获取被选择所有行的行号
			 if(row==undefined || row==null || row==""){
				return;
			 }
			 var current_rows;
			 if(!isNaN(row)){
				 current_rows=[row];
			 }else{
				 current_rows=row;
			 }

			 var data=dview.getItems();
			 var k=0;
			 for(var i=0;i<current_rows.length;i++){
				 data[current_rows[i]-k]["state"] = "2";
				 k++;
			 }
			 
			this.dataView.refresh();//过滤刷新
		 },
		 save: function(){
			var changedRows = this.getChangedData();
			var url = context + "/service/framework/demo/product/save";
			var json = JSON.stringify(changedRows);
			$.ajax({
				url : url,
				type : "POST",
				async : false,
				contentType: "application/json",
				dataType: "json",
				data: json,
				success : function(data){
					if(data.success == true)
					  $.dialog({
						 type:"alert",
						 content:"保存成功",
						 autofocus: true
					});
				},
				error : function(data, textstatus){
					$.dialog({
						 type:"alert",
						 content:"错误",
						 autofocus: true
					 });
				}
			}); 
		 },
		/**
		 * 设置提交到服务器的参数
		 * @method setParameter
		 * @param {String} key 参数名称
		 * @param {String} value 参数值
		 */
		setParameter: function(key, value){
		   this.params[key]=value;
		},
		/**
		 * 重新加载数据 可使用新的数据源
		 * 
		 **/
		reload : function(url, param){
			var _that = this;
			
			$.ajax({
				url : _that.url,
				type : "POST",
				async : false,
				contentType: "application/json",
				dataType: "json",
				data: JSON.stringify(_that.params),
				success : function(data){
					_that.oTable.setData(data.data);
					_that.oTable.render();
					if(_that.defaults.paging) {
						//_that.pageBar.param = _that.params;
						_that.pageBar.loadPageInfo(1, data,_that.params,_that.url);
					}
				},
				error : function(data, textstatus){
					$.dialog({
						 type:"alert",
						 content:data.responseText,
						 autofocus: true,
					 });
				}
			});
		}
};
function filter(items){
	 if(items["state"]=="2"){
		return false; 
	 }
	 return true;
}
