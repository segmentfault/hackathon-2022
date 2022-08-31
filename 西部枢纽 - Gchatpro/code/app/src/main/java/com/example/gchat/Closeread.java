package com.example.gchat;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gchat.viewUI.QQmoodActivity;
import com.example.gchat.viewUI.RegistActivity;


public class Closeread extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closeread);

        Button closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Closeread.this, MainActivity.class);
                Closeread.this.startActivity(intent);
                Toast.makeText(Closeread.this, "开启成功", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
