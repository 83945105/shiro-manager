webpackJsonp([2],{"74Jh":function(e,n,a){"use strict";
/*!
 * Title: 跳转
 * Description:
 * author: 白超
 * date: 2018/1/26
 * version: v1.0
 */n.a={methods:{routerToPage:function(e){switch(e){case"Index":this.pageToHome();break;case"ModuleManager":this.pageToModuleManager();break;default:alert("你还未设置这个头部菜单页面转路由跳转")}},pageToHome:function(){window.location.href="home#/index"},pageToModuleManager:function(){window.location.href="module-manager#/moduleManager"},pageToUserManager:function(e){window.open("authority-manager#/userManager/"+e)},pageToRoleManager:function(e){window.open("authority-manager#/roleManager/"+e)},pageToResManager:function(e){window.open("authority-manager#/resManager/"+e)}}}},Kls4:function(e,n){},NR9i:function(e,n){},lbmx:function(e,n){e.exports=[{label:"首页",routerName:"Index"},{label:"模块管理",routerName:"ModuleManager"}]},w2jM:function(e,n,a){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var t=a("MVMM"),o=a("zO6J"),r=a("aA9S"),i=a.n(r),l=a("s/sy"),s=a("5s8w"),u=a("8fC+"),d=a("Zci8"),c=(a("qkBV"),a("K+Rl"),a("aZ1k"),a("0Eag"),a("o+a0")),m=a.n(c),f=(a("Y8I5"),a("fD5l")),g=a.n(f),v=(a("jqXw"),a("2rb8")),w=a.n(v),p=a("HVJf"),M=a("FvRi");
/*!
 * Title: 状态管理
 * Description:
 * author: 白超
 * date: 2017/12/20
 * version: v1.0
 */
t.default.use(p.a);var h=new p.a.Store({modules:{userModule:M.a}}),x=a("GTdf");window.projectConf=a("saw0");var b=a("HBrH");window.events=new b,window.events.setMaxListeners(100),i()(window,{ManagerUrl:s.a,ModuleManagerUrl:u.a,ResourceManagerUrl:d.a}),t.default.use(m.a),t.default.use(g.a);var _=new v.Message.WeView;t.default.use(w.a,{Ajax:{options:{baseURL:a("qk+/").getBaseUrl(),withCredentials:!0,dataParserOptions:{use:v.DataParser.DataView,options:{needLoginOptions:{callback:function(e,n){}},noAuthorityOptions:{callback:function(e,n){_.open(e.resultInfo.message)}},notFoundOptions:{callback:function(e,n){_.open(e.resultInfo.message)}}}}}}});var k={name:"layout",components:{LoginWindow:l.a},store:h,mixins:[x.a],data:function(){return{loginWindowModel:!1}},created:function(){var e=this;window.events.on("showLoginWindow",function(){return e.loginWindowModel=!0}),this.getCurrentCertificate()}},I={render:function(){var e=this,n=e.$createElement,a=e._self._c||n;return a("div",[a("router-view"),e._v(" "),a("login-window",{model:{value:e.loginWindowModel,callback:function(n){e.loginWindowModel=n},expression:"loginWindowModel"}})],1)},staticRenderFns:[]},C=a("vSla")(k,I,!1,null,null,null).exports,L=a("4YfN"),T=a.n(L),R=a("FTBU"),y=a("74Jh"),H=a("lbmx"),O=a.n(H),W=a("Fm0B"),F=a("wwgf"),J=a("tov0"),N=a("cI7i"),P={components:{MerlinTabItem:a("J0gf").a,MerlinTabPanel:N.a,MerlinHeaderItem:J.a,MerlinHeaderMenu:F.a,MerlinLeftItem:W.a,MerlinLeftMenu:R.a},name:"manager",mixins:[x.a,y.a],data:function(){return{HeaderJson:O.a}},computed:T()({},Object(p.b)(["userModule"])),methods:{handleClickHeaderItem:function(e){this.routerToPage(e.routerName)},handleClickLeftItem:function(e){this.$router.push({name:e.routerName})}},created:function(){}},j={render:function(){var e=this,n=e.$createElement,t=e._self._c||n;return t("div",[t("merlin-header-menu",[e._l(e.HeaderJson,function(n,a){return[t("merlin-header-item",{key:a,attrs:{label:n.label,value:e.$route.name.indexOf(n.routerName)>=0},on:{click:function(a){e.handleClickHeaderItem(n)}}})]}),e._v(" "),t("template",{slot:"right"},[!1===e.userModule.isLogin?t("div",{staticClass:"avalon-merlin-default-userinfo"},[t("h3",{staticClass:"avalon-merlin-default-username avalon-merlin-ellipsis",on:{click:function(n){return n.stopPropagation(),e.handleShowLoginWindow(n)}}},[e._v("登录")])]):t("div",{staticClass:"avalon-merlin-default-userinfo"},[t("img",{staticClass:"avalon-merlin-default-face avalon-merlin-circle",attrs:{src:a("3DOL")}}),e._v(" "),t("h3",{staticClass:"avalon-merlin-default-username avalon-merlin-ellipsis"},[e._v("您好，"+e._s(e.userModule.username))])]),e._v(" "),!0===e.userModule.isLogin?t("ul",{staticClass:"avalon-merlin-default-sys-function"},[t("li",{on:{click:function(n){return n.stopPropagation(),e.logout(n)}}},[t("i",{staticClass:"merlin merlinfont merlin-tuichu"})])]):e._e()])],2),e._v(" "),t("router-view")],1)},staticRenderFns:[]};var S=a("vSla")(P,j,!1,function(e){a("Kls4")},"data-v-511630eb",null).exports,U={render:function(){var e=this.$createElement;return(this._self._c||e)("h1",[this._v("欢迎使用")])},staticRenderFns:[]};var $=a("vSla")({name:"Index"},U,!1,function(e){a("NR9i")},"data-v-90d54fee",null).exports;
/*!
 * Title:
 * Description: 路由管理
 * author: 白超
 * date: 2017/12/5
 * version: v1.0
 */
t.default.use(o.a);var B=new o.a({routes:[{path:"/",redirect:{name:"Layout"}},{path:"/",name:"Layout",component:C,redirect:{name:"Manager"},children:[{path:"/manager",name:"Manager",component:S,redirect:{name:"Index"},children:[{path:"/index",name:"Index",component:$}]}]}]}),E={render:function(){var e=this.$createElement,n=this._self._c||e;return n("div",{attrs:{id:"index"}},[n("router-view")],1)},staticRenderFns:[]},V=a("vSla")({name:"index"},E,!1,null,null,null).exports;
/*!
 * Title: 系统模块
 * Description:
 * author: 白超
 * date: 2017/12/5
 * version: v1.0
 */
new t.default({el:"#index",router:B,template:"<index/>",components:{Index:V}})}},["w2jM"]);