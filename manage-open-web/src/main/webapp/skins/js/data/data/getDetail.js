var exitNum=0;
var existColumn = [];
var dataSet=[];
var dataSourceId=0;
var dataSetSelect=[];
var dataSetSelectid=0
var allData=[];
var applyId = generateUUID();
var uploadData=[];
var tableRecord=[];
function submit() {
    wf.window.createAndSend(applyId);
    $("#submitbtn").css({"display":"none"});
}
$(function() {
    var search=location.search;
    $(".titleLeft").select2({
        allowClear: true,
        theme: "classic"
    });
    initServiceList();
    loadDataColumnsToSelect();
    parse(search);
})

$(document).on("click", "#returnColumns>li>label>input,#seleteAllColumnsBtn,#seleteRevsernColumnsBtn", function () {
    var resourceId =$("#resourceId").val();
    getOutParam();
    getFilterParam();
    // getSql();
    if($('input[name="checkbox-ReturnColumn"]:checked').length!==0) {
        $("#" + resourceId).prop("checked", true);
        // initSave();
        saveState();
    }
    else {
        $("#" + resourceId).prop("checked", false);
    }
});
$(document).on("change", "input[name=COLUMN_VALUEorNAME]", function () {
    var resourceId =$("#resourceId").val();
    getFilterParam();
    getOutParam();
    if(($('input[name=COLUMN_VALUEorNAME]').value)!=="") {
        $("#" + resourceId).prop("checked", true);
        // initSave();
        saveState();
    }
    else {
        $("#" + resourceId).prop("checked", false);
    }
});
$(document).on("click", "#tableNameList>li", tableLiClick);

$(document).on("change", ".titleLeft", function () {
    var setId = $(".titleLeft option:selected").attr("data-id");
    $("#setId").val(setId);
    initTableName();
    $("#serviceSql").empty();
    callTableData();
    $("#tableNameList>li").eq(0).click();


});
$(document).on("click", ".titleLeft", function () {
    recordTable();
    getallData();
});


//记录不同数据集所选的表
function recordTable(){
    $('input[name="COLUNM_LEFT_LIST"]:checked').each(function(){
        var resourceId = $(this).next().attr("data-resourceid");
        var setId = $(".titleLeft option:selected").attr("data-id");
        var data = {
            setId: setId,
            resourceId: resourceId,
        };
        tableRecord.push(data);
    });
}
//切换数据集时回显之前所选的数据
function callTableData(){
    var setId = $("#setId").val();
    if(tableRecord.length>0){
        console.log(dataSet);
        for (var i =0 ;i<dataSet.length ;i++){
            for ( var j =0 ; j<tableRecord.length;j++){
                if(setId==tableRecord[j].setId&&dataSet[i].resourceId==tableRecord[j].resourceId){
                    $("#" + dataSet[i].resourceId).prop("checked", true);
                }
            }

        }
    }
}
//生成一个随机数id
function generateUUID() {
    var d = new Date().getTime();
    if (window.performance && typeof window.performance.now === "function") {
        d += performance.now(); //use high-precision timer if available
    }
    var uuid = 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid;
}
function getallData() {
    var setId = $(".titleLeft option:selected").attr("data-id");
    var setName =$(".titleLeft option:selected").text();
    $('input[name="COLUNM_LEFT_LIST"]:checked').each(function(){
        var resourceId = $(this).next().attr("data-resourceid");
        var tableName =$(this).next().attr("data-text");
        if(allData.length>0) {
            var flag = 0;
            for (var i = 0; i < allData.length; i++) {
                if (setId == allData[i].setId && resourceId == allData[i].resourceId) {
                    uploadData.push(allData[i]);
                    flag =1;
                }
            }
            if(flag = 0){
                    var data = {
                        setName: setName,
                        setId: setId,
                        tableName: tableName,
                        resourceId: resourceId,
                    };
                    uploadData.push(data);
                }
        }else {
            var data = {
                setName: setName,
                setId: setId,
                tableName: tableName,
                resourceId: resourceId,
            };
            uploadData.push(data);
        }
    });
}

