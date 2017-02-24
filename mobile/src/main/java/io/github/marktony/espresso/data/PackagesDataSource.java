package io.github.marktony.espresso.data;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.entity.Package;
import io.reactivex.Observable;

/**
 * Created by lizhaotailang on 2017/2/25.
 */

public interface PackagesDataSource {

    Observable<List<Package>> getPackages();

    Observable<Package> getPackage();

    void savePackage(@NonNull Package pack);

    void deletePackage(@NonNull String packageId);

    void refreshPackages();

}
