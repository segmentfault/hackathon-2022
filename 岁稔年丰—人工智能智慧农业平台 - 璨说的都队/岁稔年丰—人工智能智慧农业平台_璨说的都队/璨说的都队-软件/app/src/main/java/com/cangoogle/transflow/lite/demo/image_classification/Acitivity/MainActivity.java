package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.paddle.lite.demo.image_classification.Fragment.Fragment_First;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Fragment_fourth;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Fragment_second;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Fragment_third;
import com.baidu.paddle.lite.demo.image_classification.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    protected Fragment_First fragment_first = new Fragment_First();
    protected Fragment_second fragment_second = new Fragment_second();
    protected Fragment_third fragment_third = new Fragment_third();
    protected Fragment_fourth fragment_fourth = new Fragment_fourth();
    protected ImageView imageView_first,imageView_second,imageView_third;
    protected CircleImageView imageView_fourth;
    protected LinearLayout linearLayout_first,linearLayout_second,linearLayout_third,linearLayout_fourth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fragment管理类
        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.container_content,fragment_first)
                .add(R.id.container_content,fragment_second)
                .hide(fragment_second)
                .add(R.id.container_content,fragment_third)
                .hide(fragment_third)
                .add(R.id.container_content,fragment_fourth)
                .hide(fragment_fourth)
                .commit();
        InitView();
    }

    private void InitView() {
        linearLayout_first = (LinearLayout)this.findViewById(R.id.main_first);
        linearLayout_second = (LinearLayout)this.findViewById(R.id.main_second);
        linearLayout_third = (LinearLayout)this.findViewById(R.id.main_third);
        linearLayout_fourth = (LinearLayout)this.findViewById(R.id.main_fourth);

        linearLayout_first.setOnClickListener(this);
        linearLayout_second.setOnClickListener(this);
        linearLayout_third.setOnClickListener(this);
        linearLayout_fourth.setOnClickListener(this);

        imageView_first = (ImageView)this.findViewById(R.id.main_first_view);
        imageView_second = (ImageView)this.findViewById(R.id.main_second_view);
        imageView_third = (ImageView)this.findViewById(R.id.main_third_view);
        imageView_fourth = (CircleImageView) this.findViewById(R.id.main_fourth_view);


    }


    /**
     * 更新底部按钮动画为缩放型动画
     * @return
     */
    private Animation shakeAnmation() {
        AnimationSet aset_3=new AnimationSet(true);
        ScaleAnimation aa_3=new ScaleAnimation(1, 0.85f, 1, 0.85f, Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
        aa_3.setDuration(189);
        aset_3.addAnimation(aa_3);
        return aset_3;
    }

    /**
     * 视图点击事件
     * @param v
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_first:
                imageView_first.startAnimation(shakeAnmation());

                this.getSupportFragmentManager().beginTransaction()
                        .show(fragment_first)
                        .hide(fragment_second)
                        .hide(fragment_third)
                        .hide(fragment_fourth)
                        .commit();

                imageView_first.setImageResource(R.drawable.first_down_1);
                imageView_second.setImageResource(R.drawable.second_down_2);
                imageView_third.setImageResource(R.drawable.third_down_2);
                imageView_fourth.setBorderColor(R.color.border_icon_fourth_two);
                break;
            case R.id.main_second:
                imageView_second.startAnimation(shakeAnmation());
                this.getSupportFragmentManager().beginTransaction()
                        .hide(fragment_first)
                        .show(fragment_second)
                        .hide(fragment_third)
                        .hide(fragment_fourth)
                        .commit();
                imageView_first.setImageResource(R.drawable.first_down_2);
                imageView_second.setImageResource(R.drawable.second_down_1);
                imageView_third.setImageResource(R.drawable.third_down_2);
                imageView_fourth.setBorderColor(R.color.border_icon_fourth_two);
                break;
            case R.id.main_third:
                imageView_third.startAnimation(shakeAnmation());

                this.getSupportFragmentManager().beginTransaction()
                        .hide(fragment_first)
                        .hide(fragment_second)
                        .show(fragment_third)
                        .hide(fragment_fourth)
                        .commit();
                imageView_first.setImageResource(R.drawable.first_down_2);
                imageView_second.setImageResource(R.drawable.second_down_2);
                imageView_third.setImageResource(R.drawable.third_down_1);
                imageView_fourth.setBorderColor(R.color.border_icon_fourth_two);
                break;
            case R.id.main_fourth:
                imageView_fourth.startAnimation(shakeAnmation());

                this.getSupportFragmentManager().beginTransaction()
                        .hide(fragment_first)
                        .hide(fragment_second)
                        .hide(fragment_third)
                        .show(fragment_fourth)
                        .commit();
                imageView_first.setImageResource(R.drawable.first_down_2);
                imageView_second.setImageResource(R.drawable.second_down_2);
                imageView_third.setImageResource(R.drawable.third_down_2);
                imageView_fourth.setBorderColor(0xFF2EE2C9);
                break;
        }
    }

}