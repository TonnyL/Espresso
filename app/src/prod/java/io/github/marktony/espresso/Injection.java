package io.github.marktony.espresso;

import android.content.Context;
import android.support.annotation.NonNull;

import io.github.marktony.espresso.data.source.PackagesRepository;
import io.github.marktony.espresso.data.source.local.PackagesLocalDataSource;
import io.github.marktony.espresso.data.source.remote.PackagesRemoteDataSource;

/**
 * Created by lizhaotailang on 2017/5/14.
 * Enables injection of production implementations for
 * {@link PackagesRepository} at compile time.
 */

public class Injection {

    public static PackagesRepository providePackagesRepository(@NonNull Context context) {
        return PackagesRepository.getInstance(
                PackagesRemoteDataSource.getInstance(),
                PackagesLocalDataSource.getInstance()
        );
    }

}
