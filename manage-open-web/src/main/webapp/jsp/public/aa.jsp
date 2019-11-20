<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="/tags/loushang-web" prefix="l" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
    <link rel="stylesheet" type="text/css" href="<l:asset path='platform/css/home.css'/>"/>

    <script type="text/javascript" src="<l:asset path='knockout.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='jquery.js'/>" ></script>


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
    @font-face {
        font-family: 'dsdigit';
    <%--src: url(<l:asset path='fonts/dsdigit.ttf'/>);--%>
        src: url(../../skins/skin/fonts/dsdigit.ttf);
        font-weight: 600;
        font-style: normal
    }
    body{
        font-family:Microsoft YaHei;
    }
    .bg{
        margin: 0;
        padding: 0;
    }
    .bg>img{
        /*position: absolute;*/
        top: 0;
        left: 0;
        width:100%;
        display: block;
        margin: 0;
        padding: 0;
    }
    body,div{
        margin: 0px 0px;
        padding: 0px 0px;
    }


    .sub-title{
        font-size: 36px;
        color: #F2F2F2;
        display: inline;
    }
    .title{
        font-size: 28px;
        font-weight:bold;
        color:#333;
        position: relative;
        margin-top: -32px;

    }
    .data{
         height: 90px;
         width: 240px;
         margin-left: 20px;
         margin-top: 10px;
        border-right: 1px solid #eee;
        border-bottom: 1px solid #eee;
     }
    .data>div{
        height: 90px;
        line-height: 90px;

    }
    .data>div>a>img,.data>div>a>img:hover{
        margin-left: 18px;
        width: 40px;
        height: 40px;
    }
    .data>div>a{
        text-decoration: none;
        color: #333;
    }
    .data>div>a>span,.data>div>a>span:hover{
        width: 50px;
        height: 50px;
        margin-left: 30px;
        font-size: 20px;
        color: #333;
    }
    .data:hover{
        border: 1px solid #0095F9;
        background-color: #F0F2F5;
    }

    .service{
        height: 300px;
        width: 300px;
        margin: 22px;
        background-color: #fff;
    }
    .service-ch{
        /*text-align: center;*/
        font-size: 10px;
        color: #333;
        margin: 20px;
        text-indent:2em;
        font-weight: 500;
    }
    .service>div:first-child{
        width: 300px;
        height: 150px;
        text-align: center;
        line-height: 150px;
        color: #fff;
        font-size:20px;
    }
    .service>img{
        width: 300px;
        height: 130px;
    }
    .servicedata{
        background: url(<l:asset path='img/icon/capability_service/1.png'/>) center center;
        cursor: pointer;
    }
    .serviceserver{
        background: url(<l:asset path='img/icon/capability_service/2.png'/>) center center;
        cursor: pointer;
    }
    .servicecloud{
        background: url(<l:asset path='img/icon/capability_service/3.png'/>) center center;
        cursor: pointer;
    }
    .center{
        border: 1px solid #eee;
        margin: 20px;
        height: 400px;
        width: 300px;
        position: relative;

    }
    .center-img>img{
        width: 120px;
        position: absolute;
        left: 50%;
        margin-left: -60px;
        margin-top: 20px;
    }
    .center-ch{
        font-size: 24px;
        margin-top: 170px;
        text-align: center;
        font-weight: bold;
    }
    .center-en{
        font-size: 10px;
        margin-top: 20px;
        margin-right: 10px;
    }
    .center-ul li{
        list-style-type: none;
        background: url(<l:asset path='img/icon/data_center/4.png'/>) no-repeat 0rem 0.3rem;
        background-size: 8px 8px;
        text-indent: 1.5em;
        margin-left: 15px;
        /*line-height: 30px;*/
        margin-top: 10px;
        /*cursor: pointer;*/
    }
    .center-ul>li>span{
        float: right
    }
    .centermore{
        width: 70px;
        height: 25px;
        background-color: #4094FB;
        text-align: center;
        line-height: 25px;
        color: #fff;
        position: absolute;
        left: 50%;
        margin-left: -30px;
        margin-top: 10px;
        cursor: pointer;
    }
    .type{
        height: 200px;
        width: 170px;
        margin:15px;
        position: relative;
    }
    .type-img{
        width: 120px;
        height: 120px;
        position: absolute;
        text-align: center;
        left: 50%;
        margin-left: -60px;
        border-radius: 60px;
        background-color: #fff;
        margin-top: 20px;
    }
    .type-img>img{
        margin-top: 15px;
    }
    .type-ch{
        margin-top: 150px;
        font-size: 18px;
        font-weight: 600;
        text-align: center;
        cursor: pointer;

    }
    .direct{
        width: 120px;
        height: 100%;
    }
    .directTitle{
        width: 164px;
        height: 47px;
        background-color: #1890FF;
        font-size: 24px;
        color: #fff;
        border-radius: 3px;
        line-height: 47px;
        text-align: center;
        font-weight: bold;
        margin-left: -3%;
    }
    .directsubTitle{
        width: 154px;
        height: 40px;
        border: 1px solid #ccc;
        background-color: #fff;
        font-size: 13px;
        line-height: 40px;
        text-align: center;
        margin-top: 8px;
        box-shadow: 1px 1px #CCCCCC;
        cursor: pointer;
    }
    .serviceContent{
        width: 120px;
        height: 96px;
        border: 1px solid #eee;
        background-color: #fff;
        margin-left: 38px;
        margin-top: 10px;
        position: relative;
        cursor: pointer;
    }
    .serviceContent>img{
        width: 25px;
        height: 25px;
        display: block;
        position: absolute;
        left: 50%;
        margin-left: -10px;
        margin-top: 15px;
    }
    .serviceContent>div{
        margin-top: 55px;
        text-align: center;
        font-size: 10px;
        cursor: pointer;
    }
    .active{
        display: flex;
    }
    .directRight{
        width: 880px;
        height: 320px;
        display: flex;
        flex-direction: column;
        margin-top: -20px;
    }
    .directRight>div{
        margin-left: 80px;
    }
    .nonactive{
        display: none;
    }
    .directsubTitle:hover{
        font-weight: bold;
        background-color: #0072D9;
        color: #fff;
    }
    .serviceTitle{
        font-size: 22px;
        font-weight: bold;
        text-align: center;
        margin-top: 20px;
    }
    /*.service:hover{*/
    /*    border: 1px solid #0072D9;*/
    /*}*/
     a{
         text-decoration:none;
     }
