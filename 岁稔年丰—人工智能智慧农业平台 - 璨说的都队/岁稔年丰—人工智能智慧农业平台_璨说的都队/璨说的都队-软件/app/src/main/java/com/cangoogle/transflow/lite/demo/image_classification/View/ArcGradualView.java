package com.baidu.paddle.lite.demo.image_classification.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.baidu.paddle.lite.demo.image_classification.R;


public class ArcGradualView extends View {
    private Paint mPaint;
    private PointF mStartPoint, mEndPoint, mControlPoint;
    private int mWidth;
    private int mHeight;
    private Path mPath = new Path();
    private float mArcHeight;
    private int mStartColor;
    private int mEndColor;
    private int mOthColor;
    private LinearGradient mLinearGradient;
    private int anInt;
    private Paint mBgPaint;

    public ArcGradualView(Context context) {
        super(context);
        init(context, null);
    }

    public ArcGradualView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ArcGradualView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArcGradualView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcGradualView);
            mArcHeight = a.getDimension(R.styleable.ArcGradualView_height_arc, 30);
            anInt = a.getInt(R.styleable.ArcGradualView_gradual_or, 0);
            mStartColor = a.getColor(R.styleable.ArcGradualView_color_gradual_start, Color.parseColor("#000000"));
            mEndColor = a.getColor(R.styleable.ArcGradualView_color_gradual_end, Color.parseColor("#FFFFFF"));
            mOthColor = a.getColor(R.styleable.ArcGradualView_color_other_arc, Color.parseColor("#CCCCCC"));
        } else {
            //低版本情况  这里写一些默认值
        }
        //渐变颜色画笔初始化--不需要设置颜色 用setShader去设置
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);

        //初始化其他背景的画笔
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStrokeWidth(10);
        mBgPaint.setStyle(Paint.Style.FILL);

        //初始化圆弧的控制点
        mStartPoint = new PointF(0, 0);
        mEndPoint = new PointF(0, 0);
        mControlPoint = new PointF(0, 0);
    }

    public void setGradient(int anInt) {
        switch (anInt) {
            case 0:
                mLinearGradient = new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight, mStartColor, mEndColor, Shader.TileMode.MIRROR);
                break;
            case 1:
                mLinearGradient = new LinearGradient(0, mHeight / 2, mWidth, mHeight / 2, mStartColor, mEndColor, Shader.TileMode.MIRROR);
                break;
            case 2:
                mLinearGradient = new LinearGradient(0, mHeight, mWidth, 0, mStartColor, mEndColor, Shader.TileMode.MIRROR);
                break;
            case 3:
                mLinearGradient = new LinearGradient(0, 0, mWidth, mHeight, mStartColor, mEndColor, Shader.TileMode.MIRROR);
                break;
        }
    }

    /**
     * 动态修改渐变色  暴露在外的
     * @param startColor
     * @param endColor
     * @param anType
     */
    public ArcGradualView setColor(@ColorInt int startColor, @ColorInt int endColor, @IntRange(from = 0, to = 3) int anType) {
        mStartColor = startColor;
        mEndColor = endColor;
        anInt = anType;
        setGradient(anType);
        invalidate();
        return this;
    }

    /**
     * 修改弧高
     * @param acrH
     * @return
     */
    public ArcGradualView changeArcHeight(int acrH){
        mArcHeight = acrH;
        invalidate();
        return this;
    }

    /**
     * 修改非圆弧背景
     * @param otherColor
     * @return
     */
    public ArcGradualView changOtherColor(@ColorInt int otherColor){
        mOthColor = otherColor;
        invalidate();
        return this;
    }

    /**
     * onSizeChanged才能拿到准确的w和h
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mPath.reset();
        //顺时针移动
        mPath.moveTo(0, 0);
        mPath.addRect(0, 0, mWidth, mHeight - mArcHeight, Path.Direction.CCW);
        //起始点= 0，布局总高-圆弧高度
        mStartPoint.x = 0;
        mStartPoint.y = mHeight - mArcHeight;
        //起始点= 总宽，布局总高-圆弧高度
        mEndPoint.x = mWidth;
        mEndPoint.y = mHeight - mArcHeight;
        //贝塞尔曲线控制点 = 中点，总高+控制点
        mControlPoint.x = mWidth / 2;
        mControlPoint.y = mHeight + mArcHeight;
        setGradient(anInt);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBgPaint.setColor(mOthColor);

        mPaint.setShader(mLinearGradient);
        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.quadTo(mControlPoint.x, mControlPoint.y, mEndPoint.x, mEndPoint.y);
        //先画背景
        canvas.drawRect(new Rect(0, 0, mWidth, mHeight), mBgPaint);
        //画大圆弧
        canvas.drawPath(mPath, mPaint);
    }
}
