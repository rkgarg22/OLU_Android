package com.elisa.olu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.google.gson.Gson;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.CommonResponse;
import APIResponse.PaymentCollectResponse;
import APIResponse.PaymentIntiateResponse;
import Adapter.CategoriesAdapter;
import Adapter.ComprarAdapter;
import CustomControl.AvenirNextCondensedRegularTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit Chhabra on 4/26/2018.
 */

public class ComprarPlanActivity extends GenricActivity {

    @BindView(R.id.comprarRecyclerView)
    RecyclerView comprarRecyclerView;

    @BindView(R.id.popup)
    RelativeLayout popup;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Call call;
    PaymentIntiateResponse paymentIntiateResponse;
    int selectedPlanID;

    public int WEBVIEW_RETURN_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_plan);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        comprarRecyclerView.setLayoutManager(layoutManager);
        comprarRecyclerView.setAdapter(new ComprarAdapter(this));
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.yes)
    public void yes(View v) {
        startActivity(new Intent(this, CategoriesActivity.class));
        popup.setVisibility(View.GONE);
        this.finish();
    }

    @OnClick(R.id.no)
    public void no(View v) {
        //  startActivity(new Intent(this, MenuActivity.class));
        popup.setVisibility(View.GONE);
        this.finish();
    }

    public void setOnClick(final int planID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        if (planID == 1)
            builder.setMessage("¿Estas  seguro  que  deseas  comprar  el  plan  de  $300.000? ");
        else if (planID == 2)
            builder.setMessage("¿Estas  seguro  que  deseas  comprar  el  plan  de  $600.000? ");
        else if (planID == 3)
            builder.setMessage("¿Estas  seguro  que  deseas  comprar  el  plan  de  $900.000? ");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                selectedPlanID = planID;
                paymentIntiateAPI();
            }
        });
        builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                // finish();
            }
        });
        builder.show();
    }

    public void paymentIntiateAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ComprarPlanActivity.this).isConnectingToInternet(ComprarPlanActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.paymentIntiate(AppCommon.getInstance(this).getUserID(), "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        paymentIntiateResponse = (PaymentIntiateResponse) response.body();
                        if (paymentIntiateResponse.getSuccess() == 1) {
                            if (paymentIntiateResponse.getPaymentInfoObject().getIsTokenAvailable() == 1) {
                                paymentCollectAPI();
                            } else {
                                showDialog();
                            }
                        } else {
                            AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, paymentIntiateResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void paymentCollectAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ComprarPlanActivity.this).isConnectingToInternet(ComprarPlanActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.paymentCollect(AppCommon.getInstance(this).getUserID(),
                    paymentIntiateResponse.getPaymentInfoObject().getCardInfoArrayList().get(0).getToken(), String.valueOf(selectedPlanID));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        PaymentCollectResponse paymentCollectResponse = (PaymentCollectResponse) response.body();
                        if (paymentCollectResponse.getSuccess() == 1) {
                            popup.setVisibility(View.VISIBLE);
                        } else {
                            AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, paymentIntiateResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void setSelectCardAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ComprarPlanActivity.this).isConnectingToInternet(ComprarPlanActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.setCardSelection(AppCommon.getInstance(this).getUserID(),
                    paymentIntiateResponse.getPaymentInfoObject().getProcessUrlObject().getRequestId(), "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse commonResponse = (CommonResponse) response.body();
                        if (commonResponse.getSuccess() == 1) {
                            paymentIntiateAPI();
                        } else {
                            AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, paymentIntiateResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ComprarPlanActivity.this).showDialog(ComprarPlanActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ComprarPlanActivity.this).clearNonTouchableFlags(ComprarPlanActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEBVIEW_RETURN_CODE) {
            if (resultCode == RESULT_OK) {
                setSelectCardAPI();
            }
        }
    }

    public void showDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.reservar_payment_confirm));

        builder.setPositiveButton(ComprarPlanActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(ComprarPlanActivity.this, WebViewActivity.class);
                intent.putExtra("url", paymentIntiateResponse.getPaymentInfoObject().getProcessUrlObject().getProcessURL());
                startActivityForResult(intent, WEBVIEW_RETURN_CODE);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
