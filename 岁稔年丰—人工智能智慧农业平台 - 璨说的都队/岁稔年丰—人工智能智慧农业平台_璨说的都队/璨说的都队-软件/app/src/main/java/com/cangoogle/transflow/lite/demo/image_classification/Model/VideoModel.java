package com.baidu.paddle.lite.demo.image_classification.Model;

import java.util.List;

public class VideoModel {
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
        public String VideoId;
        public String Title;
        public String Label;
        public String Hours;
        public String Persons;
        public String TeaId;
        public String TeaName;
        public String TeaUrl;
        public String VideoPic;
        public String VideoUrl;
        public String Price;


        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }

        public String getVideoId() {
            return VideoId;
        }

        public void setVideoId(String videoId) {
            VideoId = videoId;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getLabel() {
            return Label;
        }

        public void setLabel(String label) {
            Label = label;
        }

        public String getHours() {
            return Hours;
        }

        public void setHours(String hours) {
            Hours = hours;
        }

        public String getPersons() {
            return Persons;
        }

        public void setPersons(String persons) {
            Persons = persons;
        }

        public String getTeaId() {
            return TeaId;
        }

        public void setTeaId(String teaId) {
            TeaId = teaId;
        }

        public String getTeaName() {
            return TeaName;
        }

        public void setTeaName(String teaName) {
            TeaName = teaName;
        }

        public String getTeaUrl() {
            return TeaUrl;
        }

        public void setTeaUrl(String teaUrl) {
            TeaUrl = teaUrl;
        }

        public String getVideoPic() {
            return VideoPic;
        }

        public void setVideoPic(String videoPic) {
            VideoPic = videoPic;
        }

        public String getVideoUrl() {
            return VideoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            VideoUrl = videoUrl;
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
