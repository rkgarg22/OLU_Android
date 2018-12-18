package com.tucan.olu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CardInfo;
import APIResponse.CardListResponse;
import APIResponse.CommonIntResponse;
import APIResponse.CommonResponse;
import APIResponse.PaymentIntiateResponse;
import APIResponse.ProcessUrlResponse;
import Adapter.CardAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetadosPagosActivity extends GenricActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.cardsRecyclerView)
    RecyclerView cardsRecyclerView;

    @BindView(R.id.promoCodeLayout)
    LinearLayout promoCodeLayout;

    @BindView(R.id.promoCodeEditText)
    EditText promoCodeEditText;

    @BindView(R.id.placeToPay)
    ImageView placeToPay;

    Call call;

    PaymentIntiateResponse paymentIntiateResponse;

    int selectedPlanID;

    public int WEBVIEW_RETURN_CODE = 100;

    ArrayList<CardInfo> cardInfoArrayList = new ArrayList<>();

    CardAdapter cardAdapter;

    String requestId;
    boolean isDeletedSelectedOne = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos);
        ButterKnife.bind(this);
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardAdapter = new CardAdapter(this, cardInfoArrayList);
        cardsRecyclerView.setAdapter(cardAdapter);
        getCardListAPI();
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.placeToPay)
    public void setPlaceToPay(View v) {
        startActivity(new Intent(this, WebViewPlaceToPayActivity.class));
    }

    @OnClick({R.id.addCardLayout, R.id.addCardButton})
    public void addCardLayout(View v) {
        processPaymentAPI();
    }

    @OnClick({R.id.onDemandLayout, R.id.onDemand})
    public void onDemandLayout(View v) {
        promoCodeLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.crossBtn)
    public void crossBtnClick(View v) {
        promoCodeLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.submitBtn)
    public void submit(View v) {
        if (promoCodeEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el código de promoción", Toast.LENGTH_SHORT).show();
        } else {
            addPromoCode(promoCodeEditText.getText().toString());
            promoCodeLayout.setVisibility(View.GONE);
        }
    }

