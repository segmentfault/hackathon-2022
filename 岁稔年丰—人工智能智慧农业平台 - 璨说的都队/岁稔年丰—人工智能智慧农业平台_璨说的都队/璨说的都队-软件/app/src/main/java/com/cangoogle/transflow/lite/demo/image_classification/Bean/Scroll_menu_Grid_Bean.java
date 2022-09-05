package com.baidu.paddle.lite.demo.image_classification.Bean;

import java.util.ArrayList;
import java.util.List;

public class Scroll_menu_Grid_Bean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

    public Scroll_menu_Grid_Bean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public Scroll_menu_Grid_Bean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }

    public static List<Scroll_menu_Grid_Bean> getTestData() {
        List<Scroll_menu_Grid_Bean> list = new ArrayList<>();
        list.add(new Scroll_menu_Grid_Bean("https://www.twoyl.cn/xczxpt/banner/xc_py.png", null, 1));
        list.add(new Scroll_menu_Grid_Bean("https://www.twoyl.cn/xczxpt/banner/1.jpg", null, 1));
        list.add(new Scroll_menu_Grid_Bean("https://www.twoyl.cn/xczxpt/banner/2.jpg", null, 1));
        list.add(new Scroll_menu_Grid_Bean("https://www.twoyl.cn/xczxpt/banner/3.jpg", null, 1));
        list.add(new Scroll_menu_Grid_Bean("https://www.twoyl.cn/xczxpt/banner/4.jpg", null, 1));
        return list;
    }

}

