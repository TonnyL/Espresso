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
        packagesRepository.cancelAllRequests();
    }

    private void openDetail() {
        Disposable disposable = packagesRepository
                .getPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {

                        view.setPackageNumber(packageId);
                        view.setPackageName(value.getName());

                        // Convert the RealmList to a normal List.
                        // Pass a RealmList as parameter directly where List is required
                        // is NOT a good idea.
                        // The code below will make some terrible bugs.
                        // view.showPackageStatus(list);
                        // One the them is that the first load works perfectly.
                        // But when the screen call onPause and back to user again,
                        // the list (RecyclerView) is lost and all the data is removed from DB.
                        List<PackageStatus> list = new ArrayList<>();
                        for (PackageStatus status : value.getData()) {
                            list.add(status);
                        }

                        view.showPackageStatus(list);

                        int state = Integer.parseInt(value.getState());
                        if (state == Package.STATUS_FAILED) {
                            view.setToolbarBackground(R.drawable.banner_background_error);
                        } else if (state == Package.STATUS_DELIVERED) {
                            view.setToolbarBackground(R.drawable.banner_background_delivered);
                        } else {
                            view.setToolbarBackground(R.drawable.banner_background_on_the_way);
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
    public void setPackageReadUnread() {
        packagesRepository.setPackageReadUnread(packageId);
    }

    @Override
    public void refreshPackage() {

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
