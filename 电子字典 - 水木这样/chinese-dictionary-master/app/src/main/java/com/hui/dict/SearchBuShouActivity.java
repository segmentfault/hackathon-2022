package com.hui.dict;

import android.os.Bundle;
import android.util.Log;

import com.hui.dict.bean.PinBuWordBean;
import com.hui.dict.db.DBManager;
import com.hui.dict.utils.CommonUtils;
import com.hui.dict.utils.URLUtils;

import java.util.List;

public class SearchBuShouActivity extends BaseSearchActivity {

    String url;   //获取指定部首对应的网址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleTv.setText(R.string.main_tv_bushou);
        initData(CommonUtils.FILE_BUSHOU,CommonUtils.TYPE_BUSHOU);
        setExLvListener(CommonUtils.TYPE_BUSHOU);
        exLv.expandGroup(0);   //默认展开第一组
        word = "丨";     //默认进去时获取的是第一个 a
        url = URLUtils.getBushouurl(word,page,pagesize);
        // 加载网络数据
        loadData(url);
        setGVListener(CommonUtils.TYPE_BUSHOU);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        List<PinBuWordBean.ResultBean.ListBean> list = DBManager.queryBsWordFromPywordtb(word, page, pagesize);
        refreshDataByGV(list);
    }
}
