<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" language="java"%>
<%@ taglib uri="/tags/loushang-web" prefix="l"%>
<html lang="en" style="height:100%">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>楼上云应用支撑平台</title>

    <!-- 需要引用的CSS -->
    <link rel="shortcut icon" href="<l:asset path='platform/img/favicon.ico'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/bootstrap.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/font-awesome.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/ui.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/form.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='css/intro.css'/>" />
    <link rel="stylesheet" type="text/css" href="<l:asset path='platform/css/home.css'/>" />
    
    <script  type="text/javascript" src="<l:asset path='knockout.js'/>"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="<l:asset path='html5shiv.js'/>"></script>
      <script src="<l:asset path='respond.js'/>"></script>
    <![endif]-->
    <script type="text/javascript">
	    //项目上下文
		var context="<%=request.getContextPath()%>";
		//获取静态资源上下文路径 
		var assetPath ="<l:assetcontext/>";
    </script>
  </head>
  <body style="height:100%;overflow: hidden;">
	<!-- 页面结构 -->
	<header class="navbar navbar-static-top">
   		<div class="navbar-header">
   			<a class="fa fa-bars pull-left" onclick="toggleSide()" data-step="4" data-intro="点击此处可扩展恢复工作区空间" data-position="right"></a>
   			<a href="javascript: location=location;" class="navbar-brand"><span>|</span>楼上云应用支撑平台</a>
		</div>
    	<nav class="collapse navbar-collapse">
    		<ul class="nav navbar-nav navbar-right">
    			<li role="search">
    				<a id="searchImg" class="fa fa-search" onclick="showSearch(this)" ></a>
    			</li>
   				<li>
    				<a id="protal" class="fa fa-dashboard" title="门户" ></a>
    			</li>
    			<li>
    				<div class="dropdown" id="favoritesMenu">
    					<a class="fa fa-star-o" data-toggle="dropdown" onclick="showFavorites(this)"></a>
    		<%-- 			<ul class="dropdown-menu ue-dropdown-menu dropdown-menu-right" >
						  	<li class="ue-dropdown-angle"></li>
						  	<div data-bind="foreach: menuList" class="drop-area">
						  		<li>
						  			<a class="ue-title ellipsis" data-bind="click:showFavorites">
						  		    <span data-bind=" text: name"></span>
						  		    <i class="fa fa-times ue-vmenu-icon-r" data-bind="click:saveFavorites.bind($data,$index()),clickBubble: false" data-toggle="tooltip" data-placement="right" title="删除"></i>
						  		    </a>
						  		</li>
						  	</div>
						    <li><a id="favoritesMore" class="more" data-bind="click: favoritesMenuView.showMore, clickBubble: false">更多</a></li>
					  	</ul> --%>
    				</div>
    			</li>
	    		<li data-step="1" data-intro="请选择所需模块" data-position="left">
	    			<div class="dropdown" id="topMenu">
	    				<a class="fa fa-th-large" data-toggle="dropdown"></a>
		    		<!-- 	<div class="dropdown-menu ue-dropdown-menu dropdown-menu-right app">
		    				<span class="ue-dropdown-angle"></span>
		    				<div data-bind="foreach: menuList" class="app drop-area">
		    					<div class="ue-icon-title" data-bind="click: topMenuView.getSubMenu">
			    					<img data-bind="attr: { src:topMenuView.topMenuIcon($data)}" class="ue-icon"/>
									<a class="ue-title ellipsis" data-bind="attr: { title:text},text:text"></a>
								</div>
							</div>	
							<a id="topMore" class="more" data-bind="click: showMore, clickBubble: false">更多</a>
						</div> -->
					</div>
		      	</li>
		        <li>
		        	<div class="dropdown" id="userInfo">
		        		<a data-toggle="dropdown" data-bind="text:userName" class="username"><span class="caret"></span></a>
		        	</div>
		        </li>
		    </ul>
		    
		    <aside id="searchview" class="searchview" role="search" >
				<div class="searchview-content">
					<form id="searchform" class="searchform" method="get" onsubmit="return false;">
						<div class="searchform-wrapper">
							<input id="searchform-input" name="searchform-input" style="background-position-y: 10px;" class="searchform-input" type="text" onblur="searchCancel()" onkeydown="query(event)"/>
							<span class="searchform-submit fa fa-search" ></span>
						</div>
					</form>
				</div>
			</aside>
   		</nav>
	</header>
	
	<div class="ue-menu-wrap">
		<div class="ue-menu-left" id="leftMenu">
			<div class="ue-left-top" >
				<span id="dyn-top">
				    <a class="ue-title ellipsis" data-bind="click:loadUrl">
						<img data-bind="attr: { src:PMenuIcon}" class="title-icon"/>
						<span class="left-top-text ellipsis" data-bind="text:pMenuName"></span>
					</a>
				</span>
			</div>
			<div class="ue-left-content" data-step="2" data-intro="请选择模块的具体操作" data-position="right">
				<div class="ue-vmenu">
				</div>
			</div>
		</div>
		<div class="ue-menu-right">
			<div class="ue-right-top">Welcome</div>
			<div class="ue-right-content" data-step="3" data-intro="工作区" data-position="left">
				<%-- <iframe id="mainFrame" name="mainFrame" src="<%=request.getContextPath()%>/jsp/public/introduce.jsp" frameborder="0" allowtransparency="true" width="100%" height="100%"></iframe> --%>
				<div id="fullpage">
					<div class="section framework">
						<div class="headline">
							<label class="title">框架</label>
							<label class="subtitle">组件多、开发快、体验好、架构优、稳定强的轻应用开发运行底层支撑平台</label>
						</div>
						<div>
							<img class="intro" src="<l:assetcontext/>/skins/skin/platform/img/intro_framework.png">
						</div>
					</div>
				</div>
			
			</div>
		</div>
	</div>
	
	<div class="portal-wrap" style="display: none;">
	    <div class="portal-content">
			<iframe id="portalFrame" name="portalFrame" src="<%=request.getContextPath()%>/jsp/public/portal.jsp" frameborder="0" allowtransparency="true" width="100%" height="100%"></iframe>
		</div>
	</div>
	
    <!-- 需要引用的JS -->
    <script type="text/javascript" src="<l:asset path='jquery.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='bootstrap.js'/>"></script>
    <script type="text/javascript" src="<l:asset path='form.js'/>"></script>
    <script  type="text/javascript" src="<l:asset path='ui.js'/>"></script>
    <script  type="text/javascript" src="<l:asset path='intro.js'/>"></script>
    <script  type="text/javascript" src="<l:asset path='home.js'/>"></script>
    
  </body>
</html>