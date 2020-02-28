package com.example.eztoll;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper mydb;
    //SQLiteDatabase mDatabase;
    public static final String DATABASE_NAME = "Registration.db";
    public static final String TABLE_NAME = "toll_location_table";

    static String[] array_lat;
    static String[] array_lon;
    static String[] array_date;
    static int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        DatabaseHelper mydb = new DatabaseHelper(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Cursor res = mydb.getlld();
        if (res.getCount() == 0) {
            //showMessage("Error", "Nothing Found");
            return;
        }

        array_lat = new String[res.getCount()];
        array_lon = new String[res.getCount()];
        array_date = new String[res.getCount()];

        while(res.moveToNext()){
            String lat = res.getString(res.getColumnIndex("LATITUDE"));
            String lon = res.getString(res.getColumnIndex("LONGITUDE"));
            String date = res.getString(res.getColumnIndex("DATE"));
            array_lat[i] = lat;
            array_lon[i] = lon;
            array_date[i] = date;
            i++;
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
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw =(BitmapDrawable)getResources().getDrawable(R.drawable.logo7);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        Marker m1 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(12.936110, 77.602583))
                .anchor(0.5f, 0.5f)
                .title(array_lat[0] +','+ array_lon[0])
                .snippet(array_date[0])
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                );


        Marker m2 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(array_lat[i-1]),Double.parseDouble(array_lon[i-1])))
                .anchor(0.5f, 0.5f)
                .title(array_lat[i-1] +','+ array_lon[i-1])
                .snippet(array_date[i-1])
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                );


        Marker m3 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(12.935140,77.609224))
                .anchor(0.5f, 0.5f)
                .title(array_lat[0] +','+ array_lon[0])
                .snippet(array_date[0])
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                );

    }
}
