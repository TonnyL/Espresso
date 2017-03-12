package io.github.marktony.espresso.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.data.Package;
import io.reactivex.Observable;

/**
 * Created by lizhaotailang on 2017/2/25.
 * Main entry point for accessing packages data.
 * <p>
 */

public interface PackagesDataSource {

    Observable<List<Package>> getPackages();

    Observable<Package> getPackage(@NonNull final String packNumber);

    void savePackage(@NonNull Package pack);

    void deletePackage(@NonNull String packageId);

    Observable<List<Package>> refreshPackages();

    Observable<Package> refreshPackage(@NonNull String packageId);

    void setAllPackagesRead();

    void setPackageReadable(@NonNull String packageId, boolean readable);

    boolean isPackageExist(@NonNull String packageId);

}
