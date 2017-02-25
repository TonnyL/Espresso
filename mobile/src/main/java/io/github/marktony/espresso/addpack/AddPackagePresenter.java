package io.github.marktony.espresso.addpack;

import android.support.annotation.NonNull;

import io.github.marktony.espresso.data.CompanyAuto;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.retrofit.RetrofitClient;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackagePresenter implements AddPackageContract.Presenter{

    private AddPackageContract.View view;
    private CompositeDisposable compositeDisposable;

    public AddPackagePresenter(@NonNull AddPackageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
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
    public void addNumber(String number, String name) {

        compositeDisposable.clear();

        checkNumber(number);

    }

    private void checkNumber(final String number) {

        view.setProgressIndicator(true);

        RetrofitClient.getInstance().create(RetrofitService.class)
                .query(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompanyAuto>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CompanyAuto value) {
                        if (value.getAuto().size() > 0) {
                            checkPackageLatestStatus(value.getAuto().get(0).getCompanyCode(), number);
                        } else {
                            view.showNumberError();
                            view.setProgressIndicator(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNumberError();
                        view.setProgressIndicator(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void checkPackageLatestStatus(String type, String number) {

        RetrofitClient.getInstance().create(RetrofitService.class)
                .getPackageState(type, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Package>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Package value) {

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
    }

}
