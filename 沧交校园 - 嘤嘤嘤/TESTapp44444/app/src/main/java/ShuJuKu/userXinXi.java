package ShuJuKu;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.SQLException;

/**
 * 这个类用来存储用户的所有信息。
 */
public class userXinXi implements Parcelable {
    private String username="名字";//设置用户名，默认未登录显示“名字”


    public static final Creator<userXinXi> CREATOR = new Creator<userXinXi>() {
        @Override
        public userXinXi createFromParcel(Parcel in) {
            userXinXi userXinXi1=new userXinXi();
            userXinXi1.setUsername(in.readString());
            return userXinXi1;
        }

        @Override
        public userXinXi[] newArray(int size) {
            return new userXinXi[size];
        }
    };


    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
    }
    public userXinXi fuzhi(String username) throws ClassNotFoundException, SQLException {
        //userXinXi userXinXi1=new userXinXi();
        ShuJuKuLianJie lianjie = new ShuJuKuLianJie();
        lianjie.openDB();//链接数据库
        String sql="SELECT * from user_inf where username=?";
        lianjie.ps = lianjie.con.prepareStatement(sql);//预编译sql2,先判断用户名是否存在
        lianjie.ps.setString(1, username);
        lianjie.re=lianjie.ps.executeQuery();

        this.setUsername(username);
     return this;
    }
}
