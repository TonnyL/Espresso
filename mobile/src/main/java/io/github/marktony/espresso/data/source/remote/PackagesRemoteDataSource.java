package io.github.marktony.espresso.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesDataSource;
import io.github.marktony.espresso.data.source.local.PackagesLocalDataSource;
import io.github.marktony.espresso.retrofit.RetrofitClient;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lizhaotailang on 2017/3/7.
 */

public class PackagesRemoteDataSource implements PackagesDataSource {

    @Nullable
    private static PackagesRemoteDataSource INSTANCE;

    private PackagesLocalDataSource localDataSource;

    // Prevent direct instantiation
    private PackagesRemoteDataSource() {
        localDataSource = PackagesLocalDataSource.getInstance();
    }

    public static PackagesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PackagesRemoteDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Package>> getPackages() {
        return localDataSource.getPackages()
                .flatMap(new Function<List<Package>, ObservableSource<List<Package>>>() {
                    @Override
                    public ObservableSource<List<Package>> apply(List<Package> packages) throws Exception {
                        return Observable.fromIterable(packages)
                                .doOnNext(new Consumer<Package>() {
                                    @Override
                                    public void accept(Package aPackage) throws Exception {
                                        getPackage(aPackage.getNumber());
                                    }
                                })
                                .toList()
                                .toObservable();
                    }
                });
    }

    @Override
    public Observable<Package> getPackage(@NonNull String packNumber) {
        return localDataSource.getPackage(packNumber)
                .flatMap(new Function<Package, ObservableSource<Package>>() {
                    @Override
                    public ObservableSource<Package> apply(final Package pack) throws Exception {
                        return RetrofitClient.getInstance()
                                .create(RetrofitService.class)
                                .getPackageState(pack.getCompany(), pack.getNumber())
                                .doOnNext(new Consumer<Package>() {
                                    @Override
                                    public void accept(Package aPackage) throws Exception {
                                        localDataSource.savePackage(aPackage);
                                    }
                                });
                    }
                });

    }

    @Override
    public void savePackage(@NonNull Package pack) {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
    }

    @Override
    public void deletePackage(@NonNull String packageId) {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
    }

    @Override
    public Observable<List<Package>> refreshPackages() {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source.
        return null;
    }

    @Override
    public Observable<Package> refreshPackage(@NonNull String packageId) {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source.
        return null;
    }

    @Override
    public void setAllPackagesRead() {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
    }

    @Override
    public void setPackageReadable(@NonNull String packageId, boolean readable) {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
    }

    @Override
    public boolean isPackageExist(@NonNull String packageId) {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
        return false;
    }
}
