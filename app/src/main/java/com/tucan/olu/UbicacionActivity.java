package com.tucan.olu;


import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tucan.olu.LocationInfrastructure.FusedLocationTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonStringResponse;
import APIResponse.UserListingResponse;
import Adapter.UbicacionAdapter;
import ApiObject.UserListObject;
import CustomControl.AvenirNextCondensedMediumTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static infrastructure.AppCommon.LOCATION_PERMISSION_REQUEST_CODE;
import static infrastructure.AppCommon.address;

public class UbicacionActivity extends GenricActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    @BindView(R.id.trainerRecyclerView)
    RecyclerView trainerRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.listBtn)
    AvenirNextCondensedMediumTextView listBtn;

    @BindView(R.id.mapBtn)
    AvenirNextCondensedMediumTextView mapBtn;

    @BindView(R.id.mapFragmentLayout)
    LinearLayout mapFragmentLayout;

    @BindView(R.id.listLayout)
    RelativeLayout listLayout;

    public Map<Marker, String> markersOrderNumbers = new HashMap<>();

    private static final long MIN_TIME = 400;

    private static final float MIN_DISTANCE = 2000;

    Call call;

    UbicacionAdapter ubicacionAdapter;

    List<UserListObject> userListObjectList = new ArrayList<>();

    String categoryId = "";

    int mon;

    int date;

    String bookingDate = "";

    int month;
    String time = "";

    String bookingType = "";

    String gender = "";

    String rating = "";

    String latitude = "";

    String longitude = "";

    @BindView(R.id.noDataFound)
    TextView noDataFound;

    GoogleMap googleMap;
    public LatLng currentUserLatLon;
    FusedLocationTracker locationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        ButterKnife.bind(this);
        locationTracker = new FusedLocationTracker(this);
        mapBtn.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        listBtn.setTextColor(ContextCompat.getColor(this, R.color.grey));
        mapFragmentLayout.setVisibility(View.VISIBLE);
        listLayout.setVisibility(View.GONE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        trainerRecyclerView.setLayoutManager(layoutManager);
        ubicacionAdapter = new UbicacionAdapter(this, userListObjectList);
        trainerRecyclerView.setAdapter(ubicacionAdapter);
        if (getIntent().getStringExtra("categoryId") != null) {
            categoryId = getIntent().getStringExtra("categoryId");
            bookingDate = getIntent().getStringExtra("bookingDate");
            time = getIntent().getStringExtra("time");
            bookingType = getIntent().getStringExtra("bookingType");
            date = getIntent().getIntExtra("date", 0);
            month = getIntent().getIntExtra("month", 1);
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            AppCommon.latitudeValue = latitude;
            AppCommon.longitudeValue = longitude;
            AppCommon.address = getIntent().getStringExtra("address");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(UbicacionActivity.this);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userListObjectList.clear();
                getUserListing();
            }
        });
    }

    @OnClick(R.id.listBtn)
    public void listBtnClick(View v) {
        mapBtn.setTextColor(ContextCompat.getColor(this, R.color.grey));
        listBtn.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        mapFragmentLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.mapBtn)
    public void mapBtnClick(View v) {
        mapBtn.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        listBtn.setTextColor(ContextCompat.getColor(this, R.color.grey));
        mapFragmentLayout.setVisibility(View.VISIBLE);
        listLayout.setVisibility(View.GONE);
    }

    public void setLocation() {
        AppCommon.latitudeValue = latitude;
        AppCommon.longitudeValue = longitude;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();

                if (address.getMaxAddressLineIndex() > 0) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append(",");
                    }
                } else {
                    try {
                        sb.append(address.getAddressLine(0));
                    } catch (Exception ignored) {
                        if (address.getSubLocality() != null) {
                            sb.append(address.getSubLocality()).append(",");
                        }
                        if (address.getLocality() != null) {
                            sb.append(address.getLocality()).append(",");
                        }
                    }
                }
                AppCommon.address = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveUserLocation();
        getUserListing();
    }


    public void locationGet(Location location) {
        locationTracker.stopLocationUpdates();
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        setLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        enableMyLocationIfPermitted();
        googleMap.setOnInfoWindowClickListener(this);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (AppCommon.getInstance(this).getUserLatitude() != 0.0 && AppCommon.getInstance(this).getUserLongitude() != 0.0) {
            latitude = String.valueOf(AppCommon.getInstance(this).getUserLatitude());
            longitude = String.valueOf(AppCommon.getInstance(this).getUserLongitude());
            setLocation();
        }
    }

    public void addCurrentLocationMarker() {
        if (AppCommon.getInstance(this).getUserLatitude() != 0.0f && AppCommon.getInstance(this).getUserLongitude() != 0.0f) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.current_location_icon);
            currentUserLatLon = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            Marker marker = this.googleMap.addMarker(new MarkerOptions()
                    .position(currentUserLatLon)
                    .title("")
                    .icon(icon)
                    .alpha(1.0f));
        }
        if (currentUserLatLon != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLon, 18.0f));
        }
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    //  showDefaultLocation();
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    //  googleMap.setMinZoomPreference(12);