//保存功能
function nextstep(){
    getallData();
    var data ={
        allData: uploadData,
        comment:"药品研究所用数据资源",
        jdbcUser:"ceshi",
        applyId:applyId
    }
    $.ajax({
        type: "post",
        // url:'http://172.19.221.67:9071/manage-open/service/open/data/applyNew',
        // url: "http://172.19.221.67:7070/manage-open/service/open/data/applyNew",
        url: "http://172.16.12.95:7070/manage-open/service/open/data/applyNew",
        data: {
            json:JSON.stringify(data)
        },
        // dataType:"json",
        success: function(result) {
            if(result.result){
                sticky("保存成功");
            }
        }
    });
}
//获取传过来的参数
function parse(search){
    var str=search.substring(1);
    var strs=str.split("&");
    var keyvalue=strs[0].split("=");
    dataSetSelectid=keyvalue[1];
    for (var i=0;i<dataSetSelect.length;i++) {
        if(dataSetSelectid==dataSetSelect[i].remoteId){
            $(".titleLeft").val(dataSetSelectid);
            // $("#setName").val(dataSetSelect[i].name);
            // $("#setId").val(dataSetSelect[i].id);
        }
    }
    dataSourceId=strs[1];
    initTableName();
}


//顶部步骤蓝切换样式变化
$(document).on("click", "#nextBtn", function (){
    nextstep();
    $("#sjlb").css({"display":"none"});
    $(".showpage").css({"display":""});
    $(".firstStep").removeClass("activeStep");
    $(".thirdStep").addClass("activeStep");
    $(".firstStep").addClass("otherStep");
    $(".thirdStep").removeClass("otherStep");
});

//初始化服务列表,获取数据集
function initServiceList() {
    var param = {
        start: 0,
        limit:9999,
        dataTotal:true,
        groupId: "",
        name: ""
    };
    $.ajax({
        type: "post",
        url: context + "/service/open/data/getApplyList",
        data: param,
        async:false,
        dataType:"json",
        success: function(result) {
            dataSetSelect = result.data;
        }
    });
}
//初始化数据集选择框
function loadDataColumnsToSelect(){
    var selObj = $(".titleLeft");
    var num = dataSetSelect.length;
    var sel= "";
    for (var i=0;i<num;i++) {
        sel=sel+ "<option value='"+dataSetSelect[i].remoteId+"' data-id='"+dataSetSelect[i].id+"'>"+dataSetSelect[i].name+"</option>";
    }
    selObj.html(sel);
}
//获取表名
function initTableName() {
    var remoteId = ignoreSpaces( $(".titleLeft option:selected").val());
    var setid = $(".titleLeft option:selected").attr("data-id");
    $("#setName").val($(".titleLeft option:selected").text());
    $("#setId").val(setid);
    $.ajax({
        type: "post",
        async:false,
        url: context + "/service/open/data/getTableInfo?remoteId=" + ignoreSpaces(remoteId),
        success: function(data) {
            dataSet = eval(data.json);
            if(dataSet.length > 0) {
                var temp= template("tableTemp", {"tableList": dataSet});
                $("#tableNameList").empty().append(temp);
                $("#tableNameList>li:first").addClass("active");
                var resourceId =  $("#tableNameList>li:first").attr("data-resourceid");
                var tableName =  $("#tableNameList>li:first").attr("data-text");
                $("#resourceId").val(resourceId);
                $("#tableName").val(tableName);
                initTableCol(resourceId);
                // console.log("initTableName","dataSet");
                // console.log(dataSet);
                // initSave();
            }
            else {$("#tableNameList").empty()};
        }
    });
}
//默认保存页面
// function initSave(){
//     var setName = $("#setName").val();
//     var setId = $("#setId").val();
//     var tableName = $("#tableName").val();
//     var resourceId = $("#resourceId").val()
//     var data = {setName:setName,setId:setId,tableName:tableName,resourceId:resourceId};
//     for (var i=0 ; i<allData.length ;i++) {
//         if (setId == allData[i].setId && resourceId == allData[i].resourceId) {
//             return;
//         } else {
//             allData.push(data);
//         }
//     }
// }
// 切换表格
function tableLiClick() {
    $(this).addClass("active").siblings().removeClass("active");
    var resourceId = $(this).attr("data-resourceid");
    var tableName = $(this).attr("data-text");
    $("#tableName").val(tableName);
    $("#resourceId").val(resourceId);
    initTableCol(resourceId);
    callData(resourceId);
}
//回显数据
function callData(resourceId){
    var setId = $("#setId").val();
    $("#conditionColumns_tbody").empty();
    $("#serviceSql").empty();
    for (var i=0;i<allData.length;i++){
        if(setId==allData[i].setId&&resourceId==allData[i].resourceId) {
            var outputParamName = allData[i].outputParamName;
            // if(outputParamName!==undefined){
            var filterParam = allData[i].filterParam;
            filterParam = eval(filterParam);
            var sql = allData[i].sql;
            if (filterParam!==undefined) {
                for (var j = 0; j < filterParam.length; j++) {
                    addConditionRow();
                    var aa = filterParam[j].column;
                    var bb = ($("select[name='CONDITION_COLUMN_NAME']").find("option[value='" + aa + "']")).text();
                    $("select[name='CONDITION_COLUMN_NAME']").eq(j).val(filterParam[j].column);
                    $("input[name=CONDITION_COLUMN_TYPE]").eq(j).val(filterParam[j].type);
                    $("select[name='COLUMN_OPERATOR']").eq(j).val(filterParam[j].operation);
                    $("input[name=COLUMN_VALUEorNAME]").eq(j).val(filterParam[j].param);
                }
            }
            $("#serviceSql").html(sql);
            outputParamName = eval(outputParamName);
            for (var j=0;j<outputParamName.length;j++){
                console.log($("label[value='outputParamName[j]']"))
                // var selector =outputParamName[j];
                $("label[value='"+outputParamName[j]+"']").children().prop("checked",true);
            }
        }
    }
}
// $(document).on("click", "#tableNameList>li>input", function(){
//     $(this).prop('checked',false);
//     throw SyntaxError();
// });

