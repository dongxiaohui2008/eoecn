package cn.eoe.app.db;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 尚未完成 query insert 的方法重写
 * 
 */
public class DBProvider extends ContentProvider {

	private DBHelper mDBHelper = null;
	private static final int NEWS = 1;
	private static final int BLOGS = 2;
	
	/**
	 * 用于匹配Uri
	 */
	private static final UriMatcher URIMATCHER;
	/**
	 * 注册Uri
	 */
	static {
		URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URIMATCHER.addURI(DatabaseColumn.AUTHORITY, BlogColumn.TABLE_NAME,
				BLOGS);
	}

	/**
	 * Init DB
	 */
	@Override
	public boolean onCreate() {
		mDBHelper = DBHelper.getInstance(getContext());
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		return db == null ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId = 0;
		int witch = URIMATCHER.match(uri);
		switch (witch) {
		case NEWS:
			break;
		case BLOGS:
			rowId = insert(BlogColumn.TABLE_NAME, null, values);
			break;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		int witch = URIMATCHER.match(uri);
		switch (witch) {
		case NEWS:
			count = delete(NewsColumn.TABLE_NAME, selection, selectionArgs);
			notifyChange(uri);
			break;
		case BLOGS:
			count = delete(BlogColumn.TABLE_NAME, selection, selectionArgs);
			notifyChange(uri);
			break;
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (URIMATCHER.match(uri)) {
		case NEWS:
			count = update(NewsColumn.TABLE_NAME, values, selection,
					selectionArgs);
			notifyChange(uri);
			break;
		case BLOGS:
			count = update(BlogColumn.TABLE_NAME, values, selection,
					selectionArgs);
			notifyChange(uri);
			break;
		}
		return count;
	}

	/********* Define Insert Delete Update Query ***********/
	private Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return null;
			}
			try {
				return db.query(table, columns, selection, selectionArgs,
						groupBy, having, orderBy);
			} catch (Exception e) {
				return null;
			}
		}
	}

	/**
	 * 删除
	 * @param table
	 * @param where
	 * @param whereArgs
	 * @return
	 */
	private int delete(String table, String where, String[] whereArgs) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			try {
				return db.delete(table, where, whereArgs);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	/**
	 * 修改
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	private int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			try {
				return db.update(table, values, whereClause, whereArgs);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	private long insert(String table, String nullColumnHack,
			ContentValues values) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			return db.insert(table, nullColumnHack, values);
		}
	}

	private void notifyChange(List<Uri> uri) {
		ContentResolver cr = getContext().getContentResolver();
		for (int i = 0; i < uri.size(); i++) {
			cr.notifyChange(uri.get(i), null);
		}

	}

	/**
	 * 通知更改
	 * @param uri
	 */
	private void notifyChange(Uri uri) {
		ContentResolver cr = getContext().getContentResolver();
		cr.notifyChange(uri, null);
	}
}
