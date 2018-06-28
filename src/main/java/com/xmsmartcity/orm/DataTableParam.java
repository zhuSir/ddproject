package com.xmsmartcity.orm;

/**
 * dataTale请求数据
 * @author chenbinyi
 *
 */
public class DataTableParam {

	private int draw;//datatable从第几条开始
	
	private int start;//第一条数据的起始位置，比如0代表第一条数据
	
	private int length;//每页显示的条数

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
}
