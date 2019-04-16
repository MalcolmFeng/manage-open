<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
	<title>发布服务</title>

	<!-- 需要引用的CSS -->
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<l:asset path='css/ztree.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='data/datadev.css'/>" />
	<link rel="stylesheet" type="text/css" href="<l:asset path='data/register.css'/>"/>

	<script type="text/javascript" src="<l:asset path='jquery.js'/>" ></script>
	<script type="text/javascript" src="<l:asset path='bootstrap.js'/>" ></script>
	<script type="text/javascript" src="<l:asset path='form.js'/>" ></script>
	<script type="text/javascript" src="<l:asset path='arttemplate.js'/>" ></script>
	<script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
	<script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
	<script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
	<script type="text/javascript" src="<l:asset path='ztree.js'/>"></script>
	<script type="text/javascript" src="<l:asset path='data/register.js'/>"></script>

	<title>数据编辑</title>
	<style type="text/css">
		.Validform_input {
			width: 50%;
			margin-right: 10px;
		}
	</style>
</head>
<body>
<div>
	<div class="col-xs-12 col-md-12">
		<form class="form-horizontal" id="saveForm" name="saveForm" onsubmit="return false;">
			<h3 class="text-left htext">基本信息</h3>
			<hr class="fenge"/>
			<div class="form-group">
				<div class="col-xs-2 col-md-2 control-label">
					<label class="control-label">数据名称<span class="required">*</span></label>
				</div>
				<div class="col-xs-10 col-md-10">
					<input type="text" class="form-control ue-form Validform_input" id="name"
						   name="name" value="${dataDef.name}" placeholder="数据名称"
						   datatype="s" errormsg="请输入字母、数字、汉字或下划线" nullmsg="请输入数据名称"/>
					<span class="Validform_checktip Validform_span">4-20个字符(中英文、数字。中文算一个字符)</span>
				</div>
			</div>
			<div class="form-group">
				<input type="hidden" name="jdbcurl" value="${dataDef.jdbcUrl}"/>
				<div class="col-xs-2 col-md-2 control-label">
					<label class="control-label">数据源<span class="required">*</span></label>
				</div>
				<div class="col-xs-10 col-md-10">
					<div id="sourceDiv" style="width: 25%; display: inline-block"></div>
					<div id="subdatasourceDiv" style="width: 25%; display:none"></div>
					<span class="Validform_checktip Validform_span" style="float: none"></span>
				</div>
			</div>
			<div class="form-group">
				<input type="hidden" name="tablename" value="${dataDef.tableName}"/>
				<div class="col-xs-2 col-md-2 control-label">
					<label class="control-label">表名称<span class="required">*</span></label>
				</div>
				<div class="col-xs-10 col-md-10">
					<div id="tableDiv" style="width: 25%; display: inline-block"></div>
					<div id="subtabledataDiv" style="width: 25%; display:none"></div>
					<span class="Validform_checktip Validform_span" style="float: none"></span>
				</div>
			</div>
			<%--    <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">表名称<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <input type="text" class="form-control ue-form Validform_input" id="tableName"
                            name="tableName" value="${dataDef.tableName}" placeholder="tablename"
                            datatype="s"  nullmsg="请输入表名"/>
                        <span class="Validform_checktip Validform_span"></span>
                    </div>
                </div>--%>
			<div class="form-group">
				<div class="col-xs-2 col-md-2 control-label">
					<label class="control-label">数据授权<span class="required">*</span></label>
				</div>
				<div class="col-xs-10 col-md-10 text-left radio" style="margin-top: 5px;">
					<label><input type="radio" name="authType" value="1" <c:if test="${dataDef.authType eq 1 || !edit}"> checked="checked"</c:if>/>是&emsp;</label>
					<label><input type="radio" name="authType" value="0" <c:if test="${dataDef.authType eq 0 }"> checked="checked"</c:if>/>否&emsp;</label>
				</div>
			</div>
			<div class="form-group">
				<input type="hidden" name="groupId" value="${dataDef.groupId }"/>
				<div class="col-xs-2 col-md-2 control-label">
					<label class="control-label">数据分组<span class="required">*</span></label>
				</div>
				<div class="col-xs-10 col-md-10">
					<div id="groupDiv" style="width: 25%; display: inline-block"></div>
					<div id="subgrouplistDiv" style="width: 25%; display:none"></div>
					<span class="Validform_checktip Validform_span" style="float: none"></span>
				</div>
			</div>
			<%--  <div class="form-group" id="userAuth" type="hiden">
                  <div class="col-xs-2 col-md-2 control-label">
                      <label class="control-label">是否需要用户授权<span class="required">*</span></label>
                  </div>
                  <div class="col-xs-10 col-md-10 text-left radio" style="margin-top: 5px;">
                      <label><input type="radio" name="needUserAuth" value="0"<c:if test="${dataDef.needUserAuth eq 0 || !edit }"> checked="checked"</c:if>/>否&emsp;</label>
                      <label><input type="radio" name="needUserAuth" value="1"<c:if test="${dataDef.needUserAuth eq 1}"> checked="checked"</c:if>/>是&emsp;</label>
                  </div>
              </div>--%>
			<%--    <div class="form-group" id="servicePrice">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">价格</label>
                    </div>
                    <div class="col-xs-5 col-md-5 text-left" style="margin-top: 5px;">
                        <input type="text" class="form-control ue-form Validform_input" id="price"
                            name="price" value="${dataDef.price}" placeholder="0.0"
                            datatype="verifyPrice" errormsg="数字或小数点" nullmsg="请输入价格"/>
                        <span class="Validform_checktip Validform_span">元/次</span>
                    </div>
                </div>--%>
			<div class="form-group">
				<label class="col-xs-2 col-md-2 control-label">数据描述<span class="required">*</span></label>
				<div class="col-xs-10 col-md-10">
					<c:if test="${not empty dataDef.description}">
						<textarea id="description" name="description" rows="3" cols="80" style="width:80%;"
								  class="form-control ue-form Validform_input" placeholder="简述数据信息（5-200字）"
								  overflow="auto" errormsg="数据描述字数限制在5-200个!" nullmsg="请填写数据描述" readonly>${dataDef.description }</textarea>
					</c:if>
					<c:if test="${empty dataDef.description}">
					<textarea id="description" name="description" rows="3" cols="80" style="width:80%;"
							  class="form-control ue-form Validform_input" placeholder="简述数据信息（5-200字）"
							  overflow="auto" dataType="verifyDescritpion" errormsg="数据描述字数限制在5-200个!" nullmsg="请填写数据描述">${dataDef.description }</textarea>
					</c:if>
					<span class="Validform_checktip Validform_span"></span>
				</div>
			</div>
			<h3 class="text-left htext">表信息</h3>
			<hr class="fenge"/>
			<div class="form-group">
				<label class="col-xs-2 col-md-2 control-label">表字段</label>
				<div class="col-xs-10 col-md-10 param-list">
					<table id="inputtable">
						<tr>
							<th style="width: 180px;">字段名称</th>
							<th style="width: 180px;">字段类型</th>
							<th style="width: 180px;">是否必填</th>
							<th>字段描述</th>
							<%--     <th style="width: 60px;">操作</th>--%>
						</tr>
						<%--<c:if test="${not empty dataDef.columnList }">
                            <c:forEach items="${dataDef.columnList }" var="item">
                                <tr>
                                   <td>
                                       <input type="hidden" name="id" value="${item.id }" />
                                       <input type="text" name="columnName" value="${item.columnName }" />
                                   </td>
                                   <td>
                                       <select name="columnType">
                                           <option value="string" <c:if test="${item.columnType eq 'string'}"> selected="selected"</c:if> >字符串</option>
                                           <option value="number" <c:if test="${item.columnType eq 'number'}"> selected="selected"</c:if> >数值型</option>
                                       </select>
                                   </td>
                                   <td>
                                       <select name="isNull">
                                           <option value="1" <c:if test="${item.isNull eq '1' }">selected="selected"  </c:if> >是</option>
                                           <option value="0" <c:if test="${item.isNull eq '0' }">selected="selected"  </c:if> >否</option>
                                       </select>

                                   </td>
                                   <td><input type="text" name="description" value="${item.description }"/></td>
                                   <td><a onclick="register.forColumnDel(this)">删除</a></td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty dataDef.columnList }">
                            <tr>
                               <td><input type="text" name="columnName" value="" /></td>
                               <td>
                                   <select name="columnType">
                                       <option value="string" selected="selected">字符串</option>
                                       <option value="number">数值型</option>
                                   </select>
                               </td>
                               <td>
                                   <select name="isNull">
                                       <option value="1" selected="selected">是</option>
                                       <option value="0">否</option>
                                   </select>
                               </td>
                               <td><input type="text" name="description" value=""/></td>
                               <td><a onclick="register.forColumnDel(this)">删除</a></td>
                            </tr>
                        </c:if>--%>
					</table>
					<%--<div class="pull-right addrow"><a onclick="register.addInputParam()">增加一行</a></div>--%>
				</div>
			</div>
			<div class="form-group">
				<label class="col-xs-2 col-md-2 control-label">数据示例<span class="required">*</span></label>
				<div class="col-xs-10 col-md-10 param-list">
					<input id="dataExample" name="dataExample" hidden  value="${dataDef.dataExample}">
					<table id="inputExample">

					</table>
				</div>
			</div>
			<div class="form-group" >
				<label class="col-xs-2 col-md-2 control-label"></label>
				<div class="col-xs-10 col-md-10">
					<input type="submit" class="btn ue-btn-primary" value="发布">&emsp;
					<input id="goback" type="button" class="btn ue-btn" value="返回">
				</div>
			</div>
			<input type="hidden" id="id" value="${dataDef.id }" />
		</form>
	</div>
