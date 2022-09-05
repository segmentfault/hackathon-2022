package com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.data;

public class PersonRecommendation {
    private String pictureUrl;
    private String name;

    @Override
    public String toString() {
        return "PersonRecommendation{" +
                "pictureUrl='" + pictureUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
