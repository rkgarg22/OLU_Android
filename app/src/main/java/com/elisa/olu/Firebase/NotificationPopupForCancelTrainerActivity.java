package com.elisa.olu.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.elisa.olu.R;
import com.elisa.olu.SearchActivity;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import ApiObject.TodayBookingObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;

public class NotificationPopupForCancelTrainerActivity extends Activity {

    @BindView(R.id.descTextView)
    TextView descTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_popup_for_cancel_trainer);
        ButterKnife.bind(this);
        AppCommon.getInstance(this).unRegisterAlarm(Integer.parseInt(getIntent().getStringExtra("bookingID")));
        descTextView.setText("Lo sentimos, " + getIntent().getStringExtra("firstName") + " " + getIntent().getStringExtra("lastName") + " ha tenido que cancelar la sesión.");
    }

    private void finishActivity() {
        NotificationPopupForCancelTrainerActivity.this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.okBtn)
    protected void click() {
        NotificationPopupForCancelTrainerActivity.this.finish();
    }
}

