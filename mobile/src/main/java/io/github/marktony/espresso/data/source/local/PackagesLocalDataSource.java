/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.Callable;

import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesDataSource;
import io.github.marktony.espresso.realm.RealmHelper;
import io.reactivex.Observable;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by lizhaotailang on 2017/2/25.
 * Concrete implementation of a data source as a db.
 */

public class PackagesLocalDataSource implements PackagesDataSource {

    @Nullable
    private static PackagesLocalDataSource INSTANCE;

    // Prevent direct instantiation
    private PackagesLocalDataSource() {

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
        return Observable.fromCallable(new Callable<List<Package>>() {
            @Override
            public List<Package> call() throws Exception {
                Realm rlm = RealmHelper.newRealmInstance();
                return rlm.copyFromRealm(rlm.where(Package.class)
                                            .findAllSorted("timestamp", Sort.DESCENDING));
            }
        });
    }

    /**
     * Get a package in database of specific number.
     * @param packNumber The primary key
     *                   or in another words, the package id.
     *                   See {@link Package#number}
     * @return The observable package from database.
     */
    @Override
    public Observable<Package> getPackage(@NonNull final String packNumber) {
        return Observable.fromCallable(new Callable<Package>() {
            @Override
            public Package call() throws Exception {
                Realm rlm = RealmHelper.newRealmInstance();
                return rlm.copyFromRealm(rlm.where(Package.class)
                                            .equalTo("number", packNumber)
                                            .findFirst());
            }
        });
    }

    /**
     * Save a package to database.
     * @param pack The package to save. See {@link Package}
     */
    @Override
    public void savePackage(@NonNull Package pack) {
        Realm rlm = RealmHelper.newRealmInstance();
        // DO NOT forget begin and commit the transaction.
        rlm.beginTransaction();
        rlm.copyToRealmOrUpdate(pack);
        rlm.commitTransaction();
        rlm.close();
    }

    /**
     * Delete a package with specific id from database.
     * @param packageId The primary key of a package
     *                  or in another words, the package id.
     *                  See {@link Package#number}
     */
    @Override
    public void deletePackage(@NonNull String packageId) {
        Realm rlm = RealmHelper.newRealmInstance();
        Package p = rlm.where(Package.class)
                .equalTo("number", packageId)
                .findFirst();
        if (p != null) {
            rlm.beginTransaction();
            p.deleteFromRealm();
            rlm.commitTransaction();
        }
        rlm.close();
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
        Realm rlm = RealmHelper.newRealmInstance();
        List<Package> results = rlm.copyFromRealm(rlm.where(Package.class).findAll());

        for (Package p : results) {
            p.setReadable(false);
            p.setPushable(false);
            rlm.beginTransaction();
            rlm.copyToRealmOrUpdate(p);
            rlm.commitTransaction();
        }
        rlm.close();
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
        Realm rlm = RealmHelper.newRealmInstance();
        Package p = rlm.copyFromRealm(rlm.where(Package.class)
                .equalTo("number", packageId)
                .findFirst());
        if (p != null) {
            rlm.beginTransaction();
            p.setReadable(readable);
            // When a package is not readable, it is not pushable.
            p.setPushable(readable);
            rlm.copyToRealmOrUpdate(p);
            rlm.commitTransaction();
            rlm.close();
        }
    }

    /**
     * Query the existence of a specific number.
     * @param packageId the package number to query.
     *                  See {@link Package#number}
     * @return whether the number is in the database.
     */
    @Override
    public boolean isPackageExist(@NonNull String packageId) {
        Realm rlm = RealmHelper.newRealmInstance();
        RealmResults<Package> results = rlm.where(Package.class)
                .equalTo("number", packageId)
                .findAll();
        return (results != null) && (!results.isEmpty());
    }

    @Override
    public void updatePackageName(@NonNull String packageId, @NonNull String name) {
        Realm rlm = RealmHelper.newRealmInstance();
        Package p = rlm.where(Package.class)
                .equalTo("number", packageId)
                .findFirst();
        if (p != null) {
            rlm.beginTransaction();
            p.setName(name);
            rlm.copyToRealmOrUpdate(p);
            rlm.commitTransaction();
        }
        rlm.close();
    }

    @Override
    public Observable<List<Package>> searchPackages(@NonNull
                                                    final String keyWords) {

        return Observable.fromCallable(new Callable<List<Package>>() {
            @Override
            public List<Package> call() throws Exception {
                Realm rlm = RealmHelper.newRealmInstance();
                return rlm.copyFromRealm(rlm.where(Package.class)
                                            .like("name", "*" + keyWords + "*", Case.INSENSITIVE)
                                            .or()
                                            .like("companyChineseName",
                                                    "*" + keyWords + "*",
                                                    Case.INSENSITIVE)
                                            .or()
                                            .like("company", "*" + keyWords + "*", Case.INSENSITIVE)
                                            .or()
                                            .like("number", "*" + keyWords + "*", Case.INSENSITIVE)
                                            .findAll());
            }
        });
    }

}
