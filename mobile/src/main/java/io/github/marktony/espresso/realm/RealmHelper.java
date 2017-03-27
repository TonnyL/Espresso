package io.github.marktony.espresso.realm;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by lizhaotailang on 2017/3/22.
 */

public class RealmHelper {

    public static final String DATABASE_NAME = "Espresso.realm";

    public static Realm newRealmInstance() {
        return Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(RealmHelper.DATABASE_NAME)
                .build());
    }

}
