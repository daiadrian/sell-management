package com.dai.common.pojo;

import java.io.Serializable;
/**
 * 菜品类目的树节点工具类
 * @author adrain
 *
 */
@SuppressWarnings("serial")
public class EasyUITreeNode implements Serializable {

	//类目ID
	private long id;
	//类目的名称
	private String text;
	//是否有子节点（closed,open）
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public EasyUITreeNode(long id, String text, String state) {
		this.id = id;
		this.text = text;
		this.state = state;
	}
	public EasyUITreeNode() {
	}
	
	
	
}
