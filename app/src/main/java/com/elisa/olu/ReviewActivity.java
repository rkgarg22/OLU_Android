package com.elisa.olu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import ApiEntity.ReviewEntity;
import ApiObject.TodayBookingObject;
import CustomControl.AvenirNextCondensedRegularTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewActivity extends GenricActivity {

    @BindView(R.id.commentEditText)
    EditText commentEditText;

    @BindView(R.id.reviewRatingBar)
    RatingBar reviewRatingBar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.categoryImage)
    ImageView categoryImage;

    @BindView(R.id.categoryName)
    TextView categoryName;

    @BindView(R.id.monthTextView)
    TextView monthTextView;

    @BindView(R.id.time)
    TextView bookingTime;

    @BindView(R.id.date)
    TextView bookingDateTextView;

    @BindView(R.id.dayTextView)
    AvenirNextCondensedRegularTextView dayTextView;

    @BindView(R.id.userNameTextView)
    TextView userNameTextView;

    @BindView(R.id.userNameTitleTextView)
    TextView userNameTitleTextView;

    @BindView(R.id.endTime)
    TextView endTime;

    Call call;

    TodayBookingObject todayBookingObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        todayBookingObj = new Gson().fromJson(AppCommon.getInstance(this).getTrainerObject(), TodayBookingObject.class);

        String endDateFormat = todayBookingObj.getDate() + " " + todayBookingObj.getBookingStart();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date currentDate = sdfDate.parse(endDateFormat);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            bookingDateTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));

            String dayOfTheWeek = (String) DateFormat.format("EEEE", currentDate);
            dayOfTheWeek = dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1);

            String month = (String) DateFormat.format("MMM", currentDate);
            month = month.substring(0, 1).toUpperCase() + month.substring(1);

            monthTextView.setText(month);
            dayTextView.setText(dayOfTheWeek);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setupCategories(todayBookingObj.getCategoryID());
        endTime.setText(getTimeInFormat(todayBookingObj.getBookingEnd()));
        bookingTime.setText(getTimeInFormat(todayBookingObj.getBookingStart()));

        if (AppCommon.getInstance(this).getCurrentUser() == 2) {
            userNameTitleTextView.setVisibility(View.VISIBLE);
            userNameTextView.setText("por " + todayBookingObj.getFirstName() + " " + todayBookingObj.getLastName() + " ?");
        } else {
            userNameTitleTextView.setVisibility(View.GONE);
            userNameTextView.setText("¿Como calificarías a " + todayBookingObj.getFirstName() + " " + todayBookingObj.getLastName() + " como usuario?");
        }
    }

    public String getTimeInFormat(String time) {
        String formattedTime = "";
        String myFormat = "hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            Date currentDate = sdf.parse(time);

            sdf = new SimpleDateFormat("hh:mm", Locale.US);
            formattedTime = sdf.format(currentDate);
        } catch (Exception e) {

        }
        return formattedTime;
    }

    private void setupCategories(String categoryId) {
        switch (categoryId) {
            case "1":
                categoryImage.setImageResource(R.drawable.kick_boxing);
                categoryName.setText(getString(R.string.kickboxing));
                break;
            case "2":
                categoryImage.setImageResource(R.drawable.cardio_crossfit);
                categoryName.setText(getString(R.string.EntrenamientoFuncional));
                break;
            case "3":
                categoryImage.setImageResource(R.drawable.stretching);
                categoryName.setText(getString(R.string.stretching));
                break;
            case "4":
                categoryImage.setImageResource(R.drawable.yoga);
                categoryName.setText(getString(R.string.yoga));
                break;
            case "5":
                categoryImage.setImageResource(R.drawable.pilates);
                categoryName.setText(getString(R.string.pilates));
                break;
            case "8":
                categoryImage.setImageResource(R.drawable.masajes_deportivos);
                categoryName.setText(getString(R.string.masajes));
                break;
            case "9":
                categoryImage.setImageResource(R.drawable.fisioterapia);
                categoryName.setText(getString(R.string.fisioterapia));
                break;
            case "11":
                categoryImage.setImageResource(R.drawable.danza_fit);
                categoryName.setText(getString(R.string.danza_fit));
                break;
            case "10":
                categoryImage.setImageResource(R.drawable.gimnasia);
                categoryName.setText(getString(R.string.Gimnasia));
                break;
        }
    }

    @OnClick(R.id.submit)
    public void submit(View v) {
        String comment = commentEditText.getText().toString().trim();
        if (comment.isEmpty()) {
            commentEditText.setError(getString(R.string.emailEmptyError));
        } else {
            trainingComplete(comment);
        }
    }

    private void trainingComplete(String comment) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ReviewActivity.this).isConnectingToInternet(ReviewActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.trainingComplete(new ReviewEntity(AppCommon.getInstance(this).getUserID(), getIntent().getStringExtra("bookingID"), reviewRatingBar.getRating(), comment));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ReviewActivity.this).clearNonTouchableFlags(ReviewActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse registrationResponse = (CommonIntResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            showDialog();
                        } else {
                            AppCommon.getInstance(ReviewActivity.this).showDialog(ReviewActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ReviewActivity.this).showDialog(ReviewActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ReviewActivity.this).clearNonTouchableFlags(ReviewActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ReviewActivity.this).showDialog(ReviewActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ReviewActivity.this).clearNonTouchableFlags(ReviewActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if (AppCommon.getInstance(this).getCurrentUser() == 2) {
            builder.setMessage(getString(R.string.ratingMessageFromUserSide));
        } else {
            builder.setMessage(getString(R.string.ratingMessage));
        }
        builder.setNegativeButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (AppCommon.getInstance(ReviewActivity.this).getCurrentUser() == 2) {
                    Intent intent = new Intent(ReviewActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {

                }
                ReviewActivity.this.finish();
                AppCommon.getInstance(ReviewActivity.this).setStartTrainingObject("");
            }
        });
        builder.show();
    }

}
