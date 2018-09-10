package com.dai.service.Impl;

import java.util.List;

import com.dai.common.pojo.SearchItem;
import com.dai.search.mapper.ItemMapper;
import com.dai.service.SearchItemService;
import com.dai.util.E3Result;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 搜索菜品的索引服务
 * @author adrain
 *
 */
@Service("searchItemService")
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrClient solrClient;
	
	@Override
	public E3Result importItmes() {
		try{
			//查询菜品列表
			List<SearchItem> itemList = itemMapper.getItemList();
			//导入索引库
			for (SearchItem searchItem : itemList) {
				//创建文档对象
				SolrInputDocument document = new SolrInputDocument();
				//向文档中添加域
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_cid", searchItem.getCid());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				//写入索引库
				solrClient.add(document);
			}
			//提交
			solrClient.commit();
			//返回成功
			return E3Result.ok();
		}catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "索引导入失败");
		}
	}

	@Override
	public void addDocument(long itemId) throws Exception {
		//根据id获取菜品
		SearchItem searchItem = itemMapper.getItemByItemId(itemId);
		//创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域
		document.addField("id", searchItem.getId());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_sell_point", searchItem.getSell_point());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_cid", searchItem.getCid());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getCategory_name());
		//写入索引库
		solrClient.add(document);
		//提交
		solrClient.commit();
	}

}
