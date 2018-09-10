package com.dai.service;


import com.dai.common.pojo.SearchResult;

public interface SearchService {

	public SearchResult search(String keyWord, int page, int rows) throws Exception ;
	
	public SearchResult searchTitle(String keyWord, int page, int rows) throws Exception ;
}
