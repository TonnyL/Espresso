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

    @Override
    public void addNumber(String number) {
        if (number.length() < 5) {
            view.showNumberError();
            return;
        }
        // check the string if only contains numbers and characters
        for (char c : number.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                view.showNumberError();
                return;
            }
        }
        // more code here
    }
}
