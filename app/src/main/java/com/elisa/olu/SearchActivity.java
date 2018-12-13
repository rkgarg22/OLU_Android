package com.elisa.olu;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elisa.olu.LocationInfrastructure.FusedLocationService;
import com.elisa.olu.LocationInfrastructure.FusedLocationTracker;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.UserListingResponse;
import Adapter.TrainersAdapter;
import Adapter.UbicacionAdapter;
import ApiObject.TrainerDetailObject;
import ApiObject.UserListObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends GenricActivity {

    @BindView(R.id.trainerRecyclerView)
    RecyclerView trainerRecyclerView;

    TrainersAdapter adapter;

    Call call;

    UbicacionAdapter ubicacionAdapter;

    List<UserListObject> userListObjectList = new ArrayList<>();

    String categoryId = "";

    int date = 0;

    String time = "";

    String gender = "";


    @BindView(R.id.noDataFound)
    TextView noDataFound;

    String bookingDate = "";

    int month;

    int noPerson;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.searchEditText)
    EditText searchEditText;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    String search;

    String bookingType;

    // String priceGroup;

    String isDate;

    public int LOCATION_FILTER = 100;

    FusedLocationTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        gpsTracker = new FusedLocationTracker(this);
        // gpsTracker=new FusedLocationService(this);
        if (getIntent().getStringExtra("categoryId") != null) {
            categoryId = getIntent().getStringExtra("categoryId");
            date = getIntent().getIntExtra("date", 0);
            bookingDate = getIntent().getStringExtra("bookingDate");
            month = getIntent().getIntExtra("month", 1);
            noPerson = getIntent().getIntExtra("noOfPerson", 2);
            time = getIntent().getStringExtra("time");
            bookingType = getIntent().getStringExtra("bookingType");
            if (bookingDate.isEmpty()) {
                isDate = "0";
            } else {
                isDate = "1";
            }
            // setupPriceGroup();
            if (!AppCommon.latitudeValue.equals("") && !AppCommon.longitudeValue.equals("")) {
                getUserListing();
            }
        }

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trainerRecyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new TrainersAdapter(this, userListObjectList, bookingType);
        trainerRecyclerView.setAdapter(adapter);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search = searchEditText.getText().toString().trim();
                    userListObjectList.clear();
                    getUserListing();
                }
                return false;
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userListObjectList.clear();
                AppCommon.longitudeValue = String.valueOf(gpsTracker.getLongitude());
                AppCommon.latitudeValue = String.valueOf(gpsTracker.getLatitude());
                getUserListing();
            }
        });
    }


