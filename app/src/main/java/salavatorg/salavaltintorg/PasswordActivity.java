package salavatorg.salavaltintorg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by masoomeh on 12/14/17.
 */

public class PasswordActivity extends Activity{
    @BindView(R.id.input_password)
    EditText password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_verification_page);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btncheckPass)
    public void checkPass(){
        String passwordString = password.getText().toString();
        if(passwordString != null && !passwordString.isEmpty() &&
                passwordString.length() >1){
            Intent intent = new Intent(PasswordActivity.this , MainActivity.class);
            startActivity(intent);

        }else{

        }
    }
}
