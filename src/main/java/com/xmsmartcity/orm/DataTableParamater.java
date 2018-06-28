package com.xmsmartcity.orm;

/**
 * 封装DataTable默认的参数类
 * 
 * @author linweiqin
 * @Date 2014-9-26 上午11:27:44
 */
public class DataTableParamater {
	private String aoData;
	private Integer pageNo;
	private String sEcho; // DataTable请求服务器端次数
	private String sSearch; // 过滤文本
	private Integer iDisplayLength; // 每页显示的数量
	private Integer iDisplayStart; // 分页时每页跨度数量
	private Integer iColumns; // 列数
	private Integer iSortingCols; // 排序列的数量
	private Integer sColumns; // 逗号分割所有的列

	/**
	 * 分页查询时的第一个参数，startIndex
	 * @author linweiqin
	 * @Date 2014-11-7 上午10:54:10
	 * @return
	 */
	public Integer getStartIndex(){
		return (pageNo - 1) * iDisplayLength;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getAoData() {
		return aoData;
	}

	public void setAoData(String aoData) {
		this.aoData = aoData;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setSEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	public String getSSearch() {
		return sSearch;
	}

	public void setSSearch(String sSearch) {
		this.sSearch = sSearch;
	}

	public Integer getIDisplayLength() {
		return iDisplayLength;
	}

	public void setIDisplayLength(Integer iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public Integer getIDisplayStart() {
		return iDisplayStart;
	}

	public void setIDisplayStart(Integer iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public Integer getIColumns() {
		return iColumns;
	}

	public void setIColumns(Integer iColumns) {
		this.iColumns = iColumns;
	}

	public Integer getISortingCols() {
		return iSortingCols;
	}

	public void setISortingCols(Integer iSortingCols) {
		this.iSortingCols = iSortingCols;
	}

	public Integer getSColumns() {
		return sColumns;
	}

	public void setSColumns(Integer sColumns) {
		this.sColumns = sColumns;
	}

}
