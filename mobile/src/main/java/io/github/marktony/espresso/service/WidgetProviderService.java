package io.github.marktony.espresso.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by lizhaotailang on 2017/3/8.
 */

public class WidgetProviderService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }

}
