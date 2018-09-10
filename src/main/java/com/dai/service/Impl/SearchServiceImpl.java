package com.dai.service.Impl;

import com.dai.common.pojo.SearchResult;
import com.dai.search.dao.SearchDao;
import com.dai.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 站内搜索服务器
 * @author adrain
 *
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;
	@Value("${CID_FIELD}")
	private String CID_FIELD;
	
	@Override
	public SearchResult search(String keyWord, int page, int rows) throws Exception {
		//创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询的条件
		//设置查询条件
		solrQuery.setQuery(keyWord);
		//设置分页条件
		solrQuery.setStart((page - 1) * rows);
		//设置rows
		solrQuery.setRows(rows);
		//设置默认搜索域
		solrQuery.set("df", DEFAULT_FIELD);
		//设置高亮显示
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		//执行查询
		SearchResult searchResult = searchDao.search(solrQuery);
		//计算总页数
		int recourdCount = (int) searchResult.getRecordCount();
		int pages = recourdCount / rows;
		if (recourdCount % rows > 0) pages++;
		//设置到返回结果
		searchResult.setTotalPages(pages);
		return searchResult;
	}

	@Override
	public SearchResult searchTitle(String keyWord, int page, int rows)
			throws Exception {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(keyWord);
		solrQuery.setStart((page - 1) * rows);
		solrQuery.setRows(rows);
		solrQuery.set("df", CID_FIELD);
		SearchResult searchResult = searchDao.searchTitle(solrQuery);
		int recourdCount = (int) searchResult.getRecordCount();
		int pages = recourdCount / rows;
		if (recourdCount % rows > 0) pages++;
		searchResult.setTotalPages(pages);
		return searchResult;
	}

}
