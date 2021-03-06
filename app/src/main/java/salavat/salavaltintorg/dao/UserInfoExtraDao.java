package salavat.salavaltintorg.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by masoomeh on 12/14/17.
 */
@Dao
public interface UserInfoExtraDao {

    @Insert
    void insertMultipleRecord(UserInfoExtra... UserInfoExtra);

    @Insert
    void insertMultipleListRecord(List<UserInfoExtra> userInfoBases);

    @Insert
    long insertOnlySingleRecord(UserInfoExtra UserInfoExtra);

    @Query("SELECT * FROM UserInfoExtra")
    List<UserInfoExtra> fetchAllData();

//    @Query("SELECT * FROM UserInfoExtra WHERE id =:college_id")
//    UserInfoExtra getSingleRecord(int college_id);
    @Query("DELETE FROM UserInfoExtra")
    int deleteAllRecords();


    @Update
    int updateRecord(UserInfoExtra UserInfoExtra);




}
