package com.example.hasee.microwlibrary.MoodNote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.hasee.microwlibrary.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteEditActivity extends AppCompatActivity {


	public static int ENTER_STATE = 0;
	public static String last_content;
	public static int id;

	private TextView tvEditAddress;
	private Button btnEditOk;
	private EditText editWrite;
	private TextView tvEditDate;
	private SimpleDateFormat sdf;
	private Date date;
	private OkNoteDB okNoteDB;
	private Toolbar toolbar;
	private String userName;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_note);
		btnEditOk = (Button) findViewById(R.id.btn_edit_ok);
		editWrite = (EditText) findViewById(R.id.edit_write_note);
		tvEditDate = (TextView) findViewById(R.id.tv_edit_date);
		tvEditAddress= (TextView) findViewById(R.id.tv_edit_address);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		sdf = new SimpleDateFormat("yyyy.MM.dd");
		date = new Date();
		okNoteDB = OkNoteDB.getInstance(this);
		Intent intent1=getIntent();
		userName=intent1.getStringExtra("userName");

		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		tvEditAddress.setText("123");
		tvEditDate.setText(sdf.format(date));
		Bundle myBundle = this.getIntent().getExtras();
		last_content = myBundle.getString("info");
		Log.d("LAST_CONTENT", last_content);
		editWrite.setText(last_content);
		editWrite.setSelection(last_content.length());
		btnEditOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 获取日志内容
				String NoteContent = editWrite.getText().toString();
				// 获取日志日期
				String NoteDate = sdf.format(date);
				// 获取日志地址
				String NoteAddress = "123";
				// 添加一个新的日志
				if (ENTER_STATE == 0) {
					Log.d("ENTER_STATE == 0", "执行");
					if (!NoteContent.equals("")) {
						Note note = new Note();
						note.setNoteContent(NoteContent);
						note.setNoteAddress(NoteAddress);
						note.setNoteDate(NoteDate);
						okNoteDB.saveNote(note);

					}else{
						//Toast.makeText(this, "您还没有写下您的心情", Toast.LENGTH_SHORT).show();
					}
				}
				// 查看并,修改一个已有的日志s
				else {
					Log.d("ENTER_STATE != 0   id： ", id+"");
					okNoteDB.updateNote(NoteContent,id);
				}
				
				Intent data = new Intent(NoteEditActivity.this, NoteActivity.class);
				data.putExtra("userName", userName);
				setResult(2, data);
				startActivityForResult(data, 1);
				
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if (requestCode == 3 && resultCode == 4) {
		// last_content=data.getStringExtra("data");
		// Log.d("LAST_STRAING", last_content+"gvg");
		// }
	}
	


}
