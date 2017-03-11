package io.github.marktony.espresso.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesDataSource;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by lizhaotailang on 2017/2/25.
 */

public class PackagesLocalDataSource implements PackagesDataSource {

    @Nullable
    private static PackagesLocalDataSource INSTANCE;

    private Realm realm;

    public static final String DATABASE_NAME = "Espresso.realm";

    // Prevent direct instantiation
    private PackagesLocalDataSource() {
        if ( realm == null) {
            realm = Realm.getInstance(new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .name(DATABASE_NAME)
                    .build());
        }
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
                .findAllSorted("timestamp", Sort.DESCENDING);
        return Observable.just(realm.copyFromRealm(results));
    }

    @Override
    public Observable<Package> getPackage(@NonNull String packNumber) {
        Package pack = realm.where(Package.class)
                .equalTo("number", packNumber)
                .findFirst();
        return pack != null ? Observable.just(realm.copyFromRealm(pack)) : null;
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
    public Observable<List<Package>> refreshPackages() {
        return null;
    }

    @Override
    public Observable<Package> refreshPackage(@NonNull String packageId) {
        return null;
    }

    @Override
    public void setAllPackagesRead() {
        RealmResults<Package> results = realm.where(Package.class)
                .notEqualTo("readable", true).findAll();
        for (Package p : results) {
            realm.beginTransaction();
            p.setReadable(false);
            realm.copyFromRealm(p);
            realm.commitTransaction();
        }
    }

    @Override
    public void setPackageReadable(@NonNull String packageId, boolean readable) {
        realm.beginTransaction();
        Package p = realm.where(Package.class)
                .equalTo("number", packageId)
                .findFirst();
        if (p != null) {
            p.setReadable(readable);
            realm.copyToRealmOrUpdate(p);
        }
        realm.commitTransaction();
    }

    /**
     * Query the existence of a specific number.
     * @param packageId the package number to query
     * @return whether the number is in the database
     */
    @Override
    public boolean isPackageExist(@NonNull String packageId) {
        RealmResults<Package> results = realm.where(Package.class)
                .equalTo("number", packageId)
                .findAll();
        return (results != null) && (!results.isEmpty());
    }

}
