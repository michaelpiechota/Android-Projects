package mike.piechota_project6;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, SensorEventListener {
    private GoogleMap myMap;
    private Location currentLocation;
    private LocationManager locationManager;
    private GoogleApiClient myGoogleApi_Client;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long interval_1_fast = 1000 * 5 * 1;
    private static final String logNOTE = "LocationActivity";
    private static final long INTERVAL = 1000 * 5 * 1;
    private static final float displacement = 0.25F;
    private ArrayList<LatLng> walkPoints;
    Polyline myPolyLine;

    //START VARIABLES NEEDED FOR Barometer (PRESSURE)
    String altString;
    boolean minUpdate;
    boolean maxUpdate;
    private TextView locMin;
    private TextView locMax;
    private TextView pressureMinView;
    private TextView pressureMaxView;
    SensorManager mngr;
    Sensor pressure;
    boolean pressureExists;
    TextView pressureView;
    //Min/max sensor Views
    TextView pressureExtremesView;
    Float minpress = 0.00f;
    Float maxpress = 0.00f;
    float current = 0.00f;
    Float firstFloor_pressure = 0.00f;
    Float secondFloor_pressure = 0.00f;
    Float thirdFloor_pressure = 0.00f;
    Float fourthFloor_pressure = 0.00f;
    boolean firstPressure = false;
    //END VARIABLES NEEDED FOR  Barometer (PRESSURE)

    public int currentFloor= 6;

    boolean firstPoint = true;
    int previousPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET},
                0);
        
        super.onCreate(savedInstanceState);

        walkPoints = new ArrayList<LatLng>();

        setContentView(R.layout.activity_maps);
        
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myGoogleApi_Client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        verifyLocationServices();
        sensorExistCheck();
    }


    //Sydney is set BY DEFAULT when using the GoogleMaps Template in Android Studio
    @Override
    public void onMapReady(GoogleMap myGoogleMap) {
        myMap = myGoogleMap;
        //LatLng sydney = new LatLng(-34, 151);
        //myMap.addMarker(new MarkerOptions().position(sydney).title("DEFAULT Marker in Sydney"));
        //myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
    

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(logNOTE, "*ERROR* INCORRECT PERMISSIONS");
            return;
        }

        checkLocationUpdates();

        currentLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleApi_Client);

        if (currentLocation == null) {
            checkLocationUpdates();
        }
        if (currentLocation != null) {

        } else {
            Toast.makeText(this, "ERROR THE LOCATION IS NOT DETECTED", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(logNOTE, "*WARNING* THE CONNECTION HAS BEEN SUSPENDED");
        myGoogleApi_Client.connect();
    }

    
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(logNOTE, "*ERROR* THE CONNECTION HAS FAILED BECAUSE: " + connectionResult.getErrorCode());
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (myGoogleApi_Client != null) {
            myGoogleApi_Client.connect();
        }
    }
    

    @Override
    protected void onStop() {
        super.onStop();
        if (myGoogleApi_Client.isConnected()) {
            myGoogleApi_Client.disconnect();
        }
        unregisterSensor();
    }
    

    protected void checkLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(INTERVAL)
                .setFastestInterval(interval_1_fast)
                .setSmallestDisplacement(displacement);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.i(logNOTE, "*ERROR* NEED PERMISSIONS!");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApi_Client,
                mLocationRequest, this);
        Log.d("QUEUE AGAIN", "--->>>>");
    }
    

    //This function is where the magic happens.
    //it redraws the line when the location is changed!
    @Override
    public void onLocationChanged(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        LatLng currentLocation = new LatLng(latitude, longitude);
        walkPoints.add(currentLocation);

        redrawLine();
    }
    

    //this function redraws the polyline based on the current floor
    //1st Floor = rellow; 2nd Floor = black; 3rd Floor = ; 4th Floor = green.
    private void redrawLine(){
        if(firstPoint){
            myMap.clear();  //clears all Markers and Polylines

            Toast.makeText(this, "THIS IS THE 1st FLOOR!", Toast.LENGTH_SHORT).show();


            PolylineOptions poly_opt;
            if(currentFloor == 1){
                poly_opt = new PolylineOptions().width(5).color(Color.YELLOW).geodesic(true);
                Toast.makeText(this, "THIS IS THE 1st FLOOR!", Toast.LENGTH_SHORT).show();
            }
            else if(currentFloor == 2){
                poly_opt = new PolylineOptions().width(5).color(Color.BLACK).geodesic(true);
                Toast.makeText(this, "THIS IS THE 2nd FLOOR!", Toast.LENGTH_SHORT).show();
            }
            else if (currentFloor == 3){
                poly_opt = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
                Toast.makeText(this, "THIS IS THE 3rd FLOOR!", Toast.LENGTH_SHORT).show();
            }
            else if (currentFloor == 4){
                poly_opt = new PolylineOptions().width(5).color(Color.GREEN).geodesic(true);
                Toast.makeText(this, "THIS IS THE 4th FLOOR!", Toast.LENGTH_SHORT).show();
            }
            else{
                poly_opt = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            }

            //PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int i = 0; i < walkPoints.size(); i++) {
                LatLng point = walkPoints.get(i);
                poly_opt.add(point);
            }
            myMap.addMarker(new MarkerOptions().position(walkPoints.get(0)).title("Marker in start loc"));
            myMap.moveCamera(CameraUpdateFactory.newLatLng(walkPoints.get(0)));
            myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            myPolyLine = myMap.addPolyline(poly_opt);
            firstPoint = false;
            previousPoint = walkPoints.size()-1; //changed

        }
        else{


            PolylineOptions poly_opt;
            if(currentFloor == 1){
                poly_opt = new PolylineOptions().width(5).color(Color.GREEN).geodesic(true);
                Toast.makeText(this, "THIS IS THE 1st FLOOR!", Toast.LENGTH_SHORT).show();
            }
            else if(currentFloor == 2){
                poly_opt = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
                Toast.makeText(this, "THIS IS THE 2nd FLOOR!", Toast.LENGTH_SHORT).show();
            }
            else if (currentFloor == 3){
                poly_opt = new PolylineOptions().width(5).color(Color.BLACK).geodesic(true);
                Toast.makeText(this, "THIS IS THE 3rd FLOOR!", Toast.LENGTH_SHORT).show();
            }
            else if (currentFloor == 4){
                poly_opt = new PolylineOptions().width(5).color(Color.YELLOW).geodesic(true);
                Toast.makeText(this, "THIS IS THE 4th FLOOR!", Toast.LENGTH_SHORT).show();
            }
            else{
                poly_opt = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            }

            for (int i = previousPoint; i < walkPoints.size(); i++) {
                LatLng point = walkPoints.get(i);
                poly_opt.add(point);
            }
            myPolyLine = myMap.addPolyline(poly_opt);
            previousPoint = walkPoints.size()-1; //changed
        }
    }




    /*THIS WORKS BUT IT CHANGES THE WHOLE  LINE
    //this function redraws the polyline based on the current floor
    //1st Floor = blue; 2nd Floor = red; 3rd Floor = green; 4th Floor = yellow
    private void redrawLine() {
        int floorColor = Color.BLACK;

        myMap.clear();


        if(currentFloor == 1){
            floorColor = Color.YELLOW;
            Toast.makeText(this, "THIS IS THE 1st FLOOR!", Toast.LENGTH_SHORT).show();

        }
        else if(currentFloor == 2){
            floorColor = Color.RED;
            Toast.makeText(this, "THIS IS THE 2nd FLOOR!", Toast.LENGTH_SHORT).show();

        }
        else if(currentFloor == 3){
            floorColor = Color.GREEN;
            Toast.makeText(this, "THIS IS THE 3rd FLOOR!", Toast.LENGTH_SHORT).show();

        }
        else if(currentFloor == 4){
            floorColor = Color.BLUE;
            Toast.makeText(this, "THIS IS THE 4th FLOOR!", Toast.LENGTH_SHORT).show();

        }


        PolylineOptions options = new PolylineOptions().width(5).color(floorColor).geodesic(true);


        for (int i = 0; i < walkPoints.size(); i++) {
            LatLng point = walkPoints.get(i);
            options.add(point);
        }
        myMap.addMarker(new MarkerOptions().position(walkPoints.get(0)).title("THE MARKER IS IN THE STARTING LOCATION"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(walkPoints.get(0)));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        myPolyLine = myMap.addPolyline(options);
    }
    */


    protected String getLocationName(Double lat, Double lon) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("THE CURRENT ADDRESS IS:", "" + strReturnedAddress.toString());
            } else {
                Log.w("THE CURRENT ADDRESS IS:", " ERROR! NO ADDRESS HAS BEEN RETURNED!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }


    protected void verifyLocationServices() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean availableGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean availableNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!availableGPS && !availableNetwork) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("ENABLE LOCATION!")
                    .setMessage("*WARNING* YOUR LOCATION SETTINGS MAY BE OFF, FIX IT NOW!" +
                            "")
                    .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    })
                    .setNegativeButton("CANCEL!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        }
                    });
            dialog.show();
        }
    }


    //////////////////////// Sensor Logic //////////////////////////////////////////////////////////
    public void onAccuracyChanged(Sensor event, int arg1) {

    }

    //NEW onSensorChanged function
    //This function identifies which floor your are on because the floors are seperated by what
    //their pressure should be. These pressures work for any given floor in Jacaranda but not elsewhere
    //because the elevation will be different --> the pressure will be different.
    //THIS ASSUMES YOU ARE STARTING ON THE FIRST FLOOR
    public void onSensorChanged(SensorEvent event) {
        if (!firstPressure) {
            firstFloor_pressure = event.values[0];
            currentFloor = 1;
            firstPressure = true;

        } else {

            findFloor(event.values[0], firstFloor_pressure);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerSensor();
    }


    //this function finds the # of the floor you are on.
    private void findFloor(float newPressure, float firstPressure){
        if (newPressure < firstPressure ){
            if (newPressure < (firstPressure + 0.15) && newPressure > (firstPressure + 0.15)){
                currentFloor = 1;
            }
            else if (newPressure < (firstPressure - 0.40) && newPressure > (firstPressure - 0.50)){
                currentFloor = 2;
            }
            else if (newPressure < (firstPressure - 0.90) && newPressure > (firstPressure - 1.00)){
                currentFloor = 3;
            }
            else if (newPressure < (firstPressure - 1.20) && newPressure > (firstPressure - 1.40)){
                currentFloor = 4;
            }
        }
    }


    private void sensorExistCheck() {
        mngr = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (mngr.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            pressureExists = true;
            pressure = mngr.getDefaultSensor(Sensor.TYPE_PRESSURE);
        } else {
            pressureView.setText("No barometer!");
            pressureExtremesView.setText("No barometer!");
        }
    }


    private void registerSensor() {
        if (pressureExists) {
            mngr.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void unregisterSensor() {
        if (pressureExists) {
            mngr.unregisterListener(this, pressure);
        }
    }

//////////////////////// END Sensor Logic //////////////////////////////////////////////////////////

}

