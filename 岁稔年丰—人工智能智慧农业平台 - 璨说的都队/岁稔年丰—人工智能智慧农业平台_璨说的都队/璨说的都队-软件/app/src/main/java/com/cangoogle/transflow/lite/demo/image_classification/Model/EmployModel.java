package com.baidu.paddle.lite.demo.image_classification.Model;

import java.util.List;

public class EmployModel {
    public String message;;
    public Data data;
    public int code;


    public static class Data {

        private int total;
        private List<Result> list;

        public void setTotal(int total) {
            this.total = total;
        }
        public int getTotal() {
            return total;
        }

        public void setList(List<Result> list) {
            this.list = list;
        }
        public List<Result> getList() {
            return list;
        }

    }

    public static class Result {
        public String Id;
        public String Title;
        public String Price;
        public String UserName;
        public String Types;
        public String Tele;
        public String Descs;
        public String ShopName;
        public String Url;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getTypes() {
            return Types;
        }

        public void setTypes(String types) {
            Types = types;
        }

        public String getTele() {
            return Tele;
        }

        public void setTele(String tele) {
            Tele = tele;
        }

        public String getDescs() {
            return Descs;
        }

        public void setDescs(String descs) {
            Descs = descs;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }
    }


    @Override
    public String toString() {
        return "JsonBean{" +
                "message='" + message + '\'' +
                ",  data=" + data +'\'' +
                ", code="+code +
                '}';
    }
}
