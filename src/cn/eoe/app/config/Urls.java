package cn.eoe.app.config;

public class Urls {

	/**
	 * 根目录
	 */
	public static final String BASIC_URL = "http://api.eoe.cn/client";

	/**
	 * 推荐资讯
	 */
	public static final String TOP_NEWS_URL = BASIC_URL + "/news?k=lists&t=top";
	/**
	 * 推荐博客
	 */
	public static final String TOP_BLOG_URL = BASIC_URL + "/blog?k=lists&t=top";
	/**
	 * 推荐教程
	 */
	public static final String TOP_WIKI_URL = BASIC_URL + "/wiki?k=lists&t=top";

	/**
	 * 社区精选
	 */
	public static final String TOP_LIST = BASIC_URL + "/top";
	/**
	 * 社区博客
	 */
	public static final String BLOGS_LIST = BASIC_URL + "/blog?k=lists";
	/**
	 * 新闻资讯
	 */
	public static final String NEWS_LIST = BASIC_URL + "/news?k=lists";	
	/**
	 * 学习教程
	 */
	public static final String WIKI_LIST = BASIC_URL + "/wiki?k=lists";

	/**
	 * searchURL
	 */
	public static final String BASE_SEARCH_URL = BASIC_URL + "/search?";

	/**
	 * 登录接口
	 */
	public static final String USER_LOGIN = BASIC_URL + "/key?uname=%s&pwd=%s";

	/**
	 * 1 k 2 act 3 model 4 itemid
	 */
	public static final String DETAILS_ActionBar = BASIC_URL
			+ "/bar?k=%s&act=%s&model=%s&itemid=%s";

	public static final String userlike = "userlike";
	public static final String favorite = "favorite";
	public static final String add = "add";
	public static final String del = "del";
	public static final String like = "like";
	public static final String useless = "useless";
	public static final String news = "news";
	public static final String wiki = "wiki";
	public static final String blog = "blog";

	/**
	 * 获取用户信息接口
	 */
	public static final String KEYBindURL = BASIC_URL + "/userinfo?key=%s";
	

	/**
	 * 资本动态
	 */
	public static final String NewsB_LIST = "http://news.entgroup.cn/b/index.shtml";
	public static final String NewsB_LIST_Base = "http://news.entgroup.cn";
	
	/**
	 * 娱乐营销
	 */
	public static final String NewsM_LIST = "http://news.entgroup.cn/m/index.shtml";
	public static final String NewsM_LIST_Base = "http://news.entgroup.cn";
	
	/**
	 * 政策法规
	 */
	public static final String NewsC_LIST = "http://news.entgroup.cn/c/index.shtml";
	public static final String NewsC_LIST_Base = "http://news.entgroup.cn";
	
	/**
	 * 人物访谈
	 */
	public static final String Interview_LIST = "http://interview.entgroup.cn/index.shtml";
	public static final String Interview_LIST_Base = "http://interview.entgroup.cn";
}
