<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/jsp/public/images/favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/datatables.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ztree.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/datadev.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='data/register.css'/>"/>

    <script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='form.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='arttemplate.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='ztree.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='service/register.js'/>"></script>

    <title>服务编辑</title>
    <style type="text/css">
        .Validform_input {
            width: 50%;
            margin-right: 10px;
        }
    </style>
</head>
<body>
<div>
    <div class="demo1"></div>
    <div class="col-xs-12 col-md-12">
        <form class="form-horizontal" id="saveForm" name="saveForm" onsubmit="return false;">
            <div id="step_0" class="_step">
                <h3 class="text-left htext">基本信息</h3>
                <hr class="fenge"/>
                <div class="form-group">
                    <input type="hidden" id="groupId" name="groupId" value="${serviceDef.apiGroup }"/>
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">服务分组<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <div id="groupDiv" style="width: 25%; display: inline-block"></div>
                        <div id="subgrouplistDiv" style="width: 25%; display:none"></div>
                        <span class="Validform_checktip Validform_span" style="float: none"></span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">服务名称<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <input type="text" style="width: 25%" class="form-control ue-form Validform_input" id="name"
                               name="name" value="${serviceDef.name}" placeholder="名称"
                               datatype="checkname" errormsg="请输入字母、数字、汉字或下划线" nullmsg="必填"/>
                        <span class="Validform_checktip Validform_span">3-20个字符(中英文、数字。中文算一个字符)</span>
                    </div>
                </div>
                <%--<div class="form-group">--%>
                <%--<div class="col-xs-2 col-md-2 control-label">--%>
                <%--<label class="control-label">服务版本<span class="required">*</span></label>--%>
                <%--</div>--%>
                <%--<div class="col-xs-10 col-md-10">--%>
                <%--<input type="text"  style="width: 25%" class="form-control ue-form Validform_input" id="version"--%>
                <%--name="version" value="${serviceDef.version}" placeholder="1.0"--%>
                <%--datatype="verifyVersion" errormsg="数字或小数点" nullmsg="必填"/>--%>
                <%--<span class="Validform_checktip Validform_span">1-10个字符(数字或小数点)</span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">服务授权<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10 text-left radio" style="margin-top: 5px;">
                        <label><input type="radio" name="authType" value="0" <c:if test="${serviceDef.authType eq 0 }"> checked="checked"</c:if>/>无需授权&emsp;</label>
                        <label><input type="radio" name="authType" value="1" <c:if test="${serviceDef.authType eq 1 || !edit}"> checked="checked"</c:if>/>需要授权&emsp;</label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">服务价格</label>
                    </div>
                    <div class="col-xs-10 col-md-10 text-left radio" style="margin-top: 5px;">
                        <input type="text" style="width: 25%" class="form-control ue-form Validform_input" id="price"
                               name="price" value="${serviceDef.price}" placeholder="价格"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">服务描述<span class="required">*</span></label>
                    <div class="col-xs-10 col-md-10">
					<textarea id="description" name="description" rows="3" cols="80" style="width:80%;"
                              class="form-control ue-form Validform_input" placeholder="简述（5-200字）"
                              overflow="auto" dataType="verifyDescritpion" errormsg="描述字数限制在5-200个!" nullmsg="请填写描述">${serviceDef.description }</textarea>
                        <span class="Validform_checktip Validform_span"></span>
                    </div>
                </div>
            </div>
            <div id="step_1" class="_step" style="display: none;">
                <h3 class="text-left htext">后端基础定义</h3>
                <hr class="fenge"/>
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">后端服务地址<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <div>
                            <input type="text" style="width: 25%" class="form-control ue-form Validform_input" id="serviceAddr"
                                   name="serviceAddr" value="${serviceDef.scAddr}"
                                   datatype="verifyUrl" nullmsg="必填"/>
                            <%--<span style="margin-left: 10px;"><a href="#" onclick="">选择服务</a></span>--%>
                            <button id="search" type="button" class="btn ue-btn ">
                                <span class="fa"></span>数据服务
                            </button>
                            <button id="clear" type="button" class="btn ue-btn ">
                                <span class="fa"></span>重置
                            </button>
                        </div>
                        <span class="Validform_checktip Validform_span"></span>
                    </div>
                </div>
                <%--<div class="form-group">--%>
                <%--<div class="col-xs-2 col-md-2 control-label">--%>
                <%--<label class="control-label">后端服务协议<span class="required">*</span></label>--%>
                <%--</div>--%>
                <%--<div class="col-xs-10 col-md-10 text-left radio" style="margin-top: 5px;">--%>
                <%--<label><input  datatype="*" nullmsg="请选择协议" type="radio" name="serviceProtocol" value="http" <c:if test="${serviceDef.scProtocol eq 'http' }">checked  </c:if>  />HTTP&emsp;</label>--%>
                <%--&lt;%&ndash;<label><input   type="radio" name="serviceProtocol" value="https"  />HTTPS&emsp;</label>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<label><input   type="radio" name="serviceProtocol" value="websocket" />WEBSOCKET</label>&ndash;%&gt;--%>
                <%--<span class="Validform_checktip"></span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">HTTP Method<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <select id="serviceHttpMethod" name="serviceHttpMethod" style="width: 25%" class="form-control ue-form Validform_input" datatype="s" nullmsg="请选择HTTP Method">
                            <option value="get" <c:if test="${serviceDef.scHttpMethod eq 'get' }">selected="selected"  </c:if>>GET</option>
                            <option value="post" <c:if test="${serviceDef.scHttpMethod eq 'post' }">selected="selected"  </c:if>>POST</option>
                        </select>
                        <span class="Validform_checktip Validform_span" style="float: none"></span>
                    </div>
                </div>
                <%--<div class="form-group">--%>
                <%--<div class="col-xs-2 col-md-2 control-label">--%>
                <%--<label class="control-label">后端超时(ms)<span class="required">*</span></label>--%>
                <%--</div>--%>
                <%--<div class="col-xs-10 col-md-10">--%>
                <%--<input type="text" style="width: 25%" class="form-control ue-form Validform_input" id="timeout"--%>
                <%--name="timeout" value="${serviceDef.timeout}" placeholder="30000"--%>
                <%--datatype="verifyMs" errormsg="数字" nullmsg="必填"/>--%>
                <%--<span class="Validform_checktip Validform_span"></span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="form-group">--%>
                <%--<div class="col-xs-2 col-md-2 control-label">--%>
                <%--<label class="control-label">ContentType的值	<span class="required">*</span></label>--%>
                <%--</div>--%>
                <%--<div class="col-xs-10 col-md-10">--%>
                <%--<input type="text" style="width: 25%" class="form-control ue-form Validform_input" id="contentType"--%>
                <%--name="contentType" value="${serviceDef.contentType}"--%>
                <%--datatype="*" nullmsg="必填"/>--%>
                <%--<span class="Validform_checktip Validform_span"></span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <h3 class="text-left htext">后端服务参数配置</h3>
                <hr class="fenge"/>
                <div class="form-group">
                    <div class="col-xs-12 col-md-12 param-list">
                        <table id="backendinputtable">
                            <tr>
                                <th style="width: 12%;">后端参数名称</th>
                                <th style="width: 12%;">后端参数类型</th>
                                <th style="width: 12%;">后端参数位置</th>
                                <th style="width: 8%;">是否必填</th>
                                <th style="width: 5%;">排序</th>
                                <th>后端参数描述</th>
                                <th style="width: 12%;">操作</th>
                            </tr>
                            <tbody id="backendinputtbody">
                            <c:forEach items="${serviceDef.inputList}" var="inparam">
                                <c:if test="${serviceDef.remoteId !=null}">
                                    <tr>
                                        <td>
                                            <input type="hidden" name="id" value=""/>
                                            <input type="text" name="paramName" readonly value="${inparam.scName}"/>
                                        </td>
                                        <td>
                                            <input type="hidden" name="type" value="${inparam.scType}"/>
                                                ${inparam.type}
                                        </td>
                                        <td>
                                            <select name="paramType">
                                                <option value="path" <c:if test="${inparam.scParamType =='path'}"> selected="selected" </c:if>>Parameter Path</option>
                                                <option value="head" <c:if test="${inparam.scParamType =='head'}"> selected="selected" </c:if>>Head</option>
                                                <option value="query" <c:if test="${inparam.scParamType =='query'}"> selected="selected" </c:if>>Query</option>
                                                <option value="body" <c:if test="${inparam.scParamType =='body'}"> selected="selected" </c:if>>Body</option>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="hidden" name="required" value="${inparam.scRequired}"/>
                                            <c:if test="${inparam.scRequired =='1'}">是</c:if>
                                            <c:if test="${inparam.scRequired =='0'}">否</c:if>
                                        </td>
                                        <td>
                                            <input type="hidden" name="seq" readonly value="${inparam.scSeq}"/>
                                        </td>
                                        <td>
                                            <input type="text" name="description" readonly value="${inparam.scDescription}"/>
                                        </td>
                                        <td>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${serviceDef.remoteId ==null}">
                                    <tr>
                                        <td>
                                            <input type="hidden" name="id" value=""/>
                                            <input type="text" name="paramName" value="${inparam.scName}" onchange="changeEditFlag()"/>
                                        </td>
                                        <td>
                                            <select name="type" onchange="changeEditFlag()">
                                                <option value="string" <c:if test="${inparam.scType =='string'}"> selected="selected" </c:if> >String</option>
                                                <option value="int" <c:if test="${inparam.scType =='int'}"> selected="selected" </c:if>>Int</option>
                                                <option value="long" <c:if test="${inparam.scType =='long'}"> selected="selected" </c:if>>Long</option>
                                                <option value="double" <c:if test="${inparam.scType =='double'}"> selected="selected" </c:if>>Double</option>
                                                <option value="float" <c:if test="${inparam.scType =='float'}"> selected="selected" </c:if>>Float</option>
                                                <option value="boolean" <c:if test="${inparam.scType =='boolean'}"> selected="selected" </c:if>>Boolean</option>
                                            </select>
                                        </td>
                                        <td>
                                            <select name="paramType" onchange="changeEditFlag()">
                                                <option value="path" <c:if test="${inparam.scParamType =='path'}"> selected="selected" </c:if>>Parameter Path</option>
                                                <option value="head" <c:if test="${inparam.scParamType =='head'}"> selected="selected" </c:if>>Head</option>
                                                <option value="query" <c:if test="${inparam.scParamType =='query'}"> selected="selected" </c:if>>Query</option>
                                                <option value="body" <c:if test="${inparam.scParamType =='body'}"> selected="selected" </c:if>>Body</option>
                                            </select>
                                        </td>
                                        <td>
                                            <select name="required" onchange="changeEditFlag()">
                                                <option value="1"
                                                        <c:if test="${inparam.scRequired =='1'}">selected="selected"</c:if> >是
                                                </option>
                                                <option value="0"
                                                        <c:if test="${inparam.scRequired =='1'}">selected="selected"</c:if> >否
                                                </option>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="text" name="seq" value="${inparam.scSeq}" onchange="changeEditFlag()"/>
                                        </td>
                                        <td>
                                            <input type="text" name="description" value="${inparam.scDescription}" onchange="changeEditFlag()"/>
                                        </td>
                                        <td><a onclick="register.forColumnDel(this)">删除</a></td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                        <c:if test="${serviceDef.remoteId ==null}">
                            <div id="addBackend" class="pull-right addrow"><a onclick="register.addbackendInputParamTr()">增加一行</a></div>
                        </c:if>
                    </div>
                    <%--<div class="col-xs-2 col-md-2">--%>
                    <%--<input type="hidden" value="*" datatype="verifyParam"  errormsg="前后端参数问题"/>--%>
                    <%--<span class="Validform_checktip Validform_span" style="float: none"></span>--%>
                    <%--</div>--%>
                </div>
            </div>
            <div id="step_2" class="_step" style="display: none;">
                <h3 class="text-left htext">请求基础定义</h3>
                <hr class="fenge"/>
                <%--<div class="form-group">--%>
                <%--<div class="col-xs-2 col-md-2 control-label">--%>
                <%--<label class="control-label">协议<span class="required">*</span></label>--%>
                <%--</div>--%>
                <%--<div class="col-xs-10 col-md-10 text-left radio" style="margin-top: 5px;">--%>
                <%--<label><input datatype="*" nullmsg="请选择协议" type="radio" name="protocol" value="http" <c:if test="${serviceDef.protocol eq 'http' }"> checked="checked"</c:if>/>HTTP</label>--%>
                <%--<label><input type="radio" name="protocol" value="https" <c:if test="${serviceDef.protocol eq 'https' }"> checked="checked"</c:if>/>HTTPS</label>--%>
                <%--<label><input type="radio" name="protocol" value="websocket" <c:if test="${serviceDef.protocol eq 'websocket' }"> checked="checked"</c:if>/>WEBSOCKET</label>--%>
                <%--<span class="Validform_checktip"></span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">请求Path<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <input type="text" style="width: 25%" class="form-control ue-form Validform_input" id="reqPath"
                               name="reqPath" value="${serviceDef.reqPath}"
                               datatype="verifyReqPath" errormsg="请输入字母、数字或下划线" nullmsg="请输入请求Path"/>
                        <span class="Validform_checktip Validform_span">4-20个字符(英文、数字)</span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">HTTP Method<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <select id="httpMethod" name="httpMethod" style="width: 25%" class="form-control ue-form Validform_input" datatype="s" nullmsg="请选择HTTP Method">
                            <option value="get" <c:if test="${serviceDef.httpMethod eq 'get' }">selected="selected"  </c:if>>GET</option>
                            <option value="post" <c:if test="${serviceDef.httpMethod eq 'post' }">selected="selected"  </c:if>>POST</option>
                        </select>
                        <span class="Validform_checktip Validform_span" style="float: none"></span>
                    </div>
                </div>
                <h3 class="text-left htext">入参定义</h3>
                <hr class="fenge"/>
                <div class="form-group">
                    <div class="col-xs-12 col-md-12 param-list">
                        <table id="inputtable">
                            <tr>
                                <th style="width: 20%">参数名</th>
                                <th style="width: 20%">类型</th>
                                <th style="width: 20%">是否必填</th>
                                <th>字段描述</th>
                                <th style="width: 20%">后端参数</th>
                            </tr>
                            <tbody id="inputtabletbody">
                            <c:if test="${not empty serviceDef.inputList }">
                                <c:forEach items="${serviceDef.inputList }" var="item">
                                    <tr>
                                        <td>
                                            <input type="hidden" name="id" value="${item.id }"/>
                                            <input type="text" name="inputParamName" value="${item.name }"/>
                                        </td>
                                        <td>
                                            <input type="text" readonly name="inputParamType" value="${item.type}"/>
                                                <%--<select name="inputParamType">--%>
                                                <%--<option value="string" <c:if test="${item.type eq 'string'}"> selected="selected"</c:if>>String</option>--%>
                                                <%--<option value="int" <c:if test="${item.type eq 'int'}"> selected="selected"</c:if>>Int</option>--%>
                                                <%--<option value="long" <c:if test="${item.type eq 'long'}"> selected="selected"</c:if>>Long</option>--%>
                                                <%--<option value="double" <c:if test="${item.type eq 'double'}"> selected="selected"</c:if>>Double</option>--%>
                                                <%--<option value="float" <c:if test="${item.type eq 'float'}"> selected="selected"</c:if>>Float</option>--%>
                                                <%--<option value="boolean" <c:if test="${item.type eq 'boolean'}"> selected="selected"</c:if>>Boolean</option>--%>
                                                <%--</select>--%>
                                        </td>
                                        <td>
                                            <select name="inputRequired">
                                                <option value="1"
                                                        <c:if test="${item.required eq '1' }">selected="selected"  </c:if> >是
                                                </option>
                                                <option value="0"
                                                        <c:if test="${item.required eq '0' }">selected="selected"  </c:if> >否
                                                </option>
                                            </select>
                                        </td>
                                        <td><input type="text" name="inputDescription" value="${item.description }"/></td>
                                        <td><input type="text" name="backname" value="${item.scName }" readonly/></td>
                                            <%--<td><a onclick="register.forColumnDel(this)">删除</a></td>--%>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                        <%--<div class="pull-right addrow"><a onclick="register.addInputParam()">增加一行</a></div>--%>
                    </div>
                </div>
            </div>

            <div id="step_3" class="_step" style="display: none;">
                <h3 class="text-left htext">返回结果基础定义</h3>
                <hr class="fenge"/>
                <div class="form-group">
                    <div class="col-xs-2 col-md-2 control-label">
                        <label class="control-label">返回ContentType<span class="required">*</span></label>
                    </div>
                    <div class="col-xs-10 col-md-10">
                        <select id="responseContentType" name="responseContentType" style="width: 40%" class="form-control ue-form Validform_input" datatype="s" nullmsg="请选择返回ContentType">
                            <option value="json"
                                    <c:if test="${serviceDef.contentType eq 'json' }">selected="selected"  </c:if> >JSON (application/json;charset=utf-8)
                            </option>
                            <option value="text" <c:if test="${serviceDef.contentType eq 'text' }">selected="selected"  </c:if>>文本 (text/plain;charset=utf-8)</option>
                            <option value="binary" <c:if test="${serviceDef.contentType eq 'binary' }">selected="selected"  </c:if>>二进制 (application/octet-stream;charset=utf-8)</option>
                            <option value="xml" <c:if test="${serviceDef.contentType eq 'xml' }">selected="selected"  </c:if>>XML (application/xml;charset=utf-8)</option>
                            <option value="html" <c:if test="${serviceDef.contentType eq 'html' }">selected="selected"  </c:if>>HTML (text/html;charset=utf-8)</option>
                            <%--<option  value="passthrough" <c:if test="${serviceDef.contentType eq 'passthrough' }">selected="selected"  </c:if>>透传后端Content-Type</option>--%>
                        </select>
                        <span class="Validform_checktip Validform_span" style="float: none"></span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-2 col-md-2 control-label">返回结果示例<span class="required">*</span></label>
                    <div class="col-xs-10 col-md-10">
					<textarea id="returnSample" name="returnSample" rows="3" cols="80" style="width:80%;"
                              class="form-control ue-form Validform_input" placeholder="必填"
                              overflow="auto" dataType="*" errormsg="请填写示例" nullmsg="请填写示例">${serviceDef.returnSample}</textarea>
                        <span class="Validform_checktip Validform_span"></span>
                    </div>
                </div>
                <h3 class="text-left htext">返回值释义</h3>
                <hr class="fenge"/>
                <div class="form-group">
                    <div class="col-xs-12 col-md-12 param-list">
                        <table id="outtable">
                            <tr>
                                <th style="width: 12%;">参数名称</th>
                                <th style="width: 12%;">后端参数类型</th>
                                <th>参数描述</th>
                                <th style="width: 12%;">操作</th>
                            </tr>
                            <tbody id="outbody">
                            </tbody>
                        </table>
                        <div id="addOutParam" class="pull-right addrow"><a onclick="register.addOutParamTr()">增加一行</a></div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-12 col-md-12" style="text-align: center">
                    <input id="prevStep" style="display: none;" type="button" class="btn ue-btn-primary" value="上一步">&emsp;
                    <input id="nextStep" type="button" class="btn ue-btn-primary" value="下一步">&emsp;
                    <input id="_submit" type="submit" style="display: none;" class="btn ue-btn-primary" value="保存">&emsp;
                    <input id="goback" type="button" class="btn ue-btn" value="返回">
                </div>
            </div>
            <input type="hidden" id="id" value="${serviceDef.id }"/>
            <input type="hidden" id="remoteId" value="${serviceDef.remoteId }"/>
        </form>
    </div>
