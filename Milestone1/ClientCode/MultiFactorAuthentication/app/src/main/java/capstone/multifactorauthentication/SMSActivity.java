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

public class SMSActivity extends AppCompatActivity {
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                name = null;
            } else {
                name = extras.getString("name");
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
        EditText mEdit = (EditText)findViewById(R.id.sms);

        Log.v("Text", mEdit.getText().toString());
        JSONMessage m = new JSONMessage(this);
        m.AddString("verType", "SMS");
        m.AddString("data", mEdit.getText().toString());
        m.AddString("name", name);
        Intent intent = new Intent(this, MessageSendActivity.class);
        intent.putExtra("JSON", m.getStringContent());
        startActivity(intent);

    }

}
