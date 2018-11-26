package com.elisa.olu;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import ApiObject.TrainerDetailObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservarActivity extends GenricActivity {
    String categoryId;

    @BindView(R.id.horaClick)
    TextView horaClick;

    @BindView(R.id.noOfPerson)
    TextView noOfPerson;

    @BindView(R.id.groupLayout)
    LinearLayout groupLayout;

    @BindView(R.id.groupImageView)
    ImageView groupImageView;

    @BindView(R.id.individualImageView)
    ImageView individualImageView;

    @BindView(R.id.noOfPersonLayout)
    LinearLayout noOfPersonLayout;

    @BindView(R.id.calenderView)
    CalendarView calenderView;

    @BindView(R.id.plus)
    ImageView plus;

    @BindView(R.id.minus)
    ImageView minus;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.individualLayout)
    LinearLayout individualLayout;

    //int person = 2;

    int mon;

    int date;

    int hours, minutes;

    //int noPerson = 2;

    String bookingDate;

    String priceGroup = "";

    String fromProfile = "0";

    String trainerId;

    Call call;

    TrainerDetailObject trainerDetailObject;

    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);
        if (getIntent().getStringExtra("categoryId") != null) {
            categoryId = getIntent().getStringExtra("categoryId");
        }
        if (getIntent().getStringExtra("fromProfile") != null) {
            fromProfile = getIntent().getStringExtra("fromProfile");
            trainerId = getIntent().getStringExtra("trainerId");
            trainerDetailObject = new Gson().fromJson(getIntent().getStringExtra("userObject"), TrainerDetailObject.class);
            price = getIntent().getStringExtra("price");
        }
        ButterKnife.bind(this);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        bookingDate = formatter.format(calendar.getTime());
        date = calendar.getTime().getDate();
        mon = calendar.getTime().getMonth();
        calenderView.setMinDate(calendar.getTime().getDate());
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
                bookingDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                date = dayOfMonth;
                mon = (month + 1);
            }
        });
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        onBackPressed();
    }

    @OnClick(R.id.cancelButton)
    public void cancelButton() {
        this.finish();
    }

    @OnClick(R.id.horaClick)
    public void horaClick() {
        Calendar mcurrentTime = Calendar.getInstance();
        mcurrentTime.set(Calendar.MINUTE, mcurrentTime.get(Calendar.MINUTE) + 15);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ReservarActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                hours = selectedHour;
                minutes = selectedMinute;
                Date selectedDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    selectedDate = formatter.parse(bookingDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final Calendar selectedDateCalendar = Calendar.getInstance();
                selectedDateCalendar.setTime(selectedDate);
                selectedDateCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                selectedDateCalendar.set(Calendar.MINUTE, selectedMinute);
                selectedDateCalendar.set(Calendar.SECOND, 0);
                selectedDateCalendar.set(Calendar.MILLISECOND, 0);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 15);

                if (selectedDateCalendar.getTimeInMillis() >= c.getTimeInMillis()) {
                    String myFormat = "HH:mm"; // your own format
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    String formated_time = sdf.format(selectedDateCalendar.getTimeInMillis());
                    horaClick.setText(formated_time);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_booking_time), Toast.LENGTH_LONG).show();
                }
            }
        }, hour, minute, true);//Yes 24 hour time

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    @OnClick(R.id.confirmBtn)
    public void confirmBtnClick(View v) {
        if (fromProfile.equals("1")) {
            if (horaClick.getText().toString().equals(getString(R.string.selecciona_la_hora))) {
                Toast.makeText(this, "Please Select the Booking Time", Toast.LENGTH_SHORT).show();
            } else if (priceGroup.isEmpty()) {
                Toast.makeText(this, "Please Select the Booking Type", Toast.LENGTH_SHORT).show();
            } else {
                checkTrainerAvailable();
            }
        } else if (fromProfile.equals("2")) {
            if (horaClick.getText().toString().equals(getString(R.string.selecciona_la_hora))) {
                Toast.makeText(this, "Please Select the Booking Time", Toast.LENGTH_SHORT).show();
            } else if (priceGroup.isEmpty()) {
                Toast.makeText(this, "Please Select the Booking Type", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ReservarActivity.this, IngresaActivity.class);
                intent.putExtra("trainerId", trainerId);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("date", date);
                intent.putExtra("bookingDate", bookingDate);
                intent.putExtra("month", mon);
                intent.putExtra("price", price);
                intent.putExtra("time", horaClick.getText().toString());
                intent.putExtra("bookingType", priceGroup);
                intent.putExtra("userObject", new Gson().toJson(trainerDetailObject));
                intent.putExtra("isComingFromReservar", true);
                startActivity(intent);
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            if (horaClick.getText().toString().equals(getString(R.string.selecciona_la_hora))){
                Toast.makeText(this, "Please Select the Booking Time", Toast.LENGTH_SHORT).show();
            }else {
                String dateInString = bookingDate + " " + horaClick.getText().toString();
                Date date = null;
                try {
                    date = sdf.parse(dateInString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long selectedTimeMillis = date.getTime();
                long myCurrentTimeMillis = System.currentTimeMillis() + 7199580;
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = sdfDate.format(new Date());
                if (selectedTimeMillis > myCurrentTimeMillis) {
                    showResults("2");
                    // if ()
                } else {
                    showResults("1");
                }
            }
        }
    }

    public void checkTrainerAvailable() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(ReservarActivity.this).isConnectingToInternet(ReservarActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.trainerAvailable(AppCommon.getInstance(this).getUserID(), trainerId, categoryId, bookingDate, horaClick.getText().toString());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ReservarActivity.this).clearNonTouchableFlags(ReservarActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ReservarActivity.this);
                            builder.setTitle(getResources().getString(R.string.app_name));
                            builder.setMessage("El entrenador está disponible para reservar. ¿Quieres reservar el entrenador?");
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setCancelable(false);
                            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    Intent intent = new Intent(ReservarActivity.this, ResumenActivity.class);
                                    intent.putExtra("trainerId", trainerId);
                                    intent.putExtra("categoryId", categoryId);
                                    intent.putExtra("date", date);
                                    intent.putExtra("bookingDate", bookingDate);
                                    intent.putExtra("month", mon);
                                    intent.putExtra("price", price);
                                    intent.putExtra("time", horaClick.getText().toString());
                                    intent.putExtra("bookingType", priceGroup);
                                    intent.putExtra("userObject", new Gson().toJson(trainerDetailObject));
                                    startActivity(intent);

                                }
                            });
                            builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            AppCommon.getInstance(ReservarActivity.this).showDialog(ReservarActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(ReservarActivity.this).showDialog(ReservarActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ReservarActivity.this).clearNonTouchableFlags(ReservarActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ReservarActivity.this).showDialog(ReservarActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(ReservarActivity.this).clearNonTouchableFlags(ReservarActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    void showResults(String type) {
        if (horaClick.getText().toString().equals(getString(R.string.selecciona_la_hora))) {
            Toast.makeText(this, "Please Select the Booking Time", Toast.LENGTH_SHORT).show();
        } else if (priceGroup.isEmpty()) {
            Toast.makeText(this, "Please Select the Booking Type", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent;
            if (type.equals("1")) {
                intent = new Intent(this, UbicacionActivity.class);

            } else {
                intent = new Intent(this, IngresaActivity.class);
            }
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("date", date);
            intent.putExtra("bookingDate", bookingDate);
            intent.putExtra("month", mon);
            intent.putExtra("time", horaClick.getText().toString());
            intent.putExtra("bookingType", priceGroup);
            intent.putExtra("isComingFromReservar", true);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.individualLayout)
    public void setIndividualLayout(View v) {
        individualImageView.setImageResource(R.drawable.individual_selected);
        groupImageView.setImageResource(R.drawable.group);
        noOfPerson.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.greyColor));
        priceGroup = "1";
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.groupLayout)
    public void setGroupLayout(View v) {
        individualImageView.setImageResource(R.drawable.individual);
        groupImageView.setImageResource(R.drawable.group_selected);
        noOfPerson.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.greyColor));
        priceGroup = "3";
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.noOfPersonLayout)
    public void setNoOfPersonLayout(View v) {
        individualImageView.setImageResource(R.drawable.individual);
        groupImageView.setImageResource(R.drawable.group);
        noOfPerson.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.grey));
        priceGroup = "2";
    }

    @OnClick(R.id.plus)
    public void setPlus(View v) {
        int count = Integer.parseInt(noOfPerson.getText().toString());
        if (count != 4) {
            count = count + 1;
            noOfPerson.setText(String.valueOf(count));
        }
        if (count == 2) {
            priceGroup = "2";
        } else if (count == 3) {
            priceGroup = "4";
        } else if (count == 4) {
            priceGroup = "5";
        }
    }

    @OnClick(R.id.minus)
    public void setMinus(View v) {
        int count = Integer.parseInt(noOfPerson.getText().toString());
        if (count != 2) {
            count = count - 1;
            noOfPerson.setText(String.valueOf(count));
        }
        if (count == 2) {
            priceGroup = "2";
        } else if (count == 3) {
            priceGroup = "4";
        } else if (count == 4) {
            priceGroup = "5";
        }
    }
}
