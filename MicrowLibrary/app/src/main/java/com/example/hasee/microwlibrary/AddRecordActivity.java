package com.example.hasee.microwlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasee.microwlibrary.com.example.Tools;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


/**
 * Created by GeniusHe on 2017/3/15.
 */

public class AddRecordActivity extends AppCompatActivity {
    private Handler mHandler;
    private Toolbar toolbar;
    private TextView tvBookName;
    private TextView tvBookAuthor;
    private TextView tvBookState;
    private TextView tvScoreOrPlantime;
    private EditText editBookName;
    private EditText editBookAuthor;
    private Spinner spinBookState;
    private Spinner spinScoreOrPlantime;
    private Button btnAddBook;

    private ArrayAdapter<String> adapterScore;
    private ArrayAdapter<String> adapterPlanTime;
    private ArrayAdapter<String> adapterIsReadBook;
    private String bookName;
    private String bookAuthor;
    private int readState; // 读书状态
    private double bookScore = 0;//评分
    private String  planTime = "";//计划时间
    private Book book;
    private String userName = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent1=getIntent();
        userName = intent1.getStringExtra("userName");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvBookName = (TextView) findViewById(R.id.tv_book_name);
        tvBookAuthor = (TextView) findViewById(R.id.tv_book_author);
        tvBookState = (TextView) findViewById(R.id.tv_state);
        tvScoreOrPlantime = (TextView) findViewById(R.id.tv_score_plantime);
        editBookName = (EditText) findViewById(R.id.edit_book_name);
        editBookAuthor = (EditText) findViewById(R.id.edit_book_author);
        spinBookState = (Spinner) findViewById(R.id.spinner_reading_state);
        spinScoreOrPlantime = (Spinner) findViewById(R.id.spinner_score_plantime);
        btnAddBook = (Button) findViewById(R.id.btn_add_book);


        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


         // 建立数据源
        String[] ItemIsReadBook = getResources().getStringArray(R.array.ArrayIsReadBook);
        String[] ItemScore = getResources().getStringArray(R.array.ArrayScore);
        String[] ItemPlanTime = getResources().getStringArray(R.array.ArrayPlanTime);

        // 建立Adapter并且绑定数据源
        adapterIsReadBook=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, ItemIsReadBook);
        adapterScore=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, ItemScore);
        adapterPlanTime=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, ItemPlanTime);

        adapterIsReadBook.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterScore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPlanTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //绑定 Adapter到控件
        spinBookState .setAdapter(adapterIsReadBook);
        spinBookState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                switch (pos){
                    case 0://已读
                        readState = 1;
                        tvScoreOrPlantime.setText("评分");
                        spinScoreOrPlantime.setAdapter(adapterScore);

                        break;
                    case 1://未读
                        readState = 0;
                        tvScoreOrPlantime.setText("计划时间");
                        spinScoreOrPlantime.setAdapter(adapterPlanTime);

                        break;


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        spinScoreOrPlantime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (readState == 1) {//已读
                    bookScore = position + 1;
                    //Toast.makeText(AddRecordActivity.this, "您当前选择" + bookScore + "分", Toast.LENGTH_SHORT).show();

                }else if (readState == 0) {//未读
                    switch (position){
                        case 0:
                            planTime = "一周内";
                            break;
                        case 1:
                            planTime = "两周内";
                            break;
                        case 2:
                            planTime = "一个月内";
                            break;
                        case 3:
                            planTime = "两个月内";
                            break;
                    }

                    //Toast.makeText(AddRecordActivity.this,"您选择在"+planTime+"看完这本书",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        //Handler用来出现数据
        mHandler=new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        //获取传递的数据
                        Bundle data = msg.getData();
                        String text = data.getString("str");
                        //处理UI更新等操作
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


                }
            };
        };

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookName = editBookName.getText().toString();
                bookAuthor = editBookAuthor.getText().toString();
                if (readState == 1){//已读

                    book = new Book(bookName,bookAuthor,readState,bookScore);

                }else if (readState == 0){//未读
                    book = new Book(bookName,bookAuthor,readState,planTime);
                }
                //跳转到阅读记录页
                //将这本书book传到服务器存储
                Tools tools = new Tools();
                //调用tools类的方法，把book对象转换为json的string。
                Log.d("AddRecord","准备添加书籍");
                Log.d("AddRecord",userName);
                final String joString =  tools.getRecordString(book,readState,userName)+"\n";
                Log.d("AddRecord",joString);
                Log.d("AddRecord","转换json成功");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 用一个Handler来处理Toast
                         */
                        Message msg = new Message(); //创建一个Message
                        msg.what = 1;          //给这个Message一个标识
                        Bundle data = new Bundle();  //创建一个数据包
                        Log.d("add",joString);

                        Socket socket = null;
                        String result = null;
                        Log.d("add","开始建立通道");
                        try {
                            socket = new Socket(LoginActivity.host,LoginActivity.port);
                            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            Log.d("add","建立通道成功，准备传输数据");
                            OutputStream  os = socket.getOutputStream();
                            os.write(joString.getBytes());
                            Log.d("add","传输数据成功，等待接收数据");
                            os.flush();
                            String answer  = br.readLine();
                            Log.d("add",answer);
                            Log.d("add","接收数据成功，准备转换json");
                            JSONObject janswer = JSONObject.fromObject(answer);
                            result = janswer.getString("result");
                            Log.d("add","转换数据成功");
                            socket.shutdownInput();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("true")) {
                            //存入书籍成功就跳转。
                            Log.d("State", "成功");
                            data.putString("str", "存储成功");
                            msg.setData(data);
                            //使用Handler将Message发送出去
                            mHandler.sendMessage(msg);

                            Intent intent = new Intent(AddRecordActivity.this, ReadRecordActivity.class);
                            intent.putExtra("mainid",userName);
                            startActivity(intent);

                        }
                        if(result.equals("false")){
                            data.putString("str", "未找到书籍");
                            msg.setData(data);
                            //使用Handler将Message发送出去
                            mHandler.sendMessage(msg);
                            return;
                        }
                    }}).start();
            }
        });



    }


}
