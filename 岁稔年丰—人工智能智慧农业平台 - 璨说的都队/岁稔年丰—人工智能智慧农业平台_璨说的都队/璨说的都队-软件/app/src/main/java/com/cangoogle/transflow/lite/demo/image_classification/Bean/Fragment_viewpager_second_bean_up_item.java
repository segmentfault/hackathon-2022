package com.baidu.paddle.lite.demo.image_classification.Bean;

public class Fragment_viewpager_second_bean_up_item {
        private String title, number;
        private int pic;

    public Fragment_viewpager_second_bean_up_item(String title, String number, int pic) {
        this.title = title;
        this.number = number;
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
