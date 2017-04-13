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

package io.github.marktony.espresso.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.github.marktony.espresso.data.Package;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lizhaotailang on 2017/2/12.
 * Concrete implementation to load packages from the data sources into a cache.
 * <p/>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database
 * is not the latest.
 */

public class PackagesRepository implements PackagesDataSource {

    @Nullable
    private static PackagesRepository INSTANCE = null;

    @NonNull
    private final PackagesDataSource packagesRemoteDataSource;

    @NonNull
    private final PackagesDataSource packagesLocalDataSource;

    private Map<String, Package> cachedPackages;

    // Prevent direct instantiation
    private PackagesRepository(@NonNull PackagesDataSource packagesRemoteDataSource,
                               @NonNull PackagesDataSource packagesLocalDataSource) {
        this.packagesRemoteDataSource = packagesRemoteDataSource;
        this.packagesLocalDataSource = packagesLocalDataSource;
    }

    // The access for other classes.
    public static PackagesRepository getInstance(@NonNull PackagesDataSource packagesRemoteDataSource,
                                                 @NonNull PackagesDataSource packagesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PackagesRepository(packagesRemoteDataSource, packagesLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * It is designed to gotten the packages from both database
     * and network. Which are faster then return them.
     * But in our app, we need not to update the data by accessing the network
     * when user enter the app every time because we run a service in backyard.
     * So we just return the data from database.
     * But in future, it may change.
     * @return Packages from {@link io.github.marktony.espresso.data.source.local.PackagesLocalDataSource}.
     */
    @Override
    public Observable<List<Package>> getPackages() {
        if (cachedPackages != null) {
            return Observable.fromCallable(new Callable<List<Package>>() {
                @Override
                public List<Package> call() throws Exception {
                    List<Package> arrayList = new ArrayList<Package>(cachedPackages.values());
                    // Sort by the timestamp to make the list shown in a descend way
                    Collections.sort(arrayList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            if (o1.getTimestamp() > o2.getTimestamp()) {
                                return -1;
                            } else if (o1.getTimestamp() < o2.getTimestamp()) {
                                return 1;
                            }
                            return 0;
                        }
                    });
                    return arrayList;
                }
            });
        } else {

            cachedPackages = new LinkedHashMap<>();

            // Return the cached packages.
            return packagesLocalDataSource
                    .getPackages()
                    .publish(new Function<Observable<List<Package>>, ObservableSource<List<Package>>>() {
                        @Override
                        public ObservableSource<List<Package>> apply(Observable<List<Package>> listObservable) throws Exception {
                            listObservable.flatMapIterable(new Function<List<Package>, Iterable<Package>>() {
                                @Override
                                public Iterable<Package> apply(List<Package> packages) throws Exception {
                                    return packages;
                                }
                            }).subscribe(new Consumer<Package>() {
                                @Override
                                public void accept(Package aPackage) throws Exception {
                                    cachedPackages.put(aPackage.getNumber(), aPackage);
                                }
                            });
                            return listObservable;
                        }
                    });
        }

    }

    /**
     * Get a package of specific number from data source.
     * @param packNumber The primary key or the package id. See {@link Package}.
     * @return The package.
     */
    @Override
    public Observable<Package> getPackage(@NonNull final String packNumber) {
        Package cachedPackage = getPackageWithNumber(packNumber);
        if (cachedPackage != null) {
            return Observable.just(cachedPackage);
        }
        return getPackageWithNumberFromLocalRepository(packNumber);
    }

    /**
     * Save the package to data source and cache.
     * It is supposed to save it to database and network too.
     * But we have no cloud(The account system) yet.
     * It may change either.
     * @param pack The package to save. See more @{@link Package}.
     */
    @Override
    public void savePackage(@NonNull Package pack) {
        packagesLocalDataSource.savePackage(pack);
        if (cachedPackages == null) {
            cachedPackages = new LinkedHashMap<>();
        }
        if (!isPackageExist(pack.getNumber())) {
            cachedPackages.put(pack.getNumber(), pack);
        }
    }

    /**
     * Delete a package from data source and cache.
     * @param packageId The primary id or in another words, the package number.
     *                  See more @{@link Package#number}.
     */
    @Override
    public void deletePackage(@NonNull String packageId) {
        packagesLocalDataSource.deletePackage(packageId);
        cachedPackages.remove(packageId);
    }

