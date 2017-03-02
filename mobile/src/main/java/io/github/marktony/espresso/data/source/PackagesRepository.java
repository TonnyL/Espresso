package io.github.marktony.espresso.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 */

public class PackagesRepository implements PackagesDataSource {

    @Nullable
    private static PackagesRepository INSTANCE = null;

    @NonNull
    private final PackagesDataSource packagesLocalDataSource;

    private Map<String, Package> cachedPackages;

    // Prevent direct instantiation
    private PackagesRepository(@NonNull PackagesDataSource packagesLocalDataSource) {
        this.packagesLocalDataSource = packagesLocalDataSource;
    }

    public static PackagesRepository getInstance(@NonNull PackagesDataSource packagesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PackagesRepository(packagesLocalDataSource);
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }

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

            return packagesLocalDataSource
                    .getPackages()
                    .flatMap(new Function<List<Package>, ObservableSource<List<Package>>>() {
                        @Override
                        public ObservableSource<List<Package>> apply(List<Package> packages) throws Exception {
                            return Observable
                                    .fromIterable(packages)
                                    .doOnNext(new Consumer<Package>() {
                                        @Override
                                        public void accept(Package aPackage) throws Exception {
                                            cachedPackages.put(aPackage.getNumber(), aPackage);
                                        }
                                    })
                                    .toList()
                                    .toObservable();
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

        return getPackageWithNumberFromLocalRepository(packNumber);
    }

    @Override
    public void savePackage(@NonNull Package pack) {
        packagesLocalDataSource.savePackage(pack);
        if (cachedPackages == null) {
            cachedPackages = new LinkedHashMap<>();
        }
        cachedPackages.put(pack.getNumber(), pack);
    }

    @Override
    public void deletePackage(@NonNull String packageId) {
        packagesLocalDataSource.deletePackage(packageId);
        cachedPackages.remove(packageId);
    }

    @Override
    public void refreshPackages() {

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

}