</div>
<script type="text/javascript">
    var _step = 0;
    var context = "<l:assetcontext/>";
    var defGroupId = '${serviceDef.apiGroup}';
    var serviceId = '${serviceDef.id}';
    //编辑时的初始值
    var initContext = '${serviceDef.reqPath}';
    var initRemoteId = '${serviceDef.remoteId}';
    var initVersion;
    $(function () {
        reNavBar();
        register.loadServiceGroupList("${serviceDef.apiGroup}");//初始化分组
        $("#saveForm").uValidform({
            datatype: {
                "verifyExample": verifyExample
            },
            callback: function (form) {
                $.dialog({
                    type: 'confirm',
                    content: '您确定要提交表单吗？',
                    ok: function () {
                        register.forSave();
                    },
                    cancel: function () {
                    }
                });
            }
        });
        var step_0 = $("#step_0").uValidform({
            datatype: {
                "checkname": checkname,
//                    "verifyVersion" : verifyVersion,
                "verifyGroup": verifyGroup,
                "verifyDescritpion": verifyDescritpion
            }
        });
        var step_1 = $("#step_1").uValidform({
            datatype: {
                "verifyUrl": verifyUrl,
                "verifyMs": verifyMs
            }
        });
        var step_2 = $("#step_2").uValidform({
            datatype: {
                "verifyReqPath": verifyReqPath,
                "verifyParam": verifyParam
            }
        });
        $("#nextStep").click(function () {
            if (_step == 0 && !step_0.check()) {
                return
            }
            ;
            if (_step == 1 && !step_1.check()) {
                return
            }
            if (_step == 2 && !step_2.check()) {
                return
            }
            _step = _step + 1;
            if (_step > 0) {
                $("#prevStep").show();
            }
            if (_step == 3) {
                $("#nextStep").hide();
                $("#_submit").show();
            }
            if (_step == 1) {
//                    register.loadApiList();
            }
            if (_step == 2) {
                register.addInputParamList();
            }
            reNavBar();
        });
        $("#prevStep").click(function () {
            _step = _step - 1;
            if (_step < 3) {
                $("#_submit").hide();
                $("#nextStep").show();
            }
            if (_step == 0) {
                $("#prevStep").hide();
            }
            reNavBar();
        });
        $("#goback").click(function () {
            location.href = context + "/service/open/api/getApiPage";
        });
        $("#search").click(function () {
            $.dialog({
                type: "iframe",
                url: context + "/service/open/api/remotePage",
                title: "选择服务",
                width: 600,
                height: 400,
                onclose: function () {
                    var serviceId = this.returnValue;
                    if (serviceId) {
                        $("#addBackend").hide();
                        //初始后端请求参数
                        register.loadApiDetail(serviceId);
                    }
                }
            });
        });
        $("#clear").click(function () {
            // if($("#remoteId").val()){
            $("#serviceAddr").val("");
            $("#returnSample").val("");
            $("#remoteId").val("");
            $("#backendinputtbody").html("");
            $("#addBackend").show();
            $("#serviceHttpMethod").removeAttr("disabled");
            // }
        });
    });

    function reNavBar() {
        $('.demo1').html("");
        $("#step_" + _step).show().siblings("._step").hide();
        if (_step == 0) {
            $('.demo1').getNavBar(['基本信息', '定义API后端服务', '定义API请求', '定义返回结果']);
        } else if (_step == 1) {
            $('.demo1').getNavBar(['基本信息', '定义API后端服务', '定义API请求', '定义返回结果']).next();
        } else if (_step == 2) {
            $('.demo1').getNavBar(['基本信息', '定义API后端服务', '定义API请求', '定义返回结果']).next().next();
        } else if (_step == 3) {
            $('.demo1').getNavBar(['基本信息', '定义API后端服务', '定义API请求', '定义返回结果']).next().next().next();
        }
        $('.ue-nav-box .nav-box').css("width", "90px");
        $('.ue-nav-box  span.text-h').css("width", "90px");
        // $('.ue-nav-box .line-h').css("width","167px");
    }

    function verifyUrl(gets, obj, curform, datatye) {
        var re = /(http|https):\/\/([\w.]+\/?)\S*/;
        if (!re.test(gets)) {
            obj.attr("errormsg", "必须以http,https开头,且需符合URL规范,检查格式是否正确");
            return false;
        }
        return true
    }

    function verifyMs(gets, obj, curform, datatye) {
        var re = /^[1-9]+[0-9]*]*$/;
        if (re.test(gets) && gets > 30000) {
            obj.attr("errormsg", "最大值30000");
            return false;
        }
        return re.test(gets)
    }

    function checkname(gets, obj, curform, datatye) {
        if (gets.length > 20 || gets.length < 3) {
            obj.attr("errormsg", "名称限制在3-20个字符!");
            return false;
        }
        return true;
    }

    function verifyReqPath(gets, obj, curform, datatye) {
        var context = $("#reqPath").val();
//            var version = $("#version").val();
        var groupId = $("#groupId").val();
        var contextVal = $("#reqPath").parent().find(".Validform_right");
        var versioinVal = $("#version").parent().find(".Validform_right");
        //编辑
        if (initContext != '' && initContext == context) {
            if (defGroupId != '' && defGroupId == groupId) {
                return true;
            }
        }
        if (context == null || context == '') {
            obj.attr("nullmsg", "请求path不能为空");
            return false;
        } else if (context.length > 200) {
            obj.attr("errormsg", "不能超过200个字符");
            return false;
        } else {
            var regx = /^\/[a-zA-Z0-9_]+$/;
            if (regx.test(context) == false) {
                obj.attr("errormsg", "请输入以/开头的字符、数字、下划线");
                return false;
            }
        }
        //地址是否已存在
        if (addressValidate(context, groupId) == false) {
            obj.attr("errormsg", "所选分组存在同名请求Path");
            return false;
        }
        return true;
    }

    function verifyVersion(gets, obj, curform, datatye) {
        var context = $("#reqPath").val();
        var version = $("#version").val();
        var contextVal = $("#reqPath").parent().find(".Validform_right");
        var versioinVal = $("#version").parent().find(".Validform_right");
        //编辑
        if (initVersion != '' && initVersion == version) {
            return;
        }
        if (version == null || version == '') {
            obj.attr("errormsg", "版本不能为空");
            return false;
        } else if (version.length > 30) {
            obj.attr("errormsg", "不能超过30个字符");
            return false;
        } else {
            var regx = /^[0-9.]+$/;
            if (regx.test(gets) == false) {
                obj.attr("errormsg", "数字或小数");
                return false;
            }
        }
        if (version != initVersion) {
            //地址是否已存在
            if (contextVal.length != 0) {
                if (addressValidate(context, version) == false) {
                    obj.attr("errormsg", "服务地址已经存在");
                    return false;
                }
            }
        }
        return true;
    }

    function verifyGroup(gets, obj, curform, datatype) {
        if (gets == null || gets == '') {
            obj.attr("errormsg", "请选择服务分组");
            return false;
        }
    }

    function verifyDescritpion(gets, obj, curform, datatye) {
        if (gets.length > 200 || gets.length < 5) {
            obj.attr("errormsg", "数据描述字数限制在5-200个!");
            return false;
        }
        return true;
    }

    function verifyExample(gets, obj, curform, datatye) {
        if (gets.length < 5) {
            obj.attr("errormsg", "返回内容描述不能小于5个字符!");
            return false;
        }
        return true;
    }

    function addressValidate(reqpath, groupId) {
        var validate = false;
        $.ajax({
            type: "get",
            async: false,
            url: context + "/service/open/api/" + groupId + reqpath,
            success: function (msg) {
                validate = msg;
            }
        });
        return validate;
    }

    //验证后端参数
    function verifyParam(gets, obj, curform, datatye) {
        $(obj).parent().find(".Validform_checktip").text("").removeClass("Validform_wrong");
        var endList = register.getBackendParamList();
        var inList = register.getInputParamList();
        if (inList.length > 0 && inList.length < endList.length) {
            obj.attr("errormsg", "请求参数不能少于后端参数");
            return false;
        }
        return true;
    }

    var initInputList = [];
    <c:forEach items="${serviceDef.inputList}" var="item">
    var param = {
        name: "${item.name}",
        type: "${item.type}",
        required: "${item.required}",
        description: "${item.description}",
        scName: "${item.scName}",
        scType: "${item.scType}",
        scParamType: "${item.scParamType}",
        scSeq: "${item.scSeq}",
        scRequired: "${item.scRequired}",
        scDescription: "${item.scDescription}"
    }
    initInputList.push(param);
    </c:forEach>
    var editFlag = false;

    function changeEditFlag() {
        editFlag = true;
    }
