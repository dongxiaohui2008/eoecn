package cn.eoe.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import cn.eoe.app.config.Configs;
import cn.eoe.app.config.Constants;
import cn.eoe.app.db.DBHelper;
import cn.eoe.app.db.RequestCacheColumn;
import cn.eoe.app.https.HttpUtils;

//整个调用方法就是异步，这里不需要异步
public class RequestCacheUtil {

	private static final String TAG = "RequestCacheUtil";
	/**
	 * SoftReference多用作来实现cache机制
	 */
	private static LinkedHashMap<String, SoftReference<String>> RequestCache = new LinkedHashMap<String, SoftReference<String>>(
			20);

	// [start] 公有方法
	/**
	 * 根据RequestUrl获取内容
	 * 
	 * @param context
	 * @param RequestUrl
	 * @param source_type
	 * @param content_type
	 * @param UseCache
	 * @return
	 */
	public static String getRequestContent(Context context, String RequestUrl,
			String source_type, String content_type, boolean UseCache) {
		DBHelper dbHelper = DBHelper.getInstance(context);
		String md5 = MD5.encode(RequestUrl);
		// 缓存目录
		if (!CommonUtil.sdCardIsAvailable()) {
			String cachePath = context.getCacheDir().getAbsolutePath() + "/"
					+ md5; // data里的缓存
			return getCacheRequest(context, RequestUrl, cachePath, source_type,
					content_type, dbHelper, UseCache);
		} else {
			String imagePath = getExternalCacheDir(context) + File.separator
					+ md5; // sd卡
			return getCacheRequest(context, RequestUrl, imagePath, source_type,
					content_type, dbHelper, UseCache);
		}
	}

	// [end]

	// [start] 私有方法
	/**
	 * 获得程序在sd卡上的cahce目录
	 * 
	 */
	@SuppressLint("NewApi")
	private static String getExternalCacheDir(Context context) {
		// android 2.2 以后才支持的特性
		if (hasExternalCacheDir()) {
			return context.getExternalCacheDir().getPath() + File.separator
					+ "request";
		}
		// android 2.2以前我们需要自己构造缓存目录
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/request/";
		return Environment.getExternalStorageDirectory().getPath() + cacheDir;
	}

