package com.dai.service;

import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.pojo.TbItem;
import com.dai.pojo.TbItemDesc;
import com.dai.util.E3Result;

import java.util.List;


/**
 * 菜品信息的service接口
 * @author adrain
 *
 */
public interface ItemService {

	/**
	 * 根据id查询菜品
	 * @param itemId
	 * @return
	 */
	public TbItem getTbItemByItemId(long itemId);
	
	/**
	 * 查询所有菜品并且进行分页显示
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult getTbItemList(int page, int rows);
	
	/**
	 * 新增菜品
	 * @param item
	 * @param desc
	 * @return
	 */
	public E3Result addTbItem(TbItem item, String desc);
	
	/**
	 * 根据菜品ID删除菜品
	 * @param ids
	 * @return
	 */
	public E3Result deleteTbItemByItemId(long ids);
	
	/**
	 * 根据菜品ID查询菜品描述
	 * @param id
	 * @return
	 */
	public E3Result findItemDescByItemId(long id);
	
	/**
	 * 修改菜品
	 * @param item
	 * @param desc
	 * @return
	 */
	public E3Result updateItem(TbItem item, String desc);
	
	/**
	 * 上架
	 * @param ids
	 * @return
	 */
	public E3Result reshelf(long ids);

	/**
	 * 下架
	 * @param ids
	 * @return
	 */
	public E3Result instock(long ids);
	
	/**
	 * 根据菜品id查询菜品详情
	 * @param itemId
	 * @return
	 */
	public TbItemDesc getItemDescById(long itemId);
	
	EasyUIDataGridResult getTbItemListWithIdOrTitle(int page, int rows, long id, String title);

	EasyUIDataGridResult findAllinstockItemList(int page, int rows);
	
}

