package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Map.Utils;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.Util_view;
import com.baidu.paddle.lite.demo.image_classification.Utils.Util;


public class Local_Activity extends CheckPermissionsActivity  {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView mOneLocTV;
    private boolean isFirstLoc = true;
    private int mCount = 0;
    private LocationClient mLocClientOne = null;
    private LocationClient mLocClientContinuoue = null;
    private Marker mContinuoueLocMarker = null;
    private Marker mOneLocMarker = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity);
        mMapView = findViewById(R.id.map_views);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        startOneLocaton();
    }



    /**
     * 启动单次定位
     */
    private void startOneLocaton() {
        mLocClientOne = new LocationClient(this);
        mLocClientOne.registerLocationListener(oneLocationListener);
        LocationClientOption locationClientOption = new LocationClientOption();
        // 可选，设置定位模式，默认高精度 LocationMode.Hight_Accuracy：高精度；
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，设置返回经纬度坐标类型，默认GCJ02
        locationClientOption.setCoorType("bd09ll");
        // 如果设置为0，则代表单次定位，即仅定位一次，默认为0
        // 如果设置非0，需设置1000ms以上才有效
        locationClientOption.setScanSpan(0);
        // 设置是否进行单次定位，单次定位时调用start之后会默认返回一次定位结果
        locationClientOption.setOnceLocation(true);
        //可选，设置是否使用gps，默认false
        locationClientOption.setOpenGps(true);
        // 可选，是否需要地址信息，默认为不需要，即参数为false
        // 如果开发者需要获得当前点的地址信息，此处必须为true
        locationClientOption.setIsNeedAddress(true);
        // 设置定位参数
        mLocClientOne.setLocOption(locationClientOption);
        // 开启定位
        mLocClientOne.start();
    }

    /**
     * 停止单次定位
     */
    private void stopOneLocaton() {
        if (null != mLocClientOne) {
            mLocClientOne.stop();
        }
    }



    /**
     * 停止连续定位
     */
    private void stopContinuoueLocaton() {
        if (null != mLocClientContinuoue) {
            mLocClientContinuoue.stop();
            isFirstLoc = true;
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopContinuoueLocaton();
        stopOneLocaton();
        mBaiduMap.clear();
        mMapView.onDestroy();
    }

    /*****
     *
     * 单次定位回调监听
     *
     */
    private BDAbstractLocationListener oneLocationListener = new BDAbstractLocationListener() {

        /**
         * 定位请求回调函数
         * @param location 定位结果
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null == location || null == mBaiduMap) {
                return;
            }
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng);
            int padding = 0;
            int paddingBottom = 600;
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build(), padding,
                    padding, padding, paddingBottom);
            StringBuffer sb = new StringBuffer(256);
            // 更新地图状态
            mBaiduMap.animateMapStatus(mapStatusUpdate);
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("离线定位成功");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("服务端网络定位失败");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            String locationStr = Utils.getLocationStr(location, mLocClientOne);
            if (!TextUtils.isEmpty(locationStr)) {
                sb.append(locationStr);
            }
            if (null != mOneLocTV) {
                mOneLocTV.setText(sb.toString());
            }
        }
    };



}
