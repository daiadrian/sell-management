<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线点餐后台管理系统</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/e3.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
<style type="text/css">
	.content {
		padding: 10px 10px 10px 10px;
	}
</style>
</head>
<body class="easyui-layout">
    <!-- 头部标题 -->
	<div data-options="region:'north',border:false" style="height:60px; padding:5px; background:#F3F3F3"> 
		<span class="northTitle">在线点餐后台管理系统</span>
	    <span class="loginInfo">登录用户：other&nbsp;&nbsp;姓名：管理员&nbsp;&nbsp;</span>
	</div>
    <div data-options="region:'west',title:'菜单',split:true" style="width:180px;">
    	<ul id="menu" class="easyui-tree" style="margin-top: 10px;margin-left: 5px;">
         	<li>
         		<span>实时订单处理</span>
         		<ul>
	         		<li data-options="attributes:{'url':'current-order'}">查看订单</li>
	         	</ul>
         	</li>
         </ul>
    </div>
    <div data-options="region:'center',title:''">
    	<div id="tabs" class="easyui-tabs">
		    <div title="首页" style="padding:20px;">
		        	<img alt="欢迎登录" src="${pageContext.request.contextPath}/images/ZHKUpage.png" align="middle">
		    </div>
		</div>
    </div>
    <!-- 页脚信息 -->
	<div data-options="region:'south',border:false" style="height:20px; background:#F3F3F3; padding:2px; vertical-align:middle;">
		<span id="sysVersion">系统版本：V1.0</span>
	    <span id="nowTime"></span>
	</div>
<script type="text/javascript">
$(function(){
	$('#menu').tree({
		onClick: function(node){
			if($('#menu').tree("isLeaf",node.target)){
				var tabs = $("#tabs");
				var tab = tabs.tabs("getTab",node.text);
				if(tab){
					tabs.tabs("select",node.text);
				}else{
					tabs.tabs('add',{
					    title:node.text,
					    href: node.attributes.url,
					    closable:true,
					    bodyCls:"content"
					});
				}
			}
		}
	});
});
setInterval("document.getElementById('nowTime').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",1000);

window.setInterval(togetcurOrder,10000);
function togetcurOrder() {
	$.ajax({
		url:"http://localhost:8081/order/current/untreated",
		type:"GET",
		data:null,
		dataType:"json",
		success:function (data) {
			if (data.status == 200) {
                if (data.data > 0) {
					$.messager.show({
						title: '订单提醒',
						msg: '你有' + data.data + '个未处理的订单 <a onclick="viewAllOrder()">查看订单</a>',
						timeout: 8000,
						showType: 'slide'
					});
            	}
			}
		}
	});
};


function viewAllOrder() {
    $("#tabs").tabs('add',{
        title:"实时订单",
        closable:true,
        href:"http://localhost:8083/current-order"
    });
}

</script>
</body>
</html>