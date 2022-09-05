package com.baidu.paddle.lite.demo.image_classification.Acitivity.range;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.BaseActivity;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RangeActivity extends BaseActivity implements
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnCalendarRangeSelectListener,
        CalendarView.OnMonthChangeListener,
        View.OnClickListener {

    TextView mTextLeftDate;
    TextView mTextLeftWeek;
    TextView title;

    TextView mTextRightDate;
    TextView mTextRightWeek;

    CalendarView mCalendarView;

    ImageView imageViewleft,imageViewright;

    private int mCalendarHeight;


    public static void show(Context context) {
        context.startActivity(new Intent(context, RangeActivity.class));
    }


    @Override
    protected int getLayoutId() { return R.layout.activity_range;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextLeftDate = findViewById(R.id.tv_left_date);
        mTextLeftWeek = findViewById(R.id.tv_left_week);
        mTextRightDate = findViewById(R.id.tv_right_date);
        mTextRightWeek = findViewById(R.id.tv_right_week);
        title = findViewById(R.id.range_month);
        imageViewleft = findViewById(R.id.left_month);
        imageViewright = findViewById(R.id.right_month);

        mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setOnCalendarRangeSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        //设置日期拦截事件，当前有效
        mCalendarView.setOnCalendarInterceptListener(this);

//        findViewById(R.id.iv_clear).setOnClickListener(this);
//        findViewById(R.id.iv_reduce).setOnClickListener(this);
//        findViewById(R.id.iv_increase).setOnClickListener(this);
//        findViewById(R.id.tv_commit).setOnClickListener(this);
//        findViewById(R.id.tv_title).setOnClickListener(this);

        mCalendarHeight = dipToPx(this, 90);

        mCalendarView.setRange( mCalendarView.getCurYear(), mCalendarView.getCurMonth(), mCalendarView.getCurDay(),

                mCalendarView.getCurYear(), mCalendarView.getCurMonth()+2, mCalendarView.getCurDay()+14

                );
        mCalendarView.post(new Runnable() {
            @Override
            public void run() {
                mCalendarView.scrollToCurrent();
            }
        });

        imageViewleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToPre(false);
            }
        });

        imageViewright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToNext(false);
            }
        });

        mCalendarView.setOnCalendarRangeSelectListener(new CalendarView.OnCalendarRangeSelectListener() {
            @Override
            public void onCalendarSelectOutOfRange(Calendar calendar) {
            }

            @Override
            public void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange) {

            }

            @Override
            public void onCalendarRangeSelect(Calendar calendar, boolean isEnd) {
            }
        });
    }

    @Override
    protected void initData() {

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        int day  = mCalendarView.getCurDay();
        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, day, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, day, 0xFF40db25, "假"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                List<Calendar> calendars = mCalendarView.getSelectCalendarRange();
                if (calendars == null || calendars.size() == 0) {
                    return;
                }
                for (Calendar c : calendars) {
                    Log.e("SelectCalendarRange", c.toString()
                            + " -- " + c.getScheme()
                            + "  --  " + c.getLunar());
                }
                Toast.makeText(this, String.format("选择了%s个日期: %s —— %s", calendars.size(),
                        calendars.get(0).toString(), calendars.get(calendars.size() - 1).toString()),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 屏蔽某些不可点击的日期
     * @param calendar calendar
     * @return
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return false;
        //return calendar.getTimeInMillis()<getCurrentDayMill() ;
    }


    private long getCurrentDayMill(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR,0);
        calendar.set(java.util.Calendar.MINUTE,0);
        calendar.set(java.util.Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this,
                calendar.toString() + (isClick ? "拦截不可点击" : "拦截设定为无效日期"),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChange(int year, int month) {
        title.setText(month+"月");
    }


    @Override
    public void onCalendarSelectOutOfRange(Calendar calendar) {
        Toast.makeText(this,
                "aaaaaaa",
                Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange) {
        Toast.makeText(this,
                calendar.toString() + (isOutOfMinRange ? "小于最小选择范围" : "超过最大选择范围"),
                Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarRangeSelect(Calendar calendar, boolean isEnd) {
        if (!isEnd) {
            mTextLeftDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日" +" "+ WEEK[calendar.getWeek()]);
            mTextLeftWeek.setText("出发");
            mTextRightWeek.setText("结束日期");
            mTextRightDate.setText("");
        } else {
            mTextRightDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日" + " "+ WEEK[calendar.getWeek()]);
            mTextRightWeek.setText("返回");
        }
    }

    private static final String[] WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
}
