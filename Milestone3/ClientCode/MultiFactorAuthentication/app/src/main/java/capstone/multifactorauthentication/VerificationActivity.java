package capstone.multifactorauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class VerificationActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.capstone.MultiFactorAuthentication.MESSAGE";
    public String name;
    public String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
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
            }
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

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "test";//editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void renderPassword(View view){
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void renderSMS(View view){
        Intent intent = new Intent(this, SMSActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void renderFacial(View view){
        Intent intent = new Intent(this, FaceActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void renderVoice(View view){
        Intent intent = new Intent(this, VoiceActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }
}
