package com.dai.service;

import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.pojo.TbContent;
import com.dai.util.E3Result;

import java.util.List;


/**
 * 内容管理接口
 * @author adrain
 *
 */
public interface ContentService {

	/**
	 * 根据内容类目的ID查找内容
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDataGridResult getContentList(long categoryId, int page, int rows);
	
	/**
	 * 根据categroyId查询content
	 * @param id
	 * @return
	 */
	public List<TbContent> getContentList(long id);

	/**
	 * 添加新的内容
	 * @param content
	 * @return
	 */
	public E3Result addContent(TbContent content);

	/**
	 * 修改内容
	 * @param content
	 * @return
	 */
	public E3Result updateContent(TbContent content);

	/**
	 * 删除内容
	 * @param id
	 * @return
	 */
	public E3Result deleteContent(long id);

}
