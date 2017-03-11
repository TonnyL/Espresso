package io.github.marktony.espresso.appwidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by lizhaotailang on 2017/3/8.
 */

public class AppWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListFactory(this.getApplicationContext());
    }

}