    /**
     * Refresh the packages.
     * Just call the remote data source and it will make everything done.
     * @return The observable packages.
     */
    @Override
    public Observable<List<Package>> refreshPackages() {
        return packagesRemoteDataSource
                .refreshPackages()
                .publish(new Function<Observable<List<Package>>, ObservableSource<List<Package>>>() {
                            @Override
                            public ObservableSource<List<Package>> apply(Observable<List<Package>> listObservable) throws Exception {
                                listObservable.flatMapIterable(new Function<List<Package>, Iterable<Package>>() {
                                    @Override
                                    public Iterable<Package> apply(List<Package> packages) throws Exception {
                                        return packages;
                                         }
                                })
                                              .subscribe(new Consumer<Package>() {
                                    @Override
                                    public void accept(Package aPackage) throws Exception {
                                        Package p = cachedPackages.get(aPackage.getNumber());
                                        if (p != null) {
                                            p.setData(aPackage.getData());
                                            p.setPushable(true);
                                            p.setReadable(true);
                                        }
                                    }
                                });
                                return listObservable;
                            }
                        });
    }

    /**
     * Refresh one package.
     * Just call the remote data source and it will make everything done.
     * @param packageId The primary key(The package number).
     *                  See more @{@link Package#number}.
     * @return The observable package.
     */
    @Override
    public Observable<Package> refreshPackage(@NonNull final String packageId) {
        return packagesRemoteDataSource.refreshPackage(packageId)
                                       .publish(new Function<Observable<Package>, ObservableSource<Package>>() {
                                           @Override
                                           public ObservableSource<Package> apply(Observable<Package> packageObservable) throws Exception {
                                               packageObservable.subscribe(new Consumer<Package>() {
                                                   @Override
                                                   public void accept(Package aPackage) throws Exception {
                                                       Package pkg = cachedPackages.get(aPackage.getNumber());
                                                       if (pkg != null) {
                                                           pkg.setData(aPackage.getData());
                                                           pkg.setReadable(true);
                                                       }
                                                   }

                                               });
                                               return packageObservable;
                                           }
                                       });
    }

    /**
     * Set the packages read in both data source and cache.
     */
    @Override
    public void setAllPackagesRead() {

        packagesLocalDataSource.setAllPackagesRead();

        if (cachedPackages == null) {
            cachedPackages = new HashMap<>();
        }
        for (Package p : cachedPackages.values()) {
            p.setReadable(false);
            p.setPushable(false);
        }
    }

    /**
     * Set a package with specific number read or unread new
     * in both data source and cache.
     * @param packageId The primary key (The package number).
     *                  See more @{@link Package#number}.
     * @param readable Unread new or read.
     */
    @Override
    public void setPackageReadable(@NonNull String packageId, boolean readable) {
        Package p = cachedPackages.get(packageId);
        p.setReadable(readable);
        p.setPushable(readable);
        packagesLocalDataSource.setPackageReadable(packageId, readable);
    }

    /**
     * Checkout out the existence of a package with specific number.
     * @param packageId The primary key or in another words, the package number.
     *                  See more @{@link Package#number}.
     * @return Whether the package exists.
     */
    @Override
    public boolean isPackageExist(@NonNull String packageId) {
        return getPackageWithNumber(packageId) != null;
    }

    @Override
    public void updatePackageName(@NonNull String packageId, @NonNull String name) {
        if (getPackageWithNumber(packageId) != null) {
            getPackageWithNumber(packageId).setName(name);
        }
        packagesLocalDataSource.updatePackageName(packageId, name);
    }

    @Override
    public Observable<List<Package>> searchPackages(@NonNull String keyWords) {
        // Do nothing but just let local data source handle it.
        return packagesLocalDataSource.searchPackages(keyWords);
    }

    /**
     * Get a package with package number.
     * @param packNumber The package id(number). See more @{@link Package#number}.
     * @return The package with specific number.
     */
    @Nullable
    private Package getPackageWithNumber(@NonNull String packNumber) {
        if (cachedPackages == null || cachedPackages.isEmpty()) {
            return null;
        } else {
            return cachedPackages.get(packNumber);
        }
    }

    /**
     * As the function name says, get a package from local repository.
     * @param packNumber The package number.
     * @return The package.
     */
    @Nullable
    private Observable<Package> getPackageWithNumberFromLocalRepository(@NonNull final String packNumber) {
        return packagesLocalDataSource
                .getPackage(packNumber)
                .publish(new Function<Observable<Package>, ObservableSource<Package>>() {
                    @Override
                    public ObservableSource<Package> apply(Observable<Package> packageObservable) throws Exception {
                        packageObservable.subscribe(new Consumer<Package>() {
                            @Override
                            public void accept(Package aPackage) throws Exception {
                                if (cachedPackages == null) {
                                    cachedPackages = new LinkedHashMap<>();
                                }
                                cachedPackages.put(packNumber, aPackage);
                            }
                        });
                        return packageObservable;
                    }
                });
    }

}
