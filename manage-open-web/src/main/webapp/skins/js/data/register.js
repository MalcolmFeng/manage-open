var register = {
  loadServiceGroupList: function(groupId) {
    $.ajax({
      type: "post",
      url: context + "/service/data/group/list/-1",
      data:JSON.stringify({parentId:-1}),
      contentType:'application/json;charset=UTF-8',
      success: function(data) {
        var html = template("grouplist2", data);
        $("#groupDiv").empty().append(html);
          if(groupId != null && groupId != '') {
              register.setGroupId(groupId);
          }
      }
    });
  },
  loadDataOriginList:function(dataSourceId,remoteId){
      $.ajax({
          type: "post",
          url: context + "/service/data/group/dataorigin/-1",
          data:JSON.stringify({parentId:-1}),
          contentType:'application/json;charset=UTF-8',
          success: function(data) {
              var html = template("grouplist3", data);
              $("#sourceDiv").empty().append(html);
              if(dataSourceId != null && dataSourceId != '' )
                     $("#groupSelect3").val(dataSourceId);
              if(dataSourceId != null && dataSourceId != '' &&remoteId !=null && remoteId!='') {
                  register.loadTableList(remoteId);
              }

          }
      });
  },
  loadTableList:function(remoteId){
        var sourceId = $("#groupSelect3 option:selected").val();
        $.ajax({
            type: "post",
            url: context + "/service/data/group/table?sourceId="+sourceId,
            data:JSON.stringify({parentId:-1}),
            contentType:'application/json;charset=UTF-8',
            success: function(data) {
                var html = template("grouplist4", data);
                $("#tableDiv").empty().append(html);
                /*$("#groupSelect4").find("option:contains("+tableName+")").attr("selected",true);*/
                $("#groupSelect4").val(remoteId);
                if(remoteId !=null && remoteId!=''){
                  register.loadTableDetail(remoteId);
                }
            }
        });
    },
   loadTableDetail:function(remoteId) {
       var tableId = $("#groupSelect4 option:selected").val();
       //不存在tableId 时，置空以下项
       if(!tableId){
           $("#description").val("");
           $("#inputtable").empty();
           $("#inputExample").empty();
           $("#dataExample").val("");
           return;
       }
       $.ajax({
           type: "post",
           url: context + "/service/data/group/tableDetail?tableId=" + tableId,
           data: JSON.stringify({parentId: -1}),
           contentType: 'application/json;charset=UTF-8',
           success: function (data) {
             var des = $("#description").val();
             if(remoteId ==null || remoteId==''||des==null || des==""){
                 $("#description").val(data.data.dataResourceDes);
             }
               var tableName = data.data.tableName;
               var dataResourceId = data.data.dataResourceId;
               var columnInfo = data.data;
               var html = template("detail", columnInfo);

               var columnInfodata = data.data.columnInfo;
               var exampledata = data.data.exampleData;
               var examplelist = [];
                //将获取到的数据解析成list
               for(var i in exampledata){
                     var onedata=[];
                     for(var j in columnInfodata){
                         var eachColumnName = columnInfodata[j].columnName;
                         var key = data.data.tableName+"."+eachColumnName;
                         var value = exampledata[i][key];
                         if(typeof(value) == 'object')
                             onedata.push(JSON.stringify(value));
                         else onedata.push(value);
                      }
                      examplelist.push(onedata);
               }
               //将解析以后的数据假如到模板数据中
               columnInfo["example"] = examplelist;
               var htmlExample = template("example", columnInfo);
               $("#inputtable").empty().append(html);
               $("#inputExample").empty().append(htmlExample);
               $("#dataExample").val( JSON.stringify(data.data.exampleData));
           }
       })
   },
  loadSubServiceGroupList: function(parentId) {
    // 清空子分组列表
    $("#subgrouplistDiv").empty();
    var groupId = $("#groupSelect option:selected").val();//获取父组
    $("input[name=groupId]").val(groupId);
    if(groupId != null && groupId != '') {
      $.ajax({
        type: "post",
        url: context + "/service/data/group/list/" + groupId,
        data:JSON.stringify({parentId:groupId}),
        contentType:'application/json;charset=UTF-8',
        success: function(data) {
          if(data != null && data.data != null && data.data.length != 0) {
            var html = template("subgrouplist2", data);
            $("#subgrouplistDiv").empty().append(html);
            $("#subgrouplistDiv").css("display","inline-block");
            if(parentId){//更新数据的情况
                $("#subgroupSelect").val(defGroupId);
                $("input[name=groupId]").val(defGroupId);
            }else{
                $("input[name=groupId]").val(data.data[0].id);//默认赋值第一个子组
            }
          }else{
        	$("#subgrouplistDiv").css("display","none");
          }
        }
      });
    }
  },
  
  selectServiceGroup: function(obj) {
    $("input[name=groupId]").val($("#subgroupSelect").val());
  },
  
  setNeedAuth: function(needAuth, authUser) {
    if(needAuth != null && needAuth != '') {
      if(needAuth == '0') {
        $("input[name=needAuth][value=0]").prop("checked", true);
      } else {
        if(authUser == 'platform' || authUser == 'PLATFORM') {
          $("input[name=needAuth][value=1]").prop("checked", true);
        } else {
          $("input[name=needAuth][value=2]").prop("checked", true);
        }
      }
    }
  },
  
  setGroupId: function(groupId) {
    if(groupId != null && groupId != '') {
      $.ajax({
        type: "get",
        url: context + "/service/data/group/get/" + groupId,
        success: function(data) {
          if(data != null && data.parentId != null) {
            if("-1"==data.parentId){
                $("#groupSelect").val(groupId);
            }else{
                $("#groupSelect").val(data.parentId);
                register.loadSubServiceGroupList(data.parentId);
            }
          }
        }
      });
    }
  },

  selectProxyType: function(proxyType) {
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

  addHttpService: function() {
    $("#httptable").append(template('httpitem'));
  },

  addWebService: function() {
    $("#webservicetable").append(template('webserviceitem'));
  },

  addNettyService: function() {
    $("#nettytable").append(template('nettyitem'));
  },

  addInputParam: function() {
    $("#inputtable").append(template('inputitem'));
  },

  addOutputParam: function() {
    $("#outputtable").append(template('outputitem'));
  },

  forColumnDel: function(obj) {
    var rowCount = $(obj).closest('table').find('tr').length;
    if (rowCount > 2) {
      $(obj).closest("tr").remove();
    } else {
      $(obj).closest("tr").find("input").val('');
    }
  },

  forSave: function() {
    var serviceInfo = register.getServiceInfo();
    $.ajax({
      type: 'post',
      url: context + "/service/scdev/myservice/register",
      data: serviceInfo,
      success: function(msg) {
        if (msg == true) {
          sticky("保存成功", "success", "center");
          setTimeout(function() {
            window.location.href = context + "/service/scdev/myservice";
          }, 1000);
        } else {
          sticky("保存失败", "error", "center");
        }
      }
    });
  },

  forPublish: function() {
    var serviceInfo = register.getServiceInfo();
    serviceInfo.remote=remote;
    var url=context + "/service/open/data/register";
    $.ajax({
      type: 'post',
      url: url,
      data:JSON.stringify(serviceInfo),
      contentType:'application/json;charset=UTF-8',
      success: function(resp) {
        if(resp.result=="editsuccess"){
            $.dialog({
                autofocus: true,
                type: "alert",
                content:"编辑保存成功!"
            });
            setTimeout(function() {
                window.location.href = context + "/service/open/data/getPage";
            }, 1000);
        }else if (resp.result == true) {
            $.dialog({
                autofocus: true,
                type: "alert",
                content:"成功!"
            });
          setTimeout(function() {
            window.location.href = context + "/service/open/data/getPage";
          }, 1000);
        } else {
            $.dialog({
                autofocus: true,
                type: "alert",
                content:"失败!"+resp.message
            });
        }
      }
    });
  },

  getServiceInfo: function() {
    var serviceInfo = {};
    serviceInfo.id = $("#id").val();
    serviceInfo.name = $("#name").val();
    serviceInfo.dbName= $("#groupSelect3 option:selected").text();
    serviceInfo.tableName = $("#tableDiv option:selected").text();
    serviceInfo.needUserAuth = $('input[name=needUserAuth]:checked').val();//是否需要用户授权
    serviceInfo.groupId = $("input[name=groupId]").val();
    serviceInfo.description = $("#description").val();
    serviceInfo.dataExample = $("#dataExample").val();
    serviceInfo.columnList = register.getColumnList();
    serviceInfo.authType = $('input[name=authType]:checked').val();//授权方式

    serviceInfo.remoteId = $("#tableDiv option:selected").val();
    serviceInfo.instanceName = $("#sourceDiv option:selected").text();
    serviceInfo.dataSourceId = $("#sourceDiv option:selected").val();
    serviceInfo.auditStatus = $("#auditStatus").val();
    return serviceInfo;
  },

  getColumnList: function() {
    var inputParam = [];
    $("#inputtable tr:gt(0)").each(function(i, e) {
      var name = $(this).find("input[name=columnName]").val();
      if(name != null && name != '') {
        var param = {
          id: $(this).find("input[name=id]").val(),
          columnName: $(this).find('input[name=columnName]').val(),
          columnType: $(this).find('select[name=columnType] option:selected').val(),
          isNull: $(this).find('select[name=isNull] option:selected').val(),
          description: $(this).find('input[name=description]').val()
        };
        inputParam.push(param);
      }
    });
    return inputParam;
  },

  getOutputParamList: function() {
    var outputParam = [];
    $("#outputtable tr:gt(0)").each(function(i, e) {
      var name = $(this).find("input[name=name]").val();
      if(name != null && name != '') {
        var param = {
          id: $(this).find("input[name=id]").val(),
          name: $(this).find("input[name=name]").val(),
          description: $(this).find("input[name=description]").val(),
          seq: i
        };
        outputParam.push(param);
      }
    });
    return JSON.stringify(outputParam);
  },

  getHttpAddressList: function() {
    var addressList = [];
    $("#httptable tr:gt(0)").each(function(i, e) {
      var address = $(this).find("input[name=address]").val();
      if(address != null && address != '') {
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

  getWebServiceAddressList: function() {
    var addressList = [];
    $("#webservicetable tr:gt(0)").each(function(i, e) {
      var address = $(this).find("input[name=address]").val();
      if(address != null && address != '') {
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

  getNettyAddressList: function() {
    var addressList = [];
    $("#nettytable tr:gt(0)").each(function(i, e) {
      var address = $(this).find("input[name=address]").val();
      if(address != null && address != '') {
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
