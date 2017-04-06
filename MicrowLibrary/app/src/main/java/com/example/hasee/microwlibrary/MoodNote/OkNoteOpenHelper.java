package com.example.hasee.microwlibrary.MoodNote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class OkNoteOpenHelper extends SQLiteOpenHelper {

	/**
	 *建表语句
	 *
	 */
	public static final String CREATE_NOTE = "create table NOTE("
			+ "note_id integer primary key autoincrement,"
			+ "note_date text, "
			+ "note_content text, "
			+ "note_address text)";
	
	public OkNoteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_NOTE);//创建Note表
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
