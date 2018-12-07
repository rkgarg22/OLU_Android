package com.elisa.olu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elisa.olu.Firebase.MyFirebaseInstanceIDService;
import com.elisa.olu.LocationInfrastructure.FusedLocationService;
import com.facebook.CallbackManager;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.RegistrationResponse;
import ApiEntity.LoginSignupEntity;
import ApiObject.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import infrastructure.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends GenricActivity {

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @BindView(R.id.lastNameEditText)
    EditText lastNameEditText;

    @BindView(R.id.emailEditText)
    EditText emailEditText;

    @BindView(R.id.celularEditText)
    EditText celularEditText;

    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @BindView(R.id.confirmPasswordEditText)
    EditText confirmPasswordEditText;

    @BindView(R.id.acceptCheckBox)
    CheckBox acceptCheckBox;

    @BindView(R.id.genderPopup)
    RelativeLayout genderPopup;

    @BindView(R.id.male)
    TextView male;

    @BindView(R.id.genderTextView)
    TextView genderTextView;

    @BindView(R.id.dobTextView)
    TextView dobTextView;

    @BindView(R.id.women)
    TextView women;

    @BindView(R.id.notSpecified)
    TextView notSpecified;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    String street, city, state, zipcode;

    String role = "1";

    CallbackManager callbackManager;


    Call call;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date;

    String tokenId = "";

    MyFirebaseInstanceIDService myFirebaseInstanceIDService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
//        new FusedLocationTracker(this);
        startService(new Intent(getApplicationContext(), FusedLocationService.class));

        tokenId = FirebaseInstanceId.getInstance().getToken();
        if (tokenId != "") {
            AppCommon.getInstance(this).setTokenId(tokenId);
        }

        callbackManager = CallbackManager.Factory.create();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dobTextView.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.dobTextView)
    void setDobTextView() {
        new DatePickerDialog(RegisterActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    private void convertLatLngintoAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(RegisterActivity.this);
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 5);
            state = addressList.get(0).getAdminArea();
            city = addressList.get(0).getSubAdminArea();
            street = addressList.get(0).getAddressLine(1);
            zipcode = addressList.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.loginBtn)
    public void loginBtnClick(View v) {
        String fName = nameEditText.getText().toString();
        String lName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = celularEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String gender = genderTextView.getText().toString();
        String dob = dobTextView.getText().toString();

        if (fName.isEmpty()) {
            nameEditText.setError(getString(R.string.nameEmptyError));
        } else if (lName.isEmpty()) {
            lastNameEditText.setError(getString(R.string.lNameEmptyError));
        } else if (dob.equals("Fecha de nacimiento")) {
            Toast.makeText(this, "Por favor seleccione Fecha de nacimiento", Toast.LENGTH_SHORT).show();
        } else if (gender.equals("Género")) {
            Toast.makeText(this, "Por favor seleccione género", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.emailEmptyError));
        } else if (!AppCommon.getInstance(this).isEmailValid(email)) {
            emailEditText.setError(getString(R.string.emailValidError));
        } else if (phone.isEmpty()) {
            celularEditText.setError(getString(R.string.phoneEmptyError));
        } else if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.passportEmptyError));
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError(getString(R.string.passportEmptyError));
        } else if (password.equals(confirmPassword)) {
            if (acceptCheckBox.isChecked()) {
                register(fName, lName, email, phone, password, gender, dob);
            } else {
                Toast.makeText(this, "Please Accept the terms and condition to proceed further", Toast.LENGTH_SHORT).show();
            }
        } else {
            passwordEditText.setError(getString(R.string.passportValidError));
            confirmPasswordEditText.setError(getString(R.string.passportValidError));
        }
    }

    private void register(String fName, String lName, String email, final String phone, String password, final String gender, final String dob) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(RegisterActivity.this).isConnectingToInternet(RegisterActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            LoginSignupEntity loginSignupEntity = new LoginSignupEntity(fName, lName, "", email, password, "", String.valueOf(AppCommon.getInstance(this).getUserLatitude()), String.valueOf(AppCommon.getInstance(this).getUserLongitude()),
                    AppCommon.getInstance(this).getCurrentUser(), tokenId,
                    getResources().getString(R.string.android), gender, 0, phone, dob);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.registration(loginSignupEntity);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(RegisterActivity.this).clearNonTouchableFlags(RegisterActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        RegistrationResponse registrationResponse = (RegistrationResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            User user = registrationResponse.getUserEntity();
                            AppCommon.getInstance(RegisterActivity.this).setUserID(user.getUserID());
                            AppCommon.getInstance(RegisterActivity.this).setIsUserLogIn(true);
                            AppCommon.getInstance(RegisterActivity.this).setName(user.getFirstName());
                            AppCommon.getInstance(RegisterActivity.this).setLastName(user.getLastName());
                            AppCommon.getInstance(RegisterActivity.this).setProfilePicUrl(user.getUserImageUrl());
                            AppCommon.getInstance(RegisterActivity.this).setUserEmail(user.getEmailAddress());
                            AppCommon.getInstance(RegisterActivity.this).setCurrentUser(user.getRole());
                            AppCommon.getInstance(RegisterActivity.this).setGender(gender);
                            AppCommon.getInstance(RegisterActivity.this).setDOB(dob);
                            AppCommon.getInstance(RegisterActivity.this).setPhone(phone);

                            Intent intent = new Intent(RegisterActivity.this, TutorialsActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            AppCommon.getInstance(RegisterActivity.this).showDialog(RegisterActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(RegisterActivity.this).showDialog(RegisterActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(RegisterActivity.this).clearNonTouchableFlags(RegisterActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(RegisterActivity.this).showDialog(RegisterActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(RegisterActivity.this).clearNonTouchableFlags(RegisterActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.genderTextView)
    public void genderTextView() {
        genderPopup.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.male)
    public void setMale() {
        genderTextView.setText(male.getText().toString());
        genderPopup.setVisibility(View.GONE);
    }

    @OnClick(R.id.women)
    public void setWomen() {
        genderTextView.setText(women.getText().toString());
        genderPopup.setVisibility(View.GONE);
    }

    @OnClick(R.id.notSpecified)
    public void setNotSpecified() {
        genderTextView.setText(notSpecified.getText().toString());
        genderPopup.setVisibility(View.GONE);
    }

    @OnClick(R.id.termsLayout)
    public void termsLayout(View v) {
        Intent intent = new Intent(this, TermsActivity.class);
        intent.putExtra("isTerms", "28");
        startActivity(intent);
    }

    public void setLocation(double latitude, double longitude) {
        // this.latitude = String.valueOf(latitude);
        // this.longitude = String.valueOf(longitude);

    }
}
