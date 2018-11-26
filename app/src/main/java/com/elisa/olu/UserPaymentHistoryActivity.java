package com.elisa.olu;

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

import java.util.ArrayList;
import java.util.List;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.TodayBookingResponse;
import APIResponse.UserPaymentHistoryResponse;
import Adapter.PaymentHistoryAdapter;
import Adapter.SesionerAdapter;
import Adapter.UserPaymentHistoryAdapter;
import ApiObject.PaymentHistoryObject;
import ApiObject.TodayBookingObject;
import ApiObject.UserPaymentHistoryObject;
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

public class UserPaymentHistoryActivity extends GenricActivity {

    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @BindView(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;


    UserPaymentHistoryAdapter paymentHistoryAdapter;
    ArrayList<UserPaymentHistoryObject> paymentHistoryObjectArrayList = new ArrayList<UserPaymentHistoryObject>();
    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment_history);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(layoutManager);

        paymentHistoryAdapter = new UserPaymentHistoryAdapter(this, paymentHistoryObjectArrayList);
        historyRecyclerView.setAdapter(paymentHistoryAdapter);
        getPaymentHistory();
        progressBar.setVisibility(View.VISIBLE);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPaymentHistory();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getPaymentHistory() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(UserPaymentHistoryActivity.this).isConnectingToInternet(UserPaymentHistoryActivity.this)) {

            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.getPaymentHistoryForUser(AppCommon.getInstance(this).getUserID(), "DESC");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(UserPaymentHistoryActivity.this).clearNonTouchableFlags(UserPaymentHistoryActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                        UserPaymentHistoryResponse userPaymentHistoryResponse = (UserPaymentHistoryResponse) response.body();
                        if (userPaymentHistoryResponse.getSuccess() == 1) {
                            noDataFound.setVisibility(View.GONE);
                            paymentHistoryObjectArrayList = userPaymentHistoryResponse.getPaymentHistoryObjectList();
                            if(paymentHistoryObjectArrayList.size()>0) {
                                paymentHistoryAdapter.setData(paymentHistoryObjectArrayList);
                            }else{
                                noDataFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        AppCommon.getInstance(UserPaymentHistoryActivity.this).showDialog(UserPaymentHistoryActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(UserPaymentHistoryActivity.this).clearNonTouchableFlags(UserPaymentHistoryActivity.this);
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    AppCommon.getInstance(UserPaymentHistoryActivity.this).setUpdate("0");
                    AppCommon.getInstance(UserPaymentHistoryActivity.this).showDialog(UserPaymentHistoryActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(UserPaymentHistoryActivity.this).clearNonTouchableFlags(UserPaymentHistoryActivity.this);
            swipeContainer.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(UserPaymentHistoryActivity.this).setUpdate("0");
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

}
