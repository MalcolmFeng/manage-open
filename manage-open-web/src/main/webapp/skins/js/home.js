	var topMenu = [];
	var leftMenu = [];
	var favoriteMenu = [];
	//指定初始加载的模块
	var initModelName="我的收藏";
	var showPortalFrame = false;
	var favoritesModel = {name:"Welcome",
						  iconPath:assetPath + "/skins/skin/platform/img/collect.png",
						  url:'/jsp/public/introduce.jsp'};
	//用户信息视图
	var userInfoView = {
		//data
    	userName :  ko.observable("系统管理员"),
    	//behaviors
    	userUpdate:function(data) {
    		userName(data.userName);
 		},
	};
    ko.applyBindings(userInfoView,$("#userInfo")[0]);
	
	function menuRender() {
    	// beautify scroller
		$('.ue-vmenu').slimScroll({
			height: $('.ue-vmenu').height()
		});
		// initialize menu
		$('.ue-vmenu').vmenu({
			autostart: false
		});
    }
	
	function afterRender(){
		//判断收藏菜单是否被加载，根据收藏菜单对叶子节点收藏图标进行渲染
		if(favoriteMenu.length ==0)
			getMenu(favoritesMenuView,"favoritesMenu"); 
		//获取所有叶子节点	
		var leafItem =$('.fa.fa-star-o.ue-vmenu-icon-r').parent().parent();
		for(var i= 0; i < leafItem.length; ++i){
    		for(var j = 0; j < favoriteMenu.length; ++j){
    			if(leafItem[i].id == favoriteMenu[j].menuId){
    				$(leafItem[i]).find("i:last-child")[0].className = "fa fa-star ue-vmenu-icon-r";
    				$(leafItem[i]).find("i:last-child")[0].title = "取消收藏"; 
    			}
    		}
    	}
	}
	//加载工作区页面
	function loadUrl(data) {
		var url = "";
		//点击左侧菜单标题
		if(data.pUrl!=null&&data.pUrl!=undefined){
			data.url = data.pUrl();
			if(data.tmpName()==""){
				data.text =  data.pMenuName();
			}else{
				data.text = data.tmpName();
			}
		}
		//叶子节点
		if(data.url!=null&&data.url!=undefined&&data.url!=""){
			$('.ue-right-top').text(data.text || data.name);
			if((data.url).indexOf("http:")>-1||(data.url).indexOf("https:")>-1)
				url = data.url;
			else
				url = context + data.url;
//	 		var ifram = "{text:'"+$('.ue-right-top').text()+"'"+",url:'"+data.url+"'"+"}";
//	 		setCookie("ifram",ifram);
	 		$('#mainFrame').attr('src', url);
		}
	  }
	//模块菜单视图
    var topMenuView = {
    	//data
    	menuList :  ko.observableArray([]),
    	hideList : ko.observableArray([]),
    	actionUrl : ko.observable("/service/home/topmenu"),
    	viewId :ko.observable("topMenuView"),
    	//behaviors
    	showMore:function() {
   		 		if( $("#topMore").text()=="更多"){
	   		 		this.menuList(this.menuList().concat(this.hideList()));
	   		 	    $("#topMore").text("收起");
   		 		}else if($("#topMore").text()=="收起"){
   		 			this.menuList(this.menuList().slice(0,6));
   		 			$("#topMore").text("更多");
   		 		}
   		    },
		getSubMenu: function (data){},
        topMenuIcon: function (menuItem){
        	return menuItem.icon ? assetPath+"/skins/skin/"+menuItem.icon : assetPath + "/skins/skin/platform/img/bsp.png";
        }
	 };
    
	 ko.applyBindings(topMenuView,$("#topMenu")[0]);
	//收藏菜单视图 
    var favoritesMenuView = {
    		//data
    		menuList: ko.observableArray([]),
    		hideList : ko.observableArray([]),
    		actionUrl : ko.observable("/service/home/menu/favourite"),
    		viewId :ko.observable("favoritesMenuView"),
   			//behaviors
 			showMore:function() {
   		 		if( $("#favoritesMore").text()=="更多"){
	   		 		this.menuList(this.menuList().concat(this.hideList()));
	   		 	    $("#favoritesMore").text("收起");
   		 		}else if($("#favoritesMore").text()=="收起"){
   		 			this.menuList(this.menuList().slice(0,6));
   		 			$("#favoritesMore").text("更多");
   		 		}
   		    },
   		    deleteFavorite:function(){
   		    	alert("delete!"); 		    	
   		    }
    }; 
    ko.applyBindings(favoritesMenuView,$("#favoritesMenu")[0]);
   //左边菜单视图
    var leftMenuView = {
    		//data
    		pMenuId : ko.observable(""),
    		pMenuName : ko.observable(initModelName),
    		PMenuIcon : ko.observable(favoritesModel.iconPath),
    		menuList: ko.observableArray([]),
   			actionUrl : ko.observable("/service/home/menu/submenu"),
   			viewId :ko.observable("leftMenuView"),
   			//模块没有配置url时，会使用第一个操作的url
   			pUrl : ko.observable('/jsp/public/introduce.jsp'),
   			//模块没有配置url时，保存第一个操作的名称，用于右边标题展示
   			tmpName: ko.observable("Welcome")
    }; 
    ko.applyBindings(leftMenuView,$("#leftMenu")[0]);
     /**
     * 获取菜单项
     * @param {ModelView} view 视图模型
     * @param {String} domId 展示模型的dom区域id
     */
	function getMenu(view,domId){}
    //页面初始化操作：事件绑定及数据加载
    $(function() {
    	//新手导航
    	var lastUser = getCookie("lastUser");
    	if(lastUser==null||lastUser!=userInfoView.userName()){
    		setCookie("lastUser",userInfoView.userName());
        	introJs().setOptions({
    		  	'nextLabel':'下一步 ',
    		  	'skipLabel':'跳过',
    		  	'doneLabel':'完成',
    		  	'showBullets':false,
    		  	'showStepNumbers': false
    		  })
    		  .start();
    	}    	   	
    	$('#favoritesMenu').hover(function(){
    			if(favoriteMenu.length ==0){
    			getMenu(favoritesMenuView,"favoritesMenu");  
    		}
    		$(this).children().show();
    	}, function() {
    		$(this).children().hide();
    		$($(this).children()[0]).show();
    	});
    	$('#topMenu').hover(function(){
    	    if(topMenu.length ==0){
    			getMenu(topMenuView,"topMenu");  
    		}
     	   $(this).children().show();
    	},function() {
    		$(this).children().hide();
    		$($(this).children()[0]).show();
    	});
    	$('#userInfo').hover(function(){
    		$($(this).children("div")).show(); 		
    	},function() {
    		$($(this).children("div")).hide();		
    	});
    	
    	//加载指定的初始模块
    	loadData();
    	showPortal(showPortalFrame);
    });
    
    /**
     * 保存对收藏菜单的操作
     * @param index 在leftMenu数组中的下标
     * @param data
     * @param event
     */
    function saveFavorites(index,data,event){}
    /**
     * 获取菜单项
     * @param {event} event 
     * @param {String} domId 展示模型的dom区域id
     */
    function sendFavorRequest(event,menuInfo,arg){}
    
    function logout(){}    
    
	Array.prototype.removeByIndex=function(index){
        if(isNaN(index)||index>=this.length){
            return false;
        }
        for(var i=0,n=0;i<this.length;i++){
            if(this[i]!=this[index]){
                this[n++]=this[i];
            }
        }
        this.length-=1;
    };
    
	Array.prototype.removeByMenuId=function(menuId){
		for(var i = 0 ; i < this.length; i++){
			if(this[i].menuId==menuId){
				this.removeByIndex(i);
				break;
			}
		}
    };
    
    function toggleSide() {
    	if (!$('.ue-menu-left').data('isClose')) {
        	$('.ue-menu-right').css({'border-left':'1px solid #ddd'}).animate({left:'0px'}, "slow");
        	$('.ue-menu-left').css({'z-index':'auto'});
        	$('.ue-menu-left').data('isClose', true);
    	} else {
        	$('.ue-menu-right').css({'border-left':'none'}).animate({left:'260px'}, "slow");
        	$('.ue-menu-left').data('isClose', false);
    	}
    }
    //主页面搜索框中提示信息处理
    $(function(){
	    $("#searchform-input").keyup(function(){
		      //当输入框有按键输入同时输入框不为空时，去掉背景图片；有按键输入同时为空时（删除字符），添加背景图片
		      destyle();
		     });
		$("#searchform-input").keydown(function(){
			//keydown事件可以在按键按下的第一时间去掉背景图片
			$("#searchform-input").removeClass("search_hint");
		});
    });
    //动态添加或移除提示信息
	function destyle(){
	      if($("#searchform-input").val() != ""){
	        $("#searchform-input").removeClass("search_hint");
	      }else{
	        $("#searchform-input").addClass("search_hint");
	      }
	}
    
    function showSearch(searchImg){
    }
    
    function searchCancel(){
    }
    //查询
    function doQuery(){}
    
    function query(e){
   	  var ev= window.event||e;
   	  //回车键
   	  if (ev.keyCode == 13) {
      //执行搜索
      doQuery();
   	  }
    }
    
    function showFavorites(data){}
    //根据initModelName加载模块
    function loadData(){}
    //根据showPortalFrame加载模块
    function showPortal(showPortalFrame){
    	if(showPortalFrame==null||showPortalFrame==""||showPortalFrame==false){
    		$(".portal-wrap").hide();
    		$(".ue-menu-wrap").show();
    	}else{
    		$(".ue-menu-wrap").hide();
    		$(".portal-wrap").show();
    	}
    }

    //保存菜单到cookie中
    function setCookie(name, value){
    	document.cookie = name + "=" + escape(value);
    }
    //获取cookie中的菜单
    function getCookie(name) {
    	var cookies = document.cookie;
    	var menu_cookies = cookies.split(';');
    	for (var i = 0; i <  menu_cookies.length; i++) {
    		var arr = menu_cookies[i].split("=");
    		if (arr[0] == name||arr[0] ==" "+name){
    			return unescape(arr[1]);
    		}
    	}
    	return null;
    }