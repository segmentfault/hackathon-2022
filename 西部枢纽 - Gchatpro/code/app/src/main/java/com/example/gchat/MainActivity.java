package com.example.gchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gchat.adapter.PersonAdapter;
import com.example.gchat.dao.PersonDAO;
import com.example.gchat.model.Person;
import com.example.gchat.utils.APPglobal;
import com.example.gchat.utils.CameraActivity;
import com.example.gchat.utils.MapActivity;
import com.example.gchat.utils.MessageActivity;
import com.example.gchat.utils.WordsNavigation;
import com.example.gchat.viewUI.QQmoodActivity;
import com.example.gchat.viewUI.QzoneActivity;
import com.example.gchat.viewUI.RegistActivity;
import com.example.gchat.viewUI.SettingsActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.exoplayer2.util.Log;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



/**
 * 功能：软件主界面类
 */
public class MainActivity extends AppCompatActivity
        implements  WordsNavigation.onWordsChangeListener,
        NavigationView.OnNavigationItemSelectedListener , AbsListView.OnScrollListener{
    //侧拉菜单滑出来的那一部分属于NavigationView

    private static boolean isExit=false;//判断是否直接退出程序
    //---联系人列表属性
    private Handler handler;        //Handler主要接受子线程发送的数据， 并用此数据配合主线程更新UI。
    private List<Person> list;      //联系人列表基本信息
    private TextView tv;            //绘制的字母
    private ListView listView;      //使用适配器的联系人列表
    private WordsNavigation word;   //右侧字母导航
    //---联系人列表属性



    @Override
    protected void onResume() {
        super.onResume();
        /*--------------联系人 begin--------------*/
        tv = (TextView) findViewById(R.id.tv);
        word = (WordsNavigation) findViewById(R.id.words);
        listView = (ListView) findViewById(R.id.list);

        initData();//初始化联系人数据
        initListView();//初始化联系人列表


        handler = new Handler();//设置列表点击滑动监听
        word.setOnWordsChangeListener(this);
        /*--------------联系人 end--------------*/

        //顶部工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Android侧滑菜单DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle监听Drawer拉出、隐藏
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //设置左侧菜单
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //阅后即焚
        FloatingActionButton button3= (FloatingActionButton) findViewById(R.id.button_close);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Closeread.class);
                MainActivity.this.startActivity(intent);
            }
        });
        //发表说说
        FloatingActionButton button= (FloatingActionButton) findViewById(R.id.button_mood);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, QQmoodActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        // 添加好友
        FloatingActionButton AddFriendActivityButton = (FloatingActionButton) findViewById(R.id.button_add);
        AddFriendActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,AddFriendActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        //浏览空间
        FloatingActionButton button2= (FloatingActionButton) findViewById(R.id.button_qzone);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, QzoneActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        //给左侧导航赋值当前登录用户名
        //TextView loginName= (TextView) findViewById(R.id.loginname);
        //loginName.setText(APPglobal.USERNAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 取Back键的按下事件
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if ( drawer.isDrawerOpen(GravityCompat.START) ) {
            drawer.closeDrawer(GravityCompat.START);  //如果左侧导航打开，单机Back键时关闭左侧导航
        } else {
            super.onBackPressed();//如果左侧导航关闭，，单机Back键时退出程序
        }
    }

    /**
     * 用于初始化菜单 (只会在第一次初始化菜单时调用）
     * @param menu 即将要显示的Menu实例
     * @return 返回true则显示该menu, false 则不显示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 菜单项被点击时调用，也就是菜单项的监听方法。
     * @param item 菜单项点击的子菜单
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings ) {   //安全退出

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
            return true;
        }
        if ( id == R.id.action_regist ) {    //立即注册
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, RegistActivity.class);
            MainActivity.this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 左侧导航被选择时触发的事件
     * @param item 左侧选中的菜单
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if ( id == R.id.nav_camera ) {//拍照
            // Handle the camera action
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,CameraActivity.class);
            MainActivity.this.startActivity(intent);

        } else if ( id == R.id.nav_gallery ) {  //相册 获取指定目录下的所有照片

            Intent intent = new Intent(Intent.ACTION_VIEW);
            String type = "image/*";
            Uri uri = Uri.parse("file:///sdcard/QQ_Test/");
            intent.setDataAndType(uri, type);
            startActivity(intent);

        } else if ( id == R.id.nav_slideshow ) {  //视频 调用视频播放器播放指定的视频
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String type = "video/mp4";
            Uri uri = Uri.parse("file:///sdcard/QQ_TEST_Video/03简单句的补充.mp4");
            intent.setDataAndType(uri, type);
            startActivity(intent);

        } else if ( id == R.id.nav_manage ) {  //系统设置
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(intent);
        } else if ( id == R.id.nav_share ) {  //分享软件
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MessageActivity.class);
            MainActivity.this.startActivity(intent);

        } else if ( id == R.id.nav_send ) {  //地理位置
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MapActivity.class);
            MainActivity.this.startActivity(intent);
        }

        //选择之后关闭左侧导航
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 继承 WordsNavigation.onWordsChangeListener 手指按下字母改变监听回调
     * @param words 按下的字母
     */
    @Override
    public void wordsChange(String words) {
        updateWord(words);      //更新字母
        updateListView(words);  //更新列表
    }

    /**
     * 初始化好友列表
     */
    private void initListView() {
        if(list.size()==0)return ;// 无好友列表直接返回
        PersonAdapter adapter = new PersonAdapter(this, list);
            listView.setAdapter(adapter);
            listView.setOnScrollListener(this);
    }

    /**
     * 初始化联系人列表信息
     */
    private void initData() {
        list = new ArrayList<>();

        list= PersonDAO.getPersonList();//数据库中的联系人列表

        //对集合排序：按字母顺序排序
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                //根据拼音进行排序
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //当滑动列表的时候，更新右侧字母列表的选中状态
        word.setTouchIndex(list.get(firstVisibleItem).getHeaderWord());
    }

    /**
     * 根据右侧的选中字母重新排序中间联系人的选中
     * 把那个字母的联系人的第一个找到就可以了 设置那个时候的选中状态为那个位置
     * @param words 首字母
     */
    private void updateListView(String words) {
        for (int i = 0; i < list.size(); i++) {
            String headerWord = list.get(i).getHeaderWord();
            //将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(headerWord)) {
                //将列表选中哪一个
                listView.setSelection(i);
                //找到开头的一个即可
                return;
            }
        }
    }

    /**
     * 更新中央的字母提示
     * @param words 首字母
     */
    private void updateWord(String words) {
        tv.setText(words);
        tv.setVisibility(View.VISIBLE);
        //清空之前的所有消息
        handler.removeCallbacksAndMessages(null);
        //1s后让tv隐藏
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setVisibility(View.GONE);
            }
        }, 500);//绘制的画面停留0.5s
    }


    Handler myhandler=new Handler(){
        // 这个handler 处理退出界面点击
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit=false;
        }
    };

    //退出确认
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(!isExit){
                isExit=true;
                Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
                myhandler.sendEmptyMessageDelayed(0,2000);
            }
        }else {
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

}