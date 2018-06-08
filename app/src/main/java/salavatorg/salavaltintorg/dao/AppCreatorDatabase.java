package salavatorg.salavaltintorg.dao;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by masoomeh on 12/14/17.
 */

    @Database(entities = {Faraj.class ,Niat.class ,Notification.class ,RequestForSalavat.class
            ,UserInfoBase.class ,UserInfoExtra.class}, version = 4)
    public abstract class AppCreatorDatabase extends RoomDatabase {
    public static final String DB_NAME = "salavat_db";
    public abstract FarajDao farajDao();
    public abstract NiatDao niatDao();
    public abstract NotificationDao  notificationDao();
    public abstract RequestForSalavatDao requestForSalavatDao();
    public abstract UserInfoBaseDao userInfoBaseDao();
    public abstract UserInfoExtraDao userInfoExtraDao();

}
