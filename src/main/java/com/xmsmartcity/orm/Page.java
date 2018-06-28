package com.xmsmartcity.orm;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果
 * 
 * @author felix
 * @date 2014-3-19 下午1:06:03
 */
public class Page<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//总数
	private int total;
	//当前页面数据
	private List<T> list;
	//当前页码
	private int pageNo;
	//每页总数
	private int pageSize;
	//总页数
	private int totalPage;
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
