package cn.eoe.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import cn.eoe.app.R;
import cn.eoe.app.biz.BaseDao;
import cn.eoe.app.biz.MyCsdn;
import cn.eoe.app.config.Urls;
import cn.eoe.app.entity.CategorysEntity;
import cn.eoe.app.entity.NewsCategoryListEntity;
import cn.eoe.app.entity.NewsResponseEntity;
import cn.eoe.app.ui.MainActivity.MyTask;
import cn.eoe.app.ui.base.BaseActivity;
//import com.umeng.update.UmengUpdateAgent;
import cn.eoe.app.view.NewsFragment;

public class SplashActivity extends BaseActivity {

	private Handler mHandler = new Handler();
	
	private MyCsdn mycsdn;
	private NewsResponseEntity newsResponseData;
	private List<Object> categoryList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = View.inflate(this, R.layout.start_activity, null);
		setContentView(view);
		
//		Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
//		view.startAnimation(animation);
//		animation.setAnimationListener(new AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation arg0) {
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation arg0) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						//goHome();
//					}
//				}, 500);
//			}
//		});
		
		mycsdn = new MyCsdn(this);
		mycsdn.setUrl(Urls.NewsA_LIST);// 产业资讯
		new MyTask().execute(mycsdn);
	}

	/**
	 * (异步)加载分类list的task
	 * 
	 */
	public class MyTask extends AsyncTask<BaseDao, String, Map<String, Object>> {
		
		public MyTask() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		// 后台执行，比较耗时的操作都可以放在这里。
		@Override
		protected Map<String, Object> doInBackground(BaseDao... params) {
			BaseDao dao = params[0];
			List<CategorysEntity> categorys = new ArrayList<CategorysEntity>();
			Map<String, Object> map = new HashMap<String, Object>();
			if (dao instanceof MyCsdn) {
				// mTag = 1;
				if ((newsResponseData = mycsdn.mapperJson(true)) != null) {

					categorys = newsResponseData.getCategorys();
					categoryList = (List) newsResponseData.getList();

					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			}
			return map;
		}

		// 相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。
		// 此方法在主线程执行，任务执行的结果作为此方法的参数返回
		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			goHome();
		}
	}

	protected void onResume() {
		super.onResume();
	}

	private void goHome() {
		openActivity(MainActivity.class);
		defaultFinish();
	}
}