</div>
<script type="text/javascript">
	var context = "<l:assetcontext/>";
	var remote='${remote}';
	var defGroupId='${dataDef.groupId}';
	//编辑时的初始值
	var initContext;
	var initVersion;
	$(function() {
		initContext=$("#context").val();
		initVersion=$("#version").val();
		register.loadServiceGroupList("${dataDef.groupId}");
		register.loadDataOriginList("${dataDef.dataSourceId}","${dataDef.remoteId}");
		register.loadTableList();
		$("#saveForm").uValidform({
			datatype: {
				"verifyPrice" : verifyPrice,
				"verifyGroup": verifyGroup,
				"verifyDescritpion": verifyDescritpion,
				"verifyExample": verifyExample
			},
			callback:function(form){
				$.dialog({
					type: 'confirm',
					content: '您确定要提交表单吗？',
					ok: function () {
						register.forPublish();
					},
					cancel: function () {}
				});
			}
		});

		$("#goback").click(function(){
			location.href=context + "/service/open/data/getPage";

			//设置cockie，value值为layoutleft.jsp中所定义
			setCookie("leftselmenu","slist");
		});
	});

	function verifyGroup(gets, obj, curform, datatype) {
		if(gets == null || gets == '') {
			obj.attr("errormsg", "请选择服务分组");
			return false;
		}
	}

	function verifyPrice(gets, obj, curform, datatye) {
		var regx = /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
		if(regx.test(gets) == false) {
			obj.attr("errormsg","请输正确价格");
			return false;
		}
		return true;
	}

	function verifyDescritpion(gets, obj, curform, datatye) {
		if(gets.length>200 || gets.length<5){
			obj.attr("errormsg", "数据描述字数限制在5-200个!");
			return false;
		}
		return true;
	}

	function verifyExample(gets, obj, curform, datatye) {
		if(gets.length<5){
			obj.attr("errormsg", "返回内容描述不能小于5个字符!");
			return false;
		}
		return true;
	}
