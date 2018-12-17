package com.elisa.olu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;


import com.elisa.olu.LocationInfrastructure.FusedLocationService;
import com.elisa.olu.LocationInfrastructure.FusedLocationTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import APIResponse.SavedLocationResponse;
import Adapter.PlaceArrayAdapter;
import Adapter.SavedLocationAdapter;
import ApiObject.SavedLocation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;

import static infrastructure.AppCommon.LOCATION_PERMISSION_REQUEST_CODE;


public class IngresaActivity extends GenricActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;

    private PlaceArrayAdapter mPlaceArrayAdapter;

    private LatLngBounds BOUNDS_MOUNTAIN_VIEW = null;

    private static final String LOG_TAG = "";
    String addressText;

    int GOOGLE_API_CLIENT_ID = 204015;

    @BindView(R.id.editTextLocation)
    TextView editTextLocation;

    @BindView(R.id.savedLocationRecyclerView)
    RecyclerView savedLocationRecyclerView;

    String categoryId = "";

    int mon;

    int date;

    String bookingDate = "";

    int month;

    String time = "";

    String bookingType = "";

    String latitude = "";

    String longitude = "";

    List<SavedLocation> savedLocationList = new ArrayList<>();
    SavedLocationAdapter savedLocationAdapter;

    AutocompleteFilter typeFilter;

    boolean isComingFromReserverScreen = false;

    GoogleMap googleMap;
    public LatLng currentUserLatLon;

    public boolean isSelectFromAutoAPI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa);
        ButterKnife.bind(this);
        isComingFromReserverScreen = getIntent().getBooleanExtra("isComingFromReservar", false);
        typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setCountry("CO")
                .build();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //googlePlaceApi();
        savedLocationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (getIntent().getStringExtra("categoryId") != null) {
            categoryId = getIntent().getStringExtra("categoryId");
            bookingDate = getIntent().getStringExtra("bookingDate");
            time = getIntent().getStringExtra("time");
            bookingType = getIntent().getStringExtra("bookingType");
            date = getIntent().getIntExtra("date", 0);
            month = getIntent().getIntExtra("month", 1);
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            editTextLocation.setText(getIntent().getStringExtra("address"));
            AppCommon.latitudeValue = latitude;
            AppCommon.longitudeValue = longitude;
            AppCommon.address = getIntent().getStringExtra("address");
        }
    }

    @OnClick(R.id.editTextLocation)
    public void click(View view){
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, 100);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        enableMyLocationIfPermitted();
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (!isSelectFromAutoAPI) {
                    LatLng latLng = IngresaActivity.this.googleMap.getCameraPosition().target;
                    latitude = String.valueOf(latLng.latitude);
                    longitude = String.valueOf(latLng.longitude);
                    Geocoder geocoder = new Geocoder(IngresaActivity.this);
                    List<Address> addresses;
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
                            addressText = sb.toString();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editTextLocation.setText(addressText);
                } else {
                    isSelectFromAutoAPI = false;
                }
            }
        });
        if (AppCommon.getInstance(this).getUserLatitude() != 0.0 && AppCommon.getInstance(this).getUserLongitude() != 0.0) {
            latitude = String.valueOf(AppCommon.getInstance(this).getUserLatitude());
            longitude = String.valueOf(AppCommon.getInstance(this).getUserLongitude());
            setLocation();
        } else {
            new FusedLocationTracker(this);
           // startService(new Intent(getApplicationContext(), FusedLocationService.class));
        }
    }

    private LatLngBounds setBounds(LatLng latLng, double mDistanceInMeters) {
        double latRadian = Math.toRadians(latLng.latitude);

       // double degLatKm = 110.574235;
      //  double degLongKm = 110.572833 * Math.cos(latRadian);
       // double deltaLat = mDistanceInMeters / 1000.0 / degLatKm;
       // double deltaLong = mDistanceInMeters / 1000.0 / degLongKm;

        double minLat = latLng.latitude - mDistanceInMeters;
        double minLong = latLng.longitude - mDistanceInMeters;
        double maxLat = latLng.latitude + mDistanceInMeters;
        double maxLong = latLng.longitude + mDistanceInMeters;

        Log.d("loc1", "Min: " + Double.toString(minLat) + "," + Double.toString(minLong));
        Log.d("loc2", "Max: " + Double.toString(maxLat) + "," + Double.toString(maxLong));

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        return new LatLngBounds(new LatLng(minLat, minLong), new LatLng(maxLat, maxLong));
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
                addressText = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        addCurrentLocationMarker();
        editTextLocation.setText(addressText);
    }

    public void locationGet(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        setLocation();

    }

    public void addCurrentLocationMarker() {
        if (!latitude.equals("") && !longitude.equals("")) {
            currentUserLatLon = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }
        try {
            if (currentUserLatLon != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLon, 18.0f));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
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

//    private void googlePlaceApi() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
//                .addConnectionCallbacks(this)
//                .build();
//        editTextLocation.setThreshold(1);
//        editTextLocation.setOnItemClickListener(mAutocompleteClickListener);
////        if (!latitude.equals("") && !longitude.equals("")) {
////            BOUNDS_MOUNTAIN_VIEW = setBounds(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), 0.001);
////        }
//        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
//                BOUNDS_MOUNTAIN_VIEW, typeFilter);
//        editTextLocation.setAdapter(mPlaceArrayAdapter);
//    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }

        private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
                = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    Log.e(LOG_TAG, "Place query did not complete. Error: " +
                            places.getStatus().toString());
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);
                CharSequence attributions = places.getAttributions();
                addressText = String.valueOf(place.getAddress());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                isSelectFromAutoAPI = true;
                addCurrentLocationMarker();
            }
        };
    };

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.confirmBtn)
    public void confirmBtn() {
        String location = editTextLocation.getText().toString();
        if (location.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese la ubicación", Toast.LENGTH_SHORT).show();
        } else if (latitude == null || latitude.equals("") || longitude == null || longitude.equals("")) {
            getLatitdueAndLongitudeFromAddress(location);
        } else {
            moveToNextScreen();
        }
    }

    public void moveToNextScreen() {
        AppCommon.latitudeValue = latitude;
        AppCommon.longitudeValue = longitude;
        AppCommon.address = addressText;
        if (isComingFromReserverScreen) {
            SavedLocation savedLocation = new SavedLocation(addressText, latitude, longitude);
            savedLocalLocation(savedLocation);
            if (getIntent().getStringExtra("trainerId") == null) {
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("address", addressText);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("date", date);
                intent.putExtra("bookingDate", bookingDate);
                intent.putExtra("month", mon);
                intent.putExtra("time", time);
                intent.putExtra("bookingType", bookingType);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ResumenActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("address", addressText);
                intent.putExtra("trainerId", getIntent().getStringExtra("trainerId"));
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("date", date);
                intent.putExtra("bookingDate", bookingDate);
                intent.putExtra("month", mon);
                intent.putExtra("price", getIntent().getStringExtra("price"));
                intent.putExtra("time", time);
                intent.putExtra("bookingType", bookingType);
                intent.putExtra("userObject", getIntent().getStringExtra("userObject"));
                intent.putExtra("isComingFromReservar", true);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra("latitude", AppCommon.latitudeValue);
            intent.putExtra("longitude", AppCommon.longitudeValue);
            intent.putExtra("address", AppCommon.address);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void getLatitdueAndLongitudeFromAddress(String addressStr) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(addressStr, 5);
            if (address == null) {
                Toast.makeText(this, "Por favor seleccione ubicación diferente", Toast.LENGTH_SHORT).show();
                return;
            }
            Address location = address.get(0);
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            addressText = addressStr;
            addCurrentLocationMarker();
        } catch (Exception e) {

        }
    }

    private void savedLocalLocation(SavedLocation savedLocation) {
        if (!AppCommon.getInstance(this).getLocalLocation().isEmpty()) {
            SavedLocationResponse savedLocationResponse = new Gson().fromJson(AppCommon.getInstance(this).getLocalLocation(), SavedLocationResponse.class);
            savedLocationList = savedLocationResponse.getSavedLocationResponseList();
        }
        Boolean check = checkIfValueExist(savedLocation);
        if (!check) {
            if (savedLocationList.size() == 2) {
                savedLocationList.remove(0);
            }
            savedLocationList.add(savedLocation);
        }
        AppCommon.getInstance(this).setLocalLocation(new Gson().toJson(new SavedLocationResponse(savedLocationList)));
    }

    public boolean checkIfValueExist(SavedLocation savedLocation) {
        Boolean check = false;
        for (int i = 0; i < savedLocationList.size(); i++) {
            if (savedLocation.getLocation().equals(savedLocationList.get(i).getLocation())) {
                check = true;
                break;
            } else {
                check = false;
            }
        }
        return check;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!AppCommon.getInstance(this).getLocalLocation().isEmpty()) {
            SavedLocationResponse savedLocationResponse = new Gson().fromJson(AppCommon.getInstance(this).getLocalLocation(), SavedLocationResponse.class);
            savedLocationList = savedLocationResponse.getSavedLocationResponseList();
        }
        savedLocationAdapter = new SavedLocationAdapter(this, savedLocationList);
        savedLocationRecyclerView.setAdapter(savedLocationAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    public void setOnClick(int adapterPosition) {
        AppCommon.latitudeValue = savedLocationList.get(adapterPosition).getLatitude();
        AppCommon.longitudeValue = savedLocationList.get(adapterPosition).getLongitude();
        AppCommon.address = savedLocationList.get(adapterPosition).getLocation();

        latitude = AppCommon.latitudeValue;
        longitude = AppCommon.longitudeValue;
        addressText = AppCommon.address;

        addCurrentLocationMarker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                addressText = String.valueOf(place.getAddress());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                editTextLocation.setText(addressText);
                isSelectFromAutoAPI = true;
                addCurrentLocationMarker();
               // Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                //Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