//    public void paymentIntiateAPI() {
//        AppCommon.getInstance(this).setNonTouchableFlags(this);
//        if (AppCommon.getInstance(MetadosPagosActivity.this).isConnectingToInternet(MetadosPagosActivity.this)) {
//            progressBar.setVisibility(View.VISIBLE);
//            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
//            call = pretoAppService.paymentIntiate(AppCommon.getInstance(this).getUserID(), "es");
//            call.enqueue(new Callback() {
//                @Override
//                public void onResponse(Call call, Response response) {
//                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
//                    if (response.code() == 200) {
//                        progressBar.setVisibility(View.GONE);
//                        PaymentIntiateResponse paymentIntiateResponse = (PaymentIntiateResponse) response.body();
//                        if (paymentIntiateResponse.getSuccess() == 1) {
//                            cardInfoArrayList.addAll(paymentIntiateResponse.getPaymentInfoObject().getCardInfoArrayList());
//                            cardAdapter.notifyDataSetChanged();
//                        } else {
//                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, paymentIntiateResponse.getError());
//                        }
//                    } else {
//                        AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getString(R.string.serverError));
//                    }
//                }
//
//                @Override
//                public void onFailure(Call call, Throwable t) {
//                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
//                    progressBar.setVisibility(View.GONE);
//                    AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getResources().getString(R.string.network_error));
//
//                }
//            });
//        } else {
//            AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
//            progressBar.setVisibility(View.GONE);
//            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
//        }
//    }


    public void processPaymentAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(MetadosPagosActivity.this).isConnectingToInternet(MetadosPagosActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.paymentProcessUrl(AppCommon.getInstance(this).getUserID(), "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        ProcessUrlResponse processUrlResponse = (ProcessUrlResponse) response.body();
                        if (processUrlResponse.getSuccess() == 1) {
                            requestId = processUrlResponse.getProcessUrlObject().getRequestId();
                            showGoToWebViewDialog(processUrlResponse.getProcessUrlObject().getProcessURL());
                        } else {
                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, processUrlResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void processRequiredAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(MetadosPagosActivity.this).isConnectingToInternet(MetadosPagosActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.paymentCollectReq(AppCommon.getInstance(this).getUserID(), requestId, "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            getCardListAPI();
                            //paymentIntiateAPI();
                        } else {
                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void deletePayment(String reqId, final int pos) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(MetadosPagosActivity.this).isConnectingToInternet(MetadosPagosActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.deletePayment(AppCommon.getInstance(this).getUserID(), reqId, "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getString(R.string.card_deleted_successfully));
                            if (cardInfoArrayList.get(pos).getDefaultCard() == 1) {
                                isDeletedSelectedOne = true;
                            } else {
                                isDeletedSelectedOne = false;
                            }
                            getCardListAPI();
                        } else {
                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void addPromoCode(String code) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(MetadosPagosActivity.this).isConnectingToInternet(MetadosPagosActivity.this)) {
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.promoCode(AppCommon.getInstance(this).getUserID(), code);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            promoCodeLayout.setVisibility(View.GONE);
                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, "código de promoción agregado con éxito");
                        } else {
                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MetadosPagosActivity.this).setUpdate("0");
                    AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
            progressBar.setVisibility(View.VISIBLE);
            AppCommon.getInstance(MetadosPagosActivity.this).setUpdate("0");
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEBVIEW_RETURN_CODE) {
            if (resultCode == RESULT_OK) {
                processRequiredAPI();
            }
        }
    }

    public void setOnClick(final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("¿Seguro que quieres eliminar la tarjeta?");
        builder.setCancelable(false);
        builder.setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePayment(cardInfoArrayList.get(adapterPosition).getRequestId(), adapterPosition);
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }


    public void setSelectedCard(final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.switch_card_confirmation));
        builder.setCancelable(false);
        builder.setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setSelectCardAPI(cardInfoArrayList.get(adapterPosition).getRequestId());
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    public void getCardListAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(MetadosPagosActivity.this).isConnectingToInternet(MetadosPagosActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.getCardList(AppCommon.getInstance(this).getUserID(), "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    if (response.code() == 200) {
                        CardListResponse cardListResponse = (CardListResponse) response.body();
                        if (cardListResponse.getSuccess() == 1) {
                            cardInfoArrayList.clear();
                            cardInfoArrayList.addAll(cardListResponse.getCardInfoArrayList());
                            cardAdapter.notifyDataSetChanged();

                            if (isDeletedSelectedOne && cardInfoArrayList.size() > 0) {
                                setSelectCardAPI(cardInfoArrayList.get(0).getRequestId());
                                isDeletedSelectedOne = false;
                            } else if (cardInfoArrayList.size() == 1) {
                                if (cardInfoArrayList.get(0).getDefaultCard() == 0) {
                                    setSelectCardAPI(cardInfoArrayList.get(0).getRequestId());
                                    isDeletedSelectedOne = false;
                                }
                            } else {
                                isDeletedSelectedOne = false;
                            }
                        } else {
                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, cardListResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void setSelectCardAPI(String requestID) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(MetadosPagosActivity.this).isConnectingToInternet(MetadosPagosActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.setCardSelection(AppCommon.getInstance(this).getUserID(),
                    requestID, "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse commonResponse = (CommonResponse) response.body();
                        if (commonResponse.getSuccess() == 1) {
                            getCardListAPI();
                        } else {
                            AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, paymentIntiateResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MetadosPagosActivity.this).showDialog(MetadosPagosActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(MetadosPagosActivity.this).clearNonTouchableFlags(MetadosPagosActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }


    public void showGoToWebViewDialog(final String processURl) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.reservar_payment_confirm));

        builder.setPositiveButton(MetadosPagosActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(MetadosPagosActivity.this, WebViewActivity.class);
                intent.putExtra("url", processURl);
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
