package salavatorg.salavaltintorg.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by masoomeh on 12/14/17.
 */
@Entity
public class UserInfoExtra {

    @PrimaryKey
    int userId;
    String occupation, birthday,introducer, education, country, city, national_id;
    int salavat_num;
    boolean group_head;

}
