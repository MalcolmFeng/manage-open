<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%
    String resourceId = request.getParameter("resourceId");
    String dataSourceType = request.getParameter("dataSourceType");
%>
<html lang="en" style="height:100%">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>开放平台-首页</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>

    <script type="text/javascript" src="<l:asset path='jquery.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='form.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='arttemplate.js'/>" ></script>
    <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='knockout.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='data/data/getDetail.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='data/data/select2.min.js'/>" ></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="<l:asset path='html5shiv.js'/>"></script>
    <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
    <script type="text/javascript">
        //项目上下文
        var context = "<%=request.getContextPath()%>";
        //获取静态资源上下文路径
        var assetPath = "<l:assetcontext/>";

    </script>
</head>

</head>
<style>
    #sjlb{
        font-family:Microsoft YaHei;
        color:#333;
        font-size: 14px;
    }
    .navLeft{
        width: 15%;
        /*height: 100%;*/
        margin-top: 15px;
        margin-left: 25px;
        border-right: 1px solid #EEE;
        margin-bottom: 20px;
    }
    .contentRight{
        width: 79%;
        height: 100%;
        margin-left: 20px;
    }
    .titleLeft{
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 10px;
        border:none;
        font-weight:bold;
        width: 150px;
    }
    #tableNameList{
        height: 30px;
        line-height: 30px;
    }
    #tableNameList>li{
        padding-left: 30px;
        margin-left: -30px;
    }
    #tableNameList>li.active {
        background-color: RGBA(187,220,253,0.4);
        cursor: pointer;
    }
    .content{
        margin-top: 50px;
    }
    .hr_tall{
        width: 30%;
        margin-bottom: -20px;
    }
    .hr_tall>span{
        font-weight: bold;
        margin-left: 5px;

    }
    .lineBlue {
        width: 4px;
        height: 12px;
        background-color:#4094FB;
        display: inline-block;
        margin-top: 2px;
    }
    .pull-right{
        margin-bottom: 2px;
    }
    .smallLine{
        width: 2px;
        height: 12px;
        background-color:#4094FB;
        display: inline-block;
        margin-top: 2px;
    }
    #getSqlBtn{
        margin-left: -20px;
    }
    .select2-container--classic{
        display: none;
    }

    #tableNameList>input{
        float: left;
        margin-top:8px;
    }
    .buttonGroup{
        margin-top: 30px;
        margin-left: -80px;
    }
    #nextBtn{
        width:80px;
        height:36px;
        background-color: #4094FB;
        border: none;
        border-radius:4px;
        color: #fff;

    }
    #backBtn{
        margin-left: 20px;
        width:80px;
        height:36px;
        border: none;
        background-color: #DDDDDD;
        color:#333 ;
        border-radius:4px;
    }
    #saveBtn{
        width:80px;
        height:36px;
        background-color: #4094FB;
        border: none;
        border-radius:4px;
        color: #fff;
        margin-left: 20px;
    }
    li{
        cursor: pointer;
    }
    .lastStep{
        width: 100%;
        height: 500px;
    }
    .showpage{
        height: 1000px;
        overflow-x: hidden;
        overflow: hidden;
    }
   .thirdbtn{
       position: absolute;
       left: 50%;
       width:80px;
       height:36px;
       margin-left: -40px;
   }
   .topTitle{
       position: relative;
   }
    #submitbtn{
        position: fixed;
        top: 90%;
        left: 50%;
        width:80px;
        height:36px;
        margin-left: -40px;
    }
    .content{
        margin-top: -8px;
    }
    .returnDiv{
        margin-top: 23px;
    }

