package salavatorg.salavaltintorg.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by masoomeh on 12/14/17.
 */

    @Database(entities = {Faraj.class ,Niat.class ,Notification.class ,RequestForSalavat.class
            ,UserInfoBase.class ,UserInfoExtra.class}, version = 1)
    abstract class AppCreatorDatabase extends RoomDatabase {
    public abstract FarajDao farajDao();
    public abstract NiatDao niatDao();
    public abstract NotificationDao  notificationDao();
    public abstract RequestForSalavatDao requestForSalavatDao();
    public abstract UserInfoBaseDao userInfoBaseDao();
    public abstract UserInfoExtraDao userInfoExtraDao();

}
