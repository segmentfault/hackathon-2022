package com.baidu.paddle.lite.demo.image_classification.entity;

import cn.bmob.v3.BmobObject;

public class order_buy extends BmobObject {
    private String name;
    private String phone;
    private String address;
    private String number;
    private String price;
    private User userid;
    private String prodtitle;

    public String getPrice() {
        return price;
    }

    public String getProdtitle() {
        return prodtitle;
    }

    public User getUserid() {
        return userid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProdtitle(String prodtitle) {
        this.prodtitle = prodtitle;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
