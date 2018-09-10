package com.dai.common.pojo;


import com.dai.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查看订单详情返回的结果集
 * @author adrain
 *
 */
@SuppressWarnings("serial")
public class OrderDetailResult implements Serializable {

	private String orderId;

	private String payment;

	private String paymentType;

	private Date paymentTime;

	private Date consignTime;

	private String shoppingCode;

	private String status;

	private String username;

	private String useraddress;

	private Integer postFee;

	private String userPhone;

	//订单的详情
	private List<TbOrderItem> orderItems;

	public String getShoppingCode() {
		return shoppingCode;
	}

	public void setShoppingCode(String shoppingCode) {
		this.shoppingCode = shoppingCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUseraddress() {
		return useraddress;
	}

	public void setUseraddress(String useraddress) {
		this.useraddress = useraddress;
	}

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Date getConsignTime() {
		return consignTime;
	}

	public void setConsignTime(Date consignTime) {
		this.consignTime = consignTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPostFee() {
		return postFee;
	}

	public void setPostFee(Integer postFee) {
		this.postFee = postFee;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
}
