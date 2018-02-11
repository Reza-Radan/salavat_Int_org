package salavatorg.salavaltintorg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by masoomeh on 2/11/18.
 */

public class TajilInFarajActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tajil_in_faraj_page);
        setTitle(getString(R.string.hurry_faraj));
    }
}
