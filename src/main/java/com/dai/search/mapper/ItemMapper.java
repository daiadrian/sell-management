package com.dai.search.mapper;

import com.dai.common.pojo.SearchItem;

import java.util.List;


public interface ItemMapper {

	List<SearchItem> getItemList();
	
	SearchItem getItemByItemId(long itemId);
}