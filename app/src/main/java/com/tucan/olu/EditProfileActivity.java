package com.tucan.olu;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import ApiObject.MyProfileObject;
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

public class EditProfileActivity extends GenricActivity {

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @BindView(R.id.lastNameEditText)
    EditText lastNameEditText;

    @BindView(R.id.emailEditText)
    EditText emailEditText;

    @BindView(R.id.celularEditText)
    EditText celularEditText;

    @BindView(R.id.dobTextView)
    TextView dobTextView;

    @BindView(R.id.genderTextView)
    TextView genderTextView;

    @BindView(R.id.genderPopup)
    RelativeLayout genderPopup;

    @BindView(R.id.male)
    TextView male;

    @BindView(R.id.women)
    TextView women;

    @BindView(R.id.notSpecified)
    TextView notSpecified;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    MyProfileObject myProfileObject;

    String imageUrl;
    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("profileObject") != null) {
            myProfileObject = new Gson().fromJson(getIntent().getStringExtra("profileObject"), MyProfileObject.class);
            nameEditText.setText(myProfileObject.getFirstName());
            lastNameEditText.setText(myProfileObject.getLastName());
            emailEditText.setText(AppCommon.getInstance(this).getUserEmail());
            celularEditText.setText(AppCommon.getInstance(this).getPhone());
            dobTextView.setText(AppCommon.getInstance(this).getDOb());
            genderTextView.setText(AppCommon.getInstance(this).getGender());
        }

        if (getIntent().getStringExtra("imageUrl") != null) {
            imageUrl = getIntent().getStringExtra("imageUrl");
        }
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.dobTextView)
    void setDobTextView() {
        final Calendar myCalendar = Calendar.getInstance();
        new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dobTextView.setText(sdf.format(myCalendar.getTime()));
            }
        }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

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

    @OnClick(R.id.updateBtnClick)
    public void updateBtnClick(View v) {
        String fName = nameEditText.getText().toString();
        String lName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = celularEditText.getText().toString();
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
        } else {
            updateUserProfiel(imageUrl, fName, lName, phone, gender, dob);
        }
    }

    private void updateUserProfiel(String imageUrl, final String fName, final String lName, final String phone, final String gen, final String dateOFBirth) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            MultipartBody.Part image = null;
            if (!imageUrl.isEmpty()) {
                File file = FileUtils.getFile(EditProfileActivity.this, Uri.parse(imageUrl));
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("imageUrl", file.getName(), requestFile);
            }

            RequestBody userID = RequestBody.create(MultipartBody.FORM, String.valueOf(AppCommon.getInstance(this).getUserID()));
            final RequestBody firstName = RequestBody.create(MultipartBody.FORM, fName);
            final RequestBody lastName = RequestBody.create(MultipartBody.FORM, lName);
            RequestBody Phone = RequestBody.create(MultipartBody.FORM, phone);
            final RequestBody gender = RequestBody.create(MultipartBody.FORM, gen);
            final RequestBody dob = RequestBody.create(MultipartBody.FORM, dateOFBirth);
            call = pretoAppService.updateUserProfile(userID, firstName, lastName, gender, Phone, dob, image);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(EditProfileActivity.this).clearNonTouchableFlags(EditProfileActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse registrationResponse = (CommonIntResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            AppCommon.getInstance(EditProfileActivity.this).setName(fName);
                            AppCommon.getInstance(EditProfileActivity.this).setLastName(lName);
                            AppCommon.getInstance(EditProfileActivity.this).setGender(gen);
                            AppCommon.getInstance(EditProfileActivity.this).setPhone(phone);
                            AppCommon.getInstance(EditProfileActivity.this).setDOB(dateOFBirth);
                            AppCommon.getInstance(EditProfileActivity.this).setProfilePicUrl(registrationResponse.getResult());
                            showDialog();
                        } else {
                            AppCommon.getInstance(EditProfileActivity.this).showDialog(EditProfileActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(EditProfileActivity.this).showDialog(EditProfileActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(EditProfileActivity.this).clearNonTouchableFlags(EditProfileActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(EditProfileActivity.this).showDialog(EditProfileActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(EditProfileActivity.this).clearNonTouchableFlags(EditProfileActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getResources().getString(R.string.profile_update));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                EditProfileActivity.this.finish();
            }
        });

        builder.show();
    }
}
