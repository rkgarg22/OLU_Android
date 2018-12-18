package com.tucan.olu.Firebase;

import android.app.Activity;
import android.os.Bundle;

import com.tucan.olu.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationPopupForStartActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_popup_for_start);
        ButterKnife.bind(this);
    }

    private void finishActivity() {
        NotificationPopupForStartActivity.this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.okBtn)
    protected void click() {
        NotificationPopupForStartActivity.this.finish();
    }
}

