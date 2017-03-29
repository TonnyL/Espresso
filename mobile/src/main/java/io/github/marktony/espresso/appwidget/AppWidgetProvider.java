/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.mvp.packagedetails.PackageDetailsActivity;

/**
 * Created by lizhaotailang on 2017/3/8.
 */

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    private static final String REFRESH_ACTION = "io.github.marktony.espresso.appwidget.action.REFRESH";

    public static Intent getRefreshBroadcastIntent(Context context) {
        return new Intent(REFRESH_ACTION)
                .setComponent(new ComponentName(context, AppWidgetProvider.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int id) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.launcher_list_widget);

        Intent intent = new Intent(context, AppWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter(R.id.listViewWidget, intent);
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.emptyView);

        Intent tempIntent = new Intent(context, PackageDetailsActivity.class);
        tempIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        remoteViews.setPendingIntentTemplate(R.id.listViewWidget,
                PendingIntent.getActivity(context, 0, tempIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (REFRESH_ACTION.equals(action)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName name = new ComponentName(context, AppWidgetProvider.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(name), R.id.listViewWidget);
        }
    }

}
