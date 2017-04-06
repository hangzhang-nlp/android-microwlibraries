package com.example.hasee.microwlibrary;

import android.content.Intent;
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
 * Created by HASEE on 2017/3/29.
 */

public class RecommedActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ListView mlistView=null;
    private Tools tools ;
    private String userName;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        textView = (TextView)findViewById(R.id.textView);
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
        textView.setText("书童推荐");
        tools = new Tools();
        String getJson = tools.getBookRecords("getRecommend",userName);
        if(getJson.equals(null)){
            Toast.makeText(RecommedActivity.this, "您还没有读过任何书籍哦，读的阅读，推荐越准", Toast.LENGTH_SHORT).show();
        }else {
            dataArray = tools.getList(getJson,"recommend");

            MapListImageAndTextListAdapter adapter = new MapListImageAndTextListAdapter(this, dataArray, mlistView);
            mlistView.setAdapter(adapter);
        }

    }
}

