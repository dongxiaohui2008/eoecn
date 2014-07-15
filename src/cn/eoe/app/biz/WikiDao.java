package cn.eoe.app.biz;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import cn.eoe.app.config.Constants;
import cn.eoe.app.config.Urls;
import cn.eoe.app.entity.WikiJson;
import cn.eoe.app.entity.WikiMoreResponse;
import cn.eoe.app.entity.WikiResponseEntity;
import cn.eoe.app.utils.RequestCacheUtil;
import cn.eoe.app.utils.Utility;

/**
 * 学习教程
 *
 */
public class WikiDao extends BaseDao {

	public WikiDao(Activity activity) {
		super(activity);
	}

	private WikiResponseEntity mWikiResponseEntity;

	public WikiResponseEntity getmWikiResponseEntity() {
		return mWikiResponseEntity;
	}

	public void setmWikiResponseEntity(WikiResponseEntity mWikiResponseEntity) {
		this.mWikiResponseEntity = mWikiResponseEntity;
	}

	public WikiResponseEntity mapperJson(boolean useCache) {
		WikiJson wikiJson;
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					Urls.WIKI_LIST + Utility.getScreenParams(mActivity),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, useCache);
			wikiJson = mObjectMapper.readValue(result,
					new TypeReference<WikiJson>() {
					});
			if (wikiJson == null) {
				return null;
			}
			this.mWikiResponseEntity = wikiJson.getResponse();
			return mWikiResponseEntity;
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
	
	public WikiMoreResponse getMore(String more_url) {
		WikiMoreResponse response;
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					more_url + Utility.getScreenParams(mActivity),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, true);
			response = mObjectMapper.readValue(result,
					new TypeReference<WikiMoreResponse>() {
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
