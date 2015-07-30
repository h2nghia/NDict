package nghia.ndict.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import nghia.ndict.R;
import nghia.ndict.dictionary.DictTextToSpeech;

/**
 * Created by Jack Huynh on 7/11/2015.
 */
public class SplashActivity extends BaseActivity implements TextToSpeech.OnInitListener {
    private static final String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        DictTextToSpeech.release();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Initialize TTS
                DictTextToSpeech.initialize(SplashActivity.this, SplashActivity.this);
                Log.d(TAG, "TTS initialized (not null)");
            }
        }, 2000);
    }

    @Override
    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }


    @Override
    public void onInit(int status) {
        if(DictTextToSpeech.getInstance() == null){
            DictTextToSpeech.initialize(this, this);
            return;
        }

        if (status == TextToSpeech.SUCCESS) {
            int result = DictTextToSpeech.getInstance().setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(DictionaryApplication.getContext(), "Language is not supported for text to speech", Toast.LENGTH_LONG).show();
            } else {
                DictTextToSpeech.getInstance().setSpeechRate(0.5f);
                DictTextToSpeech.loaded(true);
            }
        } else {
            Toast.makeText(DictionaryApplication.getContext(), "Failed to start text to speech", Toast.LENGTH_LONG).show();
        }

        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "On new intent");
    }

    @Override
    public void onBackPressed() {
    }
}

