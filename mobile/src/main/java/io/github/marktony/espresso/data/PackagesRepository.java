package io.github.marktony.espresso.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

import io.github.marktony.espresso.entity.Package;
import io.reactivex.Observable;

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

    Map<String, Package> cachePackages;

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

    // TODO: 2017/2/25 继续完成

    @Override
    public Observable<List<Package>> getPackages() {
        return null;
    }

    @Override
    public Observable<Package> getPackage() {
        return null;
    }

    @Override
    public void savePackage(@NonNull Package pack) {

    }

    @Override
    public void deletePackage(@NonNull String packageId) {

    }

    @Override
    public void refreshPackages() {

    }

}
