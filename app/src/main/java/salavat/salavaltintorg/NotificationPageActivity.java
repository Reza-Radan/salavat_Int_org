package salavat.salavaltintorg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import salavat.salavaltintorg.dao.AppCreatorDatabase;
import salavat.salavaltintorg.dao.Notification;

/**
 * Created by masoomeh on 2/20/18.
 */

public class NotificationPageActivity extends AppCompatActivity {

    private AppCreatorDatabase db;
    private List<Notification> notifications;

//    @BindView(R.id.toolbarNo)
//    Toolbar toolbar;
    String header =" header",body ="body",footer= "footer";

    @BindView(R.id.txtvBody)
    TextView txtvBody;

    @BindView(R.id.txtvBottom)
    TextView txtvBottom;

    @BindView(R.id.txtvHeader)
    TextView txtvHeader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        ButterKnife.bind(this);
        if(getIntent().getExtras() != null){
            header = getIntent().getExtras().getString("header","");
            body = getIntent().getExtras().getString("body");
            footer = getIntent().getExtras().getString("footer");
            Log.e("tag" ,"extra: " +getIntent().getExtras() + " body: " +body);

        }

        txtvHeader.setText(header);
        txtvBody.setText(body);
        txtvBottom.setText(footer);

        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle(getString(R.string.notification_list));




    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
