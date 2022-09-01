package userXinWei;

public class Brower {
    private String username;
    private String userimageurl;
    private String imageurl;
    private String time;
    private String NeiRong;
    private int DianZanNUMS;
    private int PinLunNUMS;
    private String TieZiID;

    public Brower(String username,String userimageurl, String imageurl,String time,String NeiRong,int DianZanNUMS,int PinLunMNUS,String TieZiID) {
        this.username = username;
        this.userimageurl=userimageurl;
        this.imageurl = imageurl;
        this.time=time;
        this.NeiRong=NeiRong;
        this.DianZanNUMS=DianZanNUMS;
        this.PinLunNUMS=PinLunMNUS;
        this.TieZiID=TieZiID;
    }

    public String getUsername() {
        return username;
    }

    public String getUserimageurl() {
        return userimageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getTime() {
        return time;
    }

    public String getNeiRong() {
        return NeiRong;
    }

    public int getDianZanNUMS() {
        return DianZanNUMS;
    }

    public int getPinLunNUMS() {
        return PinLunNUMS;
    }

    public String getTieZiID() {
        return TieZiID;
    }
}
