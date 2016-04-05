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

public class SMSRegister extends AppCompatActivity {

    private String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsregister);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                json = null;
            } else {
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

    public void submit(View view){
        JSONObject j;
        EditText mEdit = (EditText)findViewById(R.id.sms);
        try {
            j = new JSONObject(json);
        }
        catch(JSONException e){
            return;
        }

        Log.v("Text", mEdit.getText().toString());
        JSONMessage m = new JSONMessage(j);
        m.AddString("SMS", mEdit.getText().toString());
        Intent intent = new Intent(this, FaceActivity.class);
        intent.putExtra("JSON", m.getStringContent());
        intent.putExtra("register", true);
        startActivity(intent);

    }

}
