package com.xmsmartcity.orm;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.PageInfo;

public class Pagination<T> extends PageInfo<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private RowBounds rowBounds;
	
	private int pageNum;
	
	private int pageSize;
	
	private int draw;//前端分页需要，用于识别是那次请求
	
	public Pagination(List<T> list){
		super(list);
	}
	
	public Pagination(int pageNum,int pageSize){
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public RowBounds getRowBounds() {
		if(this.rowBounds == null){
			int startIndex = (pageNum-1)*pageSize;
			this.rowBounds = new RowBounds(startIndex,pageSize);
		}
		return rowBounds;
	}

	public void setRowBounds(RowBounds rowBounds) {
		this.rowBounds = rowBounds;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}
	
	
}