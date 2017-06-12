package mike.android_sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView title;
    TextView accelerationView;
    TextView linearAccelerationView;
    TextView gravityView;
    TextView gyroscopeView;
    TextView lightView;
    TextView magneticFieldView;
    TextView proximityView;
    TextView pressureView;
    TextView temperatureView;
    TextView accelerationExtremesView;
    TextView linearAccelerationExtremesView;
    TextView gravityExtremesView;
    TextView gyroscopeExtremesView;
    TextView lightExtremesView;
    TextView magneticFieldExtremesView;
    TextView proximityExtremesView;
    TextView pressureExtremesView;
    TextView temperatureExtremesView;

    SensorManager myManager;
    Sensor acceleration;
    Sensor linear_acceleration;
    Sensor gravity;
    Sensor gyroscope;
    Sensor light;
    Sensor magneticField;
    Sensor proximity;
    Sensor pressure;
    Sensor temperature;

    Float min_acceleration_X = 0.00f;
    Float min_acceleration_Y = 0.00f;
    Float min_acceleration_Z = 0.00f;
    Float min_linearAcceleration_X = 0.00f;
    Float min_linearAcceleration_Y = 0.00f;
    Float min_linearAcceleration_Z = 0.00f;
    Float min_gravity_X = 0.00f;
    Float min_gravity_Y = 0.00f;
    Float min_gravity_Z = 0.00f;
    Float min_gyroscope_X = 0.00f;
    Float min_gyroscope_Y = 0.00f;
    Float min_gyroscope_Z = 0.00f;
    Float min_light_value = 0.00f;
    Float min_magneticField_X = 0.00f;
    Float min_magneticField_Y = 0.00f;
    Float min_magneticField_Z = 0.00f;
    Float min_proximity_value = 0.00f;
    Float min_pressure_value = 0.00f;
    Float min_temperature_value = 0.00f;
    Float max_acceleration_X = 0.00f;
    Float max_acceleration_Y = 0.00f;
    Float max_acceleration_Z = 0.00f;
    Float max_linearAcceleration_X = 0.00f;
    Float max_linearAcceleration_Y = 0.00f;
    Float max_linearAcceleration_Z = 0.00f;
    Float max_gravity_X = 0.00f;
    Float max_gravity_Y = 0.00f;
    Float max_gravity_Z = 0.00f;
    Float max_gyroscope_X = 0.00f;
    Float max_gyroscope_Y = 0.00f;
    Float max_gyroscope_Z = 0.00f;
    Float max_light_value = 0.00f;
    Float max_magneticField_X = 0.00f;
    Float max_magneticField_Y = 0.00f;
    Float max_magneticField_Z = 0.00f;
    Float max_proximity_value = 0.00f;
    Float max_pressure_value = 0.00f;
    Float max_temperature_value = 0.00f;

    boolean accelerationExists;
    boolean linear_accelerationExists;
    boolean gravityExists;
    boolean gyroscopeExists;
    boolean lightExists;
    boolean magneticFieldExists;
    boolean proximitySensorExists;
    boolean pressureExists;
    boolean temperatureExists;
    boolean first_acceleration = false;
    boolean first_linearAcceleration = false;
    boolean first_gravity = false;
    boolean first_gyroscope = false;
    boolean first_light = false;
    boolean first_magneticField = false;
    boolean first_proximity = false;
    boolean first_pressure = false;
    boolean first_temperature = false;

    //*************************************ON CREATE METHOD*****************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.current);
        accelerationView = (TextView) findViewById(R.id.textView1);
        linearAccelerationView = (TextView) findViewById(R.id.textView2);
        gravityView = (TextView) findViewById(R.id.textView3);
        gyroscopeView = (TextView) findViewById(R.id.textView4);
        lightView = (TextView) findViewById(R.id.textView5);
        magneticFieldView = (TextView) findViewById(R.id.textView6);
        proximityView = (TextView) findViewById(R.id.textView7);
        pressureView = (TextView) findViewById(R.id.textView8);
        temperatureView = (TextView) findViewById(R.id.textView9);
        accelerationExtremesView = (TextView) findViewById(R.id.textView12);
        linearAccelerationExtremesView = (TextView) findViewById(R.id.textView13);
        gravityExtremesView = (TextView) findViewById(R.id.textView14);
        gyroscopeExtremesView = (TextView) findViewById(R.id.textView15);
        lightExtremesView = (TextView) findViewById(R.id.textView16);
        magneticFieldExtremesView = (TextView) findViewById(R.id.textView17);
        proximityExtremesView = (TextView) findViewById(R.id.textView18);
        pressureExtremesView = (TextView) findViewById(R.id.textView19);
        temperatureExtremesView = (TextView) findViewById(R.id.textView20);

        sensorExistCheck();
    }

    @Override
    public void onAccuracyChanged(Sensor event, int arg1) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            switch(event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:{
                    if(!first_acceleration){
                        min_acceleration_X = max_acceleration_X = event.values[0];
                        min_acceleration_Y = max_acceleration_Y = event.values[1];
                        min_acceleration_Z = max_acceleration_Z = event.values[2];
                        first_acceleration = true;
                    }

                    else{
                        min_acceleration_X = update_Minumum(event.values[0], min_acceleration_X);
                        min_acceleration_Y = update_Minumum(event.values[1], min_acceleration_Y);
                        min_acceleration_Z = update_Minumum(event.values[2], min_acceleration_Z);
                        max_acceleration_X =update_Maximum(event.values[0], max_acceleration_X);
                        max_acceleration_Y = update_Maximum(event.values[1], max_acceleration_Y);
                        max_acceleration_Z =update_Maximum(event.values[2], max_acceleration_Z);
                    }
                    accelerationView.setText("Acceleration:"+setText_XYXZ_Axis(event)+"   (m/s^2)");
                    accelerationExtremesView.setText("Acceleration:" + setText_XYZ_minMax(event)+"   (m/s^2)");
                    break;
                }

                case Sensor.TYPE_LINEAR_ACCELERATION:{
                    if(!first_linearAcceleration){
                        min_linearAcceleration_X = max_linearAcceleration_X = event.values[0];
                        min_linearAcceleration_Y = max_linearAcceleration_Y = event.values[1];
                        min_linearAcceleration_Z = max_linearAcceleration_Z = event.values[2];
                        first_linearAcceleration = true;
                    }

                    else{
                        min_linearAcceleration_X = update_Minumum(event.values[0], min_linearAcceleration_X);
                        min_linearAcceleration_Y = update_Minumum(event.values[1], min_linearAcceleration_Y);
                        min_linearAcceleration_Z = update_Minumum(event.values[2], min_linearAcceleration_Z);

                        max_linearAcceleration_X =update_Maximum(event.values[0], max_linearAcceleration_X);
                        max_linearAcceleration_Y = update_Maximum(event.values[1], max_linearAcceleration_Y);
                        max_linearAcceleration_Z =update_Maximum(event.values[2], max_linearAcceleration_Z);
                    }
                    linearAccelerationView.setText("Linear Acceleration:"+setText_XYXZ_Axis(event)+"   (m/s^2)");
                    linearAccelerationExtremesView.setText("Linear Acceleration:"+ setText_XYZ_minMax(event)+"   (m/s^2)");
                    break;
                }

                case Sensor.TYPE_GRAVITY:{
                    if(!first_gravity){
                        min_gravity_X = max_gravity_X = event.values[0];
                        min_gravity_Y = max_gravity_Y = event.values[1];
                        min_gravity_Z = max_gravity_Z = event.values[2];
                        first_gravity = true;
                    }

                    else{
                        min_gravity_X = update_Minumum(event.values[0], min_gravity_X);
                        min_gravity_Y = update_Minumum(event.values[1], min_gravity_Y);
                        min_gravity_Z = update_Minumum(event.values[2], min_gravity_Z);
                        max_gravity_X =update_Maximum(event.values[0], max_gravity_X);
                        max_gravity_Y = update_Maximum(event.values[1], max_gravity_Y);
                        max_gravity_Z =update_Maximum(event.values[2], max_gravity_Z);
                    }
                    gravityView.setText("Gravity:"+setText_XYXZ_Axis(event)+"   (m/s^2)");
                    gravityExtremesView.setText("Gravity:"+ setText_XYZ_minMax(event)+"   (m/s^2)");

                    break;

                }

                case Sensor.TYPE_GYROSCOPE:{
                    if(!first_gyroscope){
                        min_gyroscope_X = max_gyroscope_X = event.values[0];
                        min_gyroscope_Y = max_gyroscope_Y = event.values[1];
                        min_gyroscope_Z = max_gyroscope_Z = event.values[2];
                        first_gyroscope = true;
                    }

                    else{
                        min_gyroscope_X = update_Minumum(event.values[0], min_gyroscope_X);
                        min_gyroscope_Y = update_Minumum(event.values[1], min_gyroscope_Y);
                        min_gyroscope_Z = update_Minumum(event.values[2], min_gyroscope_Z);
                        max_gyroscope_X =update_Maximum(event.values[0], max_gyroscope_X);
                        max_gyroscope_Y = update_Maximum(event.values[1], max_gyroscope_Y);
                        max_gyroscope_Z =update_Maximum(event.values[2], max_gyroscope_Z);
                    }
                    gyroscopeView.setText("Gyroscope:"+setText_XYXZ_Axis(event)+"   (rad/sec)");
                    gyroscopeExtremesView.setText("Gyroscope:"+ setText_XYZ_minMax(event)+"   (rad/sec)");
                    break;
                }

                case Sensor.TYPE_LIGHT:{
                    if(!first_light){
                        min_light_value=max_light_value=event.values[0];
                        first_light = true;
                    }
                    else{
                        min_light_value = update_Minumum(event.values[0], min_light_value);
                        max_light_value = update_Maximum(event.values[0], max_light_value);
                    }
                    lightView.setText("Light: "+setTextforOneVal(event)+"   (LX)");
                    lightExtremesView.setText("Light: "+setText_MinMax(event)+"   (LX)");
                    break;
                }

                case Sensor.TYPE_MAGNETIC_FIELD:{
                    if(!first_magneticField){
                        min_magneticField_X = max_magneticField_X = event.values[0];
                        min_magneticField_Y = max_magneticField_Y = event.values[1];
                        min_magneticField_Z = max_magneticField_Z = event.values[2];
                        first_magneticField = true;
                    }

                    else{
                        min_magneticField_X = update_Minumum(event.values[0], min_magneticField_X);
                        min_gravity_Y = update_Minumum(event.values[1], min_magneticField_Y);
                        min_magneticField_Z = update_Minumum(event.values[2], min_magneticField_Z);
                        max_magneticField_X =update_Maximum(event.values[0], max_magneticField_X);
                        max_magneticField_Y = update_Maximum(event.values[1], max_magneticField_Y);
                        max_magneticField_Z =update_Maximum(event.values[2], max_magneticField_Z);
                    }
                    magneticFieldView.setText("Magnetic Field:"+setText_XYXZ_Axis(event)+"   (μT)");
                    magneticFieldExtremesView.setText("Magnetic Field:"+ setText_XYZ_minMax(event)+"   (μT)");
                    break;

                }

                case Sensor.TYPE_PROXIMITY:{
                    if(!first_proximity){
                        min_proximity_value=max_proximity_value=event.values[0];
                        first_proximity = true;
                    }
                    else{
                        min_proximity_value = update_Minumum(event.values[0], min_proximity_value);
                        max_proximity_value = update_Maximum(event.values[0], max_proximity_value);
                    }
                    proximityView.setText("Proximity: "+setTextforOneVal(event)+"   (cm)");
                    proximityExtremesView.setText("Proximity: "+setText_MinMax(event)+"   (cm)");
                    break;
                }

                case Sensor.TYPE_PRESSURE:{
                    if(!first_pressure){
                        min_pressure_value=max_pressure_value=event.values[0];
                        first_pressure = true;
                    }
                    else{
                        min_pressure_value = update_Minumum(event.values[0], min_pressure_value);
                        max_pressure_value = update_Maximum(event.values[0], max_pressure_value);
                    }
                    pressureView.setText("Pressure: "+setTextforOneVal(event)+"   (hPa)");
                    pressureExtremesView.setText("Pressure: "+setText_MinMax(event)+"   (hPa)");
                    break;
                }

                case Sensor.TYPE_AMBIENT_TEMPERATURE:{
                    if(!first_temperature){
                        min_temperature_value=max_temperature_value=event.values[0];
                        first_temperature = true;
                    }
                    else{
                        min_temperature_value = update_Minumum(event.values[0], min_temperature_value);
                        max_temperature_value = update_Maximum(event.values[0], max_temperature_value);
                    }
                    temperatureView.setText("Temperature: "+setTextforOneVal(event)+"   (°C)");
                    temperatureExtremesView.setText("Temperature: "+setText_MinMax(event)+"   (°C)");;
                    break;
                }
            }
        }
    }


    private String setText_XYXZ_Axis(SensorEvent e){
        return "X: "+String.format("%.2f", e.values[0])+"; Y: "+
                String.format("%.2f", e.values[1])+ "; Z: "+
                String.format("%.2f", e.values[2]);
    }

    private Float update_Minumum(Float current, Float minimum){
        if(current<minimum){
            return current;
        }
        else return minimum;
    }


    private Float update_Maximum(Float current, Float maximum){
        if(current>maximum){
            return current;
        }
        else return maximum;
    }


    private String setTextforOneVal(SensorEvent e ){
        return String.format("%.2f", e.values[0]);
    }


    private String setText_XYZ_minMax(SensorEvent e){
        if(e.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            return "X-Min: "+String.format("%.2f", min_acceleration_X)+"; Y-Min: "+
                    String.format("%.2f", min_acceleration_Y)+ "; Z-Min: "+
                    String.format("%.2f", min_acceleration_Z)+"; X-Max: "+String.format("%.2f", max_acceleration_X)+"; Y-Max: "+
                    String.format("%.2f", max_acceleration_Y)+ "; Z-Max: "+
                    String.format("%.2f", max_acceleration_Z);
        }
        else if(e.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            return "X-Min: "+String.format("%.2f", min_linearAcceleration_X)+"; Y-Min: "+
                    String.format("%.2f", min_linearAcceleration_Y)+ "; Z-Min: "+
                    String.format("%.2f", min_linearAcceleration_Z)+"; X-Max: "+String.format("%.2f", max_linearAcceleration_X)+"; Y-Max: "+
                    String.format("%.2f", max_linearAcceleration_Y)+ "; Z-Max: "+
                    String.format("%.2f", max_linearAcceleration_Z);
        }
        else if(e.sensor.getType() == Sensor.TYPE_GRAVITY){
            return "X-Min: "+String.format("%.2f", min_gravity_X)+"; Y-Min: "+
                    String.format("%.2f", min_gravity_Y)+ "; Z-Min: "+
                    String.format("%.2f", min_gravity_Z)+"; X-Max: "+String.format("%.2f", max_gravity_X)+"; Y-Max: "+
                    String.format("%.2f", max_gravity_Y)+ "; Z-Max: "+
                    String.format("%.2f", max_gravity_Z);
        }
        else if(e.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            return "X-Min: "+String.format("%.2f", min_gyroscope_X)+"; Y-Min: "+
                    String.format("%.2f", min_gyroscope_Y)+ "; Z-Min: "+
                    String.format("%.2f", min_gyroscope_Z)+"; X-Max: "+String.format("%.2f", max_gyroscope_X)+"; Y-Max: "+
                    String.format("%.2f", max_gyroscope_Y)+ "; Z-Max: "+
                    String.format("%.2f", max_gyroscope_Z);
        }
        else if(e.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            return "X-Min: "+String.format("%.2f", min_magneticField_X)+"; Y-Min: "+
                    String.format("%.2f", min_magneticField_Y)+ "; Z-Min: "+
                    String.format("%.2f", min_magneticField_Z)+"; X-Max: "+String.format("%.2f", max_magneticField_X)+"; Y-Max: "+
                    String.format("%.2f", max_magneticField_Y)+ "; Z-Max: "+
                    String.format("%.2f", max_magneticField_Z);
        }
        else{
            return "";
        }

    }


    private String setText_MinMax(SensorEvent e){
        if(e.sensor.getType() == Sensor.TYPE_LIGHT){
            return "Minimum: "+String.format("%.2f", min_light_value)+"; Maximum: "+
                    String.format("%.2f", max_light_value);
        }
        else if(e.sensor.getType() == Sensor.TYPE_PROXIMITY){
            return "Minimum: "+String.format("%.2f", min_proximity_value)+"; Maximum: "+
                    String.format("%.2f", max_proximity_value);
        }
        else if(e.sensor.getType() == Sensor.TYPE_PRESSURE){
            return "Minimum: "+String.format("%.2f", min_pressure_value)+"; Maximum: "+
                    String.format("%.2f", max_pressure_value);
        }
        else if(e.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            return "Minimum: "+String.format("%.2f", min_temperature_value)+"; Maximum: "+
                    String.format("%.2f", max_temperature_value);
        }
        else {
            return "";
        }
    }


    private void sensorExistCheck(){
        myManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            accelerationExists = true;
            acceleration = myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        else{
            accelerationView.setText("ERROR-No Accelerometer Found!");
            accelerationExtremesView.setText("ERROR-No Accelerometer Found!");
        }

        if(myManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            linear_accelerationExists = true;
            linear_acceleration = myManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }
        else{
            linearAccelerationView.setText("ERROR-NO Linear Accelerometer Found!");
            linearAccelerationExtremesView.setText("ERROR-NO Linear Accelerometer Found!");
        }
        if(myManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            gravityExists = true;
            gravity = myManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }
        else{
            gravityView.setText("ERROR-No Gravity Sensor Found!");
            gravityExtremesView.setText("ERROR-No Gravity Sensor Found!");
        }

        if(myManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            gyroscopeExists = true;
            gyroscope = myManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }
        else{
            gyroscopeView.setText("ERROR-No Gyroscope Found!");
            gyroscopeExtremesView.setText("ERROR-No Gyroscope Found!");
        }
        if(myManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            lightExists = true;
            light = myManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        else{
            lightView.setText("ERROR-No Ambient Light Sensor Found!");
            lightExtremesView.setText("ERROR-No Ambient Light Sensor Found!");
        }
        if(myManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            magneticFieldExists = true;
            magneticField = myManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        else{

            magneticFieldView.setText("ERROR-NO Magnetic Field Sensor Found!");
            magneticFieldExtremesView.setText("ERROR-NO Magnetic Field Sensor Found!");
        }
        if(myManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            proximitySensorExists = true;
            proximity = myManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        else{

            proximityView.setText("ERROR-No Proximity Sensor Found!");
            proximityExtremesView.setText("ERROR-No Proximity Sensor Found!");
        }
        if(myManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            pressureExists = true;
            pressure = myManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        }
        else{

            pressureView.setText("ERROR - NO Barometer Found!");
            pressureExtremesView.setText("ERROR-NO Barometer Found!!");
        }
        if(myManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            temperatureExists = true;
            temperature = myManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        else{

            temperatureView.setText("*ERROR* - No Ambient Temperature Sensor Found!");
            temperatureExtremesView.setText("*ERROR* - No Ambient Temperature Sensor Found!");
        }
    }


    private void sensorRegistry(){
        if (accelerationExists) {
            myManager.registerListener(this, acceleration, SensorManager.SENSOR_DELAY_UI);
        }
        if (linear_accelerationExists) {
            myManager.registerListener(this, linear_acceleration, SensorManager.SENSOR_DELAY_UI);
        }
        if (gravityExists) {
            myManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_UI);
        }
        if (gyroscopeExists) {
            myManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
        }
        if (lightExists) {
            myManager.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);
        }
        if (magneticFieldExists) {
            myManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_UI);
        }
        if (proximitySensorExists) {
            myManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_UI);
        }
        if (pressureExists) {
            myManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI);
        }
        if (temperatureExists) {
            myManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_UI);
        }
    }


    private void sensorUnregister(){
        if (accelerationExists) {
            myManager.unregisterListener(this, acceleration);
        }
        if (linear_accelerationExists) {
            myManager.unregisterListener(this, linear_acceleration);
        }
        if (gravityExists) {
            myManager.unregisterListener(this, gravity);
        }
        if (gyroscopeExists) {
            myManager.unregisterListener(this, gyroscope);
        }
        if (lightExists) {
            myManager.unregisterListener(this, light);
        }
        if (magneticFieldExists) {
            myManager.unregisterListener(this, magneticField);
        }
        if (proximitySensorExists) {
            myManager.unregisterListener(this, proximity);
        }
        if (pressureExists) {
            myManager.unregisterListener(this, pressure);
        }
        if (temperatureExists) {
            myManager.unregisterListener(this, temperature);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorRegistry();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorUnregister();
    }
}
