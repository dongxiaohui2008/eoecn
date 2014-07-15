package cn.eoe.app.entity;

import cn.eoe.app.entity.base.BaseContentItem;

/**
 * 博客ListItem实体类
 * 
 */
public class BlogContentItem extends BaseContentItem {

	private String name; // 博主姓名

	private String head_image_url; // 博主头像

	/**
	 * 博主姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 博主姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 博主头像
	 */
	public String getHead_image_url() {
		return head_image_url;
	}

	/**
	 * 博主头像
	 */
	public void setHead_image_url(String head_image_url) {
		this.head_image_url = head_image_url;
	}

}
