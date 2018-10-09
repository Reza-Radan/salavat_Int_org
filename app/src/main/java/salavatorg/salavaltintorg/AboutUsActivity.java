package salavatorg.salavaltintorg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by masoomeh on 5/12/18.
 */

public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.bind(this);

        if(getIntent().getExtras()!=null){
            url = getIntent().getExtras().getString("url");
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }
}
