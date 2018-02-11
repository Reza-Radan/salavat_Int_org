package salavatorg.salavaltintorg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by masoomeh on 2/11/18.
 */

public class RequestForSalavatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_for_salavat);
        setTitle(getString(R.string.request_for_salavat));
    }
}
