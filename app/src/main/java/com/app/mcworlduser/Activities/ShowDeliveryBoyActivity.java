package com.app.mcworlduser.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.app.mcworlduser.DirectionsJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowDeliveryBoyActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    double latitude, longitude;
    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LatLng s_latLng, d_latLng;

    TextView txtAddress;
    Button btnCall;
    ImageView backImage;
    String st_Latitude="";
    String st_Lontitude="";
    String st_user_id="";

    double sourceLat;
    double sourceLng;

    double destinationLat;
    double destinationLng;
    String strOrderId="";
    String mobile="";
    ImageView imgCurrentLocation;
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_delivery_boy);
        getSupportActionBar().hide();

        AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,0);
        st_Latitude= AppConstant.sharedpreferences.getString(AppConstant.Latitude,"");
        st_Lontitude= AppConstant.sharedpreferences.getString(AppConstant.Lontitude,"");
        st_user_id= AppConstant.sharedpreferences.getString(AppConstant.Userid,"");
        strOrderId= AppConstant.sharedpreferences.getString(AppConstant.OrderId,"");
        Log.e("dfgdfgdgfgdfg",strOrderId);

        sourceLat= Double.parseDouble(st_Latitude);
        sourceLng= Double.parseDouble(st_Lontitude);
        s_latLng = new LatLng(sourceLat, sourceLng);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map.getMapAsync(this);

        imgCurrentLocation=findViewById(R.id.imgCurrentLocation);
        imgCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int heights = 100;
                int widths = 80;
                BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.green_marker);
                Bitmap bs = bitmapdraws.getBitmap();


                Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, widths, heights, false);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(s_latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))

                );


            }
        });




        txtAddress=findViewById(R.id.txtAddress);
        backImage=findViewById(R.id.backImage);
        btnCall=findViewById(R.id.btnCall);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mobile));
                startActivity(intent);
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();
            }
        });

        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                Tracking();
                handler.postDelayed(runnable, 4000);
            }
        }, 4000);

        ShowDeliveryBoyLocation();
    }
    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(15).build();
    }


    public void Tracking(){

        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "show_accept_driver")
                .addBodyParameter("order_id", strOrderId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dgfdh", response.toString());
                        try {


                                if (response.getString("result").equals("Successful")) {

                                    String user_id=response.getString("id");
                                     mobile=response.getString("mobile");
                                    String name=response.getString("name");
                                    String email=response.getString("email");
                                    String latitude=response.getString("latitude");
                                    String longitude=response.getString("longitude");
                                    String address=response.getString("address");
                                    String sublocality=response.getString("sublocality");
                                    String user_type=response.getString("user_type");

                                    txtAddress.setText("Street 11 Bangaluru");
                                    destinationLat= Double.parseDouble(latitude);
                                    destinationLng= Double.parseDouble(longitude);

                                    DrawPath();


                                }
                                else {

                                }

                        } catch (Exception e) {

                            Log.e("fdgfd", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("gfdf", anError.getMessage());


                    }
                });
    }



    public void ShowDeliveryBoyLocation(){

        final ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Getting location..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "show_accept_driver")
                .addBodyParameter("order_id", strOrderId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dgfdh", response.toString());
                        try {


                            if (response.getString("result").equals("Successful")) {

                                String user_id=response.getString("id");
                                mobile=response.getString("mobile");
                                String name=response.getString("name");
                                String email=response.getString("email");
                                String latitude=response.getString("latitude");
                                String longitude=response.getString("longitude");
                                String address=response.getString("address");
                                String sublocality=response.getString("sublocality");
                                String user_type=response.getString("user_type");

                                txtAddress.setText("Street 11 Bangaluru");
                                destinationLat= Double.parseDouble(latitude);
                                destinationLng= Double.parseDouble(longitude);

                                DrawPath();
                                progressDialog.dismiss();


                            }
                            else {

                                progressDialog.dismiss();
                                Toast.makeText(ShowDeliveryBoyActivity.this, "Not Accepted yet once accept we will notify you.", Toast.LENGTH_SHORT).show();
                                txtAddress.setText("Not Accepted yet once accept we will notify you.");

                                int heights = 100;
                                int widths = 80;
                                BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.green_marker);
                                Bitmap bs = bitmapdraws.getBitmap();


                                Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, widths, heights, false);
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(s_latLng)
                                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))

                                );

                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(s_latLng)));

                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Log.e("fdgfd", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e("gfdf", anError.getMessage());


                    }
                });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(ShowDeliveryBoyActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.maps_style);
        googleMap.setMapStyle(style);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ShowDeliveryBoyActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();
        }


        DrawPath();



    }
    public void DrawPath(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.25); // offset from edges of the map 10% of screen

        Log.e("dfgdgdgdgf","dedsfdfds"+destinationLat);
        Log.e("dfgdgdgdgf","dedsfdfds"+destinationLng);



        if (s_latLng != null) {

            int heights = 90;
            int widths = 80;
            BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.green_marker);
            Bitmap bs = bitmapdraws.getBitmap();

            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, widths, heights, false);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(s_latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkers)));
            builder.include(marker.getPosition());
        }
        d_latLng = new LatLng(destinationLat, destinationLng);

        if (d_latLng != null) {
         /* Marker marker = googleMap.addMarker(new MarkerOptions().position(d_latLng));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.loc));
            builder.include(marker.getPosition());*/


            int heights = 100;
            int widths = 80;
            BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.deliv_boy_marker);
            Bitmap bs = bitmapdraws.getBitmap();


            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, widths, heights, false);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(d_latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))
            );
            builder.include(marker.getPosition());
        }

        if (s_latLng != null && d_latLng != null) {
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mMap.animateCamera(cu);
            String url = getDirectionsUrl(s_latLng, d_latLng);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        } else{

            Toast.makeText(ShowDeliveryBoyActivity.this, "Enter Source and Destination Address", Toast.LENGTH_SHORT).show();
        }


    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
       // String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyClkiUQxIeczQEjTrw-dy9h2fj9qjXDk_4";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyAURsUjEy5yIa7CTgW_vyn8f5L7iKXz5DI";
        return url;
    }



    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(ShowDeliveryBoyActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowDeliveryBoyActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(ShowDeliveryBoyActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(ShowDeliveryBoyActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(ShowDeliveryBoyActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(ShowDeliveryBoyActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ShowDeliveryBoyActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            try {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = result.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(R.color.red);
                    mMap.addPolyline(lineOptions);
                }
            } catch (Exception ex) {
                Log.e("dfgdfgdgd",ex.getMessage());
            }
        }
    }


    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


}