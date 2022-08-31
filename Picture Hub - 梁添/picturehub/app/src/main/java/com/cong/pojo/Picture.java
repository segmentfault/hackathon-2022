package com.cong.pojo;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Picture extends BmobObject {
    private int pid;
    private int uid;
    private BmobFile curl;;

    public Picture(int pid, int uid, BmobFile curl) {
        this.pid = pid;
        this.uid = uid;
        this.curl = curl;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public BmobFile getCurl() {
        return curl;
    }

    public void setCurl(BmobFile curl) {
        this.curl = curl;
    }
}
