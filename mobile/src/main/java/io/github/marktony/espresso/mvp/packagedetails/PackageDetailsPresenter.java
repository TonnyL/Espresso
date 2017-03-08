package io.github.marktony.espresso.mvp.packagedetails;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.PackageStatus;
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

    @NonNull
    private String packageId;
    private int position;

    public PackageDetailsPresenter(@NonNull String packageId,
                                   @NonNull int position,
                                   @NonNull PackagesRepository packagesRepository,
                                   @NonNull PackageDetailsContract.View packageDetailView) {
        this.packageId = packageId;
        this.position = position;
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

    private void openDetail() {
        Disposable disposable = packagesRepository
                .getPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {

                        view.showPackageStatus(value);

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

    @Override
    public void setPackageUnread() {
        packagesRepository.setPackageReadable(packageId, true);
        view.setPackageUnread(packageId, position);
    }

    @Override
    public void refreshPackage() {
        Disposable disposable = packagesRepository
                .refreshPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {

                        List<PackageStatus> list = new ArrayList<>();
                        for (PackageStatus status : value.getData()) {
                            list.add(status);
                        }

                        view.showPackageStatus(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setLoadingIndicator(false);
                    }

                    @Override
                    public void onComplete() {
                        view.setLoadingIndicator(false);
                    }
                });
        compositeDisposable.add(disposable);

    }

    @Override
    public void deletePackage() {
        view.deletePackage(packageId, position);
    }

    @Override
    public void copyPackageNumber() {
        view.copyPackageNumber(packageId);
    }

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

}
