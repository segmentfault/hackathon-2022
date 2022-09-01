package userXinWei;

public class Brower_pinlun {
    private String username;
    private String userimageurl;
    private String time;
    private String NeiRong;
    private String tieziID;

    public String getTieziID() {
        return tieziID;
    }

    public void setTieziID(String tieziID) {
        this.tieziID = tieziID;
    }

    public String getUsername() {
        return username;
    }

    public String getUserimageurl() {
        return userimageurl;
    }

    public String getTime() {
        return time;
    }

    public String getNeiRong() {
        return NeiRong;
    }


    public Brower_pinlun(String username, String userimageurl, String time, String NeiRong,String tieziID) {
        this.username = username;
        this.userimageurl=userimageurl;
        this.time=time;
        this.NeiRong=NeiRong;
        this.tieziID=tieziID;
    }
}
