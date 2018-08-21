webpackJsonp([1],{"1aEh":function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=i("MVMM"),l=i("zO6J"),r=i("aA9S"),o=i.n(r),n=i("s/sy"),s=i("5s8w"),u=i("8fC+"),d=i("Zci8"),c=(i("qkBV"),i("K+Rl"),i("aZ1k"),i("0Eag"),i("o+a0")),m=i.n(c),f=(i("Y8I5"),i("fD5l")),v=i.n(f),h=(i("jqXw"),i("2rb8")),M=i.n(h),p=i("HVJf"),g=i("FvRi");
/*!
 * Title: 状态管理
 * Description:
 * author: 白超
 * date: 2017/12/20
 * version: v1.0
 */
a.default.use(p.a);var w=new p.a.Store({modules:{userModule:g.a}}),x=i("GTdf");window.projectConf=i("saw0");var b=i("HBrH");window.events=new b,window.events.setMaxListeners(100),o()(window,{ManagerUrl:s.a,ModuleManagerUrl:u.a,ResourceManagerUrl:d.a}),a.default.use(m.a),a.default.use(v.a);var I=new h.Message.WeView;a.default.use(M.a,{Ajax:{options:{baseURL:i("qk+/").getBaseUrl(),withCredentials:!0,dataParserOptions:{use:h.DataParser.DataView,options:{needLoginOptions:{callback:function(e,t){window.events.emit("showLoginWindow")}},noAuthorityOptions:{callback:function(e,t){I.open(e.resultInfo.message)}},notFoundOptions:{callback:function(e,t){I.open(e.resultInfo.message)}}}}}}});var _={name:"layout",components:{LoginWindow:n.a},store:w,mixins:[x.a],data:function(){return{loginWindowModel:!1}},created:function(){var e=this;window.events.on("showLoginWindow",function(){return e.loginWindowModel=!0}),this.getCurrentCertificate()}},S={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("router-view"),e._v(" "),i("login-window",{model:{value:e.loginWindowModel,callback:function(t){e.loginWindowModel=t},expression:"loginWindowModel"}})],1)},staticRenderFns:[]},F=i("vSla")(_,S,!1,null,null,null).exports,C=i("4YfN"),k=i.n(C),L=i("FTBU"),y=i("74Jh"),N=i("ih1J"),E=i.n(N),$=i("Fm0B"),T=i("wwgf"),P=i("tov0"),D=i("cI7i"),O={components:{MerlinTabItem:i("J0gf").a,MerlinTabPanel:D.a,MerlinHeaderItem:P.a,MerlinHeaderMenu:T.a,MerlinLeftItem:$.a,MerlinLeftMenu:L.a},name:"manager",mixins:[x.a,y.a],data:function(){return{HeaderJson:E.a}},computed:k()({},Object(p.b)(["userModule"])),methods:{handleClickHeaderItem:function(e){this.routerToPage(e.routerName)},handleClickLeftItem:function(e){this.$router.push({name:e.routerName})}},created:function(){}},U={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("merlin-header-menu",[e._l(e.HeaderJson,function(t,i){return[a("merlin-header-item",{key:i,attrs:{label:t.label,value:e.$route.name.indexOf(t.routerName)>=0},on:{click:function(i){e.handleClickHeaderItem(t)}}})]}),e._v(" "),a("template",{slot:"right"},[!1===e.userModule.isLogin?a("div",{staticClass:"avalon-merlin-default-userinfo"},[a("h3",{staticClass:"avalon-merlin-default-username avalon-merlin-ellipsis",on:{click:function(t){return t.stopPropagation(),e.handleShowLoginWindow(t)}}},[e._v("登录")])]):a("div",{staticClass:"avalon-merlin-default-userinfo"},[a("img",{staticClass:"avalon-merlin-default-face avalon-merlin-circle",attrs:{src:i("3DOL")}}),e._v(" "),a("h3",{staticClass:"avalon-merlin-default-username avalon-merlin-ellipsis"},[e._v("您好，"+e._s(e.userModule.username))])]),e._v(" "),!0===e.userModule.isLogin?a("ul",{staticClass:"avalon-merlin-default-sys-function"},[a("li",{on:{click:function(t){return t.stopPropagation(),e.logout(t)}}},[a("i",{staticClass:"merlin merlinfont merlin-tuichu"})])]):e._e()])],2),e._v(" "),a("router-view")],1)},staticRenderFns:[]};var R=i("vSla")(O,U,!1,function(e){i("uWOM")},"data-v-3c75ff62",null).exports,j=i("XQXf"),W={id:null,path:null,serviceId:null,serviceName:null,url:null,loginUrl:null,index:null,status:j.a.data().dict.status.normal},B={name:"module-manager",mixins:[j.a,y.a],data:function(){var e=this;return{serviceIdSuffix:"-service",pathPrefix:"/api-",pathSuffix:"/**",newModuleModel:!1,newModuleLoading:!1,newModule:{ruleForm:o()({},W),rules:{serviceId:[{required:!0,message:"请输入服务ID",trigger:"blur"},{min:3,max:20,message:"长度在 3 到 20 个字符",trigger:"blur"},{validator:function(t,i,a){e.$ajax.post(ModuleManagerUrl.post.validateServiceId,{serviceId:i+"-service"}).success(!0,function(){return a()}).fail(!0,function(e){a(new Error(e.record))}).notSuccess(function(){a(new Error("校验失败"))})},trigger:"blur"}],serviceName:[{required:!0,message:"请输入服务名称",trigger:"blur"},{min:3,max:20,message:"长度在 3 到 20 个字符",trigger:"blur"}],path:[{required:!0,message:"请输入根路径",trigger:"blur"},{min:3,max:20,message:"长度在 3 到 20 个字符",trigger:"blur"}]}},editModuleModel:!1,editModuleLoading:!1,editModule:{editModuleId:null,editModuleIndex:-1,ruleForm:o()({},W),rules:{serviceId:[{required:!0,message:"请输入服务ID",trigger:"blur"},{min:3,max:20,message:"长度在 3 到 20 个字符",trigger:"blur"}],serviceName:[{required:!0,message:"请输入服务名称",trigger:"blur"},{min:3,max:20,message:"长度在 3 到 20 个字符",trigger:"blur"}],path:[{required:!0,message:"请输入根路径",trigger:"blur"},{min:3,max:20,message:"长度在 3 到 20 个字符",trigger:"blur"}]}},form:{id:"",name:"",rootPath:"",status:""},moduleList:[]}},watch:{"newModule.ruleForm.serviceId":{handler:function(e){this.newModule.ruleForm.path=e},deep:!0}},methods:{getModuleList:function(){var e=this;this.$ajax.get(ModuleManagerUrl.get.moduleList).success(!0,function(t){e.moduleList=t.rows})},handleClickNewModuleConfirmButton:function(){var e=this;this.$refs.newModule.validate(function(t){t&&(e.newModuleLoading=!0,e.$ajax.post(ModuleManagerUrl.post.newModule,o()({serviceIdSuffix:e.serviceIdSuffix,pathPrefix:e.pathPrefix,pathSuffix:e.pathSuffix},e.newModule.ruleForm,{id:e.newModule.ruleForm.path,serviceId:""+e.newModule.ruleForm.serviceId+e.serviceIdSuffix,path:""+e.pathPrefix+e.newModule.ruleForm.path+e.pathSuffix})).success(function(){e.$refs.newModule.resetFields(),e.getModuleList()}).finally(function(){return e.newModuleLoading=!1}))})},showEditModule:function(e,t){var i=e.serviceId.substring(0,e.serviceId.length-this.serviceIdSuffix.length);o()(this.editModule.ruleForm,{serviceId:i,serviceName:e.serviceName,path:i,status:e.status}),this.editModule.editModuleId=e.id,this.editModule.editModuleIndex=t,this.editModuleModel=!0},handleClickEditModuleConfirmButton:function(){var e=this;this.$refs.editModule.validate(function(t){t&&(e.editModuleLoading=!0,e.$ajax.put(ModuleManagerUrl.put.editModule,o()({},e.editModule.ruleForm,{id:e.editModule.editModuleId,serviceId:""+e.editModule.ruleForm.serviceId+e.serviceIdSuffix,path:""+e.pathPrefix+e.editModule.ruleForm.path+e.pathSuffix})).success(function(){o()(e.moduleList[e.editModule.editModuleIndex],{serviceName:e.editModule.ruleForm.serviceName,status:e.editModule.ruleForm.status})}).finally(function(){return e.editModuleLoading=!1}))})},handleClickDeleteModule:function(e,t){var i=this;this.$confirm('你确定要删除该模块及相关数据吗?<div style="color: red">删除后无法恢复！！！</div>',function(a,l){i.$ajax.delete(ModuleManagerUrl.delete.deleteModule,{id:e}).success(function(){i.moduleList.splice(t,1),l()})})}},created:function(){this.getModuleList()}},H={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"avalon-merlin-default-main-content avalon-merlin-v-scroll",staticStyle:{"background-color":"#F9F9F9"}},[i("div",{staticClass:"module-list-main"},[i("ul",{staticClass:"module-list"},[e._l(e.moduleList,function(t,a){return i("li",[i("div",{staticClass:"module-tools"},[i("div",{staticClass:"tools-left"},["root-service"!==t.serviceId?i("i",{staticClass:"el-icon-edit",on:{click:function(i){e.showEditModule(t,a)}}}):e._e(),e._v(" "),e._e()]),e._v(" "),"root-service"!==t.serviceId?i("div",{staticClass:"tools-right",on:{click:function(i){e.handleClickDeleteModule(t.id,a)}}},[i("i",{staticClass:"el-icon-delete"})]):e._e()]),e._v(" "),i("div",{staticClass:"module-one",on:{click:function(i){e.pageToUserManager(t.id)}}},[i("ul",{staticClass:"module-info-form"},[i("li",[i("em",{staticClass:"info-title tar"},[e._v("服务ID：")]),e._v(" "),i("div",{staticClass:"info-content ellipsis"},[e._v(e._s(t.serviceId))])]),e._v(" "),i("li",[i("em",{staticClass:"info-title tar"},[e._v("服务名：")]),e._v(" "),i("div",{staticClass:"info-content ellipsis"},[e._v(e._s(t.serviceName))])]),e._v(" "),i("li",[i("em",{staticClass:"info-title tar"},[e._v("根路径：")]),e._v(" "),i("div",{staticClass:"info-content ellipsis"},[e._v(e._s(t.path))])]),e._v(" "),i("li",[i("em",{staticClass:"info-title tar"},[e._v("状态：")]),e._v(" "),i("div",{staticClass:"info-content ellipsis"},[i("em",{directives:[{name:"show",rawName:"v-show",value:t.status===e.dict.status.normal,expression:"row.status === dict.status.normal"}],staticClass:"fc-normal"},[e._v("正常")]),e._v(" "),i("em",{directives:[{name:"show",rawName:"v-show",value:t.status===e.dict.status.disabled,expression:"row.status === dict.status.disabled"}],staticClass:"fc-disable"},[e._v("禁用")])])])])])])}),e._v(" "),i("li",{on:{click:function(t){e.newModuleModel=!0}}},[e._m(0)])],2),e._v(" "),i("we-layer",{attrs:{width:500,height:400,position:"center",drag:"",title:"新建模块","confirm-button-loading":e.newModuleLoading},on:{"click-confirm":e.handleClickNewModuleConfirmButton},model:{value:e.newModuleModel,callback:function(t){e.newModuleModel=t},expression:"newModuleModel"}},[i("div",{staticClass:"form-padding"},[i("el-form",{ref:"newModule",attrs:{model:e.newModule.ruleForm,rules:e.newModule.rules,"label-width":"90px"}},[i("el-form-item",{attrs:{label:"服务ID:",prop:"serviceId"}},[i("el-input",{staticStyle:{width:"70%"},attrs:{label:"right"},model:{value:e.newModule.ruleForm.serviceId,callback:function(t){e.$set(e.newModule.ruleForm,"serviceId",t)},expression:"newModule.ruleForm.serviceId"}}),e._v(" "),i("el-input",{staticStyle:{width:"24%"},attrs:{label:"right",disabled:""},model:{value:e.serviceIdSuffix,callback:function(t){e.serviceIdSuffix=t},expression:"serviceIdSuffix"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"服务名称:",prop:"serviceName"}},[i("el-input",{staticStyle:{width:"95%"},attrs:{label:"right"},model:{value:e.newModule.ruleForm.serviceName,callback:function(t){e.$set(e.newModule.ruleForm,"serviceName",t)},expression:"newModule.ruleForm.serviceName"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"根路径:",prop:"path"}},[i("el-input",{staticStyle:{width:"24%"},attrs:{label:"right",disabled:""},model:{value:e.pathPrefix,callback:function(t){e.pathPrefix=t},expression:"pathPrefix"}}),e._v(" "),i("el-input",{staticStyle:{width:"45%"},attrs:{label:"right",readonly:""},model:{value:e.newModule.ruleForm.path,callback:function(t){e.$set(e.newModule.ruleForm,"path",t)},expression:"newModule.ruleForm.path"}}),e._v(" "),i("el-input",{staticStyle:{width:"24%"},attrs:{label:"right",disabled:""},model:{value:e.pathSuffix,callback:function(t){e.pathSuffix=t},expression:"pathSuffix"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"状态:"}},[i("el-switch",{attrs:{"active-value":e.dict.status.normal,"inactive-value":e.dict.status.disabled,"active-text":"启用","inactive-text":"禁用"},model:{value:e.newModule.ruleForm.status,callback:function(t){e.$set(e.newModule.ruleForm,"status",t)},expression:"newModule.ruleForm.status"}})],1)],1)],1)]),e._v(" "),i("we-layer",{attrs:{width:500,height:400,position:"center",drag:"",title:"编辑模块","confirm-button-loading":e.editModuleLoading},on:{"click-confirm":e.handleClickEditModuleConfirmButton},model:{value:e.editModuleModel,callback:function(t){e.editModuleModel=t},expression:"editModuleModel"}},[i("div",{staticClass:"form-padding"},[i("el-form",{ref:"editModule",attrs:{model:e.editModule.ruleForm,rules:e.editModule.rules,"label-width":"90px"}},[i("el-form-item",{attrs:{label:"服务ID:",prop:"serviceId"}},[i("el-input",{staticStyle:{width:"70%"},attrs:{label:"right",disabled:""},model:{value:e.editModule.ruleForm.serviceId,callback:function(t){e.$set(e.editModule.ruleForm,"serviceId",t)},expression:"editModule.ruleForm.serviceId"}}),e._v(" "),i("el-input",{staticStyle:{width:"24%"},attrs:{label:"right",disabled:""},model:{value:e.serviceIdSuffix,callback:function(t){e.serviceIdSuffix=t},expression:"serviceIdSuffix"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"服务名称:",prop:"serviceName"}},[i("el-input",{staticStyle:{width:"95%"},attrs:{label:"right"},model:{value:e.editModule.ruleForm.serviceName,callback:function(t){e.$set(e.editModule.ruleForm,"serviceName",t)},expression:"editModule.ruleForm.serviceName"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"根路径:",prop:"path"}},[i("el-input",{staticStyle:{width:"24%"},attrs:{label:"right",disabled:""},model:{value:e.pathPrefix,callback:function(t){e.pathPrefix=t},expression:"pathPrefix"}}),e._v(" "),i("el-input",{staticStyle:{width:"45%"},attrs:{label:"right",readonly:""},model:{value:e.editModule.ruleForm.path,callback:function(t){e.$set(e.editModule.ruleForm,"path",t)},expression:"editModule.ruleForm.path"}}),e._v(" "),i("el-input",{staticStyle:{width:"24%"},attrs:{label:"right",disabled:""},model:{value:e.pathSuffix,callback:function(t){e.pathSuffix=t},expression:"pathSuffix"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"状态:"}},[i("el-switch",{attrs:{"active-value":e.dict.status.normal,"inactive-value":e.dict.status.disabled,"active-text":"启用","inactive-text":"禁用"},model:{value:e.editModule.ruleForm.status,callback:function(t){e.$set(e.editModule.ruleForm,"status",t)},expression:"editModule.ruleForm.status"}})],1)],1)],1)])],1)])},staticRenderFns:[function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"module-one"},[t("i",{staticClass:"module-add-btn el-icon-plus"})])}]};var J=i("vSla")(B,H,!1,function(e){i("8yvj")},"data-v-e5dc8aa6",null).exports;
/*!
 * Title:
 * Description: 路由管理
 * author: 白超
 * date: 2017/12/5
 * version: v1.0
 */
a.default.use(l.a);var q=new l.a({routes:[{path:"/",redirect:{name:"Layout"}},{path:"/",name:"Layout",component:F,redirect:{name:"Manager"},children:[{path:"/manager",name:"Manager",component:R,redirect:{name:"ModuleManager"},children:[{path:"/moduleManager",name:"ModuleManager",component:J}]}]}]}),A={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{attrs:{id:"index"}},[t("router-view")],1)},staticRenderFns:[]},V=i("vSla")({name:"index"},A,!1,null,null,null).exports;
/*!
 * Title: 系统模块
 * Description:
 * author: 白超
 * date: 2017/12/5
 * version: v1.0
 */
new a.default({el:"#index",router:q,template:"<index/>",components:{Index:V}})},"74Jh":function(e,t,i){"use strict";
/*!
 * Title: 跳转
 * Description:
 * author: 白超
 * date: 2018/1/26
 * version: v1.0
 */t.a={methods:{routerToPage:function(e){switch(e){case"Index":this.pageToHome();break;case"ModuleManager":this.pageToModuleManager();break;default:alert("你还未设置这个头部菜单页面转路由跳转")}},pageToHome:function(){window.location.href="home#/index"},pageToModuleManager:function(){window.location.href="module-manager#/moduleManager"},pageToUserManager:function(e){window.open("authority-manager#/userManager/"+e)},pageToRoleManager:function(e){window.open("authority-manager#/roleManager/"+e)},pageToResManager:function(e){window.open("authority-manager#/resManager/"+e)}}}},"8yvj":function(e,t){},XQXf:function(e,t,i){"use strict";t.a={data:function(){return{dict:{status:{normal:"NORMAL",enabled:"ENABLED",disabled:"DISABLED",deleted:"DELETED"},searchType:{globalSearch:"GlobalSearch",treeSearch:"TreeSearch"},searchTypes:[{text:"全局查询",value:"GlobalSearch"},{text:"树查询",value:"TreeSearch"}],resourceType:{url:"URL",permission:"PERMISSION",node:"NODE"},resourceTypes:[{text:"统一资源定位符",value:"URL"},{text:"资源许可(Shiro注解用)",value:"PERMISSION"},{text:"节点",value:"NODE"}],roleType:{local:"LOCAL"},roleTypes:[{text:"本地角色",value:"LOCAL"}]}}}}},ih1J:function(e,t){e.exports=[{label:"首页",routerName:"Index"},{label:"模块管理",routerName:"ModuleManager"}]},uWOM:function(e,t){}},["1aEh"]);