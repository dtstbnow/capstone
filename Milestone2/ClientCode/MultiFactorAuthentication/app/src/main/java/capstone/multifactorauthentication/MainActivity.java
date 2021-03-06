package capstone.multifactorauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.capstone.MultiFactorAuthentication.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Log.v("Text", mEdit.getText().toString());
//        JSONMessage m = new JSONMessage(this);
//        m.AddString("name", mEdit.getText().toString());
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("name", mEdit.getText().toString());
        startActivity(intent);
    }

    public void register(View view){
        EditText mEdit = (EditText)findViewById(R.id.name);
        if(mEdit.length() == 0) {
            TextView textView = (TextView) findViewById(R.id.error);
            textView.setText("Name cannot be blank");
            return;
        }

        Log.v("Text", mEdit.getText().toString());
//        JSONMessage m = new JSONMessage(this);
//        m.AddString("name", mEdit.getText().toString());
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.putExtra("name", mEdit.getText().toString());
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

}
