package com.example.hasee.microwlibrary.com.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;

import com.example.hasee.microwlibrary.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HASEE on 2017/3/23.
 */
//这个类收集了J
public class Tools {
    private BufferedReader br;
    private PrintWriter pr;
    private OutputStream os;
    private ArrayList<HashMap<String, Object>> list;
    private ArrayList<Book> books;
    HashMap<String, Object> map = null;

    //向服务器发送数据并却接收到服务器发来的数据
    public String getBookRecords(String requestkind, String userName) {
        final String requestKind = requestkind;
        final String userName1 = userName;
        String l1 = "";
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().
                    detectDiskWrites().detectNetwork().penaltyLog().build());
            Log.d("Tools", "线程开启成功");
            Socket socket = new Socket(LoginActivity.host, 6100);
            Log.d("Tools", "网络连接成功");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JSONObject jo = new JSONObject();
            jo.put("request", requestKind);
            jo.put("username", userName1);
            //显示阅读过的书籍
            String request = jo.toString();
            Log.d("Tools", request);

            PrintWriter os = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            Log.d("Tools", "创建输出通道成功");
            os.flush();
            os.write(request);
            Log.d("Tools", request);
            Log.d("Tools", "输出数据成功");
            os.flush();
            socket.shutdownOutput();
            //接收转换为字符串
            String answer = br.readLine();
            Log.d("Tools", answer);
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //输入：json的字符串,将json转换成books
    public ArrayList<Book> ParseJson(String jsonString) {
        JSONObject jb = JSONObject.fromObject(jsonString);
        JSONArray ja = jb.getJSONArray("Books");
        books = new ArrayList<Book>();

        for (int i = 0; i < ja.size(); i++) {
            Book book = new Book(
                    ja.getJSONObject(i).getString("bookName"),
                    ja.getJSONObject(i).getString("bookAuthor"),
                    1,
                    ja.getJSONObject(i).getDouble("bookScore"),
                    ja.getJSONObject(i).getString("bookImage")
            );
            book.setBookDescription(ja.getJSONObject(i).getString("bookDescription"));
            books.add(book);
        }

        return books;
    }

    //
    public ArrayList<HashMap<String, Object>> getList() {
        return list;
    }

    //添加阅读图书状态  输入：对象书     输出：发送成功或者失败
    public String getRecordString(Book book, int readState, String userName) {

        JSONObject jo = new JSONObject();
        jo.put("request", "recordBook");
        jo.put("userName", userName);
        jo.put("bookName", book.getBookName());
        jo.put("author", book.getBookAuthor());
        jo.put("status", book.getReadState());
        if (readState == 0) {
            jo.put("planTime", book.getPlanTime());
        } else if (readState == 1) {
            jo.put("bookScore", book.getBookScore());
        }
        String joString = jo.toString();
        return joString;
    }

    public List<Book> getList(String jsonString,String requestHead) {
        JSONObject jb = JSONObject.fromObject(jsonString);
        JSONArray ja = jb.getJSONArray(requestHead);
        List<Book> bookList = new ArrayList<Book>();
        for (int i = 0; i < ja.size(); i++) {
            Book book = new Book();

            book.setBookName(ja.getJSONObject(i).getString("bookname"));
            book.setBookAuthor("作者："+ja.getJSONObject(i).getString("author"));
            Log.d("Tools", "2222222222222222");
            if (ja.getJSONObject(i).getString("doubanrate") != null) {
                Log.d("Tools", "333333333333");
                Log.d("Tools", ja.getJSONObject(i).getString("doubanrate") + "333333333");
                book.setBookScore(Double.parseDouble(ja.getJSONObject(i).getString("doubanrate")));
                Log.d("Tools", "444444444");
            } else {
                book.setBookScore(0.0);
            }
            book.setBookimageUrl(ja.getJSONObject(i).getString("imgurl"));

            bookList.add(book);
        }
        return bookList;
    }

    //得到当前热点图片的uml
    public String getHotUrlString(String requestkind) {
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().
                    detectDiskWrites().detectNetwork().penaltyLog().build());
            Log.d("Tools", "线程开启成功");
            Socket socket = new Socket(LoginActivity.host, 6100);
            Log.d("Tools", "网络连接成功");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JSONObject jo = new JSONObject();
            jo.put("request", requestkind);
            //显示阅读过的书籍
            String request = jo.toString();
            Log.d("Tools", request);
            PrintWriter os = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            Log.d("Tools", "Hot创建输出通道成功");
            os.flush();
            os.write(request);
            Log.d("Tools", request);
            Log.d("Tools", "Hot request输出数据成功");
            os.flush();
            socket.shutdownOutput();
            //接收转换为字符串
            String answer = br.readLine();
            socket.shutdownInput();
            Log.d("Tools Hot request", answer);
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //将hot的json转换为string[]
    public String[] getHotUrl(String jsonString) {
        JSONObject jb = JSONObject.fromObject(jsonString);
        JSONArray ja = jb.getJSONArray("hotbook");
        String[] imageUrls = {
                "https://img3.doubanio.com/lpic/s4427770.jpg",
                "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
                "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
                "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
        };
        for (int i = 0; i < ja.size(); i++) {
            String url = ja.getJSONObject(i).getString("imgurl");
            imageUrls[i] = url;
        }
        return imageUrls;
    }


}