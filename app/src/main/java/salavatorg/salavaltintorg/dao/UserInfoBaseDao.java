package salavatorg.salavaltintorg.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by masoomeh on 12/14/17.
 */
@Dao
public interface UserInfoBaseDao {

    @Insert
    void insertMultipleRecord(UserInfoBase... userInfoBase);

    @Insert
    void insertMultipleListRecord(List<UserInfoBase> userInfoBases);

    @Insert
    long insertOnlySingleRecord(UserInfoBase userInfoBase);

    @Query("SELECT * FROM UserInfoBase")
    List<UserInfoBase> fetchAllData();

    @Query("SELECT * FROM UserInfoBase WHERE id =:college_id")
    UserInfoBase getSingleRecord(int college_id);

    @Update
    int updateRecord(UserInfoBase userInfoBase);

    @Delete
    void deleteRecord(UserInfoBase userInfoBase);

    @Query("UPDATE UserInfoBase SET password = :password  WHERE id = :id ")
    int updatePassword(int id ,String password);

}
