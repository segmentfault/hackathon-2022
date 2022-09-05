package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;


import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.View.Dialog_updata;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cn.bmob.v3.BmobUser;

public class Setting_Activity extends AppCompatActivity {
    private Button loginout;
    private Toolbar toolbar;
    private CardView cardView,qq_add,updata,share;
    private Typeface tf,tfs;
    private AssetManager mgrs;
    private Dialog_updata customDialog;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initViews(); //初始化控件
        back = findViewById(R.id.setting_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new Dialog_updata(Setting_Activity.this);
                customDialog.setMessage("当前暂无更新")//设置提示内容
                        .show();//显示对话框
            }
        });
        qq_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting_Activity.this,add_me.class));
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting_Activity.this, Creat_Person.class));
            }
        });

        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                Toast.makeText(Setting_Activity.this,"退出登陆成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Setting_Activity.this,MainActivity.class));
                finish();
            }
        });

    }

    private void initViews() {
        updata = (CardView)findViewById(R.id.gengxin);
        loginout = (Button)findViewById(R.id.login_out);
        cardView = (CardView)findViewById(R.id.creat_perspn_card);
        qq_add = (CardView)findViewById(R.id.QQ_add);
        share = (CardView)findViewById(R.id.share);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:

        }
        return true;
    }






    /**
     * 分享功能
     */

    private void share() {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        share_intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件:"+"基于乡村振兴的惠农APP" );
        share_intent = Intent.createChooser(share_intent, "分享");
        startActivity(share_intent);
    }

}
