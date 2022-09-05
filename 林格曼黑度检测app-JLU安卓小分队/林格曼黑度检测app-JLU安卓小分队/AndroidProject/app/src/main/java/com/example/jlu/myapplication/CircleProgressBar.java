package com.example.smokedetector;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.jlu.myapplication.R;

public class CircleProgressBar extends View {

    private Paint circlePaint;
    private Paint textPaint;
    private int circleColor;//圆弧颜色
    private int circleBgColor;//圆弧背景颜色
    private float circleWidth;//圆弧宽度
    private float circleBgWidth;//圆弧背景宽度
    private int textColor;//字体颜色
    private float textSize;//字体大小
    private int totalAngle;//总角度
    private int startAngle;//开始角度
    private float currentProgress;//当前进度
    private float maxProgress;//最大进度
    private float section;//分段

    private float currentAngle;//当前角度
    private float lastAngle;
    private ValueAnimator progressAnimator;//圆弧动画
    private int duration = 1000;//动画时长
    private  boolean isDefaultText;//是否设置文字显示的值
    private  String mTextValue;//字体显示的值

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circlePaint = new Paint();
        textPaint = new Paint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        circleColor = typedArray.getColor(R.styleable.CircleProgressBar_circle_color, Color.RED);
        circleBgColor = typedArray.getColor(R.styleable.CircleProgressBar_circle_bg_color, Color.YELLOW);
        circleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_circle_width, 2);
        circleBgWidth = typedArray.getDimension(R.styleable.CircleProgressBar_circle_bg_width, 2);
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_text_color, Color.BLUE);
        textSize = typedArray.getDimension(R.styleable.CircleProgressBar_text_size, 10);
        totalAngle = typedArray.getInteger(R.styleable.CircleProgressBar_total_angle, 360);
        startAngle = typedArray.getInteger(R.styleable.CircleProgressBar_start_angle, 0);
        currentProgress = typedArray.getFloat(R.styleable.CircleProgressBar_current_progress, 0);
        maxProgress = typedArray.getFloat(R.styleable.CircleProgressBar_max_progress, 100);
        setCurrentProgress(currentProgress);
        setMaxProgress(maxProgress);
        typedArray.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = (int) (centre - circleWidth / 2) - 2; // 圆环的半径
        circlePaint.setColor(circleBgColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);// 圆头
        circlePaint.setStrokeWidth(circleBgWidth);
        RectF oval = new RectF(centre - radius - 1, centre - radius - 1, centre + radius + 1, centre + radius + 1); // 用于定义的圆弧的形状和大小的界限
        //背景圆
        canvas.drawArc(oval, startAngle, totalAngle, false, circlePaint);
        //数据圆
        circlePaint.setStrokeWidth(circleWidth);
        circlePaint.setColor(circleColor);
        canvas.drawArc(oval, startAngle, currentAngle, false, circlePaint);
        //
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        float textWidth = textPaint.measureText((int) currentProgress + "");
        canvas.drawText(mTextValue, centre - textWidth / 2, centre + textSize / 2, textPaint);
        //
        invalidate();
    }

    public float getMaxProgress(){
        return maxProgress;
    }

    public void setMaxProgress(float maxProgress){
        if(maxProgress < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.maxProgress = maxProgress;
        section = totalAngle / maxProgress;
    }

    public void setAnimationDuration(int duration){
        this.duration = duration;
    }

    public void setCurrentProgress(float progress){
        if(progress >= 0){
            this.currentProgress = progress;
            if(progress > maxProgress){
                progress = maxProgress;
            }
            lastAngle = currentAngle;
            setAnimation(lastAngle, progress * section, duration);
        }

    }

    private void setAnimation(float last, float current, int duration){
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(duration);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentAngle = (float) valueAnimator.getAnimatedValue();
                currentProgress = currentAngle / section;
            }
        });
        progressAnimator.start();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getCircleBgColor() {
        return circleBgColor;
    }

    public void setCircleBgColor(int circleBgColor) {
        this.circleBgColor = circleBgColor;
    }

    public float getCircleWidth() {
        return circleWidth;
    }

    public void setCircleWidth(float circleWidth) {
        this.circleWidth = circleWidth;
    }

    public float getCircleBgWidth() {
        return circleBgWidth;
    }

    public void setCircleBgWidth(float circleBgWidth) {
        this.circleBgWidth = circleBgWidth;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    /**
     * @param isText 为true,自定义设置字体显示
     * @param text
     */
    public  void setText(boolean isText,String text){
        isDefaultText = isText;
        mTextValue = text;
    }
}

