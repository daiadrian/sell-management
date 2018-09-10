package com.dai.Controller;

import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.pojo.TbItem;
import com.dai.service.ItemService;
import com.dai.util.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;


/**
 * 菜品信息的控制器
 * @author adrain
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getTbItemByItemId(@PathVariable Long itemId){
		TbItem tbItem = itemService.getTbItemByItemId(itemId);
		return tbItem;
	}
	
	/**
	 * 查询菜品列表
	 * @param page	当前页
	 * @param rows	每页大小
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getTbItemList(int page,int rows){
		EasyUIDataGridResult result = itemService.getTbItemList(page, rows);
		return result;
	}
	
	/**
	 * 添加菜品
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(value="/item/save")
	@ResponseBody
	public E3Result addTbItem(TbItem item, String desc) {
		E3Result result = itemService.addTbItem(item, desc);
		return result;
		
	}
	
	/**
	 * 根据菜品ID删除菜品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteTbItemByItemId(@RequestParam("ids") long ids){
		E3Result result = itemService.deleteTbItemByItemId(ids);
		return result;
	}
	
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public E3Result findItemDescByItemId(@PathVariable long id){
		E3Result result = itemService.findItemDescByItemId(id);
		return result;
	}
	
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem item , String desc){
		E3Result result = itemService.updateItem(item, desc);
		return result;
	}
	
	@RequestMapping("/rest/item/param/item/query/{id}")
	@ResponseBody
	public E3Result findParam(@PathVariable long id){
		return E3Result.ok();
	}
	
	/**
	 * 上架
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result reshelf(@RequestParam("ids") long ids){
		E3Result result = itemService.reshelf(ids);
		return result;
	}
	
	/**
	 * 下架
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3Result instock(@RequestParam("ids") long ids){
		E3Result result = itemService.instock(ids);
		return result;
	}

	/**
	 * 根据查询条件获取list
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/findIdOrTitleWithItemList")
	@ResponseBody
	public EasyUIDataGridResult getIdOrTitleWithItemList(int page,int rows,
														 @RequestParam(defaultValue = "0") long id,String title) throws Exception {
		EasyUIDataGridResult result = null;
		if (0 == id){
			if (!StringUtils.isNotBlank(title))
				result = itemService.getTbItemList(page, rows);
		}
		if (0 == id) {
			title = new String(title.getBytes("ISO-8859-1"),"UTF-8");
			result = itemService.getTbItemListWithIdOrTitle(page, rows, id, title);
		}else{
			result = itemService.getTbItemListWithIdOrTitle(page, rows, id, null);
		}
		return result;
	}

	/**
	 * 查看所有下架的菜品
	 * @return
	 */
	@RequestMapping("/item/findAllinstockItemList")
	@ResponseBody
	public EasyUIDataGridResult findAllinstockItemList(int page,int rows){
		EasyUIDataGridResult result = itemService.findAllinstockItemList(page,rows);
		return result;
	}

}
