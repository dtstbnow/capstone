package capstone.multifactorauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;


public class VoiceActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName =  "output";
    boolean mStartPlaying = true;
    private String name;

    Activity _this = this;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;

    private submitButton mSubmitButton = null;
    boolean mStartRecording = true;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(getFilesDir().toString() + mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        if(mRecorder != null){return;}
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(getFilesDir().toString() + mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            Log.e(LOG_TAG, e.toString());
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {


        OnClickListener clicker = new OnClickListener() {


            public void onClick(View v) {

                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {


        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }



    class submitButton extends Button {

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
//                Log.v("startPlaying", ""+mStartRecording);
//                if (mStartRecording) {
//                    setText("Finish Recording before submitting");
//                } else {
//                    setText("Submit");
                    submit();
                }
//            }
        };

        public submitButton(Context ctx) {
            super(ctx);
            setText("Submit");
            setOnClickListener(clicker);
        }

        public void submit(){

            File f2 = new File(getFilesDir().toString() + mFileName);

            if(f2.exists()){
                Log.v("file", "Does exist");
                JSONMessage m = new JSONMessage(_this);
                m.AddString("verType", "voice");
                m.AddFile("data", f2);
                m.AddString("name", name);
                Intent intent = new Intent(_this, MessageSendActivity.class);
                intent.putExtra("JSON", m.getStringContent());
                startActivity(intent);

            }
            else{
                Log.v("file", "Doesn't exist");
            }
        }
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        mSubmitButton = new submitButton(this);
        ll.addView(mSubmitButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));


        setContentView(ll);
        if (icicle == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                name = null;
            } else {
                name = extras.getString("name");
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}




