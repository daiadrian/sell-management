<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<span>按条件查找</span><br/>
<form>
    货物名称:<input type="text" name="title" id="theStockTitle"/> &nbsp;&nbsp;&nbsp;
    <input type="button" onclick="findTitleWithStockList()" value="查询"/>
</form>
<br/>
<table class="easyui-datagrid" id="stockList" title="进货列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/stock/list',method:'get',pageSize:30,toolbar:toolstockbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">ID</th>
            <th data-options="field:'title',width:200">货品名称</th>
            <th data-options="field:'price',width:100,formatter:E3.formatPrice">货品单价</th>
            <th data-options="field:'num',width:100">货品数量</th>
            <th data-options="field:'totalprice',width:100,formatter:E3.formatPrice">总价</th>
            <th data-options="field:'created',width:130,align:'center',formatter:E3.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:E3.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
<div id="stockEditWindow" class="easyui-window" title="编辑记录" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/stock-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<div id="stockAddWindow" class="easyui-window" title="新增进货记录" data-options="modal:true,closed:true,iconCls:'icon-save'" style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function findTitleWithStockList(){
        var theStockTitle = $("#theStockTitle").val();
        if(undefined == theStockTitle){
            theStockTitle = "";
        }
        $("#stockList").datagrid({
            singleSelect:false,
            collapsible:true,
            pagination:true,
            url:'/stock/findTitleWithStockList?title='+theStockTitle,
            method:'get',
            pageSize:30,
            toolbar:toolstockbar
        });
    }

    function getSelectionsStockIds(){
    	var stockList = $("#stockList");
    	var sels = stockList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolstockbar = [{
        text:'新增',
        iconCls:'icon-add',
        handler:function(){
            $("#stockAddWindow").window({
                href : "/stock-add"
            }).window("open");
        }
    },{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsStockIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个记录才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个记录!');
        		return ;
        	}

        	$("#stockEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#stockList").datagrid("getSelections")[0];
        			data.priceView = E3.formatPrice(data.price);
        			$("#stockEditForm").form("load",data);
        		}
        	}).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsStockIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中记录!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的记录吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/stock/rest/delete",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','删除记录成功!',undefined,function(){
            					$("#stockList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];


</script>