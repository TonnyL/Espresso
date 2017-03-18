package io.github.marktony.espresso.mvp.addpackage;

import android.support.annotation.NonNull;

import java.util.Random;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.CompanyRecognition;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesDataSource;
import io.github.marktony.espresso.retrofit.RetrofitClient;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackagePresenter implements AddPackageContract.Presenter{

    @NonNull
    private final AddPackageContract.View view;

    @NonNull
    private final PackagesDataSource packagesDataSource;

    @NonNull
    private CompositeDisposable compositeDisposable;

    @NonNull
    private int[] avatarColors = {R.color.cyan_500, R.color.amber_500, R.color.gray_500,
            R.color.indigo_500, R.color.light_blue_500, R.color.lime_500,
            R.color.teal_500};

    public AddPackagePresenter(@NonNull PackagesDataSource dataSource, @NonNull AddPackageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.packagesDataSource = dataSource;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void savePackage(String number, String name) {
        compositeDisposable.clear();
        checkNumber(number, name);
    }

    private void checkNumber(final String number, final String name) {

        if (packagesDataSource.isPackageExist(number)) {
            view.showNumberExistError();
            return;
        }

        view.setProgressIndicator(true);

        Disposable disposable = RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .query(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CompanyRecognition>() {
                    @Override
                    public void onNext(CompanyRecognition value) {

                        if (value.getAuto().size() > 0 && value.getAuto().get(0).getCompanyCode() != null) {
                            checkPackageLatestStatus(value.getAuto().get(0).getCompanyCode(), number, name);
                        } else {
                            view.showNumberError();
                            view.setProgressIndicator(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNumberError();
                        view.setProgressIndicator(false);
                        view.showNetworkError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
    }

    private void checkPackageLatestStatus(final String type, final String number, final String name) {

        Disposable disposable = RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getPackageState(type, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {
                        if (value != null) {

                            if (value.getData() != null && value.getData().size() > 0) {
                                value.setReadable(true);
                            }
                            // Set the company
                            value.setCompany(type);
                            // Set the name of package
                            value.setName(name);
                            // Some package numbers, which are unable to get the latest status
                            // has no number value after converted to a gson  value
                            // We need to set the number manually.
                            value.setNumber(number);
                            // Set a random color value for the package
                            value.setColorAvatar(avatarColors[new Random().nextInt(avatarColors.length)]);
                            // Set the add-time(timestamp) of package
                            value.setTimestamp(System.currentTimeMillis());

                            packagesDataSource.savePackage(value);
                        }
                        view.showPackagesList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNumberError();
                        view.setProgressIndicator(false);
                    }

                    @Override
                    public void onComplete() {
                        view.setProgressIndicator(false);
                    }
                });

        compositeDisposable.add(disposable);
    }

}