</script>
<script id="inputitem" type="text/html">
	<tr>
		<td>
			<input type="hidden" name="id" value="" />
			<input type="text" name="columnName" value="" />
		</td>
		<td>
			<select name="columnType">
				<option value="string" selected="selected">字符串</option>
				<option value="number">数值型</option>
			</select>
		</td>
		<td>
			<select name="isNull">
				<option value="1" selected="selected">是</option>
				<option value="0">否</option>
			</select>
		</td>
		<td><input type="text" name="description" value=""/></td>
		<%-- <td><a onclick="register.forColumnDel(this)">删除</a></td>--%>
	</tr>
</script>

<script id="grouplist2" type="text/html">
	<select id="groupSelect" class="form-control ue-form" onchange="register.loadSubServiceGroupList();" datatype="s" nullmsg="请选择数据分组">
		<option value="">请选择分组</option>
		{{each data as group}}
		<option value="{{group.id}}">{{group.name}}</option>
		{{/each}}
	</select>
</script>

<script id="grouplist3" type="text/html">
	<select id="groupSelect3" class="form-control ue-form" onchange="register.loadTableList();" datatype="s" nullmsg="请选择数据源分组">
		<option value="">请选择数据源</option>
		{{each data as group}}
		<option value="{{group.dataSourceId}}">{{group.instanceName}}</option>
		{{/each}}
	</select>
