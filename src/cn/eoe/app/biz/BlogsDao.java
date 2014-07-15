package cn.eoe.app.biz;

import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import android.app.Activity;
import cn.eoe.app.config.Constants;
import cn.eoe.app.config.Urls;
import cn.eoe.app.entity.BlogsJson;
import cn.eoe.app.entity.BlogsMoreResponse;
import cn.eoe.app.entity.BlogsResponseEntity;
import cn.eoe.app.utils.RequestCacheUtil;
import cn.eoe.app.utils.Utility;


/**
 * 博客
 *
 */
public class BlogsDao extends BaseDao {

	private BlogsResponseEntity _blogsResponse;

	public BlogsDao(Activity activity) {
		super(activity);
	}

	public BlogsResponseEntity getBlogsResponse() {
		return _blogsResponse;
	}

	public void setBlogsResponse(BlogsResponseEntity blogsResponse) {
		this._blogsResponse = blogsResponse;
	}

	/**
	 * 博客列表
	 * 
	 * @param more_url
	 * @return
	 */
	public BlogsResponseEntity mapperJson(boolean useCache) {
		BlogsJson blogsJson_;
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					Urls.BLOGS_LIST + Utility.getScreenParams(mActivity),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, useCache);
			blogsJson_ = mObjectMapper.readValue(result,
					new TypeReference<BlogsJson>() {
					});
			if (blogsJson_ == null) {
				return null;
			}
			_blogsResponse = blogsJson_.getResponse();
			return _blogsResponse;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 博客列表分类list
	 * 
	 * @param more_url
	 * @return
	 */
	public BlogsMoreResponse getMore(String more_url) {
		BlogsMoreResponse response;
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					more_url + Utility.getScreenParams(mActivity),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, true);
			response = mObjectMapper.readValue(result,
					new TypeReference<BlogsMoreResponse>() {
					});
			return response;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
