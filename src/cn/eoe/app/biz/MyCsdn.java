package cn.eoe.app.biz;

import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import android.app.Activity;
import cn.eoe.app.config.Constants;
import cn.eoe.app.config.Urls;
import cn.eoe.app.entity.NewsJson;
import cn.eoe.app.entity.NewsMoreResponse;
import cn.eoe.app.entity.NewsResponseEntity;
import cn.eoe.app.utils.RequestCacheUtil;
import cn.eoe.app.utils.Utility;


/**
 * MyCsdn
 *
 */
public class MyCsdn extends BaseDao {

	private String url;
	
	public MyCsdn(Activity activity) {
		super(activity);
	}
	
	public MyCsdn(Activity activity,String url) {
		super(activity);
		this.setUrl(url);
	}

	private NewsResponseEntity _newsResponse;

	public NewsResponseEntity get_newsResponse() {
		return _newsResponse;
	}

	public void set_newsResponse(NewsResponseEntity _newsResponse) {
		this._newsResponse = _newsResponse;
	}

	public NewsResponseEntity mapperJson(boolean useCache) {
		NewsJson newsJson;
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					getUrl(),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list,
					useCache);
			
			newsJson = mObjectMapper.readValue(result,
					new TypeReference<NewsJson>() {
					});
			if (newsJson == null) {
				return null;
			}
			this._newsResponse = newsJson.getResponse();
			return _newsResponse;
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

	public NewsMoreResponse getMore(String more_url) {
		NewsMoreResponse response;
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					more_url + Utility.getScreenParams(mActivity),					
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, true);
			response = mObjectMapper.readValue(result,
					new TypeReference<NewsMoreResponse>() {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
