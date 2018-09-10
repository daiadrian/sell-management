package com.dai.service.Impl;

import com.dai.common.jedis.JedisItemClient;
import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.mapper.TbItemDescMapper;
import com.dai.mapper.TbItemMapper;
import com.dai.pojo.TbItem;
import com.dai.pojo.TbItemDesc;
import com.dai.pojo.TbItemExample;
import com.dai.service.ItemService;
import com.dai.util.E3Result;
import com.dai.util.IDUtils;
import com.dai.util.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.*;

/**
 * 菜品信息的service
 * @author adrain
 *
 */
@Service("itemService")
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisItemClient jedisItemClient;
	//Redis中的item的存储前缀
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	//缓存的过期时间
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;

	@Override
	public TbItem getTbItemByItemId(long itemId) {
		//BASE代表基本详情
		//判断缓存中有没有
		//缓存中有就先从缓存中取
		String itemJson = jedisItemClient.get(ITEM_INFO_PRE+":"+itemId+":BASE");
		if(StringUtils.isNotBlank(itemJson)){
			return JsonUtils.jsonToPojo(itemJson, TbItem.class);
		}
		//如果缓存中没有则从数据库中查，并且添加到缓存中
		TbItem tbItem=tbItemMapper.selectByPrimaryKey(itemId);
		jedisItemClient.set(ITEM_INFO_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(tbItem));
		//设置缓存的过期时间
		jedisItemClient.expire(ITEM_INFO_PRE+":"+itemId+":BASE", ITEM_INFO_EXPIRE);
		return tbItem;
	}

	@Override
	public EasyUIDataGridResult getTbItemList(int page, int rows) {
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo((byte)3);
		//设置分页参数
		PageHelper.startPage(page, rows);
		List<TbItem> tbItemList = tbItemMapper.selectByExample(example);
		//通过list得到一个PageInfo
		PageInfo<TbItem> info = new PageInfo<TbItem>(tbItemList);
		//作为一个结果集传给表现层
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) info.getTotal());
		result.setRows(info.getList());
		return result;
	}

	@Override
	public E3Result addTbItem(TbItem item, String desc) {
		//添加菜品的id,得到当前时间的毫秒值并且加上随机的两位数(使用ID生成工具类)
		final long itemID = IDUtils.genItemId();
		item.setId(itemID);
		//添加菜品的状态,1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		//添加菜品的创建时间
		item.setCreated(new Date());
		//添加菜品的修改时间
		item.setUpdated(new Date());
		//新增菜品
		tbItemMapper.insert(item);
		//新增菜品的desc(描述)
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemID);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		tbItemDesc.setItemDesc(desc);
		//保存菜品desc
		tbItemDescMapper.insert(tbItemDesc);
		//将添加菜品的消息添加到队列
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemID + "");
			}
		});
		return E3Result.ok();
	}

	@Override
	public E3Result deleteTbItemByItemId(long ids) {
		//逻辑删除菜品
		////菜品状态，1-正常，2-下架，3-删除
		TbItem item = tbItemMapper.selectByPrimaryKey(ids);
		item.setStatus((byte)3);
		tbItemMapper.updateByPrimaryKeySelective(item);
		return E3Result.ok();
	}

	@Override
	public E3Result findItemDescByItemId(long id) {
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(id);
		return E3Result.ok(itemDesc);
	}

	@Override
	public E3Result updateItem(TbItem item, String desc) {
		//更新修改时间
		item.setUpdated(new Date());
		//更新菜品
		tbItemMapper.updateByPrimaryKeySelective(item);
		//更新菜品描述
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(item.getId());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setUpdated(new Date());
		tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
		return E3Result.ok();
	}

	@Override
	public E3Result reshelf(long ids) {
		//菜品状态，1-正常，2-下架，3-删除
		//修改菜品状态为上架
		TbItem item = tbItemMapper.selectByPrimaryKey(ids);
		item.setStatus((byte)1);
		tbItemMapper.updateByPrimaryKeySelective(item);
		return E3Result.ok();
	}

	@Override
	public E3Result instock(long ids) {
		//菜品状态，1-正常，2-下架，3-删除
		//修改菜品状态为下架
		TbItem item = tbItemMapper.selectByPrimaryKey(ids);
		item.setStatus((byte)2);
		tbItemMapper.updateByPrimaryKeySelective(item);
		return E3Result.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//DESC代表菜品描述
		//判断缓存是否有
		//缓存中有就从缓存中取
		String itemDescJson = jedisItemClient.get(ITEM_INFO_PRE+":"+itemId+":DESC");
		if(StringUtils.isNotBlank(itemDescJson)){
			return JsonUtils.jsonToPojo(itemDescJson, TbItemDesc.class);
		}
		//如果缓存中没有则从数据库中查，并且添加到缓存中
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		//添加到缓存
		jedisItemClient.set(ITEM_INFO_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
		//设置过期时间
		jedisItemClient.expire(ITEM_INFO_PRE+":"+itemId+":DESC", ITEM_INFO_EXPIRE);
		return itemDesc;
	}

	@Override
	public EasyUIDataGridResult getTbItemListWithIdOrTitle(int page, int rows, long id, String title) {
		TbItemExample example = new TbItemExample();
		//设置分页参数
		PageHelper.startPage(page, rows);
		TbItemExample.Criteria criteria = example.createCriteria();
		if (!(0 == id)) {
			criteria.andIdEqualTo(id);
		}
		if (StringUtils.isNotBlank(title)) {
			criteria.andTitleLike("%"+title+"%");
		}
		List<TbItem> tbItemList = tbItemMapper.selectByExample(example);
		//通过list得到一个PageInfo
		PageInfo<TbItem> info = new PageInfo<TbItem>(tbItemList);
		//作为一个结果集传给表现层
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) info.getTotal());
		result.setRows(info.getList());
		return result;
	}

	@Override
	public EasyUIDataGridResult findAllinstockItemList(int page,int rows) {
		TbItemExample example = new TbItemExample();
		//设置分页参数
		PageHelper.startPage(page, rows);
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo((byte)2);
		List<TbItem> tbItemList = tbItemMapper.selectByExample(example);
		//通过list得到一个PageInfo
		PageInfo<TbItem> info = new PageInfo<TbItem>(tbItemList);
		//作为一个结果集传给表现层
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) info.getTotal());
		result.setRows(info.getList());
		return result;
	}

}
