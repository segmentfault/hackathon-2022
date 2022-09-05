package com.hui.dict.bean;

import java.util.List;

public class ChengyuBean {
    /**
     * reason : success
     * result : {"bushou":"一","head":"一","pinyin":"yī xīn yī yì","chengyujs":" 只有一个心眼儿，没有别的考虑。","from_":" 《三国志·魏志·杜恕传》：\u201c免为庶人，徙章武郡，是岁嘉平元年。\u201d裴松之注引《杜氏新书》：\u201c故推一心，任一意，直而行之耳。\u201d","example":" 所以彭官保便～的料理防守事宜，庄制军便～料理军需器械。 清·张春帆《宦海》第四回","yufa":" 联合式；作定语、状语；含褒义","ciyujs":"[heart and soul;whole-heartedly] 形容专心专意,毫无他念","yinzhengjs":"《三国志·魏志·杜恕传》\u201c免为庶人，徙 章武郡 ，是岁 嘉平 元年\u201d 裴松之 注引《杜氏新书》：\u201c故推一心，任一意，直而行之耳。\u201d后因以\u201c一心一意\u201d谓同心同意；或专心专意，毫无他念。 唐 骆宾王 《代女道士王灵妃赠道士李荣》诗：\u201c想知人意自相寻，果得深心共一心。一心一意无穷已，投漆投胶非足拟。\u201d 明 王守仁 《传习录》卷上：\u201c静而不妄动则安，安则一心一意只在此处，千思万想，务求必得此至善。\u201d《儒林外史》第五二回：\u201c 陈正公 见他如此至诚，一心一意要把银子借与他。\u201d 浩然 《艳阳天》第六章：\u201c这时候的 马之悦 ，更是想尽一切办法，寻找一切机会，表现自己跟党一心一意。\u201d","tongyi":["全心全意","专心致志","聚精会神","全神贯注","真心实意","一心一意"],"fanyi":["三心二意","见异思迁","心猿意马"]}
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

        private String chengyu;
        private String bushou;
        private String head;
        private String pinyin;
        private String chengyujs;
        private String from_;
        private String example;
        private String yufa;
        private String ciyujs;
        private String yinzhengjs;
        private List<String> tongyi;
        private List<String> fanyi;

        public ResultBean() {
        }

        public ResultBean(String chengyu, String bushou, String head, String pinyin, String chengyujs, String from_, String example, String yufa, String ciyujs, String yinzhengjs, List<String> tongyi, List<String> fanyi) {
            this.chengyu = chengyu;
            this.bushou = bushou;
            this.head = head;
            this.pinyin = pinyin;
            this.chengyujs = chengyujs;
            this.from_ = from_;
            this.example = example;
            this.yufa = yufa;
            this.ciyujs = ciyujs;
            this.yinzhengjs = yinzhengjs;
            this.tongyi = tongyi;
            this.fanyi = fanyi;
        }

        public void setChengyu(String chengyu) {
            this.chengyu = chengyu;
        }

        public String getChengyu() {
            return chengyu;
        }

        public String getBushou() {
            return bushou;
        }

        public void setBushou(String bushou) {
            this.bushou = bushou;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getChengyujs() {
            return chengyujs;
        }

        public void setChengyujs(String chengyujs) {
            this.chengyujs = chengyujs;
        }

        public String getFrom_() {
            return from_;
        }

        public void setFrom_(String from_) {
            this.from_ = from_;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public String getYufa() {
            return yufa;
        }

        public void setYufa(String yufa) {
            this.yufa = yufa;
        }

        public String getCiyujs() {
            return ciyujs;
        }

        public void setCiyujs(String ciyujs) {
            this.ciyujs = ciyujs;
        }

        public String getYinzhengjs() {
            return yinzhengjs;
        }

        public void setYinzhengjs(String yinzhengjs) {
            this.yinzhengjs = yinzhengjs;
        }

        public List<String> getTongyi() {
            return tongyi;
        }

        public void setTongyi(List<String> tongyi) {
            this.tongyi = tongyi;
        }

        public List<String> getFanyi() {
            return fanyi;
        }

        public void setFanyi(List<String> fanyi) {
            this.fanyi = fanyi;
        }
    }
}
