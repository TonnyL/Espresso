package io.github.marktony.espresso.provider;

import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.mvp.packagedetails.PackageDetailsActivity;
import io.github.marktony.espresso.service.WidgetProviderService;

/**
 * Created by lizhaotailang on 2017/3/8.
 */

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetRecyclerView(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetRecyclerView(Context context, int id) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.launcher_list_widget);

        Intent intent = new Intent(context, WidgetProviderService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter(R.id.recyclerViewWidget, intent);
        remoteViews.setEmptyView(R.id.recyclerViewWidget, R.id.emptyView);

        Intent tempIntent = new Intent(context, PackageDetailsActivity.class);
        tempIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        remoteViews.setPendingIntentTemplate(R.id.recyclerViewWidget,
                PendingIntent.getActivity(context, 0, tempIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        return remoteViews;
    }

    public static void updateManually(Application app) {
        int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app, WidgetProvider.class));
        AppWidgetManager.getInstance(app).notifyAppWidgetViewDataChanged(ids, R.id.recyclerViewWidget);
    }

}
