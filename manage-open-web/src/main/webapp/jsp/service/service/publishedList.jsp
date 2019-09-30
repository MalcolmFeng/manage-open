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
    <title>选择申请的API</title>
    
    <!-- 需要引用的CSS -->
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
       <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
       <link rel="stylesheet" type="text/css" href="<l:asset path='css/ztree.css'/>" />
       <link rel="stylesheet" type="text/css" href="<l:asset path='data/datadev.css'/>" />
       <link rel="stylesheet" type="text/css" href="<l:asset path='data/datalist/css/datalist.css'/>" />
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="<l:asset path='html5shiv.js'/>"></script>
      <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
    <script type="text/javascript" src="<l:asset path='jquery.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>" ></script>
	<script type="text/javascript" src="<l:asset path='form.js'/>" ></script>
	<script type="text/javascript" src="<l:asset path='arttemplate.js'/>" ></script>	
	<script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ztree.js'/>"></script>
	    <title>API市场</title>
	</head>
	<body>
		<div class="container" style="width: 98%; padding-top:10px;">
		    <div class="row" style="display: flex">
				<form class="form-inline" onsubmit="return false;">
					<div class="input-group">
						<input type="hidden" value="${groupId }" id="groupId"/>
				        <input class="form-control ue-form" type="text" id="serviceName" placeholder="请输入关键词"/>
				        <div class="input-group-addon ue-form-btn" onclick="forQuery()">
				        	<span class="fa fa-search"></span>
				        </div>
			        </div>
				</form>
				<div style="height: 53px;line-height: 62px;text-align: center">
					<button class="btn btn-primary" onclick="doApply()">申请</button>

					<button style="margin-left: 50px;" class="btn btn-default">返回</button>
				</div>
			</div>
			<div class="row" id="subGroupList" style="margin-left:10px;"></div>
		    <div class="row" id="openServiceList" style="margin-top:10px"></div>
		    <div class="row">
		    	<div class="pagination_info">
		    	</div>
		    	<div class="pagination_number">
		    		<div class="pagination"></div>
		    	</div>
		    	<div class="clearfix"></div>
		    </div>
		</div>
		<script type="text/javascript">
		var context = "<l:assetcontext/>";
		var pageSize=10;
			$(function() {
				initServiceList(0);
			});

			function pageSelectCallback(curPage, pagination) {
				initServiceList(curPage,"page");
			}
			
			//查询框回车键
		    $("#serviceName").bind('keypress',function(event) {
		  		if (event.keyCode == "13") {
		  			forQuery();
		  		 }
		  	});

			function forQuery() {
			  initServiceList(0);
			}

			function setPaginationInfo(curPage,pageCount) {
			  var html = '<span>共&nbsp;'+pageCount+'&nbsp;页,当前第&nbsp;' + curPage + '&nbsp;页</span>';
			  $(".pagination_info").empty().append(html);
			}
			
			//初始化服务列表
			function initServiceList(curPage,isPage) {
                var start=pageSize*curPage;
				   var param = {
				      start: start,
					  limit:pageSize,
                      dataTotal:true,
                      groupId: $("#groupId").val(),
				      name: $("#serviceName").val()
				  };
				  $.ajax({
					  type: "post",
					  url: context + "/service/open/api/getApplyList",
					  data: param,
                      async:false,
                      dataType:"json",
				    success: function(result) {
				      var html = '';
				      if(result['data'] != null && result['data'] != '') {
				        html = template('servicelist', result);
				      } else {
				        html = template('emptylist');
				      }
				      $("#openServiceList").empty().append(html);
				      // 设置页面高度
				      setPageHeight();
				      //重新显示分页
						var totalPage=0;
						if(result.total>0){
                            totalPage=  result.total%pageSize==0?result.total/pageSize:parseInt(result.total/pageSize)+1
                            setPaginationInfo(curPage+1,totalPage);
						}else{
                            setPaginationInfo(0,totalPage);
						}
                        if(!isPage){
                            initPagination = function() {
                                $(".pagination").pagination(totalPage, {
                                    num_edge_entries: 1,
                                    num_display_entries: 4,
                                    items_per_page:1,
                                    callback: pageSelectCallback
                                });
                            }();
						}
				    }
				  });
				}
			function setPageHeight() {
			  var height = $(".ue-menu-right .container").innerHeight();
			  $(".ue-menu-right").height(height + 50);
			}
			
			function loadAppList(openServiceId) {
                var apply_Flag="0";
                $.dialog({
                    type: "iframe",
                    title: "应用列表",
                    url: context + '/jsp/service/applist/apply_list.jsp?openServiceId=' + openServiceId+"&applyFlag="+apply_Flag,
                    width: 700,
                    height: 400
                });
			}
			function loadServiceInfo(openServiceId) {
			  window.location.href = context + "/service/open/api/get/" + openServiceId+"/apply";
			}

			// 遍历所有选中的进行申请
			function doApply(){
				var ids = new Array();
				$(".iteratorDiv").each(function(index,object){
					var div = $(object).children()[0];
					var input = $(div).children()[0];
					if ( $(input).is(':checked')){
						ids.push( $(this).attr("value") );
					}
				});
				$.ajax({
					type: "post",
					url: context + "/service/open/api/doApply",
					data: {
						"ids":ids
					},
					async:false,
					traditional: true,//这里设置为true
					dataType:"json",
					success: function(result) {
						console.log(JSON.stringify(result));
					},
					error : function (result) {
						console.log(JSON.stringify(result));
					}
				});
			}
		</script>
		<script id="servicelist" type="text/html">
            {{each data as item i}}
               <ul class="ue-list-n">
			     <li>
					 <div class="iteratorDiv" style="height: 20px;width: 100%;display: flex;align-items: center;" value="{{item.id}}">
						 <div style="height: 20px;line-height: 20px;margin-right: 30px;"><input type="checkbox"/></div>
						 <div style="height: 20px;line-height: 20px;"><a onclick="loadServiceInfo('{{item.id}}')">{{item.name}}</a></div>
					 </div>
			    </li>
			  </ul>
            {{/each}}
		</script>

 	</body>
</html>