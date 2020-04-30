package com.example.demo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    //-- picker
    private final int PICKER_REQUEST_CODE = 1;
//    SupportMapFragment supportMapFragment;
//    FusedLocationProviderClient client;
    private final int AUTOCOMPLETE_REQUEST_CODE = 2;
    // variables
    Button btnDonate;
    private GoogleMap gMap;
    private GoogleMapOptions gMapOptions;
    private List<MarkerItem> markerItems = new ArrayList<>();
    private ClusterManager<MarkerItem> clusterManager;
    //--

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // hooks
        btnDonate = findViewById(R.id.btnDonate);
        //supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        gMapOptions = new GoogleMapOptions();


        // -----------------support map fragment show map
        gMapOptions.mapType(GoogleMap.MAP_TYPE_TERRAIN).compassEnabled(false)
                .zoomControlsEnabled(true);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance(gMapOptions);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

        // --------------------- finish


        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DonateInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        clusterManager = new ClusterManager<MarkerItem>(this, gMap);
        gMap.setOnCameraIdleListener(clusterManager);
        gMap.setOnMarkerClickListener(clusterManager);

        LatLng sampleLatlang = new LatLng(23.7077934, 90.4236807);

        gMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                markerItems.add(new MarkerItem(latLng));
                clusterManager.addItems(markerItems);
                clusterManager.cluster();
            }
        });

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLatlang, 15));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        gMap.setMyLocationEnabled(true);

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage("Do you want to exit app?")
                .setNegativeButton("NO", null)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();

        // Another Code for another way ...............................

        /*
        public void onBackPressed(){

        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), "Press Back again to Exit", Toast.LENGTH_SHORT).show();
        if (backpress>1) {
        this.finish();
        }}

        */

    }
}