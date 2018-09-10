package com.dai.search.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dai.common.pojo.SearchItem;
import com.dai.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 站内搜索dao
 * @author adrain
 *
 */
@Component
public class SearchDao {
	
	@Autowired
	private SolrClient solrClient;

	public SearchResult search(SolrQuery solrQuery) throws Exception {
		//先根据条件查询索引库
		QueryResponse query = solrClient.query(solrQuery);
		//查询总记录数
		SolrDocumentList solrDocumentList = query.getResults();
		long numFound = solrDocumentList.getNumFound();
		//创建一个SearchResult,将总记录数添加进去
		SearchResult searchResult = new SearchResult();
		searchResult.setRecordCount(numFound);
		//查询到的结果封装到SearchResult中
		//创建一个列表来封装得到的searchList
		List<SearchItem> itemList =  new ArrayList<SearchItem>();
		//得到高亮后的结果
		Map<String, Map<String, List<String>>> highlighting = 
				query.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			//得到菜品的信息
			SearchItem searchItem = new SearchItem();
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((Long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			//得到高亮的信息
			List<String> titleList = 
					highlighting.get(solrDocument.get("id")).get("item_title");
			//判断得到的高亮结果，不为空则直接赋值，为空则直接从document中取
			if(titleList!=null && titleList.size()>0){
				searchItem.setTitle(titleList.get(0));
			}else{
				searchItem.setTitle((String)solrDocument.get("item_title"));
			}
			itemList.add(searchItem);
		}
		searchResult.setItemList(itemList);
		return searchResult;
	}
	
	public SearchResult searchTitle(SolrQuery solrQuery) throws Exception {
		//先根据条件查询索引库
		QueryResponse query = solrClient.query(solrQuery);
		//查询总记录数
		SolrDocumentList solrDocumentList = query.getResults();
		long numFound = solrDocumentList.getNumFound();
		//创建一个SearchResult,将总记录数添加进去
		SearchResult searchResult = new SearchResult();
		searchResult.setRecordCount(numFound);
		//查询到的结果封装到SearchResult中
		//创建一个列表来封装得到的searchList
		List<SearchItem> itemList =  new ArrayList<SearchItem>();
		for (SolrDocument solrDocument : solrDocumentList) {
			//得到菜品的信息
			SearchItem searchItem = new SearchItem();
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((Long) solrDocument.get("item_price"));
			searchItem.setCid((String) solrDocument.get("item_cid"));
			searchItem.setTitle((String)solrDocument.get("item_title"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			itemList.add(searchItem);
		}
		searchResult.setItemList(itemList);
		return searchResult;
	}
}
