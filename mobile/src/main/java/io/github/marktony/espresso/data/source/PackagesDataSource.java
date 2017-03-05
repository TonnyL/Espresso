package io.github.marktony.espresso.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.data.Package;
import io.reactivex.Observable;

/**
 * Created by lizhaotailang on 2017/2/25.
 */

public interface PackagesDataSource {

    Observable<List<Package>> getPackages();

    Observable<Package> getPackage(@NonNull final String packNumber);

    void savePackage(@NonNull Package pack);

    void deletePackage(@NonNull String packageId);

    void refreshPackages();

    void refreshPackage(@NonNull String packageId);

    void cancelAllRequests();

    void setPackageReadUnread(@NonNull String packageId);

    boolean isPackageExist(@NonNull String packageId);

}
