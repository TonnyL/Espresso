package io.github.marktony.espresso.app;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        // Prevent using a constructor to instantiation
        context = getApplicationContext();
    }

    // Access to the global context.
    public static Context getInstance() {
        return context;
    }

}
