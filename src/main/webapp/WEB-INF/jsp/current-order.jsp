<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="curorderList" title="实时订单列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/order/current/list',method:'get',pageSize:30,toolbar:toolcurorderbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'orderId',width:60">订单ID</th>
            <th data-options="field:'paymentType',width:100">就餐类型</th>
            <th data-options="field:'createTime',width:130">创建日期</th>
        </tr>
    </thead>
</table>
<div id="curorderEditWindow" class="easyui-window" title="订单详情" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/current-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function getSelectionsCurOrderIds(){
    	var curorderList = $("#curorderList");
    	var sels = curorderList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].orderId);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolcurorderbar = [{
        text:'查看详情',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsCurOrderIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个订单才能查看!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个订单!');
        		return ;
        	}

            //回显数据
            var data;
            var restatus;
            $.ajax({
                url: "${pageContext.request.contextPath}/order/rest/findCurOrderDetail/" + ids,
                type: "GET",
                data: null,
                dataType: "json",
                success: function (redata) {
                    restatus == redata.status;
                    if (redata.status == 200) {
                        data = redata.data;
                    }
                }
            });

            $("#curorderEditWindow").window({
                onLoad: function () {
                    $("#curorderEditForm").form("load", data);
                }
            }).window("open");
        }
    }];
</script>