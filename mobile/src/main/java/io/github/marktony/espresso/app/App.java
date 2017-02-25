package io.github.marktony.espresso.app;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class App extends Application {

    private static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public static synchronized App getApplication() {
        return INSTANCE;
    }

}
