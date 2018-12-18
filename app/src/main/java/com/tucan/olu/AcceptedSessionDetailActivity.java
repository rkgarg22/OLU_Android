package com.tucan.olu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
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
import CustomControl.AvenirNextCondensedMediumTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedSessionDetailActivity extends GenricActivity {


    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.monthTextView)
    TextView monthTextView;

    @BindView(R.id.time)
    TextView bookingTime;

    @BindView(R.id.endTime)
    TextView bookingEndTime;

    @BindView(R.id.date)
    TextView bookingDateTextView;

    @BindView(R.id.categoryImage)
    ImageView categoryImage;

    @BindView(R.id.categoryName)
    TextView categoryName;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.dayTextView)
    AvenirNextCondensedMediumTextView dayTextView;

    @BindView(R.id.userImage)
    SimpleDraweeView userImage;

    @BindView(R.id.iniciarBtn)
    Button iniciarBtn;

    @BindView(R.id.finishBtn)
    Button finishBtn;

    Call call;
    String selectedPhoneNumber = "";

    TodayBookingObject todayBookingObj;
    private static final int REQUESTCODE_CALL_PERMISSION = 130;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accepted_session_detail);
        ButterKnife.bind(this);
        todayBookingObj = new Gson().fromJson(getIntent().getStringExtra("userObject"), TodayBookingObject.class);

        String endDateFormat = todayBookingObj.getDate() + " " + todayBookingObj.getBookingStart();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date currentDate = sdfDate.parse(endDateFormat);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            bookingDateTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));
            //setUpMonth(calendar.get(Calendar.MONTH) + 1);
            String dayOfTheWeek = (String) DateFormat.format("EEEE", currentDate);
            dayOfTheWeek = dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1);

            String month = (String) DateFormat.format("MMM", currentDate);
            month = month.substring(0, 1).toUpperCase() + month.substring(1);

            monthTextView.setText(month);
            dayTextView.setText(dayOfTheWeek);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bookingEndTime.setText(getTimeInFormat(todayBookingObj.getBookingEnd()));
        bookingTime.setText(getTimeInFormat(todayBookingObj.getBookingStart()));
        setupTrainerProfile();
        setupCategories(todayBookingObj.getCategoryID());
        userImage.setImageURI(Uri.parse(todayBookingObj.getUserImageUrl()));

        if (AppCommon.getInstance(this).getBookingID().equals(todayBookingObj.getBookingId())) {
            iniciarBtn.setVisibility(View.GONE);
        } else {
            iniciarBtn.setVisibility(View.VISIBLE);
        }

        bookingDateTextView.setPadding(0, 0, 0, 0);
        bookingDateTextView.setIncludeFontPadding(false);
    }

    public String getTimeInFormat(String time) {
        String formattedTime = "";
        String myFormat = "hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            Date currentDate = sdf.parse(time);

            sdf = new SimpleDateFormat("hh:mm", Locale.US);
            formattedTime = sdf.format(currentDate);
        } catch (Exception e) {

        }
        return formattedTime;
    }

    private void setupTrainerProfile() {
        name.setText(todayBookingObj.getFirstName() + " " + todayBookingObj.getLastName());
    }

    private void setupCategories(String categoryId) {
        switch (categoryId) {
            case "1":
                categoryImage.setImageResource(R.drawable.kick_boxing);
                categoryName.setText(getString(R.string.kickboxing));
                break;
            case "2":
                categoryImage.setImageResource(R.drawable.cardio_crossfit);
                categoryName.setText(getString(R.string.EntrenamientoFuncional));
                break;
            case "3":
                categoryImage.setImageResource(R.drawable.stretching);
                categoryName.setText(getString(R.string.stretching));
                break;
            case "4":
                categoryImage.setImageResource(R.drawable.yoga);
                categoryName.setText(getString(R.string.yoga));
                break;
            case "5":
                categoryImage.setImageResource(R.drawable.pilates);
                categoryName.setText(getString(R.string.pilates));
                break;
            case "8":
                categoryImage.setImageResource(R.drawable.masajes_deportivos);
                categoryName.setText(getString(R.string.masajes));
                break;
            case "9":
                categoryImage.setImageResource(R.drawable.fisioterapia);
                categoryName.setText(getString(R.string.fisioterapia));
                break;
            case "11":
                categoryImage.setImageResource(R.drawable.danza_fit);
                categoryName.setText(getString(R.string.danza_fit));
                break;
            case "10":
                categoryImage.setImageResource(R.drawable.gimnasia);
                categoryName.setText(getString(R.string.Gimnasia));
                break;
        }
    }


    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.finishBtn)
    public void finishBtnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getString(R.string.finishSeassionConfirmation));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                statusChangeBooking("1");
            }
        });
        builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @OnClick(R.id.iniciarBtn)
    public void startSession(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getString(R.string.startSeassionConfirmation));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                statusChangeBooking("4");
            }
        });
        builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void statusChangeBooking(final String status) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(AcceptedSessionDetailActivity.this).isConnectingToInternet(AcceptedSessionDetailActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.bookingStateChange(AppCommon.getInstance(this).getUserID(), todayBookingObj.getBookingId(), status);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(AcceptedSessionDetailActivity.this).clearNonTouchableFlags(AcceptedSessionDetailActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            if (status.equals("4")) {
                                AppCommon.getInstance(AcceptedSessionDetailActivity.this).setIsAvailable(0);
                                AppCommon.getInstance(AcceptedSessionDetailActivity.this).setStartBookingID(todayBookingObj.getBookingId());
                                AppCommon.getInstance(AcceptedSessionDetailActivity.this).setStartTrainingObject(getIntent().getStringExtra("userObject"));
                                iniciarBtn.setVisibility(View.GONE);
                                AppCommon.getInstance(AcceptedSessionDetailActivity.this).unRegisterAlarm(Integer.parseInt(todayBookingObj.getBookingId()));
                            } else {
                                AppCommon.getInstance(AcceptedSessionDetailActivity.this).setStartBookingID("");
                                AppCommon.getInstance(AcceptedSessionDetailActivity.this).setIsAvailable(1);
                                Intent intent = new Intent(AcceptedSessionDetailActivity.this, ReviewActivity.class);
                                intent.putExtra("bookingID", todayBookingObj.getBookingId());
                                intent.putExtra("userName", todayBookingObj.getFirstName() + " " + todayBookingObj.getLastName());
                                intent.putExtra("category", todayBookingObj.getCategory());
                                startActivity(intent);
                                finish();
                            }
                            AppCommon.getInstance(AcceptedSessionDetailActivity.this).setUpdate("1");

                        } else {
                            AppCommon.getInstance(AcceptedSessionDetailActivity.this).showDialog(AcceptedSessionDetailActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(AcceptedSessionDetailActivity.this).showDialog(AcceptedSessionDetailActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(AcceptedSessionDetailActivity.this).clearNonTouchableFlags(AcceptedSessionDetailActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(AcceptedSessionDetailActivity.this).showDialog(AcceptedSessionDetailActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(AcceptedSessionDetailActivity.this).clearNonTouchableFlags(AcceptedSessionDetailActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @OnClick(R.id.directionLayout)
    public void mapBtnClick(View v) {
        if ((todayBookingObj.getBookingLatitude() != null && !todayBookingObj.getBookingLatitude().equals("")) &&
                (todayBookingObj.getBookingLongitude() != null && !todayBookingObj.getBookingLongitude().equals(""))) {
            CharSequence colors[] = new CharSequence[]{"WAZE", "GOOGLE MAPS"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccionar opción");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        String uri = "geo:" + todayBookingObj.getBookingLatitude() + "," + todayBookingObj.getBookingLongitude();
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(uri)));
                    } else {
                        String url = "http://maps.google.com/maps?daddr=" + todayBookingObj.getBookingLatitude() + "," + todayBookingObj.getBookingLongitude();
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

    @OnClick(R.id.phoneLayout)
    public void phoneLayoutClick(View v) {
        if (todayBookingObj.getPhoneNumber() != null && !todayBookingObj.getPhoneNumber().equals("")) {
            requestPermissionForCall(todayBookingObj.getPhoneNumber());
        }
    }

    @OnClick(R.id.chatLayout)
    public void chatLayoutClick(View v) {
        Intent intent = new Intent(this, ChatScreenActivity.class);
        intent.putExtra("AnotherUserID", todayBookingObj.getUserID());
        intent.putExtra("name", todayBookingObj.getFirstName() + " " + todayBookingObj.getLastName());
        startActivity(intent);
    }

    @OnClick(R.id.crossLayout)
    public void crossLayoutClick(View v) {
        showDialog(this, getString(R.string.declineJob), "0");
    }

    public void requestPermissionForCall(String phoneNumber) {
        selectedPhoneNumber = phoneNumber;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    REQUESTCODE_CALL_PERMISSION);
        } else {
            fireCallIntent(phoneNumber);
        }
    }

    private void fireCallIntent(String mDealPhoneNo) {
        if (mDealPhoneNo != null && !mDealPhoneNo.equals("")) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mDealPhoneNo));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE_CALL_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fireCallIntent(selectedPhoneNumber);
                }
        }
    }

    public void showDialog(Activity mactivity, String body, final String isPaymentRequire) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(body);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                // decline booking
                cancelSessionAPI("2", isPaymentRequire);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void cancelSessionAPI(String status, String isPaymentRequire) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(AcceptedSessionDetailActivity.this).isConnectingToInternet(AcceptedSessionDetailActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.bookingConfirm(AppCommon.getInstance(this).getUserID(), todayBookingObj.getBookingId(), status, isPaymentRequire);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(AcceptedSessionDetailActivity.this).clearNonTouchableFlags(AcceptedSessionDetailActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            AcceptedSessionDetailActivity.this.finish();
                            AppCommon.getInstance(AcceptedSessionDetailActivity.this).unRegisterAlarm(Integer.parseInt(todayBookingObj.getBookingId()));
                        } else {
                            AppCommon.getInstance(AcceptedSessionDetailActivity.this).showDialog(AcceptedSessionDetailActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(AcceptedSessionDetailActivity.this).showDialog(AcceptedSessionDetailActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(AcceptedSessionDetailActivity.this).clearNonTouchableFlags(AcceptedSessionDetailActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(AcceptedSessionDetailActivity.this).showDialog(AcceptedSessionDetailActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(AcceptedSessionDetailActivity.this).clearNonTouchableFlags(AcceptedSessionDetailActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }
}
