<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="orderEditForm" class="itemForm" method="post">
		<input type="hidden" name="id"/>
	    <table cellpadding="5">
	        <tr>
	            <td>订单ID:</td>
	            <td><input class="easyui-textbox" type="text" readonly="readonly" name="orderId" style="width: 280px;"/></td>
	        </tr>
	        <tr>
	            <td>实付金额:</td>
	            <td><input class="easyui-textbox" name="payment" type="text" readonly="readonly" style="width: 280px;"/></td>
	        </tr>
			<c:if test="${sessionScope.get('paymentType') eq '堂食'}">
				<tr>
					<td>堂食单号:</td>
					<td><input class="easyui-textbox" type="text" name="shoppingCode" readonly="readonly" /></td>
				</tr>
			</c:if>
			<c:if test="${sessionScope.get('paymentType') eq '外卖'}">
				<tr>
					<td>配送地址:</td>
					<td><input class="easyui-textbox" type="text" name="useraddress" readonly="readonly"  style="width: 280px;"/></td>
				</tr>
				<tr>
					<td>收餐人姓名:</td>
					<td><input class="easyui-textbox" type="text" name="username" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>配送费:</td>
					<td><input class="easyui-textbox" type="text" name="postFee" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>收餐人电话:</td>
					<td><input class="easyui-textbox" type="text" name="userPhone" readonly="readonly" /></td>
				</tr>
			</c:if>
	        <tr>
	            <td>订单状态:</td>
	            <td><input class="easyui-textbox" type="text" name="status" readonly="readonly" /></td>
	        </tr>
	        <tr>
	            <td>订单详细:</td>
	            <td>菜品名称</td>
				<td>菜品数量</td>
	        </tr>
			<c:forEach items="${sessionScope.get('orderItems')}" var="ois" >
				<tr>
					<td></td>
					<td><input class="easyui-textbox" type="text" value="${ois.title}" readonly="readonly" style="width: 280px;"/></td>
					<td><input class="easyui-textbox" type="text" value="${ois.num}" readonly="readonly" /></td>
				</tr>
			</c:forEach>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="reForm()">返回</a>
	</div>
</div>
<script type="text/javascript">
	function reForm(){
        sessionStorage.removeItem("paymentType");
        sessionStorage.removeItem("orderItems");
        $("#orderEditWindow").window('close');
        $("#orderList").datagrid("reload");
	}
</script>
