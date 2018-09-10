<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<span>按条件查找</span><br/>
<form>
    订单ID:<input type="text" name="orderId" id="theOrderId"/> &nbsp;&nbsp;&nbsp;
    用户名:<input type="text" name="username" id="theOrderUserName"/> &nbsp;&nbsp;&nbsp;
    <input type="button" onclick="findOrderIdOrUsernameWithOrderList()" value="查询"/>
    <input type="button" onclick="findAllCompleteOrderList()" value="查询所有交易完成的订单"/>
</form>
<table class="easyui-datagrid" id="orderList" title="订单列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/order/list',method:'get',pageSize:30,toolbar:toolorderbar">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true"></th>
        <th data-options="field:'orderId',width:60">订单ID</th>
        <th data-options="field:'payment',width:200">实付金额</th>
        <th data-options="field:'paymentType',width:100">就餐类型</th>
        <th data-options="field:'userName',width:100">用户姓名</th>
        <th data-options="field:'status',width:60,align:'center'">状态</th>
        <th data-options="field:'paymentTime',width:130,align:'center',formatter:E3.formatDateTime">付款时间</th>
        <th data-options="field:'consignTime',width:130,align:'center',formatter:E3.formatDateTime">完成日期</th>
        <th data-options="field:'createTime',width:130,align:'center',formatter:E3.formatDateTime">创建日期</th>
        <th data-options="field:'updateTime',width:130,align:'center',formatter:E3.formatDateTime">更新日期</th>
    </tr>
    </thead>
</table>
<div id="orderEditWindow" class="easyui-window" title="查看订单"
     data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/order-edit'"
     style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function findAllCompleteOrderList() {
        $("#orderList").datagrid({
            singleSelect: false,
            collapsible: true,
            pagination: true,
            url: '/order/findAllCompleteOrderList',
            method: 'get',
            pageSize: 30,
            toolbar: toolbar
        });
    };

    function findOrderIdOrUsernameWithOrderList() {
        var theOrderId = $("#theOrderId").val();
        var theOrderUserName = $("#theOrderUserName").val();
        if (undefined == theOrderId) {
            theOrderId = 0;
        }
        if (undefined == theOrderUserName) {
            theOrderUserName = "";
        }
        $("#orderList").datagrid({
            singleSelect: false,
            collapsible: true,
            pagination: true,
            url: '/order/findOrderIdOrUsernameWithOrderList?orderId=' + theOrderId + '&username=' + theOrderUserName,
            method: 'get',
            pageSize: 30,
            toolbar: toolbar
        });
    }

    function getSelectionsOrderIds() {
        var orderList = $("#orderList");
        var sels = orderList.datagrid("getSelections");
        var ids = [];
        for (var i in sels) {
            ids.push(sels[i].orderId);
        }
        ids = ids.join(",");
        return ids;
    }

    var toolorderbar = [{
        text: '查看详情',
        iconCls: 'icon-edit',
        handler: function () {
            var ids = getSelectionsOrderIds();
            if (ids.length == 0) {
                $.messager.alert('提示', '必须选择一个订单才能查看!');
                return;
            }
            if (ids.indexOf(',') > 0) {
                $.messager.alert('提示', '只能选择一个订单!');
                return;
            }

            //回显数据
            var data;
            var restatus;
            $.ajax({
                url: "${pageContext.request.contextPath}/order/rest/findDetail/" + ids,
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

            $("#orderEditWindow").window({
                onLoad: function () {
                    $("#orderEditForm").form("load", data);
                }
            }).window("open");
        }
    }, {
        text: '取消',
        iconCls: 'icon-cancel',
        handler: function () {
            var ids = getSelectionsOrderIds();
            if (ids.length == 0) {
                $.messager.alert('提示', '未选中订单!');
                return;
            }
            $.messager.confirm('确认', '确定取消ID为 ' + ids + ' 的订单吗？', function (r) {
                if (r) {
                    var params = {"ids": ids};
                    $.post("/order/rest/delete", params, function (data) {
                        if (data.status == 200) {
                            $.messager.alert('提示', '取消订单成功!', undefined, function () {
                                $("#orderList").datagrid("reload");
                            });
                        }
                    });
                }
            });
        }
    }];
</script>