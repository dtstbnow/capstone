package capstone.multifactorauthentication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;

public class MainActivity extends AppCompatActivity implements HttpRequest.RequestCallback {
    public final static String EXTRA_MESSAGE = "com.capstone.MultiFactorAuthentication.MESSAGE";
    private int mGetRequest;
    private int mPostRequest;
    private int mPutRequest;
    private int mDeleteRequest;
    ProgressBar lightMeter;
    TextView lightMax, lightReading;
    TextView soundMax, soundReading;
    float counter;
    Button read;
    TextView display1; // light
    TextView display2; // sound
    double REFERENCE = 0.00002;
    double decibelVal = 0.0;
    int bufferSize = 0;
    AudioRecord recorder;
    String TAG = "OUTPUT";
    private int frequency = 44100;
    private TimerTask tt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SensorManager sensorManager
                = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null){
            Toast.makeText(MainActivity.this,
                    "No Light Sensor! quit-",
                    Toast.LENGTH_LONG).show();
        }else{
            float max =  lightSensor.getMaximumRange();

            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }

        // noise level
        bufferSize = AudioRecord.getMinBufferSize(frequency, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        // set the buffer size larger
        bufferSize=bufferSize*4;

        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "This is a test", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void sendMessage(View view){
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
////        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = "test";//editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }
//
    public void submit(View view){
        EditText mEdit = (EditText)findViewById(R.id.name);

        EditText ip = (EditText)findViewById(R.id.ip);
//        JSONMessage m = new JSONMessage(this);
//        m.AddString("name", mEdit.getText().toString());

        Intent intent = new Intent(this, VerificationActivity.class);
        if(ip.getText().toString().length() == 0){
            intent.putExtra("ip", "http://camelot.memphis.edu:8080");
        }
        else{
            intent.putExtra("ip", ip.getText().toString());
        }

        intent.putExtra("name", mEdit.getText().toString());
        HttpRequest.init(getApplicationContext());
        HttpRequest.setRequestTimeout(20000);
        HttpRequest.setRequestRetries(4);
//        mPostRequest = HttpRequest.get(this, ip.getText().toString() + "/Register", new JSONObject());
        JSONMessage m = new JSONMessage(this);
        //Should send Decibel level, luminance, username, mac
        m.AddString("name", mEdit.getText().toString());
        mPostRequest = HttpRequest.post(this, ip.getText().toString() + "/Reserve", m.getContent());
//            mPostRequest = HttpRequest.post(this, "http://camelot.memphis.edu:8080/Register", json);
//        startActivity(intent);
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        if (requestCode == mGetRequest){

        }
        else if (requestCode == mPostRequest){
            Log.v("res", result);
            JSONObject j;
            String method1;
            String method2;
            Intent intent;// = new Intent(this, VerificationActivity.class);
            EditText mEdit = (EditText)findViewById(R.id.name);

            EditText ip = (EditText)findViewById(R.id.ip);
            try{
                j = new JSONObject(result);
                Log.v("json response", j.toString());
                method1 = j.getString("MethodOne");
                method2 = j.getString("MethodTwo");
                switch(method1){
                    case "face": intent = new Intent(this, FaceActivity.class);
                        break;
                    case "voice":  intent = new Intent(this, VoiceActivity.class);
                        break;
                    case "pwd":  intent = new Intent(this, PasswordActivity.class);
                        break;
                    case "sms":  intent = new Intent(this, SMSActivity.class);
                            break;
                    default: intent = new Intent(this, PasswordActivity.class);
                }
                if(ip.getText().toString().length() == 0){
                    intent.putExtra("ip", "http://camelot.memphis.edu:8080");
                }
                else{
                    intent.putExtra("ip", ip.getText().toString());
                }

                intent.putExtra("name", mEdit.getText().toString());
                intent.putExtra("method2", method2);
                startActivity(intent);

            }
            catch(JSONException e){
                Log.e("JSON", e.toString());
            }

        }
        else if (requestCode == mPutRequest){

        }
        else if (requestCode == mDeleteRequest){

        }
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){
        Log.v("req errr", error.toString());

        if (requestCode == mGetRequest){

        }
        else if (requestCode == mPostRequest){

        }
        else if (requestCode == mPutRequest){

        }
        else if (requestCode == mDeleteRequest){

        }
    }

    public void register(View view){
        EditText mEdit = (EditText)findViewById(R.id.name);
        EditText ip = (EditText)findViewById(R.id.ip);
        if(mEdit.length() == 0) {
            TextView textView = (TextView) findViewById(R.id.error);
            textView.setText("Name cannot be blank");
            return;
        }

//        JSONMessage m = new JSONMessage(this);
//        m.AddString("name", mEdit.getText().toString());
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.putExtra("name", mEdit.getText().toString());
        if(ip.getText().toString().length() == 0){
            intent.putExtra("ip", "http://camelot.memphis.edu:8080");
        }
        else{
            intent.putExtra("ip", ip.getText().toString());
        }
        intent.putExtra("register", true);
        startActivity(intent);
    }
//
//    public void renderSMS(View view){
//        Intent intent = new Intent(this, SMSActivity.class);
//        startActivity(intent);
//    }
//
//    public void renderFacial(View view){
//        Intent intent = new Intent(this, FaceActivity.class);
//        startActivity(intent);
//    }
//
//    public void renderVoice(View view){
//        Intent intent = new Intent(this, VoiceActivity.class);
//        startActivity(intent);
//    }
SensorEventListener lightSensorEventListener
        = new SensorEventListener(){

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            final float currentReading = event.values[0];
//            lightMeter.setProgress((int)currentReading);
//            lightReading.setText("Current Reading(Lux): " + String.valueOf(currentReading));
//            read.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    display1.setText("" + String.valueOf(currentReading));
//                }
//            });

        }
    }

};

    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };
    public AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        Log.d(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                Log.e(TAG, "get Something!");
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, rate + "Exception, keep trying.",e);
                    }
                }
            }
        }
        return null;
    }
    public double getNoiseLevel()
    {
        short data [] = new short[bufferSize];
        double average = 0.0f;
        //recording data;
        recorder.read(data, 0, bufferSize);
        // recorder.stop();
        for (short s : data)
        {
            if(s>0)
            {
                average += Math.abs(s);
            }
            else
            {
                bufferSize--;
            }
        }
        //x=max;
        double x = average/bufferSize;
        Log.d(TAG, "x value:"+x);
        // recorder.release();

        double db=0;
        if (x==0){
            return -20.0f;
        }
        // calculating the pascal pressure based on the idea that the max amplitude (between 0 and 32767) is
        // relative to the pressure
        double pressure = x/51805.5336; //the value 51805.5336 can be derived from assuming that x=32767=0.6325 Pa and x=1 = 0.00002 Pa (the reference value)
//        Logging.d(TAG, "x="+pressure +" Pa");
        db = (20 * Math.log10(pressure/REFERENCE));
        Log.d(TAG, "db="+db);
        if(db>0)
            return db;
        return -0.0f;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
