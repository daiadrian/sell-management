package com.dai.service;


import com.dai.util.E3Result;

public interface SearchItemService {

	/**
	 * 导入索引库
	 * @return
	 */
	public E3Result importItmes();
	
	/**
	 * 根据新增菜品id添加到索引库
	 * @param itemId
	 */
	public void addDocument(long itemId) throws Exception;
}
