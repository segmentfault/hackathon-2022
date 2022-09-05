package com.baidu.paddle.lite.demo.image_classification.PartLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.baidu.paddle.lite.demo.image_classification.Adapter.CourseItemAdapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.CourseItem;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class AllCourseLayout extends CardView {
    private Context context;
    private View view;
    public AllCourseLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private TextView textViewNum,textViewMore;
    private RecyclerView recyclerView;
    private CourseItemAdapter adapter;
    private GridLayoutManager manager;
    private void loadView(){
        view= LayoutInflater.from(context)
                .inflate(R.layout.course_detail_video_all_course,this);
        textViewNum=view.findViewById(R.id.textViewCourseDetailVideoAllCourseNum);
        textViewMore=view.findViewById(R.id.textViewCourseDetailVideoAllCourseReadMore);
        textViewMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 注意这里！！！
                 * 这是点击更多后的跳转
                 */
            }
        });
        recyclerView=view.findViewById(R.id.recyclerViewCourseDetailVideoAllCourse);

    }
    public void setData(int num,List<CourseItem> list){
        textViewNum.setText("(共"+num+"课)");
        adapter=new CourseItemAdapter(list);
        adapter.setListener(new CourseItemAdapter.Listener() {
            @Override
            public void onItemChange(String url) {
                listener.onItemChange(url);
            }
        });
        manager=new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener{
        public void onItemChange(String url);
    }
}
