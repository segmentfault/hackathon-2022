package com.baidu.paddle.lite.demo.image_classification.Bean.Craft;

import android.widget.ScrollView;

public class Craft_first_Bean {
        private String _name;
        private int id_1, id_2, id_3, id_4;
        private String _Content;
        public Craft_first_Bean(int _id_1, String _name){
            this.id_1 = _id_1;
            this._name = _name;
        }
        public String get_name(){
            return this._name;
        }


        public String get_Content(){return this._Content;}
        public void set_Content(String x){this._Content=x;}

    public int getId_1() {
        return id_1;
    }

    public void setId_1(int id_1) {
        this.id_1 = id_1;
    }

    public int getId_2() {
        return id_2;
    }

    public void setId_2(int id_2) {
        this.id_2 = id_2;
    }

    public int getId_3() {
        return id_3;
    }

    public void setId_3(int id_3) {
        this.id_3 = id_3;
    }

    public int getId_4() {
        return id_4;
    }

    public void setId_4(int id_4) {
        this.id_4 = id_4;
    }

    public void set_name(String _name) {
            this._name = _name;
        }

}
