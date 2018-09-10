package com.dai.common.pojo;


import java.io.Serializable;
import java.util.Date;

/**
 * 实时订单页面返回的结果集
 * @author adrain
 *
 */
@SuppressWarnings("serial")
public class CurrentOrderResult implements Serializable {

	private String orderId;

	private String paymentType;

	private String createTime;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