</style>

<body>

<div class="bg">
    <img src="<l:asset path='img/icon/banner.png'/>">
</div>

<!-- 开放数据 -->
<div>
    <div style="width: 100%;display: flex;flex-direction: column;align-items: center;align-content: center;padding: 10px;">
        <span class="sub-title">Open data</span>
        <div class="title">开放数据</div>
    </div>
    <div style="width: 100%;height: 150px;display: flex;flex-direction: row;justify-content: center;">
        <div class="data" id="sjml" >
            <div><a ><img src="<l:asset path='img/icon/open_data/shujuku1.png'/>"><span>&nbsp;&nbsp;179</span>&nbsp;个数据目录</a>
            </div></div>
        <div class="data" id="sjjs">
            <div><a><img  src="<l:asset path='img/icon/open_data/shujuku1.png'/>"><span>&nbsp;&nbsp;46</span>&nbsp;个数据集市</a></div>
        </div>
        <div class="data" id="sjjk">
            <div><a ><img  src="<l:asset path='img/icon/open_data/shujuku1.png'/>"><span>&nbsp;&nbsp;15</span>&nbsp;个数据接口</a></div>
        </div>
        <div class="data">
            <div><a href=""><img  src="<l:asset path='img/icon/open_data/shujuku1.png'/>"><span>&nbsp;&nbsp;101</span>&nbsp;亿条数据总量</a></div>
        </div>
    </div>
</div>

