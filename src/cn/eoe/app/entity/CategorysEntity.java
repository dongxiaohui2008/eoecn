package cn.eoe.app.entity;

/**
 * 分类 tab
 *
 */
public class CategorysEntity {
	
	private String name;
	private String url;
	
	/**
	 * 分类名称
	 * 
	 */
	public String getName() {
		return name;
	}
	/**
	 * 分类名称
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 分类Url
	 * 
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 分类Url
	 * 
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
