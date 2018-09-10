package com.dai.common.pojo;

import java.io.Serializable;
import java.util.List;
/**
 * 站内搜索后返回的结果集
 * @author adrain
 *
 */
@SuppressWarnings("serial")
public class SearchResult implements Serializable {

	//总记录数
	private long recordCount;
	//总页数
	private int totalPages;
	//内容列表
	private List<SearchItem> itemList;
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	
}
