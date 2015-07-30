package nghia.ndict.dictionary;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import nghia.ndict.activity.DictionaryApplication;
import nghia.ndict.utils.Utils;

/**
 * Created by Jack Huynh on 7/12/2015.
 */
public class DictTextToSpeech {
    private static TextToSpeech mTextToSpeech;
    private static boolean mLoaded = false;

    public static void loaded(boolean value){
        mLoaded = true;
    }

    public static boolean isLoaded(){
        return mLoaded;
    }

    public static void initialize(Context context, TextToSpeech.OnInitListener listener) {
        mTextToSpeech = new TextToSpeech(context, listener);
    }

    public static TextToSpeech getInstance(){
        return mTextToSpeech;
    }

    public static void speak(String text){
        if(mTextToSpeech != null && mLoaded){
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
        else{
            Utils.toast(DictionaryApplication.getContext(), "Text to speech is not ready");
        }
    }

    public static void release(){
        if(mTextToSpeech != null && mLoaded){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
    }
}
