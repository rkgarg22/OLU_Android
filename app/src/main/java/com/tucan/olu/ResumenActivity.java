package com.tucan.olu;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.BookingResponse;
import APIResponse.PaymentIntiateResponse;
import APIResponse.CommonResponse;
import ApiObject.CategoriesObject;
import ApiObject.TrainerDetailObject;
import CustomControl.AvenirNextCondensedRegularTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumenActivity extends GenricActivity {

    @BindView(R.id.popup)
    RelativeLayout popup;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.monthTextView)
    TextView monthTextView;

    @BindView(R.id.time)
    TextView bookingTime;

    @BindView(R.id.noOfPerson)
    TextView noOfPersonTextView;

    @BindView(R.id.date)
    TextView bookingDateTextView;

    @BindView(R.id.imageViewGroup)
    ImageView imageViewGroup;

    @BindView(R.id.bookingText)
    TextView bookingText;

    @BindView(R.id.categoryImage)
    ImageView categoryImage;

    @BindView(R.id.categoryName)
    TextView categoryName;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.userInfo)
    TextView userInfo;

    @BindView(R.id.priceTextView)
    TextView priceTextView;

    @BindView(R.id.updatedPriceTextView)
    TextView updatedPriceTextView;

    @BindView(R.id.okBtn)
    Button okBtn;

    @BindView(R.id.dayTextView)
    AvenirNextCondensedRegularTextView dayTextView;

    @BindView(R.id.userImage)
    SimpleDraweeView userImage;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.addressText)
    TextView addressText;

    Call call;

    String categoryId = "";

    int date;

    String bookingDate = "";

    String time = "";

    String price = "";

    String bookingType = "";

    String trainerId = "";

    @BindView(R.id.endTime)
    TextView endTime;

    TrainerDetailObject userListObject;
    public int WEBVIEW_RETURN_CODE = 100;
    PaymentIntiateResponse paymentIntiateResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("trainerId") != null) {
            trainerId = getIntent().getStringExtra("trainerId");
            categoryId = getIntent().getStringExtra("categoryId");
            bookingDate = getIntent().getStringExtra("bookingDate");
            date = getIntent().getIntExtra("date", 0);
            time = getIntent().getStringExtra("time");
            price = getIntent().getStringExtra("price");
            bookingType = getIntent().getStringExtra("bookingType");
            userListObject = new Gson().fromJson(getIntent().getStringExtra("userObject"), TrainerDetailObject.class);
            setUpPrice();
            addressText.setText(AppCommon.address);
            //setUpMonth();
            setupBookingType();
            setupCategories();
            setupTrainerProfile();

            String endDateFormat = bookingDate + " " + time;
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                Date currentDate = sdfDate.parse(endDateFormat);
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                String myFormat = "hh:mm a"; // your own format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String formattedStartime = sdf.format(calendar.getTimeInMillis());
                bookingTime.setText(formattedStartime);
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                String formattedEndtime = sdf.format(calendar.getTimeInMillis());
                endTime.setText(formattedEndtime);


                String dayOfTheWeek = (String) DateFormat.format("EEEE", currentDate);
                dayOfTheWeek = dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1);

                String monthString = (String) DateFormat.format("MMM", currentDate);
                monthString = monthString.substring(0, 1).toUpperCase() + monthString.substring(1);

                String date = (String) DateFormat.format("dd", currentDate);
                dayTextView.setText(dayOfTheWeek);
                monthTextView.setText(monthString);
                bookingDateTextView.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        ratingBar.setClickable(false);
    }

    private void setupTrainerProfile() {
        userImage.setImageURI(Uri.parse(userListObject.getUserImageUrl()));
        name.setText(userListObject.getFirstName() + " " + userListObject.getLastName());
        try {
            ratingBar.setRating(Integer.parseInt(userListObject.getReviews()));
        } catch (Exception e) {

        }
    }

    private void setupCategories() {
        switch (categoryId) {
            case "1":
                categoryImage.setImageResource(R.drawable.kick_boxing);
                categoryName.setText(getString(R.string.kickboxing));
                userInfo.setText(getString(R.string.kickboxing));
                break;
            case "2":
                categoryImage.setImageResource(R.drawable.cardio_crossfit);
                categoryName.setText(getString(R.string.EntrenamientoFuncional));
                userInfo.setText(getString(R.string.EntrenamientoFuncional));
                break;
            case "3":
                categoryImage.setImageResource(R.drawable.stretching);
                categoryName.setText(getString(R.string.stretching));
                userInfo.setText(getString(R.string.stretching));
                break;
            case "4":
                categoryImage.setImageResource(R.drawable.yoga);
                categoryName.setText(getString(R.string.yoga));
                userInfo.setText(getString(R.string.yoga));
                break;
            case "5":
                categoryImage.setImageResource(R.drawable.pilates);
                categoryName.setText(getString(R.string.pilates));
                userInfo.setText(getString(R.string.pilates));
                break;
            case "8":
                categoryImage.setImageResource(R.drawable.masajes_deportivos);
                categoryName.setText(getString(R.string.masajes));
                userInfo.setText(getString(R.string.masajes));
                break;
            case "9":
                categoryImage.setImageResource(R.drawable.fisioterapia);
                categoryName.setText(getString(R.string.fisioterapia));
                userInfo.setText(getString(R.string.fisioterapia));
                break;
            case "11":
                categoryImage.setImageResource(R.drawable.danza_fit);
                categoryName.setText(getString(R.string.danza_fit));
                userInfo.setText(getString(R.string.danza_fit));
                break;
            case "10":
                categoryImage.setImageResource(R.drawable.gimnasia);
                categoryName.setText(getString(R.string.Gimnasia));
                userInfo.setText(getString(R.string.Gimnasia));
                break;
        }
    }

    private void setUpPrice() {
        for (CategoriesObject obj : userListObject.getCategoriesObjectList()) {
            if (obj.getCategoryID().equals(categoryId)) {

                Double d = 0.0;
                switch (bookingType) {
                    case "1":
                        priceTextView.setText(obj.getSinglePrice());
                        d = Double.parseDouble(obj.getSinglePrice());
                        //updatedPriceTextView.setText("" + (Float.parseFloat(obj.getSinglePrice()) + 2.000));
                        break;
                    case "3":
                        priceTextView.setText(obj.getCompanyPrice());
                        d = Double.parseDouble(obj.getCompanyPrice());
                        //updatedPriceTextView.setText("" + (Float.parseFloat(obj.getCompanyPrice()) + 2.000));
                        break;
                    case "2":
                        priceTextView.setText(obj.getGroupPrice2());
                        d = Double.parseDouble(obj.getGroupPrice2());
                        //updatedPriceTextView.setText("" + (Float.parseFloat(obj.getGroupPrice2()) + 2.000));
                        break;
                    case "4":
                        priceTextView.setText(obj.getGroupPrice3());
                        d = Double.parseDouble(obj.getGroupPrice3());
                        //updatedPriceTextView.setText("" + (Float.parseFloat(obj.getGroupPrice3()) + 2.000));
                        break;
                    case "5":
                        priceTextView.setText(obj.getGroupPrice4());
                        d = Double.parseDouble(obj.getGroupPrice4());
                       // updatedPriceTextView.setText("" + (Float.parseFloat(obj.getGroupPrice4()) + 2.000));
                        break;
                }
                d= d+2.000;
                String priceFormat = String.format("%.3f", d);
                priceFormat = priceFormat.replace(",",".");
                updatedPriceTextView.setText(priceFormat);
                break;
            }
        }
    }

    private void setupBookingType() {
        switch (bookingType) {
            case "1":
                imageViewGroup.setImageResource(R.drawable.individual);
                bookingText.setText(getString(R.string.individual));
                break;
            case "3":
                imageViewGroup.setImageResource(R.drawable.group);
                bookingText.setText(getString(R.string.group));
                break;
            case "2":
                imageViewGroup.setImageResource(R.drawable.users);
                bookingText.setText("Grupo 2");
                break;
            case "4":
                imageViewGroup.setImageResource(R.drawable.users);
                bookingText.setText("Grupo 3");
                break;
            case "5":
                imageViewGroup.setImageResource(R.drawable.users);
                bookingText.setText("Grupo 4");
                break;
        }
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.placeToPayLayout)
    public void setPlaceToPayLayout(View v) {
        startActivity(new Intent(this, WebViewPlaceToPayActivity.class));
    }

    @OnClick(R.id.confirmBtn)
    public void confirmBtnClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getString(R.string.accetJob));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                vaultBalance();
