package cn.eoe.app.entity;

import cn.eoe.app.entity.base.BaseContentItem;

/**
 * 资讯详情item实体类
 *
 */
public class NewsContentItem extends BaseContentItem{
	
	private String 	title;//标题
	private String	thumbnail_url;//缩略图
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getThumbnail_url() {
		return thumbnail_url;
	}
	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}	
}
