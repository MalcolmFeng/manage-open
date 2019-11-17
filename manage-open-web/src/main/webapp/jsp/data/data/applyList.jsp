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
    <title>数据列表</title>
    
    <!-- 需要引用的CSS -->
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/datalist/css/datalist.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/datalist/css/applyList.css'/>" />
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
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='data/data/applyList.js'/>"></script>
	</head>
	<body style="background-color: #f5f5f5;">
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
		</script>
		<script id="servicelist" type="text/html">
            {{each data as item i}}
            <div class="queue-con">
                <div class="con-body">
                    <img src="<l:assetcontext/>/skins/skin/data/datalist/img/queue-img.png" />
                    <div class="con-right">
                        <div class="queue-title">
                            <div class="queue-name" onclick="loadAppList('{{item.id}}')">{{item.name}}</div>
                            <div class="queue-time">{{item.createTime}}</div>
                        </div>
                        <%--<div class="table-name">表名：tb3</div>--%>
                        <div class="queue-desc" title='{{item.description}}'>{{item.description}}</div>
                    </div>
                </div>
				<a onclick="todetail('{{item.remoteId}}&{{item.dataSourceId}}')">
				<a>
				<button type="button" id="applyBtn" data-id={{item.id}} data-dataSourceId={{item.dataSourceId}}>申请</button>
				</a>
            </div>
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