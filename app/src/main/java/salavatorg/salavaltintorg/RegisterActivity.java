package salavatorg.salavaltintorg;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by masoomeh on 12/14/17.
 */

public class RegisterActivity extends AppCompatActivity{
    @BindView(R.id.input_name)
    EditText name;
    @BindView(R.id.input_family)
    EditText family;
    @BindView(R.id.input_email)
    EditText email;
    @BindView(R.id.male)RadioButton rbfemale;
    @BindView(R.id.fmale)RadioButton rbmale;
    @BindView(R.id.radioGroup)RadioGroup  radioGroup;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        ButterKnife.bind(this);
    }

    private void validation() {
        String nameString , familyString , emailString;
        nameString = name.getText().toString();
        familyString = family.getText().toString();
        emailString = email.getText().toString();



        if(nameString.length()<=2 || nameString.matches("[0-9_-]")){
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
//            snackbar.show();
            name.setError(getString(R.string.less_char_name));

        }else if(familyString.length()<=3 || familyString.matches("[0-9_-]")){
            family.setError(getString(R.string.less_char_family));
        }else if(!emailValidation(emailString)){
            email.setError(getString(R.string.email_validation));
        }else if(!rbfemale.isChecked() && !rbmale.isChecked()){

            Snackbar.
                    make(coordinatorLayout, getString(R.string.select_gender), Snackbar.LENGTH_LONG).show();

        }
    }


    private boolean emailValidation(String emailString) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(emailString).matches();
    }


    @OnClick(R.id.RegisterBtn)
    public void registerPage(){
        validation();
    }
}
