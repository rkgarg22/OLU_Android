package com.elisa.olu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 7/25/2018.
 */

public class TermsOptionActivity extends GenricActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_options);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.termsLayout)
    public void termsLayout(View v) {
        startActivity(new Intent(this,TermsActivity.class).putExtra("isTerms","28"));
    }
    @OnClick(R.id.piliticasLayout)
    public void piliticasLayout(View v) {
        startActivity(new Intent(this,TermsActivity.class).putExtra("isTerms","19"));
    }

}
