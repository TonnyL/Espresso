package io.github.marktony.espresso.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesDataSource;
import io.github.marktony.espresso.retrofit.RetrofitClient;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static io.github.marktony.espresso.realm.RealmHelper.DATABASE_NAME;

/**
 * Created by lizhaotailang on 2017/3/7.
 * Implementation of the data source that adds a latency simulating network.
 */

public class PackagesRemoteDataSource implements PackagesDataSource {

    @Nullable
    private static PackagesRemoteDataSource INSTANCE;

    // Prevent direct instantiation
    private PackagesRemoteDataSource() {

    }

    // Access this instance for outside classes.
    public static PackagesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PackagesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Package>> getPackages() {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
        return null;
    }

    @Override
    public Observable<Package> getPackage(@NonNull String packNumber) {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
        return null;
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

    /**
     * Update and save the packages' status by accessing the Internet.
     * @return The observable packages whose status are the latest.
     */
    @Override
    public Observable<List<Package>> refreshPackages() {
        // It is necessary to build a new realm instance
        // in a different thread.
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

        return Observable.fromIterable(realm.copyFromRealm(realm.where(Package.class).findAll()))
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Package, ObservableSource<Package>>() {
                    @Override
                    public ObservableSource<Package> apply(Package aPackage) throws Exception {
                        // A nested request.
                        return refreshPackage(aPackage.getNumber());
                    }
                })
                .toList()
                .toObservable();
    }

    /**
     * Update and save a package's status by accessing the network.
     * @param packageId The package's id or number. See {@link Package#number}
     * @return The observable package of latest status.
     */
    @Override
    public Observable<Package> refreshPackage(@NonNull String packageId) {
        // It is necessary to build a new realm instance
        // in a different thread.
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());
        // Set a copy rather than use the raw data.
        final Package p = realm.copyFromRealm(realm.where(Package.class)
                .equalTo("number", packageId)
                .findFirst());

        // Access the network.
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getPackageState(p.getCompany(), p.getNumber())
                .filter(new Predicate<Package>() {
                    @Override
                    public boolean test(Package aPackage) throws Exception {
                        return aPackage.getData() != null && aPackage.getData().size() > p.getData().size();
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Package>() {
                    @Override
                    public void accept(Package aPackage) throws Exception {

                        // To avoid the server error or other problems
                        // making the data in database being dirty.
                        if (aPackage != null && aPackage.getData() != null) {
                            // It is necessary to build a new realm instance
                            // in a different thread.
                            Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                                    .deleteRealmIfMigrationNeeded()
                                    .name(DATABASE_NAME)
                                    .build());

                            // Only when the origin data is null or the origin
                            // data's size is less than the latest data's size
                            // set the package unread new(readable = true).
                            if (p.getData() == null || aPackage.getData().size() > p.getData().size()) {
                                p.setReadable(true);
                                p.setPushable(true);
                                p.setState(aPackage.getState());
                            }

                            p.setData(aPackage.getData());
                            // DO NOT forget to begin a transaction.
                            rlm.beginTransaction();
                            rlm.copyToRealmOrUpdate(p);
                            rlm.commitTransaction();

                            rlm.close();
                        }
                    }
                });
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

    @Override
    public void updatePackageName(@NonNull String packageId, @NonNull String name) {
        // Not required because the {@link PackagesRepository} handles the logic
        // of refreshing the packages from all available data source
    }

}