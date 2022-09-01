package com.ouni.common;

import lombok.Data;

public enum ResultEnum {

    SUCCESS("200","Success"),
    UNKNOWN_ERROR("444","An unknown error has occurred"),
    FAIL_LIKE("422","Please do not LIKE repeatedly"),
    FAIL_TOKEN("400","Invalid token, illegal access!!"),
    FAIL_SIGN("401","Please do not check in repeatedly!"),
//    xianzhi
    FAIL_UPLOAD("401","Failed to publish product!"),
    NONE_FIND_GOOD("402","Could not find the good!"),
    ;



    private String code;
    private String msg;

    private ResultEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
