package com.dai.service.Impl;

import com.dai.common.pojo.EasyUITreeNode;
import com.dai.mapper.TbContentCategoryMapper;
import com.dai.pojo.TbContentCategory;
import com.dai.pojo.TbContentCategoryExample;
import com.dai.service.ContentCategoryService;
import com.dai.util.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理服务层
 * @author adrain
 *
 */
@Service("contentCategoryService")
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		//获取内容分类
		TbContentCategoryExample example = new TbContentCategoryExample();
		TbContentCategoryExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		//插入新的内容节点
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		//排序，默认为1
		tbContentCategory.setSortOrder(1);
		//状态。可选值:1(正常),2(删除)
		tbContentCategory.setStatus(1);
		//新插入的节点均为子节点
		tbContentCategory.setIsParent(false);
		//自增长的id返回后 会自动插入到tbContentCategory对象中
		tbContentCategoryMapper.insert(tbContentCategory);
		//查询父节点，修改父节点的isParent
		TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		//不是父节点就修改
		if(!parentNode.getIsParent()){
			parentNode.setIsParent(true);
		}
		tbContentCategoryMapper.updateByPrimaryKeySelective(parentNode);
		return E3Result.ok(tbContentCategory);
	}

	@Override
	public E3Result updateContentCategoryById(long id, String name) {
		//修改名字
		TbContentCategory contentCategroy = tbContentCategoryMapper.selectByPrimaryKey(id);
		contentCategroy.setName(name);
		tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategroy);
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContentCategoryById(long parentId , long id) {
		//删除节点
		//使用逻辑删除
		//先判断时候为父节点，如果为父节点则不能删除
		TbContentCategory contentCategroy = tbContentCategoryMapper.selectByPrimaryKey(id);
		if(!contentCategroy.getIsParent()){
			return E3Result.ok();
		}
		//状态。可选值:1(正常),2(删除)
		contentCategroy.setStatus(2);
		tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategroy);
		//根据父亲节点来查询 父亲节点 是否还有子节点，如果没有则修改 isParent ,否则不用修改
		TbContentCategoryExample example = new TbContentCategoryExample();
		TbContentCategoryExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> contentCategroyList = tbContentCategoryMapper.selectByExample(example);
		if(!(contentCategroyList!=null && contentCategroyList.size() > 0)){
			//需要修改父节点的isParent
			TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
			parentNode.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKeySelective(parentNode);
		}
		return E3Result.ok();
	}

}