<!-- 平台能力 -->
<div style="background-color: #F3F7FE;">
    <div style="width: 100%;display: flex;flex-direction: column;align-items: center;align-content: center;padding: 10px;">
        <div class="sub-title">Data Directory</div>
        <div class="title">数据目录</div>
    </div>
    <div style="width: 100%;height: 350px;display: flex;flex-direction: row; margin-top: 20px;align-items: center;align-content: center;justify-content: center">
        <div class="direct">
            <div class="directTitle">数据分类</div>
            <div class="directsubTitle dzblTitle" value="ff8080816e725c2b016e72688fd70001">电子病历</div>
            <div class="directsubTitle jkdaTitle" value="ff8080816e73cd2f016e73cd2fd30000">健康档案</div>
            <div class="directsubTitle qyrkTitle" value="ff8080816e725c2b016e725c2b1c0000">全员人口</div>
            <div class="directsubTitle jczyTitle" value="ff8080816e81c020016e81c020850000">基础资源</div>
            <div class="directsubTitle dzzzTitle">电子证照</div>
            <div class="directsubTitle qtzyTitle">其他资源</div>
        </div>
        <div class="active directRight"   id="dzbl">
            <div style="width: 90%;display: flex;flex-direction: row; flex-wrap: wrap">
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/mjzgh.png'/>"><div>门诊/急诊挂号</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/jyjl.png'/>"><div>检验记录</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/jyjgmx.png'/>"><div>检验结果明细</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/jcjl.png'/>"><div>检查记录</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/zy.png'/>"><div>入院记录</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/tjbgd.png'/>"><div>体检报告单</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/tjxmb.png'/>"><div>体检项目表</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/tjxmmx.png'/>"><div>体检项目明细</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/jybgd.png'/>"><div>检验报告单</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/blxx.png'/>"><div>病历信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/zybasy.png'/>"><div>住院病案首页</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/cyjl.png'/>"><div>出院记录</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/yzxx.png'/>"><div>住院医嘱信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzbl/zyfyxx.png'/>"><div>门诊 /急诊 /住院费用信息</div></div>
                <div class="serviceContent"><div style="margin-top: 35px;">更多……</div></div>
            </div>
        </div>
        <div class="directRight  nonactive"  id="jkda" >
            <div style="width: 90%;display: flex;flex-direction: row;flex-wrap: wrap">
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/grjkxx.png'/>"><div>个人健康信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/jktjb.png'/>"><div>健康体检表</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/jzjl.png'/>"><div>接诊记录</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/hyjl.png'/>"><div>会诊记录</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/jtcy.png'/>"><div>家庭成员</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/jtjbxx.png'/>"><div>家庭基本信息数据</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/7.png'/>"><div>产前检查记录表</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/8.png'/>"><div>新生儿访视记录表</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/9.png'/>"><div>儿童健康体检记录表</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/10.png'/>"><div>残疾人表</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/11.png'/>"><div>学生体检表</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/12.png'/>"><div>出生医学证明</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/13.png'/>"><div>孕检记录表</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jkda/14.png'/>"><div>住陪管理接口</div></div>
                <div class="serviceContent"><div style="margin-top: 35px;">更多……</div></div>
            </div>
        </div>
        <div class="directRight  nonactive" id="qyrk">
            <div style="width: 90%;display: flex;flex-direction: row; flex-wrap: wrap;">
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qyrk/1.png'/>"><div>居民个人基本信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qyrk/2.png'/>"><div>社保信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qyrk/3.png'/>"><div>住房信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qyrk/4.png'/>"><div>基民诚信失信信息</div></div>
            </div>
        </div>
        <div class="directRight  nonactive" id="jczy" >
            <div style="width: 90%;display: flex;flex-direction: row; flex-wrap: wrap">
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/1.png'/>"><div>区域总体情况</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/2.png'/>"><div>机构信息（医院、院区）</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/3.png'/>"><div>科室</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/4.png'/>"><div>床位</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/5.png'/>"><div>家庭成员</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/6.png'/>"><div>在建项目信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/7.png'/>"><div>安全隐患信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/8.png'/>"><div>人力信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/9.png'/>"><div>人力培训信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/10.png'/>"><div>人力考核信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/11.png'/>"><div>土地信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/12.png'/>"><div>房产信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/13.png'/>"><div>设备信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/jczy/14.png'/>"><div>收入与支出</div></div>
                <div class="serviceContent"><div style="margin-top: 35px">更多……</div></div>
            </div>
        </div>
        <div class="directRight  nonactive" id="dzzz" >
            <div style="width: 90%;display: flex;flex-direction: row; flex-wrap: wrap">
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/1.png'/>"><div>医师资格证书</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/2.png'/>"><div>医师执业证书</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/3.png'/>"><div>护士执业资格证书</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/4.png'/>"><div>医疗机构执业许可证</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/5.png'/>"><div>台湾医师短期行医执业证书</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/6.png'/>"><div>港澳医师短期行医执业证书</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/7.png'/>"><div>外国医师短期行医许可证</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/8.png'/>"><div>出生医学证明</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/9.png'/>"><div>生育证</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/10.png'/>"><div>人力考核信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/dzzz/11.png'/>"><div>电子医学证明</div></div>

            </div>

        </div>
        <div class="directRight  nonactive" id="qtzy" >
            <div style="width: 90%;display: flex;flex-direction: row;flex-wrap: wrap">
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/1.png'/>"><div>食品安全企业标准备案信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/2.png'/>"><div>中小学生体检信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/3.png'/>"><div>药品采购信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/4.png'/>"><div>计划免疫数据</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/5.png'/>"><div>妇幼保健数据</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/6.png'/>"><div>卫生监督和行政执法信息</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/7.png'/>"><div>住院医师规范化培训</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/8.png'/>"><div>电子健康卡系统数据</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/9.png'/>"><div>预约挂号系统数据</div></div>
                <div class="serviceContent"><img  src="<l:asset path='img/homeicon/qt/10.png'/>"><div>健康扶贫系统数据</div></div>
            </div>

        </div>

    </div>
