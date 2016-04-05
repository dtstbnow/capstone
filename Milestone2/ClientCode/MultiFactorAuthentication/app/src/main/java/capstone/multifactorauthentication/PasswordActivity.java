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

public class PasswordActivity extends AppCompatActivity {
    private String name;
    private Boolean register = false;
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
            } else {
                name = extras.getString("name");
                register = extras.getBoolean("register");
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

            Log.v("Text", mEdit.getText().toString());
            JSONMessage m = new JSONMessage(this);
            m.AddString("verType", "Password");
            m.AddString("data", mEdit.getText().toString());
            m.AddString("name", name);
            Intent intent = new Intent(this, MessageSendActivity.class);
            intent.putExtra("JSON", m.getStringContent());
            startActivity(intent);
        }
        else{
            JSONMessage m = new JSONMessage(this);
            m.AddString("Password", mEdit.getText().toString());
            m.AddString("name", name);
            Intent intent = new Intent(this, SMSRegister.class);
            intent.putExtra("JSON", m.getStringContent());
            intent.putExtra("register", true);
            startActivity(intent);
        }


    }

}
