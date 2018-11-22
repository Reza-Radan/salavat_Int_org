package salavat.salavaltintorg.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by masoomeh on 12/14/17.
 */
@Entity
public class Faraj {


    //getId from server
    @PrimaryKey
    int id;
    int salavat;
    int date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalavat() {
        return salavat;
    }

    public void setSalavat(int salavat) {
        this.salavat = salavat;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}