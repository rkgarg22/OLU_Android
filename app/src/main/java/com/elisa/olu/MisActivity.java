package com.elisa.olu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MisActivity extends GenricActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick({R.id.paymentHistoryBtn, R.id.paymentHistoryImage})
    public void paymentHistoryClick(View v) {
        Intent pymentHistoryIntent = new Intent(this, UserPaymentHistoryActivity.class);
        startActivity(pymentHistoryIntent);
    }

    @OnClick({R.id.onDemand, R.id.matadosDePagos})
    public void matadosDePagos(View v) {
        Intent pymentHistoryIntent = new Intent(this, MetadosPagosActivity.class);
        startActivity(pymentHistoryIntent);
    }
}