</div>
<!-- 平台能力 -->
<div style="background-color: #F5F5F5;">
    <div style="width: 100%;display:flex;flex-direction: column;align-items: center;align-content: center;padding: 10px;">
        <div class="sub-title">Capability service</div>
        <div class="title">平台能力</div>
    </div>
    <div style="width: 100%;height: 350px;display: flex;flex-direction: row;justify-content: center;">
        <div class="service">
            <div class="servicedata">数据服务</div>
            <div class="service-ch"><p>基于省平台四大库、五大业务应用系统的数据资源，面向医疗机构、科研团体和企业等机构采用申请入驻平台管理机制，提供各专项数据集、数据表等资源的申请调阅，助力机构挖掘数据价值。
            </p></div>
        </div>
        <div class="service">
            <div class="serviceserver">API服务</div>
            <%--            <img src="<l:asset path='img/icon/capability_service/2.png'/>">--%>
            <a ><div class="service-ch">基于省平台四大库、五大业务应用系统的数据资源，面向医疗机构、科研团体和企业等平台入驻机构提供数据API接口调用服务，满足机构数据调用需求。
            </div></a>
        </div>
        <div class="service">
            <div class="servicecloud">云资源服务</div>
            <%--            <img src="<l:asset path='img/icon/capability_service/3.png'/>">--%>
            <a ><div class="service-ch">        面向入驻机构用户提供云主机服务、关系型数据库服务和大数据服务，用户可根据自身业务需求申请云资源相关配置。
        </div></a>
        </div>
    </div>
</div>

<!-- 数据类型 -->

<div style="background-color: black;color: #FFFFFF;padding: 40px;width: 100%;display: flex;flex-direction: column;align-items: center;align-content: center;padding: 10px;">
    <div style="margin-top: 5px;margin-bottom: 5px;">版权所有：山东省卫生健康委员会  鲁ICP备05023201号-4</div>
    <div style="margin-top: 5px"> 技术支持：浪潮软件集团有限公司</div>
</div>

<script type="text/javascript">
    document.getElementsByTagName("html")[0].style.fontSize = document.body.clientWidth / 1920 + "px";
    document.documentElement.style.fontSize = document.documentElement.clientWidth / 100 + 'px';
</script>
<script type="text/html" id="loginTemp">
    <li id="avatar-img">
        <img src="<l:asset path='img/avatar.png'/>" class="img-circle">
    </li>
    <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            {{data.userName}} <b class="caret"></b>
        </a>
        <ul class="dropdown-menu" style="min-width: 100%;">
            <li><a id="logout" href="#">退出登录</a></li>
        </ul>
    </li>
