package com.elisa.olu;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;

public class SelectUserTypeActivity extends GenricActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        ButterKnife.bind(SelectUserTypeActivity.this);

    }

    @OnClick(R.id.userTypeLayout)
    public void userTypeClick(View v) {
        Intent intent = new Intent(this, LoginScreenActivity.class);
        AppCommon.getInstance(this).setCurrentUser(2);
        startActivity(intent);
    }

    @OnClick(R.id.trainerTypeLayout)
    public void trainerTypeLayout(View v) {
        Intent intent = new Intent(this, LoginScreenActivity.class);
        AppCommon.getInstance(this).setCurrentUser(1);
        startActivity(intent);
    }
}
