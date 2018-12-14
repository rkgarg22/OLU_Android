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
import com.facebook.CallbackManager;
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
import APIResponse.CommonIntResponse;
import APIResponse.CommonStringResponse;
import APIResponse.RegistrationResponse;
import ApiEntity.CategoryEntity;
import ApiObject.CategoriesListObject;
import ApiObject.CategoriesObject;
import ApiObject.TrainerDetailObject;
import ApiObject.TrainerProfileObject;
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

public class TrainerEditProfileActivity extends GenricActivity {

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @BindView(R.id.lastNameEditText)
    EditText lastNameEditText;

    @BindView(R.id.emailEditText)
    EditText emailEditText;

    @BindView(R.id.celularEditText)
    EditText celularEditText;

    @BindView(R.id.descriptionEditText)
    EditText descriptionEditText;

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


    Call call;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    List<CategoriesListObject> categoriesObjectList = new ArrayList<>();
    TrainerProfileObject trainerDetailObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_edit_profile_layout);
        ButterKnife.bind(this);
        trainerDetailObject = new Gson().fromJson(getIntent().getStringExtra("profileObject"), TrainerProfileObject.class);
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

        nameEditText.setText(trainerDetailObject.getFirstName());
        lastNameEditText.setText(trainerDetailObject.getLastName());
        emailEditText.setText(trainerDetailObject.getEmailAddress());
        celularEditText.setText(trainerDetailObject.getPhone());
        dobTextView.setText(trainerDetailObject.getDob());
        genderTextView.setText(trainerDetailObject.getGender());
        descriptionEditText.setText(trainerDetailObject.getDescription());

        categoriesObjectList = trainerDetailObject.getCategoriesObjectList();
//        for (int i = 0; i < objList.size(); i++) {
//            CategoriesObject obj = objList.get(i);
//            CategoriesListObject listObject = new CategoriesListObject(obj.getCategoryName(), obj.getCategoryID()
//                    , obj.getSinglePrice(), obj.getGroupPrice2(), obj.getGroupPrice3(), obj.getGroupPrice4(), obj.getCompanyPrice());
//            categoriesObjectList.add(listObject);
//        }
        String category = "";
        for (int i = 0; i < categoriesObjectList.size(); i++) {
            category = category + categoriesObjectList.get(i).getCatergoryName() + ", ";
        }
        categoryTextView.setText(category);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dobTextView.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.dobTextView)
    void setDobTextView() {
        new DatePickerDialog(TrainerEditProfileActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    @OnClick(R.id.updateBtn)
    public void updateBtnClick(View v) {
        String fName = nameEditText.getText().toString();
        String lName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = celularEditText.getText().toString();
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
        } else {
            updateTrainerProfile(getIntent().getStringExtra("imageUrl"), fName, lName, phone, gender, dob, desc);
        }
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
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

    private void updateTrainerProfile(String imageUrl, String fName, String lName, String phone, String gen, String dateOFBirth, String desc) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            MultipartBody.Part image = null;
            if (!imageUrl.isEmpty()) {
                File file = FileUtils.getFile(TrainerEditProfileActivity.this, Uri.parse(imageUrl));
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("imageUrl", file.getName(), requestFile);
            }
            RequestBody userID = RequestBody.create(MultipartBody.FORM, String.valueOf(AppCommon.getInstance(this).getUserID()));
            RequestBody firstName = RequestBody.create(MultipartBody.FORM, fName);
            RequestBody lastName = RequestBody.create(MultipartBody.FORM, lName);
            RequestBody Phone = RequestBody.create(MultipartBody.FORM, phone);
            RequestBody gender = RequestBody.create(MultipartBody.FORM, gen);
            RequestBody dob = RequestBody.create(MultipartBody.FORM, dateOFBirth);
            RequestBody description = RequestBody.create(MultipartBody.FORM, desc);
            RequestBody category = RequestBody.create(MultipartBody.FORM, new Gson().toJson(categoriesObjectList));
            call = pretoAppService.updateTrainer(userID, firstName, lastName, gender, Phone, dob, description, category, image);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerEditProfileActivity.this).clearNonTouchableFlags(TrainerEditProfileActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse registrationResponse = (CommonIntResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            showDialog();
                        } else {
                            AppCommon.getInstance(TrainerEditProfileActivity.this).showDialog(TrainerEditProfileActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(TrainerEditProfileActivity.this).showDialog(TrainerEditProfileActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerEditProfileActivity.this).clearNonTouchableFlags(TrainerEditProfileActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerEditProfileActivity.this).showDialog(TrainerEditProfileActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(TrainerEditProfileActivity.this).clearNonTouchableFlags(TrainerEditProfileActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.profile_verificationmessage));
        builder.setNegativeButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                TrainerEditProfileActivity.this.finish();

            }
        });

        builder.show();
    }

}