</style>
<body style="width: 100%;height: 100%;">
    <div class="topTitle" style="background-color: rgba(248,248,248,1); width: 97%;border: 1px solid rgba(238,238,238,1);height: 100px;margin: 0 auto;margin-top: 30px;display: flex;justify-content: center;align-items: center;">
        <div style="display: flex;flex-direction: column;align-items: center;">
            <div class="firstStep" style="height: 40px;width: 40px;border-radius: 20px;background-color: rgba(64,148,251,1);color: #FFFFFF;text-align: center;line-height: 40px;;">01</div>
            <div>选择数据集</div>
        </div>
        <div style="width:37%;height: 2px;border: 1px solid rgba(238,238,238,1);background-color: rgba(238,238,238,1);"></div>

        <div style="display: flex;flex-direction: column;align-items: center;">
            <div class="secondStep" style="height: 40px;width: 40px;border-radius: 20px;background-color: rgba(238,238,238,1);color: rgba(102,102,102,1);text-align: center;line-height: 40px;;">02</div>
            <div>已选择数据集预览</div>
        </div>
        <div style="width:37%;height: 2px;border: 1px solid rgba(238,238,238,1);background-color: rgba(238,238,238,1);"></div>
        <div class="thirdStep" style="display: flex;flex-direction: column;align-items: center;">
            <div style="height: 40px;width: 40px;border-radius: 20px;background-color: rgba(238,238,238,1);color: rgba(102,102,102,1);text-align: center;line-height: 40px;;">03</div>
            <div>填写表单</div>
        </div>
    </div>
    <div id="sjlb" style="width: 100%;display: flex;flex-direction: row;">
    <div class="navLeft">
        <select class="titleLeft">数据列表</select>
        <ul id="tableNameList">
            <li>无表格</li>
        </ul>
    </div>
    <div class="contentRight">
        <input type="hidden" name="inputParam" id="inputParam"/>
        <input type="hidden" name="outputParam" id="outputParam"/>
        <input type="hidden" name="filterParam" id="filterParam"/>
        <input type="hidden" name="orderParam" id="orderParam" value=[]>
        <input type="hidden" name="setName" id="setName"/>
        <input type="hidden" name="setId" id="setId"/>
        <input type="hidden" name="sql" id="sql"/>
        <input type="hidden" id="tableName" name="tableName" ></input>
        <input type="hidden" id="resourceId" >

        <div class="form-hr">
            <div class="shown"><label id="resourceName" data-bind="text:resourceName"> </label></div>
            <div class="fieldInput">
            <div class="clear"></div>
        </div>
        <div class="form-hr content ">
            <div class="fieldLabel hr_tall"><div class="lineBlue "></div><span>服务逻辑</span>

            </div>
            <div class="fieldInput">
                <div id="conditionColumnsDiv">
                    <div class="pull-right">
                        <button class="btn btn-link" type="button" id="addConditionRowBtn" onclick="addConditionRow()"> 增加</button>
                        <div class="smallLine"></div>
                        <button class="btn btn-link"  id="deleteConditionRowBtn" type="button" onclick="deleteConditionRow()"> 删除 </button>
                    </div>
                    <table class="table table-hover table-bordered" id="conditionColumnsTable">
                        <thead>
                        <tr role="row">
                            <th style="width: 5%;" ><input type="checkbox" class="select-condition-all"></th>
                            <th  style="width: 30%;"  >字段名称</th>
                            <th  style="width: 15%;" >字段类型</th>
                            <th  style="width: 15%;" >运算符</th>
                            <th  style="width: 35%;" ><div class="Tipchecktip Tipwrong">*值(like,not like自定通配符)</div></th>
                        </tr>
                        </thead>
                        <tbody id="conditionColumns_tbody">
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="clear"></div>
        </div>
        <div class="form-hr ">
            <div class="fieldLabel hr_tall "><div class="lineBlue "></div><span>返回字段</span>
                <button class="btn btn-link" id="seleteAllColumnsBtn" type="button" onclick="seleteAllColumns()"> <i class="fa fa-check-square fa-fw"></i>全选 </button>
                <button class="btn btn-link"  id="seleteRevsernColumnsBtn" type="button" onclick="seleteRevsernColumns()"> <i class="fa fa-check-square-o fa-fw"></i>反选 </button>
            </div>
            <div class="fieldInput returnDiv">
                <div id="returnColumnsDiv">
                    <div style="margin-top: -20px;">
                        <ul class="returnColumns clearfix" id="returnColumns">
                        </ul>
                    </div>
                </div>
            </div>
<%--            <div class="clear"  style="margin-bottom: 20px;"></div>--%>
        </div>
        <div class="form-hr">
            <div class="fieldLabel "><button class="btn btn-link" type="button" id="getSqlBtn" onclick="getSql()" style="margin-left:6px"> <i class="fa fa-hand-o-right fa-fw"></i>生成SQL </button></div>
            <div class="fieldInput  fieldInputsql"><textarea id="serviceSql" readonly
                                              style="resize: none; word-wrap: break-word; margin-left: 0px; margin-right: 0px; width: 100%; height: 100px;"
                                              class="form-control"></textarea></div>
            <div class="clear"></div>
        </div>
            <div class="buttonGroup" style="width: 100%;display: flex;justify-content: center;">
            <button type="button" class="btnicon" id="nextBtn" >下一步</button>
            <%--        <button type="button" class="btnicon" id="saveBtn" onclick="nextstep()">保存</button>--%>
            <button type="button" class="btnicon" id="backBtn" onclick="history.back();">返回</button>
        </div>
    </div>
</div>

</div>
<div class="body showpage" style="display: none" >
    <iframe id="wf" name="wf" style="width: 100%;height: 100%;border: none;"></iframe>
    <div class="row">
        <div class="col-md-12" class="thirdbtn">
            <button class="btn ue-btn "  id="submitbtn" onclick="submit()">提交</button>
<%--            <button type="button" class="btnicon" id="saveBtn" onclick="nextstep()">保存</button>--%>
        </div>
    </div>
</div>
<script type="text/javascript">
    var context = "<l:assetcontext/>";
    $(function () {
        $("#wf").attr("src","http://172.16.12.95:7070/open-bsp/command/dispatcher/org.loushang.workflow.tasklist.forward.TaskListDispatcherCmd/newTaskForward?procDefUniqueId=2c935dc36e67aeaf016e680d3aa80979")
    })

</script>
<!--表-->
<script type="text/html" id="tableTemp">
    {{each tableList as table}}
    <input type="checkbox" id="{{table.resourceId}}" name="COLUNM_LEFT_LIST" > <li data-resourceId='{{table.resourceId}}' data-text="{{table.tableName}}"> &nbsp;&nbsp;{{table.resourceName}}</li>
    {{/each}}
</script>
</body>
</html>