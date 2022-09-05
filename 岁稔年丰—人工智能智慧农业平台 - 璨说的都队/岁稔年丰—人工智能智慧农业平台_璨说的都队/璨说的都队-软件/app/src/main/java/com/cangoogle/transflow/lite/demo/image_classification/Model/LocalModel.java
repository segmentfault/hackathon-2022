package com.baidu.paddle.lite.demo.image_classification.Model;

import java.util.List;

public class LocalModel {
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
        public String Descs;
        public String Address;
        public String Type1;
        public String Prize;
        public String Timea;
        public String Timeb;
        public String Type1Timea;
        public String Type1Timeb;
        public String Type1Prize;
        public String Type2;
        public String Type2Timea;
        public String Type2Timeb;
        public String Type2Prize;
        public String Type3;
        public String Type3Timea;
        public String Type3Timeb;
        public String Type3Prize;
        public String Maintype;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getDescs() {
            return Descs;
        }

        public void setDescs(String descs) {
            Descs = descs;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getType1() {
            return Type1;
        }

        public void setType1(String type1) {
            Type1 = type1;
        }

        public String getPrize() {
            return Prize;
        }

        public void setPrize(String prize) {
            Prize = prize;
        }

        public String getTimea() {
            return Timea;
        }

        public void setTimea(String timea) {
            Timea = timea;
        }

        public String getTimeb() {
            return Timeb;
        }

        public void setTimeb(String timeb) {
            Timeb = timeb;
        }

        public String getType1Timea() {
            return Type1Timea;
        }

        public void setType1Timea(String type1Timea) {
            Type1Timea = type1Timea;
        }

        public String getType1Timeb() {
            return Type1Timeb;
        }

        public void setType1Timeb(String type1Timeb) {
            Type1Timeb = type1Timeb;
        }

        public String getType1Prize() {
            return Type1Prize;
        }

        public void setType1Prize(String type1Prize) {
            Type1Prize = type1Prize;
        }

        public String getType2() {
            return Type2;
        }

        public void setType2(String type2) {
            Type2 = type2;
        }

        public String getType2Timea() {
            return Type2Timea;
        }

        public void setType2Timea(String type2Timea) {
            Type2Timea = type2Timea;
        }

        public String getType2Timeb() {
            return Type2Timeb;
        }

        public void setType2Timeb(String type2Timeb) {
            Type2Timeb = type2Timeb;
        }

        public String getType2Prize() {
            return Type2Prize;
        }

        public void setType2Prize(String type2Prize) {
            Type2Prize = type2Prize;
        }

        public String getType3() {
            return Type3;
        }

        public void setType3(String type3) {
            Type3 = type3;
        }

        public String getType3Timea() {
            return Type3Timea;
        }

        public void setType3Timea(String type3Timea) {
            Type3Timea = type3Timea;
        }

        public String getType3Timeb() {
            return Type3Timeb;
        }

        public void setType3Timeb(String type3Timeb) {
            Type3Timeb = type3Timeb;
        }

        public String getType3Prize() {
            return Type3Prize;
        }

        public void setType3Prize(String type3Prize) {
            Type3Prize = type3Prize;
        }

        public String getMaintype() {
            return Maintype;
        }

        public void setMaintype(String maintype) {
            Maintype = maintype;
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
