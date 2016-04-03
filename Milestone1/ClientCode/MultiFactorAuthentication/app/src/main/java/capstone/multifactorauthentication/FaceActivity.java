package capstone.multifactorauthentication;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.File;
import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;


public class FaceActivity extends AppCompatActivity{

        private String name;

        /** Create a file Uri for saving an image or video */
        private Uri getOutputMediaFileUri(String name){
            return Uri.fromFile(getOutputMediaFile(name));
        }

        /** Create a File for saving an image or video */
        private File getOutputMediaFile(String name){
            File mediaStorageDir = new File(this.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES), "MultiFactorAuthentication");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d("MFA", "failed to create directory");
                    return null;
                }
            }

            File mediaFile;
             mediaFile = new File(mediaStorageDir.getPath() + File.separator+ name + ".jpg");
            Log.v("name", name);
            Log.v("fname", mediaFile.getName());
            return mediaFile;
        }


        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putString("name", name);
        }

        @Override
        public void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            name = savedInstanceState.getString("name");
        }

        private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
        private Uri fileUri;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_face);
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    name = null;
                } else {
                    name = extras.getString("name");
                }
            }


            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri("Image_out"); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);// getFilesDir().toString()+"Image_out"); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//            onActivityResult();

        }




        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            String res = getString(R.string.result);
            JSONMessage m = new JSONMessage(this);
//            File file = new File(getFilesDir().toString()+ File.separator+"Image_out");
            File f2 = new File(fileUri.getPath());
            Bitmap b = BitmapFactory.decodeFile(fileUri.getPath());
            Log.v("res code", (resultCode == Activity.RESULT_OK)+"");
            if(f2.exists()){
                Log.v("face", "success");
                Log.v("path", fileUri.getPath());
                res = "Success";
//                return;

                m.AddString("verType", "face");
                m.AddBmp("data", b);
                m.AddString("name", name);
                Intent intent = new Intent(this, MessageSendActivity.class);
                intent.putExtra("JSON", m.getStringContent());
                startActivity(intent);
            }
            else{
                Log.v("face", "Failure");
                Log.v("path", fileUri.getPath());
                res = "failure";
            }
        }





}