function  saveState(){
    var setName = $("#setName").val();
    var setId = $("#setId").val();
    var tableName = $("#tableName").val();
    var resourceId =$("#resourceId").val();
    var filterParam = $("#filterParam").val();
    var outputParamName = $("#outputParam").val();
    var sql = $("#sql").val();
    // if(outputParamName!=="") {
    if (allData.length > 0) {
        var flag = 0 ;
        for (var i = 0; i<allData.length; i++) {
            if (setId == allData[i].setId && resourceId == allData[i].resourceId) {
                allData[i].outputParamName = outputParamName;
                allData[i].filterParam = filterParam;
                allData[i].sql = sql;
                flag = 1;
                break;
            }
        }
            if(flag == 0){
                var data = {
                    setName: setName,
                    setId: setId,
                    tableName: tableName,
                    resourceId: resourceId,
                    outputParamName: outputParamName,
                    filterParam: filterParam,
                    sql: sql
                };
                allData.push(data);
            }

    }
    else if(allData.length==0) {
        var data = {
            setName: setName,
            setId: setId,
            tableName: tableName,
            resourceId: resourceId,
            outputParamName: outputParamName,
            filterParam: filterParam,
            sql: sql
        };
        allData.push(data);
    }
    $("#" + resourceId).prop("checked", true);
}
function initTableCol(resourceId) {
    $.ajax({
        type: "post",
        async:false,
        url: context + "/service/open/data/getItemsByRI?resourceId=" + ignoreSpaces(resourceId),
        success: function (data) {
            var dataSet = eval("(" + data.json + ")");
            if (dataSet.recordSet.length > 0) {
                existColumn=dataSet.recordSet;
                // console.log("initTableCol","existColumn");
                console.log(existColumn)
                loadExistedDataItemsOzone();
            }
        }
    });
    return 1;
}

/**
 * 增加过滤参数
 */

