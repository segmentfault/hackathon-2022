package com.example.my_travel.activity.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.my_travel.R;

public class Home_List2Activity extends AppCompatActivity {
    private ImageView back_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__list2);
        back_img = findViewById(R.id.Home_list2_back);
    }

    @Override
    protected void onStart() {
        super.onStart();
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
