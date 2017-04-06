package com.example.hasee.microwlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by GeniusHe on 2017/3/15.
 */

public class WriteMoodActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editWriteMood;
    private String nowMood;
    private Button btnSend;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_mood);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editWriteMood = (EditText) findViewById(R.id.edit_write_mood);
        btnSend = (Button) findViewById(R.id.btn_enter);

        setSupportActionBar(toolbar);
        toolbar.setTitle("写心情");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nowMood = editWriteMood.getText().toString();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nowMood.equals(" ")){
                    //提交该段文字，并跳转至查看界面
                    startActivity(new Intent(WriteMoodActivity.this,MoodActivity.class));
                }else{
                    Toast.makeText(WriteMoodActivity.this,"你什么都还没有写哦",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }





}
