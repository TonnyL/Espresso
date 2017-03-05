package io.github.marktony.espresso.packagedetails;

import android.support.annotation.NonNull;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
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

    private void openDetail() {
        compositeDisposable.add(packagesRepository
                .getPackage(packageId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Package>() {
                    @Override
                    public void accept(Package aPackage) throws Exception {
                        if (aPackage.getData() == null || aPackage.getData().size() < 1) {
                            view.setPackageNumber(packageId);
                        } else {
                            view.setPackageNumber(aPackage.getNumber());
                        }
                        view.showPackageStatus(aPackage.getData());
                        view.setPackageName(aPackage.getName());

                        int state = Integer.parseInt(aPackage.getState());
                        if (state == Package.STATUS_FAILED) {
                            view.setToolbarBackground(R.drawable.banner_background_error);
                        } else if (state == Package.STATUS_DELIVERED) {
                            view.setToolbarBackground(R.drawable.banner_background_delivered);
                        } else {
                            view.setToolbarBackground(R.drawable.banner_background_on_the_way);
                        }

                        view.setPackageReadUnread(true);
                        if (aPackage.isUnread()) {
                            packagesRepository.setPackageReadUnread(aPackage.getNumber());
                        }
                    }
                }));

    }

    @Override
    public void setPackageReadUnread(@NonNull String packageId) {
        packagesRepository.setPackageReadUnread(packageId);
    }
}