</script>

<script id="grouplist4" type="text/html">
	<select id="groupSelect4" class="form-control ue-form" onchange="register.loadTableDetail();" datatype="s" nullmsg="请选择表">
		<option value="">请选择表</option>
		{{each data as group}}
		<option value="{{group.dataResourceId}}">{{group.tableName}}</option>
		{{/each}}
	</select>
</script>

<script id="subgrouplist2" type="text/html">
	<select id="subgroupSelect" class="form-control ue-form" onchange="register.selectServiceGroup();">
		{{each data as group}}
		<option value="{{group.id}}">{{group.name}}</option>
		{{/each}}
	</select>
</script>

<script id="detail" type="text/html">
	<tr>
		<th style="width: 180px;">字段名称</th>
		<th style="width: 180px;">字段类型</th>
		<th style="width: 180px;">是否必填</th>
		<th>字段描述</th>
		<%--<th style="width: 60px;">操作</th>--%>
	</tr>
	{{each columnInfo as oneinfo}}
	<tr>
		<td id="columnName">
			{{oneinfo.columnName }}
		</td>
		<td>
			<input type="hidden" name="columnType" value="{{oneinfo.columnType}}" />
			{{if oneinfo.columnType== 'string'}}
			字符串
			{{else}}
			数值型
			{{/if}}
			<%--<select name="columnType">
                    <option value="string" <c:if test="{{oneinfo.columnType eq 'string'}} "> selected="selected"</c:if> >字符串</option>
                    <option value="number" <c:if test="{{oneinfo.columnType eq 'number'}}"> selected="selected"</c:if> >数值型</option>
                </select>--%>
		</td>
		<td>
			<input type="hidden" name="isNull" value="{{oneinfo.isNull}}" />
			{{if oneinfo.isNull== '1'}}
			是
			{{else}}
			否
			{{/if}}
			<%--	<select name="isNull">
                    <option value="1" <c:if test="{{oneinfo.isNull eq '1' }}">selected="selected"  </c:if> >是</option>
                    <option value="0" <c:if test="{{oneinfo.isNull eq '0' }}">selected="selected"  </c:if> >是</option>
                </select>--%>

		</td>
		<td><input type="text" name="description" value="{{oneinfo.columnDescription }}"/></td>
		<%--<td><a onclick="register.forColumnDel(this)">删除</a></td>--%>
	</tr>
	{{/each}}
</script>

<script id="example" type="text/html">
	<tr>
		{{each columnInfo as oneinfo}}
		<th>{{oneinfo.columnName }}</th>
		{{/each}}
	</tr>
	{{each example as oneinfo}}
	<tr>
		{{each oneinfo as one}}
		<td>
			{{one}}
		</td>
		{{/each}}
	</tr>
	{{/each}}
</script>

</body>
</html>