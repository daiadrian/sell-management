package com.dai.common.pojo;

import java.io.Serializable;
import java.util.List;
/**
 * 查询的表格结果集
 * @author adrain
 *
 */
@SuppressWarnings("serial")
public class EasyUIDataGridResult implements Serializable {

	//总记录数
	private Integer total;
	//结果集
	private List<?> rows;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public EasyUIDataGridResult(Integer total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}
	public EasyUIDataGridResult() {
	}
	
	
	
}
