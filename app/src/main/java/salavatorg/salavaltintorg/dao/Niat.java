package salavatorg.salavaltintorg.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by masoomeh on 12/14/17.
 */
@Entity
public class Niat {

    //getId from server
    @PrimaryKey
    int id;
    String des_fa , des_en ,des_ae;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDes_fa() {
        return des_fa;
    }

    public void setDes_fa(String des_fa) {
        this.des_fa = des_fa;
    }

    public String getDes_en() {
        return des_en;
    }

    public void setDes_en(String des_en) {
        this.des_en = des_en;
    }

    public String getDes_ae() {
        return des_ae;
    }

    public void setDes_ae(String des_ae) {
        this.des_ae = des_ae;
    }
}