//                    CircleOptions circleOptions = new CircleOptions();
//                    circleOptions.center(new LatLng(location.getLatitude(),
//                            location.getLongitude()));
//
//                    circleOptions.radius(200);
//                    //circleOptions.fillColor(Color.BLUE);
//                    circleOptions.strokeWidth(6);
//
//                    googleMap.addCircle(circleOptions);
                }
            };


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.locationClick)
    public void locationClick(View v) {
        Intent intent = new Intent(this, IngresaActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("address", address);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("date", date);
        intent.putExtra("bookingDate", bookingDate);
        intent.putExtra("month", mon);
        intent.putExtra("time", time);
        intent.putExtra("bookingType", bookingType);
        startActivityForResult(intent, 456);
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
                categoryId = String.valueOf(data.getIntExtra("categoryId", 0));
                userListObjectList.clear();
                getUserListing();
            } else if (requestCode == 456) {
                googleMap.clear();
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");
                AppCommon.latitudeValue = latitude;
                AppCommon.longitudeValue = longitude;
                getUserListing();
                AppCommon.address = data.getStringExtra("address");
            }
        }
    }

    @OnClick(R.id.searchBtn)
    public void searchBtnClick(View v) {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.putExtra("categoryId", categoryId);
        searchIntent.putExtra("date", date);
        searchIntent.putExtra("bookingDate", bookingDate);
        searchIntent.putExtra("month", month);
        searchIntent.putExtra("time", time);
        searchIntent.putExtra("bookingType", bookingType);
        startActivity(searchIntent);
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
                        AppCommon.getInstance(UbicacionActivity.this).setUserLongitude(Double.parseDouble(longitude));
                        AppCommon.getInstance(UbicacionActivity.this).setUserLatitude(Double.parseDouble(latitude));
                    }
                } else {
                    AppCommon.getInstance(UbicacionActivity.this).showDialog(UbicacionActivity.this, getString(R.string.serverError));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void getUserListing() {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(UbicacionActivity.this).isConnectingToInternet(UbicacionActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.userListing(AppCommon.getInstance(this).getUserID(), "", AppCommon.latitudeValue, AppCommon.longitudeValue,//latitude, longitude,
                    1, time, bookingDate, categoryId, gender, rating, bookingType, "es");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(UbicacionActivity.this).clearNonTouchableFlags(UbicacionActivity.this);
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                        UserListingResponse userListingResponse = (UserListingResponse) response.body();
                        if (userListingResponse.getSuccess() == 1) {
                            userListObjectList = userListingResponse.getUserListObjectList();
                            setUpMapMarker();
                            ubicacionAdapter = new UbicacionAdapter(UbicacionActivity.this, userListObjectList);
                            trainerRecyclerView.setAdapter(ubicacionAdapter);
                            noDataFound.setVisibility(View.GONE);
                        } else {
                            setUpMapMarker();
                            noDataFound.setVisibility(View.VISIBLE);
                            ubicacionAdapter.notifyDataSetChanged();
                        }
                    } else {
                        AppCommon.getInstance(UbicacionActivity.this).showDialog(UbicacionActivity.this, getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(UbicacionActivity.this).clearNonTouchableFlags(UbicacionActivity.this);
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    AppCommon.getInstance(UbicacionActivity.this).showDialog(UbicacionActivity.this, getResources().getString(R.string.network_error));

                }
            });
        } else {
            AppCommon.getInstance(UbicacionActivity.this).clearNonTouchableFlags(UbicacionActivity.this);
            swipeContainer.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            AppCommon.getInstance(this).showDialog(this, getResources().getString(R.string.network_error));
        }
    }

    private void setUpMapMarker() {
        try {
            googleMap.clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        markersOrderNumbers.clear();
        addCurrentLocationMarker();
        for (int i = 0; i < userListObjectList.size(); i++) {
            try {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.user_icon);
                LatLng markerLat = new LatLng(Double.parseDouble(userListObjectList.get(i).getLatitude()), Double.parseDouble(userListObjectList.get(i).getLongitude()));
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(markerLat)
                        .title(Html.fromHtml(userListObjectList.get(i).getFirstName()) + "  " + userListObjectList.get(i).getLastName() + " \n" + userListObjectList.get(i).getCategoryName())
                        .icon(icon)
                        .alpha(1.0f));
                markersOrderNumbers.put(marker, Integer.toString(i));
            } catch (Exception e) {
            }
        }
        if (currentUserLatLon != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLon, 18.0f));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
        }
    }

    public void setOnClick(int adapterPosition) {
        Intent intent = new Intent(this, InstructorProfileActivity.class);
        intent.putExtra("trainerId", String.valueOf(userListObjectList.get(adapterPosition).getUserID()));
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("isDate", "1");
        intent.putExtra("date", date);
        intent.putExtra("bookingDate", bookingDate);
        intent.putExtra("month", month);
        intent.putExtra("time", time);
        intent.putExtra("bookingType", bookingType);
        startActivity(intent);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int selectedIndex = Integer.parseInt(markersOrderNumbers.get(marker));
        Intent intent = new Intent(this, InstructorProfileActivity.class);
        intent.putExtra("trainerId", String.valueOf(userListObjectList.get(selectedIndex).getUserID()));
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("isDate", "1");
        intent.putExtra("date", date);
        intent.putExtra("bookingDate", bookingDate);
        intent.putExtra("month", month);
        intent.putExtra("time", time);
        intent.putExtra("bookingType", bookingType);
        startActivity(intent);
    }
}
