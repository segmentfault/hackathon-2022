package com.baidu.paddle.lite.demo.image_classification.data;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class PhoneCodeLayout extends RelativeLayout {
    private Context context;
    private TextView tv_code1;
    private TextView tv_code2;
    private TextView tv_code3;
    private TextView tv_code4;
    private View v1;
    private View v2;
    private View v3;
    private View v4;
    private EditText et_code;
    private List<String> codes = new ArrayList<>();
    private InputMethodManager imm;


    public PhoneCodeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadView();
    }

    public PhoneCodeLayout(Context context) {
        super(context);
    }

    private void loadView(){
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.security_code_edit_text, this);
        initView(view);
        initLineColor();
        initEvent();
    }

    private void initView(View view){
        tv_code1 = (TextView) view.findViewById(R.id.tv_code1);
        tv_code2 = (TextView) view.findViewById(R.id.tv_code2);
        tv_code3 = (TextView) view.findViewById(R.id.tv_code3);
        tv_code4 = (TextView) view.findViewById(R.id.tv_code4);
        et_code = (EditText) view.findViewById(R.id.et_code);
        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);
        v3 = view.findViewById(R.id.v3);
        v4 = view.findViewById(R.id.v4);
    }

    private void initEvent(){
        //验证码输入
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null && editable.length()>0) {
                    et_code.setText("");
                    if(codes.size() < 4){
                        codes.add(editable.toString());
                        showCode();
                    }
                }
            }
        });
        // 监听验证码删除按键
        et_code.setOnKeyListener(new OnKeyListener() {


            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size()>0) {
                    codes.remove(codes.size()-1);
                    showCode();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 显示输入的验证码
     */
    private void showCode(){
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        if(codes.size()>=1){
            code1 = codes.get(0);
        }
        if(codes.size()>=2){
            code2 = codes.get(1);
        }
        if(codes.size()>=3){
            code3 = codes.get(2);
        }
        if(codes.size()>=4){
            code4 = codes.get(3);
        }
        tv_code1.setText(code1);
        tv_code2.setText(code2);
        tv_code3.setText(code3);
        tv_code4.setText(code4);

        setColor();//设置高亮颜色
        callBack();//回调
    }

    /**
     * 设置高亮颜色
     */
    private final String FOCUS="#101010";
    private final String DEFAULT="#888888";
    private void initLineColor(){
        int color_default = Color.parseColor(DEFAULT);
        int color_focus = Color.parseColor(FOCUS);
        v1.setBackgroundColor(color_focus);
        v2.setBackgroundColor(color_default);
        v3.setBackgroundColor(color_default);
        v4.setBackgroundColor(color_default);
    }
    private void setColor(){
        int color_default = Color.parseColor(DEFAULT);
        int color_focus = Color.parseColor(FOCUS);
        v1.setBackgroundColor(color_default);
        v2.setBackgroundColor(color_default);
        v3.setBackgroundColor(color_default);
        v4.setBackgroundColor(color_default);
        if(codes.size()==0){
            v1.setBackgroundColor(color_focus);
        }
        if(codes.size()==1){
            v2.setBackgroundColor(color_focus);
        }
        if(codes.size()==2){
            v3.setBackgroundColor(color_focus);
        }
        if(codes.size()>=3){
            v4.setBackgroundColor(color_focus);
        }
    }

    /**
     * 回调
     */
    private void callBack(){
        if(onInputListener==null){
            return;
        }
        if(codes.size()==4){
            onInputListener.onSuccess(getPhoneCode());
        }else{
            onInputListener.onInput();
        }
    }

    //定义回调
    public interface OnInputListener{
        void onSuccess(String code);
        void onInput();
    }
    private OnInputListener onInputListener;
    public void setOnInputListener(OnInputListener onInputListener){
        this.onInputListener = onInputListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftInput(){
        //显示软键盘
        if(imm!=null && et_code!=null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(et_code, 0);
                }
            },200);
        }
    }

    /**
     * 获得手机号验证码
     * @return 验证码
     */
    public String getPhoneCode(){
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }
}
