package com.dai.service;

import com.dai.common.pojo.EasyUITreeNode;
import com.dai.util.E3Result;

import java.util.List;

public interface ContentCategoryService {

	/**
	 * 查询内容管理的所有节点
	 * @param parentId 父亲节点id
	 * @return
	 */
	public List<EasyUITreeNode> getContentCategoryList(long parentId);
	
	/**
	 * 添加新的内容节点
	 * @param parentId	父节点的ID
	 * @param name	新节点的名称
	 * @return
	 */
	public E3Result addContentCategory(long parentId, String name);
	
	/**
	 * 重命名节点内容
	 * @param id	节点ID
	 * @param name	节点新内容
	 * @return
	 */
	public E3Result updateContentCategoryById(long id, String name);
	
	/**
	 * 删除节点
	 * @param parentId
	 * @param id
	 * @return
	 */
	public E3Result deleteContentCategoryById(long parentId, long id);
	
}
