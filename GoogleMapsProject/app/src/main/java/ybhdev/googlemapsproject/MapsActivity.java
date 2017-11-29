package ybhdev.googlemapsproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;
import java.util.Map;


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


        // Add a marker in Tunisia and move the camera
        LatLng sydney = new LatLng(36.79385533873269, 10.151367189999974);
        mMap.addMarker(new MarkerOptions().position(sydney).title("yaaaaaaaaaaa rabi hezou"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(600);
        mMap.animateCamera(zoom);
// displaying current location from anchor Y X
        //to complete
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude)).title("EMBOUTEILLAGE");
                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                LatLng coord = marker.getPosition();
                Toast.makeText(MapsActivity.this, "" + marker.getTitle(), Toast.LENGTH_SHORT).show();
                mMap.addMarker(marker);



                try{
                    Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "location waiting", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (addresses.size() > 0) {
                            String str = addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() +"," + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                           // System.out.println(addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() +"," + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                            Toast.makeText(MapsActivity.this, str, Toast.LENGTH_SHORT).show(); //displaying coordinates
                            //TO DO save data in the sqlite DB
                            // transfer it to the mysql DB via a RESTFUL API
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                // System.out.println(latLng.latitude+"---"+ latLng.longitude);
            }
        });
        //directions implementation
        GoogleDirection.withServerKey("AIzaSyDwEDLHJdmg_iEUIuhW4MjzKX-WZEm87Gk")
                .from(new LatLng(37.7681994, -122.444538))
                .to(new LatLng(37.7749003,-122.4034934))
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            Toast.makeText(MapsActivity.this, "Direction", Toast.LENGTH_SHORT).show();
                        } else {
                            // Do something
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });


    }

    // locationlisteners



}

