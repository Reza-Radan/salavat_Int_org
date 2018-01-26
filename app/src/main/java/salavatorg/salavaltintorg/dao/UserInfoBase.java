package salavatorg.salavaltintorg.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by masoomeh on 12/14/17.
 */
@Entity
public class UserInfoBase {

    @PrimaryKey
    int id;
    String name ,family , gender , phone_num;
}
