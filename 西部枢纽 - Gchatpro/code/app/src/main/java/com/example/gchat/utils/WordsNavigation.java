package com.example.gchat.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * User: xiahao
 * DateTime: 2022/4/15 16:51
 * Description:实现手机联系人列表导航
 */
public class WordsNavigation extends View {

    /*绘制的列表导航字母*/
    private String words[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};


    private Paint wordsPaint;        //字母画笔
    private Paint bgPaint;           //字母背景画笔
    private int itemWidth;           //每一个字母的宽度
    private int itemHeight;         //每一个字母的高度
    private int touchIndex = 0;     //手指按下的字母索引
    private onWordsChangeListener listener;//自定义类  手指按下的字母改变接口


    /**
     * 初始化画笔
     */
    private void init(){
        //字母画笔
        wordsPaint=new  Paint();
        wordsPaint.setColor(Color.parseColor("#F7F7F7"));
        wordsPaint.setAntiAlias(true);
        wordsPaint.setTextSize(40);
        wordsPaint.setTypeface(Typeface.DEFAULT_BOLD);

        //字母背景画笔
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor("#1dcdef"));

    }

    /*---------------继承View类自动生成的类 begin------------------*/

    public WordsNavigation(Context context) {
        super(context);
        init();
    }

    public WordsNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WordsNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*-----------------继承View类自动生成  end----------------*/


    /*
        可以说重载onMeasure()，onLayout()，onDraw()三个函数构建了自定义View的外观形象。再加上onTouchEvent()等重载
        视图的行为，可以构建任何我们需要的可感知到的自定义View。
        1.View本身大小多少，这由onMeasure()决定；
        2.View在ViewGroup中的位置如何，这由onLayout()决定；
        3.绘制View，onDraw()定义了如何绘制这个View。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth=getMeasuredWidth();
        int height = getMeasuredHeight() - 10;  //使得边距好看一些
        itemHeight = height / 27;
    }


    @Override
    protected void onDraw(Canvas canvas) {//Canvas类相当于一个画布，你可以在里面画很多东西
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {

            if (touchIndex == i) {//判断是不是我们按下的当前字母
                //绘制文字圆形背景
                canvas.drawCircle(itemWidth / 2, itemHeight / 2 + i * itemHeight, 23, bgPaint);
                wordsPaint.setColor(Color.WHITE);//当前按下的是白色字体
            } else {
                wordsPaint.setColor(Color.GRAY); //否则为黑色字体
            }
            //获取文字的宽高 即字母所占的大小
            Rect rect = new Rect();
            wordsPaint.getTextBounds(words[i], 0, 1, rect);
            int wordWidth = rect.width();
            //绘制字母
            float wordX = itemWidth / 2 - wordWidth / 2;
            float wordY = itemWidth / 2 + i * itemHeight;
            canvas.drawText(words[i], wordX, wordY, wordsPaint);//第一个参数是我们需要绘制的文本，第四个参数是我们的画笔
        }
    }


    /**
     * 实现手指滑动或者点击字母列表的时候来改变当前选中的字母和在屏幕中央进行显示。
     * 当手指触摸按下的时候改变字母背景颜色
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //获得我们按下的是那个索引(字母)
                int index = (int) (y / itemHeight);
                if (index != touchIndex)
                    touchIndex = index;
                //防止数组越界
                if (listener != null && 0 <= touchIndex && touchIndex <= words.length - 1) {
                    //回调按下的字母
                    listener.wordsChange(words[touchIndex]);
                }
                invalidate();//请求重绘View树，即draw()过程，假如视图发生大小没有变化就不会调用layout()过程，并且只绘制那些“需要重绘的”
                            //视图，即谁(View的话，只绘制该View ；ViewGroup，则绘制整个ViewGroup)请求invalidate()方法，就绘制该视图。
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起,不做任何操作
                break;
        }
        return true;
    }

    /**
     * 设置当前按下的是那个字母
     * @param word
     */
    public void setTouchIndex(String word) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word)) {
                touchIndex = i;
                invalidate();
                return;
            }
        }
    }

    /**
     * 手指按下了哪个字母的回调接口
     */
    public interface onWordsChangeListener {
        void wordsChange(String words);
    }

    /*设置手指按下字母改变监听*/
    public void setOnWordsChangeListener(onWordsChangeListener listener) {
        this.listener = listener;
    }

}
