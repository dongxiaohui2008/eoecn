package cn.eoe.app.db.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.eoe.app.db.DBHelper;
import cn.eoe.app.db.DetailColumn;

public class DetailDB {

	private DBHelper dbHelper;

	public DetailDB(Context context) {
		dbHelper = DBHelper.getInstance(context);
	}

	/**
	 * 插入操作
	 * 
	 */
	public void insertSQL(String url, String Key, int good, int bad, int collect) {
		String SQL = "insert into " + DetailColumn.TABLE_NAME + "("
				+ DetailColumn.URL + "," + DetailColumn.KEY + ","
				+ DetailColumn.GOOD + "," + DetailColumn.BAD + ","
				+ DetailColumn.COLLECT + ") values('" + url + "','" + Key
				+ "','" + good + "','" + bad + "','" + collect + "')";
		dbHelper.ExecSQL(SQL);
	}

	/**
	 * 更改评价
	 * 
	 */
	public int updateSQL(int id, int good, int bad, int collect) {
		ContentValues values = new ContentValues();
		values.put(DetailColumn.GOOD, good);
		values.put(DetailColumn.BAD, bad);
		values.put(DetailColumn.COLLECT, collect);
		return dbHelper.update(DetailColumn.TABLE_NAME, values,
				DetailColumn._ID + "=?", new String[] { id + "" });
	}

	/**
	 * 删除
	 * 
	 */	
	public int deleteSQL(int id) {
		return dbHelper.delete(DetailColumn.TABLE_NAME, id);
	}

	/**
	 * 查询
	 * 
	 */	
	public Cursor querySQL(String url) {
		String SQL = "select * from " + DetailColumn.TABLE_NAME + " where "
				+ DetailColumn.URL + "='" + url + "'";
		return dbHelper.rawQuery(SQL, null);
	}

	/**
	 * 关闭数据库
	 * 
	 */	
	public void dbClose() {
		dbHelper.closeDb();
	}
}
