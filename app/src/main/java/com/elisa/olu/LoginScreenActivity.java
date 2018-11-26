package com.elisa.olu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elisa.olu.Firebase.MyFirebaseInstanceIDService;
import com.elisa.olu.LocationInfrastructure.FusedLocationTracker;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.RegistrationResponse;
import ApiEntity.LoginSignupEntity;
import ApiObject.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  LoginScreenActivity extends GenricActivity {

    @BindView(R.id.text2)
    TextView text2;

    @BindView(R.id.facebookBtnLayout)
    LinearLayout facebookBtnLayout;

    @BindView(R.id.emailEditText)
    EditText emailEditText;

    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @BindView(R.id.login_button)
    LoginButton login_button;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Call call;

    private AccessTokenTracker accessTokenTracker;

    private AccessToken accessToken;

    String firstName, lastName, email, gender, password, imageUrl;

    String facebookId = "";



    CallbackManager callbackManager;

    String latitude, longitude, street, city, state, zipcode;

    private static final String EMAIL = "email";

    String tokenId = "";

    MyFirebaseInstanceIDService myFirebaseInstanceIDService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        ButterKnife.bind(this);
        new FusedLocationTracker(this);
        tokenId = FirebaseInstanceId.getInstance().getToken();
        if (tokenId != "") {
            AppCommon.getInstance(this).setTokenId(tokenId);
        }

        if (AppCommon.getInstance(this).getCurrentUser() == 2) {
            text2.setText(getString(R.string.user_type1_text));
            facebookBtnLayout.setVisibility(View.VISIBLE);
        } else {
            text2.setText(getString(R.string.user_type2_text));
            facebookBtnLayout.setVisibility(View.GONE);
        }

        callbackManager = CallbackManager.Factory.create();

    }


    @OnClick(R.id.facebookBtnLayout)
    public void openFacebook() {
        authorizeFaceBook();
    }

    private void authorizeFaceBook() {
        LoginManager.getInstance().logInWithReadPermissions(LoginScreenActivity.this, Arrays.asList("email", "public_profile"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                getFacebookData(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginScreenActivity.this, "Cancelled by user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
                error.printStackTrace();
            }
        });
    }

    private void getFacebookData(LoginResult loginResult) {
        progressBar.setVisibility(View.VISIBLE);
        final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.i("Response", response.toString());
                    facebookId = object.getString("id");
                    if (response.getJSONObject().has("email")) {
                        email = response.getJSONObject().getString("email");
                        firstName = object.getString("first_name");
                        lastName = object.getString("last_name");
                        imageUrl = "https://graph.facebook.com/" + facebookId + "/picture?type=normal";
                        register(firstName, lastName, email, "", "olu@123", "", "");
                    } else {
                        AppCommon.getInstance(LoginScreenActivity.this).showDialog(LoginScreenActivity.this, "Your email can't be fetched \n" + "from facebook, Please use sign up");
                    }


                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void register(String fName, String lName, String email, String phone, String password, String gender, String dob) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(LoginScreenActivity.this).isConnectingToInternet(LoginScreenActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            //  final String token = myFirebaseInstanceIDService.getDeviceToken();
            LoginSignupEntity loginSignupEntity = new LoginSignupEntity(fName, lName, facebookId, email, password, imageUrl, latitude, longitude,
                    AppCommon.getInstance(this).getCurrentUser(), tokenId,
                    getResources().getString(R.string.android), gender, 0, phone, dob);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.registration(loginSignupEntity);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(LoginScreenActivity.this).clearNonTouchableFlags(LoginScreenActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        RegistrationResponse registrationResponse = (RegistrationResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            User user = registrationResponse.getUserEntity();
                            AppCommon.getInstance(LoginScreenActivity.this).setUserID(user.getUserID());
                            AppCommon.getInstance(LoginScreenActivity.this).setIsUserLogIn(true);
                            AppCommon.getInstance(LoginScreenActivity.this).setName(user.getFirstName());
                            AppCommon.getInstance(LoginScreenActivity.this).setProfilePicUrl(user.getUserImageUrl());
                            AppCommon.getInstance(LoginScreenActivity.this).setUserEmail(user.getEmailAddress());
                            AppCommon.getInstance(LoginScreenActivity.this).setCurrentUser(user.getRole());
                            AppCommon.getInstance(LoginScreenActivity.this).setLastName(user.getLastName());
                            AppCommon.getInstance(LoginScreenActivity.this).setDOB(user.getDob());
                            AppCommon.getInstance(LoginScreenActivity.this).setGender(user.getGender());
                            AppCommon.getInstance(LoginScreenActivity.this).setPhone(user.getPhone());

                            try {
                                AppCommon.getInstance(LoginScreenActivity.this).setUserLatitude(Double.parseDouble(latitude));
                                AppCommon.getInstance(LoginScreenActivity.this).setUserLongitude(Double.parseDouble(longitude));
                            } catch (Exception e) {

                            }
                            Intent intent = new Intent(LoginScreenActivity.this, TutorialsActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            AppCommon.getInstance(LoginScreenActivity.this).showDialog(LoginScreenActivity.this, registrationResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(LoginScreenActivity.this).showDialog(LoginScreenActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(LoginScreenActivity.this).clearNonTouchableFlags(LoginScreenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(LoginScreenActivity.this).showDialog(LoginScreenActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(LoginScreenActivity.this).clearNonTouchableFlags(LoginScreenActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.emailBtnLayout)
    public void emailBtnClick(View v) {
        if (AppCommon.getInstance(this).getCurrentUser() == 2) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PhotoTarinerActivity.class);
            startActivity(intent);
        }
    }


    @OnClick(R.id.forgotLayout)
    public void forgotLayout(View v) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.loginBtn)
    public void loginBtnClick(View v) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.emailEmptyError));
        } else if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.passportEmptyError));
        } else {
            login(email, password);
        }
    }

    private void login(String email, String password) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(LoginScreenActivity.this).isConnectingToInternet(LoginScreenActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            LoginSignupEntity loginSignupEntity = new LoginSignupEntity(email, password, tokenId, getResources().getString(R.string.android),AppCommon.getInstance(this).getCurrentUser());
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.login(loginSignupEntity);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(LoginScreenActivity.this).clearNonTouchableFlags(LoginScreenActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        RegistrationResponse registrationResponse = (RegistrationResponse) response.body();
                        if (registrationResponse.getSuccess() == 1) {
                            User user = registrationResponse.getUserEntity();
                            if(user.getIsApprove()==1) {
                                AppCommon.getInstance(LoginScreenActivity.this).setUserID(user.getUserID());
                                AppCommon.getInstance(LoginScreenActivity.this).setIsUserLogIn(true);
                                AppCommon.getInstance(LoginScreenActivity.this).setName(user.getFirstName());
                                AppCommon.getInstance(LoginScreenActivity.this).setProfilePicUrl(user.getUserImageUrl());
                                AppCommon.getInstance(LoginScreenActivity.this).setUserEmail(user.getEmailAddress());
                                AppCommon.getInstance(LoginScreenActivity.this).setCurrentUser(user.getRole());
                                AppCommon.getInstance(LoginScreenActivity.this).setDOB(user.getDob());
                                AppCommon.getInstance(LoginScreenActivity.this).setGender(user.getGender());
                                AppCommon.getInstance(LoginScreenActivity.this).setPhone(user.getPhone());
                                AppCommon.getInstance(LoginScreenActivity.this).setLastName(user.getLastName());
                                try {
                                    AppCommon.getInstance(LoginScreenActivity.this).setUserLatitude(Double.parseDouble(user.getLatitude()));
                                    AppCommon.getInstance(LoginScreenActivity.this).setUserLongitude(Double.parseDouble(user.getLongitude()));
                                } catch (Exception e) {

                                }
                                if (AppCommon.getInstance(LoginScreenActivity.this).getCurrentUser() == 2) {
                                    Intent intent = new Intent(LoginScreenActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(LoginScreenActivity.this, TrainerHomeActivity.class);
                                    startActivity(intent);
                                }
                                finishAffinity();
                            }else{
                                verificationPopup();
                            }

                        } else {
                            AppCommon.getInstance(LoginScreenActivity.this).showDialog(LoginScreenActivity.this, registrationResponse.getError());
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        AppCommon.getInstance(LoginScreenActivity.this).showDialog(LoginScreenActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(LoginScreenActivity.this).clearNonTouchableFlags(LoginScreenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(LoginScreenActivity.this).showDialog(LoginScreenActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(LoginScreenActivity.this).clearNonTouchableFlags(LoginScreenActivity.this);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
        }
    }

    public void setLocation(double latitude, double longitude) {
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
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
