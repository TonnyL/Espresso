package io.github.marktony.espresso.mvp;

import android.view.View;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface BaseView<T> {

    void initViews(View view);

    void setPresenter(T presenter);

}
