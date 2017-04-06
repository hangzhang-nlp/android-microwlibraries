package com.example.hasee.microwlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by HASEE on 2017/3/7.
 */

public class RegisterActivity extends Activity {
    private Socket socket;
    //private String host = "42.159.233.161";//同一个局域网内作为服务端的手机的IP，使用端口8155  
    private Button buttonRegister;
    private Button buttonCancel;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;
    private EditText editTextEmail;
    private String sex;
    private EditText editTextAge;
    RadioGroup groupSex ;//！！！！！！！！！男女信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        buttonRegister = (Button)findViewById(R.id.registconfirm);
        buttonCancel = (Button)findViewById(R.id.cancel);
        editTextUsername = (EditText)findViewById(R.id.usernameregist);
        editTextPassword = (EditText)findViewById(R.id.passwordregist);
        editTextPasswordConfirm = (EditText)findViewById(R.id.passwordconfirm);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        groupSex = (RadioGroup)findViewById(R.id.Sex);//！！！！！！！！！男女信息
        editTextAge = (EditText)findViewById(R.id.editTextAge);

        groupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)RegisterActivity.this.findViewById(radioButtonId);
                //更新文本内容，以符合选中项
                sex = rb.getText().toString();
            }
        });




        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Regi","1");
                if (editTextUsername.length() > 10 || editTextUsername.length() < 5) {
                    Toast.makeText(RegisterActivity.this, "用户名不合规范,最好设置为5-10位哦(#`O′)", Toast.LENGTH_SHORT).show();
                } else if(editTextPassword.length() < 5 || editTextPassword.length() > 10) {
                    Toast.makeText(RegisterActivity.this, "密码长度不合规范,应设置在5-10位(#`O′)", Toast.LENGTH_SHORT).show();
                }else if(editTextPassword.getText().toString().equals(editTextPasswordConfirm.getText().toString())==false){
                    Toast.makeText(RegisterActivity.this, "确认密码有误", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(editTextAge.getText().toString())>80||Integer.parseInt(editTextAge.getText().toString())<0){
                    Toast.makeText(RegisterActivity.this, "年龄有误", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("Regi","2");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jo = new JSONObject();
                            Log.d("Regi","3");
                            jo.put("request", "register");
                            jo.put("username",editTextUsername.getText().toString());
                            jo.put("password",editTextPassword.getText().toString());
                            jo.put("email",editTextEmail.getText().toString());
                            jo.put("sex",sex);
                            jo.put("age",editTextAge.getText().toString());
                            String request = jo.toString();
                            Log.d("Regi",request);
                            try {
                                socket = new Socket(LoginActivity.host,LoginActivity.port);
                                socket.setSoTimeout(1000);
                                OutputStream os = socket.getOutputStream();
                                os.write(request.getBytes());
                                os.flush();
                                socket.shutdownOutput();
                                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                //接收转换为字符串
                                String answer = br.readLine();
                                //json数据解析
                                JSONObject janswer = JSONObject.fromObject(answer);
                                String result = janswer.getString("result");
                                socket.shutdownInput();
                                if (result.equals("true")) {
                                    Log.d("RegisterState","成功");
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.putExtra("userName", editTextUsername.getText().toString());
                                    startActivity(intent);
                                } else if (result.equals("false")) {
                                    Log.d("RegisterState","失败");
                                } else if (result.equals("hadexist")) {
                                    //Toast.makeText(RegisterActivity.this, "用户名已被注册", Toast.LENGTH_SHORT).show();
                                    Log.d("RegisterState","用户名已被注册");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                }
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}

