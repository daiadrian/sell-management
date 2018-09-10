package com.dai.service.Impl;

import java.util.Date;
import java.util.List;

import com.dai.common.jedis.JedisClient;
import com.dai.common.jedis.JedisContentClient;
import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.mapper.TbContentMapper;
import com.dai.pojo.TbContent;
import com.dai.pojo.TbContentExample;
import com.dai.service.ContentService;
import com.dai.util.E3Result;
import com.dai.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 内容管理服务层
 * @author adrain
 *
 */
@Service("contentService")
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisContentClient jedisContentClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Override
	public E3Result addContent(TbContent content) {
		//将内容数据插入到内容表
		content.setCreated(new Date());
		content.setUpdated(new Date());
		String url = content.getUrl();
		if (StringUtils.isNotBlank(url))
			url = "http://localhost:8082/item/" + url;
		content.setUrl(url);
		//插入到数据库
		tbContentMapper.insert(content);
		//缓存同步,删除缓存中对应的数据。
		jedisContentClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	@Override
	public E3Result updateContent(TbContent content) {
		tbContentMapper.updateByPrimaryKeySelective(content);
		jedisContentClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContent(long id) {
		TbContent tbContent = tbContentMapper.selectByPrimaryKey(id);
		tbContentMapper.deleteByPrimaryKey(id);
		jedisContentClient.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
		return E3Result.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
		//根据内容类目查找商品列表
 		//使用分页插件进行查询
 		TbContentExample example = new TbContentExample();
 		TbContentExample.Criteria criteria = example.createCriteria();
 		criteria.andCategoryIdEqualTo(categoryId);
 		PageHelper.startPage(page, rows);
 		List<TbContent> contentList = tbContentMapper.selectByExample(example);
 		PageInfo<TbContent> info = new PageInfo<TbContent>(contentList);
 		EasyUIDataGridResult result = new EasyUIDataGridResult(
 				(int)info.getTotal() , info.getList());
		return result;
	}

	@Override
	public List<TbContent> getContentList(long id) {
		//根据categoryId查询content列表
		//限定最多7条 ， 使用分页助手来获取
		//先从缓存中取，如果缓存中不存在，则向数据库中查询，再添加到缓存中
		try {
			String jsonContent = jedisContentClient.hget(CONTENT_LIST, "89");
			if(StringUtils.isNotBlank(jsonContent)){
				return JsonUtils.jsonToList(jsonContent, TbContent.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(id);
		PageHelper.startPage(1, 7);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		PageInfo<TbContent> info = new PageInfo<TbContent>(list);
		//将内容添加到缓存中
		try {
			jedisContentClient.hset(CONTENT_LIST, id+"", JsonUtils.objectToJson(info.getList()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info.getList();
	}

}
