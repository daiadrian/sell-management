<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="stockAddForm" class="itemForm" method="post">
	    <table cellpadding="5">
			<tr>
				<td>货品名称:</td>
				<td><input class="easyui-textbox" type="text" name="title" data-options="required:true" style="width: 280px;" /></td>
			</tr>
			<tr>
				<td>货品单价:</td>
				<td><input class="easyui-numberbox" type="text" name="priceView" data-options="min:1,max:99999999,precision:2,required:true" />元
					<input type="hidden" name="price"/>
				</td>
			</tr>
			<tr>
				<td>货品数量:</td>
				<td><input class="easyui-numberbox" type="text" name="num" data-options="min:1,max:99999999,precision:0" /></td>
			</tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">提交</a>
	</div>
</div>
<script type="text/javascript">
	//提交表单
	function submitForm(){
		//有效性验证
		if(!$('#stockAddForm').form('validate')){
			$.messager.alert('提示','表单还未填写完成!');
			return ;
		}
		$("#stockAddForm [name=price]").val(eval($("#stockAddForm [name=priceView]").val()) * 100);

		$.post("/stock/save",$("#stockAddForm").serialize(), function(data){
			if(data.status == 200){
                $.messager.alert('提示','新增记录成功!','info',function(){
                    $("#stockAddWindow").window('close');
                    $("#stockList").datagrid("reload");
                });
			}
		});
	}

</script>
