package com.tucan.olu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.TodayBookingResponse;
import Adapter.SesionerAdapter;
import ApiObject.TodayBookingObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class HistoricalActivity extends GenricActivity {

    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @BindView(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.pending)
    TextView pending;

    @BindView(R.id.completed)
    TextView completed;

    @BindView(R.id.accepted)
    TextView accepted;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    List<TodayBookingObject> todayBookingObjectList = new ArrayList<>();

    SesionerAdapter sesionerAdapter;

    Call call;

    String bookingType = "1";
    private static final int REQUESTCODE_CALL_PERMISSION = 130;

    String selectedPhoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(layoutManager);
        if (AppCommon.getInstance(this).getCurrentUser() == 2) {
            accepted.setVisibility(View.GONE);
        } else {
            accepted.setVisibility(View.VISIBLE);
        }
        setPending();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (bookingType.equals("0")) {
                    getBookingHistory("0");
                } else if (bookingType.equals("3")) {
                    getBookingHistory("3");
                } else {
                    getBookingHistory("1");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppCommon.getInstance(this).getUpdate().equals("1")) {
            todayBookingObjectList.clear();
            if (bookingType.equals("0")) {
                getBookingHistory("0");
            } else if (bookingType.equals("3")) {
                getBookingHistory("3");
            } else {
                getBookingHistory("1");
            }
        }
    }

    @OnClick(R.id.pending)
    void setPending() {
        if (AppCommon.getInstance(this).getCurrentUser() == 2) {
            bookingType = "3";
            getBookingHistory("3");
        } else {
            bookingType = "0";
            getBookingHistory("0");
        }
        sesionerAdapter = new SesionerAdapter(this, todayBookingObjectList, "0", false);
        historyRecyclerView.setAdapter(sesionerAdapter);
        todayBookingObjectList.clear();

        pending.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        accepted.setTextColor(ContextCompat.getColor(this, R.color.grey));
        completed.setTextColor(ContextCompat.getColor(this, R.color.grey));
    }

    @OnClick(R.id.accepted)
    void setAccepted() {
        bookingType = "3";
        sesionerAdapter = new SesionerAdapter(this, todayBookingObjectList, "3", false);
        historyRecyclerView.setAdapter(sesionerAdapter);
        todayBookingObjectList.clear();
        getBookingHistory("3");
        pending.setTextColor(ContextCompat.getColor(this, R.color.grey));
        completed.setTextColor(ContextCompat.getColor(this, R.color.grey));
        accepted.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @OnClick(R.id.completed)
    void setCompleted() {
        bookingType = "1";
        sesionerAdapter = new SesionerAdapter(this, todayBookingObjectList, "1", false);
        historyRecyclerView.setAdapter(sesionerAdapter);
        todayBookingObjectList.clear();
        getBookingHistory("1");
        pending.setTextColor(ContextCompat.getColor(this, R.color.grey));
        accepted.setTextColor(ContextCompat.getColor(this, R.color.grey));
        completed.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }


    public void getBookingHistory(final String status) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(HistoricalActivity.this).isConnectingToInternet(HistoricalActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.bookingHistory(AppCommon.getInstance(this).getUserID(), status, "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(HistoricalActivity.this).clearNonTouchableFlags(HistoricalActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                        TodayBookingResponse todayBookingResponse = (TodayBookingResponse) response.body();
                        if (todayBookingResponse.getSuccess() == 1) {
                            todayBookingObjectList = todayBookingResponse.getTodayBookingObjectList();
                            sesionerAdapter = new SesionerAdapter(HistoricalActivity.this, todayBookingObjectList, status, false);
                            historyRecyclerView.setAdapter(sesionerAdapter);
//                            checkingTotalAmount(todayBookingResponse.);
                            noDataFound.setVisibility(View.GONE);
                            AppCommon.getInstance(HistoricalActivity.this).setUpdate("0");
                        } else {
                            todayBookingObjectList.clear();
                            noDataFound.setVisibility(View.VISIBLE);
                            if (bookingType.equals("3")) {
                                noDataFound.setText("No tienes reservas programadas!");
                            } else {
                                noDataFound.setText("¡Aún no tienes OLU actividades!");
                            }
                            sesionerAdapter.notifyDataSetChanged();
                        }
                    } else {
                        AppCommon.getInstance(HistoricalActivity.this).showDialog(HistoricalActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(HistoricalActivity.this).clearNonTouchableFlags(HistoricalActivity.this);
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    AppCommon.getInstance(HistoricalActivity.this).setUpdate("0");
                    AppCommon.getInstance(HistoricalActivity.this).showDialog(HistoricalActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(HistoricalActivity.this).clearNonTouchableFlags(HistoricalActivity.this);
            swipeContainer.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(HistoricalActivity.this).setUpdate("0");
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    public void setCanclerBooking(int adapterPosition) {
        if (AppCommon.getInstance(this).getCurrentUser() == 2) {
            checkCancelOptions(adapterPosition);
        } else {
            showDialog(this, getString(R.string.declineJob), adapterPosition, "0");
        }
    }

    private void checkCancelOptions(int adapterPosition) {
        Calendar cal = Calendar.getInstance();
        long current = cal.getTimeInMillis();
        long bookingCreated = 0;
        long bookingAccepted = 0;
        long bookingDateTime = 0;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // for booking accepted, created
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// for booking date
        String bookCre = todayBookingObjectList.get(adapterPosition).getBookingCreated();
        String bookAcc = todayBookingObjectList.get(adapterPosition).getBookingAccepted();
        String bookDate = todayBookingObjectList.get(adapterPosition).getDate() + " " + todayBookingObjectList.get(adapterPosition).getBookingStart();
        try {
            bookingCreated = sdf1.parse(bookCre).getTime();
            bookingAccepted = sdf1.parse(bookAcc).getTime();
            bookingDateTime = sdf2.parse(bookDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long different = bookingDateTime - bookingCreated;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = different / hoursInMilli;

        if (elapsedHours > 2) { // greater than 2 hous
//            if (bookingDateTime - bookingCreated < 2700000) {// request done with in 45 min
//                showDialog(this, "¿Estas seguro que deseas cancelar? Al confirmar, la sesión te será cobrada en su totalidad.", adapterPosition, true);
//            } else
            if (bookingDateTime - current > 4500000) {
                showDialog(this, " ¿Estas seguro que deseas cancelar? ", adapterPosition, "0");
            } else {
                showDialog(this, "¿Estas seguro que deseas cancelar ? Recuerda que a partir te será cobrada la totalidad.", adapterPosition, "1");
            }

        } else { //  less than 2 hours
//            if (bookingDateTime - bookingCreated < 2700000) {// request done with in 45 min
//                showDialog(this, "¿Estas seguro que deseas cancelar? Al confirmar, la sesión te será cobrada en su totalidad.", adapterPosition, true);
//            }
            if (bookingAccepted + 900000 < current) {// greater than 15 min
                showDialog(this, "¿Estas seguro que deseas cancelar? Al confirmar, la sesión te será cobrada en su totalidad.", adapterPosition, "1");
            } else { // less than 15 min
                showDialog(this, "¿Estas seguro que deseas cancelar? ", adapterPosition, "0");
            }
        }
    }


    public void showDialog(Activity mactivity, String body, final int adapterPosition, final String isPaymentRequire) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(body);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                // decline booking
                statusChangeBooking("2", adapterPosition, isPaymentRequire);
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

    private void statusChangeBooking(String status, final int adapterPosition, String isPaymentRequire) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(HistoricalActivity.this).isConnectingToInternet(HistoricalActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.bookingConfirm(AppCommon.getInstance(this).getUserID(), todayBookingObjectList.get(adapterPosition).getBookingId(), status, isPaymentRequire);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(HistoricalActivity.this).clearNonTouchableFlags(HistoricalActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            if (bookingType.equals("0")) {
                                getBookingHistory("0");
                            } else if (bookingType.equals("3")) {
                                getBookingHistory("3");
                            } else {
                                getBookingHistory("1");
                            }
                            AppCommon.getInstance(HistoricalActivity.this).unRegisterAlarm(Integer.parseInt(todayBookingObjectList.get(adapterPosition).getBookingId()));
                        } else {
                            AppCommon.getInstance(HistoricalActivity.this).showDialog(HistoricalActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(HistoricalActivity.this).showDialog(HistoricalActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(HistoricalActivity.this).clearNonTouchableFlags(HistoricalActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(HistoricalActivity.this).showDialog(HistoricalActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(HistoricalActivity.this).clearNonTouchableFlags(HistoricalActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
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

    public void openChatScreen(int adapterPosition) {
        TodayBookingObject todayBookingObject = todayBookingObjectList.get(adapterPosition);
        Intent intent = new Intent(this, ChatScreenActivity.class);
        intent.putExtra("AnotherUserID", todayBookingObject.getUserID());
        intent.putExtra("name", todayBookingObject.getFirstName() + " " + todayBookingObject.getLastName());
        startActivity(intent);
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

    public void directionClick(int adapterPos) {
        final TodayBookingObject todayBookingObj = todayBookingObjectList.get(adapterPos);
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
}
