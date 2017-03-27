package io.github.marktony.espresso.app;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import io.github.marktony.espresso.util.SettingsUtil;
import io.realm.Realm;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        /*if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(SettingsUtil.KEY_NIGHT_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }*/
    }

}
