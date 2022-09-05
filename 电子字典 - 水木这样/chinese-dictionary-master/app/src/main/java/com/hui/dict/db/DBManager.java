package com.hui.dict.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.hui.dict.bean.ChengyuBean;
import com.hui.dict.bean.PinBuWordBean;
import com.hui.dict.bean.WordBean;
import com.hui.dict.utils.CommonUtils;
import java.util.ArrayList;
import java.util.List;
public class DBManager {
    private static SQLiteDatabase db;
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }
    /**
     * 插入很多数据到pywordtb表当中
     * 插入了多个对象所在的集合
     * */
    public static void insertListToPywordtb(List<PinBuWordBean.ResultBean.ListBean>list){
        if (list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
//                获取集合当中的每一个bean对象
                PinBuWordBean.ResultBean.ListBean bean = list.get(i);
//                调用单个对象插入的方法
                try {
                    insertWordToPywordtb(bean);
                }catch (Exception e){
                    Log.i("animee", "insertListToPywordtb: "+bean.getZi()+"已存在！");
                }
            }
        }
    }
    /*
     * 执行插入数据到pywordtb表当中
     * 插入一个对象的方法
     * */
    public static void insertWordToPywordtb(PinBuWordBean.ResultBean.ListBean bean){
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("zi",bean.getZi());
        values.put("py",bean.getPy());
        values.put("wubi",bean.getWubi());
        values.put("pinyin",bean.getPinyin());
        values.put("bushou",bean.getBushou());
        values.put("bihua",bean.getBihua());
        long loc = db.insert(CommonUtils.TABLE_PYWORDTB, null, values);
    }
    /**
     * 查询pywordtb表当中指定拼音的数据
     * */
    public static List<PinBuWordBean.ResultBean.ListBean>queryPyWordFromPywordtb(String py,int page,int pagesize){
        List<PinBuWordBean.ResultBean.ListBean>list = new ArrayList<>();
        // 0,48   48,48+48    96,96+48
        String sql = "select * from pywordtb where py=? or py like ? or py like ? or py like ? limit ?,?";
        int start = (page-1)*pagesize;
        int end = page*pagesize;
        String type1 = py+",%";
        String type2 = "%,"+py+",%";
        String type3 = "%,"+py;
        Cursor cursor = db.rawQuery(sql, new String[]{py,type1,type2,type3, start + "", end + ""});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            PinBuWordBean.ResultBean.ListBean bean = new PinBuWordBean.ResultBean.ListBean(id, zi, py, wubi, pinyin, bushou, bihua);
            list.add(bean);
        }
        return list;
    }

    /**
     * 查询pywordtb表当中指定部首的数据
     * */
    public static List<PinBuWordBean.ResultBean.ListBean>queryBsWordFromPywordtb(String bs,int page,int pagesize){
        List<PinBuWordBean.ResultBean.ListBean>list = new ArrayList<>();
        // 0,48   48,48+48    96,96+48
        String sql = "select * from pywordtb where bushou=? limit ?,?";
        int start = (page-1)*pagesize;
        int end = page*pagesize;
        Cursor cursor = db.rawQuery(sql, new String[]{bs, start + "", end + ""});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            PinBuWordBean.ResultBean.ListBean bean = new PinBuWordBean.ResultBean.ListBean(id, zi, py1, wubi, pinyin, bushou, bihua);
            list.add(bean);
        }
        return list;
    }
    /**
     * id : 27f0004b4e320c21
     * zi : 我
     * py : wo
     * wubi : trnt
     * pinyin : wǒ
     * bushou : 戈
     * bihua : 7
     * jijie : ["我","wǒ","自称，自己，亦指自己一方：我们。我见（我自己的看法）。我辈。我侪（我们）。自我。我盈彼竭。","Ime","人尔敌","","笔画数：7；","部首：戈；","笔顺编号：3121534"]
     * xiangjie : ["我","wǒ","【代】","(会意。从戈,从戈。\u201c我\u201d表示兵器。甲骨文字形象兵器形。本义:兵器。基本义:第一人称代词)","自称;自己〖Iimy;myself〗","我,施身自谓也。\u2014\u2014《说文》","观我生。\u2014\u2014《易·观卦》","万物皆备于我矣。\u2014\u2014《孟子·尽心上》","又如:我行(我这里);我身(我自己;我这个人);我咱(我自。我,我自己);我见犹怜(形容女子容貌姿态美丽动人);我家(自己。我们家);我侬(方言。我)\u2014\u2014按:上古时代,\u201c吾\u201d和\u201c我\u201d在语法上有分别。\u201c吾\u201d不用于动词后面作为宾语","今者吾丧我。\u2014\u2014《庄子》","己方;己国〖we;our〗。如:敌我友;我每(我们);我伲(方言。我们);我曹(我们);我辈(我等,我们)","","我","wǒ","【形】","自己的〖self〗。如:我生(我之行为);我仪(我的匹配)","表示亲密的〖mydear〗。如:我丈(对老人的亲切称呼);我老彭;我老叶;我东海","向一边倾斜的,扭歪的〖wry〗","我,顷顿也。\u2014\u2014《说文》。段玉裁注:\u201c谓倾侧也。顷,头不正也。顿,下首也。故引申为顷侧之意。\u201d","我","wǒ","【动】","杀〖kill〗","我伐用张。\u2014\u2014《书·泰誓中》","我,古杀字。\u2014\u2014《说文》","","我见","wǒjiàn","〖myview〗我自己的看法","没有丝毫我见","我们","wǒmen","〖we;us〗包括我在内的一组人","当我们想到劳动时,也只有在这时,我们才觉得我们太老了","我思","wǒsī","〖cogito〗","认为一个人的存在是由一个思想人这个事实来予以说明的哲学原理","自身或自我的理智活动","我行我素","wǒxíng-wǒsù","〖persistinone\u2019soldwags;livebyone'sowndifinitions;sticktoone'soldwayofdoingthings;thedogsbark,butthecaravangoeson〗无视别人的议论与看法,还是按照自己平时的一套做法去做","所以这件事,外头已当作新闻;他夫妇二人还是毫无闻见,依旧我行我素。\u2014\u2014《官场现形记》"]
     */
    /**
    * @des 插入汉字到汉字详情表当中
    * */
    public static void insertWordToWordtb(WordBean.ResultBean bean){
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("zi",bean.getZi());
        values.put("py",bean.getPy());
        values.put("wubi",bean.getWubi());
        values.put("pinyin",bean.getPinyin());
        values.put("bushou",bean.getBushou());
        values.put("bihua",bean.getBihua());
//        将集合转化成字符串类型进行插入
        String jijie = listToString(bean.getJijie());
        values.put("jijie",jijie);
        String xiangjie = listToString(bean.getXiangjie());
        values.put("xiangjie",xiangjie);
        db.insert(CommonUtils.TABLE_WORDTB,null,values);
    }
    /**
     * 根据传入的汉字，查找对应信息组成的对象
     * */
    public static WordBean.ResultBean queryWordFromWordtb(String word){
        String sql = "select * from wordtb where zi=?";
        Cursor cursor = db.rawQuery(sql, new String[]{word});
        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            String jijie = cursor.getString(cursor.getColumnIndex("jijie"));
            String xiangjie = cursor.getString(cursor.getColumnIndex("xiangjie"));
            List<String> jijielist = stringToList(jijie);
            List<String> xiangxilist = stringToList(xiangjie);
            WordBean.ResultBean bean = new WordBean.ResultBean(id, zi, py1, wubi, pinyin, bushou, bihua, jijielist, xiangxilist);
            return bean;
        }
        return null;
    }
    /**
     * 插入数据到成语表当中
     * */
    public static void insertCyToCyutb(ChengyuBean.ResultBean bean){
        ContentValues values = new ContentValues();
        values.put("chengyu",bean.getChengyu());
        values.put("bushou",bean.getBushou());
        values.put("head",bean.getHead());
        values.put("pinyin",bean.getPinyin());
        values.put("chengyujs",bean.getChengyujs());
        values.put("from_",bean.getFrom_());
        values.put("example",bean.getExample());
        values.put("yufa",bean.getYufa());
        values.put("ciyujs",bean.getCiyujs());
        values.put("yinzhengjs",bean.getYinzhengjs());
        String ty = listToString(bean.getTongyi());
        values.put("tongyi",ty);
        String fy = listToString(bean.getFanyi());
        values.put("fanyi",fy);
        db.insert(CommonUtils.TABLE_CYUB,null,values);
    }
    /**
     * 根据传入的成语，查看详情内容
     * */
    public static ChengyuBean.ResultBean queryCyFromCyutb(String cyu){
        String sql = "select * from cyutb where chengyu = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{cyu});
        if (cursor.moveToFirst()) {
            String chengyu = cursor.getString(cursor.getColumnIndex("chengyu"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String head = cursor.getString(cursor.getColumnIndex("head"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String chengyujs = cursor.getString(cursor.getColumnIndex("chengyujs"));
            String from_ = cursor.getString(cursor.getColumnIndex("from_"));
            String example = cursor.getString(cursor.getColumnIndex("example"));
            String yufa = cursor.getString(cursor.getColumnIndex("yufa"));
            String ciyujs = cursor.getString(cursor.getColumnIndex("ciyujs"));
            String yinzhengjs = cursor.getString(cursor.getColumnIndex("yinzhengjs"));
            String tongyi = cursor.getString(cursor.getColumnIndex("tongyi"));
            String fanyi = cursor.getString(cursor.getColumnIndex("fanyi"));
            List<String> tylist = stringToList(tongyi);
            List<String> fyList = stringToList(fanyi);
            ChengyuBean.ResultBean bean = new ChengyuBean.ResultBean(chengyu, bushou, head, pinyin, chengyujs, from_, example, yufa, ciyujs, yinzhengjs, tylist, fyList);
            return bean;
        }

        return null;
    }
    /*  向收藏汉字的表格当中传入数据*/
    public static void insertZiToCollwordtb(String zi){
        ContentValues values = new ContentValues();
        values.put("zi",zi);
        db.insert(CommonUtils.TABLE_COLLECT_WORDTB,null,values);
    }
    /* 删除收藏表格当中的数据*/
    public static void deleteZiToCollwordtb(String zi){
        String sql = "delete from collwordtb where zi = ?";
        db.execSQL(sql,new Object[]{zi});
    }
    /** 查找此汉字是否收藏在表格当中*/
    public static boolean isExistZiInCollwordtb(String zi){
        String sql = "select * from collwordtb where zi = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{zi});
        if (cursor.getCount()>0) {
            return true;
        }
        return false;
    }
    /* 查找收藏汉字表当中的所有汉字*/
    public static List<String>queryAllInCollwordtb(){
        String sql = "select * from collwordtb";
        Cursor cursor = db.rawQuery(sql,null);
        List<String>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            list.add(zi);
        }
        return list;
    }
    /*  向收藏成语的表格当中传入数据*/
    public static void insertCyuToCollcyutb(String cy){
        ContentValues values = new ContentValues();
        values.put("chengyu",cy);
        db.insert(CommonUtils.TABLE_COLLECT_CYUTB,null,values);
    }
    /* 删除收藏表格当中的数据*/
    public static void deleteCyuToCollcyutb(String cy){
        String sql = "delete from collcyutb where chengyu = ?";
        db.execSQL(sql,new Object[]{cy});
    }
    /** 查找此汉字是否收藏在表格当中*/
    public static boolean isExistCyuInCollcyutb(String cy){
        String sql = "select * from collcyutb where chengyu = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{cy});
        if (cursor.getCount()>0) {
            return true;
        }
        return false;
    }
    /* 查找收藏成语表格中的所有成语*/
    public static List<String>queryAllCyuInCollcyutb(){
        String sql = "select * from collcyutb";
        Cursor cursor = db.rawQuery(sql, null);
        List<String>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String chengyu = cursor.getString(cursor.getColumnIndex("chengyu"));
            list.add(chengyu);
        }
        return list;
    }

    /**
     * 查询成语表当中所有的记录
     * */
    public static List<String>queryAllCyFromCyutb(){
        List<String>cyAllList = new ArrayList<>();
        String sql = "select chengyu from cyutb";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String chengyu = cursor.getString(cursor.getColumnIndex("chengyu"));
            cyAllList.add(chengyu);
        }
        return cyAllList;
    }
    /** 将字符串转换成List集合的方法*/
    public static List<String>stringToList(String msg){
        List<String>list = new ArrayList<>();
        if (!TextUtils.isEmpty(msg)) {
            String[] arr = msg.split("\\|");
            for (int i = 0; i < arr.length; i++) {
                String s = arr[i].trim();
                if (!TextUtils.isEmpty(s)) {
                    list.add(s);
                }
            }
        }
        return list;
    }
    /* 将List集合转化成字符串的方法*/
    public static String listToString(List<String>list){
        StringBuilder sb = new StringBuilder();
        if (list!=null&&!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String msg = list.get(i);
                msg+="|";
                sb.append(msg);
            }
        }
        return sb.toString();
    }
}
