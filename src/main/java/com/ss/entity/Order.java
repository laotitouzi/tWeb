package com.ss.entity;

import java.io.Serializable;
import java.util.Date;

import com.ss.utils.DateUtils;

public class Order implements Serializable {
	public String orderNo;
	public Date createDate;
	public String buyer;
	public String goodsName;
	public double fee;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCreateDate() {
		return DateUtils.format(createDate);
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}
}
