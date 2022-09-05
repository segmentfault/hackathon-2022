package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.paddle.lite.demo.image_classification.R;

public class Activity_AI extends AppCompatActivity {
    protected ImageView detection_ai;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane);
        initview();
    }

    private void initview() {

    }
}
