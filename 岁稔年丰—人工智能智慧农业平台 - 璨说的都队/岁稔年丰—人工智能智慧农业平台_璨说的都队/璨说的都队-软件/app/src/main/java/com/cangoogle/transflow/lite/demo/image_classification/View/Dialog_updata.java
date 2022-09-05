package com.baidu.paddle.lite.demo.image_classification.View;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.baidu.paddle.lite.demo.image_classification.R;


public class Dialog_updata extends AlertDialog {

    private OnClickListener listener;



    private TextView tvTitle;
    private TextView tvContent;

    private String title;
    private String message;
    private Typeface tf;
    private AssetManager mgr;

    public Dialog_updata(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_updata);

        setCanceledOnTouchOutside(true);

        //设置对话框的显示位置
        setDialogStartPositon();

        //绑定控件
        initView();

        //初始化个组件的内容
        initText();

        //设置点击事件触发
        initEvent();

    }

    private void setDialogStartPositon() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置起始位置
//        lp.x  = 100;
//        lp.y = 100;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

    }

    private void initEvent() {

//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null)
//                    listener.onCancelClick();
//
//                dismiss();
//            }
//        });
//
//        tvConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null)
//                    listener.onConfirmClick();
//                dismiss();
//            }
//        });
    }

    private void initText() {
        if (title != null) tvTitle.setText(title);
        if (message != null) tvContent.setText(message);
//        tvCancel.setText("取消");
//        tvConfirm.setText("确认");
    }

    public Dialog_updata setTitle(String title) {
        this.title = title;
        return this;
    }

    public Dialog_updata setMessage(String message) {
        this.message = message;
        return this;
    }

    private void initView() {

//        tvCancel = findViewById(R.id.tv_cancel);
//        tvConfirm = findViewById(R.id.tv_confirm);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvContent.setTypeface(tf);
    }


    //提供给外部使用的方法
    public Dialog_updata setOnClickListener(OnClickListener listener){
        this.listener = listener;
        return this;
    }

    public interface OnClickListener{
        void onCancelClick();
        void onConfirmClick();
    }
}

