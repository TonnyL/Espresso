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
 * Concrete implementation of a data source as a db.
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

    // Access this instance for other classes.
    public static PackagesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PackagesLocalDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Get the packages in database and sort them in timestamp descending.
     * @return The observable packages from database.
     */
    @Override
    public Observable<List<Package>> getPackages() {
        RealmResults<Package> results = realm.where(Package.class)
                .findAllSorted("timestamp", Sort.DESCENDING);
        return Observable.just(realm.copyFromRealm(results));
    }

    /**
     * Get a package in database of specific number.
     * @param packNumber The primary key
     *                   or in another words, the package id.
     *                   See {@link Package#number}
     * @return The observable package from database.
     */
    @Override
    public Observable<Package> getPackage(@NonNull String packNumber) {
        Package pack = realm.where(Package.class)
                .equalTo("number", packNumber)
                .findFirst();
        return pack != null ? Observable.just(realm.copyFromRealm(pack)) : null;
    }

    /**
     * Save a package to database.
     * @param pack The package to save. See {@link Package}
     */
    @Override
    public void savePackage(@NonNull Package pack) {
        // DO NOT forget begin and commit the transaction.
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pack);
        realm.commitTransaction();
    }

    /**
     * Delete a package with specific id from database.
     * @param packageId The primary key of a package
     *                  or in another words, the package id.
     *                  See {@link Package#number}
     */
    @Override
    public void deletePackage(@NonNull String packageId) {
        Package p = realm.where(Package.class)
                .equalTo("number", packageId)
                .findFirst();
        realm.beginTransaction();
        if (p != null) {
            p.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    @Override
    public Observable<List<Package>> refreshPackages() {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
        return null;
    }

    @Override
    public Observable<Package> refreshPackage(@NonNull String packageId) {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
        return null;
    }

    /**
     * Set all the packages which are the unread new read.
     */
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

    /**
     * Set a package of specific number read or unread new.
     * @param packageId The primary key or the package id.
     *                  See {@link Package#number}
     * @param readable Read or unread new.
     *                 See {@link Package#readable}
     */
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
     * @param packageId the package number to query.
     *                  See {@link Package#number}
     * @return whether the number is in the database.
     */
    @Override
    public boolean isPackageExist(@NonNull String packageId) {
        RealmResults<Package> results = realm.where(Package.class)
                .equalTo("number", packageId)
                .findAll();
        return (results != null) && (!results.isEmpty());
    }

}
