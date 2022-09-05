package com.baidu.paddle.lite.demo.image_classification.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.youth.banner.indicator.BaseIndicator;

public class BannerIndicator extends BaseIndicator{
    private RectF rectF;
    private int mNormalRadius;
    private int mSelectedRadius;
    private int maxRadius;

        public BannerIndicator(Context context) {
            this(context, null);
        }

        public BannerIndicator(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public BannerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mNormalRadius = config.getNormalWidth() / 2;
            mSelectedRadius = config.getSelectedWidth() / 2;
            rectF = new RectF();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int count = config.getIndicatorSize();
            if (count <= 1) {
                return;
            }
            mNormalRadius = config.getNormalWidth() / 2;
            mSelectedRadius = config.getSelectedWidth() / 2;
            //考虑当 选中和默认 的大小不一样的情况
            maxRadius = Math.max(mSelectedRadius, mNormalRadius);
            //间距*（总数-1）+选中宽度+默认宽度*（总数-1）
            int width = (count - 1) * config.getIndicatorSpace() + config.getSelectedWidth() + config.getNormalWidth() * (count - 1);
            setMeasuredDimension(width, Math.max(config.getNormalWidth(), config.getSelectedWidth()));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int count = config.getIndicatorSize();
            int position = config.getCurrentPosition();
            if (count <= 1) {
                return;
            }
            float left = 0;
            for (int i = 0; i < count; i++) {
                if(i == position){
                    mPaint.setColor(config.getCurrentPosition() == i ? config.getSelectedColor() : config.getNormalColor());
                    int indicatorWidth = config.getCurrentPosition() == i ? config.getSelectedWidth()+1 : config.getNormalWidth();
                    rectF.set(left, 2, left + indicatorWidth, config.getHeight()*2);
                    left += indicatorWidth + config.getIndicatorSpace();
                    canvas.drawRoundRect(rectF, config.getRadius(), config.getRadius(), mPaint);
                }else{
                    mPaint.setColor(config.getCurrentPosition() == i ? config.getSelectedColor() : config.getNormalColor());
                    int indicatorWidths = config.getCurrentPosition() == i ? config.getSelectedWidth() : config.getNormalWidth();
                    int radius = config.getCurrentPosition() == i ? mSelectedRadius : mNormalRadius;
                    canvas.drawCircle(left + radius, maxRadius, radius, mPaint);
                    left += indicatorWidths + config.getIndicatorSpace();
                }
            }
        }
    }

