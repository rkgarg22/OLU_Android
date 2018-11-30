package com.elisa.olu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.elisa.olu.Firebase.NotificationPopupActivity;
import com.elisa.olu.LocationInfrastructure.FusedLocationTracker;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.CommonResponse;
import APIResponse.CommonStringResponse;
import APIResponse.OnGoingBookingResponse;
import APIResponse.TodayBookingResponse;
import APIResponse.TrainerProfileResponse;
import APIResponse.UserListingResponse;
import Adapter.SesionerAdapter;
import Adapter.TodayBookingAdapter;
import Adapter.UbicacionAdapter;
import ApiEntity.CreateAgendaEntity;
import ApiObject.TodayBookingObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainerHomeActivity extends GenricActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.sesinorRecyclerView)
    RecyclerView sesinorRecyclerView;

    @BindView(R.id.lock)
    ImageView lock;

    @BindView(R.id.unlock)
    ImageView unlock;

    @BindView(R.id.availableProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.progressBar)
    ProgressBar progressBarHome;

    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @BindView(R.id.iniciarBtn)
    Button iniciarBtn;

    @BindView(R.id.finishSession)
    Button finishButton;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.personalEventView)
    LinearLayout personalEventView;

    @BindView(R.id.titleEditText)
    EditText titleEditText;

    @BindView(R.id.dateEditView)
    EditText dateTextView;

    @BindView(R.id.startTime)
    EditText startTimeTextView;

    @BindView(R.id.endTime)
    EditText endTimeTextView;


    @BindView(R.id.repeatTypeSpinner)
    Spinner repeatTypeSpinner;

    @BindView(R.id.eliminarBtn)
    Button eliminarBtn;

    private static final long MIN_TIME = 400;

    private
    static final float MIN_DISTANCE = 2000;

    List<TodayBookingObject> todayBookingObjectList = new ArrayList<>();

    TodayBookingAdapter todayBookingAdapter;

    @BindView(R.id.calenderView)
    CalendarView calenderView;
    Call call;
    String bookingDate;
    private String latitude;
    private String longitude;


    int selRepetir = 0;

    String startTimeInput = "", endTimeInput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_home);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        sesinorRecyclerView.setLayoutManager(layoutManager);
        todayBookingAdapter = new TodayBookingAdapter(this, todayBookingObjectList);
        sesinorRecyclerView.setAdapter(todayBookingAdapter);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        bookingDate = formatter.format(calendar.getTime());

        new FusedLocationTracker(this);
        getToodayBooking();
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
                bookingDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                todayBookingObjectList.clear();
                getToodayBooking();
            }
        });
        setLockUnlock();
        ratingBar.setClickable(false);
        getProfile();
        getOnGoingSession();
        setRepetirDropDown();
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                checkLockButtonStatusAPI();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setRepetirDropDown() {
        List<String> categories = new ArrayList<String>();
        categories.add(getString(R.string.nunca));
        categories.add(getString(R.string.cadaDia));
        categories.add(getString(R.string.todasLasSemanas));
        categories.add(getString(R.string.cadaMes));
        categories.add(getString(R.string.cadaAno));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, categories);
        repeatTypeSpinner.setAdapter(dataAdapter);

        repeatTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                selRepetir = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setLockUnlock() {
        if (AppCommon.getInstance(this).getIsAvailable() == 1) {
            lock.setVisibility(View.GONE);
            unlock.setVisibility(View.VISIBLE);
        } else {
            unlock.setVisibility(View.GONE);
            lock.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInciarButtonAccordingly();
        getToodayBooking();
        setLockUnlock();
    }

    public void setInciarButtonAccordingly() {
        if (AppCommon.getInstance(this).getBookingID().equals("")) {
            finishButton.setVisibility(View.GONE);
            iniciarBtn.setVisibility(View.GONE);
        } else {
            finishButton.setVisibility(View.VISIBLE);
            iniciarBtn.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.navback)
    public void backButtonClick(View v) {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    @OnClick(R.id.backButtonClick)
    void setImageViewRight() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    @OnClick(R.id.plusicon)
    public void plusClick(View v) {
        workingRole = "create";
        eliminarBtn.setVisibility(View.GONE);
        personalEventView.setVisibility(View.VISIBLE);
        titleEditText.setText("");
        dateTextView.setText("");
        startTimeTextView.setText("");
        endTimeTextView.setText("");
    }

    String workingRole = "";

    @OnClick(R.id.guardarBtn)
    public void guardarBtnClick() {
        personalEventView.setVisibility(View.GONE);
        if (workingRole.equals("create")) {
            if (createValidation())
                callingCreateAgenda(String.valueOf(AppCommon.getInstance(this).getUserID()), this.titleEditText.getText().toString().trim()
                        , dateInputFormat, startTimeInput, endTimeInput, String.valueOf(selRepetir));
        } else {
            callingUpdateAPI(String.valueOf(AppCommon.getInstance(this).getUserID()), String.valueOf(agendaID),
                    this.titleEditText.getText().toString().trim(), dateInputFormat, startTimeInput, endTimeInput, String.valueOf(selRepetir));
        }
    }

    @OnClick(R.id.crossIcon)
    public void crossIconClick(View v) {
        personalEventView.setVisibility(View.GONE);
    }

    String dateInputFormat = "";

    @OnClick(R.id.dateEditView)
    void setDobTextView() {
        final Calendar myCalendar = Calendar.getInstance();
        new DatePickerDialog(TrainerHomeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date currentDate = myCalendar.getTime();

                String dayOfTheWeek = (String) DateFormat.format("EEEE", currentDate);
                dayOfTheWeek = dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1);

                String monthString = (String) DateFormat.format("MMM", currentDate);
                monthString = monthString.substring(0, 1).toUpperCase() + monthString.substring(1);

                String date = (String) DateFormat.format("dd", currentDate);

                dateTextView.setText(dayOfTheWeek + ", " + monthString + " " + date);

                String InputFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf2 = new SimpleDateFormat(InputFormat, Locale.US);
                dateInputFormat = sdf2.format(myCalendar.getTime());

            }
        }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @OnClick(R.id.startTime)
    public void startTimeClick() {
        setTime(startTimeTextView, "startTime");
    }

    @OnClick(R.id.endTime)
    public void endTIme(View v) {
        setTime(endTimeTextView, "endTime");
    }

    public void setTime(final EditText editTextView, final String commingFor) {
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TrainerHomeActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
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

                String myFormat = "HH:mm a"; // your own format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String formated_time = sdf.format(selectedDateCalendar.getTimeInMillis());
                editTextView.setText(formated_time);
                if (commingFor.equals("startTime")) {
                    String myFormat2 = "HH:mm"; // your own format
                    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
                    startTimeInput = sdf2.format(selectedDateCalendar.getTimeInMillis()) + ":00";
                } else {
                    String myFormat3 = "HH:mm"; // your own format
                    SimpleDateFormat sdf3 = new SimpleDateFormat(myFormat3, Locale.US);
                    endTimeInput = sdf3.format(selectedDateCalendar.getTimeInMillis()) + ":00";

                }

            }
        }, hour, minute, true);//Yes 24 hour time

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    @OnClick({R.id.historyLayout, R.id.historyBtn})
    public void tutorialLayout(View v) {
        Intent intent = new Intent(this, HistoricalActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.termsLayout, R.id.termsBtn})
    public void termsLayout(View v) {
        Intent intent = new Intent(this, TermsActivity.class);
        intent.putExtra("isTerms", "21");
        startActivity(intent);
    }

    @OnClick({R.id.profileLayout, R.id.profileBtn})
    public void profile(View v) {
        Intent intent = new Intent(this, TrainerProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.finishSession)
    public void finishSession(View v) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getString(R.string.finishSeassionConfirmation));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                statusChangeBooking("1");
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

    private void checkLockButtonStatusAPI() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.checkLockButtonStatus(AppCommon.getInstance(this).getUserID(), "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse commonIntResponse = (CommonResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            if (commonIntResponse.getResult() == 1) {
                                lock.setVisibility(View.GONE);
                                unlock.setVisibility(View.VISIBLE);
                            } else {
                                unlock.setVisibility(View.GONE);
                                lock.setVisibility(View.VISIBLE);
                            }
                            AppCommon.getInstance(TrainerHomeActivity.this).setIsAvailable(commonIntResponse.getResult());
                        }
                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void getToodayBooking() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBarHome.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.todayBooking(AppCommon.getInstance(this).getUserID(), "es", bookingDate);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBarHome.setVisibility(View.GONE);
                        TodayBookingResponse todayBookingResponse = (TodayBookingResponse) response.body();
                        if (todayBookingResponse.getSuccess() == 1) {
                            todayBookingObjectList = todayBookingResponse.getTodayBookingObjectList();
                            todayBookingAdapter.setData(todayBookingObjectList);
                            noDataFound.setVisibility(View.GONE);
                        } else {
                            todayBookingObjectList.clear();
                            noDataFound.setVisibility(View.VISIBLE);
                            todayBookingAdapter.notifyDataSetChanged();
                        }
                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBarHome.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            progressBarHome.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }


    private void getProfile() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.getTrainerProfile(String.valueOf(AppCommon.getInstance(this).getUserID()));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        TrainerProfileResponse myProfileResponse = (TrainerProfileResponse) response.body();
                        if (myProfileResponse.getSuccess() == 1) {
                            ratingBar.setRating(Integer.parseInt(myProfileResponse.getTrainerProfileObject().getReviews()));
                        }
                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    //AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            progressBar.setVisibility(View.GONE);
            //AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }


    @OnClick(R.id.availableLayout)
    public void availableLayout(View v) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            final int isAvailble;
            if (AppCommon.getInstance(this).getIsAvailable() == 0) {
                isAvailble = 1;
            } else {
                isAvailble = 0;
            }
            call = pretoAppService.isAvailable(AppCommon.getInstance(this).getUserID(), isAvailble);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            if (isAvailble == 1) {
                                lock.setVisibility(View.GONE);
                                unlock.setVisibility(View.VISIBLE);
                                unRegisterAlarm();
                            } else {
                                unlock.setVisibility(View.GONE);
                                lock.setVisibility(View.VISIBLE);
                                setAlaram();
                            }
                            AppCommon.getInstance(TrainerHomeActivity.this).setIsAvailable(isAvailble);
                        } else {
                            AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
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
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            //  final String token = firebaseInstanceIDService.getDeviceToken();
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.logout(AppCommon.getInstance(this).getUserID());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            AppCommon.getInstance(TrainerHomeActivity.this).clearSharedPreference();
                            Intent intent = new Intent(TrainerHomeActivity.this, SelectUserTypeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    private void statusChangeBooking(final String status) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.bookingStateChange(AppCommon.getInstance(this).getUserID(), AppCommon.getInstance(this).getBookingID(),
                    status);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            TodayBookingObject todayBookingObj = new Gson().fromJson(AppCommon.getInstance(TrainerHomeActivity.this).getTrainerObject(), TodayBookingObject.class);
                            Intent intent = new Intent(TrainerHomeActivity.this, ReviewActivity.class);
                            intent.putExtra("bookingID", todayBookingObj.getBookingId());
                            intent.putExtra("userName", todayBookingObj.getFirstName() + " " + todayBookingObj.getLastName());
                            intent.putExtra("category", todayBookingObj.getCategory());
                            startActivity(intent);
                            AppCommon.getInstance(TrainerHomeActivity.this).setStartBookingID("");
                            AppCommon.getInstance(TrainerHomeActivity.this).setIsAvailable(1);
                            finishButton.setVisibility(View.GONE);
                            iniciarBtn.setVisibility(View.GONE);
                            setLockUnlock();
                            AppCommon.getInstance(TrainerHomeActivity.this).unRegisterAlarm(Integer.parseInt(todayBookingObj.getBookingId()));
                        }

                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }


    public void setLocation(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        saveUserLocation();
    }


    public void saveUserLocation() {
        PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
        call = pretoAppService.updateUserLocation(AppCommon.getInstance(this).getUserID(), latitude, longitude, "es");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    CommonStringResponse commonStringResponse = (CommonStringResponse) response.body();
                    if (commonStringResponse.getSuccess().equals("1")) {
                        AppCommon.getInstance(TrainerHomeActivity.this).setUserLongitude(Double.parseDouble(longitude));
                        AppCommon.getInstance(TrainerHomeActivity.this).setUserLatitude(Double.parseDouble(latitude));
                    }
                } else {
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private boolean createValidation() {
        boolean flag = true;
        String titleEditText = this.titleEditText.getText().toString().trim();
        String dateTextView = this.dateTextView.getText().toString().trim();
        String startTimeTextView = this.startTimeTextView.getText().toString().trim();
        String endTimeTextView = this.endTimeTextView.getText().toString().trim();
        String agendaType = String.valueOf(selRepetir);

        if (titleEditText.isEmpty()) {
            Toast.makeText(this, getString(R.string.pleaseEnterTitle), Toast.LENGTH_LONG).show();
            flag = false;
        } else if (dateTextView.isEmpty()) {
            Toast.makeText(this, getString(R.string.pleaseSelectDate), Toast.LENGTH_LONG).show();
            flag = false;
        } else if (startTimeTextView.isEmpty()) {
            Toast.makeText(this, getString(R.string.pleaseSelectStartTime), Toast.LENGTH_LONG).show();
            flag = false;
        } else if (endTimeTextView.isEmpty()) {
            Toast.makeText(this, getString(R.string.pleaseSelectEndTime), Toast.LENGTH_LONG).show();
            flag = false;
        }
        return flag;
    }

    void callingCreateAgenda(String userID, String agendaText, String agendaDate, String agendaStartTime,
                             String agendaEndTime, String agendaType) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.createAgenda(new CreateAgendaEntity(userID, agendaText, agendaDate, agendaStartTime, agendaEndTime, agendaType));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse commonResponse = (CommonResponse) response.body();
                        if (commonResponse.getSuccess() == 1) {
                            Toast.makeText(TrainerHomeActivity.this, getString(R.string.createdSuccessfully), Toast.LENGTH_LONG).show();
                        } else {
                            AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, commonResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    int agendaID;

    public void setUpdatePopUpValue(int adapterPosition) {
        workingRole = "update";
        eliminarBtn.setVisibility(View.VISIBLE);
        try {
            agendaID = Integer.parseInt(todayBookingObjectList.get(adapterPosition).getAgendaID());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        titleEditText.setText(todayBookingObjectList.get(adapterPosition).getAgendaText());
        dateTextView.setText(getDateInFormat(todayBookingObjectList.get(adapterPosition).getDate()));
        startTimeTextView.setText(getTimeInForamt(todayBookingObjectList.get(adapterPosition).getBookingStart()));
        endTimeTextView.setText(getTimeInForamt(todayBookingObjectList.get(adapterPosition).getBookingEnd()));
        repeatTypeSpinner.setSelection(Integer.parseInt(todayBookingObjectList.get(adapterPosition).getAgendaType()));
        personalEventView.setVisibility(View.VISIBLE);
        dateInputFormat = todayBookingObjectList.get(adapterPosition).getDate();
        startTimeInput = todayBookingObjectList.get(adapterPosition).getBookingStart();
        endTimeInput = todayBookingObjectList.get(adapterPosition).getBookingEnd();
        selRepetir = Integer.parseInt(todayBookingObjectList.get(adapterPosition).getAgendaType());
    }

    public String getTimeInForamt(String time) {
        String formatterTimeString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        try {
            Date formatDate = formatter.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatDate);
            formatter = new SimpleDateFormat("hh:mm a", Locale.US);
            formatterTimeString = formatter.format(formatDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatterTimeString;
    }

    public String getDateInFormat(String date) {
        String dateFormatterTimeString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date formatDate = formatter.parse(date);

            String dayOfTheWeek = (String) DateFormat.format("EEEE", formatDate);
            dayOfTheWeek = dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1);

            String monthString = (String) DateFormat.format("MMM", formatDate);
            monthString = monthString.substring(0, 1).toUpperCase() + monthString.substring(1);

            String currentDate = (String) DateFormat.format("dd", formatDate);

            dateFormatterTimeString = dayOfTheWeek + ", " + monthString + " " + currentDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatterTimeString;
    }

    @OnClick(R.id.eliminarBtn)
    void setEliminarBtn() {
        callingDeleteAPI(agendaID);
        personalEventView.setVisibility(View.GONE);
    }

    private void callingUpdateAPI(String userID, String agendaID, String agendaText, String agendaDate,
                                  String agendaStartTime, String agendaEndTime, String agendaType) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.updateAgenda(new CreateAgendaEntity(userID, agendaID, agendaText, agendaDate, agendaStartTime, agendaEndTime, agendaType));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse commonResponse = (CommonResponse) response.body();
                        if (commonResponse.getSuccess() == 1) {
                            getToodayBooking();
                        } else {
                            AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, commonResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    void callingDeleteAPI(int agendaID) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.deleteAgenda(AppCommon.getInstance(this).getUserID(), agendaID, AppCommon.getInstance(this).getSelectedLanguage());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonResponse commonResponse = (CommonResponse) response.body();
                        if (commonResponse.getSuccess() == 1) {
                            getToodayBooking();
                        } else {
                            AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, commonResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void setAlaram() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        long timeInMills = c.getTimeInMillis();
        Intent myIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void unRegisterAlarm() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private void getOnGoingSession() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerHomeActivity.this).isConnectingToInternet(TrainerHomeActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.getOnGoingSession(AppCommon.getInstance(this).getUserID(),"es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        OnGoingBookingResponse onGoingBookingResponse = (OnGoingBookingResponse) response.body();
                        if (onGoingBookingResponse.getSuccess() == 1) {
                            AppCommon.getInstance(TrainerHomeActivity.this).setStartBookingID(onGoingBookingResponse.getTodayBookingObject().getBookingId());
                            AppCommon.getInstance(TrainerHomeActivity.this).setStartTrainingObject(new Gson().toJson(onGoingBookingResponse.getTodayBookingObject()));
                            setInciarButtonAccordingly();
                        }else{
                            AppCommon.getInstance(TrainerHomeActivity.this).setStartBookingID("");
                            setInciarButtonAccordingly();
                        }
                    } else {
                        //AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
                    progressBar.setVisibility(View.GONE);
                    //AppCommon.getInstance(TrainerHomeActivity.this).showDialog(TrainerHomeActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(TrainerHomeActivity.this).clearNonTouchableFlags(TrainerHomeActivity.this);
            progressBar.setVisibility(View.GONE);
            //AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

}