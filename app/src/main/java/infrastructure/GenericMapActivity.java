package infrastructure;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;

import com.elisa.olu.GenricActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.elisa.olu.R;

import ApiObject.ResturantObject;


public class GenericMapActivity extends GenricActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    public GoogleMap mMap;
    public LatLng currentUserLatLon;
    public Map<Marker, String> markersOrderNumbers = new HashMap<>();
    public ArrayList<ResturantObject> originalResturantObjectArrayList = new ArrayList<ResturantObject>();
    public ArrayList<ResturantObject> resturantObjectArrayList = new ArrayList<ResturantObject>();
    public boolean isMapActive = false;

    public RelativeLayout markerClickLayout;
    public int selectedIndex = 0;

//    public void setMapView() {
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap!=null) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.clear();
            mMap.setOnInfoWindowClickListener(this);
            markersOrderNumbers.clear();
            if (AppCommon.getInstance(this).getUserLatitude() != 0.0f && AppCommon.getInstance(this).getUserLongitude() != 0.0f) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
                String currentLat = String.valueOf(AppCommon.getInstance(this).getUserLatitude());
                String curretnLon = String.valueOf(AppCommon.getInstance(this).getUserLongitude());
                currentUserLatLon = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(curretnLon));
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(currentUserLatLon)
                        .title("")
                        .icon(icon)
                        .alpha(1.0f));

            }
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
            if (resturantObjectArrayList.size() > 0) {
                for (int i = 0; i < resturantObjectArrayList.size(); i++) {
                    ResturantObject resturantObject = resturantObjectArrayList.get(i);
                    try {
                        LatLng markerLat = new LatLng(Double.parseDouble(resturantObject.getLattitude()), Double.parseDouble(resturantObject.getLongitude()));
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(markerLat)
                                .title(Html.fromHtml(resturantObject.getRestName()) + "\n" + resturantObject.getAddress())
                                .icon(icon)
                                .alpha(1.0f));
                        markersOrderNumbers.put(marker, Integer.toString(i));
                    }catch(Exception e){}
                }
            }
            if(currentUserLatLon!=null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLon, 10.0f));
            }
        }
    }

    public void googleMapClick() {
        ResturantObject object = resturantObjectArrayList.get(selectedIndex);
        String url = "http://maps.google.com/maps?daddr=" + object.getLattitude() + "," + object.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(intent);
    }

    public void wazeClick() {
        ResturantObject object = resturantObjectArrayList.get(selectedIndex);
        String uri = "geo:" + object.getLattitude() + "," + object.getLongitude();
        //String uri = "waze://?ll=" + object.getLattitude() + "," + object.getLongitude() + "&navigate=yes";
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(uri)));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        markerClickLayout.setVisibility(View.VISIBLE);
        selectedIndex = Integer.parseInt(markersOrderNumbers.get(marker));
    }
}



