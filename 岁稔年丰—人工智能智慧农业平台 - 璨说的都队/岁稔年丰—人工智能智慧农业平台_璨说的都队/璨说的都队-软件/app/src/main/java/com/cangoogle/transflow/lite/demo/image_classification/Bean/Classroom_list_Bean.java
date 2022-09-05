package com.baidu.paddle.lite.demo.image_classification.Bean;

public class Classroom_list_Bean {
        private String title,price,username,shopname,type,id_1;
        private int  id_2, id_3, id_4;
        private String _Content;

    public Classroom_list_Bean(String title, String price, String username, String shopname, String type, String id_1) {
        this.title = title;
        this.price = price;
        this.username = username;
        this.shopname = shopname;
        this.type = type;
        this.id_1 = id_1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String get_Content(){return this._Content;}
        public void set_Content(String x){this._Content=x;}

    public String getId_1() {
        return id_1;
    }

    public void setId_1(String id_1) {
        this.id_1 = id_1;
    }

    public int getId_2() {
        return id_2;
    }

    public void setId_2(int id_2) {
        this.id_2 = id_2;
    }

    public int getId_3() {
        return id_3;
    }

    public void setId_3(int id_3) {
        this.id_3 = id_3;
    }

    public int getId_4() {
        return id_4;
    }

    public void setId_4(int id_4) {
        this.id_4 = id_4;
    }



}
