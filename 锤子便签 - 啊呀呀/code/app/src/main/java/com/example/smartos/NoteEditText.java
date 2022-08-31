package com.example.smartos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.core.view.DragStartHelper;

public class NoteEditText extends androidx.appcompat.widget.AppCompatEditText {

    private Paint paint;
    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GRAY);
          // 开启抗锯齿 较耗内存
        paint.setAntiAlias(true);
    }

    @Override
     protected void onDraw(Canvas canvas) {
         super.onDraw(canvas);
         // 得到总行数
         float lineCount = getLineCount();
         // 得到每行的高度
         float lineHeight = getLineHeight();

         for (int i = 0; i < lineCount; i++) {
                 float lineY = (i + 1) * lineHeight;
                canvas.drawLine(0, lineY, this.getWidth(), lineY, paint);
             }
        }


}
// 根据行数循环画线
//        if(lineCount <=10){
//            for (int i = 0; i < 17; i++) {
//                float lineY = (i + 1) * lineHeight;
//                canvas.drawLine(0, lineY, this.getWidth(), lineY, paint);
//            }
//        }
//        if(lineCount >10)