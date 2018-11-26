package com.elisa.olu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.RegistrationResponse;
import ApiEntity.LoginSignupEntity;
import ApiObject.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdjustesActivity extends GenricActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.faqLayout)
    public void faqLayout(View v) {
        Intent intent = new Intent(this, FAQActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tutorialLayout)
    public void tutorialLayout(View v) {
        Intent intent = new Intent(this, TutorialsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.termsLayout)
    public void termsLayout(View v) {
        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.contactUsLayout)
    public void contactUsLayout(View v) {
        Intent intent = new Intent(this, SaldoActivity.class);
        startActivity(intent);
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
        if (AppCommon.getInstance(AdjustesActivity.this).isConnectingToInternet(AdjustesActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            //  final String token = firebaseInstanceIDService.getDeviceToken();
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.logout(AppCommon.getInstance(this).getUserID());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(AdjustesActivity.this).clearNonTouchableFlags(AdjustesActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            AppCommon.getInstance(AdjustesActivity.this).clearSharedPreference();
                            Intent intent = new Intent(AdjustesActivity.this, SelectUserTypeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            AppCommon.getInstance(AdjustesActivity.this).showDialog(AdjustesActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(AdjustesActivity.this).showDialog(AdjustesActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(AdjustesActivity.this).clearNonTouchableFlags(AdjustesActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(AdjustesActivity.this).showDialog(AdjustesActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(AdjustesActivity.this).clearNonTouchableFlags(AdjustesActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }
}
