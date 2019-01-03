package com.tucan.olu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.CommonResponse;
import ApiEntity.ChangePasswordEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordActivity extends GenricActivity {

    @BindView(R.id.oldPasswordEditText)
    EditText oldPasswordEditText;

    @BindView(R.id.newPasswordEditText)
    EditText newPasswordEditText;

    @BindView(R.id.confirmPasswordEditText)
    EditText confirmPasswordEditText;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        oldPasswordEditText.setText(AppCommon.getInstance(this).getPassword());
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.submit)
    public void submit(View v) {
        String oldPassword = oldPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        if (oldPassword.isEmpty()) {
            oldPasswordEditText.setError(getString(R.string.passportEmptyError));
        } else if (newPassword.isEmpty()) {
            newPasswordEditText.setError(getString(R.string.passportEmptyError));
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError(getString(R.string.passportEmptyError));
        } else if (!newPassword.equals(confirmPassword)) {
            newPasswordEditText.setError(getString(R.string.passportValidError));
            confirmPasswordEditText.setError(getString(R.string.passportValidError));
        } else {
            changePassword(oldPassword,newPassword);
        }
    }

    private void changePassword(String oldPassword, String newPassword) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ChangePasswordActivity.this).isConnectingToInternet(ChangePasswordActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            ChangePasswordEntity entity = new ChangePasswordEntity(AppCommon.getInstance(this).getUserID(),newPassword,oldPassword);
            call = pretoAppService.resetPassword(entity);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ChangePasswordActivity.this).clearNonTouchableFlags(ChangePasswordActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse registrationResponse = (CommonResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            finish();
                        } else {
                            AppCommon.getInstance(ChangePasswordActivity.this).showDialog(ChangePasswordActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ChangePasswordActivity.this).showDialog(ChangePasswordActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ChangePasswordActivity.this).clearNonTouchableFlags(ChangePasswordActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ChangePasswordActivity.this).showDialog(ChangePasswordActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ChangePasswordActivity.this).clearNonTouchableFlags(ChangePasswordActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

}