function addConditionRow(){
    exitNum++;
    var _row = "<tr role='row' class='data_service service-condition'>";
    var row_ ="</tr>";
    var tdcs = "<td class=' center'>";
    var tdfs = "<td class=''>";
    var tde = "</td>";
    //复选框
    var checkbox = "<input type='checkbox' name='checkbox-condition' class='select-condition-all-member' value='"+exitNum+"'>"+
        "<input type='hidden' id='condition-itemId"+exitNum+"' value='"+exitNum+"'></input>";
    //参数名称
    var column_name="<select class='form-control' onchange='changeConditionColumnNameAction("+exitNum+")' id='CONDITION_COLUMN_NAME"+exitNum+"' name='CONDITION_COLUMN_NAME'></select>";
    // 参数类型
    var column_type="<input type='text' name='CONDITION_COLUMN_TYPE' readonly id='CONDITION_COLUMN_TYPE"+exitNum+"' value='' class='form-control'></input>";
    //运算符
    var option= "<option value='=' selected>=</option><option value='>'>></option><option value='>='>>=</option>" +
        "<option value='<'><</option><option value='<='><=</option><option value='in'>in</option><option value='!='>!=</option><option value='like'>like</option><option value='not like'>not like</option>";
    var column_operator="<select  class='form-control' id='COLUMN_OPERATOR"+exitNum+"' name='COLUMN_OPERATOR'>" + option+"</select>";
    //值、参数名称
    var column_valueOrName ="<input type='text' placeholder='参数值' readonly  onChange=' checkConditionColumnOrValue("+exitNum+
        ");' id='COLUMN_VALUEorNAME"+exitNum+"'  name='COLUMN_VALUEorNAME' value='' class='form-control'></input><label id='COLUMN_VALUEorNAMEError"+exitNum+"' class=' Validformwrong Validformchecktip' style='display:none;'></label>";

    //组装结果
    var row	= _row
        +tdcs + checkbox + tde
        +tdfs + column_name + tde
        +tdfs + column_type + tde
        +tdfs + column_operator + tde
        +tdfs + column_valueOrName + tde
        + row_;
    //显示
    $("#conditionColumns_tbody").append(row);
    changeColour();
    $("#COLUMN_OPERATOR"+exitNum).select2({
        allowClear: true,
        theme: "classic"
    });
    loadFilterColumnsToSelect();
    $("#CONDITION_COLUMN_NAME"+exitNum).select2({
        allowClear: true,
        theme: "classic"
    });
}
/**
 * 删除过滤参数行
 */
function deleteConditionRow(){
    var count = $("input[name='checkbox-condition']:checked").length;
    if(count>0){
        $.dialog({
            type: 'confirm',
            content: '确认删除?',
            autofocus: true,
            ok: function() {
                $("input[name='checkbox-condition']:checked").each(function(){
                    delRow(this);
                });
                $(".select-condition-all").attr("checked",false);
                changeColour();
            },
            cancel: function(){
            }
        });
    }else{
        sticky("提示:请至少选择一行！");
        // $(".select-condition-all").attr("checked",false);
        return false;
    }};
function delRow(i) {
    $(i).parent().parent().remove();
}
/**更新弹窗提示*/
function sticky(msg, style, position) {
    var type = style ? style : 'success';
    var place = position ? position : 'top';
    $.sticky(
        msg,
        {
            autoclose : 2000,
            position : place,
            style : type
        }
    );
}


function changeColour(){
    $("#inputColumns_tbody").find("tr:even").css("background-color","#FFFFFF");
    $("#inputColumns_tbody").find("tr:odd").css("background-color", "#FFF5EE");
    $("#conditionColumns_tbody").find("tr:even").css("background-color","#FFFFFF");
    $("#conditionColumns_tbody").find("tr:odd").css("background-color", "#FFF5EE");
    $("#orderColumns_tbody").find("tr:even").css("background-color","#FFFFFF");
    $("#orderColumns_tbody").find("tr:odd").css("background-color", "#FFF5EE");
}
//初始化过滤参数select选择框
function loadFilterColumnsToSelect(){
    var selObj = $("#CONDITION_COLUMN_NAME"+exitNum);
    var num = existColumn.length;
    var sel="<option  value='-1' selected >请选择参数名称</option>";
    for (var i=0;i<num;i++) {
        var desc=existColumn[i].data.columnDescription==" "||existColumn[i].data.columnDescription==""?existColumn[i].data.columnName:existColumn[i].data.columnDescription;
        sel=sel+ "<option value='"+existColumn[i].data.columnName+"'>"+desc+"</option>";
    }
    selObj.html(sel);
}

