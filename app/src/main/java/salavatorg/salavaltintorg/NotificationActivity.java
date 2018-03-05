package salavatorg.salavaltintorg;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.etiennelawlor.imagegallery.library.adapters.ImageGalleryAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import salavatorg.salavaltintorg.dao.AppCreatorDatabase;
import salavatorg.salavaltintorg.dao.Notification;
import salavatorg.salavaltintorg.dao.UserInfoBase;

/**
 * Created by masoomeh on 2/20/18.
 */

public class NotificationActivity extends AppCompatActivity {

    private AppCreatorDatabase db;
    private List<Notification> notifications;
    @BindView(R.id.recyclerNotification)
    RecyclerView recyclerNotification;

    @BindView(R.id.toolbarNo)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.notification_list));


        db = Room.databaseBuilder(
                NotificationActivity.this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();
        getNotificationData();

    }

    public void getNotificationData() {
        new AsyncTask<Void, Void, List<Notification>>() {
            @Override
            protected List<Notification> doInBackground(Void... voids) {
                notifications = db.notificationDao().fetchAllData();
                Log.i("Tag" ,"notification: " +notifications.size());
                return notifications;
            }

            @Override
            protected void onPostExecute(List<Notification> notifications) {

                recyclerNotification.setHasFixedSize(true);
                recyclerNotification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                recyclerNotification.setItemAnimator(new DefaultItemAnimator());
                recyclerNotification.setAdapter(new NotificationAdaptor(NotificationActivity.this ,notifications));
            }
        }.execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
