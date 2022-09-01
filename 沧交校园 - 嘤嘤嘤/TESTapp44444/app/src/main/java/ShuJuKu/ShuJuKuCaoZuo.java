package ShuJuKu;

import java.sql.SQLException;

public class ShuJuKuCaoZuo extends ShuJuKuLianJie{
    /**
     * 上传头像的网络路径到数据库
     * @param url
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void touxinag(String url) throws ClassNotFoundException, SQLException {
        this.openDB();
        String sql="update user_inf set touxiang=? where username=?";
        ps =con.prepareStatement(sql);
        ps.setString(1, url);
        ps.setString(2, QuanJU.getInstance().username);
        ps.executeUpdate();
        this.closeResoure();
    }
    /**
     * 在数据库里插入一条帖子数据
     * @param username
     * @param time
     * @param NeiRong
     * @param imageURL
     * @param userImageUrl
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void fatie(String username,String time,String NeiRong,String imageURL,String userImageUrl) throws ClassNotFoundException, SQLException {
        TieZiLianJie tiezilianjie=new TieZiLianJie();
        tiezilianjie.openDB();
        String sql="INSERT INTO tu_cao (tieziID, username, time, NeiRong, imageURL,userImageUrl) VALUES (?, ?, ?, ?, ?,?)";
        tiezilianjie.ps =tiezilianjie.con.prepareStatement(sql);
        tiezilianjie.ps.setString(1, username+time);
        tiezilianjie.ps.setString(2, username);
        tiezilianjie.ps.setString(3,time);
        tiezilianjie.ps.setString(4,NeiRong);
        tiezilianjie.ps.setString(5,imageURL);
        tiezilianjie.ps.setString(6,userImageUrl);
        tiezilianjie.ps.execute();
        tiezilianjie.ps.close();
    }
    /**
     * 修改帖子的点赞数量（+1，-1）
     */
    public void DianZanNUMS(String tieziID,int xiugai,int dianzanNUMS) throws SQLException, ClassNotFoundException {
        TieZiLianJie tiezilianjie=new TieZiLianJie();
        tiezilianjie.openDB();
        String sql="update tu_cao set DianZanNUMS=? where tieziID=?";
        tiezilianjie.ps =tiezilianjie.con.prepareStatement(sql);
        tiezilianjie.ps.setString(2, tieziID);
        if(xiugai==1){//表示是增加一个点赞
            tiezilianjie.ps.setInt(1,dianzanNUMS );
        }else if(xiugai==0){//表示是减少一个点赞
            tiezilianjie.ps.setInt(1, dianzanNUMS);
        }
        tiezilianjie.ps.executeUpdate();
        tiezilianjie.closeResoure();
        /**
         * 下面是增加或减少用户信息中的wodianzande数据，将帖子的id加入或删除，从而达到保存已点赞帖子的目的。
         */
        this.openDB();//开的是user数据库
        String sql1="update user_inf set WODianZanDe=? where username=?";
        ps =con.prepareStatement(sql1);
        ps.setString(2, QuanJU.getInstance().username);
        if(xiugai==1){//表示是增加一个点赞
            QuanJU.getInstance().WODianZanDe=QuanJU.getInstance().WODianZanDe+tieziID+",";//先修改个人全局,再将这个全局写回数据库。正常情况下个人全局信息会在登录时获取，所以这里不需要获取
        }else if(xiugai==0){//表示是减少一个点赞
            //这里需要先删除子串,使用replaceFirst()函数将帖子id用“”替换，并返回新字符串接收
            QuanJU.getInstance().WODianZanDe=QuanJU.getInstance().WODianZanDe.replaceAll(tieziID+",","");
        }
        ps.setString(1,QuanJU.getInstance().WODianZanDe);
        ps.executeUpdate();
        this.closeResoure();
    }
    /** 在数据库里插入一条评论数据
     *
     * @param tieziID//评论的帖子的id
     * @param username//发表评论的人的用户名
     * @param userImageUrl//发表评论的人的头像地址
     * @param neirong//评论内容
     * @param time//评论时间
     */
    public void PinLun(String tieziID,String username,String userImageUrl,String neirong,String time) throws ClassNotFoundException, SQLException {
        TieZiLianJie tiezilianjie=new TieZiLianJie();
        tiezilianjie.openDB();
        String sql="INSERT INTO pin_lun (username, userImageUrl, neirong, time,TieZiID) VALUES (?, ?, ?, ?, ?)";
        tiezilianjie.ps =tiezilianjie.con.prepareStatement(sql);
        tiezilianjie.ps.setString(1, username);
        tiezilianjie.ps.setString(2, userImageUrl);
        tiezilianjie.ps.setString(3,neirong);
        tiezilianjie.ps.setString(4,time);
        tiezilianjie.ps.setString(5,tieziID);
        tiezilianjie.ps.execute();
        tiezilianjie.closeResoure();
    }
}
