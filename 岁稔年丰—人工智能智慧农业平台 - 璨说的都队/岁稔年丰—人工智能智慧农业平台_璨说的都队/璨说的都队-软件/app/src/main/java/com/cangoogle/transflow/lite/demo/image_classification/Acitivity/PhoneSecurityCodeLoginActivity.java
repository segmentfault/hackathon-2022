package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.data.UserEntity;


public class PhoneSecurityCodeLoginActivity extends AppCompatActivity {

    private CheckBox checkBoxAgree;
    private EditText editTextUserPhone;
    private Button buttonLogin;
    private UserEntity userInfo=new UserEntity();
    private TextView textViewCode,textViewQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_security_code_login);
        setButtonLogin();
        setEditText();
        SetCheckBoxAgree();
        setCodeAndQuestion();
    }
    private final String LOW_GREEN="#8DF2D3",HIGH_GREEN="#14EBB6";
    private final String LOW_GRY="#888888",HIGH_GRY="#8C8C8C";
    private void setButtonLogin(){
        buttonLogin=(Button)findViewById(R.id.buttonLogin);
        buttonLogin.setClickable(false);
        buttonLogin.setBackgroundColor(Color.parseColor(LOW_GREEN));
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextUserPhone.getText().toString().length()!=11){
                    Toast.makeText(PhoneSecurityCodeLoginActivity.this,
                            "请输入正确的手机号", Toast.LENGTH_SHORT);
                }else{
                    Intent intent=new Intent(PhoneSecurityCodeLoginActivity.this
                            ,CheckSecurityCodeActivity.class);
                    intent.putExtra("PhoneNumber"
                            ,editTextUserPhone.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void setEditText(){
        editTextUserPhone=(EditText)findViewById(R.id.editTextTextPhoneNumber);
    }

    private void changeClickable(){
        if(buttonLogin.isClickable()&&!checkBoxAgree.isChecked()){
            buttonLogin.setClickable(false);
            buttonLogin.setBackgroundColor(Color.parseColor(LOW_GREEN));
        }
        else if(checkBoxAgree.isChecked()){
            buttonLogin.setClickable(true);
            buttonLogin.setBackgroundColor(Color.parseColor(HIGH_GREEN));
        }
    }

    private void SetCheckBoxAgree(){
        checkBoxAgree=(CheckBox)findViewById(R.id.checkBoxLogin);
        String agreeContent=getResources().getString(R.string.checkboxLogin_text);
        checkBoxAgree.setText(Html.fromHtml(agreeContent));
        checkBoxAgree.setChecked(false);
        checkBoxAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeClickable();
            }
        });
    }

    private boolean GetCheckLoginInfo(UserEntity userInfo){
        return true;
    }

    private void setCodeAndQuestion(){
        textViewCode=(TextView)findViewById(R.id.textViewGoSecurityCode);
        textViewQuestion=(TextView)findViewById(R.id.textViewGoToUQuestion);

        textViewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(PhoneSecurityCodeLoginActivity.this
//                        ,QuestionActivity.class);
//                startActivity(intent);
            }
        });
        textViewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PhoneSecurityCodeLoginActivity.this
                        ,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}