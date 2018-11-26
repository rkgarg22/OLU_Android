package com.elisa.olu.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.elisa.olu.R;
import com.elisa.olu.SearchActivity;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import ApiObject.TodayBookingObject;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationPopupForCancelActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_popup_for_cancel);
        ButterKnife.bind(this);

    }

    private void finishActivity() {
        NotificationPopupForCancelActivity.this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.okBtn)
    protected void click() {
        String bookingDate = getIntent().getStringExtra("bookingDate");
        String time = getIntent().getStringExtra("time");
        String endDatetime = bookingDate + " " + time;
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "";
        String timeStr = "";
        try {
            Date currentDate = sdfDate.parse(endDatetime);
            dateStr = (String) DateFormat.format("dd-MM-yyyy", currentDate);
            timeStr = (String) DateFormat.format("HH:mm", currentDate);
        } catch (Exception e) {

        }

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("categoryId", getIntent().getStringExtra("categoryId"));
        intent.putExtra("date", "");
        intent.putExtra("bookingDate", dateStr);
        intent.putExtra("month", "");
        intent.putExtra("time", timeStr);
        intent.putExtra("bookingType", getIntent().getStringExtra("bookingType"));

        intent.putExtra("latitude", getIntent().getStringExtra("latitude"));
        intent.putExtra("longitude", getIntent().getStringExtra("longitude"));
        intent.putExtra("address", getIntent().getStringExtra("address"));
        intent.putExtra("isComingFromReservar", true);
        startActivity(intent);
        NotificationPopupForCancelActivity.this.finish();
    }
}

