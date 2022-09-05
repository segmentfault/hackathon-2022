package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.paddle.lite.demo.image_classification.R;

public class Pic_Word_Details_Activity extends AppCompatActivity {
    protected ImageView back;
    protected ImageView details;
    protected TextView label,title,content;
    protected ImageView url_user;
    protected TextView User_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_word_details_activity);

        initView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        details = (ImageView) this.findViewById(R.id.pic_word_details_down_image);
        back = (ImageView) this.findViewById(R.id.pic_word_back);
        label = (TextView) this.findViewById(R.id.pic_words_details_label);
        url_user = (ImageView) this.findViewById(R.id.pic_words_details_url);
        content = (TextView) this.findViewById(R.id.pic_words_details_content);
        User_name = (TextView) this.findViewById(R.id.pic_words_details_name);
    }
}
