(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-record-record"],{"30a6":function(e,t,r){"use strict";r("7a82");var a=r("4ea4").default;Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var s=a(r("2909"));r("ac1f"),r("841c"),r("d3b7"),r("159b"),r("d81d"),r("99af"),r("7db0");var i={data:function(){return{records:[],statusList:[{val:1,name:"入"},{val:2,name:"龙"},{val:3,name:"出"},{val:4,name:"虎"}],nstatusList:[{val:0,name:"无牛"},{val:1,name:"牛一"},{val:2,name:"牛二"},{val:3,name:"牛三"},{val:4,name:"牛四"},{val:5,name:"牛五"},{val:6,name:"牛六"},{val:7,name:"牛七"},{val:8,name:"牛八"},{val:9,name:"牛九"},{val:10,name:"牛牛"}],search:{pageIdx:0,pageSize:10},totalPage:1,totalCount:0,refresherTriggered:!1,userinfo:{},orgtype:"",userno:"",fdate:"",mode:0,tabs:[{clevel:0,name:"宝斗"},{clevel:1,name:"牛牛"}],loadMore:!1}},onLoad:function(e){this.userinfo=JSON.parse(uni.getStorageSync("userinfo")),this.orgtype=this.userinfo.orgtype,this.userno=this.userinfo.userno,this.records=[]},onShow:function(){this.mode=uni.getStorageSync("record_mode"),uni.removeStorageSync("record_mode"),1!=this.mode&&(this.mode=0),this.userinfo=JSON.parse(uni.getStorageSync("userinfo")),this.orgtype=this.userinfo.orgtype,this.userno=this.userinfo.userno,this.records=[],this.search.pageIdx=0,this.totalPage=1,this.totalCount=0,this.loadData()},methods:{findData:function(e){this.mode=e.clevel,this.search.pageIdx=0,this.totalPage=1,this.totalCount=0,this.records=[],this.loadData()},scrolltolower:function(){this.records.length>=this.totalCount||this.loadMore||(this.loadMore=!0,this.loadData())},getRefresherrefresh:function(){this.refresherTriggered=!0,this.search.pageIdx=0,this.totalPage=1,this.totalCount=0,this.records=[],this.loadData()},loadData:function(){var e=this,t="";1==this.orgtype?(t="/Query/SubOrdList",this.search.fdate=this.fdate):t="/Query/GetOrderList",this.search.userno=this.userno,this.search.mode=this.mode,this.$http.post(t,this.search,(function(t){var r=t.rData||[];r.forEach((function(t){if(t.bnno){var r=t.bnno.split(",")||[],a=t.result.split(",")||[];r=r.map((function(t,r){return{name:e.getTitle(r),status:e.getNstatus(t),nums:e.getNums(a,r)}})),t.bNos=r}})),e.records=[].concat((0,s.default)(e.records),(0,s.default)(r)),e.totalCount=t.iCount,e.search.pageIdx>=e.totalCount?e.search.pageIdx=e.totalCount+1:e.search.pageIdx=e.search.pageIdx+e.search.pageSize,e.refresherTriggered=!1,e.loadMore=!1}))},getNums:function(e,t){for(var r=[],a=0;a<5;a++)r[a]=e[t+a];return r},getNstatus:function(e){var t=this.nstatusList.find((function(t){return t.val==e}))||{};return t.name},getTitle:function(e){var t=[{val:0,name:"庄"},{val:1,name:"闲一"},{val:2,name:"闲二"},{val:3,name:"闲三"},{val:4,name:"闲四"},{val:5,name:"闲五"}].find((function(t){return t.val==e}))||{};return t.name},goBack:function(){1==this.mode?uni.navigateTo({url:"/pages/home/qmnn"}):uni.navigateTo({url:"/pages/home/qmbd"})},getStatus:function(e){var t=this.statusList.find((function(t){return t.val==e}))||{};return t.name}}};t.default=i},"5cec":function(e,t,r){"use strict";r.d(t,"b",(function(){return s})),r.d(t,"c",(function(){return i})),r.d(t,"a",(function(){return a}));var a={uniNavBar:r("73c5").default,uniCollapse:r("1926").default,uniCollapseItem:r("1507").default},s=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("v-uni-view",{staticClass:"order"},[r("uni-nav-bar",{attrs:{"left-icon":"left",title:"投注记录","background-color":"rgb(40,148,255)",color:"#fff",border:!1},on:{clickLeft:function(t){arguments[0]=t=e.$handleEvent(t),e.goBack.apply(void 0,arguments)}}}),e.fdate?r("v-uni-view",{staticClass:"search-date"},[e._v("查询日期："+e._s(e.fdate))]):e._e(),r("v-uni-view",{staticClass:"tab-bar"},e._l(e.tabs,(function(t,a){return r("v-uni-view",{key:a,staticClass:"tab-item",class:t.clevel==e.mode?"active":"",on:{click:function(r){arguments[0]=r=e.$handleEvent(r),e.findData(t)}}},[e._v(e._s(t.name))])})),1),r("v-uni-scroll-view",{staticStyle:{height:"95%"},attrs:{"scroll-y":"true","refresher-enabled":!1,"refresher-triggered":e.refresherTriggered,"refresher-background":"transparent"},on:{scrolltolower:function(t){arguments[0]=t=e.$handleEvent(t),e.scrolltolower.apply(void 0,arguments)},refresherrefresh:function(t){arguments[0]=t=e.$handleEvent(t),e.getRefresherrefresh.apply(void 0,arguments)}}},[r("v-uni-view",{staticClass:"record-list"},e._l(e.records,(function(t,a){return r("v-uni-view",{key:a,staticClass:"record-item"},[0==t.mode?[r("v-uni-view",{staticClass:"left"},[1==e.userinfo.orgtype?r("v-uni-view",{staticClass:"row"},[e._v("所属账号："+e._s(t.userno))]):e._e(),r("v-uni-view",{staticClass:"row"},[e._v("下注单号：No."+e._s(t.orderno))]),r("v-uni-view",{staticClass:"row"},[e._v("期数："+e._s(t.issue))]),r("v-uni-view",{staticClass:"row"},[e._v("开奖号码："),r("v-uni-text",{staticClass:"row-result"},[e._v(e._s(t.result))])],1),r("v-uni-view",{staticClass:"row"},[e._v("龙虎出入："+e._s(e.getStatus(t.bno)))]),r("v-uni-view",{staticClass:"row"},[e._v("游戏："),r("v-uni-text",{staticClass:"red"},[e._v("宝斗")])],1),r("v-uni-view",{staticClass:"row"},[e._v("玩法："),r("v-uni-text",{staticClass:"red"},[e._v(e._s(t.artid))])],1),r("v-uni-view",{staticClass:"row"},[e._v("倍率："),r("v-uni-text",{staticClass:"red"},[e._v(e._s(t.cpright))])],1),r("v-uni-view",{staticClass:"row"},[e._v("金额："),r("v-uni-text",{staticClass:"red"},[e._v(e._s(t.bailmoney))])],1),0==t.status?r("v-uni-view",{staticClass:"row"},[e._v("中奖金额："),t.bno?r("v-uni-text",{staticClass:"red"},[e._v(e._s(t.loss+t.bailmoney-t.comm))]):e._e()],1):e._e(),r("v-uni-view",{staticClass:"row"},[e._v("下注时间："+e._s(t.ordtime))]),1==t.status?r("v-uni-view",{staticClass:"row"},[e._v("注销时间："+e._s(t.cantime))]):e._e()],1),r("v-uni-view",{staticClass:"right"},[1==t.status?r("v-uni-view",{staticClass:"red"},[e._v("已注销")]):t.bno?r("v-uni-view",[t.loss<0?r("v-uni-view",{staticClass:"red"},[e._v("未中奖")]):0==t.loss?r("v-uni-view",{staticClass:"blue"},[e._v("打和")]):r("v-uni-view",{staticClass:"blue"},[e._v("中奖")])],1):r("v-uni-view",{staticClass:"red"},[e._v("未开奖")])],1)]:1==t.mode?[r("v-uni-view",{staticClass:"left"},[1==e.userinfo.orgtype?r("v-uni-view",{staticClass:"row"},[e._v("所属账号："+e._s(t.userno))]):e._e(),r("v-uni-view",{staticClass:"row"},[e._v("下注单号：No."+e._s(t.orderno))]),r("v-uni-view",{staticClass:"row"},[r("uni-collapse",[r("uni-collapse-item",{attrs:{"show-animation":!0,name:a},scopedSlots:e._u([{key:"title",fn:function(){return[r("v-uni-view",{staticClass:"col-title"},[r("v-uni-view",{staticClass:"left"},[e._v("期数："+e._s(t.issue))])],1)]},proxy:!0}],null,!0)},[r("v-uni-view",{staticClass:"col-content"},e._l(t.bNos,(function(t,a){return r("v-uni-view",{key:a,staticClass:"col-item"},[r("v-uni-view",{staticClass:"left"},[r("v-uni-view",{class:"left-name"+a},[e._v(e._s(t.name))]),r("v-uni-view",{class:"left-name"+a},[e._v(e._s(t.status))])],1),r("v-uni-view",{staticClass:"right"},e._l(t.nums,(function(t,a){return r("v-uni-view",{key:a,staticClass:"result-item",class:"color"+t},[e._v(e._s(t))])})),1)],1)})),1)],1)],1)],1),r("v-uni-view",{staticClass:"row"},[e._v("开奖号码："),r("v-uni-text",{staticClass:"row-result"},[e._v(e._s(t.result))])],1),r("v-uni-view",{staticClass:"row"},[e._v("游戏："),r("v-uni-text",{staticClass:"red"},[e._v("牛牛")])],1),r("v-uni-view",{staticClass:"row"},[e._v("玩法："),r("v-uni-text",{staticClass:"red"},[e._v(e._s(e.getTitle(t.artid))),0==t.recvno?r("v-uni-text",[e._v("（平投）")]):e._e(),1==t.recvno?r("v-uni-text",[e._v("（倍投）")]):e._e()],1)],1),r("v-uni-view",{staticClass:"row"},[e._v("倍率："),r("v-uni-text",{staticClass:"red"},[e._v(e._s(t.cpright))])],1),r("v-uni-view",{staticClass:"row"},[e._v("金额："),r("v-uni-text",{staticClass:"red"},[e._v(e._s(t.bailmoney))])],1),0==t.status?r("v-uni-view",{staticClass:"row"},[e._v("本局输赢："),t.loss>0?r("v-uni-text",{staticClass:"red"},[e._v(e._s(t.loss+t.bailmoney-t.comm))]):r("v-uni-text",{staticClass:"red"},[e._v(e._s(t.loss-t.comm))])],1):e._e(),r("v-uni-view",{staticClass:"row"},[e._v("下注时间："+e._s(t.ordtime))]),1==t.status?r("v-uni-view",{staticClass:"row"},[e._v("注销时间："+e._s(t.cantime))]):e._e()],1),r("v-uni-view",{staticClass:"right"},[1==t.status?r("v-uni-view",{staticClass:"red"},[e._v("已注销")]):0===t.comm&&0==t.loss?r("v-uni-view",{staticClass:"red"},[e._v("未开奖")]):r("v-uni-view",[t.loss<0?r("v-uni-view",{staticClass:"red"},[e._v("未中奖")]):0==t.loss?r("v-uni-view",{staticClass:"blue"},[e._v("打和")]):r("v-uni-view",{staticClass:"blue"},[e._v("中奖")])],1)],1)]:e._e()],2)})),1)],1)],1)},i=[]},"60bd8":function(e,t,r){"use strict";r.r(t);var a=r("5cec"),s=r("c2d3");for(var i in s)["default"].indexOf(i)<0&&function(e){r.d(t,e,(function(){return s[e]}))}(i);r("a261");var o=r("f0c5"),n=Object(o["a"])(s["default"],a["b"],a["c"],!1,null,"424bfbae",null,!1,a["a"],void 0);t["default"]=n.exports},a261:function(e,t,r){"use strict";var a=r("e352"),s=r.n(a);s.a},c2d3:function(e,t,r){"use strict";r.r(t);var a=r("30a6"),s=r.n(a);for(var i in a)["default"].indexOf(i)<0&&function(e){r.d(t,e,(function(){return a[e]}))}(i);t["default"]=s.a},e352:function(e,t,r){var a=r("eafa");a.__esModule&&(a=a.default),"string"===typeof a&&(a=[[e.i,a,""]]),a.locals&&(e.exports=a.locals);var s=r("4f06").default;s("9d977818",a,!0,{sourceMap:!1,shadowMode:!1})},eafa:function(e,t,r){var a=r("24fb");t=a(!1),t.push([e.i,'@charset "UTF-8";\r\n/**\r\n * 这里是uni-app内置的常用样式变量\r\n *\r\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\r\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\r\n *\r\n */\r\n/**\r\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\r\n *\r\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\r\n */\r\n/* 颜色变量 */\r\n/* 行为相关颜色 */\r\n/* 文字基本颜色 */\r\n/* 背景颜色 */\r\n/* 边框颜色 */\r\n/* 尺寸变量 */\r\n/* 文字尺寸 */\r\n/* 图片尺寸 */\r\n/* Border Radius */\r\n/* 水平间距 */\r\n/* 垂直间距 */\r\n/* 透明度 */\r\n/* 文章场景相关 */.order[data-v-424bfbae]{width:%?750?%;height:100vh}.order .tab-bar[data-v-424bfbae]{display:flex;width:%?670?%;padding-left:%?40?%;padding-right:%?40?%}.order .tab-bar .tab-item[data-v-424bfbae]{width:%?150?%;height:%?80?%;line-height:%?80?%;text-align:center;border-bottom:%?5?% solid #eee}.order .tab-bar .active[data-v-424bfbae]{border-bottom:%?5?% solid #2894ff}.order .search-date[data-v-424bfbae]{padding:%?20?%}.order .record-list .record-item[data-v-424bfbae]{background-color:#fff;padding:%?20?%;display:flex;justify-content:space-between;align-items:center;width:%?670?%;margin:%?10?% auto}.order .record-list .record-item .row-result[data-v-424bfbae]{font-size:%?26?%}.order .record-list .record-item .blue[data-v-424bfbae]{color:#00f}.order .record-list .record-item .red[data-v-424bfbae]{color:red}.order .record-list .record-item .right[data-v-424bfbae]{font-size:%?32?%;font-weight:600;padding-right:%?40?%}.order .record-list .record-item .left .col-title[data-v-424bfbae]{height:%?50?%;line-height:%?50?%}.order .record-list .record-item .left .col-content .col-item[data-v-424bfbae]{display:flex;justify-content:space-between;align-items:center}.order .record-list .record-item .left .col-content .col-item .left[data-v-424bfbae]{width:%?150?%;display:flex;justify-content:space-between}.order .record-list .record-item .left .col-content .col-item .left .left-name0[data-v-424bfbae]{color:red}.order .record-list .record-item .left .col-content .col-item .right[data-v-424bfbae]{display:flex}.order .record-list .record-item .left .col-content .col-item .right .result-item[data-v-424bfbae]{width:%?50?%;height:%?50?%;border-radius:50%;display:flex;justify-content:center;align-items:center;margin-bottom:%?20?%;color:#fff;margin-left:%?10?%}.order .record-list .record-item .left .color01[data-v-424bfbae]{background-color:gold}.order .record-list .record-item .left .color02[data-v-424bfbae]{background-color:#00bfff}.order .record-list .record-item .left .color03[data-v-424bfbae]{background-color:#8b4513}.order .record-list .record-item .left .color04[data-v-424bfbae]{background-color:orange}.order .record-list .record-item .left .color05[data-v-424bfbae]{background-color:#48d1cc}.order .record-list .record-item .left .color06[data-v-424bfbae]{background-color:#7b68ee}.order .record-list .record-item .left .color07[data-v-424bfbae]{background-color:#789}.order .record-list .record-item .left .color08[data-v-424bfbae]{background-color:#dc143c}.order .record-list .record-item .left .color09[data-v-424bfbae]{background-color:salmon}.order .record-list .record-item .left .color10[data-v-424bfbae]{background-color:#32cd32}',""]),e.exports=t}}]);