/**
 * 改变过滤参数列名称时，自动填充相关信息
 */
function changeConditionColumnNameAction(num){
    var colName = $("#CONDITION_COLUMN_NAME"+num+" option:selected").val();
    if(colName!=="-1"){
        var obj;
        for(var i=0;i<existColumn.length;i++){// 查找对应的Record
            if(existColumn[i].data.columnName===colName){
                obj=existColumn[i];
                break;
            }
        }
        $("#condition-itemId"+num).val(obj.data.itemId);
        $("#CONDITION_COLUMN_TYPE"+num).val(obj.data.columnType);
        $("#COLUMN_VALUEorNAME"+num).val("");
        // 变更元素信息
        $("#COLUMN_VALUEorNAME"+num).removeAttr("readonly");
    }else{
        $("#COLUMN_VALUEorNAME"+num).val("");
        $("#CONDITION_COLUMN_TYPE"+num).val("");
    }
}

// 加载数据列
function loadExistedDataItemsOzone(){
    var ulObj = $("#returnColumns");
    ulObj.html("");
    var liReturnColumns="";
    // existColumn=data;
    var num = existColumn.length;
    for (var i=0;i<num;i++) {
        var obj = existColumn[i].data;
        addInputRowOzone(obj);
        liReturnColumns = liReturnColumns + "<li style='float: left;height: 35px;'><label value='"+obj.columnName+"'>&nbsp; &nbsp;<input type='checkbox'  " +
            "name='checkbox-ReturnColumn' class='select-ReturnColumn-all-member' " +
            "value=' " + obj.itemId + " '>&nbsp;" + obj.columnDescription + "</label></li>"
    }
    var ulObj = $("#returnColumns");
    ulObj.html(liReturnColumns);
}

function addInputRowOzone(obj){
    exitNum++;
    var _row = "<tr role='row' class='data_service input-column'>";
    var row_ ="</tr>";
    var tdcs = "<td class=' center'>";
    var tdfs = "<td class=''>";
    var tde = "</td>";

    //复选框
    var checkbox = "--";
    //字段描述
    var column_desc="<input type='text' name='INPUT_COLUMN_DESCRIPTION' id='INPUT_COLUMN_DESCRIPTION' value ='" + obj.columnDescription +"'"+
        "readonly class='form-control' maxlength='16' placeholder='字段描述'></input>";
    //输入名称
    var inputName="<input type='text' name='INPUT_NAME' id='INPUT_NAME' value='' placeholder='英文字符串'"+
        "class='form-control' maxlength='16' onblur='checkInputNameOzone(this)'></input><label id='INPUT_NAMEError' name='INPUT_NAMEError' class=' Validformwrong Validformchecktip' style='display:none;'>请输入英文字符串</label>";
    //参数名称
    var column_name="<input type='text' name='INPUT_COLUMN_NAME' id='INPUT_COLUMN_NAME' value ='" + obj.columnName +"'"+
        "readonly class='form-control' maxlength='16' placeholder='字段描述'></input>";
    // 参数类型
    var column_type="<input type='text' name='INPUT_COLUMN' readonly id='INPUT_COLUMN_TYPE' value='String' class='form-control'></input>";
    // 操作符
    var column_operator="<input type='text' name='INPUT_COLUMN_OPERATOR' readonly id='INPUT_COLUMN_OPERATOR' value='=' class='form-control'></input>";
    //是否必需
    var is_null = "--";
    //组装结果
    var row	= _row
        +tdcs + checkbox + tde
        +tdfs + column_name + tde
        +tdfs + column_type + tde
        +tdfs + column_operator + tde
        +tdcs + is_null + tde
        +tdfs + inputName + tde
        +tdfs + column_desc + tde
        + row_;
    //显示
    $("#inputColumns_tbodyOzone").append(row);
    changeColour();
    loadInputColumnsToSelect();
    $("#INPUT_COLUMN_NAME"+exitNum).select2({
        allowClear: true,
        theme: "classic"
    });
    $("#INPUT_COLUMN_OPERATOR"+exitNum).select2({
        allowClear: true,
        theme: "classic"
    });
}
//初始化输入参数 select选择框
function loadInputColumnsToSelect(){
    var selObj = $("#INPUT_COLUMN_NAME"+exitNum);
    var num = existColumn.length;
    var sel="<option  value='-1' selected >请选择参数名称</option>";
    for (var i=0;i<num;i++) {
        var desc=existColumn[i].columnDescription==" "||existColumn[i].columnDescription==""?existColumn[i].columnName:existColumn[i].columnDescription;
        sel=sel+ "<option value='"+existColumn[i].columnName+"'>"+desc+"</option>";
    }
    selObj.html(sel);
}
/**
 * 全选
 */