</script>

<script id="outputitem" type="text/html">
    <tr>
        <td>
            <input type="hidden" name="id"/>
            <input type="text" name="name" value=""/>
        </td>
        <td>
            <select name="type">
                <option value="string" selected="selected">字符串</option>
                <option value="number">数值型</option>
            </select>
        </td>
        <td><input type="text" name="description" value=""/></td>
        <td><a onclick="register.forColumnDel(this)">删除</a></td>
    </tr>
</script>

<script id="inputitem" type="text/html">
    <tr>
        <td>
            <input type="hidden" name="id" value=""/>
            <input type="text" name="inputParamName" value=""/>
        </td>
        <td>
            <select name="inputParamType">
                <option value="string" selected="selected">String</option>
                <option value="int">Int</option>
                <option value="long">Long</option>
                <option value="double">Double</option>
                <option value="float">Float</option>
                <option value="boolean">Boolean</option>
            </select>
        </td>
        <td>
            <select name="inputRequired">
                <option value="1" selected="selected">是</option>
                <option value="0">否</option>
            </select>
        </td>
        <td><input type="text" name="inputDescription" value=""/></td>
        <td><a onclick="register.forColumnDel(this)">删除</a></td>
    </tr>
</script>
<script id="inputitemlist" type="text/html">
    {{each data as inparam}}
    <tr>
        <td>
            <input type="hidden" name="id" value=""/>
            <input type="text" name="inputParamName" value="{{inparam.name}}"/>
        </td>
        <td>
            <input type="text" readonly name="inputParamType" value="{{inparam.type}}"/>
            <%--<select name="inputParamType">--%>
            <%--<option value="string" selected="selected">String</option>--%>
            <%--<option value="int">Int</option>--%>
            <%--<option value="long">Long</option>--%>
            <%--<option value="double">Double</option>--%>
            <%--<option value="float">Float</option>--%>
            <%--<option value="boolean">Boolean</option>--%>
            <%--</select>--%>
        </td>
        <td>
            <select name="inputRequired">
                {{ if inparam.required=='1' }}
                <option value="1" selected="selected">是</option>
                <option value="0">否</option>
                {{/if}}
                {{ if inparam.required=='0' }}
                <option value="1">是</option>
                <option value="0" selected="selected">否</option>
                {{/if}}
            </select>
        </td>
        <td><input type="text" name="inputDescription" value="{{inparam.description}}"/></td>
        <td><input type="text" name="backname" value="{{inparam.name}}" readonly/></td>
    </tr>
    {{/each}}
