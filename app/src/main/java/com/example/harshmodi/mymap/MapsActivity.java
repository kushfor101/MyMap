package com.example.harshmodi.mymap;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.Address;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Uri gmmIntentUri = Uri.parse("geo:0,0?q=restaurants");
        //Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        //mapIntent.setPackage("com.google.android.apps.maps");
        //startActivity(mapIntent);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
               .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = "Jadavpur University " + locationSearch.getText().toString();
        List<Address> addressList = null;

        if (!location.equals("Jadavpur University ")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 5);

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "No Result Found", Toast.LENGTH_SHORT).show();
                //e.printStackTrace();
            }
            int len = addressList.size();
            for(int i = 0; i < len; i++){
                String tmp = addressList.get(i).getAddressLine(0);
                Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_SHORT).show();
            }
            if(len == 0){
                Toast.makeText(getApplicationContext(), "No Result Found", Toast.LENGTH_SHORT).show();
            }
            else {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                LatLngBounds latLngBounds = new LatLngBounds(latLng, latLng);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng, 20));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 7));
            }
        }

    }
    void getLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            Toast.makeText(getApplicationContext(), "In 1", Toast.LENGTH_LONG).show();
        }
        else{
            //locationManager.reques
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 300, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    LatLng sydney = new LatLng(lat, lon);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    Toast.makeText(getApplicationContext(), ""+lat+"  "+lon, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
            /*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                int lat = (int)location.getLatitude();
                int lon = (int)location.getLongitude();
                LatLng sydney = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                Toast.makeText(getApplicationContext(), "In 2", Toast.LENGTH_LONG).show();
            }
            else{
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                Toast.makeText(getApplicationContext(), "In 3", Toast.LENGTH_LONG).show();
            }*/
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocation();
        // Add a marker in Sydney and move the camera

    }
}
