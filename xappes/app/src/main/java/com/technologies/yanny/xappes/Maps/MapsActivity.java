package com.technologies.yanny.xappes.Maps;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.models.nosql.TrobadesDO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.Trobades.TrobadesFragment;
import com.technologies.yanny.xappes.utilities.Marca;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private Marker mCurrLocationMarker;
    private Map<String,Marca> marcas;

    private List<TrobadesDO> trobades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        trobades = (ArrayList) intent.getParcelableArrayListExtra("selectedTrobada");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        this.marcas = new ArrayMap<>();

        mGoogleMap=googleMap;

        /*Marca m0 = new Marca("Marker in Sydney","HOLA QUE ASE",new LatLng(-34, 151), R.drawable.san, this);
        Marca m1 = new Marca("Marker in Sydney 2","HOLA QUE ASE 2",new LatLng(-64, 111), -1, this);
        Marca m2 = new Marca("Marker in Sydney 3","HOLA QUE ASE 3",new LatLng(4, 41), -1, this);

        this.marcas.put(m0.generateMarker(mGoogleMap), m0);
        this.marcas.put(m1.generateMarker(mGoogleMap), m1);
        this.marcas.put(m2.generateMarker(mGoogleMap), m2);*/

        for (TrobadesDO trobada : this.trobades) {
            int marca;
            switch (trobada.getTipoMarca().toLowerCase().trim()) {
                case "mgris":
                    marca = R.drawable.mgris;
                    break;
                case "mrojo":
                    marca = R.drawable.mrojo;
                    break;
                case "mrosa":
                    marca = R.drawable.mrosa;
                    break;
                case "mturquesa":
                    marca = R.drawable.mturquesa;
                    break;
                case "mverde":
                    marca = R.drawable.mverde;
                    break;
                default:
                    marca = R.drawable.mazul;
                    break;
            }
            Marca m = new Marca(trobada.getTrobadaTitle(),trobada.getTrobadaText(),new LatLng(Integer.parseInt(trobada.getTrobadaLat()), Integer.parseInt(trobada.getTrobadaLon())), marca, this);
            this.marcas.put(m.generateMarker(mGoogleMap), m);


        }
        //Initialize Google Play Services
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mGoogleMap.setMyLocationEnabled(true);
        }
        if (this.trobades.size() == 1) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Integer.parseInt(this.trobades.get(0).getTrobadaLat()), Integer.parseInt(this.trobades.get(0).getTrobadaLon()))).zoom(13).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } else {
            moveMapToMyLocation(getLastBestLocation());
        }

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Marca selectedMarca = marcas.get(marker.getId());
                startFragmentTrobades(selectedMarca);
                return true;
            }
        });
    }

    private Location getLastBestLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location locationGPS = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            long GPSLocationTime = 0;
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }

            long NetLocationTime = 0;

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            if (0 < GPSLocationTime - NetLocationTime) {
                return locationGPS;
            } else {
                return locationNet;
            }
        }
        return null;
    }

    private void startFragmentTrobades(Marca selectedMarca) {
        Bundle args = new Bundle();
        args.putString("title", selectedMarca.getTitle());
        args.putString("description", selectedMarca.getContenido());
        TrobadesFragment fragment = new TrobadesFragment();
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, fragment)
                .commit();
    }

    private void moveMapToMyLocation(Location loc) {
        if (loc != null) {
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .zoom(12.8f)
                    .build();

            CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
            mGoogleMap.moveCamera(camUpdate);

        } else {
            Toast.makeText(this,"ERROR buscando ubicación", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location)
    {
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
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

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