</script>
<script id="backendinputitem" type="text/html">
    {{each data as inparam}}
    <tr>
        <td>
            <input type="hidden" name="id" value=""/>
            <input type="text" name="paramName" readonly value="{{inparam.name}}"/>
        </td>
        <td>
            <input type="hidden" name="type" value="{{inparam.type}}"/>
            {{inparam.type}}
        </td>
        <td>
            <select name="paramType" disabled="disabled">
                <option value="path">Parameter Path</option>
                <option value="head">Head</option>
                <%--默认选中--%>
                <option value="query" selected>Query</option>
                <option value="body">Body</option>
            </select>
        </td>
        <td>
            <input type="hidden" name="required" value="{{inparam.required}}"/>
            {{ if inparam.required=="1"}} 是 {{ /if }}
            {{ if inparam.required=="0"}} 否 {{ /if }}
        </td>
        <td>
            <input type="hidden" name="seq" value="{{inparam.seq}}"/>
            {{inparam.seq}}
        </td>
        <td>
            <input type="hidden" name="description" value="{{inparam.description}}"/>
            {{inparam.description}}
        </td>
        <td>
            <%--<select name="inname">--%>
            <%--{{include 'inOptions' data}}--%>
            <%--</select>--%>
        </td>
    </tr>
    {{/each}}
