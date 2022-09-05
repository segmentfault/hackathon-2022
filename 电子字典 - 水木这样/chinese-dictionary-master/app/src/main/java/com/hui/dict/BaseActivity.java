package com.hui.dict;
import androidx.appcompat.app.AppCompatActivity;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
/**
 * 网络请求页面的工具类
 * */
public class BaseActivity extends AppCompatActivity implements Callback.CommonCallback<String> {

//    封装加载网络数据的过程
    public void loadData(String path){
//        创建请求参数体
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }
    @Override
    public void onSuccess(String result) {
//        网络请求成功时，会调用的接口。result获取到的json数据
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
//        网络请求失败时，会调用的接口。
    }

    @Override
    public void onCancelled(CancelledException cex) {
//        网络请求取消时，会调用的接口。
    }

    @Override
    public void onFinished() {
//        网络请求完成时，会调用的接口。
    }
}
