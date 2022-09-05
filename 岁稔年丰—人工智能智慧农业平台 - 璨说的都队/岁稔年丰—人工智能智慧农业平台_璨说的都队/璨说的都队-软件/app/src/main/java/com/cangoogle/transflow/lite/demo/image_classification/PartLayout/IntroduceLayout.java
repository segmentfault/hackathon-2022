package com.baidu.paddle.lite.demo.image_classification.PartLayout;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.R;


public class IntroduceLayout extends CardView {
    private View view;
    private Context context;
    public IntroduceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private TextView textViewTitle,textViewProgress,textViewPeople;
    private TextView textViewAuthor,textViewContent,textViewReadMore;
    private Boolean isMore;
    private void loadView(){
        view= LayoutInflater.from(context)
                .inflate(R.layout.course_detail_video_introduce,this);
        textViewTitle=view.findViewById(R.id.textViewCourseDetailVideoIntroduceTitle);
        textViewProgress=view.findViewById(R.id.textViewCourseDetailVideoIntroduceProgress);
        textViewPeople=view.findViewById(R.id.textViewCourseDetailVideoIntroducePeople);
        textViewAuthor=view.findViewById(R.id.textViewCourseDetailVideoIntroduceAuthor);
        textViewContent=view.findViewById(R.id.textViewCourseDetailVideoIntroduceContent);
        textViewReadMore=view.findViewById(R.id.textViewCourseDetailVideoIntroduceReadMore);
        isMore=false;
        textViewReadMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMore){
                    textViewContent.setText("课程介绍: "+contentBri);
                    textViewReadMore.setText("展开更多");
                    isMore=false;
                }else{
                    textViewContent.setText("课程介绍: "+content);
                    textViewReadMore.setText("收起");
                    isMore=true;
                }
            }
        });
    }
    private String title,Author,content,contentBri;
    private int progress;
    Long people;

    /**
     *
     * @param title 课程的名字
     * @param author 课程的主讲人
     * @param content 课程的介绍
     * @param progress 课程的进度（int类型的整数）
     * @param people 参加该课程的人数（Long类型的整数）
     */
    public void setData(String title,String author,String content,int progress,Long people){
        this.title=title;
        this.Author=author;
        this.content=content;
        this.progress=progress;
        this.people=people;
        textViewTitle.setText(title);
        textViewProgress.setText(Integer.valueOf(progress).toString());
        textViewPeople.setText(people+"个人已加入学习");
        textViewAuthor.setText("主讲人: "+author);
        if(content.length()>=26){
            content=content.substring(0,25);
            content+="...";
        }
        this.contentBri=content;
        textViewContent.setText("课程介绍: "+contentBri);
    }
}
