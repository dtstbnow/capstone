package capstone.multifactorauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordActivity extends AppCompatActivity {
    private String name;
    private String ip;
    private Boolean register = false;
    private String next_auth;
    private String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                name = null;
                ip = null;
            } else {
                name = extras.getString("name");
                ip = extras.getString("ip");
                register = extras.getBoolean("register");
                next_auth = extras.getString("method2");
                json = extras.getString("JSON");

            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void submit(View view) {
        EditText mEdit = (EditText) findViewById(R.id.password);
        if (!register) {

            JSONObject j;
            try {
                j = new JSONObject(json);
            }
            catch(JSONException e){
                return;
            }
            JSONMessage m = new JSONMessage(j);
            m.AddString("verType", "Password");
            if(next_auth != null){
                m.AddString("method1",  mEdit.getText().toString());
            }
            else{
                m.AddString("method2", mEdit.getText().toString());
            }

            m.AddString("name", name);
            Intent intent;
            switch(next_auth){
                case "face": intent = new Intent(this, FaceActivity.class);
                    break;
                case "voice":  intent = new Intent(this, VoiceActivity.class);
                    break;
                case "pwd":  intent = new Intent(this, PasswordActivity.class);
                    break;
                case "sms":  intent = new Intent(this, SMSActivity.class);
                    break;
                default:  intent =  new Intent(this, MessageSendActivity.class);
            }
            intent.putExtra("JSON", m.getStringContent());
            intent.putExtra("ip", ip);
            startActivity(intent);
        }
        else{
            JSONMessage m = new JSONMessage(this);

            m.AddString("name", name);
            m.AddString("Password", mEdit.getText().toString());
            Intent intent = new Intent(this, SMSRegister.class);
            intent.putExtra("JSON", m.getStringContent());
            intent.putExtra("ip", ip);
            intent.putExtra("register", true);
            startActivity(intent);
        }


    }

}
