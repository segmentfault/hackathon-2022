package com.baidu.paddle.lite.demo.image_classification.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class FirstFragmentGridView extends GridView {
    public FirstFragmentGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstFragmentGridView(Context context) {
        super(context);
    }

    public FirstFragmentGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
