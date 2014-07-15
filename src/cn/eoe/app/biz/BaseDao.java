package cn.eoe.app.biz;

import org.codehaus.jackson.map.ObjectMapper;
import android.app.Activity;

public class BaseDao {

	/**
	 * JSON操作类（开源的）
	 */
	ObjectMapper mObjectMapper = new ObjectMapper();

	protected Activity mActivity;

	public BaseDao() {
	};

	public BaseDao(Activity activity) {
		mActivity = activity;
	}

}
