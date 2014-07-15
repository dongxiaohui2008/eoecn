package cn.eoe.app.entity.base;
import java.util.List;

import cn.eoe.app.entity.CategorysEntity;

/**
 * 分类 tabs
 *
 */
public abstract class BaseResponseData {
	 
	private long date;				//创建时间
	private List<CategorysEntity> categorys;	//分类
		
	public long getDate() {
		return date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}
	
	/**
	 * 分类集合 tabs
	 * 
	 */
	public List<CategorysEntity> getCategorys() {
		return categorys;
	}
	
	/**
	 *  分类集合 tabs
	 * 
	 */
	public void setCategorys(List<CategorysEntity> categorys) {
		this.categorys = categorys;
	}
}