function seleteAllColumns(){
    $('.select-ReturnColumn-all-member').prop('checked', true);
}
/**
 * 反选
 */
function seleteRevsernColumns(){
    $('.select-ReturnColumn-all-member').each(function(){
        if (this.checked) {
            this.checked = false;
        }
        else {
            this.checked = true;
        }
    });
}

/**
 *生成sql
 */
function getSql(){
    var isSql =false;
    if(getInputParam()&&getFilterParam()&&judgeSql()){
        var filterParam =  encodeURI(encodeURI($("#filterParam").val()));
        var outputParamName = $("#outputParam").val();
        // var outputParam = encodeURI(encodeURI(JSON.parse($("#outputParam").val())));
        var outputParam = encodeURI(encodeURI(JSON.parse(JSON.stringify("[]"))));
        var orderParam = encodeURI(encodeURI(JSON.parse(JSON.stringify("[]"))));
        var tableName = encodeURI(encodeURI(JSON.parse(JSON.stringify(ignoreSpaces($("#tableName").val())))));
        var sourceId = encodeURI(encodeURI(JSON.parse(JSON.stringify(ignoreSpaces((dataSourceId))))));

        // var data = {sourceId:sourceId,filterParam:filterParam,outputParam:outputParam,tableName:tableName,orderParam:orderParam};
        var data = {filterParam:filterParam,outputParam:outputParam,tableName:tableName,orderParam:orderParam};

        //进行请求
        $.ajax({
            async: false,
            // url:  "http://172.19.221.67:7070/odmgr/command/dispatcher/com.inspur.od.dataService.servicePublish.cmd.ServicePublishDispatcherCmd/getSqlByOutputAndFilterParams",
            url:  "http://172.16.12.95:7070/odmgr/command/dispatcher/com.inspur.od.dataService.servicePublish.cmd.ServicePublishDispatcherCmd/getSqlByOutputAndFilterParams",
            type: "POST",
            data: data,
            success: function(data) {
                var sql =[];
                var a = data.split(" ");
                var index = data.indexOf("select");
                var right = data.substring(index+6)
                sql = a[0]+'\xa0'+JSON.parse(outputParamName) +right;
                $("#serviceSql").html(sql);
                var sqlstatement =$("#serviceSql").val();
                $("#sql").val();
                $("#sql").val(sqlstatement);
                isSql=true;
            }
        });
    }
    saveState();
    return isSql;
}
/**
 *获取输入参数
 */

