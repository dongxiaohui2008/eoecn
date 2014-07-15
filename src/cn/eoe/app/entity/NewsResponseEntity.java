package cn.eoe.app.entity;

import java.util.List;

import cn.eoe.app.entity.base.BaseResponseData;

/**
 * 分类集tabs - 资讯列表集
 * 
 */
public class NewsResponseEntity extends BaseResponseData {

	private List<NewsCategoryListEntity> list;

	/**
	 * (资讯列表)集合 --- 跟tabs对应
	 * 
	 */
	public List<NewsCategoryListEntity> getList() {
		return list;
	}

	/**
	 * (资讯列表)集合 --- 跟tabs对应
	 */
	public void setList(List<NewsCategoryListEntity> list) {
		this.list = list;
	}
}
