package com.tucan.olu;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class TermsActivity extends GenricActivity {
    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.title1)
    TextView title1;

    @BindView(R.id.title2)
    TextView title2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
//        String url = "http://ec2-13-58-57-186.us-east-2.compute.amazonaws.com/api/pages/?userID=1&pageID=21&language=en&lang=en";
        String url = "http://3.16.104.146/api/pages/?userID=1&pageID=21&language=en&lang=en";
        if (getIntent().getStringExtra("isTerms") != null) {
            if (getIntent().getStringExtra("isTerms").equals("21")) {
                title1.setText("TÉRMINOS");
                title2.setText("Y CONDICIONES");
//                url = "http://ec2-13-58-57-186.us-east-2.compute.amazonaws.com/api/pages/?userID=1&pageID=21&language=en&lang=es";
                url = "http://3.16.104.146/api/pages/?userID=1&pageID=21&language=en&lang=es";
            } else if (getIntent().getStringExtra("isTerms").equals("19")) {
                title1.setText("POLÍTICAS");
                title2.setText("DE PRIVACIDAD");
//                url = "http://ec2-13-58-57-186.us-east-2.compute.amazonaws.com/api/pages/?userID=1&pageID=19&language=en&lang=es";
                url = "http://3.16.104.146/api/pages/?userID=1&pageID=19&language=en&lang=es";
            } else if (getIntent().getStringExtra("isTerms").equals("17")) {
                title1.setText("¿Quiénes");
                title2.setText("somos?");
//                url = "http://ec2-13-58-57-186.us-east-2.compute.amazonaws.com/api/pages/?userID=1&pageID=17&language=en&lang=es";
                url = "http://3.16.104.146/api/pages/?userID=1&pageID=17&language=en&lang=es";
            } else {
                title1.setText("TÉRMINOS");
                title2.setText("Y CONDICIONES");
//                url = "http://ec2-13-58-57-186.us-east-2.compute.amazonaws.com/api/pages/?userID=1&pageID=28&language=en&lang=es";
                url = "http://3.16.104.146/api/pages/?userID=1&pageID=28&language=en&lang=es";
            }
        }
        //webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

}