//                booking();
            }
        });
        builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @OnClick(R.id.okBtn)
    public void setPopup(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void vaultBalance() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ResumenActivity.this).isConnectingToInternet(ResumenActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);

            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.vaultBalance(AppCommon.getInstance(this).getUserID(), AppCommon.getInstance(this).getSelectedLanguage());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse commonResponse = (CommonResponse) response.body();
                        if (commonResponse.getSuccess() == 1) {
                            if (commonResponse.getResult() == 0) {
                                booking();
                            } else if (commonResponse.getResult() > Float.parseFloat(price.trim())) {
                                booking();
                            } else if (commonResponse.getResult() < Float.parseFloat(price.trim())) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ResumenActivity.this);
                                builder.setCancelable(false);
                                builder.setMessage(getString(R.string.payment_decuct_from_card));
                                builder.setCancelable(false);
                                builder.setPositiveButton(ResumenActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        booking();
                                    }
                                });
                                builder.setNegativeButton(ResumenActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        } else {
                            AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, commonResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void booking() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ResumenActivity.this).isConnectingToInternet(ResumenActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);

            if (AppCommon.longitudeValue.equals("") || AppCommon.latitudeValue.equals("")) {
                AppCommon.longitudeValue = String.valueOf(AppCommon.getInstance(this).getUserLongitude());
                AppCommon.latitudeValue = String.valueOf(AppCommon.getInstance(this).getUserLatitude());
            }

            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.booking(AppCommon.getInstance(this).getUserID(), trainerId, categoryId, bookingDate, time, bookingType,
                    AppCommon.longitudeValue,
                    AppCommon.latitudeValue, AppCommon.address);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        BookingResponse bookingResponse = (BookingResponse) response.body();
                        if (bookingResponse.getSuccess() == 1) {
//                            if (bookingResponse.getPaymentRequire() == 1) {
//                                AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getString(R.string.payment_decuct_from_card));
//                            }
                            popup.setVisibility(View.VISIBLE);
                        } else {
                            if (bookingResponse.getIsTokenSaved() == 0) {
                                showDialog();
                            } else {
                                AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, bookingResponse.getError());
                            }
                        }
                    } else {
                        AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void paymentIntiateAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ResumenActivity.this).isConnectingToInternet(ResumenActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.paymentIntiate(AppCommon.getInstance(this).getUserID(), "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        paymentIntiateResponse = (PaymentIntiateResponse) response.body();
                        if (paymentIntiateResponse.getSuccess() == 1) {
                            if (paymentIntiateResponse.getPaymentInfoObject().getIsTokenAvailable() == 0) {
                                Intent intent = new Intent(ResumenActivity.this, WebViewActivity.class);
                                intent.putExtra("url", paymentIntiateResponse.getPaymentInfoObject().getProcessUrlObject().getProcessURL());
                                startActivityForResult(intent, WEBVIEW_RETURN_CODE);
                            }
                        } else {
                            AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, paymentIntiateResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void setSelectCardAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ResumenActivity.this).isConnectingToInternet(ResumenActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.setCardSelection(AppCommon.getInstance(this).getUserID(),
                    paymentIntiateResponse.getPaymentInfoObject().getProcessUrlObject().getRequestId(), "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse commonResponse = (CommonResponse) response.body();
                        if (commonResponse.getSuccess() == 1) {
                            booking();
                        } else {
                            AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, paymentIntiateResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ResumenActivity.this).showDialog(ResumenActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ResumenActivity.this).clearNonTouchableFlags(ResumenActivity.this);
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

        builder.setPositiveButton(ResumenActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                paymentIntiateAPI();
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
