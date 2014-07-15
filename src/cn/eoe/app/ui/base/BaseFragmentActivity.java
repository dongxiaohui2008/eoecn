package cn.eoe.app.ui.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import cn.eoe.app.R;
//import com.umeng.analytics.MobclickAgent;

/**
 * 新增了umeng监测（继承自FragmentActivity）
 *
 */
public class BaseFragmentActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//窗口无标题
		//MobclickAgent.onError(this);// umeng监测
	}

	@Override
	protected void onPause() {
		super.onPause();
		//MobclickAgent.onPause(this);// umeng监测
	}

	@Override
	protected void onResume() {
		super.onResume();
		//MobclickAgent.onResume(this);// umeng监测
	}

	public void finish() {
		super.finish();
		// Activity切换效果（overridePendingTransition）
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void defaultFinish() {
		super.finish();
	}
}
