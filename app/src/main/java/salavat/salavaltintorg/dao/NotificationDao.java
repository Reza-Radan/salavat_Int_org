package salavat.salavaltintorg.dao;

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
public interface NotificationDao {
    
    @Insert
    void insertMultipleRecord(Notification... notifications);

    @Insert
    void insertMultipleListRecord(List<Notification> notifications);

    @Insert
    void insertOnlySingleRecord(Notification Notification);

    @Query("SELECT * FROM Notification")
    List<Notification> fetchAllData();

    @Query("SELECT * FROM Notification WHERE id =:college_id")
    Notification getSingleRecord(int college_id);

    @Update
    void updateRecord(Notification Notification);

    @Delete
    void deleteRecord(Notification Notification);
    

}
