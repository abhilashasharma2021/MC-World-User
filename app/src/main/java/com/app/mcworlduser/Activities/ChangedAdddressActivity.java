package com.app.mcworlduser.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mcworlduser.CustomMapFragment;
import com.app.mcworlduser.MapWrapperLayout;
import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;
import com.app.mcworlduser.GPSTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChangedAdddressActivity extends AppCompatActivity implements MapWrapperLayout.OnDragListener, OnMapReadyCallback {


    private GoogleMap googleMap;
    private ImageView marker;
    private ImageView imgCurrentLocation;
    private int centerX = -1;
    private int centerY = -1;
    String lat = "", lng = "";
    TextView txtAddress;
    TextView txtLocalAddress;
    Button btnConfirmLocation;
    String strUserId = "", strCityName = "", strAddress = "", strLatitude = "", strLontitude = "";
    String strProfAdd = "";
    LatLng s_latLng;
    LatLng latLng;
    double latitude;
    double longtitude;
    GPSTracker gpsTracker;
    String pincode = "560045";
    double lat1, lng1;
    String address = "";

    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    String strAccurateAdd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changed_adddress);
        getSupportActionBar().hide();

        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strCityName = AppConstant.sharedpreferences.getString(AppConstant.CityName, "");
        strAddress = AppConstant.sharedpreferences.getString(AppConstant.Address, "");
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        strLatitude = AppConstant.sharedpreferences.getString(AppConstant.Latitude, "");
        strLontitude = AppConstant.sharedpreferences.getString(AppConstant.Lontitude, "");

        Log.e("sfdgfdg", strUserId);
        Log.e("sfdgfdg", strAddress);
        Log.e("sfdgfdg", strCityName);
        Log.e("sfdgfdg", strLatitude);
        Log.e("sfdgfdg", strLontitude);
        txtLocalAddress = findViewById(R.id.txtLocalAddress);
        txtAddress = findViewById(R.id.txtAddress);
        btnConfirmLocation = findViewById(R.id.btnConfirmLocation);
        ImageView backImage = findViewById(R.id.backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


         marker = findViewById(R.id.marker);
        imgCurrentLocation = findViewById(R.id.imgCurrentLocation);
        imgCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimateCamera();
            }
        });

        gpsTracker = new GPSTracker(this);
        lat1 = Double.parseDouble(strLatitude);
        lng1 = Double.parseDouble(strLontitude);

        Log.e("uyfuyhj", lat1+"");
        Log.e("uyfuyhj", lng1+"");

    }


    @Override
    protected void onResume() {
        super.onResume();
        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            initilizeMap();

        } else {

            gpsTracker.showSettingsAlert();
        }

    }

    private void initilizeMap() {
        if (googleMap == null) {

            CustomMapFragment supportMapFragment = (CustomMapFragment)
                    getFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.setOnDragListener(ChangedAdddressActivity.this);
            supportMapFragment.getMapAsync(this);

            if (googleMap == null) {

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.maps_style);
        googleMap.setMapStyle(style);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            s_latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            updateLocation(s_latLng);
            AnimateCamera();

        } else {

            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onDrag(MotionEvent motionEvent) {


        Log.e("uyttttuy", "DRAG");
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Projection projection = (googleMap != null && googleMap
                    .getProjection() != null) ? googleMap.getProjection()
                    : null;
            if (projection != null) {
                LatLng centerLatLng = projection.fromScreenLocation(new Point(
                        centerX, centerY));


                GPSTracker gpsTracker = new GPSTracker(this);

                if (gpsTracker.canGetLocation()) {
                    updateLocation(centerLatLng);

                } else {

                    gpsTracker.showSettingsAlert();
                }

            }
        }
    }


    private void updateLocation(final LatLng centerLatLng) {
        strAccurateAdd = "";
        if (centerLatLng != null) {

            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                addresses = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses.size() != 0) {

                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    final String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String knownName = addresses.get(0).getFeatureName();
                    final String pincode = addresses.get(0).getPostalCode();

                    Log.e("gigjk", addresses.get(0).getPostalCode() + "");

                    Log.e("gigjk", "add" + address);
                    Log.e("gigjk", "city" + city);

                    String strAdd[] = address.split(",");

                    for (int i = 0; i < strAdd.length; i++) {

                        Log.e("fgdgdfgdfgf", i + " " + strAdd[i]);
                        if (i != 0) {

                            if (strAccurateAdd.equals("")) {

                                strAccurateAdd = strAdd[i];
                            } else {
                                strAccurateAdd = strAccurateAdd + "," + strAdd[i];
                            }
                        }

                    }


                    // txtLocalAddress.setText(knownName);
                    txtLocalAddress.setText(city);
                    txtAddress.setText(strAccurateAdd);
                    lat = String.valueOf(centerLatLng.latitude);
                    lng = String.valueOf(centerLatLng.longitude);


                    btnConfirmLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                            editor.putString(AppConstant.Address, txtAddress.getText().toString());
                            editor.putString(AppConstant.CityName, city);
                            editor.putString(AppConstant.Latitude, lat);
                            editor.putString(AppConstant.Lontitude, lng);
                            editor.putString(AppConstant.Pincode, pincode);
                            editor.commit();
                            finish();

                        }
                    });

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void AnimateCamera() {
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.canGetLocation()) {

            try {
                LatLng latLngs = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLngs)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            gpsTracker.showSettingsAlert();
        }

    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }
}
