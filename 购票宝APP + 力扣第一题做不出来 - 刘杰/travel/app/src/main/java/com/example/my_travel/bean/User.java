package com.example.my_travel.bean;
/**
* id -- id
 * username -- 用户名
 * userpwd -- 用户密码
* */
public class User {
    private Integer id;
    private String username;
    private String userpwd;

    public User(String username, String userpwd) {
        this.username = username;
        this.userpwd = userpwd;
    }

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

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userpwd='" + userpwd + '\'' +
                '}';
    }
}
