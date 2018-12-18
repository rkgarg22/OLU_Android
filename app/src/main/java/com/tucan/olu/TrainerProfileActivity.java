package com.tucan.olu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.TrainerProfileResponse;

import ApiObject.TrainerProfileObject;
import CustomControl.AvenirNextCondensedRegularTextView;
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

public class TrainerProfileActivity extends GenricActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.userImage)
    SimpleDraweeView userImage;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.userName)
    AvenirNextCondensedRegularTextView userNameTextView;

    @BindView(R.id.phoneNumberTextView)
    AvenirNextCondensedRegularTextView phoneNumberTextView;

    @BindView(R.id.emailtextView)
    AvenirNextCondensedRegularTextView emailtextView;

    Call call;

    TrainerProfileResponse myProfileResponse;

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;

    private static final int UPDATE_PROFILE = 100;
    Uri outPutfileUri = Uri.parse("");
    String isMedia = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);
        ButterKnife.bind(this);
        getProfile();
        ratingBar.setClickable(false);
    }


    @OnClick(R.id.editProfile)
    public void setEditProfile() {
        Intent editProfileIntent = new Intent(this, TrainerEditProfileActivity.class);
        editProfileIntent.putExtra("profileObject", new Gson().toJson(myProfileResponse.getTrainerProfileObject()));
        editProfileIntent.putExtra("imageUrl",outPutfileUri.toString());
        startActivityForResult(editProfileIntent, UPDATE_PROFILE);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        if (isMedia.equals("1")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.updateImageText));
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
                    updateImage();
                }
            });
            builder.show();
        } else {
            this.finish();
        }

    }

    private void updateImage() {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            MultipartBody.Part image = null;
            if (outPutfileUri != null) {
                File file = FileUtils.getFile(TrainerProfileActivity.this, outPutfileUri);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("imageUrl", file.getName(), requestFile);
            }
            RequestBody userID = RequestBody.create(MultipartBody.FORM, String.valueOf(AppCommon.getInstance(this).getUserID()));
            call = pretoAppService.updateUserImage(userID, image);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerProfileActivity.this).clearNonTouchableFlags(TrainerProfileActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse registrationResponse = (CommonIntResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            AppCommon.getInstance(TrainerProfileActivity.this).setProfilePicUrl(String.valueOf(outPutfileUri));
                            finish();
                        } else {
                            AppCommon.getInstance(TrainerProfileActivity.this).showDialog(TrainerProfileActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(TrainerProfileActivity.this).showDialog(TrainerProfileActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerProfileActivity.this).clearNonTouchableFlags(TrainerProfileActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerProfileActivity.this).showDialog(TrainerProfileActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(TrainerProfileActivity.this).clearNonTouchableFlags(TrainerProfileActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }


    @OnClick({R.id.pagosLayout, R.id.pagosBtn})
    public void pagosClick(View v) {
        Intent paymentHistortyIntent = new Intent(this, TrainerPaymentHistoryActivity.class);
        startActivity(paymentHistortyIntent);
    }

    @OnClick({R.id.sesionLayout, R.id.sessionBtn})
    public void sessionClick(View v) {
        Intent paymentHistortyIntent = new Intent(this, HistoricalActivity.class);
        startActivity(paymentHistortyIntent);
    }

    private void getProfile() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(TrainerProfileActivity.this).isConnectingToInternet(TrainerProfileActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.getTrainerProfile(String.valueOf(AppCommon.getInstance(this).getUserID()));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TrainerProfileActivity.this).clearNonTouchableFlags(TrainerProfileActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        myProfileResponse = (TrainerProfileResponse) response.body();
                        if (myProfileResponse.getSuccess() == 1) {
                            setUpProfile(myProfileResponse.getTrainerProfileObject());
                        }
                    } else {
                        AppCommon.getInstance(TrainerProfileActivity.this).showDialog(TrainerProfileActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(TrainerProfileActivity.this).clearNonTouchableFlags(TrainerProfileActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(TrainerProfileActivity.this).showDialog(TrainerProfileActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(TrainerProfileActivity.this).clearNonTouchableFlags(TrainerProfileActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    private void setUpProfile(TrainerProfileObject myProfileObject) {
        userNameTextView.setText(myProfileObject.getFirstName() + " " + myProfileObject.getLastName());
        phoneNumberTextView.setText(myProfileObject.getPhone());
        emailtextView.setText(myProfileObject.getEmailAddress());
        userImage.setImageURI(Uri.parse(myProfileObject.getUserImageUrl()));
        ratingBar.setRating(Integer.parseInt(myProfileObject.getReviews()));
    }


    @OnClick(R.id.userImage)
    public void chooseFile() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_option_dialog);
        dialog.setTitle(getResources().getString(R.string.app_name));
        TextView camera = (TextView) dialog.findViewById(R.id.camera);
        TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
                dialog.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGalleryPermission();
                dialog.dismiss();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TrainerProfileActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    200);
        } else {
            startCameraIntent();
        }
    }

    public void startCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),
                "attachment.jpg");
        outPutfileUri = FileProvider.getUriForFile(TrainerProfileActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    public void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(TrainerProfileActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TrainerProfileActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    201);
        } else {
            startGalleryIntent();
        }
    }

    public void startGalleryIntent() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                outPutfileUri = data.getData();
                userImage.setImageURI(outPutfileUri);
                isMedia = "1";
            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    outPutfileUri = Uri.parse(url);
                    userImage.setImageURI(outPutfileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isMedia = "1";
            }
        }

        if (requestCode == UPDATE_PROFILE) {
            userNameTextView.setText(AppCommon.getInstance(this).getName() + " " + AppCommon.getInstance(this).getLastName());
            userImage.setImageURI(AppCommon.getInstance(this).getProfilePicUrl());
            isMedia = "0";
        }

    }

}
