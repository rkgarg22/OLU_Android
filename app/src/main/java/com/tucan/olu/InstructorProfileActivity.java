package com.tucan.olu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.UserDetailResponse;
import Adapter.CategoryTagAdapter;

import ApiObject.CategoriesObject;
import ApiObject.TrainerDetailObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorProfileActivity extends GenricActivity {

    String trainerId;

    Call call;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.categoryName)
    TextView categoryName;

    @BindView(R.id.firstName)
    TextView firstName;

    @BindView(R.id.lastName)
    TextView lastName;

    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.fullDescription)
    TextView fullDescription;

    @BindView(R.id.smallDescription)
    TextView smallDescription;

    @BindView(R.id.priceTextView)
    TextView priceTextView;

    @BindView(R.id.reseverBtn)
    Button reseverBtn;

    @BindView(R.id.userImage)
    SimpleDraweeView userImage;

    String categoryId = "";

    int date;

    String bookingDate = "";

    int month;

    int noPerson;

    String time = "";

    String isDate;

    String bookingType = "";

    TrainerDetailObject userListObject;

    CategoryTagAdapter categoryTagAdapter;

    String priceGroup;

    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_profile);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        if (getIntent().getStringExtra("trainerId") != null) {
            trainerId = getIntent().getStringExtra("trainerId");
            isDate = getIntent().getStringExtra("isDate");
            categoryId = getIntent().getStringExtra("categoryId");
            bookingType = getIntent().getStringExtra("bookingType");
            bookingDate = getIntent().getStringExtra("bookingDate");
            date = getIntent().getIntExtra("date", 0);
            month = getIntent().getIntExtra("month", 1);
            noPerson = getIntent().getIntExtra("noOfPerson", 2);
            time = getIntent().getStringExtra("time");
            bookingType = getIntent().getStringExtra("bookingType");
            setupPriceGroup();
            setupCategories();
            if (categoryId.isEmpty()) {
                reseverBtn.setVisibility(View.GONE);
            }
            getUserDetail();
        }
        ratingBar.setClickable(false);
    }

    private void setupCategories() {
        switch (categoryId) {
            case "1":
                categoryName.setText(getString(R.string.kickboxing));
                break;
            case "2":
                categoryName.setText(getString(R.string.EntrenamientoFuncional));
                break;
            case "3":
                categoryName.setText(getString(R.string.stretching));
                break;
            case "4":
                categoryName.setText(getString(R.string.yoga));
                break;
            case "5":
                categoryName.setText(getString(R.string.pilates));
                break;
            case "8":
                categoryName.setText(getString(R.string.masajes));
                break;
            case "9":
                categoryName.setText(getString(R.string.fisioterapia));
                break;
            case "11":
                categoryName.setText(getString(R.string.danza_fit));
                break;
            case "10":
                categoryName.setText(getString(R.string.Gimnasia));
                break;
        }
    }

    private void setupPriceGroup() {
        switch (bookingType) {
            case "1":
                priceGroup = "1";
                break;
            case "2":
                switch (noPerson) {
                    case 2:
                        priceGroup = "2";
                        break;
                    case 3:
                        priceGroup = "4";
                        break;
                    case 4:
                        priceGroup = "5";
                        break;
                }
                break;
            case "3":
                priceGroup = "3";
                break;
        }
    }

    public void getUserDetail() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(InstructorProfileActivity.this).isConnectingToInternet(InstructorProfileActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            //  final String token = firebaseInstanceIDService.getDeviceToken();
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.userDetails(AppCommon.getInstance(this).getUserID(), trainerId, categoryId, "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(InstructorProfileActivity.this).clearNonTouchableFlags(InstructorProfileActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        UserDetailResponse userDetailResponse = (UserDetailResponse) response.body();
                        if (userDetailResponse.getSuccess() == 1) {
                            userListObject = userDetailResponse.getUserListObject();
                            setupUserDetail(userListObject);
                        } else {
                            AppCommon.getInstance(InstructorProfileActivity.this).showDialog(InstructorProfileActivity.this, userDetailResponse.getError());
                        }
                    } else {
                        AppCommon.getInstance(InstructorProfileActivity.this).showDialog(InstructorProfileActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(InstructorProfileActivity.this).clearNonTouchableFlags(InstructorProfileActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(InstructorProfileActivity.this).showDialog(InstructorProfileActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(InstructorProfileActivity.this).clearNonTouchableFlags(InstructorProfileActivity.this);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    private void setupUserDetail(TrainerDetailObject userListObject) {
        userImage.setImageURI(Uri.parse(userListObject.getUserImageUrl()));
        firstName.setText(userListObject.getFirstName());
        lastName.setText(userListObject.getLastName());
        fullDescription.setText(userListObject.getDescription());
        smallDescription.setText(userListObject.getDescription());
        categoryTagAdapter = new CategoryTagAdapter(this, userListObject.getCategoriesObjectList());
        categoryRecyclerView.setAdapter(categoryTagAdapter);
        setupPrice();
        try {
            ratingBar.setRating(Float.valueOf(userListObject.getReviews()));
        } catch (Exception e) {

        }
    }

    private void setupPrice() {
        switch (bookingType) {
            case "1":
                price = userListObject.getSinglePrice();
                break;
            case "2":
                if (noPerson == 2) {
                    price = userListObject.getGroupPrice2();
                } else if (noPerson == 3) {
                    price = userListObject.getGroupPrice3();
                } else if (noPerson == 4) {
                    price = userListObject.getGroupPrice4();
                }
                break;
            case "3":
                price = userListObject.getCompanyPrice();
                break;
            default:
                price = userListObject.getSinglePrice();
                if (price.equals("")) {
                    price = userListObject.getGroupPrice2();
                    if (price.equals("")) {
                        price = userListObject.getGroupPrice3();
                        if (price.equals("")) {
                            price = userListObject.getGroupPrice4();
                            if (price.equals("")) {
                                price = userListObject.getCompanyPrice();
                            }
                        }
                    }
                }
                break;
        }
        priceTextView.setText(AppCommon.getInstance(this).getPriceInFormat(price));

    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.add)
    public void add() {
        if (fullDescription.getVisibility() == View.VISIBLE) {
            fullDescription.setVisibility(View.GONE);
            smallDescription.setVisibility(View.VISIBLE);
        } else {
            fullDescription.setVisibility(View.VISIBLE);
            smallDescription.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.chat, R.id.phone})
    public void chat() {
//        Intent intent = new Intent(this, ChatScreenActivity.class);
//        intent.putExtra("AnotherUserID", trainerId);
//        intent.putExtra("name", userListObject.getFirstName());
//        startActivity(intent);
        AppCommon.getInstance(this).showDialog(this, "No puedes usar esta característica antes de reservar");
    }

    @OnClick(R.id.agenda)
    public void agenda() {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("fromProfile", "1");
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("trainerId", trainerId);
        intent.putExtra("userObject", new Gson().toJson(userListObject));
        intent.putExtra("price", price);
        startActivity(intent);
    }

    @OnClick(R.id.reseverBtn)
    void reseverBtn() {
        if (isDate.equals("0")) {
            Intent intent = new Intent(this, ReservarActivity.class);
            intent.putExtra("fromProfile", "2");
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("trainerId", trainerId);
            intent.putExtra("userObject", new Gson().toJson(userListObject));
            intent.putExtra("price", price);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ResumenActivity.class);
            intent.putExtra("trainerId", trainerId);
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("date", date);
            intent.putExtra("bookingDate", bookingDate);
            intent.putExtra("month", month);
            intent.putExtra("noOfPerson", noPerson);
            intent.putExtra("price", price);
            intent.putExtra("time", time);
            intent.putExtra("bookingType", bookingType);
            intent.putExtra("userObject", new Gson().toJson(userListObject));
            startActivity(intent);
        }

    }

    public void setCategory(CategoriesObject categoriesObject) {
        categoryName.setText(categoriesObject.getCategoryName());
        categoryId = categoriesObject.getCategoryID();
        userListObject.setSinglePrice(categoriesObject.getSinglePrice());
        userListObject.setGroupPrice2(categoriesObject.getGroupPrice2());
        userListObject.setGroupPrice3(categoriesObject.getGroupPrice3());
        userListObject.setGroupPrice4(categoriesObject.getGroupPrice4());
        userListObject.setCompanyPrice(categoriesObject.getCompanyPrice());
        setupPrice();

    }
}
