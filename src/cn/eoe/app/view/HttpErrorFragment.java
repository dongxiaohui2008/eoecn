package cn.eoe.app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.eoe.app.R;

/**
 * 404。。没有相关数据。。
 *
 */
public class HttpErrorFragment extends Fragment {

	public HttpErrorFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.http_error_fragment, null);
		return view;
	}	
}
