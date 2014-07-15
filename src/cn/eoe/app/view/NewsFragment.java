package cn.eoe.app.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.eoe.app.R;
import cn.eoe.app.biz.NewsDao;
import cn.eoe.app.entity.NewsCategoryListEntity;
import cn.eoe.app.entity.NewsContentItem;
import cn.eoe.app.entity.NewsMoreResponse;
import cn.eoe.app.utils.ImageUtil;

@SuppressLint({ "NewApi", "ValidFragment" })
public class NewsFragment extends BaseListFragment {

	public Activity mActivity;
	private List<NewsContentItem> items_list = new ArrayList<NewsContentItem>();//当前-资讯列表
	private String more_url;//加载更多地址
	private MyAdapter mAdapter;//适配器
	private NewsCategoryListEntity loadMoreEntity;//更多-资讯列表

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				more_url = loadMoreEntity.getMore_url();
				mAdapter.appendToList(loadMoreEntity.getItems());//加载下一页
				break;
			}
			onLoad();
		}
	};

	// add this constructor by King0769, 2013/5/7
	// in order to solve an exception that "can't instantiate class cn.eoe.app.view.NewsFragment; no empty constructor"
	// I found it in this case : 1.open eoe program -> 2.change system language -> 3.reopen eoe, can see FC(force close)
	// I think this bug will happens in many cases.
	public NewsFragment() {
	}
	
	public NewsFragment(Activity c, NewsCategoryListEntity categorys) {
		this.mActivity = c;
		if (categorys != null) {
			this.items_list = categorys.getItems();
			more_url = categorys.getMore_url();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		//NewsFragment的基类实现了IXListViewListener接口： 监听onRefresh、onLoadMore两个事件
		listview.setXListViewListener(this);
		
		// 加载XListView
		mAdapter = new MyAdapter();
		mAdapter.appendToList(items_list);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewsContentItem item = (NewsContentItem) mAdapter
						.getItem(position - 1);
				startDetailActivity(mActivity, item.getDetail_url(), "资讯",
						item.getTitle());
			}
		});
		return view;
	}

	class MyAdapter extends BaseAdapter {
		List<NewsContentItem> mList = new ArrayList<NewsContentItem>();

		public MyAdapter() {
		}

		public void appendToList(List<NewsContentItem> lists) {
			if (lists == null) {
				return;
			}
			mList.addAll(lists);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			NewsContentItem item = mList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater
						.inflate(R.layout.news_item_layout, null);
				holder.title_ = (TextView) convertView
						.findViewById(R.id.news_title);
				holder.short_ = (TextView) convertView
						.findViewById(R.id.news_short_content);
				holder.img_thu = (ImageView) convertView
						.findViewById(R.id.img_thu);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();//??利用系统中缓存的VIEW
			}
			holder.title_.setText(item.getTitle());
			holder.short_.setText(item.getShort_content());
			String img_url = item.getThumbnail_url();			
			if (img_url.equals(null) || img_url.equals("")) {
				holder.img_thu.setVisibility(View.GONE);
			} else {
				holder.img_thu.setVisibility(View.VISIBLE);
				ImageUtil.setThumbnailView(img_url, holder.img_thu, mActivity,
						callback1, false);//设置图片。。。。。缓存。。。。。。。。。
			}
			return convertView;
		}
	}

	static class ViewHolder {
		public TextView title_;
		public TextView short_;
		public ImageView img_thu;
	}

	@Override
	public void onRefresh() {
		onLoad();
	}

	@Override
	public void onLoadMore() {
		if (more_url==null || more_url.equals("")) {
			mHandler.sendEmptyMessage(1);
			return;
		} else {
			new Thread() {
				@Override
				public void run() {
					NewsMoreResponse response = new NewsDao(mActivity)
							.getMore(more_url);
					if (response != null) {
						loadMoreEntity = response.getResponse();
						mHandler.sendEmptyMessage(0);
					}
					super.run();
				}
			}.start();
		}
	}
}
