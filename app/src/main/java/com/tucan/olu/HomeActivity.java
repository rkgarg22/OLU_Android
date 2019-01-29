package com.tucan.olu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends GenricActivity {

    Call call;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.profileBtn)
    SimpleDraweeView profileBtn;

    @BindView(R.id.placeToPay)
    ImageView placeToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            profileBtn.setImageURI(Uri.parse(AppCommon.getInstance(this).getProfilePicUrl()));
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.placeToPay)
    public void onPlaceToPay(View v) {
        startActivity(new Intent(this, WebViewPlaceToPayActivity.class));
    }

    @OnClick(R.id.onDemandLayout)
    public void onDemandClick(View v) {
        Intent categoryIntent = new Intent(this, CategoriesActivity.class);
        startActivity(categoryIntent);
    }

    @OnClick(R.id.misReservesLayout)
    public void misReservesLayout(View v) {
        Intent categoryIntent = new Intent(this, HistoricalActivity.class);
        startActivity(categoryIntent);
    }

    @OnClick(R.id.shareButton)
    void shareButton() {
        String text = "Olu. App te contenta con los mejores, donde quieras y cuando quieras. \n¡Te invito a descargarla! \n\nhttp://bit.ly/2sND77K";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, text);

        startActivity(Intent.createChooser(share, "Share Link Using"));
    }

    @OnClick(R.id.menuLayout)
    public void menuClick(View v) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.profileBtn)
    public void profileBtn() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.walletDiscountLayout)
    public void especialisisLayout(View v) {
        Intent categoryIntent = new Intent(this, ComprarPlanActivity.class);
        startActivity(categoryIntent);
    }

    @OnClick(R.id.especialisisLayout)
    public void walletDiscountLayout(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("categoryId", "");
        intent.putExtra("date", "");
        intent.putExtra("bookingDate", "");
        intent.putExtra("month", "");
        intent.putExtra("noOfPerson", "");
        intent.putExtra("time", "");
        intent.putExtra("bookingType", "");
        startActivity(intent);
    }

    @OnClick(R.id.quienes)
    public void quienes(View v) {
        startActivity(new Intent(this, TermsActivity.class).putExtra("isTerms", "17"));
    }

    @OnClick(R.id.ayuda)
    public void ayuda(View v) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hola@oluapp.com"});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        sendIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(sendIntent, "OLU"));
    }

    @OnClick(R.id.termsLayout)
    public void termsLayout(View v) {
        Intent categoryIntent = new Intent(this, TermsOptionActivity.class);
        startActivity(categoryIntent);
    }

    @OnClick(R.id.cerrarLayout)
    public void cerrarLayout(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.logoutText));
        builder.setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                logout();
            }
        });
        builder.show();
    }

    private void logout() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(HomeActivity.this).isConnectingToInternet(HomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            //  final String token = firebaseInstanceIDService.getDeviceToken();
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.logout(AppCommon.getInstance(this).getUserID());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(HomeActivity.this).clearNonTouchableFlags(HomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            AppCommon.getInstance(HomeActivity.this).clearSharedPreference();
                            Intent intent = new Intent(HomeActivity.this, SelectUserTypeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            AppCommon.getInstance(HomeActivity.this).showDialog(HomeActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(HomeActivity.this).showDialog(HomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(HomeActivity.this).clearNonTouchableFlags(HomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(HomeActivity.this).showDialog(HomeActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(HomeActivity.this).clearNonTouchableFlags(HomeActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }


}
