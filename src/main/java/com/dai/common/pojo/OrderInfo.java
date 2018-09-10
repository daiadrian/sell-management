package com.dai.common.pojo;

import com.dai.pojo.TbOrder;
import com.dai.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;


public class OrderInfo extends TbOrder implements Serializable{

	//订单的详情
	private List<TbOrderItem> orderItems;
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}
