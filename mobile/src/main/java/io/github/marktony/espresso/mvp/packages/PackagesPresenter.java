package io.github.marktony.espresso.mvp.packages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    @Nullable
    private Package mayRemovePackage;

    public PackagesPresenter(@NonNull PackagesContract.View view,
                             @NonNull PackagesRepository packagesRepository) {
        this.view = view;
        this.packagesRepository = packagesRepository;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadPackages();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    /**
     * Get all the packages from repository and show on view.
     */
    @Override
    public void loadPackages() {
        compositeDisposable.clear();
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
                        view.setLoadingIndicator(false);
                    }

                    @Override
                    public void onComplete() {
                        view.setLoadingIndicator(false);
                    }
                });

        compositeDisposable.add(disposable);
    }

    /**
     * Force update the packages data by accessing network.
     */
    @Override
    public void refreshPackages() {
        Disposable disposable = packagesRepository
                .refreshPackages()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Package>>() {
                    @Override
                    public void onNext(List<Package> value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        loadPackages();
                    }
                });
        compositeDisposable.add(disposable);
    }

    /**
     * Set all the packages unreadable which means data is NOT brand new.
     */
    @Override
    public void markAllPacksRead() {
        packagesRepository.setAllPackagesRead();
        loadPackages();
    }

    /**
     * Sets the current package filtering type.
     *
     * @param requestType Can be {@link PackageFilterType#ALL_PACKAGES},
     *                    {@link PackageFilterType#ON_THE_WAY_PACKAGES}, or
     *                    {@link PackageFilterType#DELIVERED_PACKAGES}
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

    /**
     * Set a specific package's read-status, readable or unreadable,
     * which means the data is or not brand new.
     * @param packageId The package number.
     * @param readable Readable or unreadable(New or dated).
     */
    @Override
    public void setPackageReadable(@NonNull final String packageId, boolean readable) {
        packagesRepository.setPackageReadable(packageId, readable);
        loadPackages();
    }

    /**
     * Delete a package from the repository.
     * But this action could revoked.
     * See {@link PackagesPresenter#recoverPackage()} for more
     * recovering details
     * @param position The position of the package which may
     *                 be delete completely.
     */
    @Override
    public void deletePackage(final int position) {
        if (position < 0) {
            return;
        }
        Disposable disposable = packagesRepository
                .getPackages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Package>>() {
                    @Override
                    public void onNext(List<Package> value) {
                        mayRemovePackage = value.get(position);
                        packagesRepository.deletePackage(mayRemovePackage.getNumber());
                        value.remove(position);
                        view.showPackages(value);
                        view.showPackageRemovedMsg(mayRemovePackage.getName());
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

    /**
     * Share a package's data.
     * Just get the wanted package and pass it to view.
     * @param packageId The package number.
     */
    @Override
    public void setShareData(@NonNull String packageId) {
        Disposable disposable = packagesRepository
                .getPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package aPackage) {
                        view.shareTo(aPackage);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
    }

    /**
     * Revoke package which may be removed from repository.
     */
    @Override
    public void recoverPackage() {
        if (mayRemovePackage != null) {
            packagesRepository.savePackage(mayRemovePackage);
        }
        loadPackages();
    }

}
