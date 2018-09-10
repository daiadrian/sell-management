package com.dai.Controller;

import java.util.List;

import com.dai.common.pojo.EasyUITreeNode;
import com.dai.service.ItemCatService;
import com.dai.util.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品类目的控制器
 * @author adrain
 *
 */
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(
			@RequestParam(value="id",defaultValue="0") long parentId){
		List<EasyUITreeNode> itemCatList = itemCatService.getItemCatList(parentId);
		return itemCatList;
	}

	@RequestMapping("/item/cat/create")
	@ResponseBody
	public E3Result addContentCategory(long parentId, String name){
		E3Result result = itemCatService.addItemCat(parentId, name);
		return result;
	}

	@RequestMapping("/item/cat/update")
	@ResponseBody
	public E3Result updateContentCategoryById(long id , String name){
		E3Result result = itemCatService.updateItemCatById(id, name);
		return result;
	}

	@RequestMapping("/item/cat/delete")
	@ResponseBody
	public E3Result deleteContentCategoryById(
			@RequestParam(value="parentId" , defaultValue="0") long parentId ,
			@RequestParam(value="id") long id){
		E3Result result = itemCatService.deleteItemCatById(parentId,id);
		return result;
	}
	
}
