package com.example.hasee.microwlibrary.MoodNote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.hasee.microwlibrary.MainActivity;
import com.example.hasee.microwlibrary.R;
import com.example.hasee.microwlibrary.SignActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GeniusHe on 2017/3/15.
 */

public class NoteActivity extends AppCompatActivity implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private Toolbar toolbar;
    private Context mContext;
    private ListView listview;
    private SimpleAdapter simp_adapter;
    private List<Note> NoteList;
    private FloatingActionButton addNote;
    private OkNoteDB okNoteDB;
    private String userName;
    private List<Map<String, Object>> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listview = (ListView) findViewById(R.id.listview);
        NoteList = new ArrayList<>();
        dataList = new ArrayList<>();
        addNote = (FloatingActionButton) findViewById(R.id.fab_add_note);
        mContext = this;
        okNoteDB = OkNoteDB.getInstance(mContext);
        NoteList = okNoteDB.loadNote();

        Intent intent1=getIntent();
        userName=intent1.getStringExtra("userName");

        Log.d("TAG","Listsize: "+NoteList.size());

        addNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                NoteEditActivity.ENTER_STATE = 0;
                Intent intent = new Intent(mContext, NoteEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Log.d("NoteActivity",userName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
        RefreshListView();

        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_FLING:
                Log.i("main", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
            case SCROLL_STATE_IDLE:
                Log.i("main", "视图已经停止滑动");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("main", "手指没有离开屏幕，试图正在滑动");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String contentTemp = listview.getItemAtPosition(position) + "";
        Log.d("onClick",contentTemp);
        String content_id = contentTemp.substring(contentTemp.indexOf("id=") + 3,
                contentTemp.indexOf("}"));
        Log.d("onClick",content_id);
        String content = contentTemp.substring(contentTemp.indexOf("content=") + 8,
                contentTemp.indexOf(", n"));
        Log.d("onClick",content);

        Log.d("onClick",
                "contentTemp = "+ contentTemp +
                "\ncontent = "+ content +
                "\nid = "+ id);

        NoteEditActivity.id = Integer.parseInt(content_id);

        NoteEditActivity.ENTER_STATE = 1;//更改数据
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("info", content);
        intent.putExtras(bundle);
        intent.setClass(NoteActivity.this, NoteEditActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("position = ", position+"");
        final int POS = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ListInfo = listview.getItemAtPosition(POS) + "";
                String id = ListInfo.substring(ListInfo.indexOf("id=") + 3,
                        ListInfo.indexOf("}"));
                Log.d("onClick", "content = "+ListInfo+"\nid = "+ id);
                okNoteDB.deleteNote(Integer.parseInt(id));
                RefreshListView();

            }
        });



        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();

        return true;

    }

    @Override
    // 接受上一个页面返回的数据，并刷新页面
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            RefreshListView();

        }
    }


    /**
     * 刷新ListView
     */
    public void RefreshListView() {
        if ( dataList.size() > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            listview.setAdapter(simp_adapter);
        }
        simp_adapter = new SimpleAdapter(
                mContext, getData(), R.layout.note_list_item,
                new String[] { "tv_list_content", "tv_list_date" },
                new int[] {R.id.tv_list_content, R.id.tv_list_date }
        );

        listview.setAdapter(simp_adapter);
    }

    private List<Map<String, Object>> getData() {
        okNoteDB = OkNoteDB.getInstance(mContext);
        NoteList = okNoteDB.loadNote();
        Log.d("TAG","Listsize: "+NoteList.size());
        if(NoteList.size()>0){
            for(Note note: okNoteDB.loadNote()){
                Map<String, Object> map = new HashMap<>();
                map.put("note_id", note.getId());
                map.put("tv_list_date", note.getNoteDate());
                map.put("tv_list_content", note.getNoteContent());
                dataList.add(map);

				/*Log.d("MainActivity",
						"\n tv_content "+note.getNoteContent()+
						"\n tv_date "   +note.getNoteDate()
						);*/
            }
        }

        return dataList;

    }
}
