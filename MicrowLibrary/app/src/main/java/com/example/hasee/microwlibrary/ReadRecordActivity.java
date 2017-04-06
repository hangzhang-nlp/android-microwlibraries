package com.example.hasee.microwlibrary;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasee.microwlibrary.com.example.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GeniusHe on 2017/3/16.
 */

public class ReadRecordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ListView mlistView=null;
    private Tools tools ;
    String userName;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
       // textView.findViewById(R.id.textView);
       // textView.setText("阅读记录");
        Intent intent1=getIntent();
        userName=intent1.getStringExtra("userName");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mlistView=(ListView)findViewById(R.id.usedBooksList1);
        List<Book> dataArray=new ArrayList<Book>();

        tools = new Tools();
        String getJson = tools.getBookRecords("getRecord" ,userName);
        if(getJson.equals(null)){
            Toast.makeText(ReadRecordActivity.this, "您还没有读过任何书籍哦", Toast.LENGTH_SHORT).show();
        }else {
            dataArray = tools.getList(getJson,"record");

            MapListImageAndTextListAdapter adapter = new MapListImageAndTextListAdapter(this, dataArray, mlistView);
            mlistView.setAdapter(adapter);
        }
}}
