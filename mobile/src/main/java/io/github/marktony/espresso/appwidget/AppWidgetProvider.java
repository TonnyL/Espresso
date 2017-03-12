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

    private static final String EXTRA_PERFORM_SYNC = "io.github.marktony.espresso.appwidget.extra.PERFORM_SYNC";

    public static Intent getRefreshBroadcastIntent(Context context, boolean performSync) {
        return new Intent(REFRESH_ACTION)
                .setComponent(new ComponentName(context, AppWidgetProvider.class))
                .putExtra(EXTRA_PERFORM_SYNC, performSync);
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

    public static void updateManually(Context context) {
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, AppWidgetProvider.class));
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(ids, R.id.listViewWidget);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        if (REFRESH_ACTION.equals(action)) {
            boolean shouldSync = intent.getBooleanExtra(EXTRA_PERFORM_SYNC, false);
            if (shouldSync) {
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                ComponentName name = new ComponentName(context, AppWidgetProvider.class);
                manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(name), R.id.listViewWidget);
            }
        }
    }

}
