package com.example.hasee.microwlibrary;

/**
 * Created by HASEE on 2017/3/22.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import net.sf.json.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;

public class LoginActivity extends Activity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //
     //final static public String host = "192.168.43.252";//同一个局域网内作为服务端的手机的IP，使用端口8155 
    final static public int port = 6100;//  
    private Socket socket;
    final static public String host = "42.159.233.161";//同一个局域网内作为服务端的手机的IP，使用端口8155  
    private Button button1;
    private Button button2;
    private EditText editText1;
    private EditText editText2;

    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        button1 = (Button) findViewById(R.id.Login);
        button2 = (Button) findViewById(R.id.Inregist);
        editText1 = (EditText) findViewById(R.id.Username);
        editText2 = (EditText) findViewById(R.id.Password);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取控件里的信息
                final String userName = editText1.getText().toString();
                final String passWord = editText2.getText().toString();
                //用户输入规范检测
                if (userName.length() > 10 || userName.length() < 5) {
                    Toast.makeText(LoginActivity.this, "用户名不合规范,应在5-10位(#`O′)", Toast.LENGTH_SHORT).show();
                } else if (passWord.length() < 5 || passWord.length() > 10) {
                    Toast.makeText(LoginActivity.this, "密码长度不合规范,应设置在5-10位(#`O′)", Toast.LENGTH_SHORT).show();
                } else {
                    button1.setClickable(false);
                    button1.setText("正在登陆中");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jo = new JSONObject();
                            try {
                                jo.put("request", "login");
                                jo.put("username", userName);
                                jo.put("password",passWord);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String request = jo.toString();
                            //开启线程？传输数据
                            try {
                                //链接服务器，端口名和ip
                                Log.d("TAG3","3333333");
                                try {
                                    //Toast.makeText(LoginActivity.this, "1111111", Toast.LENGTH_SHORT).show();
                                    socket = new Socket(host, port);
                                    socket.setSoTimeout(100000);



                                } catch ( IOException e) {
                                    //   Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    Thread.interrupted();
                                    e.printStackTrace();
                                }
                                //发送信息
                                Log.d("TAG3","333");
                                OutputStream os = socket.getOutputStream();
                                Log.d("TAG3","334");
                                os.write(request.getBytes());
                                Log.d("TAG3","335");
                                os.flush();
                                socket.shutdownOutput();
                                //接收信息
                                Log.d("TAG3","336");
                                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                                Log.d("TAG3","337");
                                //接收转换为字符串
                                answer = br.readLine();
                                Log.d("TAG3","338");
                                //json数据解析
                                JSONObject js = JSONObject.fromObject(answer);
                                String result = js.getString("result");
                                Log.d("TAG3",result);
                           //     socket.shutdownInput();
                                if (result.equals("true")) {
                                    Log.d("TAG3","成功");
                                    //Toast.makeText(LoginActivity.this,"lalla",Toast.LENGTH_SHORT).show();
                                     Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Log.d("TAG3","跳转");
                                    intent.putExtra("userName", userName);
                                    startActivity(intent);
                                } else if (result.equals("false")) {
                                    //  Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
//                                    button1.setClickable(true);
//                                    button1.setText("登陆");
                                    Log.d("TAG3","失败" );
                                } else if (result.equals("notexist")) {
                                    //Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
//                                    button1.setClickable(true);
//                                    button1.setText("登陆");
                                }
                            } catch (IOException e) {
                                button1.setClickable(true);
                                Thread.interrupted();
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }

        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent1);
                } catch (Exception e2) {
                    System.out.println("Error:" + e2);
                }
            }

            ;
        });
    }


}
