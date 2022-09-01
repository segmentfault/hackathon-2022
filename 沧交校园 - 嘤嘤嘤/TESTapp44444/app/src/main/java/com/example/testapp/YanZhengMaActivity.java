package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import ShuJuKu.QuanJU;

import static android.widget.Toast.LENGTH_SHORT;

public class YanZhengMaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText yanzhengma;
    private Button   queding;

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yanzhengma);

        yanzhengma=findViewById(R.id.editText);
        queding=findViewById(R.id.button3);
        queding.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String yanzhengma1=yanzhengma.getText().toString();
        if(yanzhengma1.equals(QuanJU.getInstance().YanZhengMa)){
            QuanJU.getInstance().isYanzheng=1;
            Toast.makeText(this, "验证码正确", LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "验证码输入错误", LENGTH_SHORT).show();
            finish();
        }
    }
}
