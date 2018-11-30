package com.elisa.olu;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.PaymentHistoryResponse;
import APIResponse.TodayBookingResponse;
import Adapter.PaymentHistoryAdapter;
import Adapter.SesionerAdapter;
import ApiObject.PaymentHistoryObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainerPaymentHistoryActivity extends GenricActivity {

    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @BindView(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.pending)
    TextView pending;

    @BindView(R.id.completed)
    TextView completed;

    @BindView(R.id.totalLayout)
    LinearLayout totalLayout;

    @BindView(R.id.totalPayment)
    TextView totalPayment;

    PaymentHistoryAdapter paymentHistoryAdapter;
    ArrayList<PaymentHistoryObject> paymentHistoryObjectArrayList = new ArrayList<PaymentHistoryObject>();
    Call call;
    boolean isOnPendingTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_payment_history);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(layoutManager);
        paymentHistoryAdapter = new PaymentHistoryAdapter(this, paymentHistoryObjectArrayList);
        historyRecyclerView.setAdapter(paymentHistoryAdapter);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isOnPendingTab) {
                    getPaymentHistory("0");
                } else {
                    getPaymentHistory("1");
                }
            }
        });
        getPaymentHistory("0");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.pending)
    void setPending() {
        isOnPendingTab = true;
        getPaymentHistory("0");
        pending.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        completed.setTextColor(ContextCompat.getColor(this, R.color.grey));
        paymentHistoryObjectArrayList.clear();
        paymentHistoryAdapter.setData(paymentHistoryObjectArrayList);
    }

    @OnClick(R.id.completed)
    void setCompleted() {
        isOnPendingTab = false;
        getPaymentHistory("1");
        pending.setTextColor(ContextCompat.getColor(this, R.color.grey));
        completed.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        paymentHistoryObjectArrayList.clear();
        paymentHistoryAdapter.setData(paymentHistoryObjectArrayList);

    }

    public void getPaymentHistory(String isPaid) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerPaymentHistoryActivity.this).isConnectingToInternet(TrainerPaymentHistoryActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.getPaymentHistory(AppCommon.getInstance(this).getUserID(), "DESC", isPaid);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerPaymentHistoryActivity.this).clearNonTouchableFlags(TrainerPaymentHistoryActivity.this);
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    PaymentHistoryResponse paymentHistoryResponse = (PaymentHistoryResponse) response.body();
                    if (paymentHistoryResponse.getSuccess() == 1) {
                        noDataFound.setVisibility(View.GONE);
                        totalLayout.setVisibility(View.VISIBLE);
                        paymentHistoryObjectArrayList = paymentHistoryResponse.getPaymentHistoryObjectList();
                        paymentHistoryAdapter.setData(paymentHistoryObjectArrayList);
                        totalAmount(paymentHistoryObjectArrayList);
                    } else {
                        totalLayout.setVisibility(View.GONE);
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerPaymentHistoryActivity.this).clearNonTouchableFlags(TrainerPaymentHistoryActivity.this);
                    noDataFound.setVisibility(View.VISIBLE);
                    totalLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    AppCommon.getInstance(TrainerPaymentHistoryActivity.this).setUpdate("0");
                    AppCommon.getInstance(TrainerPaymentHistoryActivity.this).showDialog(TrainerPaymentHistoryActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(TrainerPaymentHistoryActivity.this).clearNonTouchableFlags(TrainerPaymentHistoryActivity.this);
            noDataFound.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.GONE);
            swipeContainer.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(TrainerPaymentHistoryActivity.this).setUpdate("0");
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    void totalAmount(ArrayList<PaymentHistoryObject> paymentHistoryObjectArrayList) {
        float totalAmount = 0;
        for (int i = 0; i < paymentHistoryObjectArrayList.size(); i++) {
            totalAmount = totalAmount + Float.parseFloat(paymentHistoryObjectArrayList.get(i).getAmount());
        }
        //totalAmount = totalAmount * 1000;
       // String amount = String.format("%.3f", totalAmount);
      //  amount = amount.replace(",",".");
        NumberFormat format = NumberFormat.getCurrencyInstance(getResources().getConfiguration().locale);
        String amount= format.format(Float.valueOf(totalAmount));
        amount = amount.replace(",",".");
        totalPayment.setText(amount);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }
}
