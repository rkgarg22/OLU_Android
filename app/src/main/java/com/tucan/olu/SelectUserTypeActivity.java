package com.tucan.olu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;

public class SelectUserTypeActivity extends GenricActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        ButterKnife.bind(SelectUserTypeActivity.this);

    }

    @OnClick(R.id.userTypeLayout)
    public void userTypeClick(View v) {
        showUserPopUp();
    }

    @OnClick(R.id.trainerTypeLayout)
    public void trainerTypeLayout(View v) {
        showTrainerPopUp();
    }

    public void showUserPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.user_popup));
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
                Intent intent = new Intent(SelectUserTypeActivity.this, LoginScreenActivity.class);
                AppCommon.getInstance(SelectUserTypeActivity.this).setCurrentUser(2);
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void showTrainerPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.trainer_popup));
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
                Intent intent = new Intent(SelectUserTypeActivity.this, LoginScreenActivity.class);
                AppCommon.getInstance(SelectUserTypeActivity.this).setCurrentUser(1);
                startActivity(intent);
            }
        });
        builder.show();
    }
}
