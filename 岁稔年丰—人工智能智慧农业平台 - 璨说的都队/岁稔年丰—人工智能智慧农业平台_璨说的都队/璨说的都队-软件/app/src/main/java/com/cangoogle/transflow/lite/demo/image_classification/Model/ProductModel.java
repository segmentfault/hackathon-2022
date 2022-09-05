package com.baidu.paddle.lite.demo.image_classification.Model;

import java.util.List;

public class ProductModel {
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
        public String ProdId;
        public String ProdTitle;
        public String ProdPrice;
        public String ProdUrl;
        public String ProdFrom;
        public String ProdDetails;
        public String ProdFee;
        public String ProdTypes;
        public String ProdDays;

        public String getProdFrom() {
            return ProdFrom;
        }

        public void setProdFrom(String prodFrom) {
            ProdFrom = prodFrom;
        }

        public String getProdDetails() {
            return ProdDetails;
        }

        public void setProdDetails(String prodDetails) {
            ProdDetails = prodDetails;
        }

        public String getProdFee() {
            return ProdFee;
        }

        public void setProdFee(String prodFee) {
            ProdFee = prodFee;
        }

        public String getProdTypes() {
            return ProdTypes;
        }

        public void setProdTypes(String prodTypes) {
            ProdTypes = prodTypes;
        }

        public String getProdDays() {
            return ProdDays;
        }

        public void setProdDays(String prodDays) {
            ProdDays = prodDays;
        }

        public String getProdId() {
            return ProdId;
        }

        public void setProdId(String prodId) {
            ProdId = prodId;
        }

        public String getProdTitle() {
            return ProdTitle;
        }

        public void setProdTitle(String prodTitle) {
            ProdTitle = prodTitle;
        }

        public String getProdPrice() {
            return ProdPrice;
        }

        public void setProdPrice(String prodPrice) {
            ProdPrice = prodPrice;
        }

        public String getProdUrl() {
            return ProdUrl;
        }

        public void setProdUrl(String prodUrl) {
            ProdUrl = prodUrl;
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
