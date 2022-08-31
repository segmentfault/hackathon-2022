package com.cong.picturehub;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.cong.pojo.Picture;
import com.cong.util.SlideMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Slide extends AppCompatActivity {

    private ImageView cela;
    private SlideMenu slideMenu;
    private ImageView search;
    private Button bt;


    private static final int column = 2;//3列
    private static final int pageCount = 6; //每页加载个数
    private int currentPage = 0; //当前页
    private int columnWidth = 0;//列宽
    private LinearLayout mianContainer;//主容器
    private RequestQueue queue;
    private List<LinearLayout> columnLayouts = new ArrayList<LinearLayout>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        cela = findViewById(R.id.cela);
        slideMenu = findViewById(R.id.slideMenu);
        search = findViewById(R.id.search);


        mianContainer = (LinearLayout) findViewById(R.id.mianContainer);
        queue = Volley.newRequestQueue(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        columnWidth = (width - 4)/ 2 ;//4为中间2条空隙 /3
        ((MyScrollView)findViewById(R.id.scrollView)).setScrollCallBack(new Slide.MyScrollCallBack());
        addColumn();

        cela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideMenu.switchMenu();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Slide.this,Search.class));
            }
        });

        bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Slide.this,Image.class));
            }
        });
    }

    /**
     * 滚动回调
     */
    class MyScrollCallBack implements MyScrollView.ScrollCallBack {

        @Override
        public void onTop() {
        }

        @Override
        public void onBottom() {
            currentPage++;
            addImageView2Column();
        }

        @Override
        public void onScroll() {

        }

    }

    /**
     * 构造列
     */
    private void addColumn() {
        for(int i = 0;i < column;i++) {//构造列
            LinearLayout columnLayout = new LinearLayout(this);
            columnLayout.setLayoutParams(new ViewGroup.LayoutParams(columnWidth, ViewGroup.LayoutParams.MATCH_PARENT));
            columnLayout.setOrientation(LinearLayout.VERTICAL);
            columnLayouts.add(columnLayout);
            mianContainer.addView(columnLayout);
        }
        addImageView2Column();
    }

    /**
     * 构造完后开始加入imageView到列中
     */
    private void addImageView2Column() {


        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String[] urls= bundle.getStringArray("name");

        //System.out.println("获取到的name值为"+urls);

        int columnIndex = 0;
        int imageCount = urls.length;//ImageConst.
        for(int i = currentPage * pageCount;i< (currentPage +1)*pageCount && i < imageCount;i++) {
            columnIndex = columnIndex >= column ? columnIndex = 0 : columnIndex;
            ImageView itemImage = new ImageView(this);//////////////////////////////////////
            itemImage.setLayoutParams(new ViewGroup.LayoutParams(columnWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemImage.setPadding(2, 2, 2, 2);
            columnLayouts.get(columnIndex).addView(itemImage);
            downloadImage(itemImage,i);
            columnIndex++;
        }

    }


    /**
     * 下载图片，自带缓存
     * @param itemImage
     * @param index
     */
    private void downloadImage(final ImageView itemImage, int index) {


        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String[] urls= bundle.getStringArray("name");
        //System.out.println("获取到的name值为"+urls);
        //int c=urls.length;
        //columnWidth这个是设置下载图片的maxWidth,0代表不限定
        ImageRequest request = new ImageRequest(urls[index], new Response.Listener<Bitmap>() {
            @Override//ImageConst.
            public void onResponse(Bitmap response) {
                itemImage.setImageBitmap(response);
            }
        }, columnWidth, 0, Bitmap.Config.RGB_565, null);
        request.setShouldCache(true);//设置缓存 缓存路径看我以前的帖子
        queue.add(request);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


}