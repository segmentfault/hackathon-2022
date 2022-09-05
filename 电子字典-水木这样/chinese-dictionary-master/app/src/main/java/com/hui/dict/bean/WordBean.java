package com.hui.dict.bean;

import java.util.List;

public class WordBean {

    /**
     * reason : 返回成功
     * result : {"id":"27f0004b4e320c21","zi":"我","py":"wo","wubi":"trnt","pinyin":"wǒ","bushou":"戈","bihua":"7","jijie":["我","wǒ","自称，自己，亦指自己一方：我们。我见（我自己的看法）。我辈。我侪（我们）。自我。我盈彼竭。","Ime","人尔敌","","笔画数：7；","部首：戈；","笔顺编号：3121534"],"xiangjie":["我","wǒ","【代】","(会意。从戈,从戈。\u201c我\u201d表示兵器。甲骨文字形象兵器形。本义:兵器。基本义:第一人称代词)","自称;自己〖Iimy;myself〗","我,施身自谓也。\u2014\u2014《说文》","观我生。\u2014\u2014《易·观卦》","万物皆备于我矣。\u2014\u2014《孟子·尽心上》","又如:我行(我这里);我身(我自己;我这个人);我咱(我自。我,我自己);我见犹怜(形容女子容貌姿态美丽动人);我家(自己。我们家);我侬(方言。我)\u2014\u2014按:上古时代,\u201c吾\u201d和\u201c我\u201d在语法上有分别。\u201c吾\u201d不用于动词后面作为宾语","今者吾丧我。\u2014\u2014《庄子》","己方;己国〖we;our〗。如:敌我友;我每(我们);我伲(方言。我们);我曹(我们);我辈(我等,我们)","","我","wǒ","【形】","自己的〖self〗。如:我生(我之行为);我仪(我的匹配)","表示亲密的〖mydear〗。如:我丈(对老人的亲切称呼);我老彭;我老叶;我东海","向一边倾斜的,扭歪的〖wry〗","我,顷顿也。\u2014\u2014《说文》。段玉裁注:\u201c谓倾侧也。顷,头不正也。顿,下首也。故引申为顷侧之意。\u201d","我","wǒ","【动】","杀〖kill〗","我伐用张。\u2014\u2014《书·泰誓中》","我,古杀字。\u2014\u2014《说文》","","我见","wǒjiàn","〖myview〗我自己的看法","没有丝毫我见","我们","wǒmen","〖we;us〗包括我在内的一组人","当我们想到劳动时,也只有在这时,我们才觉得我们太老了","我思","wǒsī","〖cogito〗","认为一个人的存在是由一个思想人这个事实来予以说明的哲学原理","自身或自我的理智活动","我行我素","wǒxíng-wǒsù","〖persistinone\u2019soldwags;livebyone'sowndifinitions;sticktoone'soldwayofdoingthings;thedogsbark,butthecaravangoeson〗无视别人的议论与看法,还是按照自己平时的一套做法去做","所以这件事,外头已当作新闻;他夫妇二人还是毫无闻见,依旧我行我素。\u2014\u2014《官场现形记》"]}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {


        private String id;
        private String zi;
        private String py;
        private String wubi;
        private String pinyin;
        private String bushou;
        private String bihua;
        private List<String> jijie;
        private List<String> xiangjie;

        public ResultBean(String id, String zi, String py, String wubi, String pinyin, String bushou, String bihua, List<String> jijie, List<String> xiangjie) {
            this.id = id;
            this.zi = zi;
            this.py = py;
            this.wubi = wubi;
            this.pinyin = pinyin;
            this.bushou = bushou;
            this.bihua = bihua;
            this.jijie = jijie;
            this.xiangjie = xiangjie;
        }

        public ResultBean() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getZi() {
            return zi;
        }

        public void setZi(String zi) {
            this.zi = zi;
        }

        public String getPy() {
            return py;
        }

        public void setPy(String py) {
            this.py = py;
        }

        public String getWubi() {
            return wubi;
        }

        public void setWubi(String wubi) {
            this.wubi = wubi;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getBushou() {
            return bushou;
        }

        public void setBushou(String bushou) {
            this.bushou = bushou;
        }

        public String getBihua() {
            return bihua;
        }

        public void setBihua(String bihua) {
            this.bihua = bihua;
        }

        public List<String> getJijie() {
            return jijie;
        }

        public void setJijie(List<String> jijie) {
            this.jijie = jijie;
        }

        public List<String> getXiangjie() {
            return xiangjie;
        }

        public void setXiangjie(List<String> xiangjie) {
            this.xiangjie = xiangjie;
        }
    }
}
