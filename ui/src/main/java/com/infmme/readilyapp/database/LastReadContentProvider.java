package com.infmme.readilyapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by infm on 6/10/14. Enjoy ;)
 */
public class LastReadContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.infmme.readilyapp.provider";
	public static final String PATH = LastReadDBHelper.TABLE;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final int URI_LAST_READ = 1;
	public static final int URI_LAST_READ_ID = 2;
	static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
			+ AUTHORITY + "." + PATH;
	static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
			+ AUTHORITY + "." + PATH;
	private static final UriMatcher uriMatcher;

	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, PATH, URI_LAST_READ);
		uriMatcher.addURI(AUTHORITY, PATH + "/#", URI_LAST_READ_ID);
	}

	private LastReadDBHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public boolean onCreate(){
		dbHelper = new LastReadDBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
		switch (uriMatcher.match(uri)){
			case URI_LAST_READ:
				break;
			case URI_LAST_READ_ID:
				String rowId = uri.getLastPathSegment();
				selection = updateSingleSelection(selection, rowId);
				break;
			default:
				throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(LastReadDBHelper.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri){
		switch (uriMatcher.match(uri)){
			case URI_LAST_READ:
				return CONTENT_TYPE;
			case URI_LAST_READ_ID:
				return CONTENT_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values){
		if (uriMatcher.match(uri) != URI_LAST_READ){ throw new IllegalArgumentException("Wrong URI: " + uri); }

		db = dbHelper.getWritableDatabase();
		long rowId = db.insertWithOnConflict(LastReadDBHelper.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
		getContext().getContentResolver().notifyChange(resultUri, null);
		return resultUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs){
		switch (uriMatcher.match(uri)){
			case URI_LAST_READ:
				break;
			case URI_LAST_READ_ID:
				String rowId = uri.getLastPathSegment();
				selection = updateSingleSelection(selection, rowId);
				break;
			default:
				throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		db = dbHelper.getWritableDatabase();
		int count = db.delete(LastReadDBHelper.TABLE, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
		switch (uriMatcher.match(uri)){
			case URI_LAST_READ:
				break;
			case URI_LAST_READ_ID:
				String rowId = uri.getLastPathSegment();
				selection = updateSingleSelection(selection, rowId);
				break;
			default:
				throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		db = dbHelper.getWritableDatabase();
		int count = db.update(LastReadDBHelper.TABLE, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private String updateSingleSelection(String selection, String rowId){
		if (TextUtils.isEmpty(selection)){ selection = LastReadDBHelper.KEY_ROWID + " = " + rowId; } else {
			selection += " AND " + LastReadDBHelper.KEY_ROWID + " = " + rowId;
		}
		return selection;
	}
}
