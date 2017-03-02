package io.github.marktony.espresso.packages;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackagesPresenter implements PackagesContract.Presenter {

    @NonNull
    private final PackagesContract.View view;

    @NonNull
    private final PackagesRepository packagesRepository;

    @NonNull
    private final CompositeDisposable compositeDisposable;

    @NonNull
    private PackageFilterType currentFiltering = PackageFilterType.ALL_PACKAGES;

    public PackagesPresenter(@NonNull PackagesContract.View view,
                             @NonNull PackagesRepository packagesRepository) {
        this.view = view;
        this.packagesRepository = packagesRepository;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadPackages(false);
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void loadPackages(boolean forceUpdate) {
        Disposable disposable = packagesRepository
                .getPackages()
                .flatMap(new Function<List<Package>, ObservableSource<Package>>() {
                    @Override
                    public ObservableSource<Package> apply(List<Package> list) throws Exception {
                        return Observable.fromIterable(list);
                    }
                })
                .filter(new Predicate<Package>() {
                    @Override
                    public boolean test(Package aPackage) throws Exception {
                        int state = Integer.parseInt(aPackage.getState());
                        switch (currentFiltering) {
                            case ON_THE_WAY_PACKAGES:
                                return state != Package.STATUS_DELIVERED;
                            case DELIVERED_PACKAGES:
                                return state == Package.STATUS_DELIVERED;
                            case ALL_PACKAGES:
                                return true;
                            default:
                                return true;
                        }
                    }
                })
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Package>>() {
                    @Override
                    public void onNext(List<Package> value) {
                        view.showPackages(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showEmptyView(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void openPackageDetails(@NonNull Package pack) {

    }

    @Override
    public void markAllPacksRead() {

    }

    /**
     * Sets the current package filtering type.
     *
     * @param requestType Can be {@Link PackageFilterType#ALL_PACKAGES},
     *                    {@Link PackageFilterType#ON_THE_WAY_PACKAGES}, or
     *                    {@Link PackageFilterType#DELIVERED_PACKAGES}
     */
    @Override
    public void setFiltering(@NonNull PackageFilterType requestType) {
        currentFiltering = requestType;
    }

    /**
     * Get current package filtering type.
     * @return Current filtering type.
     */
    @Override
    public PackageFilterType getFiltering() {
        return currentFiltering;
    }

}
