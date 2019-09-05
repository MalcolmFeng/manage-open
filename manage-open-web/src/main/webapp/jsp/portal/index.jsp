<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%--<%@ taglib uri="/tags/loushang-web" prefix="l"%>--%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
  <title>首页</title>

  <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>"/>
  <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>"/>
  <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>"/>
  <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>"/>

  <link rel="stylesheet" type="text/css" href="<l:asset path='portal/axure_rp_page.css'/>"/>
  <link rel="stylesheet" type="text/css" href="<l:asset path='portal/styles.css'/>"/>
  <link rel="stylesheet" type="text/css" href="<l:asset path='portal/index/styles.css'/>"/>

  <script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='form.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='datatables.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='loushang-framework.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='jquery.form.js'/>"></script>
  <script type="text/javascript" src="<l:asset path='ui.js'/>"></script>
  <script type="text/javascript">
      var data_url;
      var data_service;
      var data_dev;
      $(function(){
          var protocal = window.location.protocol;
          var HostIp = protocal+"//"+window.location.host;
          data_service=HostIp+"/open-platform/jsp/servicedev/console.jsp?realm="+'${realm}';
          if(protocal.indexOf("172")>0){
            //内网访问
            data_url="http://172.19.221.54:9181/odmgr";
            data_dev="http://172.19.221.54/open-platform/?realm="+'${realm}';
          }else{
            data_url='${dataurl}';
            data_dev=HostIp+"/open-platform/?realm="+'${realm}';
          }
          document.getElementById("datasource").href=data_url;
          document.getElementById("dataservice").href=data_service;
          document.getElementById("datadevelop").href=data_dev;

      });

  </script>
</head>
<body>
<div id="base" class="">
  <!-- Unnamed (组合) -->
  <div id="u0" class="ax_default" data-width="220" data-height="80">

    <!-- Unnamed (矩形) -->
    <div id="u1" class="ax_default box_2 ax_default_hidden" style="display:none; visibility: hidden">
      <div id="u1_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u2" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>

    <!-- Unnamed (矩形) -->
    <div id="u3" class="ax_default box_2">
      <div id="u3_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u4" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>

    <!-- Unnamed (图片) -->
    <div id="u5" class="ax_default _图片">
      <img id="u5_img" class="img " src="<l:asset path='portal/img/u5.png'/>"/>
      <!-- Unnamed () -->
      <div id="u6" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>

    <!-- Unnamed (矩形) -->
    <div id="u7" class="ax_default label">
      <div id="u7_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u8" class="text">
        <p><span><a id="datasource"  target="_blank">数据管理</a></span></p>
      </div>
    </div>
  </div>

  <!-- Unnamed (组合) -->
  <div id="u9" class="ax_default" data-width="220" data-height="80">

    <!-- Unnamed (矩形) -->
    <div id="u10" class="ax_default box_2 ax_default_hidden" style="display:none; visibility: hidden">
      <div id="u10_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u11" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>

    <!-- Unnamed (矩形) -->
    <div id="u12" class="ax_default box_2">
      <div id="u12_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u13" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>

    <!-- Unnamed (图片) -->
    <div id="u14" class="ax_default _图片">
      <img id="u14_img" class="img " src="<l:asset path='portal/img/u14.png'/>"/>
      <!-- Unnamed () -->
      <div id="u15" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>

    <!-- Unnamed (矩形) -->
    <div id="u16" class="ax_default label">
      <div id="u16_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u17" class="text">
       <%-- <p><span><a id="dataservice" href="http://10.111.24.112/dev/jsp/servicedev/console.jsp?realm=realm1234" target="_blank">数据服务</a></span></p>--%>
        <p><span><a id="dataservice"  target="_blank">数据服务</a></span></p>
      </div>
    </div>
  </div>

  <!-- Unnamed (组合) -->
  <div id="u57" class="ax_default" data-width="220" data-height="80">

    <!-- Unnamed (矩形) -->
    <div id="u58" class="ax_default box_2 ax_default_hidden" style="display:none; visibility: hidden">
      <div id="u58_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u59" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>

    <!-- Unnamed (矩形) -->
    <div id="u60" class="ax_default box_2">
      <div id="u60_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u61" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>

    <!-- Unnamed (矩形) -->
    <div id="u62" class="ax_default label">
      <div id="u62_div" class=""></div>
      <!-- Unnamed () -->
      <div id="u63" class="text">
        <%--<p><span><a id="datadevelop" href="http://10.111.24.112/dev/?realm=realm1234" target="_blank">数据集成开发</a></span></p>--%>
        <p><span><a id="datadevelop"  target="_blank">数据集成开发</a></span></p>
      </div>
    </div>

    <!-- Unnamed (图片) -->
    <div id="u64" class="ax_default _图片">
      <img id="u64_img" class="img " src="<l:asset path='portal/img/u64.png'/>"/>
      <!-- Unnamed () -->
      <div id="u65" class="text" style="display:none; visibility: hidden">
        <p><span></span></p>
      </div>
    </div>
  </div>

  <!-- Unnamed (矩形) -->
 <%-- <div id="u66" class="ax_default box_1">
    <img id="u66_img" class="img " src="images/index/u66.png"/>
    <!-- Unnamed () -->
    <div id="u67" class="text" style="display:none; visibility: hidden">
      <p><span></span></p>
    </div>
  </div>--%>

<%--  <!-- Unnamed (矩形) -->
  <div id="u68" class="ax_default box_1">
    <img id="u68_img" class="img " src="images/index/u68.png"/>
    <!-- Unnamed () -->
    <div id="u69" class="text" style="display:none; visibility: hidden">
      <p><span></span></p>
    </div>
  </div>--%>

  <!-- Unnamed (矩形) -->
 <%-- <div id="u70" class="ax_default box_1">
    <div id="u70_div" class=""></div>
    <!-- Unnamed () -->
    <div id="u71" class="text">
      <p><span>XXX标题</span></p>
    </div>
  </div>--%>
</div>
</body>
</html>