</script>
<script id="backendinputitemtr" type="text/html">
    <tr>
        <td>
            <input type="hidden" name="id" value=""/>
            <input type="text" name="paramName" value="" onchange="changeEditFlag()"/>
        </td>
        <td>
            <select name="type" onchange="changeEditFlag()">
                <option value="string" selected="selected">String</option>
                <option value="int">Int</option>
                <option value="long">Long</option>
                <option value="double">Double</option>
                <option value="float">Float</option>
                <option value="boolean">Boolean</option>
            </select>
        </td>
        <td>
            <select name="paramType" onchange="changeEditFlag()">
                <option value="path">Parameter Path</option>
                <option value="head">Head</option>
                <option value="query">Query</option>
                <option value="body">Body</option>
            </select>
        </td>
        <td>
            <select name="required" onchange="changeEditFlag()">
                <option value="1" selected="selected">是</option>
                <option value="0">否</option>
            </select>
        </td>
        <td>
            <input type="text" name="seq" value="" onchange="changeEditFlag()"/>
        </td>
        <td>
            <input type="text" name="description" value="" onchange="changeEditFlag()"/>
        </td>
        <td><a onclick="register.forColumnDel(this)">删除</a></td>
    </tr>
</script>
<script id="inOptions" type="text/html">
    {{each inList as inp}}
    <option value="{{inp.name}}">{{inp.name}}</option>
    {{/each}}
</script>
<script id="grouplist2" type="text/html">
    <select id="groupSelect" class="form-control ue-form" onchange="register.loadSubServiceGroupList();" datatype="s" nullmsg="必填">
        <option value="">请选择分组</option>
        {{each data as group}}
        <option value="{{group.id}}">{{group.name}}</option>
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
<script id="apiList" type="text/html">
    <select id="apiSelect" class="form-control ue-form" onchange="register.loadApiDetail();" datatype="s" nullmsg="必填">
        <option value="">请选择服务</option>
        {{each data as api}}
        <option value="{{api.serviceId}}">{{api.serviceName}}</option>
        {{/each}}
    </select>
</script>
</body>
</html>