</script>
<script>

    //顶部步骤蓝切换样式变化
    $(document).on("mouseover", ".dzblTitle", function (){
        // var obj = $(".dzblTitle");
        // initTable(obj);
        $("#jkda,#qyrk,#jczy,#dzzz,#qtzy").css({"display":"none"});
        $("#dzbl").css({"display":"flex"});
    });
    $(document).on("mouseover", ".jkdaTitle", function (){
        $("#dzbl,#qyrk,#jczy,#dzzz,#qtzy").css({"display":"none"});
        $("#jkda").css({"display":"flex"});
    });
    $(document).on("mouseover", ".qyrkTitle", function (){
        $("#dzbl,#jkda,#jczy,#dzzz,#qtzy").css({"display":"none"});
        $("#qyrk").css({"display":"flex"});
    });
    $(document).on("mouseover", ".jczyTitle", function (){
        $("#dzbl,#qyrk,#jkda,#dzzz,#qtzy").css({"display":"none"});
        $("#jczy").css({"display":"flex"});
    });
    $(document).on("mouseover", ".dzzzTitle", function (){
        $("#dzbl,#qyrk,#jczy,#jkda,#qtzy").css({"display":"none"});
        $("#dzzz").css({"display":"flex"});
    });
    $(document).on("mouseover", ".qtzyTitle", function (){
        $("#dzbl,#qyrk,#jczy,#dzzz,#jkda").css({"display":"none"});
        $("#qtzy").css({"display":"flex"});
    });
</script>
<script>
    $(document).on("click", "#sjml,#sjjs,.servicedata", function (){
        var val = parent.document.getElementById("dataSource");
        val.click();
    });
    $(document).on("click", "#sjjk,.serviceserver", function (){
        var val = parent.document.getElementById("apilist");
        val.click();
    });
    $(document).on("click",".servicecloud", function (){
        var val = parent.document.getElementById("cloud-resource");
        val.click();
    });
</script>
<script>
    // var dataCatalog =[];
    // $(function() {
    //     initDataCatalog();
    // })
    // function initDataCatalog(){
    //     var param={
    //         id: "ff8080816e725c2b016e725c2b1c0000",
    //         name: "四大资源库",
    //         otherParam: "zTreeAsyncTest"
    //     }
    //
    //     $.ajax({
    //         type: "post",
    //         url:  "http://172.16.12.95:7070/odmgr/command/dispatcher/com.inspur.od.dataSource.cmd.DataSourceDispatcherCmd/getNodesByPID",
    //         async:false,
    //         contentType:"application/x-www-form-urlencoded",
    //         data: param,
    //         success: function(result) {
    //             console.log(result);
    //             dataCatalog=result;
    //             for (var i=0 ; i<dataCatalog.length ;i++) {
    //                 if (dataCatalog.dataSourceName == "电子病历资源库") {
    //                     $(".dzblTitle").val(dataCatalog.dataSourceId)
    //                 }
    //                 else if (dataCatalog.dataSourceName == "健康档案资源库") {
    //                     $(".jkdaTitle").val(dataCatalog.dataSourceId)
    //                 }
    //                 else if (dataCatalog.dataSourceName == "全员人口资源库") {
    //                     $(".qyrkTitle").val(dataCatalog.dataSourceId)
    //                 }
    //             }
    //         }
    //     })
    // }
 //    function initTable(obj) {
 //        console.log(obj);
 //        var dataSourceId =$(obj).attr("value");
 //       var data={
 //            "params": {
 //            "javaClass": "ParameterSet",
 //                "map": {
 //                "needTotal": true,
 //                    "DATA_SOURCE_ID": dataSourceId,
 //                    "start": 0,
 //                    "limit": 10
 //            },
 //            "length": 7
 //        },
 //            "context": {
 //            "javaClass": "HashMap",
 //                "map": {},
 //            "length": 0
 //        }
 //        }
 // $.ajax({
 //     type: "post",
 //     url:  "http://172.16.12.95:7070/odmgr/command/ajax/com.inspur.od.dataSource.cmd.DataSourceQueryCmd/getReourceByDataSource",
 //     async:false,
 //     contentType:"application/json",
 //     data: JSON.stringify(data),
 //     success: function(result) {
 //         console.log(result);
 //         dataCatalog=result;
 //
 //         }
 //
 // })
 //    }
</script>
</body>
</html>