package com.baidu.paddle.lite.demo.image_classification.Bean.productBean;

public class productBean1 {
        private String title,name,pri;
        private String id_1;
        private String _Content;

    public productBean1(String title, String pri, String id_1) {
        this.title = title;
        this.pri = pri;
        this.id_1 = id_1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public String getId_1() {
        return id_1;
    }

    public void setId_1(String id_1) {
        this.id_1 = id_1;
    }

    public String get_Content() {
        return _Content;
    }

    public void set_Content(String _Content) {
        this._Content = _Content;
    }
}
