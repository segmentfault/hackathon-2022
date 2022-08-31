package com.example.my_travel.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.my_travel.R;
import com.example.my_travel.sql.gp.GpHelp;
import com.example.my_travel.sql.gp.GpSqlite;

public class PurchaseActivity extends AppCompatActivity {
    private ImageView back_img;
    private EditText ed_dy;
    private Button bnt;
    private static GpSqlite gpSqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        gpSqlite = new GpSqlite(this,"Gp.db",null,2);
        initView();
    }

    private void initView() {
        back_img = findViewById(R.id.Home_ph_back);
        ed_dy = findViewById(R.id.gp_ed);
        bnt = findViewById(R.id.gp_bnt);
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    Toast.makeText(getBaseContext(),"购买成功！",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onStart() {
        super.onStart();
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String conten = ed_dy.getText().toString();
                int sum = Integer.parseInt(conten)*50;
                String strsum = String.valueOf(sum);
                GpSqlite sqlite = getSqlite();
                ContentValues values = new ContentValues();
                values.put("quantity",conten);
                values.put("money",strsum);
                GpHelp.insertGp(sqlite,values);
                handler.sendEmptyMessage(1);
            }
        });
    }
    public static GpSqlite getSqlite() {
        return gpSqlite;
    }
}
