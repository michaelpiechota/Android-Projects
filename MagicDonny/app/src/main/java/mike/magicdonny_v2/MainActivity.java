package mike.magicdonny_v2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorMan;
    private Sensor accelerometer;
    private String ballString;
    private float[] mGravity;
    private double mAccel;
    private double mAccelCurrent;
    private double mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        if (savedInstanceState != null) {
            ballString = savedInstanceState.getString(BALLSTRING);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            double x = mGravity[0];
            double y = mGravity[1];
            double z = mGravity[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = Math.sqrt( z * z);;
            double delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect

            if(mAccel > 15 & z > -120){

                // do something

                int ranNum = (int) (Math.random() * 20);

                switch (ranNum) {
                    case 1:
                        ballString = "It is certain";
                        break;
                    case 2:
                        ballString = "It is decidedly so";
                        break;
                    case 3:
                        ballString = "Without a doubt";
                        break;
                    case 4:
                        ballString = "Yes definitely";
                        break;
                    case 5:
                        ballString = "You may rely on it";
                        break;
                    case 6:
                        ballString = "As I see it yes";
                        break;
                    case 7:
                        ballString = "Most likely";
                        break;
                    case 8:
                        ballString = "Outlook good";
                        break;
                    case 9:
                        ballString = "Yes";
                        break;
                    case 10:
                        ballString = "Signs point to yes";
                        break;
                    case 11:
                        ballString = "Reply hazy try again";
                        break;
                    case 12:
                        ballString = "Ask again later";
                        break;
                    case 13:
                        ballString = "Better not tell you now";
                        break;
                    case 14:
                        ballString = "Cannot predict now";
                        break;
                    case 15:
                        ballString = "Concentrate and ask again";
                        break;
                    case 16:
                        ballString = "Don't count on it";
                        break;
                    case 17:
                        ballString = "My reply is no";
                        break;
                    case 18:
                        ballString = "My sources say no";
                        break;
                    case 19:
                        ballString = "Outlook not so good";
                        break;
                    case 20:
                        ballString = "Very doubtful";
                        break;
                }
                TextView magicEightBall = (TextView) findViewById(R.id.prediction);
                magicEightBall.setText(ballString);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    private static final String BALLSTRING = "BALLSTRING";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BALLSTRING, ballString);
    }

}