//    private void setupPriceGroup() {
//        switch (bookingType) {
//            case "1":
//                priceGroup = "1";
//                break;
//            case "2":
//                switch (noPerson) {
//                    case 2:
//                        priceGroup = "2";
//                        break;
//                    case 3:
//                        priceGroup = "4";
//                        break;
//                    case 4:
//                        priceGroup = "5";
//                        break;
//                }
//                break;
//            case "3":
//                priceGroup = "3";
//                break;
//        }
//    }

    public void getUserListing() {
        if ((!AppCommon.latitudeValue.equals("")) && (!AppCommon.longitudeValue.equals(""))) {
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            if (AppCommon.getInstance(SearchActivity.this).isConnectingToInternet(SearchActivity.this)) {
                progressBar.setVisibility(View.VISIBLE);
                PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
                call = pretoAppService.userListing(AppCommon.getInstance(this).getUserID(),
                        search, AppCommon.latitudeValue, AppCommon.longitudeValue,
                        1, time, bookingDate,
                        categoryId, gender, "", bookingType, "es");
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        AppCommon.getInstance(SearchActivity.this).clearNonTouchableFlags(SearchActivity.this);
                        if (response.code() == 200) {
                            progressBar.setVisibility(View.GONE);
                            swipeContainer.setRefreshing(false);
                            UserListingResponse userListingResponse = (UserListingResponse) response.body();
                            if (userListingResponse.getSuccess() == 1) {
                                userListObjectList = userListingResponse.getUserListObjectList();
                                adapter = new TrainersAdapter(SearchActivity.this, userListObjectList, bookingType);
                                trainerRecyclerView.setAdapter(adapter);
                                noDataFound.setVisibility(View.GONE);
                            } else {
                                //   AppCommon.getInstance(SearchActivity.this).showDialog(SearchActivity.this, userListingResponse.getError());
                                noDataFound.setVisibility(View.VISIBLE);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            AppCommon.getInstance(SearchActivity.this).showDialog(SearchActivity.this, getString(R.string.serverError));
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        AppCommon.getInstance(SearchActivity.this).clearNonTouchableFlags(SearchActivity.this);
                        swipeContainer.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        AppCommon.getInstance(SearchActivity.this).showDialog(SearchActivity.this, getResources().getString(R.string.network_error));

                    }
                });
            } else {
                AppCommon.getInstance(SearchActivity.this).clearNonTouchableFlags(SearchActivity.this);
                swipeContainer.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
            }
        }
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
        }
    }

    @OnClick(R.id.locationClick)
    public void locationClick(View v) {
        Intent locationIntent = new Intent(this, IngresaActivity.class);
        startActivityForResult(locationIntent, LOCATION_FILTER);
    }

    @OnClick(R.id.filterBtn)
    public void filterBtn(View v) {
        Intent locationIntent = new Intent(this, FilterActivity.class);
        locationIntent.putExtra("gender", gender);
        locationIntent.putExtra("categoryId", categoryId);
        startActivityForResult(locationIntent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 123) {
                gender = data.getStringExtra("gender");
                categoryId = data.getStringExtra("categoryId");
                userListObjectList.clear();
                getUserListing();
            } else if (requestCode == LOCATION_FILTER) {
                AppCommon.latitudeValue = data.getStringExtra("latitude");
                AppCommon.longitudeValue = data.getStringExtra("longitude");
                AppCommon.address = data.getStringExtra("addressText");
                userListObjectList.clear();
                getUserListing();
            }
        }
    }

    public void setOnClick(int adapterPosition) {
        UserListObject userListObject = userListObjectList.get(adapterPosition);
        TrainerDetailObject trainerDetailObject = new TrainerDetailObject(userListObject.getFirstName(), userListObject.getLastName(), userListObject.getUserImageUrl(), userListObject.getReviews(), userListObject.getCategoriesObjectArrayList());
        if (isDate.equals("0")) {
            Intent intent = new Intent(this, ReservarActivity.class);
            intent.putExtra("fromProfile", "2");
            intent.putExtra("categoryId", userListObjectList.get(adapterPosition).getCategoryID());
            intent.putExtra("trainerId", String.valueOf(userListObjectList.get(adapterPosition).getUserID()));
            intent.putExtra("userObject", new Gson().toJson(trainerDetailObject));
            intent.putExtra("price", userListObjectList.get(adapterPosition).getPrice());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ResumenActivity.class);
            intent.putExtra("trainerId", String.valueOf(userListObjectList.get(adapterPosition).getUserID()));
            intent.putExtra("categoryId", userListObjectList.get(adapterPosition).getCategoryID());
            intent.putExtra("isDate", isDate);
            intent.putExtra("date", date);
            intent.putExtra("bookingDate", bookingDate);
            intent.putExtra("month", month);
            intent.putExtra("noOfPerson", noPerson);
            intent.putExtra("price", userListObjectList.get(adapterPosition).getPrice());
            intent.putExtra("time", time);
            intent.putExtra("bookingType", bookingType);
            intent.putExtra("userObject", new Gson().toJson(trainerDetailObject));
            startActivity(intent);
        }
    }

    public void setOnClickPlusIcon(int adapterPosition) {
        Intent intent = new Intent(this, InstructorProfileActivity.class);
        intent.putExtra("trainerId", String.valueOf(userListObjectList.get(adapterPosition).getUserID()));
        intent.putExtra("categoryId", String.valueOf(userListObjectList.get(adapterPosition).getCategoryID()));
        intent.putExtra("isDate", isDate);
        intent.putExtra("date", date);
        intent.putExtra("bookingDate", bookingDate);
        intent.putExtra("month", month);
        intent.putExtra("noOfPerson", noPerson);
        intent.putExtra("time", time);
        intent.putExtra("bookingType", bookingType);
        startActivity(intent);
    }

    public void locationGet(Location location) {
        AppCommon.latitudeValue = String.valueOf(location.getLatitude());
        AppCommon.longitudeValue = String.valueOf(location.getLongitude());
        getUserListing();
    }
}
