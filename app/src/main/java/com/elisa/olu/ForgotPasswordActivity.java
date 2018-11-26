package com.elisa.olu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.RegistrationResponse;
import Adapter.FAQAdapter;
import ApiEntity.LoginSignupEntity;
import ApiObject.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit Chhabra on 7/22/2018.
 */

public class ForgotPasswordActivity extends GenricActivity {

    @BindView(R.id.emailEditText)
    EditText emailEditText;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.submit)
    public void submit(View v) {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.emailEmptyError));
        } else {
            forgotPassword(email);
        }
    }


    private void forgotPassword(String email) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ForgotPasswordActivity.this).isConnectingToInternet(ForgotPasswordActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.forgotPassword(email);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ForgotPasswordActivity.this).clearNonTouchableFlags(ForgotPasswordActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse registrationResponse = (CommonIntResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            Toast.makeText(ForgotPasswordActivity.this, "Correo electrónico enviado a su correo electrónico registrado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            AppCommon.getInstance(ForgotPasswordActivity.this).showDialog(ForgotPasswordActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ForgotPasswordActivity.this).showDialog(ForgotPasswordActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ForgotPasswordActivity.this).clearNonTouchableFlags(ForgotPasswordActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ForgotPasswordActivity.this).showDialog(ForgotPasswordActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ForgotPasswordActivity.this).clearNonTouchableFlags(ForgotPasswordActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

}
