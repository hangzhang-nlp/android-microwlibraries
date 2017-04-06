package com.example.hasee.microwlibrary.MoodNote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class OkNoteDB {

	/**
	 * 数据库名
	 */
	
	public static final String DB_NAME = "ok_note";

	/**
	 * 数据库版本
	 */
	
	public static final int VERSION = 1;
	
	private static OkNoteDB okNoteDB;
	
	private SQLiteDatabase db;
	
	private final String TAG = "OkNoteDB";

	private int countId = 1;

	/**
	 * 构造方法私有化
	 */
	
	private OkNoteDB(Context context){
		OkNoteOpenHelper dbHelper = new OkNoteOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 获取OkNoteDB的实例
	 */
	public synchronized static OkNoteDB getInstance(Context context){
		if(okNoteDB == null){
			okNoteDB = new OkNoteDB(context);
		}
		return okNoteDB;
		
	}

	/**
	 * 将心情内容保存到数据库
	 */
	public void saveNote(Note note){

		if (okNoteDB.loadNote().size()>0){
			List<Note> temp = okNoteDB.loadNote();
			for (Note tempNote:temp){
				if (tempNote.getId() == countId){
					countId++;
				}
			}
		}

		if(note != null){
			note.setId(countId);
			
			ContentValues values = new ContentValues();						
			values.put("note_id", note.getId());
			values.put("note_content",note.getNoteContent());
			values.put("note_date",note.getNoteDate());
			values.put("note_address", note.getNoteAddress());
			
			Log.d(TAG,
					"note_id "+note.getId()+
					"\nnote_content "+note.getNoteContent()+
					"\nnote_date "+note.getNoteDate()+
					"\nnote_address "+note.getNoteAddress()
					);
			
			db.insert("NOTE", null, values);
			countId++;
		}
	}

	/**
	 * 更新已有的内容
	 */
	public void updateNote(String NowContent,int noteId){
		ContentValues values = new ContentValues();	
		values.put("note_content", NowContent);
		db.update("NOTE", values, "note_id = ?", new String[] {noteId+""});

	}

	/**
	 * 删除已有的内容
	 */
	public void deleteNote(int noteId){
		db.delete("NOTE", "note_id = ?", new String[]{noteId+""});
	}

	/**
	 * 设置每一条记录的Id
	 */
	private int getCountId(){
		String sql_count = "SELECT COUNT(*) FROM note";
		SQLiteStatement statement = db.compileStatement(sql_count);
		long count = statement.simpleQueryForLong();
		Log.d("COUNT", count + "");
		countId++;
		return (int) count+countId;
	}



	/**
	 * 从数据库读取所有的心情信息
	 */
	public List<Note> loadNote(){
		List<Note> list = new ArrayList<Note>();
		Cursor cursor = db.query("NOTE", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Note note = new Note();
				//Log.d("loadNote","111");
				note.setId(cursor.getInt(cursor.getColumnIndex("note_id")));
				note.setNoteContent(cursor.getString(cursor.getColumnIndex("note_content")));
				note.setNoteDate(cursor.getString(cursor.getColumnIndex("note_date")));
				note.setNoteAddress(cursor.getString(cursor.getColumnIndex("note_address")));
				list.add(note);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		
		
		return list;
		
	}
	
	
	
}
