package nghia.ndict.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 3/20/13
 * Time: 11:20 PM
 */
public class DictionaryApplication extends Application {
    public static DictionaryApplication dictionaryApplication;

    public static DictionaryApplication getInstance() {
        return dictionaryApplication;
    }

    public static Context getContext() {
        return dictionaryApplication.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dictionaryApplication = this;
    }
}
