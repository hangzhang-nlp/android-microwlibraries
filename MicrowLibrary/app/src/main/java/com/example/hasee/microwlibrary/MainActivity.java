package com.example.hasee.microwlibrary;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hasee.microwlibrary.CycleImage.CycleViewPager;
import com.example.hasee.microwlibrary.CycleImage.ViewFactory;
import com.example.hasee.microwlibrary.MoodNote.NoteActivity;
import com.example.hasee.microwlibrary.MoodNote.NoteEditActivity;
import com.example.hasee.microwlibrary.com.example.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private long mCurrentTime;
    private List<ImageView> views = new ArrayList<ImageView>();
    private CycleViewPager cycleViewPager;
    private UnScrollListView mListView;
    private ScrollView mScrollView;
    private  String getJson;
    private float mStartY = 0, mLastY = 0, mLastDeltaY;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private TextView headText;
    private String[] imageUrls = {
            "https://img3.doubanio.com/lpic/s4427770.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
            "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
    };
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        headText = (TextView)findViewById(R.id.tv_user_id);
        //给imageButton赋值
        imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);

        mListView= (UnScrollListView) findViewById(R.id.usedBooksList);
        //mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mListView.setFocusable(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Intent intent1=getIntent();
        userName=intent1.getStringExtra("userName");
      //  headText.setText(userName);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Tools tools = new Tools();
        getJson = tools.getHotUrlString("getHotBook");
        Log.d("Main","getJson成功");
        imageUrls = tools.getHotUrl(getJson);
        /*
        *图书列表
        */
        List<Book> dataArray=new ArrayList<Book>();
        getJson = tools.getBookRecords("getRecommend",userName);
        Log.d("Main","1111111111111111111");
        Log.d("Main","getJson成功");
        dataArray = tools.getList(getJson,"recommend");
        Log.d("Main","得到服务器输出,转换的dataArray成功");


        MapListImageAndTextListAdapter adapter=new MapListImageAndTextListAdapter(this, dataArray, mListView);
        Log.d("TAG","显示书籍成功");
        mListView.setAdapter(adapter);
        Log.d("TAG","4");
        //得到用户名

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float y = event.getY();
                float translationY = toolbar.getTranslationY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
						Log.v("TAG", "Down");
                        mStartY = y;
                        mLastY = mStartY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float mDeltaY = y - mLastY;

                        float newTansY = translationY + mDeltaY;
                        if (newTansY <= 0 && newTansY >= -toolbar.getHeight()) {
                            toolbar.setTranslationY(newTansY);
                        }
                        mLastY = y;
                        mLastDeltaY = mDeltaY;
//						Log.v(TAG, "Move");
                        break;
                    case MotionEvent.ACTION_UP:
                        ObjectAnimator animator = null;
                        Log.d("TAG", "mLastDeltaY=" + mLastDeltaY);
                        if (mLastDeltaY < 0 && mListView.getFirstVisiblePosition() > 1) {
                            Log.v("TAG", "listView.first=" + mListView.getFirstVisiblePosition());
                            animator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), -toolbar.getHeight());
                        } else {
                            animator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), 0);
                        }
                        animator.setDuration(100);
                        animator.start();
                        animator.setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.interpolator.linear));
//						Log.v(TAG, "Up");
                        break;
                }
                return false;
            }
        });

        configImageLoader();
        initialize();
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecommedActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }});
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadRecordActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }});
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }});

    }

    @SuppressLint("NewApi")
    private void initialize() {

        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);



        views.add(ViewFactory.getImageView(this, imageUrls[0]));
        for (int i = 0; i < 4; i++) {
            views.add(ViewFactory.getImageView(this, imageUrls[i]));
        }
        views.add(ViewFactory.getImageView(this, imageUrls[0]));
        //views.add(ViewFactory.getImageView(this, imageUrls[imageUrls.length-1]));


        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();
            }
        }



    };

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder(). showImageOnLoading(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 返回键监听，
     * 如果侧边栏打开，就关闭侧边栏
     * 如果侧边栏是关闭的，就调用原返回键方法
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return false;
            } else {
                if ((System.currentTimeMillis() - mCurrentTime) > 2000) {
                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mCurrentTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;//如果是后退键，则截获动作

            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_read_record) {
            //添加读书记录
            Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }else if(id == R.id.add_mood){
            //写笔记
            NoteEditActivity.ENTER_STATE = 0;
            Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("info", "");

            intent.putExtras(bundle);
            intent.putExtra("userName", userName);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.reading_record) {
            Intent intent = new Intent(MainActivity.this, ReadRecordActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        } else if (id == R.id.my_mood) {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        } else if (id == R.id.daily_check) {
            Intent intent = new Intent(MainActivity.this, SignActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        } else if (id == R.id.drawer_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        } else if (id == R.id.drawer_about){
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }else if (id == R.id.my_footprint){
            Intent intent = new Intent(MainActivity.this,RecommedActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
