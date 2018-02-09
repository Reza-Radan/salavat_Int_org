package salavatorg.salavaltintorg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by masoomeh on 2/8/18.
 */

public class CountriesLstActivity extends AppCompatActivity {

    @BindView(R.id.lstvCountry)
    ListView countries;
    String Tag = "CountriesLstActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countries_lst_page);

        setTitle(getString(R.string.country));

        ButterKnife.bind(this);

        countries.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,  getResources().getStringArray(R.array.countries_array)));
        countries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("country",parent.getItemAtPosition(position).toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
