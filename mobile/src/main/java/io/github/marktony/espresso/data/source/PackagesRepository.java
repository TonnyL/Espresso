package io.github.marktony.espresso.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import io.github.marktony.espresso.data.Package;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lizhaotailang on 2017/2/12.
 */

public class PackagesRepository implements PackagesDataSource {

    @Nullable
    private static PackagesRepository INSTANCE = null;

    @NonNull
    private final PackagesDataSource packagesRemoteDataSource;

    @NonNull
    private final PackagesDataSource packagesLocalDataSource;

    Map<String, Package> cachedPackages;

    boolean cacheIsDirty;

    // Prevent direct instantiation
    private PackagesRepository(@NonNull PackagesDataSource packagesRemoteDataSource,
                               @NonNull PackagesDataSource packagesLocalDataSource) {
        this.packagesRemoteDataSource = packagesRemoteDataSource;
        this.packagesLocalDataSource = packagesLocalDataSource;
    }

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

    @Override
    public Observable<List<Package>> getPackages() {
        if (cachedPackages != null && !cacheIsDirty) {
            return Observable.fromCallable(new Callable<List<Package>>() {
                @Override
                public List<Package> call() throws Exception {
                    return new ArrayList<Package>(cachedPackages.values());
                }
            });
        } else if (cachedPackages == null) {
            cachedPackages = new LinkedHashMap<>();
        }

        Observable<List<Package>> remotePackages = getAndSaveRemotePackages();

        if (cacheIsDirty) {
            return remotePackages;
        } else {
            // Query the local storage if available. If not, query the internet.
            Observable<List<Package>> localPackages = getAndCacheLocalPackages();
            return Observable.concat(localPackages, remotePackages)
                    .filter(new Predicate<List<Package>>() {
                        @Override
                        public boolean test(List<Package> packages) throws Exception {
                            return packages.isEmpty();
                        }
                    });
        }

    }

    @Override
    public Observable<Package> getPackage(@NonNull final String packNumber) {
        Package cachedPackage = getPackageWithNumber(packNumber);
        if (cachedPackage != null) {
            return Observable.just(cachedPackage);
        }

        if (cachedPackages == null) {
            cachedPackages = new LinkedHashMap<>();
        }

        Observable<Package> localPackage = getPackageWithNumberFromLocalRepository(packNumber);
        Observable<Package> remotePackage = packagesRemoteDataSource
                .getPackage(packNumber)
                .doOnNext(new Consumer<Package>() {
                    @Override
                    public void accept(Package aPackage) throws Exception {
                        packagesLocalDataSource.savePackage(aPackage);
                        cachedPackages.put(aPackage.getNumber(), aPackage);
                    }
                });

        return Observable.concat(localPackage, remotePackage)
                .first(null)
                .flatMapObservable(new Function<Package, ObservableSource<? extends Package>>() {
                    @Override
                    public ObservableSource<? extends Package> apply(Package aPackage) throws Exception {
                        if (aPackage == null) {
                            throw new NoSuchElementException("No package found with package number" + packNumber);
                        }
                        return null;
                    }
                });
    }

    @Override
    public void savePackage(@NonNull Package pack) {
        packagesLocalDataSource.savePackage(pack);
        packagesRemoteDataSource.savePackage(pack);
        if (cachedPackages == null) {
            cachedPackages = new LinkedHashMap<>();
        }
        cachedPackages.put(pack.getNumber(), pack);
    }

    @Override
    public void deletePackage(@NonNull String packageId) {
        packagesLocalDataSource.deletePackage(packageId);
        packagesRemoteDataSource.deletePackage(packageId);

        cachedPackages.remove(packageId);
    }

    @Override
    public void refreshPackages() {
        cacheIsDirty = true;
    }

    @Nullable
    private Package getPackageWithNumber(@NonNull String packNumber) {
        if (cachedPackages == null || cachedPackages.isEmpty()) {
            return null;
        } else {
            return cachedPackages.get(packNumber);
        }
    }

    @Nullable
    private Observable<Package> getPackageWithNumberFromLocalRepository(@NonNull final String packNumber) {
        return packagesLocalDataSource
                .getPackage(packNumber)
                .doOnNext(new Consumer<Package>() {
                    @Override
                    public void accept(Package aPackage) throws Exception {
                        cachedPackages.put(packNumber, aPackage);
                    }
                });
    }

    public Observable<List<Package>> getAndSaveRemotePackages() {
        /*return packagesRemoteDataSource
                .getPackages()
                .flatMap(new Function<List<Package>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(List<Package> packages) throws Exception {
                        return Observable.fromIterable(packages)
                                .doOnNext(new Consumer<Package>() {
                                    @Override
                                    public void accept(Package aPackage) throws Exception {
                                        // packagesLocalDataSource.savePackage(aPackage);
                                        // cachedPackages.put(aPackage.getNumber(), aPackage);
                                    }
                                }).toList();
                    }
                });*/

        return null;
    }

    public Observable<List<Package>> getAndCacheLocalPackages() {
        /*return packagesLocalDataSource.getPackages()
                .flatMap(new Function<List<Package>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(List<Package> packages) throws Exception {
                        return Observable.just(packages)
                                .doOnNext(new Consumer<List<Package>>() {
                                    @Override
                                    public void accept(List<Package> packages) throws Exception {

                                    }
                                })
                    }
                });*/

        return null;
    }
}
