package com.dai.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dai.common.pojo.EasyUITreeNode;
import com.dai.mapper.TbItemCatMapper;
import com.dai.pojo.TbItemCat;
import com.dai.pojo.TbItemCatExample;
import com.dai.service.ItemCatService;
import com.dai.util.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 菜品类目的服务
 * @author adrain
 *
 */
@Service("itemCatService")
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		//创建example
		TbItemCatExample example = new TbItemCatExample();
		//创建添加条件的criteria
		TbItemCatExample.Criteria criteria = example.createCriteria();
		//添加条件
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);
		//根据父节点查询类目列表
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//将列表转换成EasyUITreeNode
		List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
		for (TbItemCat itemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			node.setState(itemCat.getIsParent()?"closed":"open");
			//将节点添加到nodeList中
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public E3Result addItemCat(long parentId, String name) {
		//插入新的内容节点
		TbItemCat tbItemCat = new TbItemCat();
		tbItemCat.setParentId(parentId);
		tbItemCat.setName(name);
		tbItemCat.setCreated(new Date());
		tbItemCat.setUpdated(new Date());
		//排序，默认为1
		tbItemCat.setSortOrder(1);
		//状态。可选值:1(正常),2(删除)
		tbItemCat.setStatus(1);
		//新插入的节点均为子节点
		tbItemCat.setIsParent(false);
		//自增长的id返回后 会自动插入到tbContentCategory对象中
		itemCatMapper.insert(tbItemCat);
		//查询父节点，修改父节点的isParent
		TbItemCat parentNode = itemCatMapper.selectByPrimaryKey(parentId);
		//不是父节点就修改
		if(!parentNode.getIsParent()){
			parentNode.setIsParent(true);
		}
		itemCatMapper.updateByPrimaryKeySelective(parentNode);
		return E3Result.ok(tbItemCat);
	}

	@Override
	public E3Result updateItemCatById(long id, String name) {
		//修改名字
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(id);
		itemCat.setName(name);
		itemCatMapper.updateByPrimaryKeySelective(itemCat);
		return E3Result.ok();
	}

	@Override
	public E3Result deleteItemCatById(long parentId, long id) {
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(id);
		if(!itemCat.getIsParent()){
			return E3Result.ok();
		}
		//状态。可选值:1(正常),2(删除)
		itemCat.setStatus(2);
		itemCatMapper.updateByPrimaryKeySelective(itemCat);
		TbItemCatExample example = new TbItemCatExample();
		TbItemCatExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> contentCategroyList = itemCatMapper.selectByExample(example);
		if(!(contentCategroyList!=null && contentCategroyList.size() > 0)){
			TbItemCat parentNode = itemCatMapper.selectByPrimaryKey(parentId);
			parentNode.setIsParent(false);
			itemCatMapper.updateByPrimaryKeySelective(parentNode);
		}
		return E3Result.ok();
	}


}
