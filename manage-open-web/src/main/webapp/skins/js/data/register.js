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
        // var sourceName=$("#sourceDiv option:selected").attr("data-value");
        // console.info(sourceName);
        var sourceId = $("#groupSelect3 option:selected").val();
        $.ajax({
            type: "post",
            url: context + "/service/data/group/table?sourceId="+sourceId,
            data:JSON.stringify({parentId:-1}),
            contentType:'application/json;charset=UTF-8',
            success: function(data) {
                // var html = template("grouplist4", data);
                $(".fs-label").empty().append("请选择表");
                var opt="";
                var tableIds=$("#tableInput").val();
                var tablename=$("#tablename").val();
                for (var i in data){
                    var item=data[i];
                    for (var j in item) {
                        var jitem = item[j];
                        if (tableIds.indexOf(jitem.dataResourceId)!=-1){
                            opt = opt + "<div class='fs-option selected' title="+jitem.dataResourceName+" data-value=" + jitem.dataResourceId + " data-index='"
                                + j + "'><span class='fs-checkbox'><i></i></span><div class='fs-option-label'>" +
                                jitem.tableName + "</div></div>";
                        }else{
                            opt = opt + "<div class='fs-option' title="+jitem.dataResourceName+" data-value=" + jitem.dataResourceId + " data-index='"
                                + j + "'><span class='fs-checkbox'><i></i></span><div class='fs-option-label'>" +
                                jitem.tableName + "</div></div>";
                        }
                    }
                }
                $(".fs-options").empty().append(opt);
                if(tablename!=""){
                    $(".fs-label").empty().append(tablename);
                }
                // $("#tableDiv").empty().append(html);
                // $('.demo').fSelect();
                /*$("#groupSelect4").find("option:contains("+tableName+")").attr("selected",true);*/
                // $("#groupSelect4").val(remoteId);
                // if(remoteId !=null && remoteId!=''){
                //   register.loadTableDetail(remoteId);
                // }
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
    register.getTableSelect(serviceInfo);
    serviceInfo.id = $("#id").val();
    serviceInfo.name = $("#name").val();
    serviceInfo.dbName= $("#groupSelect3 option:selected").text();
    // serviceInfo.tableName = $("#tableDiv option:selected").text();
    serviceInfo.needUserAuth = $('input[name=needUserAuth]:checked').val();//是否需要用户授权
    serviceInfo.groupId = $("input[name=groupId]").val();
    serviceInfo.description = $("#description").val();
    serviceInfo.dataDetailUrl = $("#dataDetailUrl").val();
    // serviceInfo.dataExample = $("#dataExample").val();
    // serviceInfo.columnList = register.getColumnList();
    serviceInfo.authType = $('input[name=authType]:checked').val();//授权方式

    // serviceInfo.remoteId = $("#tableDiv option:selected").val();
    // serviceInfo.instanceName = $("#sourceDiv option:selected").text();
    serviceInfo.instanceName = $("#sourceDiv option:selected").attr("data-value");
    serviceInfo.dataSourceId = $("#sourceDiv option:selected").val();
    serviceInfo.auditStatus = $("#auditStatus").val();
    return serviceInfo;
  },
    getTableSelect:function(serviceInfo){
      var tableNames="";
      var tableIds="";
      var result=[];
      $(".fs-option.selected").each(function (i,e) {
          if (tableIds==""){
              tableIds=$(this).attr("data-value");
          }else{
              tableIds=tableIds+","+$(this).attr("data-value");
          }
          if (tableNames==""){
              tableNames=$(this).find('div[class=fs-option-label]').text();
          }else{
              tableNames=tableNames+","+$(this).find('div[class=fs-option-label]').text();
          }
      });
        serviceInfo.remoteId=tableIds;
        serviceInfo.tableName=tableNames;
    },
  getColumnList: function() {
    var inputParam = [];
    $("#inputtable tr:gt(0)").each(function(i, e) {
      var name = $(this).children('td').eq(0).text().trim();
      console.info(name);
      if(name != null && name != '') {
        var param = {
            id: $(this).find("input[name=id]").val(),
            columnName:name,
            columnType:$(this).find('input[name=columnType]').val(),
            isNull:$(this).find('input[name=isNull]').val(),
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
/**                        多选下拉框                         **************/
(function($) {

    $.fn.fSelect = function(options) {

        if (typeof options == 'string' ) {
            var settings = options;
        }
        else {
            var settings = $.extend({
                placeholder: '请选择表',
                numDisplayed: 5,
                overflowText: '已选择{n}个',
                searchText: 'Search',
                showSearch: true
            }, options);
        }

        /**
         * Constructor
         */
        function fSelect(select, settings) {
            this.$select = $(select);
            this.settings = settings;
            this.create();
        }

        /**
         * Prototype class
         */
        fSelect.prototype = {
            create: function() {
                var multiple = this.$select.is('[multiple]') ? ' multiple' : '';
                this.$select.wrap('<div class="fs-wrap' + multiple + '" style="width: 25%; display: inline-block"></div>');
                this.$select.before('<div class="fs-label-wrap form-control ue-form"><div class="fs-label">' + this.settings.placeholder + '</div><span class="fs-arrow"></span></div>');
                this.$select.before('<div class="fs-dropdown hidden"><div class="fs-options"></div></div>');
                this.$select.addClass('hidden');
                this.$wrap = this.$select.closest('.fs-wrap');
                this.reload();
            },

            reload: function() {
                if (this.settings.showSearch) {
                    var search = '<div class="fs-search"><input type="search" placeholder="' + this.settings.searchText + '" /></div>';
                    this.$wrap.find('.fs-dropdown').prepend(search);
                }
                var choices = this.buildOptions(this.$select);
                this.$wrap.find('.fs-options').html(choices);
                this.reloadDropdownLabel();
            },

            destroy: function() {
                this.$wrap.find('.fs-label-wrap').remove();
                this.$wrap.find('.fs-dropdown').remove();
                this.$select.unwrap().removeClass('hidden');
            },

            buildOptions: function($element) {
                var $this = this;

                var choices = '';
                $element.children().each(function(i, el) {
                    var $el = $(el);

                    if ('optgroup' == $el.prop('nodeName').toLowerCase()) {
                        choices += '<div class="fs-optgroup">';
                        choices += '<div class="fs-optgroup-label">' + $el.prop('label') + '</div>';
                        choices += $this.buildOptions($el);
                        choices += '</div>';
                    }
                    else {
                        var selected = $el.is('[selected]') ? ' selected' : '';
                        choices += '<div class="fs-option' + selected + '" data-value="' + $el.prop('value') + '"><span class="fs-checkbox"><i></i></span><div class="fs-option-label">' + $el.html() + '</div></div>';
                    }
                });

                return choices;
            },

            reloadDropdownLabel: function() {
                var settings = this.settings;
                var labelText = [];

                this.$wrap.find('.fs-option.selected').each(function(i, el) {
                    labelText.push($(el).find('.fs-option-label').text());
                });

                if (labelText.length < 1) {
                    labelText = settings.placeholder;
                }
                else if (labelText.length > settings.numDisplayed) {
                    labelText = settings.overflowText.replace('{n}', labelText.length);
                }
                else {
                    labelText = labelText.join(', ');
                }

                this.$wrap.find('.fs-label').html(labelText);
                this.$select.change();
            }
        }


        /**
         * Loop through each matching element
         */
        return this.each(function() {
            var data = $(this).data('fSelect');

            if (!data) {
                data = new fSelect(this, settings);
                $(this).data('fSelect', data);
            }

            if (typeof settings == 'string') {
                data[settings]();
            }
        });
    }


    /**
     * Events
     */
    window.fSelect = {
        'active': null,
        'idx': -1
    };

    function setIndexes($wrap) {
        $wrap.find('.fs-option:not(.hidden)').each(function(i, el) {
            $(el).attr('data-index', i);
            $wrap.find('.fs-option').removeClass('hl');
        });
        $wrap.find('.fs-search input').focus();
        window.fSelect.idx = -1;
    }

    function setScroll($wrap) {
        var $container = $wrap.find('.fs-options');
        var $selected = $wrap.find('.fs-option.hl');

        var itemMin = $selected.offset().top + $container.scrollTop();
        var itemMax = itemMin + $selected.outerHeight();
        var containerMin = $container.offset().top + $container.scrollTop();
        var containerMax = containerMin + $container.outerHeight();

        if (itemMax > containerMax) { // scroll down
            var to = $container.scrollTop() + itemMax - containerMax;
            $container.scrollTop(to);
        }
        else if (itemMin < containerMin) { // scroll up
            var to = $container.scrollTop() - containerMin - itemMin;
            $container.scrollTop(to);
        }
    }

    $(document).on('click', '.fs-option', function() {
        var $wrap = $(this).closest('.fs-wrap');

        if ($wrap.hasClass('multiple')) {
            var selected = [];

            $(this).toggleClass('selected');
            $wrap.find('.fs-option.selected').each(function(i, el) {
                selected.push($(el).attr('data-value'));
            });
        }
        else {
            var selected = $(this).attr('data-value');
            $wrap.find('.fs-option').removeClass('selected');
            $(this).addClass('selected');
            $wrap.find('.fs-dropdown').hide();
        }

        $wrap.find('select').val(selected);
        $wrap.find('select').fSelect('reloadDropdownLabel');
    });

    $(document).on('keyup', '.fs-search input', function(e) {
        if (40 == e.which) {
            $(this).blur();
            return;
        }

        var $wrap = $(this).closest('.fs-wrap');
        var keywords = $(this).val();

        $wrap.find('.fs-option, .fs-optgroup-label').removeClass('hidden');

        if ('' != keywords) {
            $wrap.find('.fs-option').each(function() {
                var regex = new RegExp(keywords, 'gi');
                if (null === $(this).find('.fs-option-label').text().match(regex)) {
                    $(this).addClass('hidden');
                }
            });

            $wrap.find('.fs-optgroup-label').each(function() {
                var num_visible = $(this).closest('.fs-optgroup').find('.fs-option:not(.hidden)').length;
                if (num_visible < 1) {
                    $(this).addClass('hidden');
                }
            });
        }

        setIndexes($wrap);
    });

    $(document).on('click', function(e) {
        var $el = $(e.target);
        var $wrap = $el.closest('.fs-wrap');

        if (0 < $wrap.length) {
            if ($el.hasClass('fs-label')) {
                window.fSelect.active = $wrap;
                var is_hidden = $wrap.find('.fs-dropdown').hasClass('hidden');
                $('.fs-dropdown').addClass('hidden');

                if (is_hidden) {
                    $wrap.find('.fs-dropdown').removeClass('hidden');
                }
                else {
                    $wrap.find('.fs-dropdown').addClass('hidden');
                }

                setIndexes($wrap);
            }
        }
        else {
            $('.fs-dropdown').addClass('hidden');
            window.fSelect.active = null;
        }
    });

    $(document).on('keydown', function(e) {
        var $wrap = window.fSelect.active;

        if (null === $wrap) {
            return;
        }
        else if (38 == e.which) { // up
            e.preventDefault();

            $wrap.find('.fs-option').removeClass('hl');

            if (window.fSelect.idx > 0) {
                window.fSelect.idx--;
                $wrap.find('.fs-option[data-index=' + window.fSelect.idx + ']').addClass('hl');
                setScroll($wrap);
            }
            else {
                window.fSelect.idx = -1;
                $wrap.find('.fs-search input').focus();
            }
        }
        else if (40 == e.which) { // down
            e.preventDefault();

            var last_index = $wrap.find('.fs-option:last').attr('data-index');
            if (window.fSelect.idx < parseInt(last_index)) {
                window.fSelect.idx++;
                $wrap.find('.fs-option').removeClass('hl');
                $wrap.find('.fs-option[data-index=' + window.fSelect.idx + ']').addClass('hl');
                setScroll($wrap);
            }
        }
        else if (32 == e.which || 13 == e.which) { // space, enter
            $wrap.find('.fs-option.hl').click();
        }
        else if (27 == e.which) { // esc
            $('.fs-dropdown').addClass('hidden');
            window.fSelect.active = null;
        }
    });

})(jQuery);

