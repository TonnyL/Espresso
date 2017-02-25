package io.github.marktony.espresso.data.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesDataSource;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by lizhaotailang on 2017/2/25.
 */

public class PackagesLocalDataSource implements PackagesDataSource {

    private static final String DATABASE_NAME = "Espresso.realm";

    @Nullable
    private static PackagesLocalDataSource INSTANCE;

    private Realm realm;

    // Prevent direct instantiation
    private PackagesLocalDataSource() {
        realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());
    }

    public static PackagesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PackagesLocalDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Package>> getPackages() {
        RealmResults<Package> results = realm.where(Package.class)
                .findAll();
        return Observable.just(realm.copyFromRealm(results));
    }

    @Override
    public Observable<Package> getPackage(@NonNull String packNumber) {
        Package pack = realm.where(Package.class)
                .equalTo("number", packNumber)
                .findFirst();
        // Can not send the Realm stuff, it is a deep copy that will
        // copy all referenced objects
        Package packCopy = realm.copyFromRealm(pack);
        return Observable.just(packCopy);
    }

    @Override
    public void savePackage(@NonNull Package pack) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pack);
        realm.commitTransaction();
    }

    @Override
    public void deletePackage(@NonNull String packageId) {
        Package p = realm.where(Package.class).equalTo("number", packageId)
                .findFirst();
        realm.beginTransaction();
        if (p != null) {
            p.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    @Override
    public void refreshPackages() {
        INSTANCE = null;
    }

}