function getInputParam(){
    //进行组装固定条件
    var temp =true;
    var params=[];
    $("#inputParam").val(encode(params));
    return temp;
}
function judgeSql(){
    if(!judgeReturn()){
        return false;
    }
    return true;
}
function encode(s){
    if (/["\\\x00-\x1f]/.test(s)) {
        return '"' + s.replace(/([\x00-\x1f\\"])/g, function(a, b) {
            var c = m[b];
            if(c){
                return c;
            }
            c = b.charCodeAt();
            return "\\u00" +
                Math.floor(c / 16).toString(16) +
                (c % 16).toString(16);
        }) + '"';
    }
    return '"' + s + '"';
};

/**
 *获取过滤参数
 */
function getFilterParam(){
    //进行组装过滤条件
    var params=[];
    var temp =true;
    var temp1=true;
    $("#conditionColumns_tbody tr").each(function(){
        var selectArray=$("select",this);
        var inputArray=$("input[name='COLUMN_VALUEorNAME']",this);
        var typeArray=$("input[name='CONDITION_COLUMN_TYPE']",this);
        var num =inputArray[0].id.substring(18,inputArray[0].id.length);
        if(!checkConditionColumnOrValue(num)){
            temp=false;
            return false ;
        }
        if(selectArray[0].value!=="-1"){
            var trArr ={}; //存行数据
            trArr.logic="and",
                trArr.column= selectArray[0].value;
            trArr.operation= selectArray[1].value;
            trArr.param= inputArray[0].value ;
            if(judgeArray(typeArray[0].value,numArray)){
                trArr.type	= "number";
            }else if(judgeArray(typeArray[0].value,timeArray)){
                trArr.type	= "time";
            }
            else{
                trArr.type	= "string";
            }
            params.push(trArr);
        }
    });
    $("#filterParam").val(JSON.stringify(params));
    return temp&&temp1;
}

function checkConditionColumnOrValue(num){
    var obj = $("#COLUMN_VALUEorNAME"+num);
    obj.val(ignoreSpaces(obj.val()));
    var context = obj.val();
    // if (G3.isEmpty(context)) {
    if (context.isEmpty){
        obj.css("border-color", "red");
        $("#COLUMN_VALUEorNAMEError"+num).text("请填写过滤参数值");
        $("#COLUMN_VALUEorNAMEError"+num).show();
        validationValue=false;
        return false;
    }
    var re =/^[\u4e00-\u9fa5a-zA-Z0-9'%!\]\[_,.@\/]+$/;
    if (!re.test(context)) {
        $("#COLUMN_VALUEorNAMEError"+num).text("过滤参数值不符合要求！");
        $("#COLUMN_VALUEorNAMEError"+num).show();
        obj.css("border-color", "red");
        validationValue=false;
        return false;
    }
    obj.removeAttr("style");
    $("#COLUMN_VALUEorNAMEError"+num).hide();
    validationValue=true;
    return true;
}

/**
 * 删除所有空格
 */
function ignoreSpaces(string) {
    var temp = "";
    string = '' + string;
    var splitstring = string.split(" ");
    for(var i = 0; i < splitstring.length; i++)
        temp += splitstring[i];
    return temp;
}
var timeArray =["date","timestamp(6)","smalldatetime","SMALLDATETIME","timestamp","time","DATE","DATETIME","TIMESTAMP","TIME","TIMESTAMP(6)"];//时间数组
var numArray = ["int","float","doubble","number","NUMBER","DOUBBLE","NUMERIC","Integer","INT","FLOAT","numeric"];//数字数组
function judgeArray(string,array){
    var judgeString=false;
    string = string.replace( /^\s+|\s+$/g, "" );

    for(var i=0;i<array.length;i++){
        if(string === array[i]){
            judgeString=true;
            break;
        }
    }
    return judgeString;
}
function judgeReturn(){
    var temp =true;
    if($(".select-ReturnColumn-all-member:checked").length===0){
        sticky("提示:至少一个返回参数！");
        $("#serviceSql").html("");
        temp= false;
    }
    getOutParam();
    return temp;
}
/**
 *获取输出参数
 */
function getOutParam() {
    var output = [];
    var isHas = false;
    $('input[name="checkbox-ReturnColumn"]:checked').each(function(){
        var obj = $(this).parent().attr("value");
        output.push(obj);
        isHas = true;
    });
    $("#outputParam").val(JSON.stringify(output));
    return isHas;
}
