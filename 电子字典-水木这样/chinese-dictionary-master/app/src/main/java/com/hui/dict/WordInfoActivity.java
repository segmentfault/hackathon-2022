package com.hui.dict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hui.dict.bean.WordBean;
import com.hui.dict.db.DBManager;
import com.hui.dict.utils.URLUtils;

import java.util.ArrayList;
import java.util.List;

/*
* 文字详情页面
* */
public class WordInfoActivity extends BaseActivity {
    TextView ziTv,pyTv,wubiTv,bihuaTv,bushouTv,jsTv,xxjsTv;
    ListView jsLv;
    ImageView collectIv;
     String zi;
    List<String> mDatas;   //数据源
    private ArrayAdapter<String> adapter;
    private List<String> jijie;
    private List<String> xiangjie;
//    设置标志位，表示汉字是否被收藏
    boolean isCollect = false;
    boolean isExist = false;    //判断这个汉字是否已经存在
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_info);
        //接受上一个页面传递过来的对象
        Intent intent = getIntent();
        zi = intent.getStringExtra("zi");
        String url = URLUtils.getWordurl(zi);  // 拼接网址
        initView();
//        初始化ListView显示的数据源
        mDatas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item_word_jslv, R.id.item_wordlv_tv, mDatas);
        jsLv.setAdapter(adapter);
//        加载网络数据
        loadData(url);
//        调用判断是否已经收藏了的方法
        isExist = DBManager.isExistZiInCollwordtb(zi);
        isCollect = isExist;   //记录初始状态
        setCollectIvStyle();
    }
    /* 根据收藏的状态，改变星星的颜色*/
    private void setCollectIvStyle() {
        if (isCollect) {
            collectIv.setImageResource(R.mipmap.ic_collection_fs);
        }else{
            collectIv.setImageResource(R.mipmap.ic_collection);
        }
    }

    /**
     * 表示获取数据成功时会调用的方法
     * */
    @Override
    public void onSuccess(String json) {
        WordBean wordBean = new Gson().fromJson(json, WordBean.class);
        WordBean.ResultBean resultBean = wordBean.getResult();
        // 插入数据库
        DBManager.insertWordToWordtb(resultBean);
        // 将数据显示在View控件上
        notifyView(resultBean);
    }
    /**
     * @des 更新控件信息
     * */
    private void notifyView(WordBean.ResultBean resultBean) {
        ziTv.setText(resultBean.getZi());
        pyTv.setText(resultBean.getPinyin());
        wubiTv.setText("五笔 : "+resultBean.getWubi());
        bihuaTv.setText("笔画 : "+resultBean.getBihua());
        bushouTv.setText("部首 : "+resultBean.getBushou());
        jijie = resultBean.getJijie();
        xiangjie = resultBean.getXiangjie();
        // 默认一进去，就显示基本解释
        mDatas.clear();
        mDatas.addAll(jijie);
        adapter.notifyDataSetChanged();
    }

    /* 获取数据失败时，会调用的方法*/
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
//            联网失败，获取数据库当中字的信息
        WordBean.ResultBean bean = DBManager.queryWordFromWordtb(zi);
        if (bean!=null) {
            notifyView(bean);
        }
    }

    private void initView() {
        ziTv = findViewById(R.id.word_tv_zi);
        wubiTv = findViewById(R.id.word_tv_wubi);
        pyTv = findViewById(R.id.word_tv_pinyin);
        bihuaTv = findViewById(R.id.word_tv_bihua);
        bushouTv = findViewById(R.id.word_tv_bushou);
        jsTv = findViewById(R.id.word_tv_js);
        xxjsTv = findViewById(R.id.word_tv_xxjs);
        jsLv = findViewById(R.id.word_lv_js);
        collectIv = findViewById(R.id.wordinfo_iv_collection);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wordinfo_iv_back:
                finish();
                break;
            case R.id.wordinfo_iv_collection:
                isCollect = !isCollect;   //将收藏状态取反
                setCollectIvStyle();
                break;
            case R.id.word_tv_js:
                jsTv.setTextColor(Color.RED);
                xxjsTv.setTextColor(Color.BLACK);
                //清空之前数据源
                mDatas.clear();
                mDatas.addAll(jijie);
                adapter.notifyDataSetChanged();
                break;
            case R.id.word_tv_xxjs:
                xxjsTv.setTextColor(Color.RED);
                jsTv.setTextColor(Color.BLACK);
                mDatas.clear();
                mDatas.addAll(xiangjie);
                adapter.notifyDataSetChanged();
                break;
        }
    }
    /*
    * 当activity被销毁时回调的方法
    * 将汉字进行插入或者删除
    * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isExist&&!isCollect) {
//            原来数据收藏，后来不想收藏了，需要删除
            DBManager.deleteZiToCollwordtb(zi);
        }
        if (!isExist&&isCollect) {
//            原来不存在，后来需要收藏，要插入数据
            DBManager.insertZiToCollwordtb(zi);
        }
    }
}
