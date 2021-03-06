package capstone.multifactorauthentication;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;

public class MessageSendActivity extends AppCompatActivity implements HttpRequest.RequestCallback {

    private int mGetRequest;
    private int mPostRequest;
    private int mPutRequest;
    private int mDeleteRequest;

    private Boolean register = false;
    private String ip = "http://camelot.memphis.edu:8080";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        HttpRequest.init(getApplicationContext());
        setContentView(R.layout.activity_message_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String newString;
        JSONObject json = new JSONObject();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            register = extras.getBoolean("register");
            if(extras == null) {
                newString= null;

            } else {
                newString= extras.getString("JSON");
                if(extras.getString("ip") != null){
                    ip = extras.getString("ip");
                }
                try {
                    json = new JSONObject(newString);
                }
                catch(JSONException e){
                    Log.v("Json", e.toString());
                }

            }
        } else {
            newString= (String) savedInstanceState.getSerializable("JSON");
        }

        JSONObject postBody = new JSONObject();
        JSONObject putBody = new JSONObject();
        //Add parameters to the json objects
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(json.toString());
        //Make the requests
//        mGetRequest = HttpRequest.get(this, "https://141.225.8.149");
        HttpRequest.setRequestTimeout(20000);
        HttpRequest.setRequestRetries(4);
        Log.v("about to send", json.toString());
        if(register) {
            mPostRequest = HttpRequest.post(this, ip+"/Register", json);

//            mPostRequest = HttpRequest.post(this, "http://camelot.memphis.edu:8080/Register", json);
        }else{
            WifiManager manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            String address = info.getMacAddress();
            try{

                json.accumulate("macAdress", address);
            }
            catch(JSONException e){}
            Log.v("Sent", json.toString());
            mPostRequest = HttpRequest.post(this, ip+"/Authenticate", json);

        }
//        mPutRequest = HttpRequest.put(this, "https://141.225.8.149", putBody);
//        mDeleteRequest = HttpRequest.delete(this, "https://141.225.8.149");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onRequestComplete(int requestCode, String result){
        if (requestCode == mGetRequest){

        }
        else if (requestCode == mPostRequest){
            Log.v("res", result);
            JSONObject j;
            String response;
            try{
                j = new JSONObject(result);
                Log.v("j", j.toString());
                response = j.getString("Response");
                Log.v("Res", response);

            }
            catch(JSONException e){
                response = e.toString();
            }
            TextView textView = (TextView) findViewById(R.id.request_result);
            textView.setText(response);

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

}
