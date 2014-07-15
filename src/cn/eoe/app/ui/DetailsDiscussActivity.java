package cn.eoe.app.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.eoe.app.R;
import cn.eoe.app.biz.DiscussDao;
import cn.eoe.app.entity.DetailsDiscussItem;
import cn.eoe.app.entity.DetailsDiscussJson;
import cn.eoe.app.entity.DetailsOwnDiscussJson;
import cn.eoe.app.https.HttpUtils;
import cn.eoe.app.ui.base.BaseActivity;
import cn.eoe.app.utils.ImageUtil;
import cn.eoe.app.utils.ImageUtil.ImageCallback;
import cn.eoe.app.utils.Utility;

public class DetailsDiscussActivity extends BaseActivity implements
		OnClickListener {
	
	private ListView mListview;
	private Button mEnter;
	private ImageView mGoBack;
	private EditText mEditDiscuss;
	private SimpleAdapter mAdapter;
	private List<Map<String, Object>> mlist;
	private TextView title;
	private String mDiscussList = "";
	private String mDiscuss = "";
	ObjectMapper mObjectMapper = new ObjectMapper();
	SharedPreferences share;
	private String mKey = "";
	private DiscussDao mDao;
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.details_discuss_activity);
		
		share = getSharedPreferences(UserLoginUidActivity.SharedName,
				Context.MODE_PRIVATE);		
		mKey = share.getString(UserLoginUidActivity.KEY, "");
		
		mDao = new DiscussDao(this);
		
		getIntentEntra();
		initControl();
		initList();
		new DataAsyncTask().execute(mDiscussList);
	}

	/**
	 * 接收参数
	 */
	private void getIntentEntra() {
		Intent intent = getIntent();
		mDiscussList = intent.getStringExtra("discuss_list");
		mDiscuss = intent.getStringExtra("discuss");
		mTitle = intent.getStringExtra("title");
	}

	/**
	 * 初始化控件
	 */
	private void initControl() {
		mListview = (ListView) findViewById(R.id.details_listview_show);
		mEnter = (Button) findViewById(R.id.details_button_enter);
		mGoBack = (ImageView) findViewById(R.id.details_imageview_gohome);
		mEditDiscuss = (EditText) findViewById(R.id.details_edittext_discuss);
		mEnter.setOnClickListener(this);
		mGoBack.setOnClickListener(this);
		title = (TextView) findViewById(R.id.details_textview_title);
		title.setText(mTitle);
	}

	/**
	 * 加载评论列表
	 */
	private void initList() {
		mlist = new ArrayList<Map<String, Object>>();
		
		mAdapter = new SimpleAdapter(this, mlist,
				R.layout.details_discuss_item, new String[] { "image", "name",
						"content", "time" }, new int[] {
						R.id.details_imageview_head,
						R.id.details_textview_name,
						R.id.details_textview_content,
						R.id.details_textview_time }) {
			@Override
			public void setViewImage(ImageView v, String value) {				
				super.setViewImage(v, value);
				ImageUtil.setThumbnailView(value, v,
						DetailsDiscussActivity.this, callback, false);
			}

			ImageCallback callback = new ImageCallback() {
				@Override
				public void loadImage(Bitmap bitmap, String imagePath) {					
					try {
						ImageView img = (ImageView) mListview
								.findViewWithTag(imagePath);
						img.setImageBitmap(bitmap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		};
		
		mListview.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {		
		switch (v.getId()) {
		case R.id.details_button_enter://发表评论			
			new PublishAsyncTask().execute();
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(DetailsDiscussActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.details_imageview_gohome://退出
			finish();
			break;
		}
	}

	/**
	 * 加载评论列表
	 *
	 */
	class DataAsyncTask extends
			AsyncTask<String, Void, List<Map<String, Object>>> {
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {			
			String result;
			try {
				result = HttpUtils.getByHttpClient(
						DetailsDiscussActivity.this, params[0]);
			} catch (Exception e1) {				
				e1.printStackTrace();
				return null;
			}
			DetailsDiscussJson DiscussJson = null;
			try {
				DiscussJson = mObjectMapper.readValue(result,
						new TypeReference<DetailsDiscussJson>() {
						});
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (DiscussJson != null) {
				List<DetailsDiscussItem> items = DiscussJson.getResponse()
						.getItems();
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Map<String, Object> map;
				for (int i = 0; i < items.size(); i++) {
					map = new HashMap<String, Object>();
					map.put("image", items.get(i).getHead_image_url());
					map.put("name", items.get(i).getUname());
					map.put("content", items.get(i).getBody());
					map.put("time", Date(items.get(i).getTime()));
					list.add(map);
				}
				return list;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {			
			super.onPostExecute(result);
			if (result == null)
				return;
			mlist.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 发表评论
	 *
	 */
	class PublishAsyncTask extends AsyncTask<Void, Void, Map<String, Object>> {
		private String getUrl(String url) {
			return String.format(url + "&body=%s", mEditDiscuss.getText()
					.toString())
					+ Utility.getParams(mKey);
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {			
			if (mKey.equals(""))
				return null;
			DetailsOwnDiscussJson json = mDao
					.mapperJson(getUrl(mDiscuss), true);
			if (json != null && json.getResponse().getIsErr() == 0) {
				DetailsDiscussItem item = json.getResponse().getItem();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("image", item.getHead_image_url());
				map.put("name", item.getUname());
				map.put("content", item.getBody());
				map.put("time", Date(item.getTime()));
				return map;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {			
			super.onPostExecute(result);
			if (result == null) {
				showLongToast(getResources().getString(R.string.discuss_fail));
				return;
			}
			mlist.add(result);
			showLongToast(getResources().getString(R.string.discuss_succeed));
			mAdapter.notifyDataSetChanged();
		}
	}

	private String Date(String longtime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time = new Long(longtime + "000");
		String result = format.format(time);
		return result;
	}
}
