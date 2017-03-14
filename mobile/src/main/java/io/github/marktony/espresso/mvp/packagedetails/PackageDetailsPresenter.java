package io.github.marktony.espresso.mvp.packagedetails;

import android.support.annotation.NonNull;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackageDetailsPresenter implements PackageDetailsContract.Presenter {

    @NonNull
    private PackageDetailsContract.View view;

    @NonNull
    private PackagesRepository packagesRepository;

    @NonNull
    private CompositeDisposable compositeDisposable;

    private String packageName;

    @NonNull
    private String packageId;

    public PackageDetailsPresenter(@NonNull String packageId,
                                   @NonNull PackagesRepository packagesRepository,
                                   @NonNull PackageDetailsContract.View packageDetailView) {
        this.packageId = packageId;
        this.view = packageDetailView;
        this.packagesRepository = packagesRepository;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        openDetail();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    /**
     * Load data from repository.
     */
    private void openDetail() {
        Disposable disposable = packagesRepository
                .getPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {

                        packageName = value.getName();

                        view.showPackageDetails(value);

                        int state = Integer.parseInt(value.getState());
                        if (state == Package.STATUS_FAILED) {
                            view.setToolbarBackground(R.drawable.banner_background_error);
                        } else if (state == Package.STATUS_DELIVERED) {
                            view.setToolbarBackground(R.drawable.banner_background_delivered);
                        } else {
                            view.setToolbarBackground(R.drawable.banner_background_on_the_way);
                        }

                        packagesRepository.setPackageReadable(packageId, false);

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
     * Set the current package is unread new.
     * Once the user enter the package details page,
     * the package will be marked as read(NOT unread new).
     * So here is only one option to set the package unread.
     */
    @Override
    public void setPackageUnread() {
        packagesRepository.setPackageReadable(packageId, true);
        view.exit();
    }

    /**
     * Refresh the package by access the network.
     * No matter the refresh is successful or failed,
     * Do not forget to stop the indicator.
     */
    @Override
    public void refreshPackage() {
        Disposable disposable = packagesRepository
                .refreshPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {
                        view.showPackageDetails(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setLoadingIndicator(false);
                        view.showNetworkError();
                    }

                    @Override
                    public void onComplete() {
                        view.setLoadingIndicator(false);
                    }
                });
        compositeDisposable.add(disposable);

    }

    /**
     * Delete the package from repository(both in cache and database).
     */
    @Override
    public void deletePackage() {
        packagesRepository.deletePackage(packageId);
        view.exit();
    }

    /**
     * Copy the current package number to clipboard.
     */
    @Override
    public void copyPackageNumber() {
        view.copyPackageNumber(packageId);
    }

    /**
     * Share the package data.
     */
    @Override
    public void shareTo() {
        Disposable disposable = packagesRepository
                .getPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {
                        view.shareTo(value);
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
     * Get the current package name.
     * @return Package name. See at {@link Package#name}
     */
    @Override
    public String getPackageName() {
        return packageName;
    }

    /**
     * Set the current package a new name(both in cache and database).
     * @param newName The new name of package.
     *                See at {@link Package#name}
     */
    @Override
    public void updatePackageName(String newName) {
        packagesRepository.updatePackageName(packageId, newName);
        openDetail();
    }

}
