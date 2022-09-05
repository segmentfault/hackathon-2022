package com.baidu.paddle.lite.demo.image_classification.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.LoginActivity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.OssActivity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.RegisterActivity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Setting_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.ShoppingCart_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.helpuse;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.refund_Activity;
import com.baidu.paddle.lite.demo.image_classification.R;

import cn.bmob.v3.BmobUser;

public class Fragment_fourth extends Fragment {
    private LinearLayout linearLayout;
    private LinearLayout login;
    private LinearLayout yinsizhengce,refund;
    private TextView is_login;
    private ImageView setting;
    private LinearLayout dongtai_up;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fourth,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linearLayout = (LinearLayout) getView().findViewById(R.id.shopping_frg4);
        login = (LinearLayout) getView().findViewById(R.id.login_in);
        yinsizhengce = (LinearLayout) getView().findViewById(R.id.yinsizhengce);
        is_login = (TextView) getView().findViewById(R.id.is_login);
        setting = getView().findViewById(R.id.setting);
        dongtai_up = getView().findViewById(R.id.dongtai_up);
        refund = getView().findViewById(R.id.refund);
        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), refund_Activity.class));
            }
        });
        dongtai_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OssActivity.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Setting_Activity.class));
            }
        });
        if(BmobUser.isLogin()){
            is_login.setText(BmobUser.getCurrentUser().getUsername());
        }else {
            is_login.setText("点击登录");
        }
        yinsizhengce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), helpuse.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BmobUser.isLogin()){
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ShoppingCart_Activity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
