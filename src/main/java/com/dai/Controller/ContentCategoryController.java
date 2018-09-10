package com.dai.Controller;

import java.util.List;

import com.dai.common.pojo.EasyUITreeNode;
import com.dai.service.ContentCategoryService;
import com.dai.util.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 内容管理处理器
 * @author adrain
 *
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping(value="/list" , method=RequestMethod.GET)
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(
			@RequestParam(value="id" , defaultValue="0") long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
		return list;
	}
	
	@RequestMapping("/create")
	@ResponseBody
	public E3Result addContentCategory(long parentId, String name){
		E3Result result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public E3Result updateContentCategoryById(long id , String name){
		E3Result result = contentCategoryService.updateContentCategoryById(id, name);
		return result;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public E3Result deleteContentCategoryById(
			@RequestParam(value="parentId" , defaultValue="0") long parentId , 
			@RequestParam(value="id") long id){
		E3Result result = contentCategoryService.deleteContentCategoryById(parentId,id);
		return result;
	}
	
}
