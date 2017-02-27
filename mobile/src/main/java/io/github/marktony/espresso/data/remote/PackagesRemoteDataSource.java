package io.github.marktony.espresso.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesDataSource;
import io.reactivex.Observable;

/**
 * Created by lizhaotailang on 2017/2/26.
 */

public class PackagesRemoteDataSource implements PackagesDataSource {

    @Nullable
    private static PackagesRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private Map<String, Package> PACKAGES_SERVICE_DATA;

    // Prevent direct instantiation
    private PackagesRemoteDataSource() {
        PACKAGES_SERVICE_DATA = new HashMap<>();
    }

    public static PackagesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PackagesRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Package>> getPackages() {
        return Observable
                .fromIterable(PACKAGES_SERVICE_DATA.values())
                .delay(SERVICE_LATENCY_IN_MILLIS, TimeUnit.MILLISECONDS)
                .toList()
                .toObservable();
    }

    @Override
    public Observable<Package> getPackage(@NonNull String packNumber) {
        Package pack = PACKAGES_SERVICE_DATA.get(packNumber);
        if (pack != null) {
            return Observable
                    .just(pack)
                    .delay(SERVICE_LATENCY_IN_MILLIS, TimeUnit.MILLISECONDS);
        } else {
            return Observable.empty();
        }
    }

    @Override
    public void savePackage(@NonNull Package pack) {
        PACKAGES_SERVICE_DATA.put(pack.getNumber(), pack);
    }

    @Override
    public void deletePackage(@NonNull String packageId) {
        PACKAGES_SERVICE_DATA.remove(packageId);
    }

    @Override
    public void refreshPackages() {

    }
}
