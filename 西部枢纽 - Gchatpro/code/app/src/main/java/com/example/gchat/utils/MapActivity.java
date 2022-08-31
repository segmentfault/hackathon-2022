package com.example.gchat.utils;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.gchat.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: xiahao
 * DateTime: 2022/4/15 16:52
 * Description:
 * 由于SDK并没有提供用于管理地图生命周期的Activity，因此需要用户继承Activity后管理地图的生命周期，防止内存泄露
 */

//监听定位和定位变化
public class MapActivity  extends AppCompatActivity implements LocationSource, AMapLocationListener  {

    //显示地图需要的变量
    AMap aMap= null;
    MapView mMapView = null;


    //定位需要的声明
    private AMapLocationClient mLocationClient = null;          //定位发起端
    private AMapLocationClientOption mLocationOption = null;    //定位参数
    private OnLocationChangedListener mListener = null;         //定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

        if(aMap==null){
            aMap = mMapView.getMap();//由MapView实例化AMap  //初始化地图变量
        }

        UiSettings settings = aMap.getUiSettings(); //设置显示定位按钮 并且可以点击
        aMap.setLocationSource(this);               //设置定位监听
        settings.setMyLocationButtonEnabled(true);  // 是否显示定位按钮
        aMap.setMyLocationEnabled(true);            // 是否可触发定位并显示定位层


        //定位的小图标 默认是蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);

        //开始定位
        try {
            initLoc();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //定位
    private void initLoc() throws Exception {

        mLocationClient = new AMapLocationClient(getApplicationContext()); //初始化定位
        mLocationClient.setLocationListener(this);                          //设置定位回调监听
        mLocationOption = new AMapLocationClientOption();                   //初始化定位参数
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(true);               //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setOnceLocation(false);             //设置是否只定位一次,默认为false
        mLocationOption.setWifiActiveScan(true);            //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setMockEnable(false);               //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setInterval(2000);                  //设置定位间隔,单位毫秒,默认为2000ms
        mLocationClient.setLocationOption(mLocationOption); //给定位客户端对象设置定位参数
        mLocationClient.startLocation();                    //启动定位
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if ( amapLocation.getErrorCode() == 0 ) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType(); //获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();     //获取纬度
                amapLocation.getLongitude();    //获取经度
                amapLocation.getAccuracy();     //获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);                //定位时间
                amapLocation.getAddress();      //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();      //国家信息
                amapLocation.getProvince();     //省信息
                amapLocation.getCity();         //城市信息
                amapLocation.getDistrict();     //城区信息
                amapLocation.getStreet();       //街道信息
                amapLocation.getStreetNum();    //街道门牌号信息
                amapLocation.getCityCode();     //城市编码
                amapLocation.getAdCode();       //地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if ( isFirstLoc ) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    //添加图钉
                    aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }




    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        MarkerOptions options = new MarkerOptions(); //设置图钉选项
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));//位置
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        options.title(buffer.toString());   //标题
        options.snippet("我在这里");        //子标题
        options.period(60);                //设置多少帧刷新一次图片资源
        return options;

    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
    }

}
