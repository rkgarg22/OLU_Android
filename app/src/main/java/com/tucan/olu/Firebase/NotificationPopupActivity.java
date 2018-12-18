package com.tucan.olu.Firebase;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tucan.olu.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import ApiObject.TodayBookingObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPopupActivity extends Activity {

    TodayBookingObject todayBookingObject;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.iniciarBtn)
    Button iniciarBtn;

    @BindView(R.id.acceptBtn)
    Button acceptBtn;

    @BindView(R.id.declineBtn)
    Button declineBtn;

    @BindView(R.id.statusImageView)
    ImageView statusImageView;

    @BindView(R.id.statusText)
    TextView statusText;

    @BindView(R.id.noOfPerson)
    TextView noOfPerson;


    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_popup);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("pendingObject") != null) {
            todayBookingObject = new Gson().fromJson(getIntent().getStringExtra("pendingObject"), TodayBookingObject.class);
            String bookingFor = getIntent().getStringExtra("bookingFor");
            setupObject(bookingFor.trim());
        }
    }

    private void setupObject(String bookingFor) {
        name.setText(todayBookingObject.getFirstName() + " " + todayBookingObject.getLastName());
        category.setText(todayBookingObject.getCategory());
        time.setText(getTimeInForamt(todayBookingObject.getBookingStart()));
        date.setText(getDateInFormat(todayBookingObject.getDate()));
        location.setText(todayBookingObject.getBookingAddress());

        if (getIntent().getStringExtra("bookingType").equals("0")) {
            iniciarBtn.setVisibility(View.GONE);
            acceptBtn.setVisibility(View.VISIBLE);
            declineBtn.setVisibility(View.VISIBLE);
        } else {
            iniciarBtn.setVisibility(View.VISIBLE);
            acceptBtn.setVisibility(View.GONE);
            declineBtn.setVisibility(View.GONE);
        }

        switch (bookingFor) {
            case "1":
                statusImageView.setImageResource(R.drawable.individual);
                statusImageView.setVisibility(View.VISIBLE);
                noOfPerson.setVisibility(View.GONE);
                statusText.setText(getString(R.string.individual));
                break;
            case "2":
                noOfPerson.setText("2");
                noOfPerson.setVisibility(View.VISIBLE);
                statusImageView.setVisibility(View.GONE);
                statusText.setText(getString(R.string.grupal));
                break;
            case "3":
                statusImageView.setImageResource(R.drawable.group);
                statusImageView.setVisibility(View.VISIBLE);
                noOfPerson.setVisibility(View.GONE);
                statusText.setText(getString(R.string.group));
                break;
            case "4":
                noOfPerson.setText("3");
                noOfPerson.setVisibility(View.VISIBLE);
                statusImageView.setVisibility(View.GONE);
                statusText.setText(getString(R.string.grupal));
                break;
            case "5":
                noOfPerson.setText("4");
                noOfPerson.setVisibility(View.VISIBLE);
                statusImageView.setVisibility(View.GONE);
                statusText.setText(getString(R.string.grupal));
                break;
        }
    }

    public String getDateInFormat(String date) {
        String formatterString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date formatDate = formatter.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatDate);
            formatter = new SimpleDateFormat("dd/MMMM");
            formatterString = formatter.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatterString;
    }

    public String getTimeInForamt(String time) {
        String formatterTimeString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        try {
            Date formatDate = formatter.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatDate);
            formatter = new SimpleDateFormat("hh:mm a", Locale.US);
            formatterTimeString = formatter.format(formatDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatterTimeString;
    }

    private void finishActivity() {
        NotificationPopupActivity.this.finish();
    }

    public void showDialog(Activity mactivity, String body, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(body);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                statusChangeBooking(type);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void statusChangeBooking(final String status) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(NotificationPopupActivity.this).isConnectingToInternet(NotificationPopupActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.bookingConfirm(AppCommon.getInstance(this).getUserID(), todayBookingObject.getBookingId(), status, "0");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(NotificationPopupActivity.this).clearNonTouchableFlags(NotificationPopupActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            if (status.equals("4")) {
                                AppCommon.getInstance(NotificationPopupActivity.this).setStartBookingID(todayBookingObject.getBookingId());
                            }
                            if(status.equals("3")){
                                AppCommon.getInstance(NotificationPopupActivity.this).setAlaramForSession(todayBookingObject);
                            }
                            AppCommon.getInstance(NotificationPopupActivity.this).setUpdate("1");
                            NotificationPopupActivity.this.finish();
                        } else {
                            AppCommon.getInstance(NotificationPopupActivity.this).showDialog(NotificationPopupActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(NotificationPopupActivity.this).showDialog(NotificationPopupActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(NotificationPopupActivity.this).clearNonTouchableFlags(NotificationPopupActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(NotificationPopupActivity.this).showDialog(NotificationPopupActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(NotificationPopupActivity.this).clearNonTouchableFlags(NotificationPopupActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.Popup_notification)
    protected void click() {
        NotificationPopupActivity.this.finish();
    }

    @OnClick(R.id.acceptBtn)
    protected void acceptBtn() {
        showDialog(this, getString(R.string.accetJob), "3");
    }

    @OnClick(R.id.declineBtn)
    protected void declineBtn() {
        showDialog(this, getString(R.string.declineJob), "2");
    }

    @OnClick(R.id.iniciarBtn)
    protected void inciar() {
        showDialog(this, getString(R.string.startSeassion), "4");
    }


    @OnClick(R.id.mapaBtn)
    public void mapBtnClick(View v) {
        if ((todayBookingObject.getBookingLatitude() != null && !todayBookingObject.getBookingLatitude().equals("")) &&
                (todayBookingObject.getBookingLongitude() != null && !todayBookingObject.getBookingLongitude().equals(""))) {
            CharSequence colors[] = new CharSequence[]{"WAZE", "GOOGLE MAPS"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccionar opción");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        String uri = "geo:" + todayBookingObject.getBookingLatitude() + "," + todayBookingObject.getBookingLongitude();
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(uri)));
                    } else {
                        String url = "http://maps.google.com/maps?daddr=" + todayBookingObject.getBookingLatitude() + "," + todayBookingObject.getBookingLongitude();
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(intent);
                    }
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

}


