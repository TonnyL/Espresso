package io.github.marktony.espresso.app;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import io.realm.Realm;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

}