	/**
	 * 当前sdk版本是否>=2.2版
	 * 
	 * @return
	 */
	private static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;// SDK版本号的枚举类(2.2版)
	}

	/**
	 * 获取缓存内容
	 * 
	 * @param context
	 * @param requestUrl
	 * @param requestPath
	 * @param source_type
	 * @param content_type
	 * @param dbHelper
	 * @param useCache
	 * @return
	 */
	private static String getCacheRequest(Context context, String requestUrl,
			String requestPath, String source_type, String content_type,
			DBHelper dbHelper, boolean useCache) {
		String result = "";
		if (useCache) {
			result = getStringFromSoftReference(requestUrl);
			if (!result.equals(null) && !result.equals("")) {
				return result;
			}
			result = getStringFromLocal(requestPath, requestUrl, dbHelper);
			if (!result.equals(null) && !result.equals("")) {
				putStringForSoftReference(requestUrl, result);
				return result;
			}
		}
		result = getStringFromWeb(context, requestPath, requestUrl,
				source_type, content_type, dbHelper);
		return result;
	}

	/**
	 * 缓存
	 * 
	 * @param requestUrl
	 * @param result
	 */
	private static void putStringForSoftReference(String requestUrl,
			String result) {
		SoftReference<String> referece = new SoftReference<String>(result);
		RequestCache.put(requestUrl, referece);
	}

	/**
	 * 获取web内容并保存。。。
	 * 
	 * @param context
	 * @param requestPath
	 * @param requestUrl
	 * @param source_type
	 * @param content_type
	 * @param dbHelper
	 * @return
	 */
	private static String getStringFromWeb(Context context, String requestPath,
			String requestUrl, String source_type, String content_type,
			DBHelper dbHelper) {
		String result = "";

		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("/[0-9]*.shtml");
		java.util.regex.Matcher match = pattern.matcher(requestUrl
				.substring(requestUrl.lastIndexOf("/")));

		// hardy拓展方法
		if (match.matches()) {
			// 文章
			Document doc = null;
			try {
				doc = Jsoup.connect(requestUrl).timeout(5000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Element ele = doc.getElementsByClass("content").get(0);

			String title = ele.getElementsByTag("h1").get(0).html();
			String desc = ele.getElementsByClass("art_info").get(0).html();
			String cnt = ele.getElementsByClass("art_content").get(0).html();
			String artInfo = ele.getElementsByClass("art_info").get(0).html();

			result = "<style> /* CSS Document */  "
					+ " img{} "
					+ " .show{margin:0 auto;font:12px/24px '微软雅黑',Arial,Helvetica,sans-serif;; color:#757575;}"
					+ "	.show h1{font-size:22px;line-height:36px;font-weight:normal;color:#363636;}"
					+ "	 .show h4{font-size:14px;font-weight:normal;line-height:20px;color:#bdbdbd;}"
					+ "	 </style>" + "	<div class=\"show\"><h1>" + title
					+ "</h1>" + "	<h4><span style=\"padding-right:8px;\">"
					+ artInfo + "</span>"
					+ "	<div class=\"content\"><div class='markdown'><p>" + cnt
					+ "</p></div></div></div>";

			Elements imgs = ele.getElementsByTag("img");
			for (int i = 0; i < imgs.size(); i++) {
				Element img = imgs.get(i);

				String flg = img.className();
				if (!flg.equals("img_p")) {
					String wid = img.attributes().get("width");
					String sty = img.attributes().get("style");

					result = result.replace("width=\"" + wid + "\"", "");
					result = result.replace("style=\"" + sty + "\"", "");
				}
			}
		} else if (requestUrl.contains("entgroup")) {

			// http://interview.entgroup.cn/index1.shtml&screen=720*1280
			if (requestUrl.contains("&")) {
				requestUrl = requestUrl.substring(0,
						requestUrl.lastIndexOf("&"));
			}

			// 列表页
			Document doc = null;
			try {
				doc = Jsoup.connect(requestUrl).timeout(5000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String moreUrl = "";
			Elements pages = doc.getElementsByClass("pagelist").get(0)
					.getElementsByTag("li");
			String maxpage = pages.get(pages.size() - 1).getElementsByTag("a")
					.get(0).attributes().get("href");
			// http://interview.entgroup.cn/index.shtml
			String currpage = requestUrl
					.substring(requestUrl.lastIndexOf("/") + 1);

			int max = Integer.parseInt(maxpage.replace("index", "").replace(
					".shtml", ""));
			int curr = Integer.parseInt(currpage.replace("index", "")
					.replace(".shtml", "").isEmpty() ? "0" : currpage.replace(
					"index", "").replace(".shtml", ""));
			if (curr < max) {
				curr = curr + 1;
				if (curr == max) {
					moreUrl = "";
				} else {
					moreUrl = requestUrl.substring(0,
							requestUrl.lastIndexOf("/") + 1)
							+ "index" + curr + ".shtml";
				}
			}

			if (curr == 1) {
				result = extracted(requestUrl, doc, moreUrl);
			} else {
				result = extracted2(requestUrl, doc, moreUrl);
			}
		} else if (!requestUrl.contains("entgroup")) {
			try {
				result = HttpUtils.getByHttpClient(context, requestUrl);
				if (result.equals(null) && result.equals("")) {
					return result;
				}
				// 更新数据库
				Cursor cursor = getStringFromDB(requestUrl, dbHelper);
				updateDB(cursor, requestUrl, source_type, content_type,
						dbHelper);
				saveFileByRequestPath(requestPath, result);
				putStringForSoftReference(requestUrl, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 列表首次加载
	 */
	private static String extracted(String requestUrl, Document doc,
			String moreUrl) {
		String result;
		result = "{\"response\":{\"date\":1405412322,\"categorys\":[{\"name\":\"人物访谈\",\"url\":\"http://api.eoe.cn//client/news?k=lists&t=new\"}], "
				+ " \"list\":[{\"name\":\"人物访谈\",\"more_url\":\""
				+ moreUrl
				+ "\",\"items\":[ ";

		Elements eles = doc.getElementsByClass("listbox").get(0)
				.getElementsByTag("li");
		long nowTime = System.currentTimeMillis();

		for (int i = 0; i < eles.size(); i++) {
			Element ele = eles.get(i);

			String title = ele.getElementsByTag("a").get(0).html();
			String desc = ele.getElementsByTag("p").get(0).text();
			String url = requestUrl.substring(0, requestUrl.lastIndexOf("/"))
					+ ele.getElementsByTag("a").get(0).attributes().get("href");

			if (i < eles.size() - 1) {
				result += " {\"id\":\"18633\",\"thumbnail_url\":\"\",\"title\":\""
						+ title
						+ "\",\"time\":\"1405303382\",\"short_content\":\""
						+ desc + "\",\"detail_url\":\"" + url + "\"}, ";
			} else {
				result += " {\"id\":\"18633\",\"thumbnail_url\":\"\",\"title\":\""
						+ title
						+ "\",\"time\":\"1405303382\",\"short_content\":\""
						+ desc + "\",\"detail_url\":\"" + url + "\"} ";
			}
		}

		result += " ]}]}}";
		return result;
	}

	/**
	 * 列表加载more
	 */
	private static String extracted2(String requestUrl, Document doc,
			String moreUrl) {
		String result;
		result = "{\"response\":{\"name\":\"人物访谈\",\"more_url\":\"" + moreUrl
				+ "\", " + " \"items\":[ ";

		Elements eles = doc.getElementsByClass("listbox").get(0)
				.getElementsByTag("li");
		long nowTime = System.currentTimeMillis();

		for (int i = 0; i < eles.size(); i++) {
			Element ele = eles.get(i);

			String title = ele.getElementsByTag("a").get(0).html().replace(",", "\\,");
			String desc = ele.getElementsByTag("p").get(0).text().replace(",", "\\,");
			String url = requestUrl.substring(0, requestUrl.lastIndexOf("/"))
					+ ele.getElementsByTag("a").get(0).attributes().get("href").replace(",", "\\,");
			
			desc=desc.replace("(", "").replace(")", "").replace(",", "");

			if (i < eles.size() - 1) {
				result += " {\"id\":\"18633\",\"thumbnail_url\":\"\",\"title\":\""
						+ title
						+ "\",\"time\":\"1405303382\",\"short_content\":\""
						+ desc + "\",\"detail_url\":\"" + url + "\"}, ";
			} else {
				result += " {\"id\":\"18633\",\"thumbnail_url\":\"\",\"title\":\""
						+ title
						+ "\",\"time\":\"1405303382\",\"short_content\":\""
						+ desc + "\",\"detail_url\":\"" + url + "\"} ";
			}
		}

		result += " ]}}";
		return result;
	}

	/**
	 * 根据RequestPath保存内容到文件
	 * 
	 * @param requestPath
	 * @param result
	 */
	private static void saveFileByRequestPath(String requestPath, String result) {
		deleteFileFromLocal(requestPath);
		saveFileForLocal(requestPath, result);
	}

	/**
	 * 保存内容到文件
	 * 
	 * @param requestPath
	 * @param result
	 */
	private static void saveFileForLocal(String requestPath, String result) {
		File file = new File(requestPath);
		if (!file.exists()) {
			try {
				File parentFile = file.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				file.createNewFile();
				FileOutputStream fout = new FileOutputStream(file);
				byte[] buffer = result.getBytes();
				fout.write(buffer);
				fout.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * update首条记录、其余insert
	 * 
	 * @param cursor
	 * @param requestUrl
	 * @param source_type
	 * @param content_type
	 * @param dbHelper
	 */
	private static void updateDB(Cursor cursor, String requestUrl,
			String source_type, String content_type, DBHelper dbHelper) {
		if (cursor.moveToFirst()) {
			// 更新
			int id = cursor.getInt(cursor
					.getColumnIndex(RequestCacheColumn._ID));
			long timestamp = System.currentTimeMillis();
			String SQL = "update " + RequestCacheColumn.TABLE_NAME + " set "
					+ RequestCacheColumn.Timestamp + "=" + timestamp
					+ " where " + RequestCacheColumn._ID + "=" + id;
			dbHelper.ExecSQL(SQL);
		} else {
			// 添加
			String SQL = "insert into " + RequestCacheColumn.TABLE_NAME + "("
					+ RequestCacheColumn.URL + ","
					+ RequestCacheColumn.SOURCE_TYPE + ","
					+ RequestCacheColumn.Content_type + ","
					+ RequestCacheColumn.Timestamp + ") values('" + requestUrl
					+ "','" + source_type + "','" + content_type + "','"
					+ System.currentTimeMillis() + "')";
			dbHelper.ExecSQL(SQL);
		}
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param requestUrl
	 * @return
	 */
	private static String getStringFromSoftReference(String requestUrl) {
		if (RequestCache.containsKey(requestUrl)) {
			SoftReference<String> reference = RequestCache.get(requestUrl);
			String result = (String) reference.get();
			if (result != null && !result.equals("")) {
				return result;
			}
		}
		return "";
	}

	/**
	 * 获取内容
	 * 
	 * @param requestPath
	 * @param requestUrl
	 * @param dbHelper
	 * @return
	 */
	private static String getStringFromLocal(String requestPath,
			String requestUrl, DBHelper dbHelper) {
		String result = "";
		Cursor cursor = getStringFromDB(requestUrl, dbHelper);
		if (cursor.moveToFirst()) {
			Long timestamp = cursor.getLong(cursor
					.getColumnIndex(RequestCacheColumn.Timestamp));
			String strContentType = cursor.getString(cursor
					.getColumnIndex(RequestCacheColumn.Content_type));
			long span = getSpanTimeFromConfigs(strContentType);
			long nowTime = System.currentTimeMillis();
			if ((nowTime - timestamp) > span * 60 * 1000) {
				// 过期
				deleteFileFromLocal(requestPath);
			} else {
				// 没过期
				result = getFileFromLocal(requestPath);
			}
		}
		return result;
	}

	/**
	 * 从db中查找数据
	 * 
	 * @param requestUrl
	 * @param dbHelper
	 * @return
	 */
	private static Cursor getStringFromDB(String requestUrl, DBHelper dbHelper) {
		String SQL = "select * from " + RequestCacheColumn.TABLE_NAME
				+ " where " + RequestCacheColumn.URL + "='" + requestUrl + "'";
		Cursor cursor = dbHelper.rawQuery(SQL, new String[] {});
		return cursor;
	}

	/**
	 * 读取文件
	 * 
	 * @param requestPath
	 * @return
	 */
	private static String getFileFromLocal(String requestPath) {
		File file = new File(requestPath);
		String result = "";
		if (file.exists()) {
			FileInputStream fileIn;
			try {
				fileIn = new FileInputStream(file);
				int length = fileIn.available();
				byte[] buffer = new byte[length];
				fileIn.read(buffer);
				result = EncodingUtils.getString(buffer, "UTF-8");
				fileIn.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		return "";
	}

	/**
	 * 删除文件
	 * 
	 */
	private static void deleteFileFromLocal(String requestPath) {
		File file = new File(requestPath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 根据类型获取缓存时间
	 * 
	 * @param str
	 * @return
	 */
	private static long getSpanTimeFromConfigs(String str) {
		long span = 0;
		if (str.equals(Constants.DBContentType.Content_list)) {
			span = Configs.Content_ListCacheTime;
		} else if (str.equals(Constants.DBContentType.Content_content)) {
			span = Configs.Content_ContentCacheTime;
		} else if (str.equals(Constants.DBContentType.Discuss)) {
			span = Configs.DiscussCacheTime;
		} else {
			span = Configs.Content_DefaultCacheTime;
		}
		return span;
	}
	// [end]
}
