package com.harpreet.leave_management;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng gndu = new LatLng(31.633969 , 74.825948);
        LatLng gndu2 = new LatLng(31.635485 , 74.825626);
        LatLng gndu3 = new LatLng(31.638318 , 74.823642);

        mMap.addMarker(new MarkerOptions().position(gndu).title("GNDU"));
        mMap.addMarker(new MarkerOptions().position(gndu2).title("GNDU1"));
        mMap.addMarker(new MarkerOptions().position(gndu3).title("GNDU2"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gndu, 12.0f));



    }
}
