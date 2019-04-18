var register = {
    loadServiceGroupList: function (groupId) {
        $.ajax({
            type: "post",
            url: context + "/service/dev/group/listNoPage",
            data: JSON.stringify({}),
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                var html = template("grouplist2", data);
                $("#groupDiv").empty().append(html);
                $("#groupSelect").val(groupId);
                // if (groupId != null && groupId != '') {
                //     register.setGroupId(groupId);
                // }
            }
        });
    },
    loadSubServiceGroupList: function (parentId) {
        // 清空子分组列表
        $("#subgrouplistDiv").empty();
        var groupId = $("#groupSelect option:selected").val();//获取父组
        $("#groupId").val(groupId);
    },

    selectServiceGroup: function (obj) {
        $("#groupId").val($("#subgroupSelect").val());
    },

    setNeedAuth: function (needAuth, authUser) {
        if (needAuth != null && needAuth != '') {
            if (needAuth == '0') {
                $("input[name=needAuth][value=0]").prop("checked", true);
            } else {
                if (authUser == 'platform' || authUser == 'PLATFORM') {
                    $("input[name=needAuth][value=1]").prop("checked", true);
                } else {
                    $("input[name=needAuth][value=2]").prop("checked", true);
                }
            }
        }
    },

    setGroupId: function (groupId) {
        if (groupId != null && groupId != '') {
            $.ajax({
                type: "get",
                url: context + "/service/service/group/get/" + groupId,
                success: function (data) {
                    if (data != null && data.parentId != null) {
                        if ("-1" == data.parentId) {
                            $("#groupSelect").val(groupId);
                        } else {
                            $("#groupSelect").val(data.parentId);
                            register.loadSubServiceGroupList(data.parentId);
                        }
                    }
                }
            });
        }
    },
    loadApiList: function () {
        $.ajax({
            type: "get",
            url: context + "/service/open/api/apiList",
            dataType: "json",
            success: function (data) {
                var html = template("apiList", data);
                $("#apiListDiv").empty().append(html);
                if (serviceId) {//编辑
                    $("#apiSelect").val($("#remoteId").val());
                    register.loadApiDetail(serviceId);
                }
            }
        });
    },
    loadApiDetail: function (apiId) {
        // if (!apiId) {
        //     apiId = $("#apiSelect option:selected").val();//获取api
        // }
        // $("input[name=groupId]").val(groupId);
        if (apiId != null && apiId != '') {
            $.ajax({
                type: "get",
                url: context + "/service/open/api/apiDetail/" + apiId,
                dataType: "json",
                success: function (data) {
                    // $("input[name='serviceProtocol'][value='"+(data.requestType).toLowerCase()+"']").attr("checked",true);
                    $("#serviceAddr").val(data.requestAddr);
                    $("#returnSample").val(data.returnSample);
                    $("#serviceHttpMethod").val("get").attr("disabled","disabled");//默认get请求
                    $("#remoteId").val(apiId);
                    //初始化后端参数
                    register.addBackendInputParam(data.inputParam);
                }
            });
        }
    },
    selectProxyType: function (proxyType) {
        var html = '';
        if (proxyType == 'webservice') {
            html = template("webservicelist");
        } else if (proxyType == 'netty') {
            html = template("nettylist");
        } else {
            html = template("httplist");
        }
        $("#addresslist").empty().append(html);
    },

    addHttpService: function () {
        $("#httptable").append(template('httpitem'));
    },

    addWebService: function () {
        $("#webservicetable").append(template('webserviceitem'));
    },

    addNettyService: function () {
        $("#nettytable").append(template('nettyitem'));
    },

    addInputParam: function () {
        $("#inputtable").append(template('inputitem'));
    },
    addInputParamList: function () {
        var endList = register.getBackendParamList();
        //判断编辑还是新增
        if(register.checkNeedLoadEdit(endList)){
            var data={data:endList}
            $("#inputtabletbody").html("").append(template('inputitemlist',data));
        }
    },
    //判断是否需要加载
    checkNeedLoadEdit: function (endList) {
        //远程id变化要加载最新的后端数据
        if(initRemoteId!=$("#remoteId").val()){
            return true;
        }
        if(endList.length!=initInputList.length){
            return true;
        }
        return editFlag;
    },
    addbackendInputParamTr: function () {
        $("#backendinputtbody").append(template('backendinputitemtr'));
    },
    addOutParamTr: function () {
        $("#outbody").append(template('outputitem'));
    },
    addBackendInputParam: function (inputParam) {
        // var arry = [];
        // $("#inputtable tr").each(function (index) {
        //     if (index > 0) {
        //         var data = {};
        //         var paramName = $(this).find("input[name='inputParamName']").val();
        //         data.name = paramName;
        //         arry.push(data)
        //     }
        // });
        // inputParam["inList"] = arry;
        var data = {data: inputParam};
        $("#backendinputtbody").html("").append(template('backendinputitem', data));
    },
    addOutputParam: function () {
        $("#outputtable").append(template('outputitem'));
    },

    forColumnDel: function (obj) {
        var rowCount = $(obj).closest('table').find('tr').length;
        if (rowCount > 1) {
            $(obj).closest("tr").remove();
        } else {
            $(obj).closest("tr").find("input").val('');
        }
    },
    forSave: function () {
        var serviceInfo = register.getServiceInfo();
        var url = context + "/service/open/api/add";
        $.ajax({
            type: 'post',
            url: url,
            data: JSON.stringify(serviceInfo),
            contentType: 'application/json;charset=UTF-8',
            success: function (resp) {
                if (resp.result == true) {
                    $.dialog({
                        autofocus: true,
                        type: "alert",
                        content: "成功!"
                    });
                    setTimeout(function () {
                        window.location.href = context + "/service/open/api/getApiPage";
                    }, 1000);
                } else {
                    $.dialog({
                        autofocus: true,
                        type: "alert",
                        content: "失败!" + resp.message
                    });
                }
            }
        });
    },

    getServiceInfo: function () {
        var serviceInfo = {};

        serviceInfo.id = $("#id").val();
        serviceInfo.remoteId = $("#remoteId").val();
        serviceInfo.name = $("#name").val();
        serviceInfo.apiGroup = $("#groupId").val();
        serviceInfo.description = $("#description").val();
        serviceInfo.authType = $('input[name=authType]:checked').val();//授权方式

        // serviceInfo.protocol = $('input[name=protocol]:checked').val();//请求协议
        serviceInfo.protocol = "http";//请求协议
        serviceInfo.reqPath = $('#reqPath').val();//请求path
        serviceInfo.httpMethod = $('#httpMethod').val();//httpMethod


        // serviceInfo.scProtocol = $('input[name=serviceProtocol]:checked').val();//后端请求协议
        serviceInfo.scProtocol = "http";//后端请求协议
        serviceInfo.scAddr = $('#serviceAddr').val();//后端服务地址
        serviceInfo.scHttpMethod = $('#serviceHttpMethod').val();//后端httpMethod
        serviceInfo.contentType = $('#responseContentType').val();//返回ContentType
        serviceInfo.returnSample = $('#returnSample').val();

        serviceInfo.inputList = register.initInputParam();
        serviceInfo.outputList = register.getOutputParamList($("#id").val());


        return serviceInfo;
    },
    initInputParam: function () {
        var inList = register.getInputParamList();
        var endList = register.getBackendParamList();
        var inputParam = [];
        // if (inList.length == 0) { //如果没有请求参数直接使用后端参数
        //     for (var i in endList) {
        //         var param = {
        //             name: endList[i].name,
        //             type: endList[i].type,
        //             required: endList[i].required,
        //             description: endList[i].description,
        //             scName: endList[i].name,
        //             scType: endList[i].type,
        //             scRequired: endList[i].required,
        //             scDescription: endList[i].description,
        //             scSeq: endList[i].seq
        //         };
        //         inputParam.push(param);
        //     }
        //     return;
        // }
        for (var i in inList) {
            var backname = inList[i].backname;
            var param = {
                name: inList[i].name,
                type: inList[i].type,
                required: new Number(inList[i].required),
                description: inList[i].description
            };
            for (var j in endList) {
                if (backname == endList[j].name) {
                        param.scName = endList[j].name,
                        param.scType = endList[j].type,
                        param.scRequired = new Number(endList[j].required),
                        param.scDescription = endList[j].description,
                        param.scSeq = new Number(endList[j].seq),
                        param.scParamType = endList[j].paramType
                }
            }
            inputParam.push(param);
        }
        return inputParam;
    },
    getInputParamList: function () {
        var inputParam = [];
        $("#inputtable tr:gt(0)").each(function (i, e) {
            var name = $(this).find("input[name=inputParamName]").val();
            if (name != null && name != '') {
                var param = {
                    id: $(this).find("input[name=id]").val(),
                    name: $(this).find('input[name=inputParamName]').val(),
                    // type: $(this).find('select[name=inputParamType] option:selected').val(),
                    type: $(this).find('input[name=inputParamType]').val(),
                    required: $(this).find('select[name=inputRequired] option:selected').val(),
                    description: $(this).find('input[name=inputDescription]').val(),
                    backname:$(this).find('input[name=backname]').val()
                };
                inputParam.push(param);
            }
        });
        return inputParam;
    },
    getBackendParamList: function () {
        var endParam = [];
        $("#backendinputtable tr:gt(0)").each(function (i, e) {
            var name = $(this).find("input[name=paramName]").val();
            if (name != null && name != '') {
                if($("#remoteId").val()){//远程接口数据
                    var param = {
                        name: name,
                        type:$(this).find("input[name=type]").val(),
                        required: $(this).find("input[name=required]").val(),
                        seq: $(this).find("input[name=seq]").val(),
                        description: $(this).find("input[name=description]").val(),
                        paramType: $(this).find('select[name=paramType] option:selected').val()
                    };
                    endParam.push(param);
                }else{//手动添加
                    var param = {
                        name: name,
                        type:$(this).find('select[name=type] option:selected').val(),
                        required: $(this).find('select[name=required] option:selected').val(),
                        seq: $(this).find("input[name=seq]").val(),
                        description: $(this).find("input[name=description]").val(),
                        paramType: $(this).find('select[name=paramType] option:selected').val()
                    };
                    endParam.push(param);
                }
            }
        });
        return endParam;
    },
    getOutputParamList: function(openServiceId) {
        var outputParam = [];
        $("#outtable tr:gt(0)").each(function(i, e) {
            var name = $(this).find("input[name=name]").val();
            if(name != null && name != '') {
                var param = {
                    id: $(this).find("input[name=id]").val(),
                    openServiceId:openServiceId,
                    name: $(this).find("input[name=name]").val(),
                    description: $(this).find("input[name=description]").val(),
                    type: $(this).find('select[name=type] option:selected').val(),
                    seq: i
                };
                outputParam.push(param);
            }
        });
        // return JSON.stringify(outputParam);
        return outputParam;
    },

    getHttpAddressList: function () {
        var addressList = [];
        $("#httptable tr:gt(0)").each(function (i, e) {
            var address = $(this).find("input[name=address]").val();
            if (address != null && address != '') {
                var address = {
                    id: $(this).find("input[name=id]").val(),
                    type: $(this).find("select[name=type] option:selected").val(),
                    address: $(this).find("input[name=address]").val(),
                };
                addressList.push(address);
            }
        });
        return JSON.stringify(addressList);
    },

    getWebServiceAddressList: function () {
        var addressList = [];
        $("#webservicetable tr:gt(0)").each(function (i, e) {
            var address = $(this).find("input[name=address]").val();
            if (address != null && address != '') {
                var address = {
                    id: $(this).find("input[name=id]").val(),
                    type: $(this).find("select[name=type] option:selected").val(),
                    namespace: $(this).find("input[name=namespace]").val(),
                    address: $(this).find("input[name=address]").val(),
                };
                addressList.push(address);
            }
        });
        return JSON.stringify(addressList);
    },

    getNettyAddressList: function () {
        var addressList = [];
        $("#nettytable tr:gt(0)").each(function (i, e) {
            var address = $(this).find("input[name=address]").val();
            if (address != null && address != '') {
                var address = {
                    id: $(this).find("input[name=id]").val(),
                    interfaceName: $(this).find("input[name=interfaceName]").val(),
                    address: $(this).find("input[name=address]").val(),
                };
                addressList.push(address);
            }
        });
        return JSON.stringify(addressList);
    }
};

function sticky(msg, style, position) {
    var type = style ? style : 'success';
    var place = position ? position : 'top';
    $.sticky(msg, {
        autoclose: 1000,
        position: place,
        style: type
    });
}
