package com.baidu.paddle.lite.demo.image_classification.Bean;

import android.util.Log;

public class Frist_fragment_list_Bean {
        private String _name;
        private int id_1, id_2, id_3, id_4;
        private String _Content;
        public Frist_fragment_list_Bean(int _id_1, int _id_2, int _id_3, int _id_4){
            this.id_1 = _id_1;
            this.id_2 = _id_2;
            this.id_3 = _id_3;
            this.id_4 = _id_4;
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
