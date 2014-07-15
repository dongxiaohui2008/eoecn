package cn.eoe.app.entity;

import java.util.List;

import cn.eoe.app.entity.base.BaseContentList;

/**
 * 资讯列表
 *
 */
public class NewsCategoryListEntity extends BaseContentList {
	
	private List<NewsContentItem> items;

	public List<NewsContentItem> getItems() {
		return items;
	}
	public void setItems(List<NewsContentItem> items) {
		this.items = items;
	}
}
