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
    <title>数据申请</title>
    
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
	    <title>数据市场</title>
	</head>
	<body>
		<div class="container" style="width: 98%; padding-top:10px;">
		    <div class="row">
				<form class="form-inline" onsubmit="return false;">										
					<div class="input-group">
						<input type="hidden" value="${groupId }" id="groupId"/>
				        <input class="form-control ue-form" type="text" id="serviceName" placeholder="请输入关键词"/>
				        <div class="input-group-addon ue-form-btn" onclick="forQuery()">
				        	<span class="fa fa-search"></span>
				        </div>
			        </div>
				</form>
			</div>
			<div class="row" id="groupList" style="margin-top:20px;margin-left:10px;">
				<span style="margin-right: 10px;">数据分组:</span>
				<ul class="list-inline" style="display:inline-block">
	                <li class="activedlink"><a onclick="queryAll();" >全部</a></li>
	                <c:forEach items="${groupList }" var="item">
	                	<li><a onclick="reloadServiceListByGroupId('${item.id}')">${item.name}</a></li>
	                </c:forEach>
	            </ul>
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
		      $("#groupList .list-inline").on("click","li",function(){
		    	  $(this).addClass("activedlink").siblings().removeClass("activedlink");
		      });
		      //清空子分组
			  $("#subGroupList").empty();
			});

			function pageSelectCallback(curPage, pagination) {
				initServiceList(curPage,"page");
			}
			
			function renderGroup(groupId) {
				$("#" + groupId).addClass("activedlink").siblings().removeClass("activedlink");
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
			
			function queryAll() {
                $("#groupId").val("");
				initServiceList(0);
				//清空子分组
				$("#subGroupList").empty();
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
					  url: context + "/service/open/data/getApplyList",
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
			
			function reloadServiceListByGroupId(groupId) {
				  $("#groupId").val(groupId);
                  initServiceList(0);
				  //清空子分组
				  $("#subGroupList").empty();
				  //加载子分组
				  loadSubGroup(groupId);
				}
			
			function loadSubGroup(groupId) {
				  $.ajax({
				    type: "post",
				    url: context + "/service/data/group/list/"+groupId,
				    data: JSON.stringify({parentId:groupId}),
					contentType:"application/json;charset=utf-8",
				    success: function(result) {
				      var html = '';
				      if(result['data'] != null && result['data'] != '') {
				        html = template('subGroupListTemp', result);
				        $("#subGroupList").empty().append(html);
				      } 
				    }
				  });
				}
			
			function reloadServiceListBySubGroupId(groupId) {
				$("#groupId").val(groupId);
                initServiceList(0);
			}
			
			function loadAppList(openServiceId) {
                window.location.href = context + "/service/open/data/get/" + openServiceId+"/apply";
			}
			function loadServiceInfo(openServiceId) {
			  window.location.href = context + "/service/open/data/get/" + openServiceId+"/apply";
			}
		</script>
		<script id="servicelist" type="text/html">
            {{each data as item i}}
               <ul class="ue-list-n">
			     <li>
			        <div class="ue-list-left">
			            
			            <div class="ue-list-left-content">
			                <div class="title"><a onclick="loadServiceInfo('{{item.id}}')">{{item.name}}</a></div>
			                <div class="desc">表名:{{item.tableName}}&emsp;简介:{{item.description}}</div>
			            </div>
			        </div>
			        <div class="ue-list-right">
			            <span>{{item.createTime}}</span>
			            <button class="btn ue-btn" style="margin-top:8px" onclick="loadAppList('{{item.id}}')">申请</button>
			        </div>
			    </li>
			  </ul>
            {{/each}}
		</script>
		<script id="emptylist" type="text/html">
            <div class="data-empty">
                <span class="norecord"></span>
            </div>
        </script>
        
        <script id="subGroupListTemp" type="text/html">
			<span style="margin-right: 10px;">&nbsp;子分组:</span>
			<ul class="list-inline" style="display:inline-block">
			{{each data as item i}}
               	<li id="{{item.id}}" onclick="renderGroup('{{item.id}}')">
					<a onclick="reloadServiceListBySubGroupId('{{item.id}}')">{{item.name}}</a>
				</li>
			{{/each}}
			</ul>
		</script>
 	</body>
</html>