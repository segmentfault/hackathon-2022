package com.example.my_travel.bean.list;

import com.example.my_travel.R;

import java.util.ArrayList;
import java.util.List;

public class ListImg {
    private  int img;
    private  String content;

    public ListImg(int img, String content) {
        this.img = img;
        this.content = content;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public  static List<ListImg> getlist(){
        List<ListImg> meList=new ArrayList<>();
        //图片位置
        int[] img={R.drawable.sz1,R.drawable.sz2,R.drawable.sz3,R.drawable.sz4,R.drawable.sz5,R.drawable.sz6};
        //显示的项目
        String[]text={"苏州园林","拙政园","狮子林","沧浪亭","留园","怡园 "};
        ListImg[] meItems =new ListImg[img.length];
        for (int i=0;i<img.length;i++){
            meItems[i] =new ListImg(img[i],text[i]);
            meList.add(meItems[i]);
        }
        return meList;
    }
}
