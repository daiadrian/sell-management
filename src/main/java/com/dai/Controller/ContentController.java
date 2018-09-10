package com.dai.Controller;

import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.pojo.TbContent;
import com.dai.service.ContentService;
import com.dai.util.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 内容管理的控制器
 * @author adrain
 *
 */
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/content/query/list" , method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getContentList(
			@RequestParam("categoryId") long categoryId,
			@RequestParam("page") int page,
			@RequestParam("rows") int rows) {
		EasyUIDataGridResult result = contentService.getContentList(categoryId, page, rows);
		return result;
	}


	@RequestMapping(value="/content/save")
	@ResponseBody
	public E3Result contentSave(TbContent tbContent) {
		E3Result result = contentService.addContent(tbContent);
		return result;
	}

	@RequestMapping(value="/content/delete")
	@ResponseBody
	public E3Result contentDelete(long ids) {
		E3Result result = contentService.deleteContent(ids);
		return result;
	}

	@RequestMapping(value="/rest/content/edit")
	@ResponseBody
	public E3Result contentEdit(TbContent tbContent) {
		E3Result result = contentService.updateContent(tbContent);
		return result;
	}


}
