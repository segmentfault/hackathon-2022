package com.baidu.paddle.lite.demo.image_classification.Bean;

public class Video_Bean {
        private String title,label,name,url,price,count,imageview,time;

    public Video_Bean(String title, String label, String name, String url, String price, String count, String imageview, String time) {
        this.title = title;
        this.label = label;
        this.name = name;
        this.url = url;
        this.price = price;
        this.count = count;
        this.imageview = imageview;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getImageview() {
        return imageview;
    }

    public void setImageview(String imageview) {
        this.imageview = imageview;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
