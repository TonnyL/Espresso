package io.github.marktony.espresso.packagedetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    @Nullable
    private String packageId;

    public PackageDetailsPresenter(@Nullable String packageId,
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
        if (packageId == null || packageId.isEmpty()) {
            view.showDetailError();
            return;
        }
        compositeDisposable.add(packagesRepository
                .getPackage(packageId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Package>() {
                    @Override
                    public void accept(Package aPackage) throws Exception {
                        view.showPackageStatus(aPackage.getData());
                    }
                }));

    }

}
