package com.elisa.olu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.elisa.olu.LocationInfrastructure.FusedLocationTracker;
import com.facebook.CallbackManager;
import com.facebook.internal.ImageResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.RegistrationResponse;
import ApiEntity.CategoryEntity;
import ApiEntity.LoginSignupEntity;
import ApiObject.CategoriesListObject;
import ApiObject.CategoriesObject;
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

/**
 * Created by Ankit Chhabra on 6/3/2018.
 */

public class TrainerRegisterActivity extends GenricActivity {

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

    @BindView(R.id.descriptionEditText)
    EditText descriptionEditText;

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

    @BindView(R.id.categoryTextView)
    TextView categoryTextView;

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

    List<CategoriesListObject> categoriesObjectList = new ArrayList<>();
    List<CategoriesListObject> selectedCategoriesObjectList = new ArrayList<>();

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_register_layout);
        ButterKnife.bind(this);
       new FusedLocationTracker(this);
        callbackManager = CallbackManager.Factory.create();
        tokenId = FirebaseInstanceId.getInstance().getToken();
        if (getIntent().getStringExtra("imageUrl") != null) {
            imageUrl = getIntent().getStringExtra("imageUrl");
        }
        if (tokenId != "") {
            AppCommon.getInstance(this).setTokenId(tokenId);
        }
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
        new DatePickerDialog(TrainerRegisterActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void convertLatLngintoAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(TrainerRegisterActivity.this);
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
        String desc = descriptionEditText.getText().toString();

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
        } else if (desc.isEmpty()) {
            descriptionEditText.setError(getString(R.string.descriptionEmptyError));
        } else if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.passportEmptyError));
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError(getString(R.string.passportEmptyError));
        } else if (password.equals(confirmPassword)) {
            if (acceptCheckBox.isChecked()) {
                registerTrainer(imageUrl, fName, lName, email, phone, password, gender, dob, desc);
            } else {
                Toast.makeText(this, "Por favor, acepte los términos y condiciones para continuar", Toast.LENGTH_SHORT).show();
            }
        } else {
            passwordEditText.setError(getString(R.string.passportValidError));
            confirmPasswordEditText.setError(getString(R.string.passportValidError));
        }
    }

    private void registerTrainer(String imageUrl, String fName, String lName, String email, String phone, String password, String gen, String dateOFBirth, String desc) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            MultipartBody.Part image = null;
            if (!imageUrl.isEmpty()) {
                File file = FileUtils.getFile(TrainerRegisterActivity.this, Uri.parse(imageUrl));
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("imageUrl", file.getName(), requestFile);
            }
            for (int i = 0; i < categoriesObjectList.size(); i++) {
                selectedCategoriesObjectList.add(new CategoriesListObject(categoriesObjectList.get(i).getCategryID(), categoriesObjectList.get(i).getSinglePrice(), categoriesObjectList.get(i).getGroupPrice2(),
                        categoriesObjectList.get(i).getGroupPrice3(), categoriesObjectList.get(i).getGroupPrice4(), categoriesObjectList.get(i).getCompanyPrice()));
            }
            RequestBody firstName = RequestBody.create(MultipartBody.FORM, fName);
            RequestBody lastName = RequestBody.create(MultipartBody.FORM, lName);
            RequestBody emailAddress = RequestBody.create(MultipartBody.FORM, email);
            RequestBody Phone = RequestBody.create(MultipartBody.FORM, phone);
            RequestBody pass = RequestBody.create(MultipartBody.FORM, password);
            RequestBody lat = RequestBody.create(MultipartBody.FORM, String.valueOf(AppCommon.getInstance(this).getUserLatitude()));
            RequestBody lang = RequestBody.create(MultipartBody.FORM, String.valueOf(AppCommon.getInstance(this).getUserLongitude()));
            RequestBody firebase = RequestBody.create(MultipartBody.FORM, tokenId);
            RequestBody gender = RequestBody.create(MultipartBody.FORM, gen);
            RequestBody dob = RequestBody.create(MultipartBody.FORM, dateOFBirth);
            RequestBody description = RequestBody.create(MultipartBody.FORM, desc);
            RequestBody category = RequestBody.create(MultipartBody.FORM, new Gson().toJson(categoriesObjectList));
            RequestBody deviceType = RequestBody.create(MultipartBody.FORM, getString(R.string.android));
            call = pretoAppService.registrationTrainer(firstName, lastName, emailAddress, pass, lat, lang, firebase, deviceType, gender, dob, Phone, dob, description, category, image);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerRegisterActivity.this).clearNonTouchableFlags(TrainerRegisterActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        RegistrationResponse registrationResponse = (RegistrationResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
//                            User user = registrationResponse.getUserEntity();
//                            AppCommon.getInstance(TrainerRegisterActivity.this).setUserID(user.getUserID());
//                            AppCommon.getInstance(TrainerRegisterActivity.this).setIsUserLogIn(true);
//                            AppCommon.getInstance(TrainerRegisterActivity.this).setName(user.getFirstName());
//                            AppCommon.getInstance(TrainerRegisterActivity.this).setProfilePicUrl(user.getUserImageUrl());
//                            AppCommon.getInstance(TrainerRegisterActivity.this).setUserEmail(user.getEmailAddress());
//                            AppCommon.getInstance(TrainerRegisterActivity.this).setCurrentUser(user.getRole());
//                            if (AppCommon.getInstance(TrainerRegisterActivity.this).getCurrentUser() == 2) {
//                                Intent intent = new Intent(TrainerRegisterActivity.this, HomeActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(TrainerRegisterActivity.this, TrainerHomeActivity.class);
//                                startActivity(intent);
//                            }
//                            finishAffinity();
                            verificationPopup();
                        } else {
                            AppCommon.getInstance(TrainerRegisterActivity.this).showDialog(TrainerRegisterActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(TrainerRegisterActivity.this).showDialog(TrainerRegisterActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerRegisterActivity.this).clearNonTouchableFlags(TrainerRegisterActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerRegisterActivity.this).showDialog(TrainerRegisterActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(TrainerRegisterActivity.this).clearNonTouchableFlags(TrainerRegisterActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }


    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.termsLayout)
    public void termsLayout(View v) {
        Intent intent = new Intent(this, TermsActivity.class);
        intent.putExtra("isTerms","21");
        startActivity(intent);
    }

    @OnClick({R.id.categoryLayout, R.id.categoryTextView})
    public void categoryLayout(View v) {
        Intent intent = new Intent(this, TrainerCategoryActivity.class);
        intent.putExtra("category", new Gson().toJson(new CategoryEntity(categoriesObjectList)));
        startActivityForResult(intent, 123);
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

    public void setLocation(double latitude, double longitude) {
        //this.latitude = String.valueOf(latitude);
        //this.longitude = String.valueOf(longitude);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 123) {
                CategoryEntity categoryEntity = new Gson().fromJson(data.getStringExtra("category"), CategoryEntity.class);
                categoriesObjectList = categoryEntity.getCategoriesListObjectList();
                String category = "";
                for (int i = 0; i < categoriesObjectList.size(); i++) {
                    category = category + categoriesObjectList.get(i).getCatergoryName() + ", ";
                }
                categoryTextView.setText(category);
            }
        }
    }

    public void verificationPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.trainer_registration_verification));
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
