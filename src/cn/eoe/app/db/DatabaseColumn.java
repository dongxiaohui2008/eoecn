package cn.eoe.app.db;

import java.util.ArrayList;
import java.util.Map;

import android.net.Uri;
import android.provider.BaseColumns;

public abstract class DatabaseColumn implements BaseColumns {
	/**
	 * ContentProvider地址
	 */
	public static final String AUTHORITY = "cn.eoe.app.provider";
	/**
	 * 数据库名称
	 */
	public static final String DATABASE_NAME = "eoecn.db";
	/**
	 * 数据库版本
	 */
	public static final int DATABASE_VERSION = 1;
	/**
	 * 返回所有子类名
	 */
	public static final String[] SUBCLASSES = new String[] {
			"cn.eoe.app.db.BlogColumn", "cn.eoe.app.db.NewsColumn",
			"cn.eoe.app.db.DetailColumn", "cn.eoe.app.db.ImageCacheColumn",
			"cn.eoe.app.db.RequestCacheColumn" };

	/**
	 * 返回建表语句
	 * 
	 * @return
	 */
	public String getTableCreateor() {
		return getTableCreator(getTableName(), getTableMap());
	}

	/**
	 * 返回所有子类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final Class<DatabaseColumn>[] getSubClasses() {
		ArrayList<Class<DatabaseColumn>> classes = new ArrayList<Class<DatabaseColumn>>();
		Class<DatabaseColumn> subClass = null;
		for (int i = 0; i < SUBCLASSES.length; i++) {
			try {
				subClass = (Class<DatabaseColumn>) Class.forName(SUBCLASSES[i]);
				classes.add(subClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}
		}
		return classes.toArray(new Class[0]);
	}

	/**
	 * 拼接建表语句
	 * 
	 * @param tableName
	 * @param map
	 * @return
	 */
	private static final String getTableCreator(String tableName,
			Map<String, String> map) {
		String[] keys = map.keySet().toArray(new String[0]);
		String value = null;
		StringBuilder creator = new StringBuilder();
		creator.append("CREATE TABLE ").append(tableName).append("( ");
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			value = map.get(keys[i]);
			creator.append(keys[i]).append(" ");
			creator.append(value);
			if (i < length - 1) {
				creator.append(",");
			}
		}
		creator.append(")");
		return creator.toString();
	}

	/**
	 * 返回表名称
	 * 
	 * @return
	 */
	abstract public String getTableName();

	/**
	 * 返回表地址
	 * 
	 * @return
	 */
	abstract public Uri getTableContent();

	/**
	 * 返回表字段集合
	 * 
	 * @return
	 */
	abstract protected Map<String, String> getTableMap();
}
