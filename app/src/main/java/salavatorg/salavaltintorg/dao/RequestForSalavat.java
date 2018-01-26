package salavatorg.salavaltintorg.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by masoomeh on 12/14/17.
 */

@Entity
public class RequestForSalavat {

    //getId from server
    @PrimaryKey
    int id;
    int nId ,userId;
    String desc;
    int date;
}
