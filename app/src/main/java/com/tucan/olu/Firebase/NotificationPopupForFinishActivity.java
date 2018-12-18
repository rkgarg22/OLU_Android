package com.tucan.olu.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tucan.olu.R;
import com.tucan.olu.ReviewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationPopupForFinishActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_popup_for_complete);
        ButterKnife.bind(this);
    }

    private void finishActivity() {
        NotificationPopupForFinishActivity.this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.okBtn)
    protected void click() {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("bookingID", getIntent().getStringExtra("bookingID"));
        intent.putExtra("userName", getIntent().getStringExtra("userName"));
        intent.putExtra("category", getIntent().getStringExtra("category"));
        startActivity(intent);
        finish();
    }
}

