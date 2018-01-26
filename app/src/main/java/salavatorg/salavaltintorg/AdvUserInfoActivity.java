package salavatorg.salavaltintorg;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;

/**
 * Created by masoomeh on 12/14/17.
 */

public class AdvUserInfoActivity extends Activity {

    @BindView(R.id.input_occupation)
    EditText occupation;
    @BindView(R.id.input_introducer)
    EditText introducer;
    @BindView(R.id.input_education)
    EditText education;
    @BindView(R.id.input_country)
    EditText country;
    @BindView(R.id.input_city)
    EditText city;
    @BindView(R.id.input_salavat_num)
    EditText salavat_id;
    @BindView(R.id.chkbxGroupHead)
    CheckBox chkboxHeader;

    @BindView(R.id.male)RadioButton rbfemale;
    @BindView(R.id.fmale)RadioButton rbmale;
    @BindView(R.id.radioGroup)RadioGroup radioGroup;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_adv);
    }


}
