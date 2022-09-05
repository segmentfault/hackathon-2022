package com.baidu.paddle.lite.demo.image_classification.api;

public interface TtitCallback {

    void onSuccess(String res);

    void onFailure(Exception e);
}
