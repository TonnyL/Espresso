package io.github.marktony.espresso.addpack;

import android.support.annotation.NonNull;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackagePresenter implements AddPackageContract.Presenter{

    private AddPackageContract.View view;

    public AddPackagePresenter(@NonNull AddPackageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

}
