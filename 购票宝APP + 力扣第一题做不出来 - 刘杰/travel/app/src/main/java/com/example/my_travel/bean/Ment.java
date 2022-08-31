package com.example.my_travel.bean;

import java.io.Serializable;

public class Ment implements Serializable {
    private Integer id;
    private String username;
    private String conten;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConten() {
        return conten;
    }

    public void setConten(String conten) {
        this.conten = conten;
    }
}
