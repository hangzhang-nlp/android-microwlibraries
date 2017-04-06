package com.example.hasee.microwlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hasee.microwlibrary.R;
import com.example.hasee.microwlibrary.com.example.Tools;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * Created by GeniusHe on 2017/3/15.
 */

public class SignActivity extends AppCompatActivity {
    public static boolean isAlreadySignUp = true;//可以签到
    private Toolbar toolbar;
    private MaterialCalendarView calendarView;
    private FloatingActionButton fab;
    private SimpleDateFormat sDateFormat;
    private String today;
    private Date date;
    private String userName;
    private Tools tools;
    private ArrayList<Date> dataArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        //继承来userName
        Intent intent1=getIntent();
        userName = intent1.getStringExtra("userName");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        fab = (FloatingActionButton) findViewById(R.id.fab_sign);
        sDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        today= sDateFormat.format(new Date());
        Log.d("SignActivity.class",today);
        date = ConvertStringToDate(today);
        dataArray = new ArrayList<Date>();

        /**
         * 询问服务器签到信息，将之前的签到信息显示在日历上
         * calendarView.setDateSelected(new Date(System.currentTimeMillis()),true);
         * 并将今天的签到信息发送给服务器SendSignMessage（String username, String nowDate）
         * 或询问今天是否签到
         */
        tools = new Tools();
        String signData = tools.getBookRecords("getSignData",userName);
        JSONObject jb = JSONObject.fromObject(signData);
        JSONArray ja = jb.getJSONArray("signInData");
        for (int i = 0; i < ja.size(); i++) {
            String data = ja.getJSONObject(i).getString("eachData");
            Date newDate = ConvertStringToDate(data);
            calendarView.setDateSelected(newDate,true);
        }



        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendarView.addDecorator(new CalendarViewDecorator());
        calendarView.setDateSelected(date,true);
        //calendarView.setDateSelected(new Date(System.currentTimeMillis()+24*60*60*1000),true);

        if(!isAlreadySignUp){
            fab.setImageResource(R.drawable.sign_success);
        }else{
            fab.setImageResource(R.drawable.sign_icon);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAlreadySignUp){
                    String state = tools.getBookRecords("signIn",userName);
                    JSONObject ja = JSONObject.fromObject(state);
                    Log.d("Sign",date.toString());
                    if(ja.getString("result").equals("true")) {
                        fab.setImageResource(R.drawable.sign_success);
                        Toast.makeText(SignActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        isAlreadySignUp = false;//不可以签到
                    }
                }else{
                    Toast.makeText(SignActivity.this,"你今天已经签过到了",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    /**
     * 将服务器发过来的日期格式yyyy_MM_dd，转化为Date实例
     * @param time
     * @return
     */
    private Date ConvertStringToDate(String time) {

        int year = Integer.parseInt(time.substring(0,4));
        int month = Integer.parseInt(time.substring(5,7));
        int dayOfMonth = Integer.parseInt(time.substring(8,10));
        Log.d("SignActivity.class",year+" "+month+" "+dayOfMonth);
        Calendar calendar = new GregorianCalendar(year,month-1,dayOfMonth);
        Date nowDate =calendar.getTime();
        Log.d("SignActivity.class",nowDate.toString());
        return nowDate;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAlreadySignUp = false;
    }
}

class CalendarViewDecorator implements DayViewDecorator {

    private Time t;
    private static boolean[] PRIME_TABLE = new boolean[34];

    private void setValueToArray(boolean[] table){

        for (int i = 0; i < table.length; ++i){
            table[i] = true;
        }
        t = new Time("GMT+8");
        t.setToNow();
        int date = t.monthDay;
        table[date] = false;
    }

    /**
     * 需要实现效果的天数返回true
     * @param day {@linkplain CalendarDay} to possibly decorate
     *
     * @return
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        setValueToArray(PRIME_TABLE);
        return PRIME_TABLE[day.getDay()];
    }

    /**
     * 上面方法返回true的天，会设置无法选择
     * @param view View to decorate
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
    }



}
