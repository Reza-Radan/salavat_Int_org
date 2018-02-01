package salavatorg.salavaltintorg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by masoomeh on 12/14/17.
 */

public class LoginActivity extends Activity {

    @BindView(R.id.spinner_prePhone)
    MaterialSpinner prePhoneSpinner;

    @BindView(R.id.PhoneNumber)
    EditText phoneNumberetxt;

    @BindView(R.id.spinnerSelectLan)
    MaterialSpinner selectionLanSpinner;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        ButterKnife.bind(this);

        prePhoneSpinner.setItems(getResources().getStringArray(R.array.CountryCodes));
        prePhoneSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        selectionLanSpinner.setItems(getResources().getStringArray(R.array.languages));
        selectionLanSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.Next)
    public void next(){

        String prePhone, selectionLan , phoneNumber;
        prePhone     = prePhoneSpinner.getText().toString();
        selectionLan = selectionLanSpinner.getText().toString();
        phoneNumber  = phoneNumberetxt.getText().toString();
        if(prePhone != null && !prePhone.isEmpty() && prePhone.length() >0){

            if(selectionLan != null && !selectionLan.isEmpty() && selectionLan.length() >0){

                if(phoneNumber != null && !phoneNumber.isEmpty() && phoneNumber.length() >0){
                    if(checkPhoneNumber(phoneNumber)){
                        //send number to server and return data
                        Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
                        intent.putExtra("phone_num",phoneNumber);
                        startActivity(intent);
                    }

                }else{
                    phoneNumberetxt.setError(getString(R.string.insert_your_phone));
                    Snackbar.
                            make(coordinatorLayout, getString(R.string.insert_your_phone), Snackbar.LENGTH_LONG).show();

                }

            }else{
                selectionLanSpinner.setError(getString(R.string.select_lan));
            }

        }else{
            prePhoneSpinner.setError(getString(R.string.select_prePhone));
            Snackbar.
                    make(coordinatorLayout, getString(R.string.select_prePhone), Snackbar.LENGTH_LONG).show();
        }






    }

    /**
     * should check data phone nuber is correct
     * @param phoneNumber
     * @return
     */
    private boolean checkPhoneNumber(String phoneNumber) {
        return true;
    }


}
