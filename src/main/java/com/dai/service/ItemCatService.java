package com.dai.service;

import com.dai.common.pojo.EasyUITreeNode;
import com.dai.util.E3Result;

import java.util.List;


/**
 * 菜品类目服务的接口
 * @author adrain
 *
 */
public interface ItemCatService {

	public List<EasyUITreeNode> getItemCatList(long parentId);

	public E3Result addItemCat(long parentId, String name);

	public E3Result updateItemCatById(long id, String name);

	public E3Result deleteItemCatById(long parentId, long id);

}
