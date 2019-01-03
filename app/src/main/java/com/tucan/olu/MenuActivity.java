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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import APIResponse.MyProfileResponse;
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

public class MenuActivity extends GenricActivity {

    @BindView(R.id.fName)
    TextView fName;

    @BindView(R.id.lName)
    TextView lName;

    @BindView(R.id.wallet)
    TextView wallet;

    @BindView(R.id.editProfile)
    ImageView editProfile;

    @BindView(R.id.userImage)
    SimpleDraweeView userImage;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.categoryImage1)
    ImageView categoryImage1;

    @BindView(R.id.categoryImage2)
    ImageView categoryImage2;

    @BindView(R.id.categoryImage3)
    ImageView categoryImage3;

    @BindView(R.id.categoryText1)
    TextView categoryText1;

    @BindView(R.id.categoryText2)
    TextView categoryText2;

    @BindView(R.id.categoryText3)
    TextView categoryText3;

    @BindView(R.id.promoCodeLayout)
    LinearLayout promoCodeLayout;

    @BindView(R.id.promoCodeEditText)
    EditText promoCodeEditText;

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;

    private static final int UPDATE_PROFILE = 100;
    Uri outPutfileUri = Uri.parse("");
    String isMedia = "0";

    MyProfileObject myProfileObject;

    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        getProfile();
    }

    private void getProfile() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(MenuActivity.this).isConnectingToInternet(MenuActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.myProfile(AppCommon.getInstance(this).getUserID(), "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        MyProfileResponse myProfileResponse = (MyProfileResponse) response.body();
                        if (myProfileResponse.getSuccess().equals("1")) {
                            setUpProfile(myProfileResponse.getMyProfileObject());
                        }
                    } else {
                        AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    private void setUpProfile(MyProfileObject myProfileObject) {
        this.myProfileObject = myProfileObject;
        fName.setText(myProfileObject.getFirstName());
        lName.setText(myProfileObject.getLastName());
        wallet.setText(myProfileObject.getWallet() + ".000");
        if (myProfileObject.getImage() != null)
            userImage.setImageURI(myProfileObject.getImage());
        else
            userImage.setImageURI(Uri.parse(AppCommon.getInstance(this).getProfilePicUrl()));
        setupCatergories();
    }

    private void setupCatergories() {
        if (myProfileObject.getCategoriesObjectList().size() == 1) {
            setupCatergoryImage(myProfileObject.getCategoriesObjectList().get(0).getCategoryID(), categoryImage1, categoryText1);
        } else if (myProfileObject.getCategoriesObjectList().size() == 2) {
            setupCatergoryImage(myProfileObject.getCategoriesObjectList().get(0).getCategoryID(), categoryImage1, categoryText1);
            setupCatergoryImage(myProfileObject.getCategoriesObjectList().get(1).getCategoryID(), categoryImage2, categoryText2);
        } else if (myProfileObject.getCategoriesObjectList().size() == 3) {
            setupCatergoryImage(myProfileObject.getCategoriesObjectList().get(0).getCategoryID(), categoryImage1, categoryText1);
            setupCatergoryImage(myProfileObject.getCategoriesObjectList().get(1).getCategoryID(), categoryImage2, categoryText2);
            setupCatergoryImage(myProfileObject.getCategoriesObjectList().get(2).getCategoryID(), categoryImage3, categoryText3);
        }
    }

    private void setupCatergoryImage(String categoryID, ImageView categoryImage, TextView categoryName) {
        switch (categoryID) {
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
                    MenuActivity.this.finish();
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
                File file = FileUtils.getFile(MenuActivity.this, outPutfileUri);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("imageUrl", file.getName(), requestFile);
            }
            RequestBody userID = RequestBody.create(MultipartBody.FORM, String.valueOf(AppCommon.getInstance(this).getUserID()));
            call = pretoAppService.updateUserImage(userID, image);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse registrationResponse = (CommonIntResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            AppCommon.getInstance(MenuActivity.this).setProfilePicUrl(String.valueOf(outPutfileUri));
                            finish();
                        } else {
                            AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @OnClick({R.id.editProfile, R.id.editarText})
    public void setEditProfile() {
        Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
        editProfileIntent.putExtra("profileObject", new Gson().toJson(myProfileObject));
        editProfileIntent.putExtra("imageUrl", outPutfileUri.toString());
        editProfileIntent.putExtra("type", "0");
        startActivityForResult(editProfileIntent, UPDATE_PROFILE);
    }

    @OnClick({R.id.misReservesLayout, R.id.misReserverBtn})
    public void historyLayout(View v) {
        startActivity(new Intent(this, HistoricalActivity.class));
    }

    @OnClick({R.id.misLayout, R.id.pagosReservarBtn})
    public void misLayout(View v) {
        startActivity(new Intent(this, MisActivity.class));
    }

    @OnClick(R.id.promoLayout)
    public void promoLayout(View v) {
        promoCodeLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.crossBtn)
    public void crossBtnClick(View v) {
        promoCodeLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.submitBtn)
    public void submit(View v) {
        if (promoCodeEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el código de promoción", Toast.LENGTH_SHORT).show();
        } else {
            addPromoCode(promoCodeEditText.getText().toString());
            promoCodeLayout.setVisibility(View.GONE);
        }
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
            ActivityCompat.requestPermissions(MenuActivity.this,
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
        outPutfileUri = FileProvider.getUriForFile(MenuActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    public void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MenuActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MenuActivity.this,
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
            fName.setText(AppCommon.getInstance(this).getName());
            lName.setText(AppCommon.getInstance(this).getLastName());
            userImage.setImageURI(AppCommon.getInstance(this).getProfilePicUrl());
            isMedia = "0";
        }

    }

    public void addPromoCode(String code) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(MenuActivity.this).isConnectingToInternet(MenuActivity.this)) {
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.promoCode(AppCommon.getInstance(this).getUserID(), code);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                        if (commonIntResponse.getSuccess() == 1) {
                            promoCodeLayout.setVisibility(View.GONE);
                            AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, "código de promoción agregado con éxito");
                        } else {
                            AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, commonIntResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(MenuActivity.this).setUpdate("0");
                    AppCommon.getInstance(MenuActivity.this).showDialog(MenuActivity.this, getResources().getString(R.string.network_error));
                }
            });
        } else {
            AppCommon.getInstance(MenuActivity.this).clearNonTouchableFlags(MenuActivity.this);
            progressBar.setVisibility(View.VISIBLE);
            AppCommon.getInstance(MenuActivity.this).setUpdate("0");
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }
}
