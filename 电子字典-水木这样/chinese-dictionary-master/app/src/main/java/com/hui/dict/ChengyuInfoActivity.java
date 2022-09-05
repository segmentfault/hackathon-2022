package com.hui.dict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hui.dict.bean.ChengyuBean;
import com.hui.dict.db.DBManager;
import com.hui.dict.utils.MyGridView;
import com.hui.dict.utils.URLUtils;

import java.util.ArrayList;
import java.util.List;

public class ChengyuInfoActivity extends BaseActivity {
    TextView ziTv1,ziTv2,ziTv3,ziTv4,pyTv,jsTv,fromTv,exampleTv,yufaTv,yinzhengTv,yinghangTv;
    MyGridView tyGv,fyGv;
    ImageView collectIv;
    private String chengyu;
    List<String>tongyiList,fanyinList;    //GridView的数据源
    ArrayAdapter<String> tyAdapter,fyAdapter;
    //    设置标志位，表示汉字是否被收藏
    boolean isCollect = false;
    boolean isExist = false;    //判断这个汉字是否已经存在
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chengyu_info);
        initView();
        initAdapter();
//        获取上一个页面传递的数据
        Intent intent = getIntent();
        chengyu = intent.getStringExtra("chengyu");
        String url = URLUtils.getChengyuurl(chengyu);
        loadData(url);
        isExist = DBManager.isExistCyuInCollcyutb(chengyu);
        isCollect = isExist;

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
    /* 为GridView设置加载数据的适配器和数据源*/
    private void initAdapter() {
        tongyiList = new ArrayList<>();
        fanyinList = new ArrayList<>();
        tyAdapter = new ArrayAdapter<>(this, R.layout.item_word_jslv, R.id.item_wordlv_tv, tongyiList);
        fyAdapter = new ArrayAdapter<>(this, R.layout.item_word_jslv, R.id.item_wordlv_tv, fanyinList);
        tyGv.setAdapter(tyAdapter);
        fyGv.setAdapter(fyAdapter);
    }

    /* 网络数据加载成功时回调用的方法*/
    @Override
    public void onSuccess(String result) {

        ChengyuBean bean = new Gson().fromJson(result, ChengyuBean.class);
        ChengyuBean.ResultBean cyBean = bean.getResult();
        if (cyBean!=null) {
//        因数据源当中不包括成语本身，但是后期要插入数据库，所以需要保存这个成语
             cyBean.setChengyu(chengyu);
            // 插入到数据库当中
            DBManager.insertCyToCyutb(cyBean);
            // 显示数据
            showDataToView(cyBean);
        }else{
            Toast.makeText(this,"此成语无法查到，请重新查询！",Toast.LENGTH_SHORT).show();
        }
    }
    /* 网络加载数据失败时，会调用的方法*/
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
//        获取数据库当中缓存的数据
        ChengyuBean.ResultBean bean = DBManager.queryCyFromCyutb(chengyu);
        if (bean!=null) {
            showDataToView(bean);
        }
    }

    /**
     * 将获取到的数据显示在View上
     * */
    private void showDataToView(ChengyuBean.ResultBean cyBean) {
        String chengyu = cyBean.getChengyu();
        ziTv1.setText(String.valueOf(chengyu.charAt(0)));
        ziTv2.setText(String.valueOf(chengyu.charAt(1)));
        ziTv3.setText(String.valueOf(chengyu.charAt(2)));
        ziTv4.setText(String.valueOf(chengyu.charAt(3)));

        pyTv.setText("拼音 : "+cyBean.getPinyin());
        jsTv.setText(cyBean.getChengyujs());
        fromTv.setText(cyBean.getFrom_());
        exampleTv.setText(cyBean.getExample());
        yufaTv.setText(cyBean.getYufa());
        yinzhengTv.setText(cyBean.getYinzhengjs());

        String ciyujs = cyBean.getCiyujs();
        if (!TextUtils.isEmpty(ciyujs)) {
             ciyujs = ciyujs.replace("]", "\n").replace("[", "").replace(":", "");
            yinghangTv.setText(ciyujs);
        }
        List<String> tList = cyBean.getTongyi();
//        判断是否有同义词
        if (tList!=null&&tList.size()!=0) {
            tongyiList.addAll(tList);
            tyAdapter.notifyDataSetChanged();
        }
        List<String> fList = cyBean.getFanyi();
        if (fList!=null&&fList.size()!=0) {
            fanyinList.addAll(fList);
            fyAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 查找控件的方法
     * */
    private void initView() {
        ziTv1 = findViewById(R.id.cyinfo_tv_zi1);
        ziTv2 = findViewById(R.id.cyinfo_tv_zi2);
        ziTv3 = findViewById(R.id.cyinfo_tv_zi3);
        ziTv4 = findViewById(R.id.cyinfo_tv_zi4);
        pyTv = findViewById(R.id.cyinfo_tv_py);
        jsTv = findViewById(R.id.cyinfo_tv_js);
        fromTv = findViewById(R.id.cyinfo_tv_from);
        exampleTv = findViewById(R.id.cyinfo_tv_example);
        yufaTv = findViewById(R.id.cyinfo_tv_yufa);
        yinzhengTv = findViewById(R.id.cyinfo_tv_yinzheng);
        yinghangTv = findViewById(R.id.cyinfo_tv_yinghan);
        tyGv = findViewById(R.id.cyinfo_gv_tongyi);
        fyGv = findViewById(R.id.cyinfo_gv_fanyi);
        collectIv = findViewById(R.id.cyinfo_iv_collection);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cyinfo_iv_back:
                finish();
                break;
            case R.id.cyinfo_iv_collection:
                isCollect = !isCollect;
                setCollectIvStyle();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isExist&&!isCollect) {
            DBManager.deleteCyuToCollcyutb(chengyu);
        }
        if (!isExist&&isCollect) {
            DBManager.insertCyuToCollcyutb(chengyu);
        }
    }
}
