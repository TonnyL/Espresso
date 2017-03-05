package io.github.marktony.espresso.data.source;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.github.marktony.espresso.app.App;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.mvp.packages.PackagesFragment;
import io.github.marktony.espresso.retrofit.RetrofitClient;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhaotailang on 2017/2/12.
 */

public class PackagesRepository implements PackagesDataSource {

    @Nullable
    private static PackagesRepository INSTANCE = null;

    @NonNull
    private final PackagesDataSource packagesLocalDataSource;

    private Map<String, Package> cachedPackages;

    private CompositeDisposable compositeDisposable;

    // Prevent direct instantiation
    private PackagesRepository(@NonNull PackagesDataSource packagesLocalDataSource) {
        this.packagesLocalDataSource = packagesLocalDataSource;
        compositeDisposable = new CompositeDisposable();
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

    /**
     * Update the local data by accessing to network.
     * Get all the packages from cache or db(local repository)
     * and then update one by one.
     */
    @Override
    public void refreshPackages() {
        Disposable disposable = getPackages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Package>>() {
                    @Override
                    public void onNext(List<Package> value) {
                        for (Package p : value) {
                            updatePackage(p);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void refreshPackage(@NonNull String packageId) {
        Disposable disposable  = getPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {
                        updatePackage(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void cancelAllRequests() {
        compositeDisposable.clear();
    }

    @Override
    public void setPackageReadUnread(@NonNull String packageId) {
        Package p = cachedPackages.get(packageId);
        p.setUnread(!p.isUnread());
        packagesLocalDataSource.setPackageReadUnread(packageId);
    }

    @Override
    public boolean isPackageExist(@NonNull String packageId) {
        return getPackageWithNumber(packageId) != null;
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

    /**
     * Update the package's data through the network.
     * When all the packages are updated, send a local broadcast
     * to notify that data has been upgraded
     * successfully(send in onComplete()) or not(send in onError()).
     * @param p The package to update.
     */
    private void updatePackage(final Package p) {
        Disposable d = RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getPackageState(p.getCompany(), p.getNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {
                        if (p.getData().size() > value.getData().size()) {
                            value.setUnread(true);
                        }
                        p.setData(value.getData());
                        packagesLocalDataSource.savePackage(p);
                        cachedPackages.remove(p.getNumber());
                        cachedPackages.put(p.getNumber(), p);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Intent intent = new Intent(PackagesFragment.LocalReceiver.PACKAGES_RECEIVER_ACTION);
                        intent.putExtra("result", false);
                        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
                    }

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(PackagesFragment.LocalReceiver.PACKAGES_RECEIVER_ACTION);
                        intent.putExtra("result", true);
                        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
                    }
                });
        compositeDisposable.add(d);
    }

}
