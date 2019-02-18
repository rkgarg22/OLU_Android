package com.tucan.olu.Firebase;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.tucan.olu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationPopupForAcceptActivity extends Activity {


    @BindView(R.id.titleTextView)
    TextView titleTextView;

    @BindView(R.id.descriptionTextView)
    TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_notification_popup_for_accept);
        ButterKnife.bind(this);

        String bookingCreatedTime = getIntent().getStringExtra("bookingCreated");
        String bookingStartTime = getIntent().getStringExtra("bookingStartTime");
        String bookingDate = getIntent().getStringExtra("bookingDate");
        String bookingAcceptedDate = getIntent().getStringExtra("bookingUpdated");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateInString = bookingDate + " " + bookingStartTime;
        Date bookingDateTime = null;
        Date bookingCreatedDateObj = null;
        Date bookingAcceptedTimeObj = null;
        try {
            bookingDateTime = sdf.parse(dateInString);
            bookingCreatedDateObj = sdf.parse(bookingCreatedTime);
            bookingAcceptedTimeObj = sdf.parse(bookingAcceptedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long different = bookingDateTime.getTime() - bookingCreatedDateObj.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = different / hoursInMilli;

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm a");
        String accpetedTime = sdf1.format(bookingAcceptedTimeObj);

        if (elapsedHours < 2) {
            titleTextView.setText(getResources().getString(R.string.accepted_title));
            descriptionTextView.setText("Ten en cuenta que solo durante los próximos 15 minutos contados a partir de las " + accpetedTime + " podrás cancelar sin cobro. A partir de ese momento, si cancelas te será cobrada la totalidad de la actividad.");
        } else {
            titleTextView.setText(getResources().getString(R.string.accepted_title));
            descriptionTextView.setText(getResources().getString(R.string.accepted_desc));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.okBtn)
    protected void click() {
        NotificationPopupForAcceptActivity.this.finish();
    }
}

