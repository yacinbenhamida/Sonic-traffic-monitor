package ybhdev.googlemapsproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng myPosition;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // needs manifest authororization otherwise it'll display compiler err
        // no need to check if else etc
        Intent i = getIntent(); //passing the username
       username = i.getStringExtra("username");

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
         LatLngBounds TUNIS = new LatLngBounds(
                new LatLng(30.037000, 8.854980), new LatLng( 37.095717,  11.052246));
        googleMap.setLatLngBoundsForCameraTarget(TUNIS);
        googleMap.isTrafficEnabled(); // traffic
        // camera zoom
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(6);
        mMap.animateCamera(zoom);
// displaying current location from anchor Y X
        //to complete
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            String m_Text;
            MarkerOptions marker;
            String fulladr = "";
            @Override
            public void onMapClick(LatLng latLng) {
                  marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude)).title("EMBOUTEILLAGE");
                //marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                LatLng coord = marker.getPosition();
                //Toast.makeText(MapsActivity.this, "" + marker.getTitle(), Toast.LENGTH_SHORT).show();
               // mMap.addMarker(marker);

                //used geocoder to get the clicked pointer location from Anchor X & Y
                try{
                    Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "location waiting", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (addresses.size() > 0) {
                            fulladr = addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() +"," + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();



                          //  Toast.makeText(MapsActivity.this, fulladr, Toast.LENGTH_SHORT).show(); 
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                // dialog box prompt intensity

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Traffic intensity? ( in %)");

            // Set up the input
                final EditText input = new EditText(MapsActivity.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
               input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

            // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Toast.makeText(MapsActivity.this, ""+m_Text, Toast.LENGTH_SHORT).show();
                        // save to DB
                        saveToDatabase(marker.getAnchorV(),marker.getAnchorU(),Integer.parseInt(m_Text),fulladr);
                        mMap.addMarker(marker);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                // end dialog

            }
        });

    // find current location
        //display getcurrent location button (top right)
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //  needs authorization via manifest
        // in android v2.0 this test isn't needed
        // for android v3.0 + IDE's compilers + Target devices marshmallow 6.0 +
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } // bullshit
        mMap.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            myPosition = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(myPosition).title("Ma position"));


        }



    }
    public void saveToDatabase(double anchorX,double anchorY,int intensity,String fullAdr)
    {
        String type="savepost";
        AsyncSaveReport asyncSaveReport = new AsyncSaveReport(this);
        asyncSaveReport.execute(type,String.valueOf(anchorX),String.valueOf(anchorY),fullAdr,String.valueOf(intensity),username);
    }





}

